package com.carel.supervisor.field.dataconn.impl;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.io.SocketComm;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.field.FieldConnectorMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.DriverReturnCode;

public class DataConnFTD2IO extends DataConnBase {
	
	private Boolean initialized = false;

	private int port = 1980;
	
	// command strings for 'Internal IO' access
	// managed by infoService
	private String LOAD_DLL_CMD ="MIO;0";
	private String OPEN_CMD ="MIO;1";
	private String CLOSE_CMD ="MIO;2";
	private String READ_CMD ="MIO;3";
	private String WRITE_CMD ="MIO;4";
	private String WRITE_NOSAVERELAYSTATUS_CMD ="MIO;44";
	private String INIT_CMD ="MIO;10";
	
	private static final String LOG_LVL ="logLvl";
	private static final String NUM_RET ="numRetry";
	private static final String READ_DELAY ="readDelay";
	private static final String VALUE = "value";
	
	private String RESULT_OK ="0";
	
	private String logLevel = "0";
	private String numRetry = "2";
	private String readDelay = "100";
	
	public DataConnFTD2IO() {
		initialized = false;
	}

	public void init(XMLNode xmlStatic) throws InvalidConfigurationException {
		
		try
		{
			XMLNode[] n = xmlStatic.getNodes();
	    	for (int i = 0; i < n.length; i++) {
	    		if(n[i].getAttribute("name").equalsIgnoreCase(LOG_LVL)){
	    			logLevel = n[i].getAttribute(VALUE);
	    		}	
    			
	    		if(n[i].getAttribute("name").equalsIgnoreCase(NUM_RET)){
	    			numRetry = n[i].getAttribute(VALUE);
	    		}		
    			
	    		if(n[i].getAttribute("name").equalsIgnoreCase(READ_DELAY)){
	    			readDelay = n[i].getAttribute(VALUE);
	    		}
			}
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error("Error on init FTDIO dll: "+e);
		}
	}

	// NOTE:
	// - variable.isBlockingError is used only within DataConnXXX class (for all drivers)
	// - DriverReturnCode is managed only on DeviceDetectBean class (autodetect of devices --> Carel protocol)

	public DriverReturnCode loadDllDriver() {

		String result = "";

		try {
			
			result = SocketComm.sendCommand("localhost", port, LOAD_DLL_CMD);
			
			if (!result.equalsIgnoreCase(RESULT_OK)) {
				// set blocking error implies not read/write from/to device if error is set. 
				// It is used also on 'ping device' function begins to DataConnBase class
				setBlockingError();

				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error("Error during 'FTD2IOdll' loading");

				// new message on cfmessage table
				// F008: 'Shutdown error during loading of driver {0}' / 'Errore bloccante durante il caricamento del driver {0}'
				EventMgr.getInstance().log(new Integer(1), "Field-FTD2IO",
						"Connection", EventDictionary.TYPE_ERROR, "F008",
						getName());

				return new DriverReturnCode((short) -1);
			}

			return new DriverReturnCode((short) 0);
			
		} catch (Throwable e) {
			// set blocking error implies not read/write from/to device if error is set. 
			// It is used also on 'ping device' function begins to DataConnBase class
			setBlockingError();

			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
			
			// new message on cfmessage table
			// F008: 'Shutdown error during loading of driver {0}' / 'Errore bloccante durante il caricamento del driver {0}'
			EventMgr.getInstance().log(new Integer(1), "Field-FTD2IO", "Connection",
							EventDictionary.TYPE_ERROR, "F008", getName());

			return new DriverReturnCode(e);
		}
	}

	// only initDriver method without configuration file path
	public DriverReturnCode initDriver() {

		String result = "";

		
		// Correctly manage the FTD2IO communication in case of DEMO version 
		// At every PVPROService start the initialization phase for the FTD2IO channel 
		// is ignored, and the 'initialized' flag (device online/offline) is put to 'false'
		if(BaseConfig.isDemo())
		{
			initialized = false;
			
			return new DriverReturnCode((short)-1, "", true);
		}
		
		try {	
	    		result = SocketComm.sendCommand("localhost", port, INIT_CMD + ";" + logLevel + ";" + numRetry + ";" + readDelay);
			
				result = SocketComm.sendCommand("localhost", port, OPEN_CMD);
	
				if (result.equalsIgnoreCase(RESULT_OK)) {
					initialized = true;
			}

			short retCode = (short) Integer.parseInt(result);

			return new DriverReturnCode(retCode, "", true);

		} catch (Throwable e) {
			
			// set blocking error implies not read/write from/to device if error is set. 
			// It is used also on 'ping device' function begins to DataConnBase class
			setBlockingError();

			// F004: 'Error during driver initialization: [{0}]'
			EventMgr.getInstance().log(new Integer(1), "Field-FTD2IO", "Connection",EventDictionary.TYPE_ERROR, "F004", getName());

			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);

			return new DriverReturnCode(e);
		}
	}

	public DriverReturnCode closeDriver() {
		
		String result = "";
		
		try {
			if (initialized) {
				
				result = SocketComm.sendCommand("localhost", port, CLOSE_CMD);
				
				short retCode = (short) Integer.parseInt(result);
				
				DriverReturnCode d = new DriverReturnCode(retCode);

				if (d.getReturnCode() != 0) {
					// F009: 'Exception on dll {0}: {1}'
					EventMgr.getInstance().log(new Integer(1), "Field-FTD2IO","Connection", EventDictionary.TYPE_ERROR, "F009",
							new String[] { getName(), "closeDriver" });
					LoggerMgr.getLogger(this.getClass()).error("Exception in FTD2IOdll: closeDriver");
				}

				return d;
			}

			else
				return new DriverReturnCode((short) -1);

		} 
		catch (Throwable e) {
			return new DriverReturnCode(e);
		}
	}

	public void retrieve(Variable variable) {
		String msg = null;
		String result = "";

		try {
			// TODO: check if 'offline' variable and offline management is necessary
			if (0 == variable.getInfo().getAddressIn().intValue()) // This section manages 'OFFLINE' variable
			{
				if (variable.isDeviceOffLine()) {
					variable.setValue(new Float(1));
					// 'Offline' variable is an alarm.
					// If 'offline' is active, than it is saved. Otherwise it is  managed as a logic variable
					DataConnBase.saveOfflineGuardian(variable);
				} else {
					variable.setValue(new Float(0));
				}
			} else // if any other variable (not OFFLINE)
			{
				if (!variable.isDeviceOffLine()) {
					VariableInfo variableInfo = variable.getInfo();
					
					result = SocketComm.sendCommand("localhost", port, READ_CMD+";"+variableInfo.getAddressIn().toString());

					short retCode = (short) Integer.parseInt(result);
					
					switch (retCode) {

					case -4: // read error
						variable.activeBlockingError();
						// FLDE1011: 'Error during field acquisition'
						msg = CoreMessages.format("FLDE1011");

						break;

					case -5: // wrong variable address
						variable.activeBlockingError();
						// FLDE1026: 'VarIndex out of range'
						msg = CoreMessages.format("FLDE1026");
						break;

					default: // read ok
						variable.setValue((float) retCode);
					}

					if (variable.isBlockingError()) {
						// F005: 'Error while retrieving the variable. Driver: {0}'
						EventMgr.getInstance().log(new Integer(1), "Field-FTD2IO", "Acquisition", EventDictionary.TYPE_ERROR, "F005", getName());

						Logger logger = LoggerMgr.getLogger(this.getClass());

						// FLDE0010: 'Retrieve error: [MSG:{0}] [GLOBALINDEX:{1}] [ADDRESS:{2}] [TYPE:{3}]'
						logger.error(CoreMessages.format("FLDE0010",new Object[] {msg,String.valueOf(variableInfo.getGlobalIndex()),
														String.valueOf(variableInfo.getAddressIn().intValue()),
														String.valueOf(variableInfo.getVarTypeForAcquisition())}));
					}
				} else // device is offline
				{
					variable.setValue(null);
				}
			}
		} catch (Throwable e) {
			setBlockingError();
			EventMgr.getInstance().log(new Integer(1), "Field-FTD2IO",
					"Acquisition", EventDictionary.TYPE_ERROR, "F005",
					getName());

			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}

	public int setOnField(Variable variable)
	{
		return setOnField(variable,true);
	}
	public int setOnField(Variable variable,boolean saveRelayStatus) {
		String msg = null;
		String result = "";

		try {
			
			VariableInfo variableInfo = variable.getInfo();

			variable.setValue(new Float(variable.getCurrentValue()));

			if(saveRelayStatus)
				result = SocketComm.sendCommand("localhost", port, WRITE_CMD+";"+Integer.toString((int)variable.getCurrentValue())+";"+variableInfo.getAddressIn().toString());
			else
				result = SocketComm.sendCommand("localhost", port, WRITE_NOSAVERELAYSTATUS_CMD+";"+Integer.toString((int)variable.getCurrentValue())+";"+variableInfo.getAddressIn().toString());
			
			short retCode = (short) Integer.parseInt(result);

			switch (retCode) {
			case 0: // write ok
				break;

			case -3: // write error
				variable.activeBlockingError();
				// FLDE1034: 'Error during field set'
				msg = CoreMessages.format("FLDE1034");

				break;

			case -5: // wrong variable address
				variable.activeBlockingError();
				// FLDE1026: 'VarIndex out of range'
				msg = CoreMessages.format("FLDE1026");
				break;

			default: // this is executed only if dll returns unknown error codes
				variable.activeBlockingError();
				// FLDE1033: 'Unknown error'
				msg = CoreMessages.format("FLDE1033");
			}

			if (variable.isBlockingError()) {
				EventMgr.getInstance().log(new Integer(1), "Field-FTD2IO", "Acquisition", EventDictionary.TYPE_ERROR, "F006",
						getName() + " " + msg);

				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(CoreMessages.format("FLDE0010",
						new Object[] {msg, String.valueOf(variableInfo.getGlobalIndex()), String.valueOf(variableInfo.getAddressIn().intValue()),
								String.valueOf(variableInfo.getVarTypeForAcquisition()) }));
			}
			return retCode;
		}
		catch (Throwable e) 
		{
			setBlockingError();
			EventMgr.getInstance().log(new Integer(1), "Field-FTD2IO", "Acquisition", EventDictionary.TYPE_ERROR, "F006", getName());

			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
			return DataConnBase.BLOCKING_ERROR;
		}
	}

	// this method is used to know the online-offline status of a device.
	// Device is shown 'offline' only when dll initialization fails 
	public boolean getDeviceStatus(DeviceInfo deviceInfo) {
		if(initialized)
			return true;
		else
			return false;
	}

	// for test purposes
	public static void main(String[] argv) throws Throwable {
		BaseConfig.init();
		DataConnFTD2IO d = (DataConnFTD2IO) FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("INTERNAL");
		d.initDriver();
	}
}
