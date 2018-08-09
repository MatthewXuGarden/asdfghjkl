package com.carel.supervisor.controller.setfield;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.event.VariableHelper;


public class CmdCallBack extends DefaultCallBack 
{
	private String[] descs = null;
	
	public CmdCallBack() 
	{
		super();
	}

	public void executeOnError(SetContext setContext, SetWrp wrp)
	{
	}
	
	
	public boolean continueOnError(SetContext setContext, SetWrp wrp)
	{
		return true;
	}
	
	public void onStart(SetContext setContext)
	{
		if (setContext.isLoggable())
		{
			try
			{
				Iterator<Integer> iterator = setContext.keys();
				Integer key = null;
				key = (Integer)iterator.next();
				List<SetWrp> variables = setContext.get(key);
				SetWrp wrp = (SetWrp)variables.get(0);
				int idvar = wrp.getVar().getInfo().getId();	
				
				Map<Integer, String[]> map = VariableHelper.getDescriptions(setContext.getLanguagecode(),1,new int[]{idvar});
				iterator = map.keySet().iterator();
				key = (Integer)iterator.next();
				descs = (String[])map.get(key);
			}
			catch(Exception e)
			{
				descs = new String[]{" "," ; "};
			}
			EventMgr.getInstance().info(new Integer(1), setContext.getUser(), "Action",
	                "W057", new Object[] { ""+setContext.getID(), descs[1], descs[0]});
		}
	}
	
	public void onStop(SetContext setContext)
	{
		if (setContext.isLoggable())
		{
			EventMgr.getInstance().warning(new Integer(1), setContext.getUser(), "Action",
					"W058", new Object[] { ""+setContext.getID(), descs[1], descs[0]});
		}
	}

	public int onEnd(SetContext setContext)
	{
		return -1;
	}
	
}
