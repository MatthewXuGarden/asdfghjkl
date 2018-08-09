package com.carel.supervisor.dispatcher.bean;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;
import java.sql.Timestamp;


public class HSActionQBean
{
    private final static String HQ_1 = "idhsactionqueue";
    private final static String HQ_2 = "pvcode";
    private final static String HQ_3 = "idsite";
    private final static String HQ_4 = "priority";
    private final static String HQ_5 = "retrynum";
    private final static String HQ_6 = "retryafter";
    private final static String HQ_7 = "inserttime";
    private final static String HQ_8 = "lastupdate";
    private final static String HQ_9 = "timetogo";
    private final static String HQ_10 = "channel";
    private final static String HQ_11 = "actiontype";
    private final static String HQ_12 = "status";
    private final static String HQ_13 = "pathfile";
    private final static String HQ_14 = "freetext";
    private final static String HQ_15 = "receiversid";
    private final static String HQ_16 = "actionsid";
    private int key = 0;
    private String pvcode = "";
    private int site = 0;
    private int priority = 0;
    private int retrynum = 0;
    private int retryafter = 0;
    private Timestamp insertime = null;
    private Timestamp lastupdate = null;
    private Timestamp timetogo = null;
    private String channel = "";
    private String type = "";
    private int status = 0;
    private String path = "";
    private String message = "";
    private String receivers = "";
    private String actionsid = "";

    public HSActionQBean(Record r)
    {
        this.key = ((Integer) r.get(HQ_1)).intValue();
        this.pvcode = UtilBean.trim(r.get(HQ_2));
        this.site = ((Integer) r.get(HQ_3)).intValue();
        this.priority = ((Integer) r.get(HQ_4)).intValue();
        this.retrynum = ((Integer) r.get(HQ_5)).intValue();
        this.retryafter = ((Integer) r.get(HQ_6)).intValue();
        this.insertime = (Timestamp) r.get(HQ_7);
        this.lastupdate = (Timestamp) r.get(HQ_8);
        this.timetogo = (Timestamp) r.get(HQ_9);
        this.channel = UtilBean.trim(r.get(HQ_10));
        this.type = UtilBean.trim(r.get(HQ_11));
        this.status = ((Integer) r.get(HQ_12)).intValue();
        ;
        this.path = UtilBean.trim(r.get(HQ_13));
        this.message = UtilBean.trim(r.get(HQ_14));
        this.receivers = UtilBean.trim(r.get(HQ_15));
        this.actionsid = UtilBean.trim(r.get(HQ_16));
    }

    public HSActionQBean(int key, String pvcode, int site, int priority, int retrynum,
        int retryafter, String channel, String type, int status, String path, String message,
        String receivers, String actionsid)
    {
        this.key = key;
        this.pvcode = pvcode;
        this.site = site;
        this.priority = priority;
        this.retrynum = retrynum;
        this.retryafter = retryafter;
        this.channel = channel;
        this.type = type;
        this.status = status;
        this.path = path;
        this.message = message;
        this.receivers = receivers;
        this.actionsid = actionsid;
    }

    public String getActionsid()
    {
        return actionsid;
    }

    public String getChannel()
    {
        return channel;
    }

    public Timestamp getInsertime()
    {
        return insertime;
    }

    public int getKey()
    {
        return key;
    }

    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    public String getMessage()
    {
        return message;
    }

    public String getPath()
    {
        return path;
    }

    public int getPriority()
    {
        return priority;
    }

    public String getPvcode()
    {
        return pvcode;
    }

    public String getReceivers()
    {
        return receivers;
    }

    public int getRetryafter()
    {
        return retryafter;
    }

    public int getRetrynum()
    {
        return retrynum;
    }

    public int getSite()
    {
        return site;
    }

    public int getStatus()
    {
        return status;
    }

    public Timestamp getTimetogo()
    {
        return timetogo;
    }

    public String getType()
    {
        return type;
    }
}
