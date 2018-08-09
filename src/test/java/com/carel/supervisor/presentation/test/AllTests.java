package com.carel.supervisor.presentation.test;

import com.carel.supervisor.base.config.BaseConfig;
import junit.framework.Test;
import junit.framework.TestSuite;


public class AllTests
{
    public static void main(String[] args)
    {
        junit.swingui.TestRunner.run(AllTests.class);
    }

    public static Test suite() throws Throwable
    {
        BaseConfig.init();

        TestSuite suite = new TestSuite(
                "Test for com.carel.supervisor.presentation.test");

        suite.addTestSuite(HTMLTest.class);

        return suite;
    }
}
