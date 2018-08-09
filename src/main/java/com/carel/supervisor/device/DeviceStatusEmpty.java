package com.carel.supervisor.device;

public class DeviceStatusEmpty extends DeviceStatus
{

	public DeviceStatusEmpty()
	{
		super();
	}
	
	public void ping()
    {
    }

    /**
         * @return: boolean
         */
    public boolean isLogic()
    {
        return false;
    }

    /**
     * @param isLogic
     */
    public void setLogic(boolean isLogic)
    {
    }

    /**
    * @return: boolean
    */
    public boolean getStatus()
    {
        return true;
    }

    public void addAlarm()
    {
    }

    public void removeAlarm()
    {
    }

    public void resetAlarm()
    {
    }

    public boolean isAlarm()
    {
        return false;
    }

    public String toString()
    {
        return "";
    }

    public void setDeviceActive(boolean status)
    {
    }

    public void disable()
    {
    }

    public void active()
    {
    }

    public boolean isDisabled()
    {
     return false;
    }
}
