package com.carel.supervisor.director.ac;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.event.EventMgr;


public class AcManager
{
    private static AcManager myInstance = new AcManager();
    private static Integer polling = 1200;
    private AcClock clock = null;
    private boolean isChanged = false;
    private AcConfigBeanList list = null;
    private long time2wait = 0L;

    public static AcManager getInstance()
    {
        return myInstance;
    }

    public long getTime2Wait()
    {
    	return this.time2wait;
    }
    
    public void setTime2Wait(long time)
    {
    	this.time2wait = time;
    }
    
    private static void init()
    {
        AcProperties prop = new AcProperties();
        polling = prop.getProp("ac_clock");
    }
    
    // START
    public boolean startAC(String user, long timetowait) throws DataBaseException
    {
        init();

        this.setTime2Wait(timetowait);
        
        if (clock == null)
        {
            clock = new AcClock(this, polling);
        }

        if (clock.isStopped())
        {
            clock.startPoller();
        }

        if (clock.isStarted())
        {
        	//controllo tabelle "ac_master" e "ac_slave" per consistenza configurazioni dopo riavvio motore:
        	boolean changed = AcProcess.ctrl_ac_tables();
        	
        	// Log salvataggio master
			EventMgr.getInstance().info(1,user,"ac","AC01",null);
			
			//salvo avvio modulo nella tabella di configurazione:
			AcProperties.updateProp("ac_running", new Integer(1));
        }
        
        return clock.isStarted();
    }

    // STOP
    public void stopAC(String user)
    {
        clock.stopPoller();
        if (clock.isStopped())
        {
        	// Log salavataggio master
			EventMgr.getInstance().info(1,user,"ac","AC02",null);
			clock = null;
			
			//segnalo lo stop nella tabella di configurazione del modulo:
			AcProperties.updateProp("ac_running", new Integer(0));
			
			//scodo tutti i set residui del modulo dalla coda principale del PVPro:
			AcProcess.dequeueAll();
			LoggerMgr.getLogger(this.getClass().getName()).info(this.getClass().getName() + " :  DEQUEUE_OK");
        }
    }

    public void executeSet()
    {
    	//ritardo (solo) la prima propagazione dopo riavvio motore pvpro:
    	if (time2wait > 0L)
        {
	    	try
	        {
				Thread.sleep(time2wait);
			}
	        catch (Exception e1)
	        {
	            // PVPro-generated catch block:
	            Logger logger = LoggerMgr.getLogger(this.getClass());
	            logger.error(e1);
			}
	        
	        this.setTime2Wait(0L);
        }
    	
    	//LoggerMgr.getLogger(this.getClass()).info("isChanged: " + isChanged);
    	if ((list == null) || isChanged())
        {
            list = new AcConfigBeanList();
        }

        try
        {
            AcProcess.executePropagation(list);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    public boolean isRunning()
    {
        if (clock != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public synchronized boolean isChanged()
    {
        return isChanged;
    }

    public void setChanged(boolean isChanged)
    {
    	this.setChanged(isChanged, 0L);
    }
    
    public synchronized void setChanged(boolean isChanged, long newTime)
    {
        if ((newTime > 0L) && (clock != null))
        {
        	clock.updSleepTime(newTime);
        }
        
    	this.isChanged = isChanged;
    }
}
