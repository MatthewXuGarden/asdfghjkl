package com.carel.supervisor.plugin.switchtech;

import java.sql.Timestamp;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.controller.setfield.SwitchCallBack;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.event.VariableHelper;
import com.carel.supervisor.field.Variable;


public class Switch
{
    private static final String AUTO_SWITCH = "autoswitch";

    //switchconfig
    private static final String HOUR = "hour";
    private static final String START_HOUR = "starthour";
    private static final String START_TYPE = "starttype";
    private static final String MANUAL_TYPE = "manualtype";
    private static final String AUTO_RESUME = "autoresume";
    private static final String SKIP_ALARM = "skipalarm";
    private static final String ALARM_WAIT = "alarmwait";
    private static final String DESCRIPTION = "description";

    //switchstatus
    private static final String RUNNING = "running";
    private static final String STATUS_AUTOSWITCH = "autoswitch";
    private static final String STATUS_TYPE = "type";
    private static final String LAST_SWITCH = "lastswitch";
    private static final String NEXT_SWITCH = "nextswitch";

    //configurazione
    private int idswitch = -1;
    private int idsite = -1;
    private String autoresume = "FALSE";
    private String skipalarm = "FALSE";
    private String manual_type = null;
    private String auto_type = null;
    private String autoswitch = "FALSE";
    private int wait_time = -1;
    private int hour = -1;
    private Timestamp starthour = null;
    private String description = null;

    //stato
    private String st_running = "FALSE";
    private String st_autoswitch = "FALSE";
    private String st_type = "FALSE";
    private Timestamp st_lastswitch = null;
    private Timestamp st_nextswitch = null;
    private int[] ids_alarms = null;
    private int[] ids_variables = null;
    private String[] tev_values = null;
    private String[] eev_values = null;
    private int time_alarm_counter = -1;
    private int id_var_startalarm = -1;
    private boolean sw_loaded = false;
    private boolean force_tev = false;
    private boolean start_autoswitch = false;
    private boolean first_set_after_change = true;

    public Switch()
    {
    }

    public void reload(int idsite, int idswitch) throws DataBaseException
    {
        String sql = "select * from switchconfig where idsite = ? and idswitch = ?";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idswitch);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        if (rs.size() > 0)
        {
            Record switchconfig = rs.get(0);
            this.idsite = idsite;
            this.idswitch = idswitch;
            this.autoswitch = switchconfig.get(AUTO_SWITCH).toString().trim();
            this.wait_time = Integer.parseInt(switchconfig.get(ALARM_WAIT)
                                                          .toString());
            this.autoresume = switchconfig.get(AUTO_RESUME).toString().trim();
            this.manual_type = switchconfig.get(MANUAL_TYPE).toString().trim();
            this.auto_type = switchconfig.get(START_TYPE).toString().trim();
            this.skipalarm = switchconfig.get(SKIP_ALARM).toString().trim();
            this.hour = Integer.parseInt(switchconfig.get(HOUR).toString());
            this.starthour = (Timestamp) switchconfig.get(START_HOUR);
            this.description = switchconfig.get(DESCRIPTION).toString().trim();
        }

        sql = "select * from switchstatus where idsite = ? and idswitch = ?";
        rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        if (rs.size() > 0)
        {
            Record switchstatus = rs.get(0);
            this.st_running = switchstatus.get(RUNNING).toString().trim();
            this.st_autoswitch = switchstatus.get(STATUS_AUTOSWITCH).toString()
                                             .trim();
            this.st_type = switchstatus.get(STATUS_TYPE).toString().trim();
            this.st_lastswitch = (Timestamp) switchstatus.get(LAST_SWITCH);
            this.st_nextswitch = (Timestamp) switchstatus.get(NEXT_SWITCH);
        }
    }

    public void initializeSwitchVariable() throws DataBaseException
    {
        //retrieve variabili
        String sql =
            "select cfvariable.idvariable, switchvar.eev, switchvar.tev from cfvariable inner join switchvar on cfvariable.idsite=? and " +
            "switchvar.idsite=? and switchvar.idswitch=? and cfvariable.idvarmdl = switchvar.idvarmdl and cfvariable.idhsvariable is not null and " +
            "cfvariable.iscancelled='FALSE' and switchvar.isalarm=? and cfvariable.iddevice in " +
            "(select iddevice from switchdev where idsite=? and idswitch=?)";
        Object[] param = new Object[6];
        param[0] = new Integer(this.idsite);
        param[1] = new Integer(this.idsite);
        param[2] = new Integer(this.idswitch);
        param[3] = "FALSE";
        param[4] = new Integer(this.idsite);
        param[5] = new Integer(this.idswitch);

        RecordSet rs_variable = DatabaseMgr.getInstance().executeQuery(null,
                sql, param);

        int tmp_id = -1;
        ids_variables = new int[rs_variable.size()];
        eev_values = new String[rs_variable.size()];
        tev_values = new String[rs_variable.size()];

        for (int i = 0; i < rs_variable.size(); i++)
        {
            tmp_id = ((Integer) rs_variable.get(i).get(0)).intValue();
            ids_variables[i] = tmp_id;
            eev_values[i] = rs_variable.get(i).get(1).toString().trim();
            tev_values[i] = rs_variable.get(i).get(2).toString().trim();
        }
    }

    public void initializeSwitchAlarms() throws DataBaseException
    {
        //retrieve id allarmi
        String sql =
            "select idvariable from cfvariable inner join switchvar on cfvariable.idsite=? and " +
            "switchvar.idsite=? and switchvar.idswitch=? and cfvariable.idvarmdl = switchvar.idvarmdl and " +
            "cfvariable.iscancelled='FALSE' and switchvar.isalarm=? and cfvariable.iddevice in " +
            "(select iddevice from switchdev where idsite=? and idswitch=?)";
        Object[] param = new Object[6];
        param[0] = new Integer(this.idsite);
        param[1] = new Integer(this.idsite);
        param[2] = new Integer(this.idswitch);
        param[3] = "TRUE";
        param[4] = new Integer(this.idsite);
        param[5] = new Integer(this.idswitch);

        RecordSet rs_alarm = DatabaseMgr.getInstance().executeQuery(null, sql,
                param);

        ids_alarms = new int[rs_alarm.size()];

        for (int i = 0; i < rs_alarm.size(); i++)
        {
            ids_alarms[i] = ((Integer) rs_alarm.get(i).get(0)).intValue();
        }
    }

    public String getAuto_type()
    {
        return auto_type;
    }

    public void setAuto_type(String auto_tech)
    {
        this.auto_type = auto_tech;
    }

    public String getAutoresume()
    {
        return autoresume;
    }

    public void setAutoresume(String autoresume)
    {
        this.autoresume = autoresume;
    }

    public String getAutoswitch()
    {
        return autoswitch;
    }

    public void setAutoswitch(String autoswitch)
    {
        this.autoswitch = autoswitch;
    }

    public boolean isStart_autoswitch()
    {
        return start_autoswitch;
    }

    public void setStart_autoswitch(boolean start_autoswitch)
    {
        this.start_autoswitch = start_autoswitch;
    }

    public int getHour()
    {
        return hour;
    }

    public void setHour(int hour)
    {
        this.hour = hour;
    }

    public int getIdswitch()
    {
        return idswitch;
    }

    public void setIdswitch(int idswitch)
    {
        this.idswitch = idswitch;
    }

    public Timestamp getSt_lastswitch()
    {
        return st_lastswitch;
    }

    public String getManual_type()
    {
        return manual_type;
    }

    public void setManual_type(String manual_tech)
    {
        this.manual_type = manual_tech;
    }

    public Timestamp getSt_nextswitch()
    {
        return st_nextswitch;
    }

    public String getSt_running()
    {
        return st_running;
    }

    public void setSt_running(String running)
    {
        this.st_running = running;
    }

    public String getSkipalarm()
    {
        return skipalarm;
    }

    public void setSkipalarm(String skipalarm)
    {
        this.skipalarm = skipalarm;
    }

    public String getSt_autoswitch()
    {
        return st_autoswitch;
    }

    public void setSt_autoswitch(String status_autoswitch)
    {
        this.st_autoswitch = status_autoswitch;
    }

    public String getSt_type()
    {
        return st_type;
    }

    public void setSt_type(String status_type)
    {
        this.st_type = status_type;
    }

    public int getWait_time()
    {
        return wait_time;
    }

    public int getIdsite()
    {
        return idsite;
    }

    public String getDescription()
    {
        return description;
    }

    public Timestamp getStarthour()
    {
        return starthour;
    }

    public void setStarthour(Timestamp starthour)
    {
        this.starthour = starthour;
    }

    public int getId_var_startalarm()
    {
        return id_var_startalarm;
    }

    public void setId_var_startalarm(int id_var_startalarm)
    {
        this.id_var_startalarm = id_var_startalarm;
    }

    public void reset_alarm_counter()
    {
        this.time_alarm_counter = -1;
        this.id_var_startalarm = -1;
    }

    public void increaseAlarmTimeCounter()
    {
        this.time_alarm_counter += (SwitchMgr.getInstance().getSleep() / 1000);
    }

    public boolean isForce_tev()
    {
        return force_tev;
    }

    public void setForce_tev(boolean force_tev)
    {
        this.force_tev = force_tev;
    }

    public boolean isFirst_set_after_change()
    {
        return first_set_after_change;
    }

    public void setFirst_set_after_change(boolean set_param)
    {
        this.first_set_after_change = set_param;
    }

    public int getTime_alarm_counter()
    {
        return time_alarm_counter;
    }

    public void updateSwitchStatusRun(String run) throws DataBaseException
    {
        this.st_running = run;

        String update = "update switchstatus set running=? where idsite=? and idswitch=?";
        Object[] param = new Object[3];
        param[0] = run;
        param[1] = new Integer(this.idsite);
        param[2] = new Integer(this.idswitch);
        DatabaseMgr.getInstance().executeStatement(null, update, param);
    }

    public boolean isOnAlarmStatus() throws Exception
    {
        if (skipalarm.equalsIgnoreCase("FALSE"))
        {
            boolean onalarm = false;
            int idalarm = -1;

            for (int i = 0; i < ids_alarms.length; i++)
            {
                idalarm = ids_alarms[i];

                if (ControllerMgr.getInstance().getFromField(idalarm)
                                     .getFormattedValue().equalsIgnoreCase("1"))
                {
                    onalarm = true;

                    if (this.id_var_startalarm == -1)
                    {
                        this.id_var_startalarm = idalarm;
                    }

                    break;
                }
            }

            if (onalarm)
            {
                increaseAlarmTimeCounter();

                if (time_alarm_counter >= this.getWait_time())
                {
                    if ((time_alarm_counter - this.getWait_time()) < (SwitchMgr.getInstance()
                                                                                   .getSleep() / 1000))
                    {
                        //LOG variabile di allarme che ha generato lo stato di allarme
                        String desc = VariableHelper.getDeviceAndVariableDesc("EN_en",
                                idsite, id_var_startalarm);

                        EventMgr.getInstance().warning(new Integer(this.idsite),
                            "System", "switch", "SW11",
                            new Object[] { this.description, desc });
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                if (id_var_startalarm != -1)
                {
                    //LOG allarme rientrato
                    String desc = VariableHelper.getDeviceAndVariableDesc("EN_en",
                            idsite, id_var_startalarm);
                    EventMgr.getInstance().warning(new Integer(this.idsite),
                        "System", "switch", "SW12",
                        new Object[] { desc, this.description });
                }

                reset_alarm_counter();

                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean isTimeToChangeTech(Timestamp now) throws DataBaseException
    {
        if (now.after(this.st_nextswitch))
        {
            //set lastswitch e nextswitch
            this.st_lastswitch = this.st_nextswitch;
            this.st_nextswitch = new Timestamp((this.st_lastswitch.getTime() +
                    (this.hour * 1000)));

            String sql = "update switchstatus set lastswitch=?,nextswitch=? where idsite=? and idswitch=?";
            Object[] param = new Object[4];
            param[0] = this.st_lastswitch;
            param[1] = this.st_nextswitch;
            param[2] = new Integer(this.idsite);
            param[3] = new Integer(this.idswitch);
            DatabaseMgr.getInstance().executeStatement(null, sql, param);

            return true;
        }

        else
        {
            return false;
        }
    }

    public boolean isSw_loaded()
    {
        return sw_loaded;
    }

    public void setSw_loaded(boolean var_loaded)
    {
        this.sw_loaded = var_loaded;
    }

    public void changeTech() throws DataBaseException
    {
        String new_type = "";

        if (this.st_type.equalsIgnoreCase("eev"))
        {
            new_type = "tev";
        }
        else
        {
            new_type = "eev";
        }

        this.st_type = new_type;

        String sql = "update switchstatus set type=? where idsite=? and idswitch=?";
        Object[] param = new Object[3];
        param[0] = new_type;
        param[1] = new Integer(this.idsite);
        param[2] = new Integer(this.idswitch);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        setFirst_set_after_change(true); //setta booleana che mi dice che ho appena switchato in modo da logare il prossimo set sul campo
    }

    public void changeTech(String new_type) throws DataBaseException
    {
        this.st_type = new_type;

        String sql = "update switchstatus set type=? where idsite=? and idswitch=?";
        Object[] param = new Object[3];
        param[0] = new_type;
        param[1] = new Integer(this.idsite);
        param[2] = new Integer(this.idswitch);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        setFirst_set_after_change(true); //setta booleana che mi dice che ho appena switchato in modo da logare il prossimo set sul campo
    }

    public void setSwitchValues(String technology, boolean log)
    {
    	
        if (ids_variables.length != 0)
        {
        	SetContext setContext = new SetContext();
        	String lang = "EN_en";
    		try
    		{
    			lang = LangUsedBeanList.getDefaultLanguage(1);
    		}
    		catch (Exception e)
    		{
    		}
    		setContext.setLanguagecode(lang);
        	setContext.setCallback(new SwitchCallBack());
    		setContext.setUser("System");
    		setContext.setLoggable(log);
    		Variable var = null;
        	for(int i = 0; i < ids_variables.length; i++)
        	{
        		try
        		{
        			var = ControllerMgr.getInstance().getFromField(ids_variables[i]);
        		}
        		catch(Exception e)
        		{
        			LoggerMgr.getLogger(this.getClass()).error(e);
        		}
        		
        		float newValue = 0;
        		if (technology.equalsIgnoreCase("eev"))
                {
        			newValue = Float.parseFloat(eev_values[i]);
                }
                else
                {
                	newValue = Float.parseFloat(tev_values[i]);
                }
        		
        		if (var.getCurrentValue() != newValue && !new Float(var.getCurrentValue()).equals(Float.NaN))
    			{
    				setContext.addVariable(var, newValue);
    				setContext.setLoggable(true);		//loggo ogni volta che il valore � diverso 
    			}
        		else if (log) //La prima volta faccio lo stesso il set e loggo i valori sulla tabella hsparams
        		{
        			setContext.addVariable(var, newValue);
        		}
        	}
            
            SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
        }

        setFirst_set_after_change(false);
    }

    public void startAutoswitch() throws DataBaseException
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        setFirst_set_after_change(true);

        if (this.getSt_lastswitch() == null) //AUTOSWITCH MAI PARTITO PRIMA
        {
            String tech_for_clock = getNowTecnologyByClock(now);

            this.setSt_type(tech_for_clock);

            Timestamp next = new Timestamp(this.st_nextswitch.getTime() +
                    (this.getHour() * 1000));

            this.st_lastswitch = this.st_nextswitch;
            this.st_nextswitch = next;

            String sql = "update switchstatus set lastswitch=?,nextswitch=?,type=?,lastupdate=? where idsite=? and idswitch=?";
            Object[] param = new Object[6];
            param[0] = now;
            param[1] = next;
            param[2] = this.getSt_type();
            param[3] = new Timestamp(System.currentTimeMillis());
            param[4] = new Integer(this.idsite);
            param[5] = new Integer(this.idswitch);
            DatabaseMgr.getInstance().executeStatement(null, sql, param);

            //LOG cambio tecnologia programmato
            String last_tech = (tech_for_clock.equalsIgnoreCase("eev") ? "tev"
                                                                       : "eev");
            EventMgr.getInstance().info(new Integer(this.getIdsite()),
                "System", "switch", "SW09",
                new Object[] { this.getDescription(), last_tech, tech_for_clock });
        }
        else //AUTOSWITCH GIA PARTITO
        {
            if (now.after(this.getSt_nextswitch())) //se ho passato l'ora di switch
            {
                String tech_for_clock = getNowTecnologyByClock(now);

                if (!this.st_type.equalsIgnoreCase(tech_for_clock))
                {
                    changeTech();
                }

                //ricalcolare il prossimo next_switch
                this.st_nextswitch = getNextSwitchByClock(now);
                this.st_lastswitch = now;

                String sql = "update switchstatus set lastswitch=?,nextswitch=?,lastupdate=? where idsite=? and idswitch=?";
                Object[] param = new Object[5];
                param[0] = this.st_lastswitch;
                param[1] = this.st_nextswitch;
                param[2] = new Timestamp(System.currentTimeMillis());
                param[3] = new Integer(this.idsite);
                param[4] = new Integer(this.idswitch);
                DatabaseMgr.getInstance().executeStatement(null, sql, param);
            }
            else
            {
                this.setSt_type(getNowTecnologyByClock(now));

                String sql = "update switchstatus set type=?,lastupdate=? where idsite=? and idswitch=?";
                Object[] param = new Object[4];
                param[0] = this.getSt_type();
                param[1] = new Timestamp(System.currentTimeMillis());
                param[2] = new Integer(this.idsite);
                param[3] = new Integer(this.idswitch);
                DatabaseMgr.getInstance().executeStatement(null, sql, param);
            }
        }
    }

    public String getNowTecnologyByClock(Timestamp now) //restituisce la tecnologia che va usata in autoswitch in base all'ora e tecnologia di partenza
    {
        long delta_hour = (now.getTime() - this.getStarthour().getTime()) / 3600000; //ore passate da ultimo switch
        long n_switch = delta_hour / (this.hour / 3600); //numero cambi tecnologia secondo il clock

        if ((n_switch % 2) != 0)
        {
            if (this.auto_type.equalsIgnoreCase("eev"))
            {
                return "tev";
            }
            else
            {
                return "eev";
            }
        }
        else
        {
            return this.auto_type;
        }
    }

    public Timestamp getNextSwitchByClock(Timestamp now) //restituisce il timestamp di quando de avvenire il prossimo cambio di  tecnologia 
    {
        long delta_hour = (now.getTime() - this.getSt_nextswitch().getTime()) / 3600000; //ore passate da ultimo switch
        long n_switch = delta_hour / (this.hour / 3600); //numero cambi tecnologia secondo il clock
        long inc_hours = (n_switch + 1) * this.hour;

        Timestamp next_switch = new Timestamp(this.st_nextswitch.getTime() +
                (inc_hours * 1000));

        return next_switch;
    }

    public boolean isOnAlarmStatusForPage() throws Exception
    {
        //if (skipalarm.equalsIgnoreCase("FALSE"))
        //{
        boolean onalarm = false;
        int idalarm = -1;

        for (int i = 0; i < ids_alarms.length; i++)
        {
            idalarm = ids_alarms[i];

            if (ControllerMgr.getInstance().getFromField(idalarm)
                                 .getFormattedValue().equalsIgnoreCase("1"))
            {
                onalarm = true;

                if (this.id_var_startalarm == -1)
                {
                    this.id_var_startalarm = idalarm;
                }

                break;
            }
        }

        if (onalarm)
        {
            if (time_alarm_counter >= this.getWait_time())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }

        /*}
         *else
        {
            return false;
        }*/
    }
}
