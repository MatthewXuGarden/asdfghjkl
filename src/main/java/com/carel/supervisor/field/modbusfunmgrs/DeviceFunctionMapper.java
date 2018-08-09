package com.carel.supervisor.field.modbusfunmgrs;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class DeviceFunctionMapper
{
	private static DeviceFunctionMapper me;
	private HashMap<Integer, IFunctionMgr> map;
	
	private DeviceFunctionMapper()
	{
		map = new HashMap<Integer, IFunctionMgr>();
		try
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from cfmodbusfunclasses");
			for(int i=0;i<rs.size();i++)
			{
				try
				{
	                Class<?> c = Class.forName( rs.get(i).get("functionclass").toString() );
	                Method m = c.getDeclaredMethod("getInstance", new Class[]{});
	                IFunctionMgr ifmgr = (IFunctionMgr)m.invoke((Object)null, (Object[])null);
					map.put((Integer)rs.get(i).get("iddevmdl"),ifmgr);
					LoggerMgr.getLogger(this.getClass()).info("New instance of class: "+ifmgr.getClass().toString()+" for mdl: "+rs.get(i).get("iddevmdl"));
				}
				catch(Exception ex)
				{
					try
					{
		                Class<?> c = Class.forName( "com.carel.supervisor.field.modbusfunmgrs.IdentityFunctionMgr" );
		                Method m = c.getDeclaredMethod("getInstance", new Class[]{});
		                IFunctionMgr ifmgr = (IFunctionMgr)m.invoke((Object)null, (Object[])null);
						map.put((Integer)rs.get(i).get("iddevmdl"),ifmgr);
						LoggerMgr.getLogger(this.getClass()).info("New instance of default class: "+ifmgr.getClass().toString()+" for mdl: "+rs.get(i).get("iddevmdl"));
					}
					catch(Exception e){}
					LoggerMgr.getLogger(this.getClass()).error(ex);
				}
			}
			try
			{
                Class<?> c = Class.forName( "com.carel.supervisor.field.modbusfunmgrs.IdentityFunctionMgr" );
                Method m = c.getDeclaredMethod("getInstance", new Class[]{});
                IFunctionMgr ifmgr = (IFunctionMgr)m.invoke((Object)null, (Object[])null);
				map.put(-1,ifmgr);
			}
			catch(Exception e){}
		} catch (Exception e)
		{
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	public static DeviceFunctionMapper getInstance()
	{
		if(me==null)
			me = new DeviceFunctionMapper();
		return me;
	}
	
	public IFunctionMgr getFunctionMgr(int iddevmdl)
	{
		try
		{
//			return map.containsKey(iddevmdl)?map.get(iddevmdl):map.get(-1);
			return map.get(iddevmdl); //if null apply standard linearization
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
