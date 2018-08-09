package com.carel.supervisor.dataaccess.db.types.dbregistry;

import com.carel.supervisor.dataaccess.db.types.*;
import java.sql.*;


public class DBBoolean extends DBTypeBase implements IDBType
{

    public void valorize(PreparedStatement preparedStatement, int pos,
        Object value) throws Exception
    {
    	if (null == value)
    	{
    		preparedStatement.setNull(pos, Types.BOOLEAN);
    	}
    	else
    	{
    		preparedStatement.setBoolean(pos, ((Boolean) value).booleanValue());
    	}
    }

    public Object retrieve(ResultSet resultSet, int pos)
        throws Exception
    {
        return new Boolean(resultSet.getBoolean(pos));
    }
}
