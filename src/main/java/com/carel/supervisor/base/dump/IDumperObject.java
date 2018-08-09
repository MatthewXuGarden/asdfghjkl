package com.carel.supervisor.base.dump;

public interface IDumperObject
{
    public abstract void write(String name, Object value, DumpWriter dumpWriter);
}
