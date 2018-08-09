/**
 * 
 */
package com.carel.supervisor.dispatcher.plantwatch1;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author FabioV
 *
 */
public class PlantWatchHistoryVars {
	private Map mp_HistoryVars = Collections.synchronizedMap( new TreeMap());
	
	public synchronized void Add(int unit, int type, PlantWatchHistoryVarList pwuvl){
		//questa funzione carica un oggetto nella mp_HistoryVars con tutte la varibili storiche di quel tipo
		mp_HistoryVars.put(Integer.valueOf( unit).toString()+"_"+Integer.valueOf( type).toString(),pwuvl );
	}
	
	public synchronized boolean Get(int unit, int type, PlantWatchHistoryVarList pwuvl){
		//questa funzione carica un oggetto nella mp_HistoryVars con tutte la varibili storiche di quel tipo
		pwuvl = (PlantWatchHistoryVarList)mp_HistoryVars.get(Integer.valueOf( unit).toString()+"_"+Integer.valueOf( type).toString());
		if(pwuvl!=null)
			return true;
		else
			return false;
	}
	
	
}
