package com.carel.supervisor.dataaccess.db.connection.impl;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.dataaccess.db.connection.*;
import java.sql.*;
import java.util.*;


public class DirectConnection extends ConnectionBase implements IConnection
{
    private static final String DRIVER = "driver";
    private static final String URL = "url";
    private String url = null;

    public DirectConnection(Properties properties)
        throws InvalidConfigurationException
    {
        super(properties);
        url = retrieveAttribute(properties, URL, "DTCE0008");

        String driver = retrieveAttribute(properties, DRIVER, "DTCE0008");

        try
        {
            DriverManager.registerDriver((Driver) Class.forName(driver, false,
                    this.getClass().getClassLoader()).newInstance());
        }
        catch (Exception e)
        {
            FatalHandler.manage(this,
                CoreMessages.format("DTCE0009",
                    new Object[] { "DRIVER", driver }), e);
        }
    }

    public java.sql.Connection createConnection() throws Exception
    {
        java.sql.Connection connection = DriverManager.getConnection(url,
                super.getUser(), super.getPassword());
        connection.setAutoCommit(false);

        return connection;
    }
    
    public void resetConnections() throws Exception
    {
    	//Do nothing
    }
}
