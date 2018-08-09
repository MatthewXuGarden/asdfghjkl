package com.carel.supervisor.controller.setfield;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;

public class MultiCastCallBack implements ISetCallBack
{
	private boolean errors = false;
	protected String device = " ; ";
	
	public MultiCastCallBack() 
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
		if (setContext.isLoggable())
		{
			String sql = "select description from cftableext where tablename='cfdevice' and languagecode=? and" +
			" idsite=1 and tableid=?";
			Integer idDevice = setContext.getFirstDevice();
			if (idDevice.intValue() == 0) //C'� stato un errore in fase di retrieve
			{
				device = " ; ";
			}
			else
			{
				try
				{
					RecordSet r = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{setContext.getLanguagecode(), idDevice});
					if(r.size()>0){
						device = (String)r.get(0).get(0);
					}else{
						device = " ; ";
					}
				}
				catch(Exception e)
				{
					device = " ; ";
				}
			}
			EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
	                "W052", new Object[] {""+setContext.getID(), device});
		}
	}
	
	public int onEnd(SetContext setContext)
	{int idEvent=-1;
		if (setContext.isLoggable())
		{
			if (errors)
			{
				idEvent=EventMgr.getInstance().error(new Integer(1), setContext.getUser(), "Action",
	                "W054", new Object[] {""+setContext.getID(), device});
			}
			else
			{
				idEvent=EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
		                "W054", new Object[] {""+setContext.getID(), device});
			}
		}
		return idEvent;
	}
	
	public void onStop(SetContext setContext)
	{
		if (setContext.isLoggable())
		{
			EventMgr.getInstance().warning(new Integer(1), setContext.getUser(), "Action",
					"W055", new Object[] {""+setContext.getID()});
		}
	}
}
