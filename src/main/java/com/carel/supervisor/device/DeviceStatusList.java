package com.carel.supervisor.device;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList;
import com.carel.supervisor.field.dataconn.DataCollector;


public class DeviceStatusList implements IDeviceStatusList
{
    private DeviceStatus[] deviceStatus = null;
    private Map presenceById = new HashMap();

    public DeviceStatusList(DeviceInfoList devInfoList,
        DataCollector dataCollector)
    {
        deviceStatus = new DeviceStatus[devInfoList.size()];

        for (int i = 0; i < devInfoList.size(); i++)
        {
            if (!devInfoList.get(i).isLogic())
            {
	        	deviceStatus[i] = new DeviceStatus(devInfoList.get(i),
	                    dataCollector);
	            presenceById.put(devInfoList.get(i).getId(), deviceStatus[i]);
            }
        }
    }

    public void addLogic(DeviceInfoList devInfoList, Map functions)
    {
    	for (int i = 0; i < devInfoList.size(); i++)
        {
            if (devInfoList.get(i).isLogic())
            {
	        	deviceStatus[i] = new DeviceStatus(devInfoList.get(i),
	        			functions);
	            presenceById.put(devInfoList.get(i).getId(), deviceStatus[i]);
            }
        }
    }
    
    public DeviceStatus get(int pos)
    {
        return deviceStatus[pos];
    }

    public DeviceStatus getById(Integer pos)
    {
        return (DeviceStatus) presenceById.get(pos);
    }

    public int size()
    {
        return deviceStatus.length;
    }

    public boolean existOffLine()
    {
        boolean status = true;

        for (int i = 0; i < deviceStatus.length; i++)
        {
            status = status && deviceStatus[i].getStatus();
        }

        return !status;
    }

    public boolean isOffLineDevice(Integer idDevice)
    {
        DeviceStatus device = (DeviceStatus) presenceById.get(idDevice);

        if (null == device)
        {
            return false;
        }
        else
        {
            return !(device.getStatus());
        }
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < deviceStatus.length; i++)
        {
            buffer.append(deviceStatus[i].toString());
            buffer.append("\n");
        }

        return buffer.toString();
    }

    public boolean isAlarmDevice(Integer idDevice)
    {
        DeviceStatus device = (DeviceStatus) presenceById.get(idDevice);

        if (null == device)
        {
            return false;
        }
        else
        {
            return device.isAlarm();
        }
    }

    public void setDeviceActive(Integer idDevice, boolean status)
    {
        DeviceStatus device = (DeviceStatus) presenceById.get(idDevice);

        if (null != device)
        {
            device.setDeviceActive(status);
        }
    }
    
    public void setDeviceActive(Integer[] idDevice, boolean status)
    {
    	for (int i = 0; i < idDevice.length; i++)
    	{
    		setDeviceActive(idDevice[i], status);
    	}
    }

    public void resetAlarm()
    {
    	for (int i = 0; i < deviceStatus.length; i++)
        {
            deviceStatus[i].resetAlarm();
        }
    }
    
    public void activeDevice(Integer idDevice)
    {
        DeviceStatus device = (DeviceStatus) presenceById.get(idDevice);

        if (null != device)
        {
            device.active();
        }
    }

    public boolean isDisableDevice(Integer idDevice)
    {
        DeviceStatus device = (DeviceStatus) presenceById.get(idDevice);

        if (null != device)
        {
            return device.isDisabled();
        }
        else
        {
            return true;
        }
    }

    public void addAlarm(Integer idDevice)
    {
        DeviceStatus device = (DeviceStatus) presenceById.get(idDevice);

        if (null != device)
        {
            device.addAlarm();
        }
    }

    public void removeAlarm(Integer idDevice)
    {
        DeviceStatus device = (DeviceStatus) presenceById.get(idDevice);

        if (null != device)
        {
            device.removeAlarm();
        }
        
    }

    public boolean existAlarm()
    {
        boolean status = false;

        for (int i = 0; i < deviceStatus.length; i++)
        {
            status = status || deviceStatus[i].isAlarm();
        }

        return status;
    }
    
    public boolean existAlarm(int[] ids)
    {	
    	DeviceStatus device = null;
        for (int i = 0; i < ids.length; i++)
        {
        	device = (DeviceStatus)presenceById.get(new Integer(ids[i]));
        	if (null != device)
        	{
	        	if (device.isAlarm())
	        	{
	        		return true;
	        	}
        	}
        }

        return false;
    }
}
