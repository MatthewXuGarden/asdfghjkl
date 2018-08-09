package com.carel.supervisor.base.test;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.test.config.CoreMessagesTest;
import com.carel.supervisor.base.test.timer.MultiThreadClearTest;
import com.carel.supervisor.base.test.timer.TTimerFrequencyTest;
import com.carel.supervisor.base.test.timer.TTimerMultiThreadTest;
import com.carel.supervisor.base.test.timer.TTimerNormalTest;
import com.carel.supervisor.base.test.timer.TimerTest;
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
                "Test for com.carel.supervisor.base.test");

        //$JUnit-BEGIN$
        suite.addTestSuite(DateUtilsTest.class);
        suite.addTestSuite(DumpWriterTest.class);
        suite.addTestSuite(LoggerTest.class);
        suite.addTestSuite(ManagerLoaderTest.class);
        suite.addTestSuite(MethodInvokerTest.class);
        suite.addTestSuite(DumperMgrTest.class);
        suite.addTestSuite(XMLNodeTest.class);
        suite.addTestSuite(ReplacerTest.class);
        suite.addTestSuite(TestDumperTest.class);

        //dal packeg com.carel.supervisor.base.profiling
        suite.addTestSuite(ProfilingMgrTest.class);
        suite.addTestSuite(UserCredentialTest.class);

        //dal packeg com.carel.supervisor.base.profiling.impl
        suite.addTestSuite(UserProfileTest.class);
        suite.addTestSuite(SectionProfileTest.class);
        suite.addTestSuite(LDAPProfilerTest.class);

        //nuovi
        suite.addTestSuite(BaseContainerTest.class);
        suite.addTestSuite(SimpleContainerTest.class);

        //config test
        suite.addTestSuite(CoreMessagesTest.class);

        //timer test
        suite.addTestSuite(MultiThreadClearTest.class);
        suite.addTestSuite(TimerTest.class);
        suite.addTestSuite(TTimerFrequencyTest.class);
        suite.addTestSuite(TTimerMultiThreadTest.class);
        suite.addTestSuite(TTimerNormalTest.class);

        //Crypter
        suite.addTestSuite(CrypterTest.class);

        //da Printer
//        suite.addTestSuite(PrinterMgrTest.class);
//        suite.addTestSuite(TemplateMgrTest.class);

        return suite;
    }
}
