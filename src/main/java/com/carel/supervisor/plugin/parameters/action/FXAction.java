package com.carel.supervisor.plugin.parameters.action;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.VariableHelper;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.action.DispatcherAction;
import com.carel.supervisor.dispatcher.bean.HSActionQBean;
import com.carel.supervisor.dispatcher.bean.HSActionQBeanList;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.plugin.parameters.dataaccess.ParametersEvent;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.text.RtfTab;


public class FXAction extends DispatcherAction
{
    public static final String SPLIT = ";";

    public FXAction(Integer key, Integer id, Integer pri, Integer sta, String rec, Timestamp itime,
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

    public void buildTemplate(String pathDir) throws Exception
    {
//        String[] tmplString = null;

//        if (!this.isAlarm())
//        {
//            Object[] obj = 
//                {
//                    DateUtils.date2String(new Timestamp(System.currentTimeMillis()),
//                        "yyyy/MM/dd HH:mm:ss")
//                };
//        }

        //20090109 -- Nicola Compagno
        // generation of rtf doc changed.
        // iTex utilities now used to support UTF8 on rtf documents
        // NOTE: RTF document generation is stricly related with fax preview (HTML) from events table (see SRVLDispDoc.java)
        generateRtfDocs(pathDir);        
    }

    public int[] putActionInQueue() throws Exception
    {
        List keyact = this.getKeyAction();
        int[] ret = new int[0];

        String grpactid = "";

        for (int j = 0; j < keyact.size(); j++)
        {
            grpactid += (((Integer) keyact.get(j)).intValue() + ",");
        }

        grpactid = grpactid.substring(0, grpactid.length() - 1);

        HSActionQBeanList actionQList = new HSActionQBeanList();

        HSActionQBean actionQ = null;
        Integer key = null;

        String[] receiver = this.getRecepients();
        String[] paths = this.getPathFiles();

        boolean allOk = true;

        if (receiver != null)
        {
            for (int i = 0; i < receiver.length; i++)
            {
                try
                {
                    key = SeqMgr.getInstance().next(null, "hsactionqueue", "idhsactionqueue");
                    actionQ = new HSActionQBean(key.intValue(), this.getNameSite(),
                            this.getIdSite(), this.getPriority(), this.getRetryNum(),
                            this.getRetryAfter(), this.getFisicDevice(), this.getTypeAction(), 1,
                            paths[i], "", receiver[i], grpactid);

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

            if (allOk)
            {
                ret = new int[keyact.size()];

                for (int i = 0; i < ret.length; i++)
                {
                    ret[i] = ((Integer) keyact.get(i)).intValue();
                }
            }
        }

        return ret;
    }

    private void generateRtfDocs(String pathDir) throws Exception
    {
    	String[] receivers = this.getRecepients();
    	String[] infoSender = this.getInfoSender(this.getIdSite());
    	String repPath = DispatcherMgr.getInstance().getRepositoryPath();
    	String filePath = "";
    	String rtfFilePath = "";
    	FileOutputStream fosRtf = null;
    	
    	String s = this.getTemplate();
    	
    	try
        {
    		// Import fax header file (rtf)
	    	String templatePath = this.getResourcePath(pathDir, "PVSendFax_light.rtf");
	    	FileInputStream fr = new FileInputStream(templatePath);
	    	
	    	//Write n docs. One for each receiver.
	    	for (int i = 0; i < receivers.length; i++)
	        {
                DispatcherBook db = DispatcherMgr.getInstance().getReceiverInfo(Integer.parseInt(receivers[i]));
                filePath = repPath + this.getTypeAction() + i + "_" + String.valueOf(System.currentTimeMillis());
                rtfFilePath = filePath + ".rtf";
                fosRtf = new FileOutputStream(rtfFilePath);

                
                Document document = new Document();
    			RtfWriter2 rtfw2 = RtfWriter2.getInstance(document, fosRtf);
    			document.open();
                
                //Document creation
    			
				//import document template (only header)
    			rtfw2.importRtfDocument(fr);

			    Font helvetica11Bold = new Font(Font.HELVETICA, Font.DEFAULTSIZE,
						Font.BOLD);
				helvetica11Bold.setSize(11F);

				Font helvetica11 = new Font(Font.HELVETICA, Font.DEFAULTSIZE,
						Font.NORMAL);
				helvetica11.setSize(11F);

				Font courier11Bold = new Font(Font.COURIER, Font.DEFAULTSIZE,
						Font.BOLD);
				courier11Bold.setSize(11F);

				Paragraph p = new Paragraph();
				p.setFont(helvetica11);

				RtfTab tab = new RtfTab(260, RtfTab.TAB_LEFT_ALIGN);
				p.add(tab);

				p.setFont(helvetica11Bold);
				p.add("From / Da / Von / De / Desde:");
				p.setFont(helvetica11);
				
				//insert sender name (site name)
				p.add("\t"+infoSender[0]+"\n");
				
				p.setFont(helvetica11Bold);
				p.add("From No. / Da Num. / Von Nr. / De No. / De Nu.:");
				p.setFont(helvetica11);
				
				//insert sender number
				p.add("\t"+infoSender[1]+"\n");
				
				p.setFont(helvetica11Bold);
				p.add("To No. / A Num. / Zu Nr. / À No. / A Nu.:");
				p.setFont(helvetica11);
				
				//insert receiver name
				p.add("\t"+db.getReceiver()+"\n");
				
				p.setFont(helvetica11Bold);
				p.add("Date / Data / Datum / Date / Fecha:");
				p.setFont(helvetica11);
				p.add("\t"+DateUtils.date2String(new Date(System.currentTimeMillis()), "yyyy/MM/dd HH:mm:ss")+"\n");
				document.add(p);
				p = new Paragraph();
				p.setAlignment(Element.ALIGN_CENTER);
				p.add("--------------------------------------------------");
				p.add("\n\n");
				document.add(p);

				
				System.out.println("");
				for (int ii = 0; ii < this.getIdVariable().size(); ii++) {
					String lang = LangUsedBeanList.getDefaultLanguage(1);
					ParametersEvent pe = new ParametersEvent(lang, this.getIdVariable().get(ii));
					p.add( pe.getDatetime().toString()+":"+ pe.getEventtype()+" "+
			           pe.getUsername() + " "+ 
			           pe.getDev_descr() + " - " + pe.getVar_descr() +" "+
			           pe.getStartingvalue() + " - > "+pe.getEndingvalue()+
			           " \n\n");
				}
				
				
				document.close();
				
				fosRtf.close();
	        }
	    	
	    	fr.close();
	    	this.addPathFile(rtfFilePath);
    	
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }
        
   
    /*
     * Build standard body alarm:
     * - TIME ALARM START | TIME ALARM END
     * - DESCRIPTION VARIABLE
     * - CURRENT VALUE OF VARIABLE
     */
    private Document buildBodyAlarm(String language, Document doc)
    {
        List lstartime = this.getStartTime();
        List lendtime = this.getEndTime();
        List lVariable = this.getIdVariable();
        int[] idVar = null;
        String description = "";
        Timestamp dateAlarm = null;
        Map descMap = new HashMap();
        String[] descriptions = new String[0];

        idVar = new int[lVariable.size()];

        for (int i = 0; i < idVar.length; i++)
        {
            idVar[i] = ((Integer) lVariable.get(i)).intValue();
        }

        descMap = VariableHelper.getDescriptions(language, this.getIdSite(), idVar);

        try
        {
        	Paragraph p = new Paragraph();
        	Font courier11 = new Font(Font.COURIER, Font.DEFAULTSIZE, Font.NORMAL);
			courier11.setSize(11F);
			
			p.setFont(courier11);
        	
			RtfTab tab = new RtfTab(120, RtfTab.TAB_LEFT_ALIGN);
			p.add(tab);
			
        	for (int i = 0; i < idVar.length; i++)
            {
            	            	
            	descriptions = (String[]) descMap.get(new Integer(idVar[i]));

                try
                {
                    dateAlarm = (Timestamp) lendtime.get(i);
                }
                catch (Exception e)
                {
                    dateAlarm = null;
                }

                if (dateAlarm == null)
                {
                	p.add("START   ");

                    try
                    {
                        dateAlarm = (Timestamp) lstartime.get(i);
                    }
                    catch (Exception e)
                    {
                        dateAlarm = null;
                    }
                }
                else
                {
                	p.add("END ");
                }

                p.add(DateUtils.date2String(dateAlarm, "yyyy/MM/dd HH:mm:ss "));

                description = descriptions[1];

                if (description == null)
                {
                    description = "DEVICE OF VARIABLE " + idVar[i] + "NO DESC ";
                }

                p.add("\t"+descriptions[1]+"\n");
                
                description = descriptions[0];

                if (description == null)
                {
                    description = "VARIABLE " + idVar[i] + "NO DESC ";
                }

                p.add("- "+description+"\n\n");
            }
        	
        	doc.add(p);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

    	return doc;
    }
  
    /*
     * Build standard body alive:
     * - TIME ALIVE
     */
    private Document buildBodyAlive(String language, Document doc)
    {
    	Paragraph p = new Paragraph();
    	try
    	{
    		p.add("ALIVE SIGNAL FAX");
    		doc.add(p);
    	}
    	catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    	return doc;
    }
}
