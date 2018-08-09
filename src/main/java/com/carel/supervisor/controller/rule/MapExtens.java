package com.carel.supervisor.controller.rule;

import java.util.*;


public class MapExtens
{
    private Map map = new HashMap();
    private Map rules = new HashMap();

    public void add(Object key1, Object key2, Object value)
    {
        Map mapInner = null;
        List rulesList = null;
        mapInner = (Map) map.get(key1);
        rulesList = (List) rules.get(key1);

        if (null == mapInner)
        {
            mapInner = new HashMap();
            rulesList = new ArrayList();
            rules.put(key1, rulesList);
            map.put(key1, mapInner);
        }

        mapInner.put(key2, value);
        rulesList.add(value);
    }

    public List getRules(Object key)
    {
        return (List) rules.get(key);
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

    public boolean hasKey1(Object key)
    {
        Map mapInner = (Map) map.get(key);

        if (null == mapInner)
        {
            return false;
        }

        if (0 == mapInner.size())
        {
            return false;
        }

        return true;
    }

    public void clear()
    {
        map.clear();
        rules.clear();
    }
}
