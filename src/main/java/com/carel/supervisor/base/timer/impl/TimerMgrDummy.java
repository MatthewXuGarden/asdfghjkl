package com.carel.supervisor.base.timer.impl;

import com.carel.supervisor.base.timer.*;
import java.util.*;


public class TimerMgrDummy implements ITimerMgr
{
    private static final ILocalTimer localTimer = new LocalTimerDummy();
    private static final ITimerContainer timerContainer = new TimerContainerDummy();

    public ILocalTimer getTimer(String name,
        TimerMgr.TIMER_LEVEL_TYPE timerLevelType)
    {
        return localTimer;
    }

    public ITimerContainer getContainer()
    {
        return timerContainer;
    }

    public void setGlobalLevel(TimerMgr.TIMER_LEVEL_TYPE timerLevelType)
    {
    }

    public void activate()
    {
    }

    public Date getActivationTime()
    {
        return null;
    }

    public long getActivationPeriod()
    {
        return -1;
    }

    public void deActivate()
    {
    }

    public boolean isActivated()
    {
        return false;
    }

    public void clear()
    {
    }

    public boolean checkCondition(TimerMgr.TIMER_LEVEL_TYPE timerLevelType)
    {
        return false;
    }
}
