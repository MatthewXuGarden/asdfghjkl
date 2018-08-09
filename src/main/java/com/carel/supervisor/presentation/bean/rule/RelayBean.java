package com.carel.supervisor.presentation.bean.rule;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.Record;


public class RelayBean
{
    private static final String ID_RELAY = "idrelay";
    private static final String ID_SITE = "idsite";
    private static final String ID_VARIABLE = "idvariable";
    private static final String RESET_TYPE = "resettype";
    private static final String RESET_TIME = "resettime";
    private static final String ACTIVE_STATE = "activestate";
    private static final String LAST_UPDATE = "lastupdate";
    private static final String IOTESTSTATUS = "ioteststatus";
    private static final String DESC = "description";
    private static final String DEVDESC = "device";
    private static final String SHOW = "show";
    
    private int idrelay = -1;
    private int idsite = -1;
    private int idvariable = -1;
    private String resettype = "M";
    private int resettime = -1;
    private int activestate = 1;
    private Timestamp lastupdate = null;
    private String description = "";
    private String devdescription = "";
    private String ioteststatus = "";
    private boolean show = true;

    public RelayBean(Record record) throws DataBaseException
    {
        this.idrelay = ((Integer) record.get(ID_RELAY)).intValue();
        this.idsite = ((Integer) record.get(ID_SITE)).intValue();
        this.idvariable = ((Integer) record.get(ID_VARIABLE)).intValue();
        this.resettype = UtilBean.trim(record.get(RESET_TYPE));
        this.resettime = ((Integer) record.get(RESET_TIME)).intValue();
        this.activestate = ((Integer) record.get(ACTIVE_STATE)).intValue();
        this.lastupdate = (Timestamp) record.get(LAST_UPDATE);
        this.ioteststatus = UtilBean.trim(record.get(IOTESTSTATUS));
        this.description = UtilBean.trim(record.get(DESC));
        this.show = ((Boolean) record.get(SHOW)); 
        
        try{
        	this.devdescription = UtilBean.trim(record.get(DEVDESC));
        }
        catch(Exception e)
        {
        	this.devdescription = "";
        }
    }
    
    public String getDeviceDesc() {
    	return this.devdescription;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdrelay() {
        return this.idrelay;
    }

    public void setIdrelay(int idrelay) {
        this.idrelay = idrelay;
    }

    public int getIdsite() {
        return this.idsite;
    }

    public void setIdsite(int idsite) {
        this.idsite = idsite;
    }

    public int getIdvariable(){
        return this.idvariable;
    }

    public void setIdvariable(int idvariable) {
        this.idvariable = idvariable;
    }

    public Timestamp getLastupdate() {
        return this.lastupdate;
    }

    public void setLastupdate(Timestamp lastupdate) {
        this.lastupdate = lastupdate;
    }

	public int getActivestate() {
		return this.activestate;
	}

	public void setActivestate(int activestate) {
		this.activestate = activestate;
	}

	public int getResettime() {
		return this.resettime;
	}

	public void setResettime(int resettime) {
		this.resettime = resettime;
	}

	public String getResettype() {
		return this.resettype;
	}

	public void setResettype(String resettype) {
		this.resettype = resettype;
	}
	public String getIoteststatus()
	{
		return this.ioteststatus;
	}
	
	public boolean getShow()
	{
		return this.show;
	}
}
