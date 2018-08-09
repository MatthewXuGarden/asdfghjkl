package com.carel.supervisor.field.types;


public class PollingInfoT
{
	// types
	public static final short LINE_AVRG_POLLING_TIME = 10;
	public static final short LINE_CURRENT_UNIT = 20;
	public static final short PER_READ_TIME = 30;
	
	// return codes
	public static final short RESPONSE_VALID = 0;
	public static final short PERIF_OUT_RANGE = 100;
	public static final short NO_DEVICE_CONFIGURED = 200;
	public static final short TYPE_UNMANAGED = 300;
	
	// data
	private int value;
	private short returnCode;

	
	public PollingInfoT(int value, short returnCode)
	{
	    this.value = value;
	    this.returnCode = returnCode;
	}
	
	
	public void setValue(int value)
	{
	    this.value = value;
	}	

	
	public int getValue()
	{
		return value;
	}
	
	
	public void setReturnCode(short returnCode)
	{
	    this.returnCode = returnCode;
	}	

	
	public short getReturnCode()
	{
		return returnCode;
	}

}
