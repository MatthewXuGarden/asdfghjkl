package com.carel.supervisor.director.packet;

import java.util.Map;

public class Packet 
{
	public static final String PACKET_DEVICE = "D";
	public static final String PACKET_FUNCTION = "F";
	public static final String PACKET_BOTH = "B";

	private String code = "";
	private String type = "";
	private String ident = "";
	private String[] functionList = new String[0];

	public Packet(String code,String type,String ident,String[] functionList) 
	{
		this.code = code;
		this.type = type;
		this.ident = ident;
		this.functionList = functionList;
	}
	
	public String getDeviceNumber()
	{
		String ret = "";
		try 
		{
			if((this.type.equalsIgnoreCase(Packet.PACKET_DEVICE)))
				ret = functionList[PacketStaticData.DEVICE_NUMBER];
			else
				ret = "0";
		}
		catch(Exception e){
			ret = "0";
		}
		return ret;
	}
	

	public String getLoggingThreshold()
	{
		return getDeviceFunction(PacketStaticData.LOGGING_THRESHOLD);
	}
	
	
	public String getDeviceFunction(int id)
	{
		String ret = null;
		try {
			if( (this.type.equalsIgnoreCase(Packet.PACKET_DEVICE)) )
				ret = functionList[id];
		}
		catch(Exception e) {
		}
		return ret;
	}
	
	
	public Map<String,String> storeFunctionInMemory(Map<String,String> memory)
	{
		for(int i=0; i<functionList.length; i++)
		{
			if(this.type.equalsIgnoreCase(Packet.PACKET_DEVICE) && (i==0 || i==1))
				continue;
			memory.put(functionList[i],"OK");
		}
		return memory;
	}
	
	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getFunctionList() {
		return functionList;
	}

	public void setFunctionList(String[] functionList) {
		this.functionList = functionList;
	}
}
