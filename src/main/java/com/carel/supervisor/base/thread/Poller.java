package com.carel.supervisor.base.thread;

public class Poller extends Thread
{
    private boolean start = false;

    public boolean isStopped()
    {
        return (!start);
    }

    public void stopPoller()
    {
        this.start = false;
    }

    public boolean isStarted()
    {
        return start;
    }

    public void startPoller()
    {
        start();
        this.start = true;
    }
}
