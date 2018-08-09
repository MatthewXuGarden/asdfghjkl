package com.carel.supervisor.dataaccess.db.types.dbregistry;

import com.carel.supervisor.dataaccess.db.types.*;
import java.sql.*;


public class DBTimeStamp extends DBTypeBase implements IDBType
{

    public void valorize(PreparedStatement preparedStatement, int pos,
        Object value) throws Exception
    {
        //preparedStatement.setTimestamp(pos, (java.sql.Timestamp)value);
    	if (null == value)
    	{
    		preparedStatement.setObject(pos,null);
    	}
    	else
    	{
    		preparedStatement.setTimestamp(pos,new java.sql.Timestamp(((java.util.Date) value).getTime()));
    	}
    }

    public Object retrieve(ResultSet resultSet, int pos)
        throws Exception
    {
        return resultSet.getTimestamp(pos);
    }
}
