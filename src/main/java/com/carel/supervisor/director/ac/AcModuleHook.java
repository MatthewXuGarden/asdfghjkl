package com.carel.supervisor.director.ac;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.director.module.IModule;

public class AcModuleHook implements IModule 
{
	private String name = "";
	
	public boolean hookModule(boolean state) 
	{
		boolean modStarted = false;
		
		if ((new AcProperties()).getProp("ac_running") == 1)
    	{
			if(!state)
			{
				try
				{
					AcManager.getInstance().stopAC("System");
					/*
					 * Se l'utente aveva attivato il modulo, lo risetto come attivo. 
					 */
					AcProperties.updateProp("ac_running", new Integer(1));
					modStarted = true;
				}
				catch (Exception e)
				{
					Logger logger = LoggerMgr.getLogger(this.getClass());
					logger.error(e);
				}
			}
			else
			{
	    		try
	    		{
	    			modStarted = AcManager.getInstance().startAC("System",60000L);
				}
	    		catch (Exception e)
	    		{
	                Logger logger = LoggerMgr.getLogger(this.getClass());
	                logger.error(e);
				}
			}
    	}
		return modStarted;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
