package com.carel.supervisor.dataaccess.event;

import java.sql.Timestamp;
import java.text.MessageFormat;

import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;


public class Event
{
    private static final String ID = "idevent";
    private static final String CATEGORY = "categorycode";
    private static final String MESSAGE = "messagecode";
    private static final String USEREVENT = "userevent";
    private static final String PARAMETERS = "parameters";
    private static final String LASTUPDATE = "lastupdate";
    private static final String TYPE = "type";
    private static final String SITE = "idsite";
    
    private int idevent = 0;
    private String categorycode = null;
    private String messagecode = null;
    private String category = null;
    private String message = null;
    private String user = null;
    private String parameters = null;
    private Timestamp lastupdate = null;
    private Integer type = null;
    private Integer idSite = null;

    public Event(Record record)
    {
        idevent = ((Integer) record.get(ID)).intValue();
        categorycode = UtilBean.trim(record.get(CATEGORY));
        messagecode = UtilBean.trim(record.get(MESSAGE));
        user = UtilBean.trim(record.get(USEREVENT));
        parameters = UtilBean.trim(record.get(PARAMETERS));
        lastupdate = (Timestamp) record.get(LASTUPDATE);
        type = (Integer) record.get(TYPE);
        idSite = (Integer) record.get(SITE);
    }

    /**
     * @return: String
     */
    public String getCategorycode()
    {
        return categorycode;
    }

    /**
     * @return: int
     */
    public int getIdevent()
    {
        return idevent;
    }

    public Integer getIdSite()
    {
        return idSite;
    }
    
    /**
     * @return: String
     */
    public String getMessagecode()
    {
        return messagecode;
    }

    /**
     * @return: String
     */
    public String getParameters()
    {
        return parameters;
    }

    /**
     * @return: String
     */
    public String getUser()
    {
        return user;
    }

    /**
     * @return: String
     */
    public String getCategory()
    {
        return category;
    }

    /**
     * @param category
     */
    public void setCategory(String category)
    {
        this.category = category;
    }

    /**
     * @return: String
     */
    public String getMessage()
    {
        String[] tmp = parameters.split(";");

        if ((this.message != null) && (tmp != null))
        {
            return MessageFormat.format(message, tmp);
        }
        else
        {
            return "";
        }
    }

    /**
     * @param message
     */
    public void setMessage(String message)
    {
        if (null != message)
        {
        	this.message = Replacer.replace(message,"'","''");
        }
    }

    /**
     * @return: Timestamp
     */
    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    /**
     * @param lastupdate
     */
    public void setLastupdate(Timestamp lastupdate)
    {
        this.lastupdate = lastupdate;
    }

	/**
	 * @return: Integer
	 */
	
	public Integer getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(Integer type) {
		this.type = type;
	}
    
    
}
