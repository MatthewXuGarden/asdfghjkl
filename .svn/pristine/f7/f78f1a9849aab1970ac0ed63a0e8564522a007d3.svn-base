//
//
//classe da cestinare
//
//
package com.carel.supervisor.plugin.energy;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.director.lightnight.LightNightMgr;

public class EnergySchedulerClock implements Runnable
{
	private static final int GROUPS = 6;
	private boolean continuerun;
	private long pollingtime;
	
	public EnergySchedulerClock(long pollingtime)
	{
		continuerun=true;
		this.pollingtime = pollingtime;
	}

	public void run()
	{
		while(continuerun)
		{
			try
			{
				Thread.sleep(pollingtime);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			synchronized (LightNightMgr.monitor)
			{
				try
				{
					LightNightMgr lnm = LightNightMgr.getInstance();
					for(int i=1;i<=GROUPS;i++)
					{
						try
						{
							if(!lnm.getForceManual(i))
							{
								//Scheduler
								if(lnm.isTime(i))
								{
									if(lnm.isLightsNightEnabled(i))
									{
										LoggerMgr.getLogger(this.getClass()).info("Set scheduled lightsnight group "+i+" "+lnm.scheduledValue(i));
										lnm.setLightsNight(i, lnm.scheduledValue(i));
									}
									if(lnm.isOnOffEnabled(i))
									{
										LoggerMgr.getLogger(this.getClass()).info("Set scheduled onoff group "+i+" "+lnm.scheduledValue(i));
										lnm.setOnOff(i, lnm.scheduledValue(i));
									}
								}
								//Variabili
								if(lnm.isField(i))
								{
									if(lnm.isLightsNightEnabled(i))
									{
										LoggerMgr.getLogger(this.getClass()).info("Set field lightsnight group "+i+" "+lnm.getFieldOnOff(i));
										lnm.setLightsNight(i, lnm.getFieldOnOff(i));
									}
									if(lnm.isOnOffEnabled(i))
									{
										LoggerMgr.getLogger(this.getClass()).info("Set field onoff group "+i+" "+lnm.getFieldOnOff(i));
										lnm.setOnOff(i, lnm.getFieldOnOff(i));
									}
								}
							}
						}
						catch(Exception ex)
						{
							LoggerMgr.getLogger(this.getClass()).error(ex);
						}
					}
				}
				catch(Exception e)
				{
					LoggerMgr.getLogger(this.getClass()).error(e);
				}
			}
		}
	}
	
	public void setStop()
	{
		continuerun = false;
	}
}
