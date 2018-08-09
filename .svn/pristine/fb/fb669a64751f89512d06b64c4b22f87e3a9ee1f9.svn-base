package com.carel.supervisor.dispatcher.comm.layer;

import java.util.Vector;

import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.comm.field.CommConfig;
import com.carel.supervisor.dispatcher.comm.field.DeviceMessage;
import com.carel.supervisor.dispatcher.comm.field.DevicesMessages;
import com.carel.supervisor.dispatcher.comm.field.ICommConfig;
import com.carel.supervisor.dispatcher.comm.field.impl.CommBase;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.ZMemory;

public abstract class DispLayer 
{
	private static int SLEEP_INIT = 1000;
	private static int COUNT_INIT = 30;
	
	public DispLayer(String t)
	{
	}
	
	public String[][] getFisicChannel(String type)
	{
		return DispMemMgr.getInstance().getMachineDevice(type);
	}
	
	public String[][] getUsedFisicChannel()
	{
		String[][] ret = new String[3][2];
		ZMemory zMem = null;
		zMem = DispMemMgr.getInstance().readConfiguration("F");
		ret[0][0] = zMem.getFisicDeviceId();
		zMem = DispMemMgr.getInstance().readConfiguration("S");
		ret[1][0] = zMem.getFisicDeviceId();
		zMem = DispMemMgr.getInstance().readConfiguration("E");
		ret[2][0] = zMem.getFisicDeviceId();
		return ret;
	}
	
	public String[] getFisicChannel()
	{
		CommBase cs = getImplementation();
		String[] deviceList = new String[0];

		if(this.loadMainLibrary())
		{
			if(this.loadinitSubSystem(cs))
			{
				ICommConfig commc = new CommConfig();
				if(cs.getConfigObject(commc).getReturnCode() == 0)
				{
					Vector devices = commc.getCommunicationConfig();
					if(devices != null)
					{
						deviceList = new String[devices.size()];
						for (int i=0; i<deviceList.length; i++)
							deviceList[i] = (String)devices.elementAt(i);
					}
				}
			}
			
			this.unloadclearSubSystem(cs);
		}
		return deviceList;
	}
	
	public boolean pingDevice(String fisicDevice)
	{
		CommBase cs = getImplementation();
		boolean ris = false;
		
		if(this.loadMainLibrary())
		{
			if(this.loadinitSubSystem(cs))
			{
				DevicesMessages dms = new DevicesMessages();
				DeviceMessage dm = new DeviceMessage();
				dm = new DeviceMessage();
				dm.message_id = getId();
				dm.message_sub = getPingService();
				dm.message_s = "";
				dms.add(dm);
				
				if(cs.setSubSystemMessages(dms).getReturnCode() >= 0)
				{
					if(cs.runDefaultCommand(fisicDevice).getReturnCode() == 0)
						ris = true;
				}
			}
		}
		this.unloadclearSubSystem(cs);
		return ris;
	}
	
	
	public String getModemDialUp(String providerId)
	{
		return "";
	}
	
	protected boolean loadMainLibrary()
	{
		boolean ret = false;
		
		try {
			CommBase.loadMe();
			ret = true;
		}
		catch(Exception e) {
			EventMgr.getInstance().log(new Integer(1),"Dispatcher","Action",new Integer(1),"D016",null);
		}
		
		return ret;
	}
	
	protected boolean loadinitSubSystem(CommBase cb)
	{
		boolean state = false;
		int ret = cb.loadDLLSubsystem().getReturnCode();
		if(ret == 0)
		{
			ret = cb.initSubSystem().getReturnCode();
			if(ret == 0)
			{
				int count = COUNT_INIT;
				DevicesMessages msgFromC = new DevicesMessages();
				DeviceMessage message = new DeviceMessage();
				
				while((count != 0) && (!state))
				{
					ret = cb.getSubSystemMessages(msgFromC).getReturnCode();
					if(ret == 0)
					{
						while(msgFromC.HasMore(message))
						{
							if(message != null && message.message_id==getId() && message.message_sub==getInit())
							{
								state = true;
								count = 0;
							}
						}
					}
					
					if(!state) {
						try {
							count--;
							Thread.sleep(SLEEP_INIT);
						}
						catch(Exception e){
						}
					}
				}
			}
		}
		return state;
	}
	
	protected boolean unloadclearSubSystem(CommBase cb)
	{
		boolean state = true;
		
		int ret = cb.doneSubSystem().getReturnCode();
		if(ret != 0)
			state = false;
		
		ret = cb.unloadDLLSubsystem().getReturnCode();
		if(ret != 0)
			state = false;
		
		return state;
	}
	
	public boolean closeChannel(String fisicDevice) {
		return true;
	}
	
	protected void insertMessageEvent(String typeAction, String IdMsg)
	{
		Object[] p = null;
		
		try 
		{
			if(typeAction != null && typeAction.length() > 1)
				p = new Object[]{typeAction};
			else
				p = new Object[]{DispatcherMgr.getInstance().decodeActionType(typeAction)};
			
			EventMgr.getInstance().log(new Integer(1),"Dispatcher","Action",new Integer(1),IdMsg,p);
		}
		catch(Exception e){}
	}
	
	protected abstract CommBase getImplementation();
	protected abstract int getId();
	protected abstract int getInit();
	protected abstract int getPingService();
	
	public abstract boolean openChannel(String fisicDevice);
}
