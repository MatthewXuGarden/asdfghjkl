package com.carel.supervisor.dataaccess.db.types.dbregistry;

import com.carel.supervisor.dataaccess.db.types.*;
import java.sql.*;


public class DBDate extends DBTypeBase implements IDBType
{

    public void valorize(PreparedStatement preparedStatement, int pos,
        Object value) throws Exception
    {
        if (null == value)
        {
        	preparedStatement.setObject(pos, null);
        }
        else
        {
	    	java.sql.Date sqlDate = new java.sql.Date(((java.util.Date) value).getTime());
	        preparedStatement.setDate(pos, sqlDate);
        }
    }

    public Object retrieve(ResultSet resultSet, int pos)
        throws Exception
    {
        return resultSet.getDate(pos);
    }
}
