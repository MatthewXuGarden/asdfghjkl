package com.carel.supervisor.dataaccess.db.types;

import java.sql.*;


public interface IDBType
{
    public abstract void valorize(PreparedStatement preparedStatement, int pos,
        Object value) throws Exception;

    public abstract Object retrieve(ResultSet resultSet, int pos)
        throws Exception;

    public abstract int getType(int pos);

    public abstract int numTypes();

    public abstract void addType(int type);
}
