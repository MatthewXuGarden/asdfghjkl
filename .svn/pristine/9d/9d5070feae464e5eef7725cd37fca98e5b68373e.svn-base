package com.carel.supervisor.dataaccess.datalog.impl;

import java.sql.Timestamp;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ReportBean {
	private static final String IDREPORT = "idreport";
	private static final String IDSITE = "idsite";
	private static final String CODE = "code";
	private static final String HISTORY = "history";
	private static final String HACCP = "haccp";
	private static final String STEP = "step";
	private static final String TIMELENGTH = "timelength";
	private static final String OUTPUTTYPE = "outputtype";
	private static final String TEMPLATECLASS = "templateclass";
	private static final String TEMPLATEFILE = "templatefile";
	private static final String LASTUPDATE = "lastupdate";

	private Integer idreport;
	private Integer idsite;
	private String code;
	private Boolean history;
	private Boolean haccp;
	private Integer step;
	private Integer timelength;
	private String outputtype;
	private String templateclass;
	private String templatefile;
	private Timestamp lastupdate;
	private Integer[] variables;
	private Integer[] vars_mdl;
	private Integer[] timeValues;

	public ReportBean(Record r) {
		this.idreport = (Integer) r.get(IDREPORT);
		this.idsite = (Integer) r.get(IDSITE);
		this.code = UtilBean.trim(r.get(CODE));
		this.history = (Boolean) r.get(HISTORY);
		this.haccp = (Boolean) r.get(HACCP);
		this.step = (Integer) r.get(STEP);
		this.timelength = (Integer) r.get(TIMELENGTH);
		this.outputtype = UtilBean.trim(r.get(OUTPUTTYPE));
		this.templateclass = UtilBean.trim(r.get(TEMPLATECLASS));
		this.templatefile = UtilBean.trim(r.get(TEMPLATEFILE));
		this.lastupdate = (Timestamp) r.get(LASTUPDATE);
		loadDetails();
		this.setTimeValues(loadTimeValues());
	}
	
	private void loadDetails() {
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select cfvariable.idvariable,idvarmdl from cfreportdetail,cfvariable where idreport="+getIdreport()+" and " +
					"cfvariable.idvariable=cfreportdetail.idvariable and iscancelled='FALSE'" + " order by idrow");
			
			Integer[] ret = new Integer[rs.size()];
			Integer[] mdl = new Integer[rs.size()];
			
			if(rs!=null){
				
				for(int i = 0; i< rs.size(); i++){
					ret[i] = (Integer)rs.get(i).get(0);
					mdl[i] = (Integer)rs.get(i).get(1);
				}
				
			}
			this.variables = ret;
			this.vars_mdl = mdl;
			
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}

	private Integer[] loadTimeValues() {
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,
					"select timeval from cfreporttime where idreport="
					+ getIdreport()
					+ " order by timeval");
			if(rs!=null){
				Integer[] ret = new Integer[rs.size()];
				for(int i = 0; i< rs.size(); i++){
					ret[i] = (Integer)rs.get(i).get(0);
				}
				return ret;
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		return null;
	}
	
	public ReportBean(Record r, Integer[] vars) {
		this(r);
		this.setVariables(vars);
	}

	public Integer getIdreport() {
		return idreport;
	}
	
	public void setIdreport(Integer idreport) {
		this.idreport = idreport;
	}
	
	public Integer getIdsite() {
		return idsite;
	}
	
	public void setIdsite(Integer idsite) {
		this.idsite = idsite;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public Boolean getHistory() {
		return history;
	}
	
	public void setHistory(Boolean history) {
		this.history = history;
	}
	
	public Boolean getHaccp() {
		return haccp;
	}
	
	public void setHaccp(Boolean haccp) {
		this.haccp = haccp;
	}
	
	public Integer getStep() {
		return step;
	}
	
	public void setStep(Integer step) {
		this.step = step;
	}
	
	public Integer getTimelength() {
		return timelength;
	}

	public void setTimelength(Integer timelength) {
		this.timelength = timelength;
	}

	public String getOutputtype() {
		return outputtype;
	}

	public void setOutputtype(String outputtype) {
		this.outputtype = outputtype;
	}

	public String getTemplateclass() {
		return templateclass;
	}
	
	public void setTemplateclass(String templateclass) {
		this.templateclass = templateclass;
	}
	
	public String getTemplatefile() {
		return templatefile;
	}
	
	public void setTemplatefile(String templatefile) {
		this.templatefile = templatefile;
	}
	
	public Timestamp getLastupdate() {
		return lastupdate;
	}
	
	public void setLastupdate(Timestamp lastupdate) {
		this.lastupdate = lastupdate;
	}

	public void setVariables(Integer[] variables) {
		this.variables = variables;
	}

	public Integer[] getVariables() {
		return variables;
	}
	
	public Integer[] getVarsMdl() {
		return vars_mdl;
	}
	
	public void setTimeValues(Integer[] values) {
		timeValues = values;
	}

	public Integer[] getTimeValues() {
		return timeValues;
	}
	
}

