package com.carel.supervisor.dataaccess.db.types.dbregistry;

import com.carel.supervisor.dataaccess.db.types.*;
import java.sql.*;


public class DBShort extends DBTypeBase implements IDBType
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
        	preparedStatement.setShort(pos, ((Short) value).shortValue());
        }
    }

    public Object retrieve(ResultSet resultSet, int pos)
        throws Exception
    {
        return new Short(resultSet.getShort(pos));
    }
}
