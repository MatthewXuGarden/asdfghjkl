package com.carel.supervisor.ide.dc.DbModel;

public class VariableModBusInformationDb {

	private float aValue;
	private float bValue;
	private int varEncoding;
	
	public void fillInformations(float a, float b, int encoding)
	{
		aValue = a;
		bValue = b;
		varEncoding = encoding;
	}

	public float getAValue() {
		return aValue;
	}

	public void setAValue(float value) {
		aValue = value;
	}

	public float getBValue() {
		return bValue;
	}

	public void setBValue(float vlaue) {
		bValue = vlaue;
	}
	
	public int getVarEncoding() {
		return varEncoding;
	}
	
	public void setVarEncoding(int encoding) {
		varEncoding = encoding;
	}

}
