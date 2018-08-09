package com.carel.supervisor.presentation.bo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import supervisor.SRVLRefresh;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.config.Supervisor;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.io.FileSystemUtils;
import com.carel.supervisor.base.io.SocketComm;
import com.carel.supervisor.base.io.Zipper;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.script.ScriptInvoker;
import com.carel.supervisor.base.system.PvproInfo;
import com.carel.supervisor.base.system.SystemInfoExt;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dataaccess.support.Information;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.director.guardian.CheckersMgr;
import com.carel.supervisor.director.guardian.GuardianCheck;
import com.carel.supervisor.director.ide.ExportMgr;
import com.carel.supervisor.director.ide.XmlStream;
import com.carel.supervisor.director.maintenance.VarHistorMgr;
import com.carel.supervisor.director.packet.PacketMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.ide.dc.DeviceModelImportManager;
import com.carel.supervisor.ide.dc.xmlDAO.ImportException;
import com.carel.supervisor.plugin.optimum.OptimumManager;
import com.carel.supervisor.presentation.assistance.ClearCommisioning;
import com.carel.supervisor.presentation.assistance.GuardianConfig;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.bean.DeviceDetectBean;
import com.carel.supervisor.presentation.bo.helper.BackupHelper;
import com.carel.supervisor.presentation.bo.helper.GuardianHelper;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.helper.Buzzer;
import com.carel.supervisor.presentation.helper.FtpCommander;
import com.carel.supervisor.presentation.helper.ModbusSlaveCommander;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.session.Transaction;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.svgmaps.SvgMapsUtils;


public class BSystem extends BoMaster
{
	private Logger logger = LoggerMgr.getLogger(this.getClass());
	private static final long serialVersionUID = -4738292183372263043L;
	private final static String CUSTOM_PATH = "custom" + File.separator + "dtlview";
    private final static String RELATIVE_PATH = "app" + File.separator + "mstrmaps";
	private final static String FOLDER_CUSTOM = "custom_login";
	public final static String ZIP_NAME = "SITE_";
	public final static String ZIP_NAME_4SVG = "DATAPOINTS_";
	public final static String ZIP_NAME_4MDL = "DEVMDL_";
	private final static String DEFAULT_IMG = "login.jpg";
	private final static String DEFAULT_IMG_TOP = "top/left.png";
	private final static int BUFFER = 1024;
	public final static String SHOWSAFETYLEVEL = "showsafetylevel";

    private int screenw = 1024;
    private int screenh = 768;

	public BSystem(String l)
	{
		super(l, 0);
	}

	protected Properties initializeEventOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "enableAction(1);enableAction(2);SysAlertLicense();");
		p.put("tab2name", "SysAlertBackUp();Error();");
		p.put("tab3name", "Error();HideImportMsg();ShowMsgWindow();");
		p.put("tab5name", "subtab5_init();");
		p.put("tab6name", "initMultiMessage();");
		p.put("tab7name", "pluginOnLoad();");

		return p;
	}

	protected Properties initializeJsOnLoad()
	{
		String virtkey = "";
        //determino se � abilitata la VirtualKeyboard:
        if (VirtualKeyboard.getInstance().isOnScreenKey())
        {
            virtkey = ";keyboard.js;";
        }
        
        Properties p = new Properties();
		
        p.put("tab1name", "system.js"+virtkey);
		p.put("tab2name", "system.js;../arch/FileDialog.js" + virtkey);
		p.put("tab3name", "system.js;../arch/FileDialog.js" + virtkey);
        
        p.put("tab4name", "error.js;../arch/FileDialog.js"+virtkey);
		
        p.put("tab5name", "dbllistbox.js;guardian.js;"+virtkey);
		p.put("tab6name", "system.js;");
		p.put("tab7name", "plugin.js"+virtkey);

		return p;
	}
	
	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab2name", DOCTYPE_STRICT);
		p.put("tab3name", DOCTYPE_STRICT);
		p.put("tab4name", DOCTYPE_STRICT);
		return p;
    }

	public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
	{	
		String langcode = us.getLanguage();
		LangService lan = LangMgr.getInstance().getLangService(langcode);
		String error = lan.getString("mgr", "error");

		//vars x msg dal guardianPRO x Eventi:
		String old_gconf = "";
        String action = prop.getProperty("action");
    	String user = "";
		
    	// 20091028 - HOT FIX - Just to allow the "engine restart"-profile users to restart the engine
    	// added "&& !"rest".equalsIgnoreCase(action)"
		if (tabName.equals("tab1name") && !"rest".equalsIgnoreCase(action))
		{
			//this.saveRegistrationInfo(us, prop);
			this.activePacket(us, prop);
			BSiteView bsiteview = new BSiteView(us.getLanguage());
			bsiteview.saveSiteinfo(us, prop);
			
			//20100613 Nicola Compagno -- added engine restart after product/packet/kit activation
			if(us.getProperty("licensestatus")!= null && us.getProperty("licensestatus").equals("msglok"))
			{
				stopPvEngine(us);
	            startPvEngine(us);
			}
		}
        else if (tabName.equals("tab5name"))
		{
			// tab GUARDIAN
			String cmd = us.getProperty("cmd");

			if (cmd.equals("sendguardianvars"))
			{
				String s_values = us.getPropertyAndRemove("values");
				String[] ids = StringUtility.split(s_values, ",");
				String sql = "truncate cfvarguardian";
				DatabaseMgr.getInstance().executeStatement(null, sql, null);
				sql = "select iddevice from cfvariable where idvariable = ?";

				ArrayList<Object[]> lista = new ArrayList<Object[]>();
				Integer idvar = null;
				Integer iddev = null;

				for (int i = 0; i < ids.length; i++)
				{
					idvar = new Integer(ids[i]);

					RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{idvar});
					iddev = (Integer) recordset.get(0).get(0);
					lista.add(new Object[]{idvar, iddev});
				}

				if (ids.length > 0)
				{
					sql = "insert into cfvarguardian values (?,?)";
					DatabaseMgr.getInstance().executeMultiStatement(null, sql, lista);
				}

				String check_alarm = prop.getProperty("check_ver_alarm");

				if (check_alarm != null)
				{
					check_alarm = "TRUE";
				} else
				{
					check_alarm = "FALSE";
				}

				//ctrl stato precedente checkAlarm x scrittura Eventi:
				String oldCheckAlarm = SystemConfMgr.getInstance().get("checkalarm").getValue();
				
				if (!oldCheckAlarm.trim().equals(check_alarm.trim()))
				{
					//se modifico ctrl segnalo quale utente lo ha fatto:
					EventMgr.getInstance().log(new Integer(us.getIdSite()), us.getUserName(), "Action", (check_alarm.equals("TRUE")?EventDictionary.TYPE_INFO:EventDictionary.TYPE_WARNING), (check_alarm.equals("TRUE")?"G007":"G006"), null);
				}
				else
				{
					//se confermo settaggio esistente, segnalo configurazione attuale del guardianPRO:
					EventMgr.getInstance().log(new Integer(us.getIdSite()), "guardianPRO", "Config", (check_alarm.equals("TRUE")?EventDictionary.TYPE_INFO:EventDictionary.TYPE_WARNING), (check_alarm.equals("TRUE")?"G007":"G006"), null);
				}
				
				SystemConfMgr.getInstance().save("checkalarm", check_alarm, 0);
				Float varping = Float.parseFloat(us.getProperty("varping"));
				if( varping < 5 )
					varping = new Float(5);
				if( varping > 60 )
					varping = new Float(60);
				SystemConfMgr.getInstance().save("varping", null, varping * 60);
				SystemConfMgr.getInstance().save("errorping", null, Float.parseFloat(us.getProperty("errorping")));

				// sia per il locale che per il remoto
				//SystemConfMgr.getInstance().save("machine", us.getProperty("machine"), 0);
				//SystemConfMgr.getInstance().save("guardianport", null, Float.parseFloat(us.getProperty("port")));
				SystemConfMgr.getInstance().refreshSystemInfo();
                
                /*
                 * Guardian Snooze
                 */
                String snoozeActive = prop.getProperty("szac");
                
                //ctrl stato precedente x scrittura Eventi:
                old_gconf = ProductInfoMgr.getInstance().getProductInfo().get("gsnooze");
                
                if(snoozeActive != null)
                {
                    if(snoozeActive.equalsIgnoreCase("on") || snoozeActive.equalsIgnoreCase("1"))
                    {
                    	String snoozeDays = prop.getProperty("szdd");
                    	
                        if(snoozeDays != null)
                        {
                            ProductInfoMgr.getInstance().getProductInfo().remove("gsnooze");
                            ProductInfoMgr.getInstance().getProductInfo().store("gsnooze",snoozeDays);
                            EventMgr.getInstance().log(us.getIdSite(),
                                    us.getUserName(),"Action",EventDictionary.TYPE_WARNING,"R017",new Object[]{snoozeDays});
                            
                            /*
                             * Send info to guardian
                             */
                            try
                            {
                                long deadLine = ProductInfoMgr.getInstance().getProductInfo().getTime("gsnooze");
                                int days = Integer.parseInt(ProductInfoMgr.getInstance().getProductInfo().get("gsnooze"));
                                deadLine = (deadLine + (86400000L * days));
                                SocketComm.sendCommand(SystemConfMgr.getInstance().get("machine").getValue(),
                                                       (int)SystemConfMgr.getInstance().get("guardianport").getValueNum(),
                                                       "snooze"+deadLine);
                            }
                            catch(Exception e)
                            {
                            	e.printStackTrace();
                                logger.error("CST: ERROR IN BSYSTEM. Check logs."+e);
                            }
                            /*
                             * End 
                             */
                        }
                    }
                    else
                    {
                    	//guardianPRO notifica lo stato attuale:
                    	EventMgr.getInstance().log(us.getIdSite(),"guardianPRO","Config",EventDictionary.TYPE_WARNING,"R017",
                    			new Object[]{old_gconf});
                    }
                }
                else
                {
                	if (old_gconf == null)
                	{
                		//caso nessuna modifica allo stato precedente:
                		user = "guardianPRO";
                		action = "Config";
                	}
                	else
                	{
                		//caso modifica conf da parte dell'utente:
                		user = us.getUserName();
                		action = "Action";
                	}
                    
                	ProductInfoMgr.getInstance().getProductInfo().remove("gsnooze");
                    EventMgr.getInstance().log(us.getIdSite(),user,action,EventDictionary.TYPE_INFO,"R018",null);
                    
                    SocketComm.sendCommand(SystemConfMgr.getInstance().get("machine").getValue(),
                            (int)SystemConfMgr.getInstance().get("guardianport").getValueNum(),"removesnooze");
                }
                /*
                 * End
                 */
                
                
                //modify by Kevin, 2011-3-25. Now save the gchannel in Guardian.xml. Not in DB
            	//notification --simon add 2010-3-15
		        String gchannel = prop.getProperty("gch");
		        //ctrl stato precedente x scrittura Eventi:
		        old_gconf = GuardianConfig.getGardianSignal();
		        if (gchannel != null) {
					if ((old_gconf == null) || (!"F".equals(old_gconf))) {
						// caso nessuna modifica allo stato precedente:
						user = "guardianPRO";
						action = "Config";
					} else {
						// caso modifica conf da parte dell'utente:
						user = us.getUserName();
						action = "Action";
					}

					//ProductInfoMgr.getInstance().getProductInfo().remove("gsignal");
					EventMgr.getInstance().log(new Integer(1), user, action, EventDictionary.TYPE_INFO, "R016", null);
				} else {
					if ((old_gconf != null) && ("F".equals(old_gconf))) {
						// caso nessuna modifica allo stato precedente:
						user = "guardianPRO";
						action = "Config";
					} else {
						// caso modifica conf da parte dell'utente:
						user = us.getUserName();
						action = "Action";
					}

					//ProductInfoMgr.getInstance().getProductInfo().remove("gsignal");
					//ProductInfoMgr.getInstance().getProductInfo().store("gsignal", "F");
					EventMgr.getInstance().log(us.getIdSite(), user, action, EventDictionary.TYPE_WARNING, "R015", null);
				}
				GuardianConfig.saveConfiguration(prop, us.getIdSite(), us.getSiteName());
                /*
                 * Guardian Variables K
                 */
                String gvark = prop.getProperty("gvk");
                
                //ctrl stato precedente x scrittura Eventi:
                old_gconf = ProductInfoMgr.getInstance().getProductInfo().get("gvark");
                
                if(gvark != null)
                {
                	if ((old_gconf == null) || (!"F".equals(old_gconf)))
                	{
                		//caso nessuna modifica allo stato precedente:
                		user = "guardianPRO";
                		action = "Config";
                	}
                	else
                	{
                		//caso modifica conf da parte dell'utente:
                		user = us.getUserName();
                		action = "Action";
                	}
                	
                	ProductInfoMgr.getInstance().getProductInfo().remove("gvark");
                    EventMgr.getInstance().log(us.getIdSite(),user,action,EventDictionary.TYPE_INFO,"R014",null);
                }
                else
                {
                	if ((old_gconf != null) && ("F".equals(old_gconf)))
                	{
                		//caso nessuna modifica allo stato precedente:
                		user = "guardianPRO";
                		action = "Config";
                	}
                	else
                	{
                		//caso modifica conf da parte dell'utente:
                		user = us.getUserName();
                		action = "Action";
                	}
                	
                    ProductInfoMgr.getInstance().getProductInfo().remove("gvark");
                    ProductInfoMgr.getInstance().getProductInfo().store("gvark","F");
                    EventMgr.getInstance().log(us.getIdSite(),user,action,EventDictionary.TYPE_WARNING,"R013",null);
                }
                
                /*
                 * Check SSD
                String gssd = ProductInfoMgr.getInstance().getProductInfo().get("gssd");
                boolean bOldSSD = gssd != null && gssd.equals("true");
                boolean bNewSSD = prop.getProperty("gssd") != null;
                if( bNewSSD != bOldSSD ) {
                    ProductInfoMgr.getInstance().getProductInfo().remove("gssd");
                	ProductInfoMgr.getInstance().getProductInfo().store("gssd", bNewSSD ? "true" : "false");
                	EventMgr.getInstance().log(us.getIdSite(),
                		gssd != null ? us.getUserName() : "guardianPRO",
                		gssd != null ? "Action" : "Config",
                		bNewSSD ? EventDictionary.TYPE_INFO : EventDictionary.TYPE_WARNING,
                		bNewSSD ? "R019" : "R020",
                		null);                	
                }
                 */
				DirectorMgr.getInstance().mustRestart();
			}

//			String changeGMessage = "3";
//			if (!GuardianConfig.userConfGuiChannel())
//				changeGMessage = "1";
//			else if (!GuardianConfig.userConfGuiVariable())
//				changeGMessage = "2";
//			
//            // Check if snooze
//            if((ProductInfoMgr.getInstance().getProductInfo().get("gsnooze")) != null)
//                changeGMessage = "3";
            DirectorMgr.getInstance().updateGuardianCode();
			//us.setProperty("actionIconG", changeGMessage);
		} 
		
		if (prop.get("action") != null)
		{
			for (;;)
			{
				if (prop.get("action").equals("stop"))
				{
                    stopPvEngine(us);
					break;
				}

				if (prop.get("action").equals("start"))
				{
                    startPvEngine(us);
					break;
				}
                
                if (prop.get("action").equals("rest"))
                {
                    SRVLRefresh.setRestartEngine(true);
                	stopPvEngine(us);
                    startPvEngine(us);
                    SRVLRefresh.setRestartEngine(false);
                    break;
                }

                if( prop.get("action").equals("sysrestart") )
                {
                    sysRestart(us);
                    break;
                }

                
                if (prop.get("action").equals("remdevcreator"))
				{
					Transaction transaction = us.getTransaction();
					Properties properties = new Properties();
					
					Integer id = -1;
					
					try
					{
						String idTmp = prop.getProperty("remdevcreator");
						id = Integer.valueOf(idTmp);
						
						DeviceModelImportManager devImport = new DeviceModelImportManager();
						devImport.removeDeviceModel(id);
						
						String msg = lan.getString("impdev", "ok");
						properties.setProperty("remdevcrearetu", msg);
						transaction.setSystemParameter(properties);
						
					} 
					catch (ImportException ie)
					{
						ie.printStackTrace();
						logger.debug("CST: import device with errors."+ie);
						String msg = lan.getString("impdev", ie.getMessage());
            			if (msg.equals(""))
            			{
            				msg = lan.getString("impdev", "error");
            			}
            			properties.setProperty("remdevcrearetu", msg);
						transaction.setSystemParameter(properties);
					}
                    catch (Exception e)
					{
                    	e.printStackTrace();
                    	String msg = lan.getString("impdev", "error");
            			properties.setProperty("impdevcreator", msg);	
            			transaction.setSystemParameter(properties);
            	
            			Logger logger = LoggerMgr.getLogger(BSystem.class);
            			logger.error(e);
					}
				
            		//remove custom directory for device main page (SubTab1.jsp) from cfdevcustom
            		try{
            			String sql = "delete from cfdevcustom where iddevmdl = ?";
            			DatabaseMgr.getInstance().executeStatement(null,sql,new Object[]{id});
            		}
            		catch (Exception e)
            		{
            			e.printStackTrace();
            		}
				
				}
                
                if (prop.get("action").equals("remmaps"))
				{
                	try{
	                	String folder = BaseConfig.getAppHome() + "app" + File.separator + "mstrmaps";
		            	File f= new File(folder);
		                String[] fileNames;
		                if (f.isDirectory()) {
		                	fileNames = f.list();
		                    for (int i = 0; i < fileNames.length; i++) {
		                        if(fileNames[i].equals("SubTab1.jsp"))
		                        	continue;
		                        File currFile = new File(f, fileNames[i]);
		                        if (currFile.isDirectory()) {
		                        	FileUtils.deleteDirectory(currFile);
		                        }
		                        else
		                        currFile.delete();	
		                    }
		                
		                    File dir = new File(BaseConfig.getAppHome() + "app" + File.separator + "mstrmaps"  + File.separator + "svgmaps"); 
		                    dir.mkdir();
		                }
                	}
	                catch(Exception e)
	                {
	                	LoggerMgr.getLogger(BSystem.class).error(e);
	                }
				}

				if (prop.get("action").equals("backupimp"))
				{
                    // Stop Guardian
                    try {
                        CheckersMgr.getInstance().stopForImport();
                    }catch(Exception e){}
                    
                    DirectorMgr.getInstance().mustCreateProtocolFile();
                    
                    // Stop motore
					stopPvEngine(us);
					
					Properties properties = new Properties();
					String file = (String) prop.get("backupimp");
					String r = BackupHelper.restoreAll(us.getUserName(), file);
					properties.setProperty("backupimp", r);

					Transaction transaction = us.getTransaction();
					transaction.setSystemParameter(properties);

					startPvEngine(us); 

					us.getGroup().reloadDeviceStructureList(us.getIdSite(), us.getLanguage());
					us.setForceLogout(true);
					
					//reload languages
                	LangMgr.getInstance().init();
                	
                	// update optimum plugins
                	OptimumManager.getInstance().init();
					
                    try {
                        CheckersMgr.getInstance().startAfterImport();
                    }catch(Exception e){
                    	e.printStackTrace();
                    }
                    
					break;
				}

				if (prop.get("action").equals("backupexp"))
				{
                    // Stop Guardian
                    try {
                        CheckersMgr.getInstance().stopForImport();
                    }catch(Exception e){
                    	e.printStackTrace();
                    }
                    
					// Stop motore
					stopPvEngine(us);
					
					Properties properties = new Properties();
					String r = BackupHelper.backupAll(us.getUserName());
					//properties.setProperty("backupexp", "OK");
					String lastPathDir = "";
					if ((r != null) && (! "".equals(r)))
					{
						lastPathDir = lastPathDir + r.substring(r.lastIndexOf(File.separator)+1);
					}
					properties.setProperty("backupexp", lastPathDir);
					properties.setProperty("pathexp", r);

					Transaction transaction = us.getTransaction();
					transaction.setSystemParameter(properties);

					startPvEngine(us); 

                    try {
                        CheckersMgr.getInstance().startAfterImport();
                    }catch(Exception e){
                    	e.printStackTrace();
                    }

                    break;
				}
				
				// GESTIONE TIMEOUT SESSIONE
				if (prop.get("action").equals("sessiontimeout"))
				{
					Properties properties = new Properties();
					
					//ProductInfo p_info = new ProductInfo();
					IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
					p_info.set("session",prop.getProperty("timeoutselect"));
					
					ServletHelper.setSESSION_TIMEOUT(Long.parseLong(prop.getProperty("timeoutselect"))*1000*60); //Imposto il timeout di sessione
					properties.setProperty("timeoutok", "OK");
				
					Transaction transaction = us.getTransaction();
					transaction.setSystemParameter(properties);

					break;

				}

				// cambio pagina home
				if (prop.get("action").equals("changehome"))
				{
					Transaction transaction = us.getTransaction();
					Properties properties = new Properties();
					String param_to_set = us.getProperty("homeselect");
					
					IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
					//ProductInfo p_info = new ProductInfo();
					// p_info.load();
					
					try
					{
						p_info.set("home", param_to_set);
						properties.setProperty("changehome", lan.getString("mgr", "homeok"));
					} catch (Exception e)
					{
						e.printStackTrace();
						properties.setProperty("changehome", error);
						transaction.setSystemParameter(properties);

						Logger logger = LoggerMgr.getLogger(BSystem.class);
						logger.error(e);
					}

					transaction.setSystemParameter(properties);

					break;
				} // if
//				change default login language
				if (prop.get("action").equals("deflanguage"))
				{
					String languagecode = us.getProperty("deflanguageselect");
					String loginLanguage = BaseConfig.getProductInfo("login_language");
					if (loginLanguage ==null ){
						ProductInfoMgr.getInstance().getProductInfo().store("login_language", languagecode);
					}else{
						ProductInfoMgr.getInstance().getProductInfo().set("login_language", languagecode);
					}
					break;
				} // if

				if (prop.get("action").equals("reloaddevice"))
				{
					DispatcherMgr.getInstance().loadDeviceInMemory();

					break;
				} // if

				if (prop.get("action").equals("maintenance"))
				{
					// Stop motore
					stopPvEngine(us);

					int code = VarHistorMgr.move();
					Transaction transaction = us.getTransaction();
					Properties properties = new Properties();

					switch (code) {
						case 0 :
							EventMgr.getInstance().info(new Integer(1), us.getUserName(), "Action", "M004", null);
							properties.setProperty("error", lan.getString("mgr", "mainok"));

							break;

						case 1 :
						case 2 :
							EventMgr.getInstance().error(new Integer(1), us.getUserName(), "Action", "M002", null);
							properties.setProperty("error", lan.getString("mgr", "mainerr1"));

							break;

						case 3 :
						case 4 :
							EventMgr.getInstance().error(new Integer(1), us.getUserName(), "Action", "M003", null);
							properties.setProperty("error", lan.getString("mgr", "mainerr2"));

							break;
					}

					transaction.setSystemParameter(properties);

					startPvEngine(us);
					break;
				}
				
				if (prop.get("action").equals("ssd_stat"))
				{
					Supervisor.getInstance().checkSSD();
					break;
				}
                
                /*
                 * Pulizia tabelle
                 */
                if (prop.getProperty("action").equalsIgnoreCase("reset"))
                {
                    new ClearCommisioning(us.getUserName()).startWork();
                    break;
                }
                
                // gestione Tastiera Virtuale su touch-screen:
                if (prop.get("action").equals("doVirtualKey"))
                {
                    boolean azione = Boolean.parseBoolean(prop.getProperty("whereKey"));
                    VirtualKeyboard.getInstance().setOnScreenKey(azione);
                    break;
                }
                
                if(prop.getProperty("action").equals("FTPstart"))
                {
                	FtpCommander.startFTP("FileZilla Server");
                	EventMgr.getInstance().info(us.getIdSite(), us.getUserName(), "Action", "W060", new Object[]{"FTP server", "start"});
                	break;
                }
                
                if(prop.getProperty("action").equals("FTPstop"))
                {
                	FtpCommander.stopFTP("FileZilla Server");
                	EventMgr.getInstance().info(us.getIdSite(), us.getUserName(), "Action", "W060", new Object[]{"FTP server ", "stop"});
                	break;
                }             
                if("modbusslavestart".equalsIgnoreCase(prop.getProperty("action")))
                {
        			ModbusSlaveCommander.startService();
                	EventMgr.getInstance().info(us.getIdSite(), us.getUserName(), "Action", "W060", new Object[]{"ModbusSlave","start"});
                	break;
                }
                
                if("modbusslavestop".equalsIgnoreCase(prop.getProperty("action")))
                {
                	ModbusSlaveCommander.stopService();
                	EventMgr.getInstance().info(us.getIdSite(), us.getUserName(), "Action", "W060", new Object[]{"ModbusSlave","stop"});
                	break;
                }
                if("modbusslave_autostart".equalsIgnoreCase(prop.getProperty("action")))
                {
                	String strAutoStart = prop.getProperty("modbusslave_autostart");
                	int starttype = strAutoStart != null && strAutoStart.equalsIgnoreCase("on")
                		? ModbusSlaveCommander.E_STARTTYPE_AUTO_START
                		: ModbusSlaveCommander.E_STARTTYPE_DEMAND_START;
                	String typestr = "";
                	switch(starttype)
                	{
	                	case ModbusSlaveCommander.E_STARTTYPE_AUTO_START:
	                		typestr = "auto";
	                		break;
	                	case ModbusSlaveCommander.E_STARTTYPE_DEMAND_START:
	                		typestr = "manual";
	                		break;
	                	case ModbusSlaveCommander.E_STARTTYPE_DISABLED:
	                		typestr = "disabled";
	                		break;
                	}
                	ModbusSlaveCommander.setStarttype(starttype);	
                	EventMgr.getInstance().info(us.getIdSite(), us.getUserName(), "Action", "W060", new Object[]{"Change ModbusSlave Start type",typestr});
                	break;
                }
				if("buzzeron".equalsIgnoreCase(prop.getProperty("action")))
                {
                	Buzzer.setBuzzerOn(true);
                }
                if("buzzeroff".equalsIgnoreCase(prop.getProperty("action")))
                {
                	Buzzer.setBuzzerOn(false);
                }

				if("remoteusersmngmon".equalsIgnoreCase(prop.getProperty("action")))
                {
                	String sql = "update productinfo set value = 'on', lastupdate = now() where key = 'remoteusermngm'" ;
                	DatabaseMgr.getInstance().executeStatement(null,sql,null);
                }
                if("remoteusersmngmoff".equalsIgnoreCase(prop.getProperty("action")))
                {
                	String sql = "update productinfo set value = 'off', lastupdate = now() where key = 'remoteusermngm'" ;
                	DatabaseMgr.getInstance().executeStatement(null,sql,null);
                }
                
                // Hide/Show Safety Level
                if (prop.get("action").equals("safetylevel"))
                {
                	IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
                	String showHideSL = prop.getProperty("rd_hide");
                	System.out.println("ShowhideSL: "+showHideSL);
                	if (showHideSL == null)
                		// showHideSL == null means "NOT checked", -> Safety Level box must be shown
                		product.set(SHOWSAFETYLEVEL, "1");
                	else
                		// showHideSL != null neams "checked" -> Safety Level box must be hidden
                		product.set(SHOWSAFETYLEVEL, "0");
                }

                if("license".equalsIgnoreCase(prop.getProperty("action")))
                {
                	String strLicense = prop.getProperty("license_agreement") != null ? "yes" : "no";
                	ProductInfoMgr.getInstance().getProductInfo().set("license_agreement", strLicense);                	
                }
                
                if( prop.get("action").equals("remote_control") ) {
                	String batch = prop.get("remote_control").equals("true")
                		? "firewallSet.bat" : "firewallSet2.bat";
                	ScriptInvoker script = new ScriptInvoker();
            		try {
            			script.execute(new String[] { BaseConfig.getCarelPath() + "tools\\Batch\\" + batch, "" }
            				,BaseConfig.getCarelPath() + "tools\\Batch\\firewall.log"
            			);
            		} catch(Exception e) {
            			LoggerMgr.getLogger(this.getClass()).error(e);
            		}
                }
                
                if( prop.get("action").equals("firefox_portable") ) {
                	String batch = prop.get("firefox_portable").equals("true")
                		? "browserSETfp.bat" : "browserSETstd.bat";
                	ScriptInvoker script = new ScriptInvoker();
            		try {
            			script.execute(new String[] { BaseConfig.getCarelPath() + "tools\\Batch\\" + batch, "" }
            				,BaseConfig.getCarelPath() + "tools\\Batch\\Browser.log"
            			);
            		} catch(Exception e) {
            			LoggerMgr.getLogger(this.getClass()).error(e);
            		}
                }
                if("value_note".equalsIgnoreCase(prop.getProperty("action")))
                {
                	String strValueNote = prop.getProperty("value_note") != null ? "yes" : "no";
                	
                	String exist= ProductInfoMgr.getInstance().getProductInfo().get("value_note");
                	if(exist!=null && exist!=""){
                		ProductInfoMgr.getInstance().getProductInfo().set("value_note", strValueNote);                	
                	}else{
                		ProductInfoMgr.getInstance().getProductInfo().store("value_note", strValueNote); 
                	}
                	
                }
                // Sempre uscire
				break;
			}
		}
	}

	public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
	{
		String toReturn = "";
    	String cmd = prop.getProperty("cmd");
		if("rest".equals(cmd)){
			SRVLRefresh.setRestartEngine(true);
			stopPvEngine(us);
            startPvEngine(us);
            SRVLRefresh.setRestartEngine(false);
            return toReturn;
		}

		if("tab6name".equals(tabName.trim()))
		{
			if("init".equalsIgnoreCase(cmd))
			{
				//guardian
				String guardian = GuardianCheck.isEnableWin();
				toReturn = "<msg>";
				toReturn += "<guardian>"+guardian+"</guardian>";
				toReturn += "</msg>";
			}
		}
		else if("expsiteconf".equalsIgnoreCase(cmd))
		{
			toReturn = "<response>";
			XmlStream a = ExportMgr.getInstance().getExporter("site").exporter(us.getLanguage());
			
			String local = prop.getProperty("local");
            String basename = "";
            if("true".equalsIgnoreCase(local))
            {
            	String path = prop.getProperty("path");
            	basename = path;
            }
            else
            {
            	basename = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator  + ZIP_NAME+DateUtils.date2String(new Date(), "yyyyMMddhhmmss");
            }
			
			String siteXML = a.getFile("site");
			String descXML = a.getFile("desc");
			
			try
			{
				//ZipperFile.zip(filePath, ZIP_NAME, new String[]{"Site", "SiteDictionary"}, "xml", new String[]{siteXML, descXML});
				Zipper.zipFiles(true,basename, new String[]{siteXML,descXML});
				EventMgr.getInstance().info(us.getIdSite(), us.getUserName(), "Action", "S021", BackupHelper.format(basename + ".zip"));
				toReturn += "<file><![CDATA["+basename+".zip]]></file></response>";
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.error("CST: ERROR."+e);
				prop.setProperty("error", e.getMessage());
				EventMgr.getInstance().info(us.getIdSite(), us.getUserName(), "Action", "S022", "");
				toReturn += "<file><![CDATA[ERROR]]></file></response>";			
			}
		}
		else if("expsiteconfForSVG".equalsIgnoreCase(cmd))
		{
			
			String local = prop.getProperty("local");
            String basename = "";
            if("true".equalsIgnoreCase(local))
            {
            	String path = prop.getProperty("path");
            	basename = path;
            }
            else
            {
            	basename = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator  + ZIP_NAME_4SVG+DateUtils.date2String(new Date(), "yyyyMMddhhmmss");
            }
			
            Writer writer = null;
            
            try {
            	String xmlToWrite = SvgMapsUtils.getSiteDatapoint();
			
	    	    OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(basename+".xml"), "utf-8");
            	
            	writer = new BufferedWriter(os);
	    	    writer.write(xmlToWrite);
	    	    writer.close();
	    	    os.close();
	    	    
	    	    Zipper.zipFiles(true,basename, new String[]{basename+".xml"});
	    	    
	    	    toReturn += "<response><file><![CDATA["+basename+".zip]]></file></response>";
	    	} catch (Exception e) {
	    		e.printStackTrace();
				logger.error("CST: ERROR."+e);
				prop.setProperty("error", e.getMessage());
				EventMgr.getInstance().info(us.getIdSite(), us.getUserName(), "Action", "S022", "");
				toReturn += "<file><![CDATA[ERROR]]></file></response>";
	    	} finally {
	    	   try {writer.close();} catch (Exception ex) {}
	    	}
		}
		else if("expMdlConfForSVG".equalsIgnoreCase(cmd))
		{
			
			String local = prop.getProperty("local");
            String basename = "";
            String idTmp = prop.getProperty("expsvg_iddevmdl");
        	int iddevmdl = Integer.valueOf(idTmp);
            DevMdlBean devmdl = new DevMdlBeanList().retrieveById(1,"EN_en",iddevmdl);
			String devmdlcode = devmdl.getCode();
			
            if("true".equalsIgnoreCase(local))
            {
            	String path = prop.getProperty("path");
            	basename = path;
            }
            else
            {
            	basename = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator  + ZIP_NAME_4MDL + devmdlcode;
            }
			
            Writer writer = null;
            
            try {
            	
            	 
            	String xmlToWrite = SvgMapsUtils.getDevMdlDatapoint(iddevmdl,devmdlcode);
            	basename = basename.replaceAll("/","");
	    	    OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(basename+".xml"), "utf-8");
            	
            	writer = new BufferedWriter(os);
	    	    writer.write(xmlToWrite);
	    	    writer.close();
	    	    os.close();
	    	    
	    	    Zipper.zipFiles(true,basename, new String[]{basename+".xml"});
	    	    
	    	    toReturn += "<response><file><![CDATA["+basename+".zip]]></file></response>";
	    	} catch (Exception e) {
	    		e.printStackTrace();
				logger.error("CST: ERROR."+e);
				prop.setProperty("error", e.getMessage());
				EventMgr.getInstance().info(us.getIdSite(), us.getUserName(), "Action", "S022", "");
				toReturn += "<file><![CDATA[ERROR]]></file></response>";
	    	} finally {
	    	   try {writer.close();} catch (Exception ex) {}
	    	}
		}
		else if("savelog".equalsIgnoreCase(cmd))
		{
			toReturn = "<response>";
		     String command = BaseConfig.getCarelPath()+"diagnose"+File.separator+"diagnose.bat";      
		     try 
		     {    
		          Process child = Runtime.getRuntime().exec(command);    
		          InputStream in = child.getInputStream();    
		          in.close();     
	              child.waitFor();
	              String defaultpath = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator +
	              	"Diagnose_"+DateUtils.date2String(new Date(), "yyyyMMddhhmmss")+".zip";
	              String local = prop.getProperty("local");
	              String file = defaultpath;
	              if("true".equalsIgnoreCase(local))
	              {
	            	  String path = prop.getProperty("path");
	            	  File fin = new File(defaultpath);
	            	  File fout = new File(path);
	            	  FileSystemUtils.copyFile(fin, fout);
	            	  file = path;
	              }
	              toReturn += "<file><![CDATA["+file+
	              			  "]]></file></response>";
		      }
		      catch (IOException e) 
		      {
		    	  logger.error(e);
		          e.printStackTrace(); 
		          toReturn += "<file><![CDATA[ERROR]]></file></response>";
		      }    
		}
		else if ("sitemgrexp".equalsIgnoreCase(cmd))
		{
			toReturn = "<response>";
			String local = prop.getProperty("local");
            String file = "";
            if("true".equalsIgnoreCase(local))
            {
            	String path = prop.getProperty("path");
            	file = path+BackupHelper.CONF_EXT;
            }
            else
            {
            	file = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator +
            	BackupHelper.CONF_FILE + "_" + BackupHelper.dataOdierna() + "_" + BackupHelper.getVersion() + BackupHelper.CONF_EXT;
            }
			int r = BackupHelper.backupConf(us.getUserName(),file);
			if(r == 1)
			{
				toReturn += "<file><![CDATA["+file+"]]></file></response>";
			}
			else
			{
				toReturn += "<file><![CDATA[ERROR]]></file></response>";
			}
		}
		else if ("rulemgrexp".equalsIgnoreCase(cmd))
		{
			toReturn = "<response>";
			String local = prop.getProperty("local");
	        String file = "";
	        if("true".equalsIgnoreCase(local))
	        {
	        	String path = prop.getProperty("path");
	        	file = path+BackupHelper.RULE_EXT;
	        }
	        else
	        {
	        	file = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator +
	        	BackupHelper.RULE_FILE + "_" + BackupHelper.dataOdierna() + "_" + BackupHelper.getVersion() + BackupHelper.RULE_EXT;
	        }
			int r = BackupHelper.backupRule(us.getUserName(),file);
			if(r == 1)
			{
				toReturn += "<file><![CDATA["+file+"]]></file></response>";
			}
			else
			{
				toReturn += "<file><![CDATA[ERROR]]></file></response>";
			}
		}
		else if ("expsitebackup".equalsIgnoreCase(cmd))
		{
			toReturn = "<response>";
			String selectfile = prop.getProperty("selectfile");
			String local = prop.getProperty("local");
	        String file = "";
	        if("true".equalsIgnoreCase(local))
	        {
	        	String path = prop.getProperty("path");
	        	file = path+BackupHelper.SITEBACKUP_EXT;
	        }
	        else
	        {
	        	file = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator +selectfile+BackupHelper.SITEBACKUP_EXT;
	        }
			boolean r = BackupHelper.SiteBackupExportFile(us.getUserName(),selectfile, file);
			if(r)
			{
				toReturn += "<file><![CDATA["+file+"]]></file></response>";
			}
			else
			{
				toReturn += "<file><![CDATA[ERROR]]></file></response>";
			}
		}
		else if("cpuperusage".equalsIgnoreCase(cmd))
		{
			toReturn = "<response>";
			toReturn += "<cpu><![CDATA["+SystemInfoExt.getInstance().getCpuPerUsage()+"]]></cpu></response>";
		}
		else if("loaddevice".equalsIgnoreCase(cmd))
		{
			toReturn = "<response>";
			int iddev = Integer.valueOf(prop.getProperty("iddev"));
			toReturn += GuardianHelper.getOptionListXML(us.getLanguage(), iddev, us);
			toReturn += "</response>";
		}
		return toReturn;
	}
	public String getHTMLSafetyLevel(String language)
	{
		IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
		LangService lan = LangMgr.getInstance().getLangService(language);
		
		String txtSafetylevel = lan.getString("multimsg", "safetylevel");
		
		String showSafetyLevel = product.get(SHOWSAFETYLEVEL);
		StringBuffer table = new StringBuffer();
		if(showSafetyLevel != null && "1".equals(showSafetyLevel))
		{
			int guardian_num = DirectorMgr.getInstance().getGuardian_valid_num();
			int alarm_num = DirectorMgr.getInstance().getAlarm_valid_num();
			boolean keepalive = DirectorMgr.getInstance().isKeepAlive();
			int safetylevel = 0;
			if(guardian_num == 0 || alarm_num == 0)
			{
				safetylevel = 0;
			}
			else if(guardian_num >1 && alarm_num>1 && keepalive == true)
			{
				safetylevel = 2;
			}
			else
			{
				safetylevel = 1;
			}
			String guardian_img = "images/gpro/";
			String guardian_status = "";
			if(guardian_num == 0)
			{
				guardian_img += "red.png";
				guardian_status = lan.getString("multimsg", "guardian_r");
			}
			else if(guardian_num == 1)
			{
				guardian_img += "yellow.png";
				guardian_status = lan.getString("multimsg", "guardian_y");
			}
			else if(guardian_num >1)
			{
				guardian_img += "green.png";
			}
			String alarm_img = "images/gpro/";
			String alarm_status = "";
			if(alarm_num == 0)
			{
				alarm_img += "red.png";
				alarm_status = lan.getString("multimsg", "alarm_r");
			}
			else if(alarm_num == 1)
			{
				alarm_img += "yellow.png";
				alarm_status = lan.getString("multimsg", "alarm_y");
			}
			else if(alarm_num >1)
			{
				alarm_img += "green.png";
			}
			String keepalive_img = "images/gpro/";
			String keepalive_status = "";
			if(keepalive == false)
			{
				keepalive_img += "red.png";
				keepalive_status = lan.getString("multimsg", "keepalive_r");
			}
			else if(keepalive == true)
			{
				keepalive_img += "green.png";
			}
			String safetylevel_img = "images/gpro/";
			if(safetylevel == 0)
			{
				safetylevel_img += "safetylevel_r.png";
			}
			else if(safetylevel == 1)
			{
				safetylevel_img += "safetylevel_y.png";
			}
			else if(safetylevel == 2)
			{
				safetylevel_img += "safetylevel_g.png";
			}
			table.append("<table height='100%' width='100%' border=0>");
			table.append("<tr><td colspan=3 class='tdTitleTable'>"+txtSafetylevel+"</td></tr>");
			
			//safety
			table.append("<tr><td rowspan=4 align='center' width='120px'><img src='"+safetylevel_img+"'></td>");
			table.append("<td width='5%' align='center' valign='middle'><img src='"+guardian_img+"'></td>");
			table.append("<td class='standardTxt' width='15%'>"+lan.getString("mgr", "tab5name")+"</td>");
			table.append("<td class='menu'>"+guardian_status+"</td></tr>");
			table.append("<tr><td align='center' valign='middle'><img src='"+alarm_img+"'></td>");
			table.append("<td class='standardTxt'>"+lan.getString("multimsg", "noti_chan")+"</td>");
			table.append("<td class='menu'>"+alarm_status+"</td></tr>");
			table.append("<tr><td align='center' valign='middle'><img src='"+keepalive_img+"'></td>");
			table.append("<td class='standardTxt'>"+lan.getString("multimsg", "keepalivesig")+"</td>");
			table.append("<td class='menu'>"+keepalive_status+"</td></tr>");
			table.append("</table>");
		}
		return table.toString();
	}
	
    /*
     * Metodo che elimina ogni file dalla cartella di cache delle jsp importate dall'IDE
     */
    private boolean delJspCache(UserSession usrSess, String errore)
    {
        boolean cancellato = false;
        
        //TODO: non � sempre localhost !?!
        String host = "localhost";
        
        String jspcachedir = BaseConfig.getCarelPath() + "engine" + File.separator + "work" + File.separator + "Catalina" + File.separator;
        jspcachedir += host;
        jspcachedir += File.separator + "PlantVisorPRO" + File.separator + "org" + File.separator + "apache" + File.separator + "jsp" + File.separator + RELATIVE_PATH;
        
        try
        {
            File jspcachedircontent = new File(jspcachedir);
            File[] jspcachefiles = jspcachedircontent.listFiles();
            
            if (jspcachefiles != null)
            {
                for (int j=0; j < jspcachefiles.length; j++)
                {
                    cancellato = new File(jspcachefiles[j].getCanonicalPath()).delete();
                    
                    if (! cancellato)
                    {
                        Logger logger = LoggerMgr.getLogger(this.getClass());
                        logger.error(errore+": deleteFile() " + jspcachefiles[j].getCanonicalPath());
                    }
                }
            }
        }
        catch (Exception e)        
        {
        	e.printStackTrace();
            //PVPro-generated catch block
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        return cancellato;
    }
    
    /*
     * Metodo invocato per lo STOP del motore
     */
    public static void stopPvEngine(UserSession us)
        throws Exception
    {
        long sleep = 1000L;
        // Stop motore
        if (DirectorMgr.getInstance().isStarted())
        {
            DirectorMgr.getInstance().stopEngine(us.getUserName());
            sleep = 10000L;
        }

        // Rest macchina a stati
        ControllerMgr.getInstance().reset();

        // Stop dispatcher
        if (DispatcherMgr.getInstance().isServiceRunning())
        {
            DispatcherMgr.getInstance().stopService();
            sleep = 10000L;
        }

        Thread.sleep(sleep);
    }
    
    public static void startPvEngine(UserSession us)
        throws Exception
    {
    	// prevent engine to start when evaluation period expires and there is no license
        if( !Information.getInstance().canStartEngine() ) {
            EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S028",null);
            return;
        }

    	// prevent engine restart during device detection
        if( DeviceDetectBean.isDetection() )
        	return;
      
    	// prevent engine to start when device no exceeds the license
        if( PvproInfo.getInstance().getLicenseOverload() > 0 ) {
    		EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S038",null);
    		return;
    	}
    	
    	// prevent engine to start when logging threshold was reached
        PvproInfo.getInstance().resetLoggedVariables();
    	if( PvproInfo.getInstance().isLoggingOverload() ) {
            EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S037",null);
    		return;
    	}
        	
    	if (DirectorMgr.getInstance().isStopped())
        {
            DirectorMgr.getInstance().reloadConfiguration(us.getUserName());
            DirectorMgr.getInstance().startEngine(us.getUserName());

            if (!DirectorMgr.getInstance().isStopped())
            {
                if (!DispatcherMgr.getInstance().isServiceRunning())
                {
                    DispatcherMgr.getInstance().startService(true);
                }
            }
        }
        
        Thread.sleep(1000L);
    }

    
    public static void sysRestart(UserSession us)
    {
		try {
			EventMgr.getInstance().log(us.getIdSite(), us.getUserName(), "Action", EventDictionary.TYPE_INFO, "S036", null);
			ScriptInvoker script = new ScriptInvoker();
			script.execute(new String[] { "C:\\Program Files\\Carel\\scr\\reboot.bat" }, "C:\\Carel\\PlantVisorPRO\\log\\Carel.log", false);
        } catch(Exception e) {
        	LoggerMgr.getLogger(BSystem.class).error(e);	
        }
    }
    
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }
    
    
    public void activePacket(UserSession us, Properties prop)
    {
		String state = "F";
		String code = prop.getProperty("code");
		String serial = prop.getProperty("serial");
		String activation = prop.getProperty("activation");
		
		// Licenza - start
		String[] params = {code,serial,activation};		
		try {
			PacketMgr.getInstance().checkAutomaticLicense(params);
			serial = params[1];
			activation = params[2];
		}
		catch(Exception e){
			// Don't trace
		}
		// End
				
		if(!"".equals(serial) || !"".equals(activation))
		{
			String ris = PacketMgr.getInstance().activePacket(code,serial,activation,us.getIdSite(),us.getLanguage());
			
			if ("ok".equals(ris))
				state = "msglok";
			else
				state = "msglko";
			
			us.setProperty("licensestatus", state);
		}
    }
}
