package com.carel.supervisor.base.test;

import com.carel.supervisor.base.dump.*;


public class TestSlave implements IDumpable
{
    private static final String stringaLocale = "Stringa Locale";
    private Integer intero = new Integer("29");

    public DumpWriter getDumpWriter()
    {
        DumpWriter o = DumperMgr.createDumpWriter("Oggetto Slave", this);
        o.print("nome", stringaLocale);
        o.print("eta", intero);
        o.print("numero_lungo", 4L);
        o.print("numero_intero", 4);
        o.print("variabile_booleana", false);

        return o;
    }
}
