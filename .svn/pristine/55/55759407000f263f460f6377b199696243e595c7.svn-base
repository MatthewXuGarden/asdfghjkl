package com.carel.supervisor.device;

import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableLogicInfoList;
import com.carel.supervisor.field.IFunction;
import com.carel.supervisor.field.dataconn.DataCollector;
import com.carel.supervisor.field.dataconn.IDataConnector;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class DeviceStatus implements Serializable
{
    private boolean status = false; //Stato del dispositivo: OFF-LINE/ON-LINE
    private int alarmCount = 0; //Stato di allarme del dispositivo (0 OK, >= ALARM)
    private String lines = null;
    private boolean disable = false;
    private boolean isLogic = false;
    private DeviceInfo deviceInfo = null;
    private IDataConnector dataConnector = null;
    //logic variable could refer to physical variables, the devices here means the physical variables' devices
    private Integer[] devices = null;

    protected DeviceStatus()
    {	
    }
  
    public DeviceStatus(DeviceInfo deviceInfo, DataCollector dataCollector)
    {
        isLogic = deviceInfo.isLogic();
        disable = (!deviceInfo.getEnabled());
        this.lines = deviceInfo.getLineInfo().getTypeProtocol();
        this.deviceInfo = deviceInfo;
        dataConnector = dataCollector.getDataConnector(lines);
    }

    public DeviceStatus(DeviceInfo deviceInfo, Map functions)
    {
        isLogic = deviceInfo.isLogic();
        disable = (!deviceInfo.getEnabled());
        Map map = new HashMap();
        Integer idVar = null;
        VariableLogicInfoList varLogicInfoList = (VariableLogicInfoList) DataConfigMgr.getInstance()
                                                                                      .getConfig("cfvarlg");
        VariableInfo variableInfo = null;

        for (int i = 0; i < varLogicInfoList.size(); i++)
        {
            variableInfo = varLogicInfoList.get(i);

            if (variableInfo.getDevice().intValue() == deviceInfo.getId().intValue())
            {
                idVar = variableInfo.getId();
                map.putAll(((IFunction) functions.get(idVar)).getIdDev());
            }
        }

        devices = (Integer[]) map.keySet().toArray(new Integer[map.size()]);
    }
    
    public void ping()
    {
        if (!isLogic)
        {
            if (!dataConnector.isBlockingError())
            {
                //status = dataConnector.getDeviceStatus((short)globalIndex);
                status = dataConnector.getDeviceStatus(deviceInfo);
            }
            else
            {
                status = false;
            }
        }
        else
        {
            status = true;

            DeviceStatusMgr dvsMgr = DeviceStatusMgr.getInstance();
            //Il dispositivo logico � offline se esiste almeno 1 dispositivo fisico offline
            // If at leat 1 var is offline, the whole device is marked as offline
            for (int i = 0; i < devices.length; i++)
            {
                status = status && (!dvsMgr.isOffLineDevice(devices[i]));
                if(!status)
                {
                	break;
                }
            }
        }
    }

    /**
         * @return: boolean
         */
    public boolean isLogic()
    {
        return isLogic;
    }

    /**
     * @param isLogic
     */
    public void setLogic(boolean isLogic)
    {
        this.isLogic = isLogic;
    }

    /**
    * @return: boolean
    */
    public boolean getStatus()
    {
        return status;
    }

    public void addAlarm()
    {
        if (!disable)
        {
            alarmCount++;
        }
    }

    public void removeAlarm()
    {
        if (!disable)
        {
            if (0 < alarmCount)
            {
                alarmCount--;
            }
        }
    }

    public void resetAlarm()
    {
        alarmCount = 0;
        status = false;
    }

    public boolean isAlarm()
    {
        return (alarmCount > 0);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("DEVICE: ");
        buffer.append(deviceInfo.getId());
        buffer.append(" STATUS: ");
        buffer.append(status);

        return buffer.toString();
    }

    public void setDeviceActive(boolean status)
    {
        this.disable = (!status);
    }

    public void disable()
    {
        this.disable = true;
    }

    public void active()
    {
        this.disable = false;
    }

    public boolean isDisabled()
    {
        if (!isLogic)
        {
            return disable;
        }
        else
        {
            if (disable) //Vince lo stato del dispositivo logico
            {
                return disable;
            }
//Se � attivo, vidiamo se tutti i suoi dispositivi sono stati disabilitati
            boolean active = true;
            DeviceStatusMgr dvsMgr = DeviceStatusMgr.getInstance();

            for (int i = 0; i < devices.length; i++)
            {
                active = active && (!dvsMgr.isDisabled(devices[i]));
            }

            return (!active);
        }
    }
}
