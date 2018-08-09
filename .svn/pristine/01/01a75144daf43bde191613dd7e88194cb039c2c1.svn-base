package com.carel.supervisor.dispatcher.main;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.comm.external.ExternalDial;
import com.carel.supervisor.dispatcher.comm.external.ExternalDialRem;
import com.carel.supervisor.dispatcher.memory.DMemory;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;


public class DispatcherManual
{
    private String number = "";

    public DispatcherManual(int idsite, String num)
    {
        this.number = num;
    }

    public boolean connect(String proto, String port, String url)
    {
        boolean risultato = false;

        String u = DispatcherMgr.getInstance().getUser4Remote();
        String p = DispatcherMgr.getInstance().getPass4Remote();
        String n = this.number;
        String c = "";
        String device = "";
        String cert = DispatcherMgr.getInstance().getCertificatePath();

        DMemory zMem = (DMemory) DispMemMgr.getInstance().readConfiguration("D");

        if (zMem != null)
        {
            try
            {
                n = this.number;
                c = zMem.getCentralino();
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }

            device = zMem.getFisicDeviceId();

            if ((device != null) && !device.equalsIgnoreCase(""))
            {
                ExternalDialRem eDial = new ExternalDialRem(u, p, n, c, device, "O", proto, port,
                        url, cert);
                risultato = eDial.send();
            }
            else
            {
                try
                {
                    EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                        EventDictionary.TYPE_ERROR, "D050", new Object[] { n });
                }
                catch (Exception e)
                {
                }
            }
        }

        return risultato;
    }

    public boolean disconnect()
    {
        boolean risultato = false;
        DMemory zMem = (DMemory) DispMemMgr.getInstance().readConfiguration("D");

        if (zMem != null)
        {
            String device = zMem.getFisicDeviceId();
            ExternalDial eDial = new ExternalDial("", "", "", "", device, "C");
            risultato = eDial.send();
        }

        return risultato;
    }
}
