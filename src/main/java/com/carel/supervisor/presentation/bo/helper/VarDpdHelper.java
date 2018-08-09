package com.carel.supervisor.presentation.bo.helper;

public class VarDpdHelper {
	int varId;
	int devId;
	String dpdName ;
	String dpdType;
	
	public VarDpdHelper(int varId, String dpdName, String dpdType) {
		//usually ,variable dependency do not use iddev. 
		this(-1,varId,dpdName,dpdType); 
	}
	
	public  VarDpdHelper(int devId, int varId, String dpdName,String dpdType) {
		// iddev just for some dependencies use device information and do not use variables. such as booklet cabinet.
		this.devId = devId;
		this.varId = varId;
		this.dpdName = dpdName;
		this.dpdType = dpdType;
	}

	public int getVarId() {
		return varId;
	}

	public void setDevId(int varId) {
		this.varId = varId;
	}
	
	public int getDevId() {
		return devId;
	}

	public void setVarId(int devId) {
		this.devId = devId;
	}

	public String getDpdName() {
		return dpdName;
	}

	public void setDpdName(String dpdName) {
		this.dpdName = dpdName;
	}

	public String getDpdType() {
		return dpdType;
	}

	public void setDpdType(String dpdType) {
		this.dpdType = dpdType;
	}
	
	

}
