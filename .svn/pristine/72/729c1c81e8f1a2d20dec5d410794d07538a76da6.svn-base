package com.carel.supervisor.plugin.co2;

import com.carel.supervisor.director.module.IModule;

public class CO2SavingModuleHook implements IModule 
{
	private String name = "CO2Saving";
	
	
	public boolean hookModule(boolean state) 
	{
		if( state ) {
			CO2SavingManager.getInstance().loadConfig();
			if( CO2SavingManager.getInstance().isEnabled() )
				CO2SavingManager.getInstance().start("System");
		}
		else
			CO2SavingManager.getInstance().stop("System");
		return true;
	}
	
	
	public String getName() {
		return name;
	}
	

	public void setName(String name) {
		this.name = name;
	}
}
