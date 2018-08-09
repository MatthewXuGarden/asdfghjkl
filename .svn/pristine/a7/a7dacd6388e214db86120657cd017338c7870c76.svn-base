package com.carel.supervisor.dispatcher.engine;

import java.util.Hashtable;
import java.util.Iterator;


public class LineStatus
{
    private Hashtable status = null;

    public LineStatus()
    {
        status = new Hashtable();
    }

    public void resetStatusByIp(String ip)
    {
        String key = "";
        String val = "";
        Iterator i = this.status.keySet().iterator();

        while (i.hasNext())
        {
            key = (String) i.next();
            val = (String) this.status.get(key);

            if ((val != null) && val.equalsIgnoreCase(ip))
            {
                resetStatus(key);

                break;
            }
        }
    }

    public void setStatus(String device, String ipRemote)
    {
        changeStatus(device, ipRemote);
    }

    public void resetStatus(String device)
    {
        changeStatus(device, "NOP");
    }

    public boolean freeStatus(String device)
    {
        String ip = (String) this.status.get(device);
        boolean st = true;

        if (ip != null)
        {
            if (!ip.equalsIgnoreCase("NOP"))
            {
                st = false;
            }
        }

        return st;
    }

    private void changeStatus(String device, String remote)
    {
        this.status.put(device, remote);
    }
}
