package com.carel.supervisor.field.dataconn.impl;

import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.*;
import com.carel.supervisor.field.types.ShortValue;


public class DataConnEMPTY extends DataConnBase
{
    
    public DataConnEMPTY() 
    {
    }
    
	public void init(XMLNode xmlStatic) throws InvalidConfigurationException 
    {
    }

    public DriverReturnCode loadDllDriver()
    {
        return new DriverReturnCode((short)0);
    }

    public DriverReturnCode initDriver()
    {
    	return new DriverReturnCode((short)0);
    }

    public DriverReturnCode closeDriver()
    {
    	return new DriverReturnCode((short)0);
    }

    public void retrieve(Variable variable)
    {
    }

    public int setOnField(Variable variable)
    {
    	return DataConnBase.SET_ON_QUEUE_OK;
    }
    
    public boolean getDeviceStatus(DeviceInfo deviceInfo) 
    {
    	return false;
    }
    
    public short retrieve(short globalIndex, short address, short type, ShortValue value)
    {
    	return 0;
    }
    
}
