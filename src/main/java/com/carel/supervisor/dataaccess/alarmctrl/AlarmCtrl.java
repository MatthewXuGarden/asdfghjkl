package com.carel.supervisor.dataaccess.alarmctrl;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class AlarmCtrl
{
    private static AlarmCtrl me = new AlarmCtrl();
    private Map data = new HashMap();
    /*
    * Stati:
    * I : inserito
    * P : segnalato da PlantVisor
    * G : segnalato da Guardiano
    * R : resettato alla ripartenza del PVPro dopo 15 minuti
    * U : resettato dall'utente
    * N : non gestito e da notificare
    */
    private AlarmCtrl()
    {
    }

    public static AlarmCtrl getInstance()
    {
        return me;
    }

    //Inserimento degli allarmi ogni volta che dal campo vengono identificati con valore 1
    public void insert(Integer idVariable, Integer priority)
    {
        if (checkAlarm())
        {
            try
            {
                int alarmPriority = priority.intValue();
                if (alarmPriority > 4)
                {
                	alarmPriority = 4;
                }
            	String key = "priority_" + alarmPriority;
                int minutes = 30;

                //Nel caso in cui non si sia salvata l'informazione sulla base dati, uso il default
                try
                {
                    minutes = (int) SystemConfMgr.getInstance().get(key).getValueNum();
                }
                catch (Exception e)
                {
                    switch (alarmPriority)
                    {
                    case 1:
                        minutes = 30;

                        break;

                    case 2:
                        minutes = 60;

                        break;

                    case 3:
                        minutes = 90;

                        break;

                    case 4:
                        minutes = 120;

                        break;

                    default:
                        minutes = 120;

                        break;
                    }
                }

                long now = System.currentTimeMillis();
                long expired = now + (minutes * 60 * 1000);
                /*
                 * Inserito nuovo punto di controllo per la gestione degli allarmi
                 * in base alle regole e fascie
                 */ 
                if(checkVariable(idVariable, priority))
                {
                	// Allarme da gestire nel tempo limite
                	data.put(idVariable,
                			new Object[]{idVariable,new Integer(0),"I",new Timestamp(expired)});
                }
                else
                {
                	// Nel caso in cui l'allarme non si inserito in una regola e non è in fascia
                	if(priority.intValue() == 1)
                	{
                		// Allarme non gestito da regola ma di P=1 e quindi da segnalare dopo il tempo limite
                		data.put(idVariable,
                				 new Object[]{idVariable,new Integer(0),"N",new Timestamp(expired)});
                	}
                }
            }
            catch (Exception e) {
            	e.printStackTrace();
                LoggerMgr.getLogger(this.getClass()).error(e);
            }
        }
    }

    public synchronized void commitInsert() throws DataBaseException
    {
        if (checkAlarm())
        {
            if (0 < data.size())
            {
                String sql = "select idvariable from alarmsctrl";
                RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, null);
                Integer id = null;
                
                // Toglie dalla memoria quelli già presenti in tabella
                for (int i = 0; i < recordset.size(); i++)
                {
                    id = (Integer) recordset.get(i).get(0);
                    data.remove(id);
                }
                // Inserisce i nuovi allarmi
                if (0 < data.size())
                {
                    Object[][] values = new Object[data.size()][];
                    Iterator key = data.keySet().iterator();
                    Integer k = null;
                    int i = 0;

                    while (key.hasNext())
                    {
                        k = (Integer) key.next();
                        values[i] = (Object[]) data.get(k);
                        i++;
                    }

                    sql = "insert into alarmsctrl values (?,?,?,current_timestamp,?,current_timestamp)";
                    DatabaseMgr.getInstance().executeMultiStatementNoError(null, sql, values);
                    data.clear();
                    values = null;
                }
            }
        }
    }

    /*
     * Retrive degli allarmi che devono dovevano essere processati e non lo sono stati.
     * Questo metodo viene richiamato dalla sonda: AllarmController.jsp
     */
    public static Integer[] retriveExpired()
    {
        try
        {
            Integer[] idvariable = null;
            String sql = "select idvariable from alarmsctrl where expired < current_timestamp and status = 'I'";
            RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, null);

            if (recordset.size() > 0)
            {
                idvariable = new Integer[recordset.size()];
            }

            for (int i = 0; i < recordset.size(); i++)
            {
                idvariable[i] = (Integer) recordset.get(i).get(0);
            }

            return idvariable;
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(AlarmCtrl.class);
            logger.error(e);
            return null;
        }
    }
    
    public static Integer[] retriveNotifyExpired()
    {
        try
        {
            Integer[] idvariable = null;
            String sql = "select idvariable from alarmsctrl where expired < current_timestamp and status = 'N'";
            RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, null);

            if (recordset.size() > 0)
            {
                idvariable = new Integer[recordset.size()];
            }

            for (int i = 0; i < recordset.size(); i++)
            {
                idvariable[i] = (Integer) recordset.get(i).get(0);
            }

            return idvariable;
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(AlarmCtrl.class);
            logger.error(e);
            return null;
        }
    }
    
    //Appena il Plantvisor riesce a gestire l'allarme almeno con un canale, allora cambia lo stato all'allarme
    //solo se l'allarme non è stato cancellato dall'utente o già gestito dal guardiano
    public static void notifyPVPRO(Integer idVariable)
    {
        if (checkAlarm())
        {
            try
            {
                String sql = "update alarmsctrl set counter = counter + 1, status = 'P'," +
                    " lastupdate = current_timestamp where idvariable = ? and status in ('I','P','N')";
                DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idVariable });
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(AlarmCtrl.class);
                logger.error(e);
            }
        }
    }

    /*
     * Se la sonda AlarmController.jsp verifica e notifica la presenza di allarmi non gestiti
     * viene cambiato lo stato a quelli non resettati dall'utente e non gestiti da PVPRO.
     * Viene richiamato dal guardianPRO mediante la sonda RemoveAlarms.jsp
     */
    public static void notifyGuardian(Integer idVariable)
    {
        if (checkAlarm())
        {
            try
            {
                String sql = "update alarmsctrl set status = 'G'," +
                    " lastupdate = current_timestamp where idvariable = ? and status = 'I'";
                DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idVariable });
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(AlarmCtrl.class);
                logger.error(e);
            }
        }
    }
    
    public static void notifyGuardianNotify(Integer idVariable)
    {
        if (checkAlarm())
        {
            try
            {
                String sql = "update alarmsctrl set status = 'G'," +
                    " lastupdate = current_timestamp where idvariable = ? and status = 'N'";
                DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idVariable });
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(AlarmCtrl.class);
                logger.error(e);
            }
        }
    }
    
    public static void calledOff(Integer idVariable)
    {
        remove(idVariable);
    }

    //Cancellazione da parte dell'utente solo se l'allarme non è stato gestito da nessuno
    public static void userReset(Integer idVariable)
    {
        if (checkAlarm())
        {
            try
            {
                String sql = "update alarmsctrl set status = 'U'," +
                    " lastupdate = current_timestamp where idvariable = ? and status in ('I','N')";
                DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idVariable });
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(AlarmCtrl.class);
                logger.error(e);
            }
        }
    }
    
    /*
     * Richiamato da ControllerMgr.reset();
     * Esegue un backup degli allarmi dalla tabella alarmsctrl in hsalarmsctrl.
     * Pulisce la tabella alarmsctrl.
     */
    public static void reset()
    {
        try
        {
            String sql = "insert into hsalarmsctrl (select idvariable, counter, status, inserttime, expired, lastupdate, current_timestamp,'TRUE' from alarmsctrl)";
            DatabaseMgr.getInstance().executeStatement(null, sql, null);

            sql = "delete from alarmsctrl";
            DatabaseMgr.getInstance().executeStatement(null, sql, null);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(AlarmCtrl.class);
            logger.error(e);
        }
    }

    private static void remove(Integer idVariable)
    {
        if (checkAlarm())
        {
            try
            {
                //Retrive dell'allarme dalla tabella di supporto e inserimento nello storico
                String sql = "insert into hsalarmsctrl (select idvariable, counter, status, inserttime, expired, lastupdate, current_timestamp,'FALSE' from alarmsctrl where idvariable = ?)";
                DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idVariable });

                //Cancellazione dalla tabella alarmsctrl
                sql = "delete from alarmsctrl where idvariable = ?";
                DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { idVariable });
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(AlarmCtrl.class);
                logger.error(e);
            }
        }
    }

    private static boolean checkAlarm()
    {
        boolean check = true;

        try
        {
            String value = SystemConfMgr.getInstance().get("checkalarm").getValue();

            if ("TRUE".equalsIgnoreCase(value))
            {
                check = true;
            }
            else
            {
                check = false;
            }
        }
        catch (Exception e)
        {
            check = true;
        }
        

        return check;
    }
    
    /*
     * Metodo richiamato per controllare se l'allarme è coinvolto in una regola in fascia temporale corretta.
     * Utilizza una memoria ricaricata ad ogni riavvio del motore.
     * Questo metodo utilizza la reflection per recuperare il riferimento della classe
     * com.carel.supervisor.director.guardian.GuardianVarDispFilter
     */
    private boolean checkVariable(Integer idVariable, Integer priority)
    {
    	boolean ret = true;
    	try
    	{
    		Class<?> GuardianVarFilter = Class.forName("com.carel.supervisor.director.guardian.GuardianVarDispFilter");
    		Method mainMethod = GuardianVarFilter.getMethod("verifyVariable", new Class[]{Integer.class,Integer.class});
    		Object ris = mainMethod.invoke(null,new Object[]{idVariable,priority});
    		if(ris instanceof Boolean)
    			ret = ((Boolean)ris).booleanValue();
    	}
    	catch(Exception e) 
    	{
    	}
    	return ret;
    }
}
