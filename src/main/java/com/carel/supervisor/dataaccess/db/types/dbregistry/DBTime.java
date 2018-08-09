package com.carel.supervisor.dataaccess.db.types.dbregistry;

import com.carel.supervisor.dataaccess.db.types.*;
import java.sql.*;


public class DBTime extends DBTypeBase implements IDBType
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
        	preparedStatement.setTime(pos, ((java.sql.Time) value));
        }
    }

    public Object retrieve(ResultSet resultSet, int pos)
        throws Exception
    {
        return resultSet.getTime(pos);
    }
}
