package com.carel.supervisor.base.timer.impl;

import com.carel.supervisor.base.timer.*;
import java.util.*;


public class TimerMgrImpl implements ITimerMgr
{
    private static TimerMgr.TIMER_LEVEL_TYPE globalLevel = null;
    private static boolean activate = false;
    private static long activationTime = 0;
    private static ITimerContainer timerContainer = null;

    public TimerMgrImpl()
    {
        timerContainer = new TimerContainer();
    }

    public ILocalTimer getTimer(String name,
        TimerMgr.TIMER_LEVEL_TYPE timerLevelType)
    {
        IPrivateLocalTimer timer = new TimerProbe();
        ITimerData timerData = new SimpleTimerData();
        timer.init(name, timerData, timerLevelType);

        return (ILocalTimer) timer;
    }

    public ITimerContainer getContainer()
    {
        return timerContainer;
    }

    public void setGlobalLevel(TimerMgr.TIMER_LEVEL_TYPE timerLevelType)
    {
        globalLevel = timerLevelType;
    }

    public void activate()
    {
        synchronized (TimerMgrImpl.class)
        {
            activate = true;
            activationTime = System.currentTimeMillis();
            globalLevel = TimerMgr.HIGH;
        }
    }

    public Date getActivationTime()
    {
        return new Date(activationTime);
    }

    public long getActivationPeriod()
    {
        return System.currentTimeMillis() - activationTime;
    }

    public void deActivate()
    {
        if (activate)
        {
            synchronized (TimerMgrImpl.class)
            {
                activate = false;
                globalLevel = null;
                activationTime = 0;
            }
        }
    }

    public boolean isActivated()
    {
        return activate;
    }

    //Solo se già attivo posso invocare il clear 
    public void clear()
    {
        if (activate)
        {
            synchronized (TimerMgrImpl.class)
            {
                activationTime = System.currentTimeMillis();
                timerContainer.clear();
            }
        }
    }

    public boolean checkCondition(TimerMgr.TIMER_LEVEL_TYPE timerLevelType)
    {
        return ((activate) && (timerLevelType.lessAndEqual(globalLevel)));
    }
}
