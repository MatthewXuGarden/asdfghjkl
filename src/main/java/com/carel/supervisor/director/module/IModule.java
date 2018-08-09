package com.carel.supervisor.director.module;

public interface IModule 
{
	public void setName(String name);
	public String getName();
	public boolean hookModule(boolean state);
}
