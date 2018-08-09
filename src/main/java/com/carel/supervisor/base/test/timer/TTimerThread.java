package com.carel.supervisor.base.test.timer;

import com.carel.supervisor.base.timer.*;


public class TTimerThread extends Thread
{
    private static int finished = 0;

    public void run()
    {
        ILocalTimer timer = TimerMgr.getTimer("Main" +
                Thread.currentThread().getName(), TimerMgr.LOW);
        timer.start();

        for (int i = 0; i < 10000000; i++)
        {
            /* ILocalTimer timerTmp = TimerMgr.getTimer("Interno", TimerMgr.LOW);
             timerTmp.start();
             timerTmp.stop();*/
        }

        timer.stop();

        finished++;
    }

    public int numFinished()
    {
        return finished;
    }
}
