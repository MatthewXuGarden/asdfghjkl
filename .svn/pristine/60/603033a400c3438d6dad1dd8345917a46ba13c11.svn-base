package com.carel.supervisor.base.log;

import com.carel.supervisor.base.dump.*;


public class LoggerDumpable extends Logger
{
    private String formatMessage(String message, IDumpable dumpable)
    {
        return message + "  [" +
        dumpable.getDumpWriter().getStringBuffer().toString() + "]";
    }

    public final void debug(String message, IDumpable dumpable)
    {
        logger.debug("[" + name() + "] " + formatMessage(message, dumpable));
    }

    public final void info(String message, IDumpable dumpable)
    {
        logger.info("[" + name() + "] " + formatMessage(message, dumpable));
    }
}
