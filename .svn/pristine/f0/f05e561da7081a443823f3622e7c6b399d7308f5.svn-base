package com.carel.supervisor.base.dump;

import com.carel.supervisor.base.io.*;


public abstract class DumpWriter extends SimplePrinter
{
    private IDumperFactory dumperFactory = null;

    public DumpWriter(IDumperFactory dumperFactoryPar)
    {
        dumperFactory = dumperFactoryPar;
    }

    //TODO: PIUTTOSTO CHE USARE OBJECT, PROVARE A VEDERE SE TRA LE SUPER-CLASSI (E LE INTERFACCE) C'E' LA CLASSE CHE MI SERVE
    public void print(String name, Object value)
    {
        if (null == value)
        {
            DumperMgr.getInstance().getDefaultDumper().write(name, value, this);
        }
        else
        {
            if (value.getClass().isArray())
            {
                DumperMgr.getInstance().getDumper("[").write(name, value, this);
            }
            else
            {
                try
                {
                    if (value instanceof IDumpable)
                    {
                        DumperMgr.getInstance()
                                 .getDumper(IDumpable.class.getName()).write(name,
                            value, this);
                    }
                    else
                    {
                        DumperMgr.getInstance()
                                 .getDumper(value.getClass().getName()).write(name,
                            value, this);
                    }
                }
                catch (Exception e)
                {
                    DumperMgr.getInstance().getDefaultDumper().write(name,
                        value, this);
                }
            }
        }
    }

    public DumpWriter createDumpWriter(String name, Class className)
    {
        return dumperFactory.createDumpWriter(name, className);
    }

    public DumpWriter createDumpWriter(String name, String className)
    {
        return dumperFactory.createDumpWriter(name, className);
    }

    public abstract void print(Object objectToAdd);

    public abstract StringBuffer getStringBuffer();

    public abstract Object getStream();

    public abstract Object formatObject(String name, String value, String type);

    public abstract Object formatObjectSon(String name, Object value);
}
