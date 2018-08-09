package com.carel.supervisor.presentation.polling;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;


public class PollingRecord implements IDumpable {
	
	private static final String ID = "id";
	private static final String IDSITE = "idsite";
    private static final String SLOT_1 = "slot_1";
    private static final String SLOT_2 = "slot_2";
    private static final String SLOT_3 = "slot_3";
    private static final String SLOT_4 = "slot_4";
    private static final String CURRENT_ROW ="current_row";
    private static final String LAST_UPDATE = "lastupdate";
    
    
    private int id = 0;
    private int idsite = 0;
    private Timestamp slot_1 = null;
    private Timestamp slot_2 = null;
    private Timestamp slot_3 = null;
    private Timestamp slot_4 = null;
    private Date current_row = null;
    private Timestamp lastupdate = null;
    
    public PollingRecord()
    {
    }
    
    public PollingRecord(Record record)
    {

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
        dumpWriter.print(LAST_UPDATE, lastupdate);
        
        return dumpWriter;
    }
    
    
    
    public int save() throws DataBaseException
    {
        Object[] values = new Object[8];
        SeqMgr o = SeqMgr.getInstance();
        values[0] = o.next(null,"rmpollingstatus","id");
        values[1] = new Integer(idsite);
        values[2] = slot_1;
        values[3] = slot_2;
        values[4] = slot_3;
        values[5] = slot_4;
        values[6] = Calendar.getInstance().getTime();
        values[7] = new Timestamp(System.currentTimeMillis());
        
        String insert = "insert into rmpollingstatus values (?,?,?,?,?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, insert, values);
       
        return ((Integer) values[0]).intValue();
    }
    
    
    public int update() throws DataBaseException
    {
    	
        Object[] values = new Object[6];
        values[0] = slot_1;
        values[1] = slot_2;
        values[2] = slot_3;
        values[3] = slot_4;
        values[4] = new Timestamp(System.currentTimeMillis());
        values[5] = new Integer(id);
        
        String update = "update rmpollingstatus set slot_1=?,slot_2=?,slot_3=?,slot_4=?,lastupdate=? where id=? ";
        DatabaseMgr.getInstance().executeStatement(null, update, values);
       
        return ((Integer) values[5]).intValue();
    }
    
    public int  mod_type(Date current_row){
     try{
    	int id=0;
    	if(current_row == null)
    		id=save();
    	else
    		id=update();
     }catch(DataBaseException e){
    	 Logger logger = LoggerMgr.getLogger(this.getClass());
         logger.error(e);
     }
    	return id;
    }
    
    public void setSlotTimestamp(Timestamp value,int slot){
    	if(slot == 1)
    		slot_1 = value;
    	else if(slot == 2)
    		slot_2 = value;
    	else if(slot == 3)
    		slot_3 = value;
    	else if(slot == 4)
    		slot_4 = value;
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

	public Date getCurrent_row() {
		return current_row;
	}

	public void setCurrent_row(Date current_row) {
		this.current_row = current_row;
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
