package com.carel.supervisor.dispatcher;

public class DispatcherID 
{
	public static final int INITIAL_STATE = -99;
	
	public static final int COMMBASE_NOTLOADED = 2;
	
	public static final int DLLFAX_NOTLOADED  = 3;
	public static final int DLLSMS_NOTLOADED  = 4;
	public static final int DLLMAIL_NOTLOADED = 5;
	public static final int DLLDIAL_NOTLOADED = 6;
	public static final int DLLRAS_NOTLOADED  = 7;
	public static final int DLLPING_NOTLOADED = 8;
	
	public static final int CMDFAX_NOTRUN  = 9;
	public static final int CMDSMS_NOTRUN  = 10;
	public static final int CMDMAIL_NOTRUN = 11;
	public static final int CMDDIAL_NOTRUN = 12;
	public static final int CMDRAS_NOTRUN  = 13;
	public static final int CMDPING_NOTRUN = 14;
	
	// Fax
	public static final int FAX_ABORTING = 271;
	
	// Mail
	public static final int MAIL_NOT_SEND = 900;
	
	// Ping
	public static final int PING_OK = 800;
	public static final int PING_KO = 801;
	
	// General
	public static final int TIMEOUT_SERVICE = 98;
	public static final int PARAM_NUM_ERR = 99;
	
	// Ras
	public static final int MODEM_CONF_OK = 97;
	public static final int MODEM_CLOSE_OK = 96;
}
