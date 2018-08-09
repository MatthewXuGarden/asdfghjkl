package com.carel.supervisor.director.test.dataconn.impl;

import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.*;
import com.carel.supervisor.field.dataconn.impl.DataConnBase;
import com.carel.supervisor.field.types.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Random;


public class DataConnTEST1 extends DataConnBase
{
    private static Random random = new Random();
    private boolean blockingError = false;
    private boolean deviceOnLine = true;
    File fileOutput = new File(
            "C:\\swdept_prj\\plantvisorpro\\developments\\applications\\Director\\test data file\\WriteDataTest1.txt");
    FileOutputStream fileOutputStream = null;
    PrintStream printStream = null;
    long cicli = 0;
    SimpleDateFormat dateFormat = new SimpleDateFormat("k:m:s");
    int conta = 0;
    int gaussSum = 0;

    public DataConnTEST1()
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
        System.out.println("\nCiclo:" + cicli + "\n");

        int type = variable.getInfo().getType();
        float value = variable.getCurrentValue();

        if (Float.isNaN(value))
        {
            value = 0;
        }

        if (cicli == 0)
        {
            printStream.print("Valore | Stato\n");
        }

        switch (type)
        {
        case VariableInfo.TYPE_ANALOGIC:

            float tmp = random.nextInt(200);

            if (tmp != value)
            {
                value = tmp;
            }
            else
            {
                value += 1;
            }

            printStream.println(value + "|" +
                ((variable.isDeviceDisabled() == false) ? 1 : 0));
            System.out.println(value + "|" + cicli);
        }

        conta++;
        cicli++;
        variable.setValue(new Float(value));
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
