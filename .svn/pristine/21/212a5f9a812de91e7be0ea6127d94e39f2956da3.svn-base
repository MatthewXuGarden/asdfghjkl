package com.carel.supervisor.base.test.timer;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.timer.*;
import junit.framework.TestCase;


public class MultiThreadClearTest extends TestCase
{
    private final static int NUM_THREAD = 10;

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(MultiThreadClearTest.class);
    }

    public void testMain() throws Throwable
    {
        BaseConfig.init();

        ITimerController timerControl = TimerMgr.getInstance().getController();
        timerControl.activate();
        timerControl.setGlobalLevel(TimerMgr.LOW);

        for (int i = 0; i < NUM_THREAD; i++)
        {
            new ThreadClear().start();
        } //for
    }
}
