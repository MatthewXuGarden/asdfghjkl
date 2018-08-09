package com.carel.supervisor.presentation.polling;


import java.util.Properties;

import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.plugin.base.Plugin;



public class PollingPluginImpl implements Plugin{

	private Properties properties;
	
	public PollingPluginImpl(Properties properties)
	{
	        this.properties = properties;
	}
	
	public PollingPluginImpl()
	{
		
	}
	
	
	public Properties getProperties() {
		return properties;
	}

	public int install() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void startPlugin() {
		// TODO Auto-generated method stub
		ThreadController con = ThreadController.getInstance();
		con.startThread();
		EventMgr.getInstance().info(new Integer(1), "System", "Polling",
                "POL01", null);
	}

	public void stopPlugin() {
		// TODO Auto-generated method stub
		
	}
}
