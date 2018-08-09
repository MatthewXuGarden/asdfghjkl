package com.carel.supervisor.base.thread;

public class Writer extends Thread
{
    private boolean stop = false;
    private boolean start = false;

    public boolean isStopped()
    {
        return stop;
    }

    public void stopPoller()
    {
        this.stop = true;
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
