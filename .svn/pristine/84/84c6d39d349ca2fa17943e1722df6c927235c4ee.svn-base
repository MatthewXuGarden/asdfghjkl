package com.carel.supervisor.base.test;

import com.carel.supervisor.base.dump.*;
import com.carel.supervisor.base.xml.*;
import junit.framework.TestCase;


public class DumperMgrTest extends TestCase
{
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(DumperMgrTest.class);
    }

    /*
     * Test method for 'com.carel.supervisor.base.dump.DumperMgr.getInstance()'
     */
    public void testGetInstance()
    {
        DumperMgr.getInstance();
    }

    /*
     * Test method for 'com.carel.supervisor.base.dump.DumperMgr.init(XMLNode)'
     */
    public void testInit() throws Exception
    {
        String xml;

        xml = "<component class=\"com.carel.supervisor.base.dump.DumperMgr\">" +
            "<element type=\"java.util.List\" class=\"com.carel.supervisor.base.dump.registry.DumperList\"/>" +
            "<element type=\"java.util.ArrayList\" class=\"com.carel.supervisor.base.dump.registry.DumperList\"/>" +
            "<element type=\"java.util.Vector\" class=\"com.carel.supervisor.base.dump.registry.DumperList\"/>" +
            "<element type=\"java.util.LinkedList\" class=\"com.carel.supervisor.base.dump.registry.DumperList\"/>" +
            "<element type=\"java.util.Map\" class=\"com.carel.supervisor.base.dump.registry.DumperMap\"/>" +
            "<element type=\"java.util.HashMap\" class=\"com.carel.supervisor.base.dump.registry.DumperMap\"/>" +
            "<element type=\"java.util.Hashtable\" class=\"com.carel.supervisor.base.dump.registry.DumperMap\"/>" +
            "<element type=\"java.lang.Object\" class=\"com.carel.supervisor.base.dump.registry.DumperObject\"/>" +
            "<element type=\"com.carel.supervisor.base.dump.IDumpable\" class=\"com.carel.supervisor.base.dump.registry.DumperDumpable\"/>" +
            "<element type=\"[\" class=\"com.carel.supervisor.base.dump.registry.DumperArray\"/></component>";

        XMLNode n = XMLNode.parse(xml);

        DumperMgr.getInstance().init(n);
    }

    /*
     * Test method for 'com.carel.supervisor.base.dump.DumperMgr.getDumper(String)'
     */
    public void testGetDumper()
    {
        IDumperObject o = DumperMgr.getInstance().getDumper("com.carel.supervisor.base.timer.TimeMgr");

        if (null == o) //Il default è comunque Object
        {
            throw new TestRunException("Comportamento errato");
        }

        o = DumperMgr.getInstance().getDumper("java.util.Hashtable");

        if (null == o)
        {
            throw new TestRunException("Comportamento errato");
        }
    }
}
