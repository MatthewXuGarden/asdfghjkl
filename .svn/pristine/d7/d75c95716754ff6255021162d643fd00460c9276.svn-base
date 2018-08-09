package com.carel.supervisor.dispatcher.main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.comm.external.ExternalPing;
import com.carel.supervisor.dispatcher.comm.layer.DispLayPing;
import com.carel.supervisor.dispatcher.comm.layer.DispLayer;


public class DispatcherDQMgr
{
    private static DispatcherDQMgr dqMgr = new DispatcherDQMgr();
    private Map fisicDevList = null;

    private DispatcherDQMgr()
    {
        this.fisicDevList = new HashMap();
    }

    public static DispatcherDQMgr getInstance()
    {
        return dqMgr;
    }

    public void startFisicDevice(String idFd, String sa, String sl, String at)
    {
        DispatcherDQ dispDq = null;

        if (idFd != null)
        {
            idFd = idFd.trim();

            if ((!idFd.equalsIgnoreCase("")) && (!this.fisicDevList.containsKey(idFd)))
            {
                dispDq = new DispatcherDQ(sa, sl, at, idFd);
                dispDq.startPoller();
                this.fisicDevList.put(idFd, dispDq);
            }
        }
    }

    public void stopFisicDevice(String idFd)
    {
        if (idFd != null)
        {
            idFd = idFd.trim();

            DispatcherDQ dispDq = (DispatcherDQ) this.fisicDevList.get(idFd);

            if (dispDq != null)
            {
                dispDq.stopPoller();
                dispDq = null;
            }
        }
    }
    
    /*
     * Utilizzato in fase di stop del dispatcher da pagina di sistema.
     * Stoppa tutti i thread relativi alle periferiche fisiche avviate dall'utente.
     */
    public void stopDevices()
    {
    	String key = "";
    	if(this.fisicDevList != null)
    	{
    		Iterator i = this.fisicDevList.keySet().iterator();
    		while(i.hasNext())
    		{
    			key = (String)i.next();
    			stopFisicDevice(key);
    		}
    		this.fisicDevList.clear();
    	}
    }
    
    public synchronized boolean ping(String device)
    {
    	if (device.indexOf("#")>=0){
    		//TODO sistemare in futuro per BMS
    		//per evitare che le device BA e BS non vengano pingate ma rientrino tutte nel classico giro
    		// del dispatcher (da correggere in futuro)
    		return true;	
    	}
    	else if( device.equals("LAN") )
    		return true;
    	else
        if (DispatcherMgr.getInstance().serviceExternal())
        {
            ExternalPing ePing = new ExternalPing(device);
            return ePing.send();
        }
        else
        {
            DispLayer d = new DispLayPing();
            return d.pingDevice(device);
        }
    }
}
