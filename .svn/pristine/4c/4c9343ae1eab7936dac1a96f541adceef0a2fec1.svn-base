package com.carel.supervisor.director.lightnight;

//import java.util.Calendar;
//import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class LNConfig
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
	
	private HashMap<Integer, Properties> groups;
	private Properties clockconfig;
	private HashMap<Integer, LinkedList<Integer>> devsxgroup;
	private HashMap<Integer, Properties> fieldvars;
	private HashMap<Integer, HashMap<String, Integer>> scheduler;
	private HashMap<Integer, HashMap<String, Integer>> devconf;
	private HashMap<Integer, Integer> devltnt;
	private HashMap<Integer, HashMap<String, Integer>> exceptions;
	
	public LNConfig()
	{
		RecordSet rs;

		groups = new HashMap<Integer, Properties>();
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null, "select * from ln_groups");
			for (int i = 0; i < rs.size(); i++)
			{
				Integer id = (Integer) rs.get(i).get("idgroup");
				groups.put(id, new Properties());
				Properties p = groups.get(id);
				p.put("idgroup", rs.get(i).get("idgroup"));
				p.put("nome_grp", rs.get(i).get("nome_grp"));
				p.put("manuale", rs.get(i).get("manuale"));
				p.put("onoff", rs.get(i).get("onoff"));
				p.put("scheduler", rs.get(i).get("scheduler"));
				p.put("campo", rs.get(i).get("campo"));
				p.put("lcnt", rs.get(i).get("lcnt"));
//				p.put("enabled", rs.get(i).get("enabled"));
			}
		}
		catch (Exception e)
		{
			groups = null;
			LoggerMgr.getLogger(this.getClass()).error(e);
		}

		clockconfig = new Properties();
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null, "select * from ln_clockconfig");
			for (int i = 0; i < rs.size(); i++)
			{
				clockconfig.setProperty(rs.get(i).get("key").toString(), rs.get(i).get("value").toString());
			}
		}
		catch (Exception e)
		{
			clockconfig = null;
			LoggerMgr.getLogger(this.getClass()).error(e);
		}

		devsxgroup = new HashMap<Integer, LinkedList<Integer>>();
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null, "select idgroup, abs(iddev) as iddev from ln_devsxgroup");
			for (int i = 0; i < rs.size(); i++)
			{
				Integer id = (Integer) rs.get(i).get("idgroup");
				if (null == devsxgroup.get(id)) devsxgroup.put(id, new LinkedList<Integer>());
				devsxgroup.get(id).add((Integer) rs.get(i).get("iddev"));
			}
		}
		catch (Exception e)
		{
			devsxgroup = null;
			LoggerMgr.getLogger(this.getClass()).error(e);
		}

		fieldvars = new HashMap<Integer, Properties>();
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null, "select * from ln_fieldvars");
			for (int i = 0; i < rs.size(); i++)
			{
				Integer id = (Integer) rs.get(i).get("idgroup");
				fieldvars.put(id, new Properties());
				Properties p = fieldvars.get(id);
				p.setProperty("idgroup", rs.get(i).get("idgroup").toString());
				p.setProperty("iddev", rs.get(i).get("iddev").toString());
				p.setProperty("idvardig", rs.get(i).get("idvardig").toString());
				p.setProperty("stato_on", rs.get(i).get("stato_on").toString());
				p.setProperty("attivo", rs.get(i).get("attivo").toString());
			}
		}
		catch (Exception e)
		{
			fieldvars = null;
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		
		scheduler = new HashMap<Integer, HashMap<String, Integer>>();
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null, "select * from ln_scheduler");
			for (int i = 0; i < rs.size(); i++)
			{
				Integer id = (Integer) rs.get(i).get("idgroup");
				Integer id2 = (Integer) rs.get(i).get("day");
				scheduler.put(id2*1000000+id, new HashMap<String, Integer>());
				HashMap<String, Integer> hm = scheduler.get(id2*1000000+id);
				hm.put("idgroup", (Integer)rs.get(i).get("idgroup"));
				hm.put("day", (Integer)rs.get(i).get("day"));
				hm.put("on_1", (Integer)rs.get(i).get("on_1"));
				hm.put("off_1", (Integer)rs.get(i).get("off_1"));
				hm.put("on_2", (Integer)rs.get(i).get("on_2"));
				hm.put("off_2", (Integer)rs.get(i).get("off_2"));
			}
		}
		catch (Exception e)
		{
			scheduler = null;
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		
		exceptions = new HashMap<Integer, HashMap<String, Integer>>();
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null, "select * from ln_exceptdays");
			for (int i = 0; i < rs.size(); i++)
			{
				Integer idgrp = (Integer) rs.get(i).get("idgrp");
				Integer id = (Integer) rs.get(i).get("idmonth");
				Integer id2 = (Integer) rs.get(i).get("idday");
				exceptions.put(idgrp*10000+id*100+id2, new HashMap<String, Integer>());
				HashMap<String, Integer> hm = exceptions.get(idgrp*10000+id*100+id2);
				
				hm.put("idmonth", (Integer)rs.get(i).get("idmonth"));
				hm.put("idday", (Integer)rs.get(i).get("idday"));
				hm.put("on_1", (Integer)rs.get(i).get("on_1"));
				hm.put("off_1", (Integer)rs.get(i).get("off_1"));
				hm.put("on_2", (Integer)rs.get(i).get("on_2"));
				hm.put("off_2", (Integer)rs.get(i).get("off_2"));
			}
		}
		catch (Exception e)
		{
			scheduler = null;
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		
		devconf = new HashMap<Integer, HashMap<String,Integer>>();
		for(int i = 0; i<groups.keySet().size();i++)
		{
			try
			{
				rs = DatabaseMgr.getInstance().executeQuery(null, 
						"select "+
						"cfdevice.iddevice, idvariable, cfdevice.iddevmdl, cfvariable.idvarmdl, tipo, logica "+
						"from "+
						"ln_varmdl, ln_devsxgroup, cfvariable, cfdevice "+
						"where "+
						"abs(ln_devsxgroup.iddev) = cfvariable.iddevice and "+
						"abs(ln_devsxgroup.iddev) = cfdevice.iddevice and "+
						"ln_varmdl.idvarmdl=cfvariable.idvarmdl and "+
						"ln_varmdl.iddevmdl=cfdevice.iddevmdl and "+
						"cfvariable.iscancelled = 'FALSE' and "+
						"cfdevice.iscancelled='FALSE' and "+
						"cfvariable.idhsvariable is not null " +
						
						"union " +
						
						"select " +
						"cfdevice.iddevice, " +
						"cfvariable.idvariable, " +
						"cfdevice.iddevice, " +
						"cfvariable.idvarmdl, " +
						"ln_varmdl.tipo, " +
						"ln_varmdl.logica " +
						"from " +
						"ln_varmdl, " +
						"cfdevice, " +
						"cfvariable, " +
						"ln_devsxgroup " +
						"where " +
						"-ln_devsxgroup.iddev = cfdevice.iddevice and " +
						"cfdevice.iddevice=-ln_varmdl.iddevmdl and " +
						"cfvariable.idvariable=ln_varmdl.idvarmdl and " +
						"cfvariable.iscancelled = 'FALSE' and " +
						"cfdevice.iscancelled='FALSE' ");
				for(int ii = 0;ii<rs.size();ii++)
				{
					Integer id = (Integer)rs.get(ii).get("iddevice");
					if(null==devconf.get(id))
					{
						devconf.put(id, new HashMap<String, Integer>());
					}
					HashMap<String, Integer> hm = devconf.get(id);
					hm.put("iddevice", id);
					hm.put("iddevmdl", (Integer)rs.get(ii).get("iddevmdl"));
					switch ((Integer) rs.get(ii).get("tipo"))
					{
					case 0:
						hm.put("idvaronoff", (Integer) rs.get(ii).get("idvariable"));
						hm.put("logicvaronoff", DIRECT == (Integer) rs.get(ii).get("logica") ? DIRECT : INVERSE == (Integer) rs.get(ii).get("logica")? INVERSE :-1);
						break;
					case 1:
						hm.put("idvarlights", (Integer) rs.get(ii).get("idvariable"));
						hm.put("logicvarlights", DIRECT == (Integer) rs.get(ii).get("logica") ? DIRECT : INVERSE == (Integer) rs.get(ii).get("logica")? INVERSE :-1);
						break;
					case 2:
						hm.put("idvarnight", (Integer) rs.get(ii).get("idvariable"));
						hm.put("logicvarnight", DIRECT == (Integer) rs.get(ii).get("logica") ? DIRECT : INVERSE == (Integer) rs.get(ii).get("logica")? INVERSE :-1);
						break;
					case 3:
						hm.put("idstateonoff", (Integer) rs.get(ii).get("idvariable"));
						hm.put("logicstateonoff", DIRECT == (Integer) rs.get(ii).get("logica") ? DIRECT : INVERSE == (Integer) rs.get(ii).get("logica")? INVERSE :-1);
						break;
					case 4:
						hm.put("idstatelights", (Integer) rs.get(ii).get("idvariable"));
						hm.put("logicstatelights", DIRECT == (Integer) rs.get(ii).get("logica") ? DIRECT : INVERSE == (Integer) rs.get(ii).get("logica")? INVERSE :-1);
						break;
					case 5:
						hm.put("idstatenight", (Integer) rs.get(ii).get("idvariable"));
						hm.put("logicstatenight", DIRECT == (Integer) rs.get(ii).get("logica") ? DIRECT : INVERSE == (Integer) rs.get(ii).get("logica")? INVERSE :-1);
						break;
					}
				}
			}
			catch (Exception e)
			{
				devconf = null;
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		
		devltnt = new HashMap<Integer, Integer>();
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null, "select * from ln_devlcnt");
			for(int i=0;i<rs.size();i++)
			{
				devltnt.put((Integer)rs.get(i).get("iddev"), LIGHTS.equalsIgnoreCase(rs.get(i).get("lcnt").toString())?0:NIGHT.equalsIgnoreCase(rs.get(i).get("lcnt").toString())?1:-1);
			}
		}
		catch (Exception e)
		{
			devltnt = null;
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}

	public Properties getClockConfig()
	{
		return clockconfig;
	}
	
	public LinkedList<Integer> getDevices(int idgrp)
	{
		return devsxgroup.get(idgrp);
	}

	public boolean getLNCheck(int idgrp)
	{
		if(null!=groups.get(idgrp) && null!=groups.get(idgrp).getProperty("lcnt"))
			return groups.get(idgrp).getProperty("lcnt").equalsIgnoreCase(ON);
		else
			return false;
	}

	public boolean lights(Integer iddev)
	{
		if(null!=devltnt.get(iddev))
			return 0==devltnt.get(iddev);
		else
			return false;
	}

	public boolean night(Integer iddev)
	{
		if(null!=devltnt.get(iddev))
			return 1==devltnt.get(iddev);
		else
			return false;
	}

	public int lightsVar(Integer iddev)
	{
		if(devconf.get(iddev)!=null &&devconf.get(iddev).get("idvarlights")!=null)
			return devconf.get(iddev).get("idvarlights");
		else
			return -1;
	}

	public int lightsValue(Integer iddev, boolean on)
	{
		if(devconf.get(iddev)!=null && devconf.get(iddev).get("logicvarlights")!=null)
			return on?
					(0==devconf.get(iddev).get("logicvarlights")?1:0): //acceso
					(0==devconf.get(iddev).get("logicvarlights")?0:1); //spento
		else
			return -1;
	}

	public int nightVar(Integer iddev)
	{
		if(devconf.get(iddev)!=null && devconf.get(iddev).get("idvarnight")!=null)
			return devconf.get(iddev).get("idvarnight");
		else
			return -1;
	}

	public int nightValue(Integer iddev, boolean on)
	{
		if(devconf.get(iddev)!=null && devconf.get(iddev).get("logicvarnight")!=null)
			return on?
					(0==devconf.get(iddev).get("logicvarnight")?1:0):
					(0==devconf.get(iddev).get("logicvarnight")?0:1);
		else
			return -1;
	}

	public boolean getOnOff(int idgrp)
	{
		return ON.equalsIgnoreCase(groups.get(idgrp).getProperty("onoff"));
	}

	public int onoffVar(Integer iddev)
	{
		if(devconf.get(iddev)!=null && devconf.get(iddev).get("idvaronoff")!=null)
			return devconf.get(iddev).get("idvaronoff");
		else
			return -1;
	}

	public int onoffValue(Integer iddev, boolean on)
	{
		if(devconf.get(iddev)!=null && devconf.get(iddev).get("logicvaronoff")!=null)
			return on?
					(0==devconf.get(iddev).get("logicvaronoff")?1:0):
					(0==devconf.get(iddev).get("logicvaronoff")?0:1);
		else
			return -1;
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

	public boolean getForceManual(int idgrp)
	{
		if(groups!=null && groups.get(new Integer(idgrp))!=null && groups.get(new Integer(idgrp)).getProperty("manuale")!=null)
			return "on".equalsIgnoreCase(groups.get(new Integer(idgrp)).getProperty("manuale"));
		else
			return false;
	}

	public boolean isLightsNightEnabled(int idgrp)
	{
		return "on".equalsIgnoreCase(groups.get(new Integer(idgrp)).getProperty("lcnt"));
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

	public boolean isFieldActive(int idgrp)
	{
		try
		{
			return "on".equalsIgnoreCase(fieldvars.get(idgrp).getProperty("attivo"));
		}
		catch(Exception e)
		{
			return false;
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

	public HashMap<String, Integer> getTodayExceptions(int idgrp, int monthday)
	{
//		GregorianCalendar gc = new GregorianCalendar();
//		int i = (gc.get(Calendar.MONTH)+1)*100+gc.get(Calendar.DAY_OF_MONTH);
		if(exceptions.get(idgrp*10000+monthday)!=null)
			return exceptions.get(idgrp*10000+monthday);
		else
			return exceptions.get(monthday);
	}
}
