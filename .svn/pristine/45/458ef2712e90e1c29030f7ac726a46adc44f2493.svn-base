package com.carel.supervisor.presentation.devices;

import java.util.Collection;
import java.util.LinkedList;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ResetSubset
{
	private Collection coll;
	private static boolean initialized = false;
	private static ResetSubset rsss;
	
	private ResetSubset()
	{
		coll = new LinkedList();
		try
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select distinct iddevmdl from cfvarmdl where type = 4 and readwrite='3'");
			for(int i=0;i<rs.size();i++)
			{
				coll.add((Integer)rs.get(i).get(0));
			}
		} catch (DataBaseException e)
		{
			LoggerMgr.getLogger(this.getClass()).error("resetsubset");
		}
	}
	
	public static ResetSubset getInstance()
	{
		if (!initialized)
		{
			rsss = new ResetSubset();
			initialized = true;
		}
		return rsss;
	}
	
	public boolean hasReset(Integer i)
	{
		return coll.contains(i);
	}

	public static boolean isInitialized() {
		return initialized;
	}

	public static void setInitialized(boolean initialized) {
		ResetSubset.initialized = initialized;
	}
	
	
	
}
