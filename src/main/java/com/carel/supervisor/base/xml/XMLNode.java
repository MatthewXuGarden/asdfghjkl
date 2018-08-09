package com.carel.supervisor.base.xml;

import org.w3c.dom.*;
import java.io.*;
import java.util.*;


public class XMLNode
{
    private String nodeName = "";
    private String textValue = "";
    private transient Map attributeList = new HashMap();
    private transient List nodeList = new ArrayList();

    public XMLNode(String nodeName, String textValue)
    {
        super();
        checkNodeName(nodeName);
        this.nodeName = nodeName;
        this.textValue = textValue;
    }

    public XMLNode()
    {
        super();
    }

    public XMLNode(String nodeName, String textValue, Map attributeList, ArrayList nodeList)
    {
        super();
        checkNodeName(nodeName);
        this.nodeName = nodeName;
        this.textValue = textValue;
        this.attributeList = attributeList;
        this.nodeList = nodeList;
    }

    public void set(String nodeName, String textValue)
    {
        checkNodeName(nodeName);
        this.nodeName = nodeName;
        this.textValue = textValue;
    }

    protected static final XMLNode create(String nodeName, String textValue)
    {
        checkNodeName(nodeName);

        return new XMLNode(nodeName, textValue);
    }

    public static XMLNode parse(String xml) throws Exception
    {
        NodeList nodeList = XMLUtil.extractNodes(xml);

        return parse(nodeList);
    }

    public static XMLNode parse(InputStream inputStream)
        throws Exception
    {
        NodeList nodeList = XMLUtil.extractNodes(inputStream);

        return parse(nodeList);
    }

    public static XMLNode parse(Reader reader) throws Exception
    {
        NodeList nodeList = XMLUtil.extractNodes(reader);

        return parse(nodeList);
    }

    private static XMLNode parse(NodeList nodeList)
    {
        boolean found = false;
        int pos = -1;
        XMLNode xmlNode = null;

        while ((!found) && (pos < nodeList.getLength()))
        {
            pos++;

            switch (nodeList.item(pos).getNodeType())
            {
            case Node.COMMENT_NODE:
                found = true;

                break;

            case Node.ELEMENT_NODE:
                xmlNode = newSon(nodeList.item(pos));
                found = true;

                break;

            default:
                found = true;

                break;
            }
        }

        return xmlNode;
    }

    private static XMLNode newSon(Node node)
    {
        XMLNode xmlTmp = new XMLNode();

        switch (node.getNodeType())
        {
        case Node.COMMENT_NODE:
            xmlTmp = null;

            break;

        case Node.ELEMENT_NODE:
            xmlTmp.setNodeName(node.getNodeName());

            NamedNodeMap attributes = node.getAttributes();
            HashMap attributeList = null;

            if (0 < attributes.getLength())
            {
                attributeList = new HashMap();

                for (int i = 0; i < attributes.getLength(); i++)
                {
                    Node attr = attributes.item(i);
                    attributeList.put(attr.getNodeName(), attr.getNodeValue());
                }

                xmlTmp.setAttributes(attributeList);
            }

            NodeList children = node.getChildNodes();

            if (null != children)
            {
                int numChilds = children.getLength();

                for (int i = 0; i < numChilds; i++)
                {
                    if ((Node.TEXT_NODE == children.item(i).getNodeType()) ||
                            (Node.CDATA_SECTION_NODE == children.item(i).getNodeType()))
                    {
                        String textValueTmp = children.item(i).getNodeValue();

                        if (!((null == textValueTmp) || (0 == textValueTmp.trim().length())))
                        {
                            xmlTmp.textValue = textValueTmp;
                        }
                    }
                    else
                    {
                        XMLNode xmlDinamicTmp = XMLNode.newSon(children.item(i));

                        if (null != xmlDinamicTmp) //Nel caso fosse un commento, non verrebbe aggiunto un puntatore null
                        {
                            xmlTmp.addNode(xmlDinamicTmp);
                        }
                    }
                }
            }

            break;

        default:
            xmlTmp = null;
        }

        return xmlTmp;
    }

    public void setNodeName(String newNodeName)
    {
        checkNodeName(newNodeName);
        nodeName = newNodeName;
    }

    public String getNodeName()
    {
        return nodeName;
    }

    public void setTextValue(String textValue)
    {
        this.textValue = textValue;
    }

    public String getTextValue()
    {
        return textValue;
    }

    public void addNodes(XMLNode[] xmlNodes)
    {
        if (null != xmlNodes)
        {
            for (int i = 0; i < xmlNodes.length; i++)
            {
                nodeList.add(xmlNodes[i]);
            }
        }
    }

    public void addNode(String nodeName, String textValue)
    {
        checkNodeName(nodeName);

        XMLNode xmlNode = new XMLNode(nodeName, textValue);
        addNode(xmlNode);
    }

    public void addNode(XMLNode xmlNode)
    {
        if (null == nodeList)
        {
            nodeList = new ArrayList();
        }

        nodeList.add(xmlNode);
    }

    public void setNode(String nodeName, int position, XMLNode xmlNode)
    {
        if (null == nodeList)
        {
            throw new XMLNodeNotFound(nodeName + " pos: " + String.valueOf(position));
        }

        int realPos = findNodePosition(nodeName, position);
        nodeList.set(realPos, xmlNode);
    }

    public void setNode(String nodeName, XMLNode xmlNode)
    {
        setNode(nodeName, 0, xmlNode);
    }

    public boolean removeNode(String nodeName)
    {
        return removeNode(nodeName, 0);
    }

    public boolean removeNode(String nodeName, int position)
    {
        boolean code = false;

        if (null != nodeList)
        {
            try
            {
                nodeList.remove(findNodePosition(nodeName, position));

                code = true;
            }
            catch (Exception e)
            {
                code = false;
            }
        }

        return code;
    }

    public boolean removeNodes(String nodeName)
    {
        boolean removed = false;

        if (null != nodeList)
        {
            int pos = 0;

            while (pos < nodeList.size())
            {
                if (((XMLNode) nodeList.get(pos)).getNodeName().equals(nodeName))
                {
                    nodeList.remove(pos);
                    removed = true;
                }
                else
                {
                    pos++;
                }
            }
        }

        return removed;
    }

    public void removeNodes()
    {
        if (null != nodeList)
        {
            nodeList.clear();
        }
    }

    public boolean hasNode(String nodeName)
    {
        return hasNode(nodeName, 0);
    }

    public boolean hasNode(String nodeName, int position)
    {
        boolean code = false;

        if (null != nodeList)
        {
            try
            {
                findNodePosition(nodeName, position);
                code = true;
            }
            catch (XMLNodeNotFound e)
            {
                code = false;
            }
        }

        return code;
    }

    public boolean hasNodes()
    {
        boolean code = false;

        if (null != nodeList)
        {
            code = (0 < nodeList.size());
        }

        return code;
    }

    public void clear()
    {
        if (null != attributeList)
        {
            attributeList.clear();
            attributeList = null;
        }

        if ((null != nodeList) && (nodeList.size() > 0))
        {
            XMLNode xmlDinamic = null;

            for (int i = 0; i < nodeList.size(); i++)
            {
                xmlDinamic = (XMLNode) nodeList.get(i);
                xmlDinamic.clear();
            }
        }

        nodeList = null;
    }

    public XMLNode[] getNodes(String nodeName)
    {
        int iSize = size(nodeName);
        XMLNode[] xmlNode = null;

        if (0 != iSize)
        {
            xmlNode = new XMLNode[iSize];

            XMLNode xmlNodeTmp = null;
            int pos = -1;

            for (int i = 0; i < nodeList.size(); i++)
            {
                xmlNodeTmp = (XMLNode) nodeList.get(i);

                if (xmlNodeTmp.getNodeName().equals(nodeName))
                {
                    pos++;
                    xmlNode[pos] = xmlNodeTmp;
                }
            }
        }

        return xmlNode;
    }

    public XMLNode[] getNodes()
    {
        XMLNode[] xmlNodes = null;

        if (0 != size())
        {
            xmlNodes = (XMLNode[]) (nodeList.toArray(new XMLNode[nodeList.size()]));
        }

        return xmlNodes;
    }

    public XMLNode getNode(String nodeName)
    {
        return getNode(nodeName, 0);
    }

    public XMLNode getNode(String nodeName, int position)
    {
        XMLNode xml = null;

        try
        {
            xml = (XMLNode) nodeList.get(findNodePosition(nodeName, position));
        }
        catch (Exception e)
        {
            xml = null;
        }

        return xml;
    }

    public XMLNode getNode(int position)
    {
        XMLNode xml = null;

        try
        {
            xml = (XMLNode) nodeList.get(position);
        }
        catch (Exception e)
        {
            xml = null;
        }

        return xml;
    }

    public int size(String sNodeName)
    {
        int size = 0;

        if (null == nodeList)
        {
            size = 0;
        }
        else
        {
            for (int i = 0; i < nodeList.size(); i++)
            {
                if (((XMLNode) nodeList.get(i)).getNodeName().equals(sNodeName))
                {
                    size++;
                }
            }
        }

        return size;
    }

    public int size()
    {
        int size = 0;

        if (null == nodeList)
        {
            size = 0;
        }
        else
        {
            size = nodeList.size();
        }

        return size;
    }

    public void setAttribute(String attributeName, String sAttributeValue)
    {
        checkAttributeName(attributeName);

        if (null == attributeList)
        {
            attributeList = new HashMap();
        }

        attributeList.put(attributeName, sAttributeValue);
    }

    public boolean removeAttribute(String sAttributeName)
    {
        boolean code = false;

        if (null == attributeList)
        {
            code = false;
        }
        else
        {
            code = (null != attributeList.remove(sAttributeName));
        }

        return code;
    }

    public void removeAttributes()
    {
        if (null != attributeList)
        {
            attributeList.clear();
        }
    }

    public boolean hasAttribute(String sAttributeName)
    {
        boolean code = false;

        if (null != attributeList)
        {
            code = (attributeList.containsKey(sAttributeName));
        }

        return code;
    }

    public boolean hasAttributes()
    {
        boolean code = false;

        if (null != attributeList)
        {
            code = (0 < attributeList.size());
        }

        return code;
    }

    public Map getAttributes()
    {
        return attributeList;
    }

    public String getAttribute(String attributeName)
    {
        String value = null;

        if (null != attributeList)
        {
            value = (String) attributeList.get(attributeName);
        }

        return value;
    }

    public String getAttribute(String attributeName, String defaultValue)
    {
        String value = "";

        if (null == attributeList)
        {
            value = defaultValue;
        }
        else
        {
            value = (String) attributeList.get(attributeName);

            if (null == value)
            {
                value = defaultValue;
            }
        }

        return value;
    }

    public boolean getAttribute(String attributeName, boolean defaultValue)
    {
        boolean value = false;

        if (null == attributeList)
        {
            value = defaultValue;
        }
        else
        {
            String sValue = (String) attributeList.get(attributeName);

            if (null == sValue)
            {
                value = defaultValue;
            }
            else
            {
                try
                {
                    value = sValue.equalsIgnoreCase("true");
                }
                catch (Exception e)
                {
                    value = defaultValue;
                }
            }
        }

        return value;
    }

    public long getAttribute(String attributeName, long defaultValue)
    {
        long value = 0;

        if (null == attributeList)
        {
            value = defaultValue;
        }
        else
        {
            String valueString = (String) attributeList.get(attributeName);

            if (null == valueString)
            {
                value = defaultValue;
            }
            else
            {
                try
                {
                    value = Long.parseLong(valueString);
                }
                catch (Exception e)
                {
                    value = defaultValue;
                }
            }
        }

        return value;
    }

    public int getAttribute(String attributeName, int defaultValue)
    {
        int value = 0;

        if (null == attributeList)
        {
            value = defaultValue;
        }
        else
        {
            String valueString = (String) attributeList.get(attributeName);

            if (null == valueString)
            {
                value = defaultValue;
            }
            else
            {
                try
                {
                    value = Integer.parseInt(valueString);
                }
                catch (Exception e)
                {
                    value = defaultValue;
                }
            }
        }

        return value;
    }

    public String toString()
    {
        return getStringBuffer().toString();
    }

    public StringBuffer getStringBuffer()
    {
        StringBuffer sbBuffer = new StringBuffer();
        createStringBuffer(sbBuffer);

        return sbBuffer;
    }

    private void setAttributes(Map oAttributeList)
    {
        attributeList = oAttributeList;
    }

    private int findNodePosition(String nodeName, int relativePosition)
    {
        if (null == nodeList)
        {
            throw new XMLNodeNotFound(nodeName + " pos: " + String.valueOf(relativePosition));
        }

        int totalPosition = 0;
        int currRelPos = -1;

        while (totalPosition < nodeList.size())
        {
            if (nodeName.equals(((XMLNode) nodeList.get(totalPosition)).getNodeName()))
            {
                currRelPos++;

                if (currRelPos == relativePosition)
                {
                    return totalPosition;
                }
            }

            totalPosition++;
        }

        throw new XMLNodeNotFound(nodeName + " pos: " + String.valueOf(relativePosition));
    }

    private void createStringBuffer(StringBuffer buffer)
    {
        buffer.append("<");
        buffer.append(nodeName);

        if (hasAttributes())
        {
            Map attributeList = getAttributes();
            Iterator iterator = attributeList.keySet().iterator();
            String name = "";

            while (iterator.hasNext())
            {
                name = (String) iterator.next();
                buffer.append(" ");
                buffer.append(name);
                buffer.append("=\"");
                buffer.append((String) attributeList.get(name));
                buffer.append("\"");
            }
        }

        buffer.append(">");
        buffer.append(getTextValue());

        if (0 < size())
        {
            for (int i = 0; i < size(); i++)
            {
                getNode(i).createStringBuffer(buffer);
            }
        }

        buffer.append("</");
        buffer.append(nodeName);
        buffer.append(">\n");
    }

    private static void checkNodeName(String nodeName)
    {
        if (null == nodeName)
        {
            throw new InvalidNodeNameException();
        }

        if ("".equals(nodeName))
        {
            throw new InvalidNodeNameException();
        }
    }

    private static void checkAttributeName(String attributeName)
    {
        if (null == attributeName)
        {
            throw new InvalidAttributeNameException();
        }

        if ("".equals(attributeName))
        {
            throw new InvalidAttributeNameException();
        }
    }
}
