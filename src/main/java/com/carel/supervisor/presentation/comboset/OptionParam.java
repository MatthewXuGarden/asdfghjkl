package com.carel.supervisor.presentation.comboset;

public class OptionParam {

	private Float value = null;
	private String desc = null;
	
	public OptionParam(String desc,Float value)
	{
		this.value = value;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
	
	
}
