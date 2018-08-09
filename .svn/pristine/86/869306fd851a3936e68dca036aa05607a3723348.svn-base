package com.carel.supervisor.base.dump.registry;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author LDAC
 * @version 1.0
 */
import com.carel.supervisor.base.dump.*;


public class DumperObject implements IDumperObject
{
    public void write(String name, Object value, DumpWriter dumpWriter)
    {
        if (null == value)
        {
            dumpWriter.print(dumpWriter.formatObject(name, "null", "null"));
        }
        else
        {
            dumpWriter.print(dumpWriter.formatObject(name, value.toString(),
                    value.getClass().getName()));
        }
    }
}
