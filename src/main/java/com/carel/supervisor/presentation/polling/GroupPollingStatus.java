package com.carel.supervisor.presentation.polling;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class GroupPollingStatus {

	private static final String LASTDIALUP = "lastdialup";
	private static final String IDSITE = "idsite";
	private static final String SITENAME = "name";
	private static final String CURRENT_ROW = "current_row";
	private static final String ID = "id"; //id tabella rmpollingstatus
	private PollingStatus pollingStatus;
	
	//Ho al massimo 4 slot per ogni sito
	//private TimeRow[] timeRow = new TimeRow[4];
	private ArrayList timeRow = new ArrayList();
	
	private int id=0;
	private int idsite = 0;
	private Timestamp lastdialup = null;
	private int idrmtimetable = 0;
	private String name = "";	//Nome della fascia
	private String sitename=null;
	private Date current_row = null;
	
	public GroupPollingStatus()
    {
    }
    
    public GroupPollingStatus(Record record)
    {
    	current_row = (Date) record.get(CURRENT_ROW);
    	lastdialup = ((Timestamp) record.get(LASTDIALUP));
    	id = ((Integer) record.get(ID)).intValue();
    	idsite = ((Integer) record.get(IDSITE)).intValue();
    	sitename = UtilBean.trim(record.get(SITENAME));
    	pollingStatus = new PollingStatus(record);
    	
    }
     
	public PollingStatus getPollingStatus() {
		return pollingStatus;
	}

	public void setPollingStatus(PollingStatus pollingStatus) {
		this.pollingStatus = pollingStatus;
	}

	public static String getLASTDIALUP() {
		return LASTDIALUP;
	}

	public Timestamp getLastdialup() {
		return lastdialup;
	}

	public void setLastdialup(Timestamp lastdialup) {
		this.lastdialup = lastdialup;
	}

	public static String getIDSITE() {
		return IDSITE;
	}

	public int getIdsite() {
		return idsite;
	}

	public void setIdsite(int idsite) {
		this.idsite = idsite;
	}

	public ArrayList getTimeRow() {
		return timeRow;
	}

	public void setTimeRow(ArrayList timeRow) {
		this.timeRow = timeRow;
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
	
	/**
	 * Aggiungo uno slot al sito in esame
	 * @param record
	 */
    public void addTimeRow(Record record){
    	timeRow.add(new TimeRow(record));
    }
    public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public static String getCURRENT_ROW() {
		return CURRENT_ROW;
	}

	public Date getCurrent_row() {
		return current_row;
	}

	public void setCurrent_row(Date current_row) {
		this.current_row = current_row;
	}

	public static String getID() {
		return ID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
