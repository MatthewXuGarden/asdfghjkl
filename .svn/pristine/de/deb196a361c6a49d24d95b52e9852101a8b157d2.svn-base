package com.carel.supervisor.presentation.polling;



import java.sql.Timestamp;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.SeqMgr;



public class TimeRow implements IDumpable {

	private static final String ID = "id";
    private static final String IDRMTIMETABLE = "idrmtimetable";
    private static final String HOUR_FROM = "hour_from";
    private static final String MINUTE_FROM = "minute_from";
    private static final String HOUR_TO = "hour_to";
    private static final String MINUTE_TO = "minute_to";
    private static final String DELTA = "delta";
    private static final String NSLOT = "nslot";
    private static final String LAST_UPDATE = "lastupdate";
    
    private int id = 0;
    private int idrmtimetable = 0;
    private int hour_from = 0;
    private int minute_from = 0;
    private int hour_to = 0;
    private int minute_to = 0;
    private int delta = 0;
    private int nslot = 0;
    private Timestamp lastupdate = null;

    public TimeRow()
    {
    }
    
    public TimeRow(Record record)
    {
    	id = ((Integer) record.get(ID)).intValue();
    	idrmtimetable = ((Integer) record.get(IDRMTIMETABLE)).intValue();
    	hour_from = ((Integer) record.get(HOUR_FROM)).intValue();
    	hour_to = ((Integer) record.get(HOUR_TO)).intValue();
    	minute_from = ((Integer) record.get(MINUTE_FROM)).intValue();
    	minute_to = ((Integer) record.get(MINUTE_TO)).intValue();
    	delta = ((Integer) record.get(DELTA)).intValue();
    	nslot = ((Integer) record.get(NSLOT)).intValue();
        lastupdate = ((Timestamp) record.get(LAST_UPDATE));
    }
    
    public TimeRow(Record record,boolean noId)
    {
    	idrmtimetable = ((Integer) record.get(IDRMTIMETABLE)).intValue();
    	hour_from = ((Integer) record.get(HOUR_FROM)).intValue();
    	hour_to = ((Integer) record.get(HOUR_TO)).intValue();
    	minute_from = ((Integer) record.get(MINUTE_FROM)).intValue();
    	minute_to = ((Integer) record.get(MINUTE_TO)).intValue();
    	nslot = ((Integer) record.get(NSLOT)).intValue();
    }
    
    public DumpWriter getDumpWriter()
    {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);
        dumpWriter.print(ID, id);
        dumpWriter.print(IDRMTIMETABLE, idrmtimetable);
        dumpWriter.print(HOUR_FROM, hour_from);
        dumpWriter.print(MINUTE_FROM, minute_from);
        dumpWriter.print(HOUR_TO, hour_to);
        dumpWriter.print(MINUTE_FROM, minute_from);
        dumpWriter.print(DELTA, delta);
        dumpWriter.print(NSLOT, nslot);
        
        return dumpWriter;
    }

    public int save() throws DataBaseException
    {
        Object[] values = new Object[9];
        SeqMgr o = SeqMgr.getInstance();
        values[0] = o.next(null,"rmtime","id");
        values[1] = new Integer(idrmtimetable);
        values[2] = new Integer(hour_from);
        values[3] = new Integer(minute_from);
        values[4] = new Integer(hour_to);
        values[5] = new Integer(minute_to);
        values[6] = new Integer(delta);
        values[7] = new Timestamp(System.currentTimeMillis());
        values[8] = new Integer(nslot);
        
        String insert = "insert into rmtime values (?,?,?,?,?,?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, insert, values);
       
        return ((Integer) values[0]).intValue();
    }

	public static String getDELTA() {
		return DELTA;
	}

	public static String getHOUR_FROM() {
		return HOUR_FROM;
	}

	public static String getHOUR_TO() {
		return HOUR_TO;
	}

	public static String getID() {
		return ID;
	}

	public static String getIDRMTIMETABLE() {
		return IDRMTIMETABLE;
	}

	public static String getLAST_UPDATE() {
		return LAST_UPDATE;
	}

	public static String getMINUTE_FROM() {
		return MINUTE_FROM;
	}

	public static String getMINUTE_TO() {
		return MINUTE_TO;
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}

	public int getHour_from() {
		return hour_from;
	}

	public void setHour_from(int hour_from) {
		this.hour_from = hour_from;
	}

	public int getHour_to() {
		return hour_to;
	}

	public void setHour_to(int hour_to) {
		this.hour_to = hour_to;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdrmtimetable() {
		return idrmtimetable;
	}

	public void setIdrmtimetable(int idrmtimetable) {
		this.idrmtimetable = idrmtimetable;
	}

	public Timestamp getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(Timestamp lastupdate) {
		this.lastupdate = lastupdate;
	}

	public int getMinute_from() {
		return minute_from;
	}

	public void setMinute_from(int minute_from) {
		this.minute_from = minute_from;
	}

	public int getMinute_to() {
		return minute_to;
	}

	public void setMinute_to(int minute_to) {
		this.minute_to = minute_to;
	}

	public static String getNSLOT() {
		return NSLOT;
	}

	public int getNslot() {
		return nslot;
	}

	public void setNslot(int nslot) {
		this.nslot = nslot;
	}
    
}
