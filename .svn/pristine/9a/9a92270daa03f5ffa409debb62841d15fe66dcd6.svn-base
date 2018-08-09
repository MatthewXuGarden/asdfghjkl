package com.carel.supervisor.dispatcher.enhanced;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


class SyncIdentIP
{
    public String Ident = "";
    public String Ip = "";
}


public class SyncIdentIPList
{
    private static SyncIdentIPList me = null;
    private Map mp_IdentIP = Collections.synchronizedMap(new TreeMap());

    private SyncIdentIPList()
    {
        me = null;
    }

    public static synchronized SyncIdentIPList getIstance()
    {
        if (me == null)
        {
            me = new SyncIdentIPList();

            return me;
        }
        else
        {
            return me;
        }
    }

    public synchronized void Add(String Ident, String Ip)
    {
        SyncIdentIP rec = (SyncIdentIP) mp_IdentIP.get(Ip);

        if (rec == null)
        {
            rec = new SyncIdentIP();
            rec.Ident = Ident;
            rec.Ip = Ip;
            mp_IdentIP.put(Ip, rec);
        }
    }

    public synchronized String getIdent(String Ip)
    {
        SyncIdentIP rec = (SyncIdentIP) mp_IdentIP.get(Ip);
        mp_IdentIP.remove(Ip);

        if ((rec != null) && (rec.Ident != ""))
        {
            return rec.Ident;
        }

        return null;
    }

    public void finalize() throws Throwable
    {
        mp_IdentIP.clear();
        mp_IdentIP = null;
        super.finalize();
    }
}
