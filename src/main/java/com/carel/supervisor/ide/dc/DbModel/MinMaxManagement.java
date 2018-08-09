package com.carel.supervisor.ide.dc.DbModel;

public class MinMaxManagement {

	private String codeMax;
	private String codeMin;
	
	public MinMaxManagement()
	{
		codeMax = "";
		codeMin = "";
	}
	
	public MinMaxManagement(String codeMax, String codeMin)
	{
		this.codeMax = codeMax;
		this.codeMin = codeMin;
	}

	public String getCodeMax() {
		return codeMax;
	}

	public void setCodeMax(String codeMax) {
		this.codeMax = codeMax;
	}

	public String getCodeMin() {
		return codeMin;
	}

	public void setCodeMin(String codeMin) {
		this.codeMin = codeMin;
	}
}
