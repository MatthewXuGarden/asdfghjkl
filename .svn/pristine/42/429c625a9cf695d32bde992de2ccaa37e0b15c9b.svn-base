package com.carel.supervisor.dispatcher.comm.layer;

import com.carel.supervisor.dispatcher.comm.field.DeviceMessage;
import com.carel.supervisor.dispatcher.comm.field.DevicesMessages;
import com.carel.supervisor.dispatcher.comm.field.impl.CommBase;
import com.carel.supervisor.dispatcher.comm.field.impl.CommFaxService;
import com.carel.supervisor.dispatcher.comm.field.impl.CommID;

public class DispLayFax extends DispLayer
{
	private static int SLEEP_FAX = 1000;
	private static int COUNT_FAX = 420;
	
	private String senderName = "";
	private String receiverName = "";
	private String filePath = "";
	private String telReceiver = "";
	private String telSender = "";
	
	public DispLayFax() {
		super("F");
	}
	
	public DispLayFax(String sender,String receiver,String path,String telrecnum,String telsennum)
	{
		super("F");
		this.senderName   = sender;
		this.receiverName = receiver;
		this.filePath 	  = path;
		this.telReceiver  = telrecnum;
		this.telSender 	  = telsennum;
	}
	
	protected int getId() {
		return CommID.SERVICE_FAX;
	}
	
	protected int getInit() {
		return CommID.SERVICE_FAX_INIT_OK; 
	}
	
	protected int getPingService() {
		return CommID.SERVICE_FAX_PING; 
	}
	
	protected CommBase getImplementation() {
		return new CommFaxService();
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
			int count = COUNT_FAX;
			
			while((count != 0) && (!state))
			{
				if(cs.getSubSystemMessages(msgFromC).getReturnCode() == 0)
				{
					while(msgFromC.HasMore(message))
					{
						//System.out.println(message.message_sub + ":" +message.message_s);
						
						if(message != null && message.message_id==CommID.SERVICE_FAX)
						{
							switch(message.message_sub)
							{
								case CommID.FEI_BUSY:
									state = false;
									count = 0;
									insertMessageEvent("F","D034");
									break;
								case CommID.FEI_NO_ANSWER:
									state = false;
									count = 0;
									insertMessageEvent("F","D036");
									break;
								case CommID.FEI_BAD_ADDRESS:
									state = false;
									count = 0;
									insertMessageEvent("F","D037");
									break;
								case CommID.FEI_NO_DIAL_TONE:
									state = false;
									count = 0;
									insertMessageEvent("F","D035");
									break;
								case CommID.FEI_DISCONNECTED:
									state = false;
									count = 0;
									insertMessageEvent("F","D038");
									break;
								case CommID.FEI_FATAL_ERROR:
								case CommID.FEI_NOT_FAX_CALL:
								case CommID.FEI_CALL_DELAYED:
								case CommID.FEI_CALL_BLACKLISTED:
									state = false;
									count = 0;
									insertMessageEvent("F","D039");
									break;
								case CommID.FEI_ABORTING:
									state = false;
									count = 0;
									break;
								case CommID.FEI_COMPLETED:
									state = true;
									count = 0;
									break;
								case CommID.FEI_IDLE:
									break;
							}
						}
					}
				}
				
				if((!state) && (count != 0)) {
					try {
						count--;
						Thread.sleep(SLEEP_FAX);
					}
					catch(Exception e){
					}
				}
			}
		}
		else
			insertMessageEvent(fisicDevice,"D042");
		
		this.unloadclearSubSystem(cs);
		return state;
	}
	
	private DevicesMessages getDevicesMessages()
	{
		DevicesMessages dms = new DevicesMessages();
		DeviceMessage dm = new DeviceMessage();
		
		// Sender Name
		dm.message_id = CommID.SERVICE_FAX;
		dm.message_sub = CommID.SERVICE_FAX_SENDER;
		dm.message_s = this.senderName;
		dms.add(dm);
		
		// Receiver Name
		dm = new DeviceMessage();
		dm.message_id = CommID.SERVICE_FAX;
		dm.message_sub = CommID.SERVICE_FAX_RECEIVER;
		dm.message_s = this.receiverName;
		dms.add(dm);
		
		// Path file to send
		dm = new DeviceMessage();
		dm.message_id = CommID.SERVICE_FAX;
		dm.message_sub = CommID.SERVICE_FAX_FILE;
		dm.message_s = this.filePath;
		dms.add(dm);
		
		// Receiver telephone 
		dm = new DeviceMessage();
		dm.message_id = CommID.SERVICE_FAX;
		dm.message_sub = CommID.SERVICE_FAX_PHONENUMBER;
		dm.message_s = this.telReceiver;
		dms.add(dm);
		
		// Sender telephone
		dm = new DeviceMessage();
		dm.message_id = CommID.SERVICE_FAX;
		dm.message_sub = CommID.SERVICE_FAX_SENDERNUMBER;
		dm.message_s = this.telSender;
		dms.add(dm);
		
		return dms;
	}
}
