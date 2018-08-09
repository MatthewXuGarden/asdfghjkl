package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.db.Record;

public class BookletCabinetDeviceBean {
	private int id = -1;
	private int idcabinet = -1;
	private int iddevice = -1;
	
	private static final String ID = "id";
	private static final String IDCABINET = "idcabinet";
	private static final String IDDEVICE = "iddevice";
	public BookletCabinetDeviceBean()
	{}
	
	public BookletCabinetDeviceBean(int id,int idcabinet,int iddevice)
	{
		this.id = id;
		this.idcabinet = idcabinet;
		this.iddevice = iddevice;
	}
	public BookletCabinetDeviceBean( Record record)throws Exception{
		this.id = ((Integer) record.get(ID)).intValue();
		this.idcabinet = ((Integer) record.get(IDCABINET)).intValue();
		this.iddevice = ((Integer) record.get(IDDEVICE)).intValue();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdcabinet() {
		return idcabinet;
	}

	public void setIdcabinet(int idcabinet) {
		this.idcabinet = idcabinet;
	}

	public int getIddevice() {
		return iddevice;
	}

	public void setIddevice(int iddevice) {
		this.iddevice = iddevice;
	}
}
