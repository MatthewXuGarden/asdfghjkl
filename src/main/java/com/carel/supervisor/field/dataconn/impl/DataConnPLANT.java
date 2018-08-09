package com.carel.supervisor.field.dataconn.impl;

import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.*;
import com.carel.supervisor.field.types.ShortValue;


public class DataConnPLANT extends DataConnBase
{
    
    public DataConnPLANT()
    {
    }
    
    public void init(XMLNode xmlStatic) throws InvalidConfigurationException 
    {
    	/*initFileName = retrieveAttribute(xmlStatic, FILE_NAME, "FLDE0004");
        String modeText = retrieveAttribute(xmlStatic, MODE, "FLDE0004");
        
        try
        {
        	mode = Short.parseShort(modeText);
        }
        catch(Exception e)
        {
        	FatalHandler.manage(this, CoreMessages.format("FLDE0005", String.valueOf(mode), MODE),e);
        }*/
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
