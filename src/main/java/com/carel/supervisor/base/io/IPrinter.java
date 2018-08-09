package com.carel.supervisor.base.io;

public interface IPrinter
{
    public abstract void printArrayElement(String name, Object value, int index);

    public abstract void print(String name, Object value);

    public abstract void print(String name, int value);

    public abstract void print(String name, long value);

    public abstract void print(String name, byte value);

    public abstract void print(String name, short value);

    public abstract void print(String name, boolean value);

    public abstract void print(String name, float value);

    public abstract void print(String name, char value);

    public abstract void print(String name, double value);

    public abstract StringBuffer getStringBuffer();
}
