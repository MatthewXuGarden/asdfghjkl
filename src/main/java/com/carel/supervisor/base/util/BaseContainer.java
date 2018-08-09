package com.carel.supervisor.base.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;


public class BaseContainer implements ICloneable
{
    protected Map map = null;
    private static final Logger logger = LoggerMgr.getLogger(BaseContainer.class);

    public BaseContainer()
    {
        map = new HashMap();
    }

    public BaseContainer(int initialCapacity)
    {
        map = new HashMap(initialCapacity);
    }

    public void put(Object key, ICloneable value)
    {
        map.put(key, value);
    }

    public ICloneable get(Object key)
    {
        return (ICloneable) map.get(key);
    }

    public void clear()
    {
        map.clear();
    }

    public Object[] keys()
    {
        Iterator iterator = map.keySet().iterator();
        Object[] keys = new Object[map.size()];
        int i = -1;

        while (iterator.hasNext())
        {
            i++;
            keys[i] = iterator.next();
        }

        return keys;
    }

    //Deep Cloaning
    public Object clone()
    {
        try
        {
            SimpleContainer container = new SimpleContainer(map.size());
            Iterator iterator = map.keySet().iterator();
            Object key = null;

            while (iterator.hasNext())
            {
                key = iterator.next();

                if (key == null)
                {
                    FatalHandler.manage(this,
                        CoreMessages.format("BSSE0007",
                            "Class BaseContainer Method clone key=null"));
                }

                container.put(key,
                    (ICloneable) ((ICloneable) map.get(key)).clone());
            }

            return container;
        }
        catch (Exception e)
        {
            if (BaseConfig.isDebug())
            {
                logger.error(e);
            }

            return null;
        }
    }

    public int size()
    {
        return map.size();
    }

    public boolean hasKey(Object key)
    {
        return map.containsKey(key);
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public void add(BaseContainer data)
    {
        Map mapTmp = data.map;
        Iterator keys = mapTmp.keySet().iterator();
        Object key = null;

        while (keys.hasNext())
        {
            key = keys.next();
            map.put(key, mapTmp.get(key));
        }
    }
}
