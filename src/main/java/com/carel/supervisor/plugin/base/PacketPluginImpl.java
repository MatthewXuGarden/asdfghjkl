package com.carel.supervisor.plugin.base;

import java.util.Properties;

public class PacketPluginImpl implements Plugin 
{
	private Properties properties;
	
	public PacketPluginImpl(Properties properties) {
        this.properties = properties;
    }
	
	public Properties getProperties() {
		return properties;
	}

	public int install() {
		return 0;
	}

	public void startPlugin() {
	}

	public void stopPlugin() {
	}
}
