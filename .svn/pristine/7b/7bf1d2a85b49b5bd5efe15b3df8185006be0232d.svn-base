package com.carel.supervisor.field.dataconn.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DeviceCanUtil;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.LineInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.field.FieldConnectorMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.DriverReturnCode;
import com.carel.supervisor.field.types.ExtUnitInfoT;
import com.carel.supervisor.field.types.LongValue;
import com.carel.supervisor.field.types.PER_INFO;
import com.carel.supervisor.field.types.ShortValue;


public class DataConnCAN extends DataConnBase
{
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String FILE_NAME = "fileName";
    private static final String PATH = "path";
    private static final String MODE = "mode";
    private static final String INI = "INI";
    private static final String CCT = "CCT";
    
    private String initFileName = null;
    private String path = null;
    private short mode = 1;
    
    private Boolean initialized = false;

    public DataConnCAN()
    {
    	initialized = false;
    }

    public void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        Properties properties = retrieveProperties(xmlStatic, NAME, VALUE,
                "FLDE0004");
        initFileName = retrieveAttribute(properties, FILE_NAME, "FLDE0004");
        path = retrieveAttribute(properties, PATH, "FLDE0004");

        String modeText = retrieveAttribute(properties, MODE, "FLDE0004");
        initFileName = path + initFileName;

        try
        {
            mode = Short.parseShort(modeText);
        }
        catch (Throwable e)
        {
            FatalHandler.manage(this,
                CoreMessages.format("FLDE0005", String.valueOf(mode), MODE), e);
        }
    }

    private native short intDll();

    private native short locDrvClose();
    
    private native boolean freeDll();

    private native short initDriverEx(String initFileName, Object data,
        short mode);

    private native short readDBValue(short unitIdx, short varIdx,
        short varType, Object data);

    private native short sendValue(short unitIdx, short varIdx, short varType,
        short value);

    private native short getUnitStatus(short unitIdx);
    
    private native PER_INFO getPerifInfo(short perIdx);

    private native ExtUnitInfoT getExtUnitInfo(short perIdx);
    
    public PER_INFO getPeriphericalInfo(short perIdx) //PER_INFO oPER_INFO
    {
    	//da 0 a n-1
    	PER_INFO retval = getPerifInfo((short)(perIdx - 1));
    	if(null==retval)
		{
            EventMgr.getInstance().log(new Integer(1), "Field-can", "Dll", EventDictionary.TYPE_ERROR, "F506", getName());
            LoggerMgr.getLogger(this.getClass()).error("Probably exception occurred in dll: getPerifInfo");
		}

        return retval;
    }
    
    public ExtUnitInfoT getPeriphericalInfoEx(short perIdx) //PER_INFO oPER_INFO
    {
    	//da 1 a n
    	ExtUnitInfoT retval = getExtUnitInfo(perIdx);
    	if(null==retval)
		{
            EventMgr.getInstance().log(new Integer(1), "Field-can", "Dll", EventDictionary.TYPE_ERROR, "F507", getName());
            LoggerMgr.getLogger(this.getClass()).error("Probably exception occurred in dll: getExtUnitInfo");
		}

        return retval;
    }
    
    public DriverReturnCode loadDllDriver()
    {
        try
        {
            System.loadLibrary("DataConnectorCan");
            return new DriverReturnCode((short)0);
            
        }
        catch (Throwable e)
        {
            setBlockingError();

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            EventMgr.getInstance().log(new Integer(1), "Field-can", "Connection",
                EventDictionary.TYPE_ERROR, "F003", getName());

            return new DriverReturnCode(e);
        }
    }

    public DriverReturnCode initDriver()
    {
        LongValue longValue = new LongValue();
        short code = 0;
        
        try
        {
        	short retCode = intDll(); 
        	
        	if (0 != retCode) //Error during dll load
            {
        		setBlockingError();
                EventMgr.getInstance().log(new Integer(1), "Field-can",
                    "Connection", EventDictionary.TYPE_ERROR, "F002", getName());

                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(CoreMessages.format("FLDE0008",
                        "dll CAN incompatible version",
                        String.valueOf(retCode)));

                return new DriverReturnCode(retCode, "", true);
            }
        	
            code = initDriverEx(initFileName, longValue, mode);            	
            DriverReturnCode returnCode = checkInitError(code,
                    longValue.getValue());

            if (0 != code)
            {
	            EventMgr.getInstance().log(new Integer(1), "Field-can", "Connection",
	                EventDictionary.TYPE_ERROR, "F004", getName());
	
	            Logger logger = LoggerMgr.getLogger(this.getClass());
	            logger.error(CoreMessages.format("FLDE0008",
	                    returnCode.getMessage(),
	                    String.valueOf(returnCode.getReturnCode())));
	            
	            // sbianco il file CCT in modo ke al close del driver non richiamo la chiusura
	            PrintWriter cctPrinter = new PrintWriter(new BufferedWriter(new FileWriter(initFileName+ "." + CCT, false)));
	        	cctPrinter.write("\n");
	    	    cctPrinter.flush();
	        	cctPrinter.close();
	        	// 
	        	
	        	//set initialized to remove close error
	        	initialized = true;
            }
            return returnCode;
        }
        catch (Throwable e)
        {
        	setBlockingError();
            EventMgr.getInstance().log(new Integer(1), "Field-can", "Connection",
                EventDictionary.TYPE_ERROR, "F003", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return new DriverReturnCode(e);
        }
    }

    /*public DriverReturnCode closeDriver()
    {
        int count = 0;
        try{
        	count = DeviceCanUtil.getCanLinesNumber();
        }catch (Exception e) {
			e.printStackTrace();
		}
        
        //vedere se il file CCT è vuoto. se vuoto saltare chiusura protocollo
        
        if (count>0)
        {
	    	try
	        {
	        	DriverReturnCode d = new DriverReturnCode(locDrvClose());
	        	if(999==d.getReturnCode())
	    		{
	                EventMgr.getInstance().log(new Integer(1), "Field", "Connection",EventDictionary.TYPE_ERROR, "F501", getName());
	                LoggerMgr.getLogger(this.getClass()).error("Exception in dll: locDrvClose");
	    		}
	        	boolean ret = freeDll();
	        	if(!ret)
	        	{
	                EventMgr.getInstance().log(new Integer(1), "Field", "Connection",EventDictionary.TYPE_ERROR, "F502", getName());
	                LoggerMgr.getLogger(this.getClass()).error("Probably exception occurred in dll: freeDll");
	    		}
	        	return d;
	        }
	        catch (Throwable e)
	        {
	            return new DriverReturnCode(e);
	        }
        }
        return new DriverReturnCode((short)0);
    }*/
    
    public DriverReturnCode closeDriver()
    {
    	boolean file_ok = false;
    	File file = new File(initFileName+ "." + CCT);
    	
    	if (null!=file)
    	{
    		try{
    		FileReader file_read = new FileReader(file);
	        BufferedReader buff = new BufferedReader(file_read);
	        
	        String tmp = buff.readLine();
	
	        if (null!=tmp && !"".equalsIgnoreCase(tmp))
	        {
	        	file_ok = true;
	        }
	        buff.close();
	        file_read.close();
    		}
    		catch (Exception e) {
				e.printStackTrace();
				file_ok = false;
			}
    	}
    	       
        //vedere se il file CCT è vuoto. se vuoto saltare chiusura protocollo
    	DriverReturnCode d = null;
        if (file_ok)
        {
        	d = new DriverReturnCode(locDrvClose());
        	if(999==d.getReturnCode())
    		{
                EventMgr.getInstance().log(new Integer(1), "Field-can", "Connection",EventDictionary.TYPE_ERROR, "F501", getName());
                LoggerMgr.getLogger(this.getClass()).error("Exception in dll: locDrvClose");
    		}
        }
        else 
        {
        	d = new DriverReturnCode((short)0);
        }
        	
    	try
        {
        	
        	boolean ret = freeDll();
        	if(initialized && !ret)
        	{
                EventMgr.getInstance().log(new Integer(1), "Field-can", "Connection",EventDictionary.TYPE_ERROR, "F502", getName());
                LoggerMgr.getLogger(this.getClass()).error("Probably exception occurred in dll: freeDll");
    		}
        	
        }
        catch (Throwable e)
        {
            return new DriverReturnCode(e);
        }
        
        return d;
    }

    /*public short retrieve(short globalIndex, short address, short type,
        ShortValue value)
    {
        return readDBValue(globalIndex, address, type, value);
    }*/

    public void retrieve(Variable variable)
    {
        String msg = null;

        try
        {
            if (0 == variable.getInfo().getAddressIn().intValue()) //Variabile 0 corrispondente all'OFFLINE
            {
                if (variable.isDeviceOffLine())
                {
                    variable.setValue(new Float(1));
                    //L'offline è un allarme. 
                    //Se l'allarme è attivo allora lo salvo, altrimenti appartiene al giro delle logiche
                    //E pertanto viene già eventualmente loggate dalle logiche nel Director
                    DataConnBase.saveOfflineGuardian(variable);
                }
                else
                {
                    variable.setValue(new Float(0));
                }
            }
            else
            {
                if (!variable.isDeviceOffLine())
                {
                    ShortValue shortValue = new ShortValue();
                    VariableInfo variableInfo = variable.getInfo();
                    short returnCode = readDBValue(variableInfo.getGlobalIndex(),
                            (short) variableInfo.getAddressIn().intValue(),
                            (short) variableInfo.getVarTypeForAcquisition(),
                            shortValue);

                    //0 : NO CONNECTION 
                    short value = shortValue.getValue();

                    valueFromDriver(variableInfo, variable, value);

                    switch (returnCode)
                    {
                    case 1:
                    	//Se è una variabile di allarme e vale 1, ecc... allora la inserisco nella tabella degli allarmi
                    	DataConnBase.saveAlarmGuardian(variable);
                    	
                        break;

                    case 0:
                        variable.setValue(null); //Not yet acquired

                        break;

                    case 90:
                        variable.activeBlockingError();
                        msg = CoreMessages.format("FLDE1022");

                        break;

                    case 100:
                        variable.activeBlockingError();
                        msg = CoreMessages.format("FLDE1023");

                        break;

                    case -1:
                        variable.activeBlockingError();
                        msg = CoreMessages.format("FLDE1024");

                        break;

                    case 999:
                        variable.activeBlockingError();
                        LoggerMgr.getLogger(this.getClass()).error("Exception in dll: readDBValue");
                        /*
                         * Trava
                         * Commentato troppe righe negli eventi circa 30/40 pagina al colpo
                         */
                        //EventMgr.getInstance().log(new Integer(1), "Field", "Connection", EventDictionary.TYPE_ERROR, "F503", getName());
                    	break;
                    	
                    default: //non dovrebbe mai passare di qui
                        msg = CoreMessages.format("FLDE1025");
                        variable.activeBlockingError();
                    }

                    if (variable.isBlockingError())
                    {
                        EventMgr.getInstance().log(new Integer(1), "Field-can",
                            "Acquisition", EventDictionary.TYPE_ERROR, "F005",
                            getName());

                        Logger logger = LoggerMgr.getLogger(this.getClass());
                        logger.error(CoreMessages.format("FLDE0010",
                                new Object[]
                                {
                                    msg,
                                    String.valueOf(
                                        variableInfo.getGlobalIndex()),
                                    String.valueOf(variableInfo.getAddressIn()
                                                               .intValue()),
                                    String.valueOf(
                                        variableInfo.getVarTypeForAcquisition())
                                }));
                    }
                }
                else //device is offline
                {
                    variable.setValue(null);
                }
            }
        }
        catch (Throwable e)
        {
        	setBlockingError();
            EventMgr.getInstance().log(new Integer(1), "Field-can", "Acquisition",
                EventDictionary.TYPE_ERROR, "F005", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    public int setOnField(Variable variable)
    {
        String msg = null;

        try
        {
            VariableInfo variableInfo = variable.getInfo();

            short newValue = (short)valueToDriver(variableInfo, variable);
            
            variable.setValue(new Float(variable.getCurrentValue() * 10));
            
            short returnCode = sendValue(variableInfo.getGlobalIndex(),
                    (short) variableInfo.getAddressIn().intValue(),
                    (short) variableInfo.getVarTypeForAcquisition(),
                    newValue);

            switch (returnCode)
            {
            case 0:
                break;

            case -1:
                variable.activeBlockingError();
                msg = CoreMessages.format("FLDE1026");

                break;

            case 50:
                //variable.activeBlockingError();
                //msg = CoreMessages.format("FLDE1027");
            	return DataConnBase.SET_QUEUE_FULL;

            case 90:
                variable.activeBlockingError();
                msg = CoreMessages.format("FLDE1028");

                break;

            case 100:
                variable.activeBlockingError();
                msg = CoreMessages.format("FLDE1029");

                break;

            case 110:
                //variable.activeBlockingError();
                msg = CoreMessages.format("FLDE1030");

                break;

            case 120:
                variable.activeBlockingError();
                msg = CoreMessages.format("FLDE1031");

                break;

            case 130:
                //variable.activeBlockingError();
                msg = CoreMessages.format("FLDE1032");

                break;

            case 999:
                variable.activeBlockingError();
                LoggerMgr.getLogger(this.getClass()).error("Exception in dll: sendValue");
                //EventMgr.getInstance().log(new Integer(1), "Field", "Connection", EventDictionary.TYPE_ERROR, "F504", getName());
            	break;
            	
            default: //non dovrebbe mai passare di qui
                variable.activeBlockingError();
                msg = CoreMessages.format("FLDE1033");
            }

            if (variable.isBlockingError())
            {
                EventMgr.getInstance().log(new Integer(1), "Field-can",
                    "Acquisition", EventDictionary.TYPE_ERROR, "F006", getName()+" "+msg);

                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(CoreMessages.format("FLDE0010",
                        new Object[]
                        {
                            msg, String.valueOf(variableInfo.getGlobalIndex()),
                            String.valueOf(variableInfo.getAddressIn().intValue()),
                            String.valueOf(
                                variableInfo.getVarTypeForAcquisition())
                        }));
            }
            return returnCode;
        }
        catch (Throwable e)
        {
        	setBlockingError();
            EventMgr.getInstance().log(new Integer(1), "Field-can", "Acquisition",
                EventDictionary.TYPE_ERROR, "F006", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            return DataConnBase.BLOCKING_ERROR;
        }
        
    }

    public boolean getDeviceStatus(DeviceInfo deviceInfo)
    {
        try
        {
            short status = getUnitStatus((short)deviceInfo.getGlobalindex().intValue());

            if(status == 999)
            {
            	setBlockingError();
                //EventMgr.getInstance().log(new Integer(1), "Field", "Connection", EventDictionary.TYPE_ERROR, "F505", getName());
                LoggerMgr.getLogger(this.getClass()).error("Exception in dll: getUnitStatus");
            }
            /* Status:
             * 0   periferica online
             * 1   periferica offline
             */
            return (0 == status);
        }
        catch (Throwable e)
        {
        	setBlockingError();
            EventMgr.getInstance().log(new Integer(1), "Field-can", "Acquisition",
                EventDictionary.TYPE_ERROR, "F007", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return false;
        }
    }

    private DriverReturnCode checkInitError(short code, long row)
    {
        DriverReturnCode returnCode = null;

        switch (code)
        {
        case 0:
            returnCode = new DriverReturnCode(code);
            removeBlockingError();

            break;

        case 20:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1000"), true);
            removeBlockingError();

            break;

        case 160:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1001"), true);
            removeBlockingError();

            break;

        case 180:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDW1002"), true);
            removeBlockingError();

            break;

        case 185:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDW1003"), true);
            removeBlockingError();

            break;

        case 10:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1004"), true);

            break;

        case 15:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1005"), true);
            setBlockingError();

            break;

        case 25:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1006"), true);
            setBlockingError();

            break;

        case 30:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1007"), true);
            setBlockingError();

            break;

        case 32:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1008"), true);
            setBlockingError();

            break;

        case 35:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1009", String.valueOf(row)), true);
            setBlockingError();

            break;

        case 40:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1010", String.valueOf(row)), true);
            setBlockingError();

            break;

        case 45:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1011"), true);
            setBlockingError();

            break;

        case 50:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1012"), true);
            setBlockingError();

            break;

        case 65:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1013", String.valueOf(row)), true);
            setBlockingError();

            break;

        case 75:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1014"), true);
            setBlockingError();

            break;

        case 80:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1015", String.valueOf(row)), true);
            setBlockingError();

            break;

        case 85:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1016", String.valueOf(row)), true);
            setBlockingError();

            break;

        case 165:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1017"), true);
            setBlockingError();

            break;

        case 170:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1018"), true);
            setBlockingError();

            break;

        case 175:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1019"), true);
            setBlockingError();

            break;

        case 176:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1020"), true);
            setBlockingError();

            break;

        default:
            returnCode = new DriverReturnCode(code,
                    CoreMessages.format("FLDE1021"), true);
        setBlockingError();

            break;
        }

        returnCode.setRowNumber(row);

        return returnCode;
    }

    public void writeProtocol() throws Exception
    {
    	//FILE DRIVERCAN.INI
    	LineInfo[] lines = DeviceCanUtil.getLines(null, BaseConfig.getPlantId()); 
    	LineInfo line = null;
    	
    	PrintWriter iniPrinter = new PrintWriter(new BufferedWriter(new FileWriter(initFileName+ "." + INI, false)));
    	if (lines.length>0 && null!=lines[0])
    	{
    		line = lines[0];
	    	iniPrinter.write("[General]\n");
	    	iniPrinter.write("canSpeed=");
	    	iniPrinter.write(convertBaudRate(line.getBaudrate()));
	    	iniPrinter.write("\n");
	    	iniPrinter.write("logEn=0\n");
	    	iniPrinter.write("logDur=30\n\n");
	    	iniPrinter.write("[Linea 1]\n");
	    	iniPrinter.write("Linea=");
	    	iniPrinter.write(String.valueOf(line.getCode()));
    	}
    	iniPrinter.write("\n");
    	iniPrinter.flush();
    	iniPrinter.close();
    	//end file
    	
    	//FILE DRIVERCAN.CCT
    	PrintWriter cctPrinter = new PrintWriter(new BufferedWriter(new FileWriter(initFileName+ "." + CCT, false)));
    	if (lines.length>0 && null!=lines[0])
    	{
	    	DeviceInfo[] deviceList = DeviceCanUtil.getCanDevicesOfLine(null, BaseConfig.getPlantId(), line.getId());
	    	
	    	DeviceInfo device = null;
	    	for(int i = 0; i < deviceList.length; i++)
	    	{
	    		device = deviceList[i];
	    		cctPrinter.write(String.valueOf(device.getGlobalindex().intValue()));
	    		cctPrinter.write(" ");
	    		cctPrinter.write(String.valueOf(line.getCode()));
	    		cctPrinter.write(" ");
	    		cctPrinter.write(String.valueOf(device.getAddress().intValue()));
	    		cctPrinter.write("\n");
	    	}
	    	
    	}
    	cctPrinter.flush();
    	cctPrinter.close();
    }
    
    private String convertBaudRate(int bound)
    {
    	if (62500 == bound)
    		return "0";
    	else if (125000 == bound)
    		return "1";
    	else
    		return "0";
    }
    
    public static void main(String[] argv) throws Throwable
    {
        BaseConfig.init();
    	DataConnCAN d = (DataConnCAN)FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAN");
        d.loadDllDriver();
        d.writeProtocol();
    }
}
