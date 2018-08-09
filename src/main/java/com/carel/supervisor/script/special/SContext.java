package com.carel.supervisor.script.special;

import java.util.Map;

import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;

public class SContext 
{
	private Map remapVariable = null;
	private VarphyBean variable = null;
	private int idDevice = -1;
	
	public SContext() {
	}
	
	public void setIdDevice(int id) {
		this.idDevice = id;
	}
	
	public int getIdDevice() {
		return this.idDevice;
	}
	
	public void setVariable(VarphyBean vb) {
		this.variable = vb;
	}
	
	public VarphyBean getVariable() {
		return this.variable;
	}
	
	public void setVariableMap(Map map) {
		this.remapVariable = map;
	}
	
	public int decodeVarMdl(int varmdl) {
		Integer iret = null;
		if(this.remapVariable != null)
		{
			iret = (Integer)this.remapVariable.get(new Integer(varmdl));
			if(iret != null)
				return iret.intValue();
			else
				return -1;
		}
		else
			return -1;
	}
}
