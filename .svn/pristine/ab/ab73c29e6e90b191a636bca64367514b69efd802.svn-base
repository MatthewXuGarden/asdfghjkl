package com.carel.supervisor.controller.setfield;

import java.util.HashMap;
import java.util.Map;

public class NotificationParam 
{
	private static NotificationParam me = new NotificationParam();
	private Map notificationList = new HashMap();
	
	private NotificationParam() 
	{
	}
	
	public static NotificationParam getInstance()
	{
		return me;
	}
	
	public NotificationEvent register(String username)
	{
		NotificationEvent event = (NotificationEvent)notificationList.get(username);
		if (null == event)
		{
			event = new NotificationEvent(username);
			notificationList.put(username, event);	
		}
		return event;
	}
	
	public void addMsg(String username, String message)
	{
		NotificationEvent event = (NotificationEvent)notificationList.get(username);
		event.add(message);
	}

	public String retrieve(String username)
	{
		NotificationEvent tmp = (NotificationEvent)notificationList.get(username);
		if (null != tmp)
		{
			return tmp.retrieve();
		}
		else
		{
			return null;
		}
	}
}
