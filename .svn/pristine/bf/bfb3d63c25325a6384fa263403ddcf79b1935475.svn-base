package com.carel.supervisor.dispatcher.plantwatch1;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class PlantWatchHistoryVarList {
	private Map mp_HistoryVarList = Collections.synchronizedMap( new TreeMap());
	
	public synchronized void Add(Date date, Double value) 
	{
		PlantWatchHistoryVar rec = new PlantWatchHistoryVar();
		
		if(rec != null)
		{
			rec.date = date; 
			rec.value   = value;
			mp_HistoryVarList.put( date,rec);
		}
	}

	/**
	 * @return Returns the mp_HistoryVarList.
	 */
	public Map getMp_HistoryVarList() {
		return mp_HistoryVarList;
	}
	
	
}
