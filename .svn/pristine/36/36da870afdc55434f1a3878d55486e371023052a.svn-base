package com.carel.supervisor.presentation.alarms;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.dataaccess.datalog.impl.AlarmLog;
import com.carel.supervisor.presentation.bean.DeviceStructure;

import java.sql.Timestamp;

import java.util.Date;


public class Alarm
{
    private int id = -1;
    private Date date = null;
    private String alarmDevice = "";
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
    //add by kevin, add a link to device detail page
    private int alarmIDDevice = -1;

    public Alarm(int id, Date date, String alarmDevice,
        String group, String alarmVariable, int priority)
    {
        this.id = id;
        this.date = date;
        this.alarmDevice = alarmDevice;
        this.group = group;
        this.alarmVariable = alarmVariable;
        this.priority = priority;
    }
    
    public Alarm(AlarmLog alarmLog, String deviceDesc)
    {
        id = alarmLog.getId();
        date = alarmLog.getStarttime();
        group = "";
        alarmDevice = deviceDesc;
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
    
    public Alarm(AlarmLog alarmLog, DeviceStructure deviceStructure)
    {
        id = alarmLog.getId();
        date = alarmLog.getStarttime();
        if(deviceStructure != null) {
        	group = deviceStructure.getGroupName();
        	alarmDevice = deviceStructure.getDescription();
        	alarmIDDevice = deviceStructure.getIdDevice();
        }
        else {
        	group = "";
        	alarmDevice = "";
        	alarmIDDevice = -1;
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
    
    public void setAlarmIDDevice(int id)
    {
    	this.alarmIDDevice = id;
    }
    public int getAlarmIDDevice()
    {
    	return this.alarmIDDevice;
    }
}
