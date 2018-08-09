package com.carel.supervisor.controller.setfield;

import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.field.dataconn.impl.DataConnBase;


public class GroupCallBack extends DefaultCallBack 
{
	private boolean errors = false;

	public GroupCallBack() 
	{
		super();
	}

	public void executeOnError(SetContext setContext, SetWrp wrp)
	{
	}
	
	
	public boolean continueOnError(SetContext setContext, SetWrp wrp)
	{
		if (wrp.getCode() == DataConnBase.READ_TIMEOUT)
		{
			errors = true;
			return true;
		}
		errors = true;
		return false;
	}
	
	public void onStart(SetContext setContext)
	{
		if (setContext.isLoggable())
		{		
			EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
	        "W050", new Object[] { ""+setContext.getID()});
		}
	}
	
	public void onStop(SetContext setContext)
	{
		if (setContext.isLoggable())
		{
			EventMgr.getInstance().warning(new Integer(1), setContext.getUser(), "Action",
					"W055", new Object[] { ""+setContext.getID()});
		}
	}

	public int onEnd(SetContext setContext)
	{int idEvent=-1;
		if (errors)
		{
			if (setContext.isLoggable())
			{
				idEvent=EventMgr.getInstance().error(new Integer(1), setContext.getUser(), "Action",
	                "W051", new Object[] { ""+setContext.getID()});
			}
			NotificationParam.getInstance().addMsg(setContext.getUser(), "errorpar");
		}
		else
		{
			if (setContext.isLoggable())
			{
				idEvent=EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
		                "W051", new Object[] { ""+setContext.getID()});
			}
			NotificationParam.getInstance().addMsg(setContext.getUser(), "okpar");
		}
		return idEvent;
	}
	
}
