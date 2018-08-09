package com.carel.supervisor.script.special;

import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;

public class Special 
{
	public Special() {
	}
	
	public boolean display(SContext ctx,boolean initial)
	{
		return initial;
	}
	
	public VarphyBean[] check(SContext ctx) {
		return new VarphyBean[]{ctx.getVariable()}; 
	}
	
	public String value(SContext ctx){
		return "";
	}
}
