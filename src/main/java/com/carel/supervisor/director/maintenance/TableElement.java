package com.carel.supervisor.director.maintenance;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import java.sql.Timestamp;


public class TableElement implements IElement
{
    private static String NAME = "name";
    private static String ACTION = "action";
    private static String TIME = "time";
    private static String COLUMN = "column";
    private static String DELETE = "delete";
    private static String DELETENUM = "deletenum";
    private static String REINDEX = "reindex";
    private static String FREQUENCY = "frequency";
    private static String VACUUM = "vacuum";
    private String name = "";
    private String action = "";
    private Integer innertime = null;
    private String column = "";
    private Integer frequency = null;

    public TableElement(XMLNode xmlNode)
    {
        name = xmlNode.getAttribute(NAME);
        action = xmlNode.getAttribute(ACTION);

        String timeTmp = xmlNode.getAttribute(TIME);

        try
        {
            if (null != timeTmp)
            {
                innertime = Integer.valueOf(timeTmp);
            }
        }
        catch (Exception e)
        {
            LoggerMgr.getLogger(this.getClass()).error(e);
            innertime = new Integer(60);
        }

        String thresholdTmp = xmlNode.getAttribute(FREQUENCY);

        try
        {
            frequency = Integer.valueOf(thresholdTmp);
        }
        catch (Exception e)
        {
            LoggerMgr.getLogger(this.getClass()).error(e);
            innertime = new Integer(60);
        }

        column = xmlNode.getAttribute(COLUMN, "");
    }

    //Fare il controllo se serve e se attività manuale
    public boolean activate(Timestamp time) throws Exception
    {
        if (time.getTime() == 0)
        {
            String sql = "insert into maintenance values (?,?,current_timestamp)";
            DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { name, action });
			HistorMaintenance.refreshTimestamp(name, action);
            //Prima volta, inserisco i dati nel DB
			return true;
        }
        else
        {
            //se ultimo time + frequenza < adesso allora mi attivo
            if ((time.getTime() + ((long) frequency.intValue() * 86400000L)) < System.currentTimeMillis())
            {
                if (DELETE.equalsIgnoreCase(action))
                {
                    String sql = "delete from " + name + " where " + column + "< ?";
                    Timestamp timeSql = new Timestamp(System.currentTimeMillis() -
                            ((long) innertime.intValue() * 86400000L));
                    DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { timeSql });
                    LoggerMgr.getLogger(this.getClass()).info("Svecchiata tabella " + name);
                }
                else if (DELETENUM.equalsIgnoreCase(action))
                {
                	if (innertime.intValue() == 60)
                	{
                		innertime = new Integer(100000);
                	}
                	String sql = "delete from " + name + " where " + column +" in (select " + column +" from " + name + 
   	             				 " order by " + column +" desc offset + " + innertime.intValue() +" )";
			       DatabaseMgr.getInstance().executeStatement(null, sql, null);
			       LoggerMgr.getLogger(this.getClass()).info("Svecchiata tabella " + name);
                }
                else if (REINDEX.equalsIgnoreCase(action))
                {
                    String sql = "reindex table " + name;
                    DatabaseMgr.getInstance().executeStatement(null, sql, null);
                    LoggerMgr.getLogger(this.getClass()).info("Reindex tabella " + name);
                }
                /*
                else if (VACUUM.equalsIgnoreCase(action))
                {
                    EventMgr.getInstance().warning(new Integer(1), "System", "Action", "M001", null);
                }
				*/
                HistorMaintenance.refreshTimestamp(name, action);

                return true;
            }
        }

        return false;
    }

    public Integer frequency()
    {
        return frequency;
    }

    public String getName()
    {
        return name;
    }

    public String getAction()
    {
        return action;
    }
}
