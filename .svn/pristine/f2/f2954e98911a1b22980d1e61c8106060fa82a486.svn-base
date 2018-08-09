package com.carel.supervisor.dispatcher.plantwatch1;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class PlantWatchSiteConfig {
	private Map mp_SiteConfig = Collections.synchronizedMap( new TreeMap());
	
	public synchronized void Add(int id, String type, String description, boolean disabled)
	{
		PlantWatchSiteConfigUnit rec = new PlantWatchSiteConfigUnit();
		if(rec!=null)
		{
			rec.id = id;
			rec.type = type;
			rec.description = description;
			rec.disabled = disabled;
			mp_SiteConfig.put( Integer.valueOf(id), rec);
		}
	}
	
	public boolean GetUnitConf(int id, PlantWatchSiteConfigUnit r)
	{
		PlantWatchSiteConfigUnit rec = (PlantWatchSiteConfigUnit)mp_SiteConfig.get( Integer.valueOf(id));
		if(rec!=null)
		{
			PlantWatchSiteConfigUnit r1 = (PlantWatchSiteConfigUnit)rec.clone();
			r.description=r1.description;
			r.disabled=r1.disabled;
			r.id= r1.id;
			r.type=r1.type;
			return true;
		}
		else
			return false;
			
	}
	public boolean HasMore(PlantWatchSiteConfigUnit pwcu)
	{
	    Set mappings = mp_SiteConfig.keySet();

	    java.util.Iterator i = mappings.iterator();

	    if (i.hasNext())
	    {
	    	PlantWatchSiteConfigUnit pw = (PlantWatchSiteConfigUnit) mp_SiteConfig.remove(i.next());
	    	pwcu.id = pw.id;
	    	pwcu.type = pw.type;
	    	pwcu.description = pw.description;
	    	pwcu.disabled = pw.disabled;
	        return true;
	    }
	    else
	    {
	        return false;
	    }
	}
	
}


