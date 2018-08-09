package com.carel.supervisor.director.lightnight;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.LNSetCallback;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.controller.setfield.SetWrp;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;

public class LightNightMgr
{
	private static LightNightMgr me;
	private LNConfig lnc;
	private int[] prevtime= {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private int[] prevday= {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	
	private Float[] previouses;
	private Float[] currents;
	private boolean scheduledvalue;
	
	private LightNightClock objRif = null;
	
	public static final Object monitor = new Object();
	
	private LightNightMgr()
	{
		innerCreate();
	}

	public static LightNightMgr getInstance()
	{
		if (me == null) me = new LightNightMgr();
		return me;
	}
	
	public void reloadMgr() {
		innerCreate();
	}
	
	private void innerCreate()
	{
		lnc = new LNConfig();
		previouses = new Float[]{Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN};
		currents = new Float[]{Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN};
	}
	
	public void reload()
	{
		synchronized (monitor)
		{
			LoggerMgr.getLogger(this.getClass()).info("Reload configuration");
			lnc = new LNConfig();
		}
	}
	
	public void setOnOff(int idgrp, boolean on)
	{
		LinkedList<Integer> devs = lnc.getDevices(idgrp);
		if(devs!=null)
		{
			SetContext setContext = new SetContext();
			setContext.setUser("Light&Night");
			String lang = "EN_en";
			try
			{
				lang = LangUsedBeanList.getDefaultLanguage(1);
			}
			catch (Exception e)
			{
			}
			setContext.setLanguagecode(lang);
			setContext.setCallback(new LNSetCallback());
			for(Iterator<Integer> i = devs.iterator();i.hasNext();)
			{
				Integer dev = i.next();
				int var = lnc.onoffVar(dev);
				int value = lnc.onoffValue(dev, on);
				if(var!=-1 && value!=-1){
					SetWrp sw = setContext.addVariable(var, value);
					sw.setCheckChangeValue(false);					
				}
			}
			SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
		}
	}

	public void setActive(int idgrp, boolean on)
	{
		try
		{
			if(lnc.isLightsNightEnabled(idgrp))
			{
				LoggerMgr.getLogger(this.getClass()).info("Set manual lightsnight group "+idgrp+" "+on);
				setLightsNight(idgrp, on);
			}
			if(lnc.isOnOffEnabled(idgrp))
			{
				LoggerMgr.getLogger(this.getClass()).info("Set manual onoff group "+idgrp+" "+on);
				setOnOff(idgrp, on);
			}
		}
		catch(Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}

	public void setLightsNight(int idgrp, boolean on)
	{
		int var = 0;
		int value = 0;
		Integer dev = null;
		
		LinkedList<Integer> devs = lnc.getDevices(idgrp);
		
		if(devs!=null)
		{
			SetContext setContext = new SetContext();
			setContext.setUser("Light&Night");
			String lang = "EN_en";
			try
			{
				lang = LangUsedBeanList.getDefaultLanguage(1);
			}
			catch (Exception e)
			{
			}
			
			setContext.setLanguagecode(lang);
			setContext.setCallback(new LNSetCallback());
			
			for(Iterator<Integer> i = devs.iterator();i.hasNext();)
			{
				dev = i.next();
				
				if (lnc.lights(dev))
				{
					//lights
					var = lnc.lightsVar(dev);
					value = lnc.lightsValue(dev, on);
					
					if(var!=-1 && value!=-1)
					{
						SetWrp sw = setContext.addVariable(var, value);
						sw.setCheckChangeValue(false);
					}
				}
				else if (lnc.night(dev))
				{
					//night
					var = lnc.nightVar(dev);
					value = lnc.nightValue(dev, on);
					
					if(var!=-1 && value!=-1)
					{
						SetWrp sw = setContext.addVariable(var, value);
						sw.setCheckChangeValue(false);					
					}
				}
			}
			SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
		}
	}
	
	public boolean isTime(int idgrp)
	{
		boolean toret = false;
		HashMap<String, Integer> sched; 
		if(!lnc.isSchedulerActive(idgrp))
		{
			return false;
		}
		GregorianCalendar c = new GregorianCalendar();
		int today = (c.get(Calendar.MONTH)+1)*100+c.get(Calendar.DAY_OF_MONTH);
		if(prevtime[idgrp]==-1)
		{
			prevtime[idgrp] = c.get(Calendar.HOUR_OF_DAY)*10000+c.get(Calendar.MINUTE)*100+c.get(Calendar.SECOND);
			return false;
		}
		int day = (c.get(Calendar.DAY_OF_WEEK)+6)%7;
		day=day==0?7:day;
		if(isException( idgrp, today ))
		{
			sched = lnc.getTodayExceptions(idgrp, today);
		}
		else
		{
			sched = lnc.getScheduling(idgrp, day);
		}
		if(sched==null || sched.isEmpty())
			return false;
		
		int currtime = c.get(Calendar.HOUR_OF_DAY)*10000+c.get(Calendar.MINUTE)*100+c.get(Calendar.SECOND);
		int currday = c.get(Calendar.DATE);
		
		String[] hh = {"on_1","off_1","on_2","off_2"};
		for(int i = 0;i<4;i++)
		{
			//if there is a time scheduled
			if(sched.get(hh[i])==-1) 
				continue;
			// if scheduled time is between previous comparison time and actual time
			if((prevtime[idgrp] < sched.get(hh[i]) && sched.get(hh[i]) <= currtime)){
				scheduledvalue = "on".equalsIgnoreCase(hh[i].substring(0, 2))?true:false;
				toret = true;
			} 
			//else if previous comparison time is at previous day
			else if(prevtime[idgrp]>currtime && prevday[idgrp] != currday){ 
				if ( ((prevtime[idgrp] - 240000) <= sched.get(hh[i]) && sched.get(hh[i]) <= currtime) ||
					 (prevtime[idgrp] <= sched.get(hh[i]) && sched.get(hh[i]) <= ( currtime+240000 )) ) {
					int nextday = (day % 7 ) + 1;
					int prevday = ((day + 5) % 7) +1 ;
					if( !(sched.get(hh[i]).equals(240000) && 
							lnc.getScheduling(idgrp, nextday)!=null &&
							lnc.getScheduling(idgrp, nextday).get("on_1")!=null &&
							lnc.getScheduling(idgrp, nextday).get("on_1").equals(0)) &&
						!(sched.get(hh[i]).equals(0) && 
							lnc.getScheduling(idgrp, prevday)!=null &&
							lnc.getScheduling(idgrp, prevday).get("off_1")!=null &&
							lnc.getScheduling(idgrp, prevday).get("off_1").equals(240000)) &&
						!(sched.get(hh[i]).equals(0) && 
							lnc.getScheduling(idgrp, prevday)!=null &&
							lnc.getScheduling(idgrp, prevday).get("off_2")!=null &&
							lnc.getScheduling(idgrp, prevday).get("off_2").equals(240000))) {
						scheduledvalue = "on".equalsIgnoreCase(hh[i].substring(0, 2))?true:false;
						toret = true;
					}
				}
			}
		}
		prevtime[idgrp]=currtime;
		prevday[idgrp]=currday;
		return toret;
	}

	private boolean isException(int idgrp, int monthday)
	{
		if(lnc.getTodayExceptions(idgrp,monthday) != null)
		{
			return true;
		}
		return false;
	}

	public boolean isField(int idgrp)
	{
		if(!lnc.isFieldActive(idgrp)) 
		{
			try
			{
				if(lnc.getFieldVar(idgrp)!=-1)
				{
					if(!ControllerMgr.getInstance().getFromField(lnc.getFieldVar(idgrp)).isDeviceOffLine())
					{
						previouses[idgrp] = ControllerMgr.getInstance().getFromField(lnc.getFieldVar(idgrp)).getCurrentValue();
					}
					else
					{
						previouses[idgrp]=Float.NaN;
					}
				}
			}
			catch (Exception e)
			{
				previouses[idgrp]=Float.NaN;
			}
			return false;
		}
		if(previouses[idgrp].equals(Float.NaN))
		{
			try
			{
				if(lnc.getFieldVar(idgrp)!=-1)
					previouses[idgrp] = ControllerMgr.getInstance().getFromField(lnc.getFieldVar(idgrp)).getCurrentValue();
			}
			catch (Exception e)
			{
				previouses[idgrp]=Float.NaN;
			}
			return false;
		}
		try
		{
			if(lnc.getFieldVar(idgrp)!=-1)
				currents[idgrp] = ControllerMgr.getInstance().getFromField(lnc.getFieldVar(idgrp)).getCurrentValue();
			else
				currents[idgrp] = Float.NaN;
		}
		catch (Exception e)
		{
			currents[idgrp]=Float.NaN;
		}
		if(!previouses[idgrp].equals(currents[idgrp]) &&
				//!previouses[idgrp].equals(Float.NaN) && 
				!currents[idgrp].equals(Float.NaN))
		{
			previouses[idgrp]=currents[idgrp];
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean getForceManual(int idgrp)
	{
		return lnc.getForceManual(idgrp);
	}

	public boolean isLightsNightEnabled(int idgrp)
	{
		return lnc.isLightsNightEnabled(idgrp);
	}

	public boolean isOnOffEnabled(int idgrp)
	{
		return lnc.isOnOffEnabled(idgrp);
	}

	public boolean getFieldOnOff(int idgrp)
	{
		return currents[idgrp].equals(new Float(1))?
				LNConfig.DIRECT==lnc.getFieldVarLogic(idgrp)?true:false:
				LNConfig.DIRECT==lnc.getFieldVarLogic(idgrp)?false:true;
	}

	public long getPollingTime()
	{
		long l = 0;
		try
		{
			String s = lnc.getClockConfig().getProperty("pollingtime");
			if(s==null || s.equalsIgnoreCase(""))
				l = 60000;
			else
				l = new Long(s); 
		}
		catch(Exception e)
		{
			return new Long(60000);
		}
		return l;
	}

	public boolean scheduledValue(int idgrp)
	{
		return scheduledvalue;
	}
	
	public void setLightNightClockRif(LightNightClock o) {
		this.objRif = o;
	}
	
	public LightNightClock getLightNightClockRif() {
		return this.objRif;
	}
}
