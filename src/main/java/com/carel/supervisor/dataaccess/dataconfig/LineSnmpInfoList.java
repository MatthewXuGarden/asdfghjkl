package com.carel.supervisor.dataaccess.dataconfig;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class LineSnmpInfoList extends LineInfoList
{
    public LineSnmpInfoList(String dbId, String plantId) throws DataBaseException
    {
    	String sql = "select * from cfline where typeprotocol = ? order by code";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql, new Object[]{"SNMP"});
        Record record = null;
        lineInfo = new LineInfo[recordSet.size()];

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            lineInfo[i] = new LineInfo(record);
            lineById.put(new Integer(lineInfo[i].getId()), lineInfo[i]);
        }
    }
}
