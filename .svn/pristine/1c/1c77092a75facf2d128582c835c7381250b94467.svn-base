package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.dataaccess.db.*;
import com.carel.supervisor.dataaccess.monitor.ICounter;
import com.carel.supervisor.dataaccess.monitor.StatementMgr;
import java.sql.*;


public class AllarmFinished implements IDBCommand
{
    private String pvcode = null;
    private Integer idsite = null;
    private Integer idvariable = null;
    private Timestamp endtime = null;

    public AllarmFinished(String dbId, String pvcode, Integer idsite,
        Integer idvariable, Timestamp endtime)
    {
        this.pvcode = pvcode;
        this.idsite = idsite;
        this.idvariable = idvariable;
        this.endtime = endtime;
    }

    public RecordSet execute(Connection connection) throws Exception
    {
        /*String sql = "update hsalarm set endtime = ? where idvariable = ? and endtime is null and ackuser is null";
        ICounter c = StatementMgr.getInstance().retrieve(sql);
        c.start();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setTimestamp(1, endtime);
        preparedStatement.setInt(2, idvariable.intValue());
        preparedStatement.execute();
        c.stop();
        return null;
        */
        String sql = "select max(idalarm) from hsalarm where pvcode = ? and idsite = ? and idvariable = ? and endtime is null and resetuser is null";
        ICounter c = StatementMgr.getInstance().retrieve(sql);
        c.start();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, pvcode);
        preparedStatement.setObject(2, idsite);
        preparedStatement.setObject(3, idvariable);

        ResultSet resultSet = preparedStatement.executeQuery();
        RecordSet recordset = new RecordSet(resultSet);
        c.stop();

        if (null != recordset.get(0))
        {
            if (null != recordset.get(0).get(0))
            {
                int idAlarm = ((Integer) recordset.get(0).get(0)).intValue();
                sql = "update hsalarm set endtime = ?, lastupdate = ? where idalarm = ? and idsite = ?";
                c = StatementMgr.getInstance().retrieve(sql);
                c.start();
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setTimestamp(1, endtime);
                preparedStatement.setTimestamp(2, endtime);
                preparedStatement.setInt(3, idAlarm);
                preparedStatement.setObject(4, idsite);
                preparedStatement.execute();
                c.stop();
                resultSet.close();

                return null;
            }
        }

        resultSet.close();
        throw new Exception("Alarm not found into database");
    }

    public String name()
    {
        return "AllarmFinished";
    }
}
