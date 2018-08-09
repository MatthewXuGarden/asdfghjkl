package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import java.sql.Timestamp;


/**
 * @author Loris D'Acunto <br>
 * Carel S.p.A. <br>
 * <br>
 * 11-gen-2006 10.29.46 <br>
 */
public class NoteLog
{
    private static final String ID_NOTE = "idnote";
    private static final String SITE = "idsite";
    private static final String TABLE_NAME = "tablename";
    private static final String TABLE_ID = "tableid";
    private static final String NOTE = "note";
    private static final String USER_NOTE = "usernote";
    private static final String START_TIME = "starttime";
    private static final String LAST_UPDATE = "lastupdate";
    
    private static int maxCounterForUniqueIndex = 50;
    
    //
    private int id = -1;
    private String pvcode = null;
    private int site = 0;
    private String tableName = null;
    private int tableId = -1;
    private String note = null;
    private String userNote = null;
    private Timestamp startTime = null;
    private Timestamp lastTime = null;
    
    private int counterForUniqueIndex = 0;
    
    public NoteLog(Record record)
    {
        id = ((Integer) record.get(ID_NOTE)).intValue();
        pvcode = BaseConfig.getPlantId();
        site = ((Integer) record.get(SITE)).intValue();
        tableName = UtilBean.trim(record.get(TABLE_NAME));
        tableId = ((Integer) record.get(TABLE_ID)).intValue();
        note = UtilBean.trim(record.get(NOTE));
        userNote = UtilBean.trim(record.get(USER_NOTE));
        startTime = ((Timestamp) record.get(START_TIME));
        lastTime = ((Timestamp) record.get(LAST_UPDATE));
        counterForUniqueIndex = 0;
    }

    public NoteLog()
    {
    	counterForUniqueIndex = 0;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Timestamp getLastTime()
    {
        return lastTime;
    }

    public void setLastTime(Timestamp lastTime)
    {
        this.lastTime = lastTime;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public Timestamp getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Timestamp startTime)
    {
        this.startTime = startTime;
    }

    public int getTableId()
    {
        return tableId;
    }

    public void setTableId(int tableId)
    {
        this.tableId = tableId;
    }

    public String getTableName()
    {
        return tableName;
    }

    public String getPvcode()
    {
        return pvcode;
    }

    public void setPvcode(String pvcode)
    {
        this.pvcode = pvcode;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getUserNote()
    {
        return userNote;
    }

    public void setUserNote(String userNote)
    {
        this.userNote = userNote;
    }

    public void save(int idSite) throws Exception
    {
        Object[] values = new Object[9];
        SeqMgr o = SeqMgr.getInstance();
        values[0] = o.next(null, "hsnote", "idnote");
        values[1] = BaseConfig.getPlantId();
        values[2] = new Integer(idSite);
        values[3] = tableName;
        values[4] = new Integer(tableId);
        values[5] = note;
        values[6] = userNote;
        values[7] = startTime;
        values[8] = lastTime;

        innerSave(values);
    }
    
    private void innerSave(Object[] values)
    {
		String insert = "insert into hsnote values (?,?,?,?,?,?,?,?,?)";
		try 
		{
			DatabaseMgr.getInstance().executeStatement(null, insert, values, false);
		} 
		catch (Exception e) 
		{
			this.counterForUniqueIndex++;
			if(this.counterForUniqueIndex == maxCounterForUniqueIndex)
				return;
			String msg = e.getCause().getMessage();
			/*
			 * Fix for unique index on HSNOTE -> UI_HSNOTE
			 * Try to identify a unique index violation: "ui_hsnote" This unique
			 * index has to be removed first from PVP remote system table and
			 * after in PVP local system table.
			 * 
			 * This problem is caused by global ACK functionality 
			 * when note is mandatory for alarms 
			 */
			if (msg != null && msg.indexOf("ui_hsnote") != -1) 
			{
				values[8] = new Timestamp(System.currentTimeMillis() + 1L);
				innerSave(values);
			}
		}
    }
    
    public int getSite()
    {
        return site;
    }

    public void setSite(int site)
    {
        this.site = site;
    }
}
