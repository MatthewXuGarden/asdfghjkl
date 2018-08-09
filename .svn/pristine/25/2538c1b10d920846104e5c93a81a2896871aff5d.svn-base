package com.carel.supervisor.base.dump.registry;

import com.carel.supervisor.base.dump.*;
import java.util.*;


public class DumperList implements IDumperObject
{
    public void write(String name, Object value, DumpWriter dumpWriter)
    {
        List o = (List) value;
        DumpWriter dump = dumpWriter.createDumpWriter(name, value.getClass());

        for (int i = 0; i < o.size(); i++)
        {
            dump.print("element", o.get(i));
        }

        dumpWriter.print(dump.getStream());
    }
}
