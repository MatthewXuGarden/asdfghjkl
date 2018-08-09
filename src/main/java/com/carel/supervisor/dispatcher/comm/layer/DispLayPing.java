package com.carel.supervisor.dispatcher.comm.layer;

import com.carel.supervisor.dispatcher.comm.field.impl.CommBase;
import com.carel.supervisor.dispatcher.comm.field.impl.CommID;
import com.carel.supervisor.dispatcher.comm.field.impl.CommPingService;

public class DispLayPing extends DispLayer
{
	public DispLayPing() {
		super("I");
	}
	
	protected int getId() {
		return CommID.SERVICE_SMS;
	}
	
	protected int getInit() {
		return CommID.SERVICE_SMS_INIT_OK; 
	}
	
	protected int getPingService() {
		return CommID.SERVICE_SMS_PING; 
	}
	
	protected CommBase getImplementation() {
		return new CommPingService();
	}
	
	public boolean openChannel(String fisicDevice) {
		return false;
	}
}
