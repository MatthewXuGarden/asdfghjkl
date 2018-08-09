package com.carel.supervisor.dispatcher.plantwatch1;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class PlantWatchAlarmList {
	private Map mp_PlantWatchAlarms = Collections.synchronizedMap( new TreeMap());
	private static int key = 0;
	public synchronized void Add(	String type,
									int address,
									int unit,
									String eventDescription,
									Date date)
	{
		PlantWatchAlarm rec = new PlantWatchAlarm();
		if(rec!=null)
		{
			rec.type = rec.type;
			rec.date = date;
			rec.eventDescription = eventDescription;
			rec.unit = unit;
			rec.address = address;
			String skey= Integer.toString( this.key);
			mp_PlantWatchAlarms.put( skey, rec);
			this.key++;
		}
	}
	/**
	 * @return Returns the mp_PlantWatchAlarms.
	 */
	public Map getMp_PlantWatchAlarms() {
		return mp_PlantWatchAlarms;
	}
	
}
