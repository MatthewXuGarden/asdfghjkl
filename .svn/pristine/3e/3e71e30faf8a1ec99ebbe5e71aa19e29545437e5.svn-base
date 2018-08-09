package com.carel.supervisor.dispatcher.comm.layer;

import com.carel.supervisor.dispatcher.comm.field.DeviceMessage;
import com.carel.supervisor.dispatcher.comm.field.DevicesMessages;
import com.carel.supervisor.dispatcher.comm.field.impl.CommBase;
import com.carel.supervisor.dispatcher.comm.field.impl.CommDialUp;
import com.carel.supervisor.dispatcher.comm.field.impl.CommID;

public class DispLayDialUp extends DispLayer
{
	private static int SLEEP_DU = 1000;
	private static int COUNT_DU = 420;
	
	private CommBase cs = null;
	
	private String user = "";
	private String pass = "";
	private String number = "";
	private String centra = "";
	private String remoteIP = "";
	
	public DispLayDialUp()
	{
		super("D");
	}
	
	public DispLayDialUp(String user,String pass,String num,String cen) 
	{
		super("D");
		this.user = user;
		this.pass = pass;
		this.number = num;
		this.centra = cen;
	}
	
	protected int getId() {
		return CommID.SERVICE_DIAL;
	}
	
	protected int getInit() {
		return CommID.SERVICE_DIAL_INIT_OK; 
	}
	
	protected int getPingService() {
		return CommID.SERVICE_DIAL_PING; 
	}
	
	protected CommBase getImplementation() {
		return new CommDialUp();
	}
	
	public boolean openChannel(String fisicDevice)
	{
		boolean state = false;
		boolean ret = false;
		this.cs = this.getImplementation();
		
		if(this.loadMainLibrary())
		{
			if(this.loadinitSubSystem(this.cs))
			{
				DevicesMessages dms = getOpenMessages();
				if(this.cs.setSubSystemMessages(dms).getReturnCode() >= 0)
				{
					if(this.cs.runDefaultCommand(fisicDevice).getReturnCode() == 0)
						ret = true;
				}
			}
		}
		
		if(ret)
		{
			DevicesMessages msgFromC = new DevicesMessages();
			DeviceMessage message = new DeviceMessage();
			int count = COUNT_DU;
			
			while((count != 0) && (!state))
			{
				if(this.cs.getSubSystemMessages(msgFromC).getReturnCode() == 0)
				{
					while(msgFromC.HasMore(message))
					{
						if(message != null && message.message_id==CommID.SERVICE_DIAL)
						{
							System.out.println(message.message_sub+" "+message.message_s);
							
							switch(message.message_sub)
							{
								case CommID.SERVICE_DIAL_ERROR:
									insertMessageEvent("D","D039");
									state = false;
									count = 0;
									break;
								case CommID.SERVICE_DIAL_LINE_USED_ERROR:
									insertMessageEvent("D","D035");
									state = false;
									count = 0;
									break;
								case CommID.SERVICE_DIAL_CONNECTED:
									state = true;
									count = 0;
									break;
								case CommID.SERVICE_DIAL_IP_REMOTE:
									this.setInfo(message.message_s);
							}
						}
					}
				}
				
				if((!state) && (count != 0)) {
					try {
						count--;
						Thread.sleep(SLEEP_DU);
					}
					catch(Exception e){
					}
				}
			}
		}
		else
			insertMessageEvent(fisicDevice,"D042");
		
		if(!state)
			closeChannel(fisicDevice);
				
		return state;
	}
	
	public boolean closeChannel(String fisicDevice)
	{
		boolean state = false;
		boolean ret = false;
		DevicesMessages dms = getCloseMessages();
		
		if(this.cs != null)
		{
			if(this.cs.setSubSystemMessages(dms).getReturnCode() >= 0)
			{
				if(this.cs.runDefaultCommand(fisicDevice).getReturnCode() == 0)
					ret = true;
			}
			if(ret)
			{
				DevicesMessages msgFromC = new DevicesMessages();
				DeviceMessage message = new DeviceMessage();
				int count = COUNT_DU;
				
				while((count != 0) && (!state))
				{
					if(this.cs.getSubSystemMessages(msgFromC).getReturnCode() == 0)
					{
						while(msgFromC.HasMore(message))
						{
							if(message != null && message.message_id==CommID.SERVICE_DIAL)
							{
								System.out.println(message.message_sub+" "+message.message_s);
								
								switch(message.message_sub)
								{
									case CommID.SERVICE_DIAL_HANG_ERROR:
										insertMessageEvent("D","D039");
										state = false;
										count = 0;
										break;
									case CommID.SERVICE_DIAL_HANG_OK:
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
							Thread.sleep(SLEEP_DU);
						}
						catch(Exception e){
						}
					}
				}
			}
			else
				insertMessageEvent(fisicDevice,"D042");
			
			this.unloadclearSubSystem(this.cs);
			this.cs = null;
		}
		else
			ret = true;
		
		return ret;
	}
	
	private DevicesMessages getOpenMessages()
	{
		DevicesMessages dms = new DevicesMessages();
		DeviceMessage dm = new DeviceMessage();
		
        dm.message_id = CommID.SERVICE_DIAL;
        dm.message_sub = CommID.SERVICE_DIAL_USER;
        dm.message_s = this.user;
        dms.add(dm);
        
        dm = new DeviceMessage();
        dm.message_id = CommID.SERVICE_DIAL;
        dm.message_sub = CommID.SERVICE_DIAL_PASSWORD;
        dm.message_s = this.pass;
        dms.add(dm);
        
        dm = new DeviceMessage();
        dm.message_id = CommID.SERVICE_DIAL;
        dm.message_sub = CommID.SERVICE_DIAL_TEL;
        dm.message_s = this.number;
        dms.add(dm);
        
        dm = new DeviceMessage();
        dm.message_id = CommID.SERVICE_DIAL;
        dm.message_sub = CommID.SERVICE_DIAL_REQ;
        dm.message_s = "";
        dms.add(dm);
		
		return dms;
	}
	
	private DevicesMessages getCloseMessages()
	{
		DevicesMessages dms = new DevicesMessages();
		DeviceMessage dm = new DeviceMessage();
		
		dm.message_id = CommID.SERVICE_DIAL;
		dm.message_sub = CommID.SERVICE_DIAL_HANG_REQ;
		dm.message_s = "";
		dms.add(dm);
		
		return dms;
	}
	
	private void setInfo(String message)
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
		
		this.remoteIP = i;
	}
	
	public String getRemoteIP() {
		return this.remoteIP;
	}
}
