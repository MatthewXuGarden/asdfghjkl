package com.carel.supervisor.base.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;


public class TestDumper implements IDumpable
{
    private String stringa = "stringa";
    private Date data = new Date();
    private TestSlave testSlava = new TestSlave();
    private List vettore = new ArrayList();
    private Map tabella = new Hashtable();

    public TestDumper()
    {
        vettore.add("figlio1");
        vettore.add("figlio2");
        tabella.put("costo", new Integer(12212));
        tabella.put("tempo", new Long(12));
        tabella.put("media", new Float(0.2));
        tabella.put(new Integer(12), "inverso");
        tabella.put("annidata", vettore);
    }

    public DumpWriter getDumpWriter()
    {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("Prova", this);
        dumpWriter.print("nome", stringa);
        dumpWriter.print("giornata", data);
        dumpWriter.print("figlio", testSlava);
        dumpWriter.print("lista", vettore);
        dumpWriter.print("tabellina", tabella);

        String[][] prova = 
            {
                { "ty", "uy", "ui" },
                { "ty", "uy", "ui" }
            };
        TestSlave testSlave = new TestSlave();
        dumpWriter.print("figli", testSlave);

        int[][] provaInt = 
            {
                { 0, 1 },
                { 1, 2 }
            };

        dumpWriter.print("array", provaInt);
        dumpWriter.print("array2", prova);

        java.math.BigDecimal bigDecimal = new java.math.BigDecimal(
                "123131312.1232113");
        dumpWriter.print("numerogrosso", bigDecimal);

        StringBuffer buffer = new StringBuffer("stringbuffer");
        Calendar calendar = new GregorianCalendar();
        dumpWriter.print("calendar", calendar);
        dumpWriter.print("stringbuffer", buffer);

        return dumpWriter;
    }
}
