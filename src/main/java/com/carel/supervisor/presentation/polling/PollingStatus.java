package com.carel.supervisor.presentation.polling;


import java.sql.Timestamp;
import java.util.Date;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;


public class PollingStatus implements IDumpable {
	
	private static final String ID = "id";
    private static final String IDSITE = "idsite";
    private static final String SLOT_1 = "slot_1";
    private static final String SLOT_2 = "slot_2";
    private static final String SLOT_3 = "slot_3";
    private static final String SLOT_4 = "slot_4";
    private static final String CURRENT_ROW = "current_row";
    private static final String LAST_UPDATE = "lastupdate";
    
    private int id = 0;
    private int idsite = 0;
    private Timestamp slot_1 = null;
    private Timestamp slot_2 = null;
    private Timestamp slot_3 = null;
    private Timestamp slot_4 = null;
    private Date current_row = null;
    private Timestamp lastupdate = null;

    public PollingStatus()
    {
    }
    
    public PollingStatus(Record record)
    {
    	id = ((Integer) record.get(ID)).intValue();
    	idsite = ((Integer) record.get(IDSITE)).intValue();
    	slot_1 = ((Timestamp) record.get(SLOT_1));
    	slot_2 = ((Timestamp) record.get(SLOT_2));
    	slot_3 = ((Timestamp) record.get(SLOT_3));
    	slot_4 = ((Timestamp) record.get(SLOT_4));
    	current_row = ((Date) record.get(CURRENT_ROW));
    	lastupdate = ((Timestamp) record.get(LAST_UPDATE));
    }
    
    public DumpWriter getDumpWriter()
    {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);
        dumpWriter.print(ID, id);
        dumpWriter.print(IDSITE, idsite);
        dumpWriter.print(SLOT_1, slot_1);
        dumpWriter.print(SLOT_2, slot_2);
        dumpWriter.print(SLOT_3, slot_3);
        dumpWriter.print(SLOT_4, slot_4);
        dumpWriter.print(CURRENT_ROW, current_row);
        dumpWriter.print(LAST_UPDATE, lastupdate);
        
        return dumpWriter;
    }

	public static String getCURRENT_ROW() {
		return CURRENT_ROW;
	}

	public static String getID() {
		return ID;
	}

	public static String getIDSITE() {
		return IDSITE;
	}

	public static String getLAST_UPDATE() {
		return LAST_UPDATE;
	}

	public static String getSLOT_1() {
		return SLOT_1;
	}

	public static String getSLOT_2() {
		return SLOT_2;
	}

	public static String getSLOT_3() {
		return SLOT_3;
	}

	public static String getSLOT_4() {
		return SLOT_4;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdsite() {
		return idsite;
	}

	public void setIdsite(int idsite) {
		this.idsite = idsite;
	}

	public Timestamp getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(Timestamp lastupdate) {
		this.lastupdate = lastupdate;
	}

	public Timestamp getSlot_1() {
		return slot_1;
	}

	public void setSlot_1(Timestamp slot_1) {
		this.slot_1 = slot_1;
	}

	public Timestamp getSlot_2() {
		return slot_2;
	}

	public void setSlot_2(Timestamp slot_2) {
		this.slot_2 = slot_2;
	}

	public Timestamp getSlot_3() {
		return slot_3;
	}

	public void setSlot_3(Timestamp slot_3) {
		this.slot_3 = slot_3;
	}

	public Timestamp getSlot_4() {
		return slot_4;
	}

	public void setSlot_4(Timestamp slot_4) {
		this.slot_4 = slot_4;
	}
}
