package com.carel.supervisor.controller.setfield;

import com.carel.supervisor.dataaccess.event.EventMgr;

public class FSSetCallback extends DefaultCallBack 
{
	private boolean errors = false;
	public FSSetCallback() 
	{
		super();
	}

	public void executeOnError(SetContext setContext, SetWrp wrp)
	{
	}
	
	public boolean continueOnError(SetContext setContext, SetWrp wrp)
	{
		errors = true;
		return true;
	}
	
	public void onStart(SetContext setContext)
	{
		if (setContext.isLoggable())
		{
			EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "fs",
					"W050", new Object[] { ""+setContext.getID()});
		}
	}
	
	public void onStop(SetContext setContext)
	{
		if (setContext.isLoggable())
		{
			EventMgr.getInstance().warning(new Integer(1), setContext.getUser(), "fs",
					"W055", new Object[] { ""+setContext.getID()});
		}
	}
	
	public int onEnd(SetContext setContext)
	{int idEvent=-1;
		if (errors)
		{
			if (setContext.isLoggable())
			{
				idEvent=EventMgr.getInstance().error(new Integer(1), setContext.getUser(), "fs",
						"W051", new Object[] { ""+setContext.getID()});
			}
		}
		else
		{
			if (setContext.isLoggable())
			{
				idEvent=EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "fs",
						"W051", new Object[] { ""+setContext.getID()});
			}
		}
		return idEvent;
	}
}
