package com.carel.supervisor.base.timer.impl;

import com.carel.supervisor.base.timer.*;


public class TimerProbe implements ILocalTimer, IPrivateLocalTimer
{
    private String name = null;
    private TimerMgr.TIMER_LEVEL_TYPE currentLevel = null;
    private long start = 0;
    private boolean started = false;
    private ITimerData timerData = null;

    public void init(String namePar, ITimerData timerDataPar,
        TimerMgr.TIMER_LEVEL_TYPE timerLevelType)
    {
        name = namePar;
        currentLevel = timerLevelType;
        timerData = timerDataPar;
    }

    public ITimerData getTimerData()
    {
        return timerData;
    }

    public TimerMgr.TIMER_LEVEL_TYPE getLevel()
    {
        return currentLevel;
    }

    public String getName()
    {
        return name;
    }

    public void start()
    {
        if (TimerMgr.getInstance().getController().checkCondition(getLevel()))
        {
            start = System.currentTimeMillis();
            started = true;
        }
    }

    public void stop()
    {
        if (TimerMgr.getInstance().getController().checkCondition(getLevel()))
        {
            if (started)
            {
                started = false;

                long lDelta = System.currentTimeMillis() - start;
                timerData.collect(lDelta);
                TimerMgr.getInstance().getContainer().collect(this);
                timerData.reset();
            }
        }
    }
}
