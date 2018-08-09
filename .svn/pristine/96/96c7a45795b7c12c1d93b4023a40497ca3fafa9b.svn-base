package com.carel.supervisor.dispatcher.comm.field.impl;

import java.util.Calendar;
import java.util.Vector;

import com.carel.supervisor.dispatcher.comm.field.CommConfig;
import com.carel.supervisor.dispatcher.comm.field.CommReturnCode;
import com.carel.supervisor.dispatcher.comm.field.DeviceIDs;
import com.carel.supervisor.dispatcher.comm.field.DeviceMessage;
import com.carel.supervisor.dispatcher.comm.field.DevicesMessages;
import com.carel.supervisor.dispatcher.comm.field.ICommConfig;


public class CommRasService extends CommBase 
{
	private int systemID = DeviceIDs.REMOTE;
	
	public CommRasService() {
	}
	
	public int getCommunicationID() {
		return systemID;
	}
	
	public boolean isBlockingError() {
		return blockingError;
	}

	public synchronized CommReturnCode initSubSystem() 
	{
		CommReturnCode ret = null;
        try
        {
        	short returnCode = cinitSubSystem();

            if (0 != returnCode)
            {
                blockingError = true;
                ret = new CommReturnCode(returnCode, "", true);
            }
            else
                ret = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true; 
            ret =  new CommReturnCode(e);
        }
		return ret;
	}

	public synchronized CommReturnCode doneSubSystem() 
	{
		CommReturnCode ret = null;
		try
        {
			short returnCode = cdoneSubSystem();

            if (0 != returnCode)
            {
                blockingError = true;
                ret = new CommReturnCode(returnCode, "", true);
            }
            else
            	ret = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true; 
            ret = new CommReturnCode(e);
        }
        return ret;
	}

	public synchronized CommReturnCode runDefaultCommand(String device) 
	{
		CommReturnCode ret = null;
        try
        {
        	short returnCode = crunDefaultCommand(device);

            if (0 != returnCode)
                ret = new CommReturnCode(returnCode, "", true);
            else
            	ret = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true; 
            ret = new CommReturnCode(e);
        }
		return ret;
	}

	
	public synchronized CommReturnCode getConfigObject(ICommConfig conf) 
	{
		CommReturnCode ret = null;
        try
        {
        	short returnCode = cgetConfigObject(conf);

            if (0 != returnCode)
                ret = new CommReturnCode(returnCode, "", true);
            else
                ret = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true; 
            ret = new CommReturnCode(e);
        }
        return ret;
	}
	
	public  synchronized CommReturnCode loadDLLSubsystem()
	{
		CommReturnCode ret = null;
        try
        {
        	short returnCode = cloadSubSystem();	
        	ret =  new CommReturnCode((short)returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true; 
            ret = new CommReturnCode(e);
        }
		return ret;
	}
	
	public  synchronized CommReturnCode unloadDLLSubsystem()
	{
		CommReturnCode ret = null;
        try
        {
        	short returnCode = cunloadSubSystem();
        	ret = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true; 
            ret = new CommReturnCode(e);
        }
		return ret;
	}
	
	public synchronized CommReturnCode getSubSystemMessages(DevicesMessages deviceMessages)
	{
		CommReturnCode ret = null;
        try
        {
        	
        	short returnCode = cgetSubSystemMessages(deviceMessages);
        	ret = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true; 
            ret = new CommReturnCode(e);
        }
        return ret;
	}
	
	public synchronized CommReturnCode setSubSystemMessages(DevicesMessages deviceMessages)
	{
		CommReturnCode ret = null;
        try
        {
        	short returnCode = csetSubSystemMessages(deviceMessages);

            if (0 != returnCode)
                ret = new CommReturnCode(returnCode, "", true);
            else
                ret = new CommReturnCode(returnCode);
        }
        catch (Throwable e)
        {
            blockingError = true; 
            ret = new CommReturnCode(e);
        }
        return ret;
	}

	
	
	public static void DoConfig()
	{
		CommRasService cs = new CommRasService();
		DevicesMessages devicemsg;
		DeviceMessage dm0;
		String deviceToSend = "";
		boolean loop = true;
		boolean first = true;
		boolean connected = false;

		Calendar Timeout = Calendar.getInstance();
		Timeout.add(Calendar.SECOND,300);
		System.out.println("Loading subsystem module.");
		int ret = 0;
		cs.loadMe();
		if((ret = cs.loadDLLSubsystem().getReturnCode())!=0)
		{
			System.out.println("Unable to load module commdial ErrCode = " 
								+Integer.valueOf(ret).toString() );
			return;
		}
		if(cs.cinitSubSystem()==0){
			devicemsg = new DevicesMessages();
			short res = cs.cgetSubSystemMessages(devicemsg);
			dm0= new DeviceMessage();
			boolean hasMore = false;
			while((hasMore=devicemsg.HasMore(dm0)) || loop)
			{
				if(hasMore)
				{
					System.out.println(dm0.message_s);
					//dopo l'init ok esegue una sola volta il caricamento dei messaggi e l'invio dell'sms
					if(dm0.message_id == CommID.SERVICE_REM && dm0.message_sub == CommID.SERVICE_REM_INIT_OK    )
					{
						System.out.println("Init Done.");
						ICommConfig commc = new CommConfig();
						if(cs.cgetConfigObject( commc )==0)
						{
							Vector devices = commc.getCommunicationConfig();
							for(int j=0;j<devices.size();j++ )
							{
								String device = (String)devices.elementAt(j);
								System.out.println(device);
								
								deviceToSend = device;				
								if(deviceToSend !="")
								{
									first=false;
									DevicesMessages dms = new DevicesMessages();
									DeviceMessage dm1 = new DeviceMessage();
									System.out.println("Preparing configuration ....");
									dm1.message_id = CommID.SERVICE_REM;
									dm1.message_sub = CommID.SERVICE_REM_USER; 
									dm1.message_s = "PVRemote";
									dms.add(dm1);
									dm1 = new DeviceMessage();
									dm1.message_id = CommID.SERVICE_REM;
									dm1.message_sub = CommID.SERVICE_REM_PASSWORD; 
									dm1.message_s = "PD35010";
									dms.add(dm1);
									dm1 = new DeviceMessage();
									dm1.message_id = CommID.SERVICE_REM;
									dm1.message_sub = CommID.SERVICE_REM_CONFIGURE; 
									dm1.message_s = "";
									dms.add(dm1);
									
									if(cs.csetSubSystemMessages(dms)>=0)
										System.out.println("...Done");
									else
										System.out.println("...error in send configuration");
									System.out.println("Configuring...");
									if(cs.crunDefaultCommand( deviceToSend)!=0)
										loop=false;
								}
								
							}//fine for
							
						}//fine if
					}	
					
					
					
				}
				if(
						(dm0.message_id == CommID.SERVICE_REM && 
						 dm0.message_sub == CommID.SERVICE_REM_REBOOT_REQUEST ) 
										
					)	
				{
					
					System.out.println("System configured, now restart the system.");
					
				}
				if(!Timeout.after(Calendar.getInstance()))
				{
					System.out.println("Trascorsi x minuti.");
					loop = false;
					
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dm0=new DeviceMessage();
				res = cs.cgetSubSystemMessages(devicemsg);//prendo il prossimo messaggio	
			}
			
		}
		cs.cdoneSubSystem();	
	}
	
	public static void DoDialIn()
	{
		CommRasService cs = new CommRasService();
		DevicesMessages devicemsg;
		DeviceMessage dm0;
		String deviceToSend = "";
		boolean loop = true;
		boolean first = true;
		boolean connected = false;

		Calendar Timeout = Calendar.getInstance();
		Timeout.add(Calendar.SECOND,3000);
		cs.loadMe();
		if(cs.cinitSubSystem()==0){
			devicemsg = new DevicesMessages();
			short res = cs.cgetSubSystemMessages(devicemsg);
			dm0= new DeviceMessage();
			boolean hasMore = false;
			while((hasMore=devicemsg.HasMore(dm0)) || loop)
			{
				if(hasMore)
				{
					System.out.println(dm0.message_s);
					//dopo l'init ok esegue una sola volta il caricamento dei messaggi e l'invio dell'sms
					if(dm0.message_id == CommID.SERVICE_REM && dm0.message_sub == CommID.SERVICE_REM_INIT_OK    )
					{
						System.out.println("Init Done.");
						ICommConfig commc = new CommConfig();
						if(cs.cgetConfigObject( commc )==0)
						{
							Vector devices = commc.getCommunicationConfig();
							for(int j=0;j<devices.size();j++ )
							{
								String device = (String)devices.elementAt(j);
								System.out.println(device);
								
								deviceToSend = device;				
								if(deviceToSend !="" )
								{
									first=false;
									DevicesMessages dms = new DevicesMessages();
									DeviceMessage dm1 = new DeviceMessage();
									System.out.println("Preparing configuration ....");
									dm1.message_id = CommID.SERVICE_REM;
									dm1.message_sub = CommID.SERVICE_REM_USER; 
									dm1.message_s = "PVRemote";
									dms.add(dm1);
									dm1 = new DeviceMessage();
									dm1.message_id = CommID.SERVICE_REM;
									dm1.message_sub = CommID.SERVICE_REM_PASSWORD; 
									dm1.message_s = "PD35010";
									dms.add(dm1);
									dm1 = new DeviceMessage();
									dm1.message_id = CommID.SERVICE_REM;
									dm1.message_sub = CommID.SERVICE_REM_WAIT_CALL; 
									dm1.message_s = "";
									dms.add(dm1);
									
									if(cs.csetSubSystemMessages(dms)>=0)
										System.out.println("...Done");
									else
										System.out.println("...error in send configuration");
									System.out.println("Configuring...");
									if(cs.crunDefaultCommand( deviceToSend)!=0)
										loop=false;
								}
								
							}//fine for
							
						}//fine if
					}	
					
					
					
				}
				if(
						(dm0.message_id == CommID.SERVICE_REM && 
						 dm0.message_sub == CommID.SERVICE_REM_REBOOT_REQUEST ) 
										
					)	
				{
					
					System.out.println("System configured, now restart the system.");
					
				}
				if(!Timeout.after(Calendar.getInstance()))
				{
					System.out.println("Trascorsi tre minuti.");
					loop = false;
					
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dm0=new DeviceMessage();
				res = cs.cgetSubSystemMessages(devicemsg);//prendo il prossimo messaggio	
			}
			
		}
		cs.cdoneSubSystem();	
	}
	public static void main(String[] args){
		DoConfig();
		//DoDialIn();
	}
}
