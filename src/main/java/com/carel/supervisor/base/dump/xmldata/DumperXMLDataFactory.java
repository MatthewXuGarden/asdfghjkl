package com.carel.supervisor.base.dump.xmldata;

import com.carel.supervisor.base.dump.*;


public class DumperXMLDataFactory implements IDumperFactory
{
    
    public DumpWriter createDumpWriter(String name, Class className)
    {
        return new XMLDataDumpWriter(name, className, this);
    }

    public DumpWriter createDumpWriter(String name, String className)
    {
        return new XMLDataDumpWriter(name, className, this);
    }
}
