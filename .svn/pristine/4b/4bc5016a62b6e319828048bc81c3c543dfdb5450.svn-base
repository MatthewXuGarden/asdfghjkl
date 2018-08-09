package com.carel.supervisor.dataaccess.dataconfig;

public class UtilBean
{
    private UtilBean()
    {
    }

    public static String trim(Object o)
    {
        String s = (String) o;

        if (s == null)
        {
            return s;
        }
        else
        {
            return s.trim();
        }
    }

    public static boolean checkBoolean(Object o, boolean defaultValue)
    {
        String s = (String) o;

        if (s == null)
        {
            return defaultValue;
        }
        else
        {
            if (s.trim().equals("TRUE"))
            {
                return true;
            }
            else if (s.trim().equals("FALSE"))
            {
                return false;
            }
            else
            {
                return defaultValue;
            }
        }
    }

    public static String writeBoolean(boolean value)
    {
        if (value)
        {
            return "TRUE";
        }
        else
        {
            return "FALSE";
        }
    }
}
