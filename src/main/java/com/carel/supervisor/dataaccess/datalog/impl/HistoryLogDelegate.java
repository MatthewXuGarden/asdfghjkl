package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.base.config.*;
import com.carel.supervisor.base.factory.*;
import com.carel.supervisor.base.xml.*;
import com.carel.supervisor.dataaccess.datalog.*;


public class HistoryLogDelegate
{
    private final static String CLASS = "class";
    private final static String NAME = "name";
    private IHistoryLog historyLog = null;

    public HistoryLogDelegate(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        String name = null;
        String className = null;
        XMLNode xmlTmp = null;
        IHistoryLog historyLogNext = null;

        for (int i = xmlStatic.size() - 1; i >= 0; i--)
        {
            xmlTmp = xmlStatic.getNode(i);
            name = xmlTmp.getAttribute(NAME);

            if (null == name)
            {
                FatalHandler.manage(this, CoreMessages.format("DTCE0008", NAME));
            }

            className = xmlTmp.getAttribute(CLASS);

            if (null == className)
            {
                FatalHandler.manage(this, CoreMessages.format("DTCE0008", CLASS));
            }

            try
            {
                historyLog = (IHistoryLog) FactoryObject.newInstance(className);
            }
            catch (Exception e)
            {
                FatalHandler.manage(this,
                    CoreMessages.format("DTCE0014", className), e);
            }

            historyLog.setNext(historyLogNext);
            historyLogNext = historyLog;

            //Devo fare il link
        }
    }

    public void saveHistory(HistoryContext historyContext)
        throws Exception
    {
        saveHistory(null, historyContext);
    }

    public void retrieveHistory(HistoryContext historyContext)
        throws Exception
    {
        retrieveHistory(null, historyContext);
    }

    private void saveHistory(IHistoryLog historyLog,
        HistoryContext historyContext) throws Exception
    {
        IHistoryLog historyLogTmp = null;

        if (null == historyLog)
        {
            historyLogTmp = this.historyLog;
        }
        else
        {
            historyLogTmp = historyLog;
        }

        try
        {
            historyLogTmp.saveHistory(historyContext);
            historyLogTmp = historyLogTmp.getNext();

            if (null != historyLogTmp)
            {
                saveHistory(historyLogTmp, historyContext);
            }
        }
        catch (Exception e)
        {
            historyLogTmp = historyLogTmp.getNext();

            if (null != historyLogTmp)
            {
                saveHistory(historyLogTmp, historyContext);
            }

            throw e;
        }
    }

    private void retrieveHistory(IHistoryLog historyLog,
        HistoryContext historyContext) throws Exception
    {
        IHistoryLog historyLogTmp = null;

        if (null == historyLog)
        {
            historyLogTmp = this.historyLog;
        }
        else
        {
            historyLogTmp = historyLog;
        }

        try
        {
            historyLogTmp.retrieveHistory(historyContext);
            historyLogTmp = historyLogTmp.getNext();

            if (null != historyLogTmp)
            {
                retrieveHistory(historyLogTmp, historyContext);
            }
        }
        catch (Exception e)
        {
            historyLogTmp = historyLogTmp.getNext();

            if (null != historyLogTmp)
            {
                retrieveHistory(historyLogTmp, historyContext);
            }

            throw e;
        }
    }
}
