package com.carel.supervisor.presentation.bo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.database.RuleBean;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.field.InternalRelayMgr;
import com.carel.supervisor.presentation.assistance.GuardianConfig;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;
import com.carel.supervisor.presentation.bean.rule.RelayBean;
import com.carel.supervisor.presentation.bean.rule.RelayBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.io.CioDIAL;
import com.carel.supervisor.presentation.io.CioEVT;
import com.carel.supervisor.presentation.io.CioFAX;
import com.carel.supervisor.presentation.io.CioMAIL;
import com.carel.supervisor.presentation.io.CioPrinter;
import com.carel.supervisor.presentation.io.CioSMS;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTableDyn;
import com.carel.supervisor.remote.bean.IncomingBeanList;
import com.carel.supervisor.remote.manager.RasServerMgr;


public class BSetIo extends BoMaster
{
    private static final int REFRESH_TIME = -1;
    private int screenw = 1024;
    private int screenh = 768;
    RelayBeanList relayList = null;
    int[] relayid = null;
    public BSetIo(String l)
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
        p.put("tab1name", "IO_FaxLoad();");
        p.put("tab2name", "IO_SmsLoad();");
        p.put("tab3name", "IO_MailLoad();");
        p.put("tab4name", "IO_showMsg();enableAction(1);");
        p.put("tab5name", "enableAction(1);enableAction(2);onWindowLoaded();");
        p.put("tab6name", "IO_onload();");
        p.put("tab7name", "enableAction(1);");
        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        //20091126-simon.zhang
        //append the virtual keyboard        
        p.put("tab1name", "setio.js;keyboard.js;");
        p.put("tab2name", "setio.js;keyboard.js;");
        p.put("tab3name", "setio.js;keyboard.js;");
        p.put("tab4name", "setio.js;keyboard.js;");
        p.put("tab5name", "setio.js;../arch/FileDialog.js;keyboard.js;");
        p.put("tab6name", "setio.js;keyboard.js;");
        p.put("tab7name", "setio.js;keyboard.js;");
        return p;
    }

	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab5name", DOCTYPE_STRICT);
		return p;
    }    
    
    public void executePostAction(UserSession us, String tabName, Properties prop)
        throws Exception
    {
        boolean ris = true;
        
        if (tabName.equalsIgnoreCase("tab1name"))
        {
            boolean bgr = GuardianConfig.isRecipient(GuardianConfig.FAX_RECIPIENT);
        	ris = saveDataFax(us, tabName, prop);
            if( ris && bgr )
            	GuardianConfig.saveConfiguration(us.getIdSite(), us.getSiteName());
        }
        else if (tabName.equalsIgnoreCase("tab2name"))
        {
            boolean bgr = GuardianConfig.isRecipient(GuardianConfig.SMS_RECIPIENT);
        	ris = saveDataSms(us, tabName, prop);
            if( ris && bgr )
            	GuardianConfig.saveConfiguration(us.getIdSite(), us.getSiteName());
        }
        else if (tabName.equalsIgnoreCase("tab3name"))
        {
            boolean bgr = GuardianConfig.isRecipient(GuardianConfig.EMAIL_RECIPIENT);
        	ris = saveDataMail(us, tabName, prop);
            if( ris && bgr )
            	GuardianConfig.saveConfiguration(us.getIdSite(), us.getSiteName());
        }
        else if (tabName.equalsIgnoreCase("tab4name"))
        {
            ris = saveDataDial(us, tabName, prop);
        }
        else if (tabName.equalsIgnoreCase("tab5name"))
        {
            if (prop.getProperty("cmd") != null)
            {
                if ("save".equals(prop.getProperty("cmd")))
                {
                    saveDataEvent(us, tabName, prop);
                }
                else if ("remove".equals(prop.getProperty("cmd")))
                {
                    //resetta campo suono *.wav corrente:
                    resetSound(us, prop);
                }
            }
        }
        else if (tabName.equalsIgnoreCase("tab6name"))
        {
        	saveDataRelay(us, tabName, prop);
        }
        else if (tabName.equalsIgnoreCase("tab7name"))
        {
        	saveDataPrinter(us, tabName, prop);
        }

        if (!ris)
        {
            us.getCurrentUserTransaction().setProperty("state", "ko");
        }
    }
		
    private boolean saveDataFax(UserSession us, String tabName, Properties prop)
    {
        int idconf = Integer.parseInt(prop.getProperty("iocfgid"));
        String modem = prop.getProperty("iomodeml");
        String type = prop.getProperty("iomodemtype");
        int trynum = Integer.parseInt(prop.getProperty("iotrynum"));
        int retry = Integer.parseInt(prop.getProperty("ioretryafter"));
        String centra = prop.getProperty("iocentralino");
        boolean esy = false;
        boolean isrem = true;
        
        if (modem != null)
        {
            CioFAX fax = new CioFAX(us.getIdSite());
            fax.loadConfiguration();
            String modem_old = fax.getModemId();
            if (!modem.equalsIgnoreCase("nop"))
            {
                esy = fax.saveConfiguration(idconf, modem, type, trynum, retry, centra);
            }
            else
            {
                esy = fax.removeConfiguration(idconf);
                isrem = esy;
            }
            
            // if modem is changed, then dispatcher configuration is reloaded
            
            if(modem_old == null || "".equals(modem_old))
    		{
    			modem_old = "nop";
    		}
    		if(esy && !modem.equals(modem_old))
    		{
    			DispatcherMgr.getInstance().reloadConfiguration();
    		}
        }
        
        boolean ris = false;
        ris = updateBookAddress(us, prop, "F");
        
        if (ris)
        {
	        // Azione automatica
	        String execTech = prop.getProperty("executetechact");
	        if(execTech != null && execTech.equalsIgnoreCase("T"))
	        {
	        	setAutomaticAction(us.getIdSite(),us.getUserName(),"F","PVSendFax.rtf","FAXACTIONTECH");
	        	//DirectorMgr.getInstance().mustRestart();
	        }
	        
	        // Regola automatica
	        if(execTech != null && execTech.equalsIgnoreCase("R"))
	        {
	            ris = setAutomaticRule(us.getIdSite(),us.getUserName(),"F");
	            //DirectorMgr.getInstance().mustRestart();
	        }
        }
        
        return ris & isrem;
    }

    private boolean saveDataSms(UserSession us, String tabName, Properties prop)
    {
        int idconf = Integer.parseInt(prop.getProperty("iocfgid"));
        String modem = prop.getProperty("iomodeml");
        String type = prop.getProperty("iomodemtype");
        int provider = Integer.parseInt(prop.getProperty("iosmsprovider"));
        String call;

        if (type.equalsIgnoreCase("G"))
        { // GSM
            call = "0";
        }
        else
        {
            call = prop.getProperty("iosmscall");
        }

        int trynum = Integer.parseInt(prop.getProperty("iotrynum"));
        int retry = Integer.parseInt(prop.getProperty("ioretryafter"));
        String centra = prop.getProperty("iocentralino");
        String labprovider = prop.getProperty("iolblprovider");

        if (labprovider != null)
        {
            labprovider = labprovider.trim();
        }

        boolean esy = false;
        boolean isrem = true;
        
        if (modem != null)
        {
            CioSMS sms = new CioSMS(us.getIdSite());
            sms.loadConfiguration();
            String modem_old = sms.getLabelModem();
            String provider_old=sms.getProviderLb();
            
            if (!modem.equalsIgnoreCase("nop"))
            {
                esy = sms.saveConfiguration(idconf, modem, type, provider, call, trynum, retry,
                        centra, labprovider);
            }
            else
            {
                esy = sms.removeConfiguration(idconf);
                isrem = esy;
            }
            
            // if modem is changed, then dispatcher configuration is reloaded
            
            if(modem_old == null || "".equals(modem_old))
    		{
    			modem_old = "nop";
    		}
    		// in this case (SMS channel) it is necessary to reload configuration also if
            // the provider selected is changed
            if(esy && (!modem.equals(modem_old) || !labprovider.equals(provider_old)))
    		{
    			DispatcherMgr.getInstance().reloadConfiguration();
    		}         

        }
        
        boolean ris = false;
        ris = updateBookAddress(us, prop, "S");
        
        if (ris)
        {
	        // Azione automatica
	        String execTech = prop.getProperty("executetechact");
	        if(execTech != null && execTech.equalsIgnoreCase("T"))
	        {
	        	setAutomaticAction(us.getIdSite(),us.getUserName(),"S","","SMSACTIONTECH");
	        	//DirectorMgr.getInstance().mustRestart();
	        }
	        
	        // Regola automatica
	        if(execTech != null && execTech.equalsIgnoreCase("R"))
	        {
	            ris = setAutomaticRule(us.getIdSite(),us.getUserName(),"S");
	            //DirectorMgr.getInstance().mustRestart();
	        }
        }
        
        return ris & isrem;
    }

    private boolean saveDataMail(UserSession us, String tabName, Properties prop)
    {
        int idconf = Integer.parseInt(prop.getProperty("iocfgid"));
        String smtp = prop.getProperty("iomailsmtp");
        String sender = prop.getProperty("iomailsender");
        String provider = prop.getProperty("iomailprovider");
        String type = prop.getProperty("iomailtype");
        int trynum = 0;
        int retry = 0;
        int port = 25;
        
        // User e Pass per autenticazione su SMTP
        String smtpUser = prop.getProperty("iomailsenderuser");
        String smtpPass = prop.getProperty("iomailsenderpass");
        
        String encryption = prop.getProperty("iomailencryption") != null ? "TLS" : "NONE";
        
        try {
        	trynum = Integer.parseInt(prop.getProperty("iotrynum"));
        }
        catch(Exception e) {
        }
        try {
        	retry = Integer.parseInt(prop.getProperty("ioretryafter"));
        }
        catch(Exception e) {
        }
        try {
        	port = Integer.parseInt(prop.getProperty("iomailport"));
        }
        catch(Exception e) {
        }
        
        
        boolean esy = false;
        
        String value = us.getPropertyAndRemove("email_log");
        try {
        	String valueC = value == null ? "FALSE" : "TRUE";
        	SystemConfMgr.getInstance().save("email_log", valueC, 0);
        	SystemConfMgr.getInstance().refreshSystemInfo();
        } catch (Exception localException3) {
        }
        
        if ((smtp != null) && (smtp.trim().length() > 0) && (sender != null) &&
                (sender.trim().length() > 0))
        {
            if ((type != null) && type.equalsIgnoreCase("L"))
            {
                CioMAIL mail = new CioMAIL(us.getIdSite());
                mail.setUser(smtpUser);
                mail.setPass(smtpPass);
                mail.setEncryption(encryption);
                mail.setPort(port);
                esy = mail.saveConfiguration(idconf, smtp, sender, type, provider, trynum, retry);

                if (esy)
                {
                    DispMemMgr.getInstance().storeConfiguration("E");
                }
            }
            else if ((provider != null) && !provider.equalsIgnoreCase("nop"))
            {
                CioMAIL mail = new CioMAIL(us.getIdSite());
                mail.setUser(smtpUser);
                mail.setPass(smtpPass);
                mail.setEncryption(encryption);
                mail.setPort(port);
                esy = mail.saveConfiguration(idconf, smtp, sender, type, provider, trynum, retry);

                if (esy)
                {
                    DispMemMgr.getInstance().storeConfiguration("E");
                }
            }
        }
        
        boolean ris = false;
        ris = updateBookAddress(us, prop, "E");
        
        if (ris)
        {
	        // Azione automatica
	        String execTech = prop.getProperty("executetechact");
	        if(execTech != null && execTech.equalsIgnoreCase("T"))
	        {
	        	setAutomaticAction(us.getIdSite(),us.getUserName(),"E",BSetAction.UNSCHEDULERTEMPLATE,"EMAILACTIONTECH");
	        	//DirectorMgr.getInstance().mustRestart();
	        }
	        
	        // Regola automatica
	        if(execTech != null && execTech.equalsIgnoreCase("R"))
	        {
	            ris = setAutomaticRule(us.getIdSite(),us.getUserName(),"E");
	            //DirectorMgr.getInstance().mustRestart();
	        }
        }
        
        return ris;
    }

    private boolean saveDataDial(UserSession us, String tabName, Properties prop)
    {
        int idconf = Integer.parseInt(prop.getProperty("iocfgid"));
        String modem = prop.getProperty("iomodeml");
        String type = prop.getProperty("iomodemtype");
        String number = prop.getProperty("iorasnum");
        String user = prop.getProperty("iorasuser");
        String[] modemList = new String[0];
        boolean isrem = true;
        boolean ris = false;
        
        boolean needRemoteACK = false;
        if(prop.getProperty("needRemoteACK") != null && "on".equals(prop.getProperty("needRemoteACK")))
		{
			needRemoteACK = true;
		}
        try {
        	IProductInfo pi = ProductInfoMgr.getInstance().getProductInfo();
        	String value = pi.get("needRemoteACK");
        	if(value == null)
        		pi.store("needRemoteACK", String.valueOf(needRemoteACK));
        	else
        		pi.set("needRemoteACK", String.valueOf(needRemoteACK));
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
    		logger.error(e);
		}

        
        int trynum = 1;
        try {
        	trynum = Integer.parseInt(prop.getProperty("iotrynum"));
        }
        catch(Exception e) {e.printStackTrace();}
        
        int retry = 1;
        try {
        	retry = Integer.parseInt(prop.getProperty("ioretryafter"));
        }
        catch(Exception e) {e.printStackTrace();}
        
        String centra = prop.getProperty("iocentralino");

        boolean esy = false;
        
        if (modem != null)
        {
        	CioDIAL dial = new CioDIAL(us.getIdSite());
        	dial.loadConfiguration();
        	String modem_old = dial.getModemLabel();
            if (!modem.equalsIgnoreCase("nop"))
                esy = dial.saveConfiguration(idconf,modem,type,user,number,trynum,retry,centra);
            else
            {
                esy = dial.removeConfiguration(idconf);
                isrem = esy;
                
            }
            
            // if modem is changed, then dispatcher configuration is reloaded
            if(modem_old == null || "".equals(modem_old))
    		{
    			modem_old = "nop";
    		}
    		if(esy && !modem.equals(modem_old))
    		{
    			DispatcherMgr.getInstance().reloadConfiguration();
    		}
            
            
            if (esy)
            {
                
                try {
            		IncomingBeanList.removeAllDeviceForIncoming(null,us.getIdSite());
            	}
            	catch(Exception e){
            		Logger logger = LoggerMgr.getLogger(this.getClass());
            		logger.error(e);
            	}
               
            	if(!isrem)
                {
            		modemList = new String[]{modem};
            		for(int i=0; i<modemList.length; i++)
            			RasServerMgr.getInstance().configModemIncom(us.getIdSite(),modemList[i]);
                }
            }
            
            ris = false;
            ris = updateBookAddress(us, prop, "D");
        }
        
        return ris & isrem;
    }
    
    private void saveDataEvent(UserSession us, String tabName, Properties prop)
    {
        int idconf = Integer.parseInt(prop.getProperty("iocfgid"));
        String path = prop.getProperty("iocfgevpath");

        CioEVT evt = new CioEVT(us.getIdSite());
        boolean esy = evt.saveConfiguration(idconf, path);

        if (esy)
        {
            DispMemMgr.getInstance().storeConfiguration("W");
        }
    }
    
    private void resetSound(UserSession us, Properties prop)
    {
        int idconf = Integer.parseInt(prop.getProperty("iocfgid"));
        String path = "";

        CioEVT evt = new CioEVT(us.getIdSite());
        boolean esy = evt.saveConfiguration(idconf, path);

        if (esy)
        {
        	DispMemMgr.getInstance().storeConfiguration("W");
        }
    }
    
    private void saveDataRelay(UserSession us, String tabName, Properties prop)
    { 	
    	String inter_ids = prop.getProperty("idcontainer");
    	String activeState = "1";
    	String resetType   = "M";
    	String resetTime   = "-1";
    	String showRelay   = "on";
    	
    	int idRelay = -1;
		RelayBeanList rbList = new RelayBeanList();
		
		String[] internalIORelaySafety = new String[]{"TRUE","TRUE","TRUE"};
    	if(inter_ids != null && inter_ids.length()>0)
    	{
    		String[] interid = inter_ids.split(",");
    		for(int i=0;i<interid.length;i++)
    		{
    			idRelay = Integer.valueOf(interid[i]);
				activeState = prop.getProperty("RA_"+interid[i]);
				if(activeState == null)
					activeState = "0";
				int index = -idRelay-1;
				if(index>=0 && index<=2)
					internalIORelaySafety[index] = "-1".equals(activeState)?"TRUE":"FALSE";
				//Internal IO's relay could be safety relay(-1) and normal relay(0,1)
				//for safety relay, the activestate is 1
				if((idRelay == -1 || idRelay == -2 || idRelay == -3) && "-1".equals(activeState))
					activeState = "0";
				resetType 	= prop.getProperty("RR_"+interid[i]);
				resetTime  	= prop.getProperty("RT_"+interid[i]);
				showRelay 	= prop.getProperty("RS_"+interid[i]);
				if (showRelay == null)
				{
					showRelay = "off";
				}
				if(resetTime == null || (resetTime != null && resetTime.length() == 0))
					resetTime = "-1";
				rbList.updateRelayConfiguration(idRelay,resetType,activeState,resetTime, showRelay);
    		}
    	}
    	String internalio_relay_safety = internalIORelaySafety[0]+";"+internalIORelaySafety[1]+";"+internalIORelaySafety[2];
    	try {
    		if(ProductInfoMgr.getInstance().getProductInfo().get(InternalRelayMgr.INTERNALIO_RELAY_FOR_SAFETY)==null)
    			ProductInfoMgr.getInstance().getProductInfo().store(InternalRelayMgr.INTERNALIO_RELAY_FOR_SAFETY, internalio_relay_safety);
    		else
    			ProductInfoMgr.getInstance().getProductInfo().set(InternalRelayMgr.INTERNALIO_RELAY_FOR_SAFETY, internalio_relay_safety);
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
		}
    	if(relayid != null)
    	{
			for(int i=0; i<relayid.length; i++)
			{
				idRelay = relayid[i];
				activeState = prop.getProperty("RA_"+relayid[i]);
				if(activeState == null)
					continue;
				resetType 	= prop.getProperty("RR_"+relayid[i]);
				resetTime  	= prop.getProperty("RT_"+relayid[i]);
				showRelay 	= prop.getProperty("RS_"+relayid[i]);
				if (showRelay == null)
				{
					showRelay = "off";
				}
				if(resetTime == null || (resetTime != null && resetTime.length() == 0))
					resetTime = "-1";
				rbList.updateRelayConfiguration(idRelay,resetType,activeState,resetTime, showRelay);
			}
    	}
    }

    private void saveDataPrinter(UserSession us, String tabName, Properties prop)
    {
    	boolean bReportPrinter = prop.getProperty("cb_report_printer") != null;
    	String strReportPrinter = prop.getProperty("sel_report_printer");
    	boolean bAlarmPrinter = prop.getProperty("cb_alarm_printer") != null;
    	String strAlarmPrinter = prop.getProperty("sel_alarm_printer");
    	CioPrinter printer = new CioPrinter(us.getIdSite());
    	printer.saveConfiguration(bReportPrinter, strReportPrinter, bAlarmPrinter, strAlarmPrinter);
    }
    
    //change method to public, in wizard also need to save mail address
    public boolean updateBookAddress(UserSession us, Properties prop, String type)
    {
        Iterator i = prop.keySet().iterator();
        List list = new ArrayList();
        String[] arr = null;
        String key = "";
        String val = "";
        int idk = 0;

        while (i.hasNext())
        {
            key = (String) i.next();

            if ((key != null) && key.startsWith("BAH"))
            {
                val = prop.getProperty(key);

                if (val != null)
                {
                    arr = val.split(",");

                    try
                    {
                        idk = Integer.parseInt(arr[0]);
                    }
                    catch (NumberFormatException e)
                    {
                        idk = -1;
                    }

                    if ((arr != null) && (arr.length == 4))
                    {
                        list.add(new DispatcherBook(idk, BaseConfig.getPlantId(), us.getIdSite(),
                                type, arr[1], arr[2], Integer.parseInt(arr[3])));
                    }
                }
            }
        }

        DispatcherBook[] db = new DispatcherBook[list.size()];

        for (int j = 0; j < db.length; j++)
        {
            db[j] = (DispatcherBook) list.get(j);
        }

        return DispatcherMgr.getInstance().updateBookAddress(db);
    }

    public String getBookAddress(String lang, String type)
    {
        LangService lan = LangMgr.getInstance().getLangService(lang);
    	
    	String[] header = { lan.getString("setio","rif"), lan.getString("setio","add")};
        String[] fields = { "ioflabel", "iofaddress" };
        DispatcherBook[] addrBook = DispatcherMgr.getInstance().getReceiverInfoByType(type);
        String[] unique = new String[addrBook.length];
        HTMLElement[][] dat = new HTMLElement[addrBook.length][2];

        for (int i = 0; i < addrBook.length; i++)
        {
            dat[i][0] = new HTMLSimpleElement(addrBook[i].getReceiver());
            dat[i][1] = new HTMLSimpleElement(addrBook[i].getAddress());
            unique[i] = String.valueOf(addrBook[i].getKey());
        }

        HTMLTableDyn table = new HTMLTableDyn("TbAddrFax", 1, 2, 455, 128, header, dat, fields);
        table.recHeightBaseOnScreenH(screenh);
        table.recWidthBaseOnScreenW(screenw);
        
        try
        {
            table.setColsWidth(new int[] { 205, 205 });
            table.setUniqueId(unique);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return table.getHTMLTextBuffer().toString();
    }
    
    public void setAutomaticAction(int idsite,String user,String type,String template,String description)
    {
    	String params = "";
    	try
    	{
    		DispatcherBook[] list = DispatcherBookList.getInstance().getReceiversByType(type);
    		if(list != null && list.length > 0)
    		{
    			for(int i=0; i<list.length; i++)
    				params +=  list[i].getKey()+";";
    			
    			params = params.substring(0,params.length()-1);
    		}
    		
    		if(params != null && params.length() > 0)
    		{
    			new ActionBeanList().insertAutomaticAction(idsite,type,params,template,description);
    			
    			DirectorMgr.getInstance().mustRestart();
    		}
    	}
    	catch(Exception e) {
    		Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
    	}
    }
    
    public boolean setAutomaticRule(int idsite, String user, String type)
    {
        boolean ruleCreated = false;
    	
    	try
        {
            RuleBean regola = new RuleBean();
            
            int actionCode = 0;
            boolean exitfor = false; //nel caso esista gi� un'azione automatica (exitfor == TRUE).
            
            String actDesc = "";
            String actTemplate = "";
            
            if(type.equals("F"))
            {
            	actDesc = "FAXACTIONTECH";
            	actTemplate = "PVSendFax.rtf";
            }
            else if(type.equals("S"))
            {
            	actDesc = "SMSACTIONTECH";
            	actTemplate = "";
            }
            else if(type.equals("E"))
            {
            	actDesc = "EMAILACTIONTECH";
            	actTemplate = BSetAction.UNSCHEDULERTEMPLATE;
            }
            
            String sql = "select idaction, actioncode, code from cfaction where idsite=? and actiontype=? and code=? and isscheduled=? order by actiontype, idaction";
            RecordSet rs = null;
            Record r = null;
            
            for (int i = 1; i <= 2; i++)
            {
                if (((rs == null) || (rs.size() == 0)) && (!exitfor))
                {
                    rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(idsite), type, actDesc, "FALSE"});
                    
                    // se esiste un'azione tipo "type" valida, la uso:
                    if((rs != null) && (rs.size() > 0))
                    {
                        r = rs.get(0);
                        exitfor = true;
                    }
                    // altrimenti, prima ne creo una di tipo "type" e poi la uso:
                    else
                    {
                       	setAutomaticAction(idsite, user, type, actTemplate, actDesc);
                        
                        // scrittura negli eventi x creazione nuova azione:
                        //EventMgr.getInstance().info(idsite,user,"Config","W019",new Object[]{description});
                    }
                }
            } // end for.
            
            // se ho creato una nuova azione che prima non c'era, la uso:
            if (r != null)
            {
                actionCode = ((Integer)r.get("actioncode")).intValue();

                regola.setIdsite(idsite);
                regola.setActionCode(actionCode); //azione gi� esistente o quella appena creata
                regola.setRuleCode(r.get("code").toString()+"_RULE"); // nome regola di default
                regola.setIdTimeband(1); // 1="always"
                regola.setIsenabled("TRUE"); // abilitata
                regola.setDelay(0); // ritardo=0 min
                regola.setIdCondition(1); // 1="all alarms"

                //controllo che non esista gi� tale regola:
                sql = "select idrule,isenabled from cfrule where idsite=? and rulecode=? and actioncode=? and idtimeband=? and idcondition=?";
                Object[] params = new Object[] {regola.getIdSite(),regola.getRuleCode(),regola.getActionCode(),regola.getIdTimeband(),regola.getIdCondition()};
                rs = DatabaseMgr.getInstance().executeQuery(null,sql,params);

                //solo se non esiste, allora la creo:
                if ((rs == null) || (rs.size() == 0))
                {
                	regola.saveRule();
                	
                	ruleCreated = true;
                	
                	// scrittura negli eventi x creazione nuova regola:
                	String msg = regola.getRuleCode() + " (" + (regola.getIsenabled().equals("TRUE")?"enabled":"disabled") + ")";
                    
                    //log inserimento regola
                    EventMgr.getInstance().info(new Integer(idsite), user,"Config", "W025", new Object[] { msg });

                    DirectorMgr.getInstance().mustRestart();
                }
            }
        }
        catch (Exception e)
        {
            // PVPro-generated catch block
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        return ruleCreated;
    }
    
    public void setScreenH(int height) {
    	this.screenh = height;
    }
    
    public void setScreenW(int width) {
    	this.screenw = width;
    }
    public String executeDataAction(UserSession us, String tabName, Properties prop)
    throws Exception
    {
    	StringBuffer result = new StringBuffer();
    	result.append("<response>");
    	String cmd = prop.getProperty("cmd");
    	if("tab6name".equalsIgnoreCase(tabName))
    	{
    		if("init".equals(cmd))
    		{
    			int limit = Integer.parseInt(prop.getProperty("limit"));
    			int offset = Integer.parseInt(prop.getProperty("offset"));
    			if(offset == 0 || relayList == null)
    			{
    				relayList = new RelayBeanList(us.getIdSite(),us.getLanguage(),true,true);
    				relayid = relayList.getIds();
    			}
    			int num = 0;
    			for(int i=offset;i-offset<limit && i<relayList.size();i++)
    			{
    				RelayBean relayBean = relayList.getRelayBean(i);
    				int idrelay = relayBean.getIdrelay();
    				result.append("<relay>");
    				result.append("<id><![CDATA["+idrelay+"]]></id>");
    				result.append("<des><![CDATA["+relayBean.getDeviceDesc()+"->"+relayBean.getDescription()+"]]></des>");
    				result.append("<a_state><![CDATA["+relayBean.getActivestate()+"]]></a_state>");
    				result.append("<reset_tp><![CDATA["+relayBean.getResettype()+"]]></reset_tp>");
    				result.append("<reset_tm><![CDATA["+relayBean.getResettime()+"]]></reset_tm>");
    				result.append("<show><![CDATA["+(relayBean.getShow()?"1":"0")+"]]></show>");
    				result.append("</relay>");
    				num++;
    			}
    			if(num<limit)
    			{
    				relayList = null;
    			}
    		}
    	}
    	result.append("</response>");
    	return result.toString();
    }
}
