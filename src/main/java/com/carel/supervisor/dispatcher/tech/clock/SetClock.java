package com.carel.supervisor.dispatcher.tech.clock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.presentation.bean.ClockBean;
import com.carel.supervisor.presentation.bean.ClockBeanList;

public class SetClock
implements Runnable
{
	private static final SetClock clockMaster = new SetClock();
	
	private boolean bRunning = false;
	String lang = "EN_en";
	
	Object obj = new Object();
	
	private SetClock()
	{
	}
	
	
	public static SetClock clockMaster()
	{
		return clockMaster;
	}
	
	
	public boolean setJob(String[] idvarsmdl, String lang)
	{
		if( !bRunning ) {
			this.lang = lang;
			return true;
		}
		return false;
	}
	
	
	public void goAhead()
	{
		synchronized(obj) {
			obj.notify();
		}
	}
	
	
	public boolean isRunning()
	{
		return bRunning;
	}
	
	
	public void run()
	{
    	bRunning = true;
    	
		TechClock techClock = null;
    	String sClass = "";
    	Integer idDevMdl = null;
    	ArrayList listTC = new ArrayList();
    	ClockBeanList clockList = new ClockBeanList();
    	List<ClockBean> clocks = clockList.load();
    	if (clocks != null && clocks.size()>0)
        {
            for (int i=0; bRunning && i<clocks.size(); i++)
            {
            	ClockBean clock = clocks.get(i);
            	
            	try 
            	{
            		techClock = new TechClock(clock,lang);
            		
            		if(techClock != null)
            		{
            			if(techClock.load())
            			{
            				listTC.add(techClock); // to reuse it in case of error
            				
            				Iterator it = techClock.iterator();
            				Integer idDevice = null;
            				while( bRunning && it.hasNext() )
            				{
            					idDevice = (Integer)it.next();
            					techClock.next(idDevice);

            					// freeze thread execution if engine is stopped
            					while( !DirectorMgr.getInstance().isStarted() && bRunning ) {
            						try {
	            						Thread.sleep(1000);
	            					}
	            					catch(Exception e) {
	            						// interrupted
	            						bRunning = false;
	            					}
            					}
            					
            					synchronized(obj) {
    	    						techClock.bSetOnField = false;
    	        					techClock.set();
    	        					if( techClock.bSetOnField == true ) {
		            					try {
	    	        						obj.wait(120000);
		    	        					// 2 min timeout used to prevent thread lock if callback is lost for an unexpected reason
		            					} catch(Exception e) {
		            					}
    	        					}
            					}
            				}
            			}
            		}
            	}
            	catch(Exception e){
					LoggerMgr.getLogger(this.getClass()).error(e);
            	}
            }
            
            // retry
            Iterator it = listTC.iterator();
			while( bRunning && it.hasNext() ) {
				techClock = (TechClock)it.next();
				if( techClock.isError() ) {
					Iterator itError = techClock.iteratorError();
					while( bRunning && itError.hasNext() ) {
    					int idDevice = (Integer)itError.next();
    					techClock.next(idDevice);

    					// freeze thread execution if engine is stopped
    					while( !DirectorMgr.getInstance().isStarted() && bRunning ) {
    						try {
        						Thread.sleep(1000);
        					}
        					catch(Exception e) {
        						// interrupted
        						bRunning = false;
        					}
    					}
    					
    					synchronized(obj) {
    						techClock.bSetOnField = false;
        					techClock.set();
        					if( techClock.bSetOnField == true ) {
            					try {
	        						obj.wait(120000);
    	        					// 2 min timeout used to prevent thread lock if callback is lost for an unexpected reason
            					} catch(Exception e) {
            					}
        					}
    					}
					}
				}
			}
        }
    	
        EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
       		bRunning ? EventDictionary.TYPE_INFO : EventDictionary.TYPE_WARNING,
       		bRunning ? "D060" : "D061", null);
    	
        bRunning = false;
	}

}
