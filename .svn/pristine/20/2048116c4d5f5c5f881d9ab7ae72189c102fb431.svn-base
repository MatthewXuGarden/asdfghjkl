package com.carel.supervisor.base.config;

import com.carel.supervisor.base.log.*;


public class FatalHandler
{
    private static final Logger logger = LoggerMgr.getLogger(FatalHandler.class);

    private FatalHandler()
    {
    }

    public static void manage(Object parent, String message, Throwable error)
        throws InvalidConfigurationException
    {
        logger.fatal(message, error);
        throw new InvalidConfigurationException(message);
    }

    public static void manage(Object parent, String message)
        throws InvalidConfigurationException
    {
        logger.fatal(message);
        throw new InvalidConfigurationException(message);
    }
}
