package com.carel.supervisor.director.maintenance;

import com.carel.supervisor.dataaccess.db.Record;
import java.sql.Timestamp;


public class HistorData
{
    private static final String TABLENAME = "tablename";
    private static final String ACTION = "action";
    private static final String LASTTIME = "lasttimestamp";
    private String tablename = "";
    private String action = "";
    private Timestamp lasttime = null;

    public HistorData(Record record)
    {
        tablename = (String) record.get(TABLENAME);
        action = (String) record.get(ACTION);
        lasttime = (Timestamp) record.get(LASTTIME);
    }

    /**
     * @return Returns the action.
     */
    public String getAction()
    {
        return action;
    }

    /**
     * @param action The action to set.
     */
    public void setAction(String action)
    {
        this.action = action;
    }

    /**
     * @return Returns the lasttime.
     */
    public Timestamp getLasttime()
    {
        return lasttime;
    }

    /**
     * @param lasttime The lasttime to set.
     */
    public void setLasttime(Timestamp lasttime)
    {
        this.lasttime = lasttime;
    }

    /**
     * @return Returns the tablename.
     */
    public String getTablename()
    {
        return tablename;
    }

    /**
     * @param tablename The tablename to set.
     */
    public void setTablename(String tablename)
    {
        this.tablename = tablename;
    }
}
