package com.carel.supervisor.dispatcher.main;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.DefaultCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.dataconfig.ProductInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.dataconfig.SystemConf;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.event.VariableHelper;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.bms.BMSConfiguration;
import com.carel.supervisor.director.bms.BmsMgr;
import com.carel.supervisor.director.bms.FailureVar;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.bean.HSActionBean;
import com.carel.supervisor.dispatcher.bean.HSActionBeanList;
import com.carel.supervisor.dispatcher.bean.HSActionQBean;
import com.carel.supervisor.dispatcher.bean.HSActionQBeanList;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.dispatcher.comm.external.ExternalDial;
import com.carel.supervisor.dispatcher.comm.external.ExternalFax;
import com.carel.supervisor.dispatcher.comm.external.ExternalMail;
import com.carel.supervisor.dispatcher.comm.external.ExternalSms;
import com.carel.supervisor.dispatcher.comm.layer.DispLayDialUp;
import com.carel.supervisor.dispatcher.comm.layer.DispLayFax;
import com.carel.supervisor.dispatcher.comm.layer.DispLayMail;
import com.carel.supervisor.dispatcher.comm.layer.DispLaySms;
import com.carel.supervisor.dispatcher.engine.http.LanConnection;
import com.carel.supervisor.dispatcher.engine.mail.DispMailMessage;
import com.carel.supervisor.dispatcher.engine.mail.DispMailSender;
import com.carel.supervisor.dispatcher.memory.DMemory;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.EMemory;
import com.carel.supervisor.dispatcher.memory.FMemory;
import com.carel.supervisor.dispatcher.memory.SMemory;
import com.carel.supervisor.dispatcher.util.SmsUtil;
import com.carel.supervisor.presentation.https2xml.XMLResponse;
import com.carel.supervisor.presentation.io.CioDIAL;
import com.carel.supervisor.presentation.io.CioFAX;
import com.carel.supervisor.report.PrinterMgr2;


public class DispatcherDQ extends Poller
{
	private static Logger logger = LoggerMgr.getLogger(DispatcherDQ.class);
    private static final int SA = 0;
    private static final int SL = 0;
    private static final int AT = 0;
    private static final long VAR_TIME = 60000L;
    private int startAfter = 0;
    private int sleep = 0;
    private int algTime = 0;
    private long time = 0;
    private int counter = 0;
    private int pingDevice = 30;
    private String fisicDevice = "";
    private long timeBeforeSleep = 0;
	private static int innercounter=0;

    public void run()
    {
        long curTime = 0;
        long sleepTime = 0;
        long operation = 0;

        // Start after N seconds
        try
        {
            Thread.sleep(this.startAfter);
            if( !(this.fisicDevice.indexOf("#")>-1) && !this.fisicDevice.equals("LAN") )
	            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
	                EventDictionary.TYPE_INFO, "D018", new Object[] { this.fisicDevice });
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	logger.error(e);
        }

        // Align time to go in event of machine stop
        new HSActionQBeanList().AllignTimeToGo(this.fisicDevice);

        // First time
        if (this.time <= 0)
        {
            this.time = System.currentTimeMillis();
        }

        // Loop until it's stopped
        do
        {
            // Current Time
            curTime = System.currentTimeMillis();

            /*
             * Guardian
             * Save last timestamp in millisecond for check dispatcher activity.
             
            try
            {
                DispatcherMgr.getInstance().setLastCheckCodeTwo(curTime);
            }
            catch (Exception e)
            {
            }
             */
            
            try 
    		{
    			DispatcherMonitor.getInstance().setSecondQueueTime(this.getName(),System.currentTimeMillis());
    		}
    		catch(Exception e){
            	e.printStackTrace();
            	logger.error(e);
    		}
    		
            // Check for update priority
            operation = (curTime - this.time);

            if (operation >= this.algTime)
            {
                this.runAlgo();
                this.time = System.currentTimeMillis();
            }
            else if (operation < 0)
            {
                this.time = System.currentTimeMillis();
            }

            try
            {
                // If there are some action
                if (this.execute())
                {
                    this.counter = 0;
                }
                else
                {
                    this.counter++;
                }

                // Ping to device
                if (counter == this.pingDevice)
                {
                    this.counter = 0;
                    pingDevice();
                }

                sleepTime = System.currentTimeMillis() - curTime;

                // Save currentTime before thread sleep
                this.timeBeforeSleep = System.currentTimeMillis();

                if ((this.sleep - sleepTime) > 0)
                {
                    Thread.sleep((this.sleep - sleepTime));
                }
                else
                {
                    Thread.sleep(5000);
                }

                // Check if user change the clock time
                checkTime();
            }
            catch (Exception e)
            {
            	e.printStackTrace();
            	logger.error(e);
            }
        }
        while (!isStopped());
    }

    public DispatcherDQ(String sa, String sl, String at, String fd)
	{
	    try
	    {
	        this.startAfter = Integer.parseInt(sa);
	    }
	    catch (Exception e)
	    {
	        this.startAfter = SA;
        	e.printStackTrace();
        	logger.error(e);
	    }
	
	    try
	    {
	        this.sleep = Integer.parseInt(sl);
	    }
	    catch (Exception e)
	    {
	        this.sleep = SL;
        	e.printStackTrace();
        	logger.error(e);
	    }
	
	    try
	    {
	        this.algTime = Integer.parseInt(at);
	    }
	    catch (Exception e)
	    {
	        this.algTime = AT;
        	e.printStackTrace();
        	logger.error(e);
	    }
	
	    this.fisicDevice = fd;
	    this.counter = 0;
	
	    try
	    {
	        SystemConf sc = SystemConfMgr.getInstance().get("pingDevice");
	        this.pingDevice = (int) sc.getValueNum();
	
	        if ((this.pingDevice == 0) || (this.pingDevice < 0))
	        {
	            this.pingDevice = (int) sc.getDefaultNumValue();
	        }
	    }
	    catch (Exception e)
	    {
        	e.printStackTrace();
        	logger.error(e);
	    }
	    
	    // Imposto il nome del thread con il nome del modem che lo gestisce.
	    setName("DispatcherDQ_"+(innercounter++)+"_"+fd);
	}

	private void checkTime()
    {
        long tmp = System.currentTimeMillis() - this.timeBeforeSleep;

        if ((tmp < 0) || (tmp > VAR_TIME))
        {
            new HSActionQBeanList().reAllignTimeToGo(this.fisicDevice, tmp);
        }
    }

    private void pingDevice()
    {
        try
        {
            if (!DispatcherDQMgr.getInstance().ping(this.fisicDevice))
            {
                EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                    EventDictionary.TYPE_ERROR, "D045", new Object[] { this.fisicDevice });
                
                DispatcherMgr.getInstance().increasePingTable(this.fisicDevice);
            }
            else
            {
            	DispatcherMgr.getInstance().clearPingTable(this.fisicDevice);
            }
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	logger.error(e);
        }
    }

    private boolean execute()
    {
        HSActionQBeanList obj = new HSActionQBeanList();
        HSActionQBean action = obj.dequeAction(this.fisicDevice);
        boolean actionsIn = false;
        boolean ris = false;

        String str = ProductInfoMgr.getInstance().getProductInfo().get("needRemoteACK");
        boolean needRemoteACK = Boolean.valueOf(str!=null?str:"false");
        if (action != null)
        {
            actionsIn = true;
            ris = dispatchAction(action);

            if (ris)
            {
                if(!needRemoteACK)
                {
                	sendExecute(action);
                }
                //2013-11-20, Kevin. Bug fix 10534. for action type "D"(alarm sync with Remote)
                //command "END" means real action success
                //there are two failures: 1. connection fail 2. no "END" command from Remote
                else
                {
	            	//for !"D", it is success action
	            	if(!action.getType().equalsIgnoreCase("D"))
	            		sendExecute(action);
	            	//for remote("D"), we don't know if it is a success one, just decrease the try_number
	            	else
	            		remoteActionDecreaseRetryNum(action);
                }
            }
            else
            {
                sendFailure(action);
            }
        }
        if(needRemoteACK)
        	remoteActionCheckTimeout();
        return actionsIn;
    }
    private int getTimeoutInterval()
    {
    	String str = ProductInfoMgr.getInstance().getProductInfo().get("remote_action_timeout_interval");
    	int interval = 5;
    	if(str != null && str.length()>0)
    	{
	    	try{
	    		interval = Integer.valueOf(str);
	    	}
	    	catch(Exception ex){}
    	}
    	return interval;
    }
    private void remoteActionCheckTimeout()
    {
    	DMemory zMem = (DMemory) DispMemMgr.getInstance().readConfiguration("D");
    	if(fisicDevice.equals(zMem.getModemId()))
    	{
			HSActionQBeanList obj = new HSActionQBeanList();
	        HSActionQBean[] actions = obj.getRemoteActionForTimeoutCheck();
	        if(actions != null && actions.length>0)
	        {
	        	Calendar c = Calendar.getInstance();
	        	c.add(Calendar.MINUTE, -getTimeoutInterval());
	        	for(HSActionQBean action:actions)
	        	{
	        		if(c.getTime().after(action.getTimetogo()))
	        		{
	        			try
	                    {
	                        new HSActionQBeanList().discardScheduledAction(action, this.fisicDevice);
	                        
	                        Integer evId = SeqMgr.getInstance().next(null, "hsdocdispsent", "idevent");
	                        DispDocLinker.insertDoc(action.getSite(),evId,action.getType(),action.getPath());
	                        String recName = getReceiverString(action.getReceivers(),action.getType());
	                        EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
	                                        EventDictionary.TYPE_ERROR, "D062",
	                                        new Object[]
	                                        {
	                                            this.fisicDevice,
	                                            DispatcherMgr.getInstance().decodeActionType(action.getType()), recName
	                                        });
	                    }
	                    catch (Exception e)
	                    {
	                    	e.printStackTrace();
	                    	logger.error(e);
	                    }
	        		}
	        	}
	        }
    	}
    }
    private void sendExecute(HSActionQBean action)
    {
        String recName = getReceiverString(action.getReceivers(),action.getType());

        //actionid=="-1" means it is a test, here record the test failure
        //add by kevin for scheduler dashboard
        if(action != null && action.getActionsid().equals("-1") == true &&
        		action.getReceivers()!="" && action.getReceivers().equals("")==false)
        {
        	try 
        	{
	        	CioFAX iofax = new CioFAX(action.getSite());
	        	int idReceiver= Integer.parseInt(action.getReceivers());
	            if(action != null && action.getActionsid().equals("-1") == true)
	            {
	            	iofax.setTestResult(idReceiver, true);
	            }
        	}
        	catch (Exception e) {
            	e.printStackTrace();
            	logger.error(e); 		
        	}
        }
        try
        {
            new HSActionQBeanList().sendedScheduledAction(action, this.fisicDevice);

            try
            {
                Integer evId = SeqMgr.getInstance().next(null, "hsdocdispsent", "idevent");
                if(action.getType().equalsIgnoreCase("S"))
                    DispDocLinker.insertDoc(action.getSite(),evId,action.getType(),action.getMessage());
                else
                    DispDocLinker.insertDoc(action.getSite(),evId,action.getType(),action.getPath());
                
                Object[] p = 
                {
                        "<a onclick=evnviewDoc("+evId.intValue()+",'"+action.getType()+"')><u>"+DispatcherMgr.getInstance().decodeActionType(action.getType())+"</u></a>", recName
                };
                
                EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                                           EventDictionary.TYPE_INFO, "D041", p);
            }
            catch (Exception e1)
            {
            	e1.printStackTrace();
            	logger.error(e1);
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    private void sendFailure(HSActionQBean action)
    {
        int retryNum = action.getRetrynum();
        String recName = getReceiverString(action.getReceivers(),action.getType());

        if (retryNum > 1)
        {
            retryNum--;

            long retryAfter = action.getRetryafter();

            if (retryAfter == 0)
            {
                retryAfter = 1L;
            }

            long curTimeMs = System.currentTimeMillis();
            curTimeMs = curTimeMs + (60000L * retryAfter);

            Timestamp timetogo = new Timestamp(curTimeMs);

            try
            {
                new HSActionQBeanList().requeueScheduledAction(action.getKey(), retryNum, timetogo,
                    this.fisicDevice);

                try
                {
                    Object[] p = 
                        {
                            this.fisicDevice,
                            DispatcherMgr.getInstance().decodeActionType(action.getType()), recName,
                            DateUtils.date2String(timetogo, "yyyy/MM/dd HH:mm:ss"),
                            new Integer(retryNum)
                        };
                    
                    EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                        EventDictionary.TYPE_WARNING, "D040", p);
                }
                catch (Exception e2)
                {
                	e2.printStackTrace();
                	logger.error(e2);
                }
            }
            catch (Exception e)
            {
            	e.printStackTrace();
            	logger.error(e);
            }
        }
        else
        {
            try
            {
                new HSActionQBeanList().discardScheduledAction(action, this.fisicDevice);
                
                Integer evId = SeqMgr.getInstance().next(null, "hsdocdispsent", "idevent");
                if(action.getType().equalsIgnoreCase("S"))
                    DispDocLinker.insertDoc(action.getSite(),evId,action.getType(),action.getMessage());
                else
                    DispDocLinker.insertDoc(action.getSite(),evId,action.getType(),action.getPath());
                
                //actionid=="-1" means it is a test, here record the test failure
                //add by kevin for scheduler dashboard
                if(action != null && action.getActionsid().equals("-1") == true &&
                		action.getReceivers()!="" && action.getReceivers().equals("")==false)
                {
                	try 
                	{
        	        	CioFAX iofax = new CioFAX(action.getSite());
        	        	int idReceiver= Integer.parseInt(action.getReceivers());
        	            if(action != null && action.getActionsid().equals("-1") == true)
        	            {
        	            	iofax.setTestResult(idReceiver, false);
        	            }
                	}
                	catch (Exception e) {
                    	e.printStackTrace();
                    	logger.error(e);
                	}
                }
                EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                                EventDictionary.TYPE_ERROR, "D019",
                                new Object[]
                                {
                                    this.fisicDevice,
                                    "<a onclick=evnviewDoc("+evId.intValue()+",'"+action.getType()+"')><u>"+DispatcherMgr.getInstance().decodeActionType(action.getType())+"</u></a>", recName
                                });
            }
            catch (Exception e)
            {
            	e.printStackTrace();
            	logger.error(e);
            }
        }
    }
    private void remoteActionDecreaseRetryNum(HSActionQBean action)
    {
    	int retryNum = action.getRetrynum();
    	
        if (retryNum > 1)
        {
            retryNum--;

            long retryAfter = action.getRetryafter();

            if (retryAfter == 0)
            {
                retryAfter = 1L;
            }

            long curTimeMs = System.currentTimeMillis();
            curTimeMs = curTimeMs + (60000L * retryAfter);

            Timestamp timetogo = new Timestamp(curTimeMs);

            try
            {
                new HSActionQBeanList().requeueScheduledAction(action.getKey(), retryNum, timetogo,
                    this.fisicDevice);
            }
            catch (Exception e)
            {
            	e.printStackTrace();
            	logger.error(e);
            }
        }
        else
        {
        	//retrynum =1, update only update time
        	try{
        		new HSActionQBeanList().requeueScheduledAction(action.getKey(), action.getRetrynum(), action.getTimetogo(),
                    this.fisicDevice);
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
            	logger.error(e);
        	}
        }
    }
    public static String getReceiverString(String idReceiver,String type)
    {
        int idrec = 0;
        String recName = "";
        DispatcherBook db = null;

        try
        {
            idrec = Integer.parseInt(idReceiver);
            db = DispatcherBookList.getInstance().getReceiver(idrec);

            if (db != null)
            {
                recName = "(" + db.getReceiver() + " " + db.getAddress() + ")";
            }
            else
            {
            	if(type != null && type.equalsIgnoreCase("E"))
            	{
	            	ProductInfo pi = new ProductInfo();
	            	try {
	            		pi.load();
	            		recName = pi.get("supportemail");
	            	}
	            	catch(Exception e){
	            	}
            	}
            	else
            		recName = " ";
            }
        }
        catch (Exception e1)
        {
            recName = "(NO RECEIVER)";
        	e1.printStackTrace();
        	logger.error(e1);
        }

        return recName;
    }

    private void runAlgo()
    {
        try
        {
            new HSActionQBeanList().updatePriorityScheduledAction(this.algTime, this.fisicDevice);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	logger.error(e);
        }
    }

    private boolean dispatchAction(HSActionQBean action)
    {
        boolean risultato = false;
        String receiver = action.getReceivers();
        String sEmailSupport = null;
        int idReceiver = -1;

        try {
            idReceiver = Integer.parseInt(receiver);
        }
        catch (Exception e) {
        	e.printStackTrace();
        	logger.error(e);
        }

        DispatcherBook dbReceive = DispatcherBookList.getInstance().getReceiver(idReceiver);
        
        // Controllo per email supporto
        if(idReceiver == -1 && dbReceive == null && action.getType().equalsIgnoreCase("E"))
        {
        	ProductInfo pi = new ProductInfo();
        	try {
        		pi.load();
        		sEmailSupport = pi.get("supportemail");
        	}
        	catch(Exception e){
            	e.printStackTrace();
            	logger.error(e);
        	}
        }
        
        boolean withoutRecipient=false;
        //Action without recipients (for BMS Xml and similar)
        if (action.getType().equalsIgnoreCase("#BA") || action.getType().equalsIgnoreCase("#BS"))
        {
        	withoutRecipient=true;
        }
        
        if((sEmailSupport != null) || (dbReceive != null) || withoutRecipient)
        {
            if (action.getType().equalsIgnoreCase("F"))
            {
                String siteName = "";
                String siteTelNumber = "";
                FMemory zMem = (FMemory) DispMemMgr.getInstance().readConfiguration("F");

                try
                {
                    SiteInfo si = SiteInfoList.retrieveSiteById(action.getSite());

                    if (si != null)
                    {
                        siteName = si.getName();
                        siteTelNumber = si.getPhone();
                    }
                    else
                    {
                        siteName = "PlantVisorPRO";
                        siteTelNumber = "***********";
                    }
                }
                catch (Exception e)
                {
                	e.printStackTrace();
                	logger.error(e);
                }

                if (DispatcherMgr.getInstance().serviceExternal())
                {
                    ExternalFax eFax = new ExternalFax(siteName, dbReceive.getReceiver(),
                            action.getPath(), dbReceive.getAddress(), siteTelNumber,
                            zMem.getCentralino(), action.getChannel());
                    risultato = eFax.send();
                }
                else
                {
                    DispLayFax dFax = new DispLayFax(siteName, dbReceive.getReceiver(),
                            action.getPath(), dbReceive.getAddress(), siteTelNumber);
                    risultato = dFax.openChannel(action.getChannel());
                }
            }
            else if (action.getType().equalsIgnoreCase("S"))
            {
                SMemory zMem = (SMemory) DispMemMgr.getInstance().readConfiguration("S");

                if (zMem != null)
                {
                    String msg = action.getMessage();
                    String encMsg = "";
                    if( zMem.getProviderLb().equals("Direct SMS - Extended charset") ) {
                    	if( msg.length() > 70 ) // no more than 70 unicode chars / sms
                    		msg = msg.substring(0, 70);
                    	encMsg = convertStringToHex(msg);
                    }
                    else 
                    {
                    	if(zMem.getProviderLb().equals("Direct SMS Wavecom")) 
                    	{
                        	// convert from ISO 8859-1 charset to GSM 03.38 charset (for Wavecom GSM modem only)
                        	msg = new String(SmsUtil.convert88591ToGsm0338(msg));
                        }
                    	
                    	if( msg.length() > 159 )  // no more than 160 ascii chars / sms
                    		msg = msg.substring(0, 159);
                    	
                    	encMsg = msg;
                    }
                	
                    if (DispatcherMgr.getInstance().serviceExternal())
                    {
                    	ExternalSms eSms = new ExternalSms(zMem.getCentralino(),
                                dbReceive.getAddress(), action.getPath(), zMem.getProviderLb(),
                                zMem.getCalltype(), encMsg, action.getChannel());
                        risultato = eSms.send();
                    }
                    else
                    {
                        DispLaySms dSms = new DispLaySms(action.getPath(), zMem.getCentralino(),
                                zMem.getProviderLb(), zMem.getCalltype(), encMsg,
                                dbReceive.getAddress());
                        risultato = dSms.openChannel(action.getChannel());
                    }
                }
            }
            else if (action.getType().equalsIgnoreCase("E"))
            {
                EMemory zMem = (EMemory) DispMemMgr.getInstance().readConfiguration("E");
                
                String sAddr = "";
                if(dbReceive != null)
                	sAddr = dbReceive.getAddress();
                else if(sEmailSupport != null)
                	sAddr = sEmailSupport;
                
                if (zMem != null)
                {
                	String sDate = DateUtils.date2String(new Timestamp(System.currentTimeMillis()), "yyyy/MM/dd HH:mm:ss.SSS");
                	String emailBody = "PlantVisorProMail " + sDate;
                	String subject = action.getMessage();
                	String siteName = "";
                	String[] infoSender = this.getInfoSender(action.getSite());
            		if ((infoSender != null) && (infoSender.length > 0)) {
            			siteName = infoSender[0];
            		}
            		String tempSubject = siteName + " - " + sDate;
                	if(action.getPath() != null && action.getPath().endsWith("PDF_Alive.pdf"))
                    {
                		emailBody = "";
                		subject = tempSubject;
                    	String aliveContent = ProductInfoMgr.getInstance().getProductInfo().get("alive_mail_content");
						if(aliveContent == null || (!aliveContent.equals("no")))
						{
							String lang = "EN_en";
							try{lang = LangUsedBeanList.getDefaultLanguage(1);}catch(Exception ex){}
							LangService l = LangMgr.getInstance().getLangService(lang);
							StringBuffer html_buff = new StringBuffer("");
							html_buff.append("<HTML><HEAD><TITLE>"
									+ l.getString("rep_body_mail", "rep_by_mail")
									+ "</TITLE></HEAD><BODY>");
							html_buff.append("<TABLE><TR><TD><B>");
							html_buff.append("Scheduled message from: ");
							html_buff.append("</B></TD><TD>");
							html_buff.append(siteName);
							html_buff.append("</TD></TR><TR><TD><B>");
							html_buff.append(l.getString("rep_body_mail", "date"));
							html_buff.append("</B></TD><TD>");
							html_buff.append(sDate);
							html_buff.append("</TD></TR></TABLE>");
							html_buff.append("</BODY></HTML>");
							emailBody = html_buff.toString();
						}
                    }
                	if(action.getPath() != null && action.getPath().indexOf("\\ALARM_")>0 && action.getPath().endsWith(".pdf"))
                	{
                		subject = tempSubject;
                		String alarmBody = getAlarmEmailBody(action);
                		if(alarmBody != null)
                			emailBody = alarmBody;
                	}
                    if (DispatcherMgr.getInstance().serviceExternal())
                    {
                        ExternalMail eMail = new ExternalMail(zMem.getUser(), zMem.getPass(), zMem.getSender(),
                                sAddr, emailBody ,subject ,
                                action.getPath(), zMem.getSmtp(), zMem.getProvider());
                        risultato = eMail.send();
                    }
                    else
                    {
                        DispLayMail dMail = new DispLayMail();
                        risultato = dMail.openChannel(zMem.getProvider());

                        if (risultato)
                        {
                            DispMailMessage message = new DispMailMessage(zMem.getSender(),
                                    dbReceive.getAddress(), subject,
                                    emailBody);
                            message.setAttach(action.getPath());

                            DispMailSender mailer = new DispMailSender(zMem.getSmtp(), message);
                            risultato = mailer.sendMessage();
                        }

                        dMail.closeChannel(action.getChannel());
                    }
                }
            }
            else if (action.getType().equalsIgnoreCase("D"))
            {
            	boolean bLan = false;
            	
            	try
                {
                    String strAddress = dbReceive.getAddress();
                    bLan = checkIfLan(strAddress);
                    if( bLan ) {
                    	risultato = LanConnection.startCommLanToRemote(strAddress);
                    	String recName = "(" + dbReceive.getReceiver() + " " + strAddress + ")";
                    	
                        if(risultato ) {
                        	String str = ProductInfoMgr.getInstance().getProductInfo().get("needRemoteACK");
                        	boolean needRemoteACK = Boolean.valueOf(str!=null?str:"false");
                        	 //needRemoteACK: needs ACK from remote after file transfered, so only !needRemoteACK is success
                        	if(!needRemoteACK)
                        	{
	                            Object[] p = {DispatcherMgr.getInstance().decodeActionType("D"),recName};
	                            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
	        						   EventDictionary.TYPE_INFO, "D041", p);
        					}
                     	}
                        else if( !risultato ) {
                            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                                    EventDictionary.TYPE_ERROR, "D049","");
                            
                            Object[] p = {DispatcherMgr.getInstance().decodeActionType("D"),recName};
                            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                                    EventDictionary.TYPE_ERROR, "D058", p);
                    	}
                    }
                    
                }
                catch (Exception e)
                {
                	e.printStackTrace();
                	logger.error(e);
                	
                	Object[] prm = { "DIALUP" };
                    EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                        EventDictionary.TYPE_INFO, "D023", prm);
                }
 
                if( bLan || fisicDevice.equals("LAN") )
                	return risultato;
            	// ignore phone numbers when there is no modem configured (fisiDevice is LAN)
            	if (DispatcherMgr.getInstance().serviceExternal())
                {
                    risultato = dialService(dbReceive, action);
                }
                else
                {
                    String u = DispatcherMgr.getInstance().getUser4Remote();
                    String p = DispatcherMgr.getInstance().getPass4Remote();
                    String n = "";
                    String c = "";
                    String rIP = "NOP";
                    int countTimeOut = 120;

                    try
                    {
                        countTimeOut = Integer.parseInt(DispatcherMgr.getInstance().getTout4Remote());
                    }
                    catch (Exception e){
                    	e.printStackTrace();
                    	logger.error(e);
                    }

                    DMemory zMem = (DMemory) DispMemMgr.getInstance().readConfiguration("D");
                    if (zMem != null)
                    {
                        try
                        {
                            n = dbReceive.getAddress();
                            c = zMem.getCentralino();
                        }
                        catch (Exception e)
                        {
                        	e.printStackTrace();
                        	logger.error(e);
                        }

                        DispLayDialUp dDial = new DispLayDialUp(u, p, n, c);
                        risultato = dDial.openChannel(action.getChannel());

                        if (risultato)
                        {
                            rIP = dDial.getRemoteIP();
                            DispatcherMgr.getInstance().setDeviceStatus(action.getChannel(), rIP);

                            do
                            {
                                try
                                {
                                    Thread.sleep(10000);
                                    countTimeOut--;
                                }
                                catch (Exception e)
                                {
                                }
                            }
                            while ((!DispatcherMgr.getInstance().isDeviceFree(action.getChannel())) &&
                                    (countTimeOut > 0));

                            if (countTimeOut <= 0)
                            {
                                DispatcherMgr.getInstance().setDeviceStatus(action.getChannel());

                                try
                                {
                                    Object[] par = 
                                        {
                                            "(" + dbReceive.getReceiver() + " " +
                                            dbReceive.getAddress() + ")"
                                        };
                                    EventMgr.getInstance().log(new Integer(1), "Dispatcher",
                                        "Action", EventDictionary.TYPE_WARNING, "D044", par);
                                }
                                catch (Exception e)
                                {
                                	e.printStackTrace();
                                	logger.error(e);
                                }
                            }

                            risultato = dDial.closeChannel(action.getChannel());
                        }
                    }
                }
            }
            else if (action.getType().equalsIgnoreCase("#BA") || action.getType().equalsIgnoreCase("#BS"))
            {
            	//BMS - Alarms
            	//comunicazione xml verso server
            	BMSConfiguration conf = BmsMgr.getInstance().getConfig();
            	StringBuffer response= new StringBuffer();
            	XMLNode node;
				try 
				{
					
					if (action.getType().equalsIgnoreCase("#BA"))
					{
						    // READ XML FILE CONTENT
							File path = new File(BaseConfig.getCarelPath()+File.separator+PrinterMgr2.getInstance().getSavePath());
							File file = new File(path.getAbsolutePath()+File.separator+action.getPath());
														
							
							BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF8"));
							String tmp = "";
							while (tmp!=null)
							{
								tmp = br.readLine();
								if (tmp!=null)
									response.append(tmp);
							}
					}
					else if (action.getType().equalsIgnoreCase("#BS"))
					{
						response.append(XMLResponse.getStartingTag() );
						for (Iterator<String> iterator = conf.getActmap().get(action.getType().toLowerCase()).iterator(); iterator.hasNext();) {
							String s = iterator.next();
							node = XMLNode.parse(conf.getPacketsmap().get(s));
							XMLResponse xmlr = new XMLResponse(node,true);
							response.append(xmlr.getInnerXML());
						}
						response.append(XMLResponse.getEndingTag());
					}
					
					// END READING
					if (conf.getViaSocket())
					{
						//send by socket
						Socket s = new Socket(conf.getHost(),conf.getPort() );
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream() ) );
						bw.write(response.toString());
						bw.close();
						risultato=true;
					}
					else if (conf.getViaHttpPost())
					{
						//send by post
						String coded = URLEncoder.encode(response.toString(),"UTF-8") ;
						
						URL u = new URL(conf.getHost() );
						
						//URLConnection uc = u.openConnection();
						
						
						HttpURLConnection huc = ( HttpURLConnection ) u.openConnection();
						
						
						
						huc.setRequestMethod("POST");
						huc.setDoOutput(true);
						huc.setDoInput(true);
						huc.setUseCaches (false); 
						
						huc.setAllowUserInteraction (false); 
						huc.setRequestProperty ( "Content-Type", "text/xml" );

						huc.setRequestProperty ( "application","x-www-form-urlencoded"); 
						huc.setRequestProperty ( "Content-length", "" + coded.length() );  
						
						huc.connect();
						OutputStreamWriter osw = new OutputStreamWriter(
								    new BufferedOutputStream(huc.getOutputStream()));
						osw.write(coded);
						osw.flush();
						osw.close();
						
						int i = huc.getResponseCode();
						
						huc.disconnect();
						
						if (i<300)
							risultato=true;
						else
							risultato=false;
					}
					
				} catch (Exception e) {
					risultato=false;
		        	e.printStackTrace();
		        	logger.error(e);
				}
            	
				// manage variables setting if there is an alarm management failure
				if (!risultato)
				{
					FailureVar[] fv = conf.getFailureVars();
					if (fv !=null && fv.length!=0)
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
				    	setContext.setCallback(new DefaultCallBack());
						setContext.setUser("System");
						
						
						FailureVar v = null;
						for (int i=0;i<fv.length;i++)
						{
							v = fv[i];
							VarphyBean var;
							try 
							{
								var = VarphyBeanList.retrieveVarByUuid(1, v.getUuId(), conf.getLanguage());
								if (var!=null)
									setContext.addVariable(var.getId(), Float.valueOf(v.getValue()));
							} 
							catch (DataBaseException e) 
							{
					        	e.printStackTrace();
					        	logger.error(e);
							}
						}
						SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
					}
				}
				
				//				
            }
        }
        else
        {
        	EventMgr.getInstance().log(new Integer(1),"Dispatcher","Action",EventDictionary.TYPE_WARNING,"D029",
						                new Object[]
						                {
						                    new Integer(idReceiver),
						                    DispatcherMgr.getInstance().decodeActionType(action.getType())
						                });
        }

        return risultato;
    }
    private String getAlarmEmailBody(HSActionQBean action)
    {
    	String actionsIdStr = action.getActionsid();
    	if(actionsIdStr != null && actionsIdStr.length()>0)
    	{
    		String[] actionsId = actionsIdStr.split(",");
    		int[] ids = new int[actionsId.length];
    		for(int i=0;i<ids.length;i++)
    		{
    			ids[i] = Integer.valueOf(actionsId[i]);
    		}
    		List<HSActionBean> list = HSActionBeanList.getHSActionList(ids);
    		int[] idsAlarm = new int[list.size()];
    		for(int i=0;i<idsAlarm.length;i++)
    		{
    			idsAlarm[i] = list.get(i).getIdvariable();
    		}

			try {
				String lang = LangUsedBeanList.getDefaultLanguage(1);
	    		Map<Integer, String[]> map = VariableHelper.getDescriptions(lang,1,idsAlarm);
				LangService l = LangMgr.getInstance().getLangService(lang);
				SiteInfoList s = new SiteInfoList();
				String sitename = s.getById(1).getName();
				String sDate = DateUtils.date2String(new Timestamp(System.currentTimeMillis()), "yyyy/MM/dd HH:mm:ss.SSS");
				StringBuffer html_buff = new StringBuffer("");

				html_buff.append("<HTML>\n<HEAD>\n<TITLE>"
						+ l.getString("rep_body_mail", "rep_by_mail")
						+ "</TITLE>\n</HEAD>\n<BR>\n<BODY>\n");
				html_buff.append("<TABLE width='100%'>\n\n");
				html_buff.append("<TR><TD align='center'><b>"
						+ l.getString("rep_body_mail", "alarm_report")
						+ "</b></TD><TD align='center'><b>" + sitename
						+ "</b></TD><TD align='center'><b>" + sDate
						+ "</b></TD></TR></TABLE>\n");
				html_buff.append("<BR><TABLE width='100%'>\n");
				for (int x = 0; x < list.size(); x++) {
					HSActionBean b = list.get(x);
					String start = "START";
					String[] descr = map.get(b.getIdvariable());
					if(descr == null || descr.length == 0)
						continue;
					Timestamp time = b.getStart();;
					if(b.getEnd() != null)
					{
						start = "END";
						time = b.getEnd();
					}
					html_buff.append("<TR><TD align='center'>"
							+ start
							+ " - </TD><TD align='center'>"
							+ time
							+ "</TD><TD align='center'>"
							+ descr[1]
							+ "</TD><TD align='center'>"
							+ descr[0] + "</TD></TR>\n");
				}
				// END OF MESSAGE
				html_buff.append("<TR><TD align='center'>----------</TD><TD align='center'>&nbsp;</TD><TD align='center' colspan='2'>&nbsp;</TD></TR>");
				//
				html_buff.append("\n</TABLE>\n</BODY>\n</HTML>");

				return html_buff.toString();
			} catch (Exception e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e.getMessage());
			}
    	}
    	return null;
    }
    protected String[] getInfoSender(int idsite)
    {
        String[] ret = new String[3];

        try
        {
            String sql = "select a.name as uno,a.phone as due,b.languagecode as tre from cfsite as a," +
                "cfsiteext as b where a.idsite=? and a.idsite=b.idsite and b.isdefault='TRUE'";

            Object[] param = { new Integer(idsite) };

            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

            if (rs != null)
            {
                Record r = rs.get(0);
                ret[0] = UtilBean.trim(r.get("uno"));
                ret[1] = UtilBean.trim(r.get("due"));
                ret[2] = UtilBean.trim(r.get("tre"));
            }
        }
        catch (Exception e)
        {
            ret[0] = "";
            ret[1] = "";
            ret[2] = "EN_en";
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                EventDictionary.TYPE_WARNING, "D025", null);
        }

        return ret;
    }
    private boolean dialService(DispatcherBook dbReceive, HSActionQBean action)
    {
        String u = DispatcherMgr.getInstance().getUser4Remote();
        String p = DispatcherMgr.getInstance().getPass4Remote();
        String n = "";
        String c = "";
        boolean ris = false;

        int countTimeOut = 12;

        try
        {
            countTimeOut = Integer.parseInt(DispatcherMgr.getInstance().getTout4Remote());
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	logger.error(e);
        }

        DMemory zMem = (DMemory) DispMemMgr.getInstance().readConfiguration("D");

        if (zMem != null)
        {
            try
            {
                n = dbReceive.getAddress();
                c = zMem.getCentralino();

                ExternalDial eDial = new ExternalDial(u, p, n, c, action.getChannel(), "O");
                ris = eDial.send();

                if (ris)
                {
                    DispatcherMgr.getInstance().setServiceDialState(true);

                    do
                    {
                        try
                        {
                            Thread.sleep(10000);
                            countTimeOut--;
                        }
                        catch (Exception e)
                        {
                        }
                    }
                    while ((DispatcherMgr.getInstance().getServiceDialState()) &&
                            (countTimeOut > 0));
                }

                // Check Timeout connection
                if (countTimeOut <= 0)
                {
                    DispatcherMgr.getInstance().setServiceDialState(false);

                    try
                    {
                        Object[] par = 
                            {
                                "(" + dbReceive.getReceiver() + " " + dbReceive.getAddress() + ")"
                            };
                        EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                            EventDictionary.TYPE_WARNING, "D044", par);
                    }
                    catch (Exception e)
                    {
                    	e.printStackTrace();
                    	logger.error(e);
                    }
                }

                eDial = new ExternalDial(u, p, n, c, action.getChannel(), "C");
                eDial.send();
            }
            catch (Exception e)
            {
            	e.printStackTrace();
            	logger.error(e);
            }
        }

        return ris;
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
    
    
    private static String convertStringToHex(String str)
    {
     	  StringBuffer hex = new StringBuffer();
    	  for(int i = 0; i < str.length(); i++)
    	    hex.append(String.format("%04X", str.codePointAt(i)));
    	  return hex.toString();
      }
    
}
