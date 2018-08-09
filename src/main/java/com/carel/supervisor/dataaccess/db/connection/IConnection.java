package com.carel.supervisor.dataaccess.db.connection;


/**
 * @author Loris D'Acunto
 * Carel S.p.A.
 *
 * 2-nov-2005 11.45.36
 *
 * Public interface for database connection factory
 */
public interface IConnection
{
    /**
     * @return java.sql.Connection
     * @throws Exception
     *
     * Method that allow the client to create a connection to the database
     */
    public abstract java.sql.Connection createConnection()
        throws Exception;

    public abstract void closeConnection(java.sql.Connection connection)
        throws Exception;
    
    public abstract void resetConnections() throws Exception;
}
