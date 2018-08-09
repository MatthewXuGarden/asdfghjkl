package com.carel.supervisor.base.io;

import java.lang.reflect.*;


public abstract class SimpleReader implements IReaderWrited
{
 
    public Object readArrayElement(int index)
    {
        Object value = readObject();

        return Array.get(value, index);
    }

    public int readInteger()
    {
        return ((Integer) readObject()).intValue();
    }

    public long readLong()
    {
        return ((Long) readObject()).longValue();
    }

    public byte readByte()
    {
        return ((Byte) readObject()).byteValue();
    }

    public short readShort()
    {
        return ((Short) readObject()).shortValue();
    }

    public boolean readBoolean()
    {
        return ((Boolean) readObject()).booleanValue();
    }

    public float readFloat()
    {
        return ((Float) readObject()).floatValue();
    }

    public char readChar()
    {
        return ((Character) readObject()).charValue();
    }

    public double readDouble()
    {
        return ((Double) readObject()).doubleValue();
    }
}
