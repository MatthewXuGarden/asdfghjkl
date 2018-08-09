package com.carel.supervisor.field.modbusfunmgrs;

import com.carel.supervisor.field.Variable;

public class IdentityFunctionMgr implements IFunctionMgr
{
	private static IFunctionMgr me;
	
	private IdentityFunctionMgr()
	{
		
	}
	
	public static IFunctionMgr getInstance()
	{
		if(me==null)
			me = new IdentityFunctionMgr();
		return me;
	}
	
	/* (non-Javadoc)
	 * @see com.carel.supervisor.field.modbusfunmgrs.IFunctionMgr#applyFunction(com.carel.supervisor.field.Variable, float)
	 */
	public float applyFunction(long value, Variable v)
	{
		return new Float(value);
	}

    public float applyInverseFunction(Variable v)
    {
    	return v.getCurrentValue();
    }    
}
