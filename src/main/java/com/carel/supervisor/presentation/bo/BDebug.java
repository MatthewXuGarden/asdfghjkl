package com.carel.supervisor.presentation.bo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.dataconfig.DeviceCarelInfoList;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList;
import com.carel.supervisor.dataaccess.dataconfig.LineInfo;
import com.carel.supervisor.dataaccess.dataconfig.LineInfoList;
import com.carel.supervisor.dataaccess.datalog.impl.AlarmLogList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.field.FieldConnectorMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.impl.DataConnCAREL;
import com.carel.supervisor.field.dataconn.impl.DataConnMODBUS;
import com.carel.supervisor.field.types.ExtUnitInfoT;
import com.carel.supervisor.field.types.PER_INFO;
import com.carel.supervisor.field.types.PollingInfoT;
import com.carel.supervisor.presentation.bean.DebugBeanList;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.MbMasterDebugBean;
import com.carel.supervisor.presentation.bo.helper.LineConfig;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.copydevice.PageImpExp;
import com.carel.supervisor.presentation.devices.UtilDevice;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;

public class BDebug extends BoMaster {

	private Map varphys;
	private static boolean isChanging=false;
	private int[] selectedDeviceid;
	private int[] iddevs;
	Map<Integer,Integer> serial485COM = LineConfig.getSerial485COM();
	private static Map<String,String> avgMap = null;
	
	public BDebug(String l) {
		super(l, -1);
	}

	protected Properties initializeEventOnLoad() {
		Properties p = new Properties();
		p.setProperty("tab1name", "tab1_init();");
		p.setProperty("tab2name", "tab2_init();");
		p.setProperty("tab3name", "mb_debug_init();");
		return p;
	}

	protected Properties initializeJsOnLoad() {
		Properties p = new Properties();
		p.setProperty("tab1name", "../arch/table/sorttable.js;../arch/Concurrent.Thread.js;debug.js;../arch/actb/common.js;keyboard.js;../jquery/jquery-1.11.1.min.js;");
		p.setProperty("tab2name", "../arch/table/sorttable.js;../arch/Concurrent.Thread.js;debug.js;../arch/actb/common.js;keyboard.js;../jquery/jquery-1.11.1.min.js;");
		p.setProperty("tab3name", "debug.js;../arch/FileDialog.js;keyboard.js;");
		return p;
	}

	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab3name", DOCTYPE_STRICT);
		return p;
    }
	
	public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception {
		StringBuffer result = new StringBuffer();
		if (tabName.equals("tab1name") == true) {
			result=SubTab1DataAction(us, prop);
		}else if (tabName.equals("tab2name") == true) {
			//because the business are same ,they are using the  same func
			result=SubTab1DataAction(us, prop);
		}
		return result.toString();
	}

	public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception {
		String cmd = prop.getProperty("cmd");
		if( "mb_debug_set_level".equals(cmd) ) {
			String strDebug = prop.getProperty("debug_level");
	    	if( strDebug != null && !strDebug.isEmpty() ) {
	    		ProductInfoMgr.getInstance().getProductInfo().set("modbus_debug", strDebug);
	    		DataConnMODBUS conn = (DataConnMODBUS)FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("MODBUS");
	    		try {
	    			conn.debugLog(Integer.parseInt(strDebug));
	    		} catch(Exception e) {
	    			LoggerMgr.getLogger(this.getClass()).error(e);
	    		}
	    	}
	    	String strDigVarGap = prop.getProperty("digvar_gap");
	    	String strNumVarGap = prop.getProperty("numvar_gap");
	    	String strLVarCycle = prop.getProperty("lvar_cycle");
	    	String strHDevThreshold = prop.getProperty("hdev_threshold");
	    	if( !ProductInfoMgr.getInstance().getProductInfo().get("modbus_digvar_gap").equals(strDigVarGap)
	    		|| !ProductInfoMgr.getInstance().getProductInfo().get("modbus_numvar_gap").equals(strNumVarGap)
	    		|| !ProductInfoMgr.getInstance().getProductInfo().get("modbus_lvar_cycle").equals(strLVarCycle)
	    		|| !ProductInfoMgr.getInstance().getProductInfo().get("modbus_hdev_threshold").equals(strHDevThreshold) ) {
	    		ProductInfoMgr.getInstance().getProductInfo().set("modbus_digvar_gap", strDigVarGap);
	    		ProductInfoMgr.getInstance().getProductInfo().set("modbus_numvar_gap", strNumVarGap);
	    		ProductInfoMgr.getInstance().getProductInfo().set("modbus_lvar_cycle", strLVarCycle);
	    		ProductInfoMgr.getInstance().getProductInfo().set("modbus_hdev_threshold", strHDevThreshold);
	    		DirectorMgr.getInstance().mustCreateProtocolFile();
	    		DirectorMgr.getInstance().mustRestart();
	    	}
		}
		else if( "mb_debug_refresh".equals(cmd) ) {
			// nothing to do, just reload the tab
		}
    	else if( "export".equals(cmd) ) {
    		String strFileName = prop.getProperty("exp");
    		if( strFileName != null && !strFileName.isEmpty() ) {
    			MbMasterDebugBean beanDebug = new MbMasterDebugBean(us);
    			beanDebug.exportDebugLog(strFileName);
    		}
    	}
	}

	private StringBuffer SubTab1DataAction(UserSession us, Properties prop) throws Exception {
		String cmd = prop.getProperty("cmd");
		StringBuffer result =new StringBuffer();
		if ("subtab1_init".equalsIgnoreCase(cmd)) {
			result = subtab1_init(us, prop);
			return result;
		}else if ("subtab2_init".equalsIgnoreCase(cmd)) {
			result = subtab2_init(us, prop);
			return result;
		}else if ("refresh".equalsIgnoreCase(cmd)) {
			result = refresh(us, prop);
			return result;
		}else if ("refreshSub2".equalsIgnoreCase(cmd)) {
			result = refreshSub2(us, prop);
			return result;
		}else if(cmd.equalsIgnoreCase("start")){
			isChanging=true;
			String selecteddev = prop.getProperty("selecteddev");
			String[] selecteddevs = selecteddev.split(";");
			selectedDeviceid = new int[selecteddevs.length];
			for(int i=0;i<selecteddevs.length;i++)
			{
				selectedDeviceid[i] = Integer.valueOf(selecteddevs[i]);
			}
			int timeout = Integer.valueOf(prop.getProperty("timeout"));
			DirectorMgr.getInstance().startDebugSession(selectedDeviceid,timeout,us.getUserName());
			EventMgr.getInstance().info(new Integer(1), us.getUserName(), "Action", "DB01", null);
			HashMap<Integer,String[]> omap=DirectorMgr.getInstance().getOfflineMap();
			Iterator it=omap.keySet().iterator();
			while(it.hasNext()){
				Integer t=(Integer)it.next();
				omap.put(t, new String[]{"ON","0"});
			}
			result.append("<root>");
			//result.append("<topmsg>"+ServletHelper.messageToNotify(us.getLanguage())+"</topmsg>");
			DateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowtime=s.format(new Date());
			DirectorMgr.getInstance().setDebugStartTime(nowtime);
			result.append("</root>");
			isChanging=false;
		}else if(cmd.equalsIgnoreCase("stop")){
			isChanging=true;
			DirectorMgr.getInstance().stopDebugSession(us.getUserName());
			EventMgr.getInstance().info(new Integer(1), us.getUserName(), "Action", "DB02", null);
			DirectorMgr.getInstance().setDebugStartTime("");
			HashMap<Integer,String[]> omap=DirectorMgr.getInstance().getOfflineMap();
			Iterator it=omap.keySet().iterator();
			while(it.hasNext()){
				Integer t=(Integer)it.next();
				omap.put(t, new String[]{"ON","0"});
			}
			result.append("<root>");
			//result.append("<topmsg>"+ServletHelper.messageToNotify(us.getLanguage())+"</topmsg>");
			result.append("</root>");
			isChanging=false;
		}else if(cmd.equalsIgnoreCase("reset")){
			isChanging=true;
			HashMap<Integer,String[]> omap=DirectorMgr.getInstance().getOfflineMap();
			Iterator it=omap.keySet().iterator();
			while(it.hasNext()){
				Integer t=(Integer)it.next();
				omap.put(t, new String[]{"ON","0"});
			}
			DirectorMgr.getInstance().resetDebugSession(us.getUserName());
			result.append("<root>");
			//result.append("<topmsg>"+ServletHelper.messageToNotify(us.getLanguage())+"</topmsg>");
			result.append("</root>");
			isChanging=false;
		}
		return result;
	}

	private StringBuffer subtab1_init(UserSession us, Properties prop) throws Exception {
		int[] globalindex = DirectorMgr.getInstance().getGlobalindex();
		boolean isDebugSessionOn = DirectorMgr.getInstance().isDebugSessionOn();
		int timeout = DirectorMgr.getInstance().getTimeout();
		DataConnCAREL dataconn = ((DataConnCAREL) FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAREL"));
		DeviceCarelInfoList device = new DeviceCarelInfoList(null, BaseConfig.getPlantId());
		int num = device.size();
		DeviceInfo dev = null;
		DeviceBean deviceBean = null;
		DeviceListBean deviceList = new DeviceListBean(us.getIdSite(),us.getLanguage());
		int[] ids = new int[num];
		iddevs=new int[num];
		for (int i = 0; i < num; i++) {
			dev = (DeviceInfo) device.get(i);
			ids[i] = dev.getId();
			iddevs[i]=dev.getId();
		}
		
		PER_INFO perinfo = null;
		ExtUnitInfoT extUnitInfoT = null; 
		StringBuffer result=new StringBuffer();
		result.append("<root>");
		result.append("<starttime><![CDATA["+DirectorMgr.getInstance().getDebugStartTime()+"]]></starttime>");
		result.append("<topmsg><![CDATA["+ServletHelper.messageToNotify(us.getLanguage())+"]]></topmsg>");
		if (isDebugSessionOn == true) {
			result.append("<mode><![CDATA[1]]></mode>");
			result.append("<time><![CDATA[" + timeout + "]]></time>");
		} else {
			result.append("<mode><![CDATA[0]]></mode>");
		}
		LineInfoList lineInfoList = (LineInfoList)DataConfigMgr.getInstance().getConfig("cfline");
		boolean canshowavgpollingtime = DirectorMgr.getInstance().canShowAvgPollingTime();
		if(lineInfoList != null && lineInfoList.size()>0)
		{
			for(int i=0;i<lineInfoList.size();i++)
			{
				LineInfo lineInfo = lineInfoList.get(i);
				if(PageImpExp.CAREL_STANDARD_NAME.equals(lineInfo.getTypeProtocol()))
				{
					int code = lineInfo.getCode();
					if(canshowavgpollingtime)
					{
						dataconn = ((DataConnCAREL) FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAREL"));
						PollingInfoT pollingInfo = dataconn.getPollingInfo((short)code, PollingInfoT.LINE_AVRG_POLLING_TIME);
						if(pollingInfo != null)
							result.append("<avg_t><![CDATA["+code+"]]><![CDATA["+pollingInfo.getValue()+"]]></avg_t>");
					}
					else
					{
						result.append("<avg_t><![CDATA["+code+"]]><![CDATA[---]]></avg_t>");
					}
				}
			}
		}
		if (isDebugSessionOn == true) {
			for (int i = 0; i < globalindex.length; i++) {
				for (int j = 0; j < device.size(); j++) {
					dev = (DeviceInfo) device.get(j);
					if (globalindex[i] == dev.getGlobalindex()) {
						perinfo = dataconn.getPeriphericalInfo((short) dev.getGlobalindex().intValue());
						extUnitInfoT = dataconn.getPeriphericalInfoEx((short) dev.getGlobalindex().intValue());
						deviceBean = deviceList.getDevice(dev.getId());
						appendDeviceBaseInfo(us,result, perinfo,extUnitInfoT, deviceBean, dev, isDebugSessionOn, true);
					}
				}
			}
		} 
		for (int i = 0; i < num; i++) {
			 if (isDebugSessionOn == true) {
				dev = (DeviceInfo) device.get(i);
				if (checkExist(dev, globalindex) == true) {
					continue;
				}
			}
			dev = (DeviceInfo) device.get(i);
			perinfo = dataconn.getPeriphericalInfo((short) dev.getGlobalindex().intValue());
			extUnitInfoT = dataconn.getPeriphericalInfoEx((short) dev.getGlobalindex().intValue());
			deviceBean = deviceList.getDevice(dev.getId());
			appendDeviceBaseInfo(us,result, perinfo,extUnitInfoT, deviceBean, dev, isDebugSessionOn, false);
		}
	
		 result.append("</root>");
		return result;
	}
	private StringBuffer subtab2_init(UserSession us, Properties prop) throws Exception {
		int[] globalindex = DirectorMgr.getInstance().getGlobalindex();
		boolean isDebugSessionOn = DirectorMgr.getInstance().isDebugSessionOn();
		int timeout = DirectorMgr.getInstance().getTimeout();
//		DataConnCAREL dataconn = ((DataConnCAREL) FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAREL"));
		int[] idLinesExcept = {-1,-2};
		DeviceInfoList device = new DeviceInfoList(null, BaseConfig.getPlantId(),idLinesExcept);
		
		
		int num = device.size();
		DeviceInfo dev = null;
		DeviceBean deviceBean = null;
		DeviceListBean deviceList = new DeviceListBean(us.getIdSite(),us.getLanguage());
		int[] ids = new int[num];
		iddevs=new int[num];
		for (int i = 0; i < num; i++) {
			dev = (DeviceInfo) device.get(i);
			ids[i] = dev.getId();
			iddevs[i]=dev.getId();
		}

		DebugBeanList debugBeanList = DebugBeanList.getInstance();
		List params = debugBeanList.getVarmdlcodesByides(ids);
		//collect one time when first open page.
        varphys =  VarphyBeanList.getByiddevice_devmdlcodes(us.getLanguage(),us.getIdSite(),params,false);
		PER_INFO perinfo = null;
		ExtUnitInfoT extUnitInfoT = null; 
		StringBuffer result=new StringBuffer();
		result.append("<root>");
		result.append("<starttime><![CDATA["+DirectorMgr.getInstance().getDebugStartTime()+"]]></starttime>");
		result.append("<topmsg><![CDATA["+ServletHelper.messageToNotify(us.getLanguage())+"]]></topmsg>");
		if (isDebugSessionOn == true) {
			result.append("<mode><![CDATA[1]]></mode>");
			result.append("<time><![CDATA[" + timeout + "]]></time>");
		} else {
			result.append("<mode><![CDATA[0]]></mode>");
		}
		if( varphys != null ) {
			for (int i = 0; i < num; i++) {
				dev = (DeviceInfo) device.get(i);
	//			perinfo = dataconn.getPeriphericalInfo((short) dev.getGlobalindex().intValue());
	//			extUnitInfoT = dataconn.getPeriphericalInfoEx((short) dev.getGlobalindex().intValue());
				if(varphys.containsKey(dev.getId()))
				{
					deviceBean = deviceList.getDevice(dev.getId());
					appendDeviceBaseInfo(us,result, perinfo,extUnitInfoT, deviceBean, dev, isDebugSessionOn, false);
				}
			}
		}
		result.append("</root>");
		return result;
	}
	
	
	private StringBuffer refreshSub2(UserSession us, Properties prop)
			throws Exception {
		avgMap = null;
		StringBuffer result = new StringBuffer();
		int idLines[] = { -1, -2 };
		DeviceInfoList device = new DeviceInfoList(null, BaseConfig
				.getPlantId(), idLines);
		int num = device.size();
		DeviceInfo dev = null;
		DeviceBean deviceBean = null;
		DeviceListBean deviceList = new DeviceListBean(us.getIdSite(), us
				.getLanguage());
		int ids [] = new int[device.size()];
		for(int i=0;i<device.size();i++){
			ids[i] =device.get(i).getId();
		}
		DebugBeanList debugBeanList = DebugBeanList.getInstance();
		List params = debugBeanList.getVarmdlcodesByides(ids);
		HashMap paramMap = new HashMap();
		for (int i = 0; i < params.size(); i++) {
			paramMap.put((String) (((String[]) params.get(i))[0]),
					(String[]) (params.get(i)));
		}

		result.append("<root>");
		result.append("<starttime><![CDATA["
				+ DirectorMgr.getInstance().getDebugStartTime()
				+ "]]></starttime>");
		result.append("<topmsg><![CDATA["
				+ ServletHelper.messageToNotify(us.getLanguage())
				+ "]]></topmsg>");
		result.append("<mode><![CDATA[1]]></mode>");
		result.append("<time><![CDATA["
				+ DirectorMgr.getInstance().getTimeout() + "]]></time>");
		for (int i = 0; i < num; i++) {
			dev = (DeviceInfo) device.get(i);
			deviceBean = deviceList.getDevice(dev.getId());
			if(paramMap.containsKey(dev.getId()+""))
				appendTheromValue(us.getIdSite(),us.getLanguage(),result, deviceBean, true, false, paramMap);
		}

		result.append("</root>");
		return result;
	}
	
	private StringBuffer refresh(UserSession us, Properties prop) throws Exception {
//		System.out.println("engine started="+DirectorMgr.getInstance().isStarted()+"|isChanging="+isChanging+"|refresh time="+new Date());
		StringBuffer result=new StringBuffer();
		int[] globalindex = DirectorMgr.getInstance().getGlobalindex();
		boolean isDebugSessionOn = DirectorMgr.getInstance().isDebugSessionOn();
		if(isChanging==true){
			result.append("<root>");
			result.append("<starttime><![CDATA["+DirectorMgr.getInstance().getDebugStartTime()+"]]></starttime>");
			result.append("<topmsg><![CDATA["+ServletHelper.messageToNotify(us.getLanguage())+"]]></topmsg>");
			if (isDebugSessionOn == true) {
				result.append("<mode><![CDATA[1]]></mode>");
				result.append("<time><![CDATA[" + DirectorMgr.getInstance().getTimeout() + "]]></time>");
			} else {
				result.append("<mode><![CDATA[0]]></mode>");
			}
			result.append("</root>");
			return result;
		}

		DataConnCAREL dataconn = ((DataConnCAREL) FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAREL"));
		DeviceCarelInfoList device = new DeviceCarelInfoList(null, BaseConfig.getPlantId());
		int num = device.size();
		DeviceInfo dev = null;
		DeviceBean deviceBean = null;
		DeviceListBean deviceList = new DeviceListBean(us.getIdSite(),us.getLanguage());
		

		int[] ids;
		if(isDebugSessionOn==true){
			ids=getSelectedDeviceid();
		}else{
			updateCarelProtocolDeviceid(us);
			ids=iddevs;
		}
		String subtab=prop.getProperty("subtab");
		
		DebugBeanList debugBeanList = DebugBeanList.getInstance();
		List params = debugBeanList.getVarmdlcodesByides(ids);
//        Map varphys =  VarphyBeanList.getByiddevice_devmdlcodes(us.getLanguage(),us.getIdSite(),params,false);
		PER_INFO perinfo = null;
		ExtUnitInfoT extUnitInfoT = null; 
		
		HashMap paramMap=new HashMap();
		for(int i=0;i<params.size();i++){
			paramMap.put((String)(((String[])params.get(i))[0]), (String[])(params.get(i)));
		}
		
		result.append("<root>");
		result.append("<starttime><![CDATA["+DirectorMgr.getInstance().getDebugStartTime()+"]]></starttime>");
		result.append("<topmsg><![CDATA["+ServletHelper.messageToNotify(us.getLanguage())+"]]></topmsg>");
		if (isDebugSessionOn == true) {
			result.append("<mode><![CDATA[1]]></mode>");
			result.append("<time><![CDATA[" + DirectorMgr.getInstance().getTimeout() + "]]></time>");
		} else {
			result.append("<mode><![CDATA[0]]></mode>");
		}
		boolean canshowavgpollingtime = DirectorMgr.getInstance().canShowAvgPollingTime();
		LineInfoList lineInfoList = (LineInfoList)DataConfigMgr.getInstance().getConfig("cfline");
		if(lineInfoList != null && lineInfoList.size()>0)
		{
			for(int i=0;i<lineInfoList.size();i++)
			{
				LineInfo lineInfo = lineInfoList.get(i);
				if(PageImpExp.CAREL_STANDARD_NAME.equals(lineInfo.getTypeProtocol()))
				{
					int code = lineInfo.getCode();
					if(canshowavgpollingtime)
					{
						dataconn = ((DataConnCAREL) FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAREL"));
						PollingInfoT pollingInfo = dataconn.getPollingInfo((short)code, PollingInfoT.LINE_AVRG_POLLING_TIME);
						result.append("<avg_t><![CDATA["+code+"]]><![CDATA["+pollingInfo.getValue()+"]]></avg_t>");
					}
					else
					{
						result.append("<avg_t><![CDATA["+code+"]]><![CDATA[---]]></avg_t>");
					}
				}
			}
		}
		if (isDebugSessionOn == true) {
			for (int i = 0; i < globalindex.length; i++) {
				for (int j = 0; j < device.size(); j++) {
					dev = (DeviceInfo) device.get(j);
					if (globalindex[i] == dev.getGlobalindex()) {
						perinfo = dataconn.getPeriphericalInfo((short) dev.getGlobalindex().intValue());
						extUnitInfoT = dataconn.getPeriphericalInfoEx((short) dev.getGlobalindex().intValue());
						deviceBean = deviceList.getDevice(dev.getId());
						if("1".equals(subtab)){
							//subtab1
							append485Value(result, perinfo, deviceBean, dev, isDebugSessionOn, true,paramMap);
						}else{
							//subtab2
							if(paramMap.containsKey(dev.getId()+""))
								appendTheromValue(us.getIdSite(),us.getLanguage(),result, deviceBean, isDebugSessionOn,true,paramMap);
						}
						
					}
				}
			}
		}else{
			for (int i = 0; i < num; i++) {
				dev = (DeviceInfo) device.get(i);
				perinfo = dataconn.getPeriphericalInfo((short) dev.getGlobalindex().intValue());
				deviceBean = deviceList.getDevice(dev.getId());
				if("1".equals(subtab)){
					//subtab1
					append485Value(result, perinfo, deviceBean, dev, isDebugSessionOn, false,paramMap);
				}else{
					//subtab2
					if(paramMap.containsKey(dev.getId()+""))
						appendTheromValue(us.getIdSite(),us.getLanguage(),result, deviceBean, isDebugSessionOn,false,paramMap);
				}
			}
		}
	
		 result.append("</root>");
		return result;
	}
	private void appendDeviceBaseInfo(UserSession us,StringBuffer result,PER_INFO perinfo,ExtUnitInfoT extUnitInfoT, DeviceBean deviceBean, DeviceInfo dev, boolean isDebugSessionOn,boolean isDebugDevice)throws Exception{
		result.append("<dev>");
		result.append("<desc><![CDATA["+deviceBean.getDescription()+"]]></desc>");
		if(us.isMenuActive("dtlview"))
			result.append("<devicetype><![CDATA[<a href='javascript:void(0)' style='cursor:pointer' title='detail page' onclick=top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev="+deviceBean.getIddevice()+"&desc=ncode01') >"+dev.getDevtype()+"</a>]]></devicetype>");
		else
			result.append("<devicetype><![CDATA[<a href='javascript:void(0)' style='cursor:pointer' title='detail page' >"+dev.getDevtype()+"</a>]]></devicetype>");
		result.append("<serialid><![CDATA["+(dev.getCode())+"]]></serialid>");
		result.append("<id><![CDATA["+(dev.getAddress())+"]]></id>");
		result.append("<devid><![CDATA[" + (deviceBean.getIddevice()) + "]]></devid>");
		result.append("<led><![CDATA[" + (UtilDevice.getLedColor(new Integer(deviceBean.getIddevice()))) + "]]></led>");
		result.append("<appliccode><![CDATA["+(perinfo==null?"":perinfo.getApplicCode())+"]]></appliccode>");
		result.append("<release><![CDATA["+(extUnitInfoT==null?"":extUnitInfoT.getFwRelease())+"]]></release>");
		
		String comport = dev.getComport();
		if(comport!=null && comport.trim().length()>0 && comport.contains("COM")){
			Integer comnum = Integer.parseInt(comport.replace("COM", ""));
			if(serial485COM.containsKey(comnum))
			{
				comport = "RS485 - "+serial485COM.get(comnum);
			}
			result.append("<comport><![CDATA["+comport+"]]></comport>");
		}else{
			result.append("<comport><![CDATA["+""+"]]></comport>");
		}
		if (isDebugSessionOn == true && isDebugDevice == true) {
			result.append("<chk><![CDATA[1]]></chk>");
		} else {
			result.append("<chk><![CDATA[0]]></chk>");
		}

		result.append("</dev>");
	}	
	private void append485Value(StringBuffer result,PER_INFO perinfo, DeviceBean deviceBean, DeviceInfo dev, boolean isDebugSessionOn,boolean isDebugDevice,Map params)throws Exception{
		result.append("<dev>");
		result.append("<devid><![CDATA[" + (deviceBean.getIddevice()) + "]]></devid>");
		result.append("<led><![CDATA[" + (UtilDevice.getLedColor(new Integer(deviceBean.getIddevice()))) + "]]></led>");
		if (isDebugSessionOn == true && isDebugDevice == true) {
			result.append("<chk><![CDATA[1]]></chk>");
		} else {
			result.append("<chk><![CDATA[0]]></chk>");
		}
		HashMap<Integer,String[]> omap=DirectorMgr.getInstance().getOfflineMap();
		boolean offline=DeviceStatusMgr.getInstance().isOffLineDevice(dev.getId());
//		offline=getS();
		int c=0;
		if( omap.get(dev.getId())!=null){
			String[] t=omap.get(dev.getId());
			c=Integer.parseInt(t[1]);
			if(("ON".equals(t[0])) && (offline==true)){
				c++;
			}
		}else{
			if(offline==true){
				c++;
			}
		}
		if(!DirectorMgr.getInstance().isDebugSessionOn())
		{
			AlarmLogList list = new AlarmLogList();
			c = list.getOfflineCounter(deviceBean.getIddevice(), DirectorMgr.getInstance().getCarelDLLInitTime());
		}
		omap.put(dev.getId(), new String[]{((offline==true)?"OFF":"ON"),String.valueOf(c)});
		if (isDebugSessionOn == false || (isDebugSessionOn == true && isDebugDevice == true)) {
			result.append("<noanswer><![CDATA[" + (perinfo.getNoAnswerCnt()) + "]]></noanswer>");
			result.append("<errchk><![CDATA[" + (perinfo.getErrChkCnt()) + "]]></errchk>");
			result.append("<nooffline><![CDATA["+c+"]]></nooffline>");
		}
		result.append("</dev>");
	}
	private String getAvgOfSameSetpoint(int idSite,String lang,DeviceBean device,String avgColumn,String setpointColumn)
	throws Exception
	{
		if(avgMap == null)
			avgMap = new HashMap<String,String>();
		VarphyBean var = VarphyBeanList.retrieveVarByCode(idSite, setpointColumn, device.getIddevice(), lang);
		String value = ControllerMgr.getInstance().getFromField(var).getFormattedValue();
		String key = device.getIddevmdl()+"-"+avgColumn+"-"+value;
		if(avgMap.containsKey(key))
			return avgMap.get(key);
		else
		{
			String avgValue = caculateAvgOfSameSetpoint(idSite,lang,device.getIddevmdl(),avgColumn,setpointColumn,value);
			if(avgValue != null)
				avgMap.put(key, avgValue);
			else
			{
				var = VarphyBeanList.retrieveVarByCode(idSite, avgColumn, device.getIddevice(), lang);
				avgValue = ControllerMgr.getInstance().getFromField(var).getFormattedValue();
			}
			return avgValue;
		}
	}
	private String caculateAvgOfSameSetpoint(int idSite,String language,int iddevmdl,String avgColumn,String setpointColumn,String setpoint)
	throws Exception
	{
		VarphyBean[] listSetpoint = VarphyBeanList.getByIddevmdl_varcodes(language,idSite,iddevmdl,new String[]{setpointColumn});
		VarphyBean[] listAvg = VarphyBeanList.getByIddevmdl_varcodes(language,idSite,iddevmdl,new String[]{avgColumn});
		float sum = 0;
		int num = 0;
		for(VarphyBean var:listSetpoint)
		{
			Variable v = ControllerMgr.getInstance().getFromField(var);
			if(v.getFormattedValue().equals(setpoint))
			{
				for(VarphyBean varAvg:listAvg)
				{
					if(varAvg.getDevice().intValue() == var.getDevice().intValue())
					{
						v = ControllerMgr.getInstance().getFromField(varAvg);
						sum += v.getCurrentValue();
						num++;
						break;
					}
				}
			}
		}
		if(num>0)
			return String.valueOf(sum/num);
		else
			return null;
	}
	private void appendTheromValue(int idSite,String language,StringBuffer result, DeviceBean deviceBean, boolean isDebugSessionOn,boolean isDebugDevice,Map params)
	throws Exception
	{
		result.append("<dev>");
		result.append("<devid><![CDATA[" + (deviceBean.getIddevice()) + "]]></devid>");
		result.append("<led><![CDATA[" + (UtilDevice.getLedColor(new Integer(deviceBean.getIddevice()))) + "]]></led>");
		
		if (isDebugSessionOn == true && isDebugDevice == true) {
			result.append("<chk><![CDATA[1]]></chk>");
		} else {
			result.append("<chk><![CDATA[0]]></chk>");
		}

		List varList = (List) varphys.get(deviceBean.getIddevice());
		if ((varphys == null) || (varList == null)) {
			result.append("</dev>");
			return;
		}
		String[] p=(String[])params.get(String.valueOf(deviceBean.getIddevice()));
		if(p!=null && p.length>=16){
			for(int i=0;i<varList.size();i++)
			{
				VarphyBean varphy = (VarphyBean)varList.get(i);
				String value = ControllerMgr.getInstance().getFromField(varphy).getFormattedValue();
				//the p's sequence come from function: DebugBeanList.getVarmdlcodesByides(ids)
	//			value=String.valueOf((int)(Math.random()*100));
				if(varphy.getCodeVar().equals(p[1])){
					//st
					result.append("<st><![CDATA["+value+"]]></st>");
				}else if(varphy.getCodeVar().equals(p[2])){
					//treg
					result.append("<treg><![CDATA["+value+"]]></treg>");
				}else if(varphy.getCodeVar().equals(p[3])){
					//toff
					result.append("<toff><![CDATA["+value+"]]></toff>");
				}else if(varphy.getCodeVar().equals(p[4])){
					//ton
					result.append("<ton><![CDATA["+value+"]]></ton>");
				}else if(varphy.getCodeVar().equals(p[5])){
					//tdef
					result.append("<tdef><![CDATA["+value+"]]></tdef>");
				}else if(varphy.getCodeVar().equals(p[6])){
					//tsat
					result.append("<tsat><![CDATA["+value+"]]></tsat>");
					if(value.equals("***"))
					{
						result.append("<avg_tsat><![CDATA[***]]></avg_tsat>");
					}
					else
					{
						String avgValue = getAvgOfSameSetpoint(idSite,language,deviceBean,p[6],p[1]);
						result.append("<avg_tsat><![CDATA["+avgValue+"]]></avg_tsat>");
					}
				}else if(varphy.getCodeVar().equals(p[7])){
					//tasp
					result.append("<tasp><![CDATA["+value+"]]></tasp>");
				}else if(varphy.getCodeVar().equals(p[8])){
					//Sh
					result.append("<sh><![CDATA["+value+"]]></sh>");
				}else if(varphy.getCodeVar().equals(p[9])){
					//Shset
					result.append("<shset><![CDATA["+value+"]]></shset>");
				}else if(varphy.getCodeVar().equals(p[10])){
					//valv
					result.append("<valv><![CDATA["+value+"]]></valv>");
				}else if(varphy.getCodeVar().equals(p[11])){
					//req
					result.append("<req><![CDATA["+value+"]]></req>");
				}else if(varphy.getCodeVar().equals(p[12])){
					//defr
					result.append("<defr><![CDATA["+value+"]]></defr>");
				}else if(varphy.getCodeVar().equals(p[13])){
					//fa
					result.append("<fa><![CDATA["+value+"]]></fa>");
				}else if(varphy.getCodeVar().equals(p[14])){
					//fb
					result.append("<fb><![CDATA["+value+"]]></fb>");
				}else if(varphy.getCodeVar().equals(p[15])){
					//fc
					result.append("<fc><![CDATA["+value+"]]></fc>");
				}else if(varphy.getCodeVar().equals(p[16])){
					//p1
					result.append("<p1><![CDATA["+value+"]]></p1>");
				}else if(varphy.getCodeVar().equals(p[17])){
					//cop
					result.append("<cop><![CDATA["+value+"]]></cop>");
					if(value.equals("***"))
					{
						result.append("<avg_cop><![CDATA[***]]></avg_cop>");
					}
					else
					{
						String avgValue = getAvgOfSameSetpoint(idSite,language,deviceBean,p[17],p[1]);
						result.append("<avg_cop><![CDATA["+avgValue+"]]></avg_cop>");
					}
				}else if(varphy.getCodeVar().equals(p[18])){
					//cooling
					result.append("<cooling><![CDATA["+value+"]]></cooling>");
				}else if(varphy.getCodeVar().equals(p[19])){
					//consumption
					result.append("<consumption><![CDATA["+value+"]]></consumption>");
				}else if(varphy.getCodeVar().equals(p[20])){
					//th2o_in
					result.append("<th2o_in><![CDATA["+value+"]]></th2o_in>");
				}else if(varphy.getCodeVar().equals(p[21])){
					//th2o_out
					result.append("<th2o_out><![CDATA["+value+"]]></th2o_out>");
				}else if(varphy.getCodeVar().equals(p[22])){
					//h2o_diff
					result.append("<h2o_diff><![CDATA["+value+"]]></h2o_diff>");
				}else if(varphy.getCodeVar().equals(p[23])){
					//comp_speed
					result.append("<comp_speed><![CDATA["+value+"]]></comp_speed>");
				}else if(varphy.getCodeVar().equals(p[24])){
					//liq_inj
					result.append("<liq_inj><![CDATA["+value+"]]></liq_inj>");
				}else if(varphy.getCodeVar().equals(p[25])){
					//envelope
					result.append("<envelope><![CDATA["+value+"]]></envelope>");
				}
			}
		}
		result.append("</dev>");
	}

	private boolean checkExist(DeviceInfo dev, int[] globalindex)
	{
		int index = dev.getGlobalindex();
		for(int j=0;j<globalindex.length;j++)
		{
			if(index == globalindex[j])
			{
				return true;
			}
		}
		return false;
	}
	private int[] getSelectedDeviceid() throws Exception
	{
		if(selectedDeviceid == null) 
		{
			int[] globalindex = DirectorMgr.getInstance().getGlobalindex();
			DeviceCarelInfoList device = new DeviceCarelInfoList(null, BaseConfig.getPlantId());
			selectedDeviceid = new int[globalindex.length];
			DeviceInfo dev = null;
			for (int i = 0; i < globalindex.length; i++) 
			{
				for (int j = 0; j < device.size(); j++) 
				{
					dev = (DeviceInfo) device.get(j);
					if (globalindex[i] == dev.getGlobalindex()) 
					{
						selectedDeviceid[i] = dev.getId();
						break;
					}
				}
			}
		}
		return selectedDeviceid;
	}
	private void updateCarelProtocolDeviceid(UserSession us) throws Exception
	{
		if(iddevs == null)
		{
			DeviceCarelInfoList device = new DeviceCarelInfoList(null, BaseConfig.getPlantId());
			int num = device.size();
			DeviceInfo dev = null;
			iddevs=new int[num];
			for (int i = 0; i < num; i++) {
				dev = (DeviceInfo) device.get(i);
				iddevs[i]=dev.getId();
			}
		}
	}
}
