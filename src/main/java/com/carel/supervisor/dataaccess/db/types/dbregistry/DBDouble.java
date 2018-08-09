package com.carel.supervisor.dataaccess.db.types.dbregistry;

import com.carel.supervisor.dataaccess.db.types.*;
import java.sql.*;


public class DBDouble extends DBTypeBase implements IDBType
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
        	preparedStatement.setDouble(pos, ((Double) value).doubleValue());
        }
    }

    public Object retrieve(ResultSet resultSet, int pos)
        throws Exception
    {
        return new Double(resultSet.getDouble(pos));
    }
}
