
package com.carel.supervisor.dispatcher.main;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.thread.Poller;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.action.DispatcherAction;
import com.carel.supervisor.dispatcher.bean.HSActionBeanList;


public class DispatcherQueue extends Poller
{
    private HSActionBeanList hsaBeanList = null;
    private DispatcherFormatter dispFormatter = null;
    private long timeSleep  = 0;
    private long afterStart = 0;
    private static int innercounter=0;

    public DispatcherQueue()
    {
        hsaBeanList = new HSActionBeanList();
        timeSleep = 20000L;
        afterStart = 10000L;
        this.setName("DispatcherQueue_"+(innercounter++));
    }
    
    public void setTimeSleep(long time) {
    	this.timeSleep = time;
    }
    
    public void setTimeStart(long time) {
    	this.afterStart = time;
    }
    
    public void run() {
    	try {
    		Thread.sleep(afterStart);
    	}
    	catch(Exception e) {}
    	
    	while(!isStopped()) {
	        /*
	         * Guadian, save last timestamp in millisecond for check dispatcher activity.
	        try
	        {
	            DispatcherMgr.getInstance().setLastCheckCodeOne(System.currentTimeMillis());
	        }
	        catch (Exception e){
	        }
	         */
    		
    		// Guardian, save last timestamp in millisecond for check dispatcher activity.
    		try {
    			DispatcherMonitor.getInstance().setFirstQueueTime(System.currentTimeMillis());
    		} catch(Exception e){}
	        try {
	            // Load Action
	            hsaBeanList.loadActionList();
	            if (hsaBeanList.thereIsActions()) {
	                // Format Action
	                dispFormatter = new DispatcherFormatter(hsaBeanList.getActionList());
	                // Create Template
	                dispFormatter.initData();
	                // Put action in queue
	                saveActionInQueue(dispFormatter.getActionsToDisp());
	            }
	        }
	        catch (Exception e) {
	            Logger logger = LoggerMgr.getLogger(this.getClass());
	            logger.error(e);
	        }
	        
	        try {
	        	Thread.sleep(this.timeSleep);
	        }
	        catch(Exception e) {}
    	} 
    }

    private void saveActionInQueue(DispatcherAction[] list)
    {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        int[] idsaved = new int[0];
        boolean error = false;

        if (list != null)
        {
            for (int i = 0; i < list.length; i++)
            {
                try
                {
                    idsaved = list[i].putActionInQueue();

                    if (idsaved.length > 0)
                        setStateToLoad(idsaved);
                }
                catch (Exception e)
                {
                    error = true;

                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);

                    List<Integer> l = list[i].getKeyAction();

                    if (l != null)
                    {
                        for (int a = 0; a < l.size(); a++)
                        {
                            ret.add(l.get(a));

                            Object[] pa = { list[i].getActionName() };
                            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                                EventDictionary.TYPE_ERROR, "D043", pa);
                        }
                    }
                }
            }
        }

        if (error)
        {
            idsaved = new int[ret.size()];

            for (int i = 0; i < idsaved.length; i++)
                idsaved[i] = ((Integer) ret.get(i)).intValue();

            setStateToDiscard(idsaved);
            idsaved = new int[0];
        }
    }

    private void setStateToLoad(int[] ids)
    {
        HSActionBeanList tmp = new HSActionBeanList();
        tmp.updateToLoadActionList(ids);
    }

    private void setStateToDiscard(int[] ids)
    {
        HSActionBeanList tmp = new HSActionBeanList();
        tmp.updateToDiscardActionList(ids);
    }
}
