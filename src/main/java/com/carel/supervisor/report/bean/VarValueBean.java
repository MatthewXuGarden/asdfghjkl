package com.carel.supervisor.report.bean;

import java.sql.Timestamp;

public class VarValueBean implements ReportBean{
	private String descdev;
	private String descvar;
	private String value;
	private Timestamp timestamp;

	public VarValueBean(String descdev, String descvar, String value, Timestamp timestamp) {
		this.setDescdev(descdev);
		this.setDescvar(descvar);
		this.setValue(value);
		this.setTimestamp(timestamp);
	}

	public void setDescdev(String dev) {
		this.descdev = dev;
	}

	public String getDescdev() {
		return descdev;
	}

	public void setDescvar(String var) {
		this.descvar = var;
	}

	public String getDescvar() {
		return descvar;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
}
