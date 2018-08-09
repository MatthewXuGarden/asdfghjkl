package com.carel.supervisor.director.ide;

import com.carel.supervisor.base.xml.XMLNode;
import java.util.*;


public class XmlStream
{
    private Map map = new HashMap();

    public XmlStream()
    {
    }

    public void addFile(String name, String path)
    {
    	map.put(name, path);
    }
    
    public String getFile(String name)
    {
    	return (String) map.get(name);
    }
    
    public void addXML(String name, XMLNode xmlNode)
    {
        map.put(name, xmlNode);
    }

    public XMLNode getXML(String name)
    {
        return (XMLNode) map.get(name);
    }
}
