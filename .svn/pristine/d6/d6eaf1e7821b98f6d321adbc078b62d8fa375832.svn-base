package com.carel.supervisor.dataaccess.datalog;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.xml.*;
import com.carel.supervisor.dataaccess.datalog.impl.*;


public class DataLog implements IInitializable
{
    private final static String HISTORY_LOG = "historylog";
    private final static String ALARM_LOG = "alarmlog";
    private final static String NAME = "name";
    private static boolean initialized = false;
    private static final DataLog me = new DataLog();
    private HistoryLogDelegate historyLogDelegate = null;

    private DataLog()
    {
    }

    public static DataLog getInstance()
    {
        return me;
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            XMLNode xmlTmp = null;

            for (int i = 0; i < xmlStatic.size(); i++)
            {
                xmlTmp = xmlStatic.getNode(i);

                if (xmlTmp.getAttribute(NAME).equals(HISTORY_LOG))
                {
                    historyLogDelegate = new HistoryLogDelegate(xmlTmp);
                }
                else if (xmlTmp.getAttribute(NAME).equals(ALARM_LOG))
                {
                    //typeMgr = new TypeMgr(xmlTmp);
                }
            }

            initialized = true;
        }
    }

    public HistoryLogDelegate getHistoryLog()
    {
        return historyLogDelegate;
    }
}
