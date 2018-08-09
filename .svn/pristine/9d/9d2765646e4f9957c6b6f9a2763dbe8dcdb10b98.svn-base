package com.carel.supervisor.presentation.bo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.profiling.UserProfile;
import com.carel.supervisor.base.system.PvproInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.TableExtBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dataaccess.varaggregator.VarAggregator;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.presentation.bean.DevMdlBean;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceDetectBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.bean.LineBean;
import com.carel.supervisor.presentation.bean.LineBeanList;
import com.carel.supervisor.presentation.bean.ProfileBean;
import com.carel.supervisor.presentation.bean.ProfileBeanList;
import com.carel.supervisor.presentation.bean.ProtocolBean;
import com.carel.supervisor.presentation.bean.ParamBean;
import com.carel.supervisor.presentation.bo.helper.LineConfig;
import com.carel.supervisor.presentation.bo.helper.PortInfo;
import com.carel.supervisor.presentation.bo.helper.Propagate;
import com.carel.supervisor.presentation.bo.helper.PropagateRes;
import com.carel.supervisor.presentation.bo.helper.VarDependencyList;
import com.carel.supervisor.presentation.bo.helper.VarDependencyState;
import com.carel.supervisor.presentation.bo.helper.DeviceImport.DeviceConfigImport;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.copydevice.PageImpExp;
import com.carel.supervisor.presentation.defaultconf.Propagher;
import com.carel.supervisor.presentation.helper.KeyMaxHelper;
import com.carel.supervisor.presentation.helper.SpaceHistoricalHelper;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.ldap.FunctionalityHelper;
import com.carel.supervisor.presentation.menu.MenuSection;
import com.carel.supervisor.presentation.menu.MenuTab;
import com.carel.supervisor.presentation.menu.MenuVoce;
import com.carel.supervisor.presentation.menu.configuration.MenuConfigMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuTabMgr;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;

public class BSiteView extends BoMaster
{
	private static final long serialVersionUID = 3030553841686130247L;
	private static final int REFRESH_TIME = 10000;
	private DeviceConfigImport importer = null;
    public BSiteView(String l)
    {
        super(l, REFRESH_TIME);
    }
    protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab4name", DOCTYPE_STRICT);
		p.put("tab5name", DOCTYPE_STRICT);
		return p;
    }
    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "onload_siteview();");
        p.put("tab2name", "onLoadParameters();");
        p.put("tab3name", "setdefault();");
        p.put("tab4name", "initAlrMng();");
        p.put("tab5name", "initialize();");
        p.put("tab7name", "enableAction(1);");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
    	String virtkey = "";
        //determino se �?abilitata la VirtualKeyboard:
        if (VirtualKeyboard.getInstance().isOnScreenKey())
        {
            virtkey = ";keyboard.js;";
        }
    	
    	Properties p = new Properties();
        p.put("tab1name", "line.js;../arch/actb/actb.js;../arch/actb/common.js"+virtkey);
        p.put("tab2name", "parameters_pri.js;../arch/actb/actb.js;../arch/actb/common.js"+virtkey);
        p.put("tab3name", "note.js"+virtkey);
        p.put("tab4name", "line.js"+virtkey);
        p.put("tab5name", "propagate.js;../arch/FileDialog.js" + virtkey);
        p.put("tab6name", "siteview_menu.js"+virtkey);
        p.put("tab7name", "dependencis.js;dbllistbox.js"+virtkey);

        return p;
    }
    public void setDeviceConfigImport(DeviceConfigImport importer)
    {
    	this.importer = importer;
    }
    public void executePostAction(UserSession us, String tabName, Properties prop)
    	throws Exception
    {
        if( tabName.equals("tab2name") ) {
        	executeTab2PostAction(us, prop);
        	return;
        }
        
    	UserTransaction ut = us.getCurrentUserTransaction();
        String cmd = prop.getProperty("cmd");
        int idsite = us.getIdSite();
        boolean bRestart = true; // used to control engine restart on propagate
        if (cmd.equals("save_alarm_conf"))
        {
        	String value = null, valueNA = null, valueP = null ,valueC = null;
            for (int i = 1; i < 5; i++)
            {
                value = (us.getPropertyAndRemove("c_" + i) != null)
                    ? "TRUE" : "FALSE";
                float valueInt = new Float(us.getPropertyAndRemove("p_" + i)
                                             .toString()).floatValue();
                SystemConfMgr.getInstance().save("priority_" + i, value,
                    valueInt);
                
                valueNA = (us.getPropertyAndRemove("na_" + i) != null)
                	? "TRUE" : "FALSE";
                SystemConfMgr.getInstance().save("noteack_" + i, valueNA,0);
                
             
            }
            
        
            
            value = us.getPropertyAndRemove("multiacknote");
            if (value!=null) 
            	value=value.trim(); 
            else value="";
            SystemConfMgr.getInstance().save("multiacknote", value,0);

            value = us.getPropertyAndRemove("stickyalarms");
            if (value!=null) 
            	value="TRUE"; 
            else value="FALSE";
            SystemConfMgr.getInstance().save("stickyalarms", value,0);
            
            
            
            value = us.getPropertyAndRemove("resend_enable");
            
            if(value==null){
            	
            	 valueC = "FALSE";
            	 SystemConfMgr.getInstance().save("resend_enable", valueC,0);
            	 
            }else{
            	
            	valueC = "TRUE";
            	SystemConfMgr.getInstance().save("resend_enable", valueC,0);
            	
	            value = us.getPropertyAndRemove("resend_frequency");
	            if (value!=null) 
	            	value=value.trim(); 
	            else value="";
	            SystemConfMgr.getInstance().save("resend_frequency", value,0);
	            
	            value = us.getPropertyAndRemove("resend_times");
	            if (value!=null) 
	            	value=value.trim(); 
	            else value="";
	            SystemConfMgr.getInstance().save("resend_times", value,0);
	            
	            valueC = (us.getPropertyAndRemove("resend_channel_F" ) != null)
	                	? "TRUE" : "FALSE";
	            SystemConfMgr.getInstance().save("resend_channel_F", valueC,0);
	            
	            valueC = (us.getPropertyAndRemove("resend_channel_E" ) != null)
	                	? "TRUE" : "FALSE";
	            SystemConfMgr.getInstance().save("resend_channel_E", valueC,0);
	            
	            valueC = (us.getPropertyAndRemove("resend_channel_S" ) != null)
	                	? "TRUE" : "FALSE";
	            SystemConfMgr.getInstance().save("resend_channel_S", valueC,0);
	            
	            valueC = (us.getPropertyAndRemove("resend_channel_R" ) != null)
	                	? "TRUE" : "FALSE";
	            SystemConfMgr.getInstance().save("resend_channel_R", valueC,0);
	            
	            for (int i = 1; i < 5; i++)
	            {
		            valueP = (us.getPropertyAndRemove("resend_priority_" + i) != null)
		                	? "TRUE" : "FALSE";
		            SystemConfMgr.getInstance().save("resend_priority_" + i, valueP,0);
	            }
            
            }
            
            SystemConfMgr.getInstance().refreshSystemInfo();
        }
        else if (cmd.equals("restore_default"))
        {
            for (int i = 1; i < 5; i++)
            {
                us.removeProperty(("c_" + i));
                us.removeProperty(("p_" + i));
                us.removeProperty(("na_" + i));
                SystemConfMgr.getInstance().restoreDefault("priority_" + i);
                SystemConfMgr.getInstance().restoreDefault("noteack_" + i);
            }

            SystemConfMgr.getInstance().refreshSystemInfo();
        }
        else if (cmd.equals("remove_line"))
        {
        	int idline = Integer.parseInt(prop.getProperty("idline"));
            LineBeanList linelist = new LineBeanList();
            LineBean line = linelist.retrieveLineById(idsite, idline);
            boolean isremoved = linelist.removeLine(us, idline);

            //Log rimozione linea
            if (isremoved)
            {
                EventMgr.getInstance().info(new Integer(us.getIdSite()),
                    us.getUserName(), "Config", "W014",
                    new Object[] { new Integer(line.getCode()), line.getComport() });
            }
            else //Non sono riuscito a rimuovere la linea, pertnato non devo fare la richeista di restart motore
            {
                return;
            }

            DirectorMgr.getInstance().mustCreateProtocolFile();
        }

        else if (cmd.equals("save_line"))
        {
            int idline = Integer.parseInt(prop.getProperty("idline"));
            boolean bNewLine = idline <= 0;
            LineBeanList lines = new LineBeanList();
            LineBean line = idline > 0 ? lines.retrieveLineById(idsite, idline) : new LineBean();
            
            //update alarmenable to productinfo
            String smartModbusEnable = prop.getProperty("smart_modbus_enable");
            if(smartModbusEnable!=null && !"".endsWith(smartModbusEnable))
            	ProductInfoMgr.getInstance().getProductInfo().set("smart_modbus_enable", smartModbusEnable);
            
            String[] prot = null;
            
        	String linetype = prop.getProperty("linetype");
            boolean can_save = true;
            
            String comport		= "";
            int baudrate		= 0;
            String protocol		= "";
            String typeprotocol	= "";
        	if( linetype.equalsIgnoreCase("serial") ){
        		comport = "COM" + prop.getProperty("comport");
        		baudrate = Integer.parseInt(prop.getProperty("baudrate"));
            	prot = prop.getProperty("protocol").split("_");
        		protocol = prot[1];
        		typeprotocol = prot[0];
        		
                //check number of lines CAN: only 1 line allowed
            	if( !"CAN".equalsIgnoreCase(line.getTypeprotocol()) && "CAN".equalsIgnoreCase(prot[0]) )
            	{
    	        	int numberOfCanLines = getCanLinesNumber();
    	        	if (numberOfCanLines>0)
    	        	{
    	        		us.getCurrentUserTransaction().setProperty("can_CAN", "KO");
    	        		return;
    	        	}
            	}
        		
        	} else {
        		String proto = prop.getProperty("proto");
        		String[] tokens = new String[0];
        		if(proto!=null){
        			tokens = proto.split("\\.");
        		}
        		if(tokens.length!=2){
        			tokens = new String[]{"SNMPv1","WEBGATE"};
        		}
        		comport = prop.getProperty("address");
        		baudrate = 100;
        		protocol = "LAN."+tokens[1];
        		typeprotocol = tokens[0];//"SNMPv1";
        	}
        	ProtocolBean beanProtocol = new ProtocolBean();
        	String prot_code = beanProtocol.getProtocolCode(protocol); 
            int beginAddr = beanProtocol.getBeginAddr(prot_code);
            int endAddr = beanProtocol.getEndAddr(prot_code);;
        	boolean[] save_desc = new boolean[endAddr+1];
            for(int i = beginAddr; i <= endAddr; i++)
                save_desc[i] = true;
        	
        	if( idline <= 0 ) {
	            line.setIdsite(idsite);
        		if(linetype.equalsIgnoreCase("serial")){
    	            line.setProtocol(prot[1]);
    	            line.setTypeprotocol(prot[0]);
    	            line.setCode(LineBeanList.getCode(prot[0]));
    	            if(line.getCode()<0){
    	            	us.setProperty("lineerror", ""+line.getCode());
    	            	return;
    	            }
    	            line.setComport(comport);
    	            line.setBaudrate(baudrate);
            	} else {
    	            line.setComport(comport);
    	            line.setBaudrate(100);
    	            line.setProtocol(protocol);
    	            line.setTypeprotocol(typeprotocol);
    	            line.setCode(LineBeanList.getCode("LAN"));
            	}

        		idline = line.save();
        	}
        	
            String code = lines.updateLine(idsite, idline, comport, baudrate, protocol, typeprotocol);

            LangUsedBeanList langUsed = new LangUsedBeanList();
            String langDef = us.getDefaultLanguage();
            LangUsedBean[] allLangs = langUsed.retrieveAllLanguage(idsite);
            String[] langsCode = new String[allLangs.length];

            for (int i = 0; i < allLangs.length; i++)
            {
                langsCode[i] = allLangs[i].getLangcode();
            }

            int baseGlobalIndex = 0; //valore temp

            long actualSpaceReq = SpaceHistoricalHelper.actualSpaceRequiredForHist(idsite);
            long freeDiskSpace = SpaceHistoricalHelper.calculateFreeDiskSpace();
            KeyMaxHelper totKeyMax = new KeyMaxHelper();
            DevMdlBeanList listDevMdl = new DevMdlBeanList();

            //set dispositivi nei rispettivi indirizzi
            int[] ids_oldmdl = LineBeanList.getIdMdlOfDeviceOfLine(idsite, idline, endAddr+1);

            boolean can_remove = true;

            DevMdlBean tmpDevMdl = null;
            int iddevmdl = -1;
            StringBuffer iddev2del = new StringBuffer();
            boolean vardpt = false;
            
            for (int i = beginAddr; i <= endAddr; i++)
            {
            	String devVal = prop.getProperty("var" + i);
                int oldDevMdl = ids_oldmdl[i];

                if (oldDevMdl == -1) //non c'era nessun dispositivo all'indirizzo i 
                {
                    if (!devVal.equalsIgnoreCase("empty")) //semplice inserimento di un device
                    {
                    	iddevmdl = Integer.parseInt(devVal);
                    	
                    	if ((tmpDevMdl == null) || ((tmpDevMdl != null) && (iddevmdl != tmpDevMdl.getIddevmdl())))
                    	{
                    		tmpDevMdl = listDevMdl.retrieveById(idsite, langDef, iddevmdl);
                    	}

                        can_save = LineConfig.insertDevice(idsite, code, idline, i, //dentro modifico keymax
                                baseGlobalIndex, langDef, us.getLanguage(), tmpDevMdl,
                                actualSpaceReq, freeDiskSpace, totKeyMax, us.getProfile());

                        if (!can_save)
                        {
                            break;
                        }
                    }
                }
                else //c'era un dispositivo all'indirizzo i
                {
                	String s[] = LineBeanList.getDevInfosOfLineByAddress(idsite, idline, i,langDef);
                    if (devVal.equalsIgnoreCase("empty")) //rimozione di un device da una linea
                    {
                        can_remove = lines.removeDeviceFromLineByAddress(us, idline, i);
                        can_save = true;

                        if (!can_remove)
                        {
                        	vardpt = true;
                        	iddev2del.append(s[0]+";");
                        }
                    }
                    else if (oldDevMdl != (Integer.parseInt(devVal))) //sovrascrittura solo se i modelli di dispositivi sono diversi
                    {
                        can_remove = lines.removeDeviceFromLineByAddress(us, idline, i);

                        iddevmdl = Integer.parseInt(devVal);
                        
                        if (can_remove)
                        {
                        	if ((tmpDevMdl == null) || ((tmpDevMdl != null) && (iddevmdl != tmpDevMdl.getIddevmdl())))
                        	{
                        		tmpDevMdl = listDevMdl.retrieveById(idsite, langDef, iddevmdl);
                        	}

                            can_save = LineConfig.insertDevice(idsite, code, idline, i,
                                    baseGlobalIndex, langDef, us.getLanguage(), tmpDevMdl,
                                    actualSpaceReq, freeDiskSpace, totKeyMax, us.getProfile());

                            if (!can_save)
                            {
                                break;
                            }
                        }
                        else
                        {
                            save_desc[i] = false;
                            vardpt=true;
                            iddev2del.append(s[0]+";");
                        }
                    }
                }
            }

            //update descrizioni dispositivi
            for(int i = beginAddr; i <= endAddr; i++) {
                String tmp_desc = prop.getProperty("desc_" + i);
                if( tmp_desc != null ) {
                    int tmp_id = lines.getIdDeviceOfLineByAddress(idsite, idline, i);
                    if( save_desc[i] == true ) {
                        TableExtBean.updateTableExt(idsite, us.getLanguage(), "cfdevice", tmp_id, tmp_desc);
                        String enab = prop.getProperty("disab_" + i) == null ? "TRUE" : "FALSE";
                        DeviceBean.updateIsEnabled(idsite, tmp_id, enab);
                    }
                }
            }

            LineConfig.updateCfTableExt();

            if (!can_save)
            {
                ut.setProperty("lineok", "NO");
            }
            else
            {
                if (can_remove)
                {
                	EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),
                        "Config", bNewLine ? "W013" : "W015",
                        new Object[] { new Integer(line.getCode()), line.getComport() });
                }
            }
//            us.setProperty("control", str);
            if(vardpt){
            	LangService lang_s = LangMgr.getInstance().getLangService( us.getLanguage());
            	String devs2DltStr = lang_s.getString("vardpd", "dev2dlt");
            	String dltconfirmStr  = lang_s.getString("vardpd", "dltconfirm");
            	
            	String result = devs2DltStr+"\n"+dltconfirmStr; 
            	
            	us.setProperty("control", result);
            	us.setProperty("DevIds2dlt", iddev2del.toString());
            }
            DirectorMgr.getInstance().mustCreateProtocolFile();
        }
        
        else if (cmd.equalsIgnoreCase("propagate"))
		{
            String language = us.getLanguage();
            LangService l = LangMgr.getInstance().getLangService(us.getLanguage());
            String idmast = prop.getProperty("id_master");
            int id_master = Integer.parseInt(idmast);
            String[] ids_all_slaves = prop.getProperty("ids_slaves").split(";");
            ArrayList<Integer> id_slaves = new ArrayList<Integer>();

            //dispositivo master
            DeviceBean master = DeviceListBean.retrieveSingleDeviceById(idsite, id_master, language);

            //retrieve dispositivi slave su cui propagare
            for (int i = 0; i < ids_all_slaves.length; i++)
            {
                if (prop.containsKey("ch_" + ids_all_slaves[i]))
                {
                    id_slaves.add(new Integer(ids_all_slaves[i]));
                }
            }
            //DeviceBean[] slaves = new DeviceBean[id_slaves.size()];
            DeviceListBean dlb = new DeviceListBean(language, id_slaves);

            //caratteristiche da propagare
            boolean alarm = prop.getProperty("alr") != null;
            boolean descr = prop.getProperty("descr") != null;
            boolean um = prop.getProperty("um") != null; //attualmente non usato
            boolean haccp = prop.getProperty("haccp") != null;
            boolean hist = prop.getProperty("hist") != null;
            boolean prior = prop.getProperty("prior") != null;
            boolean graphconf = prop.getProperty("graphconf") != null;
            boolean images = prop.getProperty("images") != null;  //attualmente non usato
            bRestart = alarm || descr || um || haccp || hist || prior || graphconf || images;
            	
            long freespace = 0;  //spazio libero su disco - spazio richiesto ora per la propagazione
            PropagateRes res = new PropagateRes();
            if (alarm||haccp||hist)
            {
            	freespace = SpaceHistoricalHelper.calculateFreeDiskSpace() -
                SpaceHistoricalHelper.actualSpaceRequiredForHist(idsite);
            	
            	res.setFreespace(freespace);
            }
            
            String topropag = "";
            String tot = "";
            // check if there are dependencies on variables instances
            // in case of Log configuration propagation
            if (hist)
            {
	            int idmaster = master.getIddevice();
	            int idslave = -1;
	            
	            VarAggregator varlist_master = new VarAggregator(idsite, language, idmaster, true);
	            VarphyBean[] vars_master = varlist_master.getVarList();
	            ArrayList<Integer> histvarids = new ArrayList<Integer>();
	            
	            Map<Integer,VarphyBean> varmap_master = new HashMap<Integer,VarphyBean>(); 
	            
	            for (int i = 0; i < vars_master.length; i++)
	            {
	            	varmap_master.put(vars_master[i].getIdMdl(), vars_master[i]);
	            }
	            
	            VarDependencyState dependency = new VarDependencyState();
	            
	            for (Iterator<Integer> itr = id_slaves.iterator(); itr.hasNext();)
	            {
	            	Integer iddev = itr.next();
	                DeviceBean slave = dlb.getDevice(iddev);
	                
	                idslave = slave.getIddevice();
	                VarphyBean[] vars_slave = new VarphyBeanList().getLogVarsOfDevice(language, idsite, idslave, "FALSE", new Integer(0));
	                
	                //get ids of all variables for which Log configuration has been 'removed'
	                histvarids.addAll(getHistoricalDeletion(varmap_master, vars_slave, idsite, language));   
	            }
	            
	            int[] checkids = new int[histvarids.size()];
	            
	            for(int i = 0; i<histvarids.size(); i++)
	            {
	            	checkids[i] = histvarids.get(i);
	            }
	            
	            //check var dependencies
	            dependency = LineConfig.checkVariableDependence(idsite, checkids, 1, LineConfig.HISTOR_CHECK, language);
	            
	            if(dependency.dependsOn())
	            {
	            	String message = l.getString("propagate", "cancelled")+"\n";
	            	message += l.getString("propagate", "historerror")+"\n";
	            	message += dependency.getMessagesAsString();
	            	ut.setProperty("propagated",message);
	            	return;
	            }
            }
            
            for (Iterator<Integer> itr = id_slaves.iterator(); itr.hasNext();)
            {
            	Integer iddev = itr.next();
                DeviceBean slave = dlb.getDevice(iddev);
                topropag = "";
                try {
                    if (alarm) {
                        try {
                            Propagate.propagateAlarms(idsite, us.getUserName(),us.getLanguage(), master, slave,res);
                            topropag = topropag + l.getString("propagate", "alarms") + ",";
                        }
                        catch (Exception e) {
                        	 EventMgr.getInstance().error(1, us.getUserName(), "Config", "W051", 
                        			 new Object[]{l.getString("propagate", "alarms"), master.getDescription(), slave.getDescription()});
                        }
                        DirectorMgr.getInstance().mustCreateProtocolFile();
                    }
                    if (descr) {
                        try {
                            Propagate.propagateDescriptions(idsite,
                                us.getProfile(), us.getUserName(), master,
                                slave, us.getLanguage());
                            topropag = topropag +
                                l.getString("propagate", "descr") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W051",
                                    new Object[]
                                    {
                        		l.getString("propagate", "descr"), master.getDescription(),
                                        slave.getDescription()
                                    });
                        }
                    }

                    if (um)
                    {
                        try
                        {
                            Propagate.propagateUnitMeasurement(idsite,
                                us.getProfile(), us.getUserName(), master,
                                slave, us.getLanguage());
                            topropag = topropag +
                                l.getString("propagate", "um") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W051",
                                    new Object[]
                                    {
                        		l.getString("propagate", "um"), master.getDescription(),
                                        slave.getDescription()
                                    });
                        }
                    }

                    if (haccp)
                    {
                        try
                        {
                            Propagate.propagateHACCP(idsite, us.getProfile(),
                                us.getUserName(), master, slave,
                                us.getLanguage(),res);
                            topropag = topropag +
                                l.getString("propagate", "haccp") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W051",
                                    new Object[]
                                    {
                        		l.getString("propagate", "haccp"), master.getDescription(),
                                        slave.getDescription()
                                    });
                        }
                        DirectorMgr.getInstance().mustCreateProtocolFile();
                    }

                    if (hist)
                    {
                        try
                        {
                            Propagate.propagateHistorical(idsite,
                                us.getProfile(), us.getUserName(), master,
                                slave, us.getLanguage(),res);
                            topropag = topropag +
                                l.getString("propagate", "hist") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W051",
                                    new Object[]
                                    {
                        		l.getString("propagate", "hist"), master.getDescription(),
                                        slave.getDescription()
                                    });
                        }
                        DirectorMgr.getInstance().mustCreateProtocolFile();
                    }

                    if (prior)
                    {
                        try
                        {
                            Propagate.propagateVariableVisualization(idsite,
                                us.getProfile(), us.getUserName(), master,
                                slave, us.getLanguage());
                            topropag = topropag +
                                l.getString("propagate", "prior") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W051",
                                    new Object[]
                                    {
                        		l.getString("propagate", "prior"), master.getDescription(),
                                        slave.getDescription()
                                    });
                        }
                    }

                    if (graphconf)
                    {
                        try
                        {
                            Propagate.propagateGraphConf(idsite,
                                us.getProfile(), us.getUserName(),
                                us.getLanguage(), master, slave);
                            topropag = topropag +
                                l.getString("propagate", "graphconf") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W051",
                                    new Object[]
                                    {
                        		l.getString("propagate", "graphconf"), master.getDescription(),
                                        slave.getDescription()
                                    });
                        }
                    }

                    if (images)
                    {
                        try
                        {
                            Propagate.propagateImages(idsite, us.getProfile(),
                                us.getUserName(), master, slave,
                                us.getLanguage());
                            topropag = topropag +
                                l.getString("propagate", "images") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W071",
                                    new Object[]
                                    {
                        		l.getString("propagate", "images"), master.getDescription(),
                                        slave.getDescription()
                                    });
                        }
                    }

                    //Log finale x propagazione corretta
                    if (!topropag.equals(""))
                    {
	                    topropag = topropag.substring(0, topropag.length() - 1);
	
	                    String prop_ok = l.getString("propagate", "propagation_ok");
	                    prop_ok = prop_ok.replace("$1", topropag);
	                    prop_ok = prop_ok.replace("$2", master.getDescription());
	                    prop_ok = prop_ok.replace("$3", slave.getDescription());
	
	                    EventMgr.getInstance().info(1, us.getUserName(), "Config",
	                        "W070",
	                        new Object[]
	                        {
	                            topropag, master.getDescription(),
	                            slave.getDescription()
	                        });
	                    tot = tot+prop_ok+"\n";
                    }
                   
                }
                catch (Exception e)
                {
                }
                
            }
            ut.setProperty("propagated",tot);
            graphProfProp(us,tabName,prop);
        }
        else if (cmd.equalsIgnoreCase("propagate_importdevice"))
		{
        	String topropag = "";
            String tot = "";
            String language = us.getLanguage();
            LangService l = LangMgr.getInstance().getLangService(us.getLanguage());
            String[] ids_all_slaves = prop.getProperty("ids_slaves").split(";");
            ArrayList<Integer> id_slaves = new ArrayList<Integer>();
            int idslave = -1;

            //retrieve dispositivi slave su cui propagare
            for (int i = 0; i < ids_all_slaves.length; i++)
            {
                if (prop.containsKey("ch_" + ids_all_slaves[i]))
                {
                    id_slaves.add(new Integer(ids_all_slaves[i]));
                }
            }
            //DeviceBean[] slaves = new DeviceBean[id_slaves.size()];
            DeviceListBean dlb = new DeviceListBean(language, id_slaves);

            //caratteristiche da propagare
            boolean alarm = prop.getProperty("alr") != null;
            boolean descr = prop.getProperty("descr") != null;
            boolean um = prop.getProperty("um") != null; //attualmente non usato
            boolean haccp = prop.getProperty("haccp") != null;
            boolean hist = prop.getProperty("hist") != null;
            boolean prior = prop.getProperty("prior") != null;
            boolean graphconf = prop.getProperty("graphconf") != null;
            bRestart = alarm || descr || um || haccp || hist || prior || graphconf;
            
            long freespace = 0;  //spazio libero su disco - spazio richiesto ora per la propagazione
            PropagateRes res = new PropagateRes();
            if (alarm||haccp||hist)
            {
            	freespace = SpaceHistoricalHelper.calculateFreeDiskSpace() -
                SpaceHistoricalHelper.actualSpaceRequiredForHist(idsite);
            	
            	res.setFreespace(freespace);
            }
            
            // check if there are dependencies on variables instances
            // in case of Log configuration propagation
            if (hist)
            {   
            	ArrayList<Integer> histvarids = new ArrayList<Integer>();
	            for (Iterator<Integer> itr = id_slaves.iterator(); itr.hasNext();)
	            {
	            	Integer iddev = itr.next();
	                DeviceBean slave = dlb.getDevice(iddev);
	                
	                idslave = slave.getIddevice();
	                
	                RecordSet slavelog = VarphyBeanList.getLogBydevice(idsite,idslave);
	                
	                //get ids of all variables for which Log configuration has been 'removed'
	                histvarids.addAll(getHistoricalDeletion(importer,slavelog));   
	            }
	            
	            int[] checkids = new int[histvarids.size()];
	            
	            for(int i = 0; i<histvarids.size(); i++)
	            {
	            	checkids[i] = histvarids.get(i);
	            }
	            
	            //check var dependencies
	            VarDependencyState dependency = new VarDependencyState();
	            dependency = LineConfig.checkVariableDependence(idsite, checkids, 1, LineConfig.HISTOR_CHECK, language);
	            
	            if(dependency.dependsOn())
	            {
	            	String message = l.getString("propagate", "cancelled")+"\n";
	            	message += l.getString("propagate", "historerror")+"\n";
	            	message += dependency.getMessagesAsString();
	            	ut.setProperty("propagated",message);
	            	return;
	            }
            }
            
            for (Iterator<Integer> itr = id_slaves.iterator(); itr.hasNext();)
            {
            	Integer iddev = itr.next();
                DeviceBean slave = dlb.getDevice(iddev);
                topropag = "";
                try {
                    if (alarm) {
                        try {
                            Propagate.propagateAlarms(idsite, us.getUserName(),us.getLanguage(), importer, slave,res);
                            topropag = topropag + l.getString("propagate", "alarms") + ",";
                        }
                        catch (Exception e) {
                        	 EventMgr.getInstance().error(1, us.getUserName(), "Config", "W051", 
                        			 new Object[]{l.getString("propagate", "alarms"),importer.getFilename() , slave.getDescription()});
                        }
                        DirectorMgr.getInstance().mustCreateProtocolFile();
                    }
                    if (descr) {
                        try {
                            Propagate.propagateDescriptions(idsite,
                                us.getProfile(), us.getUserName(), importer,
                                slave, us.getLanguage());
                            topropag = topropag +
                                l.getString("propagate", "descr") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W051",
                                    new Object[]
                                    {
                        		l.getString("propagate", "descr"), importer.getFilename(),
                                        slave.getDescription()
                                    });
                        }
                    }

                    if (um)
                    {
                        try
                        {
                            Propagate.propagateUnitMeasurement(idsite,
                                us.getProfile(), us.getUserName(), importer,
                                slave, us.getLanguage());
                            topropag = topropag +
                                l.getString("propagate", "um") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W051",
                                    new Object[]
                                    {
                        		l.getString("propagate", "um"), importer.getFilename(),
                                        slave.getDescription()
                                    });
                        }
                    }

                    if (haccp)
                    {
                        try
                        {
                            Propagate.propagateHACCP(idsite, us.getProfile(),
                                us.getUserName(), importer, slave,
                                us.getLanguage(),res);
                            topropag = topropag +
                                l.getString("propagate", "haccp") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W051",
                                    new Object[]
                                    {
                        		l.getString("propagate", "haccp"), importer.getFilename(),
                                        slave.getDescription()
                                    });
                        }
                        DirectorMgr.getInstance().mustCreateProtocolFile();
                    }

                    if (hist)
                    {
                        try
                        {
                            Propagate.propagateHistorical(idsite,
                                us.getProfile(), us.getUserName(), importer,
                                slave, us.getLanguage(),res);
                            topropag = topropag +
                                l.getString("propagate", "hist") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W051",
                                    new Object[]
                                    {
                        		l.getString("propagate", "hist"), importer.getFilename(),
                                        slave.getDescription()
                                    });
                        }
                        DirectorMgr.getInstance().mustCreateProtocolFile();
                    }

                    if (prior)
                    {
                        try
                        {
                            Propagate.propagateVariableVisualization(idsite,
                                us.getProfile(), us.getUserName(), importer,
                                slave, us.getLanguage());
                            topropag = topropag +
                                l.getString("propagate", "prior") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W051",
                                    new Object[]
                                    {
                        		l.getString("propagate", "prior"), importer.getFilename(),
                                        slave.getDescription()
                                    });
                        }
                    }

                    if (graphconf)
                    {
                        try
                        {
                        	ProfileBeanList profiles = new ProfileBeanList(idsite,false);
                        	String updatesameprofcode = prop.getProperty("updatesameprofcode");
                        	int[] idprofile = null;
                        	if(updatesameprofcode == null || !updatesameprofcode.equals("true"))
                        	{
                        		idprofile = new int[1];
                        		idprofile[0] = 0;
                        	}
                        	else
                        	{
                        		idprofile = getSameProfileId(importer.getGraphconfigMap(),profiles);
                        	}
                            Propagate.propagateGraphConf(idsite,
                                idprofile, us.getUserName(),
                                us.getLanguage(), importer, slave,profiles);
                            topropag = topropag +
                                l.getString("propagate", "graphconf") + ",";
                        }
                        catch (Exception e)
                        {
                        	EventMgr.getInstance().error(1, us.getUserName(), "Config",
                                    "W051",
                                    new Object[]
                                    {
                        		l.getString("propagate", "graphconf"), importer.getFilename(),
                                        slave.getDescription()
                                    });
                        }
                    }

                    //Log finale x propagazione corretta
                    if (!topropag.equals(""))
                    {
	                    topropag = topropag.substring(0, topropag.length() - 1);
	
	                    String prop_ok = l.getString("propagate", "propagation_ok");
	                    prop_ok = prop_ok.replace("$1", topropag);
	                    prop_ok = prop_ok.replace("$2", importer.getFilename());
	                    prop_ok = prop_ok.replace("$3", slave.getDescription());
	
	                    EventMgr.getInstance().info(1, us.getUserName(), "Config",
	                        "W070",
	                        new Object[]
	                        {
	                            topropag, importer.getFilename(),
	                            slave.getDescription()
	                        });
	                    tot = tot+prop_ok+"\n";
                    }
                   
                }
                catch (Exception e)
                {
                }
                
            }
            ut.setProperty("propagated",tot);
            graphProfProp(us,tabName,prop);
        }
        else if (cmd.equalsIgnoreCase("propagate_plus"))
        {
            bRestart = false;
        	graphProfProp(us,tabName,prop);
        }
        else if( cmd.equalsIgnoreCase("auto_detect") )
        {
        	Integer idline = Integer.parseInt(prop.getProperty("idline"));
        	if( idline > 0 )
        		us.setProperty("line", idline.toString());
        	ut.setProperty("cmd", "auto_detect");
        	String linetype = prop.getProperty("linetype");
        	ut.setProperty("linetype", linetype);
        	if( linetype.equals("serial") ) {
	        	ut.setProperty("comport", prop.getProperty("comport"));
	        	ut.setProperty("baudrate", prop.getProperty("baudrate"));
	        	ut.setProperty("protocol", prop.getProperty("protocol"));
	        	
	    		DeviceDetectBean detectBean = new DeviceDetectBean(us);
	    		detectBean.initDeviceDetection(Integer.parseInt(prop.getProperty("comport")),
	    				Integer.parseInt(prop.getProperty("baudrate")),
	    				prop.getProperty("protocol"));
	    		Map<Integer, DeviceBean> devices = detectBean.getDevices();
	    		ut.setAttribute("auto_detect", devices);
        	}
        	else if( linetype.equals("lan") ) {
	        	ut.setProperty("address", prop.getProperty("address"));
//	        	ut.setProperty("proto", prop.getProperty("proto").replaceFirst("AK255", "LAN"));
//	    		Map<Integer, DeviceBean> devices = com.carel.supervisor.field.dataconn.impl.ak255.DriverMgr.getInstange().getDevices(prop.getProperty("address"));
//	    		ut.setAttribute("auto_detect", devices);
        	}
        }
        else if( cmd.equalsIgnoreCase("port_detect1") )
        {
        	Integer idline = Integer.parseInt(prop.getProperty("idline"));
        	if( idline > 0 )
        		us.setProperty("line", idline.toString());
        	ut.setProperty("cmd", "port_detect1");
        	ut.setProperty("linetype", prop.getProperty("linetype"));
        	ut.setProperty("comport", prop.getProperty("comport"));
        	ut.setProperty("baudrate", prop.getProperty("baudrate"));
        	ut.setProperty("protocol", prop.getProperty("protocol"));
        }
        else if( cmd.equalsIgnoreCase("port_detect2") )
        {
        	Integer idline = Integer.parseInt(prop.getProperty("idline"));
        	if( idline > 0 )
        		us.setProperty("line", idline.toString());
        	ut.setProperty("cmd", "port_detect2");
        	
        	PortInfo pi = new PortInfo();
        	String port_detect = prop.getProperty("portXdetect");
        	String comport = "" + pi.getPort(port_detect);
        	ut.setProperty("linetype", prop.getProperty("linetype"));       	
        	ut.setProperty("comport", comport);
        	ut.setProperty("baudrate", prop.getProperty("baudrate"));
        	ut.setProperty("protocol", prop.getProperty("protocol"));
        }
        else if( cmd.equalsIgnoreCase("add_lineselect") )
        {
        	ut.setProperty("cmd", "add_lineselect");
        	
        	PortInfo pi = new PortInfo();
        	String port_detect = prop.getProperty("portXdetect");
        	String comport = "" + pi.getPort(port_detect);
        	ut.setProperty("comport", comport);
        }
        else if (cmd.equalsIgnoreCase("menu_desc"))
        {
        	String sql = "update cftext set description=? where idsite=? and languagecode=? and code=? and subcode=? ";
        	String lang_code = us.getLanguage();
        	
        	MenuSection[] listSection = MenuConfigMgr.getInstance().getMenuSection();
        	List<Object[]> upd = new ArrayList<Object[]>();
        	String menu = "";
        	String child_info = "";
        	String code = "";
        	String subcode = "";
        	
        	for(int i=0; i<listSection.length; i++)
    		{
        		MenuSection mn = listSection[i];
        		for (int j=0;j<mn.getListVoci().length;j++)
        		{
        			MenuVoce section = mn.getListVoci()[j];
        			menu = section.getName();
        			
        			insertUpdOject(upd,menu,lang_code,idsite,prop,false,null,null);
        			
        			if (FunctionalityHelper.hasChild(menu)!=null)
        			{
        				child_info = FunctionalityHelper.hasChild(menu);
        				menu = child_info.split(";")[0];
        				code = child_info.split(";")[1];
        				subcode = child_info.split(";")[2];
        				insertUpdOject(upd,menu,lang_code,idsite,prop,true,code,subcode);
        			}
        		}
    		}
        	
        	//add ICONS MENU description
        	upd.add(new Object[]{prop.getProperty("d_section5_val"),idsite,lang_code,"menu","section5"});
        	upd.add(new Object[]{prop.getProperty("d_section2_val"),idsite,lang_code,"menu","section2"});
        	upd.add(new Object[]{prop.getProperty("d_section3_val"),idsite,lang_code,"menu","section3"});
        	upd.add(new Object[]{prop.getProperty("d_section4_val"),idsite,lang_code,"menu","section4"});
        	upd.add(new Object[]{prop.getProperty("d_section7_val"),idsite,lang_code,"menu","section7"});
        	upd.add(new Object[]{prop.getProperty("d_section8_val"),idsite,lang_code,"menu","section8"});
        	upd.add(new Object[]{prop.getProperty("d_section9_val"),idsite,lang_code,"menu","section9"});
        	// add Global description
        	upd.add(new Object[]{prop.getProperty("d_entry1_val"),idsite,lang_code,"menu","entry1"});
        	
        	DatabaseMgr.getInstance().executeMultiStatement(null, sql, upd);
        	
        	//reload languages
        	LangMgr.getInstance().init();
        	
        }
        
        else if (cmd.equals("remove_dpdt"))
        {
        	String language = us.getLanguage();
        	
        	String devicesToRemoved = prop.getProperty("devicesToRemoved");
        	String devs[] = devicesToRemoved.split(";");
        	
        	LineBeanList linelist = new LineBeanList();
        	
        	for (String iddevice : devs) {
				LineConfig.dltDeviceDependence(idsite,Integer.parseInt(iddevice),LineConfig.ALL_CHECK,language,false);
				linelist.removeDevicesFromLineByIddev(idsite, Integer.parseInt(iddevice), language);
				
			}
        	String sql = "delete from cfline where  idline not in  "+
        					" ( select distinct idline from cfdevice where iscancelled ='FALSE')";
        	
        	DatabaseMgr.getInstance().executeStatement(null, sql, null);
        	
            DirectorMgr.getInstance().mustCreateProtocolFile();
        }
        
        LineConfig.reorderGlobalIndexOfDevice(idsite);
        us.getGroup().reloadDeviceStructureList(idsite, us.getLanguage());
        
        //add if condition by Kevin. for export device configuration, it is no need to restart engine
        if( bRestart && !cmd.equalsIgnoreCase("exportdevice") && !cmd.equalsIgnoreCase("menu_desc") && !cmd.equalsIgnoreCase("add_lineselect")
        	&& !cmd.equalsIgnoreCase("auto_detect") && !cmd.equalsIgnoreCase("port_detect1") && !cmd.equalsIgnoreCase("port_detect2"))
        {
        	DirectorMgr.getInstance().mustRestart();
        }
        
        // force to compute logged variables
        PvproInfo.getInstance().resetLoggedVariables();
    }
    private int[] getSameProfileId(Map map,ProfileBeanList profiles)
    {
    	ArrayList list = new ArrayList();
    	Iterator it = map.entrySet().iterator();
    	while(it.hasNext())
    	{
    		Map.Entry<String, ProfileBean> entry = (Map.Entry<String, ProfileBean>)it.next();
    		String key = entry.getKey();
    		for(int i=0;i<profiles.size();i++)
    		{
    			ProfileBean bean = profiles.getProfile(i);
    			if(bean.getCode().equals(key))
    			{
    				list.add(new Integer(bean.getIdprofile()));
    				break;
    			}
    		}
    	}
    	int[] profileid = new int[list.size()];
    	for(int i =0;i<list.size();i++)
    	{
    		profileid[i] = (Integer)list.get(i);
    	}
    	return profileid;
    }
    
    private void insertUpdOject(List<Object[]> upd, String menu, String lang, int idsite, Properties prop,boolean ischild, String code, String subcode)
    {
    	
    	MenuTab mt = null;
    	if (prop.getProperty("d_"+menu+"_val")!=null)
    	{
    		//insert for menu
    		if (!ischild)
    			upd.add(new Object[]{prop.getProperty("d_"+menu+"_val"),idsite,lang,"menu",menu});
    		else
    			upd.add(new Object[]{prop.getProperty("d_"+menu+"_val"),idsite,lang,code,subcode});
    		
    		mt = MenuTabMgr.getInstance().getTabMenuFor(menu);
			for (int i=0;i<mt.getNumTab();i++)
	    	{
				
				String tabname = mt.getTab(i).getIdTab();
				if (prop.getProperty("d_"+menu+"_"+tabname+"_val")!=null)
		    	{
					//insert for tabs
					upd.add(new Object[]{prop.getProperty("d_"+menu+"_"+tabname+"_val"),idsite,lang,menu,tabname});
		    	}
	    	}
    	}
    }

    private int getCanLinesNumber() throws DataBaseException
    {
    	int number = 0;
		String sql = "select count(1) from cfline where typeprotocol='CAN'";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		if (rs!=null && rs.size()!=0)
		{
			number = (Integer) rs.get(0).get(0);
		}
		return number;
	}

	public static void main(String[] argv) throws Exception
    {
        BaseConfig.init();

        Properties p = new Properties();
        p.setProperty("cmd", "save_line");
        p.setProperty("protocol", "CAREL_RS485N");
        p.setProperty("baudrate", "19200");
        p.setProperty("comport", "10");
        p.setProperty("linecode", "1");

        StringBuffer n = new StringBuffer();
        StringBuffer n1 = new StringBuffer();

        for (int i = 1; i <= 108; i++)
        {
            p.setProperty("varmdl" + i, String.valueOf(i));
            n1.append(i);
            n1.append(",");
            n.append(i);

            if (i < 108)
            {
                n.append(",");
            }
        }

        for (int i = 109; i <= 207; i++)
        {
            n1.append("empty");

            if (i < 207)
            {
                n1.append(",");
            }
        }

        p.setProperty("serials", n.toString());
        p.setProperty("devices", n1.toString());

        BSiteView o = new BSiteView("IT_it");
        UserSession u = new UserSession(new UserProfile());
        u.setIdSite(1);
        u.setLanguage("IT_it");
        o.executePostAction(u, null, p);
    }

    public String executeDataAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
        if( tabName.equals("tab2name") )
        	return executeTab2DataAction(us, prop);
    	
    	StringBuffer response = new StringBuffer("<response>");
        String cmd = prop.getProperty("cmd");

        if (cmd.equalsIgnoreCase("load_slaves"))
        {
            String idm = prop.getProperty("id_master");
            int id_master = -1;

            if (idm != null)
            {
                id_master = Integer.parseInt(idm);
            }

            //retrieve dispositivi 
            GroupListBean group = us.getGroup();
            int[] ids_group = group.getIds();
            DeviceListBean deviceList = new DeviceListBean(us.getIdSite(),
                    us.getLanguage(), ids_group);

            LineBeanList linelist = new LineBeanList();
            linelist.retrieveLines(us.getIdSite());

            DeviceBean tmp_dev = null;
            int[] ids_dev = deviceList.getIds();

            DeviceBean master = deviceList.getDevice(id_master);

            String dev_code = PageImpExp.getDeviceCode(us.getIdSite(), id_master);
            response.append("<fileInfo addr=\""+master.getCode()+"\" code=\""+dev_code+"\"/>");
            // creo xml da restituire 
            for (int i = 0; i < deviceList.size(); i++)
            {
                tmp_dev = deviceList.getDevice(ids_dev[i]);

                //ritorno tutti dispositivi dello stesso modello del master ed escludo il master
                if ((tmp_dev.getIddevice() != id_master) &&
                        (tmp_dev.getIddevmdl() == master.getIddevmdl()))
                {
                    response.append("<device id=\"" + tmp_dev.getIddevice() +
                        "\">");
                    response.append("<![CDATA[" + tmp_dev.getDescription() +
                        "]]>");
                    response.append("<![CDATA[" +
                        linelist.getLineById(tmp_dev.getIdline()).getCode() +
                        "]]>");
                    response.append("</device>");
                }
            }
        }
        else if("export".equalsIgnoreCase(cmd))
        {

       	 	String idmast = prop.getProperty("id_master");
            int id_master = Integer.parseInt(idmast);
            String local = prop.getProperty("local");
            String file = "";
            if("true".equalsIgnoreCase(local))
            {
            	String path = prop.getProperty("path");
            	file = path;
            }
            else
            {
            	DevMdlBeanList mdlBeanList = new DevMdlBeanList();
    	        DevMdlBean master = mdlBeanList.retrieveByDeviceId(us.getIdSite(), us.getLanguage(), id_master);
    	        DeviceStructureList deviceStructureList = us.getGroup().getDeviceStructureList();
            	DeviceStructure deviceStructure = deviceStructureList.get(id_master);
            	String code = deviceStructure.getCode();
            	file = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator +
            		"Config_"+master.getCode()+"_"+code+"_"+DateUtils.date2String(new Date(), "yyyyMMddhhmmss")+".DCFG";
            }
            
            String tot = Propagate.DeviceExport(us.getIdSite(),us.getLanguage(),id_master,us.getProfile(),file);
            return tot;
        }
        else if(cmd.equalsIgnoreCase("load_slaves_importdevice"))
        {
        	String devmdlcode = prop.getProperty("devmdlcode");
        	
        	DevMdlBean mdlBean = new DevMdlBeanList().retrieveByCode(us.getIdSite(), us.getLanguage(), devmdlcode);
            //retrieve dispositivi 
            GroupListBean group = us.getGroup();
            int[] ids_group = group.getIds();
            DeviceListBean deviceList = new DeviceListBean(us.getIdSite(),
                    us.getLanguage(), ids_group);

            LineBeanList linelist = new LineBeanList();
            linelist.retrieveLines(us.getIdSite());

            DeviceBean tmp_dev = null;
            int[] ids_dev = deviceList.getIds();

            // creo xml da restituire 
            for (int i = 0; i < deviceList.size(); i++)
            {
                tmp_dev = deviceList.getDevice(ids_dev[i]);

                //ritorno tutti dispositivi dello stesso modello del master ed escludo il master
                if (tmp_dev.getIddevmdl() == mdlBean.getIddevmdl())
                {
                    response.append("<device id=\"" + tmp_dev.getIddevice() +
                        "\">");
                    response.append("<![CDATA[" + tmp_dev.getDescription() +
                        "]]>");
                    response.append("<![CDATA[" +
                        linelist.getLineById(tmp_dev.getIdline()).getCode() +
                        "]]>");
                    response.append("</device>");
                }
            }
        }else if(cmd.equalsIgnoreCase("dependencies_fresh")){
        	String devids = prop.getProperty ("devids");
        	us.setProperty("DevIds2dlt", devids);
        	LineBeanList linelist = new LineBeanList();
        	String dpdLists = linelist.allVariablesDependencies(us,new VarDependencyList());
        	response.append(dpdLists);
        }

        response.append("</response>");

        return response.toString();
    }
    
    
    public String getProfiles()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<select class='standardTxt' name=\"prolist\" id=\"prolist\" onchange=\"loadTbProf(this);\">");
        String sql = "select idprofile,code from profilelist where idprofile >= 0";
        int id = 0;
        String code = "";
        sb.append("<option value=\"-9999\">---------------</option>");
        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
            Record r = null;
            if(rs != null)
            {
                for(int i=0; i<rs.size(); i++)
                {
                    r = rs.get(i);
                    if(r != null)
                    {
                        id = ((Integer)r.get("idprofile")).intValue();
                        code = UtilBean.trim(r.get("code"));
                        sb.append("<option value=\""+id+"\">"+code+"</option>");
                    }
                }
            }
        }
        catch(Exception e) {
        }
        sb.append("</select>");
        return sb.toString();
    }
    
    private void graphProfProp(UserSession us,String tabName,Properties prop)
    {
        String propon = "";
        String[] ids_all_prof = prop.getProperty("ids_profiles").split(";");
        ArrayList<String> idListToSet = new ArrayList<String>();
        if(ids_all_prof != null)
        {
            for (int i=0; i<ids_all_prof.length; i++)
            {
                propon = prop.getProperty("chp_"+ids_all_prof[i]);
                if(propon == null)
                    propon = "off";
                
                if(propon.equalsIgnoreCase("ON"))
                    idListToSet.add(ids_all_prof[i]);
            }
        }
        
        try
        {
            int masterForWork = Integer.parseInt(prop.getProperty("prolist"));
            int[] listForWork = new int[idListToSet.size()];
            for (int i=0; i<listForWork.length; i++)
                listForWork[i] = Integer.parseInt((String)idListToSet.get(i));
            
            Propagher.work(us,masterForWork, listForWork);
        }
        catch(Exception e) {
            
        }
    }
    
    private ArrayList<Integer> getHistoricalDeletion(Map<Integer, VarphyBean> varmap_master, VarphyBean[] vars_slave, int idsite, String language) throws Exception
    {
         VarphyBean master_var = null;
         VarphyBean slave_var = null;
         
         ArrayList<Integer> varids = new ArrayList<Integer>();
         
         for (int i = 0; i < vars_slave.length; i++) //for - all slave variables
         {
        	 slave_var = vars_slave[i]; //slave var
             master_var = (VarphyBean) varmap_master.get(slave_var.getIdMdl()); //master var
             
	         // if SLAVE var has Log configuration active
	         if (slave_var.getIdhsvariable().intValue() != -1)
	         {
	             // if MASTER var HAS NOT log configuration active    
	             if (master_var != null && master_var.getIdhsvariable().intValue() == -1)
	             {	
	            	 varids.add(slave_var.getId().intValue());
	             }
	         }
         }
         return varids;
    }
    private ArrayList<Integer> getHistoricalDeletion(DeviceConfigImport importer,RecordSet slavelog)
    {
    	ArrayList<Integer> varids = new ArrayList<Integer>();
    	if(slavelog != null && slavelog.size()>0)
    	{
	    	for(int i=0;i<slavelog.size();i++)
	    	{
	    		Record var = slavelog.get(i);
	    		String code = UtilBean.trim(var.get(VarphyBean.CODE));
	    		VarphyBean master = importer.getlogVarByCode(code);
	    		if(master == null)
	    		{
	    			varids.add((Integer)var.get(VarphyBean.ID));
	    		}
	    	}
    	}
    	return varids;
    }
    // to be removed
    public void saveSiteinfo(UserSession us,Properties prop)
    	throws Exception
    {
    	SiteInfoList sites = new SiteInfoList();
    	int idsite = us.getIdSite();
    	SiteInfo site = sites.getById(idsite);
        String site_name = prop.getProperty("site_name");
        String site_id = prop.getProperty("site_id");
        String site_phone = prop.getProperty("site_phone");
        String site_password = prop.getProperty("site_password");

        site.updateSite(site_name, site_id, site_phone, site_password);
        us.setSiteName(site_name);
    }
    
    
    // parameters set by priority
    public void executeTab2PostAction(UserSession us, Properties prop)
    {
    	String cmd = prop.getProperty("cmd");
        if( "select".equals(cmd) ) {
        	Integer idDevMdl = Integer.parseInt(prop.get("idDevMdl").toString());
        	Integer idVarGroup = Integer.parseInt(prop.get("idVarGroup").toString());
        	ParamBean beanParam = (ParamBean)us.getCurrentUserTransaction().getAttribute("ParamBean");
        	if( beanParam == null || beanParam.getIdDevMdl() != idDevMdl ) {
        		if( idDevMdl > 0 ) {
        			beanParam = new ParamBean(us.getIdSite(), idDevMdl, us.getLanguage());
        			us.getCurrentUserTransaction().setAttribute("ParamBean", beanParam);
        		}
        		else {
        			us.getCurrentUserTransaction().removeAttribute("ParamBean");
        			beanParam = null;
        		}
        	}
        	if( beanParam != null )
        		beanParam.setVarGroup(idVarGroup);
        } else if( "save".equals(cmd) ) {
        	String listVar = prop.get("listVar").toString();
        	String listPri = prop.get("listPri").toString();
        	ParamBean beanParam = (ParamBean)us.getCurrentUserTransaction().getAttribute("ParamBean");
        	beanParam.setPriorities(listVar, listPri);
        }
    }
    
    
    public String executeTab2DataAction(UserSession us, Properties prop)
    {
    	return "";
    }
}
