package com.carel.supervisor.presentation.events;

public class HsParam {
	private String deviceDesc;
	private String variableDesc;
	private Integer code;
	private String codeDesc;
	private Double oldValue;
	private Double newValue;
	private Integer idvarmdl;
	
	public String getDeviceDesc() {
		return deviceDesc;
	}
	public void setDeviceDesc(String deviceDesc) {
		this.deviceDesc = deviceDesc;
	}
	public String getVariableDesc() {
		return variableDesc;
	}
	public void setVariableDesc(String variableDesc) {
		this.variableDesc = variableDesc;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getCodeDesc() {
		return codeDesc;
	}
	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	public Double getOldValue() {
		return oldValue;
	}
	public void setOldValue(Double oldValue) {
		this.oldValue = oldValue;
	}
	public Double getNewValue() {
		return newValue;
	}
	public void setNewValue(Double newValue) {
		this.newValue = newValue;
	}
	
	public Integer getIdVarMdl() {
		return idvarmdl;
	}
	public void setIdVarMdl(Integer idvarmdl) {
		this.idvarmdl = idvarmdl;
	}
	
}
