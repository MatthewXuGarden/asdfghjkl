package com.carel.supervisor.presentation.bean;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class UserBean {
	public static final String IDUSER = "iduser";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String LASTPWDCHANGE = "lastpwdchange";
	public static final String BADLOGONS = "badlogons";
	public static final String IDPROFILE = "idprofile";
	
	private Integer iduser = null;
    private String username = null;
    private String password = null;
    private Timestamp lastpwdchange = null;
    private Integer badlogons = null;
    private Integer idprofile = null;
    
    public UserBean(Record r){
    	this.iduser = (Integer) r.get(IDUSER);
        this.username = UtilBean.trim(r.get(USERNAME));
        this.password = UtilBean.trim(r.get(PASSWORD));
        this.lastpwdchange = (Timestamp) r.get(LASTPWDCHANGE);
        this.badlogons = (Integer) r.get(BADLOGONS);
        this.idprofile = (Integer) r.get(IDPROFILE);
    }

	public Integer getIduser() {
		return iduser;
	}

	public void setIduser(Integer iduser) {
		this.iduser = iduser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getLastpwdchange() {
		return lastpwdchange;
	}

	public void setLastpwdchange(Timestamp lastpwdchange) {
		this.lastpwdchange = lastpwdchange;
	}

	public Integer getBadlogons() {
		return badlogons;
	}

	public void setBadlogons(Integer badlogons) {
		this.badlogons = badlogons;
	}

	public Integer getIdprofile() {
		return idprofile;
	}

	public void setIdprofile(Integer idprofile) {
		this.idprofile = idprofile;
	}
    
	
	

}
