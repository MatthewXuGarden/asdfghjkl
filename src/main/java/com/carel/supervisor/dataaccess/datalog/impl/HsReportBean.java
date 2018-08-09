package com.carel.supervisor.dataaccess.datalog.impl;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class HsReportBean 
{
	private int idhs = 0;
	private int idsite = 1;
	private String code = "";
	private String type = "";
	private String ishaccp = "";
	private int step = 0;
	private Timestamp creation = null;
	private String path = "";
	private Timestamp from;
	private Timestamp to;
	
	public HsReportBean(Record r)
	{
		this.idhs = ((Integer)r.get("idhsreport")).intValue();
		this.idsite = ((Integer)r.get("idsite")).intValue();
		this.code = UtilBean.trim(r.get("code"));
		this.type = UtilBean.trim(r.get("type"));
		this.ishaccp = UtilBean.trim(r.get("ishaccp"));
		this.step = ((Integer)r.get("step")).intValue();
		this.creation = (Timestamp)r.get("creationtime");
		this.path = UtilBean.trim(r.get("path"));
		this.from = (Timestamp)r.get("from");
		this.to = (Timestamp)r.get("to");
	}

	public String getCode() {
		return code;
	}

	public Timestamp getCreation() {
		return creation;
	}

	public int getIdhs() {
		return idhs;
	}

	public int getIdsite() {
		return idsite;
	}

	public String getIshaccp() {
		return ishaccp;
	}

	public String getPath() {
		return path;
	}

	public int getStep() {
		return step;
	}

	public String getType() {
		return type;
	}

	public void setFrom(Timestamp from) {
		this.from = from;
	}

	public Timestamp getFrom() {
		return from;
	}

	public void setTo(Timestamp to) {
		this.to = to;
	}

	public Timestamp getTo() {
		return to;
	}
}
