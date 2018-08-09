package com.carel.supervisor.dataaccess.queue;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.factory.*;
import com.carel.supervisor.base.xml.*;
import java.util.*;


public class QueueMgr implements IInitializable
{
    private final static String CLASS = "class";
    private final static String NAME = "name";
    private static boolean initialized = false;
    private static final QueueMgr me = new QueueMgr();
    private Map queue = new HashMap();

    private QueueMgr()
    {
    }

    public static QueueMgr getInstance()
    {
        return me;
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            String name = null;
            String className = null;
            XMLNode xmlTmp = null;

            for (int i = 0; i < xmlStatic.size(); i++)
            {
                xmlTmp = xmlStatic.getNode(i);
                name = xmlTmp.getAttribute(NAME);

                if (null == name)
                {
                    FatalHandler.manage(this,
                        CoreMessages.format("DTCE0015", NAME, String.valueOf(i)));
                }

                className = xmlTmp.getAttribute(CLASS);

                if (null == className)
                {
                    FatalHandler.manage(this,
                        CoreMessages.format("DTCE0015", CLASS, String.valueOf(i)));
                }

                try
                {
                    queue.put(name, FactoryObject.newInstance(className));
                }
                catch (Exception e)
                {
                    FatalHandler.manage(this,
                        CoreMessages.format("DTCE0016", className), e);
                }
            }

            initialized = true;
        }
    }

    public Object getQueue(String name)
    {
        return queue.get(name);
    }
}
