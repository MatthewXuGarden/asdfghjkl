package com.carel.supervisor.controller.setfield;

import java.util.ArrayList;
import java.util.List;

public class NotificationEvent 
{
	private String username = null;
	private List messages = new ArrayList();
	
	public NotificationEvent(String username) 
	{
		this.username = username;
	}
	
	public void add(String msg)
	{
		messages.add(msg);
	}
	
	public boolean checkUsername(String username)
	{
		return this.username.equals(username);
	}
	
	public String retrieve()
	{
		if (messages.size() > 0)
		{
			String s = (String)messages.remove(messages.size() - 1);
			return s;
		}
		return null;
	}
}
