package com.carel.supervisor.presentation.note;

import com.carel.supervisor.dataaccess.datalog.impl.NoteLog;
import com.carel.supervisor.dataaccess.datalog.impl.NoteLogList;
import com.carel.supervisor.presentation.session.UserTransaction;

import java.sql.Timestamp;


public class Note
{
    public Note()
    {
    }

    public static void executeNoteAction(UserTransaction ut, int idsite,
        String user_action, String cmd, String textarea, String idnota,
        String tablenote, int id) throws Exception
    {
        NoteLogList note = new NoteLogList();
        NoteLog note_to_mod = null;

        if (cmd != null)
        {
            if (cmd.equals("save"))
            {
                NoteLog noteLog = new NoteLog();
                noteLog.setLastTime(new Timestamp(System.currentTimeMillis()));
                noteLog.setStartTime(new Timestamp(System.currentTimeMillis()));

                if (textarea != null)
                {
                	// escape note to avoid js list break
                	noteLog.setNote(htmlEscape(textarea));
                }
                else
                {
                    noteLog.setNote("");
                }

                noteLog.setUserNote(user_action);
                noteLog.setTableName(tablenote);
                noteLog.setTableId(id);
                noteLog.save(idsite);
            }

            if (cmd.equals("rem"))
            {
                //controllo utente
                note_to_mod = note.retrieveNoteById(idsite, tablenote,
                        Integer.parseInt(idnota));

                if (note_to_mod.getUserNote().equals(user_action))
                {
                    note.removeNote(idsite, Integer.parseInt(idnota));
                }
                else
                {
                    ut.setProperty("wronguser", "yes");
                }
            }

            if (cmd.equals("mod"))
            {
                //            	controllo utente
                note_to_mod = note.retrieveNoteById(idsite, tablenote,
                        Integer.parseInt(idnota));

                if (note_to_mod.getUserNote().equals(user_action))
                {
                    note.updateNote(idsite, Integer.parseInt(idnota),
                        user_action, htmlEscape(textarea));
                    ut.setProperty("wronguser", "no");
                }
                else
                {
                    ut.setProperty("wronguser", "yes");
                }
            }
        }
    }
    

    private static String htmlEscape(String text)
    {
    	String str = text;
    	str = str.replaceAll("<", "&lt;");
    	str = str.replaceAll(">", "&gt;");
    	str = str.replaceAll("'", "&#39;");
    	str = str.replaceAll("\r", "&#13;");
    	str = str.replaceAll("\n", "&#10;");
    	str = str.replaceAll("\"", "&quot;");                	
    	return str;
    }
}
