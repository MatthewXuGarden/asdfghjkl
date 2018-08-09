package com.carel.supervisor.dispatcher.memory;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.io.SocketComm;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class DispMemMgr
{
    private static DispMemMgr dispMemConf = new DispMemMgr();
    private List device = null;
    private Map memory = null;
    private List fisicDevices = null;
    private Properties lProp = null;

    private DispMemMgr()
    {
        memory = new HashMap();
        device = new ArrayList();
        fisicDevices = new ArrayList();
        lProp = new Properties();
    }

    public static DispMemMgr getInstance()
    {
        return dispMemConf;
    }

    public void storeAllConfiguration(Properties prop)
    {
        // Save conf first time
        this.lProp = prop;
        
        //2010-12-27, Kevin Ge, reset memory for modem re-configure 
        memory = new HashMap();
        fisicDevices = new ArrayList();
        
        Iterator i = prop.keySet().iterator();
        String type = "";
        String sClass = "";

        while (i.hasNext())
        {
            type = (String) i.next();
            sClass = (String) prop.getProperty(type);

            if ((sClass != null) && !sClass.equalsIgnoreCase("NULL"))
            {
                try
                {
                    storeConfiguration(type, sClass);
                }
                catch (Exception e)
                {
                    Object[] obj = { type };
                    EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Config",
                        EventDictionary.TYPE_WARNING, "D017", obj);
                }
            }
        }
    }

    public void storeConfiguration(String type)
    {
        String sClass = this.lProp.getProperty(type);

        try
        {
            storeConfiguration(type, sClass);
        }
        catch (Exception e)
        {
            Object[] obj = { type };
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Config",
                EventDictionary.TYPE_WARNING, "D017", obj);
        }
    }

    public ZMemory readConfiguration(String type)
    {
        return (ZMemory) this.memory.get(type);
    }

    /*public void loadMachineDevice(String[] typeList)
    {
            DispLayer dl = null;
            String[] fisDev = null;
            String m = "";

            if(typeList != null)
            {
                    for(int i=0; i<typeList.length; i++)
                    {
                            if(typeList[i].equalsIgnoreCase("F"))
                                    dl = new DispLayFax();
                            else if(typeList[i].equalsIgnoreCase("S"))
                                    dl = new DispLaySms();
                            else if(typeList[i].equalsIgnoreCase("D"))
                                    dl = new DispLayDialUp();
                            else if(typeList[i].equalsIgnoreCase("R"))
                                    dl = new DispLayRas();
                            else if(typeList[i].equalsIgnoreCase("E"))
                                    dl = new DispLayMail();

                            if(dl != null)
                                    fisDev = dl.getFisicChannel();

                            if(fisDev != null)
                            {
                                    for(int j=0; j<fisDev.length; j++)
                                    {
                                            if(typeList[i].equalsIgnoreCase("E"))
                                            {
                                                    m = dl.getModemDialUp(fisDev[j]);
                                                    device.add(new FisicMemory(typeList[i],m,fisDev[j]));
                                            }
                                            else
                                                    device.add(new FisicMemory(typeList[i],fisDev[j]));
                                    }
                            }
                    }
            }
    }*/
    public void loadMachineDevice(int port,String[] typeList)
    {
        String[] info = new String[0];
        String provider = "";
        String fisic = "";
        String resp = "";
        int idx = -1;

        if (typeList != null)
        {
        	this.device.clear();
        	
            for (int i = 0; i < typeList.length; i++)
            {
                resp = SocketComm.sendCommand("localhost", port, "GDF;" + typeList[i]);
                
                if (resp != null)
                {
                    info = StringUtility.split(resp, ";");

                    if (info != null)
                    {
                        for (int j = 0; j < info.length; j++)
                        {
                            resp = info[j];
                            idx = resp.indexOf("@");

                            if (idx != -1)
                            {
                                fisic = resp.substring(0, idx);
                                provider = resp.substring(idx + 1);
                                this.device.add(new FisicMemory(typeList[i], fisic, provider));
                            }
                        }
                    }
                }
            }
        }
    }

    public String[][] getMachineDevice(String type)
    {
        FisicMemory fm = null;
        List lista = new ArrayList();

        if (this.device != null)
        {
            for (int i = 0; i < this.device.size(); i++)
            {
                fm = (FisicMemory) this.device.get(i);

                if ((fm != null) && fm.getTypeFisic().equalsIgnoreCase(type))
                {
                    lista.add(fm);
                }
            }
        }

        String[][] list = new String[lista.size()][2];

        for (int i = 0; i < lista.size(); i++)
        {
            list[i][0] = ((FisicMemory) lista.get(i)).getModem();
            list[i][1] = ((FisicMemory) lista.get(i)).getProvider();
        }

        return list;
    }

    private void storeConfiguration(String type, String sClass)
        throws Exception
    {
        ZMemory objMem = createObject(sClass);

        if (objMem != null)
        {
            try
            {
                objMem.storeConfiguration();
            }
            catch (Exception e)
            {
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }

            this.addFisicDevice(objMem.getFisicDeviceId());
            this.memory.put(type, objMem);
            DispatcherMgr.getInstance().startFisicDevice(objMem.getFisicDeviceId());
        }
        else
        {
            throw new Exception("Object " + sClass + " not created");
        }
    }

    private ZMemory createObject(String classe)
    {
        ZMemory objRet = null;

        try
        {
            objRet = (ZMemory) FactoryObject.newInstance(classe);
        }
        catch (Exception e)
        {
            objRet = null;
        }

        return objRet;
    }

    private void addFisicDevice(String id)
    {
        if (id != null)
        {
            if (!this.fisicDevices.contains(id))
            {
                this.fisicDevices.add(id);
            }
        }
    }
}
