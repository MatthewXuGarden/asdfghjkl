package com.carel.supervisor.controller.setfield;

import com.carel.supervisor.field.Variable;

public class SetWrp 
{
	private Variable var = null;
	private float newValue = Float.NaN;
	private int code = 0;
	private boolean checkChangeValue = true;
	private Object objectForCallBack = null;
	
	public SetWrp(Variable var, float newValue) 
	{
		this.var = var;
		this.newValue = newValue;
	}

	public SetWrp(Variable var, float newValue, Object objectForCallBack) 
	{
		this.var = var;
		this.newValue = newValue;
		this.objectForCallBack = objectForCallBack;
	}
	
	
	/**
	 * @return Returns the objectForCallBack.
	 */
	public Object getObjectForCallBack() 
	{
		return objectForCallBack;
	}

	/**
	 * @return Returns the code.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(int code) 
	{
		this.code = code;
	}

	/**
	 * @return Returns the oldValue.
	 */
	public float getNewValue() 
	{
		return newValue;
	}

	public void setNewValue(float value) {
		newValue = value;
	}

	/**
	 * @return Returns the var.
	 */
	public Variable getVar() 
	{
		return var;
	}

	/**
	 * @return Returns the checkChangeValue.
	 */
	public boolean isCheckChangeValue() 
	{
		return checkChangeValue;
	}

	/**
	 * @param checkChangeValue The checkChangeValue to set.
	 */
	public void setCheckChangeValue(boolean checkChangeValue) 
	{
		this.checkChangeValue = checkChangeValue;
	}

	
}
