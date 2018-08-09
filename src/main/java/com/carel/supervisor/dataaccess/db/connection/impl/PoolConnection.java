package com.carel.supervisor.dataaccess.db.connection.impl;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.dataaccess.db.connection.*;
import java.sql.*;
import java.util.*;


public class PoolConnection extends ConnectionBase implements IConnection
{
    private static final String DRIVER = "driver";
    private static final String URL = "url";
    private static final String NUM_CONNECTIONS = "numconnections";
    private String url = null;
    private int num = 0;
    private List connections = new ArrayList();

    public PoolConnection(Properties properties)
        throws InvalidConfigurationException
    {
        super(properties);
        url = retrieveAttribute(properties, URL, "DTCE0008");

        String driver = retrieveAttribute(properties, DRIVER, "DTCE0008");
        String numConn = retrieveAttribute(properties, NUM_CONNECTIONS,
                "DTCE0008");

        try
        {
            num = Integer.parseInt(numConn);
        }
        catch (Exception e)
        {
            FatalHandler.manage(this,
                CoreMessages.format("DTCE0008", "DTCE0008", numConn), e);
        }

        try
        {
            DriverManager.registerDriver((Driver) Class.forName(driver, false,
                    this.getClass().getClassLoader()).newInstance());
        }
        catch (Exception e)
        {
            FatalHandler.manage(this,
                CoreMessages.format("DTCE0012", "DRIVER", driver), e);
        }

        try
        {
        	Connection connection = null;
            for (int i = 0; i < num; i++)
            {
            	connection = DriverManager.getConnection(url, super.getUser(), super.getPassword());
                connections.add(connection);
                connection.setAutoCommit(false);
            }
        }
        catch (Exception e)
        {
            FatalHandler.manage(this,
                CoreMessages.format("DTCE0009", "PoolConnection"), e);
        }
    }

    public java.sql.Connection createConnection() throws Exception
    {
        if (0 < connections.size())
        {
            Connection conn = (Connection)connections.remove(connections.size() - 1);
            //System.out.println("CONNESSIONE: " + String.valueOf(actual));
            return conn;
        }
        return null;
    }

    public void closeConnection(java.sql.Connection connection)
        throws Exception
    {
        if (connections.size() <= num)
        {
            connections.add(connection);
            //System.out.println("CONNESSIONE: " + String.valueOf(actual));
        }
    }
    
    public void resetConnections() throws Exception
    {
    	try
        {
        	Connection connection = null;
        	for (int i = 0; i < num; i++)
            {
        		((Connection)connections.get(i)).close();
            }
        	
        	connections.clear();
        	
            for (int i = 0; i < num; i++)
            {
            	connection = DriverManager.getConnection(url, super.getUser(), super.getPassword());
                connections.add(connection);
                connection.setAutoCommit(false);
            }
        }
        catch (Exception e)
        {
            FatalHandler.manage(this,
                CoreMessages.format("DTCE0009", "PoolConnection"), e);
        }
    }
}
