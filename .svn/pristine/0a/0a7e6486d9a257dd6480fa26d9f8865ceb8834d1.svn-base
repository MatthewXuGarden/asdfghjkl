package com.carel.supervisor.presentation.bo;

import java.util.Properties;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.director.ac.AcGroups;
import com.carel.supervisor.director.ac.AcManager;
import com.carel.supervisor.director.ac.AcProperties;
import com.carel.supervisor.presentation.ac.AcMaster;
import com.carel.supervisor.presentation.ac.AcSlave;
import com.carel.supervisor.presentation.bean.VarMdlBean;
import com.carel.supervisor.presentation.bean.VarMdlBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.Transaction;
import com.carel.supervisor.presentation.session.UserSession;


public class BAC extends BoMaster
{
    private static final int REFRESH_TIME = 30000;
    private final int NRML_VARS = 3;
    private final int EXTRA_VARS = 6;

    public BAC(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab2name", "onload_tab2();");
        p.put("tab3name", "onload_tab3();");
        p.put("tab4name", "onload_tab4();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "ac.js");
        p.put("tab2name", "ac.js;keyboard.js;");
        p.put("tab3name", "ac.js;keyboard.js;");
        p.put("tab4name", "acmdl.js;keyboard.js;");

        return p;
    }

    public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
    {
        String cmd = us.getProperty("cmd");
        String user = us.getUserName();

        if (cmd.equalsIgnoreCase("start_ac"))
        {
            AcManager.getInstance().startAC(user,0L);
            AcGroups.loadGroups();
        }
        else if (cmd.equalsIgnoreCase("stop_ac"))
        {
            AcManager.getInstance().stopAC(user);
        }
        else if (cmd.equalsIgnoreCase("save_slave"))
        {
            Transaction transaction = us.getTransaction();
            Properties properties = new Properties();
            
            boolean risultato = AcSlave.saveAcSlaves(prop, us.getLanguage());
            long newCheck = 0L;
            
            properties.setProperty("save_slaves_risult", (new Boolean(risultato)).toString());
            
            //aggiorno abilitazione master alla propagazione:
            AcMaster.saveEnabMasters(prop);
            
            //aggiorno nomi gruppi:
            AcMaster.updGroupsNames(prop);
            
            //ricarico nomi gruppi x Eventi
            AcGroups.loadGroups();

            int clock = Integer.parseInt(prop.getProperty("ver_time")) * 60;
            
            if (clock != (new AcProperties()).getProp("ac_clock"))
            {
            	AcProperties.updateProp("ac_clock", clock);
            	newCheck = (long)clock;
            }

            // se started segnalo cambiamento
            if (AcManager.getInstance().isRunning())
            {
                // segnala di caricare la nuova configurazione dal db:
                AcManager.getInstance().setChanged(true, newCheck);
            }

            //Log salvataggio slave
            EventMgr.getInstance().info(1, user, "ac", "AC03", null);
            
            transaction.setSystemParameter(properties);
        }
        else if (cmd.equalsIgnoreCase("save_master"))
        {
            AcMaster.saveAcMasters(prop);

            // se started segnalo cambiamento
            if (AcManager.getInstance().isRunning())
            {
                AcManager.getInstance().setChanged(true);
            }

            // Log salvataggio master
            EventMgr.getInstance().info(1, user, "ac", "AC04", null);
        }
        else if (cmd.equalsIgnoreCase("save_cus_status"))
        {
            String cusStatus = prop.getProperty("cusenab");
            int maxvars = 0;
            
            if ("enab".equals(cusStatus))
            {
                maxvars = 6;
            }
            else
            {
                maxvars = 3;
            }
            
            AcProperties.updateProp("ac_maxvariable",maxvars);
        }
        else if (cmd.equalsIgnoreCase("save_models"))
        {
            //Salvo valori dal SubTab4:
            Transaction transaction = us.getTransaction();
            Properties properties = new Properties();
                        
            int idsite = us.getIdSite();
            
            // recupero iddevmdl:
            int iddevmdl = Integer.parseInt(prop.getProperty("dev_combo"));
            
            // query per recupero DevCode dal db:
            String sqlDevCode = "select code from cfdevmdl where iddevmdl=? and idsite=?";
            
            Object[] params = new Object[] {new Integer(iddevmdl), new Integer(idsite)};
            
            String devCode = "";
            
            try
            {
                RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sqlDevCode, params);
                
                if ((rs != null) && (rs.size() > 0)) devCode = (String) rs.get(0).get("code");
            }
            catch (Exception e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
            
            //recupero num max vars x propagazione:
            AcProperties acprop = new AcProperties();
            int max_n_vars = acprop.getProp("ac_maxvariable");
            
            //recupero valori x tabella Master_mdl
            int k = 0;
            
            Object[][] paramsMaster = new Object[6][6];
            
            for (int i = 1; i <= max_n_vars; i++)
            {
                if ((prop.getProperty("varmdlcode_"+i) != null) && (! "".equals(prop.getProperty("varmdlcode_"+i))))
                {
                    paramsMaster[k][0] = prop.getProperty("varmdlcode_"+i);
                    paramsMaster[k][1] = new Integer(i);
                    
                    //if ((prop.getProperty("alrmcode_"+i) != null) && (! "".equals(prop.getProperty("alrmcode_"+i))) && (! "-1".equals(prop.getProperty("alrmcode_"+i))))
                    if ((prop.getProperty("alrmcode_"+i) != null) && (! "".equals(prop.getProperty("alrmcode_"+i))))
                    {
                        paramsMaster[k][5] = prop.getProperty("alrmcode_"+i);
                    }
                    else
                    {
                        paramsMaster[k][5] = null;
                    }
 
                    if ((prop.getProperty("origmin_"+i) != null) && (! "".equals(prop.getProperty("origmin_"+i))))
                    {
                        paramsMaster[k][3] = new Float(prop.getProperty("origmin_"+i));
                    }
                    //else paramsMaster[k][3] = new Float(0);
                    else paramsMaster[k][3] = null;
                    
                    if ((prop.getProperty("origmax_"+i) != null) && (! "".equals(prop.getProperty("origmax_"+i))))
                    {
                        paramsMaster[k][4] = new Float(prop.getProperty("origmax_"+i));
                    }
                    //else paramsMaster[k][4] = new Float(0);
                    else paramsMaster[k][4] = null;
                    
                    if ((prop.getProperty("origdef_"+i) != null) && (! "".equals(prop.getProperty("origdef_"+i))))
                    {
                        paramsMaster[k][2] = new Float(prop.getProperty("origdef_"+i));
                    }
                    //else paramsMaster[k][2] = new Float(0);
                    else paramsMaster[k][2] = null;
                    
                    k++;
                }
            }

                //elimino valori precedenti del device scelto come master
                String sqlDelMstrMdl = "delete from ac_master_mdl where code = ? and index <= ?"; //var devCode
                
                Object[] delMstrParams = new Object[] {devCode, new Integer(max_n_vars)};
                
                try
                {
                    DatabaseMgr.getInstance().executeStatement(sqlDelMstrMdl, delMstrParams);
                }
                catch (Exception e)
                {
                    // PVPro-generated catch block:
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
                
            //se ho modificato la sezione master del modello:
            if (k > 0)
            {
                    // query MultiStatement x AC_Master_mdl
                    String sqlMstrMdl = "insert into ac_master_mdl values (?,?,?,?,?,?,?)";
                    
                    Object[][] newMstrParams = new Object[k][7];
                    
                    for (int i = 0; i < k; i++)
                    {
                        newMstrParams[i][0] = devCode;
                        for (int w = 0; w < 6; w++)
                            newMstrParams[i][w+1] = paramsMaster[i][w];
                    }
                    
                    try
                    {
                        DatabaseMgr.getInstance().executeMultiStatement(null, sqlMstrMdl, newMstrParams);
                    }
                    catch (Exception e)
                    {
                        // PVPro-generated catch block:
                        Logger logger = LoggerMgr.getLogger(this.getClass());
                        logger.error(e);
                    }
            }
            
            //se ho modificato la sola sezione Master del modello:
            if ("true".equals(prop.getProperty("modMaster")))
            {
                try
                {
                    //aggiornamento tabella "ac_master" e "ac_slave" dopo aggiornamento tabella "ac_master_mdl":
                    //AcMaster.removeAllMastersByCode(devCode, idsite);
                    
                    //rimuovo tutti gli slaves dei master del modello di device:
                    AcMaster.removeAllSlavesOfMaster(devCode, idsite);
                    
                    //aggiorno i dev gi� master in base al nuovo modello:
                    AcMaster.updateAllMastersOfModel(devCode, idsite);
                }
                catch (Exception e1)
                {
                    // PVPro-generated catch block:
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e1);
                }
            }
            
            //recupero valori x tabella Slave_mdl
            int j = 0;
            
            Object[][] paramsSlave = new Object[6][2];
            
            for (int i = 1; i <= max_n_vars; i++)
            {
                if ((prop.getProperty("varmdl_"+i) != null) && (! "".equals(prop.getProperty("varmdl_"+i))))
                {
                    paramsSlave[j][0] = prop.getProperty("varmdl_"+i);
                    paramsSlave[j][1] = new Integer(i);
                    j++;
                }
            }
            
                //elimino valori precedenti del device scelto come slave
                String sqlDelSlvMdl = "delete from ac_slave_mdl where code = ? and index <= ?"; //var devCode
                
                Object[] delSlvParams = new Object[] {devCode, new Integer(max_n_vars)};
                
                try
                {
                    DatabaseMgr.getInstance().executeStatement(sqlDelSlvMdl, delSlvParams);
                }
                catch (Exception e)
                {
                    // PVPro-generated catch block:
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
                
            //se ho modificato la sezione modello slave:
            if (j > 0)
            {
                //query MultiStatement x AC_Slave_mdl
                String sqlSlvMdl = "insert into ac_slave_mdl values (?,?,?)";
                
                Object[][] newSlvParams = new Object[j][3];
                
                for (int i=0; i < j; i++)
                {
                    newSlvParams[i][0] = devCode;
                    newSlvParams[i][1] = paramsSlave[i][0];
                    newSlvParams[i][2] = paramsSlave[i][1];
                }
                
                try
                {
                    DatabaseMgr.getInstance().executeMultiStatement(null, sqlSlvMdl, newSlvParams);
                }
                catch (Exception e)
                {
                    // PVPro-generated catch block:
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
            }
            
            //se ho modificato la sola sezione Slave del modello:
            if ("true".equals(prop.getProperty("modSlave")))
            {
                try
                {
                    //aggiornamento tabella "ac_slave" dopo aggiornamento tabella "ac_slave_mdl":
                    AcSlave.removeAllSlavesByCode(devCode, idsite);
                }
                catch (Exception e)
                {
                    // PVPro-generated catch block:
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
            }
            
            //aggiorno valori Extra vars:
            String sqlExtVar = "delete from ac_extra_vars where devcode=?";
            
            Object[] paramsExtraVar = new Object[] {devCode};
            
            try
            {
                DatabaseMgr.getInstance().executeStatement(null, sqlExtVar, paramsExtraVar);
            }
            catch (Exception e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }

            String ExtraVars_sql = "insert into ac_extra_vars values (?,?,?)";
            Object[] extraVarsParams = new Object[3];
            
            for (j = 1; j <= 2; j++)
            {
                if ((prop.getProperty("varmdlex_"+j) != null) && (!prop.getProperty("varmdlex_"+j).equals("")))
                {
                    extraVarsParams[0] = devCode;
                    extraVarsParams[1] = prop.getProperty("varmdlex_"+j);
                    extraVarsParams[2] = new Integer(j);
                    
                    try
                    {
                        DatabaseMgr.getInstance().executeStatement(null, ExtraVars_sql, extraVarsParams);
                    }
                    catch (Exception e)
                    {
                        // PVPro-generated catch block:
                        Logger logger = LoggerMgr.getLogger(this.getClass());
                        logger.error(e);
                    }
                }
            }

            
            //aggiorno valori var digitale del modello (lato slave):
            String sqlDigVar = "delete from ac_heartbit_mdl where code=?";
            
            Object[] paramsDigVar = new Object[] {devCode};
            
            try
            {
                DatabaseMgr.getInstance().executeStatement(null, sqlDigVar, paramsDigVar);
            }
            catch (Exception e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
            
            Float varval = null;
            
            if ((prop.getProperty("digvar") != null) && (!prop.getProperty("digvar").equals("")) && (!prop.getProperty("digvar").equals("-1")))
            {
                String DigVars_sql = "insert into ac_heartbit_mdl values (?,?,?,?)";
                Object[] digVarParams = new Object[4];
                    
                digVarParams[0] = devCode;
                digVarParams[1] = prop.getProperty("digvar");
                digVarParams[2] = prop.getProperty("timevar");
                
                varval = new Float(prop.getProperty("varval"));
                digVarParams[3] = new Float(60*varval.floatValue()); //salvo il tempo in sec.
                  
                try
                {
                    DatabaseMgr.getInstance().executeStatement(null, DigVars_sql, digVarParams);
                }
                catch (Exception e)
                {
                    // PVPro-generated catch block:
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
            }
            
            //Sezione: tbl custom vars
            if (prop.getProperty("cusenab") != null)
            {
                //aggiorno status vars "Cus":
                max_n_vars = 0;
                
                if ("enab".equals(prop.getProperty("cusenab")))
                {
                    max_n_vars = EXTRA_VARS;
                }
                else
                    if ("disab".equals(prop.getProperty("cusenab")))
                    {
                        max_n_vars = NRML_VARS;
                    }
                
                AcProperties.updateProp("ac_maxvariable",max_n_vars);
            }
            
            // se started segnalo cambiamento
            if (AcManager.getInstance().isRunning())
            {
                AcManager.getInstance().setChanged(true);
            }

            // Log salvataggio modelli
            EventMgr.getInstance().info(1, user, "ac", "AC08", null);
            
            properties.setProperty("idmstrdevmdl", (new Integer(iddevmdl)).toString());
            transaction.setSystemParameter(properties);
        }
    }

    public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
    {
        StringBuffer response = new StringBuffer("<response>");
        String cmd = prop.getProperty("cmd");
        String lang = us.getLanguage();
        int idsite = us.getIdSite();
        if ("loadvars".equalsIgnoreCase(cmd))
        {
        	int iddevmdl = Integer.parseInt(prop.getProperty("iddevmdl")) ;
        	VarMdlBean[] list = VarMdlBeanList.retrieveOrdered(idsite,iddevmdl,lang);
        	VarMdlBean tmp = null;
        	for (int i = 0; i < list.length; i++)
        	{
        		tmp = list[i];
        		response.append("<var id='" + tmp.getIdvarmdl()+"'>");
        		response.append("<descr><![CDATA[" + tmp.getDescription()+"]]></descr>");
                response.append("<type><![CDATA[" + tmp.getType()+"]]></type>");
                response.append("<rw><![CDATA[" + tmp.getReadwrite()+ "]]></rw>");
                response.append("<varcode><![CDATA[" + tmp.getCode()+"]]></varcode>");
                response.append("</var>");
        	}
        	
        	// *** x Master models ***
            //x allarme selezionato:
            String sql_1 = "select " +
                    "ac_master_mdl.index, " +
                    "ac_master_mdl.vcode, " +
                    "ac_master_mdl.alarm, " +
                    "ac_master_mdl.min, " +
                    "ac_master_mdl.max, " +
                    "ac_master_mdl.def, " +
                    //ex "v1.idvarmdl as varmdl, " +
                    //ex "v2.idvarmdl as alrmmdl, " +
                    "var1.description as vdesc, " +
                    "var2.description as adesc " +
        			" from ac_master_mdl, cftableext as var1, cftableext as var2, cfdevmdl, cfvarmdl as v1, cfvarmdl as v2 where " +
        			" cfdevmdl.iddevmdl=? and cfdevmdl.code=ac_master_mdl.code and v1.iddevmdl=? and v1.code=ac_master_mdl.vcode and " +
        			" v2.iddevmdl=? and v2.code=ac_master_mdl.alarm and var1.idsite=? and var1.tablename='cfvarmdl' and " +
        			" var1.tableid=v1.idvarmdl and var1.languagecode=? and var2.idsite=? and var2.tablename='cfvarmdl' and " +
        			" var2.tableid=v2.idvarmdl and var2.languagecode=? and cfdevmdl.idsite=? and v1.idsite=? and v2.idsite=?";
        	
            //x allarme non selezionato:
            String sql_2 = "select " +
                    "ac_master_mdl.index, " +
                    "ac_master_mdl.vcode, " +
                    "ac_master_mdl.alarm, " +
                    "ac_master_mdl.min, " +
                    "ac_master_mdl.max, " +
                    "ac_master_mdl.def, " +
                    "var1.description as vdesc, " +
                    "'' as adesc" +
                    " from ac_master_mdl, cftableext as var1, cfdevmdl, cfvarmdl as v1 where " +
                    " cfdevmdl.iddevmdl=? and cfdevmdl.code=ac_master_mdl.code and cfdevmdl.idsite=? and " +
                    " ac_master_mdl.alarm='-1' and v1.iddevmdl=? and v1.code=ac_master_mdl.vcode and " +
                    " var1.idsite=? and var1.tablename='cfvarmdl' and var1.tableid=v1.idvarmdl and var1.languagecode=? " +
                    " and v1.idsite=? order by vdesc, adesc";
            
            String sql = sql_1 + " UNION " + sql_2;
            
            Object[] param = new Object[16];
        	
            //params x la 1ma parte della query "sql_1":
            param[0] = new Integer(iddevmdl);
        	param[1] = new Integer(iddevmdl);
        	param[2] = new Integer(iddevmdl);
        	param[3] = new Integer(idsite);
        	param[4] = lang;
            param[5] = new Integer(idsite);
        	param[6] = lang;
        	param[7] = new Integer(idsite);
        	param[8] = new Integer(idsite);
        	param[9] = new Integer(idsite);
            //nuovi params x la 2nda parte della query "sql_2":
            param[10] = new Integer(iddevmdl);
            param[11] = new Integer(idsite);
            param[12] = new Integer(iddevmdl);
            param[13] = new Integer(idsite);
            param[14] = lang;
            param[15] = new Integer(idsite);
        	
            try
            {
                RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
                
                Record r = null;
                if (rs!=null && rs.size()>0)
                {
                	for (int i = 0; i < rs.size(); i++)
                	{
                		r = rs.get(i);
                		response.append("<mdl id='" + r.get("index")+"'>");
                		response.append("<varcode><![CDATA[" + r.get("vcode")+"]]></varcode>"); //ex idvarmdl var
                		response.append("<alarmcode><![CDATA[" + r.get("alarm")+"]]></alarmcode>"); //ex idvarmdl alrm
                		response.append("<minval><![CDATA[" + r.get("min")+"]]></minval>");
                		response.append("<maxval><![CDATA[" + r.get("max")+"]]></maxval>");
                		response.append("<defval><![CDATA[" + r.get("def")+"]]></defval>");
                        response.append("<vdesc><![CDATA[" + r.get("vdesc")+"]]></vdesc>");
                        response.append("<adesc><![CDATA[" + r.get("adesc")+"]]></adesc>");
                		response.append("</mdl>");
                	}
                }
            }
            catch (Exception e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
                
                response.append("</mdl>");
            }
        	
            // *** x Slaves models ***
            String sql2 = "select ac_slave_mdl.*, " +
            //" v1.idvarmdl as vmdl, " +
            " var1.description as vdesc " +
            " from ac_slave_mdl, cftableext as var1, cfdevmdl, cfvarmdl as v1 where " +
            " cfdevmdl.iddevmdl=? and cfdevmdl.code=ac_slave_mdl.code and v1.iddevmdl=? and v1.code=ac_slave_mdl.vcode and " +
            " var1.idsite=? and var1.tablename='cfvarmdl' and var1.tableid=v1.idvarmdl and var1.languagecode=? " +
            " and cfdevmdl.idsite=? and v1.idsite=? order by vdesc";
    
            Object[] param2 = new Object[6];
            param2[0] = new Integer(iddevmdl);
            param2[1] = new Integer(iddevmdl);
            param2[2] = new Integer(idsite);
            param2[3] = lang;
            param2[4] = new Integer(idsite);
            param2[5] = new Integer(idsite);

            try
            {
                RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null, sql2, param2);
                
                Record r2 = null;
                if (rs2!=null && rs2.size()>0)
                {
                    for (int i = 0; i < rs2.size(); i++)
                    {
                        r2=rs2.get(i);
                        response.append("<mdl2 id='" + r2.get("index")+"'>");
                        response.append("<varcode><![CDATA[" + r2.get("vcode")+"]]></varcode>");
                        response.append("<vdesc><![CDATA[" + r2.get("vdesc")+"]]></vdesc>");
                        response.append("</mdl2>");
                    }
                }
            }
            catch (Exception e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
                
                response.append("</mdl2>");
            }

            // *** x Extra vars models ***
            String sql3 = "select index, varcode, description from ac_extra_vars, cftableext where " +
                " devcode=(select code from cfdevmdl where iddevmdl=? and idsite=?) and tablename='cfvarmdl' and idsite=? and languagecode=? and " +
                " tableid=(select idvarmdl from cfvarmdl where code=varcode and iddevmdl=? and idsite=?) order by index";
            
            Object[] param3 = new Object[] {
            		new Integer(iddevmdl),
            		new Integer(idsite),
            		new Integer(idsite),
            		lang,
            		new Integer(iddevmdl),
            		new Integer(idsite)
            		};
            
            try
            {
                RecordSet rs3 = DatabaseMgr.getInstance().executeQuery(null, sql3, param3);
                
                if (rs3!=null && rs3.size()>0)
                {
                    for (int i = 0; i < rs3.size(); i++)
                    {
                        response.append("<mdl3 id='" + rs3.get(i).get("index").toString() + "'>");
                        response.append("<varcode><![CDATA[" + rs3.get(i).get("varcode") + "]]></varcode>");
                        response.append("<vdesc><![CDATA[" + rs3.get(i).get("description") + "]]></vdesc>");
                        response.append("</mdl3>");
                    }
                }
            }
            catch (Exception e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
                
                response.append("</mdl3>");
            }
            
            // *** x Safety (digital) vars models ***
            String sql4 = "select digvar, vcode, value, description from ac_heartbit_mdl, cftableext where " +
                " tablename='cfvarmdl' and idsite=? and languagecode=? and " +
                " tableid=(select idvarmdl from cfvarmdl where code=vcode and iddevmdl=? and idsite=?) order by code";
            
            Object[] param4 = new Object[] {new Integer(idsite), lang, new Integer(iddevmdl), new Integer(idsite)};
            Float varval = null;
            
            try
            {
                RecordSet rs4 = DatabaseMgr.getInstance().executeQuery(null, sql4, param4);
                
                if (rs4!=null && rs4.size()>0)
                {
                    for (int i = 0; i < rs4.size(); i++)
                    {
                        response.append("<digvar id='" + rs4.get(i).get("digvar").toString() + "'>");
                        response.append("<varcode><![CDATA[" + rs4.get(i).get("vcode").toString() + "]]></varcode>");
                        varval = (Float)rs4.get(i).get("value");
                        response.append("<varvalue><![CDATA[" + varval.doubleValue()/60 + "]]></varvalue>");
                        response.append("<vdesc><![CDATA[" + rs4.get(i).get("description").toString() + "]]></vdesc>");
                        response.append("</digvar>");
                    }
                }
            }
            catch (Exception e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
                
                response.append("</digvar>");
            }
            
        }
        response.append("</response>");
        return response.toString();
    }
}
