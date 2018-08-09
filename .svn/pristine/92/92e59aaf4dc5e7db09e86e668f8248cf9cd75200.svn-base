package com.carel.supervisor.base.profiling;

import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.xml.XMLNode;


public class ProfilingMgr extends InitializableBase implements IInitializable
{
    //private static final String TYPE = "type";
    private static final String CLASS = "class";
    private static ProfilingMgr meProfilingMgr = new ProfilingMgr();
    private boolean initialized = false;
    private IProfiler profiler = null;

    private ProfilingMgr()
    {
    }

    public static ProfilingMgr getInstance()
    {
        return meProfilingMgr;
    }

    public synchronized void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        if (!initialized)
        {
            XMLNode xmlTmp = xmlStatic.getNode(0);
            String className = retrieveAttribute(xmlTmp, CLASS, "BSSE0002");

            try
            {
                profiler = (IProfiler) FactoryObject.newInstance(className);
            }
            catch (Exception e)
            {
                FatalHandler.manage(this, CoreMessages.format("BSSE0003", className), e);
            }

            profiler.init(xmlTmp);
            initialized = true;
        }
    }

    public UserProfile getUserProfile(UserCredential userCredential)
        throws Exception
    {
        return profiler.getUserProfile(userCredential);
    }

    public IProfiler getProfiler()
    {
        return profiler;
    } //getProfiler
}
