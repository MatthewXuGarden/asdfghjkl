package com.carel.supervisor.script.comparator;

public class LessEqualComparator implements IComparator
{
	private static IComparator instance = new LessEqualComparator();
	private LessEqualComparator(){}
	
	public static IComparator getInstance() {
		return instance;
	}
	
	public boolean compare(float value1,float value2) {
		return (value1 <= value2);
	}
}