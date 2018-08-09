package com.carel.supervisor.dataaccess.db;

import com.carel.supervisor.dataaccess.monitor.ICounter;
import com.carel.supervisor.dataaccess.monitor.StatementMgr;
import java.sql.*;


public class SeqCommand implements IDBCommand
{
    private String table = null;
    private String column = null;
    private int buffer = 0;

    public SeqCommand(String table, String column, int buffer)
    {
        this.table = table;
        this.column = column;
        this.buffer = buffer;
    }

    public RecordSet execute(Connection connection) throws Exception
    {
        String sql = null;
        PreparedStatement preparedStatement = null;

        try
        {
            sql = "update sykeys set value = value + " +
                String.valueOf(buffer) +
                " where tablename = ? and colname = ?";

            ICounter c = StatementMgr.getInstance().retrieve("update sykeys...");
            c.start();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, table);
            preparedStatement.setString(2, column);
            preparedStatement.execute();
            c.stop();
        }
        catch (SQLException e)
        {
            //Entry table doesn't exist
            insert(connection);
        }

        RecordSet recordset = select(connection);

        if (0 == recordset.size())
        {
            //Entry table doesn't exist
            insert(connection);
            recordset = select(connection);
        }

        return recordset;
    }

    private void insert(Connection connection) throws Exception
    {
        String sql = "insert into sykeys values (?,?,?,?)";
        ICounter c = StatementMgr.getInstance().retrieve(sql);
        c.start();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, table);
        preparedStatement.setString(2, column);
        preparedStatement.setInt(3, buffer);
        preparedStatement.setTimestamp(4,
            new Timestamp(System.currentTimeMillis()));
        preparedStatement.execute();
        c.stop();
    }

    private RecordSet select(Connection connection) throws Exception
    {
        String sql = "select value from sykeys where tablename = ? and colname = ?";
        ICounter c = StatementMgr.getInstance().retrieve(sql);
        c.start();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, table);
        preparedStatement.setString(2, column);

        RecordSet r = new RecordSet(preparedStatement.executeQuery());
        c.stop();

        return r;
    }

    public String name()
    {
        return "SeqCommand";
    }
}
