package com.carel.supervisor.base.test.timer;

import junit.framework.TestCase;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.timer.ILocalTimer;
import com.carel.supervisor.base.timer.ITimerController;
import com.carel.supervisor.base.timer.TimerMgr;
import com.carel.supervisor.base.timer.impl.SimpleTimerFormatter;
import com.carel.supervisor.base.xml.XMLNode;


public class TimerTest extends TestCase
{
    private static final Logger logger = LoggerMgr.getLogger(TimerTest.class);
	public static void main(String[] args) throws Exception
    {
        junit.textui.TestRunner.run(TimerTest.class);
    } //main

    public void testMain() throws Throwable
    {
        initManager("Dummy"); //Impl or Dummy

        ITimerController timerControl = TimerMgr.getInstance().getController();
        timerControl.activate();
        timerControl.setGlobalLevel(TimerMgr.MEDIUM);

        ILocalTimer timerExt = TimerMgr.getTimer("provaExternal", TimerMgr.HIGH);
        ILocalTimer timer = TimerMgr.getTimer("prova", TimerMgr.HIGH);

        timerExt.start();

        for (int i = 0; i < 1000000; i++)
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

        String s = smplTimerForm.format(TimerMgr.getInstance().getContainer());
        logger.info(s);
    }

    private static void initManager(String Manager) throws Exception
    {
        TimerMgr mgr = TimerMgr.getInstance();
        String xml =
            "<component class=\"com.carel.supervisor.base.timer.TimerMgr\">" +
            "<element type=\"manager\" class=\"com.carel.supervisor.base.timer.impl.TimerMgr" +
            Manager + "\"/>" + "</component>";
        XMLNode node;
        node = XMLNode.parse(xml);
        mgr.init(node);
    } //initManager
} //Timer Class
