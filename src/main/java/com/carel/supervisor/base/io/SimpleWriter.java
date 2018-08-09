package com.carel.supervisor.base.io;

import com.carel.supervisor.base.util.*;
import java.lang.reflect.Array;


public abstract class SimpleWriter implements IWriter
{
 
    public void writeArrayElement(Object value, int index)
    {
        int order = ClassManaged.getDimensionArray(value.getClass());

        if (1 < order)
        {
            write(Array.get(value, index));
        }
        else
        {
            Class className = value.getClass();

            if (className.isPrimitive())
            {
                if (Integer.TYPE == className)
                {
                    write(Array.getInt(value, index));
                }
                else if (Byte.TYPE == className)
                {
                    write(Array.getByte(value, index));
                }
                else if (Long.TYPE == className)
                {
                    write(Array.getLong(value, index));
                }
                else if (Float.TYPE == className)
                {
                    write(Array.getFloat(value, index));
                }
                else if (Double.TYPE == className)
                {
                    write(Array.getDouble(value, index));
                }
                else if (Short.TYPE == className)
                {
                    write(Array.getShort(value, index));
                }
                else if (Character.TYPE == className)
                {
                    write(Array.getChar(value, index));
                }
                else if (Boolean.TYPE == className)
                {
                    write(Array.getBoolean(value, index));
                }
            }
            else
            {
                write(Array.get(value, index));
            }
        }
    }

    public void write(int value)
    {
        write(new Integer(value));
    }

    public void write(long value)
    {
        write(new Long(value));
    }

    public void write(byte value)
    {
        write(new Byte(value));
    }

    public void write(short value)
    {
        write(new Short(value));
    }

    public void write(boolean value)
    {
        write(new Boolean(value));
    }

    public void write(float value)
    {
        write(new Float(value));
    }

    public void write(char value)
    {
        write(new Character(value));
    }

    public void write(double value)
    {
        write(new Double(value));
    }

    public abstract void write(Object value);

    public abstract StringBuffer getStringBuffer();

    public abstract Object getStream();
}
