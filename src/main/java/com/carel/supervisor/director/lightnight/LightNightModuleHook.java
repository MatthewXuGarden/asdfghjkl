package com.carel.supervisor.director.lightnight;

import com.carel.supervisor.director.module.IModule;

public class LightNightModuleHook implements IModule
{
	private String name = "";
	public boolean hookModule(boolean state)
	{
		if(state)
		{
			LightNightMgr.getInstance().reloadMgr();
			LightNightClock lnc = new LightNightClock(LightNightMgr.getInstance().getPollingTime());
			LightNightMgr.getInstance().setLightNightClockRif(lnc);
			Thread t = new Thread(lnc, "LightNightClock");
			t.start();
		}
		else
		{
			LightNightClock lnc = LightNightMgr.getInstance().getLightNightClockRif();
			if(lnc != null)
			{
				lnc.setStop();
				try { Thread.sleep(2000L); } catch (Exception e) {} 
				lnc = null;
			}
		}
		return true;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
