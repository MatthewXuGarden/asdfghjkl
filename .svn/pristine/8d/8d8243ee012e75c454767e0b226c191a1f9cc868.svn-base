package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class CustomViewBttBean 
{
	private String name = "";
	private String action = "";
	private String tooltip = "";
	private String standby = "";
	private String over = "";
	private String click = "";
	private String disable = "";
	
	public CustomViewBttBean(Record r)
	{
		this.name = UtilBean.trim(r.get("name"));
		this.action = UtilBean.trim(r.get("action"));
		this.tooltip = UtilBean.trim(r.get("tooltip"));
		this.standby = UtilBean.trim(r.get("standby"));
		this.over = UtilBean.trim(r.get("over"));
		this.click = UtilBean.trim(r.get("click"));
		this.disable = UtilBean.trim(r.get("disable"));
	}

	public String getAction() {
		return action;
	}

	public String getClick() {
		return click;
	}

	public String getDisable() {
		return disable;
	}

	public String getName() {
		return name;
	}

	public String getOver() {
		return over;
	}

	public String getStandby() {
		return standby;
	}

	public String getTooltip() {
		return tooltip;
	}
}
