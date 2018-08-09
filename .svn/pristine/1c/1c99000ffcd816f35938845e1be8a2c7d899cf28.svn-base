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


public class DataConnVERYSTRESS extends DataConnBase
{
    private static Random random = new Random();

    public DataConnVERYSTRESS()
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
            value = 0;
        }

        switch (type)
        {
        case VariableInfo.TYPE_ANALOGIC:

            if (0 == value)
            {
                value = random.nextInt(150);
            }
            else
            {
                value = Math.round((value * (1 + (0.1 * random.nextGaussian()))));
            }

            break;

        case VariableInfo.TYPE_DIGITAL:

            if (0 == value)
            {
                value = 1;
            }
            else
            {
                value = 0;
            }

            break;

        case VariableInfo.TYPE_INTEGER:

            if (0 == value)
            {
                value = random.nextInt(150);
            }
            else
            {
                value = Math.round((value * (1 + (0.1 * random.nextGaussian()))));
            }

            value = (int) value;

            break;

        case VariableInfo.TYPE_ALARM:

            if (0 == value)
            {
                value = 1;
                DataConnBase.saveAlarmGuardian(variable);
            }
            else
            {
                value = 0;
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
