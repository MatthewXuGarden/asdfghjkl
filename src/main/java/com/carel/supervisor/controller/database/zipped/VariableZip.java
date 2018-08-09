package com.carel.supervisor.controller.database.zipped;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.field.Variable;

public class VariableZip extends VariableInfo
{
	private Variable objVar = null;
	private String newValue = null;
	
	public VariableZip(Record r)
	{
		super(r);
	}
	
	public void loadValue()
	{
		try {
			objVar = ControllerMgr.getInstance().getFromField(getId().intValue());
		}
		catch(Exception e) 
		{
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	public void loadValueOfDimension()
	{
		try {
			objVar = ControllerMgr.getInstance().getFromField(getId().intValue(),true);
		}
		catch(Exception e) 
		{
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	public float getRawValue() {
		return this.objVar.getCurrentValue();
	}
	
	public String getFormattedValue() {
		return this.objVar.getFormattedValue();
	}
	
	public String getNewValue() {
		return this.newValue;
	}
	
	public void setNewValue(String value) {
		this.newValue = value;
	}
}
