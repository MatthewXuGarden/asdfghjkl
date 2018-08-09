package com.carel.supervisor.base.timer;

import java.util.*;


public interface ITimerController
{
    public abstract void setGlobalLevel(
        TimerMgr.TIMER_LEVEL_TYPE timerLevelType);

    public abstract Date getActivationTime();

    public abstract long getActivationPeriod();

    public abstract void deActivate();

    public abstract void activate();

    public abstract boolean isActivated();

    public abstract void clear();

    public abstract boolean checkCondition(
        TimerMgr.TIMER_LEVEL_TYPE timerLevelType);
}
