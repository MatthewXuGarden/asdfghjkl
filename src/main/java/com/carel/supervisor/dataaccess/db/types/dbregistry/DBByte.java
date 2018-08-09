package com.carel.supervisor.dataaccess.db.types.dbregistry;

import com.carel.supervisor.dataaccess.db.types.*;
import java.sql.*;


public class DBByte extends DBTypeBase implements IDBType
{
    
    public void valorize(PreparedStatement preparedStatement, int pos,
        Object value) throws Exception
    {
        char[] c = new char[2];
        c[0] = (char) ((Byte) value).byteValue();
        c[1] = '\0';
        preparedStatement.setString(pos, new String(c));
    }

    public Object retrieve(ResultSet resultSet, int pos)
        throws Exception
    {
        return new Byte(resultSet.getByte(pos));
    }
}
