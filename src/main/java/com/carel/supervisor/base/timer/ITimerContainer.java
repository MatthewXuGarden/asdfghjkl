package com.carel.supervisor.base.timer;

public interface ITimerContainer
{
    public abstract void collect(ILocalTimer timer);

    public abstract String[] getTimersName();

    public abstract ITimerData getData(String name);

    public abstract Object clone();

    public abstract void clear();
}
