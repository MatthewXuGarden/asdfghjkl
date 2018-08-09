package com.carel.supervisor.field.modbusfunmgrs;

import com.carel.supervisor.field.Variable;

public interface IFunctionMgr
{
	public float applyFunction(long value, Variable v);

	public float applyInverseFunction(Variable v);
}