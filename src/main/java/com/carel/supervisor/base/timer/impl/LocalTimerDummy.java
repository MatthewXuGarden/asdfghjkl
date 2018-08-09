package com.carel.supervisor.base.timer.impl;

import com.carel.supervisor.base.timer.*;


public class LocalTimerDummy implements ILocalTimer, IPrivateLocalTimer
{
    private static final String NAME = "DUMMY";
    private ITimerData timerData = null;

    public void init(String sName, ITimerData timerDataPar,
        TimerMgr.TIMER_LEVEL_TYPE timerLevelType)
    {
    }

    public ITimerData getTimerData()
    {
        return timerData;
    }

    public TimerMgr.TIMER_LEVEL_TYPE getLevel()
    {
        return TimerMgr.LOW;
    }

    public String getName()
    {
        return NAME;
    }

    public void collect()
    {
    }

    public void start()
    {
    }

    public void stop()
    {
    }
}
