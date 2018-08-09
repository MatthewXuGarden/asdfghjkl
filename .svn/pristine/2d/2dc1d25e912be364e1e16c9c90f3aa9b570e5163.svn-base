package com.carel.supervisor.dataaccess.db.connection.impl;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.dataaccess.db.connection.*;
import java.util.*;


public abstract class ConnectionBase extends InitializableBase
    implements IConnection
{
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private String user = null;
    private String password = null;

    public ConnectionBase(Properties properties)
        throws InvalidConfigurationException
    {
        user = retrieveAttribute(properties, USER, "DTCE0008");
        password = retrieveAttribute(properties, PASSWORD, "DTCE0008");
    }

    protected String getPassword()
    {
        return password;
    }

    protected String getUser()
    {
        return user;
    }

    public void closeConnection(java.sql.Connection connection)
        throws Exception
    {
        connection.close();
    }
}
