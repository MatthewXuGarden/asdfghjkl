package com.carel.supervisor.presentation.switchtech;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class SwitchConfigList
{
    List switch_list = new ArrayList();
    Map switch_map = new HashMap();

    public SwitchConfigList(int idsite) throws DataBaseException
    {
        String sql = "select * from switchconfig where idsite=? order by idswitch";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });

        Record tmp = null;
        SwitchConfig tmp_switch = null;

        for (int i = 0; i < rs.size(); i++)
        {
            tmp = rs.get(i);
            tmp_switch = new SwitchConfig(tmp);
            switch_list.add(tmp_switch);
            switch_map.put(tmp_switch.getIdswitch(), tmp_switch);
            
        }
    }

    public int size()
    {
        return switch_list.size();
    }

    public SwitchConfig getSwitchById(int id)
    {
        return (SwitchConfig) switch_map.get(new Integer(id));
    }

    public SwitchConfig getSwitch(int position)
    {
        return (SwitchConfig) switch_list.get(position);
    }

    public static boolean existSwitch(int idsite, int id_switch)
        throws DataBaseException
    {
        String sql = "select count(1) from switchconfig where idsite=? and idswitch=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(id_switch) });

        int num = ((Integer) rs.get(0).get("count")).intValue();

        if (num == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static void insertDummySwitchConf(int idsite, int id_switch,
        String description) throws DataBaseException
    {
        String sql = "insert into switchconfig values (?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] param = new Object[12];
        param[0] = new Integer(idsite);
        param[1] = new Integer(id_switch);
        param[2] = "FALSE"; //autoswitch
        param[3] = new Integer(3600); //hour
        param[4] = null; //starthour
        param[5] = "eev"; //starttype
        param[6] = "tev"; //manualtype
        param[7] = "TRUE"; //autoresume
        param[8] = "FALSE"; //skipalarm
        param[9] = new Integer(1200); //alarmwait
        param[10] = description;
        param[11] = new Timestamp(System.currentTimeMillis());
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }

    public static void updateDesc(int idsite, int id_switch, String description)
        throws DataBaseException
    {
        String sql = "update switchconfig set description=?,lastupdate=? where idsite=? and idswitch=?";
        Object[] param = new Object[4];
        param[0] = description;
        param[1] = new Timestamp(System.currentTimeMillis());
        param[2] = new Integer(idsite);
        param[3] = new Integer(id_switch);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }

    public static SwitchConfig retrieveById(int idsite, int idswitch)
        throws DataBaseException
    {
        String sql = "select * from switchconfig where idsite=? and idswitch=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(idswitch) });

        if (rs.size() > 0)
        {
            return new SwitchConfig(rs.get(0));
        }
        else
        {
            return null;
        }
    }

    public static void setManualSwitch(int idsite, int id_switch,
        String manualtype, String autoresume, String skipalarm, int alarmwait)
        throws DataBaseException
    {
        String update = "update switchconfig set autoswitch='FALSE', manualtype=?, autoresume=?,skipalarm=?,alarmwait=?,lastupdate=? where idsite=? and idswitch=?";
        Object[] param = new Object[7];
        param[0] = manualtype;
        param[1] = autoresume;
        param[2] = skipalarm;
        param[3] = new Integer(alarmwait);
        param[4] = new Timestamp(System.currentTimeMillis());
        param[5] = new Integer(idsite);
        param[6] = new Integer(id_switch);
        DatabaseMgr.getInstance().executeStatement(null, update, param);
        
        update = "update switchstatus set type=?,lastupdate=? where idsite=? and idswitch=?";
        param = new Object[4];
        param[0] = manualtype;
        param[1] = new Timestamp(System.currentTimeMillis());
        param[2] = new Integer(idsite);
        param[3] = new Integer(id_switch);
        DatabaseMgr.getInstance().executeStatement(null, update, param);
    }

    public static void setAutoSwitch(int idsite, int id_switch, int hour,
        Timestamp starthour, int alarmwait, String starttype,
        String autoresume, String skipalarm) throws DataBaseException
    {
        String update = "update switchconfig set autoswitch='TRUE', hour=?, starthour=?,alarmwait=?,starttype=?,autoresume=?,skipalarm=?,manualtype=?,lastupdate=? where idsite=? and idswitch=?";
        Object[] param = new Object[10];
        param[0] = new Integer(hour);
        param[1] = starthour;
        param[2] = new Integer(alarmwait);
        param[3] = starttype;
        param[4] = autoresume;
        param[5] = skipalarm;
        param[6] = (starttype.equalsIgnoreCase("eev") ? "tev" : "eev");
        param[7] = new Timestamp(System.currentTimeMillis());
        param[8] = new Integer(idsite);
        param[9] = new Integer(id_switch);

        DatabaseMgr.getInstance().executeStatement(null, update, param);

        update = "update switchstatus set type=?,lastswitch=?,nextswitch=?,lastupdate=? where idsite=? and idswitch=?";
        param = new Object[6];
        param[0] = (starttype.equalsIgnoreCase("eev") ? "tev" : "eev");
        param[1] = null;
        param[2] = starthour;
        param[3] = new Timestamp(System.currentTimeMillis());
        param[4] = new Integer(idsite);
        param[5] = new Integer(id_switch);
        DatabaseMgr.getInstance().executeStatement(null, update, param);
    }

    public static int getNumberOfDeviceForSwitch(int idsite, int id_switch)
        throws DataBaseException
    {
        String sql = "select distinct iddevice from switchdev where idsite=? and idswitch=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(id_switch) });

        return rs.size();
    }

    public static void removeSwitchConfig(int idsite, int idswitch)
        throws DataBaseException
    {
        String sql = "delete from switchconfig where idsite=? and idswitch=?";
        DatabaseMgr.getInstance().executeStatement(null, sql,
            new Object[] { new Integer(idsite), new Integer(idswitch) });
    }

    public static Timestamp getStartTimestamp(int hour)
    {
        Timestamp t = null;
        int now_hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        Calendar cal = Calendar.getInstance();

        if (hour > now_hour)
        {
            //ora oggi
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            t = new Timestamp(cal.getTimeInMillis());
        }
        else
        {
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.DAY_OF_MONTH, (cal.get(Calendar.DAY_OF_MONTH) + 1));
            Calendar.getInstance().set(Calendar.DAY_OF_MONTH,
                (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1));
            t = new Timestamp(cal.getTimeInMillis());
        }

        return t;
    }
}
