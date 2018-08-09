package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.db.Record;

public class ClockBean {
	
	public static final int MASTER = 0;
	public static final int YEAR = 1;
	public static final int MONTH = 2;
	public static final int DAY = 3;
	public static final int WEEKDAY = 4;
	public static final int HOUR = 5;
	public static final int MINUTE = 6;
    
	String devCode = null;
	String[] varCode = null;
	public ClockBean(String devCode,String[] varCode)
	{
		this.devCode = devCode;
		this.varCode = varCode;
	}
	public ClockBean(Record record)
	 {
		this.devCode = (String) record.get("devcode");
		this.varCode = new String[7];
		varCode[MASTER] = (String)record.get("master");
		varCode[YEAR] = (String)record.get("year");
		varCode[MONTH] = (String)record.get("month");
		varCode[DAY] = (String)record.get("day");
		varCode[WEEKDAY] = (String)record.get("weekday");
		varCode[HOUR] = (String)record.get("hour");
		varCode[MINUTE] = (String)record.get("minute");
	 }
	public String getDevCode() {
		return devCode;
	}
	public void setDevCode(String devCode) {
		this.devCode = devCode;
	}
	public String[] getVarCode() {
		return varCode;
	}
	public void setVarCode(String[] varCode) {
		this.varCode = varCode;
	}
	
	
	//varDescription
	String[] varDescription = null;
	int iddevmdl = -1;
	String devDescription = null;
	public ClockBean(boolean withDescription,Record record)
	{
		this(record);
		this.varDescription = new String[7];
		this.iddevmdl = (Integer)record.get("iddevmdl");
		devDescription = (String)record.get("devd");
		varDescription[MASTER] = (String)record.get("masterd");
		varDescription[YEAR] = (String)record.get("yeard");
		varDescription[MONTH] = (String)record.get("monthd");
		varDescription[DAY] = (String)record.get("dayd");
		varDescription[WEEKDAY] = (String)record.get("weekdayd");
		varDescription[HOUR] = (String)record.get("hourd");
		varDescription[MINUTE] = (String)record.get("minuted");
	}
	public String[] getVarDescription() {
		return varDescription;
	}
	public void setVarDescription(String[] varDescription) {
		this.varDescription = varDescription;
	}
	public String getDevDescription() {
		return devDescription;
	}
	public void setDevDescription(String devDescription) {
		this.devDescription = devDescription;
	}
	public int getIddevmdl() {
		return iddevmdl;
	}
	public void setIddevmdl(int iddevmdl) {
		this.iddevmdl = iddevmdl;
	}
	
}
