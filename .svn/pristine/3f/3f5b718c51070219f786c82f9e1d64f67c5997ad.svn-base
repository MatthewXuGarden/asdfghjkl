package com.carel.supervisor.presentation.bo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.DebugBean;
import com.carel.supervisor.presentation.bean.DebugBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.kpi.KpiMgr;
import com.carel.supervisor.presentation.session.UserSession;

public class BKpiResult extends BoMaster
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7436238463717992027L;
	private static final String REPORTDIR = "TempReports";

	public BKpiResult(String l)
	{
		super(l, 0);
	}

	protected Properties initializeEventOnLoad() {
		Properties p = new Properties();
		p.put("tab1name", "enableAction(1);");

		return p;
	}

	protected Properties initializeJsOnLoad() {
		Properties p = new Properties();
		p.put("tab1name", "kpiresult.js;keyboard.js;../arch/FileDialog.js;");

		return p;
	}
	
	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab1name", DOCTYPE_STRICT);
		return p;
    }
	
	public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
	{
	}

	public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
	{
		StringBuffer toret = new StringBuffer();
		String cmd = prop.getProperty("kpicmd");
		if(cmd == null)
			cmd = prop.getProperty("cmd");
		if ("kpiresults".equalsIgnoreCase(cmd))
		{
			HashMap<String, String> hm = new HashMap<String, String>();

			hm.put("idgrp", prop.getProperty("idgrp"));

			hm.put("enddate", prop.getProperty("enddate"));
			hm.put("timewindow", prop.getProperty("timewindow"));

			hm.put("min", prop.getProperty("min"));
			hm.put("minp", prop.getProperty("minp"));
			hm.put("max", prop.getProperty("max"));
			hm.put("maxp", prop.getProperty("maxp"));

			hm.put("def", prop.getProperty("def"));
			hm.put("sol", prop.getProperty("sol"));

			hm.put("iddevmdl", prop.getProperty("iddevmdl"));
			hm.put("idmm", prop.getProperty("mastervarmdl"));
			hm.put("idmd", prop.getProperty("defvarmdl"));
			hm.put("idms", prop.getProperty("solenoidvarmdl"));
			hm.put("iddevice", prop.getProperty("iddevice"));
			hm.put("idvm", prop.getProperty("vmid"));
			hm.put("idvd", prop.getProperty("vdid"));
			hm.put("idvs", prop.getProperty("vsid"));

			Map<String, Number> res = KpiMgr.getResults(hm);
			if(res.isEmpty())
				us.setProperty("error", LangMgr.getInstance().getLangService(us.getLanguage()).getString("kpiresult", "errcomp"));

			String devdesc = "";
			try
			{
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select description "
						+ "from cftableext, cfdevice " + "where " + "cftableext.idsite=1"
						+ "and cftableext.tablename='cfdevice' " + "and cftableext.tableid='"
						+ prop.getProperty("iddevice") + "' " + "and languagecode='"
						+ us.getLanguage() + "' and cfdevice.iscancelled='FALSE'");
				devdesc = rs.get(0).get(0).toString();
			} catch (Exception e)
			{
				LoggerMgr.getLogger(this.getClass()).error(e);//.getMessage());
				us.setProperty("error", LangMgr.getInstance().getLangService(us.getLanguage()).getString("kpiresult", "errdev"));
			}

			boolean dtlviewEnabled = us.isMenuActive("dtlview");
			DecimalFormat df = new DecimalFormat("0.##");
			NumberFormat nf = new DecimalFormat("0.##%");//NumberFormat.getPercentInstance();
			
			toret.append("<result id='" + prop.getProperty("iddevice") + "'>");
			toret.append("<idgrp></idgrp>");
			toret.append("<iddevice></iddevice>");
			if(dtlviewEnabled)
				toret.append("<ddevice><![CDATA[<a href='javascript:void(0)' style='font-weight:normal' onclick=top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev="+prop.getProperty("iddevice")+"&desc=ncode01')>" + devdesc + "</a>]]></ddevice>");
			else
				toret.append("<ddevice><![CDATA["+ devdesc + "]]></ddevice>");
			toret.append("<st><![CDATA["+ getSt(us.getLanguage(),us.getIdSite(),Integer.valueOf(prop.getProperty("iddevice"))) + "]]></st>");
			toret.append("<total><![CDATA["	+ ((res.get("total") != null && !res.get("total").equals(Float.NaN)) ? formatNumber((Long)res.get("total")) : "---") + "]]></total>");
			toret.append("<tunder><![CDATA[" + ((res.get("tunder") != null && !res.get("tunder").equals(Float.NaN)) ? formatNumber((Long)res.get("tunder")) : "---") + "]]></tunder>");
			toret.append("<punder><![CDATA[" + ((res.get("punder") != null && !new Float(res.get("punder").floatValue()).equals(Float.NaN)) ? nf.format(res.get("punder")) : "---") + "]]></punder>");
			toret.append("<tover><![CDATA[" + ((res.get("tover") != null && !res.get("tover").equals(Float.NaN)) ? formatNumber((Long)res.get("tover")) : "---") + "]]></tover>");
			toret.append("<pover><![CDATA[" + ((res.get("pover") != null && !new Float(res.get("pover").floatValue()).equals(Float.NaN)) ? nf.format(res.get("pover")) : "---") + "]]></pover>");
			toret.append("<mean><![CDATA[" + ((res.get("mean") != null && !new Float(res.get("mean").floatValue()).equals(Float.NaN)) ? df.format(res.get("mean")) : "---") + "]]></mean>");
			//toret.append("<dc><![CDATA[" + ((res.get("dc") != null && !res.get("dc").equals(Float.NaN)) ? df.format(res.get("dc")) : "---") + "]]></dc>");
			toret.append("<tdef><![CDATA[" + ((res.get("tdef") != null && !res.get("tdef").equals(Float.NaN)) ? formatNumber((Long)res.get("tdef")) : "---") + "]]></tdef>");
			toret.append("<pdef><![CDATA[" + ((res.get("pdef") != null && !new Float(res.get("pdef").floatValue()).equals(Float.NaN)) ? nf.format(res.get("pdef")) : "---") + "]]></pdef>");
			toret.append("<tsol><![CDATA[" + ((res.get("tsol") != null && !res.get("tsol").equals(Float.NaN)) ? formatNumber((Long)res.get("tsol")) : "---") + "]]></tsol>");
			toret.append("<psol><![CDATA[" + ((res.get("psol") != null && !new Float(res.get("psol").floatValue()).equals(Float.NaN)) ? nf.format(res.get("psol")) : "---") + "]]></psol>");
			toret.append("</result>");
		}
		else if("kpiexportresults".equalsIgnoreCase(cmd))
		{
			toret.append("<response>");
			try
			{
				LangService ls = LangMgr.getInstance().getLangService(us.getLanguage());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

				String local = prop.getProperty("local");
	            String file = "";
	            if("true".equalsIgnoreCase(local))
	            {
	            	String path = prop.getProperty("path");
	            	file = path;
	            }
	            else
	            {
	            	file = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator + 
	            		"KPI_"+DateUtils.date2String(new Date(), "yyyyMMddhhmmss")+".csv";
	            }
				File f = new File(file);
				//FileOutputStream fos = new FileOutputStream(f);
				BufferedWriter fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), Charset.forName("UTF-8")));
				fos.write(("\""+ls.getString("kpicsv", "site")+":\";"+us.getSiteName()+"\n"));
		        fos.write(("\""+ls.getString("kpicsv", "user")+":\";"+us.getUserName()+"\n"));
		        fos.write(("\""+ls.getString("kpicsv", "time")+":\";"+new Date().toString()+"\n"));
		        fos.write(("\""+ls.getString("kpicsv", "group")+":\";"+prop.getProperty("kpigroup")+"\n"));
//		        fos.write(("\""+ls.getString("kpicsv", "mastervar")+":\";"+prop.getProperty("mastervar")+"\n"));
//		        fos.write(("\""+ls.getString("kpicsv", "defrostvar")+":\";"+prop.getProperty("defrostvar")+"\n"));
//		        fos.write(("\""+ls.getString("kpicsv", "solenoidvar")+":\";"+prop.getProperty("solenoidvar")+"\n"));
		        fos.write(("\""+ls.getString("kpicsv", "min")+":\";"+prop.getProperty("kpimin")+"\n"));
		        fos.write(("\""+ls.getString("kpicsv", "minp")+":\";"+prop.getProperty("kpiminp")+"\n"));
		        fos.write(("\""+ls.getString("kpicsv", "max")+":\";"+prop.getProperty("kpimax")+"\n"));
		        fos.write(("\""+ls.getString("kpicsv", "maxp")+":\";"+prop.getProperty("kpimaxp")+"\n"));
		        fos.write(("\""+ls.getString("kpicsv", "def")+":\";"+prop.getProperty("kpidef")+"\n"));
		        fos.write(("\""+ls.getString("kpicsv", "sol")+":\";"+prop.getProperty("kpisol")+"\n"));
		        
		        String data = prop.getProperty("data");
		        fos.write(data);
		        fos.close();
				toret.append("<file><![CDATA["+file+"]]></file>");
			}
			catch(Exception e)
			{
				LoggerMgr.getLogger(this.getClass()).error(e);
				toret.append("<file><![CDATA[ERROR]]></file>");
			}
			toret.append("</response>");
		}
		else if("kpigraph".equalsIgnoreCase(cmd))
		{
			LangService ls = LangMgr.getInstance().getLangService(us.getLanguage());
			Date dd;
			DateFormat ddf=null;
			File ff=null;
			//FileWriter fw=null;
			BufferedWriter fw =  null;
			try{
				dd = new Date();
				ddf = new SimpleDateFormat("yyyyMMdd-HHmmss");
				ff = new File(System.getenv("PVPRO_HOME")+File.separator+"PvPro");
				ff.mkdirs();
				File f = new File(ff.getAbsolutePath()+File.separator+ddf.format(dd)+".xml");
				//fw = new FileWriter(ff.getAbsolutePath()+File.separator+ddf.format(dd)+".xml");
				fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), Charset.forName("UTF-8")));
				fw.write("<kpi ReportDate='"+
						ls.getString("kpigraph", "reportfrom")+
						prop.getProperty("kpistart")+" " +
						ls.getString("kpigraph", "reportto")+
						prop.getProperty("kpistop")+"' " +
						"Info='"+
						ls.getString("kpigraph", "min")+
						prop.getProperty("kpimin")+" "+
						ls.getString("kpigraph", "minperc")+
						prop.getProperty("kpiminp")+" "+
						ls.getString("kpigraph", "max")+
						prop.getProperty("kpimax")+" "+
						ls.getString("kpigraph", "maxperc")+
						prop.getProperty("kpimaxp")+" "+
						ls.getString("kpigraph", "def")+
						("true".equalsIgnoreCase(prop.getProperty("kpidef"))?"ON":"OFF")+" "+
						ls.getString("kpigraph", "sol")+
						("true".equalsIgnoreCase(prop.getProperty("kpisol"))?"ON":"OFF")+" "+
						"' " +
						"Site='"+us.getSiteName()+" -- "+prop.getProperty("kpigroup")+"' " +
						"User='"+us.getUserName()+"' " +
						"DCText='"+ls.getString("kpigraph", "dutycolor")+"' " +
						"OMText='"+ls.getString("kpigraph", "overcolor")+"' " +
						"BMText='"+ls.getString("kpigraph", "undercolor")+"' " +
						"TopScale='"+ls.getString("kpigraph", "topscale")+"' " +
						"BottomScale='"+ls.getString("kpigraph", "bottomscale")+"' >");
				fw.write("<instruments>");
				String data = prop.getProperty("data");
				String[] datarows = data.split("\n");
				for(int i=1;i<datarows.length;i++){
					String[] datadata = datarows[i].split(";");
					fw.write("<instrument name='" +
							datadata[0] +
							"' DC='"+datadata[10].substring(0,datadata[10].length()-1)+
							"' OM='"+datadata[5].substring(0,datadata[5].length()-1)+
							"' BM='"+datadata[3].substring(0,datadata[3].length()-1)+"' />");
				}
				fw.write("</instruments>");
				fw.write("</kpi>");
				fw.close();
				toret.append("<filename>"+ddf.format(dd)+"</filename>");
			}catch(Exception e){
				LoggerMgr.getLogger(this.getClass()).error(e);
				try {
					fw.close();
				} catch (Exception exxxxxxx) {}
			}
		}
		return toret.toString();
	}
	private String getSt(String language,int idsite,int iddevice)
	throws Exception
	{
		DeviceInfoList deviceInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");
		DeviceInfo device = deviceInfoList.getByIdDevice(iddevice);
		DebugBeanList debug = DebugBeanList.getInstance();
		DebugBean varBean = debug.getDebugBean(device.getDescription());
		if(varBean != null)
		{
			String stCode = varBean.getStcode();
			VarphyBean[] vars = VarphyBeanList.getByiddevice_devmdlcodes(language,idsite,iddevice,new String[]{stCode});
			if(vars.length>0)
				return ControllerMgr.getInstance().getFromField(vars[0]).getFormattedValue()+" "+vars[0].getMeasureUnit();
			else
				return "";
		}
		return "";
	}
	private String formatNumber(Long number) {
		DecimalFormat mydf = new DecimalFormat("00");
		String toret="";
		if((int)(number/86400000L)>0)
		{
			toret += ""+(int)(number/86400000L)+"d";
			number = number - (86400000L*(int)(number/86400000L));
		}
		if((int)(number/3600000L)>0)
		{
			toret += ""+mydf.format((int)(number/3600000L))+"h";
			number = number - (3600000L*(int)(number/3600000L));
		}
		toret += mydf.format(Math.round(number/60000L)) + "m";
		return toret;
	}
}
