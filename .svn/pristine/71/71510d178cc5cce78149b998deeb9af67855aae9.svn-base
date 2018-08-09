package supervisor;

import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.base.log.LoggerMgr;


public class Demo implements Runnable {
	private boolean bRun;
	private int nCounter;
	private int nInterval;
	
	
	public Demo(int nMinutes)
	{
		nInterval = nMinutes;
		nCounter = 0;
		bRun = true;
	}
	
	
	public void run()
	{
		while( bRun ) {
			try {
				Thread.sleep(60 * 1000); // 1 minute
			} catch(Exception e) {
			}
			
			if( ++nCounter >= nInterval ) {
		        try {
					// Stop motore
			        if( DirectorMgr.getInstance().isStarted() )
			            DirectorMgr.getInstance().stopEngine();
	
			        // Rest macchina a stati
			        ControllerMgr.getInstance().reset();
	
			        // Stop dispatcher
			        if( DispatcherMgr.getInstance().isServiceRunning() )
			            DispatcherMgr.getInstance().stopService();
		        } catch(Exception e) {
		        	LoggerMgr.getLogger(this.getClass()).error(e);
		        }
				nCounter = 0;
			}
		}
	}
	
	
	public void stop()
	{
		bRun = false;
	}	
}
