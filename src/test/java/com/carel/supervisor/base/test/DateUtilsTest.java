package com.carel.supervisor.base.test;

import java.util.Date;

import junit.framework.TestCase;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;


public class DateUtilsTest extends TestCase
{
	private static final Logger logger = LoggerMgr.getLogger(DateUtilsTest.class);
	
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(DateUtilsTest.class);
    }

    /*
     * Test method for 'com.carel.supervisor.base.conversion.DateUtils.resetTime(Date)'
     */
    public void testResetTime()
    {
        logger.info("DATA SENZA ORE: " +
            DateUtils.resetTime(new Date()).toString());

        try
        {
        	logger.info(DateUtils.resetTime(null).toString());
            throw new TestRunException("Comportamento errato");
        }
        catch (TestRunException e)
        {
        	throw e;
        }
        catch (Exception e)
        {
            assertNotNull("OK",e);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.conversion.DateUtils.resetDate(Date)'
     */
    public void testResetDate()
    {
    	logger.info("ORE SENZA DATA: " +
            DateUtils.resetDate(new Date()).toString());

        try
        {
        	logger.info(DateUtils.resetDate(null).toString());
            throw new TestRunException("Comportamento errato");
        }
        catch (TestRunException e)
        {
        	throw e;
        }
        catch (Exception e)
        {
            assertNotNull("OK",e);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.conversion.DateUtils.createDate(int, int, int)'
     */
    public void testCreateDateIntIntInt()
    {
    	logger.info(DateUtils.createDate(12, 12, 12).toString());

        try
        {
        	logger.info("Strange Date:" +
                DateUtils.createDate(-12, -12, -12).toString()); //DEVE ESPLODERE
            throw new TestRunException("Comportamento errato");
        }
        catch (TestRunException e)
        {
        	throw e;
        }
        catch (Exception e)
        {
            assertNotNull("OK",e);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.conversion.DateUtils.createDate(int, int, int, int, int, int)'
     */
    public void testCreateDateIntIntIntIntIntInt()
    {
    	logger.info(DateUtils.createDate(1, 1, 1, 1, 1, 1).toString());

        try
        {
        	logger.info("Strange Date&Ora:" +
                DateUtils.createDate(-12, -12, -12, -12, -12, -12).toString());
            throw new TestRunException("Comportamento errato");
        }
        catch (TestRunException e)
        {
        	throw e;
        }
        catch (Exception e)
        {
            assertNotNull("OK",e);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.conversion.DateUtils.date2String(Date, String)'
     */
    public void testDate2String()
    {
    	logger.info(DateUtils.date2String(new Date(), "dd/MM/yyyy"));
    }
}
