package com.carel.supervisor.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.script.operator.IOperator;

public class Expression 
{
	private List members = new ArrayList();
	private List operator = new ArrayList();
	
	public Expression(String expression)
	{
		String[] vals = new String[0];
		boolean flag = true;
		
		if(expression != null)
			vals = StringUtility.split(expression," ");
		
		// Il primo è sempre AND
		this.operator.add(FactoryOperator.getOperator("&"));
		
		if(vals != null)
		{
			for(int i=0; i<vals.length; i++)
			{
				if(flag)
				{
					this.members.add(new Member(vals[i]));
					flag = false;
				}
				else
				{
					this.operator.add(FactoryOperator.getOperator(vals[i]));
					flag = true;
				}
			}
		}
	}
	
	public void retrieveIdVariable(Map varMap)
	{
		for(int i=0; i<this.members.size(); i++)
			((Member)this.members.get(i)).retrieveIdVariable(varMap);
	}
	
	public Map getIdVarmdl()
	{
		Map vars = new HashMap();
		Integer idvarmdl = null;
		Member tmp = null;
		
		for(int i=0; i<members.size(); i++)
		{
			tmp = (Member)members.get(i);
			//if(tmp.isValid())
			//{
				idvarmdl = new Integer(tmp.getIdVarmdl());
				vars.put(idvarmdl,idvarmdl);
			//}
		}
		return vars;
	}
	
	public boolean evaluate()
	{
		boolean result = true;
		for(int i=0; i<members.size(); i++)
			result = ((IOperator)operator.get(i)).compare(result,((Member)members.get(i)).evaluate());
		
		return result;
	}
}

