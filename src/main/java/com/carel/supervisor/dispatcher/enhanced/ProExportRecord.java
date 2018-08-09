package com.carel.supervisor.dispatcher.enhanced;

public class ProExportRecord
{
	private int gid;
	private int address;
	private int type;
	private int powerdegree;
	private String description;
	
	public ProExportRecord(int gid, int add, int type, int powerdeg, String description)
	{
		this.gid = gid;
		this.address = add;
		this.type = type;
		this.powerdegree = powerdeg;
		this.description = description;
	}

	public int getAddress()
	{
		return address;
	}

	public String getDescription()
	{
		return description;
	}

	public int getGid()
	{
		return gid;
	}

	public int getPowerdegree()
	{
		return powerdegree;
	}

	public int getType()
	{
		return type;
	}
}
