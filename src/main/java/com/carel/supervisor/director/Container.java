package com.carel.supervisor.director;

import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.xml.XMLNode;


public class Container implements IInitializable
{
    private static Container me = new Container();
    private static boolean initialized = false;

    private Container()
    {
    }

    public static Container getInstance()
    {
        return me;
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            initialized = true;
        }
    }
}
