package com.carel.supervisor.base.util;

import java.util.*;


public class SimpleContainer extends BaseContainer implements ICloneable
{
    public SimpleContainer()
    {
        super();
    }

    public SimpleContainer(int initialCapacity)
    {
        super(initialCapacity);
    }

    public void put(String key, ICloneable value)
    {
        map.put(key, value);
    }

    public ICloneable get(String key)
    {
        return (ICloneable) map.get(key);
    }

    public String[] keysString()
    {
        Iterator iterator = map.keySet().iterator();
        String[] keys = new String[map.size()];
        int i = -1;

        while (iterator.hasNext())
        {
            i++;
            keys[i] = (String) iterator.next();
        }

        return keys;
    }

    public boolean hasKey(String key)
    {
        return map.containsKey(key);
    }
}
