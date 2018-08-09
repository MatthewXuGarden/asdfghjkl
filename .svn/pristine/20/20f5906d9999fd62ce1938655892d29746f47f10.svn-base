package com.carel.supervisor.dispatcher.comm.field;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


//Questa classe viene scambiata con l' esterno attraverso CommunicationConnectoMgr, e contiene tutti i messaggi 
//raccolti dai vari device logici e passati al client che passa la classe come argomento.
public class DevicesMessages
{
    private Map mp_Messages = Collections.synchronizedMap(new TreeMap());
    private int counter = 0;

    public DevicesMessages()
    {
        counter = 0;
    }

    public void add(DeviceMessage dm)
    {
        mp_Messages.put(new Integer(++counter), dm);
    }

    public void add(int message_id, //Id del sottosistema. Vedo CommunicationIDs
        int message_sub, //id del messaggio di richiesta o risposta.
        String message_s) //Messaggio del sottosistema in forma di stringa
    {
        DeviceMessage dm = new DeviceMessage();
        dm.message_id = message_id;
        dm.message_s = message_s;
        dm.message_sub = message_sub;
        add(dm);
    }

    public boolean HasMore(DeviceMessage dm)
    {
        Set mappings = mp_Messages.keySet();

        java.util.Iterator i = mappings.iterator();

        if (i.hasNext())
        {
            DeviceMessage dm1 = (DeviceMessage) mp_Messages.remove(i.next());
            dm.message_id = dm1.message_id;
            dm.message_sub = dm1.message_sub;
            dm.message_s = dm1.message_s;

            return true;
        }
        else
        {
            return false;
        }
    }

    public Map getAll()
    {
        return mp_Messages;
    }

    public void clear()
    {
        mp_Messages.clear();
    }

    public void finalize() throws Throwable
    {
        this.clear();
        mp_Messages = null;
        super.finalize();
    }
}
