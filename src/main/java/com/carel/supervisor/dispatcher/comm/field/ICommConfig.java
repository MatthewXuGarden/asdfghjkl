package com.carel.supervisor.dispatcher.comm.field;

import java.util.Vector;

public interface ICommConfig {
	public Vector getCommunicationConfig();
	public void addCommunicationConfig(String device);
	
}
