package com.carel.supervisor.dataaccess.db.types.dbregistry;

import com.carel.supervisor.dataaccess.db.types.*;
import java.sql.*;


public class DBInteger extends DBTypeBase implements IDBType
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
        	preparedStatement.setInt(pos, ((Integer) value).intValue());
        }
    }

    public Object retrieve(ResultSet resultSet, int pos)
        throws Exception
    {
        return new Integer(resultSet.getInt(pos));
    }
}
