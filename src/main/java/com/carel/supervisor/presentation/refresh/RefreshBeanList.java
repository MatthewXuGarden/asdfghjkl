package com.carel.supervisor.presentation.refresh;

public class RefreshBeanList
{
    private RefreshBean[] listObjToRef = null;
    private int timeRefresh = -1;

    public RefreshBeanList(int timeRefresh, int numObjToRef)
    {
        this.timeRefresh = timeRefresh;
        this.listObjToRef = new RefreshBean[numObjToRef];
    }

    public int getRefreshTime()
    {
        return this.timeRefresh;
    }

    public void setRefreshObj(RefreshBean[] rb)
    {
        this.listObjToRef = rb;
    }

    public RefreshBean[] getRefreshObj()
    {
        return this.listObjToRef;
    }
}
