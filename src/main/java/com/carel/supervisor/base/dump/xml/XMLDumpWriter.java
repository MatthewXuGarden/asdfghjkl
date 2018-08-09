package com.carel.supervisor.base.dump.xml;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.dump.*;
import com.carel.supervisor.base.xml.*;


public class XMLDumpWriter extends DumpWriter
{
    private static final String OBJECT_TAG = "object";
    private static final String OBJECT_TYPE = "type";
    private static final String OBJECT_NAME = "name";
    private XMLNode xmlNode = null;

    public XMLDumpWriter(String name, Class className,
        IDumperFactory dumperFactory)
    {
        super(dumperFactory);

        if (null != className)
        {
            xmlNode = (XMLNode) formatObject(name, "", className.getName());
        }
        else
        {
            xmlNode = (XMLNode) formatObject(name, "", null);
        }
    }

    public XMLDumpWriter(String name, String className,
        IDumperFactory dumperFactory)
    {
        super(dumperFactory);

        if (null != className)
        {
            xmlNode = (XMLNode) formatObject(name, "", className);
        }
        else
        {
            xmlNode = (XMLNode) formatObject(name, "", null);
        }
    }

    public StringBuffer getStringBuffer()
    {
        return xmlNode.getStringBuffer();
    }

    public void print(Object objectToAdd)
    {
        if (objectToAdd.getClass() != xmlNode.getClass())
        {
            throw new DumpWriterException(CoreMessages.format("BSSE0005",
                    xmlNode.getClass().getName(),
                    objectToAdd.getClass().toString()));
        }

        xmlNode.addNode((XMLNode) objectToAdd);
    }

    public Object getStream()
    {
        return xmlNode;
    }

    public Object formatObject(String name, String value, String type)
    {
        XMLNode xmlNodeTmp = new XMLNode(OBJECT_TAG, value);

        if (null != type)
        {
            xmlNodeTmp.setAttribute(OBJECT_TYPE, type);
        }

        if (null != name)
        {
            xmlNodeTmp.setAttribute(OBJECT_NAME, name);
        }

        return xmlNodeTmp;
    }

    public Object formatObjectSon(String name, Object value)
    {
        ((XMLNode) value).setAttribute(OBJECT_NAME, name);

        return value;
    }
}
