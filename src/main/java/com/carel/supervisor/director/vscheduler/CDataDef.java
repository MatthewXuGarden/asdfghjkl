package com.carel.supervisor.director.vscheduler;

// contains the xml tokens used
public class CDataDef {
	// user name
	public static final String MY_NAME = "Visual Scheduler";
	
	// root
	public static final String strRootNode = "scheduler";
	public static final String strVersionAttr = "ver";
	public static final String strVersionVal = "1.0.1";
	
	// category
	public static final String strIdAttr = "id";
	public static final String strCategoryNode = "cat";
	public static final String strNameAttr = "name";
	public static final String strIconAttr = "icon";
	
	// group
	public static final String strGroupNode = "grp";
	// group has strIdAttr and strNameAttr also
	public static final String strTypeAttr = "tp";
	public static final String strAnalogVal = "a";
	public static final String strDigitalVal = "d";
	public static final String strMixedVal = "m";
	public static final String strUndefVal = "u";
	public static final String strEnabledAttr = "e";
	public static final String strEnabledVal = "1";
	public static final String strDisabledVal = "0";
	
	// schedule
	public static final String strScheduleNode = "sc";
	// schedule has strTypeAttr also
	public static final String strStandardVal = "s";
	public static final String strExceptionsVal = "e";
	public static final String strDayAttr = "d";
	public static final String strMonthAttr = "m";
	public static final String strYearAttr = "y";
	// exception related attributes/values
	// exception has strEnabledAttr also
	public static final String strRunAttr = "r";
	public static final String strRunOnceVal = "o";
	public static final String strRepetitiveVal = "r";
	
	// interval
	public static final String strIntervalNode = "i";
	public static final String strStartAttr = "s0";
	public static final String strStopAttr = "s1";
	public static final String strDigitalValAttr = "d";
	public static final String strAnalogValAttr = "a";
	
	// change flag; used on xml sent to server, on group and schedule nodes
	// when a schedule node is dirty all its intervals should recreated on db
	// there is no need to pass undefined nodes between client/server 
	public static final String strFlagAttr = "f";
	public static final String strNewVal = "n";
	public static final String strModifiedVal = "m";
	public static final String strRemovedVal = "r";
	public static final String strCleanVal = "c";
	
	// category map flags
	public static final int nReverseLogic = 0x01;
	public static final int nAutoReset = 0x02;
	
	// command
	public static final String strCommandNode = "cmd";
	// command has strTypeAttr and strDigitalValAttr, strAnalogValAttr also
	public static final String strIdCatAttr ="ic";
	public static final String strIdGroupAttr ="ig";
	
	// log
	public static final String strLogNode = "log";
	// log has strTypeAttr also
	public static final String strInfoVal = "i";
	public static final String strWarningVal = "w";
	public static final String strErrorVal = "e";
	public static final String strMessageAttr = "msg";
}
