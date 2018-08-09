package com.carel.supervisor.director.packet;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.system.SystemInfoExt;
import com.carel.supervisor.dataaccess.datalog.impl.ModuleBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.support.Information;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable;

public class PacketMgr 
{
	private static final String PLUGIN_TYPE = "P00";
	
	
	private static PacketMgr pmgr = new PacketMgr();
	
	public static final String PVPRO_NATIVE_SW_VERSION = "PVPRO_NATIVE_SW_VERSION";
	public static final String PVPRO_CORE_PROCESSOR = "PVPRO_CORE_PROCESSOR";
	public static final String PVPRO_CORE_PROCESSOR_MULTI = "MULTI";
	
	/*
	 * Memory with all the function allowed
	 */
	private Map<String,String> memoryFunction = null;
	
	private PacketMgr() 
	{
		memoryFunction = new HashMap<String, String>();
	}
	
	public static PacketMgr getInstance() {
		return pmgr;
	}
	
	/*
	 * Check if function is allowed and it should be displayed by menu
	 */
	public boolean checkForMenuRestriction(String functionId)
	{
		boolean ris = false;
		String value = PacketStaticData.memoryFunctionRestriction.get(functionId);
		if(value != null && value.trim().equals("*"))
			ris = true;
		else if(BaseConfig.isDemo())
			ris = true;
		return ris;
	}
	
	/*
	 * Check if function is allowed and it should be displayed by tab
	 */
	public boolean checkForTabRestriction(String functionId,String tabId)
	{
		boolean ris = false;
		String value = PacketStaticData.memoryFunctionRestriction.get(functionId);
		if(value != null && value.trim().equals(tabId))
			ris = true;
		else if(BaseConfig.isDemo())
			ris = true;		
		return ris;
	}
	
	/*
	 * Check if function is allowed
	 */
	public boolean isFunctionAllowed(String functionId)
	{
		boolean ris = false;
		if(memoryFunction.containsKey(functionId))
			ris = true;
		else if(BaseConfig.isDemo())
			ris = true;		
		return ris;
	}
	
	/**
	 * Entry point for packets activation
	 * 
	 */
	public String activePacket(String code,String serial,String activation,int idSite,String lang)
	{
		String ris = "";
		
		Packet p = null;
		
		if(serial.equalsIgnoreCase(PacketStaticData.TECHNOLOGYSWITCH_SN))
			p = PacketStaticData.packetlist.get(PacketStaticData.TECHNOLOGYSWITCH_CODE);
		else
			if(serial.substring(0, 4).equals(PacketStaticData.FLOATINGLOGIC_PREFIX))
				p = PacketStaticData.packetlist.get(PacketStaticData.FLOATINGLOGIC_CODE);
			else
				if(serial.substring(0, 4).equals(PacketStaticData.PROTOCOLS_PREFIX))
					p = PacketStaticData.packetlist.get(PacketStaticData.PROTOCOLS_CODE);
				else
					p = PacketStaticData.packetlist.get(code);
		
		
		if(p != null)
		{
			if(p.getType().equalsIgnoreCase(Packet.PACKET_DEVICE))
			{
				ris = activeDeviceLicense(p,serial,activation);
			}
			else if(p.getType().equalsIgnoreCase(Packet.PACKET_FUNCTION))
			{
				ris = activeFunction(p,serial,activation);
			}
			else if(p.getType().equalsIgnoreCase(Packet.PACKET_BOTH))
			{
				ris = activeDeviceLicenseFunction(p,serial,activation);
			}
			
			if ("ok".equalsIgnoreCase(ris))
			{
				try
				{
					String sql = "insert into cfmodule values (?,?,?,?,?)";
					DatabaseMgr.getInstance().executeStatement(sql, 
							new Object[]{p.getCode(),serial,activation,p.getIdent(),new Timestamp(System.currentTimeMillis())});
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Logger logger = LoggerMgr.getLogger(this.getClass());
					logger.error(e);
				}
				
				EventMgr.getInstance().log(idSite,"System","Start",EventDictionary.TYPE_INFO,"S044",null);
			}
			else
			{
				String msgword = LangMgr.getInstance().getLangService(lang).getString("mgr", ris);
				if ((msgword == null) || ("".equals(msgword)))
					msgword = "unknown";
				EventMgr.getInstance().log(idSite,lang,"Config",EventDictionary.TYPE_ERROR,"S045", msgword);
			}
		}
		return ris;
	}
	
	/**
	 * Method used to activate the max devices number.
	 * 
	 */
	private String activeDeviceLicense(Packet p,String serial,String activation)
	{
		String regist = "";
		try
		{
			// Save previous value
			String prevCp = ProductInfoMgr.getInstance().getProductInfo().get("cp");
			String prevSr = ProductInfoMgr.getInstance().getProductInfo().get("productcode");
			String prevAc = ProductInfoMgr.getInstance().getProductInfo().get("activation");
			
			// Set new value
			ProductInfoMgr.getInstance().getProductInfo().set("cp", p.getIdent());
			ProductInfoMgr.getInstance().getProductInfo().set("productcode", serial);
			ProductInfoMgr.getInstance().getProductInfo().set("activation", activation);
			
			regist = Information.getInstance().validStr();
			if ("ok".equalsIgnoreCase(regist))
			{
				ProductInfoMgr.getInstance().getProductInfo().set("license", p.getDeviceNumber());
				DirectorMgr.getInstance().setPvproValid(true);
				
				// update logging threshold
				ProductInfoMgr.getInstance().getProductInfo().set("logging_threshold", p.getLoggingThreshold());
			}
			else
			{
				/*
				 * In case of error reset license value inside product info table
				 */
				ProductInfoMgr.getInstance().getProductInfo().set("cp", prevCp);
				ProductInfoMgr.getInstance().getProductInfo().set("productcode", prevSr);
				ProductInfoMgr.getInstance().getProductInfo().set("activation", prevAc);
			}
		}
		catch(Exception e)
		{
			regist = "ko";
			e.printStackTrace();
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return regist;
	}
	
	private String activeFunction(Packet p,String serial,String activation)
	{
		String regist = Information.getInstance().validPluginStr(serial,activation,p.getIdent(),
																 SystemInfoExt.getInstance().getMacAddress());
		if ("ok".equalsIgnoreCase(regist))
		{
			loadAllowedFunctionInMemory(p);
		}
		return regist;
	}
	
	private String activeDeviceLicenseFunction(Packet p,String serial,String activation)
	{
		String ris = "";
		String[] packList = p.getFunctionList();
		
		// First packet should be always the device license 
		Packet pck = PacketStaticData.packetlist.get(packList[0]);
		
		// Create temporary copy packet for license
		Packet tmp = new Packet(p.getCode(), pck.getType(), p.getIdent(), pck.getFunctionList());
		ris = activeDeviceLicense(tmp,serial,activation);
		
		if("ok".equalsIgnoreCase(ris))
		{
			for(int i=1; i<packList.length; i++)
			{
				pck = PacketStaticData.packetlist.get(packList[i]);
				tmp = new Packet(p.getCode(),p.getType(),p.getIdent(),pck.getFunctionList());
				ris = activeFunction(tmp,serial,activation);
				if(!"ok".equalsIgnoreCase(ris))
					break;
			}
		}
		return ris;
	}
	
	/**
	 * Load activated modules on system startup
	 */
	public void loadActivePacketOnStartUp()
	{
		try
		{
			// Load from CFMODULE all the activated modules
			Map<String,ModuleBean> active = loadActivatedPacket();
			Iterator<String> i = PacketStaticData.packetlist.keySet().iterator();
			
			//set Custom License
			setCustomLicense();
			//keep custom license and put into packetlist
			keepCustomLicense();
			
			String code = "";
			String[] innerPacketCode = null;
			ModuleBean tmpMbean = null;
			boolean moduleIsValid = false;
			while(i.hasNext())
			{
				moduleIsValid = false;
				code = i.next();
				tmpMbean = active.get(code);
				if(tmpMbean != null)
				{
					// Check if module SN and AC
					moduleIsValid = Information.getInstance().validPlugin(tmpMbean.getSerial(), tmpMbean.getActivation(), 
														  				  tmpMbean.getIdent(), SystemInfoExt.getInstance().getMacAddress());
					if(!moduleIsValid)
						continue;

					Packet p = PacketStaticData.packetlist.get(code);
					if(p != null)
					{
						if(p.getType().equals(Packet.PACKET_BOTH))
						{
							innerPacketCode = p.getFunctionList();
							for(int j=0; j<innerPacketCode.length; j++)
							{
								loadAllowedFunctionInMemory(PacketStaticData.packetlist.get(innerPacketCode[j]));
							}
						}
						else
						{
							loadAllowedFunctionInMemory(p);
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	

	/**
	 * Load packet's functions in memory
	 */
	private void loadAllowedFunctionInMemory(Packet p)
	{
		memoryFunction = p.storeFunctionInMemory(memoryFunction);
	}
	
	public String[] getPacketComboList()
	{
		String[] rets = new String[0];
		try
		{
			Map<String,ModuleBean> active = loadActivatedPacket();
			ConcurrentHashMap<String,Packet> packet_to_show = new ConcurrentHashMap<String, Packet>(PacketStaticData.packetlist);
			removeUnnecessaryCode(packet_to_show);
			
			svgmaps_check(packet_to_show);  //check if svgmaps plugin can be enabled
			
			Iterator<String> i = packet_to_show.keySet().iterator();
			ArrayList<String> ret = new ArrayList<String>();
			String code = "";
			while(i.hasNext())
			{
				code = i.next();
				if(!active.containsKey(code))
					ret.add(code);
			}
			ret = listSort(ret);
			rets = new String[ret.size()];
			for (int j = 0; j < rets.length; j++) {
				rets[j] = ret.get(j);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return rets;
	}
	
	private ArrayList<String> listSort( ArrayList<String> arr){
		ArrayList<String> orderedArr = new ArrayList<String>();
		TreeSet<String> tempArr = new TreeSet<String>();
		
		for (String desc : arr) {
			String me =desc.substring(desc.length()-3, desc.length() );
			if( ! me.equalsIgnoreCase("P00")){
				orderedArr.add(desc);
			}
			else{
				tempArr.add(desc);
			}
		}
		Iterator ite = tempArr.iterator();
		while(ite.hasNext()){
			orderedArr.add((String)ite.next());
		}
		return orderedArr;
	}
	
	public String getPacketTable(String lang, Map<String,String> pkgDesc, int screenWidth,int screenHeight,int width,int height)
	{
		String ret = "ERROR";
		ModuleBean[] list = new ModuleBean[0];
		
		try 
		{
			list = loadActivatedPacketList();
			int rows = list.length;
			
			HTMLElement[][] data = new HTMLElement[rows][];
			String[] ClickRowFunction = new String[rows];
			String[] DBLClickRowFunction = new String[rows];
			for (int i = 0; i < rows; i++)
			{
				data[i] = new HTMLElement[4];
				data[i][0] = new HTMLSimpleElement(list[i].getCode() + " - " + pkgDesc.get(list[i].getCode()));			
				data[i][1] = new HTMLSimpleElement(list[i].getSerial());
				data[i][2] = new HTMLSimpleElement(list[i].getActivation());
				data[i][3] = new HTMLSimpleElement(list[i].getRegistration().toString());
				ClickRowFunction[i] = "";
				DBLClickRowFunction[i] = "";
			}
			String[] headerTable = new String[4];
			headerTable[0] = LangMgr.getInstance().getLangService(lang).getString("mgr", "plugindescription");
			headerTable[1] = LangMgr.getInstance().getLangService(lang).getString("mgr", "plugincode");
			headerTable[2] = LangMgr.getInstance().getLangService(lang).getString("mgr", "activation");
			headerTable[3] = LangMgr.getInstance().getLangService(lang).getString("mgr", "registration");
			
			HTMLTable table = new HTMLTable("module", headerTable, data);
			table.setScreenH(screenHeight);
			table.setScreenW(screenWidth);
			table.setAlignType(new int[]{0, 1, 1, 1});
			table.setWidth(width);
			table.setHeight(height);

			ret = table.getHTMLText();
		}
		catch (Exception e) {
			e.printStackTrace();
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ret;
	}
	
	/**
	 * Load from database (CFMODULE) the list of active packet
	 * and put it into a map
	 */
	private Map<String,ModuleBean> loadActivatedPacket()
		throws Exception
	{
		String sql = "select * from cfmodule";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql, null);
		Map<String,ModuleBean> list = new HashMap<String, ModuleBean>();
		if(rs != null)
		{
			ModuleBean tmp = null;
			for(int i=0; i<rs.size(); i++)
			{
				tmp = new ModuleBean(rs.get(i));
				list.put(tmp.getCode(), tmp);
			}
		}
		return list;
	}
	
	/**
	 * Load from database (CFMODULE) the list of active packet
	 */
	private ModuleBean[] loadActivatedPacketList()
		throws Exception
	{
		String sql = "select * from cfmodule";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql, null);
		ModuleBean[] list = null;
		if(rs != null)
		{
			list = new ModuleBean[rs.size()];
			for(int i=0; i<rs.size(); i++)
			{
				list[i] = new ModuleBean(rs.get(i));
			}
		}
		return list;
	}
	
	private void setCustomLicense()
	{
		Class<?> custom_class = customLicenseInstance();
		if (custom_class!=null)
		{
			Method function = null;
			try 
			{
				function = custom_class.getMethod("setLicense");
				function.invoke(this);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void keepCustomLicense()
	{
		Iterator<String> i = PacketStaticData.packet_to_add.keySet().iterator();
		String code = "";
		while(i.hasNext())
		{
			code = i.next();
			PacketStaticData.packetlist.put(code,PacketStaticData.packet_to_add.get(code));
		}
	}
	
	private void removeUnnecessaryCode(Map<String,Packet> packet_map)  
	{
		Properties  props = new Properties();
		try
		{
			props.load(new FileInputStream("C:\\version.ini"));
		}
		catch (Exception e){
			e.printStackTrace();
		}
		String code_from_ini = props.getProperty("product");
		Iterator<String> i = packet_map.keySet().iterator();
		String code = "";
		while(i.hasNext())
		{
			code = i.next();
			if (!code.endsWith(PLUGIN_TYPE))   //if not plugin code
			{
				if (!code_from_ini.equalsIgnoreCase(code))  // if code not like version.ini code, remove from code to show in combobox
				{
					packet_map.remove(code);
				}
				
			}
		}
		
		//remove voice to hide kept from CustomLicense packet_to_hide (filled in CustomLicense.java)
		for (int c=0;c<PacketStaticData.packet_to_hide.size();c++)
		{
			packet_map.remove(PacketStaticData.packet_to_hide.get(c));
		}
	}
	
	private void svgmaps_check(Map<String,Packet> packet_map)  
	{
		//Enable MAPS only if CPU = MULTICORE and native SW version >= 2.3.0
		String cpu_type,native_version = null;
		cpu_type = System.getenv(PVPRO_CORE_PROCESSOR);
		native_version = System.getenv(PVPRO_NATIVE_SW_VERSION);
		
		boolean disable_svg_maps = true;
		
		if (native_version!=null)
		{
			int nv = Integer.parseInt(native_version.replace(".",""));
			if (cpu_type!=null && cpu_type.equalsIgnoreCase(PVPRO_CORE_PROCESSOR_MULTI))
			{
				if (nv>=PacketStaticData.SVGMAPS_SW_REQ_LIMIT)
					disable_svg_maps = false;
			}
			
		}
		
		if (disable_svg_maps)
			packet_map.remove(PacketStaticData.SVGMAPS_CODE);
	}
	
	/**
	 * Method used by CUSTOM in order to skip the machine registration
	 */
	public void checkAutomaticLicense(String[] params)
	{
		if (((Packet)PacketStaticData.packetlist.get(params[0])).getType().equals(Packet.PACKET_FUNCTION))
			return;
		
		Class<?> custom_class = customLicenseInstance();
		if (custom_class!=null)
		{
			Method function = null;
			try 
			{
				Class[] args1 = new Class[1];
				args1[0] = String[].class;
				function = custom_class.getMethod("setAutoLicense",args1);
				function.invoke(this,(Object)params);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private Class<?> customLicenseInstance()
	{
		Class<?> custom_class = null;
		
		try 
		{
			String strProductCode = BaseConfig.getProductCode();
			String strClassName = 
				"com.carel.supervisor.director.customlicense."
				+ (strProductCode != null ? strProductCode : "Custom") + "License";  
			
			custom_class = Class.forName(strClassName);
		}
		catch (Exception e) {
			LoggerMgr.getLogger(PacketMgr.class).info("No custom License");
		}
		return custom_class;
	}
}
