package com.carel.supervisor.presentation.polling;



import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.SeqMgr;


public class TimeSite implements IDumpable {

	private static final String ID = "id";
    private static final String IDSITE = "idsite";
    private static final String IDRMTIMETABLE = "idrmtimetable";
    private static final String STATUS = "status";
    private static final String LAST_UPDATE = "lastupdate";
    
    private Map campijoin = new HashMap();
    //Campi join con tabella cfsite e rmtimetable
    private static final String NAMEF = "namef";
    private static final String NAMES = "names";
    
    private int id = 0;
    private int idsite = 0;
    private int idrmtimetable = 0;
    private String status = null;
    private Timestamp lastupdate = null;
    
    public TimeSite()
    {
    }
    
    public TimeSite(Record record)
    {
    	id = ((Integer) record.get(ID)).intValue();
    	idsite = ((Integer) record.get(IDSITE)).intValue();
    	idrmtimetable = ((Integer) record.get(IDRMTIMETABLE)).intValue();
        status = UtilBean.trim(record.get(STATUS));
        lastupdate = ((Timestamp) record.get(LAST_UPDATE));
    }
    
    public TimeSite(Record record,boolean join){
    	campijoin.put(ID,record.get(ID));
    	campijoin.put(NAMEF,record.get(NAMEF));
    	campijoin.put(NAMES,record.get(NAMES));
    	campijoin.put(STATUS,record.get(STATUS));
    }
    
    public String getValue(String key){
    	return campijoin.get(key).toString();
    }
    
    public Map getCampiJoin(){
    	return campijoin;
    }
    
    public DumpWriter getDumpWriter()
    {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);
        dumpWriter.print(ID, id);
    
        return dumpWriter;
    }

	public static String getID() {
		return ID;
	}

	public static String getIDRMTIMETABLE() {
		return IDRMTIMETABLE;
	}

	public static String getIDSITE() {
		return IDSITE;
	}

	public static String getLAST_UPDATE() {
		return LAST_UPDATE;
	}

	public static String getSTATUS() {
		return STATUS;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
	public int save() throws DataBaseException
    {
        Object[] values = new Object[5];
        SeqMgr o = SeqMgr.getInstance();
        values[0] = o.next(null, "rmtimesite", "id");
        values[1] = new Integer(idsite);
        values[2] = new Integer(idrmtimetable);
        values[3] = status;
        values[4] = new Timestamp(System.currentTimeMillis());

        String insert = "insert into rmtimesite values (?,?,?,?,?)";
        DatabaseMgr.getInstance().executeStatement(null, insert, values);
       
        return ((Integer) values[0]).intValue();
    }

	public static String getNAMEF() {
		return NAMEF;
	}

	public static String getNAMES() {
		return NAMES;
	}

	public int update() throws DataBaseException{
		Object[] values = new Object[5];
	
        String update = "update rmtimesite set idsite = ?,idrmtimetable = ?,status = ?,lastupdate = ? where id = ?";
        values[0] = new Integer(idsite);
        values[1] = new Integer(idrmtimetable);
        values[2] = status;
        values[3] = new Timestamp(System.currentTimeMillis());
        values[4] = new Integer(id);
        
        DatabaseMgr.getInstance().executeStatement(null, update, values);
        
        return ((Integer) values[4]).intValue();
	}
   
   public void delete_rmpollingstatus() throws DataBaseException{
	   Object[] values = new Object[2];
	   String sql = "delete from rmpollingstatus where idsite = ? and current_row = ? ";
	   values[0] = new Integer(idsite);
	   values[1] = DateUtils.date2String(Calendar.getInstance().getTime(),"yyyy/MM/dd");
	   
	   DatabaseMgr.getInstance().executeStatement(null, sql, values);
   }
    
}
