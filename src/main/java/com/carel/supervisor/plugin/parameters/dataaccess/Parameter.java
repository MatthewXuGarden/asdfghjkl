package com.carel.supervisor.plugin.parameters.dataaccess;

import java.sql.ResultSet;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.dataaccess.db.Record;

public class Parameter implements IDumpable {
	public static final String IDSITE = "idsite";
	public static final String IDDEVICE = "iddevice";
	public static final String IDVARIABLE = "idvariable";
	public static final String VARIABLEDESCRIPTION = "variabledescription";
	public static final String DEVICEDESCRIPTION = "devicedescription";
	
	private Integer idsite = null;
	private Integer iddevice = null;
	private Integer idvariable = null;
	private String variabledescription = null;
	private String devicedescription = null;
	
	public Parameter(){}
	
	public Parameter(ResultSet rs) throws Exception
	{
		idsite = (Integer) rs.getInt(IDSITE);
		iddevice = (Integer) rs.getInt(IDDEVICE);
		idvariable = (Integer) rs.getInt(IDVARIABLE);
		variabledescription = (String) rs.getString(VARIABLEDESCRIPTION);
		devicedescription = (String) rs.getString(DEVICEDESCRIPTION);
		
	}
	
	public Parameter(Record record) throws Exception
	{
		idsite = (Integer) record.get(IDSITE);
		iddevice = (Integer) record.get(IDDEVICE);
		idvariable = (Integer) record.get(IDVARIABLE);
		variabledescription = (String) record.get(VARIABLEDESCRIPTION);
		devicedescription = (String) record.get(DEVICEDESCRIPTION);
	}
	
	public DumpWriter getDumpWriter() {
        DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);
        return dumpWriter;		
	}

	public Integer getIdsite() {
		return idsite;
	}

	public void setIdsite(Integer idsite) {
		this.idsite = idsite;
	}

	public Integer getIddevice() {
		return iddevice;
	}

	public void setIddevice(Integer iddevice) {
		this.iddevice = iddevice;
	}

	public Integer getIdvariable() {
		return idvariable;
	}

	public void setIdvariable(Integer idvariable) {
		this.idvariable = idvariable;
	}

	public String getVariabledescription() {
		return variabledescription;
	}

	public void setVariabledescription(String variabledescription) {
		this.variabledescription = variabledescription;
	}

	public String getDevicedescription() {
		return devicedescription;
	}

	public void setDevicedescription(String devicedescription) {
		this.devicedescription = devicedescription;
	}

	public String toString(){
		return devicedescription+" - "+variabledescription;
	}
}
