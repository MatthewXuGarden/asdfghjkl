package com.carel.supervisor.base.test.timer;

import junit.framework.TestCase;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.timer.ILocalTimer;
import com.carel.supervisor.base.timer.ITimerController;
import com.carel.supervisor.base.timer.TimerMgr;
import com.carel.supervisor.base.timer.impl.SimpleTimerFormatter;


public class TTimerNormalTest extends TestCase
{
    private static final Logger logger = LoggerMgr.getLogger(TTimerNormalTest.class);

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TTimerNormalTest.class);
    }

    /*
     * Test method for 'com.carel.supervisor.base.test.timer.TTimerNormal.main(String[])'
     */
    public void testMain() throws Throwable
    {
        //    	PropertyMgr.getPropertyService("timer").addProperty("com.bway.bwa.core.performance.ITimerData", "com.bway.bwa.core.performance.SimpleTimerData");
        BaseConfig.init();

        ITimerController timerControl = TimerMgr.getInstance().getController();
        timerControl.activate();
        timerControl.setGlobalLevel(TimerMgr.LOW);

        ILocalTimer timerExt = TimerMgr.getTimer("provaExternal", TimerMgr.LOW);
        ILocalTimer timer = TimerMgr.getTimer("prova", TimerMgr.LOW);

        //ILocalTimer oTimerInternal = TimerMgr.getTimer("provaInternal", TimerMgr.LOW);
        timerExt.start();

        for (int i = 0; i < 100000; i++)
        {
            ILocalTimer timerInternal = TimerMgr.getTimer("provaInternal",
                    TimerMgr.LOW);
            timer.start();
            timerInternal.start();
            timerInternal.stop();
            timer.stop();
        }

        timerExt.stop();

        SimpleTimerFormatter smplTimerForm = new SimpleTimerFormatter();

        logger.info(smplTimerForm.format(TimerMgr.getInstance().getContainer()));
    }
}
