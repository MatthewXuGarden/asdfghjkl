package com.carel.supervisor.base.profiling;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;


public class SectionProfile implements Serializable
{
    private final static String PVENTRY = "pventry";
    private final static String SEP = ",";
    private Properties section = new Properties();

    public String getValue(String nameValue)
    {
        return section.getProperty(nameValue);
    }

    public boolean isAllowed(String nameValue)
    {
        boolean state = true;
        String[] values = getValues(PVENTRY);

        try
        {
            if (values != null)
            {
                for (int i = 0; i < values.length; i++)
                {
                    if (values[i].trim().equalsIgnoreCase(nameValue))
                    {
                        state = false;

                        break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            state = true;
        }

        return state;
    }

    public void setValue(String nameValue, String value)
    {
        section.setProperty(nameValue, value);
    }

    public Collection values()
    {
        return section.values();
    }

    public Iterator listProperty()
    {
        return section.keySet().iterator();
    }

    /*
     * Metodo per la gestione del Multivalore.
     */
    public String[] getValues(String nameKey)
    {
        String value = getValue(nameKey);
        String[] values = null;

        if (value != null)
        {
            values = value.split(SEP);
        }
        else
        {
            values = new String[0];
        }

        return values;
    }
}
