package com.carel.supervisor.base.timer.impl;

import com.carel.supervisor.base.timer.*;
import com.carel.supervisor.base.util.*;


public class SimpleTimerData implements ITimerData, ICloneable
{
    private long numSteps = 0;
    private long totalDeltaTime = 0;
    private long min = 0;
    private long max = 0;

    public long max()
    {
        return max;
    }

    public long min()
    {
        return min;
    }

    public void reset()
    {
        numSteps = 0;
        totalDeltaTime = 0;
        min = 0;
        max = 0;
    }

    public void collect(long delta)
    {
        totalDeltaTime += delta;
        numSteps++;

        if ((0 == min) || (min > delta))
        {
            min = delta;
        }

        if (max < delta)
        {
            max = delta;
        }
    }

    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public long count()
    {
        return numSteps;
    }

    public long totalTime()
    {
        return totalDeltaTime;
    }

    public long average()
    {
        if (0 == numSteps)
        {
            return 0;
        }

        return totalDeltaTime / numSteps;
    }

    public void merge(ITimerData timerData)
    {
        numSteps += timerData.count();
        totalDeltaTime += timerData.totalTime();

        if ((0 == min) || (min > timerData.min()))
        {
            min = timerData.min();
        }

        if (max < timerData.max())
        {
            max = timerData.max();
        }
    }

    public Object points()
    {
        return null;
    }
}
