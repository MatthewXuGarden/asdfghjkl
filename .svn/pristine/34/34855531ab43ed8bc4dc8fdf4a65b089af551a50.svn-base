package com.carel.supervisor.base.test.timer;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.timer.ILocalTimer;
import com.carel.supervisor.base.timer.TimerMgr;
import com.carel.supervisor.base.timer.impl.SimpleTimerFormatter;


public class ThreadClear extends Thread
{
    private static int countThread = 0;
    private static final Logger logger = LoggerMgr.getLogger(ThreadClear.class);
    private int threadNumber = 0;

    public ThreadClear()
    {
        threadNumber = countThread++;
    }

    public void run()
    {
        if (threadNumber != 9)
        {
            ILocalTimer timer = TimerMgr.getTimer("Main" +
                    Thread.currentThread().getName(), TimerMgr.HIGH);
            timer.start();

            for (int i = 0; i < 10; i++)
            {
            }

            timer.stop();
        } //if
        else
        {
            try
            {
                logger.info("Inizio");

                int tmp = 1;

                while (tmp != 10)
                {
                    logger.info(".");
                    sleep(tmp * 300);
                    tmp++;
                }

                TimerMgr.getInstance().clear();

                SimpleTimerFormatter smplTimerForm = new SimpleTimerFormatter();
                logger.info(smplTimerForm.format(TimerMgr.getInstance()
                                                         .getContainer()));
                logger.info(
                    "Fine\nCorretto se tra inizio e fine non ci sono dati");
            } //try
            catch (Exception e)
            {
                logger.info("Mi ha svegliato un'eccezione...");
            } //cathc
        } //else
    } //run
} //ThreadClear
