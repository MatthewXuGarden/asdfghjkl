package com.carel.supervisor.dataaccess.event;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.xml.XMLNode;

public class EventErrorFilter
{
	private static final String FILTER = "filter";
	private static final String REBOOT = "reboot";
	
	private Map map = new HashMap();
	private Map mapReboot = new HashMap();
	
    protected EventErrorFilter(XMLNode xmlStatic)
    {
    	XMLNode xml = null;
    	String code = null;
    	for(int i = 0; i < xmlStatic.size(); i++)
    	{
    		xml = xmlStatic.getNode(i);
    		code = xml.getAttribute(FILTER,"");
    		if (xml.hasAttribute(REBOOT))
    		{
    			if ("yes".equalsIgnoreCase(xml.getAttribute(REBOOT,"no")))
				{
    				mapReboot.put(code,code);
				}
    			else
    			{
    				map.put(code,code);
    			}
    		}
    		else
    		{
    			map.put(code,code);
    		}
    	}
    }
    
    public boolean isToNotify(String code)
    {
    	return map.containsKey(code);
    }
    
    public boolean isToNotifyAndReboot(String code)
    {
    	return mapReboot.containsKey(code);
    }
}
