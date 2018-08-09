package com.carel.supervisor.presentation.switchtech;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;


public class SwitchDevList
{
    public static DeviceBean[] getDevicesOfSwitch(int idsite, int idswitch,
        String language) throws Exception
    {
        String sql = "select iddevice from switchdev where idsite=? and idswitch=? ";

        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idswitch);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        int[] ids_device = new int[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            ids_device[i] = ((Integer) rs.get(i).get(0)).intValue();
        }

        DeviceListBean devicelist = new DeviceListBean(idsite, language);
        DeviceBean[] result = new DeviceBean[devicelist.size()];
        int[] ids = devicelist.getIds();

        for (int i = 0; i < devicelist.size(); i++)
        {
            result[i] = devicelist.getDevice(ids[i]);
        }

        return result;
    }

    public static DeviceBean[] getDeviceOfSwitch(int idsite, int is_switch,
        String language) throws Exception
    {
        String sql = "select distinct iddevmdl from switchvar where idsite=? and idswitch=? ";

        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(is_switch);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        int[] ids_device = new int[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            ids_device[i] = ((Integer) rs.get(i).get(0)).intValue();
        }

        DeviceListBean devicelist = new DeviceListBean(idsite, language);
        List dev_list = new ArrayList();
        DeviceBean tmp_dev = null;
        int[] ids = devicelist.getIds();

        for (int i = 0; i < devicelist.size(); i++)
        {
            tmp_dev = devicelist.getDevice(ids[i]);

            for (int j = 0; j < ids_device.length; j++)
            {
                if (tmp_dev.getIddevmdl() == ids_device[j])
                {
                    dev_list.add(tmp_dev);
                }
            }
        }

        DeviceBean[] result = new DeviceBean[dev_list.size()];

        for (int i = 0; i < result.length; i++)
        {
            result[i] = (DeviceBean) dev_list.get(i);
        }

        return result;
    }

    public static void saveSwitchDevConf(int idsite, int id_switch,
        int[] ids_devices, String language,String description,String username) throws DataBaseException
    {
        String sql = "delete from switchdev where idsite=? and idswitch=?";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(id_switch);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        if (ids_devices != null)
        {
            param = new Object[4];
            param[0] = new Integer(idsite);
            param[1] = new Integer(id_switch);
            param[3] = new Timestamp(System.currentTimeMillis());
            sql = "insert into switchdev values (?,?,?,?)";

            for (int i = 0; i < ids_devices.length; i++)
            {
                param[2] = new Integer(ids_devices[i]);
                DatabaseMgr.getInstance().executeStatement(null, sql, param);
            }
            // log salavataggio variabili
            EventMgr.getInstance().info(new Integer(idsite), username, "switch",
                    "SW15", new Object[]{description});
        }
    }

    public static int[] retrieveIdsDevicesOfSwitch(int idsite, int id_switch)
        throws DataBaseException
    {
        String sql = "select iddevice from switchdev where idsite=? and idswitch=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(id_switch) });

        int[] ids = null;

        if (rs.size() > 0)
        {
            ids = new int[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                ids[i] = ((Integer) rs.get(i).get(0)).intValue();
            }
        }

        return ids;
    }

    public static int[] retrieveIdsDevicesUsed(int idsite)
        throws DataBaseException
    {
        String sql = "select iddevice from switchdev where idsite=? ";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });

        int[] ids = null;

        if (rs.size() > 0)
        {
            ids = new int[rs.size()];

            for (int i = 0; i < rs.size(); i++)
            {
                ids[i] = ((Integer) rs.get(i).get(0)).intValue();
            }
        }

        return ids;
    }

    public static int[] getSwitchVarConfigured(int idsite)
        throws DataBaseException //ritorna idswitch configurati dall'amministratore
    {
        String sql = "select distinct idswitch from switchvar where idsite=?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite) });
        int[] ids = new int[rs.size()];

        for (int i = 0; i < rs.size(); i++)
        {
            ids[i] = Integer.parseInt(rs.get(i).get(0).toString());
        }

        return ids;
    }
}
