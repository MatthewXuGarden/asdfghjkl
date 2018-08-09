package com.carel.supervisor.director.vscheduler;

import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;


public class CVariable {
	private int idVar;
	private int nVarType;
	private int nFlags;
	
	
	public CVariable(int idVar, int nVarType, int nFlags)
	{
		this.idVar = idVar;
		this.nVarType = nVarType;
		this.nFlags = nFlags;
	}
	
	
	public int GetIdVar()
	{
		return idVar;
	}
	
	
	public int GetVarType()
	{
		return nVarType;
	}
	
	
	public int GetFlags()
	{
		return nFlags;
	}
	
	
	public boolean IsFlag(int nFlag)
	{
		return (nFlags & nFlag) == nFlag;
	}
	
	
	public boolean IsDigital()
	{
		return nVarType == VariableInfo.TYPE_DIGITAL;
	}
	
	
	public boolean IsAnalog()
	{
		return nVarType == VariableInfo.TYPE_ANALOGIC || nVarType == VariableInfo.TYPE_INTEGER;
	}
}
