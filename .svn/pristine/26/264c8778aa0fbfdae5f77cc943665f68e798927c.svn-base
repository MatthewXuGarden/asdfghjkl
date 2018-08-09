package com.carel.supervisor.field.dataconn.impl;

import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.math.Randomizer;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.*;
import com.carel.supervisor.field.types.ShortValue;


public class DataConnDUMMY extends DataConnBase
{
    
    public DataConnDUMMY() 
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
    	VariableInfo variableInfo = variable.getInfo();
    	if (variableInfo.getType() == VariableInfo.TYPE_DIGITAL)
    		variable.setValue(new Float(Randomizer.returnNumber(2)));
    	else
    		variable.setValue(new Float(Randomizer.returnNumber(0,3)));
    }

    public int setOnField(Variable variable)
    {
    	return DataConnBase.SET_ON_QUEUE_OK;
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
