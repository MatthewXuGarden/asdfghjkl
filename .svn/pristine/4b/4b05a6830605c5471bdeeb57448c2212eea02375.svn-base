package com.carel.supervisor.dataaccess.db.connection.impl;

import java.sql.DriverManager;
import java.util.Properties;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.dataaccess.db.connection.IConnection;

public class PvPoolConnection extends ConnectionBase implements IConnection
{
	private static final String POOL_NAME = "pvpro";
	
	private static final String DRIVER = "driver";
    private static final String URL = "url";
    private static final String NUM_CONNECTIONS = "numconnections";
    private static final String MAX_CONNECTIONS = "maxconnections";
    private static final String MIN_CONNECTIONS = "minconnections";
    
    private String url = null;
    private int num = 0;
    private int max = 0;
    private int min = 0;
        
    DriverManagerConnectionFactory connFactory = null;
    PoolableConnectionFactory poolConnFactory = null;
    GenericObjectPool connPool = null;
    PoolingDriver pDriver = null;
        
	public PvPoolConnection(Properties properties)
		throws InvalidConfigurationException
	{
		
		super(properties);
		url = retrieveAttribute(properties, URL, "DTCE0008");

		String driver = retrieveAttribute(properties,DRIVER,"DTCE0008");
		String numConn = retrieveAttribute(properties,NUM_CONNECTIONS,"DTCE0008");
		String maxConn = retrieveAttribute(properties,MAX_CONNECTIONS,"DTCE0008");
		String minConn = retrieveAttribute(properties,MIN_CONNECTIONS,"DTCE0008");

		try
		{
			num = Integer.parseInt(numConn);
		}
		catch (Exception e) {
			e.printStackTrace();
			FatalHandler.manage(this,CoreMessages.format("DTCE0008","DTCE0008",numConn),e);
		}
		
		try
		{
			max= Integer.parseInt(maxConn);
		}
		catch (Exception e) {
			e.printStackTrace();
			FatalHandler.manage(this,CoreMessages.format("DTCE0008","DTCE0008",numConn),e);
		}
		
		try
		{
			min = Integer.parseInt(minConn);
		}
		catch (Exception e) {
			e.printStackTrace();
			FatalHandler.manage(this,CoreMessages.format("DTCE0008","DTCE0008",numConn),e);
		}

		try
		{
			try 
		    {
		    	Class.forName(driver);
		    }
		    catch (Exception e) {
		    	e.printStackTrace();
		    }

		    connPool = new GenericObjectPool(null,num);
		    connPool.setMaxActive(num);
		    connPool.setMaxIdle(max);
		    connPool.setMinIdle(min);
		    
		    connFactory = new DriverManagerConnectionFactory(url,super.getUser(),super.getPassword());
		    
		    poolConnFactory = new PoolableConnectionFactory(connFactory,connPool,null,null,false,true);
		        
		    pDriver = new PoolingDriver();
		    pDriver.registerPool(POOL_NAME,connPool);
		}
		catch (Exception e) {
			e.printStackTrace();
			FatalHandler.manage(this,CoreMessages.format("DTCE0012","DRIVER",driver),e);
		}
	}
	
	public java.sql.Connection createConnection() throws Exception
	{
		java.sql.Connection connection = null;
		connection = DriverManager.getConnection("jdbc:apache:commons:dbcp:"+POOL_NAME);
		connection.setAutoCommit(false);
		return connection;
	}
	    
	public void resetConnections() throws Exception
	{
		
	}
}