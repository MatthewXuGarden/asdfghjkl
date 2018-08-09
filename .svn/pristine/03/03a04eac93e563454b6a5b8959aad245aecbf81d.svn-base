package com.carel.supervisor.presentation.alarmsevents;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.conversion.Replacer;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.datalog.impl.AlarmLog;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.presentation.bean.DeviceStructure;


public class AlarmEvent
{
    private static final String T = "t";
	private static final String ID = "id";
    private static final String CATEGORY = "categorycode";
    private static final String MESSAGE = "messagecode";
    private static final String USEREVENT = "userevent";
    private static final String PARAMETERS = "parameters";
    private static final String STARTTIME = "starttime";
    private static final String ENDTIME = "endtime";
    private static final String TYPE = "type";
    private static final String SITE = "idsite";
    private static final String PRIORITY = "priority";
    private static final String DESCRIPTION = "description";
    private static final String DEVICEID = "iddevice";
    
    private String t;
    private int id = -1;
    private Date date = null;
    private String alarmDevice = "";
    private int alarmDeviceID = -1;
    private String group = "";
    private String alarmVariable = "";
    private int priority = -1;
    private Date starttime = null;
    private Date endtime = null;
    private Date resettime = null;
    private int site = -1;
    private String ackuser = null;
    private String delelactionuser = null;
    private String resetuser = null;
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

    public AlarmEvent(int id, Date date, String alarmDevice, int devID,
        String group, String alarmVariable, int priority)
    {
    	this.t="A";
        this.id = id;
        this.date = date;
        this.alarmDevice = alarmDevice;
        this.alarmDeviceID = devID;
        this.group = group;
        this.alarmVariable = alarmVariable;
        this.priority = priority;
    }
    
    public AlarmEvent(AlarmLog alarmLog, String deviceDesc)
    {
    	t="A";
        id = alarmLog.getId();
        date = alarmLog.getStarttime();
        group = "";
        alarmDevice = deviceDesc;
        alarmDeviceID = alarmLog.getIddevice();
        priority = Integer.parseInt(alarmLog.getPriority());
        starttime = alarmLog.getStarttime();
        endtime = alarmLog.getEndtime();
        resettime = alarmLog.getResettime();
        site = alarmLog.getSite();
        ackuser = alarmLog.getAckuser();
        delelactionuser = alarmLog.getDelactionuser();
        resetuser = alarmLog.getResetuser();
        alarmVariable = alarmLog.getDescription();
    }
    
    //costruttore per modifiche parametri dal campo 
    public AlarmEvent(Record record, String msg, String deviceDesc)
    {
//         int priority 
//         Date endtime 
//         Date resettime 
//         String ackuser 
//         String delelactionuser 
//         String resetuser 
//
//         String categorycode 
//         String messagecode 
//         String category 
//         Timestamp lastupdate 
//         Integer type 
    	t=UtilBean.trim( (String) record.get(T));
        id = ((Integer) record.get(ID)).intValue();
        idevent = ((Integer) record.get(ID)).intValue();
        date = (Timestamp) record.get(STARTTIME);
        group="";
        alarmDevice = deviceDesc;
        alarmDeviceID = ((Integer) record.get(DEVICEID)).intValue();
        date =(Timestamp) record.get(STARTTIME);
        starttime =date;
        site = (Integer) record.get(SITE);
        idSite=site;
        alarmVariable = record.get(DESCRIPTION).toString();
        message=msg;
        user = UtilBean.trim(record.get(USEREVENT));
        parameters="";
        lastupdate = new Timestamp( date.getTime());
        category=deviceDesc;
    }
    
    public AlarmEvent(Record record)
    {
    	t=UtilBean.trim( (String) record.get(T));
        idevent = ((Integer) record.get(ID)).intValue();
        categorycode = UtilBean.trim(record.get(CATEGORY));
        messagecode = UtilBean.trim(record.get(MESSAGE));
        user = UtilBean.trim(record.get(USEREVENT));
        parameters = UtilBean.trim(record.get(PARAMETERS));
        lastupdate = (Timestamp) record.get(STARTTIME);
        type = (Integer) record.get(TYPE);
        idSite = (Integer) record.get(SITE);
    }
    
    public AlarmEvent(Record record, Map<String, String> message, Map<String, String> categ)
    {
    	t=(UtilBean.trim( (String) record.get(T)));
        idevent = ((Integer) record.get(ID)).intValue();
        categorycode = UtilBean.trim(record.get(CATEGORY));
        messagecode = UtilBean.trim(record.get(MESSAGE));
        user = UtilBean.trim(record.get(USEREVENT));
        parameters = UtilBean.trim(record.get(PARAMETERS));
        lastupdate = (Timestamp) record.get(STARTTIME);
        type = (Integer) record.get(TYPE);
        idSite = (Integer) record.get(SITE);
        
        this.setCategory((String) categ.get(categorycode));
    	this.setMessage((String) message.get(messagecode));
    	this.setMessage(this.getMessageWithParams());
    }
    public AlarmEvent(AlarmLog alarmLog, DeviceStructure deviceStructure)
    {
    	t="A";
        id = alarmLog.getId();
        date = alarmLog.getStarttime();
        if(deviceStructure != null) {
        	group = deviceStructure.getGroupName();
        	alarmDevice = deviceStructure.getDescription();
        	alarmDeviceID = deviceStructure.getIdDevice();
        }
        else {
        	group = "";
        	alarmDevice = "";
        	alarmDeviceID = -1;
        }
        priority = Integer.parseInt(alarmLog.getPriority());
        starttime = alarmLog.getStarttime();
        endtime = alarmLog.getEndtime();
        resettime = alarmLog.getResettime();
        site = alarmLog.getSite();
        ackuser = alarmLog.getAckuser();
        delelactionuser = alarmLog.getDelactionuser();
        resetuser = alarmLog.getResetuser();
        alarmVariable = alarmLog.getDescription();
    }
    
    public AlarmEvent(Record rec, DeviceStructure deviceStructure)
    {
    	t=UtilBean.trim( (String) rec.get(T));
        id = (Integer)rec.get(ID);
        site = (Integer)rec.get(SITE);
        date = (Timestamp) rec.get(STARTTIME);
        if(deviceStructure != null)
        {
        	group = deviceStructure.getGroupName();
        	alarmDevice = deviceStructure.getDescription();
        	alarmDeviceID = deviceStructure.getIdDevice();
        }
        priority = Integer.parseInt(rec.get(PRIORITY).toString().trim()) ; 
        starttime = (Timestamp) rec.get(STARTTIME);
        endtime = (Timestamp) rec.get(ENDTIME);
        alarmVariable = rec.get(DESCRIPTION).toString();
    }

    public Date getDate()
    {
        return date;
    }

    public String getAlarmVariable()
    {
        return alarmVariable;
    }

    public String getGroup()
    {
        return group;
    }

    public int getId()
    {
        return id;
    }

    public int getPriority()
    {
        return priority;
    }

    public String getAlarmDevice()
    {
        return alarmDevice;
    }
    
    public int getAlarmDeviceID()
    {
    	return alarmDeviceID;
    }
    
    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setAlarmVariable(String alarmVariable)
    {
        if (null != alarmVariable)
        {
            this.alarmVariable = alarmVariable;
        }
        else
        {
            this.alarmVariable = "";
        }
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public void setAlarmDevice(String alarmDevice)
    {
        this.alarmDevice = alarmDevice;
    }

    public void setAlarmDeviceID(int deviceID)
    {
    	this.alarmDeviceID = deviceID;
    }
    
    /**
         * @return: String
         */
    public String getAckuser()
    {
        return ackuser;
    }

    /**
     * @param ackuser
     */
    public void setAckuser(String ackuser)
    {
        this.ackuser = ackuser;
    }

    /**
     * @return: String
     */
    public String getDelelactionuser()
    {
        return delelactionuser;
    }

    /**
     * @param delelactionuser
     */
    public void setDelelactionuser(String delelactionuser)
    {
        this.delelactionuser = delelactionuser;
    }

    /**
     * @return: Timestamp
     */
    public Date getEndtime()
    {
        return endtime;
    }

    /**
     * @param endtime
     */
    public void setEndtime(Timestamp endtime)
    {
        this.endtime = endtime;
    }

    /**
     * @return: String
     */
    public String getResetuser()
    {
        return resetuser;
    }

    /**
     * @param resetuser
     */
    public void setResetuser(String resetuser)
    {
        this.resetuser = resetuser;
    }

    /**
     * @return: int
     */
    public int getSite()
    {
        return site;
    }

    /**
     * @param site
     */
    public void setSite(int site)
    {
        this.site = site;
    }

    /**
     * @return: Timestamp
     */
    public Date getStarttime()
    {
        return starttime;
    }

    /**
     * @param starttime
     */
    public void setStarttime(Timestamp starttime)
    {
        this.starttime = starttime;
    }

    /**
         * @return: Date
         */
    public Date getResettime()
    {
        return resettime;
    }

    /**
     * @param resettime
     */
    public void setResettime(Date resettime)
    {
        this.resettime = resettime;
    }

    public String formatDate(Date data)
    {
        return DateUtils.date2String(data, "yyyy/MM/dd HH:mm:ss"); 
    }

	public int getIdevent() {
		return idevent;
	}

	public void setIdevent(int idevent) {
		this.idevent = idevent;
	}

	public String getCategorycode() {
		return categorycode;
	}

	public void setCategorycode(String categorycode) {
		this.categorycode = categorycode;
	}

	public String getMessagecode() {
		return messagecode;
	}

	public void setMessagecode(String messagecode) {
		this.messagecode = messagecode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMessageWithParams()
    {
        if (this.parameters != null)
        {
			String[] tmp = parameters.split(";");
	
	        if ((this.message != null) && (tmp != null))
	        {
	            return MessageFormat.format(message, tmp);
	        }
        }

        return "";
    }
	
	public String getMessage() {
		//return message;
		if (this.message != null)
        {
            return MessageFormat.format(message, "");
        }
		else
			return "";
	}

	public void setMessage(String message) {
		//this.message = message;
		if (message != null)
        {
        	this.message = Replacer.replace(message,"'","''");
        }
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public Timestamp getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(Timestamp lastupdate) {
		this.lastupdate = lastupdate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIdSite() {
		return idSite;
	}

	public void setIdSite(Integer idSite) {
		this.idSite = idSite;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getT() {
		return t;
	}
}
