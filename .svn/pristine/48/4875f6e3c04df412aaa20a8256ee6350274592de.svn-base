package com.carel.supervisor.presentation.switchtech;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

import java.sql.Timestamp;


public class SwitchStatusList
{
    public static SwitchStatus retriveSwitchStatusById(int idsite, int id_switch)
        throws DataBaseException
    {
        String sql = "select * from switchstatus where idsite=? and idswitch=?";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(id_switch);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        SwitchStatus result = null;
        int size = rs.size();

        if (size > 0)
        {
            result = new SwitchStatus(rs.get(0));
        }

        return result;
    }

    public static SwitchStatus[] retriveAllSwitchStatus(int idsite)
        throws DataBaseException
    {
        String sql = "select * from switchstatus where idsite=? and idswitch=?";
        Object[] param = new Object[1];
        param[0] = new Integer(idsite);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        SwitchStatus[] result = null;
        int size = rs.size();

        if (size > 0)
        {
            result = new SwitchStatus[size];

            for (int i = 0; i < size; i++)
            {
                result[i] = new SwitchStatus(rs.get(i));
            }
        }

        return result;
    }

    public static void insertDummySwitchStatus(int idsite, int id_switch)
        throws DataBaseException
    {
        String sql = "insert into switchstatus values (?,?,?,?,?,?,?,?)";
        Object[] param = new Object[8];
        param[0] = new Integer(idsite);
        param[1] = new Integer(id_switch);
        param[2] = "FALSE";
        param[3] = "TRUE";
        param[4] = "tev";
        param[5] = null;
        param[6] = null;
        param[7] = new Timestamp(System.currentTimeMillis());
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }

    public static void removeSwitchStatus(int idsite, int idswitch)
        throws DataBaseException
    {
        String sql = "delete from switchstatus where idsite=? and idswitch=?";
        DatabaseMgr.getInstance().executeStatement(null, sql,
            new Object[] { new Integer(idsite), new Integer(idswitch) });
    }
}
