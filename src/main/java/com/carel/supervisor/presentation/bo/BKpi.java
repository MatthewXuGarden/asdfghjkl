package com.carel.supervisor.presentation.bo;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.kpi.KpiMgr;
import com.carel.supervisor.presentation.session.UserSession;

public class BKpi extends BoMaster
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7436238463717992027L;

	public BKpi(String l)
	{
		super(l, 0);
	}

	protected Properties initializeEventOnLoad() {
		Properties p = new Properties();
		p.put("tab2name", "enableAction(1);disablegroups();");
		p.put("tab3name", "enableAction(1);");
		return p;
	}

	protected Properties initializeJsOnLoad() {
		Properties p = new Properties();
		p.put("tab1name", "kpi.js;");
		p.put("tab2name", "kpi.js;keyboard.js;");
		p.put("tab3name", "kpi.js;keyboard.js;");
		return p;
	}

	public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
	{
		String cmd = prop.getProperty("kpicmd");
		/*
		if("kpigenreport".equalsIgnoreCase(cmd))
		{
			// generate report for group
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("date", 
					us.getPropertyAndRemove("kpiyy")+"-"+
					us.getPropertyAndRemove("kpimm")+"-"+
					us.getPropertyAndRemove("kpidd")+" "+
					us.getPropertyAndRemove("kpihh")+":00");
			params.put("longrew", us.getPropertyAndRemove("kpirew"));
			params.put("minval", us.getPropertyAndRemove("kpimin"));
			params.put("minperc", us.getPropertyAndRemove("kpiminp"));
			params.put("maxval", us.getPropertyAndRemove("kpimax"));
			params.put("maxperc", us.getPropertyAndRemove("kpimaxp"));
			params.put("def", us.getPropertyAndRemove("kpidef"));
			params.put("sol", us.getPropertyAndRemove("kpisol"));
			KpiMgr.getResults(params);
		}
		*/
		if("kpiexportreport".equalsIgnoreCase(cmd))
		{
			// export report to csv
		}
		if("kpiconf1".equalsIgnoreCase(cmd))
		{
			// configure
			for(int i=1;i<=10;i++)
			{
				String grname = prop.getProperty("gr"+i);
				if(grname!=null && !grname.equals(""))
				{
					try 
					{
						Connection conn = DatabaseMgr.getInstance().getConnection(null);
						Statement st = conn.createStatement();
						int m = st.executeUpdate("update kpigroups set name = '"+grname+"' where idgrp = "+i+";");
						if( m==0 )
						{
							st.execute("insert into kpigroups values("+i+",'"+grname+"',null,null,null,null);");
						}
						st.close();
						conn.commit();
						conn.close();
					} 
					catch (Exception e) 
					{
						LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
						us.setProperty("error", LangMgr.getInstance().getLangService(us.getLanguage()).getString("kpi", "errgrp"));
					}
				}
				else
				{
					try 
					{
						Connection conn = DatabaseMgr.getInstance().getConnection(null);
						Statement st = conn.createStatement();
						st.executeUpdate("delete from kpigroups where idgrp = "+i+";");
						st.close();
						conn.commit();
						conn.close();
					} 
					catch (Exception e) 
					{
						LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
						us.setProperty("error", LangMgr.getInstance().getLangService(us.getLanguage()).getString("kpi", "errgrp"));
					}
				}
					
			}
			
			RecordSet devs = KpiMgr.getDevices("EN_en");
			for(int w=1;w<=10;w++)
			{
				DatabaseMgr.getInstance().executeStatement("delete from kpidevices where idgrp="+w+";", null);
				for(int t=0;t<devs.size();t++)
				{
					String iddevmdl = devs.get(t).get("iddevice").toString();
					String sel = prop.getProperty("gr"+w+"_"+iddevmdl);
					if(iddevmdl!=null && !"".equalsIgnoreCase(iddevmdl) && "on".equalsIgnoreCase(sel))
					{
						DatabaseMgr.getInstance().executeStatement("insert into kpidevices values ("+iddevmdl+","+w+");", null);
					}
				}
			}

			RecordSet devslogic = KpiMgr.getLogicDevices("EN_en");
			for(int w=1;w<=10;w++)
			{
				for(int t=0;t<devslogic.size();t++)
				{
					String iddevmdl = devslogic.get(t).get("iddevice").toString();
					String sel = prop.getProperty("gr"+w+"_"+iddevmdl);
					if(iddevmdl!=null && !"".equalsIgnoreCase(iddevmdl) && "on".equalsIgnoreCase(sel))
					{
						DatabaseMgr.getInstance().executeStatement("insert into kpidevices values ("+iddevmdl+","+w+");", null);
					}
				}
			}
			try
			{
				DatabaseMgr.getInstance().executeStatement("delete "+
				"from kpiconf "+
				"where kpiconf.iddevmdl not in "+
				"( "+
				"select distinct iddevmdl from cfdevice where iddevice in "+
				"( "+
				"select iddevice from kpidevices "+
				") "+
				") " +
				"or kpiconf.idgrp not in (select idgrp from kpigroups)",
				null);

			}
			catch (Exception e) 
			{
				LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
				us.setProperty("error", LangMgr.getInstance().getLangService(us.getLanguage()).getString("kpi", "errconf"));
			}
		}
		if("kpiconf2".equalsIgnoreCase(cmd))
		{
			for(int i=0;i<10;i++)
			{
				String idgrp = prop.getProperty("grp"+i);
				String min = prop.getProperty("min_"+idgrp);
				String minp = prop.getProperty("minp_"+idgrp);
				String max = prop.getProperty("max_"+idgrp);
				String maxp = prop.getProperty("maxp_"+idgrp);
				
				if(idgrp!=null && !idgrp.equalsIgnoreCase(""))
				{
					try 
					{
						Connection conn = DatabaseMgr.getInstance().getConnection(null);
						Statement st = conn.createStatement();
						int m = st.executeUpdate("update kpigroups " +
								"set min="+(!"".equalsIgnoreCase(min)?min:"null")+", " +
								"minperc="+(!"".equalsIgnoreCase(minp)?minp:"null")+", " +
								"max="+(!"".equalsIgnoreCase(max)?max:"null")+", " +
								"maxperc="+(!"".equalsIgnoreCase(maxp)?maxp:"null")+" " +
								"where idgrp="+idgrp+";");
						if( m==0 )
						{
							st.execute("insert into kpigroups values ("+idgrp+", '', "+min+", "+minp+", "+max+", "+maxp+");");
						}
						st.close();
						conn.commit();
						conn.close();
					} 
					catch (Exception e) 
					{
						LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
						us.setProperty("error", LangMgr.getInstance().getLangService(us.getLanguage()).getString("kpi", "errgrp"));
					}
				}
				
				RecordSet devs = KpiMgr.getDevicesModel(idgrp, "EN_en");
				for(int k = 0;k<devs.size();k++)
				{
					String iddm = devs.get(k).get("iddevmdl").toString();
					String mv = prop.getProperty("mv_"+idgrp+"_"+iddm);
					String dv = prop.getProperty("dv_"+idgrp+"_"+iddm);
					String sv = prop.getProperty("sv_"+idgrp+"_"+iddm);
					if("---".equalsIgnoreCase(mv))
						mv=null;
					if("---".equalsIgnoreCase(dv))
						dv=null;
					if("---".equalsIgnoreCase(sv))
						sv=null;
					try 
					{
						Connection conn = DatabaseMgr.getInstance().getConnection(null);
						Statement st = conn.createStatement();
						int m = st.executeUpdate("update kpiconf set mastervarmdl="+mv+", defvarmdl="+dv+", solenoidvarmdl="+sv+" where idgrp="+idgrp+" and iddevmdl="+iddm+";");
						if( m==0 )
						{
							st.execute("insert into kpiconf values ("+idgrp+", "+iddm+", "+mv+", "+dv+", "+sv+");");
						}
						st.close();
						conn.commit();
						conn.close();
					} 
					catch (Exception e) 
					{
						LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
						us.setProperty("error", LangMgr.getInstance().getLangService(us.getLanguage()).getString("kpi", "errconf"));
					}
				}

				RecordSet logics = KpiMgr.getLogicDevices(idgrp, "EN_en");
				for(int k = 0;k<logics.size();k++)
				{
					String iddm = logics.get(k).get("idlogic").toString();
					String mv = prop.getProperty("mv_"+idgrp+"_"+iddm);
					String dv = prop.getProperty("dv_"+idgrp+"_"+iddm);
					String sv = prop.getProperty("sv_"+idgrp+"_"+iddm);
					if("---".equalsIgnoreCase(mv))
						mv=null;
					if("---".equalsIgnoreCase(dv))
						dv=null;
					if("---".equalsIgnoreCase(sv))
						sv=null;
					try 
					{
						Connection conn = DatabaseMgr.getInstance().getConnection(null);
						Statement st = conn.createStatement();
						int m = st.executeUpdate("update kpiconf set mastervarmdl="+mv+", defvarmdl="+dv+", solenoidvarmdl="+sv+" where idgrp="+idgrp+" and iddevmdl="+iddm+";");
						if( m==0 )
						{
							st.execute("insert into kpiconf values ("+idgrp+", "+iddm+", "+mv+", "+dv+", "+sv+");");
						}
						st.close();
						conn.commit();
						conn.close();
					} 
					catch (Exception e) 
					{
						LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
						us.setProperty("error", LangMgr.getInstance().getLangService(us.getLanguage()).getString("kpi", "errconf"));
					}
				}
			}
			try
			{
				DatabaseMgr.getInstance().executeStatement("delete "+
				"from kpiconf "+
				"where kpiconf.iddevmdl not in "+
				"( "+
				"select distinct iddevmdl from cfdevice where iddevice in "+
				"( "+
				"select iddevice from kpidevices "+
				") "+
				") " +
				"or kpiconf.idgrp not in (select idgrp from kpigroups)",
				null);

			}
			catch (Exception e) 
			{
				LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
				us.setProperty("error", LangMgr.getInstance().getLangService(us.getLanguage()).getString("kpi", "errconf"));
			}
		}
	}

	public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
	{
		return "";
	}
}
