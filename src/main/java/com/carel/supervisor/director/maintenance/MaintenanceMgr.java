package com.carel.supervisor.director.maintenance;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;


public class MaintenanceMgr extends InitializableBase implements IInitializable
{
    private static MaintenanceMgr me = new MaintenanceMgr();
    private static String TABLE_LIST = "tablelist";
    private static String DIR_LIST = "dirlist";
    private List elements = new ArrayList();
    private List tableElem = null;
    private MaintenancePoller poller = null;

    private MaintenanceMgr()
    {
    }

    public static MaintenanceMgr getInstance()
    {
        return me;
    }

    public synchronized void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        try
        {
            XMLNode xml = null;
            XMLNode xmlTmp = null;

            for (int i = 0; i < xmlStatic.size(); i++)
            {
                xml = xmlStatic.getNode(i);

                if (TABLE_LIST.equals(xml.getNodeName()))
                {
                    TableElement table = null;

                    for (int j = 0; j < xml.size(); j++)
                    {
                        xmlTmp = xml.getNode(j);
                        table = new TableElement(xmlTmp);
                        elements.add(table);
                    }
                }
                else if (DIR_LIST.equals(xml.getNodeName()))
                {
                    DirElement dir = null;

                    for (int j = 0; j < xml.size(); j++)
                    {
                        xmlTmp = xml.getNode(j);
                        dir = new DirElement(xmlTmp);
                        elements.add(dir);
                    }
                }
            }

            tableElem = HistorMaintenance.retrieve();
            poller = new MaintenancePoller();
            poller.startPoller();
        }
        catch (Exception e)
        {
            throw new InvalidConfigurationException("");
        }
    }

    protected void activate() throws Exception
    {
        IElement element = null;
        String name = null;
        String action = null;
        Timestamp time = null;
        boolean reload = false;

        for (int i = 0; i < elements.size(); i++)
        {
            try
            {
                element = (IElement) elements.get(i);
                name = element.getName();
                action = element.getAction();
                time = retrieve(name, action);

                if (element.activate(time))
                {
                    reload = true;
                }
            }
            catch (Exception e)
            {
                LoggerMgr.getLogger(this.getClass()).error(e);
            }
        }

        if (reload)
        {
            tableElem = HistorMaintenance.retrieve();
        }
        //Kevin Ge, it is possible that TemporaryFolder will be deleted(it is inside PvPro)
        //so add it again here
        try{
	        File file = new File(BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder());
	        if(!file.exists())
	        {
	        	file.mkdirs();
	        }
        }
        catch(Exception ex)
        {
        	
        }
    }

    private Timestamp retrieve(String name, String action)
    {
        HistorData h = null;
        String nameTmp = null;
        String actionTmp = null;

        for (int i = 0; i < tableElem.size(); i++)
        {
            h = (HistorData) tableElem.get(i);
            nameTmp = h.getTablename();
            actionTmp = h.getAction();

            if ((name.equals(nameTmp)) && (action.equals(actionTmp)))
            {
                return h.getLasttime();
            }
        }

        return new Timestamp(0);
    }

    public void stopChecker()
    {
        if (null != poller)
        {
            poller.stopPoller();
            poller = null;
        }
    }

    public void startChecker()
    {
        if (null == poller)
        {
            try
            {
                tableElem = HistorMaintenance.retrieve();
            }
            catch (Exception e)
            {
                LoggerMgr.getLogger(this.getClass()).error(e);
            }

            poller = new MaintenancePoller();
            poller.startPoller();
        }
    }
}
