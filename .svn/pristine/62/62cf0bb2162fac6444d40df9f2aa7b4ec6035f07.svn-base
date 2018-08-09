package com.carel.supervisor.plugin.fs;

import java.util.LinkedList;

public class FSCyclicList<E> extends LinkedList<E>
{
	private static final long serialVersionUID = -6329291736641411499L;
	private int window_dimension = -1;
	
	public FSCyclicList(int window_dimension)
	{
		this.window_dimension = window_dimension/FSManager.getPolling();
	}
	public boolean add(E o)
	{
		if (this.size()>=this.window_dimension)
		{
			this.remove();
		}
		return super.add(o);

	}
}
