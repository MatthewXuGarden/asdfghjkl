package com.carel.supervisor.base.test.timer;

import com.carel.supervisor.base.timer.ITimerContainer;
import com.carel.supervisor.base.timer.ITimerFormatter;


public class MyTimerFormatter implements ITimerFormatter
{
    public String format(ITimerContainer timerContainer)
    {
        String[] tc = timerContainer.getTimersName();
        StringBuffer tmp = new StringBuffer();
        
        for (int i = 0; i < tc.length; i++)
        {
            tmp.append("\n" + tc[i]);
        }

        return tmp.toString();
    }
}
