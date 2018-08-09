package com.carel.supervisor.controller.setfield;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.dataaccess.datalog.impl.NoteLog;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.field.dataconn.impl.DataConnBase;


public class OnLineCallBack extends DefaultCallBack 
{
	private boolean errors = false;
	protected String device = " ; ";
	private SetParam setParam = null;
	
	
	public OnLineCallBack() 
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
					device = (String)r.get(0).get(0);
				}
				catch(Exception e)
				{
					device = " ; ";
				}
			}
			
			int idevent = EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
	        "W052", new Object[] { ""+setContext.getID(), device});
			
			// set value with note
			
			String strValueNote = ProductInfoMgr.getInstance().getProductInfo().get("value_note");
			boolean bValueNote = strValueNote != null && (strValueNote.equalsIgnoreCase("yes") || strValueNote.equalsIgnoreCase("true"));
			if(bValueNote && setContext.getNote()!=null){
				 NoteLog noteLog = new NoteLog();
	             noteLog.setLastTime(new Timestamp(System.currentTimeMillis()));
	             noteLog.setStartTime(new Timestamp(System.currentTimeMillis()));

	             if (setContext.getNote() != null)
	             {
	                 noteLog.setNote(setContext.getNote());
	             }
	             else
	             {
	                 noteLog.setNote("");
	             }
	             noteLog.setUserNote(setContext.getUser());
	             noteLog.setTableName("hsevent");
	             noteLog.setTableId(idevent);
	             try {
					noteLog.save(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
	}

	
	public void onStop(SetContext setContext)
	{
		if (setContext.isLoggable())
		{
			EventMgr.getInstance().warning(new Integer(1), setContext.getUser(), "Action",
					"W056", new Object[] { ""+setContext.getID(), device});
		}
	}

	
	public int onEnd(SetContext setContext)
	{
		int idEvent=-1;
		if (errors)
		{
			if (setContext.isLoggable())
			{
				idEvent=EventMgr.getInstance().error(new Integer(1), setContext.getUser(), "Action",
	                "W054", new Object[] { ""+setContext.getID(), device});
			}
			NotificationParam.getInstance().addMsg(setContext.getUser(), "errorpar");
		}
		else
		{
			if (setContext.isLoggable())
			{
				idEvent=EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
		                "W054", new Object[] { ""+setContext.getID(), device});
			}
			if( setParam == null || setParam.isComplete() )
				NotificationParam.getInstance().addMsg(setContext.getUser(), "okpar");
			if( setParam != null && !setParam.isComplete() )
				setParam.next();
		}
		return idEvent;
	}
	
	
	public void registerSetParam(SetParam setParam)
	{
		this.setParam = setParam;
	}
}
