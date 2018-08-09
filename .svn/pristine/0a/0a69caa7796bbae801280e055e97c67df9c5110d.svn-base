package com.carel.supervisor.dispatcher.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatcherMonitor 
{
	private static DispatcherMonitor dm = new DispatcherMonitor();
	
	private long limitTimeFirstQueue  = 120000L;
	private long limitTimeSecondQueue = 180000L;
	
	private long firstQueueTimer = 0L;
	private List<String> queueList = null;
	private Map<String,Long> secondQueueTimer = null;
	
	private boolean isActive = false;
	
	private DispatcherMonitor() {
		init();
	}
	
	public static DispatcherMonitor getInstance() {
		return dm;  
	}
	
	public void startMonitor() {
		init();
	}
	
	public void stopMonitor() {
		isActive = false;
	}
	
	public boolean isMonitorActive() {
		return isActive;
	}
	
	public void setFirstQueueTime(long curr) {
		this.firstQueueTimer = curr;
	}
	
	public boolean checkFirstQueue(long now) 
	{
		if(isActive)
			return calculateAndCheck(now,firstQueueTimer,limitTimeFirstQueue);
		else
			return true;
	}
	
	public void setSecondQueueTime(String id,long time)
	{
		if(!queueList.contains(id))
			queueList.add(id);
		secondQueueTimer.put(id,time);
	}
	
	public boolean checkSecondQueue(long now)
	{
		boolean ris = true;
		if(isActive)
		{
			Long value = null;
			for(int i=0; i<queueList.size(); i++)
			{
				value = secondQueueTimer.get(queueList.get(i));
				if(value != null)
				{
					ris = calculateAndCheck(now,value.longValue(),limitTimeSecondQueue);
					if(!ris)
						break;
				}
			}
		}
		return ris;
	}
	
	private void init()
	{
		isActive = true;
		firstQueueTimer = 0L;
		queueList = new ArrayList<String>();
		secondQueueTimer = new HashMap<String, Long>();
	}
	
	private boolean calculateAndCheck(long first, long second,long limit)
	{
		boolean ris = true;
		long dif = first - second;
		if(dif < 0L)
			dif *= -1L;
		if(dif > limit)
			ris = false;
		return ris;
	}
	
	public String toString()
	{
		Long value = null;
		long now = System.currentTimeMillis();
		StringBuffer ret = new StringBuffer("*** DispatcherMonitor *** \r\n");
		ret.append("*** Status -> ");
		ret.append(isActive?" RUNNING ":" FALSE");
		ret.append("\r\n");
		ret.append("*** First queue -> ");
		ret.append(firstQueueTimer);
		ret.append(checkFirstQueue(now)?" GOOD ":" FAILED ");
		ret.append("\r\n");
		for(int i=0; i<queueList.size(); i++)
		{
			value = secondQueueTimer.get(queueList.get(i));
			if(value != null)
			{
				ret.append("*** Second queue on ");
				ret.append(queueList.get(i)+" -> " + value.longValue());
				ret.append(checkSecondQueue(now)?" GOOD ":" FAILED ");
				ret.append("\r\n");
			}
		}
		return ret.toString();
	}
}
