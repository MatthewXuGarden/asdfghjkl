package com.carel.supervisor.report.bean;

import java.sql.Timestamp;

public class AlarmBean implements ReportBean{
	private String startstop;
	private Timestamp ts;
	private String dev;
	private String alr;

	public AlarmBean(String startstop, Timestamp ts, String dev, String alr) {
		this.startstop = startstop;
		this.ts = ts;
		this.dev = dev;
		this.alr = alr;
	}

	public void setStartstop(String startstop) {
		this.startstop = startstop;
	}

	public String getStartstop() {
		return startstop;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}

	public Timestamp getTs() {
		return ts;
	}

	public void setDev(String dev) {
		this.dev = dev;
	}

	public String getDev() {
		return dev;
	}

	public void setAlr(String alr) {
		this.alr = alr;
	}

	public String getAlr() {
		return alr;
	}

}
