package com.carel.supervisor.device;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList;
import com.carel.supervisor.field.dataconn.DataCollector;
import java.util.Map;


public class DeviceStatusMgr
{
    private static DeviceStatusMgr me = new DeviceStatusMgr();
    private IDeviceStatusList deviceStatusList = new DeviceStatusDummy();

    private DeviceStatusMgr()
    {
    }

    public static DeviceStatusMgr getInstance()
    {
        return me;
    }

    public synchronized void loadPhysic(DataCollector dataCollector)
    {
        DeviceInfoList deviceInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");

        if (deviceInfoList.size() > 0)
        {
            deviceStatusList = new DeviceStatusList(deviceInfoList, dataCollector);
        }
        else
        {
            deviceStatusList = new DeviceStatusDummy();
        }
    }

    public synchronized void loadLogic(Map functions)
    {
        DeviceInfoList deviceInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");

        if (deviceInfoList.size() > 0)
        {
            deviceStatusList.addLogic(deviceInfoList, functions);
        }
        else
        {
            deviceStatusList = new DeviceStatusDummy();
        }
    }

    public void pingAll()
    {
        DeviceStatus deviceStatus = null;

        for (int i = 0; i < deviceStatusList.size(); i++)
        {
            deviceStatus = deviceStatusList.get(i);
            deviceStatus.ping();
        }
    }

    public boolean isOffLineDevice(Integer idDevice)
    {
        return deviceStatusList.isOffLineDevice(idDevice);
    }

    public boolean isAlarm(Integer idDevice)
    {
        return deviceStatusList.isAlarmDevice(idDevice);
    }

    public boolean isDisabled(Integer idDevice)
    {
        return deviceStatusList.isDisableDevice(idDevice);
    }

    public void setDeviceActive(Integer[] idDevice, boolean status)
    {
        deviceStatusList.setDeviceActive(idDevice, status);
    }

    public boolean existAlarm()
    {
        return deviceStatusList.existAlarm();
    }

    public boolean existAlarm(int[] ids)
    {
        return deviceStatusList.existAlarm(ids);
    }
    
    public void removeAlarm(Integer idDevice)
    {
        deviceStatusList.removeAlarm(idDevice);
    }

    public void addAlarm(Integer idDevice)
    {
        deviceStatusList.addAlarm(idDevice);
    }

    public DeviceStatus getDeviceStatus(Integer iDevice)
    {
        return deviceStatusList.getById(iDevice);
    }

    public void resetAlarms()
    {
        deviceStatusList.resetAlarm();
    }
}
