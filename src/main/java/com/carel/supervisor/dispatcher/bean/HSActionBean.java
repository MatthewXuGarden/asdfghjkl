package com.carel.supervisor.dispatcher.bean;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;
import java.sql.Timestamp;


public class HSActionBean
{
    private final static String HSA_0 = "idhsaction";
    private final static String HSA_1 = "pvcode";
    private final static String HSA_2 = "idsite";
    private final static String HSA_13 = "code";
    private final static String HSA_3 = "idaction";
    private final static String HSA_6 = "idvariable";
    private final static String HSA_14 = "isalarm";
    private final static String HSA_15 = "starttime";
    private final static String HSA_16 = "endtime";
    private final static String HSA_4 = "priority";
    private final static String HSA_5 = "status";
    private final static String HSA_7 = "inserttime";
    private final static String HSA_8 = "lastupdate";
    private final static String HSA_9 = "actioncode";
    private final static String HSA_10 = "actiontype";
    private final static String HSA_11 = "parameters";
    private final static String HSA_12 = "template";

    // HSACTION
    private int idhsaction = 0;
    private String pvcode = "";
    private int idsite = 0;
    private int idaction = -1;
    private int priority = 0;
    private int status = 0;
    private int idvariable = 0;
    private boolean isalarm = false;
    private Timestamp start = null;
    private Timestamp end = null;
    private Timestamp inserttime = null;
    private Timestamp lastupdate = null;
    private String code = "";

    // CFACTION
    private int actioncode = 0;
    private String actiontype = "";
    private String parameters = "";
    private String template = "";

    public HSActionBean(Record r) throws Exception
    {
        if (r == null)
        {
            throw new Exception("Record null");
        }

        try
        {
            this.idhsaction = ((Integer) r.get(HSA_0)).intValue();
            this.pvcode = UtilBean.trim(r.get(HSA_1));
            this.idsite = ((Integer) r.get(HSA_2)).intValue();
            ;
            this.idaction = ((Integer) r.get(HSA_3)).intValue();
            ;
            this.priority = ((Integer) r.get(HSA_4)).intValue();
            ;
            this.status = ((Integer) r.get(HSA_5)).intValue();
            ;
            this.idvariable = ((Integer) r.get(HSA_6)).intValue();
            ;
            this.inserttime = (Timestamp) r.get(HSA_7);
            this.lastupdate = (Timestamp) r.get(HSA_8);
            this.start = (Timestamp) r.get(HSA_15);
            this.end = (Timestamp) r.get(HSA_16);
            this.code = UtilBean.trim(r.get(HSA_13));
            this.actioncode = ((Integer) r.get(HSA_9)).intValue();
            this.actiontype = UtilBean.trim(r.get(HSA_10));
            this.parameters = UtilBean.trim(r.get(HSA_11));
            this.template = UtilBean.trim(r.get(HSA_12));
            this.isalarm = Boolean.parseBoolean(UtilBean.trim(r.get(HSA_14)));
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            throw new Exception(e);
        }
    }

    public String getCodeDesc()
    {
        return this.code;
    }

    public int getIdaction()
    {
        return idaction;
    }

    public void setIdaction(int idaction)
    {
        this.idaction = idaction;
    }

    public int getIdhsaction()
    {
        return idhsaction;
    }

    public void setIdhsaction(int idhsaction)
    {
        this.idhsaction = idhsaction;
    }

    public int getIdsite()
    {
        return idsite;
    }

    public void setIdsite(int idsite)
    {
        this.idsite = idsite;
    }

    public Timestamp getInserttime()
    {
        return inserttime;
    }

    public void setInserttime(Timestamp inserttime)
    {
        this.inserttime = inserttime;
    }

    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    public void setLastupdate(Timestamp lastupdate)
    {
        this.lastupdate = lastupdate;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public String getPvcode()
    {
        return pvcode;
    }

    public void setPvcode(String pvcode)
    {
        this.pvcode = pvcode;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getActioncode()
    {
        return actioncode;
    }

    public void setActioncode(int actioncode)
    {
        this.actioncode = actioncode;
    }

    public String getActiontype()
    {
        return actiontype;
    }

    public void setActiontype(String actiontype)
    {
        this.actiontype = actiontype;
    }

    public int getIdvariable()
    {
        return idvariable;
    }

    public void setIdvariable(int idvariable)
    {
        this.idvariable = idvariable;
    }

    public boolean isIsalarm()
    {
        return isalarm;
    }

    public void setIsalarm(boolean isalarm)
    {
        this.isalarm = isalarm;
    }

    public String getParameters()
    {
        return parameters;
    }

    public void setParameters(String parameters)
    {
        this.parameters = parameters;
    }

    public String getTemplate()
    {
        return template;
    }

    public void setTemplate(String template)
    {
        this.template = template;
    }

    public Timestamp getEnd()
    {
        return end;
    }

    public void setEnd(Timestamp end)
    {
        this.end = end;
    }

    public Timestamp getStart()
    {
        return start;
    }

    public void setStart(Timestamp start)
    {
        this.start = start;
    }
}
