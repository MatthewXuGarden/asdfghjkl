package com.carel.supervisor.controller.setfield;

import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.field.dataconn.impl.DataConnBase;
import com.carel.supervisor.director.vscheduler.CGroup;

import java.util.GregorianCalendar;

public class VSCallBack extends DefaultCallBack {
	private boolean errors;
	private CGroup objGroup;
	private GregorianCalendar objCalendar;
	
	
	public VSCallBack(CGroup objGroup, GregorianCalendar gc)
	{
		super();
		errors = false;
		this.objGroup = objGroup;
		this.objCalendar = gc; 
	}

	
	public void executeOnError(SetContext setContext, SetWrp wrp)
	{
		if( wrp.getCode() != DataConnBase.SET_MAX && wrp.getCode() != DataConnBase.SET_MIN ) { 
			int idDevice = wrp.getVar().getInfo().getDevice();
			objGroup.onError(idDevice);
		}
	}
	
	
	public boolean continueOnError(SetContext setContext, SetWrp wrp)
	{
		errors = true;
		return true;
	}
	
	
	public void onStart(SetContext setContext)
	{
		if( setContext.isLoggable() )
		{
			EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
					"VS01", new Object[] { setContext.getID(), objGroup.getName() });
		}
	}
	
	
	public void onStop(SetContext setContext)
	{
		if( setContext.isLoggable() )
		{
			EventMgr.getInstance().warning(new Integer(1), setContext.getUser(), "Action",
					"VS03", new Object[] { setContext.getID(), objGroup.getName() });
		}
	}
	
	
	public int onEnd(SetContext setContext)
	{
		int idEvent = -1;
		if( errors )
		{
			if( setContext.isLoggable() )
			{
				idEvent=EventMgr.getInstance().error(new Integer(1), setContext.getUser(), "Action",
						"VS02", new Object[] { setContext.getID(), objGroup.getName() });
			}
		}
		else
		{
			if( setContext.isLoggable() )
			{
				idEvent=EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
						"VS02", new Object[] { setContext.getID(), objGroup.getName() });
			}
		}
		objGroup.onRunEnd(objCalendar);
		return idEvent;
	}	
}
