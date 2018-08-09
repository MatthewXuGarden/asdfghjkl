package com.carel.supervisor.dispatcher.engine.mail;

import com.carel.supervisor.base.thread.Poller;

public class DispMailRetryer extends Poller
{
    private long sleep = 60000L;
    private static int counter = 0;
    
    public DispMailRetryer() {
    	this.setName(this.getClass().getName()+"_"+(counter++));
    }
    
    public void run()
    {
        while(this.isStarted())
        {
            // Sleep
            try { Thread.sleep(sleep);}catch(Exception e){}
            
            DispMailSender tmp = null;
            
            for(int i=0;  i<DispMailRetryMgr.getInstance().getMailCount(); i++)
            {
                tmp = DispMailRetryMgr.getInstance().getMailAt(i);
                if(tmp != null && System.currentTimeMillis() > tmp.getTimeToGo())
                {
                    if(!tmp.sendMessage()){
                        tmp.resetTimeToGo();
                    } else {
                    	DispMailRetryMgr.getInstance().removeMailAt(i);
                    }
                }
                
                if(tmp != null && tmp.getTimeToGo() == -1)
                    DispMailRetryMgr.getInstance().removeMailAt(i);
            }
        }
    }
}
