package com.carel.supervisor.base.dump.xmldata;

import com.carel.supervisor.base.dump.*;
import com.carel.supervisor.base.dump.xml.*;
import com.carel.supervisor.base.xml.*;


public class XMLDataDumpWriter extends XMLDumpWriter
{
    public XMLDataDumpWriter(String name, Class className,
        IDumperFactory dumperFactory)
    {
        super(name, className, dumperFactory);
    }

    public XMLDataDumpWriter(String name, String className,
        IDumperFactory dumperFactory)
    {
        super(name, className, dumperFactory);
    }

    public Object formatObject(String name, String value, String type)
    {
        return new XMLNode(name, value);
    }

    public Object formatObjectSon(String name, Object value)
    {
        ((XMLNode) value).setNodeName(name);

        return value;
    }
}
