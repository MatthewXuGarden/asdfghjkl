package com.carel.supervisor.plugin.optimum;

import com.carel.supervisor.director.module.IModule;

public class OptimumModuleHook implements IModule 
{
	private String name = "";
	public boolean hookModule(boolean state) 
	{
		if (state) 
		{
			try 
			{
				OptimumManager.getInstance().startOptimum("System");
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		} 
		else 
		{
			OptimumManager.getInstance().stopOptimum("System");
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