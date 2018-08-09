package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Loris D'Acunto <br>
 * Carel S.p.A. <br>
 * <br>
 * 12-gen-2006 15.28.21 <br>
 */
public class NoteLogList
{
    private List notes = new ArrayList();

    public void retrieve(int idsite, String tableName, int tableId)
        throws Exception
    {
        String query = "select * from hsnote where idsite = ? and tablename = ? and tableid = ? order by starttime";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,
                query,
                new Object[]
                {
                    new Integer(idsite), tableName, new Integer(tableId)
                });
        NoteLog noteLog = null;

        for (int i = 0; i < recordSet.size(); i++)
        {
            noteLog = new NoteLog(recordSet.get(i));
            addNote(noteLog);
        }
    }

    public NoteLog retrieveNoteById(int idsite, String tableName, int idnote)
        throws Exception
    {
        String query = "select * from hsnote where idsite = ? and tablename = ? and idnote = ? ";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,
                query,
                new Object[] { new Integer(idsite), tableName, new Integer(idnote) });
        NoteLog noteLog = null;

        if (recordSet.size() > 0)
        {
            noteLog = new NoteLog(recordSet.get(0));
        }

        return noteLog;
    }

    public void retrieveLastNote(int idsite, String tableName, int tableId)
        throws Exception
    {
        RecordSet rs = null;
        NoteLog noteLog = null;
        Integer idTable = new Integer(tableId);
        Integer idSite = new Integer(idsite);

        String sql = "select * from hsnote where idsite = ? and tablename= ? and tableid= ? order by lastupdate desc offset 0 limit 1";

        rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { idSite, tableName, idTable });

        for (int i = 0; i < rs.size(); i++)
        {
            noteLog = new NoteLog(rs.get(i));
            addNote(noteLog);
        }
    }

    /**
     * @return
     */
    public int size()
    {
        return notes.size();
    }

    /**
     * @param pos
     * @return
     */
    public NoteLog getNote(int pos)
    {
        return (NoteLog) notes.get(pos);
    }

    /**
     * @param noteLog
     */
    public void addNote(NoteLog noteLog)
    {
        notes.add(noteLog);
    }

    /**
     * @param idNote
     * @throws Exception
     */
    public void removeNote(int idsite, int idnote) throws Exception
    {
        Object[] value = new Object[2];
        value[0] = new Integer(idsite);
        value[1] = new Integer(idnote);

        String sDelete = "delete from hsnote where idsite = ? and idnote = ?";
        DatabaseMgr.getInstance().executeStatement(null, sDelete, value);
    }

    public void updateNote(int idsite, int idnote, String user, String note)
        throws Exception
    {
        Object[] values = new Object[5];
        values[0] = note;
        values[1] = new Timestamp(System.currentTimeMillis());
        values[2] = user;
        values[3] = new Integer(idsite);
        values[4] = new Integer(idnote);

        String update = "update hsnote set note = ? , lastupdate = ?, usernote = ? where idsite = ? and idnote = ?";
        DatabaseMgr.getInstance().executeStatement(null, update, values);
    }
}
