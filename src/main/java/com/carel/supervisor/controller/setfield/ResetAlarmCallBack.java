package com.carel.supervisor.controller.setfield;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.field.dataconn.impl.DataConnBase;


public class ResetAlarmCallBack extends DefaultCallBack 
{
	private boolean errors = false;
	protected String device = " ; ";
	public ResetAlarmCallBack() 
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
		String sql = "select description from cftableext where tablename='cfdevice' and languagecode=? and" +
				" idsite=1 and tableid=?";
		Integer idDevice = setContext.getFirstDevice();
		if (idDevice.intValue() == 0) //C'è stato un errore in fase di retrieve
		{
			device = " ; ";
		}
		else
		{
			try
			{
				RecordSet r = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{setContext.getLanguagecode(), idDevice});
				device = (String)r.get(0).get(0);
			}
			catch(Exception e)
			{
				device = " ; ";
			}
		}		
		EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
        "W052", new Object[] { ""+setContext.getID(), device});
	}
	
	public void onStop(SetContext setContext)
	{
		EventMgr.getInstance().warning(new Integer(1), setContext.getUser(), "Action",
                "W056", new Object[] { ""+setContext.getID(), device});
	}

	public int onEnd(SetContext setContext)
	{int idEvent=-1;
		if (errors)
		{
			idEvent=EventMgr.getInstance().error(new Integer(1), setContext.getUser(), "Action",
                "W054", new Object[] { ""+setContext.getID(), device});
		}
		else
		{
			idEvent=EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
	                "W054", new Object[] { ""+setContext.getID(), device});
		}
		return idEvent;
	}
	
}
