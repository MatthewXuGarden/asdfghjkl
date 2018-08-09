package com.carel.supervisor.presentation.switchtech;

import com.carel.supervisor.dataaccess.db.Record;

import java.sql.Timestamp;


public class SwitchDev
{
    private static final String ID_SITE = "idsite";
    private static final String ID_SWITCH = "idswitch";
    private static final String ID_DEVICE = "iddevice";
    private static final String LASTUPDATE = "lastupdate";
    private Integer idsite = null;
    private Integer idswitch = null;
    private Integer iddevice = null;
    private Timestamp lastupdate = null;

    public SwitchDev(Record r)
    {
        this.idsite = (Integer) r.get(ID_SITE);
        this.idswitch = (Integer) r.get(ID_SWITCH);
        this.iddevice = (Integer) r.get(ID_DEVICE);
        this.lastupdate = (Timestamp) r.get(LASTUPDATE);
    }

    public Integer getIddevice()
    {
        return iddevice;
    }

    public void setIddevice(Integer iddevice)
    {
        this.iddevice = iddevice;
    }

    public Integer getIdswitch()
    {
        return idswitch;
    }

    public void setIdswitch(Integer idswitch)
    {
        this.idswitch = idswitch;
    }

    public Integer getIdsite()
    {
        return idsite;
    }

    public void setIdsite(Integer idsite)
    {
        this.idsite = idsite;
    }

    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    public void setLastupdate(Timestamp lastupdate)
    {
        this.lastupdate = lastupdate;
    }
}
