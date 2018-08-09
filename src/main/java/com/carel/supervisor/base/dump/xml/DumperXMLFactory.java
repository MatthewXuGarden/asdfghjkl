package com.carel.supervisor.base.dump.xml;

import com.carel.supervisor.base.dump.*;


public class DumperXMLFactory implements IDumperFactory
{
   
    public DumpWriter createDumpWriter(String name, Class className)
    {
        return new XMLDumpWriter(name, className, this);
    }

    public DumpWriter createDumpWriter(String name, String className)
    {
        return new XMLDumpWriter(name, className, this);
    }
}
