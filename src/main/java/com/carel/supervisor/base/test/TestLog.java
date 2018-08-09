package com.carel.supervisor.base.test;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;


public class TestLog
{
	private static final Logger logger = LoggerMgr.getLogger(TestLog.class);
	
    private TestLog()
    {
    }

    public static void main(String[] args) throws Throwable
    {
        BaseConfig.init();

        for (int i = 0; i < 300; i++)
        {
            logger.fatal("PROVA", new TestException("prova"));
            logger.error(new TestException("prova2"));
            logger.fatal("PROVA3", new TestException("prova3"));
            logger.fatal("PROVA4", new TestException("prova4"));
        }
    }
}
