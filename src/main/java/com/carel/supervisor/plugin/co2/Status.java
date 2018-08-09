package com.carel.supervisor.plugin.co2;

public class Status {
	public static final int STATUS_NOT_AVAILABLE		= 0;
	public static final int STATUS_RUNNING_CONDITION	= 1;
	public static final int STATUS_SAFE_CONDITION		= 2;
	public static final int STATUS_GROUP_DISABLED		= 3;
	
	public int id;	// rack/group id
	public int st;	// status
}
