package com.carel.supervisor.presentation.polling;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.SeqMgr;



public class TimeInfo implements IDumpable {

	private static final String IDRMTIMETABLE = "idrmtimetable";
    private static final String NAME = "name";
    private static final String LAST_UPDATE = "lastupdate";
    //Campi join con la tabella rmtime
    private static final String HOUR_FROM = "hour_from";
    private static final String HOUR_TO = "hour_to";
    private static final String MINUTE_FROM = "minute_from";
    private static final String MINUTE_TO = "minute_to";
    
    private int idrmtimetable = 0;
    private String name = null;
    private Timestamp lastupdate = null;
    
    private String from_time=null;
    private String to_time=null;
    
    //private Map tmpvalue = new HashMap();
    
    private ArrayList slot = new ArrayList();
    
    public TimeInfo()
    {
    }
    
    public TimeInfo(Record record)
    {
    	idrmtimetable = ((Integer) record.get(IDRMTIMETABLE)).intValue();
        name = UtilBean.trim(record.get(NAME));
        this.lastupdate = ((Timestamp) record.get(LAST_UPDATE));
    }
    
    public TimeInfo(Record record,boolean join)
    {
    	this(record);
    	int hour_from_int = ((Integer) record.get(HOUR_FROM)).intValue();
    	int minute_from_int = ((Integer) record.get(MINUTE_FROM)).intValue();
    	int hour_to_int = ((Integer) record.get(HOUR_TO)).intValue();
    	int minute_to_int = ((Integer) record.get(MINUTE_TO)).intValue();
    	
    	String hour_from_str,minute_from_str,hour_to_str,minute_to_str;
    	
    	hour_from_str = (hour_from_int < 10) ? "0"+hour_from_int : ""+hour_from_int;
    	minute_from_str = (minute_from_int < 10) ? "0"+minute_from_int : ""+minute_from_int;
    	hour_to_str = (hour_to_int < 10) ? "0"+hour_to_int : ""+hour_to_int;
    	minute_to_str = (minute_to_int < 10) ? "0"+minute_to_int : ""+minute_to_int;
    	
    	from_time = hour_from_str + ":" + minute_from_str;
    	to_time = hour_to_str + ":" + minute_to_str;
    }
    
    public DumpWriter getDumpWriter()
    {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);
        dumpWriter.print(IDRMTIMETABLE, idrmtimetable);
    
        return dumpWriter;
    }
    
    public Timestamp getLastupdate()
    {
        return lastupdate;
    }

    public void setLastupdate(Timestamp lastupdate)
    {
        this.lastupdate = lastupdate;
    }

	public static String getIDRMTIMETABLE() {
		return IDRMTIMETABLE;
	}

	public static String getNAME() {
		return NAME;
	}

	public int getIdrmtimetable() {
		return idrmtimetable;
	}

	public void setIdrmtimetable(int idrmtimetable) {
		this.idrmtimetable = idrmtimetable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int save() throws DataBaseException
    {
        Object[] values = new Object[3];
        SeqMgr o = SeqMgr.getInstance();
        values[0] = o.next(null, "rmtimetable", "idrmtimetable");
        values[1] = name;
        values[2] = new Timestamp(System.currentTimeMillis());

        String insert = "insert into rmtimetable values (?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, insert, values);
       
        return ((Integer) values[0]).intValue();
    }
	
	public int update() throws DataBaseException{
		Object[] values = new Object[3];
		//SeqMgr o = SeqMgr.getInstance();
	
        String update = "update rmtimetable set name = ?,lastupdate = ? where idrmtimetable = ?";
        values[0] = name;
        values[1] = new Timestamp(System.currentTimeMillis());
        values[2] = new Integer(idrmtimetable);
        
        DatabaseMgr.getInstance().executeStatement(null, update, values);
        
        return ((Integer) values[2]).intValue();
	}

	public ArrayList getSlot() {
		return slot;
	}

	//public void setSlot(ArrayList slot) {
	//	this.slot = slot;
	//}
	
	public void addValue(String time){
		slot.add(time);
	}

	public String getFrom_time() {
		return from_time;
	}

	public void setFrom_time(String from_time) {
		this.from_time = from_time;
	}

	public String getTo_time() {
		return to_time;
	}

	public void setTo_time(String to_time) {
		this.to_time = to_time;
	}
}
