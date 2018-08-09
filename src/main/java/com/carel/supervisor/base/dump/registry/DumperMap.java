package com.carel.supervisor.base.dump.registry;

import com.carel.supervisor.base.dump.*;
import java.util.*;


public class DumperMap extends DumperObject implements IDumperObject
{
    public void write(String name, Object value, DumpWriter dumpWriter)
    {
        Map o = (Map) value;
        DumpWriter dump = dumpWriter.createDumpWriter(name, value.getClass());

        Iterator iterator = o.keySet().iterator();
        Object key = null;
        DumpWriter dumpCouple = null;

        while (iterator.hasNext())
        {
            key = iterator.next();
            dumpCouple = dumpWriter.createDumpWriter("element", (String) null);
            dumpCouple.print("key", key);
            dumpCouple.print("value", o.get(key));
            dump.print(dumpCouple.getStream());
        }

        dumpWriter.print(dump.getStream());
    }
}
