package com.carel.supervisor.dispatcher.main;

public class DispatcherPoller
{
    private long startAfter = 0;
    private long sleepTime = 0;
    private DispatcherQueue dispqueue = null;

    public DispatcherPoller(String sleep, String startafer)
    {
        if (sleep == null)
            this.sleepTime = 30000;
        else
        {
            try {
                this.sleepTime = Long.parseLong(sleep);
            }
            catch (Exception e) {
                this.sleepTime = 30000;
            }
        }

        if (startafer == null)
            this.startAfter = 30000;
        else
        {
            try {
                this.startAfter = Long.parseLong(startafer);
            }
            catch (Exception e) {
                this.startAfter = 30000;
            }
        }
    }

    public void startDispatcherPoller()
    {
    	stopDispatcherPoller();
    	this.dispqueue = new DispatcherQueue();
    	this.dispqueue.setTimeSleep(this.sleepTime);
    	this.dispqueue.setTimeStart(this.startAfter);
    	this.dispqueue.startPoller();
    }

    public void stopDispatcherPoller()
    {
    	if(this.dispqueue != null)
    	{
    		this.dispqueue.stopPoller();
    		this.dispqueue = null;
    	}
    }
}
