package com.carel.supervisor.dispatcher.comm.field;

import java.util.Vector;

public class CommConfig implements ICommConfig {
	private Vector DeviceList = new Vector();
	public Vector getCommunicationConfig() {
		// TODO Auto-generated method stub
		return DeviceList;
	}

	public void addCommunicationConfig(String device) {
		// TODO Auto-generated method stub
		DeviceList.add(device);
	}

}
