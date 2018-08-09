package com.carel.supervisor.base.test;

import com.carel.supervisor.base.log.*;
import junit.framework.TestCase;


public class LoggerTest extends TestCase
{
    private static Logger l = null;

    public LoggerTest()
    {
        LoggerMgr.initialize(
            "C:\\swdept_prj\\plantvisorpro\\developments\\applications\\baseservices\\conf\\log4j.properties");
        l = LoggerMgr.getLogger(LoggerTest.class);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(LoggerTest.class);
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.isDebugEnabled()'
     */
    public void testIsDebugEnabled()
    {
        l.info("[testIsDebugEnabled] Debug Enabled: " + l.isDebugEnabled());
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.level()'
     */
    public void testLevel()
    {
        l.info("[testLevel] Level: " + l.level().toString());
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.name()'
     */
    public void testName()
    {
        l.info("[testName] Name: " + l.name());
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.debug(String)'
     */
    public void testDebugString()
    {
        l.info("[testDebugString] DeBug");
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.debug(String, IDumpable)'
     */
    public void testDebugStringIDumpable()
    {
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.info(String)'
     */
    public void testInfoString()
    {
        l.info("[testInfoString] Info");
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.warn(String)'
     */
    public void testWarnString()
    {
        l.warn("[testWarnString] WARNING");
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.warn(String, Throwable)'
     */
    public void testWarnStringThrowable()
    {
        l.warn("[testWarnStringThrowable] WARNING", new TestException("TEST"));
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.error(String)'
     */
    public void testErrorString()
    {
        l.error("[testErrorString] ERROR");
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.error(String, Throwable)'
     */
    public void testErrorStringThrowable()
    {
        l.error("[testErrorStringThrowable] ERROR", new TestException("TEST"));
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.error(Throwable)'
     */
    public void testErrorThrowable()
    {
        l.error(new TestException("[testErrorThrowable] TEST"));
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.fatal(String)'
     */
    public void testFatalString()
    {
        l.fatal("[testFatalString] FATAL");
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.fatal(String, Throwable)'
     */
    public void testFatalStringThrowable()
    {
        l.fatal("[testFatalStringThrowable] FATAL", new TestException("TEST"));
    }

    /*
     * Test method for 'com.carel.supervisor.base.log.Logger.isEnabledFor(LEVEL)'
     */
    public void testIsEnabledFor()
    {
        l.info("[testIsEnabledFor] DEBUG: " +
            String.valueOf(l.isEnabledFor(LoggerMgr.DEBUG)));
        l.info("[testIsEnabledFor] ERROR: " +
            String.valueOf(l.isEnabledFor(LoggerMgr.ERROR)));
        l.info("[testIsEnabledFor] FATAL: " +
            String.valueOf(l.isEnabledFor(LoggerMgr.FATAL)));
        l.info("[testIsEnabledFor] INFO: " +
            String.valueOf(l.isEnabledFor(LoggerMgr.INFO)));
        l.info("[testIsEnabledFor] WARN: " +
            String.valueOf(l.isEnabledFor(LoggerMgr.WARN)));
    }
}
