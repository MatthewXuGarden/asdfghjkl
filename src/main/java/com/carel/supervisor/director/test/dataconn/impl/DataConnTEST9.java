package com.carel.supervisor.director.test.dataconn.impl;

import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.DriverReturnCode;
import com.carel.supervisor.field.dataconn.impl.DataConnBase;
import com.carel.supervisor.field.types.PER_INFO;
import com.carel.supervisor.field.types.ShortValue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Random;


public class DataConnTEST9 extends DataConnBase
{
    private static Random random = new Random();
    private boolean blockingError = false;
    private boolean deviceOnLine = true;
    File fileOutput = new File(
            "C:\\swdept_prj\\plantvisorpro\\developments\\applications\\Director\\test data file\\WriteDataTest9.txt");
    FileOutputStream fileOutputStream = null;
    PrintStream printStream = null;
    long cicli = 0;
    SimpleDateFormat dateFormat = new SimpleDateFormat("k:m:s");
    int conta = 0;
    int gaussSum = 0;
    private int n = random.nextInt(127) + 1;
    private int entry = 0;

    public DataConnTEST9()
    {
        try
        {
            fileOutputStream = new FileOutputStream(fileOutput);
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        printStream = new PrintStream(fileOutputStream);
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
        blockingError = false;

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
        float value = variable.getCurrentValue();

        if (Float.isNaN(value))
        {
            value = 0;
        }

        if (cicli == 0)
        {
            printStream.print("Valore | Stato | n\n");
            variable.setValue(new Float(value));
        }

        if (entry == n)
        {
            variable.setValue(new Float(value + 1));
            printStream.println(value + 1 + "|" +
                ((variable.isDeviceDisabled() == false) ? 1 : 0) + "|" + n);
            n = random.nextInt(127) + 1;
            entry = 0;
        }

        entry++;
        cicli++;
    }

    public int setOnField(Variable variable)
    {
    	return 0;
    }

    //se true online se false off
    public boolean getDeviceStatus(DeviceInfo deviceInfo)
    {
        return deviceOnLine;
    }

    public boolean isBlockingError()
    {
        return blockingError;
    }

    public short retrieve(short globalIndex, short address, short type,
        ShortValue value)
    {
        return 0;
    }
}
