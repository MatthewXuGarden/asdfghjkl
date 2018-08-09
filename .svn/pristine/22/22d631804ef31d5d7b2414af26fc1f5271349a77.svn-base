package com.carel.supervisor.presentation.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.TableExtBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dataaccess.reorder.ReorderInformation;
import com.carel.supervisor.dataaccess.varaggregator.VarAggregator;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bo.helper.GraphVariable;
import com.carel.supervisor.presentation.bo.helper.LineConfig;
import com.carel.supervisor.presentation.bo.helper.VarDependencyCheck;
import com.carel.supervisor.presentation.bo.helper.VarDependencyState;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.helper.SpaceHistoricalHelper;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;

 
public class BDevDetail extends BoMaster
{
    private static final int REFRESH_TIME = -1;
    //private static final int HACCP_ROWSHYSTORICAL = 301;   //prof: 1 anno    freq: 30min
    //public static final int HACCP_ROWSHYSTORICAL = 1356;   //prof: 1,5 anno    freq: 10min
    public static final int HACCP_ROWSHYSTORICAL = 644;  // 13mesi x 15min.
    public static final int HACCP_FREQ = 900;  // 15min = freq. std stabilita
    public static final int ALARM_ROWSHYSTORICAL = 100;
    public static final int ALARM_FREQ = 30;
 
    public BDevDetail(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeRefreshTime()
    {
        Properties p = new Properties();

        return p;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "enableAction(1);");
        p.put("tab2name", "enableAction(1);setAlrFreqOnLoad();");
        p.put("tab3name", "enableAction(1);detailOnload();");
        p.put("tab4name", "enableAction(1);isActionSave();");
        p.put("tab5name", "setdefault();");
        p.put("tab6name", "enableAction(1);inittab6();isMainVarSave();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
    	String virtkey = "";
        //determino se � abilitata la VirtualKeyboard:
        if (VirtualKeyboard.getInstance().isOnScreenKey())
        {
        	virtkey = "keyboard.js;";
        }
    	
    	Properties p = new Properties();

    	/* Alessandro : 
    	 * to avoid javascript conflicts you need to change the order between keyboard.js and devdetail.js
    	 * putting keyboard.js as the first item and devdetail.js as the second item
    	 */
        p.put("tab1name", virtkey+"devdetail.js;../arch/FileDialog.js");
        p.put("tab2name", "devdetail_alarms.js");
        p.put("tab3name", virtkey+"devdetail.js;devdetail_hist.js;");
        p.put("tab4name", virtkey+"devdetail_descr.js");
        p.put("tab5name", virtkey+"note.js");
        p.put("tab6name", "devdetail.js;dbllistbox.js;");

        return p;
    }

    protected Properties initializeDocType()
    {
    	Properties p = new Properties();
    	p.put("tab1name", DOCTYPE_STRICT);
    	return p;
    }
    
    private boolean isVariableActive(int idsite, int idvariable)
        throws DataBaseException
    {
        String sql = "select isactive from cfvariable where idsite =? and idvariable=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(idvariable) });

        if (UtilBean.trim(rs.get(0).get("isactive").toString()).equals("TRUE"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isVariableHaccp(int idsite, int idvariable)
        throws DataBaseException
    {
        String sql = "select ishaccp from cfvariable where idsite =? and idvariable=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(idvariable) });

        if (UtilBean.trim(rs.get(0).get("ishaccp").toString()).equals("TRUE"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isDeviceActive(int idsite, int iddevice)
        throws DataBaseException
    {
        String sql = "select isenabled from cfdevice where idsite=? and iddevice=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(iddevice) });

        if (UtilBean.trim(rs.get(0).get("isenabled").toString()).equals("TRUE"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
    {
        String cmd = us.getPropertyAndRemove("cmd");
        
        int idsite = us.getIdSite();
        String language = us.getLanguage();
        LangService lang_s = LangMgr.getInstance().getLangService(language);
        
        UserTransaction ut = us.getCurrentUserTransaction();

        if (cmd.equals("dev_save"))
        {
            int iddev = Integer.parseInt(prop.getProperty("iddev"));

            //  retrieve lingue, descrizioni in lingua dalle properties,e inserimento in HashMap
            String sql = "select languagecode from cfsiteext where idsite = ?";
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                    new Object[] { new Integer(1) });
            Map description = new HashMap();

            for (int i = 0; i < rs.size(); i++)
            {
                description.put(rs.get(i).get("languagecode").toString(),
                    prop.getProperty(rs.get(i).get("languagecode").toString()));
            }

            sql = "update cftableext set description = ?, lastupdate = ? where idsite = ? and languagecode = ? and tablename = ? and tableid = ? ";

            for (int i = 0; i < rs.size(); i++)
            {
                Object[] values = new Object[6];
                values[0] = description.get(rs.get(i).get("languagecode").toString());
                values[1] = new Timestamp(System.currentTimeMillis());
                values[2] = new Integer(idsite);
                values[3] = rs.get(i).get("languagecode");
                values[4] = "cfdevice";
                values[5] = Integer.valueOf(prop.getProperty("iddev"));
                DatabaseMgr.getInstance().executeStatement(null, sql, values);
            }

            //setto lo state
            String isenabled = prop.getProperty("chk_state");

            if ((isenabled.equals("true")) && (isDeviceActive(idsite, iddev))) //disabilitazione dispositivo
            {
                DeviceBean tmp = DeviceListBean.retrieveSingleDeviceById(idsite, iddev, language);
                String code = tmp.getCode();
                int index_of_point = code.indexOf(".");
                String line = null;

                if (index_of_point != -1)
                {
                    line = code.substring(0, index_of_point);
                }

                DeviceBean.updateIsEnabled(idsite, iddev, "FALSE");
                DirectorMgr.getInstance().deActiveDevice(new Integer[] { new Integer(iddev) });

                //LOG DISAB
                if (line != null) //dispositivo fisico
                {
                    EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                        "Config", "W034", new Object[] { tmp.getDescription(), line });
                }
                else //dispositivo logico
                {
                    EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                        "Config", "W036", new Object[] { tmp.getDescription() });
                }

                //Devo rimuovere tutti gli allarmi dalla coda per il guardiano se appartengono al dispositivo appena disabilitato
                sql = "select idvariable from cfvariable where iddevice = ? and type = 4";

                RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(iddev)});

                if (null != recordset)
                {
                    Integer idVar = null;

                    for (int i = 0; i < recordset.size(); i++)
                    {
                        try
                        {
                            idVar = (Integer) recordset.get(i).get(0);
                            AlarmCtrl.calledOff(idVar);
                        }
                        catch (Exception e)
                        {
                            Logger logger = LoggerMgr.getLogger(this.getClass());
                            logger.error(e);
                        }
                    }
                }
            }
            else if ((isenabled.equals("false")) && (!isDeviceActive(idsite, iddev))) //abilitazione dispositivo
            {
                DeviceBean tmp = DeviceListBean.retrieveSingleDeviceById(idsite, iddev, language);
                String code = tmp.getCode();
                int index_of_point = code.indexOf(".");
                String line = null;

                if (index_of_point != -1)
                {
                    line = code.substring(0, index_of_point);
                }

                DeviceBean.updateIsEnabled(idsite, iddev, "TRUE");
                DirectorMgr.getInstance().activeDevice(new Integer[] { new Integer(iddev) });

                //LOG ENAB
                if (line != null) //dispositivo fisico
                {
                    EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                        "Config", "W033", new Object[] { tmp.getDescription(), line });
                }
                else //dispositivo logico
                {
                    EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                        "Config", "W035", new Object[] { tmp.getDescription() });
                }
            }

            //          aggiornamento sessione
            us.getGroup().reloadDeviceStructureList(idsite, us.getLanguage());
            DirectorMgr.getInstance().mustRestart();
        } //chiuso caso dev_save

        //////////////////////////////////////////////////////////////////////////////////////
        if (cmd.equals("dev_alr_save"))
        {
            int iddev = Integer.parseInt(prop.getProperty("iddev"));
            int freq = Integer.parseInt(prop.getProperty("frequency"));
            VarphyBeanList alarmlist = new VarphyBeanList();
            VarphyBean[] alarms = alarmlist.getAlarmVarPhy(us.getLanguage(), idsite, iddev);

            String tmp = null;
            Map check = new HashMap();
            Map prior = new HashMap();
            long historical = 0;

            for (int i = 0; i < alarms.length; i++)
            {
                int tmpAlarmId = alarms[i].getId().intValue();
                tmp = prop.getProperty("alr_" + tmpAlarmId);

                if (tmp.equalsIgnoreCase("true"))
                {
                    check.put(new Integer(tmpAlarmId), "TRUE");

                    if (!isVariableActive(idsite, alarms[i].getId().intValue()))
                    {
                        historical = historical + ALARM_ROWSHYSTORICAL; //STORICO ALLARMI ++
                    }
                }
                else
                {
                    check.put(new Integer(tmpAlarmId), "FALSE");

                    if (isVariableActive(idsite, alarms[i].getId().intValue()))
                    {
                        historical = historical - ALARM_ROWSHYSTORICAL; //STORICO ALLARMI ++
                    }
                }

                if (prop.getProperty("prior" + tmpAlarmId) != null)
                {
                    tmp = prop.getProperty("prior" + tmpAlarmId);
                    prior.put(new Integer(tmpAlarmId), new Integer(tmp));
                }
            }

            if ((historical <= 0) || (SpaceHistoricalHelper.confirmHistorical(idsite, historical)))
            {
                for (int i = 0; i < alarms.length; i++)
                {
                    int alarmid = alarms[i].getId().intValue();
                    Integer alarmId = new Integer(alarmid);

                    if (check.get(alarmId).toString().equals("TRUE")) //checkata attiva
                    {
                        alarmlist.updateIsActive(idsite, alarmid, freq,
                            ((Integer) prior.get(new Integer(alarmid))).intValue());

                        if (!VarAggregator.existInBuffer(idsite, alarmid)) //se non c'� in buffer la inserisco
                        {
                            String sql = "insert into buffer values (?,?,?,?,?)";
                            Object[] param = new Object[5];
                            param[0] = new Integer(idsite);
                            param[1] = new Integer(alarmid);
                            param[2] = new Integer(100);
                            param[3] = new Integer(-1);
                            param[4] = new Boolean(false);
                            DatabaseMgr.getInstance().executeStatement(null, sql, param);
                        }

                        if (VarAggregator.getBufferKeyActual(idsite, alarmid) != -1) //se keyactual !=-1
                        {
                            if (VarAggregator.isVaribleInReorder(idsite, alarmid))
                            {
                                String sql =
                                    "update reorder set samplingperiod = ?, historicalperiod = ? where idsite = ? " +
                                    " and idvariable = ?";
                                Object[] param = new Object[4];
                                param[0] = new Integer(freq);
                                param[1] = new Integer(100);
                                param[2] = new Integer(idsite);
                                param[3] = new Integer(alarmid);
                                DatabaseMgr.getInstance().executeStatement(null, sql, param);
                            }
                            else
                            {
                                String sql = "insert into reorder values (?,?,?,?)";
                                Object[] param = new Object[4];
                                param[0] = new Integer(idsite);
                                param[1] = new Integer(alarmid);
                                param[2] = new Integer(freq);
                                param[3] = new Integer(100);
                                DatabaseMgr.getInstance().executeStatement(null, sql, param);
                            }
                        }
                    }
                    else //se sulla pagina � de-checkata la variabile d'allarme
                    {
                        if (isVariableActive(idsite, alarmid))
                        {
                            alarmlist.updateIsNotActive(idsite, alarmid);

                            // Modifica per rimuovere della coda del guardiano gli allarmi che non sono pi� attivi
                            //e che hanno generato un allarme
                            AlarmCtrl.calledOff(new Integer(alarmid));

                            //LOG della disattivazione dell'allarme
                            String desc = VarphyBeanList.getShortDescriptionOfVars(idsite,
                                    us.getLanguage(), alarmid);

                            EventMgr.getInstance().info(new Integer(us.getIdSite()),
                                us.getUserName(), "Config", "W027", new Object[] { desc });
                        }
                    }
                }

                //DataConfigMgr.getInstance().reload(); 
                //      aggiornamento sessione
                us.getGroup().reloadDeviceStructureList(idsite, us.getLanguage());

                DirectorMgr.getInstance().mustRestart();
            }
            else
            {
                ut.setProperty("nospace", "NO");
            }
        } //chiuso dev_alr_save

        if (cmd.equals("dev_var_save"))
        {
        	VarDependencyState state = new VarDependencyState();
        	
        	boolean haveToCreateProtocolFile = false;
            
            int iddev = Integer.parseInt(prop.getProperty("iddev"));
            long historicalSpace = 0;
            
            List<Integer> removedhistids = new ArrayList<Integer>(); 

            VarAggregator varlist = new VarAggregator(idsite, us.getLanguage(), iddev);
            VarphyBean[] vars = varlist.getVarList();

            String tmphaccp = null; //serve per capire se una variabile � storicizzata secondo standard HACCP
            String tmphist = null; //serve per capire se c'� storico o meno per una varibile
            String tmpfreq = null; //frequenza, variazione e profondit� storica per ogni variabile
            String tmpdelta = null; //
            String tmpprof = null; //

            Map haccp = new HashMap();
            List var_graph_remove = new ArrayList();

            for (int i = 0; i < vars.length; i++) //ciclo per variabile, calcolo per storico
            {
                VarphyBean master = vars[i];
                int tmpVarId = master.getId().intValue();
                tmphaccp = prop.getProperty("haccp" + tmpVarId);
                tmphist = prop.getProperty("hist" + tmpVarId);
                tmpprof = prop.getProperty("prof" + tmpVarId);
                tmpfreq = prop.getProperty("freq" + tmpVarId);

                //dati della jsp mappati per ogni variabile
                if (master.getType() != 1)
                {
                    tmpdelta = prop.getProperty("delta" + tmpVarId);
                }
                else
                {
                    tmpdelta = String.valueOf(1);
                }

                if (tmphaccp != null&&!tmphaccp.equalsIgnoreCase("false"))
                {
                    haccp.put(new Integer(tmpVarId), "TRUE");

                    if (!isVariableHaccp(idsite, tmpVarId)) //SPACE++ per HACCP
                    {
                        historicalSpace = historicalSpace + HACCP_ROWSHYSTORICAL;
                    }
                }
                else
                {
                    haccp.put(new Integer(tmpVarId), "FALSE");

                    if (isVariableHaccp(idsite, tmpVarId)) //SPACE-- per HACCP
                    {
                        historicalSpace = historicalSpace - HACCP_ROWSHYSTORICAL;
                    }
                }

                if (tmphist != null&&!tmphist.equalsIgnoreCase("false"))
                {
                    if (master.getSon() != null)
                    {
                        //variazione storica -> sommo o sottraggo
                        historicalSpace = historicalSpace +
                            (ReorderInformation.calculateNewKeyMax(new Short(
                                    (short) master.getType()), new Integer(tmpprof),
                                new Integer(tmpfreq)) -
                            VarAggregator.getKeyMax(idsite, master.getSon().getId().intValue(),
                                master.getSon().getFrequency().intValue()));
                    }
                    else //SPACE++ per aggiunta variabile storico
                    {
                        historicalSpace = historicalSpace +
                            ReorderInformation.calculateNewKeyMax(new Short(
                                    (short) master.getType()), new Integer(tmpprof),
                                new Integer(tmpfreq));
                    }
                }
                else
                {
                    if (master.getSon() != null)
                    {
                        // se tolta storicizzazione log, inserisco id nell'eleco delle variabili da controllare
                    	// prima della rimozione
                    	removedhistids.add(master.getId());
                    	historicalSpace = historicalSpace -
                            VarAggregator.getKeyMax(idsite, master.getSon().getId().intValue(),
                                master.getSon().getFrequency().intValue());
                    }
                }
            }

            int[] ids_variables = new int[removedhistids.size()];
            
            for(int i=0; i<removedhistids.size(); i++)
            {
            	ids_variables[i] = removedhistids.get(i);
            }
            
            if(removedhistids.size() > 0)
            {
            	DeviceBean dev = DeviceListBean.retrieveSingleDeviceById(idsite, iddev, language);
            	int iddevmdl = dev.getIddevmdl();
                state = LineConfig.checkVariableDependence(idsite, ids_variables, iddevmdl, LineConfig.HISTOR_CHECK, language);
            }
            
            if (state.dependsOn() == false && ((historicalSpace <= 0) || (SpaceHistoricalHelper.confirmHistorical(idsite, historicalSpace))))
            {
                //AZIONI eseguite solo se spazio disponibile
                for (int i = 0; i < vars.length; i++) //ciclo per variabile
                {
                    //  HACCP update su cfvariable (master)
                    VarphyBean master = vars[i];
                    int tmpVarId = master.getId().intValue();
                    tmphaccp = prop.getProperty("haccp" + tmpVarId);
                    tmphist = prop.getProperty("hist" + tmpVarId);
                    tmpprof = prop.getProperty("prof" + tmpVarId);
                    tmpfreq = prop.getProperty("freq" + tmpVarId);

                    if (master.getType() != 1)
                    {
                        tmpdelta = prop.getProperty("delta" + tmpVarId);
                    }

                    boolean exist = VarAggregator.existInBuffer(idsite, master.getId().intValue());

                    boolean ishaccp = isVariableHaccp(idsite, master.getId().intValue());
                    VarAggregator.updateIsHACCP(idsite, tmpVarId,
                        haccp.get(new Integer(tmpVarId)).toString());

                    //LOG rimozione variabili haccp
                    if (ishaccp && haccp.get(new Integer(tmpVarId)).toString().equals("FALSE"))
                    {
                        String desc = VarphyBeanList.getShortDescriptionOfVars(idsite, language,
                                master.getId().intValue());
                        EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                            "Config", "W029", new Object[] { desc });
                        
                        //rimozione info grafico
                        var_graph_remove.add(master.getId());
                        GraphVariable.removeVariableGraphInfo(idsite,us.getProfile(),iddev,master.getId().intValue(),"TRUE");
                    }

                    //se in buffer non esiste, e se checkata -> insert in buffer e hsfrequency 
                    if ((!exist) &&
                            (haccp.get(new Integer(tmpVarId)).toString().equalsIgnoreCase("TRUE")))
                    {
                        String sql = "insert into buffer values (?,?,?,?,?)";
                        Object[] param = new Object[5];
                        param[0] = master.getSite();
                        param[1] = master.getId();
                        param[2] = new Integer(HACCP_ROWSHYSTORICAL);
                        param[3] = new Integer(-1);
                        param[4] = new Boolean(false);
                        DatabaseMgr.getInstance().executeStatement(null, sql, param);
                        /*
                        sql = "insert into hsfrequency values (?,?,?,?)";
                        param = new Object[4];
                        param[0] = master.getSite();
                        param[1] = master.getId();
                        param[2] = new Integer(HACCP_FREQUENCY);
                        param[3] = new Timestamp(System.currentTimeMillis());
                        DatabaseMgr.getInstance().executeStatement(null, sql, param);*/
                        
                        //insert variabile default per grafico haccp
                        GraphVariable.removeVariableGraphInfo(idsite,us.getProfile(),iddev,tmpVarId,"TRUE");
                        GraphVariable.insertVariableGraphInfo(idsite,tmpVarId,iddev,"TRUE",us.getProfile());
                    }
                    
                    //STORICO 
                    if (tmphist != null&&!tmphist.equalsIgnoreCase("false")) //storico checked
                    {
                        if (master.getIdhsvariable().intValue() == -1) //creazione riga storico
                        {
                            haveToCreateProtocolFile = true;
                            VarphyBean slave = new VarphyBean(master); //creo slave
                            slave.setIdhsvariable(null);
                            slave.setFrequency(new Integer(tmpfreq));

                            if (master.getType() != 1)
                            {
                                slave.setVariation(Double.parseDouble(tmpdelta));
                            }
                            else
                            {
                                slave.setVariation(1); //variazione per variabile digitale
                            }

                            slave.setDelay(master.getDelay());
                            slave.setActive(true);
                            slave.setGrpcategory(master.getGrpcategory());
                            slave.setInsertTime(new Timestamp(System.currentTimeMillis()));
                            slave.setLastUpdate(new Timestamp(System.currentTimeMillis()));
                            slave.setHaccp(false);

                            int id_slave = slave.save();

                            //inserimento buffer e hsfrequency
                            String sql = "insert into buffer values (?,?,?,?,?)";
                            Object[] param = new Object[5];
                            param[0] = slave.getSite();
                            param[1] = new Integer(id_slave);

                            param[2] = new Short(ReorderInformation.calculateNewKeyMax(
                                        new Short((short) slave.getType()), new Integer(tmpprof),
                                        slave.getFrequency()));

                            param[3] = new Integer(-1);
                            param[4] = new Boolean(false);
                            DatabaseMgr.getInstance().executeStatement(null, sql, param);
                            
                            /*
                            sql = "insert into hsfrequency values (?,?,?,?)";
                            param = new Object[4];
                            param[0] = slave.getSite();
                            param[1] = new Integer(id_slave);
                            param[2] = slave.getFrequency();
                            param[3] = new Timestamp(System.currentTimeMillis());
                            DatabaseMgr.getInstance().executeStatement(null, sql, param);*/

                            LangUsedBeanList langlist = new LangUsedBeanList();
                            LangUsedBean[] langs = langlist.retrieveAllLanguage(idsite);

                            for (int j = 0; j < langs.length; j++)
                            {
                                String tempdesc = TableExtBean.retrieveDescritionFromTableById(idsite,
                                        langs[j].getLangcode(), master.getId().intValue(),
                                        "cfvariable");
                                TableExtBean.insertTableExt(idsite, langs[j].getLangcode(),
                                    "cfvariable", tempdesc, id_slave);
                            }

                            VarAggregator.updateIdHsVariable(idsite, master.getId().intValue(),
                                id_slave);
                            
                            //insert variabile default per grafico storico
                            GraphVariable.insertVariableGraphInfo(idsite,id_slave,iddev,"FALSE",us.getProfile());
                        }
                        else //UPDATE STORICO ESISTENTE
                        {
                            //update variabile slave
                            int idslave = master.getIdhsvariable().intValue();

                            if (master.getType() != 1)
                            {
                                VarAggregator.updateFreqAndVarMin(idsite, idslave,
                                    Integer.parseInt(tmpfreq), new Double(tmpdelta).doubleValue());
                            }
                            else
                            {
                                VarAggregator.updateFrequency(idsite, idslave,
                                    Integer.parseInt(tmpfreq));
                            }

                            if (VarAggregator.getBufferKeyActual(idsite, idslave) != -1) //se keyactual !=-1
                            {
                                if (VarAggregator.isVaribleInReorder(idsite, idslave))
                                {
                                    String sql =
                                        "update reorder set samplingperiod = ?, historicalperiod = ? where idsite = ? " +
                                        " and idvariable = ?";
                                    Object[] param = new Object[4];
                                    param[0] = new Integer(tmpfreq);
                                    param[1] = new Integer(tmpprof);
                                    param[2] = new Integer(idsite);
                                    param[3] = new Integer(idslave);
                                    DatabaseMgr.getInstance().executeStatement(null, sql, param);
                                }
                                else
                                {
                                    String sql = "insert into reorder values (?,?,?,?)";
                                    Object[] param = new Object[4];
                                    param[0] = new Integer(idsite);
                                    param[1] = new Integer(idslave);
                                    param[2] = new Integer(tmpfreq);
                                    param[3] = new Integer(tmpprof);
                                    DatabaseMgr.getInstance().executeStatement(null, sql, param);
                                }
                            }
                            else //update buffer e frequency
                            {
                                String sql = "update buffer set keymax = ? where idsite = ? and idvariable = ?";
                                Object[] param = new Object[3];
                                param[0] = new Short(ReorderInformation.calculateNewKeyMax(
                                            new Short((short) master.getType()),
                                            new Integer(tmpprof), new Integer(tmpfreq)));
                                param[1] = new Integer(idsite);
                                param[2] = new Integer(idslave);
                                DatabaseMgr.getInstance().executeStatement(null, sql, param);

                                /*sql = "update hsfrequency set frequency = ?, inserttime = ? where idsite = ?  and idvariable = ?";
                                param = new Object[4];
                                param[0] = new Integer(tmpfreq);
                                param[1] = new Timestamp(System.currentTimeMillis());
                                param[2] = new Integer(idsite);
                                param[3] = new Integer(idslave);
                                DatabaseMgr.getInstance().executeStatement(null, sql, param);*/
                            }
                        }
                    }
                    else //storico unchecked
                    {
                        if (master.getIdhsvariable().intValue() != -1) //se gia esisteva slave
                        {
                            String desc = master.getSon().getShortDescription();

                            VarAggregator.cancelHsVariable(idsite, master.getId().intValue(),
                                master.getIdhsvariable().intValue());

                            //LOG eliminazione variabile dallo storico
                            EventMgr.getInstance().info(new Integer(us.getIdSite()),
                                us.getUserName(), "Config", "W028", new Object[] { desc });
                            
                            //rimozione info grafico
                            var_graph_remove.add(master.getIdhsvariable());
                            GraphVariable.removeVariableGraphInfo(idsite,us.getProfile(),iddev,master.getIdhsvariable().intValue(),"FALSE");
                        }
                    }
                }

                us.getGroup().reloadDeviceStructureList(idsite, us.getLanguage());
                
                GraphVariable.deleteVarGroupCfPageGraph(idsite,us.getProfile(),iddev,var_graph_remove);
                GraphVariable.reorderCfPageGrah(idsite,us.getProfile(),iddev);

                DirectorMgr.getInstance().mustRestart();
                
                if(haveToCreateProtocolFile)
                    DirectorMgr.getInstance().mustCreateProtocolFile();        
            }
            else
            {
               if(state.dependsOn())
               {
            	    String str =  lang_s.getString("importctrl", "nooperation")+"\n";
               	    str += state.getMessagesAsString();
            	    ut.setProperty("historerror",str);
               }
               else
            	   ut.setProperty("nospace", "NO");
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////////
        if (cmd.equals("load_var_desc"))
        {
        }
        
        if (cmd.equals("desc_var_save"))
    	{
    		int num = Integer.parseInt(prop.getProperty("num"));
    		String sql="update cftableext set description= ?, shortdescr= ?,lastupdate = ? where idsite=? and languagecode = ? and tablename='cfvariable' and tableid= ? ";
    		String sql2="update cfvariable set measureunit = ?,lastupdate = ? where idvariable in " +
    		"( " +
    		"select idvariable from cfvariable where idvariable = ? " +
    		"union "+
    		"select idhsvariable from cfvariable where idvariable =? "+
    		")";

    		Object[] paramSql = new Object[6];
    		Object[] paramSql2 = new Object[4];
    		
    		try{
	    		for(int i=0; i<num; i++)
	    		{
	    			paramSql2[0] = prop.getProperty("misura_"+i);
	    			paramSql2[1] = new Timestamp(System.currentTimeMillis());
	    			paramSql2[2] = new Integer(prop.getProperty("id_"+i));
	    			paramSql2[3] = new Integer(prop.getProperty("id_"+i));
	    			DatabaseMgr.getInstance().executeStatement(null, sql2, paramSql2, true);
	    	
	    			paramSql[0] = prop.getProperty("description_"+i);
	    			paramSql[1] = prop.getProperty("shortDesc_"+i);
	    			paramSql[2] = new Timestamp(System.currentTimeMillis());
	    			paramSql[3] = new Integer(1);
                    paramSql[4] = prop.getProperty("language");
	    			paramSql[5] = new Integer(prop.getProperty("id_"+i));
	    			DatabaseMgr.getInstance().executeStatement(null, sql, paramSql, true);
                    
	    			try
	    			{
	    				if(Integer.parseInt(prop.getProperty("id_"+i))!=-1)
	    				{
		    				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select idhsvariable from cfvariable where idvariable="+prop.getProperty("id_"+i));
		    				if(rs.size()>0)
		    				{
		    					DatabaseMgr.getInstance().executeStatement(
									null, 
									"update cftableext set description= ?, shortdescr= ?,lastupdate = ? where idsite=? and languagecode = ? and tablename='cfvariable' and tableid= ? ", 
									new Object[]{prop.getProperty("description_"+i), prop.getProperty("shortDesc_"+i),new Timestamp(System.currentTimeMillis()),new Integer(1), prop.getProperty("language"), rs.get(0).get("idhsvariable")}, 
									true);
		    				}
	    				}
	    			}
	    			catch(Exception ex)
	    			{
	    				ex.printStackTrace();
	    			}
	    		}
	    		us.setProperty("actionsave","ok");
    		}
    		catch(Exception e)
    		{
        		LoggerMgr.getLogger(this.getClass()).error(e);
        		us.setProperty("actionsave","bad");
    		}
    	}
        
        //fbb
	    if(cmd.equalsIgnoreCase("dev_varpos_save"))
	    {
	    	String ab = us.getProperty("ab");
	    	boolean b1 = false;
	    	boolean b2 = false;
	    	if(ab.equals("a"))
	    	{
	    		b1 = saveselection(prop);
	    		b2 = true;
	    	}
	    	else
	    	{
	    		b1 = true;
	    		b2 = saveorder(prop);
	    	}
	    	if(b1 && b2)
	    	{
	    		us.setProperty("varposresult", "ok");
		    	us.setProperty("ab","a");	    	
	    	}
	    	else
	    	{
	    		us.setProperty("varposresult", "bad");
		    	us.setProperty("ab",ab);
	    	}
	    	us.setProperty("ab",ab);
	    }

	    if(cmd.equalsIgnoreCase("dev_varpos_order"))
	    {
	    	boolean b = saveselection(prop);
	    	if(b)
	    	{
	    		us.setProperty("varposresult", "ok");
		    	us.setProperty("ab","b");	    	
	    	}
	    	else
	    	{
	    		us.setProperty("varposresult", "bad");
		    	us.setProperty("ab","a");	    	
	    	}
	    }

	    if(cmd.equalsIgnoreCase("dev_varpos_select"))
	    {
	    	boolean b = saveorder(prop);
	    	if(b)
	    	{
	    		us.setProperty("varposresult", "ok");
		    	us.setProperty("ab","a");	    	
	    	}
	    	else
	    	{
	    		us.setProperty("varposresult", "bad");
		    	us.setProperty("ab","b");	    	
	    	}
	    }
    } //chiuso postaction
    
    private boolean saveselection(Properties prop)
    {
    	boolean ret = true;
    	Object[] parms;
    	try
    	{
	    	String current = "";
	    	Enumeration en = prop.keys();
	    	while(en.hasMoreElements())
	    	{
	    		current = (String)en.nextElement();
	    		if( current.startsWith("radioro") || current.startsWith("radiorw"))
	    		{
		    		parms = new Object[]{prop.remove(current),new Timestamp(System.currentTimeMillis()), new Integer(current.substring(7))};
		    		DatabaseMgr.getInstance().executeStatement(null, "update cfvariable set todisplay=?,lastupdate = ? where idvariable=?", parms);
	    		}
	    	}
    	} catch (Exception e)
    	{
    		LoggerMgr.getLogger(this.getClass()).error(e);
	    	ret = false;
    	}
    	return ret;
    }

    private boolean saveorder(Properties prop)
    {
    	StringTokenizer keys = new StringTokenizer(prop.getProperty("varposorder"),",");
    	Integer iddevice = new Integer(prop.getProperty("iddev"));
    	boolean ret = true;
    	try
    	{
    		Object[] parms;
    		String sql;
    		//reset priorities
    		sql = "update cfvariable set priority=?,lastupdate = ? where iddevice=? and type <> ? and readwrite like '1 '";
    		parms = new Object[]{new Integer(9999),new Timestamp(System.currentTimeMillis()), iddevice, new Integer(4)};
    		DatabaseMgr.getInstance().executeStatement(null, sql, parms);
    		
    		//set priorities
	    	int prioritycounter = 1;
	    	while(keys.hasMoreTokens())
	    	{
	    		parms = new Object[]{new Integer(prioritycounter),new Timestamp(System.currentTimeMillis()), new Integer(keys.nextToken().trim())};
	    		sql = "update cfvariable set priority=?,lastupdate = ? where idvariable=?";
	    		DatabaseMgr.getInstance().executeStatement(null, sql, parms);
	    		
	    		prioritycounter++;
	    	}
    	} catch (Exception e)
    	{
    		LoggerMgr.getLogger(this.getClass()).error(e);
	    	ret = false;	    		
    	}
    	return ret;
    }
    
    public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
    {
		String cmd = prop.getProperty("cmd");
		String toReturn="";
		
		// Se aggiungo una nuova unit� di misura
	    if(cmd.equalsIgnoreCase("new_unitmeasure")){
	    	//SeqMgr o = SeqMgr.getInstance();
	    	String sql="insert into unitmeasurement values((select max(idunitmeasurement)+1 from unitmeasurement),?)";
	    	Object[] param = new Object[1];
	    	//param[0] = o.next(null, "unitmeasurement", "idunitmeasurement");
			param[0] = prop.getProperty("unit_misura");
			toReturn = "<inserimento>";
			try{
	    		DatabaseMgr.getInstance().executeStatement(null, sql, param);
	    		toReturn += "<attrib id='insert'>ok</attrib>";
	    		toReturn += "<attrib id='valore'>"+param[0]+"</attrib>";
	    		//us.setProperty("combo_insert","ok");
	    	}
	    	catch (DataBaseException e) {
	    		toReturn += "<attrib id='insert'>failed</attrib>";
	    		//us.setProperty("combo_insert","failed");
			}
	    	toReturn += "</inserimento>";
	    }
	    
	    //Quando elimino una misura
	    if(cmd.equalsIgnoreCase("remove_unitmeasure")){
	    	String sql="delete from unitmeasurement where idunitmeasurement = ?";
	    	Object[] param = new Object[1];
	    	try{
	    		param[0] = new Integer(prop.getProperty("combo_misure"));
	    	}catch(NumberFormatException e){
	    		sql = "delete from unitmeasurement where description = ?";
	    		param[0] = prop.getProperty("combo_misure");
	    	}
	    		toReturn = "<remove>";
	    	try{
	    		DatabaseMgr.getInstance().executeStatement(null, sql, param);
	    		//sql = "select description from unitmeasurement where idunitmeasurement <> 0";
	    		//RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
	    		//StringBuffer s = new StringBuffer();
	    		//toReturn += "<attrib id='size'>"+rs.size()+"</attrib>";
	    		//for (int i = 0; i < rs.size(); i++)
	            //{
	            //    s.append("<attrib id='val_"+i+"'>"+rs.get(i).get("description")+"</attrib>");
	            //}
	    		//toReturn += s.toString();
	    		toReturn += "<attrib id='remove'>ok</attrib>";
	    		//us.setProperty("combo_delete","ok");
	    	}
	    	catch (DataBaseException e) {
	    		toReturn += "<attrib id='remove'>failed</attrib>";
	    		//us.setProperty("combo_delete","failed");
			}
	    	toReturn += "</remove>";
	    }
	    
	    return toReturn;
    }
} //chiusa classe
