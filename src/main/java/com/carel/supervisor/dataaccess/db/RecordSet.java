package com.carel.supervisor.dataaccess.db;

import java.sql.*;
import java.util.*;


public class RecordSet
{
    private List rows = new ArrayList();
    private Map columnNames = new HashMap();
    private List columnTypes = new ArrayList();

    public RecordSet(ResultSet resultSet) throws Exception
    {
        if (null == resultSet)
        {
            throw new NoSuchElementException("ResultSet");
        }

        ResultSetMetaData meta = resultSet.getMetaData();

        for (int i = 1; i <= meta.getColumnCount(); i++)
        {
            columnNames.put(meta.getColumnName(i), new Integer(i));
            columnTypes.add(new Integer(meta.getColumnType(i)));
        }

        Record record = null;

        while (resultSet.next())
        {
            record = new Record(resultSet,
                    DatabaseMgr.getInstance().getTypeMgr(), columnNames,
                    columnTypes);
            rows.add(record);
        }

        resultSet.close();
    }

    //metodo utilizzato per paginazione    Luca
    public RecordSet(ResultSet resultSet, int numStart, int numEnd)
        throws Exception
    {
        if (null == resultSet)
        {
            throw new NoSuchElementException("ResultSet");
        }

        ResultSetMetaData meta = resultSet.getMetaData();

        for (int i = 1; i <= meta.getColumnCount(); i++)
        {
            columnNames.put(meta.getColumnName(i), new Integer(i));
            columnTypes.add(new Integer(meta.getColumnType(i)));
        }

        Record record = null;

        for (int i = 0; i < numStart; i++)
        {
            resultSet.next();
        }

        int cont = 0;

        while (resultSet.next() && (cont < (numEnd - numStart)))
        {
            record = new Record(resultSet,
                    DatabaseMgr.getInstance().getTypeMgr(), columnNames,
                    columnTypes);
            rows.add(record);
            cont++;
        }

        resultSet.close();
    }

    public int size()
    {
        return rows.size();
    }

    public Record get(int pos)
    {
        return (Record) rows.get(pos);
    }

    public String[] getColumnNames()
    {
        int size = columnNames.size();
        String[] columns = new String[size];
        Iterator iterator = columnNames.keySet().iterator();
        String key = null;

        while (iterator.hasNext())
        {
            key = (String) iterator.next();
            columns[((Integer) columnNames.get(key)).intValue() - 1] = key;
        }

        //    for (int i = 1; i < iSize + 1; i++)
        //    sColumns[i] = (String)m_oColumnNames.get(new Integer(i));
        return columns;
    }
}
