package com.carel.supervisor.presentation.bean;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.db.Record;

public class BookletCabinetBean {
	private int id = -1;
	private String cabinet = null;
	private String fileName = null;
	private Timestamp lastUpdate = null;
	
	private static final String ID = "id";
	private static final String CABINET = "cabinet";
	private static final String FILENAME = "file_name";
	private static final String LASTUPDATE = "lastupdate";
	public BookletCabinetBean()
	{}
	
	public BookletCabinetBean(String cabinet,String fileName,Timestamp lastUpdate)
	{
		this.cabinet = cabinet;
		this.fileName = fileName;
		this.lastUpdate = lastUpdate;
	}
	public BookletCabinetBean( Record record)throws Exception{
		this.id = ((Integer) record.get(ID)).intValue();
		this.cabinet = (String)record.get(CABINET);
		this.fileName = ((String) record.get(FILENAME));
		this.lastUpdate = ((Timestamp) record.get(LASTUPDATE));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCabinet() {
		return cabinet;
	}

	public void setCabinet(String cabinet) {
		this.cabinet = cabinet;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	//logic property
	private int deviceNumber = -1;
	public int getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(int deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
}
