package com.carel.supervisor.director.maintenance;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import java.util.ArrayList;
import java.util.List;


public class HistorMaintenance
{
    public HistorMaintenance()
    {
    }

    public static List retrieve() throws Exception
    {
        String sql = "select * from maintenance";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql);
        Record record = null;
        HistorData histor = null;
        List lista = new ArrayList();

        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            histor = new HistorData(record);
            lista.add(histor);
        }

        return lista;
    }

    public static void refreshTimestamp(String tablename, String action)
        throws Exception
    {
        String sql = "update maintenance set lasttimestamp = current_timestamp where tablename = ? and action = ?";
        DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { tablename, action });
    }
}
