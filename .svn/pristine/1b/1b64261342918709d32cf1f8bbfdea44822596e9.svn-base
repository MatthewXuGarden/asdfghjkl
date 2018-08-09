package com.carel.supervisor.director.ac;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;


public class AcClock extends Poller
{
    private AcManager mgr = null;
    private long sleep = 1200L;

    public AcClock(AcManager mgr, long sleep)
    {
    	setName("AcClock");
        if (sleep > 0L)
        {
        	this.sleep = sleep * 1000;
        }
        
        this.mgr = mgr;
    }

    public void updSleepTime(long newSleep)
    {
    	//modifica tempo di ctrl senza riavvio motore modulo:
    	this.sleep = newSleep * 1000;
    	
    	Logger logger = LoggerMgr.getLogger(AcClock.class);
    	logger.info("Setting new check time to "+(newSleep/60)+" min.");
    }
    
    public void run()
    {
        while (this.isStarted())
        {
        	mgr.executeSet();

            // Sleep in base al tempo di ctrl settato:
            try
            {
                Thread.sleep(this.sleep);
            }
            catch (InterruptedException e)
            {
                // PVPro-generated catch block:
                Logger logger = LoggerMgr.getLogger(AcClock.class);
                logger.error(e);
            }
        }
    }
}
