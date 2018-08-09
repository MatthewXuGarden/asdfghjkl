package com.carel.supervisor.base.dump.registry;

import com.carel.supervisor.base.dump.*;
import com.carel.supervisor.base.xml.*;
import java.util.*;


public class DumperIXMLNode extends DumperObject implements IDumperObject
{
    public void write(String name, Object value, DumpWriter dumpWriter)
    {
        XMLNode o = (XMLNode) value;
        dumpWriter.print("node-name", o.getNodeName());
        dumpWriter.print("node-value", o.getTextValue());

        //Child
        if (o.hasNodes())
        {
            DumpWriter dump = dumpWriter.createDumpWriter(name, value.getClass());

            for (int i = 0; i < o.size(); i++)
            {
                dump.print("element", o.getNode(i));
            }

            dumpWriter.print(dump.getStream());
        }

        //Attributes
        if (o.hasAttributes())
        {
            DumpWriter dump = dumpWriter.createDumpWriter(name, value.getClass());
            Map two = o.getAttributes();
            Iterator iterator = two.keySet().iterator();
            Object key = null;
            DumpWriter dumpCouple = null;

            while (iterator.hasNext())
            {
                key = iterator.next();
                dumpCouple = dumpWriter.createDumpWriter("element",
                        (String) null);
                dumpCouple.print("key", key);
                dumpCouple.print("value", two.get(key));
                dump.print(dumpCouple.getStream());
            }

            dumpWriter.print(dump.getStream());
        }
    }
}
