package com.carel.supervisor.dataaccess.datalog;

public interface IHistoryLog
{
    public abstract void saveHistory(HistoryContext historyContext)
        throws Exception;

    public abstract void retrieveHistory(HistoryContext historyContext)
        throws Exception;

    public abstract void setNext(IHistoryLog historyLog);

    public abstract IHistoryLog getNext();
}
