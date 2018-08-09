package com.carel.supervisor.director.bms;


public class BmsMgr {

	private static BmsMgr instance = new BmsMgr();
	private BMSConfiguration config;

	
	private void setConfig(BMSConfiguration config) {
		this.config = config;
	}

	public BMSConfiguration getConfig() {
		return config;
	}

	public static BmsMgr getInstance() {
		return instance;
	}

	public boolean load() {
		setConfig(new BMSConfiguration());
		return config.isLoaded();
	}

	public boolean unload() {
		config = null;
		return true;
	}
}
