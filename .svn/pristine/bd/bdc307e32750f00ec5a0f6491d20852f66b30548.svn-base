package com.carel.supervisor.base.log;

import com.carel.supervisor.base.dump.*;
import com.carel.supervisor.base.util.ExceptionHandler;


public class Logger
{
    protected org.apache.log4j.Logger logger = null;

    protected Logger(String selector)
    {
        logger = org.apache.log4j.Logger.getLogger(selector);
    }

    protected Logger()
    {
        logger = org.apache.log4j.Logger.getRootLogger();
    }

    public boolean isDebugEnabled()
    {
        return logger.isEnabledFor(LoggerMgr.DEBUG.apacheLevel);
    }

    public void setLevel(LoggerMgr.LEVEL level)
    {
        logger.setLevel(level.apacheLevel);
    }

    public LoggerMgr.LEVEL level()
    {
        return LoggerMgr.getLevel(logger.getEffectiveLevel());
    }

    public String name()
    {
        return logger.getName();
    }

    public void debug(String message)
    {
        logger.debug("[" + name() + "] " + message);
    }

    private String formatMessage(String message, IDumpable dumpable)
    {
        return message + "  [" +
        dumpable.getDumpWriter().getStringBuffer().toString() + "]";
    }

    public void debug(String message, IDumpable dumpable)
    {
        logger.debug("[" + name() + "] " + formatMessage(message, dumpable));
    }

    public void info(String message)
    {
        logger.info("[" + name() + "] " + message);
    }

    public void info(String message, IDumpable dumpable)
    {
        logger.info("[" + name() + "] " + formatMessage(message, dumpable));
    }

    public void warn(String message)
    {
        logger.warn("[" + name() + "] " + message);
    }

    public void warn(String message, Throwable throwable)
    {
        logger.warn("[" + name() + "] " + message, throwable);
    }

    public void error(String message)
    {
        logger.error("[" + name() + "] " + message);
    }

    public void error(String message, Throwable throwable)
    {
        logger.error("[" + name() + "] " + message + " " +
            ExceptionHandler.getInstance().addException(throwable));
    }

    public void error(Throwable throwable)
    {
        logger.error(ExceptionHandler.getInstance().addException(throwable));
    }

    public void fatal(String message)
    {
        logger.fatal("[" + name() + "] " + message);
    }

    public void fatal(String message, Throwable throwable)
    {
        logger.fatal("[" + name() + "] " + message + " " +
            ExceptionHandler.getInstance().addException(throwable));
    }

    public boolean isEnabledFor(LoggerMgr.LEVEL level)
    {
        return logger.isEnabledFor(level.apacheLevel);
    }
}
