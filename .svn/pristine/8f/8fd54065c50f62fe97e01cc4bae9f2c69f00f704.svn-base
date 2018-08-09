package com.carel.supervisor.dispatcher.plantwatch1;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class PlantWatchUnitVars {
	private Map mp_UnitVars = Collections.synchronizedMap( new TreeMap());
	public synchronized void Add(int type, int address, Double value) 
	{
		PlantWatchUnitVar rec = new PlantWatchUnitVar();
		if(rec != null)
		{
			rec.type = type;
			rec.address = address;
			rec.value   = value;
			mp_UnitVars.put( Integer.valueOf(address),rec);
		}
	}
	
	public boolean ReadByte(int address, VarByte val)
	{
		PlantWatchUnitVar rec = (PlantWatchUnitVar)mp_UnitVars.get(Integer.valueOf(address));
		if(rec!=null)
		{
			val.setVal(rec.value.byteValue());
			return true;
		}else
			return false;
			
	}
	
	public boolean ReadInteger(int address, VarInteger val)
	{
		PlantWatchUnitVar rec = (PlantWatchUnitVar)mp_UnitVars.get(Integer.valueOf(address));
		if(rec!=null)
		{
			val.setVal(rec.value.intValue());
			return true;
		}else
			return false;
			
	}
	
	public boolean ReadDouble(int address, VarDouble val)
	{
		PlantWatchUnitVar rec = (PlantWatchUnitVar)mp_UnitVars.get(Integer.valueOf(address));
		if(rec!=null)
		{
			val.setVal(rec.value.doubleValue());
			return true;
		}else
			return false;
			
	}
	
	
	/**
	 * @return Returns the mp_UnitVars.
	 */
	public Map getMp_UnitVars() {
		return mp_UnitVars;
	}
}
