package com.carel.supervisor.base.test.timer;

import junit.framework.TestCase;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.timer.ILocalTimer;
import com.carel.supervisor.base.timer.ITimerController;
import com.carel.supervisor.base.timer.TimerMgr;
import com.carel.supervisor.base.timer.impl.SimpleTimerFormatter;


public class TTimerFrequencyTest extends TestCase
{
	private static final Logger logger = LoggerMgr.getLogger(TTimerFrequencyTest.class);
	
	public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TTimerFrequencyTest.class);
    }

    /*
     * Test method for 'com.carel.supervisor.base.test.timer.TTimerFrequency.main(String[])'
     */
    public void testMain()
    {
        try
        {
            ITimerController timerControl = TimerMgr.getInstance()
                                                    .getController();
            timerControl.activate();
            timerControl.setGlobalLevel(TimerMgr.LOW);

            ILocalTimer timer = TimerMgr.getTimer("prova", TimerMgr.LOW);

            for (int i = 0; i < 10000; i++)
            {
                timer.start();

                ILocalTimer timerInternal = TimerMgr.getTimer("provaInternal",
                        TimerMgr.LOW);
                timerInternal.start();
                timerInternal.stop();
                timer.stop();
            }

            SimpleTimerFormatter smplTimerForm = new SimpleTimerFormatter();

            logger.info(smplTimerForm.format(
                    TimerMgr.getInstance().getContainer()));
        }
        catch (Exception e)
        {
            logger.error(e);
        }
    }
}
