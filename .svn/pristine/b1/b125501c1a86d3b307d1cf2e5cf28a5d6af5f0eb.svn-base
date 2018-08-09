package com.carel.supervisor.report.bean;

import java.sql.Timestamp;

public class HistoryReportBean implements ReportBean{
	private Timestamp ts;
	private String[] values;

	public HistoryReportBean(Timestamp ts, String[] values) {
		this.ts = ts;
		this.setValues(values);
	}

	public Timestamp getTs() {
		return ts;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public String[] getValues() {
		return values;
	}

	public String getValue(int idx){
		if (idx < 0 || idx >= values.length){
			return null;
		}
		return values[idx];
	}
}
