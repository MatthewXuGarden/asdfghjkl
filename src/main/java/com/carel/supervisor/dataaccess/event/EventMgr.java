package com.carel.supervisor.dataaccess.event;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.error.BlockingErrorMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;


public class EventMgr implements IInitializable
{
    private static final String DESCRIPTION = "description";
    private static final EventMgr me = new EventMgr();
    private static final Logger logger = LoggerMgr.getLogger(EventMgr.class);
    private List errors = new ArrayList();
    private List errorsReboot = new ArrayList();
    private EventErrorFilter filter = null;
    private boolean initialized = false;

    private EventMgr()
    {
    }

    public static EventMgr getInstance()
    {
        return me;
    }

    public boolean initialized()
    {
        return initialized;
    }

    public synchronized void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        if (!initialized)
        {
            try
            {
                log(new Integer(1), "System", "Start", EventDictionary.TYPE_INFO, "S011", null);
                log(new Integer(1), "System", "Start", EventDictionary.TYPE_INFO, "S009", null);
                filter = new EventErrorFilter(xmlStatic);
            }
            catch (Exception e)
            {
                throw new InvalidConfigurationException("", e);
            }

            initialized = true;
        }
    }

    public int log(Integer site, String user, String category, Integer type, String code,
        Object param)
    {
        return log(site, user, category, type, code, new Object[] { param });
    }

    public int log(Integer site, String user, String category, Integer type, String code,
        Object param1, Object param2)
    {
    	return log(site, user, category, type, code, new Object[] { param1, param2 });
    }

    public int log(Integer site, String user, String category, Integer type, String code,
        Object[] param)
    {
    	int result=-1;
        //Segnalazione per il guardiano
        if (type.equals(EventDictionary.TYPE_ERROR) && (filter.isToNotify(code) || filter.isToNotifyAndReboot(code)))
        {
            /*
             * Se Snooze active allora non inserisco. In questo modo
             * alla riattivazione del Guardiano non viene fatta 
             * nessuna segnalazione.
             * Controllato poche righe sotto per effettuare l'inserimento
             * del messaggio nelle Mappe.
             */
            String gsn = null;
            try {
                gsn = ProductInfoMgr.getInstance().getProductInfo().get("gsnooze");
            }
            catch(Exception e){}
            
            try
            {
                String langDef = LangUsedBeanList.getDefaultLanguage(1);

                String sql = "select description from cfmessage where languagecode = ? and messagecode = ?";
                RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                        new Object[] { langDef, code });
                String msg = (String) recordset.get(0).get(0);
                String msg2 = MessageFormat.format(msg, param);
                
                // Pulizia messagge nel caso in cui il code sia D019
                try
                {
                    if(code != null && code.equalsIgnoreCase("D019"))
                    {
                        int idxE = msg2.indexOf("</a>");
                        if(idxE != -1)
                        {
                            idxE += "</a>".length();
                            int idxS = msg2.indexOf("<a");
                            if(idxS != -1)
                                msg2 = msg2.substring(0,idxS) + msg2.substring(idxE);   
                        }
                    }
                }
                catch(Exception e){}
                // End
                
                String message = "Category [" + category +
                    "] - Description [" + msg2 + "]";
                
                if(gsn == null)
                {    
                    if (filter.isToNotifyAndReboot(code))
                    {
                    	errorsReboot.add(message);
                    }
                    else
                    {
                    	errors.add(message);
                    }
                }
            }
            catch (Exception e)
            {
                logger.error(e);
                if(gsn == null)
                {
                    String message = "Category [" + category +
                    "] - Description [" + code + "]";
                    if (filter.isToNotifyAndReboot(code))
                    {
                    	errorsReboot.add(message);
                    }
                    else
                    {
                    	errors.add(message);
                    }
                }
            }
        }

        try
        {
            String sql = "insert into hsevent values (?,?,?,?,?,?,?,?,?)";
            Object[] values = new Object[9];
            values[0] = SeqMgr.getInstance().next(null, "hsevent", "idevent");
            values[1] = BaseConfig.getPlantId();
            values[2] = site;
            values[3] = type;
            values[4] = category;
            values[5] = code;
            values[6] = user;
            values[7] = creaParam(param);
            values[8] = new Timestamp(System.currentTimeMillis());
            DatabaseMgr.getInstance().executeStatement(null, sql, values);
            result=Integer.parseInt(String.valueOf(values[0]));
        }
        catch (Exception e)
        {
            BlockingErrorMgr.getInstance().add("EVENTLOG IMPOSSIBLE");
            logger.error(e);
        }
        return result;
    }

    public int info(Integer site, String user, String category, String code, Object param)
    {
       return log(site, user, category, EventDictionary.TYPE_INFO, code, new Object[] { param });
    }

    public int info(Integer site, String user, String category, String code, Object param1,
        Object param2)
    {
    	return log(site, user, category, EventDictionary.TYPE_INFO, code, new Object[] { param1, param2 });
    }

    public int info(Integer site, String user, String category, String code, Object[] param)
    {
    	return log(site, user, category, EventDictionary.TYPE_INFO, code, param);
    }

    public int warning(Integer site, String user, String category, String code, Object param)
    {
    	return log(site, user, category, EventDictionary.TYPE_WARNING, code, new Object[] { param });
    }

    public void warning(Integer site, String user, String category, String code, Object param1,
        Object param2)
    {
        log(site, user, category, EventDictionary.TYPE_WARNING, code,
            new Object[] { param1, param2 });
    }

    public void warning(Integer site, String user, String category, String code, Object[] param)
    {
        log(site, user, category, EventDictionary.TYPE_WARNING, code, param);
    }

    public int error(Integer site, String user, String category, String code, Object param)
    {
    	return log(site, user, category, EventDictionary.TYPE_ERROR, code, new Object[] { param });
    }

    public int error(Integer site, String user, String category, String code, Object param1,
        Object param2)
    {
    	return log(site, user, category, EventDictionary.TYPE_ERROR, code, new Object[] { param1, param2 });
    }

    public int error(Integer site, String user, String category, String code, Object[] param)
    {
        return log(site, user, category, EventDictionary.TYPE_ERROR, code, param);
    }

    public String[] retriveCode(int site, String language)
        throws Exception
    {
        String[] tmp = null;
        String sql = "select distinct description from cfcategory where languagecode = ?";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { language });
        tmp = new String[recordset.size()];

        for (int i = 0; i < recordset.size(); i++)
        {
            tmp[i] = UtilBean.trim(recordset.get(i).get(DESCRIPTION));
        }

        return tmp;
    }

    public String[] retriveMessage(int site, String language)
        throws Exception
    {
        String[] tmp = null;
        String sql = "select distinct description from cfmessage where languagecode = ?";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { language });
        tmp = new String[recordset.size()];

        for (int i = 0; i < recordset.size(); i++)
        {
            tmp[i] = UtilBean.trim(recordset.get(i).get(DESCRIPTION));
        }

        return tmp;
    }
    
    public String retriveMessage(int site, String language, String messagecode)
    throws Exception
{
    String tmp = null;
    String sql = "select distinct description from cfmessage where languagecode = ? and messagecode = ?";
    RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
            new Object[] { language,messagecode });

    if(recordset != null && recordset.size()>0)
    {
        tmp = UtilBean.trim(recordset.get(0).get(DESCRIPTION));
    }

    return tmp;
}

    private String creaParam(Object[] param)
    {
        StringBuffer buffer = new StringBuffer();

        if (null == param)
        {
            return "";
        }

        for (int i = 0; i < param.length; i++)
        {
            buffer.append(param[i]);

            if (i < (param.length - 1))
            {
                buffer.append(";");
            }
        }
        //column length for parameters is 255
        String params = buffer.toString();
        if(params != null && params.length()>255)
        	return params.substring(0,255);
        return params;
    }

    public Event[] retriveLastEvent(int site, String language)
        throws Exception
    {
        Event[] events = null;
        StringBuffer sql = new StringBuffer();
        sql.append("select * from hsevent where idsite = ? order by lastupdate desc ");

        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),
                new Object[] { new Integer(site) });
        Record record = null;
        events = new Event[recordset.size()];

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            events[i] = new Event(record);
        }

        sql = new StringBuffer();
        sql.append("select * from cfmessage where languagecode = ?");
        recordset = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),
                new Object[] { language });

        Map message = new HashMap();

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            message.put(UtilBean.trim(record.get("messagecode")),
                UtilBean.trim(record.get("description")));
        }

        sql = new StringBuffer();
        sql.append("select * from cfcategory where languagecode = ?");

        recordset = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),
                new Object[] { language });

        Map category = new HashMap();

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            category.put(UtilBean.trim(record.get("categorycode")),
                UtilBean.trim(record.get("description")));
        }

        for (int i = 0; i < events.length; i++)
        {
            events[i].setCategory((String) category.get(events[i].getCategorycode()));
            events[i].setMessage((String) message.get(events[i].getMessagecode()));
        }

        return events;
    }

    public Event retriveEventById(int idevent, int site, String language)
        throws Exception
    {
        String sql = "select * from hsevent where idsite = ? and idevent= ? order by lastupdate desc";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(site), new Integer(idevent) });
        Event event = new Event(recordset.get(0));

        sql = "select * from cfmessage where languagecode = ? and messagecode = ?";
        recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { language, event.getMessagecode() });
        event.setMessage(UtilBean.trim(recordset.get(0).get("description")));

        sql = "select * from cfcategory where languagecode = ? and categorycode = ?";
        recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { language, event.getCategorycode() });

        if (recordset.size() != 0)
        {
            event.setCategory(UtilBean.trim(recordset.get(0).get("description")));
        }
        else
        {
            event.setCategory("");
        }

        return event;
    }
    public Event getEventById(int idevent, int site, String language){
    	Event event=null;
        try {
        	event=retriveEventById(idevent, site, language);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("EventMgr exception():"+e);
		}
        return event;
    }

    public Category[] retrieveCategory(int site, String language)
        throws Exception
    {
        Category[] category = null;
        String sql = "select categorycode,description from cfcategory where languagecode = ?";

        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql.toString(),
                new Object[] { language });
        Record record = null;
        category = new Category[recordset.size()];

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            category[i] = new Category(record);
        }

        return category;
    }
    
    public String getMessage()
    {
    	if (errors.size() > 0)
    	{
    		String message = (String)errors.remove(0);
    		return message;
    	}
    	else
    	{
    		return null;
    	}
    }
    
    public String getMessageAndReboot()
    {
    	if (errorsReboot.size() > 0)
    	{
    		String message = (String)errorsReboot.remove(0);
    		return message;
    	}
    	else
    	{
    		return null;
    	}
    }
    
    public boolean needShowParameters(String messageCode,String params)
    {
    	if("D000".equalsIgnoreCase(messageCode) && params != null && params.length()>0)
    		return true;
    	return false;
    }
}
