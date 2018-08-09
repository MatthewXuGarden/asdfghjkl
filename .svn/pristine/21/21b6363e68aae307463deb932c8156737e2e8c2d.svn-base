package com.carel.supervisor.director.ac;

import com.carel.supervisor.dataaccess.db.Record;

public class AcConfigBean
{
	private static final String ID_DEV_MASTER = "iddevmaster";
	private static final String ID_VAR_MASTER = "idvarmaster";
	private static final String ID_DEV_SLAVE = "iddevslave";
	private static final String ID_VAR_SLAVE = "idvarslave";
	private static final String DEFAULT_VALUE = "def";
	private static final String MIN_VALUE = "min";
	private static final String MAX_VALUE = "max";
	private static final String ID_VAR_ALARM = "idvaralarm";
	
	private Integer iddevmaster = null;
	private Integer idvarmaster = null;
	private Integer iddevslave = null;
	private Integer idvarslave = null;
	private Float def = null;
	private Float min = null;
	private Float max = null;
	private Integer idalarm = null;
	
	public AcConfigBean(Record r)
	{
		this.iddevmaster = (Integer) r.get(ID_DEV_MASTER);
		this.idvarmaster = (Integer) r.get(ID_VAR_MASTER);
		this.iddevslave = (Integer) r.get(ID_DEV_SLAVE);
		this.idvarslave = (Integer) r.get(ID_VAR_SLAVE);
		this.def = (Float) r.get(DEFAULT_VALUE);
		this.min = (Float) r.get(MIN_VALUE);
		this.max = (Float) r.get(MAX_VALUE);
		this.idalarm = (Integer) r.get(ID_VAR_ALARM);
	}

	public Float getDef() {
		return def;
	}

	public void setDef(Float def) {
		this.def = def;
	}

	public Integer getIdalarm() {
		return idalarm;
	}

	public void setIdalarm(Integer idalarm) {
		this.idalarm = idalarm;
	}

	public Integer getIddevmaster() {
		return iddevmaster;
	}

	public void setIddevmaster(Integer iddevmaster) {
		this.iddevmaster = iddevmaster;
	}

	public Integer getIddevslave() {
		return iddevslave;
	}

	public void setIddevslave(Integer iddevslave) {
		this.iddevslave = iddevslave;
	}

	public Integer getIdvarmaster() {
		return idvarmaster;
	}

	public void setIdvarmaster(Integer idvarmaster) {
		this.idvarmaster = idvarmaster;
	}

	public Integer getIdvarslave() {
		return idvarslave;
	}

	public void setIdvarslave(Integer idvarslave) {
		this.idvarslave = idvarslave;
	}

	public Float getMax() {
		return max;
	}

	public void setMax(Float max) {
		this.max = max;
	}

	public Float getMin() {
		return min;
	}

	public void setMin(Float min) {
		this.min = min;
	}
	
	
}
