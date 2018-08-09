package com.carel.supervisor.plugin.parameters;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.director.module.IModule;

public class ParamenterModuleHook implements IModule 
{
	private String name = "";
	public boolean hookModule(boolean state) 
	{
		if (state) 
		{
			try 
			{
				ParametersMgr pm = ParametersMgr.getInstance();
				pm.start();
				if (ParametersMgr.getParametersCFG().getEnabled())
					EventMgr.getInstance().info(1, "ParametersPlugin", "parameters", "PM001", new Object[] {});
				
			}
			catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
				EventMgr.getInstance().info(1, "ParametersPlugin", "parameters", "PM003", new Object[] {});
			}
		}
		else
		{
			ParametersMgr pm = ParametersMgr.getInstance();
			pm.stop();
			EventMgr.getInstance().info(1, "ParametersPlugin", "parameters", "PM002", new Object[] {});
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
