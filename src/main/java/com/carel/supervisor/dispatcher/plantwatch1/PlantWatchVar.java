package com.carel.supervisor.dispatcher.plantwatch1;

public class PlantWatchVar implements Cloneable{
	//String unitname     = "";
	public String name 		= "";
	public String description 	= "";
	public int type 			= -1;
	public int address 		= -1;
	public boolean dataLogging = false;
	protected Object clone()
	{
		PlantWatchVar me =new PlantWatchVar();
		me.address 		= this.address;
		me.dataLogging 	= this.dataLogging;
		me.description 	= this.description;
		me.name 		= this.name;
		me.type 		= this.type;
		return me;
	}
}
