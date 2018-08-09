package com.carel.supervisor.presentation.fs;

import com.carel.supervisor.presentation.bean.DeviceBean;

public class FSUtilAux {
	
	private DeviceBean util;
	private String solenoid;
	
	public FSUtilAux(DeviceBean util, String solenoid)
	{
		this.util = util;
		this.solenoid = solenoid;
	}

	public String getSolenoid() {
		return solenoid;
	}

	public void setSolenoid(String solenoid) {
		this.solenoid = solenoid;
	}

	public DeviceBean getUtil() {
		return util;
	}

	public void setUtil(DeviceBean util) {
		this.util = util;
	}
	
	
}

