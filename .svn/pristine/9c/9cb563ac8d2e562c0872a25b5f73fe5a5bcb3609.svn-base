package com.carel.supervisor.dispatcher.enhanced;

import com.carel.supervisor.base.thread.Poller;

public class PVEShield extends Poller
{
	private boolean enable = false;
	private int volte = 36;
	private long sleep = 5000L;
	
	public PVEShield()
	{
	}
	
	public void run()
	{
		System.out.println("Disabilito enhanced...");
		enable = true;
		while(volte > 0)
		{
			try
			{
				Thread.sleep(sleep);
				volte--;
			} 
			catch (InterruptedException e)
			{
			}
		}
		System.out.println("Abilito enhanced...");
		enable = false;
	}
	
	public int getCounter()
	{
		return volte;
	}
	
	public void resetCounter()
	{
		System.out.println("Resettato counter...");
		volte = 24;
	}
	
	public boolean isEnable()
	{
		return enable;
	}
	
	public boolean isDisable()
	{
		return !enable;
	}
}
