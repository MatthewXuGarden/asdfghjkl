package com.carel.supervisor.base.test;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.xml.*;


public class SingletonTest implements IInitializable
{
    private static SingletonTest me = new SingletonTest();
    private boolean initialized = false;

    private SingletonTest()
    {
    }

    public static SingletonTest getInstance()
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

    public boolean status()
    {
        return initialized;
    }
}
