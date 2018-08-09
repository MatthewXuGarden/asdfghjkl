package com.carel.supervisor.dispatcher.plantwatch1;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 */

/**
 * @author FabioV
 *
 */
import java.util.Collections;

public class PlantWatchUserList {
	private Map mp_UserPassword = Collections.synchronizedMap( new TreeMap());
	private static PlantWatchUserList me = null;
	
	private PlantWatchUserList()
	{
		me = null;
	}
	
	public static synchronized PlantWatchUserList getIstance() {
		if(me==null)
		{
			me = new PlantWatchUserList();
			return me;
		}else
			return me;
	}
	 /**
	 * Aggiunge un elemento alla <tt>Map</tt> degli utenti configurati.  
     * @param ident ident E' l'ident del nodo.
     * @param userName userName Il nome dell'utente <tt>PlantWatch</tt> (RemoteUser di solito).
     * @param password password Password dell'utente <tt>PlantWatch</tt> (4 di solito).
     * @return <tt>void</tt> 
     */
	public synchronized void Add(int ident, String userName, String password) 
	{
		PlantWatchUser rec = (PlantWatchUser)mp_UserPassword.get(Integer.valueOf(ident));
		if(rec == null)
		{
			rec = new PlantWatchUser();
			rec.Ident = ident;
			rec.UserName = userName;
			rec.Password = password;
			mp_UserPassword.put( Integer.valueOf(ident),rec);
		}
	}
	public synchronized Map getMap()
	{
		return mp_UserPassword;
	}
	public synchronized void Add(PlantWatchUser newrec) 
	{
		PlantWatchUser rec = (PlantWatchUser)mp_UserPassword.get(Integer.valueOf(newrec.Ident));
		if(rec == null)
		{
			mp_UserPassword.put( Integer.valueOf(newrec.Ident),newrec);
		}
	}
	public synchronized boolean GetUserPassword(int Ident, String UserName, String Password) 
	{
		PlantWatchUser rec = (PlantWatchUser)mp_UserPassword.get(Integer.valueOf(Ident));
		if(rec == null)
			return false;
		else{
			UserName = rec.UserName;
			Password = rec.Password;
			return true;
		}
	}

}
