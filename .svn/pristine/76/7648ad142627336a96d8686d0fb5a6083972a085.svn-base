package com.carel.supervisor.field.dataconn.impl;

import java.util.Properties;

import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.*;
import com.carel.supervisor.field.types.ShortValue;
import com.carel.supervisor.base.config.Supervisor;

/* class emulate SUPERVISOR protocol
 * used to access Supervisor's "device memory"
 */
public class DataConnSUPERVISOR extends DataConnBase
{
	private static Supervisor supervisor = null;

	
    public DataConnSUPERVISOR()
    {
    }
    
    
    public void init(XMLNode xmlStatic) throws InvalidConfigurationException 
    {
    	// initialize supervisor memory
    	Properties properties = retrieveProperties(xmlStatic, "name", "value", "FLDE0004");
        int nMemSize = Integer.parseInt(retrieveAttribute(properties, "mem_size", "FLDE0004"));
        supervisor = Supervisor.getInstance();
        supervisor.init(nMemSize);
    }

    
    public DriverReturnCode loadDllDriver()
    {
        return new DriverReturnCode();
    }

    
    public DriverReturnCode initDriver()
    {
        return new DriverReturnCode();
    }

    
    public DriverReturnCode closeDriver()
    {
        return new DriverReturnCode();
    }

    
    public void retrieve(Variable variable)
    {
   		variable.setValue(supervisor.read(variable.getInfo().getAddressIn()));
   		saveAlarmGuardian(variable);
    }

    
    public int setOnField(Variable variable)
    {
    	if( supervisor.write(variable.getInfo().getAddressOut(), variable.getCurrentValue()) )
    		return DataConnBase.SET_ON_QUEUE_OK;
    	else
    		return DataConnBase.FATAL_ERROR;
    }

    
    public boolean getDeviceStatus(DeviceInfo deviceInfo)
    {
    	return true;
    }
    
    
    public short retrieve(short globalIndex, short address, short type, ShortValue value)
    {
    	return 0;
    }
}
