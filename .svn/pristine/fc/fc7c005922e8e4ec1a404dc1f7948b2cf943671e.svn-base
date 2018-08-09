package com.carel.supervisor.controller.setfield;

import com.carel.supervisor.controller.priority.PriorityMgr;


public class SetDequeuerMgr 
{
	private SetDequeuer dequeuer = null;
	private static SetDequeuerMgr me = new SetDequeuerMgr();
	private boolean isWorking = false;
	
	private SetDequeuerMgr() 
	{	
	}

	public static SetDequeuerMgr getInstance()
	{
		return me;
	}
	
	public synchronized void startSetter()
	{
		if (dequeuer == null)
		{
			dequeuer = new SetDequeuer();
			dequeuer.startPoller();
		}	
	}
	
	public synchronized void stopSetter()
	{
		if (dequeuer != null)
		{
			dequeuer.stopPoller();
			dequeuer = null;
		}
	}
	
	public synchronized void add(SetContext setContext)
	{
		add(setContext, PriorityMgr.getInstance().getDefaultPriority()); //valore di priority generico di default
	}
	
	public synchronized void add(SetContext setContext, int priority)
	{
		if (dequeuer != null)
		{
			//dequeuer.add(setContext); //old version
			dequeuer.add(setContext, priority);
		}
	}
	
	//Plugin Controllo Parametri - add per eventi di rollback!
	public synchronized void add(SetContext setContext, int priority, boolean isRollback)
	{
		if (dequeuer != null)
		{
			//dequeuer.add(setContext); //old version
			dequeuer.add(setContext, priority, isRollback);
		}
	}
	
	public synchronized void dequeueAllByPriority(int classPriority)
	{
		if (dequeuer != null)
		{
			dequeuer.delAllByPriority(classPriority);
		}
	}
	
	public boolean isWorking()
	{
		return isWorking;
	}
	
	protected void setWorking(boolean isWorking)
	{
		this.isWorking = isWorking;
	}
}
