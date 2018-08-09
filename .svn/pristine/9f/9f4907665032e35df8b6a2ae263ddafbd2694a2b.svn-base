package com.carel.supervisor.base.timer;

import com.carel.supervisor.base.util.*;


public interface ITimerData extends ICloneable
{
    public abstract long max();

    public abstract long min();

    public abstract long average();

    public abstract long count();

    public abstract long totalTime();

    public abstract void collect(long delta);

    public abstract void merge(ITimerData timerData);

    public abstract Object points();

    public abstract void reset();
}
