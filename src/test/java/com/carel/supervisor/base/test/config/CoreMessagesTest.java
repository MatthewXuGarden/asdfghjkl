package com.carel.supervisor.base.test.config;

import junit.framework.TestCase;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;


public class CoreMessagesTest extends TestCase
{
	private static final Logger logger = LoggerMgr.getLogger(CoreMessagesTest.class);
	public static void main(String[] args)
    {
        junit.textui.TestRunner.run(CoreMessagesTest.class);
    }

    public void testInit()
    {
    }

    public void testFormatString()
    {
        try
        {
            BaseConfig.init();
            CoreMessages.format("asd");

            //CoreMessages.format(null);
            CoreMessages.format("BSSE0001");
        }
        catch (Exception e)
        {
            logger.error(e);
        }
    }
}
