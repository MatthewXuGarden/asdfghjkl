package com.carel.supervisor.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Dictionary
{
    private Map map = new HashMap();
    
    public void add(Object key1, Object key2, Object value)
    {
        Map mapInner = null;
        mapInner = (Map) map.get(key1);
        if (null == mapInner)
        {
            mapInner = new HashMap();
            map.put(key1, mapInner);
        }

        mapInner.put(key2, value);
    }

    public Object get(Object key1, Object key2)
    {
        Map mapInner = (Map) map.get(key1);

        if (null == mapInner)
        {
            return null;
        }

        return mapInner.get(key2);
    }

    public Map get(Object key1)
    {
        return (Map) map.get(key1);
    }
    
    public boolean containsKey(Object key1, Object key2)
    {
        Map mapInner = (Map) map.get(key1);

        if (null == mapInner)
        {
            return false;
        }

        return mapInner.containsKey(key2);
    }
    
    public void remove(Object key1, Object key2)
    {
        Map mapInner = (Map) map.get(key1);

        if (null != mapInner)
        {
        	mapInner.remove(key2);
        }
        else
        {
        	map.remove(key1);
        }
    }

    public void clear()
    {
        map.clear();
    }
    
    public int size()
    {
    	return map.size();
    }
    
    public Iterator iterator()
    {
    	return map.keySet().iterator();
    }
    
    public List retrieveAll()
    {
    	List values = new ArrayList();
        Iterator key = map.keySet().iterator();
        Iterator innerkey = null;
        Integer iddevice = null;
        Integer idvarmdl = null;
        Map mapInner = null;
        while (key.hasNext())
        {
            iddevice = (Integer) key.next();
            mapInner = (Map)map.get(iddevice);
            innerkey = mapInner.keySet().iterator();
            while (innerkey.hasNext())
            {
            	idvarmdl = (Integer) innerkey.next();
            	values.add(mapInner.get(idvarmdl));
            }
        }
        return values;
    }
    
}
