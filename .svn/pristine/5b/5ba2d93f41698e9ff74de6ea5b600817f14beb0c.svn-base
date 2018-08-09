package com.carel.supervisor.remote.commlayer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.support.Information;
import com.carel.supervisor.dispatcher.enhanced.SyncIdentIPList;
import com.carel.supervisor.dispatcher.enhanced.SyncLog;
import com.carel.supervisor.remote.bean.SynchBeanList;
import com.carel.supervisor.remote.engine.connection.ActiveConnections;
import com.carel.supervisor.remote.engine.impl.ImpEnanchedDevice;
import com.carel.supervisor.remote.engine.impl.RichSync;
import com.carel.supervisor.remote.manager.RasServerMgr;

public class CommLayerMgr 
{
	private Map curComm = null;
	private String typeSoft = null;
		
	public CommLayerMgr()
	{
		this.curComm = new HashMap();
		this.typeSoft = null;
	}
	
	public void setTypeSoft(String sft) {
		this.typeSoft = sft;
	}
	
	public boolean startCommunication(String proto,String port,String root,String d,String i,String r,String cert)
	{
		System.setProperty("javax.net.ssl.trustStore",cert);
		String sCode = getDefaultLanguage();
		boolean ris = true;
		
		/*
		 * In caso provenga dalla mappa del remoto, conosco a priori il tipo 
		 * di dispositivo a cui mi voglio collegare.
		 */ 
		if(this.typeSoft != null)
		{
			if(this.typeSoft.equalsIgnoreCase("PVP"))
			{
				if((isPlantVisorPro(proto+"://"+r+":"+port+"/"+root, r)))
					startCommunicationPVP(proto,port,root,d,i,r,sCode,cert);
				else
					ris = false;
			}
			else
			{
				startCommunicationPVE(r,sCode,true,d);
				RasServerMgr.getInstance().closeIncoming(d);
			}
		}
		else
		{
			if(isPlantVisorPro(proto+"://"+r+":"+port+"/"+root, r))
				startCommunicationPVP(proto,port,root,d,i,r,sCode,cert);
			else
			{
				startCommunicationPVE(r,sCode,true,d);
				RasServerMgr.getInstance().closeIncoming(d);
			}
		}
		return ris;
	}
	
	/*
	 * Enhanced Block
	 */
	public void startCommunicationDirectPVE(String ipLocal,String device)
	{
		startCommunicationPVE(ipLocal,getDefaultLanguage(),false,device);
	}
	
	private String getDefaultLanguage()
	{
		String sCode = "EN_en";
		try {
			sCode = LangUsedBeanList.getDefaultLanguage(1);
		}
		catch(Exception e){
		}
		return sCode;
	}
	
	private void startCommunicationPVE(String ipLocal,String lang,boolean close,String device)
	{
		Vector Alarms = new Vector();
		SiteInfo si = null;
		SyncLog sl = null;
		int count = 10;
		String sIdent = SyncIdentIPList.getIstance().getIdent(ipLocal);
		while(sIdent == null && count != 0)
		{
			try {
				Thread.sleep(1000);
				count--;
			}
			catch(Exception e){
			}
			sIdent = SyncIdentIPList.getIstance().getIdent(ipLocal);;
		}	
		
		try {
			si = SiteInfoList.authenticateLocal(null,sIdent,sIdent,"PVE");
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		if(si != null)
		{
			// Notify to client incoming call
			// Incoming.getInstance().addConnClient(device,si.getId());
			ActiveConnections.getInstance().putClientOnConnection(device,""+si.getId());
			
			/*
             * Controllo sulla licenza.
             * Se la licenza è valida e il periodo di prova non è scaduto
             * effettuo la sincronizzazione dei dati.
             */
			if(Information.getInstance().canStartEngineLight())
			{
				// Update Time last DialUp
				try {
					SiteInfoList.updateTimeLastDialup(null,si.getId(),String.valueOf(System.currentTimeMillis()));
				}
				catch(Exception e) {
					Logger logger = LoggerMgr.getLogger(this.getClass());
					logger.error(e);
				}
				// End
				
				// Check site status
				int state = getPVEStatus(ipLocal);
				try {
					SiteInfoList.updateSiteStatus(null,si.getId(),state);
				}
				catch(Exception e) {
					Logger logger = LoggerMgr.getLogger(this.getClass());
					logger.error(e);
				}
				// End
				
				String[] tables = SynchBeanList.getTableConf(null,si.getId());
				if(tables != null && tables.length > 0)
				{
					sl = new SyncLog(ipLocal,21,"PVRemote",si.getPassword());
					
					if((sl.DoAlign(Alarms)) && (Alarms != null))
					{
						if(close)
						{
							String path = RasServerMgr.getInstance().getPathServices();
							ActiveConnections.getInstance().closeConnection("","",path,device);
						}
						
						try 
						{
							ImpEnanchedDevice imp = new ImpEnanchedDevice();
							imp.importer(sl,Alarms,new Integer(si.getId()),lang);
							RichSync.importRichSync(sl.loadPVPexport(), si.getId());
							SiteInfoList.updateTimeLastConnection(null,si.getId(),String.valueOf(System.currentTimeMillis()));
						}
						catch(Exception e)
						{
							Logger logger = LoggerMgr.getLogger(this.getClass());
							logger.error(e);
						}
					}
					sl.Finalize();
				}
			}
			else
			{
				/*
				 * Se devo chiudere, chiudo.
				 */
				if(close)
				{
					String path = RasServerMgr.getInstance().getPathServices();
					ActiveConnections.getInstance().closeConnection("","",path,device);
				}
			}
		}
	}
	
	/*
	 * Pro Block
	 */
	private void startCommunicationPVP(String proto,String port,String root,String d,String i,String r,String lang,String cert)
	{
		CommLayerHw commHw = new CommLayerHw(proto,port,root,d,i,r,lang,cert);
		commHw.startPoller();
		curComm.put(d,commHw);
	}
	
	private boolean isPlantVisorPro(String pvpUrl, String ipRemote)
	{
		HttpsURLConnection conn = null;
		boolean isPvp = false;
		
		try
		{
			URL url = new URL(pvpUrl);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setAllowUserInteraction(true); 
			conn.setHostnameVerifier(new HostProNameVerifier());
			
			BufferedOutputStream bof = new BufferedOutputStream(conn.getOutputStream());
			bof.write(("remote="+ipRemote+"&cmd=check").getBytes());
			bof.flush();
			bof.close();
			
			BufferedInputStream bif = new BufferedInputStream(conn.getInputStream());				
			byte[] buffer = new byte[bif.available()];
			bif.read(buffer);
			bif.close();
			
			String response = new String(buffer);
			if(response != null && response.equalsIgnoreCase("OK"))
				isPvp = true;
			else
				isPvp = false;
		}
		catch(Exception e)
		{
			isPvp = false;
			
			if(conn != null)
				conn.disconnect();
			conn = null;
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		return isPvp;
	}
	
	private int getPVEStatus(String ipLocal)
	{
		CommLayerHw comm = new CommLayerHw("http","80","","","",ipLocal,"","");
		return comm.checkPveStatus();
	}
}
