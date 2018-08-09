package com.carel.supervisor.dispatcher.enhanced;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


public class SyncFtp
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        //SyncLog sl = new SyncLog("10.0.1.138",21, new File("node1/alarms.log").length() ,"PVRemote", "1");
        //Per prima cosa scarico gli allarmi
        //Primo campo IP address del nodo in remoto.
        //Secondo campo la porta (21 per l'ftp).
        //Terzo campo il nome utente, nell' enhanced è sempre PVRemote
        //Quarto campo la pasword del  nodo.
        SyncLog sl = new SyncLog("127.0.0.1", 21, "PVRemote", "1");

        Vector Alarms = new Vector();

        //Se DoAlign ritorna false c'è stato un errore nella comunicazione.
        if (sl.DoAlign(Alarms) && (Alarms != null)) //ci sono nuovi allarmi e nessun errore di comunicazione
        {
            for (int i = 0; i < Alarms.size(); i++)
            {
                SyncRecord rec = (SyncRecord) Alarms.get(i);
                System.out.println(rec.AlarmDescription);
            }
        }

        //Adesso posso vedere la configurazione del nodo, ovviamente se non ci sono allarmi
        //non ha senso andare a vedere la configurazione del nodo, quindi devo vedere prima 
        //se ci sono allarmi.
        Map nc = sl.GetNodeConfiguration();
        Set mappings = nc.entrySet();

        //java.util.Iterator i = mappings.iterator();
        for (Iterator i = mappings.iterator(); i.hasNext();)
        {
            Map.Entry me = (Map.Entry) i.next();

            //Object ok = me.getKey();
            Object ov = me.getValue();
            SyncRecNodeConfiguration obj = (SyncRecNodeConfiguration) ov;

            //Stampo i dati di ogni variabile.
            System.out.print(obj.GlobalIdent + "\t");
            System.out.print(obj.Line + "\t");
            System.out.print(obj.SerialIdent + "\t");
            System.out.print(obj.UnitType + "\t");
            System.out.print(obj.UnitDescription + "\t");
            System.out.println("\n");
        }

        //distruttore
        sl.Finalize();
        sl = null;
    }
}
