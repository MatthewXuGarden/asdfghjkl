package com.carel.supervisor.script;

import com.carel.supervisor.script.operator.AndOperator;
import com.carel.supervisor.script.operator.IOperator;
import com.carel.supervisor.script.operator.OrOperator;

public class FactoryOperator 
{
	public static IOperator getOperator(String operator)
	{
		if (operator.equals("&"))
		{
			return AndOperator.getInstance();
		}
		else if (operator.equals("|"))
		{
			return OrOperator.getInstance();
		}
		else
		{
			return null;
		}
	}
}
