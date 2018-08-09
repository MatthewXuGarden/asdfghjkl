package com.carel.supervisor.field.types;

public class DoubleValue 
{
	private Double value;
	
	public DoubleValue(Double value)
	{
		this.value  = value;
	}
	
	public DoubleValue()
	{
	}
	
	public Double getValue()
	{
		return value;
	}
	
	public void setValue(Double value)
	{
		this.value = value;
	}
}
