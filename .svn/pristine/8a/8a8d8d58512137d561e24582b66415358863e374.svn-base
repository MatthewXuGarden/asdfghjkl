package com.carel.supervisor.controller.setfield;

import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.director.ac.AcGroups;


public class AcCallBack extends OnLineCallBack 
{
	private boolean errors = false;
	private String descr = "";
	
	public AcCallBack() 
	{
		super();
	}

	public void setDescr(Integer idmaster)
	{
		if (idmaster != null)
		{
			this.descr = "master: " + AcGroups.getGroupName(idmaster);
		}
		else
		{
			this.descr = "DP Heartbits";
		}
	}
	
	public void onStart(SetContext setContext)
	{
		if (setContext.isLoggable())
		{
			EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "ac",
	        "W052", new Object[] {""+setContext.getID(), this.descr});
		}
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
				idEvent=EventMgr.getInstance().error(new Integer(1), setContext.getUser(), "ac",
		                "W054", new Object[] {""+setContext.getID(), this.descr});
			}
			else
			{
				idEvent=EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "ac",
		                "W054", new Object[] {""+setContext.getID(), this.descr});
			}
		}
		return idEvent;
	}
	
	public void onStop(SetContext setContext)
	{
		if (setContext.isLoggable())
		{
			EventMgr.getInstance().warning(new Integer(1), setContext.getUser(), "ac",
					"W056", new Object[] {""+setContext.getID(), this.descr});
		}
	}
}
