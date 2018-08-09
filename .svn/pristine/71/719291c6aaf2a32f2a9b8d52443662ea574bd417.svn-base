package com.carel.supervisor.base.io;

import com.carel.supervisor.base.util.*;
import java.lang.reflect.Array;


public abstract class SimplePrinter implements IPrinter
{
   
    public void printArrayElement(String name, Object value, int index)
    {
        int order = ClassManaged.getDimensionArray(value.getClass());

        if (1 < order)
        {
            print(name, Array.get(value, index));
        }
        else
        {
            Class className = value.getClass();

            if (className.isPrimitive())
            {
                if (Integer.TYPE == className)
                {
                    print(name, Array.getInt(value, index));
                }
                else if (Byte.TYPE == className)
                {
                    print(name, Array.getByte(value, index));
                }
                else if (Long.TYPE == className)
                {
                    print(name, Array.getLong(value, index));
                }
                else if (Float.TYPE == className)
                {
                    print(name, Array.getFloat(value, index));
                }
                else if (Double.TYPE == className)
                {
                    print(name, Array.getDouble(value, index));
                }
                else if (Short.TYPE == className)
                {
                    print(name, Array.getShort(value, index));
                }
                else if (Character.TYPE == className)
                {
                    print(name, Array.getChar(value, index));
                }
                else if (Boolean.TYPE == className)
                {
                    print(name, Array.getBoolean(value, index));
                }
            }
            else
            {
                print(name, Array.get(value, index));
            }
        }
    }

    public void print(String name, int value)
    {
        print(name, new Integer(value));
    }

    public void print(String name, long value)
    {
        print(name, new Long(value));
    }

    public void print(String name, byte value)
    {
        print(name, new Byte(value));
    }

    public void print(String name, short value)
    {
        print(name, new Short(value));
    }

    public void print(String name, boolean value)
    {
        print(name, new Boolean(value));
    }

    public void print(String name, float value)
    {
        print(name, new Float(value));
    }

    public void print(String name, char value)
    {
        print(name, new Character(value));
    }

    public void print(String name, double value)
    {
        print(name, new Double(value));
    }
}
