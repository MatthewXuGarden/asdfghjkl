package com.carel.supervisor.field.dataconn.impl.modbus;

import com.carel.supervisor.dataaccess.db.Record;

public class ModbusSettings {

	private Float avalue;
	private Float bvalue;
	private Integer encoding;
	
	public static final Integer BINARY_FIXED_POINT = 0;
	public static final Integer IEEE_FLOATING_POINT = 1;
	public static final Integer BCD = 2;
	
	public ModbusSettings(Float a, Float b, Integer c) {
		setAvalue(a);
		setBvalue(b);
		setEncoding(c);
	}

	public ModbusSettings(Record r) {
		setAvalue(1f);
		setBvalue(0f);
		setEncoding(0);
		if(r.hasColumn("avalue")){
			setAvalue((Float)r.get("avalue"));
		}
		if(r.hasColumn("bvalue")){
			setBvalue((Float)r.get("bvalue"));
		}
		if(r.hasColumn("encoding")){
			setEncoding((Integer)r.get("encoding"));
		}
	}
	
	
	public void setAvalue(Float avalue) {
		this.avalue = avalue;
	}
	
	public Float getAvalue() {
		return avalue;
	}
	
	public void setBvalue(Float bvalue) {
		this.bvalue = bvalue;
	}
	
	public Float getBvalue() {
		return bvalue;
	}

	public void setEncoding(Integer coding) {
		this.encoding = coding;
	}

	public Integer getEncoding() {
		return encoding;
	}
	
}
