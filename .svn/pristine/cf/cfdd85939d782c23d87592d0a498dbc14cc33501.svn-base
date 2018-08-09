package com.carel.supervisor.remote.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.main.DispatcherManual;
import com.carel.supervisor.dispatcher.memory.DMemory;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.remote.commlayer.CommLayerMgr;
import com.carel.supervisor.remote.engine.connection.ActiveConnections;

public class RemoteMgr extends InitializableBase implements IInitializable
{
	private static String REMOTE = "REMOTO";
	private static RemoteMgr me = new RemoteMgr();
	
	private Map localMap = null;
	private Properties configuration = null;
	private DispatcherManual manualConn = null;
	
	private RemoteMgr()
	{
		localMap = new HashMap();
		configuration = new Properties();
	}
	
	public static RemoteMgr getInstance() {
        return me;
    }
	
	public synchronized void init(XMLNode xmlStatic)
		throws InvalidConfigurationException
	{
		XMLNode xml = null;
		String node = "local";
		
		if(REMOTE.equalsIgnoreCase(ProductInfoMgr.getInstance().getProductInfo().get("installation")))
			node = "remote";
		
		for (int i = 0; i < xmlStatic.size(); i++)
		{
			xml = xmlStatic.getNode(i);
			try
			{
				String typeComp = xml.getAttribute("name");
				if(typeComp != null && typeComp.equalsIgnoreCase("conf"))
					loadConfiguration(xml);
				else if(typeComp != null && typeComp.equalsIgnoreCase(node))
					loadConf(xml);
			}
			catch (Exception e)
			{
				throw new InvalidConfigurationException("");
	        }
		}
	}
	
	public String openLocalSite(int idSite,String tel,String type,String inst)
	{
		String ris = "NOP";
		
		if(type != null && type.equalsIgnoreCase("LAN"))
		{
			try
			{
				CommLayerMgr com = new CommLayerMgr();
				com.setTypeSoft(inst);
				if(com.startCommunication(RasServerMgr.getInstance().getProtocol(),
										  RasServerMgr.getInstance().getPort(),RasServerMgr.getInstance().getRoot(),
										  "LAN","127.0.0.1",tel,DispatcherMgr.getInstance().getCertificatePath()))
				{
					ris = tel;
				}
			}
			catch(Exception e) 
			{
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
		else
		{
			// Controllo se esiste una connessione in uscita e la chiudo.
			closeLocalSite(idSite);
			
			this.manualConn = new DispatcherManual(idSite,tel);
			boolean isConnect = this.manualConn.connect(RasServerMgr.getInstance().getProtocol(),
												  RasServerMgr.getInstance().getPort(),
												  RasServerMgr.getInstance().getRoot()); 
			if(isConnect)
			{
				DMemory zMem = (DMemory) DispMemMgr.getInstance().readConfiguration("D");
				
				// Segnalo connessione via modem attiva
				try {
                    Object[] p = {zMem.getModemId()};
                    EventMgr.getInstance().log(new Integer(1),"Remote","Action",EventDictionary.TYPE_INFO,"R008",p);
                }
                catch (RuntimeException e1){}
                
                // Recupero IP del locale
				ris = ActiveConnections.getInstance().getIpLocale(zMem.getModemId());
				
				// Salvo il tipo di connessione in uscita
				ActiveConnections.getInstance().setTypeLocalOut(inst);
				// Salvo id del sito connesso in uscita
				ActiveConnections.getInstance().setIdLocalOut(idSite);
				ActiveConnections.getInstance().putClientOnConnection(zMem.getModemId(),""+idSite);
				
				CommLayerMgr com = new CommLayerMgr();

				if(inst != null && inst.equalsIgnoreCase("PVE"))
				{
					com.startCommunicationDirectPVE(ris,zMem.getFisicDeviceId());
				}
				else
				{
					String ipRemote = ActiveConnections.getInstance().getIpRemote(zMem.getModemId());
					com.startCommunication(RasServerMgr.getInstance().getProtocol(),
     					   RasServerMgr.getInstance().getPort(),RasServerMgr.getInstance().getRoot(),
     					  zMem.getModemId(),ipRemote,ris,DispatcherMgr.getInstance().getCertificatePath());
				}
			}
		}
		return ris;
	}
	
	public boolean closeLocalSite(int idSite)
	{
		boolean ris = true;
		if(this.manualConn != null)
			ris = this.manualConn.disconnect();
		
		this.manualConn = null;
		
		if(ris)
		{
			DMemory zMem = (DMemory) DispMemMgr.getInstance().readConfiguration("D");
			
			ActiveConnections.getInstance().remConnection(zMem.getModemId(),false);
			try {
                Object[] p = {zMem.getModemId()};
                EventMgr.getInstance().log(new Integer(1),"Remote","Action",EventDictionary.TYPE_INFO,"R009",p);
            }catch (RuntimeException e1){}
		}
		
		/*
		 * Aspetto 5s in modo tale che la risorsa sia completamente libera.
		 */
		try {
			Thread.sleep(5000L);
		}catch(Exception e){}
		
		return ris;
	}
	
	public String[][] getTableToElab()
	{
		String[][] list = null;
		String a = "";
		String b = "";
		int idx = 0;
		try {
			if(this.localMap != null)
			{
				list = new String[this.localMap.size()][2];
				Iterator i = this.localMap.keySet().iterator();
				while(i.hasNext())
				{
					a = (String)i.next();
					b = (String)this.localMap.get(a);
					list[idx][0] = a;
					list[idx][1] = b;
					idx++;
				}
			}
		}
		catch(Exception e) {
			list = new String[0][0];
		}
		
		return list;
	}
	
	public String getManageTable(String tabName) {
		return (String)this.localMap.get(tabName);
	}
	
	public String getPath() {
		return BaseConfig.getCarelPath() + this.configuration.getProperty("path") + File.separator; 
	}
	
	private void loadConfiguration(XMLNode xmlLocal)
	{
		XMLNode xml = null;
		String name = "";
		String clas = "";
		
		if(xmlLocal != null)
		{
			for(int i=0; i<xmlLocal.size(); i++)
			{
				xml = xmlLocal.getNode(i);
				if(xml != null)
				{
					name = xml.getAttribute("name");
					clas = xml.getAttribute("value");
					configuration.put(name,clas);
				}
			}
		}
	}
	
	private void loadConf(XMLNode xmlLocal)
	{
		XMLNode xml = null;
		String name = "";
		String clas = "";
		
		if(xmlLocal != null)
		{
			for(int i=0; i<xmlLocal.size(); i++)
			{
				xml = xmlLocal.getNode(i);
				if(xml != null)
				{
					name = xml.getAttribute("name");
					clas = xml.getAttribute("class");
					localMap.put(name,clas);
				}
			}
		}
	}
}
