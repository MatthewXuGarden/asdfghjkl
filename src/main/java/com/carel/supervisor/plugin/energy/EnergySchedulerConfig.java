package com.carel.supervisor.plugin.energy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Properties;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class EnergySchedulerConfig
{
	public static final String ON = "on";
	public static final String OFF = "off";
	public static final int DIRECT = 0;
	public static final int INVERSE = 1;
	public static final String LIGHTS = "luci";
	public static final String NIGHT = "notte";
	public static final String CMD_ONOFF = "cmd_onoff";
	public static final String CMD_LIGHTS = "cmd_luci";
	public static final String CMD_NIGHT = "cmd_notte";
	public static final String ST_ONOFF = "st_onoff";
	public static final String ST_LIGHTS = "st_luci";
	public static final String ST_NIGHT = "st_notte";
	public static final int CMD_ONOFF_I = 0;
	public static final int CMD_LIGHTS_I = 1;
	public static final int CMD_NIGHT_I = 2;
	public static final int ST_ONOFF_I = 3;
	public static final int ST_LIGHTS_I = 4;
	public static final int ST_NIGHT_I = 5;
	
	public static final String ON1 = "on_1";
	public static final String OFF1 = "off_1";
	public static final String ON2 = "on_2";
	public static final String OFF2 = "off_2";
	
	
	private HashMap<Integer, Properties> groups;
	private Properties clockconfig;
//	private HashMap<Integer, LinkedList<Integer>> devsxgroup;
	private HashMap<Integer, Properties> fieldvars;
	private HashMap<Integer, HashMap<String, Integer>> scheduler;
	private Boolean loaded = false;
//	private HashMap<Integer, HashMap<String, Integer>> devconf;
//	private HashMap<Integer, Integer> devltnt;
//	private HashMap<Integer, HashMap<String, Integer>> exceptions;
	
	public EnergySchedulerConfig()
	{
		RecordSet rs;

		groups = new HashMap<Integer, Properties>();

//		clockconfig = new Properties();
//		try
//		{
//			rs = DatabaseMgr.getInstance().executeQuery(null, "select * from energsite");
//			for (int i = 0; i < rs.size(); i++)
//			{
//				clockconfig.setProperty(rs.get(i).get("key").toString(), rs.get(i).get("value").toString());
//			}
//		}
//		catch (Exception e)
//		{
//			clockconfig = null;
//			LoggerMgr.getLogger(this.getClass()).error(e);
//		}

		fieldvars = new HashMap<Integer, Properties>();
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null, "select * from energyactive");
			for (int i = 0; i < rs.size(); i++)
			{
				Integer id = (Integer) rs.get(i).get("idgroup");
				fieldvars.put(id, new Properties());
				Properties p = fieldvars.get(id);
				p.setProperty("idgroup", rs.get(i).get("idgroup").toString());
				p.setProperty("idvariable", rs.get(i).get("idvariable").toString());
				p.setProperty("inverse", rs.get(i).get("inverse").toString());
				p.setProperty("enabled", rs.get(i).get("enabled").toString());
			}
		}
		catch (Exception e)
		{
			fieldvars = null;
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		
		scheduler = new HashMap<Integer, HashMap<String, Integer>>();
		
		Connection conn = null;
		
		try
		{
			conn = DatabaseMgr.getInstance().getConnection(null);
			ResultSet resset = conn.createStatement().executeQuery("select * from energyscheduler");
			while(resset.next()){
				Integer id = resset.getInt("idgroup");
				if(resset.wasNull()) id=null;
				Integer id2 = resset.getInt("day");
				if(resset.wasNull()) id2=null;
				if(id==null || id2==null) continue;
				scheduler.put(id2*1000000+id, new HashMap<String, Integer>());
				HashMap<String, Integer> hm = scheduler.get(id2*1000000+id);
				Integer var = resset.getInt("idgroup");
				if(resset.wasNull()) var=null;
				hm.put("idgroup", var);
				
				var = resset.getInt("day");
				if(resset.wasNull()) var=null;
				hm.put("day", var);
				
				var = resset.getInt("on_1");
				if(resset.wasNull()) var=null;
				hm.put("on_1", var);
				
				var = resset.getInt("off_1");
				if(resset.wasNull()) var=null;
				hm.put("off_1", var);
				
				var = resset.getInt("on_2");
				if(resset.wasNull()) var=null;
				hm.put("on_2", var);
				
				var = resset.getInt("off_2");
				if(resset.wasNull()) var=null;
				hm.put("off_2", var);
			}
		}
		catch (Exception e)
		{
			scheduler = null;
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		finally
		{
			try
			{
				conn.close();
			}
			catch (Exception e)
			{
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
//		exceptions = new HashMap<Integer, HashMap<String, Integer>>();
//		try
//		{
//			rs = DatabaseMgr.getInstance().executeQuery(null, "select * from ln_exceptdays");
//			for (int i = 0; i < rs.size(); i++)
//			{
//				Integer id = (Integer) rs.get(i).get("idmonth");
//				Integer id2 = (Integer) rs.get(i).get("idday");
//				exceptions.put(id*100+id2, new HashMap<String, Integer>());
//				HashMap<String, Integer> hm = exceptions.get(id*100+id2);
//				hm.put("idmonth", (Integer)rs.get(i).get("idmonth"));
//				hm.put("idday", (Integer)rs.get(i).get("idday"));
//				hm.put("on_1", (Integer)rs.get(i).get("on_1"));
//				hm.put("off_1", (Integer)rs.get(i).get("off_1"));
//				hm.put("on_2", (Integer)rs.get(i).get("on_2"));
//				hm.put("off_2", (Integer)rs.get(i).get("off_2"));
//			}
//		}
//		catch (Exception e)
//		{
//			scheduler = null;
//			LoggerMgr.getLogger(this.getClass()).error(e);
//		}
		loaded = true;
	}

	public Properties getClockConfig()
	{
		return clockconfig;
	}
	
	public HashMap<String, Integer> getScheduling(int idgrp, int day)
	{
		return scheduler.get(day*1000000+idgrp);
	}
	
	public int getFieldVar(int grp)
	{
		if(fieldvars!=null && fieldvars.get(new Integer(grp))!=null)
		{
			String s = (fieldvars.get(new Integer(grp))).getProperty("idvardig");
			if(s!=null && !"".equalsIgnoreCase(s))
				return new Integer(fieldvars.get(new Integer(grp)).getProperty("idvardig"));
			else
				return -1;
		}
		else
		{
			return -1;
		}
	}

	public boolean isOnOffEnabled(int idgrp)
	{
		return "on".equalsIgnoreCase(groups.get(new Integer(idgrp)).getProperty("onoff"));
	}

	public int getFieldVarLogic(int idgrp)
	{
		try
		{
			return new Integer(fieldvars.get(idgrp).getProperty("stato_on"));
		}
		catch(Exception e)
		{
			return 0; //dritta
		}
	}

	public boolean isSchedulerActive(int idgrp)
	{
		try
		{
			return "on".equalsIgnoreCase(groups.get(idgrp).get("scheduler").toString());
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public Boolean getLoaded() {
		return loaded;
	}
	
	public Integer getComboIndex(Integer idgrp, Integer idday, String slot) {
		try {
			if (getScheduling(idgrp, idday) != null	&& 
					getScheduling(idgrp, idday).get(slot) != null &&
					!getScheduling(idgrp, idday).get(slot).equals(-1) ) {
				return (1 + 
						(getScheduling(idgrp, idday).get(slot) % 10000 != 0 ? 1 : 0) + 
						((int) (getScheduling(idgrp, idday).get(slot) / 10000) * 2));
			} else {
				return -1;
			}
		} catch (Exception e) {
			return -1;
		}
	}
}
