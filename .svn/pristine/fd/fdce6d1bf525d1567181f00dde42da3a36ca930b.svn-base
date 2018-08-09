package com.carel.supervisor.base.timer.impl;

import com.carel.supervisor.base.timer.*;
import java.util.*;


public class SimpleTimerFormatter implements ITimerFormatter
{
    private static final String SEPARATORE = "-----------------------------------------------\n\r";

    public String format(ITimerContainer timerContainer)
    {
        return format(timerContainer, false);
    }

    public String format(ITimerContainer timerContainer, boolean details)
    {
        String[] names = timerContainer.getTimersName();

        if (null == names)
        {
            return null;
        }

        Arrays.sort(names);

        StringBuffer buffer = new StringBuffer();
        ITimerData timerData = null;

        for (int i = 0; i < names.length; i++)
        {
            timerData = timerContainer.getData(names[i]);
            buffer.append(SEPARATORE);
            buffer.append("Timer: ");
            buffer.append(names[i]);
            buffer.append("\n\r");

            if (0 < timerData.count())
            {
                buffer.append("Total: ");
                buffer.append(timerData.totalTime());
                buffer.append("\n\rMax: ");
                buffer.append(timerData.max());
                buffer.append("\n\rMin: ");
                buffer.append(timerData.min());
                buffer.append("\n\rCount: ");
                buffer.append(timerData.count());
                buffer.append("\n\rAvarage: ");
                buffer.append(timerData.average());
                buffer.append("\n\r");
            }
        }

        return buffer.toString();
    }
}
