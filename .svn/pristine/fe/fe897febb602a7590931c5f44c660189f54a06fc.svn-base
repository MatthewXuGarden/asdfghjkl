package com.carel.supervisor.base.test;

import junit.framework.TestCase;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;


public class TestDumperTest extends TestCase
{
	private static final Logger logger = LoggerMgr.getLogger(TestDumperTest.class);
    public static void main(String[] args)
    {
        junit.swingui.TestRunner.run(TestDumperTest.class);
    }

    public void test1()
    {
        TestDumper test = new TestDumper();
        logger.info(test.getDumpWriter().getStringBuffer().toString());
    }

    public void test2()
    {
        TestC test = new TestC();
        logger.info(test.getDumpWriter().getStringBuffer().toString());
    }
}
