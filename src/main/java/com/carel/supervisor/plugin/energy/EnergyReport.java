package com.carel.supervisor.plugin.energy;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

public class EnergyReport {

	//level
	public static final String SITE = "site";
	public static final String GROUP = "group";
	
	// type
	public static final Integer DAILY = 0;
	public static final Integer WEEKLY = 1;
	public static final Integer MONTHLY = 2;
	public static final Integer YEARLY = 3;
	public static final Integer CUSTOM = 4;

	// step
	public static final Integer HOUR = 10000;
	public static final Integer DAY = 10001;
	public static final Integer MONTH = 10002;

	private HashMap<String, EnergyReportRecord> reportRecords;

	private Integer type;
	private Integer step;
	private Timestamp begin;
	private Timestamp end;
	
	public EnergyReport(Timestamp begin, Timestamp end, Integer type, Integer step) {
		this.begin = begin;
		this.end = end;
		this.type = type;
		this.step = step;
		this.reportRecords = new HashMap<String, EnergyReportRecord>();
	}

	public Timestamp getBegin() {
		return begin;
	}

	public Timestamp getEnd() {
		return end;
	}

	public Integer getType() {
		return type;
	}

	public Integer getStep() {
		return step;
	}

	public HashMap<String, EnergyReportRecord> getReportRecords() {
		return reportRecords;
	}

	public EnergyReportRecord getReportRecord(String key) {
		return reportRecords.get(key);
	}
	
	public EnergyReportRecord putReportRecord(String key, EnergyReportRecord record) {
		return reportRecords.put(key, record);
	}
	
	public Integer getIntervalsNumber(){
		switch (type) {
		case 0: // EnergyReport.DAILY
			double rh = (double)(end.getTime() - begin.getTime())/3600000l;
			return (int)Math.round(rh);
		case 1: // EnergyReport.WEEKLY
			return 7;
		case 2: // EnergyReport.MONTHLY
			double r = (double)(end.getTime() - begin.getTime())/86400000l;
			return (int)Math.round(r);
		case 3: // EnergyReport.YEARLY
			return 12;
		case 4: // custom
			Long lstep = 0l;
			switch (step) {
			case 10000: // EnergyReport.HOUR
				lstep = 3600000l;
				break;
			case 10001: // EnergyReport.DAY
				lstep = 86400000l;
				break;
			case 10002: // EnergyReport.MONTH
				lstep = 2592000000l;
				break;
			default:
				lstep = 86400000l;
				break;
			}
			double r2 = (double)(end.getTime() - begin.getTime())/lstep;
			return (int)Math.round(r2);
		default:
			return null;
		}
	}

	public Timestamp endTimestamp(Timestamp begints) {
		GregorianCalendar gc = new GregorianCalendar();
		switch (step) {
		case 10000: // EnergyReport.HOUR
			gc.setTimeInMillis(begints.getTime());
			gc.add(Calendar.HOUR_OF_DAY, 1);
			return new Timestamp(gc.getTimeInMillis());
//			return new Timestamp(begints.getTime()+3600000l);
		case 10001: // EnergyReport.DAY
			gc.setTimeInMillis(begints.getTime());
			gc.add(Calendar.DAY_OF_MONTH, 1);
			return new Timestamp(gc.getTimeInMillis());
//			return new Timestamp(begints.getTime()+86400000l);
		case 10002: // EnergyReport.MONTH
			gc.setTimeInMillis(begints.getTime());
			gc.add(Calendar.MONTH, 1);
			return new Timestamp(gc.getTimeInMillis());
		default:
			return null;
		}
	}
	
	
	public double get_TS_KWh(int iTS, int iStep)
	{
		double nKWh = 0;
		int nGroups = EnergyMgr.getInstance().getIntegerProperty("groups", 10);
		for(int iGrp = 1; iGrp <= nGroups; iGrp++) {
			EnergyReportRecord record = getReportRecord("d" + iStep + ".group" + iGrp);
			if( record != null && record.getKWhTS() != null )
				nKWh += record.getKWhTS()[iTS];
		}
		EnergyReportRecord record = getReportRecord("d" + iStep + ".other");
		if( record != null && record.getKWhTS() != null )
			nKWh += record.getKWhTS()[iTS];
		
		return nKWh;
	}
}
