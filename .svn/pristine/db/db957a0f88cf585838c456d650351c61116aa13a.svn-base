package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.dataaccess.datalog.*;


public abstract class HistoryLogBase implements IHistoryLog
{
    private IHistoryLog historyLog = null;

    public void setNext(IHistoryLog historyLog)
    {
        this.historyLog = historyLog;
    }

    public IHistoryLog getNext()
    {
        return historyLog;
    }
}
