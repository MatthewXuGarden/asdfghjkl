package com.carel.supervisor.dataaccess.db.connection.impl;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.dataaccess.db.connection.*;
import java.util.*;
import javax.naming.*;
import javax.sql.*;


public class DataSourceConnection extends ConnectionBase implements IConnection
{
    private static final String DATASOURCE = "datasource";
    private DataSource dataSource = null;
    private String dataSourceName = null;

    public DataSourceConnection(Properties properties)
        throws InvalidConfigurationException
    {
        super(properties);
        dataSourceName = retrieveAttribute(properties, DATASOURCE, "DTCE0008");

        try
        {
            Context ctx = new InitialContext();
            Context ctx2 = (Context) ctx.lookup("java:/comp/env");
            dataSource = (DataSource) ctx2.lookup(dataSourceName);
        }
        catch (NamingException ex)
        {
            FatalHandler.manage(this,
                CoreMessages.format("DTCE0009",
                    new Object[] { "DATASOURCE", dataSourceName }));
        }

        //(DataSource)CacheMgr.getPointer(m_sDataSourceName,false);
        // if(null == m_oDataSource)
        //  throw new DataSourceNotFoundException(sDataSourceName);
    }

    public java.sql.Connection createConnection() throws Exception
    {
        java.sql.Connection connection = dataSource.getConnection(super.getUser(),
                super.getPassword());
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(1);

        return connection;
    }
    
    public void resetConnections() throws Exception
    {
    	//Do nothing
    }
}
