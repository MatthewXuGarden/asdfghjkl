package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.FieldConnectorMgr;
import com.carel.supervisor.field.dataconn.DriverReturnCode;
import com.carel.supervisor.field.dataconn.impl.DataConnCAREL;
import com.carel.supervisor.field.types.PER_INFO;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.bo.BSystem;
import com.carel.supervisor.director.DirectorMgr;

import java.io.*;
import java.util.*;


public class DeviceDetectBean {
	
	private Logger log = LoggerMgr.getLogger(this.getClass());
	private static String DRIVER_PATH		= "C:\\Windows\\System32\\"; 
	private static String INIT_NAME			= "DRVMNG_AUTODETECT";
	private static String CCT_EXT			= ".CCT"; 
	private static String INI_EXT			= ".INI";
	private static short BEGIN_ADDR			= 1;
	private static short END_ADDR			= 207;
	private static String APP_CODE			= "hdversion";
	// suervisor flags
	private static int NO_SUPERVISOR_FLAGS	= 0x00;
	private static int DEVICE_DETECTION_FLAG= 0x01;
	// time constants
	private static int STARTUP_DELAY		= 20000;// startup delay in ms
	private static int WAIT_FOR_RESPONSE	= 100;	// wait for response time constant in ms
	private static int RETRY_NO				= 3;	// no of iterations through address space
	
	protected Vector<Record> deviceCatalog	= new Vector<Record>();
	protected short devices[]				= new short[BEGIN_ADDR + END_ADDR];

	private LangService langService			= null;
	private int recordCounter				= 0; // used to identify ambiguous detection
	
	private UserSession session				= null;
	
	private static Object objDetectionLock	= new Object();
	private static boolean bDetection		= false;
	
	
	public DeviceDetectBean(UserSession us) throws DataBaseException
	{
		DRIVER_PATH = BaseConfig.getCarelPath();
		
		session = us;
		int idsite = us.getIdSite();
		String language = us.getLanguage();
		
		String sql = "select cfdevmdl.iddevmdl, cfdevmdl.hdversion, cftableext.description"
		+ " from cfdevmdl inner join cftableext on (cftableext.tableid = cfdevmdl.iddevmdl"
		+ " and cftableext.idsite=? and cftableext.languagecode=? and cftableext.tablename='cfdevmdl')"
		+ " where cfdevmdl.iddevmdl not in (select iddevmdl from cfdevmdlext) and not cfdevmdl.hdversion is null;";
		Object[] param = new Object[2];
		param[0] = new Integer(idsite);
		param[1] = language;
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
        for(int i = 0; i < rs.size(); i++) {
        	Record r = rs.get(i);
        	if( r != null ) {
        		String strAppCode = r.get(APP_CODE).toString();
        		try {
        			short nAppCode = Short.parseShort(strAppCode);
        			// put record at the proper position
        			int j = 0;
        			for(; j < deviceCatalog.size(); j++) {
        				Record rCat = deviceCatalog.get(j);
        				short nCatCode = Short.parseShort(rCat.get(APP_CODE).toString());
        				if( nAppCode < nCatCode ) {
        					deviceCatalog.insertElementAt(r, j);
        					break;
        				}
        			}
        			if( j == deviceCatalog.size() )
        				deviceCatalog.add(r);
        		} catch(NumberFormatException e) {
        			// ignore record
        		}
        	}
        }

        langService = LangMgr.getInstance().getLangService(language);
	}
	
	
	public Map<Integer, DeviceBean> getDevices()
	{
		Map<Integer, DeviceBean> map = new HashMap<Integer, DeviceBean>();
		for(short i = BEGIN_ADDR; i <= END_ADDR; i++) {
			if( devices[i] > 0 ) {
				DeviceBean device = new DeviceBean();
				device.setAddress(i);
				device.setIddevice(-1);

				Record r = getRecord(devices[i]);
				switch( recordCounter ) {
					case 1:
						device.setIddevmdl(Integer.parseInt(r.get("iddevmdl").toString()));
						device.setDescription(r.get("description").toString() + " - " + i);
						break;
					case 0:
						// unrecognized device
						device.setIddevmdl(0);
						device.setDescription(langService.getString("siteview", "unrecognized") + " [" + devices[i] + "]");
						break;
					default:
						// ambiguous device detection
						device.setIddevmdl(-devices[i]);
						device.setDescription(langService.getString("siteview", "ambiguous") + " [" + devices[i] + "]");
				}
				map.put(new Integer(i), device);
			}
		}
		return map;
	}
	
	
	public Vector<Short> getAppCodes(int iddevmdl)
	{
		Vector<Short> appCodes = new Vector<Short>();
		for(int i = 0; i < deviceCatalog.size(); i++) {
			Record r = deviceCatalog.get(i);
			if( Integer.parseInt(r.get("iddevmdl").toString()) == iddevmdl )
				appCodes.add(Short.parseShort(r.get(APP_CODE).toString()));
		}
		return appCodes;
	}
	
	
	public void initDeviceDetection(int portNo, int baudRate, String protocol)
		throws Exception
	{
		// only one detection instance allowed
		synchronized(objDetectionLock) {
			if( bDetection )
				return;
			else
				bDetection = true;
		}
		
		if( !writeDetectionProtocol(portNo, baudRate, protocol) )
			return;

		// stop engine
		BSystem.stopPvEngine(session);
		
		// re-init driver
		DataConnCAREL carel = (DataConnCAREL)FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAREL");
		DriverReturnCode drvRetCode = carel.closeDriver();
		int nTryInit = 10;
		do {
			try {
				Thread.sleep(100);
			}
			catch(Exception e) {
			}
			drvRetCode = carel.initDriver(DRIVER_PATH + INIT_NAME);
		} while( drvRetCode.getReturnCode() != 0 && --nTryInit > 0 );
		if( drvRetCode.getReturnCode() == 0 ) {
			log.info("++++++ initiate device autodetection procedure");
			
			// put driver in device detection mode
			carel.setFlags(DEVICE_DETECTION_FLAG);
			// force a short offline time
			carel.setOffLineTime(5000);
			// wait for driver device fetching
			try {
				log.info("+++ Waiting "+(STARTUP_DELAY/1000)+" sec for the DLL to catch up with the devices...");
				Thread.sleep(STARTUP_DELAY);				
			}
			catch(Exception e) {
			}
			// detection sequence
			for( int k = 0; k < RETRY_NO; k++) {
				log.info("+++ Cycle #"+k);
				for(short i = BEGIN_ADDR; i <= END_ADDR; i++) {
					if( k == 0 )
						devices[i] = 0;
					try {
						Thread.sleep(WAIT_FOR_RESPONSE);
					}
					catch(Exception e) {
					}
					if( devices[i] == 0 ) {
						PER_INFO info		= carel.getPeriphericalInfo(i);
						short retCode		= info.getReturnCode();
						//short cntNoAnswer	= info.getNoAnswerCnt();
						short appCode		= info.getApplicCode();
						devices[i]			= appCode;
						log.info("+retCode="+retCode+" appCode["+i+"]="+appCode);
					}
				}
			}
			// back to normal (vars are restored anyway on driver init)
			carel.setFlags(NO_SUPERVISOR_FLAGS);
			// restore default offline time
			carel.setOffLineTime(90000);
			drvRetCode = carel.closeDriver();
			
			log.info("+++ Device detection ended. Restarting the engine...");
		}
		
		synchronized(objDetectionLock) {
			bDetection = false;
		}

		// restart engine
		DirectorMgr.getInstance().mustCreateProtocolFile();
		BSystem.startPvEngine(session);
	}
	
	
	protected Record getRecord(short appCode)
	{
		recordCounter = 0;
		Record retRecord = null;
		
		for(int i = 0; i < deviceCatalog.size(); i++) {
			Record r = deviceCatalog.get(i);
			short rAppCode = Short.parseShort(r.get(APP_CODE).toString());
			if( rAppCode == appCode ) {
				retRecord = r;
				recordCounter++;
			}
			else if( rAppCode > appCode )
				break;
		}
		
		return retRecord;
	}

	
	protected boolean writeDetectionProtocol(int portNo, int baudRate, String protocol)
	{
		boolean bRet = true;
		FileWriter fw = null;
		
		// cct file
		try {
			fw = new FileWriter(DRIVER_PATH + INIT_NAME + CCT_EXT);
			for(short i = BEGIN_ADDR; i <= END_ADDR; i++) {
				String deviceRow = i + " 1 " + i + "\r\n";
				fw.write(deviceRow);
			}
			fw.close();
		}
		catch(IOException e) {
			bRet = false;
			// log error
            LoggerMgr.getLogger(this.getClass()).error("I/O Error on file " + DRIVER_PATH + INIT_NAME + CCT_EXT);
		}
		
		// ini file
		try {
			fw = new FileWriter(DRIVER_PATH + INIT_NAME + INI_EXT);
			fw.write("[config]\r\n");
			String lineRow = "LINE1=" + portNo
				+ "," + getBaudRateNdx(baudRate)
				+ "," + getProtocol(protocol) 
				+ "\r\n\r\n";
			fw.write(lineRow);
	    	//fw.write("MaxRetry485Q=3\r\n");
	    	//fw.write("MaxRetry485=8\r\n");
	    	//fw.write("MaxNullWrite=40\r\n");
			fw.close();
		}
		catch(IOException e) {
			bRet = false;
			// log error
            LoggerMgr.getLogger(this.getClass()).error("I/O Error on file " + DRIVER_PATH + INIT_NAME + INI_EXT);
		}
		
		return bRet;
	}
	
	
	private int getBaudRateNdx(int baudRate)
	{
		int ndx = 0;
		for(int i = 1200; i <= baudRate && i <= 19200; i += i)
			ndx++;
		return ndx;
	}
	
	
	private String getProtocol(String protocol)
	{
		if( protocol.equals("CAREL_RS485N") )
			return "485_232";
		else
			return "485";
	}
	
	
	public static boolean isDetection()
	{
		synchronized(objDetectionLock) {
			return bDetection;
		}
	}
}
