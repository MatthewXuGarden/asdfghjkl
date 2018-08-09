package com.carel.supervisor.plugin.switchtech;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.director.module.IModule;

public class SwitchModuleHook implements IModule 
{
	private String name = "";
	public boolean hookModule(boolean state) 
	{
		if (state) 
		{
			SwitchMgr mgr = SwitchMgr.getInstance();
			int idsite = 1;

			int n_switch = mgr.getSwitchNumber();
			Switch sw_instance = null;

			for (int i = 1; i < (n_switch + 1); i++) {
				sw_instance = new Switch();

				try {
					sw_instance.reload(idsite, i);

					if (sw_instance != null && sw_instance.getAuto_type() != null) {
						if (sw_instance.getSt_running().equalsIgnoreCase("TRUE")) {
							mgr.startSwitch(i, sw_instance, "System");
						}
					} else {
						//switch non configurato
						EventMgr.getInstance().info(new Integer(idsite),"System", "switch", "SW08",new Object[] { "Switch" + i });
					}
				} catch (Exception e) {
					LoggerMgr.getLogger(this.getClass()).error(e);
				}
			}
		}
		else
		{
			 int n = SwitchMgr.getInstance().getSwitchNumber();
	        for(int i=1;i<=n;i++){
	        	SwitchMgr.getInstance().stopSwitch(i, "System",false);
	        }
	        // elimina eventuali elementi pendenti dello SW dalla coda dei set
	        SetDequeuerMgr.getInstance().dequeueAllByPriority(PriorityMgr.getInstance().getPriority(this.getClass().getName()));
	        LoggerMgr.getLogger(this.getClass().getName()).info(this.getClass().getName() + " :  DEQUEUE_OK");
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
