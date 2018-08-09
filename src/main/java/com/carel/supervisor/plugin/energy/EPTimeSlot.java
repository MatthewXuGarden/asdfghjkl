package com.carel.supervisor.plugin.energy;


public class EPTimeSlot {
	private String strName;
	private double nCost;
	private String strColor;
		

	public EPTimeSlot(String strName, double nCost, String strColor)
	{
		this.strName = strName;
		this.nCost = nCost;
		this.strColor = strColor;
	}
	
	
	public String getName()					{ return strName; }
	public void setName(String strName)		{ this.strName = strName; }
	
	
	public double getCost()					{ return nCost; }
	public void setCost(double nCost)		{ this.nCost = nCost; }
	
	
	public String getColor()				{ return strColor; }
	public void setColor(String strColor)	{ this.strColor = strColor; }
}
