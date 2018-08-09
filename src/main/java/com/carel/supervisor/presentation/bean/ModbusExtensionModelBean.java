package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.db.Record;

public class ModbusExtensionModelBean {

	private Integer iddevmdl;
	private Integer startbit;
	private Integer stopbit;
	private Integer parity;
	private Integer readtimeout;
	private Integer writetimeout;
	private Integer activitytimeout;
	private Integer datatransmission;
	private Integer f1;
	private Integer f2;
	private Integer f3;
	private Integer f4;
	private Integer f5;
	private Integer f6;
	private Integer f15;
	private Integer f16;
	private Boolean hipriority;
	
	public ModbusExtensionModelBean(Record r){
		iddevmdl = (Integer) r.get("iddevmdl");
		startbit = (Integer) r.get("startbit");
		stopbit = (Integer) r.get("stopbit");
		parity = (Integer) r.get("parity");
		readtimeout = (Integer) r.get("readtimeout");
		writetimeout = (Integer) r.get("writetimeout");
		activitytimeout = (Integer) r.get("activitytimeout");
		datatransmission = (Integer) r.get("datatransmission");
		f1 = (Integer) r.get("f1");
		f2 = (Integer) r.get("f2");
		f3 = (Integer) r.get("f3");
		f4 = (Integer) r.get("f4");
		f5 = (Integer) r.get("f5");
		f6 = (Integer) r.get("f6");
		f15 = (Integer) r.get("f15");
		f16 = (Integer) r.get("f16");
		hipriority = (Boolean) r.get("hipriority");
	}
	
	public ModbusExtensionModelBean(){
		iddevmdl = 0;
		startbit = 1;
		stopbit = 1;
		parity = 0;
		readtimeout = 20;
		writetimeout = 100;
		activitytimeout = 0;
		datatransmission = 1;
		f1 = 1;
		f2 = 1;
		f3 = 1;
		f4 = 1;
		f5 = 1;
		f6 = 1;
		f15 = 1;
		f16 = 1;
		hipriority = false;
	}
	
	public Integer getIddevmdl() {
		return iddevmdl;
	}
	public Integer getStartbit() {
		return startbit;
	}
	public Integer getStopbit() {
		return stopbit;
	}
	public Integer getParity() {
		return parity;
	}
	public Integer getReadtimeout() {
		return readtimeout;
	}
	public Integer getWritetimeout() {
		return writetimeout;
	}
	public Integer getActivitytimeout() {
		return activitytimeout;
	}
	public Integer getDatatransmission() {
		return datatransmission;
	}
	public Integer getF1() {
		return f1;
	}
	public Integer getF2() {
		return f2;
	}
	public Integer getF3() {
		return f3;
	}
	public Integer getF4() {
		return f4;
	}
	public Integer getF5() {
		return f5;
	}
	public Integer getF6() {
		return f6;
	}
	public Integer getF15() {
		return f15;
	}
	public Integer getF16() {
		return f16;
	}
	public boolean isHiPriority()
	{
		return hipriority != null && hipriority == true;
	}
}
