package com.carel.supervisor.dataaccess.language;

import java.text.*;
import java.util.*;


public class LangSection
{
    private final static String NULL = "null";
    private final static String EMPTYSTRING = "";
    private final static String FAILURE = "#ERROR: FAILED TO LOCALIZE {0}.{1}";
    private String section = null;
    private Properties messages = null;

    public LangSection(String sectionPar)
    {
        messages = new Properties();
        section = sectionPar;
    }

    public String get(String id)
    {
    	String tmp = messages.getProperty(id);
    	if (null == tmp)
    	{
    		return "";
    	}
    	else
    	{
    		return tmp;
    	}
    }

    public String format(String key)
    {
        return format(key, null);
    }

    public String format(String key, Object[] args, boolean hideNulls)
    {
        String message = messages.getProperty(key);
        Object[] newVarArgs = null;

        if (null != args)
        {
            newVarArgs = new Object[args.length];

            for (int i = 0; i < args.length; i++)
            {
                if (null == args[i])
                {
                    newVarArgs[i] = hideNulls ? EMPTYSTRING : NULL;
                }
                else
                {
                    newVarArgs[i] = args[i];
                }
            }
        }

        if (null != message)
        {
            if (null != newVarArgs)
            {
                return MessageFormat.format(message, newVarArgs);
            }
            else
            {
                return message;
            }
        }

        String mess = ((key == null) ? NULL : key);

        return MessageFormat.format(FAILURE, new Object[] { section, mess });
    }

    public String format(String key, Object[] args)
    {
        return format(key, args, false);
    }

    protected void addResource(String key, String value)
    {
        messages.put(key, value);
    }
}
