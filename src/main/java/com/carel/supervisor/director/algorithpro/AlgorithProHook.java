package com.carel.supervisor.director.algorithpro;

import com.carel.supervisor.director.module.IModule;
import com.carel.supervisor.plugin.algorithmpro.AlgorithmProMgr;

public class AlgorithProHook implements IModule 
{
	private String name = "";
	
	public boolean hookModule(boolean state) 
	{
		if(state)
		{
			// Starting point 
			AlgorithmProMgr.getInstance().startAlgoPro();
		}
		else
		{
			// Stopping point
			AlgorithmProMgr.getInstance().stopAlgoPro();
			
		}
		return true;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
