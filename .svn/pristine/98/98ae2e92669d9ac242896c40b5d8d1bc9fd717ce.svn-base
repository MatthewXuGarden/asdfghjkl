package com.carel.supervisor.dispatcher.comm.layer;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dispatcher.comm.field.DeviceMessage;
import com.carel.supervisor.dispatcher.comm.field.DevicesMessages;
import com.carel.supervisor.dispatcher.comm.field.impl.ComSMSService;
import com.carel.supervisor.dispatcher.comm.field.impl.CommBase;
import com.carel.supervisor.dispatcher.comm.field.impl.CommID;

public class DispLaySms extends DispLayer 
{
	private static int SLEEP_SMS = 1000;
	private static int COUNT_SMS = 420;
	private static final String N = "National";
	private static final String I = "International";
	
	private String prefisso = "";
	private String receiver = "";
	private String filePath = "";
	private String provider = "";
	private String calltype = "";
	private String message  = "";
	
	public DispLaySms() {
		super("S");
	}
	
	public DispLaySms(String protFile,String prefisso,String provider,String callType,String message,String receiver)
	{
		super("S");
		this.prefisso = prefisso;
		this.receiver = receiver;
		this.filePath = protFile;
		this.provider = provider;
		this.calltype = callType;
		this.message  = message;
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
		return new ComSMSService();
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
				DevicesMessages dms = getDevicesMessages();
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
			int count = COUNT_SMS;
			
			while((count != 0) && (!state))
			{
				if(cs.getSubSystemMessages(msgFromC).getReturnCode() == 0)
				{
					while(msgFromC.HasMore(message))
					{
						if(message != null && message.message_id==CommID.SERVICE_SMS)
						{
							//System.out.println(message.message_sub + " " + message.message_s);
							
							switch(message.message_sub)
							{
								case CommID.SERVICE_SMS_LINE_ALREADY_IN_USE:
									insertMessageEvent("S","D034");
									LoggerMgr.getLogger(" - SMS Service - ").error(new String("SERVICE_SMS_LINE_ALREADY_IN_USE"));
									state = false;
									count = 0;
									break;
								case CommID.SERVICE_SMS_SEND_ERROR:
									insertMessageEvent("S","D039");
									LoggerMgr.getLogger(" - SMS Service - ").error(new String("SERVICE_SMS_SEND_ERROR"));
									state = false;
									count = 0;
									break;
								case CommID.SERVICE_SMS_EXCEPTION:
									insertMessageEvent("S","D039");
									LoggerMgr.getLogger(" - SMS Service - ").error(new String("SERVICE_SMS_EXCEPTION"));
									state = false;
									count = 0;
									break;
								case CommID.SERVICE_SMS_INIT_ERROR:
									insertMessageEvent("S","D039");
									LoggerMgr.getLogger(" - SMS Service - ").error(new String("SERVICE_SMS_INIT_ERROR"));
									state = false;
									count = 0;
									break;
								case CommID.SERVICE_SMS_SENT:
									state = true;
									count = 0;
									break;
							}
						}
					}
				}
				
				if(!state && count != 0) {
					try {
						count--;
						Thread.sleep(SLEEP_SMS);
					}
					catch(Exception e){
					}
				}
			}
			this.unloadclearSubSystem(cs);
		}
		else
			insertMessageEvent(fisicDevice,"D042"); //PERIPHERAL_ERROR
		
		return state;
	}
	
	protected DevicesMessages getDevicesMessages()
	{
		DevicesMessages dms = new DevicesMessages();
		DeviceMessage dm = new DeviceMessage();
		
		// Proto file path
		dm.message_id = CommID.SERVICE_SMS;
		dm.message_sub = CommID.SERVICE_SMS_PROTOSMS;
		dm.message_s = this.filePath;
		dms.add(dm);
		
		// Prefisso
		dm = new DeviceMessage();
		dm.message_id = CommID.SERVICE_SMS;
		dm.message_sub = CommID.SERVICE_SMS_DIAL_PREFIX;
		dm.message_s = this.prefisso;
		dms.add(dm);
		
		// Provider to call
		dm = new DeviceMessage();
		dm.message_id = CommID.SERVICE_SMS;
		dm.message_sub = CommID.SERVICE_SMS_TEL_SERVER;
		dm.message_s = this.provider;
		dms.add(dm);
		
		// Call Type 
		dm = new DeviceMessage();
		dm.message_id = CommID.SERVICE_SMS;
		dm.message_sub = CommID.SERVICE_SMS_SERVER_PREFIX;
		dm.message_s = "";
		if(this.calltype.equalsIgnoreCase("N"))
			dm.message_s = N;
		else if(this.calltype.equalsIgnoreCase("I"))
			dm.message_s = I;
		dms.add(dm);
		
		// Message 
		dm = new DeviceMessage();
		dm.message_id = CommID.SERVICE_SMS;
		dm.message_sub = CommID.SERVICE_SMS_MESSAGE;
		dm.message_s = this.message;
		dms.add(dm);
		
		// Receiver telephone
		dm = new DeviceMessage();
		dm.message_id = CommID.SERVICE_SMS;
		dm.message_sub = CommID.SERVICE_SMS_RECIPIENT_NUMBER;
		dm.message_s = this.receiver;
		dms.add(dm);
		
		return dms;
	}
}
