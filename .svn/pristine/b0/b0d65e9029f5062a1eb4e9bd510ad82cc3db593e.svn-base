package com.carel.supervisor.dataaccess.datalog.impl;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class TableExtBeanList
{
    private List tablesExt = new ArrayList();

    public void retrieve(String dbId, int idsite, String lang, String tablename)
        throws Exception
    {
        Object[] values = new Object[3];
        values[0] = new Integer(idsite);
        values[1] = lang;
        values[2] = tablename;

        String query = "select * from cftableext where idsite = ? and languagecode = ? and tablename = ? order by id";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId,
                query, values);
        TableExtBean tableext = null;

        for (int i = 0; i < recordSet.size(); i++)
        {
            tableext = new TableExtBean(recordSet.get(i));
            addTableExtBean(tableext);
        }
    }

    public void addTableExtBean(TableExtBean tableext)
    {
        tablesExt.add(tableext);
    }

    public TableExtBean getTableExt(int pos)
    {
        return (TableExtBean) tablesExt.get(pos);
    }

    public int size()
    {
        return tablesExt.size();
    }

}
