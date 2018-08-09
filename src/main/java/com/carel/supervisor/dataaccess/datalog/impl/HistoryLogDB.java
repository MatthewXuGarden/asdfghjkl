package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.dataaccess.datalog.*;
import com.carel.supervisor.dataaccess.db.*;


public class HistoryLogDB extends HistoryLogBase
{
    
    public void saveHistory(HistoryContext historyContext)
        throws Exception
    {
        Object[][] values = historyContext.getData();

        if ((null != values) && (0 < values.length))
        {
            String sInsert = "INSERT INTO hsvarphy VALUES (?,?,?,?,?)";
            DatabaseMgr.getInstance().executeMultiStatement(historyContext.getDbId(),
                sInsert, values);
        }
    }

    public void retrieveHistory(HistoryContext historyContext)
        throws Exception
    {
        //String query = "SELECT value FROM \"CAREL\".\"HSVARPHY\" WHERE idVarPhy = ? AND DATE(solarTime) = ?";
        //historyContext.setRecordSet(DatabaseMgr.getInstance().executeQuery(historyContext.getDbId(), query,
        //    new Object[] { idVarPhy, data }));
    }
}
