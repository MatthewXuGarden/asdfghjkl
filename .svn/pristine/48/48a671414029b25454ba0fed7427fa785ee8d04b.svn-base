package com.carel.supervisor.dispatcher.comm.layer;

import com.carel.supervisor.dispatcher.comm.field.DeviceMessage;
import com.carel.supervisor.dispatcher.comm.field.DevicesMessages;
import com.carel.supervisor.dispatcher.comm.field.impl.CommBase;
import com.carel.supervisor.dispatcher.comm.field.impl.CommDialer;
import com.carel.supervisor.dispatcher.comm.field.impl.CommID;

public class DispLayMail extends DispLayer
{
	private static int SLEEP_MAIL = 1000;
	private static int COUNT_MAIL = 420;
	private static int SLEEP_MODEM = 1000;
	private static int COUNT_MODEM = 10;
	
	private String user = "";
	private String pass = "";
	
	public DispLayMail() {
		super("E");
	}
	
	public DispLayMail(String u, String p)
	{
		super("E");
		this.user = u;
		this.pass = p;
	}
	
	protected int getId() {
		return CommID.SERVICE_DIALER;
	}
	
	protected int getInit() {
		return CommID.SERVICE_DIALER_INIT_OK; 
	}
	
	protected int getPingService() {
		return CommID.SERVICE_DIALER_PING; 
	}
	
	protected CommBase getImplementation() {
		return new CommDialer();
	}
	
	public boolean openChannel(String fisicDevice)
	{
		boolean state = false;
		boolean ret = false;
		CommBase cs = this.getImplementation();
		
		if(this.loadMainLibrary())
		{
			if(this.loadinitSubSystem(cs))
			{
				DevicesMessages dms = getOpenMessages();
				if(cs.setSubSystemMessages(dms).getReturnCode() >= 0)
				{
					if(cs.runDefaultCommand(fisicDevice).getReturnCode() == 0)
						ret = true;
				}
			}
		}
		
		if(ret)
		{
			DevicesMessages msgFromC = new DevicesMessages();
			DeviceMessage message = new DeviceMessage();
			int count = COUNT_MAIL;
			
			while((count != 0) && (!state))
			{
				if(cs.getSubSystemMessages(msgFromC).getReturnCode() == 0)
				{
					while(msgFromC.HasMore(message))
					{
						if(message != null && message.message_id==CommID.SERVICE_DIALER)
						{
							//System.out.println(message.message_sub + " " + message.message_s);
							
							switch(message.message_sub)
							{
								case CommID.SERVICE_DIALER_ERROR:
									insertMessageEvent("E","D039");
									state = false;
									count = 0;
									break;
								case CommID.SERVICE_DIALER_LINE_USED_ERROR:
									insertMessageEvent("E","D035");
									state = false;
									count = 0;
									break;
								case CommID.SERVICE_DIALER_CONNECTED:
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
						Thread.sleep(SLEEP_MAIL);
					}
					catch(Exception e){
					}
				}
			}
		}
		else
			insertMessageEvent(fisicDevice,"D042");
		
		return state;
	}
	
	public boolean closeChannel(String fisicDevice)
	{
		boolean state = false;
		boolean ret = false;
		CommBase cs = this.getImplementation();
		DevicesMessages dms = getCloseMessages();

		if(cs.setSubSystemMessages(dms).getReturnCode() >= 0)
		{
			if(cs.runDefaultCommand(fisicDevice).getReturnCode() == 0)
				ret = true;
		}
		
		if(ret)
		{
			DevicesMessages msgFromC = new DevicesMessages();
			DeviceMessage message = new DeviceMessage();
			int count = COUNT_MAIL;
			
			while((count != 0) && (!state))
			{
				if(cs.getSubSystemMessages(msgFromC).getReturnCode() == 0)
				{
					while(msgFromC.HasMore(message))
					{
						if(message != null && message.message_id==CommID.SERVICE_DIALER)
						{
							switch(message.message_sub)
							{
								case CommID.SERVICE_DIALER_HANG_ERROR:
									insertMessageEvent("E","D039");
									state = false;
									count = 0;
									break;
								case CommID.SERVICE_DIALER_HANG_OK:
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
						Thread.sleep(SLEEP_MAIL);
					}
					catch(Exception e){
					}
				}
			}
		}
		else
			insertMessageEvent("E","D033");
		
		this.unloadclearSubSystem(cs);
			
		return ret;
	}
	
	private DevicesMessages getOpenMessages()
	{
		DevicesMessages dms = new DevicesMessages();
		DeviceMessage dm = new DeviceMessage();
		
		dm.message_id = CommID.SERVICE_DIALER;
		dm.message_sub = CommID.SERVICE_DIALER_REQ;
		dm.message_s = "";
		dms.add(dm);
		
		dm = new DeviceMessage();
		dm.message_id = CommID.SERVICE_DIALER;
		dm.message_sub = CommID.SERVICE_DIALER_USER;
		dm.message_s = this.user;
		dms.add(dm);
		
		dm = new DeviceMessage();
		dm.message_id = CommID.SERVICE_DIALER;
		dm.message_sub = CommID.SERVICE_DIALER_PASSWORD;
		dm.message_s = this.pass;
		dms.add(dm);
		
		return dms;
	}
	
	private DevicesMessages getCloseMessages()
	{
		DevicesMessages dms = new DevicesMessages();
		DeviceMessage dm = new DeviceMessage();
		
		dm.message_id = CommID.SERVICE_DIALER;
		dm.message_sub = CommID.SERVICE_DIALER_HANG_REQ;
		dm.message_s = "";
		dms.add(dm);
		return dms;
	}
	
	public String getModemDialUp(String providerId)
	{
		CommBase cs = getImplementation();
		boolean ret = false;
		boolean state = false;
		DevicesMessages dms = new DevicesMessages();
		DeviceMessage dm = new DeviceMessage();
		
		String modemId = "";
		
		if(this.loadMainLibrary())
		{
			if(this.loadinitSubSystem(cs))
			{
				
				dm.message_id = CommID.SERVICE_DIALER;
				dm.message_sub = CommID.SERVICE_DIALER_MODEM_REQ; 
				dm.message_s = "";
				dms.add(dm);
				
				if(cs.setSubSystemMessages(dms).getReturnCode() >= 0)
				{
					if(cs.runDefaultCommand(providerId).getReturnCode() == 0)
						ret = true;
				}
			}
		}
		
		if(ret)
		{
			dms = new DevicesMessages();
			dm = new DeviceMessage();
			int count = COUNT_MODEM;
			DevicesMessages msgFromC = new DevicesMessages();
			DeviceMessage message = new DeviceMessage();
			
			while((count != 0) && (!state))
			{
				if(cs.getSubSystemMessages(msgFromC).getReturnCode() == 0)
				{
					msgFromC.HasMore(message);
					if(message != null && message.message_id==CommID.SERVICE_DIALER && 
					   message.message_sub==CommID.SERVICE_DIALER_MODEM_RES)
					{
						state = true;
						count = 0;
						modemId = message.message_s;
					}
				}
				
				if(!state) {
					try {
						count--;
						Thread.sleep(SLEEP_MODEM);
					}
					catch(Exception e){
					}
				}
			}
		}
		
		this.unloadclearSubSystem(cs);
		
		return modemId;
	}
}
