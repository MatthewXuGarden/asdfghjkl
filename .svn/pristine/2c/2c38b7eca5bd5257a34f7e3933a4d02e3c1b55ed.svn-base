package com.carel.supervisor.base.test.timer;

import junit.framework.TestCase;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.timer.ITimerController;
import com.carel.supervisor.base.timer.TimerMgr;
import com.carel.supervisor.base.timer.impl.SimpleTimerFormatter;
import com.carel.supervisor.base.xml.XMLNode;


public class TTimerMultiThreadTest extends TestCase
{
	private static final Logger logger = LoggerMgr.getLogger(TTimerMultiThreadTest.class);
	
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(TTimerMultiThreadTest.class);
    }

    /*
     * Test method for 'com.carel.supervisor.base.test.timer.TTimerMultiThread.main(String[])'
     */
    public void testMain() throws Exception
    {
        TimerMgr mgr = TimerMgr.getInstance();
        String xml =
            "<component class=\"com.carel.supervisor.base.timer.TimerMgr\"  blockingerror=\"true\">"+
		"<element name=\"manager\" value=\"com.carel.supervisor.base.timer.impl.TimerMgrImpl\"/>"+
		"<element name=\"level\" value=\"low\"/>"+
		"<element name=\"autostart\" value=\"Y\"/>"+
		"</component>";
        XMLNode node;
        node = XMLNode.parse(xml);
        mgr.init(node);

        ITimerController timerControl = TimerMgr.getInstance().getController();
        timerControl.activate();
        timerControl.setGlobalLevel(TimerMgr.LOW);

        TTimerThread timerThread = null;
        int NUM_THREADS = 4;

        for (int i = 0; i < NUM_THREADS; i++)
        {
            timerThread = new TTimerThread();
            timerThread.start();
        }

        while (NUM_THREADS != timerThread.numFinished())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
            }
        }

        SimpleTimerFormatter smplTimerForm = new SimpleTimerFormatter();
        logger.info(smplTimerForm.format(
                TimerMgr.getInstance().getContainer()));
        
    }
}
