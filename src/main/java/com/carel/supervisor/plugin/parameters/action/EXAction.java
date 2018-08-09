package com.carel.supervisor.plugin.parameters.action;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.action.DispatcherAction;
import com.carel.supervisor.dispatcher.action.PAction;
import com.carel.supervisor.dispatcher.bean.HSActionBeanList;
import com.carel.supervisor.dispatcher.bean.HSActionQBean;
import com.carel.supervisor.dispatcher.bean.HSActionQBeanList;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.engine.mail.DispMailMessage;
import com.carel.supervisor.dispatcher.engine.mail.DispMailRetryMgr;
import com.carel.supervisor.dispatcher.engine.mail.DispMailSender;
import com.carel.supervisor.dispatcher.main.DispDocLinker;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.EMemory;
import com.carel.supervisor.plugin.parameters.ParametersMgr;
import com.carel.supervisor.plugin.parameters.dataaccess.ParametersEvent;


public class EXAction extends PAction
{
    public static final String TYPE_LAN = "L";
    public static final String TYPE_DUP = "D";
    public static final String SPLIT = ";";


    private String messaggio="";
    
    public EXAction(Integer key, Integer id, Integer pri, Integer sta, String rec, Timestamp itime,
        Timestamp utime, String tmpl, String type, Boolean isalarm, Integer idvar, Timestamp start,
        Timestamp end)
    {
        super(key, id, pri, sta, rec, itime, utime, tmpl, type, isalarm, idvar, start, end);
    }

    protected String[] initializedRecepients(String recepient)
    {
        String[] recRet = new String[0];

        if (recepient != null)
        {
            recRet = recepient.split(SPLIT);
        }

        Arrays.sort(recRet);

        return recRet;
    }

    public int[] putActionInQueue() throws Exception
    {
        List<Integer> keyact = this.getKeyAction();
        int[] ret = new int[0];
       // String[] listFile = this.getPathFiles();
        boolean allOk = true;
        boolean byLan = false;
        String[] receivers = this.getRecepients();

        // Get ConfMail from memory
        EMemory eMem = (EMemory) DispMemMgr.getInstance().readConfiguration(this.getTypeAction());
        
        // Build File Path
       // String pathFile = listFile[0];
       // pathFile = DispatcherMgr.getInstance().getRepositoryPath() + pathFile;
        
        if (eMem != null)
        {
            if (eMem.getType().equalsIgnoreCase(TYPE_LAN))
            {
                byLan = true;
                allOk = sendByLAN(eMem.getSmtp(),eMem.getPort(), eMem.getSender(), receivers, 
                                  "",eMem.getRetryNum(),eMem.getRetryAfter(),
                                  eMem.getUser(),eMem.getPass(),eMem.getEncryption());
            }
            else if (eMem.getType().equalsIgnoreCase(TYPE_DUP))
            {
                HSActionQBeanList actionQList = new HSActionQBeanList();
                HSActionQBean actionQ = null;
                Integer key = null;

                if (receivers != null)
                {
                    String grpactid = "";

                    for (int j = 0; j < keyact.size(); j++)
                    {
                        grpactid += (((Integer) keyact.get(j)).intValue() + ",");
                    }

                    grpactid = grpactid.substring(0, grpactid.length() - 1);

                    for (int i = 0; i < receivers.length; i++)
                    {
                        try
                        {
                            key = SeqMgr.getInstance().next(null, "hsactionqueue", "idhsactionqueue");
                            actionQ = new HSActionQBean(key.intValue(), this.getNameSite(),
                                    this.getIdSite(), this.getPriority(), this.getRetryNum(),
                                    this.getRetryAfter(), this.getFisicDevice(),
                                    this.getTypeAction(), 1, "", this.siteName,
                                    receivers[i], grpactid);

                            actionQList.addAction(actionQ);
                        }
                        catch (Exception e)
                        {
                            allOk = false;

                            Logger logger = LoggerMgr.getLogger(this.getClass());
                            logger.error(e);
                        }
                    }

                    if (allOk)
                    {
                        allOk = actionQList.insertActions();
                    }
                }
            }

            if (allOk)
            {
                ret = new int[keyact.size()];

                for (int i = 0; i < ret.length; i++)
                {
                    ret[i] = ((Integer) keyact.get(i)).intValue();
                }
            }

            if (byLan)
            {
                ret = new int[keyact.size()];

                for (int i = 0; i < ret.length; i++)
                {
                    ret[i] = ((Integer) keyact.get(i)).intValue();
                }

                HSActionBeanList actionbean = new HSActionBeanList();

                if (allOk)
                {
                    actionbean.updateToSendActionList(ret);
                }
                else
                {
                    actionbean.updateToDiscardActionList(ret);
                }

                // Clear array
                ret = new int[0];
            }
        }

        return ret;
    }

    private boolean sendByLAN(String smtp, String port, String sender, String[] idReceivers, 
    						  String pathAtt,int retryn,int retrya,String user,String pass,String encryption)
    {
        boolean ret = true;
        DispatcherBook db = null;
        String to = "";
        String sDate = "";
        
        if (idReceivers != null)
        {
            try
            {
                // Load receivers's mail
                for (int i = 0; i < idReceivers.length; i++)
                {
                    db = DispatcherMgr.getInstance().getReceiverInfo(Integer.parseInt(
                                idReceivers[i]));
                    to += (db.getAddress() + ",");
                }

                to = to.substring(0, to.length() - 1);
                
                // Add Timestamp
                sDate = DateUtils.date2String(new Timestamp(System.currentTimeMillis()), "yyyy/MM/dd HH:mm:ss");
                
                
                String bodymail = "PlantVisorProMail\n<br/>\n Site: "+this.siteName+"\n<br/>\nDate: " + sDate+" \n<br/>\n "+messaggio;
                
                // Biolo: leggere file HTML, e inserirlo nel body della mail
//                try {
//                	String path_html = pathAtt.substring(0,pathAtt.length()-4);
//                	path_html+= ".HTML";
//                	bodymail = ReadFile.readFromFile(path_html);
//				} catch (Exception e) {
//	                Logger logger = LoggerMgr.getLogger(this.getClass());
//	                logger.error(e.getMessage());
//				}
                
                /*DispMailMessage message = new DispMailMessage(sender, to, this.siteName +" - "+sDate,
                        "PlantVisorProMail\nDate " + sDate );*/
                
                DispMailMessage message = new DispMailMessage(sender, to, "Parameter Control Warning: "+this.siteName +" - "+sDate,
                		bodymail );
                message.setAttach(pathAtt);

                DispMailSender mailer = new DispMailSender(smtp, user, pass, message);
                mailer.setRetryNum(retryn);
                mailer.setRetryAfter(retrya);
                mailer.setEncryption(encryption);
                mailer.setPort(port);
                ret = mailer.sendMessage();
                
                // Aggiunta per retry
                if(!ret)
                    DispMailRetryMgr.getInstance().addMail(mailer);
            }
            catch (Exception e)
            {
                ret = false;

                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
        }
        else
        {
            ret = false;
        }

        if (ret)
        {
            Object[] p = null;
            String recName = "";

            try
            {
                for (int i = 0; i < idReceivers.length; i++)
                {
                    db = DispatcherMgr.getInstance().getReceiverInfo(Integer.parseInt(
                                idReceivers[i]));
                    recName = "(" + db.getReceiver() + " " + db.getAddress() + ")";
                    
                    Integer evId = SeqMgr.getInstance().next(null, "hsdocdispsent", "idevent");
                    DispDocLinker.insertDoc(1,evId,"E",pathAtt);
                    
                    p = new Object[]{ DispatcherMgr.getInstance().decodeActionType("E"), recName };
                    
                    EventMgr.getInstance().log(new Integer(1),"Dispatcher","Action",EventDictionary.TYPE_INFO,"D041",p);
                }
            }
            catch (Exception e1)
            {
            }
        }
        
        

        return ret;
    }

	@Override
	public void buildTemplate(String pathDir) throws Exception {
		String[] infoSender = this.getInfoSender(this.getIdSite());
		if ((infoSender != null) && (infoSender.length > 0)) {
			siteName = infoSender[0];
		}
		String lang = LangUsedBeanList.getDefaultLanguage(1);	
		LangService l = LangMgr.getInstance().getLangService(lang);
		
		
		String modString = l.getString("parameters", "modify");
		String rolString = l.getString("parameters", "rollback");
		String phoString = l.getString("parameters", "photo");		 
		
		System.out.println("");
		for (int i = 0; i < this.getIdVariable().size(); i++) {
			
			String typeInLang ="";
			ParametersEvent pe = new ParametersEvent(lang, this.getIdVariable().get(i));

			if (pe.getEventtype().equalsIgnoreCase(ParametersMgr.MODIFYDBCODE))
			{
				typeInLang=modString;
			}
			else if (pe.getEventtype().equalsIgnoreCase(ParametersMgr.ROLLBACKCODE))
			{
				typeInLang=rolString;
			}
			else if (pe.getEventtype().equalsIgnoreCase(ParametersMgr.TAKENPHOTOGRAPHYCODE))
			{
				typeInLang=phoString;
			}
			
			messaggio +=pe.getDatetime().toString()+":"+ typeInLang+" "+
	           pe.getUsername() + " "+ 
	           pe.getDev_descr() + " - " + pe.getVar_descr() +" "+
	           pe.getStartingvalue() + " - > "+pe.getEndingvalue()+
	           " \n<br/>\n";
		}
		
		
	}

	@Override
	public boolean compareAction(DispatcherAction da) {
        String[] dest = da.getRecepients();
        boolean ret = false;
        // Only if the same action and have the same template
        if (this.getTypeAction().equalsIgnoreCase(da.getTypeAction()))
        {
            // Only if recepients are the same
            if (dest.length == this.recepients.length)
            {
                for (int i = 0; i < dest.length; i++)
                {
                    ret = this.containsRecepient(dest[i]);

                    if (!ret)
                    {
                        break;
                    }
                }
            }
        }
        return ret;
	}
	
	
}
