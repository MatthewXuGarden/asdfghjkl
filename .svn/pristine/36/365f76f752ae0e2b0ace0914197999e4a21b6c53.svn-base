package com.carel.supervisor.dataaccess.dataconfig;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.factory.*;
import com.carel.supervisor.base.xml.*;
import java.util.*;


public class DataConfigMgr implements IInitializable
{
    private final static String NAME = "name";
    private final static String CLASS = "class";
    private static final DataConfigMgr me = new DataConfigMgr();
    private Map config = new HashMap();
    private boolean initialized = false;
    private XMLNode configXML = null;

    private DataConfigMgr()
    {
    }

    public static DataConfigMgr getInstance()
    {
        return me;
    }

    public boolean initialized()
    {
        return initialized;
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            this.configXML = xmlStatic;
            load();
            initialized = true;
        }
    }

    public synchronized void clear() 
    {
    	initialized = false;

        Iterator iterator = config.keySet().iterator();
        String name = null;

        while (iterator.hasNext())
        {
            name = (String) iterator.next();
            ((IBindable) config.get(name)).clear();
        }

        config.clear();
    }
    
    public synchronized void reload() throws InvalidConfigurationException
    {
        clear();
        load();
        initialized = true;
    }

    public synchronized Object getConfig(String name)
    {
        return config.get(name);
    }

    private void load() throws InvalidConfigurationException
    {
        try
        {
            XMLNode xmlTmp = null;
            String name = null;
            String className = null;

            for (int i = 0; i < configXML.size(); i++)
            {
                xmlTmp = configXML.getNode(i);
                name = xmlTmp.getAttribute(NAME);
                className = xmlTmp.getAttribute(CLASS);
                config.put(name,
                    FactoryObject.newInstance(className,
                        new Class[] { String.class, String.class },
                        new Object[] { null, BaseConfig.getPlantId() }));
            }

            //With the same order we bind the data
            for (int i = 0; i < configXML.size(); i++)
            {
                xmlTmp = configXML.getNode(i);
                name = xmlTmp.getAttribute(NAME);
                ((IBindable) config.get(name)).bind(config);
            }

            for (int i = 0; i < configXML.size(); i++)
            {
                xmlTmp = configXML.getNode(i);
                name = xmlTmp.getAttribute(NAME);
                ((IBindable) config.get(name)).afterBind();
            }
        }
        catch (Exception e)
        {
            throw new InvalidConfigurationException("", e);
        }
    }
}
