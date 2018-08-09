package com.carel.supervisor.presentation.switchtech;

import com.carel.supervisor.dataaccess.db.Record;

import java.sql.Timestamp;


public class SwitchConfig
{
    private static final String ID_SITE = "idsite";
    private static final String ID_SWITCH = "idswitch";
    private static final String AUTO_SWITCH = "autoswitch";
    private static final String HOUR = "hour";
    private static final String START_HOUR = "starthour";
    private static final String START_TYPE = "starttype";
    private static final String MANUAL_TYPE = "manualtype";
    private static final String AUTO_RESUME = "autoresume";
    private static final String SKIP_ALARM = "skipalarm";
    private static final String ALARM_WAIT = "alarmwait";
    private static final String DESCRIPTION = "description";
    private static final String LAST_UPDATE = "lastupdate";
    private Integer idsite = null;
    private Integer idswitch = null;
    private String autoswitch = null;
    private Integer hour = null;
    private Timestamp starthour = null;
    private Integer alarmwait = null;
    private String starttype = null;
    private String manualtype = null;
    private String autoresume = null;
    private String skipalarm = null;
    private String description = null;
    private Timestamp lastupdate = null;

    public SwitchConfig(Record r)
    {
        idsite = (Integer) r.get(ID_SITE);
        idswitch = (Integer) r.get(ID_SWITCH);
        autoswitch = r.get(AUTO_SWITCH).toString().trim();
        hour = (Integer) r.get(HOUR);
        starthour = (Timestamp) r.get(START_HOUR);
        starttype = r.get(START_TYPE).toString().trim();
        manualtype = r.get(MANUAL_TYPE).toString().trim();
        autoresume = r.get(AUTO_RESUME).toString().trim();
        skipalarm = r.get(SKIP_ALARM).toString().trim();
        alarmwait = (Integer) r.get(ALARM_WAIT);
        description = r.get(DESCRIPTION).toString().trim();
        lastupdate = (Timestamp) r.get(LAST_UPDATE);
    }

    public Integer getAlarmwait()
    {
        return alarmwait;
    }

    public void setAlarmwait(Integer alarmwait)
    {
        this.alarmwait = alarmwait;
    }

    public String getAutoresume()
    {
        return autoresume;
    }

    public void setAutoresume(String autorestore)
    {
        this.autoresume = autorestore;
    }

    public String getAutoswitch()
    {
        return autoswitch;
    }

    public void setAutoswitch(String autoswitch)
    {
        this.autoswitch = autoswitch;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getHour()
    {
        return hour;
    }

    public void setHour(Integer hour)
    {
        this.hour = hour;
    }

    public Integer getIdsite()
    {
        return idsite;
    }

    public void setIdsite(Integer idsite)
    {
        this.idsite = idsite;
    }

    public Integer getIdswitch()
    {
        return idswitch;
    }

    public void setIdswitch(Integer idswitch)
    {
        this.idswitch = idswitch;
    }

    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    public void setLastupdate(Timestamp lastupdate)
    {
        this.lastupdate = lastupdate;
    }

    public String getManualtype()
    {
        return manualtype;
    }

    public void setManualtype(String manualtype)
    {
        this.manualtype = manualtype;
    }

    public Timestamp getStarthour()
    {
        return starthour;
    }

    public void setStarthour(Timestamp starthour)
    {
        this.starthour = starthour;
    }

    public String getStarttype()
    {
        return starttype;
    }

    public void setStarttype(String starttype)
    {
        this.starttype = starttype;
    }

    public String getSkipalarm()
    {
        return skipalarm;
    }

    public void setSkipalarm(String todisplay)
    {
        this.skipalarm = todisplay;
    }
}
