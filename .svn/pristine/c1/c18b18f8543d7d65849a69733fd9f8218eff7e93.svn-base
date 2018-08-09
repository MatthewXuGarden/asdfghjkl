package com.carel.supervisor.plugin.parameters.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.carel.supervisor.base.dump.DumpWriter;
import com.carel.supervisor.base.dump.DumperMgr;
import com.carel.supervisor.base.dump.IDumpable;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class ParametersPhotoRow implements IDumpable {
	public static final String IDSITE = "idsite";
	public static final String IDVARIABLE = "idvariable";
	public static final String PHOTOVALUE = "photovalue";

	public static final String IDDEVICE = "iddevice";
	public static final String DEV_DESCR = "dev_descr";
	public static final String VAR_DESCR = "var_descr";

	private Integer idsite;
	private Integer idvariable;
	private Float photovalue;
	private Integer iddevice;
	private String dev_descr;
	private String var_descr;
	
	
	public ParametersPhotoRow(){
		
	}
	
	public ParametersPhotoRow(Record record){
		idsite = (Integer) record.get(IDSITE);
		idvariable = (Integer) record.get(IDVARIABLE);
		photovalue = (Float) record.get(PHOTOVALUE);
		
		iddevice = (Integer) record.get(IDDEVICE);
		dev_descr = ((String) record.get(DEV_DESCR)).trim();
		var_descr = ((String) record.get(VAR_DESCR)).trim();
	}
	
	public ParametersPhotoRow(ResultSet rs) throws SQLException{
		idsite = (Integer) rs.getInt(IDSITE);
		idvariable = (Integer) rs.getInt(IDVARIABLE);
		photovalue = (Float) rs.getFloat(PHOTOVALUE);
		
		iddevice = (Integer) rs.getInt(IDDEVICE);
		dev_descr = ((String) rs.getString(DEV_DESCR)).trim();
		var_descr = ((String) rs.getString(VAR_DESCR)).trim();
	}
	
	public ParametersPhotoRow(String language, int id){
    	String sql = "select parameters_variable.*,cfdevice.iddevice, a_desc.description as dev_descr,b_desc.description as var_descr " +
		" from parameters_variable " +
		" inner join cfvariable on parameters_variable.idvariable = cfvariable.idvariable " +
		" inner join cfdevice on cfvariable.iddevice = cfdevice.iddevice " +
		
		" inner join cftableext a_desc on a_desc.tableid = cfdevice.iddevice "+
	    "   and a_desc.tablename='cfdevice' "+
	    "   and a_desc.languagecode='"+language+"' "+
	       
		" inner join cftableext b_desc on b_desc.tableid = cfvariable.idvariable "+
	    "   and b_desc.tablename='cfvariable' "+
	    "   and b_desc.languagecode='"+language+"' "+
	    " where id = ? "; 

		Object[] o = new Object[1];
		o[0]= new Integer(id);
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,o);
			if (rs.size()>0){
				Record record = rs.get(0);
				idsite = (Integer) record.get(IDSITE);
				idvariable = (Integer) record.get(IDVARIABLE);
				photovalue = (Float) record.get(PHOTOVALUE);
				
				iddevice = (Integer) record.get(IDDEVICE);
				dev_descr = ((String) record.get(DEV_DESCR)).trim();
				var_descr = ((String) record.get(VAR_DESCR)).trim();
			}
			
		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
	}

	public ParametersPhotoRow(Integer idsite,	Integer idvariable, Float photovalue){
		
		this.idsite=idsite;
		this.idvariable=idvariable;
		this.photovalue=photovalue;
	}
	


	public DumpWriter getDumpWriter() {
		DumpWriter dumpWriter = DumperMgr.createDumpWriter("[BEAN]", this);
        dumpWriter.print(IDSITE, idsite);
        dumpWriter.print(IDVARIABLE, idvariable);
        dumpWriter.print(PHOTOVALUE, photovalue);
		return dumpWriter;
	}


	public Integer getIdsite() {
		return idsite;
	}


	public void setIdsite(Integer idsite) {
		this.idsite = idsite;
	}


	public Integer getIdvariable() {
		return idvariable;
	}


	public void setIdvariable(Integer idvariable) {
		this.idvariable = idvariable;
	}

	public Integer getIddevice() {
		return iddevice;
	}

	public void setIddevice(Integer iddevice) {
		this.iddevice = iddevice;
	}

	public String getDev_descr() {
		return dev_descr;
	}

	public void setDev_descr(String dev_descr) {
		this.dev_descr = dev_descr;
	}

	public String getVar_descr() {
		return var_descr;
	}

	public void setVar_descr(String var_descr) {
		this.var_descr = var_descr;
	}

	public void setPhotovalue(Float photovalue) {
		this.photovalue = photovalue;
	}

	public Float getPhotovalue() {
		return photovalue;
	}

}
