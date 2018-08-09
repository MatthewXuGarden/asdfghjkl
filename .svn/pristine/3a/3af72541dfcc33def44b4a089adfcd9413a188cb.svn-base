package com.carel.supervisor.script.comparator;

public class LessComparator implements IComparator
{
	private static IComparator instance = new LessComparator();
	private LessComparator(){}
	
	public static IComparator getInstance() {
		return instance;
	}
	
	public boolean compare(float value1,float value2) {
		return (value1 < value2);
	}
}
