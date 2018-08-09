package com.carel.supervisor.device;

import java.util.Map;

import com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList;

public class DeviceStatusDummy implements IDeviceStatusList
{
    public DeviceStatus get(int pos)
    {
        return null;
    }

    public void addLogic(DeviceInfoList devInfoList, Map functions)
    {
    	
    }
    
    public DeviceStatus getById(Integer pos)
    {
        return null;
    }

    public int size()
    {
        return 0;
    }

    public boolean existOffLine()
    {
        return false;
    }

    public boolean isOffLineDevice(Integer idDevice)
    {
        return false;
    }

    public String toString()
    {
        return "";
    }

    public boolean isAlarmDevice(Integer idDevice)
    {
        return false;
    }

    public void setDeviceActive(Integer idDevice, boolean status)
    {
    }
    
    public void setDeviceActive(Integer[] idDevice, boolean status)
    {
    }

    public boolean isDisableDevice(Integer idDevice)
    {
        return true;
    }

    public void addAlarm(Integer idDevice)
    {
    }

    public void removeAlarm(Integer idDevice)
    {
    }

    public boolean existAlarm()
    {
        return false;
    }
    
    public boolean existAlarm(int[] ids)
    {	
    	return false;
    }
    
    public void resetAlarm()
    {
    	
    }
}
