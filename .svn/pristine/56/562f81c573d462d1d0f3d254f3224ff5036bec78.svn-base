package com.carel.supervisor.field.dataconn.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.math.BitManipulation;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.varaggregator.VarAggregator;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.DriverReturnCode;
import com.carel.supervisor.field.dataconn.impl.modbus.ModbusSettings;
import com.carel.supervisor.field.dataconn.impl.modbus.ModbusSettingsMap;
import com.carel.supervisor.field.modbusfunmgrs.DeviceFunctionMapper;
import com.carel.supervisor.field.modbusfunmgrs.IFunctionMgr;
import com.carel.supervisor.field.types.DoubleValue;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.LineBean;
import com.carel.supervisor.presentation.bean.LineBeanList;
import com.carel.supervisor.presentation.bean.ModbusExtensionModelBean;
import com.carel.supervisor.presentation.bean.ModbusExtensionModelBeanList;
import com.carel.supervisor.base.config.ProductInfoMgr;

public class DataConnMODBUS extends DataConnBase {

	private static final short STATUS_ERROR = 999;
	private static final short STATUS_ONLINE = 0;
	private static final boolean VALUE_OFFLINE = false;

	protected static final Integer DEBUG_LEVEL_OFF = 0;			// 0 for no logging
	protected static final Integer DEBUG_LEVEL_ERROR = 1;		// 1 for error logging
	protected static final Integer DEBUG_LEVEL_INFO = 2;		// 2 for error + info logging
	protected static final Integer DEBUG_LEVEL_VERBOSE = 3;		// 3 for verbose logging

	private static final String MODBUS = "MODBUS";				// MODBUS protocol type
	private static final String MODBUSRTU = "MODBUSRTU";		// RTU transport type
	private static final String MODBUSASCII = "MODBUSASCII";	// ASCII transport type
	private static final String MODBUSTCP = "MODBUSTCP";		// TCP transport type
	
	private static final Integer OFFLINE_IDX = 0;
	private static Integer counter =  0;
	private int debug = DEBUG_LEVEL_OFF;
	private static final String VALUE = "value";

	// dev pooling priorities
	private static final String PRI_HIGH = "H";					// high priority
	private static final String PRI_NORMAL = "N";				// normal priority
	// var pooling priorities
	private static final String PRI_ALARM = "A";				// alarm variable
	private static final String PRI_LOGGED = "L";				// logged variable
	private static final String PRI_PARAM = "P";				// static parameter


	public DataConnMODBUS() {
		LoggerMgr.getLogger(this.getClass()).info(this.getClass().getSimpleName()+"_"+(counter++) );
	}

	public synchronized void init(XMLNode xmlStatic) throws InvalidConfigurationException {
    	XMLNode[] n = xmlStatic.getNodes();
    	String tmpdebug = null;
    	for (int i = 0; i < n.length; i++) {
    		if(n[i].getAttribute("name").equalsIgnoreCase("debug")){
    			tmpdebug = n[i].getAttribute(VALUE);
    		}
		}
    	if(tmpdebug!=null) {
    		try{
    			debug = Integer.parseInt(tmpdebug);
    		} catch (Exception e) {
    			debug = DEBUG_LEVEL_OFF;
    		}
    	}
    	
    	// override xml debug
    	String strDebug = ProductInfoMgr.getInstance().getProductInfo().get("modbus_debug");
    	if( strDebug != null && !strDebug.isEmpty() )
    		debug = Integer.parseInt(strDebug);
	}
	
    private native short loadDll();
    private native short loadConfig(String xmlconfigstr);
    private native short getUnitStatus(short line, short address);
    private native short read(int line, int serial, int address, int fntype, int vardimension, byte[] value);
    private native short write(int line, int serial, int address, int fntype, int vardimension, byte[] value);
    private native short unloadConfig();
    private native short unloadDll();
    public native void debugLog(int level);
    private static String PVProModbusSmart = System.getenv("windir")+File.separator+"system32"+File.separator+"PVProModbusSmart.dll";
    private static String PVProModbusClassic = System.getenv("windir")+File.separator+"system32"+File.separator+"PVProModbusClassic.dll";
    private static String modbus_current = System.getenv("windir")+File.separator+"system32"+File.separator+"PVProModbus.dll";

    public synchronized DriverReturnCode loadDllDriver() {
        try {
			System.loadLibrary("ModbusDataConnector");
			return new DriverReturnCode((short) 0);
		} catch (Throwable e) {
			setBlockingError();
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
			EventMgr.getInstance().log(new Integer(1), "Field-modbus", "Connection", EventDictionary.TYPE_ERROR, "F003", getName());
			return new DriverReturnCode(e);
		}
	}
    
    public void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//from
            out = fo.getChannel();//to
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void initModbusDLL(){
    	try{
    		String enable = ProductInfoMgr.getInstance().getProductInfo().get("smart_modbus_enable");
        	File file_modbusSmart = new File(PVProModbusSmart);
    		File file_modbusClassic = new File(PVProModbusClassic);
    		File file_modbus_current = new File(modbus_current);
    		
        	if(enable!=null && "enable".equals(enable)&& file_modbusSmart.exists()){
        		//copy PVProModbusSmart dll and delete current dll 
        		fileChannelCopy(file_modbusSmart,file_modbus_current);
        	}
        	else if (enable!=null && "disable".equals(enable)&& file_modbusClassic.exists()){
        		//copy PVProModbusClassic dll and delete current dll 
        		fileChannelCopy(file_modbusClassic,file_modbus_current);
        	}       	
    	}catch(Throwable e){
    		 e.printStackTrace();	
    	}
    }

	public synchronized DriverReturnCode initDriver() {
		System.out.println("Init "+this.getClass().getName());
		initModbusDLL();
		short ret = loadDll();
		if(ret==0) {
			ModbusSettingsMap.loadExtension();
			debugLog(debug);
			String configxml = buildConfigXml();
			return new DriverReturnCode(loadConfig(configxml));
		} else {
			return new DriverReturnCode(ret);
		}
	}


	public synchronized DriverReturnCode closeDriver() {
		try {
			DriverReturnCode d = new DriverReturnCode(unloadConfig());
			if (999 == d.getReturnCode()) {
				EventMgr.getInstance().log(new Integer(1), "Field-modbus", "Connection", EventDictionary.TYPE_ERROR, "F501", getName());
				LoggerMgr.getLogger(this.getClass()).error("Exception in dllmodbus: locDrvClose");
			}
			short ret = unloadDll();
			if (ret != 0) {
				EventMgr.getInstance().log(new Integer(1), "Field-modbus", "Connection", EventDictionary.TYPE_ERROR, "F502", getName());
				LoggerMgr.getLogger(this.getClass()).error("Probably exception occurred in dllmodbus: freeDll");
			}
			return d;
		} catch (Throwable e) {
			return new DriverReturnCode(e);
		}
	}

	public synchronized void retrieve(Variable variable) {
        try {
			if (OFFLINE_IDX == variable.getInfo().getAddressIn().intValue()) { // Variabile 0 corrispondente all'OFFLINE
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
	                    VariableInfo variableInfo = variable.getInfo();
	                    Integer readadd = variableInfo.getAddressIn().intValue()%100000;
	                    Integer fncode = variableInfo.getAddressIn().intValue()/100000;
	                    if(fncode.equals(0))
	                    	fncode = 3;
	                    
	                    byte[] value = null;
	                    // 20090929 - Digital/Alarm vars MUST be == 1 Byte.
	                    if (fncode.equals(1) || fncode.equals(2)) {
	                    	value = new byte[1];
	                    	variableInfo.setVarDimension(8);
	                    	variableInfo.setVarLength(8);
	                    } else {
	                    	value = new byte[variableInfo.getVarDimension()/8];
	                    }

	                    int vardimension = variableInfo.getVarDimension();
	                    
	                    Integer line = variableInfo.getDeviceInfo().getLine();
	                    Integer address = variableInfo.getDeviceInfo().getAddress();
	                    short returncode = read(line, address, readadd, fncode, vardimension, value);
	                    
	                    valueFromDriver(variableInfo, variable, value);
	                    
	                    switch (returncode) {
	                    case -44:
						case 1:
							variable.setValue(null); // Not yet acquired
							if(debug>0) {
								LoggerMgr.getLogger(this.getClass()).info("offline.d"+variableInfo.getDeviceInfo().getId()+".v"+variableInfo.getId());
							}
							break;
						case 0:
							DataConnBase.saveAlarmGuardian(variable);
							break;
						case 999:
							variable.activeBlockingError();
							LoggerMgr.getLogger(this.getClass()).error("Exception in modbus dll: read(" + line + "," + address
									+ "," + readadd + "," + fncode + "," + vardimension + ")");
							break;
						default: // non dovrebbe mai passare di qui
							variable.activeBlockingError();
							LoggerMgr.getLogger(this.getClass()).error("Exception in dll: readDBValue unknown");
	                    }
					} catch (Exception e) {
						setBlockingError();
						EventMgr.getInstance().log(new Integer(1), "Field-modbus", "Acquisition", EventDictionary.TYPE_ERROR, "F005", getName());
						LoggerMgr.getLogger(this.getClass()).error(e);
					}
				} else {//device is offline
 	                variable.setValue(null);
	            }
			}
        } catch (Throwable e) {
        	setBlockingError();
            EventMgr.getInstance().log(new Integer(1), "Field-modbus", "Acquisition", EventDictionary.TYPE_ERROR, "F005", getName());
            LoggerMgr.getLogger(this.getClass()).error(e);
        }
	}

	public void valueFromDriver(VariableInfo variableInfo, Variable variable, byte[] value) throws Exception {
		//endianess
		if(variableInfo.getDeviceInfo().isLittleEndian()) { // LSW-MSW
			byte[] bb = new byte[2];
			int idx;
			int _idx;
			for(int i = 0; i<(int)value.length/4; i++){
				idx = 2*i;
				_idx = value.length - 1 - 2*i;
				bb[0] = value[idx];
				bb[1] = value[idx+1];
				
				value[idx] = value[_idx-1];
				value[idx+1] = value[_idx];
				
				value[_idx-1] = bb[0];
				value[_idx] = bb[1];
			}
		}
		//coding
		ModbusSettings mbs = ModbusSettingsMap.get(variableInfo.getModel());
		float varval = Float.NaN;
		if(mbs==null || ModbusSettings.BINARY_FIXED_POINT.equals(mbs.getEncoding()))
			varval = valueBinaryFixedPoint(variable, value, mbs);
		if(mbs!=null && ModbusSettings.IEEE_FLOATING_POINT.equals(mbs.getEncoding()))
			varval = valueIEEEFloatingPoint(variable, value, mbs);
		if(mbs!=null && ModbusSettings.BCD.equals(mbs.getEncoding()))
			varval = valueBCD(variable, value, mbs);
		//set value
		variable.setValue(varval);
	}

	private float valueBinaryFixedPoint(Variable variable, byte[] value, ModbusSettings mbs) {
		long val = 0l;
		VariableInfo variableInfo = variable.getInfo(); 
		if (1 != (variableInfo.getVarDimension() / 8)) {
			for(int i = 0; i < value.length / 2; i++) {
				val = (val<<16 & 0xffffffffffff0000L) + ((value[2*i]<<8) & 0xff00) + (value[2*i+1] & 0xff);
			}
			if (variableInfo.isSigned()) {
				if (value.length == 2) {
					val = (short) val;
				}
				if (value.length == 4) {
					val = (int) val;
				}
			}
		} else {
			val = value[0];
		}
		//isValueOfDimension() is to check if get the value of whole vardimension
		if (!variable.isValueOfDimension() && variableInfo.getVarLength() != variableInfo.getVarDimension()) {
			/*
			 * In caso di variabili compresse in word estreggo il loro valore a
			 * partire dall'offset e dalla lunghezza. Nel Gavazzi sono: 2 su 16
			 * bit da 8 bit ciascuna (PF) 2 su 8 bit da 1 bit ciascuna (Alarm)
			 */
			val = BitManipulation.extractNumber(val, variableInfo.getBitPosition(), variableInfo.getVarLength());
//			variable.setValue((float)val);
		}
		float fval = Float.NaN;
		IFunctionMgr fmgr = DeviceFunctionMapper.getInstance().getFunctionMgr(variableInfo.getDeviceInfo().getModel());
		if(fmgr!=null){
			fval = fmgr.applyFunction(val, variable);
//			fval = variable.getCurrentValue();
			variable.setValue(new Float(fval));//*(Math.pow(10, -variableInfo.getDecimal()))));
		}else{
			// ax+b
			fval = applyTransformation(variableInfo, val);
			// ax+b
		}
		return fval;
	}
	
	private float valueIEEEFloatingPoint(Variable variable, byte[] value, ModbusSettings mbs) 
	{
		ByteBuffer buf = ByteBuffer.wrap(value);
		if (value.length != 4)
		{
			double v = buf.getDouble();
			return (float) v;
		}
		return buf.getFloat();
	}

	private float valueBCD(Variable variable, byte[] value, ModbusSettings mbs) {
		throw new UnsupportedOperationException();
		//return Float.NaN;
	}

	private float applyTransformation(VariableInfo variableInfo, long val) {
		int idvarmdl = variableInfo.getModel();
		ModbusSettings abn = ModbusSettingsMap.get(idvarmdl);
		if(abn!=null){
			BigDecimal b1 = new BigDecimal(Float.toString( abn.getAvalue()));
			BigDecimal b2 = new BigDecimal(Float.toString(val));
	        BigDecimal b3 = new BigDecimal(Float.toString(abn.getBvalue()));
			return b1.multiply(b2).add(b3).floatValue();
		}
		return val;
	}

	public float applyReverseTransformation(VariableInfo variableInfo, long val) {
		int idvarmdl = variableInfo.getModel();
		ModbusSettings abn = ModbusSettingsMap.get(idvarmdl);
		if(abn!=null)
			return (val-abn.getBvalue())/abn.getAvalue();
		return val;
	}

	// private byte[] getBarray(ShortValue[] value) {
	// byte[] toret = new byte[]{0,0,0,0};
	// if(value[1]!=null){
	// toret[3] = (byte)(value[1].getValue()>>8);
	// toret[2] = (byte)value[1].getValue();
	// }
	// toret[1] = (byte)(value[0].getValue()>>8);
	// toret[0] = (byte)value[0].getValue();
	// return toret;
	// }

	public synchronized int setOnField(Variable variable) {
        String msg = null;
		try {
			VariableInfo variableInfo = variable.getInfo();
            int writeadd = variableInfo.getAddressOut().intValue()%100000;
            int fncode = variableInfo.getAddressOut().intValue()/100000;
            if(fncode==0)
            	fncode = 6;
            int vardimension = variableInfo.getVarDimension();// /16;
            /* HAMMERING */
            try {
            	RecordSet code = DatabaseMgr.getInstance().executeQuery(null, 
            			" select cfvariable.code from cfvariable,cfdevice " +
            			" where " +
            			" idvariable="+variableInfo.getId()+" and " +
            			" idvarmdl = "+variableInfo.getModel()+" and " +
            			" cfvariable.iddevice = cfdevice.iddevice and " +
            			" cfdevice.iddevmdl = (select iddevmdl from cfdevmdl where code='DUCATI'); ");
            	if(code!=null && code.size()>0 && ("KV".equals(code.get(0).get(0)) ||
            			"KA".equals(code.get(0).get(0)) ||
            			"TM".equals(code.get(0).get(0)) ||
            			"Reset".equals(code.get(0).get(0)))) {
            		vardimension = 16;
            		variableInfo.setVarDimension(vardimension);
            	}
			} catch (Exception e) {
			}
            float newval = variable.getCurrentValue();
            byte[] value = getByteArray(variable, ModbusSettingsMap.get(variableInfo.getModel()));
            if(fncode==5 && variableInfo.getType()==1 && newval==1f){
            	value = new byte[]{(byte) 0xff, 0x00};
        		vardimension = 16;
        		variableInfo.setVarDimension(vardimension);
            }
            /* HAMMERING */
            
            /*tmp*/
//            String s ="";
//            for(int i=0;i<value.length;i++){
//            	s+=value[i]+".";
//            }
//            LoggerMgr.getLogger(this.getClass()).info("value = "+s);
			/*tmp*/
            
            // Management of the little-endian
            if(variableInfo.getDeviceInfo().isLittleEndian()) { // LSW-MSW
    			byte[] bb = new byte[2];
    			int idx;
    			int _idx;
    			for(int i = 0; i<(int)value.length/4; i++){
    				idx = 2*i;
    				_idx = value.length - 1 - 2*i;
    				bb[0] = value[idx];
    				bb[1] = value[idx+1];
    				
    				value[idx] = value[_idx-1];
    				value[idx+1] = value[_idx];
    				
    				value[_idx-1] = bb[0];
    				value[_idx] = bb[1];
    			}
    		}
            
            Short returnCode = write(variableInfo.getDeviceInfo().getLine(),
					variableInfo.getDeviceInfo().getAddress(),
					writeadd, fncode, vardimension, value);
			// TO DO manage return codes
			switch (returnCode) {
				case 0:
				case 1:
					break;
				case 999:
					variable.activeBlockingError();
					LoggerMgr.getLogger(this.getClass()).error("Exception in dllmodbus: sendValue");
					break;
				default: // non dovrebbe mai passare di qui
					variable.activeBlockingError();
					msg = CoreMessages.format("FLDE1033");
			}
			if (variable.isBlockingError()) {
				EventMgr.getInstance().log(new Integer(1), "Field-modbus", "Acquisition", EventDictionary.TYPE_ERROR, "F006", getName() + " " + msg);
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
			EventMgr.getInstance().log(new Integer(1), "Field-modbus", "Acquisition",EventDictionary.TYPE_ERROR, "F006", getName());
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
			return DataConnBase.BLOCKING_ERROR;
		}
	}

	private byte[] getByteArray(Variable variable, ModbusSettings mbs) {
		float value = variable.getCurrentValue();
		if(mbs==null || ModbusSettings.BINARY_FIXED_POINT.equals(mbs.getEncoding()))
			return byteBinaryFixedPoint(variable, value, mbs);
		if(mbs!=null && ModbusSettings.IEEE_FLOATING_POINT.equals(mbs.getEncoding()))
			return byteIEEEFloatingPoint(variable, value, mbs);
		if(mbs!=null && ModbusSettings.BCD.equals(mbs.getEncoding()))
			return byteBCD(variable, value, mbs);
		return new byte[]{};
	}

	private byte[] byteBinaryFixedPoint(Variable variable, float value, ModbusSettings mbs) {
		int vardimension = variable.getInfo().getVarDimension();
		byte[] ret = new byte[vardimension / 8];
		long l = (long)value;
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (byte) (l>>(vardimension-8*(i+1)) & 0xff);
		}
		return ret;
	}
	
	private byte[] byteIEEEFloatingPoint(Variable variable, float value, ModbusSettings mbs) {
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putFloat(value);
		return buf.array();
	}

	private byte[] byteBCD(Variable variable, float value, ModbusSettings mbs) {
		throw new UnsupportedOperationException();
		//return Float.NaN;
	}


//	public double valueToDriver(VariableInfo variableInfo, Variable variable) {
//		return variable.getCurrentValue();
//	}

	public synchronized boolean getDeviceStatus(DeviceInfo deviceInfo) {
        try {
			short status = getUnitStatus((short) deviceInfo.getLine().intValue(), (short) deviceInfo.getAddress().intValue());
			if (status == STATUS_ERROR) {
				setBlockingError();
				LoggerMgr.getLogger(this.getClass()).error("Exception in dllmodbus: getUnitStatus");
			}
			// Status: 0 periferica online 1 periferica offline
			return (STATUS_ONLINE == status);
		} catch (Throwable e) {
			setBlockingError();
			EventMgr.getInstance().log(new Integer(1), "Field-modbus", "Acquisition", EventDictionary.TYPE_ERROR, "F007", getName());
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
			return VALUE_OFFLINE;
		}
	}
	
	private String buildConfigXml() {
		StringBuilder toret = new StringBuilder();
		HashMap<Integer, ModbusExtensionModelBean> mdlmap = ModbusExtensionModelBeanList.getModbusExtension();
		ModbusExtensionModelBean memb;
		// cfg tag
		toret.append("<cfg dgap='");
		toret.append(ProductInfoMgr.getInstance().getProductInfo().get("modbus_digvar_gap"));
		toret.append("' ngap='");
		toret.append(ProductInfoMgr.getInstance().getProductInfo().get("modbus_numvar_gap"));
		toret.append("' lvar='");
		toret.append(ProductInfoMgr.getInstance().getProductInfo().get("modbus_lvar_cycle"));
		toret.append("' hdev='");
		toret.append(ProductInfoMgr.getInstance().getProductInfo().get("modbus_hdev_threshold"));
		toret.append("'>");
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
			if( !lines[k].getTypeprotocol().equals(MODBUS) )
				continue;
			
			// ln tag
			String protocoltype = MODBUSRTU;
			if( lines[k].getProtocol().equals("GAVAZZI") )
				protocoltype = MODBUSRTU;
			else if( lines[k].getProtocol().equals("LAN.TCP") )
				protocoltype = MODBUSTCP;
			toret.append("<ln id='"+lines[k].getIdline()+"' t='"+protocoltype+"'  >");
			// lncfg tag
			if( protocoltype.equals(MODBUSTCP) )
				toret.append("<lncfg ip='"+lines[k].getIpAddress()+"' p='502' />");
			else
				toret.append("<lncfg p='"+lines[k].getComport()+"' b='"+lines[k].getBaudrate()+"' />");
			
			LinkedList<DeviceBean> devicelist = DeviceListBean.getDevicesByLine(1, lines[k].getIdline());
			System.out.print("   MODBUS devices: "+devicelist.size());
			// devs tag
			toret.append("<devs>");
			for (Iterator<DeviceBean> iterator = devicelist.iterator(); iterator.hasNext();) {
				DeviceBean deviceBean = iterator.next();
				memb = mdlmap.get(deviceBean.getIddevmdl())!=null?mdlmap.get(deviceBean.getIddevmdl()):mdlmap.get(0);
				toret.append("<dev addr='");
				toret.append(deviceBean.getAddress());
				toret.append("' be='");
				toret.append(memb.getStopbit());
				toret.append("' p='");
				toret.append(memb.getParity());
				toret.append("' rto='");
				toret.append(memb.getReadtimeout());
				toret.append("' wto='");
				toret.append(memb.getWritetimeout());
				toret.append("' ato='");
				toret.append(memb.getActivitytimeout());
				toret.append("' dt='");
				toret.append(memb.getDatatransmission());
				toret.append("' f1='");
				toret.append(memb.getF1());
				toret.append("' f2='");
				toret.append(memb.getF2());
				toret.append("' f3='");
				toret.append(memb.getF3());
				toret.append("' f4='");
				toret.append(memb.getF4());
				toret.append("' f5='");
				toret.append(memb.getF5());
				toret.append("' f6='");
				toret.append(memb.getF6());
				toret.append("' f15='");
				toret.append(memb.getF15());
				toret.append("' f16='");
				toret.append(memb.getF16());
				toret.append("' pri='");
				toret.append(memb.isHiPriority() ? PRI_HIGH : PRI_NORMAL);
				toret.append("' >");
				RecordSet variablelist = VarphyBeanList.retrieveVarByDevice(1, deviceBean.getIddevice());
				variables+=variablelist.size();
				for (int kk=0;kk<variablelist.size();kk++) {
					Record r = variablelist.get(kk);
					if( OFFLINE_IDX.equals(r.get("addressin"))
						|| r.get("readwrite").toString().trim().equals("2") // write only
					)
						continue;
					toret.append("<v t='");
					Integer type = (Integer)r.get("type"); 
					toret.append(type == VarphyBean.TYPE_ALARM ? VarphyBean.TYPE_DIGITAL : type);
					toret.append("' ain='");
					Integer addr = (Integer)r.get("addressin");
					toret.append(addr%100000);
					toret.append("' fin='");
					int fin = (int)(addr/100000)!=0?(int)(addr/100000):3;
					toret.append(fin);
					toret.append("' vd='");
					toret.append(r.get("vardimension"));
					toret.append("' pri='");
					if( type == VarphyBean.TYPE_ALARM )
						toret.append(PRI_ALARM);
					else if( ((String)r.get("ishaccp")).startsWith("TRUE") || (Integer)r.get("idhsvariable") > -1 )
						toret.append(PRI_LOGGED);
					else
						toret.append(PRI_PARAM);
					toret.append("' />");
				}
				toret.append("</dev>");
			}
			toret.append("</devs>");
			System.out.println("        line vars: "+variables);
			variables=0;
			toret.append("</ln>");			
		}
		toret.append("</cfg>");
		
		//TODO - DEBUG remove
		try {
			FileWriter fw;
			fw = new FileWriter("modbusconfiguration.xml");
			fw.write(toret.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TODO - DEBUG remove
		return toret.toString();
	}
	
	//LDAC FOR NOVACOOP 19/1/2012 START
	
	public synchronized short retrieve(Variable variable, DoubleValue value)
	{
		//Non valutiamo la variabile offline che è tecnica e fa il giro degli allarmi

		short status = getUnitStatus((short)variable.getInfo().getDeviceInfo().getLine().intValue(), (short)variable.getInfo().getDeviceInfo().getAddress().intValue());

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
        	try {
                VariableInfo variableInfo = variable.getInfo();
                Integer readadd = variableInfo.getAddressIn().intValue()%100000;
                Integer fncode = variableInfo.getAddressIn().intValue()/100000;
                if(fncode.equals(0))
                	fncode = 3;
                
                byte[] val = null;
                // 20090929 - Digital/Alarm vars MUST be == 1 Byte.
                if (fncode.equals(1) || fncode.equals(2)) {
                	val = new byte[1];
                	variableInfo.setVarDimension(8);
                	variableInfo.setVarLength(8);
                } else {
                	val = new byte[variableInfo.getVarDimension()/8];
                }

                int vardimension = variableInfo.getVarDimension();
                
                Integer line = variableInfo.getDeviceInfo().getLine();
                Integer address = variableInfo.getDeviceInfo().getAddress();
                short returncode = read(line, address, readadd, fncode, vardimension, val);
                
                valueFromDriver(variableInfo, variable, val);
                
                if(returncode == 0)
                {
                	value.setValue((double)variable.getCurrentValue());
                	return 1; // answer as 'value retrieved'
                }
                else
                {
                	value.setValue(Double.NaN);
                	return 0; // answer as 'value not retrieved'
                }

			} catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
				return 0; // answer as 'value not retrieved'
			}


		}
        catch (Throwable e)
        {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
            return 0; // answer as 'value not retrieved'
        }
	}
	
	//LDAC FOR NOVACOOP 19/01/2012 END
}
