package com.carel.supervisor.script.operator;

public class OrOperator implements IOperator
{
	private static IOperator instance = new OrOperator();
	private OrOperator(){}
	
	public static IOperator getInstance() {
		return instance;
	}
	
	public boolean compare(boolean b1, boolean b2) {
		return (b1 || b2);
	}
}
