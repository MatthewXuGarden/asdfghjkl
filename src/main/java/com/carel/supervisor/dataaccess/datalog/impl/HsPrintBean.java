package com.carel.supervisor.dataaccess.datalog.impl;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class HsPrintBean 
{
	private int id = 0;
	private Timestamp data = null;
	private String actioncode = "";
	private String path = "";
	
	public HsPrintBean(Record r)
	{
		this.id = ((Integer)r.get("idprint")).intValue();
		this.data = (Timestamp)r.get("creationtime");
		this.actioncode = UtilBean.trim(r.get("actioncode"));
		this.path = UtilBean.trim(r.get("path"));
	}

	public String getActioncode() {
		return actioncode;
	}

	public Timestamp getData() {
		return data;
	}

	public int getId() {
		return id;
	}

	public String getPath() {
		return path;
	}
}
