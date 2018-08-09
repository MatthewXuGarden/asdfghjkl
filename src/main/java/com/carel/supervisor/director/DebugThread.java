package com.carel.supervisor.director;

import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.device.DeviceStatusMgr;

public class DebugThread extends Poller
{
	private boolean ended = true;
	public void run()
	{
		ended = false;
		while(this.isStarted())
		{
			DeviceStatusMgr.getInstance().pingAll();
			try{Thread.sleep(1000l);}
			catch(Exception ex){}
		}
		ended = true;
	}
	public boolean ended()
	{
		return this.ended;
	}
}
