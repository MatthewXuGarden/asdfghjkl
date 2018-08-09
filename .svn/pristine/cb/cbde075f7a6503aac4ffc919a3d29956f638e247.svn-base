package com.carel.supervisor.field.dataconn.impl;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.config.ResourceLoader;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.math.BitManipulation;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DeviceCarelInfoList;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.LineCarelInfoList;
import com.carel.supervisor.dataaccess.dataconfig.LineInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.field.FieldConnectorMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.DriverReturnCode;
import com.carel.supervisor.field.types.DoubleValue;
import com.carel.supervisor.field.types.ExtUnitInfoT;
import com.carel.supervisor.field.types.LongValue;
import com.carel.supervisor.field.types.PER_INFO;
import com.carel.supervisor.field.types.PollingInfoT;
import com.carel.supervisor.field.types.ShortValue;


public class DataConnCAREL extends DataConnBase
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
    protected short mode = 1;
    
    protected Boolean initialized = false;


    public DataConnCAREL()
    {
    }

    public synchronized void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        Properties properties = retrieveProperties(xmlStatic, NAME, VALUE,
                "FLDE0004");
        initFileName = retrieveAttribute(properties, FILE_NAME, "FLDE0004");
        if(BaseConfig.isDemo())
            path = BaseConfig.getCarelPath();
        else
        	path = retrieveAttribute(properties, PATH, "FLDE0004");
        initFileName = path + initFileName;

        String modeText = retrieveAttribute(properties, MODE, "FLDE0004");
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

    protected native short intDll();

    protected native short locDrvClose();
    
    protected native boolean freeDll();

    protected native short initDriverEx(String initFileName, Object data,
        short mode);

    protected native short readDBValue(short unitIdx, short varIdx,
        short varType, Object data);

    protected native short sendValue(short unitIdx, short varIdx, short varType,
        short value);

    public native short getUnitStatus(short unitIdx);
    
    protected native PER_INFO getPerifInfo(short perIdx);

    protected native ExtUnitInfoT getExtUnitInfo(short perIdx);
    
    public native PollingInfoT getPollingInfo(short UnitIdxLineIdx, short Type);
    
    // used to tweak device detection procedure
    // no need to use them explicitly
    public native short setOffLineTime(int n);
    public native short setFlags(int n);
    
    public synchronized PER_INFO getPeriphericalInfo(short perIdx) //PER_INFO oPER_INFO
    {
    	//da 0 a n-1
    	PER_INFO retval = getPerifInfo((short)(perIdx - 1));
    	if(null==retval)
		{
            EventMgr.getInstance().log(new Integer(1), "Field", "Dll", EventDictionary.TYPE_ERROR, "F506", getName());
            LoggerMgr.getLogger(this.getClass()).error("Probably exception occurred in dll: getPerifInfo");
		}

        return retval;
    }
    
    public synchronized ExtUnitInfoT getPeriphericalInfoEx(short perIdx) //PER_INFO oPER_INFO
    {
    	//da 1 a n
    	ExtUnitInfoT retval = getExtUnitInfo(perIdx);
    	if(null==retval)
		{
            EventMgr.getInstance().log(new Integer(1), "Field", "Dll", EventDictionary.TYPE_ERROR, "F507", getName());
            LoggerMgr.getLogger(this.getClass()).error("Probably exception occurred in dll: getExtUnitInfo");
		}

        return retval;
    }
    
    public synchronized DriverReturnCode loadDllDriver()
    {
        try
        {
            System.loadLibrary("DataConnector");
            return new DriverReturnCode((short)0);
            
        }
        catch (Throwable e)
        {
            setBlockingError();

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            EventMgr.getInstance().log(new Integer(1), "Field", "Connection",
                EventDictionary.TYPE_ERROR, "F003", getName());

            return new DriverReturnCode(e);
        }
    }

    public synchronized DriverReturnCode initDriver()
    {
    	return initDriver(this.initFileName);
    }
    
    public synchronized DriverReturnCode initDriver(String initFileName)
    {
        LongValue longValue = new LongValue();
        short code = 0;

        try
        {
        	short retCode = intDll();
        	LoggerMgr.getLogger(this.getClass()).info("intDll -> " + retCode);
        	
        	if (0 != retCode) //Error during dll load
            {
        		setBlockingError();
                EventMgr.getInstance().log(new Integer(1), "Field",
                    "Connection", EventDictionary.TYPE_ERROR, "F002", getName());

                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(CoreMessages.format("FLDE0008",
                        "dll CAREL incompatible version",
                        String.valueOf(retCode)));

                return new DriverReturnCode(retCode, "", true);
            }
        	
            code = initDriverEx(initFileName, longValue, mode);
            LoggerMgr.getLogger(this.getClass()).info("initDriverEx -> " + code);
            	
            DriverReturnCode returnCode = checkInitError(code,
                    longValue.getValue());

            if (0 != code)
            {
	            EventMgr.getInstance().log(new Integer(1), "Field", "Connection",
	                EventDictionary.TYPE_ERROR, "F004", getName());
	
	            Logger logger = LoggerMgr.getLogger(this.getClass());
	            logger.error(CoreMessages.format("FLDE0008",
	                    returnCode.getMessage(),
	                    String.valueOf(returnCode.getReturnCode())));
            }

            //set initialized to remove close error
        	initialized = true;
        	specialFunctionInInitDriver();
        	return returnCode;
        }
        catch (Throwable e)
        {
        	setBlockingError();
            EventMgr.getInstance().log(new Integer(1), "Field", "Connection",
                EventDictionary.TYPE_ERROR, "F003", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return new DriverReturnCode(e);
        }
    }
    protected void specialFunctionInInitDriver()
    {}
    public synchronized DriverReturnCode closeDriver()
    {
    	specialFunctionInCloseDriver();
    	try {
			DriverReturnCode d;
        	if( initialized ) {
        		// locDrvClose crash VM if driver it was not previously initialized
        		short code = locDrvClose();
        		LoggerMgr.getLogger(this.getClass()).info("locDrvClose -> " + code);
				d = new DriverReturnCode(code);
	        	if(999==d.getReturnCode())
	    		{
	                EventMgr.getInstance().log(new Integer(1), "Field", "Connection",EventDictionary.TYPE_ERROR, "F501", getName());
	                LoggerMgr.getLogger(this.getClass()).error("Exception in dll: locDrvClose");
	    		}
	        	initialized = false;
        	}
        	else {
        		d = new DriverReturnCode();
        	}
        	
	        boolean ret = freeDll();
	        LoggerMgr.getLogger(this.getClass()).info("freeDll -> " + ret);
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
    protected void specialFunctionInCloseDriver()
    {}
    /*public short retrieve(short globalIndex, short address, short type,
        ShortValue value)
    {
        return readDBValue(globalIndex, address, type, value);
    }*/

    public synchronized void retrieve(Variable variable)
    {
        String msg = null;

        try
        {
            if (0 == variable.getInfo().getAddressIn().intValue()) //Variabile 0 corrispondente all'OFFLINE
            {
                if (variable.isDeviceOffLine())
                {
                    variable.setValue(new Float(1));
                    //L'offline � un allarme. 
                    //Se l'allarme � attivo allora lo salvo, altrimenti appartiene al giro delle logiche
                    //E pertanto viene gi� eventualmente loggate dalle logiche nel Director
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
                	VariableInfo variableInfo = variable.getInfo();
                	short returnCode = 0;
                    ShortValue shortValue = new ShortValue();
                	if( variableInfo.getVarDimension() == 32 && variableInfo.getVarLength() == 32 ) {
                    	// 32 bits variable
                		int nVal32 = 0;
                		// receive 1st half of the number
                		returnCode = readDBValue(variableInfo.getGlobalIndex(),
                			(short)variableInfo.getAddressIn().intValue(),
                			(short)variableInfo.getVarTypeForAcquisition(),
                			shortValue);
                		if( returnCode == 1 ) { // 1st half received successfully
                			nVal32 |= shortValue.getValue();
                			nVal32 <<= 16;
                    		// receive 2nd half of the number
                    		returnCode = readDBValue(variableInfo.getGlobalIndex(),
                       			(short)(variableInfo.getAddressIn().intValue() + 1),
                       			(short)variableInfo.getVarTypeForAcquisition(),
                       			shortValue);
                    		if( returnCode == 1 ) { // 2nd half received successfully
                    			nVal32 |= ((int)shortValue.getValue()&0x0000FFFF);
                    			// update variable value only if both halves are available
                    			variable.setValue((float)nVal32);
                    		}
                		}
                    }
                    else {
                    	// 16 bits variable
	                    returnCode = readDBValue(variableInfo.getGlobalIndex(),
	                        (short) variableInfo.getAddressIn().intValue(),
	                        (short) variableInfo.getVarTypeForAcquisition(),
	                        shortValue);
	
	                    short value = shortValue.getValue();
	                    valueFromDriver(variableInfo, variable, value);
                    }
                    switch (returnCode)
                    {
                    case 1:
                    	//Se � una variabile di allarme e vale 1, ecc... allora la inserisco nella tabella degli allarmi
                    	DataConnBase.saveAlarmGuardian(variable);
                        break;

                    case 0: // NO CONNECTION 
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
                        EventMgr.getInstance().log(new Integer(1), "Field",
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
            EventMgr.getInstance().log(new Integer(1), "Field", "Acquisition",
                EventDictionary.TYPE_ERROR, "F005", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    public synchronized int setOnField(Variable variable)
    {
        String msg = null;

        try
        {
            VariableInfo variableInfo = variable.getInfo();
            short returnCode = 0;
            if( variableInfo.getVarDimension() == 32 && variableInfo.getVarLength() == 32 ) {
                // 32 bits variable
            	int nVal32 = (int)variable.getCurrentValue();
            	short nVal16 = (short)((nVal32 & 0xFFFF0000) >> 16);
            	// send 1st half of the number
            	returnCode = sendValue(variableInfo.getGlobalIndex(),
            		(short)variableInfo.getAddressIn().intValue(),
            		(short)variableInfo.getVarTypeForAcquisition(),
            		nVal16);
            	// send 2nd half of the number if 1st half sent successfully
            	if( returnCode == 0 ) {
            		nVal16 = (short)(nVal32 & 0x0000FFFF);
                	returnCode = sendValue(variableInfo.getGlobalIndex(),
                    		(short)(variableInfo.getAddressIn().intValue() + 2),
                    		(short)variableInfo.getVarTypeForAcquisition(),
                    		nVal16);
            	}
            	// device should be able to detect if both halves are available before using 32 bits variable
            }
            else {
            	// 16 bits variable
	            short newValue = (short)valueToDriver(variableInfo, variable);
	            
	            // Nicola Compagno 21121206: This rows causes wrong value setting when the set operation is repeated due to 
	            // "full dll queue" (value is multiplied by 10,100,... depending on the number of retries)
	            // After code analysis it seems also that the row is not necessary 
	            
	            //variable.setValue(new Float(variable.getCurrentValue() * 10));
	            
	            returnCode = sendValue(variableInfo.getGlobalIndex(),
	                    (short) variableInfo.getAddressIn().intValue(),
	                    (short) variableInfo.getVarTypeForAcquisition(),
	                    newValue);
            }
            
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
                EventMgr.getInstance().log(new Integer(1), "Field",
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
            EventMgr.getInstance().log(new Integer(1), "Field", "Acquisition",
                EventDictionary.TYPE_ERROR, "F006", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            return DataConnBase.BLOCKING_ERROR;
        }
        
        /*
         * Intervento tampone per bufferoverflow della DLL CAREL
         */
        //try{ Thread.sleep(100); } catch(Exception e){}
    }

    public synchronized boolean getDeviceStatus(DeviceInfo deviceInfo)
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
            EventMgr.getInstance().log(new Integer(1), "Field", "Acquisition",
                EventDictionary.TYPE_ERROR, "F007", getName());

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return false;
        }
    }

    protected DriverReturnCode checkInitError(short code, long row)
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

    @SuppressWarnings("unchecked")
	public synchronized void writeProtocol() throws Exception
    {
    	Properties p = new Properties();
    	try {
    		p.load(new FileInputStream(initFileName+ "." + INI));
    	} catch(Exception e) {
    		// nothing to do just continue fn execution 
    	}
        String enableTymeSynch = p.getProperty("EnableTimeSyncMsg");
    	
    	LineCarelInfoList lineList = new LineCarelInfoList(null, BaseConfig.getPlantId());
    	DeviceCarelInfoList deviceList = new DeviceCarelInfoList(null, BaseConfig.getPlantId());
    	Map registry = new HashMap();
    	registry.put("cfline",lineList);
    	deviceList.bind(registry);
    	PrintWriter iniPrinter = new PrintWriter(new BufferedWriter(new FileWriter(initFileName+ "." + INI, false)));
    	iniPrinter.write("[config]\n");
    	LineInfo line = null;
    	String comPort = null;
    	
		//PC-Gate bug fix - 1
		Properties pBug = new Properties();
		URL fileconf = ResourceLoader.fileFromResourcePath("careldll","driverRetry.properties");
		java.io.InputStream is = fileconf.openStream();
		pBug.load(is);
		String bugVal;
		//end PC-Gate bug fix - 1
		
    	for(int i = 0; i < lineList.size(); i++)
    	{
    		line = lineList.get(i);
    		iniPrinter.write("Line");
    		iniPrinter.write(String.valueOf(line.getCode()));	
    		iniPrinter.write("=");
    		comPort = line.getComport().substring(3);
    		iniPrinter.write(comPort);
    		iniPrinter.write(",");
    		iniPrinter.write(convertBaudRate(line.getBaudrate()));
    		iniPrinter.write(",");
    		iniPrinter.write(convertProtocol(line.getProtocol().substring(2)));
    		
    		//PC-Gate bug fix - 2
    		bugVal = pBug.getProperty(line.getComport());
    		if (bugVal !=null)
    				iniPrinter.write(","+bugVal);
    		//end PC-Gate bug fix - 2
    		
    		iniPrinter.write("\n");
    	}
    	
    	iniPrinter.write("\n");
    	iniPrinter.write("MaxRetry485Q=3");
    	iniPrinter.write("\n");
    	iniPrinter.write("MaxRetry485=8");
    	iniPrinter.write("\n");
    	iniPrinter.write("MaxNullWrite=40");
    	iniPrinter.write("\n");
    		
    	if(enableTymeSynch != null)
    	{
    		iniPrinter.write("EnableTimeSyncMsg="+enableTymeSynch);
    		iniPrinter.write("\n");
    	}
    	
    	iniPrinter.flush();
    	iniPrinter.close();
    	
    	//PC-Gate bug fix - 3
    	pBug=null;
    	is.close();
    	//end PC-Gate bug fix - 3
    	
    	PrintWriter cctPrinter = new PrintWriter(new BufferedWriter(new FileWriter(initFileName+ "." + CCT, false)));
    	DeviceInfo device = null;
    	for(int i = 0; i < deviceList.size(); i++)
    	{
    		device = deviceList.get(i);
    		cctPrinter.write(String.valueOf(device.getGlobalindex().intValue()));
    		cctPrinter.write(" ");
    		cctPrinter.write(String.valueOf(device.getLineInfo().getCode()));
    		cctPrinter.write(" ");
    		cctPrinter.write(String.valueOf(device.getAddress().intValue()));
    		cctPrinter.write("\n");
    	}
    	cctPrinter.flush();
    	cctPrinter.close();
    }
    public synchronized void writeProtocol(int[] selectedDeviceid) throws Exception
    {
    	Properties p = new Properties();
    	try {
    		p.load(new FileInputStream(initFileName+ "." + INI));
    	} catch(Exception e) {
    		// nothing to do just continue fn execution 
    	}
        String enableTymeSynch = p.getProperty("EnableTimeSyncMsg");
    	
    	LineCarelInfoList lineList = new LineCarelInfoList(null, BaseConfig.getPlantId());
    	DeviceCarelInfoList deviceList = new DeviceCarelInfoList(null, BaseConfig.getPlantId());
    	Map registry = new HashMap();
    	registry.put("cfline",lineList);
    	deviceList.bind(registry);
    	PrintWriter iniPrinter = new PrintWriter(new BufferedWriter(new FileWriter(initFileName+ "." + INI, false)));
    	iniPrinter.write("[config]\n");
    	LineInfo line = null;
    	String comPort = null;
    	
		//PC-Gate bug fix - 1
		Properties pBug = new Properties();
		URL fileconf = ResourceLoader.fileFromResourcePath("careldll","driverRetry.properties");
		java.io.InputStream is = fileconf.openStream();
		pBug.load(is);
		String bugVal;
		//end PC-Gate bug fix - 1
		
    	for(int i = 0; i < lineList.size(); i++)
    	{
    		line = lineList.get(i);
    		iniPrinter.write("Line");
    		iniPrinter.write(String.valueOf(line.getCode()));	
    		iniPrinter.write("=");
    		comPort = line.getComport().substring(3);
    		iniPrinter.write(comPort);
    		iniPrinter.write(",");
    		iniPrinter.write(convertBaudRate(line.getBaudrate()));
    		iniPrinter.write(",");
    		iniPrinter.write(convertProtocol(line.getProtocol().substring(2)));
    		
    		//PC-Gate bug fix - 2
    		bugVal = pBug.getProperty(line.getComport());
    		if (bugVal !=null)
    				iniPrinter.write(","+bugVal);
    		//end PC-Gate bug fix - 2
    		
    		iniPrinter.write("\n");
    	}
    	
    	iniPrinter.write("\n");
    	iniPrinter.write("MaxRetry485Q=3");
    	iniPrinter.write("\n");
    	iniPrinter.write("MaxRetry485=8");
    	iniPrinter.write("\n");
    	iniPrinter.write("MaxNullWrite=40");
    	iniPrinter.write("\n");
    	
    	if(enableTymeSynch != null)
    	{
    		iniPrinter.write("EnableTimeSyncMsg="+enableTymeSynch);
    		iniPrinter.write("\n");
    	}
    	
    	iniPrinter.flush();
    	iniPrinter.close();
    	
    	//PC-Gate bug fix - 3
    	pBug=null;
    	is.close();
    	//end PC-Gate bug fix - 3
    	
    	PrintWriter cctPrinter = new PrintWriter(new BufferedWriter(new FileWriter(initFileName+ "." + CCT, false)));
    	DeviceInfo device = null;
    	for(int i = 0; i < deviceList.size(); i++)
    	{
    		device = deviceList.get(i);
    		int iddevice = device.getId();
    		if(checkIfSelect(iddevice,selectedDeviceid) == false)
    		{
    			continue;
    		}
    		cctPrinter.write(String.valueOf(device.getGlobalindex().intValue()));
    		cctPrinter.write(" ");
    		cctPrinter.write(String.valueOf(device.getLineInfo().getCode()));
    		cctPrinter.write(" ");
    		cctPrinter.write(String.valueOf(device.getAddress().intValue()));
    		cctPrinter.write("\n");
    	}
    	cctPrinter.flush();
    	cctPrinter.close();
    }
    private boolean checkIfSelect(int iddevice, int[] selectedDeviceid)
    {
    	for(int i=0;i<selectedDeviceid.length;i++)
    	{
    		if(iddevice == selectedDeviceid[i])
    		{
    			return true;
    		}
    	}
    	return false;
    }
    private String convertProtocol(String protocol)
    {
    	if (protocol.equals("485N"))
    		return "485_232";
    	else
    		return protocol;
    }
    
    private String convertBaudRate(int bound)
    {
    	if (1200 == bound)
    		return "1";
    	else if (2400 == bound)
    		return "2";
    	else if (4800 == bound)
    		return "3";
    	else if (9600 == bound)
    		return "4";
    	else if (19200 == bound)
    		return "5";
    	else
    		return "5";
    }
    
    public static void main(String[] argv) throws Throwable
    {
        BaseConfig.init();
    	DataConnCAREL d = (DataConnCAREL)FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAREL");
        d.writeProtocol();
        d.initDriver();
    }
    
//LDAC FOR NOVACOOP 12/07/2011 START
	
    public synchronized short retrieve(short globalindex, short address, short type, short varlength, short vardimension, short bitposition, short decimal, DoubleValue value)
    {
		//Non valutiamo la variabile offline che è tecnica e fa il giro degli allarmi

		short status = getUnitStatus(globalindex);

        /* Status:
         * 0   periferica online
         * 1   periferica offline
         * 999 Errore bloccante
         */
		if (0 != status)
		{
			value.setValue(Double.NaN);
			return 0; // asnwer as 'value not retrieved'
		}
        
        
        try
        {
				short returncode = 0;
				ShortValue val = new ShortValue();
				if(vardimension == 32 && varlength == 32 ) 
				{
					// 32 bits variable
					int nVal32 = 0;
					// receive 1st half of the number
					returncode = readDBValue(globalindex,address,type,val);
					if(returncode == 1) 
					{ // 1st half received successfully
						nVal32 |= val.getValue();
						nVal32 <<= 16;
						// receive 2nd half of the number
						returncode = readDBValue(globalindex, (short)(address + 1),type,val);
						if( returncode == 1 ) 
						{ // 2nd half received successfully
							nVal32 |= ((int)val.getValue()&0x0000FFFF);
							// update variable value only if both halves are available
							value.setValue((double)nVal32);
						}
						else
						{
							return 0; // answer as 'value not retrieved'

						}
							
					}
					else
					{
						return 0; // answer as 'value not retrieved'
					}
				}
				else 
				{
					// 16 bits variable
        			returncode = readDBValue(globalindex, address,  type, val);
					value.setValue(convert(val.getValue(), varlength,  vardimension,  bitposition,  decimal));
					if(returncode != 1)
					{
						// LoggerMgr.getLogger(this.getClass()).error("VALUE NOT RETRIEVED -- globalindex:"+globalindex+" address:"+address+" returncode:"+returncode);
						return 0; // answer as 'value not retrieved'
					}
					else
						return 1; // answer as 'value retrieved'
				}

		}
        catch (Throwable e)
        {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            return 0;
        }
        return 1; // answer as 'value retrieved'
    }
    
	
	public double convert(short value, short varlength, short vardimension, short bitposition, short decimal)
    {
        long val = value;

        if (varlength != vardimension)
        {
            // Devo tagliare il valore
            val = BitManipulation.extractNumberCarel(value, bitposition,varlength);
        }
        
        double decimalv = pow(decimal);
        return (double) (val / decimalv);
    }
	
	//LDAC FOR NOVACOOP 12/07/2011 END
	
}
