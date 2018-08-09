package com.carel.supervisor.dispatcher.tech.ssd;

import com.carel.supervisor.dispatcher.tech.ITech;
import com.carel.supervisor.base.config.Supervisor;

public class CheckSSD implements ITech
{
	public void doTechAction() {
		Supervisor.getInstance().checkSSD();
	}
}
