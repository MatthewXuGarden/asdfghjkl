package com.carel.supervisor.presentation.switchtech;

import com.carel.supervisor.dataaccess.db.Record;

import java.sql.Timestamp;


public class SwitchVar
{
    private static final String ID_SITE = "idsite";
    private static final String ID_SWITCH = "idswitch";
    private static final String ID_DEV_MDL = "iddevmdl";
    private static final String ID_VAR_MDL = "idvarmdl";
    private static final String IS_ALARM = "isalarm";
    private static final String EEV_VALUE = "eev";
    private static final String TEV_VALUE = "tev";
    private static final String LAST_UPDATE = "lastupdate";
    private static final String DESCRIPTION = "description";
    private Integer idsite = null;
    private Integer idswitch = null;
    private Integer iddevmdl = null;
    private Integer idvarmdl = null;
    private String isalarm = null;
    private Float eev = null;
    private Float tev = null;
    private Timestamp lastupdate = null;
    private String description = null;

    public SwitchVar(Record r)
    {
        idsite = (Integer) r.get(ID_SITE);
        idswitch = (Integer) r.get(ID_SWITCH);
        iddevmdl = (Integer) r.get(ID_DEV_MDL);
        idvarmdl = (Integer) r.get(ID_VAR_MDL);
        isalarm = r.get(IS_ALARM).toString().trim();
        eev = (Float) r.get(EEV_VALUE);
        tev = (Float) r.get(TEV_VALUE);
        lastupdate = (Timestamp) r.get(LAST_UPDATE);
        description = r.get(DESCRIPTION).toString().trim();
    }

    public Float getEev()
    {
        return eev;
    }

    public void setEev(Float eev)
    {
        this.eev = eev;
    }

    public Integer getIddevmdl()
    {
        return iddevmdl;
    }

    public void setIddevmdl(Integer iddevmdl)
    {
        this.iddevmdl = iddevmdl;
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

    public Integer getIdvarmdl()
    {
        return idvarmdl;
    }

    public void setIdvarmdl(Integer idvarmdl)
    {
        this.idvarmdl = idvarmdl;
    }

    public String getIsalarm()
    {
        return isalarm;
    }

    public void setIsalarm(String isalarm)
    {
        this.isalarm = isalarm;
    }

    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    public void setLastupdate(Timestamp lastupdate)
    {
        this.lastupdate = lastupdate;
    }

    public Float getTev()
    {
        return tev;
    }

    public void setTev(Float tev)
    {
        this.tev = tev;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
