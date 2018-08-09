package com.carel.supervisor.dataaccess.datalog;

import com.carel.supervisor.dataaccess.db.*;


public class HistoryContext
{
    private String dbId = null;
    private Object[][] data = null;
    private RecordSet recordSet = null;
    private String fileName = null;

    /**
     * @return: Object[][]
     */
    public Object[][] getData()
    {
        return data;
    }

    /**
     * @param dataTransfer
     */
    public void addData(Object[][] data)
    {
        this.data = data;
    }

    /**
     * @return: String
     */
    public String getDbId()
    {
        return dbId;
    }

    /**
     * @param dbId
     */
    public void setDbId(String dbId)
    {
        this.dbId = dbId;
    }

    /**
     * @return: RecordSet
     */
    public RecordSet getRecordSet()
    {
        return recordSet;
    }

    /**
     * @param recordSet
     */
    public void setRecordSet(RecordSet recordSet)
    {
        this.recordSet = recordSet;
    }

    /**
     * @return: String
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @param fileName
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
