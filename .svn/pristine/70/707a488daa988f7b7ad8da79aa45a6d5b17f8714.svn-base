package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.Record;

public class CustomVarphyBean extends VarphyBean 
{
	private String typeVis = "";
	
	public CustomVarphyBean(Record rec) 
	{
		super(rec);
		this.typeVis = UtilBean.trim(rec.get("typevis"));
	}
	
	public String getTypeVis() {
		return this.typeVis;
	}
	
	public void setTypeVis(String type) {
		this.typeVis = type;
	}
}
