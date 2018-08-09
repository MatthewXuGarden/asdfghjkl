package com.carel.supervisor.dispatcher.plantwatch1;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class PlantWatchVarUnitType {
	private Map mp_VarUnitType = Collections.synchronizedMap( new TreeMap());
	
	public synchronized void Add(String name, 
								 String description, 
								 int type, 
								 int address,
								 boolean dataLogging) 
	{
		PlantWatchVar rec = new PlantWatchVar();
		if(rec != null)
		{
			rec.address = address;
			rec.dataLogging = dataLogging;
			rec.description = description;
			rec.name = name;
			rec.type = type;
			mp_VarUnitType.put( name,rec);
		}
	}
	
	public boolean GetVar(String name, PlantWatchVar rec)
	{
		PlantWatchVar r = (PlantWatchVar)mp_VarUnitType.get(name);
	    if(r!=null)
	    {
	    	PlantWatchVar r1 = (PlantWatchVar)r.clone();
	    	rec.address= r1.address;
	    	rec.dataLogging = r1.dataLogging;
	    	rec.name = r1.name;
	    	rec.type = r1.type;
	    	return true;
	    }else
	    	return false;
	}
	
	public boolean GetDataLoggingVar(Map mp_DataLogging)
	{
		Set mappings = mp_VarUnitType.keySet();
		java.util.Iterator i = mappings.iterator();
		
		while(i.hasNext())
		{
			PlantWatchVar rec = (PlantWatchVar)mp_VarUnitType.get( i.next());
			PlantWatchVar r = new PlantWatchVar();
			r = (PlantWatchVar)rec.clone();
			if(r.name.indexOf("DataLogging")>=0)//solo le variabili che hanno nel nome dataloggin sono storicizzate
				mp_DataLogging.put(r.name ,r);//nella descrizione mi trovero' il nome della variabile
			
		}
		return true;
	}
	
	
	
}
