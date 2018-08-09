package com.carel.supervisor.dataaccess.db;

import com.carel.supervisor.dataaccess.db.types.*;
import java.sql.*;
import java.util.*;


/**
 * @author lorisdacunto
 *
 */
public class Record
{
    private ArrayList<Object> record = new ArrayList<Object>();
    private Map<String, Object> columnNames = null;

    protected Record(ResultSet resultSet, TypeMgr typeMgr, Map<String, Object> columnNames,
        List<Object> columnTypes) throws Exception
    {
        for (int i = 1; i <= columnNames.size(); i++)
        {
            record.add(typeMgr.getByType(
                    ((Integer) columnTypes.get(i - 1)).intValue()).retrieve(resultSet,
                    i));
        }

        this.columnNames = columnNames;
    }

    public Object get(int pos)
    {
        return record.get(pos);
    }

    public Object get(String columnName)
    {
        return record.get(((Integer) columnNames.get(columnName)).intValue() -
            1);
    }

    public boolean hasColumn(String columnName)
    {
        return columnNames.containsKey(columnName);
    }
}
