package com.carel.supervisor.base.error;

import java.util.Properties;


public class BlockingErrorMgr
{
    private static BlockingErrorMgr me = new BlockingErrorMgr();
    private Properties messages = new Properties();

    private BlockingErrorMgr()
    {
    }

    public static BlockingErrorMgr getInstance()
    {
        return me;
    }

    public synchronized void add(String message)
    {
        messages.setProperty(message, message);
    }

    public synchronized boolean present()
    {
        return (!messages.isEmpty());
    }

    public synchronized String[] get()
    {
        String[] tmp = new String[messages.size()];
        tmp = (String[]) messages.keySet().toArray(tmp);
        messages.clear();

        return tmp;
    }
}
