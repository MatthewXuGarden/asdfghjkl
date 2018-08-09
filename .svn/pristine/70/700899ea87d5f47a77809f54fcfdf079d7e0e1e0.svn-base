/**
 *
 */
package com.carel.supervisor.base.config;

import java.text.*;
import java.util.*;


/**
 * @author Loris D'Acunto <br>
 * Carel S.p.A. <br>
 * <br>
 * 2-nov-2005 14.36.45 <br>
 */
public class CoreMessages
{
    /**
     *
     * CoreMessages
     */
    private static final String NULL = "null";
    private static final String FAILURE = "#ERROR: FAILED TO LOCALIZE {0}";
    private static Properties messages = null;

    private CoreMessages()
    {
    }

    //TO DO : se non ho il file di messaggio aggiornato? Gestire il caso in cui String message = messages.getProperty(key);
    // è null
    protected static void init(Properties properties)
    {
        messages = properties;
    }

    public static String format(String key)
    {
        return format(key, (Object[]) null);
    }

    public static String format(String key, String value)
    {
        return format(key, new Object[] { value });
    }

    public static String format(String key, String value1, String value2)
    {
        return format(key, new Object[] { value1, value2 });
    }

    public static String format(String key, Object[] args)
    {
        String value = null;

        if (key == null)
        {
        	 value = MessageFormat.format(FAILURE, new Object[] { NULL });
        }
        else
        {
            String message = messages.getProperty(key);

            if (null == message)
            {
                value = MessageFormat.format(FAILURE, new Object[] { key });
            }
            else
            {
                value = MessageFormat.format(message, args);
            }
        }

        return value;
    }
}
