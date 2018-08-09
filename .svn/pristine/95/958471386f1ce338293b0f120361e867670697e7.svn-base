package com.carel.supervisor.base.test;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import junit.framework.TestCase;


public class DumpWriterTest extends TestCase
{
    private static final Logger logger = LoggerMgr.getLogger(DumpWriterTest.class);

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(DumpWriterTest.class);
    }

    public void testPrintStringObject() throws Exception
    {
        DumpWriter dw = init();
        logger.info("TestPrint(String,Object)");
        dw.print("Test", "ValorediTest");
        logger.info(dw.getStringBuffer().toString());
    }

    public void testCreateDumpWriterStringClass() throws Exception
    {
        DumpWriter dw = init();
        logger.info("CreateDumpWriter(String,Class)");
        dw.createDumpWriter(null, this.getClass());
        dw.createDumpWriter("TestName", this.getClass());
        logger.info(dw.getStringBuffer().toString());
    }

    /*
     * Test method for 'com.carel.supervisor.base.dump.DumpWriter.createDumpWriter(String, String)'
     */
    public void testCreateDumpWriterStringString() throws Exception
    {
        DumpWriter dw = init();
        logger.info("CreateDumpWriter(String,String)");
        dw.createDumpWriter("TestName", "TestString");
        dw.createDumpWriter(null, (String) null);
        logger.info(dw.getStringBuffer().toString());
    }

    /*
     * Test method for 'com.carel.supervisor.base.dump.DumpWriter.print(Object)'
     */
    public void testPrintObject() throws Exception
    {
        DumpWriter dw = init();
        logger.info("Print(Object)");

        XMLNode n = XMLNode.parse("<a><b><c/></b></a>");

        dw.print(n);
        logger.info(dw.getStringBuffer().toString());
    }

    /*
     * Test method for 'com.carel.supervisor.base.dump.DumpWriter.getStringBuffer()'
     */
    public void testGetStringBuffer() throws Exception
    {
        DumpWriter dw = init();
        logger.info("GetStringBuffer()");
        logger.info(dw.getStringBuffer().toString());
    }

    /*
     * Test method for 'com.carel.supervisor.base.dump.DumpWriter.getStream()'
     */
    public void testGetStream() throws Exception
    {
        DumpWriter dw = init();
        logger.info("GetStream()");
        logger.info(dw.getStream().toString());
    }

    /*
     * Test method for 'com.carel.supervisor.base.dump.DumpWriter.formatObject(String, String, String)'
     */
    public void testFormatObject() throws Exception
    {
        DumpWriter dw = init();
        logger.info("FormatObject(String,String,String)");

        XMLNode n = (XMLNode) dw.formatObject("werwerwer", "23", "String");
        dw.print(n);
        logger.info(dw.getStringBuffer().toString());
    }

    /*
     * Test method for 'com.carel.supervisor.base.dump.DumpWriter.formatObjectSon(String, Object)'
     */
    public void testFormatObjectSon() throws Exception
    {
        DumpWriter dw = init();
        logger.info("FormatObjectSon(String,Object)");
        dw.formatObjectSon("Prova", dw.getStream());
        logger.info(dw.getStringBuffer().toString());
    }

    private DumpWriter init() throws Exception
    {
        DumperMgr dm = DumperMgr.getInstance();

        DumpWriter dw = null;

        XMLNode n = XMLNode.parse(
                "<component class=\"com.carel.supervisor.base.dump.DumperMgr\">" +
                "<element type=\"java.util.List\" class=\"com.carel.supervisor.base.dump.registry.DumperList\"/>" +
                "<element type=\"java.util.ArrayList\" class=\"com.carel.supervisor.base.dump.registry.DumperList\"/>" +
                "<element type=\"java.util.Vector\" class=\"com.carel.supervisor.base.dump.registry.DumperList\"/>" +
                "<element type=\"java.util.LinkedList\" class=\"com.carel.supervisor.base.dump.registry.DumperList\"/>" +
                "<element type=\"java.util.Map\" class=\"com.carel.supervisor.base.dump.registry.DumperMap\"/>" +
                "<element type=\"java.util.HashMap\" class=\"com.carel.supervisor.base.dump.registry.DumperMap\"/>" +
                "<element type=\"java.util.Hashtable\" class=\"com.carel.supervisor.base.dump.registry.DumperMap\"/>" +
                "<element type=\"java.lang.Object\" class=\"com.carel.supervisor.base.dump.registry.DumperObject\"/>" +
                "<element type=\"com.carel.supervisor.base.dump.IDumpable\" class=\"com.carel.supervisor.base.dump.registry.DumperDumpable\"/>" +
                "<element type=\"[\" class=\"com.carel.supervisor.base.dump.registry.DumperArray\"/></component>");
        dm.init(n);
        dw = DumperMgr.createDumpWriter("Test", this);

        return dw;
    }
}
