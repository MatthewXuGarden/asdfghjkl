package com.carel.supervisor.director.bms;

import com.carel.supervisor.director.module.IModule;

public class BmsHook implements IModule {

	private String name = "";
	public boolean hookModule(boolean state) {
		if (state) {
			return BmsMgr.getInstance().load();
		} else {
			return BmsMgr.getInstance().unload();
		}
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
