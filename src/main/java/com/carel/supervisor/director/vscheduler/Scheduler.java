package com.carel.supervisor.director.vscheduler;

import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;


public class Scheduler implements Runnable {

	private static final long INCATIVITY_INTERVAL = 20 * 1000;
	private static final long STARTUP_DELAY = 90 * 1000;
	private SchedulerHook hook;
	private boolean bRun;		// continue thread when bRun is true
	private boolean bLoad;		// load/reload DB when bLoad is true
	private boolean bLoading;	// loading flag
	private CData objData;
	private Vector<CCommand> objCommandQueue;	
	
	private static Object objLock = new Object();
	
	public Scheduler(SchedulerHook hook)
	{
		this.hook = hook;
		bRun = false;
		bLoad = false;
		bLoading = false;
		objData = new CData();
		objCommandQueue = new Vector<CCommand>();
	}
	
	
	public void run()
	{
		// init
		boolean bStartup = true;
		bLoad = true;
		bLoading = true;
		bRun = true;
		
		// thread loop
		while( bRun ) {
			// load DB if required
			if( bLoad ) {
				bLoad = false;
				EventMgr.getInstance().info(new Integer(1), CDataDef.MY_NAME, "Action", "VS04", null);
				long nStartLoading = System.currentTimeMillis();
				objData.loadDB(true);
				long nStopLoading = System.currentTimeMillis();
				long nTimeLoading = nStopLoading - nStartLoading;
				LoggerMgr.getLogger(Scheduler.class).info("data loaded in " + (nTimeLoading / 1000) + " seconds");
				EventMgr.getInstance().info(new Integer(1), CDataDef.MY_NAME, "Action", "VS05", new Object[] { nTimeLoading / 1000 });
				if( bStartup ) {
					// make sure all engine components are available
					while( STARTUP_DELAY - nTimeLoading > 0 ) {
						try {
							Thread.sleep(1000);
						} catch(InterruptedException e) {
						}
						nTimeLoading += 1000;
						// go on when the engine is started
						if( DirectorMgr.getInstance().isStarted() && DispatcherMgr.getInstance().isServiceRunning() )
							break;
						// done if something happen during engine startup
						if( !bRun ) {
							hook.onThreadStop();
							return;
						}
					}
					bStartup = false;
				}
				synchronized(objLock) {				
					bLoading = false;
				}
			}
			// execution
			GregorianCalendar gc = new GregorianCalendar();
			// manual command execution
			for(Enumeration<CCommand> e = objCommandQueue.elements(); e.hasMoreElements();) {
				CCommand objCommand = e.nextElement();
				objData.execute(objCommand);
				if( objCommand.isHold() )
					objCommand.setFake();
				else
					objCommandQueue.remove(objCommand);
			}
			// scheduler execution
			objData.run(gc);
			// inactivity interval
			try	{
				Thread.sleep(INCATIVITY_INTERVAL);
			}
			catch(InterruptedException e) {
			}
		}
		
		// done
		hook.onThreadStop();
	}
	
	
	public void stop()
	{
		bRun = false;
	}
	
	
	public void loadDB()
	{
		synchronized(objLock) {
			bLoad = true;
			bLoading = true;
		}
	}
	
	
	public boolean isLoading()
	{
		synchronized(objLock) {
			return bLoading;
		}
	}
	

	public boolean isRunning()
	{
		return bRun;
	}
	
	
	public void addCommand(CCommand objCommand)
	{
		objCommandQueue.add(objCommand);
	}
}
