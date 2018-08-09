package com.carel.supervisor.director.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *	This class contains static data regarding modules.
 *	When you create new function and you want to add it on a existing module or a new module
 *	you have to edit this static data.  
 */
public class PacketStaticData 
{
	// device functions
	public static final int DEVICE_NUMBER = 0;
	public static final int LOGGING_THRESHOLD = 1;
	
	public static final int DEFAULT_DEVICE_NUMBER = 90;
	public static final int DEFAULT_LOGGING_THRESHOLD = 1400;
	
	public static final String TECHNOLOGYSWITCH_SN = "A1TSWITCH123";
	public static final String TECHNOLOGYSWITCH_CODE = "PP2STTSP00";
	
	public static final String FLOATINGLOGIC_PREFIX = "A1FL";
	public static final String FLOATINGLOGIC_CODE = "PP2STFLP00";
	
	public static final String PROTOCOLS_PREFIX = "A1PP";
	public static final String PROTOCOLS_CODE = "PP2STPPP00";
	
	// SVG Maps plugin
	public static final String SVGMAPS_CODE = "PP2STSMP00";
	public static final int SVGMAPS_SW_REQ_LIMIT = 230;
	
	/**
	 * READ
	 * Edit this method to add a new packet.
	 * It is divided in 3 sections:
	 * - LICENSE
	 * - FUNCTION
	 * - KIT
	 */
	
	public static Map <String, String> packet_descr = new ConcurrentHashMap<String,String>();
	public static Map <String, Packet> packetlist = new ConcurrentHashMap<String, Packet>();
	public static Map <String, Packet> packet_to_add = new ConcurrentHashMap<String, Packet>();
	public static List<String> packet_to_hide = new ArrayList<String>();
	static
	{
		//*************************************************************
		//******************************** LICENSE ********************
		//*************************************************************
		/*
		 * Box standard
		 * 90 devices
		 */
		packetlist.put("PP2ST00XE0", new Packet("PP2ST00XE0",Packet.PACKET_DEVICE,"XE",new String[]{"90", "1400"}));
		packet_descr.put("PP2ST00XE0", "Box Standard Installation");
		/*
		 * Touch standard
		 * 90 devices
		 */
		packetlist.put("PP2ST00TE0", new Packet("PP2ST00TE0",Packet.PACKET_DEVICE,"TE",new String[]{"90", "1400"}));
		packet_descr.put("PP2ST00TE0", "Touch Standard Installation");
		/*
		 * Box hyper
		 * 300 devices
		 */
		packetlist.put("PP2ST00XP0", new Packet("PP2ST00XP0",Packet.PACKET_DEVICE,"XP",new String[]{"300", "3500"}));
		packet_descr.put("PP2ST00XP0", "Box Hyper");
		/*
		 * Touch hyper
		 * 300 devices
		 */
		packetlist.put("PP2ST00TP0", new Packet("PP2ST00TP0",Packet.PACKET_DEVICE,"TP",new String[]{"300", "3500"}));
		packet_descr.put("PP2ST00TP0", "Touch Hyper");
		
		//*************************************************************
		//******************************** FUNCTION *******************
		//*************************************************************
		/*
		 * Green Retail
		 * - KPI
		 * - Floating suction
		 * - Energy
		 * - Dew point broadcast
		 * - Safe Restore        (co2)
		 */
		packetlist.put("PP2STGRP00", new Packet("PP2STGRP00",Packet.PACKET_FUNCTION,"GR",new String[]{"co2","kpi","fs","energy","ac"}));
		packet_descr.put("PP2STGRP00", "Green Retail Package");
		/*
		 * Safety
		 * - Parameter control
		 * - Alarm synchronization
		 */
		packetlist.put("PP2STSFP00", new Packet("PP2STSFP00",Packet.PACKET_FUNCTION,"SF",new String[]{"parameters","datatransfer","setio","setaction","setaction2"}));
		packet_descr.put("PP2STSFP00", "Safety Package");
		/*
		 * Extended
		 * - Logical variables
		 * - AlgorithmPRO
		 */
		packetlist.put("PP2STEXP00", new Packet("PP2STEXP00",Packet.PACKET_FUNCTION,"EX",new String[]{"logicdevice","algopro","devloglist"}));
		packet_descr.put("PP2STEXP00", "Extended Package");
		/*
		 * Energy
		 * - KPI
		 * - Energy
		 */
		packetlist.put("PP2STENP00", new Packet("PP2STENP00",Packet.PACKET_FUNCTION,"EN",new String[]{"kpi","energy"}));
		packet_descr.put("PP2STENP00", "Energy Package");
		/*
		 * Saving
		 * - Dew point broadcast (ac)
		 * - Safe Restore        (co2)
		 * - Floating suction    (fs)
		 */
		packetlist.put("PP2STSVP00", new Packet("PP2STSVP00",Packet.PACKET_FUNCTION,"SV",new String[]{"ac","co2","fs"}));
		packet_descr.put("PP2STSVP00", "Saving Package");
		/*
		 * ECO-HVAC 
		 * - Start&Stop
		 * - NightFreeCooling
		 * - Lights
		 */
		packetlist.put("PP2STACP00", new Packet("PP2STACP00",Packet.PACKET_FUNCTION,"AC",new String[]{"opt","opt_startstop","opt_nightfreecooling","opt_lights"}));
		packet_descr.put("PP2STACP00", "ECO-HVAC Package");
		/*
		 * FLoating-Logic --- HIDDEN
		 * - Logical variables
		 * - Floating suction
		 */
		packetlist.put(FLOATINGLOGIC_CODE, new Packet(FLOATINGLOGIC_CODE,Packet.PACKET_FUNCTION,"FL",new String[]{"logicdevice","fs","devloglist"}));
		packet_descr.put(FLOATINGLOGIC_CODE, "Floating-Logic Package");
		packet_to_hide.add(FLOATINGLOGIC_CODE);
		/*
		 * Technology switch --- HIDDEN
		 */
		packetlist.put(TECHNOLOGYSWITCH_CODE, new Packet(TECHNOLOGYSWITCH_CODE,Packet.PACKET_FUNCTION,"TS",new String[]{"switch"}));
		packet_descr.put(TECHNOLOGYSWITCH_CODE, "Technology Switch");
		packet_to_hide.add(TECHNOLOGYSWITCH_CODE);
		/*
		 * Protocols
		 * - Modbus/TCP
		 */
		packetlist.put(PROTOCOLS_CODE, new Packet(PROTOCOLS_CODE,Packet.PACKET_FUNCTION,"PP",new String[]{"lanprot"}));
		packet_descr.put(PROTOCOLS_CODE, "Protocols Package");
		
		/*
		 * Maps
		 * - Svg maps
		 */
		packetlist.put(SVGMAPS_CODE, new Packet(SVGMAPS_CODE,Packet.PACKET_FUNCTION,"SM",new String[]{"svgmaps"}));
		packet_descr.put(SVGMAPS_CODE, "SVG Maps");
		
		//************************************ ************************
		//******************************** KIT ************************
		//************************************ ************************
		/*
		 * Box kit express
		 * - Box standard
		 * - Green Retail
		 * - Safety
		 */
		packetlist.put("PP2STGSXE0", new Packet("PP2STGSXE0",Packet.PACKET_BOTH,"GSXE",new String[]{"PP2ST00XE0","PP2STGRP00","PP2STSFP00"}));
		packet_descr.put("PP2STGSXE0", "Box Kit Express");
		/*
		 * Touch kit express
		 * - Touch standard
		 * - Green Retail
		 * - Safety
		 */
		packetlist.put("PP2STGSTE0", new Packet("PP2STGSTE0",Packet.PACKET_BOTH,"GSTE",new String[]{"PP2ST00TE0","PP2STGRP00","PP2STSFP00"}));
		packet_descr.put("PP2STGSTE0", "Touch Kit Express");
		/*
		 * Box kit super store
		 * - Box standard
		 * - Green Retail
		 * - Safety
		 */
		packetlist.put("PP2STGSXP0", new Packet("PP2STGSXP0",Packet.PACKET_BOTH,"GSXP",new String[]{"PP2ST00XP0","PP2STGRP00","PP2STSFP00"}));
		packet_descr.put("PP2STGSXP0", "Box Kit Superstore");
		/*
		 * Touch kit super store
		 * - Touch standard
		 * - Green Retail
		 * - Safety
		 */
		packetlist.put("PP2STGSTP0", new Packet("PP2STGSTP0",Packet.PACKET_BOTH,"GSTP",new String[]{"PP2ST00TP0","PP2STGRP00","PP2STSFP00"}));
		packet_descr.put("PP2STGSTP0", "Touch Kit Superstore");
		/*
		 * Touch kit AC
		 * - Touch standard
		 * - Extended
		 */
		packetlist.put("PP2STXNTE0", new Packet("PP2STXNTE0",Packet.PACKET_BOTH,"XNTE",new String[]{"PP2ST00TE0","PP2STEXP00"}));
		packet_descr.put("PP2STXNTE0", "Touch Kit AC");
		
	}
	
	/**
	 * READ
	 * 
	 */
	public static Map<String,String> memoryFunctionRestriction = new HashMap<String, String>();
	static
	{
		memoryFunctionRestriction.put("kpi", "*");
		memoryFunctionRestriction.put("ac", "*");
		memoryFunctionRestriction.put("fs", "*");
		memoryFunctionRestriction.put("co2", "*");
		memoryFunctionRestriction.put("energy", "*");
		memoryFunctionRestriction.put("switch", "*");
		memoryFunctionRestriction.put("parameters", "*");
		memoryFunctionRestriction.put("algopro", "*");
		memoryFunctionRestriction.put("logical", "*");
		memoryFunctionRestriction.put("synchro", "*");
		memoryFunctionRestriction.put("logicdevice", "*");
		memoryFunctionRestriction.put("devloglist", "*");
		memoryFunctionRestriction.put("datatransfer", "*");
		memoryFunctionRestriction.put("setio", "tab4name");
		memoryFunctionRestriction.put("setaction", "tab6name");
		memoryFunctionRestriction.put("setaction2", "tab6name");
		memoryFunctionRestriction.put("opt", "*");
		memoryFunctionRestriction.put("opt_startstop", "*");
		memoryFunctionRestriction.put("opt_nightfreecooling", "*");
		memoryFunctionRestriction.put("opt_lights", "*");
	}
}
