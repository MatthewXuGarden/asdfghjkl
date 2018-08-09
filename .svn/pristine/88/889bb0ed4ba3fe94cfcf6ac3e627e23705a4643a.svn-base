package com.carel.supervisor.controller.setfield;

import com.carel.supervisor.dataaccess.event.EventMgr;

public class DefaultCallBack implements ISetCallBack
{

	private boolean errors = false;
	public DefaultCallBack() 
	{

	}

	public void executeOnError(SetContext setContext, SetWrp wrp)
	{
		
	}
	
	public void executeOnOk(SetContext setContext, SetWrp wrp)
	{
		
	}
	
	public boolean continueOnError(SetContext setContext, SetWrp wrp)
	{
		errors = true;
		return true;
	}
	
	public void onStart(SetContext setContext)
	{
	}
	
	public int onEnd(SetContext setContext)
	{
		int idEvent=-1;
		if (setContext.isLoggable())
		{
			if (errors)
			{
				idEvent=EventMgr.getInstance().error(new Integer(1), setContext.getUser(), "Action",
	                "W054", new Object[] { ""+setContext.getID()});
			}
			else
			{
				idEvent=EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
		                "W054", new Object[] { ""+setContext.getID()});
			}
		}
		return idEvent;
	}
	
	public void onStop(SetContext setContext)
	{
	}
}
