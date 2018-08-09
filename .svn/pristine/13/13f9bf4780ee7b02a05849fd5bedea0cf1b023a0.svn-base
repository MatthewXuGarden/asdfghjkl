package com.carel.supervisor.dispatcher.sim;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import com.carel.supervisor.base.config.ResourceLoader;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.setfield.BackGroundCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.bean.HSActionQBean;
import com.carel.supervisor.dispatcher.bean.HSActionQBeanList;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.dispatcher.engine.http.LanConnection;
import com.carel.supervisor.dispatcher.engine.mail.DispMailMessage;
import com.carel.supervisor.dispatcher.engine.mail.DispMailSender;
import com.carel.supervisor.dispatcher.engine.print.DispPDFPrinter;
import com.carel.supervisor.dispatcher.engine.sound.DispWinAlarm;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.EMemory;
import com.carel.supervisor.dispatcher.memory.WMemory;
import com.carel.supervisor.dispatcher.memory.ZMemory;
import com.carel.supervisor.presentation.bean.rule.RelayBean;
import com.carel.supervisor.presentation.bean.rule.RelayBeanList;
import com.carel.supervisor.presentation.io.CioFAX;


public class DispSimMgr
{
    private static DispSimMgr simMgr = new DispSimMgr();

    private DispSimMgr()
    {
    }

    public static DispSimMgr getInstance()
    {
        return simMgr;
    }

    public int setTestIo(String pvcode, String type, String dest, String value,
        String userName, String language)
    {
        String test = DispatcherMgr.getInstance().getTestFor(type);
        boolean ris = false;
        int idEvent=-1;

        if (type.equalsIgnoreCase("F"))
        {
            ris = execTestFax(pvcode, test, dest);
        }
        else if (type.equalsIgnoreCase("S"))
        {
            ris = execTestSms(pvcode, test, dest);
        }
        else if (type.equalsIgnoreCase("E"))
        {
        	idEvent = execTestEmail(pvcode, test, dest);
        	if(idEvent<0){
        		ris=false;
        	}else{
        		ris=true;
        	}
        }
        else if (type.equalsIgnoreCase("P"))
        {
            ris = execTestPrint(test, dest);
        }
        else if (type.equalsIgnoreCase("W"))
        {
            ris = execTestWindow();
        }
        else if (type.equalsIgnoreCase("L"))
        {
            ris = execTestRele(dest, value, userName, language);
        }
        else if (type.equalsIgnoreCase("D"))
        {
            ris = execTestDialup(pvcode, dest);
        }

    	
        if (ris)
        {
            try
            {
            	EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                    EventDictionary.TYPE_INFO, "D022",
                    DispatcherMgr.getInstance().decodeActionType(type));
            }
            catch (Exception e)
            {
            }
        }
        else
        {
            try
            {
            	EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                    EventDictionary.TYPE_ERROR, "D056",
                    DispatcherMgr.getInstance().decodeActionType(type));
            }
            catch (Exception e)
            {
            }
        }
        if(idEvent<0) idEvent*=-1;
        return idEvent;
    }

    private boolean execTestDialup(String pvcode, String dest)
    {
        HSActionQBeanList actionQList = new HSActionQBeanList();
        boolean ris = false;
        boolean inLan = false;
        ZMemory zMem = DispMemMgr.getInstance().readConfiguration("D");
        /*
         * Check conn via LAN
         */
        int idReceiver = -1;
        try {
            idReceiver = Integer.parseInt(dest);
            DispatcherBook dbReceive = DispatcherBookList.getInstance().getReceiver(idReceiver);
            if(dbReceive != null)
            {
            	String address = dbReceive.getAddress();
            	inLan = checkIfLan(address);
            	if(inLan)
            	{
            		ris = LanConnection.startCommLanToRemote(address);

    	        	CioFAX iofax = new CioFAX(zMem.getIdSite());
    	            iofax.setTestResult(idReceiver, ris);
            	}
            }
        }
        catch (Exception e) 
        {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
        	logger.error(e);
        	
        	Object[] prm = { "DIALUP" };
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                EventDictionary.TYPE_INFO, "D023", prm);
        }
        
        /*
         * If not via LAN
         */
        if(!inLan)
        {
	        try
	        {
	            HSActionQBean actionQ = null;
	            Integer key = SeqMgr.getInstance().next(null, "hsactionqueue", "idhsactionqueue");
	            actionQ = new HSActionQBean(key.intValue(), pvcode, zMem.getIdSite(), 1,
	                    zMem.getRetryNum(), zMem.getRetryAfter(), zMem.getFisicDeviceId(), "D", 1, "",
	                    "", dest, "-1");
	
	            actionQList.addAction(actionQ);
	            ris = actionQList.insertActions();
	        }
	        catch (Exception e)
	        {
	            Object[] prm = { "DIALUP" };
	            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
	                EventDictionary.TYPE_INFO, "D023", prm);
	        }
        }

        return ris;
    }

    private boolean execTestFax(String pvcode, String template, String dest)
    {
        HSActionQBeanList actionQList = new HSActionQBeanList();
        boolean ris = false;

        try
        {
            String path = loadTestPage(DispatcherMgr.getInstance().getTemplatePath(), template,
                    "rtf");

            try
            {
                ZMemory zMem = DispMemMgr.getInstance().readConfiguration("F");

                HSActionQBean actionQ = null;
                Integer key = SeqMgr.getInstance().next(null, "hsactionqueue", "idhsactionqueue");
                actionQ = new HSActionQBean(key.intValue(), pvcode, zMem.getIdSite(), 1,
                        zMem.getRetryNum(), zMem.getRetryAfter(), zMem.getFisicDeviceId(), "F", 1,
                        path, "", dest, "-1");

                actionQList.addAction(actionQ);
                ris = actionQList.insertActions();
            }
            catch (Exception e)
            {
                Object[] prm = { "FAX" };
                EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                    EventDictionary.TYPE_INFO, "D023", prm);
            }
        }
        catch (Exception e)
        {
            Object[] prm = { "FAX" };
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                EventDictionary.TYPE_INFO, "D023", prm);
        }

        return ris;
    }

    private boolean execTestSms(String pvcode, String template, String dest)
    {
        HSActionQBeanList actionQList = new HSActionQBeanList();
        boolean ris = false;

        try
        {
            String providerini = DispatcherMgr.getInstance().getProviderPath() +
                DispatcherMgr.getInstance().getProviderName();
            ZMemory zMem = DispMemMgr.getInstance().readConfiguration("S");

            HSActionQBean actionQ = null;
            Integer key = SeqMgr.getInstance().next(null, "hsactionqueue", "idhsactionqueue");
            actionQ = new HSActionQBean(key.intValue(), pvcode, zMem.getIdSite(), 1,
                    zMem.getRetryNum(), zMem.getRetryAfter(), zMem.getFisicDeviceId(), "S", 1,
                    providerini, template, dest, "-1");

            actionQList.addAction(actionQ);
            ris = actionQList.insertActions();
        }
        catch (Exception e)
        {
            Object[] prm = { "SMS" };
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                EventDictionary.TYPE_INFO, "D023", prm);
        }

        return ris;
    }

    private int execTestEmail(String pvcode, String template, String dest)
    {
    	int result=-1;
        HSActionQBeanList actionQList = new HSActionQBeanList();
        boolean ris = false;
        String recName = "";

        try
        {
            String path = loadTestPage(DispatcherMgr.getInstance().getTemplatePath(), template,
                    "pdf");

            try
            {
                EMemory zMem = (EMemory) DispMemMgr.getInstance().readConfiguration("E");

                if (zMem.getType().equalsIgnoreCase("L"))
                {
                    try
                    {
                        DispatcherBook db = DispatcherMgr.getInstance().getReceiverInfo(Integer.parseInt(
                                    dest));
                        DispMailMessage message = new DispMailMessage(zMem.getSender(),
                                db.getAddress(), "PlantVisorPro Mail Message Test",
                                "PlantVisorPro Mail Message Test");
                        message.setAttach(path);

                        DispMailSender mailer = new DispMailSender(zMem.getSmtp(),zMem.getUser(),zMem.getPass(),message);
                        mailer.setEncryption(zMem.getEncryption());
                        mailer.setPort(zMem.getPort());
                        result = mailer.sendMessageResult();
                        ris=(result>0)?true:false;

                        if (ris)
                        {
                            recName = "(" + db.getReceiver() + " " + db.getAddress() + ")";

                            Object[] p = { DispatcherMgr.getInstance().decodeActionType("E"), recName };
                            result=EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                                EventDictionary.TYPE_INFO, "D041", p);
                        }
                    }
                    catch (Exception e)
                    {
                        Object[] prm = { "EMAIL" };
                        result=EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                            EventDictionary.TYPE_INFO, "D023", prm);
                    }
                    try
                    {
	                    CioFAX iofax = new CioFAX(zMem.getIdSite());
	    	            iofax.setTestResult(Integer.parseInt(dest), ris);
                    }
                    catch(Exception ex)
                    {}
                }
                else
                {
                    HSActionQBean actionQ = null;
                    Integer key = SeqMgr.getInstance().next(null, "hsactionqueue", "idhsactionqueue");
                    actionQ = new HSActionQBean(key.intValue(), pvcode, zMem.getIdSite(), 1,
                            zMem.getRetryNum(), zMem.getRetryAfter(), zMem.getFisicDeviceId(), "E",
                            1, path, template, dest, "-1");

                    actionQList.addAction(actionQ);
                    ris = actionQList.insertActions();
                }
            }
            catch (Exception e)
            {
                Object[] prm = { "EMAIL" };
                result=EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                    EventDictionary.TYPE_INFO, "D023", prm);
            }
        }
        catch (Exception e)
        {
        }
        if((ris==false)&&(result>0)) result*=-1;
        return result;
    }

    private boolean execTestPrint(String template, String dest)
    {
        boolean ris = false;

        try
        {
            String path = loadTestPage(DispatcherMgr.getInstance().getTemplatePath(), template, "pdf");
            if( dest.equalsIgnoreCase("REPORT") )
            	ris = DispPDFPrinter.printReportFile(path);
            else if( dest.equalsIgnoreCase("ALARM") )
            	ris = DispPDFPrinter.printAlarmFile(path);
            else
            	ris = DispPDFPrinter.printFile(path);
        }
        catch (Exception e)
        {
            Object[] prm = { "PRINT" };
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                EventDictionary.TYPE_INFO, "D023", prm);
        }
        if(ris == false)
        {
        	
        }
        return ris;
    }

    private boolean execTestRele(String dest, String value, String userName, String language)
    {
        int idvariable = Integer.parseInt(dest);
        
        RelayBean rb = RelayBeanList.getRelayBeanByVariableid(1, language, Integer.parseInt(dest));
        
        int valueToSet = rb.getActivestate();
        
        if(value.equalsIgnoreCase("NOACT"))
        {
        	valueToSet = (valueToSet==0?1:0); 
        }
        
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
    	setContext.addVariable(idvariable, valueToSet);
    	setContext.setCallback(new BackGroundCallBack());
		setContext.setUser(userName);
		setContext.setIsTest(true);
        SetDequeuerMgr.getInstance().add(setContext);
        return true; 
    }

    private boolean execTestWindow()
    {
        boolean ris = false;

        String lang = "EN_en";
        String l1 = "User";
        String l2 = "Password";
        String l3 = "Active alarms";
        String l4 = "Stop";

        try
        {
            lang = LangUsedBeanList.getDefaultLanguage(1);

            LangService multiLanguage = LangMgr.getInstance().getLangService(lang);
            l1 = multiLanguage.getString("winalarm", "lb1");
            l2 = multiLanguage.getString("winalarm", "lb2");
            l3 = multiLanguage.getString("winalarm", "lb3");
            l4 = multiLanguage.getString("winalarm", "lb4");
        }
        catch (Exception e)
        {
        }

        try
        {
            WMemory zMem = (WMemory) DispMemMgr.getInstance().readConfiguration("W");
            ris = DispWinAlarm.getInstance().startWinAlarmTest(0, zMem.getPathSound(), l1, l2, l3, l4);
        }
        catch (Exception e)
        {
            Object[] prm = { "WIN" };
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                EventDictionary.TYPE_INFO, "D023", prm);
        }

        return ris;
    }

    private String loadTestPage(String path, String name, String ext)
        throws Exception
    {
        URL url = ResourceLoader.fileFromResourcePath(path, name);
        InputStream is = url.openStream();

        byte[] buffer = new byte[is.available()];
        is.read(buffer);

        File file = null;
        FileOutputStream fos = null;

        String repPath = DispatcherMgr.getInstance().getRepositoryPath();
        String filePath = "";

        try
        {
            file = new File(repPath);

            if (!file.exists())
            {
                file.mkdir();
            }

            filePath = repPath + String.valueOf(System.currentTimeMillis()) + "." + ext;
            fos = new FileOutputStream(filePath);
            fos.write(buffer);
            fos.flush();
            fos.close();
        }
        catch (Exception e)
        {
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                EventDictionary.TYPE_WARNING, "D024", null);
        }

        return filePath;
    }
    
    private boolean checkIfLan(String address)
    {
    	boolean ris = false;
    	int idx = -1;
    	if(address != null)
    	{
    		idx = address.indexOf("."); 
    		if(idx != -1)
    		{
    			idx = address.indexOf(".",idx+1);
    			if(idx != -1)
        		{
    				idx = address.indexOf(".",idx+1);
    				if(idx != -1)
    					ris = true;
        		}
    		}
    	}
    	return ris;
    }
}
