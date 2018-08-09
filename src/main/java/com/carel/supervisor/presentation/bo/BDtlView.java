package com.carel.supervisor.presentation.bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.*;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.NoteLog;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.alarms.AlarmList;
import com.carel.supervisor.presentation.bean.ConfigurationGraphBeanList;
import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.bean.ProfileBean;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.comboset.ComboParam;
import com.carel.supervisor.presentation.comboset.ComboParamMap;
import com.carel.supervisor.presentation.comboset.OptionParam;
import com.carel.supervisor.presentation.copydevice.PageImpExp;
import com.carel.supervisor.presentation.devices.ParamDetail;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.refresh.IRefresh;
import com.carel.supervisor.presentation.refresh.RefreshBean;
import com.carel.supervisor.presentation.refresh.RefreshBeanList;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.vscheduler.VSBase;
import com.carel.supervisor.script.EnumerationMgr;
import com.carel.supervisor.script.ExpressionMgr;
import com.carel.supervisor.script.special.Special;

public class BDtlView extends BoMaster
{
	private static final long serialVersionUID = -6217664831427171938L;
	private static final String PACK_SPECIAL = "com.carel.supervisor.script.special.";
    private static final int REFRESH_TIME = 10000;
    public static final String PREX = "dtlst_";
    private int curIdDevMdl = -1;
    private int curIdDevice = -1;
    private static final String EXPORT_PDF_CMD = "exportpdf";

    public BDtlView(String l)
    {
        super(l, REFRESH_TIME);
    }

    @SuppressWarnings("unchecked")
	protected Map initializeRefresh()
    {
        Map map = new HashMap();
        
        //SubTab1:
        RefreshBean[] rb1 = 
            {
                new RefreshBean("TAlarmDd", IRefresh.R_ALARMS, 1,false)
                //2010-9-15, remove by kevin. No use any more
//                new RefreshBean("dtlbuno", IRefresh.R_DEVICEDETAIL, 1),
//                new RefreshBean("", IRefresh.R_FLASHLED, 1)
            };
        
        //SubTab3:
        RefreshBean[] rb3 = 
        {
            new RefreshBean("TA", IRefresh.R_ALARMS, 1),
            new RefreshBean("TalarmCalledOf", IRefresh.R_ALARMSCALL, 2)
        };
        
        RefreshBeanList rbl1 = new RefreshBeanList(REFRESH_TIME, rb1.length); //SubTab1:
        RefreshBeanList rbl3 = new RefreshBeanList(REFRESH_TIME, rb3.length); //SubTab3:
        
        rbl1.setRefreshObj(rb1); //SubTab1:
        rbl3.setRefreshObj(rb3); //SubTab3:
        
        map.put("tab1name", rbl1); //SubTab1:
	    map.put("tab3name", rbl3); //SubTab3:
	
	    return map;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "dtlmain_onload();unlockModUser();");//querysitestatus();");
        p.put("tab2name", "onload_buttons();onload_group();");
        p.put("tab3name", "enableAction(1);enableAction(2);");

        p.put("tab4name", "initialize();");
        p.put("tab5name", "initialize();");
        
        p.put("tab6name", "initialize();g_onload();");
        p.put("tab7name", "setdefault();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        String virtkey = "";
        //determino se ￄ1�7 abilitata la VirtualKeyboard:
        if (VirtualKeyboard.getInstance().isOnScreenKey())
        {
            virtkey = ";keyboard.js;";
        }
        
        Properties p = new Properties();
        
        p.put("tab1name", "dtlview.js;note.js"+virtkey);
        p.put("tab2name", "../jquery/jquery-1.11.1.min.js;showModalDialog.js;dialog_imp_exp.js;dtlview_param.js;../arch/FileDialog.js;note.js"+virtkey);
        
        p.put("tab3name", "dtlview.js"+virtkey);
        
        p.put("tab4name", "graphdialog_support.js;../jquery/jquery-1.11.1.min.js;graphconf.js;../arch/jscolor/jscolor.js;showModalDialog.js;graph.js"+virtkey);
        p.put("tab5name", "graphdialog_support.js;../jquery/jquery-1.11.1.min.js;graphconf.js;../arch/jscolor/jscolor.js;showModalDialog.js;graph.js"+virtkey);
        p.put("tab6name", "graphconf.js");
        
        p.put("tab7name", "note.js"+virtkey);

        return p;
    }

    protected Properties initializeCommitKey()
    {
        Properties p = new Properties();
        p.put("tab1name", "dtlSetVars()");
        p.put("tab2name", "dtlviewSetVars()");

        return p;
    }
	// DOCTYPE STRICT is necessary to have FileDialog correctly functioning
	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab2name", DOCTYPE_STRICT);
		return p;
    } 
    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
        for (;;)
        {
            if (tabName.equalsIgnoreCase("tab2name") ||
                    tabName.equalsIgnoreCase("tab1name"))
            {	// “customTab” help to locate which tab/button was clicked by user in customization device's parameter page  
            	if(prop.containsKey("customTab") && tabName.equalsIgnoreCase("tab2name") ){
            		break;
            	}else{
            		this.execTab1Or2(us, prop, tabName);

                    break;
            	}
                
            }

        	// ENHANCEMENT 20090211 - Merged the sixth tab in the fourth/fifth tabs.
        	if (tabName.equals("tab4name")) {// HACCP
        		// stores on DB the graph layout config
        		if(prop.getProperty("save") != null) {
        			saveGeneralGraphConfiguration(us, prop);
        		}        		
        	}
        	if (tabName.equals("tab5name")) {// Historical
        		// stores on DB the graph layout config
        		if(prop.getProperty("save") != null) {
        			saveGeneralGraphConfiguration(us, prop);
        		}        		
        	}
        		
        	// deprecated
//            if (tabName.equals("tab6name"))
//            {
//                this.saveGraphConfiguration(us, prop);
//
//                break;
//            }

            break;
        }
    }

    private void saveGeneralGraphConfiguration(UserSession us, Properties prop)
        throws Exception
    {
        us.getTransaction().setGraphParameter(prop);
        ConfigurationGraphBeanList graphBeanList = new ConfigurationGraphBeanList();
        
        try
        {
        	graphBeanList.saveCosmetic(us, prop);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }

// ENHANCEMENT 20090224 - Not used any more
//        String cmd = prop.getProperty("cmd");
//        String language = us.getLanguage();
//        LangService l = LangMgr.getInstance().getLangService(language);
//        int idsite = us.getIdSite();



// ENHANCEMENT 20090224 - Not used any more
//        else if ((cmd != null) && cmd.equalsIgnoreCase("propagate"))
//        {
//            String tmp_master = prop.getProperty("master_id");
//            String tmp_slave = prop.getProperty("slave_id");
//            int id_master = (tmp_master != null) ? Integer.parseInt(tmp_master)
//                                                 : (-1);
//            int id_slave = (tmp_slave != null) ? Integer.parseInt(tmp_slave) : (-1);
//
//            DeviceBean master = DeviceListBean.retrieveSingleDeviceById(idsite,
//                    id_master, language);
//            DeviceBean slave = DeviceListBean.retrieveSingleDeviceById(idsite,
//                    id_slave, language);
//            String propag = "";
//
//            try
//            {
//                Propagate.propagateGraphConf(us.getIdSite(), us.getProfile(),
//                    us.getUserName(), us.getLanguage(), master, slave);
//                propag = l.getString("propagate", "propagation_ok");
//                propag = propag.replace("$1", l.getString("propagate", "graph"));
//                propag = propag.replace("$2", master.getDescription());
//                propag = propag.replace("$3", slave.getDescription());
//            }
//            catch (DataBaseException e)
//            {
//                propag = l.getString("propagate", "propagation_ko");
//                propag = propag.replace("$1", l.getString("propagate", "graph"));
//                propag = propag.replace("$2", master.getDescription());
//                propag = propag.replace("$3", slave.getDescription());
//                e.printStackTrace();
//            }
//            
//            us.getCurrentUserTransaction().setProperty("propagated", propag);
//        }
    }

    @SuppressWarnings("unchecked")
	private void execTab1Or2(UserSession us, Properties prop, String tab)
        throws Exception
    {
        String cmd = us.getProperty("cmd");
        String grp = prop.getProperty("current_grp");
        int idDev = -1;

        try
        {
            idDev = Integer.parseInt(us.getProperty("iddev"));
        }
        catch (Exception e)
        {
        }

        if ("export".equals(cmd)){
            if (PageImpExp.expDeviceFile(us, prop)){
    			us.setProperty("export_cmd", PageImpExp.getFileFullName());
            }else{
            	us.setProperty("export_cmd", "ERROR");
            }
        }
        else if ((cmd != null) && cmd.equals("copy_all"))
        {
            String ids_toset = us.getPropertyAndRemove("ids_toset");

            if (ids_toset != null)
            {
                String[] ids_split = ids_toset.split(";");
                int[] ids = new int[ids_split.length];

                for (int i = 0; i < ids.length; i++)
                {
                    ids[i] = Integer.parseInt(ids_split[i]);
                }

                
                PageImpExp.setDevices(us, ids,prop);
                /*
                if (!PageImpExp.setDevices(us, ids,prop)) //passo iddev sorgente, e array ids dove copiare
                {
                    us.setProperty("cmd", "export_failed");
                }
                */
            }
        }
        else if ((tab.equalsIgnoreCase("tab1name")) || (tab.equalsIgnoreCase("tab2name")) || ((cmd != null) && cmd.equals("set")))
        {
            // Tabella RW rimane aperta
            try {
                String sOt = us.getPropertyAndRemove("openwt");
                if(sOt != null)
                    us.getCurrentUserTransaction().setProperty("openwt","Y");
                else
                    us.getCurrentUserTransaction().setProperty("openwt","N");
            }
            catch(Exception e){}
            
            this.setValue(us, prop, idDev);
            
            /*
             * Intevento tecnico per set parametri nel PC-GATE
             */
            if (idDev != 0)
            {
                GroupListBean group = us.getGroup();
                DeviceStructureList devices = group.getDeviceStructureList();
                DeviceStructure device = devices.get(idDev);

                if ((device != null) && (device.getIdDevMdl() == 9))
                {
                    String sql = "select idvariable from cfvariable where iddevice=? and idvarmdl=?";

                    try
                    {
                        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,
                                sql,
                                new Object[]
                                {
                                    new Integer(idDev), new Integer(2290)
                                });

                        if ((rs != null) && (rs.size() > 0))
                        {
                            Record r = rs.get(0);

                            if (r != null)
                            {
                                int idVar = ((Integer) r.get("idvariable")).intValue();

                                if (idVar != 0)
                                {
                                	SetContext setContext2 = new SetContext();
                                	setContext2.addVariable(idVar, 5);
                                	setContext2.setCallback(new OnLineCallBack());
                                	setContext2.setUser(us.getUserName());
                                	setContext2.setLanguagecode(us.getLanguage());
                                    SetDequeuerMgr.getInstance().add(setContext2, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
                                }
                            }
                        }
                    }
                    catch (Exception e)
                    {
                    }
                }
            }

        	if("resetbtt".equals(prop.getProperty("resetbttcmd")))
        	{
	        	// pulsante reset allarmi
            	try{
                	SetContext setContext = new SetContext();
                	setContext.setLanguagecode(us.getLanguage());
    	    		setContext.setUser(us.getUserName());
            		setContext.setCallback(new ResetAlarmCallBack());
	            	String sql = "select * from cfvariable where iddevice = ? and type=4 and readwrite='3'";
	            	RecordSet r = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(us.getProperty("iddev"))});
	            	for(int k = 0 ; k<r.size(); k++)
	            	{
	            		SetWrp wrp = setContext.addVariable(((Integer)r.get(k).get("idvariable")).intValue(), 0F);
	            		wrp.setCheckChangeValue(false);
	            	}
	            	SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
            	}catch(Exception e){}
        	}
        	
        	// End

            /*
             * Sleep 3 sec. per far si che i valori vengano aggiornati
             * dalla DLL carel.
             */
            try
            {
                Thread.sleep(3000);
            }
            catch (Exception e)
            {
            }

            if(grp!=null)
            {
            	us.getCurrentUserTransaction().setProperty("current_grp", grp);
            }
        }
    }
    
    public void setValue(UserSession us,Properties prop,int idDev)
    	throws Exception
    {
    	// Caso di SET delle variabili
        int idvar = -1;
        String key = "";
        String val = "";
        Iterator i = prop.keySet().iterator();
        List listVar = new ArrayList();
        List listValue = new ArrayList();
        String listIdVar = "";
        int nparams = 0;
        String noteInfo = us.getPropertyAndRemove("noteInfo");
        
        try{
        	getCombo(prop,us.getUserName(),noteInfo,us.getLanguage());
        }
        catch(Exception e){}
    
        while (i.hasNext())
        {
            key = (String) i.next();

            if (key.startsWith(PREX))
            {
                val = prop.getProperty(key);
                key = key.substring(PREX.length());

                //gestione combobox non valorizzata:
                if ((!"".equals(val)) && (!"***".equals(val)))
                {
                	//estraggo idVariables x la query di ordinamento parametri:
                	if( !listIdVar.isEmpty() )
                		listIdVar += ",";
                	listIdVar += key;
                	nparams++;
                }
            }
        }
        
        //continuo solo se ho almeno un parametro da settare:
        if (nparams > 0)
        {
            if (nparams > 1)
            {
            	//ordino parametri da settare sul campo in base alla loro priority:
	            String sql_pri = "select idvariable,priority from cfvariable where idvariable in ("+listIdVar+") order by priority, idvargroup";
	            
	            String idvr = "";
	            String ky = "";
	            Record rec = null;
	            
	            try
	        	{
	            	RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql_pri.toString(), null);
		        	
	            	for (int j = 0; j < recordset.size(); j++)
		        	{
		        		rec = recordset.get(j);
		        		idvr = rec.get("idvariable").toString();
		        		
		        		//compongo la chiave come da tabella parametri:
		        		ky = PREX + idvr;
		        		
		        		if ((prop.containsKey(ky)) && (!"".equals(prop.getProperty(ky).toString())))
		        		{
		        			try
		        			{
								listVar.add(new Integer(idvr));
								listValue.add(prop.getProperty(ky));
							}
		        			catch (Exception e1)
		        			{
		        				LoggerMgr.getLogger(this.getClass()).error(e1);
							}
		        		}
		        	}
	        	}
	        	catch(Exception e)
	        	{
	        		LoggerMgr.getLogger(this.getClass()).error(e);
	        	}
            }
            else //caso: 1 solo parametro (es. pulsante di cmd)
            {
            	//non effettuo query sul db x 1 solo param:
            	try
                {
	            	key = PREX + listIdVar;
	            	val = prop.getProperty(key);
	            	
	            	//tengo solo valori validi
	            	if ((!"".equals(val)) && (!"***".equals(val)))
                    {
                            idvar = Integer.parseInt(listIdVar);
                            listVar.add(new Integer(idvar));
                            listValue.add(val);
                    }
                }
                catch (Exception e2)
                {
                	LoggerMgr.getLogger(this.getClass()).error(e2);
                }
            }
        }
        
        int[] idVariable = new int[listVar.size()];

        for (int j = 0; j < listVar.size(); j++)
        {
            idVariable[j] = ((Integer) listVar.get(j)).intValue();
        }

        String[] values = (String[]) listValue.toArray(new String[listValue.size()]);

        if (values.length > 0)
        {
        	// Se 1 sola variabile allora potrebbe essere un pulsante
            if (values.length == 1)
            {
            	SetContext setContext = new SetContext();
        		setContext.setUser(us.getUserName());
        		setContext.setCallback(new OnLineCallBack());
        		setContext.setLanguagecode(us.getLanguage());
                setContext.setNote(noteInfo);

            	int cmdcheck = 0;
            	try
            	{
	            	String sql = "select count(1) from cfvariable where idvariable=? and buttonpath is not null";
	            	RecordSet r = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(idVariable[0])});
	            	cmdcheck = ((Integer)r.get(0).get(0)).intValue();		            	
            	}
            	catch(Exception e)
            	{
            	}
            	
            	SetWrp wrp = setContext.addVariable(idVariable[0], Float.parseFloat(values[0]));
            	if (cmdcheck != 0)
            	{
            		wrp.setCheckChangeValue(false);
            		setContext.setCallback(new CmdCallBack());
            	}
            	
                SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
            }
            else
            {
            	SetParam setParam = new SetParam(us.getUserName(), us.getLanguage(), noteInfo, idVariable, values);
            	setParam.start();
            }
         // set note for note tap in  device page 
            String strValueNote = ProductInfoMgr.getInstance().getProductInfo().get("value_note");
			boolean bValueNote = strValueNote != null && (strValueNote.equalsIgnoreCase("yes") || strValueNote.equalsIgnoreCase("true"));
			if(bValueNote ){
            	 for(int x =0;x<values.length;x++ ){
 		    		Timestamp ts = new Timestamp(System.currentTimeMillis());
 					String sql2 = "select ext.description from cftableext ext , cfvariable var where ext.tablename = 'cfvariable' and " +
 							" tableid = "+ idVariable[x]  + " and ext.idsite=1 and ext.languagecode = '"+us.getLanguage()+"'";
 					Record r = null;
 					try {
 						r = DatabaseMgr.getInstance().executeQuery(null, sql2.toString(), null).get(0);
 					} catch (DataBaseException e) {
 						e.printStackTrace();
 					}
 					String varDesc = (String)r.get(0);
 				
 					NoteLog noteLog4Device = new NoteLog();
 					noteLog4Device.setLastTime(ts);
 		            noteLog4Device.setStartTime(ts);
 		            noteLog4Device.setUserNote(us.getUserName());
 		            if (noteInfo != null)
 		            {
 		                noteLog4Device.setNote(noteInfo+" - "+varDesc+" -> "+values[x]  );
 		            }
 		            else
 		            {
 		                noteLog4Device.setNote(varDesc+" -> "+values[x]);
 		            }
 		            noteLog4Device.setTableName("cfdevice");
 		            noteLog4Device.setTableId(idDev);
 		            try {
 		            	noteLog4Device.save(1);
 					} catch (Exception e) {
 						e.printStackTrace();
 					}
 	            }
            }
        }
        
        
    }
    
    //prendo i dati del post delle combo box
    private void getCombo(Properties prop,String user,String note,String language) throws DataBaseException 
    {
        String dev=prop.getProperty("iddev");
        RecordSet record;
        
//        String comboBoxes = prop.getProperty("conf_type");
//        if(comboBoxes!=null)
//        {
//            if(comboBoxes.length()>0)
//            {
//                record=confValvole(comboBoxes,dev);
//                setVar(record,prop,user);
//            }
//        }
//        
//        comboBoxes = prop.getProperty("prob_type");
//        
//        if(comboBoxes!=null)
//        {
//            if(comboBoxes.length()>0)
//            {
//                record=confProb(comboBoxes,dev);
//                setVar(record,prop,user);
//            }
//        }
//        
//        comboBoxes = prop.getProperty("unit_type");
//        
//        if(comboBoxes!=null)
//        {
//            if(comboBoxes.length()>0)
//            {
//                record=confUnit(comboBoxes,dev);
//                setVar(record,prop,user);
//            }
//        }
//        
//        comboBoxes = prop.getProperty("ref_type");
//        
//        if(comboBoxes!=null)
//        {
//            if(comboBoxes.length()>0)
//            {
//                record=confRef(comboBoxes,dev);
//                setVar(record,prop,user);
//            }
//        }
        
        Iterator it = prop.entrySet().iterator();
        while(it.hasNext())
        {
        	Entry e = (Entry)it.next();
        	String key = (String)e.getKey();
        	String value = (String)e.getValue();
        	if(key.endsWith("_combo") && !"".equals(value))
        	{
        		String[] values = value.split("-");
        		
        		int idcombo = Integer.parseInt(values[0]);
        		int idoption = Integer.parseInt(values[1]);
        		
        		record=combo(values[0],values[1],dev);
                setVar(record,prop,user,note);
                
                
             // set note for note tap in  device page 
                String strValueNote = ProductInfoMgr.getInstance().getProductInfo().get("value_note");
    			boolean bValueNote = strValueNote != null && (strValueNote.equalsIgnoreCase("yes") || strValueNote.equalsIgnoreCase("true"));
    			if(bValueNote ){
    				Timestamp ts = new Timestamp(System.currentTimeMillis());
    				String sql2 = "select ext.description from cftableext ext , cfcombo combo where ext.tablename = 'cfcombo' " +
    						" and tableid = "+ idcombo + " and ext.idsite=1 and ext.languagecode = '"+language+"'";
    				Record r = null;
    				r = DatabaseMgr.getInstance().executeQuery(null, sql2.toString(), null).get(0);
    				String comboDesc = (String)r.get(0);
    				
    				sql2 = "select ext.description from cftableext ext , cfoption option where ext.tablename = 'cfoption' " +
    						" and tableid = "+ idoption  + " and ext.idsite=1 and ext.languagecode = '"+language+"'" ;
    				r = DatabaseMgr.getInstance().executeQuery(null, sql2.toString(), null).get(0);
    				String optionDesc = (String)r.get(0);
    				
    				NoteLog noteLog4Device = new NoteLog();
    				noteLog4Device.setLastTime(ts);
    	            noteLog4Device.setStartTime(ts);
    	            noteLog4Device.setUserNote(user);
    	            if (note != null)
    	            {
    	                noteLog4Device.setNote(note +" - "+comboDesc+" -> "+optionDesc  );
    	            }
    	            else
    	            {
    	                noteLog4Device.setNote(comboDesc+" -> "+optionDesc);
    	            }
    	            noteLog4Device.setTableName("cfdevice");
    	            int idDev = Integer.parseInt(dev);
    	            noteLog4Device.setTableId(idDev);
                	try {
    					noteLog4Device.save(1);
    				} catch (Exception e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    			}
        		
        	}
        }
    }
    
    /**
     * Metodi per caricare gli ID e i valori delle variabili da modificare
     */ 
//    private RecordSet confValvole(String conf_type, String dev) throws DataBaseException
//    {
//        String sql="select cfvariable.idvariable, wvcfconfvar.value from cfvariable, wvcfconf, wvcfconfvar where " +
//                   "wvcfconf.idconf=? and " +
//                   "wvcfconf.idconf=wvcfconfvar.idconfvar and " +
//                   "cfvariable.idvarmdl=wvcfconfvar.idvarmdl and " +
//                   "cfvariable.iddevice=?";
//            
//        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(conf_type), new Integer(dev)});
//        return rs;     
//    }
//    
//    private RecordSet confProb(String prob_type, String dev) throws DataBaseException
//    {
//        String sql="select cfvariable.idvariable, wvcfprobvar.value from cfvariable, wvcfprobtype, wvcfprobvar where " +
//                   "wvcfprobtype.idprobtype=? and " +
//                   "wvcfprobtype.idprobtype=wvcfprobvar.idprobvar and " +
//                   "cfvariable.idvarmdl=wvcfprobvar.idvarmdl and " +
//                   "cfvariable.iddevice=?";
//            
//        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(prob_type), new Integer(dev)});
//        return rs;     
//    }
//    
//    private RecordSet confUnit(String unit_type, String dev) throws DataBaseException
//    {
//        String sql="select cfvariable.idvariable, wvcfunitvar.value from cfvariable, wvcfunittype, wvcfunitvar where " +
//                   "wvcfunittype.idunittype=? and " +
//                   "wvcfunittype.idunittype=wvcfunitvar.idunitvar and " +
//                   "cfvariable.idvarmdl=wvcfunitvar.idvarmdl and " +
//                   "cfvariable.iddevice=?";
//            
//        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(unit_type), new Integer(dev)});
//        return rs;     
//    }
//    
//    private RecordSet confRef(String ref_type, String dev) throws DataBaseException
//    {
//        String sql="select cfvariable.idvariable, wvcfreftype.value from cfvariable, wvcfreftype where " +
//                   "wvcfreftype.idreftype=? and " +
//                   "cfvariable.idvarmdl=wvcfreftype.idvarmdl and " +
//                   "cfvariable.iddevice=?";
//            
//        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(ref_type), new Integer(dev)});
//        return rs;     
//    }
    private RecordSet combo(String idcombo,String idoption, String dev) throws DataBaseException
    {
        /*String sql="select cfvariable.idvariable,wvcfcombooptionvar.value "+
				"from wvcfcombooptionvar "+
				"inner join cfvariable on cfvariable.idvarmdl=wvcfcombooptionvar.idvarmdl and cfvariable.iddevice=? and cfvariable.idhsvariable is not null "+
				"where wvcfcombooptionvar.idcombo=? and wvcfcombooptionvar.idoption=?";*/
        
        String sql="select cfvariable.idvariable, cfcomboset.value "+
        		"from cfcomboset "+
        		"inner join cfvariable on cfvariable.idvarmdl=cfcomboset.idvarmdl and cfvariable.iddevice=? and cfvariable.idhsvariable is not NULL "+
        		"where cfcomboset.idcombo=? and cfcomboset.idoption=?";
            
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(dev), new Integer(idcombo),new Integer(idoption)});
        return rs;     
    }
    /**
     * Modifica delle variabili (da combo box): Wizard Valves
     */
    private void setVar(RecordSet dati,Properties prop,String user,String note)
    {
        int idDev=(new Integer(prop.getProperty("iddev"))).intValue();
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
        int[] idVariables=new int[dati.size()];
        String[] values=new String[dati.size()];
        
        for(int i=0; i<dati.size(); i++)
        {
            idVariables[i]=((Integer)dati.get(i).get("idvariable")).intValue();
            values[i]=dati.get(i).get("value").toString();
        }
        
        setContext.addVariable(idVariables,values);    
        setContext.setUser(user);
        setContext.setCallback(new OnLineCallBack());
        setContext.setNote(note);
        
        SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
    }
    
    public void loadFilter(int idDevice, int idDevMdl, String lang)
    {
        // Intervento per cambio device da combo interna
        boolean reload = false;

        if (curIdDevMdl != idDevMdl)
        {
            this.curIdDevMdl = idDevMdl;
            reload = true;
        }
        else if(curIdDevice != idDevice)
        {
            this.curIdDevice = idDevice;
            reload = true;
        }

        // Expression
        if (reload || (this.expression == null))
        {
            this.expression = new ExpressionMgr(idDevice, idDevMdl);
        }

      

        // Special
        String sClass = PACK_SPECIAL + "S" + idDevMdl;

        try
        {
            this.special = (Special) Class.forName(sClass).newInstance();
        }
        catch (Exception e)
        {
            this.special = new Special();
        }
    }

    public String executeDataAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
        StringBuffer xmlresponse = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<response>");
        if (tabName.equalsIgnoreCase("tab5name"))
        {
        	// History Graph
        	boolean ris = false;
        	String cmd = prop.getProperty("cmd");
        	
        	if(cmd != null && cmd.equalsIgnoreCase("add_block"))
        	{
        		 ris = ConfigurationGraphBeanList.storeGraphBlock(us,prop.getProperty("block_name"),prop.getProperty("block_data"),false);
        		 if(ris)
             		xmlresponse.append( ConfigurationGraphBeanList.retrieveGraphBlocks(us,prop.getProperty("block_name")));
        	}
        	else if(cmd != null && cmd.equalsIgnoreCase("del_block"))
        	{
        		ris = ConfigurationGraphBeanList.removeGraphBlock(us, prop.getProperty("block_name"), false);
        		if(ris)
             		xmlresponse.append( ConfigurationGraphBeanList.retrieveGraphBlocks(us,null));
        	}
        	else if(cmd != null && cmd.equalsIgnoreCase("get_blocks"))
        	{
        		xmlresponse.append( ConfigurationGraphBeanList.retrieveGraphBlocks(us,null));
        	}
        }
        else if (tabName.equalsIgnoreCase("tab1name")) {
        	String cmd = prop.getProperty("cmd");
	        if( "timesync".equals(cmd) ) {
	        	int idDevice = Integer.parseInt(us.getProperty("iddev"));
	        	String codeHour = prop.getProperty("codehour");
	        	String codeMinute = prop.getProperty("codeminute");
	        	String codeConfirmation = prop.getProperty("codeconfirmation");
	    		xmlresponse.append("<timesync>");
	        	xmlresponse.append(timeSync(idDevice, codeHour, codeMinute, codeConfirmation, us));
	    		xmlresponse.append("</timesync>");
	        }
        }
        else if (tabName.equalsIgnoreCase("tab2name"))
        {
            String cmd = prop.getProperty("cmd");
    		if ("load_params".equalsIgnoreCase(cmd))
            {
                int id_group = Integer.parseInt(prop.getProperty("group_id"));
                ComboParamMap c = (ComboParamMap) us.getCurrentUserTransaction().getObjProperty("comboparam");
                loadParams(us, prop, id_group, xmlresponse, false, c);
                us.getCurrentUserTransaction().setProperty("current_grp",
                    String.valueOf(id_group));
            }
            else if ("load_from_file".equalsIgnoreCase(cmd))
            {
            	ComboParamMap c = (ComboParamMap) us.getCurrentUserTransaction().getObjProperty("comboparam");
                loadParams(us,prop, 0, xmlresponse, true,c);
                us.getCurrentUserTransaction().setProperty("current_grp",
                    String.valueOf(0));
            }else if ("getDeviceTemplateList".equalsIgnoreCase(cmd)){
            	int idsite = us.getIdSite();
            	String s_iddev = us.getProperty("iddev");
            	int iddev = Integer.parseInt(s_iddev);
            	String combo = PageImpExp.getComboConfigImport(idsite, iddev);
            	combo="<response><sel><![CDATA["+combo+"]]></sel></response>";
            	return combo;
            }
            /*
            else if(cmd.equals("tooltipquery"))
            {
            	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, 
            					"select longdescr from cftableext where tablename=? and tableid=? and languagecode=?",
            					new Object[]{"cfvariable", new Integer(prop.getProperty("idvar")), us.getLanguage()});
            	if(rs!=null && rs.size()>0)
            		xmlresponse.append("<![CDATA["+rs.get(0).get("longdescr")+"]]>");
            	else
            		xmlresponse.append("<![CDATA[BAD]]>");
            	
            }
            */
        }
        else if(tabName.equalsIgnoreCase("tab3name")) {
	       	String cmd = prop.getProperty("cmd");
	       	if("print_allAlarms".equalsIgnoreCase(cmd )){
	       		String language = us.getLanguage();
		   		String sessionName="grpview";
		   		LangService multiLanguage = LangMgr.getInstance().getLangService(language);
		   		String title = multiLanguage.getString(sessionName,"alarms");
		   		AlarmList alarmList = new AlarmList(title);
		   		alarmList.setLink(false);
		   		alarmList.loadAllFromDataBase(us);
		   		xmlresponse.append(alarmList.getAllAlarms4Print(language,multiLanguage,us));
	       	}
	   		
        }
        String group_id = prop.getProperty("group_id");
        if(group_id != null)
        {
	        try {
	        	int id = Integer.valueOf(group_id);
	        	if(id<0)
	        	{
	        		// le combo sono abilitate solo se "Param costr." oppure "Param. serv" sono in RW:
	                if (us.getVariableFilter()<ProfileBean.FILTER_SERVICES)
	                {
	                    xmlresponse.append("<view id='disabled'></view>");
	                }
	                else
	                {
	                    xmlresponse.append("<view id=''></view>");
	                }
		            //getConfName(xmlresponse, prop, us);
		            getCombo(xmlresponse,prop,us,-id);
	        	}
	        }
	        catch(Exception e)
	        {
	        	LoggerMgr.getLogger(this.getClass()).error(e);
	        }
        }
        
        xmlresponse.append("</response>");

        return xmlresponse.toString();
    }

    @SuppressWarnings("unchecked")
	StringBuffer loadParams(UserSession us,Properties prop, int id_group,
        StringBuffer xmlresponse, boolean setvalues,ComboParamMap c) throws Exception
    {
        Map values = new HashMap();
        //String lang = us.getLanguage();
        if (setvalues)
        {
        	String filename = prop.getProperty("filename");
        	if("true".equalsIgnoreCase(prop.getProperty("islocal"))){
        		values=getValueFromDefaultPathFile(filename);
        	}else{
        		values = getValueFromFile(filename);
        	}
        }
        
    	xmlresponse.append("<grp id='" + id_group + "'></grp>\n");

        ParamDetail paramDetail = new ParamDetail(us);

        try
        {
            paramDetail.loadVarphyToWrite();
        }
        catch (Exception e)
        {
        }
        
        
        VarphyBean[] varBeanList = paramDetail.getListVarWrite();
        VarphyBean var = null;
        int grp_code = -1;
        String cur_val = null;
        String min = null;
        String max = null;
        int pk_id = -1;

        //ciclo i parametri e creo xml con le info per costruire la tabella
        for (int i = 0; i < paramDetail.getNumWrite(); i++)
        {
            var = varBeanList[i];
            grp_code = var.getGrpCode();

            
            //se gruppo = 0 mostro tutti parametri
            if ((id_group == 0) || (grp_code == id_group)) 
            {
                try
                {
                    cur_val = ControllerMgr.getInstance().getFromField(var)
                                           .getFormattedValue();
                }
                catch (Exception e)
                {
                    cur_val = "***";
                }
               
             // label management    Bug 10401 - The variable's value in Parameters page is diffrent from Main page 
                String label = ""; 
                if (!cur_val.equals("***"))
                {
                	String language = us.getLanguage();
                	try
                	{
                		label = EnumerationMgr.getInstance().getEnumCode(var.getIdMdl(), Float.parseFloat(cur_val), language);
                	}
                	catch(NumberFormatException e)
                	{
                		cur_val = cur_val.replace(",", "");
                		label = EnumerationMgr.getInstance().getEnumCode(var.getIdMdl(), Float.parseFloat(cur_val), language);
                	}
                }
                if (!"".equals(label))
                {
                	cur_val = label;
                }
                // label management
                //idvar
                xmlresponse.append("<var id='" + var.getId() + "'>\n");

                //valore corrente
                xmlresponse.append("<cur_val><![CDATA[" + cur_val + "]]></cur_val>\n");

                //tipo variabile
                xmlresponse.append("<type><![CDATA[" + var.getType() + "]]></type>\n");

                //unitￄ1�7 di misura
                xmlresponse.append("<um><![CDATA[" + VSBase.xmlEscape(var.getMeasureUnit()) + "]]></um>\n");

                //codice variabile
                xmlresponse.append("<code><![CDATA[" + VSBase.xmlEscape(var.getShortDesc()) + "]]></code>\n");

                //descrizione
                xmlresponse.append("<description><![CDATA[" + VSBase.xmlEscape(var.getShortDescription()) +"]]></description>\n");

                // minimo valore ammesso nell input 
                min = var.getMinValue();
                if (min!=null && min.contains("pk"))
                {
                	pk_id = Integer.parseInt(min.substring(2,min.length()));
                	min = ControllerMgr.getInstance().getFromField(pk_id).getFormattedValue();
                }
                xmlresponse.append("<min><![CDATA[" + min + "]]></min>\n");

                // massimo valore ammesso come input
                max = var.getMaxValue();
                if (max!=null && max.contains("pk"))
                {
                	pk_id = Integer.parseInt(max.substring(2,max.length()));
                	max = ControllerMgr.getInstance().getFromField(pk_id).getFormattedValue();
                }
                xmlresponse.append("<max><![CDATA[" + max + "]]></max>\n");
                
                if ((setvalues) && (values.containsKey(var.getCodeVar())))
                {
                	xmlresponse.append("<loaded><![CDATA[" + values.get(var.getCodeVar()) + "]]></loaded>\n");
                }
                else
                {
                	xmlresponse.append("<loaded><![CDATA[]]></loaded>\n");
                }
            	xmlresponse.append("<disab><![CDATA["+paramDetail.getListVarDisab()[i]+"]]></disab>\n");
            	xmlresponse.append("<longdescr><![CDATA[" + VSBase.xmlEscape(var.getLongDescription()) +"]]></longdescr>\n");
            	xmlresponse.append("<decimals><![CDATA["+var.getDecimal()+"]]></decimals>\n");
                
            	/////////////   SET PARAMETRI TRAMITE COMBO   /////////
            	if (c.containComboForVar(var.getId()))
            	{
            		ComboParam combo = c.getComboForVar(var.getId());
            		if(combo.hasOption())
            		{
	            		xmlresponse.append("<combo>");
	            		OptionParam opt = null;
	            		for (int j=0;j<combo.getOptionNumber();j++)
	            		{
	            			opt = combo.getOption(j);
	            			xmlresponse.append("<option>");
	            			xmlresponse.append("<c_value><![CDATA["+opt.getValue()+"]]></c_value>");
	            			xmlresponse.append("<c_descr><![CDATA[" + VSBase.xmlEscape(opt.getDesc()) + "]]></c_descr>");
	            			xmlresponse.append("</option>");
	            		}
	            		xmlresponse.append("</combo>");
            		}
            	}
            	///////////// FINE  ////////// 
            	
                xmlresponse.append("</var>\n");
            }
        }

        return xmlresponse;
    }

    //verifica presenza di configurazione e passaggio dei dati: nome, descrizione e longdes
//    private StringBuffer getConfName(StringBuffer xmlresponse, Properties prop, UserSession us) 
//        throws DataBaseException
//    {
//        LangService lan = LangMgr.getInstance().getLangService(us.getLanguage());
//        String sql="select iddevmdl from cfdevice where iddevice=?";
//        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(prop.getProperty("iddev"))});
//        
//        int dev=(new Integer(prop.getProperty("iddev"))).intValue();
//        
//        String s=rs.get(0).get("iddevmdl").toString();
//        
//        String sql_conf = "select confname, idconf from wvcfconf where iddevmdl=?";
//        RecordSet rs_conf = DatabaseMgr.getInstance().executeQuery(null,sql_conf,new Object[]{new Integer(s)});
//        
//        String[] checked=new String[4];
//        checked=check(dev,s);
//        
//        for(int i=0; i<rs_conf.size(); i++)
//        {
//            if(i==0)
//                xmlresponse.append("\t<conf id='"+((Integer)rs_conf.get(i).get("idconf")).intValue()+"'><![CDATA["+UtilBean.trim(rs_conf.get(i).get("confname"))+"]]><![CDATA["+checked[0]+"]]><![CDATA["+lan.getString("dtlview","w_conf")+"]]><![CDATA["+lan.getString("dtlview","w_longdes12")+"]]></conf>\n");
//            else
//                xmlresponse.append("\t<conf id='"+((Integer)rs_conf.get(i).get("idconf")).intValue()+"'><![CDATA["+UtilBean.trim(rs_conf.get(i).get("confname"))+"]]><![CDATA["+checked[0]+"]]></conf>\n");
//        }
//        String sql_prob = "select probtypename, idprobtype from wvcfprobtype where iddevmdl=?";
//        RecordSet rs_prob = DatabaseMgr.getInstance().executeQuery(null,sql_prob,new Object[]{new Integer(s)});
//        
//        for(int i=0; i<rs_prob.size(); i++)
//        {
//            if(i==0)
//                xmlresponse.append("\t<prob id='"+((Integer)rs_prob.get(i).get("idprobtype")).intValue()+"'><![CDATA["+UtilBean.trim(rs_prob.get(i).get("probtypename"))+"]]><![CDATA["+checked[1]+"]]><![CDATA["+lan.getString("dtlview","w_prob")+"]]><![CDATA["+lan.getString("dtlview","w_longdes12")+"]]></prob>\n");
//            else
//                xmlresponse.append("\t<prob id='"+((Integer)rs_prob.get(i).get("idprobtype")).intValue()+"'><![CDATA["+UtilBean.trim(rs_prob.get(i).get("probtypename"))+"]]><![CDATA["+checked[1]+"]]></prob>\n");
//        }
//        String sql_ref = "select reftypename, idreftype from wvcfreftype where iddevmdl=?";
//        RecordSet rs_ref = DatabaseMgr.getInstance().executeQuery(null,sql_ref,new Object[]{new Integer(s)});
//        
//        for(int i=0; i<rs_ref.size(); i++)
//        {
//            if(i==0)
//                xmlresponse.append("\t<ref id='"+((Integer)rs_ref.get(i).get("idreftype")).intValue()+"'><![CDATA["+UtilBean.trim(rs_ref.get(i).get("reftypename"))+"]]><![CDATA["+checked[3]+"]]><![CDATA["+lan.getString("dtlview","w_ref")+"]]><![CDATA["+lan.getString("dtlview","w_longdes12")+"]]></ref>\n");
//            else
//                xmlresponse.append("\t<ref id='"+((Integer)rs_ref.get(i).get("idreftype")).intValue()+"'><![CDATA["+UtilBean.trim(rs_ref.get(i).get("reftypename"))+"]]><![CDATA["+checked[3]+"]]></ref>\n");
//        }      
//        String sql_unit = "select unittypename, idunittype from wvcfunittype where iddevmdl=?";
//        RecordSet rs_unit = DatabaseMgr.getInstance().executeQuery(null,sql_unit,new Object[]{new Integer(s)});
//        
//        for(int i=0; i<rs_unit.size(); i++)
//        {
//            if(i==0)
//                xmlresponse.append("\t<unit id='"+((Integer)rs_unit.get(i).get("idunittype")).intValue()+"'><![CDATA["+UtilBean.trim(rs_unit.get(i).get("unittypename"))+"]]><![CDATA["+checked[2]+"]]><![CDATA["+lan.getString("dtlview","w_unit")+"]]><![CDATA["+lan.getString("dtlview","w_longdes12")+"]]></unit>\n");
//            else
//                xmlresponse.append("\t<unit id='"+((Integer)rs_unit.get(i).get("idunittype")).intValue()+"'><![CDATA["+UtilBean.trim(rs_unit.get(i).get("unittypename"))+"]]><![CDATA["+checked[2]+"]]></unit>\n");
//        }      
//        
//        return xmlresponse;
//    }    
    private StringBuffer getCombo(StringBuffer xmlresponse, Properties prop, UserSession us,int group_id) 
	    throws DataBaseException
	{
		String language = us.getLanguage();
	    String sql="select iddevmdl from cfdevice where iddevice=?";
	    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(prop.getProperty("iddev"))});
	    String s=rs.get(0).get("iddevmdl").toString();	    
	    
	    /*String sql_conf = "select distinct table1.*,optionext.description as option,'' as optionLong  from "+
				"(select distinct wvcfcombooptionvar.idoption,wvcfcombooptionvar.idcombo,comboext.description as combo "+
				"from wvcfcombooptionvar "+
				"inner join cftext as comboext on comboext.subcode=wvcfcombooptionvar.idcombo and comboext.code='dtlview-combo' and comboext.languagecode=? "+
				"where iddevmdl=? "+
				")as table1 "+
				"inner join cftext as optionext on optionext.code='dtlview-option' and optionext.languagecode=? and optionext.subcode=table1.idoption "+
				"order by idcombo";*/
	    
	    
	    String sql_conf = "select table1.*, cftableext.description as combodescr from " + 
	    "(select distinct idoption, idcombo, description as optiondescr from cfcomboset inner join cftableext on cftableext.idsite=1 and idoption=cftableext.tableid where idvarmdl in (select idvarmdl from cfvarmdl where iddevmdl = ?) and cftableext.tablename='cfoption' and languagecode = ?) as table1 "+
	    "inner join cftableext on cftableext.idsite=1 and cftableext.tableid=table1.idcombo and cftableext.tablename = 'cfcombo' and languagecode = ? " +
	    "inner join cfcombo on cfcombo.idcombo=table1.idcombo and cfcombo.idcombogroup=?"+
	    "order by idcombo,idoption";
	    
	    rs = DatabaseMgr.getInstance().executeQuery(null,sql_conf,new Object[]{new Integer(s),language,language,new Integer(group_id)});
	    int dev=(new Integer(prop.getProperty("iddev"))).intValue();
	    int comboId = -1;
	    String combos = "";
	    int comboSize = 0;
	    for(int i=0;i<rs.size();i++)
	    {
	    	int temp = (Integer)rs.get(i).get("idcombo");
	    	if(comboId == -1 || comboId!=temp)
	    	{
	    		comboId = temp;
	    		combos += comboId+";";
	    		comboSize++;
	    	}
	    }
	    String[] checked=new String[comboSize];
	    checked=check(comboSize,dev,us.getLanguage(),group_id);
	    if(combos.length()>0)
	    	combos = combos.substring(0,combos.length()-1);
	    xmlresponse.append("<combos id='"+combos+"'></combos>");
	    comboId = -1;
	    comboSize = 0;
	    for(int i=0;i<rs.size();i++)
	    {
	    	int temp = (Integer)rs.get(i).get("idcombo");
	    	int idoption = (Integer)rs.get(i).get("idoption");
	    	String combo = ((String)rs.get(i).get("combodescr")).trim();
	    	String option = ((String)rs.get(i).get("optiondescr")).trim();
	    	String optionLong = null;//(String)rs.get(i).get("optionlong");
	    	optionLong = optionLong==null?"":optionLong.trim();
	    	if(comboId == -1)
	    	{
	    		comboId = temp;
	    		xmlresponse.append("\t<c"+comboId+" id='"+idoption+"'><![CDATA["+option+"]]><![CDATA["+checked[comboSize]+"]]><![CDATA["+combo+"]]><![CDATA["+optionLong+"]]></c"+comboId+">\n");
	    	}
	    	else if(comboId != temp)
	    	{
	    		comboSize++;
	    		comboId = temp;
	    		xmlresponse.append("\t<c"+comboId+" id='"+idoption+"'><![CDATA["+option+"]]><![CDATA["+checked[comboSize]+"]]><![CDATA["+combo+"]]><![CDATA["+optionLong+"]]></c"+comboId+">\n");
	    	}
	    	else if(comboId == temp)
	    	{
	    		xmlresponse.append("\t<c"+comboId+" id='"+idoption+"'><![CDATA["+option+"]]><![CDATA["+checked[comboSize]+"]]></c"+comboId+">\n");
	    	}
	    }
	    return xmlresponse;
	}
    /*
     *  Controllo le configurazioni del dispositivo 
     */
//    @SuppressWarnings("unchecked")
//	private String[] check(int dev, String model) 
//        throws DataBaseException
//    {
//        int i=0;
//        double val=0;
//        boolean found=false;
//        String[] valv_name = new String[4];
//        
//
//        //****************************** PROBE *************************
//        
//        String sql = "select idprobtype,probtypename,v.idvariable,value from wvcfprobtype as pt,wvcfprobvar as pv,cfvariable as v "+
//        "where pt.iddevmdl=? and pt.idprobtype=pv.idprobvar and v.iddevice=? and v.idvarmdl=pv.idvarmdl "+
//        "group by pt.idprobtype,pt.probtypename,v.idvariable,pv.value order by idprobtype";
//        
//        Map idProbe_Descr = new HashMap();
//        Integer idProbeType = null;
//        Integer idVariable = null;
//        double variableValue = 0;
//        String desc = "";
//        
//        try
//        {
//            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{model,dev});
//            Record r = null;
//            if(rs != null)
//            {
//                for(int j=0; j<rs.size(); j++)
//                {
//                    r = rs.get(j);
//                    if(r != null)
//                    {
//                        idProbeType = (Integer)r.get("idprobtype");
//                        
//                        desc = (String)idProbe_Descr.get(idProbeType);
//                        if(desc == null)
//                            desc = UtilBean.trim(r.get("probtypename"));
//                        
//                        if(desc != null && !desc.equalsIgnoreCase("X"))
//                        {
//                            idVariable = (Integer)r.get("idvariable");
//                            variableValue = ((Float)r.get("value")).doubleValue();
//                            val = ControllerMgr.getInstance().getFromField(idVariable.intValue()).getCurrentValue();
//                            
//                            if(variableValue == val)
//                                idProbe_Descr.put(idProbeType,desc);
//                            else
//                                idProbe_Descr.put(idProbeType,"X");
//                        }
//                    }
//                }
//            }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//        
//        valv_name[1]= "";
//        Iterator it = idProbe_Descr.values().iterator();
//        while(it.hasNext())
//        {
//            if((desc = (String)it.next()) != null)
//            {
//                if(!desc.equalsIgnoreCase("X"))
//                {
//                    valv_name[1]= desc;
//                    break;
//                }
//            }
//        }
//        
//        //****************************** CONFIGURATION *************************
//        
//        sql = "select pt.idconf,pt.confname,v.idvariable,pv.value from "+
//        "wvcfconf as pt,wvcfconfvar as pv,cfvariable as v where pt.iddevmdl=? and pt.idconf=pv.idconfvar and v.iddevice=? "+
//        "and v.idvarmdl=pv.idvarmdl group by pt.idconf,pt.confname,v.idvariable,pv.value order by pt.idconf";
//        
//        idProbe_Descr = new HashMap();
//        idProbeType = null;
//        idVariable = null;
//        variableValue = 0;
//        desc = "";
//        
//        try
//        {
//            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{model,dev});
//            Record r = null;
//            if(rs != null)
//            {
//                for(int j=0; j<rs.size(); j++)
//                {
//                    r = rs.get(j);
//                    if(r != null)
//                    {
//                        idProbeType = (Integer)r.get("idconf");
//                        
//                        desc = (String)idProbe_Descr.get(idProbeType);
//                        if(desc == null)
//                            desc = UtilBean.trim(r.get("confname"));
//                        
//                        if(desc != null && !desc.equalsIgnoreCase("X"))
//                        {
//                            idVariable = (Integer)r.get("idvariable");
//                            variableValue = ((Float)r.get("value")).doubleValue();
//                            val = ControllerMgr.getInstance().getFromField(idVariable.intValue()).getCurrentValue();
//                            
//                            if(variableValue == val)
//                                idProbe_Descr.put(idProbeType,desc);
//                            else
//                                idProbe_Descr.put(idProbeType,"X");
//                        }
//                    }
//                }
//            }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//        
//        valv_name[0]= "";
//        it = idProbe_Descr.values().iterator();
//        while(it.hasNext())
//        {
//            if((desc = (String)it.next()) != null)
//            {
//                if(!desc.equalsIgnoreCase("X"))
//                {
//                    valv_name[0]= desc;
//                    break;
//                }
//            }
//        }
//        
//        
//        //*****************************UNIT***********************************
//        
//        sql = "select pt.idunittype,pt.unittypename,v.idvariable,pv.value "+
//        "from wvcfunittype as pt,wvcfunitvar as pv,cfvariable as v where pt.iddevmdl=? and "+
//        "pt.idunittype=pv.idunitvar and v.iddevice=? and v.idvarmdl=pv.idvarmdl "+
//        "group by pt.idunittype,pt.unittypename,v.idvariable,pv.value order by pt.idunittype";
//        
//        idProbe_Descr = new HashMap();
//        idProbeType = null;
//        idVariable = null;
//        variableValue = 0;
//        desc = "";
//        
//        try
//        {
//            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{model,dev});
//            Record r = null;
//            if(rs != null)
//            {
//                for(int j=0; j<rs.size(); j++)
//                {
//                    r = rs.get(j);
//                    if(r != null)
//                    {
//                        idProbeType = (Integer)r.get("idunittype");
//                        
//                        desc = (String)idProbe_Descr.get(idProbeType);
//                        if(desc == null)
//                            desc = UtilBean.trim(r.get("unittypename"));
//                        
//                        if(desc != null && !desc.equalsIgnoreCase("X"))
//                        {
//                            idVariable = (Integer)r.get("idvariable");
//                            variableValue = ((Float)r.get("value")).doubleValue();
//                            val = ControllerMgr.getInstance().getFromField(idVariable.intValue()).getCurrentValue();
//                            
//                            if(variableValue == val)
//                                idProbe_Descr.put(idProbeType,desc);
//                            else
//                                idProbe_Descr.put(idProbeType,"X");
//                        }
//                    }
//                }
//            }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//        
//        valv_name[2]= "";
//        it = idProbe_Descr.values().iterator();
//        while(it.hasNext())
//        {
//            if((desc = (String)it.next()) != null)
//            {
//                if(!desc.equalsIgnoreCase("X"))
//                {
//                    valv_name[2]= desc;
//                    break;
//                }
//            }
//        }
//        
//        
//        // *********************************** REFRIGERANT *****************************************
//        
//        String num_ref = "select idreftype from wvcfreftype where iddevmdl=? order by idreftype";
//        RecordSet num_rec = DatabaseMgr.getInstance().executeQuery(null,num_ref,new Object[]{model});
//        found = false;
//        
//        if(num_rec.size()>0)
//        {
//            i=0;
//            val=0;
//            found=false;
//            
//            String sql_ref = "select cfvariable.idvariable from wvcfreftype,cfvariable where "+
//                             "wvcfreftype.idvarmdl=cfvariable.idvarmdl and "+
//                             "(cfvariable.idhsvariable=-1 or cfvariable.idhsvariable is null) and "+
//                             "cfvariable.iddevice=? and wvcfreftype.idreftype=? order by cfvariable.idvarmdl";
//            
//            RecordSet rs_ref = DatabaseMgr.getInstance().executeQuery(null,sql_ref,new Object[]{dev,num_rec.get(0).get("idreftype")});
//            if(rs_ref.size()>0)
//            {
//                while((i<num_rec.size())&&(!found))
//                {
//                    String dati="select value from wvcfreftype where idreftype=? order by idvarmdl"; 
//                    RecordSet rs_dati=DatabaseMgr.getInstance().executeQuery(null,dati,new Object[]{num_rec.get(i).get("idreftype")});
//                    
//                    try 
//                    {
//                        val=ControllerMgr.getInstance().getFromField(((Integer)(rs_ref.get(0).get("idvariable"))).intValue()).getCurrentValue();
//                        if(val==((Float)rs_dati.get(0).get("value")).doubleValue())
//                            found=true;                   
//                    }
//                    catch(Exception e){}
//                    i++;
//                }
//            }
//        }
//        
//        if(found)
//        {
//            String name = "select reftypename from wvcfreftype where iddevmdl=? order by idreftype";
//            RecordSet rs_name = DatabaseMgr.getInstance().executeQuery(null,name,new Object[]{model});  
//            valv_name[3] = (String)(rs_name.get(i-1).get("reftypename"));
//        }
//        else
//            valv_name[3] = "";        
//        
//        return valv_name;
//    }
    /*
     *  Controllo le configurazioni del dispositivo 
     */
    @SuppressWarnings("unchecked")
	private String[] check(int size,int dev,String language,int group_id) 
        throws DataBaseException
    {
    	Map<Integer,Double> valueMap = new HashMap();
        double val=0;
        String[] combo_value = new String[size];
        
        /*String sql = "select wvcfcombooptionvar.idcombo,wvcfcombooptionvar.idoption,wvcfcombooptionvar.value,cfvariable.idvariable,cftext.description as optionstr "+
				"from wvcfcombooptionvar "+
				"inner join cfvariable on cfvariable.idvarmdl=wvcfcombooptionvar.idvarmdl and cfvariable.iddevice=? and cfvariable.idhsvariable is not null "+
				"inner join cftext on cftext.code='dtlview-option' and cftext.languagecode=? and cftext.subcode=wvcfcombooptionvar.idoption "+
				"order by idcombo,idoption ";*/
        
        String sql ="select cfcomboset.idcombo, cfcomboset.idoption, cfcomboset.value, cfvariable.idvariable, cftableext.description as optionstr "+
        "from cfcomboset "+
        "inner join cfvariable on cfvariable.idvarmdl=cfcomboset.idvarmdl and cfvariable.iddevice=? and cfvariable.idhsvariable is not null "+
        "inner join cfcombo on cfcombo.idcombo=cfcomboset.idcombo and cfcombo.idcombogroup=? "+
        "inner join cftableext on cftableext.idsite= 1 and cftableext.tableid=cfcomboset.idoption and cftableext.tablename='cfoption' and cftableext.languagecode=? "+
        "order by idcombo, idoption";

        
        Integer idVariable = null;
        double variableValue = 0;
        String desc = "";
        
        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{dev,group_id,language});
            Record r = null;
            if(rs != null)
            {
            	int comboSize = 0;
            	int comboId = -1;
            	int optionId = -1;
            	boolean needCheck = false;
            	boolean optionMatched = false;
                for(int j=0; j<rs.size(); j++)
                {
                    r = rs.get(j);
                    if(r != null)
                    {
                    	int tempId = (Integer)r.get("idcombo");
                    	int tempOptionId = (Integer)r.get("idoption");
                    	if(comboId == -1 || comboId != tempId)
                    	{
                    		comboSize++;
                    		optionId = -1;
                    		optionMatched = false;
                    	}
                    	needCheck = true;
                    	//don't need to check:
                    	//1. there is already a option matched, will not check following options
                    	//2. any value dismatched for one option, will not check following variables
                    	if(tempId == comboId && optionId != -1  && optionId != tempOptionId && combo_value[comboSize-1] != null)
                    	{
                    		optionMatched = true;
                    		needCheck = false;
                    	}
                    	if(tempId == comboId && optionId != -1  && optionId == tempOptionId && combo_value[comboSize-1] == null)
                    		needCheck = false;
                    	comboId = tempId;
                    	optionId = tempOptionId;
                    	if(needCheck && !optionMatched)
                    	{        
                            idVariable = (Integer)r.get("idvariable");
                            variableValue = ((Float)r.get("value")).doubleValue();
                            val = getValue(valueMap,idVariable);
                            
                            if(variableValue == val)
                            {
                            	desc = (String)r.get("optionstr");
                            	combo_value[comboSize-1] = desc;
                            }
                            else
                            {
                            	combo_value[comboSize-1] = null;
                            }
                    	}
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<size;i++)
        {
        	if(combo_value[i] == null)
        		combo_value[i] = "";
        }
        return combo_value;
    }
    private double getValue(Map valueMap,int idvariable)
    {
    	Integer key = Integer.valueOf(idvariable);
    	if(valueMap.containsKey(key))
    	{
    		return (Double)(valueMap.get(key));
    	}
    	else
    	{
    		try {
				double val = ControllerMgr.getInstance().getFromField(idvariable).getCurrentValue();
				valueMap.put(key, val);
				return val;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
    	}
    }
    //caricamento valori dal file dei parametri del device:
    public Map getValueFromDefaultPathFile(String filename) throws IOException{
      String path = BaseConfig.getCarelPath() + PageImpExp.DIR_EXPORT_FILE;
      return getValueFromFile(path + File.separator + filename);
    }
    @SuppressWarnings("unchecked")
	public Map getValueFromFile(String filename) throws IOException
    {
        Map code_value = new HashMap();

        File file = new File(filename);

        FileReader file_read = new FileReader(file);
        BufferedReader buff = new BufferedReader(file_read);
        
        String tmp = null;
        String var_code = "";
        String var_val = "";

        for (;;)
        {
            tmp = buff.readLine();

            if (tmp == null)
            {
                break;
            }
            else
            {
                var_code = tmp.split("=")[0];
                var_val = tmp.split("=")[1];
                
                // carico solo valori validi:
                if (!"***".equals(var_val))
                {
	                // fix valori con virgola per le migliaia:
	                var_val = Replacer.replace(var_val,",","");
	            	code_value.put(var_code, var_val);
                }
            }
        }

        buff.close();
        file_read.close();

        return code_value;
    }
    
    
    private String timeSync(int idDevice, String codeHour, String codeMinute, String codeConfirmation, UserSession us)
    {
    	String strResult = "OK";

    	if( idDevice <= 0 ) {
        	strResult = LangMgr.getInstance().getLangService(us.getLanguage()).getString("dtlview", "ts_no_dev_selected");
        	return strResult;
    	}
    	
    	// retrieve idvariable for hour and minute
    	int idHour = 0;
    	int idMinute = 0;
    	int idConfirmation = 0;
    	try {
        	String sql = "select idvariable from cfvariable where idvarmdl = (select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevice where iddevice = ?) and code = ?) order by idvariable";
        	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] {idDevice, codeHour});
        	if( rs.size() > 0 )
        		idHour = (Integer)rs.get(0).get(0);
        	rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] {idDevice, codeMinute});
        	if( rs.size() > 0 )
        		idMinute = (Integer)rs.get(0).get(0);
        	if( codeConfirmation != null ) {
        		rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] {idDevice, codeConfirmation});
        		if( rs.size() > 0 )
        			idConfirmation = (Integer)rs.get(0).get(0);
        	}
        	
    	} catch(DataBaseException e) {
    		LoggerMgr.getLogger(this.getClass()).error(e);
    	}
    	
    	// synchronize time
    	if( idHour > 0 && idMinute > 0 ) {
    		Calendar cal = new GregorianCalendar();
    		int nHour = cal.get(Calendar.HOUR_OF_DAY);
    		int nMinute = cal.get(Calendar.MINUTE);
    	   	SetContext setContext = new SetContext();
    	   	setContext.setLanguagecode(us.getLanguage());
        	if( idConfirmation != 0 )
        		setContext.addVariable(new int[] {idHour, idMinute, idConfirmation}, new String [] { "" + nHour, "" + nMinute, "1"});
        	else
        		setContext.addVariable(new int[] {idHour, idMinute}, new String [] { "" + nHour, "" + nMinute});
        	setContext.setCallback(new OnLineCallBack());
        	//setContext.setCallback(new BackGroundCallBack());
    		setContext.setUser(us.getUserName());
            SetDequeuerMgr.getInstance().add(setContext);    		
    	}
    	else if( idHour == 0 ) {
			strResult = codeHour;
    		strResult += LangMgr.getInstance().getLangService(us.getLanguage()).getString("dtlview", "ts_var_not_found");
    	}
		else if( idMinute == 0 ) {
			strResult = codeMinute;
			strResult += LangMgr.getInstance().getLangService(us.getLanguage()).getString("dtlview", "ts_var_not_found");
    	}
    	if( codeConfirmation != null && idConfirmation == 0 ) {
			strResult = codeConfirmation;
			strResult += LangMgr.getInstance().getLangService(us.getLanguage()).getString("dtlview", "ts_var_not_found");
    	}
    	
    	return strResult;
    }
}
