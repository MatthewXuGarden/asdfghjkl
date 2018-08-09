package com.carel.supervisor.dataaccess.support;

import java.util.Date;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.crypter.Crypter;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.system.SystemInfoExt;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.license.License;

public class Information 
{
	private static Information me = new Information();
	private static final long DAY = 86400000L;
	private static final long CD  = 30L;
	
	private long installationTime = 0L;
	private long todayTime = 0L;
	private long countDown = 0L;
	
	private boolean memoryValidity = false;
	
	public static Information getInstance() {
		return me;
	}
	
	private Information() {
		todayUpdate();
		load();
		check();
	}
	
	public void periodic() {
		if(this.countDown != 0)
		{
			todayUpdate();
			load();
			check();
		}
	}
	
	/*
	 * Aggiorna la data corrente
	 */
	private void todayUpdate()
	{
		String sql = "update hsproduct set today=current_date";
		try {
			DatabaseMgr.getInstance().executeStatement(null,sql,null);
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(Information.class);
			logger.error(e);
		}
	}
	
	/*
	 * Carica in memoria le informazioni relative al periodo di prova
	 */
	private void load()
	{
		String sql = "select * from hsproduct";
		RecordSet rs = null;
		Record r = null;
		
		Date inst = null;
		Date today = null;
		String count = "";
		
		try {
			rs = DatabaseMgr.getInstance().executeQuery(null,sql);
			if(rs != null && rs.size() > 0)
				r = rs.get(0);
			
			if(r != null)
			{
				inst = (Date)r.get("installation");
				today = (Date)r.get("today");
				count = (String)r.get("counter");
				
				count = Crypter.decryptRAS4DB(count);
				
				this.installationTime = inst.getTime();
				this.todayTime = today.getTime();
				
				try {
					this.countDown = Integer.parseInt(count);
				}
				catch(Exception e) {}
			}
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(Information.class);
			logger.error(e);
		}
	}
	
	/*
	 * Esegui il controllo sui dati relativi al periodo di prova
	 * - Current_date - Install_date
	 * - La differenza deve essere un numero positivo
	 * - 30 - differenza
	 * - Se la differenza è un numero positivo il trial è ancora valido
	 * - Se la differenza è un numero negativo il trial è scaduto. Rimuove la riga dalla tabella
	 */
	private void check()
	{
		if(this.todayTime > 0 && this.installationTime > 0)
		{
			long daysLong = (long)(this.todayTime - this.installationTime);
			
			try 
			{
				long days = (daysLong/DAY);
				if(days < 0)
					removeTrialPeriod();
				else
				{
					days = (CD-days);
					if(days > 0)
					{
						this.countDown = days;
					}
					else
					{
						this.countDown = 0;
						removeTrialPeriod();
					}
				}
			}
			catch(Exception e) {
				Logger logger = LoggerMgr.getLogger(Information.class);
				logger.error(e);
			}
		}
	}
	
	/*
	 * Rimuove la riga dalla tabella HSPRODUCT nel caso in cui 
	 * il periodo di prova sia scaduto.
	 */
	private void removeTrialPeriod()
	{
		String sql = "delete from hsproduct";
		try
		{
			DatabaseMgr.getInstance().executeStatement(null,sql,null);
			clearInformation();
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(Information.class);
			logger.error(e);
		}
	}
	
	private void clearInformation()
	{
		this.countDown = 0L;
		this.installationTime = 0L;
		this.todayTime = 0L;
	}
	
	public boolean valid()
	{
		boolean ris = false;
    	String sn  = BaseConfig.getProductInfo("productcode");
    	String key = BaseConfig.getProductInfo("activation");
    	String cp =  BaseConfig.getProductInfo("cp");
    	String mac = SystemInfoExt.getInstance().getMacAddress();
    	ris = prvValid(sn, key, cp, mac, false);
    	
    	// Set memory validity
    	this.memoryValidity = (ris || isTrialValid());
    	
    	return ris;
	}
	
	private boolean prvValid(String sn, String key, String cp, String mac, boolean isPlugin)
	{
		boolean ris = false;
		boolean block = false;
    	char[] aSn = new char[12];
    	char[] aKy = new char[12];
    	char[] aMc = new char[12];
    	
//    	String sn  = BaseConfig.getProductInfo("productcode");
//    	String key = BaseConfig.getProductInfo("activation");
//    	String cp =  BaseConfig.getProductInfo("cp");
//    	String mac = SystemInfoExt.getInstance().getMacAddress();
    	
    	if(sn != null && sn.length() == 12)
    		aSn = sn.toCharArray();
    	else
    		block = true;
    	
    	if(key != null && key.length() == 12)
    		aKy = key.toCharArray();
    	else
    		block = true;
    	
    	if(mac != null && mac.length() == 12)
    		aMc = mac.toCharArray();
    	else
    		block = true;
    	
    	if(block)
    		return false;
    	else
    	{
    		if(checkCP(sn,cp))
    		{
    			ris = License.checkKEY(aSn,aMc,aKy);
    			if(ris&&(!isPlugin))
    				removeTrialPeriod();
    		}
    		return ris;
    	}
	}
	
	//registrazione prodotto o stringa come errore riscontrato:
	public String validStr()
	{
		String validity = "";
		
		String sn  = BaseConfig.getProductInfo("productcode");
    	String key = BaseConfig.getProductInfo("activation");
    	String cp =  BaseConfig.getProductInfo("cp");
    	String mac = SystemInfoExt.getInstance().getMacAddress();
    	
    	validity = prdValid(sn, key, cp, mac, false); //non � un plugin
    	
    	// Set memory validity
    	this.memoryValidity = (("ok".equals(validity)) || isTrialValid());
    	
    	return validity;
	}
	
	//metodo che ritorna l'eventuale errore (particolareggiato) di mancata registrazione prodotto
	//oppure l'avvenuta registrazione:
	public String prdValid(String sn, String key, String cp, String mac, boolean isPlugin)
	{
		String result = "";
		
    	char[] aSn = new char[12];
    	char[] aKy = new char[12];
    	char[] aMc = new char[12];
    	
    	if(sn != null && sn.length() == 12)
    		aSn = sn.toCharArray();
    	else
    		result = "pvpcode"; //serial number
    	
    	if(key != null && key.length() == 12)
    		aKy = key.toCharArray();
    	else
    		result = "activation"; //activation code
    	
    	if(mac != null && mac.length() == 12)
    		aMc = mac.toCharArray();
    	else
    		result = "macaddress"; //mac address
    	
    	if("".equals(result))
    	{
    		if(checkCP(sn,cp)) //tipologia prodotto
    		{
    			if (License.checkKEY(aSn, aMc, aKy)) //controllo licenza
    			{
    				result = "ok"; //registrazione valida
    			}
    			else result = "checkKEY";
    			
    			if(("ok".equals(result)) && (!isPlugin))
    				removeTrialPeriod();
    		}
    		else
    		{
    			if (!isPlugin)
    				result = "cp"; //tipologia prodotto
    			else
    				result = "pcode"; //tipo plugin
    		}
    	}
    	
		return result;
	}
	
	public boolean isTrialValid() {
		return this.countDown > 0;
	}
	
	public long getCountDown() {
		return this.countDown;
	}
	
	public boolean canStartEngine() {
		this.memoryValidity = (valid() || isTrialValid()); 
		return this.memoryValidity;
	}
	
	/**
	 * Adapted for new registration procedure.
	 * CP value could be of 2 or 4 character, so the SUBSTRING parameters start
	 * from position 2 until 2+CP.LENGHT.
	 */
	private boolean checkCP(String sn,String cp)
	{
		boolean ris = false;
		if(sn != null && sn.length() == 12 && cp != null)
		{
			if(sn.substring(2,(2+cp.length())).equals(cp))
				ris = true;
		}
		return ris;
	}
	
	public boolean validPlugin(String sn, String key, String cp, String mac)
	{    	
    	return prvValid(sn, key, cp, mac, true);
	}
	
	public String validPluginStr(String sn, String key, String cp, String mac)
	{
		return prdValid(sn, key, cp, mac, true);
	}
	
	/*
	 * Questo metodo è stato aggiunto per controllare la validità
	 * della licenza e del periodo di prova direttamente direttamente 
	 * da una variabile in memoria.
	 * In questo modo si evita di richiamare l'algoritmo di validità.
	 * Questo metodo viene utilizzato dal Remoto in fase di connessione dei Locali.
	 */
	public boolean canStartEngineLight() {
		return this.memoryValidity;
	}
}
