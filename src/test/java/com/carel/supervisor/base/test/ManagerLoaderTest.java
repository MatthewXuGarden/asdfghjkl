package com.carel.supervisor.base.test;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.xml.*;
import junit.framework.TestCase;


public class ManagerLoaderTest extends TestCase
{
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(ManagerLoaderTest.class);
    }

    /*
     * Test method for 'com.carel.supervisor.base.config.ManagerLoader.load(XMLNode)'
     */
    public void testLoad() throws Exception
    {
        String xml;

        xml = "<carel-config><component class=\"com.carel.supervisor.base.test.SingletonTest\">" +
            "<element type=\"z\" class=\"zz\"/>" +
            "</component></carel-config>";

        XMLNode n = XMLNode.parse(xml);

        if (SingletonTest.getInstance().status())
        {
            throw new TestRunException("Comportamento errato");
        }

        ManagerLoader.load(n);

        if (!SingletonTest.getInstance().status())
        {
            throw new TestRunException("Comportamento errato");
        }
    }
}
