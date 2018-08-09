package com.carel.supervisor.base.dump.registry;

import com.carel.supervisor.base.dump.*;


public class DumperDumpable implements IDumperObject
{
    public void write(String name, Object value, DumpWriter dumpWriter)
    {
        dumpWriter.print(dumpWriter.formatObjectSon(name,
                ((IDumpable) value).getDumpWriter().getStream()));
    }
}
