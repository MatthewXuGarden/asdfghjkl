package com.carel.supervisor.remote.engine.connection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.script.ScriptInvoker;
import com.carel.supervisor.dispatcher.DispatcherID;

public class ActiveConnections 
{
	private static ActiveConnections me = new ActiveConnections();
	
	private Map connections = null;
	private String ipLocalOut = null; 
	private String typeLocalOut = null;
	private int idLocalOut = 0;
	
	private ActiveConnections() {
		this.connections = new HashMap();
	}
	
	public static ActiveConnections getInstance() {
		return me;
	}
	
	/*
	 * IP LOCALE 
	 * della comunicazione in uscita dal remoto
	 */
	public String getIpLocalInOut() {
		return this.ipLocalOut;
	}
	
	public boolean isOutConActive() {
		return this.ipLocalOut != null;
	}
	
	/*
	 * TYPE LOCALE
	 * della comunicazione in uscita dal remoto
	 */
	public void setTypeLocalOut(String type) {
		this.typeLocalOut = type;
	}
	
	public String getTypeLocalOut() {
		return this.typeLocalOut;
	}
	
	/*
	 * ID LOCALE
	 * della comunicazione in uscita dal remoto
	 */
	public void setIdLocalOut(int id) {
		this.idLocalOut = id;
	}
	
	public int getIdLocalOut() {
		return this.idLocalOut;
	}
	
	/*
	 * Inserisce una nuova connessione
	 */
	public void putConnection(String modem,String type,String client, String locale, String remoto)
	{
		if(modem != null)
			modem = modem.trim();
		this.connections.put(modem,new Connection(modem,type,client,locale,remoto));
		
		if(type != null && type.equalsIgnoreCase("OUT"))
			this.ipLocalOut = locale;
	}
	
	/*
	 * Aggiorna il client della connessione sul quel modem
	 */
	public void putClientOnConnection(String modem,String idCl)
	{
		if(modem != null)
			modem = modem.trim();
		Connection con = (Connection)this.connections.remove(modem);
		if(con != null)
			con.setClient(idCl);
		if(con != null && con.getType().equals("OUT"))
		{
			int idLocal = 0;
			try {idLocal = Integer.parseInt(idCl);}catch(Exception e){}
			this.idLocalOut = idLocal;
		}
			 
		this.connections.put(modem,con);
	}
	
	/*
	 * Rimuove le informazione sulla connessione
	 */
	public void remConnection(String modem, boolean checkOut)
	{
		if(modem != null)
			modem = modem.trim();
		Connection con = (Connection)this.connections.remove(modem);
		
		if(checkOut)
		{
			if(con != null && con.getType().equalsIgnoreCase("OUT"))
				this.connections.put(modem,con);
		}
		else if(con != null && con.getType().equalsIgnoreCase("OUT") && con.getIpLocale().equalsIgnoreCase(this.ipLocalOut))
		{
			this.ipLocalOut = null;
			this.typeLocalOut = null;
			this.idLocalOut = 0;
		}
		
		
	}
	
	public String getIpLocale(String modem) 
	{
		if(modem != null)
			modem = modem.trim();
		Connection con =  (Connection)this.connections.get(modem);
		if(con != null)
			return con.getIpLocale();
		else
			return "NOP";
	}
	
	public String getIpRemote(String modem) 
	{
		if(modem != null)
			modem = modem.trim();
		Connection con =  (Connection)this.connections.get(modem);
		if(con != null)
			return con.getIpRemoto();
		else
			return "NOP";
	}
	
	public boolean isConnections() {
		return this.connections.size() > 0;
	}
	
	public String[] getConnClient() 
	{
		String[] ret = new String[this.connections.size()];
		int idx = 0;
		String key = "";
		Connection con = null;
		Iterator i = this.connections.keySet().iterator();
		
		while(i.hasNext())
		{
			key = (String)i.next();
			con = (Connection)this.connections.get(key);
			if(con != null)
				ret[idx++] = con.getClient();
		}	
		return ret;
	}
	
	public String getXmlConnClient() 
	{
		StringBuffer sb = new StringBuffer("<cls>");
		String[] idcl = getConnClient();
		for(int i=0; i<idcl.length; i++)
		{
			 if((idcl[i] != null) && (!idcl[i].equalsIgnoreCase("")))
				 sb.append("<cl>"+idcl[i]+"</cl>");
		}
		sb.append("</cls>");
		return sb.toString();
	}
	
	public boolean closeConnection(String user,String pass,String path,String modem)
	{
		boolean ris = false;
		if(modem != null && !modem.equalsIgnoreCase(""))
		{
			modem = modem.trim();
			int returnCode = 99;
			ScriptInvoker inv = new ScriptInvoker();
			String[] par = new String[]{"java","-classpath",path+"RasService.jar;"+path+"DispatcherLight.jar;",
				    					"com.carel.supervisor.service.Starter",modem};
			try 
			{
				returnCode = inv.execute(par,path+"RasServer.log");
				if(returnCode == DispatcherID.MODEM_CLOSE_OK)
				{
					ris = true;
					this.remConnection(modem,false);
				}
			}
			catch(Exception e) 
			{
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
		return ris;
	}
}
