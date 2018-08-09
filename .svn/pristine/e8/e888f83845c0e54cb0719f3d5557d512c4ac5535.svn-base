package com.carel.supervisor.presentation.bean;

import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class BookletConfBean
{
//    private static final String IdBookletConf = "idbookletconf";
//    private static final String IdUser = "iduser";
    private static final String Devparam = "devparam";
    private static final String Siteinfo = "siteinfo";
    private static final String Siteconf = "siteconf";
    private static final String Schedash = "schedash";
    private static final String Userconf = "userconf";
    private static final String Activealarm = "activealarm";
    private static final String Plugmodule = "plugmod";
    private static final String Notes = "notes";
//    private static final String Idsite = "idsite";
    private static final String IsPDF = "ispdf";

	private int idBookletConf = -1;
	private int idUser = -1;
	private boolean devparam =false;
	private boolean siteinfo =false;
	private boolean siteconf =false;
	private boolean schedash =false;
	private boolean userconf =false;
	private boolean activealarm =false;
	private boolean plugmodule =false;
	private boolean notes =false;
	private int idsite =-1;
    
	public static final int MAXENTRIES = 3000; 

    public BookletConfBean(){
    	this.devparam=true;
    	this.siteinfo=true;
    	this.siteconf =false;
    	this.schedash =false;
    	this.userconf =false;
    	this.activealarm =false;
    	this.plugmodule =false;
    	this.notes =false;
    }

    public BookletConfBean(RecordSet rs)throws Exception{
    	Record record = null;
        for (int i = 0; i < rs.size(); i++){
            record = rs.get(i);
            String opt = (String) record.get("option");
            if(opt.equalsIgnoreCase(Devparam)){
            	this.devparam=(Boolean)record.get("selected");
            	continue;
            }
            if(opt.equalsIgnoreCase(Siteinfo)){
            	this.siteinfo=(Boolean)record.get("selected");
            	continue;
            }
            if(opt.equalsIgnoreCase(Siteconf)){
            	this.siteconf=(Boolean)record.get("selected");
            	continue;
            }
            if(opt.equalsIgnoreCase(Schedash)){
            	this.schedash=(Boolean)record.get("selected");
            	continue;
            }
            if(opt.equalsIgnoreCase(Userconf)){
            	this.userconf=(Boolean)record.get("selected");
            	continue;
            }
            if(opt.equalsIgnoreCase(Activealarm)){
            	this.activealarm=(Boolean)record.get("selected");
            	continue;
            }
            if(opt.equalsIgnoreCase(Plugmodule)){
            	this.plugmodule=(Boolean)record.get("selected");
            	continue;
            }
            if(opt.equalsIgnoreCase(Notes)){
            	this.notes=(Boolean)record.get("selected");
            	continue;
            }
        }
//	    this.idBookletConf=(Integer) record.get(IdBookletConf);
//	    this.idUser=(Integer) record.get(IdUser);
//	    this.idsite=(Integer) record.get(Idsite);
//	    
//	    this.siteinfo=(Boolean)record.get(Siteinfo);
//	    this.siteconf=(Boolean)record.get(Siteconf);
//	    this.schedash=(Boolean)record.get(Schedash);
//	    this.userconf=(Boolean)record.get(Userconf);
//	    this.activealarm=(Boolean)record.get(Activealarm);
//	    this.plugmodule=(Boolean)record.get(Plugmodule);
//	    this.notes=(Boolean)record.get(Notes);	   
	}

	public int getIdBookletConf() {
		return idBookletConf;
	}

	public void setIdBookletConf(int idBookletConf) {
		this.idBookletConf = idBookletConf;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public boolean isSiteinfo() {
		return siteinfo;
	}

	public void setSiteinfo(boolean siteinfo) {
		this.siteinfo = siteinfo;
	}

	public boolean isSiteconf() {
		return siteconf;
	}

	public void setSiteconf(boolean siteconf) {
		this.siteconf = siteconf;
	}

	public boolean isSchedash() {
		return schedash;
	}

	public void setSchedash(boolean schedash) {
		this.schedash = schedash;
	}

	public boolean isUserconf() {
		return userconf;
	}

	public void setUserconf(boolean userconf) {
		this.userconf = userconf;
	}

	public boolean isActivealarm() {
		return activealarm;
	}

	public void setActivealarm(boolean activealarm) {
		this.activealarm = activealarm;
	}

	public boolean isPlugmodule() {
		return plugmodule;
	}

	public void setPlugmodule(boolean plugmodule) {
		this.plugmodule = plugmodule;
	}

	public boolean isNotes() {
		return notes;
	}

	public void setNotes(boolean notes) {
		this.notes = notes;
	}

	public int getIdsite() {
		return idsite;
	}

	public void setIdsite(int idsite) {
		this.idsite = idsite;
	}

	public boolean isDevparam() {
		return devparam;
	}

	public void setDevparam(boolean devparam) {
		this.devparam = devparam;
	}
}
