package com.carel.supervisor.dispatcher.memory;

public abstract class ZMemory
{
    private int idsite = 0;
    private int retryNumber = 0;
    private int retryAfter = 0;

    public ZMemory()
    {
    }

    public int getIdSite()
    {
        return this.idsite;
    }

    public int getRetryNum()
    {
        return this.retryNumber;
    }

    public int getRetryAfter()
    {
        return this.retryAfter;
    }

    protected void setIdSite(int is)
    {
        this.idsite = is;
    }

    protected void setRetryNum(int rn)
    {
        this.retryNumber = rn;
    }

    protected void setRetryAfter(int rf)
    {
        this.retryAfter = rf;
    }

    public abstract String getFisicDeviceId();

    public abstract void storeConfiguration() throws Exception;
}
