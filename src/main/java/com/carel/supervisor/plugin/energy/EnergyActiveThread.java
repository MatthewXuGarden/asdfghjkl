package com.carel.supervisor.plugin.energy;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.controller.setfield.EGSetCallback;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.controller.setfield.SetWrp;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;

public class EnergyActiveThread extends Poller {

	public int contatorenumeri = 0;
	private boolean continuerun;
	private long pollingtime;
	private static int contathread = 0;
	private int[] prevtime;
	private int[] prevday;
	private EnergyMgr emgr;
	private Boolean[] scheduledvalue;
	
	public EnergyActiveThread(long pollingtime) {
		super();
		setName("EnergyActiveThread_"+(contathread++));
		contatorenumeri = 0;
		this.continuerun=true;
		this.pollingtime = pollingtime;
		emgr = EnergyMgr.getInstance();
		prevtime = new int[emgr.getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10)+1];
		prevday = new int[emgr.getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10)+1];
		scheduledvalue = new Boolean[emgr.getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10)+1];
		for(int i=0;i<prevtime.length;i++){
			prevtime[i] = -1;
			prevday[i] = -1;
			scheduledvalue[i] = null;
		}
	}

	public void run() {
		while (continuerun) {
			try {
				Thread.sleep(pollingtime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (EnergyMgr.activemonitor) {
				try {
					emgr = EnergyMgr.getInstance();
					Integer groups = emgr.getIntegerProperty(EnergyConfiguration.NUMGROUPS,10);
					for (int i = 1; i <= groups; i++) {
						try {
							// Scheduler
							if (isTime(i)) {
								setActiveValue(i, scheduledValue(i));
							}
						} catch (Exception ex) {
							LoggerMgr.getLogger(this.getClass()).error(ex);
						}
					}
				} catch (Exception e) {
					LoggerMgr.getLogger(this.getClass()).error(e);
				}
			}
		}
	}
	
	private boolean scheduledValue(int i) {
		return scheduledvalue[i];
	}

	public boolean isTime(int idgrp)
	{
		//disabled group or active device
		if(emgr.getSiteConfiguration().getGroup(idgrp)!=null &&
				!emgr.getSiteConfiguration().getGroup(idgrp).isEnabled())
			return false;
		if(emgr.getSiteConfiguration().getActiveDevice(idgrp)!=null &&
				!emgr.getSiteConfiguration().getActiveDevice(idgrp).isEnabled())
			return false;
		//
		boolean toret = false;
		HashMap<String, Integer> sched; 
		GregorianCalendar c = new GregorianCalendar();
		if(prevtime[idgrp]==-1)
		{
			prevtime[idgrp] = c.get(Calendar.HOUR_OF_DAY)*10000+c.get(Calendar.MINUTE)*100+c.get(Calendar.SECOND);
			return false;
		}
			int day = (c.get(Calendar.DAY_OF_WEEK)+6)%7;
			day=day==0?7:day;
			sched = emgr.getSchedulerConfiguration().getScheduling(idgrp, day);
		if(sched==null)
			return false;
		
		int currtime = c.get(Calendar.HOUR_OF_DAY)*10000+c.get(Calendar.MINUTE)*100+c.get(Calendar.SECOND);
		int currday = c.get(Calendar.DATE);
		
		String[] hh = {"on_1","off_1","on_2","off_2"};
		for(int i = 0;i<4;i++)
		{
			if(sched.get(hh[i])==-1) 
				continue;
			if((prevtime[idgrp] <= sched.get(hh[i]) && sched.get(hh[i]) <= currtime)){
				scheduledvalue[idgrp] = "on".equalsIgnoreCase(hh[i].substring(0, 2))?true:false;
				toret = true;
			} else if(prevtime[idgrp]>currtime && prevday[idgrp] != currday){ 
				if ( ((prevtime[idgrp] - 240000) <= sched.get(hh[i]) && sched.get(hh[i]) <= currtime) ||
					 (prevtime[idgrp] <= sched.get(hh[i]) && sched.get(hh[i]) <= ( currtime+240000 )) ) {
					scheduledvalue[idgrp] = "on".equalsIgnoreCase(hh[i].substring(0, 2))?true:false;
					toret = true;
				}
			}
		}
		prevtime[idgrp]=currtime;
		prevday[idgrp]=currday;
		return toret;
	}

	public void setActiveValue(int idgrp, boolean on)
	{
		int var = 0;
		int value = 0;
		
		EnergyActiveDevice dev = emgr.getSiteConfiguration().getActiveDevice(idgrp);
		
		if(dev!=null)
		{
			SetContext setContext = new SetContext();
			String lang = "EN_en";
    		try
    		{
    			lang = LangUsedBeanList.getDefaultLanguage(1);
    		}
    		catch (Exception e)
    		{
    		}
    		setContext.setLanguagecode(lang);
			setContext.setUser("Energy");
			setContext.setCallback(new EGSetCallback());
			
				if (dev.isEnabled())
				{
					//lights
					var = dev.getIdvar();
					value = dev.getValue(on);
					
					if(var!=-1 && value!=-1)
					{
						SetWrp sw = setContext.addVariable(var, value);
						sw.setCheckChangeValue(false);
					}
				}
			SetDequeuerMgr.getInstance().add(setContext);
		}
	}
}
