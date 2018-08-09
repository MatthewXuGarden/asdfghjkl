package com.carel.supervisor.base.timer.impl;

import com.carel.supervisor.base.timer.*;


public class TimerContainerDummy implements ITimerContainer, Cloneable
{

    public Object clone()
    {
        return null;
    }

    public void collect(ILocalTimer timer)
    {
    }

    public String[] getTimersName()
    {
        return null;
    }

    public ITimerData getData(String name)
    {
        return null;
    }

    public void clear()
    {
    }
}
