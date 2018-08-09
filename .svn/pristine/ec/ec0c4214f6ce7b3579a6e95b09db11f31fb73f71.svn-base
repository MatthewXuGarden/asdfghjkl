package com.carel.supervisor.dataaccess.db.types.dbregistry;

import com.carel.supervisor.dataaccess.db.types.*;
import java.sql.*;


public class DBNull extends DBTypeBase implements IDBType
{
    
    public void valorize(PreparedStatement preparedStatement, int pos,
        Object value) throws Exception
    {
        preparedStatement.setObject(pos, null);
    }

    public Object retrieve(ResultSet resultSet, int pos)
        throws Exception
    {
        return resultSet.getObject(pos);
    }
}
