package com.carel.supervisor.remote.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.script.ScriptInvoker;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.DispatcherID;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.remote.bean.IncomingBeanList;
import com.carel.supervisor.remote.engine.RasServer;
import com.carel.supervisor.remote.engine.connection.ActiveConnections;


public class RasServerMgr extends InitializableBase implements IInitializable
{
    private static RasServerMgr me = new RasServerMgr();
    private Map localMap = null;

    private RasServerMgr()
    {
        localMap = new HashMap();
    }

    public static RasServerMgr getInstance()
    {
        return me;
    }

    public synchronized void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        XMLNode xml = null;
        String n = "";
        String v = "";

        for (int i = 0; i < xmlStatic.size(); i++)
        {
            xml = xmlStatic.getNode(i);

            try
            {
                if (xml != null)
                {
                    n = xml.getAttribute("name");
                    v = xml.getAttribute("value");
                    localMap.put(n, v);
                }
            }
            catch (Exception e)
            {
                throw new InvalidConfigurationException("");
            }
        }
    }

    private boolean isEnable()
    {
        return Boolean.parseBoolean((String) localMap.get("enable"));
    }

    public String getPathServices()
    {
        String ps = (String) localMap.get("services");

        return BaseConfig.getCarelPath() + ps + File.separator;
    }

    public String getProtocol()
    {
        return (String) localMap.get("protocol");
    }

    public String getPort()
    {
        return (String) localMap.get("port");
    }

    public String getRoot()
    {
        return (String) localMap.get("root");
    }

    private String getUser()
    {
        return (String) localMap.get("login");
    }

    private String getPass()
    {
        return (String) localMap.get("passw");
    }

    private String getAfterTime()
    {
        return (String) localMap.get("after");
    }

    private String getCheckTime()
    {
        return (String) localMap.get("check");
    }

    public boolean closeIncoming(String ip)
    {
    	return ActiveConnections.getInstance().closeConnection(getUser(), getPass(), getPathServices(), ip);
    }

    public void configModemIncom(int idsite, String modemId)
    {
        if (configModemIncoming(modemId))
        {
            try
            {
                IncomingBeanList.insertDeviceConfiged(null, idsite, modemId);

                try
                {
                    Object[] p = { modemId };
                    EventMgr.getInstance().log(new Integer(1), "Remote", "Action",
                        EventDictionary.TYPE_WARNING, "R005", p);
                }
                catch (RuntimeException e1){}
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean configModemIncoming(String device)
    {
        boolean ret = true;
        int returnCode = 99;
        ScriptInvoker inv = new ScriptInvoker();
        String[] par = new String[]
            {
                "java", "-classpath",
                getPathServices() + "RasService.jar;" + getPathServices() + "DispatcherLight.jar;",
                "com.carel.supervisor.service.Starter", getUser(), getPass(), device
            };

        try
        {
            returnCode = inv.execute(par, getPathServices() + "RasServer.log");
            if (returnCode == DispatcherID.MODEM_CONF_OK)
                ret = true;
        }
        catch (Exception e) {
        }
        return ret;
    }

    public void startUpServerRas()
    {
    	if (isEnable())
    	{
    		String cert = DispatcherMgr.getInstance().getCertificatePath();
    		RasServer rs = new RasServer(getCheckTime(), getAfterTime(), getUser(), getPass(),
    									 getProtocol(), getPort(), getRoot(), getPathServices(), cert);
    		rs.startPoller();
    	}
    }
}
