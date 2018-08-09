package com.carel.supervisor.field.dataconn.impl;

import java.util.Random;

import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.DriverReturnCode;
import com.carel.supervisor.field.types.PER_INFO;
import com.carel.supervisor.field.types.ShortValue;


public class DataConnSTRESS extends DataConnBase
{
    private static Random random = new Random();

    // rng limits
    private static final float ANALOG_MIN = 10;
    private static final float ANALOG_MAX = 40;
    private static final int INTEGER_MIN = 0;
    private static final int INTEGER_MAX = 10;
    
    public DataConnSTRESS()
    {
    }

    public void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
    }

    private native short locDrvClose();

    public short getPerifInfo(short perIdx, PER_INFO per_info) //PER_INFO oPER_INFO
    {
        return getPerifInfo(perIdx, per_info);
    }

    public DriverReturnCode loadDllDriver()
    {
        short returnCode = 0;

        return new DriverReturnCode(returnCode);
    }

    public DriverReturnCode initDriver()
    {
        return new DriverReturnCode();
    }

    public DriverReturnCode closeDriver()
    {
        try
        {
            return new DriverReturnCode(locDrvClose());
        }
        catch (Throwable e)
        {
            return new DriverReturnCode(e);
        }
    }

    public void retrieve(Variable variable)
    {
        int type = variable.getInfo().getType();
        float value = variable.getCurrentValue();

        if (Float.isNaN(value))
        {
        	value = ANALOG_MIN + (ANALOG_MAX - ANALOG_MIN) * random.nextFloat();
        }

        switch (type)
        {
        case VariableInfo.TYPE_ANALOGIC:

        	if (value<ANALOG_MAX)
        	{
        		value = Math.round((value * (1 + (0.1 * random.nextGaussian()))));
        		if( value > ANALOG_MAX )
        			value = ANALOG_MIN + (ANALOG_MAX - ANALOG_MIN) * random.nextFloat();
        	}
        	else
        	{
            	value = ANALOG_MIN + (ANALOG_MAX - ANALOG_MIN) * random.nextFloat();        	
        	}
        	break;

        case VariableInfo.TYPE_DIGITAL:

            if (random.nextInt(3) >= 1)
            {
                value = 1;
            }
            else
            {
                value = 0;
            }
            break;

        case VariableInfo.TYPE_INTEGER:

            if (value < INTEGER_MAX)
            {
                value++;
            }
            else
            {
            	value = INTEGER_MIN + random.nextInt(INTEGER_MAX - INTEGER_MIN + 1);
            }
            break;

        case VariableInfo.TYPE_ALARM:

            if (0 == value)
            {
                if (random.nextInt(660) >= 659)
                {
                    value = 1;
                    variable.setValue(new Float(value));
                    DataConnBase.saveAlarmGuardian(variable);
                }
                else
                {
                    value = 0;
                }
            }
            else
            {
                if (random.nextInt(500 + 1) >= 499)
                {
                    value = 0;
                }
                else
                {
                    value = 1;
                    variable.setValue(new Float(value));
                    DataConnBase.saveAlarmGuardian(variable);
                }
            }
            break;
        }

        variable.setValue(new Float(value));
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
