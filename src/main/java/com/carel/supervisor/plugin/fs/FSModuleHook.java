package com.carel.supervisor.plugin.fs;

import com.carel.supervisor.director.module.IModule;

public class FSModuleHook implements IModule 
{
	private String name = "";
	public boolean hookModule(boolean state) 
	{
		if (state) 
		{
			try 
			{
				FSManager mgr = FSManager.getInstance();
				FSManager.loadFSProperties();
				if (FSManager.getStart().equals(1))
					mgr.startFS("System");
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		} 
		else 
		{
			FSManager.getInstance().stopFS("System");
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