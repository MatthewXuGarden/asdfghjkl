package com.carel.supervisor.base.test;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.xml.*;
import junit.framework.TestCase;


public class MethodInvokerTest extends TestCase
{
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(MethodInvokerTest.class);
    }

    /*
     * Test method for 'com.carel.supervisor.base.config.MethodInvoker.invoke(String, XMLNode)'
     */
    public void testInvoke() throws Exception
    {
        String xml;
        MethodInvoker methodInvoker = new MethodInvoker();
        xml = "<component class=\"com.carel.supervisor.base.timer.TimerMgr\">" +
            "<element type=\"manager\" class=\"com.carel.supervisor.base.timer.impl.TimerMgrImpl\"/>" +
            "</component>";

        XMLNode n = new XMLNode();

        n = XMLNode.parse(xml);

        methodInvoker.invoke("com.carel.supervisor.base.timer.TimerMgr", n);

        try
        {
            methodInvoker.invoke(null, n);
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

        methodInvoker.invoke("com.carel.supervisor.base.timer.TimerMgr", null);
    }
}
