package com.carel.supervisor.base.dump.registry;

import com.carel.supervisor.base.dump.*;
import com.carel.supervisor.base.util.*;
import java.lang.reflect.Array;


public class DumperArray implements IDumperObject
{
    public void write(String name, Object value, DumpWriter dumpWriter)
    {
        int dim = Array.getLength(value);
        DumpWriter dump = dumpWriter.createDumpWriter(name,
                ClassManaged.interpretArray(value.getClass()));

        for (int i = 0; i < dim; i++)
        {
            dump.printArrayElement("element", value, i);
        }

        dumpWriter.print(dump.getStream());
    }
}
