package com.carel.supervisor.base.test;

import com.carel.supervisor.base.xml.*;
import junit.framework.*;
import java.util.*;


public class XMLNodeTest extends TestCase
{
    private final static String SON1 = "son1";
    private final static String SON2 = "son2";
    private final static String SON3 = "son3";
    private final static String SON = "son";
    private final static String FATHER = "father";
    private final static String ERR_PARSE = "Parse non funzionato correttamente";
    private final static String ERR_SET = "Set non ha funzionato correttamente";
    private final static String ERR_COMP = "Comportamento errato";
    private final static String NEW_SON = "NEW SON";
    private final static String TEST = "Test";
    private final static String TESTO = "Testo";
    private final static String TESTO2 = "Testo2";
    private final static String TEST1 = "Test1";
    private final static String TEST2 = "Test2";
    private final static String ATTR = "attr";
    private final static String PROVA = "prova";

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(XMLNodeTest.class);
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.XMLNode(String, String)'
     */
    public void testXMLNodeStringString()
    {
        XMLNode xmlNode1 = new XMLNode(TEST, TEST);

        try
        {
            xmlNode1 = new XMLNode(null, TEST);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }

        xmlNode1 = new XMLNode(TEST, null);

        try
        {
            xmlNode1 = new XMLNode(null, null);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }

        xmlNode1.clear();
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.XMLNode()'
     */
    public void testXMLNode()
    {
        XMLNode xmlNode1 = new XMLNode();
        xmlNode1.clear();
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.XMLNode(String, String, HashMap, ArrayList)'
     */
    public void testXMLMapArrayList1()
    {
        HashMap map = new HashMap(23);
        ArrayList list = new ArrayList();
        XMLNode xmlNode1 = new XMLNode(SON1, null);
        XMLNode xmlNode2 = new XMLNode(SON2, null);
        list.add(xmlNode1);
        list.add(xmlNode2);

        XMLNode xmlNode3 = null;

        try
        {
            xmlNode3 = new XMLNode(null, null, null, null);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }

        try
        {
            xmlNode3 = new XMLNode(null, null, null, list);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }

        try
        {
            xmlNode3 = new XMLNode(null, null, map, null);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }

        try
        {
            xmlNode3 = new XMLNode(null, null, map, list);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }

        try
        {
            xmlNode3 = new XMLNode(null, TEST, null, null);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }

        try
        {
            xmlNode3 = new XMLNode(null, TEST, null, list);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }

        try
        {
            xmlNode3 = new XMLNode(null, TEST, map, null);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }
        
        xmlNode3 = new XMLNode("P", TEST, map, null);
        xmlNode3.clear();
    }

    public void testXMLMapArrayList2()
    {
        HashMap map = new HashMap(23);
        ArrayList list = new ArrayList();
        XMLNode xmlNode1 = new XMLNode(SON1, null);
        XMLNode xmlNode2 = new XMLNode(SON2, null);
        list.add(xmlNode1);
        list.add(xmlNode2);

        XMLNode xmlNode3 = null;

        try
        {
            xmlNode3 = new XMLNode(null, TEST, map, list);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }

        xmlNode3 = new XMLNode(TEST, null, null, null);

        if (!xmlNode3.getNodeName().equals(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode3 = new XMLNode(TEST, null, null, list);
        xmlNode3 = new XMLNode(TEST, null, map, null);
        xmlNode3 = new XMLNode(TEST, null, map, list);
        xmlNode3 = new XMLNode(TEST, TESTO, null, null);

        if (!xmlNode3.getTextValue().equals(TESTO))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode3 = new XMLNode(TEST, TEST, null, list);
        xmlNode3 = new XMLNode(TEST, TEST, map, null);
        xmlNode3 = new XMLNode(TEST, TEST, map, list);

        xmlNode3.clear();
    }
    
    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.set(String, String)'
     */
    public void testSet()
    {
        XMLNode xmlNode1 = new XMLNode();

        try
        {
            xmlNode1.set(null, null);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }

        try
        {
            xmlNode1.set(null, TEST);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }

        xmlNode1.set(TEST, null);

        if (!xmlNode1.getNodeName().equals(TEST))
        {
            throw new XMLTestException(ERR_SET);
        }

        xmlNode1.set(TEST, TESTO);

        if (!xmlNode1.getTextValue().equals(TESTO))
        {
            throw new XMLTestException(ERR_SET);
        }

        xmlNode1.set(TEST1, TESTO);

        if (!xmlNode1.getNodeName().equals(TEST1))
        {
            throw new XMLTestException(ERR_SET);
        }

        if (!xmlNode1.getTextValue().equals(TESTO))
        {
            throw new XMLTestException(ERR_SET);
        }

        xmlNode1.clear();
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.parse(InputStream)'
     */
    public void testParse() throws XMLTestException
    {
        XMLNode xmlNode = null;

        try
        {
            xmlNode = XMLNode.parse("<a c=\"1\">testo<b/><b/></a>");
        }
        catch (Exception e)
        {
            throw new XMLTestException("ERROR PARSING");
        }

        if (!xmlNode.getNodeName().equals("a"))
        {
            throw new XMLTestException(ERR_PARSE);
        }

        if (!xmlNode.getTextValue().equals("testo"))
        {
            throw new XMLTestException(ERR_PARSE);
        }


        if (xmlNode.size() != 2)
        {
            throw new XMLTestException(ERR_PARSE);
        }

        if (!xmlNode.hasAttribute("c"))
        {
            throw new XMLTestException(ERR_PARSE);
        }

        if (!xmlNode.getAttribute("c").equals("1"))
        {
            throw new XMLTestException(ERR_PARSE);
        }

        
        if (!xmlNode.getNode(0).getNodeName().equals("b"))
        {
            throw new XMLTestException(ERR_PARSE);
        }
    }

    public void testParse2() throws XMLTestException
    {
        XMLNode xmlNode = null;

        try
        {
            xmlNode = XMLNode.parse(
                    "<a c=\"1\">testo<b/><!--commento--><b/></a>"); //I commenti non vengono considerati
        }
        catch (Exception e)
        {
           throw new XMLTestException();
        }

        if (!xmlNode.getNodeName().equals("a"))
        {
            throw new XMLTestException(ERR_PARSE);
        }

        if (!xmlNode.getTextValue().equals("testo"))
        {
            throw new XMLTestException(ERR_PARSE);
        }
        
        if (xmlNode.size() != 2)
        {
            throw new XMLTestException(ERR_PARSE);
        }

        if (!xmlNode.hasAttribute("c"))
        {
            throw new XMLTestException(ERR_PARSE);
        }

        if (!xmlNode.getAttribute("c").equals("1"))
        {
            throw new XMLTestException(ERR_PARSE);
        }

        if (!xmlNode.getNode(0).getNodeName().equals("b"))
        {
            throw new XMLTestException(ERR_PARSE);
        }
    }
    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.setNodeName(String)'
     */
    public void testSetNodeName()
    {
        XMLNode xmlNode1 = new XMLNode("p", "o");
        xmlNode1.setNodeName(TEST);

        if (!xmlNode1.getNodeName().equals(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }

        try
        {
            xmlNode1.setNodeName(null);
            throw new XMLTestException(ERR_COMP);
        }
        catch (InvalidNodeNameException e)
        {
            assertNotNull("OK", e);
        }

        xmlNode1.setNodeName(TEST1); //2 set di fila

        if (!xmlNode1.getNodeName().equals(TEST1))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.getNodeName()'
     */
    public void testGetNodeName()
    {
        XMLNode xmlNode1 = new XMLNode("p", "c");

        if (!xmlNode1.getNodeName().equals("p"))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.setTextValue(String)'
     */
    public void testSetTextValue()
    {
        XMLNode xmlNode1 = new XMLNode();
        xmlNode1.setTextValue(TEST);

        if (!xmlNode1.getTextValue().equals(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.setTextValue(null); //posso annulare il testo
        xmlNode1.setTextValue(TEST2); //2 set di fila

        if (!xmlNode1.getTextValue().equals(TEST2))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.getTextValue()'
     */
    public void testGetTextValue()
    {
        XMLNode xmlNode1 = new XMLNode("p", "c");

        if (!xmlNode1.getTextValue().equals("c"))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.addNodes(XMLNode[])'
     */
    public void testAddNodes()
    {
        XMLNode xmlNode1 = new XMLNode(FATHER, null);
        XMLNode xmlNode2 = new XMLNode(SON1, null);
        XMLNode xmlNode3 = new XMLNode(SON2, null);

        XMLNode[] list = new XMLNode[2];
        list[0] = xmlNode2;
        list[1] = xmlNode3;
        xmlNode1.addNodes(list);

        if (!xmlNode1.getNode(0).getNodeName().equals(SON1))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode1.getNode(1).getNodeName().equals(SON2))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNodes(list); //ne aggiungo altri 2 uguali

        if (!xmlNode1.getNode(0).getNodeName().equals(SON1))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode1.getNode(1).getNodeName().equals(SON2))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode1.getNode(2).getNodeName().equals(SON1))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode1.getNode(3).getNodeName().equals(SON2))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNodes(null); //resetto tutto
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.addNode(String, String)'
     */
    public void testAddNodeStringString()
    {
        XMLNode xmlNode1 = new XMLNode("padre", null);
        xmlNode1.addNode(SON1, TESTO);

        if (!xmlNode1.getNode(0).getNodeName().equals(SON1))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode1.getNode(0).getTextValue().equals(TESTO))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(SON2, TESTO2);

        if (!xmlNode1.getNode(0).getNodeName().equals(SON1))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode1.getNode(0).getTextValue().equals(TESTO))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode1.getNode(1).getNodeName().equals(SON2))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode1.getNode(1).getTextValue().equals(TESTO2))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(SON3, null); //Si può fare

        try
        {
            xmlNode1.addNode(null, TESTO);
            throw new XMLTestException(ERR_COMP);
        }
        catch (XMLTestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            assertNotNull("OK", e);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.addNode(XMLNode)'
     */
    public void testAddNodeXMLNode()
    {
        XMLNode xmlNode = new XMLNode(FATHER, null);
        XMLNode xmlNode1 = new XMLNode(SON1, null);
        XMLNode xmlNode2 = new XMLNode(SON2, null);
        xmlNode.addNode(xmlNode1);

        if (!xmlNode.getNode(0).getNodeName().equals(SON1))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode.addNode(xmlNode2);

        if (!xmlNode.getNode(0).getNodeName().equals(SON1))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode.getNode(1).getNodeName().equals(SON2))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode.addNode(null); //si può fare
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.setNode(String, int, XMLNode)'
     */
    public void testSetNodeStringIntXMLNode()
    {
        XMLNode xmlNode1 = new XMLNode(PROVA, TESTO);
        XMLNode xmlNode1Son = new XMLNode(SON, TESTO);
        XMLNode xmlNode2Son = new XMLNode(SON, TESTO);
        XMLNode xmlNodeNewSon = new XMLNode(SON, NEW_SON);
        xmlNode1.addNode(xmlNode1Son);
        xmlNode1.addNode(xmlNode2Son);
        xmlNode1.setNode(SON, 1, xmlNodeNewSon);

        if (xmlNode1.getNode(0).getTextValue().equals(NEW_SON)) //il primo nodo deve rimanere invariato
        {
            throw new XMLTestException(ERR_SET);
        }

        if (!xmlNode1.getNode(1).getTextValue().equals(NEW_SON)) //il secondo nodo deve essere modificato
        {
            throw new XMLTestException(ERR_SET);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.setNode(String, XMLNode)'
     */
    public void testSetNodeStringXMLNode()
    {
        XMLNode xmlNode1 = new XMLNode(PROVA, TESTO);
        XMLNode xmlNode1Son = new XMLNode(SON1, TESTO);
        XMLNode xmlNode2Son = new XMLNode(SON2, TESTO);
        XMLNode xmlNodeNewSon = new XMLNode(SON1, NEW_SON);
        xmlNode1.addNode(xmlNode1Son);
        xmlNode1.addNode(xmlNode2Son);
        xmlNode1.setNode(SON2, xmlNodeNewSon);

        if (xmlNode1.getNode(0).getTextValue().equals(NEW_SON)) //il primo nodo deve rimanere invariato
        {
            throw new XMLTestException(ERR_SET);
        }

        if (!xmlNode1.getNode(1).getTextValue().equals(NEW_SON)) //il secondo nodo deve essere modificato
        {
            throw new XMLTestException(ERR_SET);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.removeNode(String)'
     */
    public void testRemoveNodeString()
    {
        XMLNode xmlNode1 = new XMLNode(PROVA, TESTO);

        xmlNode1.removeNode(null);

        xmlNode1.addNode(TEST, TEST);

        if (!xmlNode1.removeNode(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (xmlNode1.size() > 0)
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (xmlNode1.removeNode("Un Nodo che Non Esiste"))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.removeNode(String, int)'
     */
    public void testRemoveNodeStringInt()
    {
        XMLNode xmlNode1 = new XMLNode(PROVA, TESTO);
        XMLNode xmlNode1Son = new XMLNode(SON1, TESTO);
        XMLNode xmlNode2Son = new XMLNode(SON2, TESTO);
        xmlNode1.addNode(xmlNode1Son);
        xmlNode1.addNode(xmlNode2Son);

        if (xmlNode1.removeNode(SON1, 1)) //Nodo che esite in una posizione che non esiste
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(SON1, "testoson2");

        if (!xmlNode1.removeNode(SON1, 0)) //Nodo ora esiste
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (xmlNode1.removeNode(null, 0))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.removeNodes(String)'
     */
    public void testRemoveNodesString()
    {
        XMLNode xmlNode1 = new XMLNode();

        if (xmlNode1.removeNodes(null))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (xmlNode1.removeNodes(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(TEST, TEST1);
        xmlNode1.addNode(TEST, TEST2);
        xmlNode1.addNode("aa", "aa");

        if (!xmlNode1.removeNodes(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (xmlNode1.size() != 1)
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.removeNodes()'
     */
    public void testRemoveNodes()
    {
        XMLNode xmlNode1 = new XMLNode();
        xmlNode1.addNode(TEST, TEST1);
        xmlNode1.addNode(TEST, TEST2);
        xmlNode1.removeNodes();

        if (xmlNode1.size() > 0)
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.removeNodes();
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.hasNode(String)'
     */
    public void testHasNodeString()
    {
        XMLNode xmlNode1 = new XMLNode(PROVA, null);

        if (xmlNode1.hasNode(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(TEST, TEST2);

        if (!xmlNode1.hasNode(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.hasNode(String, int)'
     */
    public void testHasNodeStringInt()
    {
        XMLNode xmlNode1 = new XMLNode();
        xmlNode1.addNode(TEST, TEST2);

        if (!xmlNode1.hasNode(TEST, 0))
        {
            throw new XMLTestException(
                "Si parte da 0 con il conteggio dei nodi");
        }

        if (xmlNode1.hasNode(TEST, 1))
        {
            throw new XMLTestException(
                "Si parte da 0 con il conteggio dei nodi");
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.hasNodes()'
     */
    public void testHasNodes()
    {
        XMLNode xmlNode1 = new XMLNode();

        if (xmlNode1.hasNodes())
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(TEST, TEST2);

        if (!xmlNode1.hasNodes())
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(TEST, TEST2);

        if (!xmlNode1.hasNodes())
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.clear()'
     */
    public void testClear() throws XMLTestException
    {
        XMLNode xmlNode1 = null;

        try
        {
            xmlNode1 = XMLNode.parse(
                    "<a class=\"com.carel.supervisor.base.timer.TimerMgr\" miao=\"xs\" > <b/><b/></a>");
        }
        catch (Exception e)
        {
            throw new XMLTestException();
        }

        if (!xmlNode1.hasNodes())
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.clear();

        if (xmlNode1.hasNodes())
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.getNodes(String)'
     */
    public void testGetNodesString()
    {
        XMLNode xmlNode1 = new XMLNode();
        xmlNode1.addNode(TEST, TEST2);

        XMLNode[] xmlNode = xmlNode1.getNodes(TEST);

        if (xmlNode.length == 0)
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(TEST, TEST2);
        xmlNode1.addNode(TEST, TEST2);
        xmlNode1.addNode(TEST, TEST2);
        xmlNode1.addNode("Altro", TEST2);
        xmlNode = xmlNode1.getNodes(TEST);

        if (xmlNode.length == 0)
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode[0].getNodeName().equals(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode[0].getTextValue().equals(TEST2))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (xmlNode.length != 4)
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.getNodes()'
     */
    public void testGetNodes()
    {
        XMLNode xmlNode1 = new XMLNode("p", "o");
        XMLNode[] xmlNode = xmlNode1.getNodes();

        if (null != xmlNode)
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(TEST, TEST2);
        xmlNode1.addNode(TEST, TEST2);
        xmlNode1.addNode(TEST, TEST2);
        xmlNode1.addNode("Altro", TEST2);
        xmlNode = xmlNode1.getNodes();

        if (xmlNode.length == 0)
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode[0].getNodeName().equals(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode[0].getTextValue().equals(TEST2))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (xmlNode.length != 4)
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.getNode(String)'
     */
    public void testGetNodeString()
    {
        XMLNode xmlNode1 = new XMLNode("alfa", "beta");
        xmlNode1.addNode("a", "a1");
        xmlNode1.addNode("b", "b1");
        xmlNode1.addNode("c", "c1");

        XMLNode xmlNode = xmlNode1.getNode("b");

        if (!xmlNode.getTextValue().equals("b1"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode.getNodeName().equals("b"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode("a");

        if (!xmlNode.getTextValue().equals("a1"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode.getNodeName().equals("a"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode("c");

        if (!xmlNode.getTextValue().equals("c1"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode.getNodeName().equals("c"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode(null);

        if (null != xmlNode)
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode("TEST");

        if (null != xmlNode)
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.getNode(String, int)'
     */
    public void testGetNodeStringInt()
    {
        XMLNode xmlNode1 = new XMLNode();
        xmlNode1.addNode("a", "a1");
        xmlNode1.addNode("a", "a2");
        xmlNode1.addNode("b", "b1");
        xmlNode1.addNode("a", "a3");
        xmlNode1.addNode("a", "a4");

        XMLNode xmlNode = xmlNode1.getNode("a", 0);

        if (!xmlNode.getTextValue().equals("a1"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode("a", 1);

        if (!xmlNode.getTextValue().equals("a2"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode("a", 2);

        if (!xmlNode.getTextValue().equals("a3"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode("a", 3);

        if (!xmlNode.getTextValue().equals("a4"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode("ppp", 0);

        if (null != xmlNode)
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode(null, 0);

        if (null != xmlNode)
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode("ppp", -1);

        if (null != xmlNode)
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.getNode(int)'
     */
    public void testGetNodeInt()
    {
        XMLNode xmlNode1 = new XMLNode();
        xmlNode1.addNode("a", "a1");
        xmlNode1.addNode("a", "a2");
        xmlNode1.addNode("b", "b1");
        xmlNode1.addNode("a", "a3");
        xmlNode1.addNode("a", "a4");

        XMLNode xmlNode = xmlNode1.getNode(0);

        if (!xmlNode.getTextValue().equals("a1"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode(1);

        if (!xmlNode.getTextValue().equals("a2"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode(2);

        if (!xmlNode.getTextValue().equals("b1"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode(3);

        if (!xmlNode.getTextValue().equals("a3"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode(4);

        if (!xmlNode.getTextValue().equals("a4"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode(5);

        if (null != xmlNode)
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode = xmlNode1.getNode(-1);

        if (null != xmlNode)
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.size(String)'
     */
    public void testSizeString()
    {
        XMLNode xmlNode1 = new XMLNode("a", "o");
        xmlNode1.addNode("w", "w");

        if (xmlNode1.size(TEST) != 0)
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(TEST, TEST);

        if (xmlNode1.size(TEST) != 1)
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(TEST, TEST);
        xmlNode1.addNode(TEST, TEST);
        xmlNode1.addNode(TEST, TEST);

        if (xmlNode1.size(TEST) != 4)
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.size()'
     */
    public void testSize()
    {
        XMLNode xmlNode1 = new XMLNode("a", "o");

        if (xmlNode1.size() != 0)
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(TEST, TEST);

        if (xmlNode1.size() != 1)
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.addNode(TEST, TEST);
        xmlNode1.addNode(TEST, TEST);
        xmlNode1.addNode(TEST, TEST);

        if (xmlNode1.size() != 4)
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.setAttribute(String, String)'
     */
    public void testSetAttribute() throws XMLTestException
    {
        XMLNode xmlNode1 = new XMLNode("u", "i");

        try
        {
            xmlNode1.setAttribute(null, null); //Lancia un'eccezzione
            throw new XMLTestException(ERR_COMP);
        }
        catch (XMLTestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            assertNotNull("OK", e);
        }

        try
        {
            xmlNode1.setAttribute(null, "dasdasd");
            throw new XMLTestException(ERR_COMP);
        }
        catch (XMLTestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            assertNotNull("OK", e);
        }

        xmlNode1.setAttribute(ATTR, null);
        xmlNode1.setAttribute(ATTR, "dasdasd");

        if (!xmlNode1.hasAttribute(ATTR))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode1.getAttribute(ATTR).equals("dasdasd"))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.removeAttribute(String)'
     */
    public void testRemoveAttribute()
    {
        XMLNode xmlNode1 = new XMLNode("p", "p");

        if (xmlNode1.removeAttribute(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.setAttribute("nome", TEST);

        if (!xmlNode1.removeAttribute("nome"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (xmlNode1.removeAttribute(null))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.removeAttributes()'
     */
    public void testRemoveAttributes()
    {
        XMLNode xmlNode1 = new XMLNode("p", "o");

        xmlNode1.setAttribute("a", "a1");
        xmlNode1.setAttribute("b", "b1");
        xmlNode1.setAttribute("c", "c1");

        if (xmlNode1.getAttributes().size() != 3)
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!((null == xmlNode1.getAttributes()) ||
                (xmlNode1.getAttributes().size() != 0)))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.hasAttribute(String)'
     */
    public void testHasAttribute()
    {
        XMLNode xmlNode1 = new XMLNode("o", "p");

        if (xmlNode1.hasAttribute(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.setAttribute(TEST, TEST);

        if (!xmlNode1.hasAttribute(TEST))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (xmlNode1.hasAttribute(null))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (xmlNode1.hasAttribute("oo"))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.hasAttributes()'
     */
    public void testHasAttributes()
    {
        XMLNode xmlNode1 = new XMLNode("o", "p");

        if (xmlNode1.hasAttributes())
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.setAttribute(TEST, TEST);

        if (!xmlNode1.hasAttributes())
        {
            throw new XMLTestException(ERR_COMP);
        }

        xmlNode1.removeAttributes();

        if (xmlNode1.hasAttributes())
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.getAttributes()'
     */
    public void testGetAttributes()
    {
        XMLNode xmlNode1 = new XMLNode("p", "o");
        xmlNode1.setAttribute("a", "a1");
        xmlNode1.setAttribute("b", "b1");
        xmlNode1.setAttribute("c", "c1");

        Map map = xmlNode1.getAttributes();

        if (map.size() != 3)
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!((String) map.get("a")).equals("a1"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!((String) map.get("b")).equals("b1"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!((String) map.get("c")).equals("c1"))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.getAttribute(String)'
     */
    public void testGetAttributeString()
    {
        XMLNode xmlNode1 = new XMLNode("p", "i");
        xmlNode1.setAttribute("a", "a1");
        xmlNode1.setAttribute("b", "b1");
        xmlNode1.setAttribute("c", "c1");

        if (!xmlNode1.getAttribute("a").equals("a1"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode1.getAttribute("b").equals("b1"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (!xmlNode1.getAttribute("c").equals("c1"))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (null != xmlNode1.getAttribute(null))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (null != xmlNode1.getAttribute("opopopO"))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.getAttribute(String, String)'
     */
    public void testGetAttributeStringString()
    {
        testGetAttributeString();
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.getAttribute(String, boolean)'
     */
    public void testGetAttributeStringBoolean()
    {
        XMLNode xmlNode1 = new XMLNode("p", "i");
        xmlNode1.setAttribute("a", "true");
        xmlNode1.setAttribute("c", "false");

        if (!xmlNode1.getAttribute("a", false))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (xmlNode1.getAttribute("c", true))
        {
            throw new XMLTestException(ERR_COMP);
        }

        if (xmlNode1.getAttribute("b", false))
        {
            throw new XMLTestException(ERR_COMP);
        }
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.toString()'
     */
    public void testToString() throws XMLTestException
    {
        String initial = "<a><b n=\"oo\"></b><c m=\"oiuoiu\"></c></a>";
        XMLNode xmlNode1 = null;

        try
        {
            xmlNode1 = XMLNode.parse(initial);
        }
        catch (Exception e)
        {
            throw new XMLTestException("Error parsing XML");
        }

        xmlNode1.toString();
    }

    /*
     * Test method for 'com.carel.supervisor.base.xml.XMLNode.getStringBuffer()'
     */
    public void testGetStringBuffer() throws XMLTestException
    {
        String initial = "<a><b n=\"oo\"></b><c m=\"oiuoiu\"></c></a>";
        XMLNode xmlNode1 = null;

        try
        {
            xmlNode1 = XMLNode.parse(initial);
        }
        catch (Exception e)
        {
            throw new XMLTestException();
        }

        StringBuffer buffer = xmlNode1.getStringBuffer();

        if (null == buffer)
        {
            throw new XMLTestException(ERR_COMP);
        }

        buffer.toString();

    }
}
