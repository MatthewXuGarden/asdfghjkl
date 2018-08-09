package com.carel.supervisor.plugin.energy;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class EnergyActiveDevice {
	
	private Integer idgrp;
	private Integer idvar;
	private Boolean inverse;
	private Boolean enabled;
	private Integer iddev;

	public EnergyActiveDevice(Integer idgrp, Integer iddev, Integer idvar, Boolean inverse, Boolean enabled){
		this.idgrp = idgrp;
		this.iddev = iddev;
		this.idvar = idvar;
		this.inverse = inverse;
		this.enabled = enabled;
	}

	public Integer getIdgrp() {
		return idgrp;
	}

	public Integer getIdvar() {
		return idvar;
	}

	public Integer getIddev() {
		return iddev;
	}

	public Boolean isInverse() {
		return inverse;
	}

	public Boolean isEnabled() {
		return enabled;
	}
	
	public Integer getValue(Boolean on){
		if(inverse)
			return (on)?0:1;
		else
			return (on)?1:0;
	}
	
	public String getDeviceDescription(String language) {
		RecordSet rs;
		try {
			rs = DatabaseMgr.getInstance().executeQuery(
					null,
					"select description from cftableext where idsite=1 and tablename='cfdevice'" + " and tableid="
							+ iddev + " and languagecode='" + language + "'");
			if (rs == null)
				return "";
			if (rs.size() > 0) {
				return UtilBean.trim(rs.get(0).get(0));
			} else {
				return "";
			}
		} catch (DataBaseException e) {
			return "";
		}

	}

	public String getVariableDescription(String language) {
		RecordSet rs;
		try {
			rs = DatabaseMgr.getInstance().executeQuery(
					null,
					"select description from cftableext where idsite=1 and tablename='cfvariable'" + " and tableid="
							+ idvar + " and languagecode='" + language + "'");
			if (rs == null)
				return "";
			if (rs.size() > 0) {
				return UtilBean.trim(rs.get(0).get(0));
			} else {
				return "";
			}
		} catch (DataBaseException e) {
			return "";
		}
	}
}
