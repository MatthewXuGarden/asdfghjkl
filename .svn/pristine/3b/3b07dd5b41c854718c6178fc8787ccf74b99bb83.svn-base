package com.carel.supervisor.director.module;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.director.packet.PacketMgr;

public class ModuleMgr implements IInitializable
{
	private static ModuleMgr mm = new ModuleMgr();
	
	private List<IModule> hookList = null;
	
	private ModuleMgr() {
		hookList = new ArrayList<IModule>();
	}
	
	public static ModuleMgr getInstance() {
        return mm;
    }
	
	public synchronized void init(XMLNode xmlStatic) 
		throws InvalidConfigurationException 
	{
		try
		{
			// First of all, loads activated modules
			PacketMgr.getInstance().loadActivePacketOnStartUp();
			
			XMLNode[] mod = xmlStatic.getNode("modules").getNodes();
			if(mod != null)
			{
				IModule hook = null;
				String sClass = "";
				String sName = "";
				for (int i=0; i<mod.length; i++) 
				{
					sClass = mod[i].getAttribute("class");
					sName = mod[i].getAttribute("name");
					if(sClass != null && sClass.length() > 0)
					{
						try {
							hook = (IModule)FactoryObject.newInstance(sClass);
							hook.setName(sName);
							hookList.add(hook);
						}
						catch(Exception ein) {
							hook = null;
						}
					}
				}
			}
			/*
			 * START
			 */
			hookModuleAndStart();
		}
		catch(Exception e) {
			FatalHandler.manage(this, "...", e);
		}
	}
	
	public void hookModuleAndStart()
	{
		Logger logger = LoggerMgr.getLogger(this.getClass());
		if(hookList != null)
		{
			IModule toHook = null;
			for(int i=0; i<hookList.size(); i++)
			{
				toHook = (hookList.get(i));
				if(toHook != null)
				{
					if(PacketMgr.getInstance().checkForMenuRestriction(toHook.getName()))
					{
						if(!PacketMgr.getInstance().isFunctionAllowed(toHook.getName()))
							continue;
					}
					
					if(toHook.hookModule(true))
						logger.info("Hook module "+ toHook.getClass() +" is running]");
					else
						logger.info("Hook module "+ toHook.getClass() +" is not running]");
				}
			}
		}
	}
	
	public void hookModuleAndStop()
	{
		Logger logger = LoggerMgr.getLogger(this.getClass());
		if(hookList != null)
		{
			IModule toHook = null;
			for(int i=0; i<hookList.size(); i++)
			{
				toHook = (hookList.get(i));
				if(toHook != null)
				{
					if(PacketMgr.getInstance().checkForMenuRestriction(toHook.getName()))
					{
						if(!PacketMgr.getInstance().isFunctionAllowed(toHook.getName()))
							continue;
					}
					
					if(toHook.hookModule(false))
						logger.info("Hook module "+ toHook.getClass() +" is stopping]");
					else
						logger.info("Hook module "+ toHook.getClass() +" is not stopping]");
				}
			}
		}
	}
}
