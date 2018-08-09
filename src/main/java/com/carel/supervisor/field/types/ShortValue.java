package com.carel.supervisor.field.types;

public class ShortValue 
{
	private short value;
	
	public ShortValue(short value)
	{
		this.value  = value;
	}
	
	public ShortValue()
	{
	}
	
	public short getValue()
	{
		return value;
	}
	
	public void setValue(short value)
	{
		this.value = value;
	}
}
