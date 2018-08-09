package com.carel.supervisor.controller;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import java.sql.Timestamp;


public class LiveStatus
{
    private LiveStatus()
    {
    }

    public static void update()
    {
        try
        {
            String sql = "update livestatus set lastupdate=? where id=1";
            DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{new Timestamp(System.currentTimeMillis())});
        }
        catch (DataBaseException e)
        {
            Logger logger = LoggerMgr.getLogger(LiveStatus.class);
            logger.error(e);
        }
    }

    public static Timestamp retrieve() throws Exception
    {
        try
        {
            String sql = "select lastupdate from livestatus where id=1";
            RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null,
                    sql, null);

            if (null == recordset)
            {
            	sql = "insert into livestatus values (1, current_timestamp);";
                DatabaseMgr.getInstance().executeStatement(null, sql, null);
                return null;
            }
            else
            {
                if (0 == recordset.size())
                {
                	sql = "insert into livestatus values (1, current_timestamp);";
                    DatabaseMgr.getInstance().executeStatement(null, sql, null);
                	return null;
                }
            	Record record = recordset.get(0);

                return (Timestamp) record.get(0);
            }
        }
        catch (DataBaseException e)
        {
            Logger logger = LoggerMgr.getLogger(LiveStatus.class);
            logger.error(e);

            return null;
        }
    }
}
