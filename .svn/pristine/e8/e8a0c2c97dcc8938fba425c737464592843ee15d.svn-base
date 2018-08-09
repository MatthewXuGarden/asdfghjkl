package com.carel.supervisor.dataaccess.datalog.impl;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class ModuleBean {
	private String code = "";
	private String serial = "";
	private String activation = "";
	private String ident = "";
	private Timestamp registration = null;

	public ModuleBean(Record r) {
		code = UtilBean.trim(r.get("code"));
		serial = UtilBean.trim(r.get("serial"));
		activation = UtilBean.trim(r.get("activation"));
		ident = UtilBean.trim(r.get("ident"));
		registration = (Timestamp) r.get("registration");
	}
	
	public ModuleBean(String c,String s,String a,String i) {
		code = c;
		serial = s;
		activation = a;
		ident = i;
		registration = new Timestamp(System.currentTimeMillis());
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getActivation() {
		return activation;
	}

	public void setActivation(String activation) {
		this.activation = activation;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	public Timestamp getRegistration() {
		return registration;
	}

	public void setRegistration(Timestamp registration) {
		this.registration = registration;
	}

}
