package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class CustomViewBean 
{
	private int idmdl = 0;
	private String code = "";
	private String target = "";
	private String javascript = "";
	private String events = "";
	private String business = "";
	
	public CustomViewBean(Record r) 
	{
		this.idmdl = ((Integer)(r.get("iddevmdl"))).intValue();
		this.code = UtilBean.trim(r.get("code"));
		this.target = UtilBean.trim(r.get("target"));
		this.javascript = UtilBean.trim(r.get("javascript"));
		this.events = UtilBean.trim(r.get("events"));
		this.business = UtilBean.trim(r.get("business"));
	}

	public int getMdl() {
		return idmdl;
	}

	public String getEvents() {
		return events;
	}

	public String getJavascript() {
		return javascript;
	}

	public String getTarget() {
		return target;
	}	
	
	public String getCode() {
		return code;
	}
	
	public String getBusiness() {
		return business;
	}
}