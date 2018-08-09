package com.carel.supervisor.dispatcher.engine.mail;

import java.util.ArrayList;
import java.util.List;

public class DispMailRetryMgr
{
    private static DispMailRetryMgr me = null;
    private DispMailRetryer poller = null;
    private List coda = null;
    
    private DispMailRetryMgr()
    {
        coda = new ArrayList();
    }
    
    public static DispMailRetryMgr getInstance() 
    {
        if(me == null)
            me = new DispMailRetryMgr();
        return me;
    }
    
    public void addMail(DispMailSender mail) 
    {
        mail.resetTimeToGo();
        this.coda.add(mail);
        
        if(poller == null)
        {
            poller = new DispMailRetryer();
            poller.startPoller();
        }
        else
        {
            if(!poller.isStarted())
            {
                poller = null;
                poller = new DispMailRetryer();
                poller.startPoller();
            }
        }
    }
    
    public int getMailCount() {
        return this.coda.size();
    }
    
    public DispMailSender getMailAt(int idx) {
        return (DispMailSender)this.coda.get(idx);
    }
    
    public void removeMailAt(int idx) 
    {
        this.coda.remove(idx);
        if(this.coda.size() == 0)
            poller.stopPoller();
    }
}
