package com.carel.supervisor.dispatcher.plantwatch1;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ResourceLoader;
import com.carel.supervisor.base.config.ResourceNotFoundException;
import com.carel.supervisor.base.xml.*;

public class PlantWatch extends Thread {
	private boolean Terminated = false;
	private boolean syncDone = false;
	private boolean BlockingError = true;
	private boolean connected = false;
	
	PlantWatchComm pwc = null;
	PlantWatchCommRec pwcr = new PlantWatchCommRec();
	public final int TYPE_DIGITAL 	= 1;
	public final int TYPE_REAL 		= 2;
	public final int TYPE_INTEGER 	= 3;
	IPlantWatchEvent pe = null;
	
	String Modem = "";	PlantWatch(String name, String ModemToWait, IPlantWatchEvent pe) throws Exception
	{
		super.setName( name);
		Modem = ModemToWait;
		
			this.pe = pe;
			PlantWatchCommRec pwcresp = new PlantWatchCommRec();
			
			pwcr.MessageId = PlantWatchIDs.REQ_KEEP_ALIVE;
			pwc = PlantWatchComm.getIstance();
			pwc.setAddress( "127.0.0.1");
			pwc.setPort( 2012);
			pwcr.Modem = PlantWatchStringToCharArray.Convert( this.Modem,512);
			if(!QuestionAndAnswer(30, 5, PlantWatchIDs.REQ_MODEM_CONF, pwcresp))
				throw new Exception("Unable to contact PlantWatch.exe module.");
			
		
			
	}
	
	private boolean QuestionAndAnswer(int timeout, int retrycount, int messageid, PlantWatchCommRec pwcresp)
	{
		boolean RecordRecived = false;
		try
		{
			
			synchronized (pwcr) 
			{
				if(pwcr.MessageId!=PlantWatchIDs.REQ_KEEP_ALIVE)
					return false;
				pwcr.MessageId = messageid ;
				pwc.SendRecord( pwcr);
				
				int count = retrycount;//lóperazione puó essere molto lunga perché devo 
				//cancellare tutta la lista degli storici
				while(pwc.ReadData(pwcresp,timeout) || (count>0))
				{
					if(pwcresp.MessageId == messageid)
					{
						//Mi ha risposto alla richiesta di letttura della variabile
						RecordRecived = true;
						break;
					}
					count--;
				}
				
				pwcr.MessageId=PlantWatchIDs.REQ_KEEP_ALIVE;
			}//esco dal mutex e aspetto con timeout il valore.
		}catch(Exception e)
		{
			pwcr.MessageId=PlantWatchIDs.REQ_KEEP_ALIVE;
			e.printStackTrace();
			return false;
		}
		return RecordRecived;
	}
	
	public synchronized boolean DisconnectDevice()
	{
		if(!connected)
			return false;
		PlantWatchCommRec pwcresp = new PlantWatchCommRec();
		
		if(QuestionAndAnswer(30, 5, PlantWatchIDs.REQ_CLOSE_CONNECTION, pwcresp))
		{
			syncDone = false;
			connected = false;
			if(pe!=null)
				pe.OnDisconnected( pwcresp.Ident);
			System.out.println("PlantWatch disconneted.");
			return true;
		}
		return false;
		
	}
	
	public synchronized boolean CloseService()
	{
		PlantWatchCommRec pwcresp = new PlantWatchCommRec();
		
		if(QuestionAndAnswer(30, 5, PlantWatchIDs.REQ_DESTRY_MODULE, pwcresp))
		{
			syncDone = false;
			connected = false;
			Terminated = true;
			System.out.println("PlantWatch destroyed.");
			return true;
		}
		return false;
		
	}
	
	
	private boolean ConnectToDevice(int CommPort,String Modem,String Tel, int Ident, byte SerialModem)
	{
//		Solo se lo stato é in Keep alive allora 
		//posso eseguire una azione
		if(syncDone)
			return false;
		
		boolean RecordRecived = false;
		PlantWatchUserList pwul = PlantWatchUserList.getIstance();
		PlantWatchCommRec pwcresp = new PlantWatchCommRec();
		
		synchronized (pwcr) {
			if(pwcr.MessageId!=PlantWatchIDs.REQ_KEEP_ALIVE)
				return false;
			
			String UserName = "";
			String Password = "";
			//setModem(Modem);
			if(pwul.GetUserPassword(Ident, UserName, Password))
			{
				pwcr.Ident = Ident;
				pwcr.Address = 0;
				pwcr.bValue = 0;
				pwcr.dValue = 0;
				pwcr.lValue = 0;
				pwcr.CommPort = CommPort;
				pwcr.Result = 0;
				pwcr.VarType = 0;
				pwcr.Unit = 0;
				pwcr.SerialModem = SerialModem;
				//pwcr.FromDate= 1151318266062L;
				//devo copiare carattere per carattere altrimenti cambia la dimensione 
				//dell'array
				if(SerialModem==0)
				{
					pwcr.Tel= PlantWatchStringToCharArray.Convert( Tel,200);
					pwcr.Modem = PlantWatchStringToCharArray.Convert( Modem,512);
					
				}
				pwcr.UserName = PlantWatchStringToCharArray.Convert( UserName,200);
				pwcr.Password = PlantWatchStringToCharArray.Convert( Password,200);
				
				//alla fine metto il thread in richiesta di connessione
//				alla fine metto il thread in richiesta di connessione
				if(QuestionAndAnswer(30, 2, PlantWatchIDs.REQ_CONNECTION, pwcresp))
				{
					//mi ha risposto alla richieta di connessione adesso aspetto che il device si collega.
					//PlantWatchIDs.LINE_CONNECTED
					int count = 5;//lóperazione puó essere molto lunga perché devo 
					//cancellare tutta la lista degli storici
					while(pwc.ReadData(pwcresp,30) || (count>0))
					{
						if(pwcresp.MessageId == PlantWatchIDs.LINE_CONNECTED)
						{
							//Mi ha risposto alla richiesta di letttura della variabile
							connected = true;
							if(pe!=null)								
								pe.OnConnected( pwcresp.Ident);
							
							System.out.println("Device connected.");
							return true;
						}
						count--;
					}
					
				}	
				
			}else
				return false;
		}
		return true;	
		
	}
	public synchronized boolean ConnectToDevice(String Modem,String Tel, int Ident)
	{
		return ConnectToDevice(0,Modem,Tel,Ident, (byte)0);
	}
	public synchronized boolean ConnectToDevice(int CommPort, int Ident)
	{
		return ConnectToDevice(CommPort,"","",Ident, (byte)1);
	}
	//Prima di tutto dopo aver fatto partire il thread bisogna inizializzare 
	//la lista deglu utenti e password.
	public synchronized void AddUserPasswordList(Map mp_UserPassword)
	{
		try{
			synchronized (pwcr) 
			{
				
				Set mapping = mp_UserPassword.keySet();
				Iterator i = mapping.iterator();
				PlantWatchCommRec pwcresp = new PlantWatchCommRec();
				
				while(i.hasNext())
				{
					PlantWatchUser rec = (PlantWatchUser)mp_UserPassword.get(i.next());
					PlantWatchUser newrec = new PlantWatchUser();
					newrec = (PlantWatchUser)rec.clone();
					PlantWatchUserList pwul = PlantWatchUserList.getIstance();
					pwul.Add( newrec);
					pwcr.Ident    = rec.Ident; 
					pwcr.UserName = PlantWatchStringToCharArray.Convert( rec.UserName,200);
					pwcr.Password = PlantWatchStringToCharArray.Convert( rec.Password,200);
					//pwcr.FromDate= 1151318266062L;
					//pwcr.ToDate= 1151318266062L;
					//pwcr.Modem = PlantWatchStringToCharArray.Convert( this.Modem,512);
					QuestionAndAnswer(20, 2, PlantWatchIDs.REQ_USER_CONF, pwcresp);
					
				}
				
					
			}
		}catch(Exception e)
		{
			e.printStackTrace(); 
		}
	}
	/*LoadAlarmsFormXMLFile(	
	String dirpath, 
	String xmlfile, 
	Date   fromDate,
	PlantWatchAlarmList pwal)*/
	public synchronized void GetAllarmsFormDate(String dirpath, 
												String xmlfile, 
												Date   fromDate,
												PlantWatchAlarmList pwal)
	{
		if(syncDone && connected)
		{
			PlantWatchCommRec pwcresp = new PlantWatchCommRec();
			synchronized (pwcr) 
			{
				if(pwcr.MessageId!=PlantWatchIDs.REQ_KEEP_ALIVE)
					return ;
				pwcr.DirPath= PlantWatchStringToCharArray.Convert( dirpath,512);
				
			}
			if(QuestionAndAnswer(30, 5, PlantWatchIDs.REQ_ALARMS, pwcresp))
			{
				System.out.println("PlantWatch alarms exported.");
				
			}	
		}
		LoadXMLFile.LoadAlarmsFormXMLFile( dirpath, xmlfile, fromDate, pwal);
	}

	public synchronized PlantWatchSiteConfig GetSiteConfig(String dirpath)
	{
		PlantWatchSiteConfig pwsc = new PlantWatchSiteConfig();
		LoadXMLFile.LoadConfFromXMLFile( dirpath,
										 "siteconfiguration.xml",
										 pwsc
				 );
		return pwsc;
	}
	
	
	public synchronized PlantWatchVarUnitType GetVarUnitType(String dirpath, String type )
	{
		PlantWatchVarUnitType pwuvt = new PlantWatchVarUnitType();
		LoadXMLFile.LoadUnitVarTypeFromXMLFile(dirpath, type+".xml", pwuvt);
		return pwuvt;
	}
	
	public synchronized boolean ReadHistoryVar(	String dirpath, 
												long fromDate, 
												long toDate, 
												PlantWatchHistoryVars pwhvs)
	{
		
		if(syncDone && connected)
		{
			PlantWatchCommRec pwcresp = new PlantWatchCommRec();
			
			synchronized (pwcr) 
			{
				if(pwcr.MessageId!=PlantWatchIDs.REQ_KEEP_ALIVE)
					return false;
				pwcr.FromDate = fromDate;
				pwcr.ToDate = toDate;
				
				pwcr.DirPath= PlantWatchStringToCharArray.Convert( dirpath,512);
				if(!QuestionAndAnswer(30, 5, PlantWatchIDs.REQ_READ_ALL_VAR, pwcresp))
					  return false;	
				
			}
		}
		//Adesso carico i file xml degli storici
		
		//per prima cosa carico la configurazione dal disco 
		PlantWatchSiteConfig pwsc = GetSiteConfig(dirpath);
		PlantWatchSiteConfigUnit pwcu = new PlantWatchSiteConfigUnit(); 
		while(pwsc.HasMore(pwcu))
		{
			PlantWatchVarUnitType pwuvt = GetVarUnitType(dirpath, pwcu.type);
			//pwuvt contiene adesso le variabili di quel tipo e anche possibile ricavare quelle storicizzate.
			Map mp_VarDataLogging = Collections.synchronizedMap( new TreeMap());
			pwuvt.GetDataLoggingVar(mp_VarDataLogging); 
			Set mappings = mp_VarDataLogging.keySet();
			java.util.Iterator i = mappings.iterator();
			
			while(i.hasNext())
			{
				PlantWatchVar rec = (PlantWatchVar)mp_VarDataLogging.get( i.next());
				//adesso la descrizione contiene il vero nome della variabile storicizzata
				PlantWatchVar r = new PlantWatchVar();
				if(pwuvt.GetVar(rec.description, r))
				{
					String filename = "unit"+Integer.valueOf(pwcu.id).toString()+"_"+
									   Integer.valueOf(r.address).toString()+"_";
					switch(r.type)
					{
						case TYPE_DIGITAL:
							filename +="D"; 
							break;
						case TYPE_INTEGER:
							filename +="I";
							break;
						case TYPE_REAL:
							filename +="A";
							break;
							
					}
					//filename = dirpath+ "/"+filename+".xml";
					//adesso ho il nome del file da caricare
					PlantWatchHistoryVarList pwhvl = new PlantWatchHistoryVarList();
					LoadXMLFile.LoadHistoryVarFromFile(dirpath, filename+".txt",  pwhvl);
					pwhvs.Add( pwcu.id, r.type, pwhvl);
					
				}
				
			}
			
			mp_VarDataLogging = null;
			pwcu = null;
			pwcu = new PlantWatchSiteConfigUnit();
		}
		//
		// poi per ogni unitá configurata carico le variabili storiche
		//
		
		
		return true;
		
	}
	
	public synchronized boolean ReadHistoryVar(	String dirpath, 
			long fromDate, 
			long toDate,
			int unit,
			PlantWatchHistoryVars pwhvs)
	{
		if(syncDone && connected)
		{
			PlantWatchCommRec pwcresp = new PlantWatchCommRec();
			
			synchronized (pwcr) 
			{
				if(pwcr.MessageId!=PlantWatchIDs.REQ_KEEP_ALIVE)
					return false;
				pwcr.FromDate = fromDate;
				pwcr.ToDate = toDate;
				
				pwcr.DirPath= PlantWatchStringToCharArray.Convert( dirpath,512);
				if(!QuestionAndAnswer(30, 5, PlantWatchIDs.REQ_READ_ALL_VAR, pwcresp))
					  return false;	
				
			}
		}
		//Adesso carico i file xml degli storici
		
		//per prima cosa carico la configurazione dal disco 
		PlantWatchSiteConfig pwsc = GetSiteConfig(dirpath);
		PlantWatchSiteConfigUnit pwcu = new PlantWatchSiteConfigUnit();
		 
		if(pwsc.GetUnitConf( unit, pwcu))
		{
			PlantWatchVarUnitType pwuvt = GetVarUnitType(dirpath, pwcu.type);
			//pwuvt contiene adesso le variabili di quel tipo e anche possibile ricavare quelle storicizzate.
			Map mp_VarDataLogging = Collections.synchronizedMap( new TreeMap());
			pwuvt.GetDataLoggingVar(mp_VarDataLogging); 
			Set mappings = mp_VarDataLogging.keySet();
			java.util.Iterator i = mappings.iterator();
			
			while(i.hasNext())
			{
				PlantWatchVar rec = (PlantWatchVar)mp_VarDataLogging.get( i.next());
				//adesso la descrizione contiene il vero nome della variabile storicizzata
				PlantWatchVar r = new PlantWatchVar();
				if(pwuvt.GetVar(rec.description, r))
				{
					String filename = "unit"+Integer.valueOf(pwcu.id).toString()+"_"+
									   Integer.valueOf(r.address).toString()+"_";
					switch(r.type)
					{
						case TYPE_DIGITAL:
							filename +="D"; 
							break;
						case TYPE_INTEGER:
							filename +="I";
							break;
						case TYPE_REAL:
							filename +="A";
							break;
							
					}
					//filename = dirpath+ "/"+filename+".xml";
					//adesso ho il nome del file da caricare
					PlantWatchHistoryVarList pwhvl = new PlantWatchHistoryVarList();
					LoadXMLFile.LoadHistoryVarFromFile(dirpath, filename+".txt",  pwhvl);
					pwhvs.Add( pwcu.id, r.type, pwhvl);
					
				}
				
			}
			
			mp_VarDataLogging = null;
			pwcu = null;
			pwcu = new PlantWatchSiteConfigUnit();
		}else
			return false;
		//
		// poi per ogni unitá configurata carico le variabili storiche
		//
		
		
		return true;
	}

	public synchronized boolean ReadUnitVar(int unit, String dirpath, PlantWatchUnitVars pwvs){
			if(syncDone && connected)
			{
				//leggo le variabili on line
				//per prima cosa faccio la richiesta
				if(syncDone && connected)
				{
					PlantWatchCommRec pwcresp = new PlantWatchCommRec();
					
					synchronized (pwcr) 
					{
						if(pwcr.MessageId!=PlantWatchIDs.REQ_KEEP_ALIVE)
							return false;
						pwcr.Unit = unit;
						pwcr.DirPath= PlantWatchStringToCharArray.Convert( dirpath,512);
						if(!QuestionAndAnswer(30, 2, PlantWatchIDs.REQ_READ_DEVICE_VAR, pwcresp))
						{
							System.out.println("Error reading all var response");  
							return false;	
						}
					}
			}
			//Se non sono connesso adesso leggo le variabili off line altrimenti sono quelle on line
			LoadXMLFile.LoadVarFromXMLFile(dirpath, 
										   "digitals_unit_"+Integer.valueOf( unit).toString()+ ".xml", 
										   TYPE_DIGITAL, 
										   pwvs);
			LoadXMLFile.LoadVarFromXMLFile(dirpath, 
										   "integers_unit_"+Integer.valueOf( unit).toString()+ ".xml", 
										   TYPE_INTEGER, 
										   pwvs);
			LoadXMLFile.LoadVarFromXMLFile(dirpath, 
										   "reals_unit_"+Integer.valueOf( unit).toString()+ ".xml", 
										   TYPE_REAL, 
										   pwvs);
		}else
		{
			//Se non sono connesso adesso leggo le variabili off line altrimenti sono quelle on line
			LoadXMLFile.LoadVarFromXMLFile(dirpath, 
										   "digitals.xml",
										   unit,
										   TYPE_DIGITAL, 
										   pwvs);
			LoadXMLFile.LoadVarFromXMLFile(dirpath, 
										   "integers.xml",
										   unit,
										   TYPE_INTEGER, 
										   pwvs);
			LoadXMLFile.LoadVarFromXMLFile(dirpath, 
										   "reals.xml",
										   unit,
										   TYPE_REAL, 
										   pwvs);
			
		}
		return true;
	}
	public synchronized boolean ReadVar(int Unit, int Address, int Type, VarDouble val)
	{
		PlantWatchCommRec pwcresp = new PlantWatchCommRec();
		
		synchronized (pwcr) 
		{
			if(pwcr.MessageId!=PlantWatchIDs.REQ_KEEP_ALIVE)
				return false;
			pwcr.Unit = Unit;
			pwcr.Address = Address;
			pwcr.VarType = (byte)Type;
			if(!QuestionAndAnswer(30, 3, PlantWatchIDs.REQ_READ_VAR, pwcresp))
			{
				System.out.println("Error reading var response");
				return false;	
			}
			switch(Type)
			{
				case TYPE_DIGITAL:
					val.setVal(pwcresp.bValue);
					break;
				case TYPE_INTEGER:
					
					val.setVal(pwcresp.lValue);
					break;
					
				case TYPE_REAL:
					val.setVal(pwcresp.dValue);
					break;
			}
		
			if(pwcresp.Result == PlantWatchIDs.RES_OK)
				return true;
			else
				return false;
		}//esco dal mutex e aspetto con timeout il valore.
		//Il ReadData lo posso fare qui in quanto sono in un mutex con il tread che cambia gli stati
		
	}
	public synchronized boolean SendVar(int Unit, int Address, int Type, Double val)
	{
		PlantWatchCommRec pwcresp = new PlantWatchCommRec();	
		
		synchronized (pwcr) 
		{
			if(pwcr.MessageId!=PlantWatchIDs.REQ_KEEP_ALIVE)
				return false;
			pwcr.Unit = Unit;
			pwcr.Address = Address;
			pwcr.VarType = (byte)Type;
			switch(Type)
			{
				case TYPE_DIGITAL:
					pwcr.bValue = val.byteValue();
					break;
				case TYPE_INTEGER:
					
					pwcr.lValue = val.longValue();
					break;
					
				case TYPE_REAL:
					pwcr.dValue = val.doubleValue();
					break;
			}
			
			if(!QuestionAndAnswer(30, 2, PlantWatchIDs.REQ_WRITE_VAR, pwcresp))
			{
				System.out.println("Error reading var response");
				return false;	
			}
			
			
		}//esco dal mutex e aspetto con timeout il valore.
		
		
		if(pwcresp.Result == PlantWatchIDs.RES_OK)
			return true;
		else
			return false;
	}
	
	public void run()
	{
		try
		{
			PlantWatchCommRec pwcresp = new PlantWatchCommRec();	
			while(!Terminated)
			{
									

				synchronized (pwcr) {
					if(Terminated)
						break;
					
					pwcresp.MessageId = PlantWatchIDs.RES_ERROR;
					if(!pwc.ReadData(pwcresp, 20))
					{
						System.out.println("No message form PlantWatch exe module");
						setBlockingError(true);
					}else
						setBlockingError(false);
//					PlantWatchIDs.REQ_MODEM_CONF;
				    //Se la connessione e' avvenuta allora porto lo stato in connesso
					
					switch(pwcresp.MessageId)
					{
						case PlantWatchIDs.CONNECTION_ESTABLISHED:
							System.out.println("PlantWatch syncronized.");
							syncDone = true;
							if(pe!=null)
								pe.OnSynchronized( pwcresp.Ident);
							//A questo punto dovrei fare una callback su di un oggetto che
							//gestisce le connesioni con il plantwatch
							break;
						case PlantWatchIDs.DISCONNECTED:
							syncDone = false;
							connected = false;
							if(pe!=null)
								pe.OnDisconnected( pwcresp.Ident);
							System.out.println("PlantWatch disconnected.");
							//A questo punto dovrei fare una callback su di un oggetto che
							//gestisce le connesioni con il plantwatch
							break;
						case PlantWatchIDs.LINE_CONNECTED:
							syncDone = false;
							connected = true;
							if(pe!=null)
								pe.OnConnected( pwcresp.Ident);
							System.out.println("PlantWatch connected.");
							break;
					}
					
					if(pwcresp.Result == PlantWatchIDs.RES_ERROR)
						System.out.println("Command failed");
					//alla fine ritorno in KeepAlive
					pwcr.MessageId = PlantWatchIDs.REQ_KEEP_ALIVE ;
					pwc.SendRecord( pwcr);	
					
					
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			pwcresp = null;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setTerminated(boolean terminated) {
		Terminated = terminated;
	}

	public boolean isSyncDone() {
		return syncDone;
	}

	public void setSyncDone(boolean connected) {
		syncDone = connected;
	}

	public boolean isBlockingError() {
		return BlockingError;
	}

	public void setBlockingError(boolean blockingError) {
		BlockingError = blockingError;
		if(BlockingError)
		{
			pwc.finalize();
			
			pwc = PlantWatchComm.getIstance();
			pwc.setAddress( "127.0.0.1");
			pwc.setPort( 2012);
			
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

}
