package com.carel.supervisor.base.config;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.*;
import java.lang.reflect.*;


public class MethodInvoker
{
    private static final Logger logger = LoggerMgr.getLogger(MethodInvoker.class);

    public void invoke(String name, XMLNode xmlStatic) throws InvalidConfigurationException, Exception
	{
	    invoke(name, true, xmlStatic);
	}
    
    public void invoke(String name, boolean blockingError, XMLNode xmlStatic)
        throws InvalidConfigurationException, Exception
    {
        Class manager = null;

        try
        {
            manager = Class.forName(name);
        }
        catch (ClassNotFoundException e)
        {
            if (blockingError)
            {
                FatalHandler.manage(this, CoreMessages.format("BSSE0011", name));
            }
            else
            {
            	logger.warn(CoreMessages.format("BSSE0011", name));
            }
            return;
        }

        Method oMethod = null;

        try
        {
            oMethod = manager.getMethod("getInstance", null);
        }
        catch (Exception e)
        {
            if (blockingError)
            {
                FatalHandler.manage(this, CoreMessages.format("BSSE0009", name),e);
            }
            else
            {
            	logger.warn(CoreMessages.format("BSSE0009", name));
            }
            return;
        }

        IInitializable initializable = null;

        try
        {
            initializable = (IInitializable) oMethod.invoke(null, null);
        }
        catch (Exception e)
        {
            if (blockingError)
            {
                FatalHandler.manage(this, CoreMessages.format("BSSE0010", name),e);
            }
            else
            {
            	logger.warn(CoreMessages.format("BSSE0010", name));
            }
            return;
        }

        try
        {
            initializable.init(xmlStatic);
        }
        catch (Throwable e)
        {
        	if (blockingError)
            {
                FatalHandler.manage(this, CoreMessages.format("BSSE0004", name),e);
            }
        	else
            {
            	logger.warn(CoreMessages.format("BSSE0004", name));
            	logger.error(e);
            }
            return;
        }
    }
}
