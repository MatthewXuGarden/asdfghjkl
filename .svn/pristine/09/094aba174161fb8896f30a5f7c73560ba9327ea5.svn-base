package com.carel.supervisor.field.dataconn.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.DriverReturnCode;
import com.carel.supervisor.field.types.ShortValue;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.LineBean;
import com.carel.supervisor.presentation.bean.LineBeanList;

public class DataConnSNMP extends DataConnBase {
	
	private static final short STATUS_ERROR = 999;
	private static final short STATUS_ONLINE = 0;
	private static final boolean VALUE_OFFLINE = false;
	private static final String LAN = "LAN";
	
	private static final Integer POLLVALUE = 30;
	private Integer pollvalue = POLLVALUE;
	
	protected static final Integer DEBUG_LEVEL_OFF = 0;//0 for no logging
	protected static final Integer DEBUG_LEVEL_ERROR = 1;// 1 for error logging
	protected static final Integer DEBUG_LEVEL_INFO = 2;// 2 for error + info logging
	protected static final Integer DEBUG_LEVEL_VERBOSE = 3;// 3 for verbose logging
	
//	private static int counter = 0;
//	private static int initcounter = 0;
//	private static int readcounter = 0;
//	private static int writecounter = 0;
	
	private Map<Integer, Object[]> ttlmap;
	private boolean debug;
	private Logger logger;

	private static final int MAXTTLSIZE = 1000;
	private static final int MAXTTLSIZE_1 = (int)(MAXTTLSIZE*1.3);
	private static final long TTL = 4999l;
	private static final String POLLING = "pollvalue";
	private static final String VALUE = "value";

	public DataConnSNMP() {
//		System.out.println(this.getClass()+"_"+(DataConnSNMP.counter++));
		ttlmap = (Map<Integer, Object[]>) Collections.synchronizedMap(new LinkedHashMap<Integer, Object[]>(MAXTTLSIZE_1));
	}

    public void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
    	logger = LoggerMgr.getLogger(this.getClass());
    	XMLNode[] n = xmlStatic.getNodes();
    	String polling = null;
    	String tmpdebug = null;
    	for (int i = 0; i < n.length; i++) {
    		if(n[i].getAttribute("name").equalsIgnoreCase(POLLING)){
    			polling = n[i].getAttribute(VALUE);
    		}
    		if(n[i].getAttribute("name").equalsIgnoreCase("debug")){
    			tmpdebug = n[i].getAttribute(VALUE);
    		}
		}
    	if(polling==null){
    		pollvalue = POLLVALUE;
    	} else {
    		try{
    			pollvalue = Integer.parseInt(polling);
    		}catch (Exception e) {
    			pollvalue = POLLVALUE;
			}
    	}
    	if(tmpdebug!=null && tmpdebug.equalsIgnoreCase("true")){
    		debug = true;
    	} else {
    		debug = false;
    	}
    }

    private native short loadDll();
    private native short loadConfig(String xmlconfigstr);
    private native short getUnitStatus(short line, short address);
    private native short read(int line, int serial, int type, int address, ShortValue value);
    private native short write(int line, int serial, int type, int address, short value);
    private native short unloadConfig();
    private native short unloadDll();
    private native void debugLog(int level);

    public DriverReturnCode closeDriver() {
        try {
//        	logger.info("start");
			DriverReturnCode d = new DriverReturnCode(unloadConfig());
//        	logger.info("after unloadconf");
//        	logger.info("before pause");
        	try {
				Thread.sleep(5000l);
			} catch (Exception e) {}
//        	logger.info("after pause");
			if (1 == d.getReturnCode()) {
	        	logger.info("unloadconf error");
				EventMgr.getInstance().log(new Integer(1), "Field-snmp", "Connection", EventDictionary.TYPE_ERROR, "F501", getName());
				LoggerMgr.getLogger(this.getClass()).error("Exception in dll: locDrvClose");
			}
//        	logger.info("before unloaddll");
			short ret = unloadDll();
//        	logger.info("after unloaddll");
			if (ret!=0) {
	        	logger.info("unloaddll error");
				EventMgr.getInstance().log(new Integer(1), "Field-snmp", "Connection", EventDictionary.TYPE_ERROR, "F502", getName());
				logger.error("Probably exception occurred in dllsnmp: freeDll");
			}
//        	logger.info("end");
			return d;
		} catch (Throwable e) {
//        	logger.info("end-error");
			return new DriverReturnCode(e);
		}
	}

	public boolean getDeviceStatus(DeviceInfo deviceInfo) {
        try {
			short status = getUnitStatus((short) deviceInfo.getLine().intValue(), (short) deviceInfo.getAddress().intValue());
			if (status == STATUS_ERROR) {
				setBlockingError();
				LoggerMgr.getLogger(this.getClass()).error("Exception in dllsnmp: getUnitStatus");
			}
			// Status: 0 periferica online 1 periferica offline
			return (STATUS_ONLINE == status);
		} catch (Throwable e) {
			setBlockingError();
			EventMgr.getInstance().log(new Integer(1), "Field-snmp", "Acquisition", EventDictionary.TYPE_ERROR, "F007", getName());
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
			return VALUE_OFFLINE;
		}
	}

	public DriverReturnCode initDriver() {
		System.out.println("Init"+this.getClass().getName());
//		unloadDll();
		loadDll();
		debugLog(DEBUG_LEVEL_VERBOSE);
//		unloadConfig();
		String configxml = buildConfigXml();
		ttlmap = (Map<Integer, Object[]>) Collections.synchronizedMap(new LinkedHashMap<Integer, Object[]>(MAXTTLSIZE_1));
		return new DriverReturnCode(loadConfig(configxml));
	}

	private String buildConfigXml() {
		//<snmpdev ip="192.168.100.1" lineid="1" poll="5" type="webgate">
		//<pvdev addr="1">
		//<var type="1" idx="1"/>
		//<var type="1" idx="2"/>
		//</pvdev>
		//</snmpdev>
		//</devices>";
		StringBuilder toret = new StringBuilder();
		toret.append("<devices>");
		LineBean[] lines;
		try {
			lines = new LineBeanList().retrieveLines(1);
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return null;
		}
		if(lines.length==0) {
			return null;
		}
		int variables = 0;
		for(int k = 0;k < lines.length;k++){
			if(!lines[k].getProtocol().startsWith(LAN)){
				continue;
			}
			String rcommunity = "public";
			String rwcommunity = "public";
			String linetype = "webgate";
			try{
				if(lines[k].getProtocol().lastIndexOf("WEBGATE")!=-1){
					linetype = "webgate";
					rcommunity = "public";
					rwcommunity = "public";
				}else if(lines[k].getProtocol().lastIndexOf("PCOWEB")!=-1){
					linetype = "pcoweb";
					rcommunity = "public";
					rwcommunity = "carel";
				}
			}catch(Exception e){
				rcommunity = "public";
				rwcommunity= "public";
			}
//			System.out.print("line ip: "+lines[k].getComport()+" line id: "+lines[k].getIdline());
			toret.append("<snmpdev ip=\""+lines[k].getComport()+
					"\" lineid=\""+lines[k].getIdline()+"\" poll=\""+pollvalue+
					"\" type=\""+linetype+"\" rcommunity=\""+rcommunity+"\" rwcommunity=\""+rwcommunity+"\">");
			LinkedList<DeviceBean> devicelist = DeviceListBean.getDevicesByLine(1, lines[k].getIdline());
//			System.out.print("    devices: "+devicelist.size());
			for (Iterator<DeviceBean> iterator = devicelist.iterator(); iterator.hasNext();) {
				DeviceBean deviceBean = iterator.next();
				toret.append("<pvdev addr=\""+deviceBean.getAddress()+"\">");
				RecordSet variablelist = VarphyBeanList.retrieveVarByDevice(1, deviceBean.getIddevice(), false);
				variables+=variablelist.size();
				for (int kk=0;kk<variablelist.size();kk++) {
					Record r = variablelist.get(kk);
					if((Integer)r.get("addressin")==0) continue;
					toret.append("<var type=\"");
					Integer type = (Integer)r.get("type"); 
					toret.append(type==4?1:type);
					toret.append("\" idx=\"");
					toret.append((Integer)r.get("addressin"));
					toret.append("\" />");					
				}
				toret.append("</pvdev>");
			}
//			System.out.println("        line vars: "+variables);
			variables=0;
			toret.append("</snmpdev>");
		}
		toret.append("</devices>");
		//TODO - DEBUG remove
		try {
			FileWriter fw;
			fw = new FileWriter("snmpconfiguration.xml");
			fw.write(toret.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TODO - DEBUG remove
		return toret.toString();
	}

	public DriverReturnCode loadDllDriver() {
        try {
			System.loadLibrary("SNMPDataConnector");
			return new DriverReturnCode((short) 0);
		} catch (Throwable e) {
			setBlockingError();
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
			EventMgr.getInstance().log(new Integer(1), "Field-snmp", "Connection", EventDictionary.TYPE_ERROR, "F003", getName());
			return new DriverReturnCode(e);
		}
	}

	public int setOnField(Variable variable) {
        String msg = null;
		try {
			VariableInfo variableInfo = variable.getInfo();
			short newValue = (short) valueToDriver(variableInfo, variable);
			variable.setValue(variable.getCurrentValue());
//            System.out.println("w "+variableInfo.getId()+" "+(writecounter++));
			
			logger.info("setOnField: " + variableInfo.getDeviceInfo().getAddress() + ", " + variableInfo.getType() + ", " + variableInfo.getAddressIn().intValue() + ", " + newValue);
			
			Short returnCode = write(variableInfo.getDeviceInfo().getLine(),
					variableInfo.getDeviceInfo().getAddress(),
					(short) variableInfo.getVarTypeForAcquisition(),
					(short) variableInfo.getAddressIn().intValue(),
					newValue);
			// TODO manage return codes
			switch (returnCode) {
			case 0:
			case 1:
				break;
			case 999:
				variable.activeBlockingError();
				LoggerMgr.getLogger(this.getClass()).error("Exception in dllsnmp: sendValue");
				break;
			default: // non dovrebbe mai passare di qui
				variable.activeBlockingError();
				msg = CoreMessages.format("FLDE1033");
			}
			if (variable.isBlockingError()) {
				EventMgr.getInstance().log(new Integer(1), "Field-snmp", "Acquisition", EventDictionary.TYPE_ERROR, "F006", getName() + " " + msg);
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(CoreMessages.format("FLDE0010",
					new Object[] { msg,
						String.valueOf(variableInfo.getGlobalIndex()),
						String.valueOf(variableInfo.getAddressIn().intValue()),
						String.valueOf(variableInfo.getVarTypeForAcquisition()) }));
			}
			return returnCode;
		} catch (Throwable e) {
			setBlockingError();
			EventMgr.getInstance().log(new Integer(1), "Field-snmp", "Acquisition",EventDictionary.TYPE_ERROR, "F006", getName());
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
			return DataConnBase.BLOCKING_ERROR;
		}
	}

	public void retrieve(Variable variable) {
        try {
			if (0 == variable.getInfo().getAddressIn().intValue()) { // Variabile 0 corrispondente all'OFFLINE
				if (variable.isDeviceOffLine()) {
					variable.setValue(new Float(1));
					// L'offline � un allarme.
					// Se l'allarme � attivo allora lo salvo, altrimenti appartiene al giro delle logiche
					// E pertanto viene gi� eventualmente loggate dalle logiche nel Director
					DataConnBase.saveOfflineGuardian(variable);
				} else {
					variable.setValue(new Float(0));
				}
			} else {
				if (!variable.isDeviceOffLine()) {
					try {
						ShortValue shortValue = new ShortValue();
	                    VariableInfo variableInfo = variable.getInfo();
	                    
	                    Object[] ttlpair = ttlmap.get(variableInfo.getId());
	                    if(ttlpair==null){
	                    	ttlpair = new Object[]{-1l, null};
	                    	ttlmap.put(variableInfo.getId(), ttlpair);
	                    }
	                    
	                    Long ttl = (Long)ttlpair[0];
	                    Short ttlvalue = (Short)ttlpair[1];
	                    
	                    short returncode=1;
	                    //TODO storico/haccp sempre in dll, tanto c'� la cache 
	                    long now = System.currentTimeMillis();
	                    if(now-ttl>=0 && now-ttl<TTL && ttlvalue!=null){
//	                    	System.out.println("          t "+(now%3600000)+"."+(ttl%3600000)+".    "+variableInfo.getId()+" "+(now-ttl));
	                    	/*
	                    	if(debug){
	                    		logger.info("hit "+variableInfo.getId());
	                    	}
	                    	*/
	                    	returncode = 0;
	                    	shortValue.setValue(ttlvalue);
	                    } else {
	                    	/*
	                    	if(debug){
	                    		logger.info("miss "+variableInfo.getId());
	                    	}
	                    	*/
//	                    	System.out.println("r "+(now%3600000)+"."+(ttl%3600000)+".    "+variableInfo.getId()+" "+(readcounter++));
	                    	returncode = read(variableInfo.getDeviceInfo().getLine(),
								variableInfo.getDeviceInfo().getAddress(),
								(short) variableInfo.getVarTypeForAcquisition(),
	                            (short) variableInfo.getAddressIn().intValue(), shortValue);
	                    	ttl = now;
	                    	ttlvalue = shortValue.getValue();
	                    	ttlmap.put(variableInfo.getId(), new Object[]{ttl,ttlvalue});
	                    }                    
	                    short value = shortValue.getValue();
	
	                    valueFromDriver(variableInfo, variable, value);
	
	                    switch (returncode) {
		                    case -44:
							case 1:
								variable.setValue(null); // Not yet acquired
								break;
							case 0:
								DataConnBase.saveAlarmGuardian(variable);
								break;
							case 999:
								variable.activeBlockingError();
								LoggerMgr.getLogger(this.getClass()).error("Exception in dll: readDBValue");
								break;
							default: // non dovrebbe mai passare di qui
								variable.activeBlockingError();
								LoggerMgr.getLogger(this.getClass()).error("Exception in dll: readDBValue unknown");
	                    }
	                    if(ttlmap.size()>MAXTTLSIZE){
	                    	if(debug){
	                    		logger.info("maintenance "+variableInfo.getId());
	                    	}
	                    	//maintenance
	                    	try{
//	                    		long start = System.currentTimeMillis();
		                    	Iterator<Integer> itr = ttlmap.keySet().iterator();
//		                    	Random r = new Random();
		                    	for(int kk = 0;kk<MAXTTLSIZE*0.4;kk++){
		                    		itr.next();
		                    		itr.remove();
//		                    		Integer i = 
//		                    		itr.next();
//		                    		if(r.nextBoolean()) {
//		                    			itr.remove();
//		                    			i = 
//		                    			itr.next();
//		                    		}
//		                    		if(r.nextBoolean()) {
//		                    			itr.remove();
//		                    			i = 
//		                    			itr.next();
//		                    		}
//		                    		//ttlmap.remove(i);
//		                    		itr.remove();
		                    	}
//		                    	LoggerMgr.getLogger(this.getClass()).info("Tttlmap resize: "+(System.currentTimeMillis()-start));
	                    	}catch (Exception e) {
	                    		LoggerMgr.getLogger(this.getClass()).error(e);
							}
	                    }
					} catch (Exception e) {
						setBlockingError();
						EventMgr.getInstance().log(new Integer(1), "Field-snmp", "Acquisition", EventDictionary.TYPE_ERROR, "F005", getName());
						LoggerMgr.getLogger(this.getClass()).error(e);
					}
				} else {//device is offline
	                variable.setValue(null);
	            }
			}
        } catch (Throwable e) {
        	setBlockingError();
            EventMgr.getInstance().log(new Integer(1), "Field-snmp", "Acquisition", EventDictionary.TYPE_ERROR, "F005", getName());
            LoggerMgr.getLogger(this.getClass()).error(e);
        }

	}
}
