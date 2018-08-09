package com.carel.supervisor.remote.bean;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class ImportBean 
{
	private String idfile = "";
	private int idsite = 0;
	private String state = "";
	Timestamp ins = null;
	Timestamp upd = null;
	
	public ImportBean(Record r)
	{
		this.idfile = UtilBean.trim(r.get("idimport"));
		this.idsite = ((Integer)r.get("idsite")).intValue();
		this.state  = UtilBean.trim(r.get("state"));
		this.ins    = (Timestamp)r.get("inserttime");
		this.upd    = (Timestamp)r.get("lastupdate");
	}

	public String getIdfile() {
		return idfile;
	}

	public int getIdsite() {
		return idsite;
	}

	public Timestamp getIns() {
		return ins;
	}

	public String getState() {
		return state;
	}

	public Timestamp getUpd() {
		return upd;
	}
}
