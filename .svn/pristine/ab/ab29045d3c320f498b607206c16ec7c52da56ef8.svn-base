package com.carel.supervisor.field;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.VariablesAccess;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.field.dataconn.impl.DataConnFTD2IO;


public class InternalRelayMgr {

	public static String INTERNALIO_RELAY_FOR_SAFETY = "internalio_relay_for_safety";
	private static InternalRelayMgr me = new InternalRelayMgr();
	
	public static InternalRelayMgr getInstance()
    {
       return me;
    }
	
	/*
	 * reset Internal IO's default value in the following two conditions
	 * 1. engine stop(stop engine, restart engine)
	 * 2. PVPRO service stop(shutdown PC, restart PC, manually stop/restart PVPRO service)
	 */
	public void resetDefaultValues()
	{
		HashMap<Integer, Integer> mapRelay = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		try
		{
			String sql = "select idvariable,idrelay, activestate from cfrelay where idvariable in (select idvariable from cfvariable where idvarmdl in (select idvarmdl from cfvarmdl where iddevmdl = (select iddevmdl from cfdevmdl where code = 'Internal IO') and code in ('DO1','DO2','DO3')))";
			RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql, null);
			
			Record record = null;
			int idvariable;
			int idrelay;
			int activestate;
			
			for (int i = 0; i < recordSet.size(); i++)
			{
				record = recordSet.get(i);
				idvariable = (Integer)record.get("idvariable");
				idrelay = (Integer)record.get("idrelay");
				activestate = (Integer)record.get("activestate");
			
				map.put(idvariable, activestate);
				mapRelay.put(idvariable, idrelay);
			}
		}
		catch (Exception e)
		{
			// error on default values retrieve
			LoggerMgr.getLogger(this.getClass()).error("Error during retrieve of Internal IO relay configuation: " + e.getStackTrace());
			return;
		}
		
		Set<Integer> set = map.keySet();
		Iterator<Integer> i = set.iterator();
		
		String str = ProductInfoMgr.getInstance().getProductInfo().get(INTERNALIO_RELAY_FOR_SAFETY);
		if(str != null && str.split(";").length == 3)
		{
			String[] relaySafety = str.split(";"); 
			try
			{
				while(i.hasNext())
				{
					int idvar = (Integer)i.next();
					int index = -mapRelay.get(idvar)-1;
					
					//idrelay must be -1,-2,-3
					if(index<0 || index>2)
						continue;
					//must be safety relay
					if(!"TRUE".equals(relaySafety[index]))
						continue;
					VariablesAccess vaccess = new VariablesAccess(FieldConnectorMgr.getInstance().getDataCollector());
					Variable var = vaccess.retrieve(idvar);
					
					DataConnFTD2IO dtconn = new DataConnFTD2IO();
//					int val = (Integer)map.get(idvar)==1 ? 0:1;
					//set safe relay fixed to 0
					var.setValue(0f);
					
					dtconn.setOnField(var,false);
				}
			}
			catch (Exception e)
			{
				// error on default values setting
				LoggerMgr.getLogger(this.getClass()).error("Error during resetting Internal IO relay configuration: " + e.getStackTrace());
				return;
			}
		}
	}
	
	public boolean[] isSafetyRelay()
	{
		boolean[] result = new boolean[]{true,true,true};
		String str = ProductInfoMgr.getInstance().getProductInfo().get(INTERNALIO_RELAY_FOR_SAFETY);
		if(str != null)
		{
			String[] strs = str.split(";");
			if(strs.length == 3)
			{
				for(int i=0;i<strs.length;i++)
				{
					String s = strs[i];
					result[i] = "TRUE".equalsIgnoreCase(s);
				}
			}
		}
		return result;
	}
}
