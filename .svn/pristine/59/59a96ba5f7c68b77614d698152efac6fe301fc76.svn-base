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
import java.util.Iterator;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.DefaultCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.dataconfig.ProductInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.director.bms.BMSConfiguration;
import com.carel.supervisor.director.bms.BmsMgr;
import com.carel.supervisor.director.bms.FailureVar;
import com.carel.supervisor.dispatcher.DispatcherMgr;
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
import com.carel.supervisor.dispatcher.engine.mail.DispMailMessage;
import com.carel.supervisor.dispatcher.engine.mail.DispMailSender;
import com.carel.supervisor.dispatcher.memory.DMemory;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.EMemory;
import com.carel.supervisor.dispatcher.memory.FMemory;
import com.carel.supervisor.dispatcher.memory.SMemory;
import com.carel.supervisor.presentation.https2xml.XMLResponse;
import com.carel.supervisor.presentation.io.CioFAX;
import com.carel.supervisor.report.PrinterMgr2;


public class DispatcherDirectly {
   private String fisicDevice = "";

   public DispatcherDirectly(String device){
	   this.fisicDevice=device;
   }


    private int sendExecute(HSActionQBean action)
    {
        String recName = getReceiverString(action.getReceivers(),action.getType());
        int idEvent=-1;
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
        	catch (Exception e) {}
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
                
                idEvent=EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                                           EventDictionary.TYPE_INFO, "D041", p);
           }
            catch (Exception e1)
            {
            }
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        return idEvent;
    }

    private int sendFailure(HSActionQBean action)
    {
        int retryNum = action.getRetrynum();
        String recName = getReceiverString(action.getReceivers(),action.getType());
        int idEvent=-1;
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
                    
                    idEvent=EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                        EventDictionary.TYPE_WARNING, "D040", p);
                }
                catch (Exception e2)
                {
                }
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());
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
                	catch (Exception e) {}
                }
                idEvent=EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                                EventDictionary.TYPE_ERROR, "D019",
                                new Object[]
                                {
                                    this.fisicDevice,
                                    "<a onclick=evnviewDoc("+evId.intValue()+",'"+action.getType()+"')><u>"+DispatcherMgr.getInstance().decodeActionType(action.getType())+"</u></a>", recName
                                });
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
        }
        return idEvent;
    }

    private String getReceiverString(String idReceiver,String type)
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
        }

        return recName;
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
        catch (Exception e) {}

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
                    Logger logger = LoggerMgr.getLogger(this.getClass());
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
                    if (DispatcherMgr.getInstance().serviceExternal())
                    {
                        ExternalSms eSms = new ExternalSms(zMem.getCentralino(),
                                dbReceive.getAddress(), action.getPath(), zMem.getProviderLb(),
                                zMem.getCalltype(), action.getMessage(), action.getChannel());
                        risultato = eSms.send();
                    }
                    else
                    {
                        DispLaySms dSms = new DispLaySms(action.getPath(), zMem.getCentralino(),
                                zMem.getProviderLb(), zMem.getCalltype(), action.getMessage(),
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
                    if (DispatcherMgr.getInstance().serviceExternal())
                    {
                    	String sDate = DateUtils.date2String(new Timestamp(System.currentTimeMillis()), "yyyy/MM/dd HH:mm:ss");
                        ExternalMail eMail = new ExternalMail(zMem.getUser(), zMem.getPass(), zMem.getSender(),
                                sAddr, "PlantVisorProMail " + sDate, action.getMessage(),
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
                                    dbReceive.getAddress(), "PlantVisorPro Mail Message",
                                    "PlantVisorPro Mail Message");
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
                    catch (Exception e){}

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
                            Logger logger = LoggerMgr.getLogger(this.getClass());
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
                    }
                }

                eDial = new ExternalDial(u, p, n, c, action.getChannel(), "C");
                eDial.send();
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
        }

        return ris;
    }
    public int doTestIO(Integer idsite,String actiontype,String receiversid,String status,String actionsid){
        HSActionQBeanList obj = new HSActionQBeanList();
        HSActionQBean action = obj.getHsactionqueueByReceiversid(idsite,actiontype, receiversid, status, actionsid);
        boolean actionsIn = false;
        boolean ris = false;
        int idEvent=-1;

        if (action != null){
            actionsIn = true;
            ris = dispatchAction(action);

            if (ris){
            	idEvent= sendExecute(action);
            }else{
            	idEvent= sendFailure(action);
            }
        }

        return idEvent;
    }
}
