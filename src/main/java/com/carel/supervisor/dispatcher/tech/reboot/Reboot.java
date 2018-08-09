package com.carel.supervisor.dispatcher.tech.reboot;

import com.carel.supervisor.base.script.ScriptInvoker;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;

public class Reboot extends Poller
{
	public Reboot() {}
	
	public void run()
	{
		boolean status = true;
		
		try
		{
			if(DispatcherMgr.getInstance().isServiceRunning())
			{
				DispatcherMgr.getInstance().stopService();
			}
		}
		catch(Exception e) {
			status = false;
		}
		
		try {
			Thread.sleep(5000);
		}
		catch(Exception e) {
		}
		
		try
		{
			if(DirectorMgr.getInstance().isStarted())
			{
				DirectorMgr.getInstance().stopPVPRO();
			}
		}
		catch(Exception e) {
			status = false;
		}
		
        try
        {
        	String pathFile = DispatcherMgr.getInstance().getServicesPath() + "Reboot.log";
            ScriptInvoker script = new ScriptInvoker();
            String programFilesPath = System.getenv("ProgramFiles");
            script.execute(new String[] { programFilesPath+"\\CAREL\\scr\\reboot.bat"},pathFile);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
	}
}