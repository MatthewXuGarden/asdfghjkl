package com.carel.supervisor.presentation.switchtech;

import com.carel.supervisor.dataaccess.db.Record;

import java.sql.Timestamp;


public class SwitchStatus
{
    private static final String ID_SITE = "idsite";
    private static final String ID_SWITCH = "idswitch";
    private static final String RUNNING = "running";
    private static final String AUTOSWITCH = "autoswitch";
    private static final String TYPE = "type";
    private static final String LAST_SWITCH = "lastswitch";
    private static final String NEXT_SWITCH = "nextswitch";
    private static final String LAST_UPDATE = "lastupdate";
    private Integer idsite = null;
    private Integer idswitch = null;
    private String running = null;
    private String autoswitch = null;
    private String type = null;
    private Timestamp lastswitch = null;
    private Timestamp nextswitch = null;
    private Timestamp lastupdate = null;

    public SwitchStatus(Record r)
    {
        this.idsite = (Integer) r.get(ID_SITE);
        this.idswitch = (Integer) r.get(ID_SWITCH);
        this.running = r.get(RUNNING).toString().trim();
        this.autoswitch = r.get(AUTOSWITCH).toString().trim();
        this.type = r.get(TYPE).toString().trim();
        this.lastswitch = (Timestamp) r.get(LAST_SWITCH);
        this.nextswitch = (Timestamp) r.get(NEXT_SWITCH);
        this.lastupdate = (Timestamp) r.get(LAST_UPDATE);
    }

    public String getAutoswitch()
    {
        return autoswitch;
    }

    public void setAutoswitch(String autoswitch)
    {
        this.autoswitch = autoswitch;
    }

    public Integer getIdswitch()
    {
        return idswitch;
    }

    public void setIdswitch(Integer id_switch)
    {
        this.idswitch = id_switch;
    }

    public Integer getIdsite()
    {
        return idsite;
    }

    public void setIdsite(Integer idsite)
    {
        this.idsite = idsite;
    }

    public Timestamp getLastswitch()
    {
        return lastswitch;
    }

    public void setLastswitch(Timestamp lastswitch)
    {
        this.lastswitch = lastswitch;
    }

    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    public void setLastupdate(Timestamp lastupdate)
    {
        this.lastupdate = lastupdate;
    }

    public Timestamp getNextswitch()
    {
        return nextswitch;
    }

    public void setNextswitch(Timestamp nextswitch)
    {
        this.nextswitch = nextswitch;
    }

    public String getRunning()
    {
        return running;
    }

    public void setRunning(String running)
    {
        this.running = running;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
