package com.carel.supervisor.script;

import com.carel.supervisor.script.comparator.EqualComparator;
import com.carel.supervisor.script.comparator.GreaterComparator;
import com.carel.supervisor.script.comparator.GreaterEqualComparator;
import com.carel.supervisor.script.comparator.IComparator;
import com.carel.supervisor.script.comparator.LessComparator;
import com.carel.supervisor.script.comparator.LessEqualComparator;
import com.carel.supervisor.script.comparator.NotEqualComparator;

public class FactoryComparator
{
	public static IComparator createComparator(String comparator)
	{
		if (comparator.equals(">"))
		{
			return GreaterComparator.getInstance();
		}
		else if (comparator.equals(">="))
		{
			return GreaterEqualComparator.getInstance();
		}
		else if (comparator.equals("<"))
		{
			return LessComparator.getInstance();
		}
		else if (comparator.equals("<="))
		{
			return LessEqualComparator.getInstance();
		}
		else if (comparator.equals("="))
		{
			return EqualComparator.getInstance();
		}
		else if (comparator.equals("!"))
		{
			return NotEqualComparator.getInstance();
		}
		else
		{
			return null;
		}
	}
}
