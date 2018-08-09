package com.carel.supervisor.controller.setfield;

import com.carel.supervisor.dataaccess.event.EventMgr;


public class BackGroundCallBack extends OnLineCallBack 
{
	private boolean errors = false;
	
	public BackGroundCallBack() 
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
	
	public int onEnd(SetContext setContext)
	{
		int idEvent=-1;
		if (setContext.isLoggable())
		{
			if (errors)
			{
				idEvent=EventMgr.getInstance().error(new Integer(1), setContext.getUser(), "Action",
		                "W038", new Object[] {""+setContext.getID(), super.device});
			}
			else
			{
				idEvent=EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
		                "W054", new Object[] {""+setContext.getID(), super.device});
			}
		}
		return idEvent;
	}
}
