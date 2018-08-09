package com.carel.supervisor.dataaccess.db.types.dbregistry;

import com.carel.supervisor.dataaccess.db.types.*;
import java.sql.*;


public class DBVarchar extends DBTypeBase implements IDBType
{

    public void valorize(PreparedStatement preparedStatement, int pos,
        Object value) throws Exception
    {
        preparedStatement.setString(pos, (String)value);
    }

    public Object retrieve(ResultSet resultSet, int pos)
        throws Exception
    {
        return resultSet.getString(pos);
    }
}
