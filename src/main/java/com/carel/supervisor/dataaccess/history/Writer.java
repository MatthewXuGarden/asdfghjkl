package com.carel.supervisor.dataaccess.history;


public class Writer
{
    private FDBQueue queue = null;

    public Writer(FDBQueue queue)
    {
        this.queue = queue;
  
    } //Writer	

    public void write(int num) throws Exception
    {
    	
        DataToWriteDb dataToWriteDb = null;
        dataToWriteDb = queue.dequeueVariables(num);

        if (null != dataToWriteDb)
        {
            HistoryMgr.getInstance().executeMultiUpdateAsData(null,
                dataToWriteDb.getSqlStatements(), dataToWriteDb.getValues());
        }
    } //write

    public void writeAll() throws Exception
    {
    	
        DataToWriteDb dataToWriteDb = queue.dequeueAllVariables();

        if (null != dataToWriteDb)
        {
            HistoryMgr.getInstance().executeMultiUpdateAsData(null,
                dataToWriteDb.getSqlStatements(), dataToWriteDb.getValues());
        }
    } //write
    
    
} //Class Write
