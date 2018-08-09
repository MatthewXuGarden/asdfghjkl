package com.carel.supervisor.director.vscheduler;

import com.carel.supervisor.director.module.IModule;

public class SchedulerHook implements IModule {
	private static SchedulerHook me = null;
	private Scheduler scheduler;
	private Thread thread;
	private String name = "";
	
	
	public SchedulerHook()
	{
		me = this;
		scheduler = null;
		thread = null;
	}
	
	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public boolean hookModule(boolean state)
	{
		if( state ) {
			scheduler = new Scheduler(this);
			thread = new Thread(scheduler, "vscheduler");
			thread.start();
		}
		else {
			if( scheduler != null ) {
				scheduler.stop();		// stop message
				if( thread != null )
					thread.interrupt();	// force exit from sleep
			}
		}
		return true;
	}
	
	
	public void onThreadStop()
	{
		// thread stopped
		scheduler = null;
		thread = null;
	}
	
	
	public static void dbChanged()
	{
		if( me != null && me.scheduler != null ) {
			me.scheduler.loadDB();
			if( me.thread != null )
				me.thread.interrupt();
		}
	}
	
	
	public static boolean isDataLoading()
	{
		if( me != null && me.scheduler != null )
			return me.scheduler.isLoading();
		else
			return false;
	}
	

	public static boolean isRunning()
	{
		if( me != null && me.scheduler != null )
			return me.scheduler.isRunning();
		else
			return false;
	}

	
	public static void onCommand(CCommand objCommand)
	{
		if( me != null && me.scheduler != null ) {
			me.scheduler.addCommand(objCommand);
			if( me.thread != null )
				me.thread.interrupt(); // force command execution
		}
	}
}
