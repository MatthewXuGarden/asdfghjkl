package com.carel.supervisor.dispatcher.comm.layer;

import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.comm.field.DeviceMessage;
import com.carel.supervisor.dispatcher.comm.field.DevicesMessages;
import com.carel.supervisor.dispatcher.comm.field.impl.CommBase;
import com.carel.supervisor.dispatcher.comm.field.impl.CommID;
import com.carel.supervisor.dispatcher.comm.field.impl.CommRasService;

public class DispLayRas extends DispLayer
{
	private CommBase localBase = null;
	
	private String user = "";
	private String pass = "";
	
	private String modemUsed = "";
	private String localIP = "";
	private String remoteIP = "";
	
	public DispLayRas() {
		super("R");
	}
	
	public DispLayRas(String l,String p) {
		super("R");
		this.user = l;
		this.pass = p;
		this.localBase = getImplementation();
	}
	
	protected int getId() {
		return CommID.SERVICE_REM;
	}
	
	protected int getInit() {
		return CommID.SERVICE_REM_INIT_OK; 
	}
	
	protected int getPingService() {
		return 0; 
	}
	
	protected CommBase getImplementation() {
		return new CommRasService();
	}

	public boolean openChannel(String fisicDevice) {
		return false;
	}
	
	public void initializeChannel(String[] fisicDevice)
	{
		DevicesMessages dms = getDevicesMessages();
		
		if(this.loadMainLibrary())
		{
			if(this.loadinitSubSystem(this.localBase))
			{
				if(this.localBase.setSubSystemMessages(dms).getReturnCode() >= 0)
				{
					for(int i=0; i<fisicDevice.length; i++) 
					{
						if(this.localBase.runDefaultCommand(fisicDevice[i]).getReturnCode() == 0)
						{
							try {
								EventMgr.getInstance().log(new Integer(1),"Remote","Action",new Integer(3),"R003",new Object[]{fisicDevice[i]});
							}
							catch(Exception e){}
						}
					}
				}
			}
		}
	}
	
	public int checkIncoming()
	{
		DevicesMessages msgFromC = new DevicesMessages();
		DeviceMessage message = new DeviceMessage();
		int state = 0;
		
		if(this.localBase.getSubSystemMessages(msgFromC).getReturnCode() == 0)
		{
			while(msgFromC.HasMore(message))
			{
				if(message != null && message.message_id==CommID.SERVICE_REM)
				{
					switch(message.message_sub)
					{
						case 3:
							state = 0;
							setInfo(message.message_s,message.message_sub);
							break;
						case 4:
							setInfo(message.message_s,message.message_sub);
							state = 0;
							break;
						case 5:
							state = 1;
							break;
						case 6:
							clearInfo();
							state = 2;
							break;
					}
				}
			}
		}
		return state;
	}
	
	public boolean configIncomingModem(String device)
	{
		boolean state = true;
		boolean ret = false;
		if(device != null)
		{
			DevicesMessages dms = getMessagesConf();
			
			if(this.localBase.setSubSystemMessages(dms).getReturnCode() >= 0)
			{
				if(this.localBase.runDefaultCommand(device).getReturnCode() == 0)
					ret = true;
			}
			
			if(ret)
			{
				DevicesMessages msgFromC = new DevicesMessages();
				DeviceMessage message = new DeviceMessage();
				int count = 10;
				
				while((count != 0) && (!state))
				{
					if(this.localBase.getSubSystemMessages(msgFromC).getReturnCode() == 0)
					{
						while(msgFromC.HasMore(message))
						{
							System.out.println(message.message_sub + ":" +message.message_s);
							
							if(message != null && message.message_id==CommID.SERVICE_REM)
							{
								switch(message.message_sub)
								{
									case CommID.SERVICE_REM_REBOOT_REQUEST:
										state = true;
										count = 0;
										break;
								}
							}
						}
					}
					
					if((!state) && (count != 0)) {
						try {
							count--;
							Thread.sleep(1000);
						}
						catch(Exception e){}
					}
				}
			}
		}
		return state;
	}
	
	public boolean closeIncomingConnection(String device)
	{
		boolean ret = false;
		DevicesMessages dms = closeConnectionEn();
		
		if(this.localBase.setSubSystemMessages(dms).getReturnCode() >= 0)
		{
			if(this.localBase.runDefaultCommand(device).getReturnCode() == 0)
				ret = true;
		}
		
		return ret;
	}
	
	private void setInfo(String message,int state)
	{
		String m = "";
		String i = "";
		if(message != null) {
			int idx = message.indexOf("\t");
			if(idx != -1) {
				m = message.substring(0,idx);
				i = message.substring(idx+1);
			}
		}
		this.modemUsed = m;
		switch(state)
		{
			case 3:
				this.localIP = i;
				break;
			case 4:
				this.remoteIP = i;
				break;
		}
	}
	
	private void clearInfo() {
		this.localIP = "";
		this.remoteIP = "";
	}
	
	public String getModemUsed() {
		return this.modemUsed;
	}
	
	public String getLocalIP() {
		return this.localIP;
	}
	
	public String getRemoteIP() {
		return this.remoteIP;
	}
	
	protected DevicesMessages getDevicesMessages()
	{
		DevicesMessages dms = new DevicesMessages();
		DeviceMessage dm1 = new DeviceMessage();
		
		dm1.message_id = CommID.SERVICE_REM;
		dm1.message_sub = CommID.SERVICE_REM_USER; 
		dm1.message_s = this.user;
		dms.add(dm1);
		
		dm1 = new DeviceMessage();
		dm1.message_id = CommID.SERVICE_REM;
		dm1.message_sub = CommID.SERVICE_REM_PASSWORD; 
		dm1.message_s = this.pass;
		dms.add(dm1);
		
		dm1 = new DeviceMessage();
		dm1.message_id = CommID.SERVICE_REM;
		dm1.message_sub = CommID.SERVICE_REM_WAIT_CALL; 
		dm1.message_s = "";
		dms.add(dm1);
		
		return dms;
	}
	
	private DevicesMessages getMessagesConf()
	{
		DevicesMessages dms = new DevicesMessages();
		DeviceMessage dm1 = new DeviceMessage();
		
		dm1.message_id = CommID.SERVICE_REM;
		dm1.message_sub = CommID.SERVICE_REM_USER; 
		dm1.message_s = this.user;
		dms.add(dm1);
		
		dm1 = new DeviceMessage();
		dm1.message_id = CommID.SERVICE_REM;
		dm1.message_sub = CommID.SERVICE_REM_PASSWORD; 
		dm1.message_s = this.pass;
		dms.add(dm1);
		
		dm1 = new DeviceMessage();
		dm1.message_id = CommID.SERVICE_REM;
		dm1.message_sub = CommID.SERVICE_REM_CONFIGURE; 
		dm1.message_s = "";
		dms.add(dm1);
		
		return dms;
	}
	
	private DevicesMessages closeConnectionEn()
	{
		DevicesMessages dms = new DevicesMessages();
		DeviceMessage dm1 = new DeviceMessage();
		
		dm1 = new DeviceMessage();
		dm1.message_id = CommID.SERVICE_REM;
		dm1.message_sub = CommID.SERVICE_REM_HANG; 
		dm1.message_s = "";
		dms.add(dm1);
		
		return dms;
	}
}
