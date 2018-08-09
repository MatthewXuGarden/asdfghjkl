package com.carel.supervisor.base.timer.impl;

import com.carel.supervisor.base.timer.*;
import com.carel.supervisor.base.util.*;


public class TimerContainer implements ITimerContainer, Cloneable
{
    private SimpleContainer totals = new SimpleContainer();
    
    public Object clone()
    {
        return totals.clone();
    }

    public void collect(ILocalTimer timer)
    {
        synchronized (TimerContainer.class)
        {
            ITimerData oTimerData = (ITimerData) totals.get(timer.getName());

            if (null == oTimerData)
            {
                totals.put(timer.getName(),
                        (ICloneable) timer.getTimerData().clone());
            }
            else
            {
            	oTimerData.merge(timer.getTimerData());   
            }
        }
    }

    public String[] getTimersName()
    {
        synchronized (TimerContainer.class)
        {
            return totals.keysString();
        }
    }

    public ITimerData getData(String name)
    {
        return (ITimerData) totals.get(name);
    }

    public void clear()
    {
        totals.clear();
    }
}
