package com.carel.supervisor.presentation.kpi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class KpiMgr 
{
	private KpiMgr(){}
	
	public static Map<String, Number> getResults(Map<String, String> params)
	{
		//return value
		HashMap<String,Number> ret = new HashMap<String,Number>();

		//allocation
		long thismastertime=0l;
		long thismasterover=0l;
		long thismasterunder=0l;
		long thisdefrosttime=0l;
		long thissolenoidtime=0l;
		long thistimeformean = 0l;

		long totalmastertime=0l;
		long totalmasterover=0l;
		long totalmasterunder=0l;
		long totaldefrosttime=0l;
		long totalsolenoidtime=0l;
		long totaltimeformean = 0l;

		//long extraoverlaptime=0l;
		double mean = 0.0;

		//timewindow
		String tw0 = params.get("timewindow");
		String[] tw1 = tw0.split("_");
		Long tw2 = new Long(tw1[1]);
		long tw3 = 1;
		if("h".equalsIgnoreCase(tw1[0]))
			tw3*=3600000l;
		if("g".equalsIgnoreCase(tw1[0]))
			tw3*=86400000l;
		if("m".equalsIgnoreCase(tw1[0]))
			tw3*=2592000000l;
		tw3*=tw2;
		
		//interval
		Timestamp time_B = Timestamp.valueOf(params.get("enddate"));
		Timestamp time_A = new Timestamp(time_B.getTime()-tw3);
		
		//conditions
		Boolean defrost = new Boolean(params.get("def")) && params.get("idvd")!=null && !"".equals(params.get("idvd"));
		Boolean solenoid = new Boolean(params.get("sol")) && params.get("idvs")!=null && !"".equals(params.get("idvs"));
		
		Boolean defrostdata = !"0".equals(params.get("idvd"));
		Boolean solenoiddata = !"0".equals(params.get("idvs"));
		
		//queries
		String sql_m = "(select * from hsvarhistor " +
				"where hsvarhistor.idvariable = "+params.get("idvm")+" " +
				"and hsvarhistor.inserttime < '"+time_A+"' " +
				"order by inserttime desc limit 1) " +
				"union " +
				"( select * from hsvarhistor "+
				"where hsvarhistor.idvariable = "+params.get("idvm")+" "+
				"and hsvarhistor.inserttime > '"+time_A+"' and hsvarhistor.inserttime < '"+time_B+"') "+
				"union "+ 
				"(select * from hsvarhistor "+
				"where hsvarhistor.idvariable = "+params.get("idvm")+" "+
				"and hsvarhistor.inserttime > '"+time_B+"' "+
				"order by inserttime limit 1)";
		
		String sql_d = "(select * from hsvarhistor " +
				"where hsvarhistor.idvariable = "+params.get("idvd")+" " +
				"and hsvarhistor.inserttime < '"+time_A+"' " +
				"order by inserttime desc limit 1) " +
				"union " +
				"( select * from hsvarhistor "+
				"where hsvarhistor.idvariable = "+params.get("idvd")+" "+
				"and hsvarhistor.inserttime > '"+time_A+"' and hsvarhistor.inserttime < '"+time_B+"') "+
				"union "+ 
				"(select * from hsvarhistor "+
				"where hsvarhistor.idvariable = "+params.get("idvd")+" "+
				"and hsvarhistor.inserttime > '"+time_B+"' "+
				"order by inserttime limit 1)";
		
		String sql_s = "(select * from hsvarhistor " +
				"where hsvarhistor.idvariable = "+params.get("idvs")+" " +
				"and hsvarhistor.inserttime < '"+time_A+"' " +
				"order by inserttime desc limit 1) " +
				"union " +
				"( select * from hsvarhistor "+
				"where hsvarhistor.idvariable = "+params.get("idvs")+" "+
				"and hsvarhistor.inserttime > '"+time_A+"' and hsvarhistor.inserttime < '"+time_B+"') "+
				"union "+ 
				"(select * from hsvarhistor "+
				"where hsvarhistor.idvariable = "+params.get("idvs")+" "+
				"and hsvarhistor.inserttime > '"+time_B+"' "+
				"order by inserttime limit 1)";
		
		Connection masterc = null;
		Connection defrostc  = null;
		Connection solenoidc  = null;
		try 
		{
			//data recovering
			masterc = DatabaseMgr.getInstance().getConnection(null);
			defrostc = DatabaseMgr.getInstance().getConnection(null);
			solenoidc = DatabaseMgr.getInstance().getConnection(null);
			ResultSet rs_m = masterc.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY).executeQuery(sql_m);
			ResultSet rs_d = null;
			ResultSet rs_s = null;
//			if(defrost)
			{
				rs_d = defrostc.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY).executeQuery(sql_d);
			}
//			if(solenoid)
			{
				rs_s = solenoidc.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY).executeQuery(sql_s);
			}
			
			rs_m.last();
			int size = rs_m.getRow();
			rs_m.beforeFirst();

			if(rs_m!=null && size>0)
			{
				//data processing
				LinkedHashMap<Timestamp, Float> m_v = getValues(rs_m);
				LinkedHashMap<Timestamp, Float> d_v = null;
				LinkedHashMap<Timestamp, Float> s_v = null;
//				if(defrost)
					d_v = getValues(rs_d);
//				if(solenoid)
					s_v = getValues(rs_s);
				
				
//				LoggerMgr.getLogger(KpiMgr.class).info("close masterc");
				rs_m.close();
//				masterc.close();
//				LoggerMgr.getLogger(KpiMgr.class).info("close defrostc");
				rs_d.close();
//				defrostc.close();
//				LoggerMgr.getLogger(KpiMgr.class).info("close solenoidc");
				rs_s.close();
//				solenoidc.close();
//				LoggerMgr.getLogger(KpiMgr.class).info("close all");
				
				//loop init
				Iterator<Timestamp> 
							masteritr = m_v.keySet().iterator(), 
							defitr = null, 
							solitr = null;
//				if (defrost)
//					defitr = d_v.keySet().iterator();
//				if (solenoid)
//					solitr = s_v.keySet().iterator();

				//allocation
				Timestamp masterbegin = masteritr.next(), masterend=null, defrostbegin=null, defrostend=null, solenoidbegin=null, solenoidend=null;

				//computing
				if (masteritr.hasNext()) {
					masterend = masteritr.next();
					//defrost data
					if (d_v.size()>0)
					{
						defitr = d_v.keySet().iterator();
						defrostbegin = defitr.next();
						if(defitr.hasNext())
						{
							defrostend = defitr.next();
						}
						else
						{
							defrostend = time_B;
						}
					}
					else
					{
						defrost = false;
						defrostdata = false;
					}
					//solenoid data
					if (s_v.size()>0)
					{
						solitr = s_v.keySet().iterator();
						solenoidbegin = solitr.next();
						if(solitr.hasNext())
						{
							solenoidend = solitr.next();
						}
						else
						{
							solenoidend = time_B;
						}
					}
					else
					{
						solenoid = false;
						solenoiddata = false;
					}
					//computing times
					do //while (masteritr.hasNext())
					{
						//reset counters
						thismastertime=0l;
						thismasterover=0l;
						thismasterunder=0l;
						thisdefrosttime=0l;
						thissolenoidtime=0l;
						thistimeformean = 0l;
						List<TimeZ> unConsiderList = new ArrayList<TimeZ>();
						
						if (masterend.after(time_A) && masterbegin.before(time_B) && !m_v.get(masterbegin).equals(Float.NaN))
						{
							//mastertime
							long begin = Math.max(time_A.getTime(), masterbegin.getTime());
							long end = Math.min(time_B.getTime(), masterend.getTime());
							thismastertime = end - begin;
							thistimeformean = end - begin;
							if(thismastertime>0)
							{
								//defrosttime
								//if (/*defrost &&*/ defitr.hasNext())
								if (defrostdata)
								{
									while(true)
									{
										while ((defrostend==null || !defrostend.after(masterbegin)) 
												&& defitr.hasNext())
										{
											defrostbegin = defrostend;
											defrostend = defitr.next();
										}
										if (!d_v.get(defrostbegin).equals(Float.NaN) && d_v.get(defrostbegin) > 0.99f)
										{
											long df_b = Math.max(time_A.getTime(),Math.max(defrostbegin.getTime(), masterbegin.getTime()));
											long df_e = Math.min(time_B.getTime(),Math.min(defrostend.getTime(), masterend.getTime()));
											if (df_e - df_b > 0)
											{
												thisdefrosttime += df_e - df_b;
												Timestamp s = new Timestamp(df_b);
												Timestamp e = new Timestamp(df_e);
												if(defrost)
													unConsiderList = updateUnConsiderList(unConsiderList,new TimeZ(s,e));
											}
										}
										if(!defitr.hasNext())
											break;
										//need to check next solenoid
										if(defrostend.before(masterend))
										{
											defrostbegin = defrostend;
											defrostend = defitr.next();
										}
										else
											break;
									}
								}
								
								//solenoidtime
								//if (/*solenoid &&*/ solitr.hasNext())
								if (solenoiddata)
								{
									while(true)
									{
										while ((solenoidend==null || !solenoidend.after(masterbegin)) 
												&& solitr.hasNext())
										{
											solenoidbegin = solenoidend;
											solenoidend = solitr.next();
										}
										if (!s_v.get(solenoidbegin).equals(Float.NaN) && s_v.get(solenoidbegin) > 0.99f)
										{
											long sl_b = Math.max(time_A.getTime(),Math.max(solenoidbegin.getTime(), masterbegin.getTime()));
											long sl_e = Math.min(time_B.getTime(),Math.min(solenoidend.getTime(), masterend.getTime()));
											if (sl_e - sl_b > 0)
											{
												thissolenoidtime += sl_e - sl_b;
											}
										}
										else if (!s_v.get(solenoidbegin).equals(Float.NaN) && s_v.get(solenoidbegin) < 0.09f)
										{
											long sl_b = Math.max(time_A.getTime(),Math.max(solenoidbegin.getTime(), masterbegin.getTime()));
											long sl_e = Math.min(time_B.getTime(),Math.min(solenoidend.getTime(), masterend.getTime()));
											if (sl_e - sl_b > 0)
											{
												Timestamp s = new Timestamp(sl_b);
												Timestamp e = new Timestamp(sl_e);
												if(solenoid)
													unConsiderList = updateUnConsiderList(unConsiderList,new TimeZ(s,e));
											}
										}
										if(!solitr.hasNext())
											break;
										//need to check next solenoid
										if(solenoidend.before(masterend))
										{
											solenoidbegin = solenoidend;
											solenoidend = solitr.next();
										}
										else
											break;
									}								
								/* fine modifica correzione media */
								}
								else
								{
									thissolenoidtime=0;
								}
								
								long unconsiderTime = getUnconsiderTime(unConsiderList);
								//compute counters
								if(m_v.get(masterbegin)>new Float(params.get("max"))/* && (solenoid || defrost)*/)
								{
	//								System.out.println(thismastertime);
									//thismasterover = thismastertime - (defrost?thisdefrosttime:0) - (solenoid?thissolenoidtime:0) + (defrost&&solenoid?extraoverlaptime:0);
									thismasterover = thismastertime;
									thismasterover -= unconsiderTime;
								}
								else
								{
									thismasterover=0l;
								}
								
								if(m_v.get(masterbegin)<new Float(params.get("min"))/* && (solenoid || defrost)*/)
								{
	//								System.out.println(-thismastertime);
	//								thismasterunder += thismastertime - (defrost?thisdefrosttime:0) - (solenoid?thissolenoidtime:0) + (defrost&&solenoid?extraoverlaptime:0);
									thismasterunder = thismastertime;
									thismasterunder -= unconsiderTime;
								}
								else
								{
									thismasterunder=0l;
								}
								
								//totals
								totalmastertime +=thismastertime;
								totalmasterover += thismasterover;
								totalmasterunder += thismasterunder;
								totaldefrosttime += thisdefrosttime;
								totalsolenoidtime += thissolenoidtime;
								long timeformean = thistimeformean - unconsiderTime;
								if(timeformean>0)
								{
									//mean = (mean * totaltimeformean+ m_v.get(masterbegin) * timeformean)/(totaltimeformean + timeformean);
									mean += m_v.get(masterbegin) * timeformean;
									totaltimeformean += timeformean;
								}
							}
						}
						masterbegin = masterend;
						if (masteritr.hasNext())
						{
							masterend = masteritr.next();
						}
					}while (masteritr.hasNext());
				} 
				else
				{
					ret.clear();
				}
			}
			else
			{
				ret.clear();
			}
			masterc.close();
			defrostc.close();
			solenoidc.close();			
			ret.put("tover", new Long(totalmasterover));//new Double(totalmasterover));
			if (totalmastertime > 0)
				ret.put("pover", (double)totalmasterover / totalmastertime);
			else
				ret.put("pover", new Double(0));
			ret.put("tunder", new Long(totalmasterunder));//new Double(totalmasterunder));
			if (totalmastertime > 0)
				ret.put("punder", (double)totalmasterunder / totalmastertime);
			else
				ret.put("punder", new Double(0));
			mean = mean/totaltimeformean;
			ret.put("mean", new Double(mean));
			if (totalmastertime > 0)
				ret.put("dc", (double)totalsolenoidtime / totalmastertime);
			else
				ret.put("dc", new Double(0));
			if(defrostdata)
				ret.put("tdef", new Long(totaldefrosttime));//new Double(totaldefrosttime));
			if (totalmastertime > 0 && defrostdata)
				ret.put("pdef", (double)totaldefrosttime / totalmastertime);
//			else
//				ret.put("pdef", new Double(0));
			if(solenoiddata)
				ret.put("tsol", new Long(totalsolenoidtime));//new Double(totalsolenoidtime));
			if (totalmastertime > 0 && solenoiddata)
				ret.put("psol", (double)totalsolenoidtime/ totalmastertime);
//			else
//				ret.put("psol", new Double(0));
			ret.put("total", new Long(totalmastertime));//new Double(totalmastertime));
		}
		catch(Exception e)
		{
			try{masterc.close();} catch (Exception ex) {}
			try{defrostc.close();} catch (Exception ex) {}
			try{solenoidc.close();} catch (Exception ex) {}
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			ret.clear();
		}
//		System.out.println();
//		System.out.println();
		return ret;
	}
	
	private static LinkedHashMap<Timestamp, Float> getValues(ResultSet rs)
	{
		LinkedHashMap<Timestamp, Float> hm = new LinkedHashMap<Timestamp, Float>();
		Timestamp ts;
		Float val;
		try
		{
			while (rs.next())
			// for(int i = 0; i<10;i++)
			{
				// Record r = rs.get(i);
				ts = (Timestamp) rs.getTimestamp("inserttime");
				val = rs.getFloat("value");
				if (!rs.wasNull())
					hm.put(ts, val);
				else
					hm.put(ts, Float.NaN);
				long freq = 1000L * rs.getInt("frequency");
				int acc = 0;
				boolean cont = true;
				for (int j = 1; (j <= 63) && (cont == true); j++)
				{
					try
					{
						long a = new Long(rs.getInt("n" + j));
						acc += a;
						val = rs.getFloat("value" + j);
						if(rs.wasNull())
							val = Float.NaN;
						if(j==63)
						{
							cont=false;
							continue;
						}
						long nexta = new Long(rs.getInt("n" + (j+1)));
						hm.put(new Timestamp(ts.getTime() + freq * acc), nexta!=0f ? val : Float.NaN);
						if (nexta == 0)
						{
							cont = false;
							continue;
						}
					} catch (SQLException e)
					{
						LoggerMgr.getLogger(KpiMgr.class).error(e);
					}
				}
			}
		} catch (Exception e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
		}
		return hm;
	}

	public static int grpNum()
	{
		try
		{
			return (Integer)DatabaseMgr.getInstance().executeQuery(null, "select count(*) from kpigroups;").get(0).get(0);
		} catch (DataBaseException e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return -1;
		}
	}
	
	// query modified to exclude 'Internal IO' device from device selection
	// Nicola Compagno 25032010
	public static RecordSet getDevices(String lan)
	{
		try {
			return DatabaseMgr.getInstance().executeQuery(null, 
					"select cfdevice.*,cftableext.description " +
					"from cfdevice, cftableext " +
					"where cftableext.idsite=1 " +
					"and cftableext.languagecode='"+lan+"' " +
					"and cftableext.tablename='cfdevice' " +
					"and cftableext.tableid=cfdevice.iddevice " +
					"and cfdevice.islogic='FALSE' " +
					"and cfdevice.iscancelled='FALSE' " +
					"and cfdevice.code != '-1.000'" +
					"order by description; "
					);
		} 
		catch (DataBaseException e) 
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
	}
	
	public static RecordSet getDevicesGroups()
	{
		try
		{
			return DatabaseMgr.getInstance().executeQuery(null,
					"select iddevice, kpidevices.idgrp " +
					"from kpigroups, kpidevices " +
					"where kpigroups.idgrp=kpidevices.idgrp " +
					"order by kpidevices.idgrp, iddevice;"
					);
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
	}
	
	public static RecordSet getGroups()
	{
		try
		{
			return DatabaseMgr.getInstance().executeQuery(null, "select * from kpigroups order by idgrp;");
		} catch (DataBaseException e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
	}
	
	public static RecordSet getDevicesModel(String grp, String lang)
	{
		try
		{
			return DatabaseMgr.getInstance().executeQuery(null,
					"select distinct " +
					"tmp.idgrp, tmp.iddevmdl, tmp.description, " +
					"kpiconf.mastervarmdl, kpiconf.defvarmdl, kpiconf.solenoidvarmdl "+
					"from "+
					"(select kpidevices.*, cfdevice.iddevmdl, dd.description "+
					"from kpidevices, cfdevice, cftableext as dd "+
					"where "+
					"kpidevices.idgrp="+grp+" " +
					"and cfdevice.iscancelled='FALSE' "+
					"and kpidevices.iddevice=cfdevice.iddevice "+
					"and dd.idsite = 1 "+
					"and dd.tablename='cfdevmdl' "+
					"and dd.tableid=cfdevice.iddevmdl "+
					"and dd.languagecode='"+lang+"' "+
					") as tmp left outer join kpiconf "+
					"on tmp.idgrp=kpiconf.idgrp "+
					"and tmp.iddevmdl=kpiconf.iddevmdl " +
					"order by tmp.idgrp, tmp.description");
		}
		catch (DataBaseException e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
	}

	public static RecordSet getMastervar(String iddevmdl, String lang)
	{
		try
		{
			return DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl, description "+
					"from "+
					"cfvarmdl,cftableext "+
					"where "+
					"cfvarmdl.iddevmdl="+iddevmdl+" " +
					//"and cfvarmdl.hsfrequency is not null " +
					"and cftableext.idsite = 1 "+
					"and cftableext.tablename='cfvarmdl' "+
					"and cftableext.tableid=cfvarmdl.idvarmdl "+
					"and cftableext.languagecode='"+lang+"' " +
					"order by description;");
		} catch (DataBaseException e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
	}

	public static RecordSet getDefrost(String iddevmdl, String lang)
	{
		try
		{
			return DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl, description "+
					"from "+
					"cfvarmdl,cftableext "+
					"where "+
					"cfvarmdl.iddevmdl="+iddevmdl+" " +
					//"and cfvarmdl.hsfrequency is not null "+
					"and cfvarmdl.type = 1 "+
					"and cftableext.idsite = 1 "+
					"and cftableext.tablename='cfvarmdl' "+
					"and cftableext.tableid=cfvarmdl.idvarmdl "+
					"and cftableext.languagecode='"+lang+"' " +
					"order by description;");
		} catch (DataBaseException e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
	}

	public static RecordSet getSolenoid(String iddevmdl, String lang)
	{
		try
		{
			return DatabaseMgr.getInstance().executeQuery(null, 
					"select idvarmdl, description "+
					"from "+
					"cfvarmdl,cftableext "+
					"where "+
					"cfvarmdl.iddevmdl="+iddevmdl+" "+
					//"and cfvarmdl.hsfrequency is not null "+
					"and cfvarmdl.type = 1 "+
					"and cftableext.idsite = 1 "+
					"and cftableext.tablename='cfvarmdl' "+
					"and cftableext.tableid=cfvarmdl.idvarmdl "+
					"and cftableext.languagecode='"+lang+"' " +
					"order by description;");
		} catch (DataBaseException e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
	}

	public static RecordSet getDeviceGroups(int dev)
	{
		try
		{
			return DatabaseMgr.getInstance().executeQuery(null,
					"select kpidevices.idgrp " +
					"from kpigroups, kpidevices " +
					"where kpigroups.idgrp=kpidevices.idgrp " +
					"and kpidevices.iddevice="+dev+" " +
					"order by kpidevices.idgrp;"
					);
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
	}
	
	public static RecordSet getGroupConf(int idgrp)
	{
		try
		{
			return DatabaseMgr.getInstance().executeQuery(null,
					"select "+
					"kpigroups.*, "+
					"kpiconf.iddevmdl, kpiconf.mastervarmdl, kpiconf.defvarmdl, kpiconf.solenoidvarmdl, "+
					"cfdevice.iddevice, "+
					"a.idvariable as vmid, b.idvariable as vdid, c.idvariable as vsid " +
					//",cfdevice.code "+
					"from "+
					"kpigroups inner join kpiconf "+
					"on "+
					"kpigroups.idgrp="+idgrp+" and "+
					"kpigroups.idgrp=kpiconf.idgrp "+
					"inner join kpidevices "+
					"on "+
					"kpiconf.idgrp=kpidevices.idgrp "+
					"inner join cfdevice "+
					"on "+
					"kpiconf.iddevmdl=cfdevice.iddevmdl and "+
					"kpidevices.iddevice=cfdevice.iddevice and "+
					"cfdevice.iscancelled='FALSE' and "+
					"cfdevice.islogic='FALSE' "+
					"left outer join "+
					"cfvariable as a "+
					"on "+
					"a.idvarmdl=kpiconf.mastervarmdl and "+
					"a.iddevice = cfdevice.iddevice and "+
					"a.idhsvariable is null and " +
					"a.iscancelled='FALSE' "+
					"left outer join cfvariable as b "+
					"on "+
					"b.idvarmdl=kpiconf.defvarmdl and "+
					"b.iddevice = cfdevice.iddevice and "+
					"b.idhsvariable is null and " +
					"b.iscancelled='FALSE' "+
					"left outer join cfvariable as c "+
					"on "+
					"c.idvarmdl=kpiconf.solenoidvarmdl and "+
					"c.iddevice = cfdevice.iddevice and "+
					"c.idhsvariable is null and " +
					"c.iscancelled='FALSE' " +
					"order by kpigroups.idgrp, cfdevice.code"
					);
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
	}
	
	public static RecordSet getLogicGroupConf(int idgrp)
	{
		try
		{
			return DatabaseMgr.getInstance().executeQuery(null,
					"select kpigroups.*, " +
					"kpiconf.iddevmdl, kpiconf.mastervarmdl, kpiconf.defvarmdl, kpiconf.solenoidvarmdl, " +
					"cfdevice.code "+
					"from kpigroups " +
					"inner join kpiconf " +
					"on kpigroups.idgrp="+idgrp+" " +
					"and kpigroups.idgrp = kpiconf.idgrp " +
					"inner join cfdevice " +
					"on kpiconf.iddevmdl=cfdevice.iddevice "+
					"and cfdevice.islogic='TRUE '" +
					"order by kpigroups.idgrp, cfdevice.code "
					);
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
	}

	public static RecordSet getLogicDevices(String lan)
	{
		try {
			return DatabaseMgr.getInstance().executeQuery(null, 
					"select cfdevice.*,cftableext.description " +
					"from cfdevice, cftableext " +
					"where cftableext.idsite=1 " +
					"and cftableext.languagecode='"+lan+"' " +
					"and cftableext.tablename='cfdevice' " +
					"and cftableext.tableid=cfdevice.iddevice " +
					"and cfdevice.islogic='TRUE' " +
					"and cfdevice.iscancelled='FALSE' " +
					"order by cftableext.description; "
					);
		} 
		catch (DataBaseException e) 
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
	}
	
	public static RecordSet getLogicDevices(String grp, String lang)
	{
		try
		{
			return DatabaseMgr.getInstance().executeQuery(null,
					"select "+
					"tmp.idgrp, tmp.idlogic, tmp.description, "+
					"kpiconf.mastervarmdl, kpiconf.defvarmdl, kpiconf.solenoidvarmdl "+
					"from "+
					"(select kpidevices.*, cfdevice.iddevice as idlogic, dd.description "+
					"from kpidevices, cfdevice, cftableext as dd "+
					"where "+
					"kpidevices.idgrp="+grp+" "+
					"and cfdevice.iscancelled='FALSE' "+
					"and cfdevice.islogic='TRUE' "+
					"and kpidevices.iddevice=cfdevice.iddevice "+
					"and dd.idsite = 1 "+
					"and dd.tablename='cfdevice' "+
					"and dd.tableid=cfdevice.iddevice "+
					"and dd.languagecode='"+lang+"' "+
					") as tmp left outer join kpiconf "+
					"on tmp.idgrp=kpiconf.idgrp "+
					"and tmp.idlogic=kpiconf.iddevmdl " +
					"order by tmp.idgrp,tmp.description"
					);
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}
	}
	
	public static RecordSet getLogicMasterVar(String iddevlogic, String language)
	{
		try
		{
			return DatabaseMgr.getInstance().executeQuery(null,
					"select idvariable, description "+
					"from "+
					"cfvariable,cftableext "+
					"where "+
					"cfvariable.iddevice="+iddevlogic+" " +
					"and cfvariable.iscancelled='FALSE' "+
					//"and cfvariable.idhsvariable is not null " +
					"and cfvariable.idhsvariable is null " +
					"and cftableext.idsite = 1 "+
					"and cftableext.tablename='cfvariable' "+
					"and cftableext.tableid=cfvariable.idvariable "+
					"and cftableext.languagecode='"+language+"' " +
					"order by description;"
					);
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}	}

	public static RecordSet getLogicDefrost(String iddevlogic, String language)
	{
		try
		{
			return DatabaseMgr.getInstance().executeQuery(null,
					"select idvariable, description "+
					"from "+
					"cfvariable,cftableext "+
					"where "+
					"cfvariable.iddevice="+iddevlogic+" "+
					"and cfvariable.iscancelled='FALSE' "+
					//"and cfvariable.idhsvariable is not null "+
					"and cfvariable.idhsvariable is null "+
					"and cfvariable.type = 1 "+
					"and cftableext.idsite = 1 "+
					"and cftableext.tablename='cfvariable' "+
					"and cftableext.tableid=cfvariable.idvariable "+
					"and cftableext.languagecode='"+language+"' " +
					"order by description;"
					);
		}
		catch (Exception e)
		{
			LoggerMgr.getLogger(KpiMgr.class).error(e);
			return null;
		}	
	}
	static class TimeZ
	{
		Timestamp start;
		Timestamp end;
		
		public TimeZ(Timestamp start,Timestamp end) throws Exception
		{
			if(start.after(end))
				throw new Exception("start after end");
			this.start = start;
			this.end = end;
		}
		public String toString()
		{
			return this.start+"~"+this.end;
		}
	}
	public static Date string2Date(String str,String formParam) throws ParseException
    {
    	SimpleDateFormat formatter = new SimpleDateFormat(formParam);
    	return formatter.parse(str);
    }
	public static void main(String[] args) throws Exception
	{
		List<TimeZ> list = null;
		String format = "yyyy-MM-dd HH:mm:ss";
		Date d1 = string2Date("2011-9-2 00:00:00",format);
		Date d2 = string2Date("2018-9-2 02:00:00",format);
		TimeZ t1 = new TimeZ(new Timestamp(d1.getTime()),new Timestamp(d2.getTime()));
		list = updateUnConsiderList(list,t1);
		d1 = string2Date("2015-9-1 02:00:00",format);
		d2 = string2Date("2015-9-1 03:00:00",format);
		TimeZ t2 = new TimeZ(new Timestamp(d1.getTime()),new Timestamp(d2.getTime()));
		list = updateUnConsiderList(list,t2);
		d1 = string2Date("2015-8-2 02:00:00",format);
		d2 = string2Date("2015-8-2 03:00:00",format);
		t2 = new TimeZ(new Timestamp(d1.getTime()),new Timestamp(d2.getTime()));
		list = updateUnConsiderList(list,t2);
		d1 = string2Date("2015-2-2 02:00:00",format);
		d2 = string2Date("2015-11-2 03:00:00",format);
		t2 = new TimeZ(new Timestamp(d1.getTime()),new Timestamp(d2.getTime()));
		list = updateUnConsiderList(list,t2);
		d1 = string2Date("2016-2-2 02:00:00",format);
		d2 = string2Date("2016-11-2 03:00:00",format);
		t2 = new TimeZ(new Timestamp(d1.getTime()),new Timestamp(d2.getTime()));
		list = updateUnConsiderList(list,t2);
		System.out.println(list);
	}
	public static List<TimeZ> updateUnConsiderList(List<TimeZ> unConsiderList, TimeZ time) throws Exception
	{
		if(unConsiderList == null)
			unConsiderList = new ArrayList<TimeZ>();
		List<Integer> removeList = new ArrayList<Integer>();
		boolean addToList = false;
		if(unConsiderList.size() == 0)
		{
			unConsiderList.add(time);
			return unConsiderList;
		}
		for(int i=0;i<unConsiderList.size();i++)
		{
			TimeZ z = unConsiderList.get(i);
			if(time.end.before(z.start))
			{
				addToList = true;
				break;
			}
			else if(!time.start.after(z.end) && !time.end.before(z.start))
			{
				if(time.start.before(z.start))
					z.start = time.start;
				if(time.end.after(z.end))
				{
					if((i+1)<unConsiderList.size())
					{
						for(int j=i+1;j<unConsiderList.size();j++)
						{
							TimeZ zNext = unConsiderList.get(i+1);
							if(time.end.before(zNext.start))
							{
								z.end = time.end;
								break;
							}
							else if(!time.end.after(zNext.end))
							{
								removeList.add(j);
								z.end = zNext.end;
								break;
							}
							removeList.add(j);
							if((j+1)>=unConsiderList.size())
								z.end = time.end;
						}
					}
					else
					{
						z.end = time.end;
					}
				}
			}
			else if(time.start.after(z.end) && (i+1)>=unConsiderList.size())
				addToList = true;
		}
		if(removeList.size()>0)
		{
			for(int i=unConsiderList.size()-1;i>=0;i--)
			{
				for(Integer j:removeList)
				{
					if(i == j.intValue())
					{
						unConsiderList.remove(i);
						break;
					}
				}
			}
		}
		if(addToList)
		{
			List<TimeZ> result = new ArrayList<TimeZ>();
			boolean added = false;
			for(int i=0;i<unConsiderList.size();i++)
			{
				TimeZ z = unConsiderList.get(i);
				if(time.end.before(z.start))
				{
					added = true;
					result.add(time);
					result.add(z);
				}
				else 
				{
					result.add(z);
				}
			}
			if(!added)
				result.add(time);
			unConsiderList = result;
		}
		return unConsiderList;
	}
	public static long getUnconsiderTime(List<TimeZ> list)
	{
		long result = 0;
		for(TimeZ t:list)
		{
			result += t.end.getTime()-t.start.getTime();
		}
		return result;
	}
}
