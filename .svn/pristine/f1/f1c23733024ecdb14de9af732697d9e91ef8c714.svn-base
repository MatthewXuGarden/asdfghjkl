package com.carel.supervisor.plugin.energy;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.director.module.IModule;

public class EnergyModuleHook implements IModule {
	private String name = "";
	public boolean hookModule(boolean state) {
		if (state) {
			try 
			{
				EnergyMgr.getInstance().load();
				EnergyMgr.getInstance().loadScheduler();
				if (EnergyMgr.getInstance().getSchedulerLoaded()
						&& new Boolean(EnergyMgr.getInstance()
								.getStringProperty("startactive", "false"))) {
					EnergyMgr.getInstance().startActive();
				}
				if (EnergyMgr.getInstance().getLoaded()
						&& new Boolean(EnergyMgr.getInstance()
								.getStringProperty("startengine", "false"))) {
					EnergyMgr.getInstance().startEngine();
				}
				EventMgr.getInstance().info(1, "Energy", "energy", "EG01",
						new Object[] {});
				EnergyMgr.getInstance().setStartedplugin(true);
			} 
			catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
				EventMgr.getInstance().info(1, "Energy", "energy", "EG03",
						new Object[] {});
			}
		} 
		else 
		{
			if (EnergyMgr.getInstance().getActiveRunning()) {
				EnergyMgr.getInstance().stopActive();
			}
			if(EnergyMgr.getInstance().getRunning()) {
				EnergyMgr.getInstance().stopEngine();
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