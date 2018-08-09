package com.carel.supervisor.device;

import java.util.Map;

import com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList;

public interface IDeviceStatusList
{
    public DeviceStatus get(int pos);

    public void addLogic(DeviceInfoList devInfoList, Map functions);

    public DeviceStatus getById(Integer pos);

    public int size();

    public boolean existOffLine();

    public boolean isOffLineDevice(Integer idDevice);

    public String toString();

    public boolean isAlarmDevice(Integer idDevice);

    public boolean isDisableDevice(Integer idDevice);

    public void setDeviceActive(Integer idDevice, boolean status);
    
    public void setDeviceActive(Integer[] idDevice, boolean status);
    
    public void addAlarm(Integer idDevice);

    public void removeAlarm(Integer idDevice);

    public boolean existAlarm();
    
    public boolean existAlarm(int[] ids);
    
    public void resetAlarm();
}
