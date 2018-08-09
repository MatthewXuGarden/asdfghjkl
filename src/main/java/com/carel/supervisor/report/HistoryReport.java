package com.carel.supervisor.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.util.DecimalFormatter;
import com.carel.supervisor.dataaccess.datalog.impl.ReportBean;
import com.carel.supervisor.dataaccess.datalog.impl.ReportExportConfigList;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.director.graph.GraphConstant;
import com.carel.supervisor.report.bean.HistoryReportBean;
import com.carel.supervisor.script.EnumerationMgr;

public class HistoryReport extends Report {

	//public static final String ENDTIME = "endtime";
	public static final String ENDTIME = "end";
	public static final String STARTTIME = "start";
	public static final String STARTINDEX = "start_idx";

	private long timeValues[] = null;
	private int timeK = 0;
	
	@Override
	public File generate() {
		ReportBean repdetail = (ReportBean) parameters.get(REPORTDETAIL); 
		Integer[] v = repdetail.getVariables();
		// old part - not refactored
		PaddingReport reportdata = null;
		ReportInformation reportInformation = new ReportInformation(10);
		for (int i = 0; i < v.length; i++) {
			reportInformation.enqueRecord(new Object[] { new Integer(1), v[i] });
		}
		
		if( parameters.get(STARTTIME) != null ){
			reportInformation.setStartTime((Timestamp)parameters.get(STARTTIME));
		}
		
		if(null!=parameters.get(ENDTIME)){
			reportInformation.setEndTime((Timestamp) parameters.get(ENDTIME));
		}else{
			reportInformation.setEndTime(new Timestamp(System.currentTimeMillis()));//(Timestamp) parameters.get(ENDTIME));			
		}
		
		if (repdetail.getTimelength()!=-1)  // daily or weekly reports
		{
			reportInformation.setReportPeriod(repdetail.getTimelength()*1000L);//(Long) parameters.get(PERIOD));
		}
		else            					// custom reports
		{
			reportInformation.setReportPeriod(reportInformation.getEndTime().getTime()- ((Timestamp) parameters.get(STARTTIME)).getTime());//(Long) parameters.get(PERIOD));
		}
		
		if( repdetail.getStep() == -1 ) {
			reportInformation.setSamplingPeriodRequest(5 * 60 * 1000L); // 5 minutes time granularity
			reportInformation.setTimeValues((Integer[])parameters.get(Report.TIMEVALUES));
		}
		else {
			reportInformation.setSamplingPeriodRequest(repdetail.getStep()*1000L);//(Long) parameters.get(STEP));
		}
		
		if (repdetail.getHaccp()){//(Boolean) parameters.get(HACCP)) {
			reportInformation.setReportType(GraphConstant.TYPE_HACCP);
		} else {
			reportInformation.setReportType(GraphConstant.TYPE_HISTORICAL);
		}
		try {
			ReportRequest reportRequest = new ReportRequest(reportInformation);
			reportRequest.startRetrieve();
			reportdata = new PaddingReport(reportRequest);
			reportdata.startPadding();
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		// old part end

		if( repdetail.getStep() == -1 ) {
        	long startTime = ((Timestamp)parameters.get(Report.START)).getTime();
			Integer itv[] = repdetail.getTimeValues();
			timeValues = new long[itv.length];
			for(int i = 0; i < itv.length; i++) {
				timeValues[i] = startTime + itv[i] * 1000;
			}
			timeK = 0;
		}
		
		if(((ReportBean)parameters.get(Report.REPORTDETAIL)).getOutputtype().equalsIgnoreCase("CSV"))
			return exportCSV(reportdata);
		else if (((ReportBean)parameters.get(Report.REPORTDETAIL)).getOutputtype().equalsIgnoreCase("HTML"))
			return exportHTML(reportdata);
		else
			return exportReport(reportdata);
	}

	@SuppressWarnings("unchecked")
	private File exportReport(PaddingReport reportdata) {
		String reportName = HistoryReport.truncate(parameters.get(Report.SITENAME).toString(), " - ",parameters.get(Report.CODE).toString(),15 ,46);
		parameters.put(Report.SITENAME, reportName);
		String tname = ((ReportBean) parameters.get(REPORTDETAIL)).getTemplatefile();
		JasperReport report = PrinterMgr2.getInstance().getTemplateMgr().getTemplate(tname);
		int reportwidth = report.getDetail().getElements().length-1;
//		System.out.println(report.getDetail().getElements().length-1);
		int subreports = (int)Math.ceil(new Float(reportdata.getAllSeries().length)/reportwidth);
		Map<Integer, String[]> m = (Map<Integer, String[]>)parameters.get(DESCRIPTIONS);
		int[] idvars = (int[]) parameters.get(VARIABLES);
		Integer[] idvarmdl = (Integer[]) parameters.get(VARS_MDL);
		LinkedList<HistoryReportBean> ll;
		LangService lang = LangMgr.getInstance().getLangService(parameters.get(LANGUAGE).toString());
		for(int i=0;i<idvars.length;i++){
			parameters.put("d"+i, m.get(idvars[i])[1]);
			parameters.put("v"+i, m.get(idvars[i])[0]);
		}
		long[] times = reportdata.getTimeGrid();
		Float[][] series = reportdata.getAllSeries();
		LinkedList<JasperPrint> jasperprintlist = new LinkedList<JasperPrint>();
		Integer offset = 0;
		parameters.put(OFFSET, offset);
		parameters.put(SUB_REP, subreports);
		int pages4rep= 0;
		String label = "";
		
		// preserve initial timeValues (filter alter timeValues content)
		// to restore them on the next variable cluster
		long timeValuesSave[] = null;
		if( timeValues != null ) {
			timeValuesSave = new long[timeValues.length];
			for(int i = 0; i < timeValues.length; i++)
				timeValuesSave[i] = timeValues[i];
		}
		
		for(int subrep=0;subrep<subreports;subrep++){
			ll = new LinkedList<HistoryReportBean>();
			if (reportdata != null) {
				for (int reprecord = 0; reprecord < times.length-1; reprecord++) {
					if( filter(times[reprecord]) )
						continue;
					String[] values = new String[reportwidth];
					for(int var=0;var<reportwidth;var++){
						String val = null;
						if(var+subrep*reportwidth<series.length){
							if(series[var+subrep*reportwidth][reprecord]!=null){
								val = series[var+subrep*reportwidth][reprecord].toString();
								label = EnumerationMgr.getInstance().getEnumCode(idvarmdl[var],series[var+subrep*reportwidth][reprecord],(String)parameters.get(LANGUAGE));
								if (!"".equals(label))
								{
									val = label;
								}
							} else {
								val="---";
							}
						} else {
							val = null;
						}
						values[var] = val;
					}
					ll.add(new HistoryReportBean(new Timestamp(times[reprecord]), values));
				}
				// restore initial timeValues
				if( timeValues != null ) {
					for(int i = 0; i < timeValues.length; i++)
						timeValues[i] = timeValuesSave[i];
					timeK = 0;
				}
			}
//			parameters.put(TITLE, parameters.get(SITENAME));// lang.getString("report", "alarmtit"));
			parameters.put(I18NDESC, lang.getString("report", I18NDESC));
			parameters.put(I18NDATE, lang.getString("report", I18NDATE));
			parameters.put(I18NTIME, lang.getString("report", I18NTIME));
			parameters.put(I18NPAGE, lang.getString("report", I18NPAGE));
			parameters.put(I18NOF, lang.getString("report", I18NOF));
			parameters.put(I18NSTAMP, lang.getString("report", I18NSTAMP));
			parameters.put(I18NSIGN1, lang.getString("report", I18NSIGN1));
			parameters.put(I18NSIGN2, lang.getString("report", I18NSIGN2));
			parameters.put(STARTINDEX, subrep*reportwidth);
			parameters.put(I18NFROM, lang.getString("report", I18NFROM));
			parameters.put(I18NTO, lang.getString("report", I18NTO));
			
//			parameters.put(IMAGE, "C:\\Carel\\PlantVisorPRO\\engine\\webapps\\PlantVisorPRO\\images\\top\\left.png");
			try {
				JRBeanCollectionDataSource bcd = new JRBeanCollectionDataSource(ll);
				JasperPrint print = PrinterMgr2.getInstance().fill(((ReportBean) parameters.get(REPORTDETAIL)).getTemplatefile(), parameters, bcd);
				if (subrep==0)
				{
					pages4rep = print.getPages().size();
				}
				offset+= pages4rep;
				parameters.put(OFFSET, offset);
				jasperprintlist.add(print);
				// TODO: paginazione con num totale di pagine; usare OFFSET
				
			} catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
				return null;
			}
		}
		ReportBean repdetail = (ReportBean) parameters.get(REPORTDETAIL);
		String islocal=(String)parameters.get(ISLOCAL);
		String filename="";
		String path="";
		if("true".equalsIgnoreCase(islocal)){
			path=(String)parameters.get(ExportPath);
			filename=(String)parameters.get(ExportFileName);
			path=path.substring(0, path.lastIndexOf(filename));
			return PrinterMgr2.getInstance().export(jasperprintlist, repdetail.getOutputtype(), filename,path);
		}else{
			if(this.isToTempFolder() == true)
			{
				path = BaseConfig.getCarelPath()+BaseConfig.getTemporaryFolder();
				return PrinterMgr2.getInstance().export(jasperprintlist, repdetail.getOutputtype(), "REPORT_" + String.valueOf(System.currentTimeMillis()),path);
			}
			else
			{
				return PrinterMgr2.getInstance().export(jasperprintlist, repdetail.getOutputtype(), "REPORT_" + String.valueOf(System.currentTimeMillis()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private File exportCSV(PaddingReport reportdata) {
		LangService lang = LangMgr.getInstance().getLangService(parameters.get(LANGUAGE).toString());
		String separator = ";";
		ReportExportConfigList conf = new ReportExportConfigList();
		Map<String,String> map = conf.loadMap();
		separator = map.get(ReportExportConfigList.SEPARATOR);
		separator = separator == null || separator.length()==0?";":separator;
		if (reportdata != null) {
			long[] times = reportdata.getTimeGrid();
			Float[][] series = reportdata.getAllSeries();
			parameters.put(I18NDATE, lang.getString("report", I18NDATE));
			parameters.put(I18NTIME, lang.getString("report", I18NTIME));
//			parameters.put("user","user name and surname");
			try {
				String islocal=(String)parameters.get(ISLOCAL);
				String filename="";
				String temppath="";
				if("true".equalsIgnoreCase(islocal)){
					temppath=(String)parameters.get(ExportPath);
					filename=(String)parameters.get(ExportFileName);
					temppath=temppath.substring(0, temppath.lastIndexOf(filename));
				}else{
					if(this.isToTempFolder())
					{
						temppath=BaseConfig.getCarelPath()+BaseConfig.getTemporaryFolder();
					}
					else
					{
						temppath=BaseConfig.getCarelPath()+File.separator+PrinterMgr2.getInstance().getSavePath();
					}
					filename="EXPORT_" + String.valueOf(System.currentTimeMillis());
				}
				
				File path = new File(temppath);
				path.mkdirs();
				File file = new File(path.getAbsolutePath()+File.separator+filename+".csv");
				BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
				
				fw.write(parameters.get(Report.CODE).toString()+separator+"\n");
				fw.write(DateUtils.date2String((Timestamp)parameters.get(Report.START),"yyyy-MM-dd HH:mm")+separator+DateUtils.date2String((Timestamp)parameters.get(Report.END),"yyyy-MM-dd HH:mm")+separator+"\n");
	        	fw.write(parameters.get("sitename").toString()+separator+"\n");
	        	fw.write((parameters.get("user")!=null?parameters.get("user").toString():"System")+separator+"\n");
	        	fw.write("\" \""+separator);
	        	//intestazione
	        	ReportBean repdetail = (ReportBean) parameters.get(REPORTDETAIL); 
	    		Integer[] v = repdetail.getVariables();
	    		Integer[] mdl = repdetail.getVarsMdl();
	    		Map<Integer,String[]> descriptions = (Map<Integer,String[]>)parameters.get(Report.DESCRIPTIONS);
	        	for(int i=0;i<v.length;i++){
	        		fw.write(descriptions.get(v[i])[1]+separator);//dev
	        	}
	        	fw.write("\n");
	        	fw.write("\" \""+separator);
	        	for(int i=0;i<v.length;i++){
	        		fw.write(descriptions.get(v[i])[0]+separator);//var
	        	}
	        	//dettaglio
	        	fw.write("\" \""+separator);
	        	fw.write("\n");
	        	
	        	String label = "";
	        	DecimalFormat formatter = DecimalFormatter.getCSVFormatter();
	        	DecimalFormat nodecimal = DecimalFormatter.getCSVFormatterNoDecimal();
	        	//vv
				if (series != null && times != null) {
					for (int i = 0; i < times.length; i++) {
						if( filter(times[i]) )
							continue;
						fw.write(new Timestamp(times[i]).toString());
						fw.write(separator);
						for (int j = 0; j < series.length; j++) {
							if(series[j][i]!=null && !series[j][i].isNaN()){
								label = EnumerationMgr.getInstance().getEnumCode(mdl[j].intValue(),series[j][i],(String)parameters.get(LANGUAGE));
								if (!"".equals(label))
									fw.write(label);//trasposed matrix
								else
									fw.write(DecimalFormatter.getValue(formatter,nodecimal,series[j][i].toString()));//trasposed matrix
							}
							fw.write(separator);
						}
						fw.write("\n");
					}
				}        	
	        	fw.flush();
	        	fw.close();
	        	return file;
			} catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
				return null;
			}
		}else{
			return null;
		}
	}
	
	private File exportHTML(PaddingReport reportdata) {
		LangService lang = LangMgr.getInstance().getLangService(parameters.get(LANGUAGE).toString());
		if (reportdata != null) {
			long[] times = reportdata.getTimeGrid();
			Float[][] series = reportdata.getAllSeries();
			parameters.put(I18NDATE, lang.getString("report", I18NDATE));
			parameters.put(I18NTIME, lang.getString("report", I18NTIME));

			try {
				String islocal=(String)parameters.get(ISLOCAL);
				String filename="";
				String temppath="";
				if("true".equalsIgnoreCase(islocal)){
					temppath=(String)parameters.get(ExportPath);
					filename=(String)parameters.get(ExportFileName);
					temppath=temppath.substring(0, temppath.lastIndexOf(filename));
				}else{
					if(this.isToTempFolder())
					{
						temppath=BaseConfig.getCarelPath()+BaseConfig.getTemporaryFolder();
					}
					else
					{
						temppath=BaseConfig.getCarelPath()+File.separator+PrinterMgr2.getInstance().getSavePath();
					}
					filename="EXPORT_" + String.valueOf(System.currentTimeMillis());
				}
				File path = new File(temppath);
				path.mkdirs();
				File file = new File(path.getAbsolutePath()+File.separator+filename+".html");
				BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
				
				fw.write("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body>\n");
				fw.write("<style type=\"text/css\">\n " +
						"  .table {" +
						"	border-color: #476AB0;" +
						"	border-spacing: 1px;" +
						"	border-style: solid;" +
						"	border-width: 1px;" +
						"	}" +
						"	.th {" +
						"	font-family: Tahoma,Verdana,Arial, Helvetica, sans-serif;" +
						"	font-size: 12px;" +
						"	border-width: 1px;" +
						"	border-color: #476AB0;" +
						"	padding: 1px 0.5em;" +
						"	background-color:#476AB0;" +
						"	color:#FFFFFF;" +
						"}	.Row1 {" +
						"	background-color:#EFF1FE;" +
						"	border-color:#FFFFFF;" +
						"	font-family: Tahoma,Verdana,Arial,Helvetica,sans-serif;" +
						" 	font-size: 12px;" +
						"}" +
						".tdTitleTable {" +
						"	font-family: Tahoma,Verdana,Arial,Helvetica,sans-serif;" +
						" 	font-size: 16px;" +
						"}</style>");
				
				fw.write("<p class='tdTitleTable'>"+(parameters.get(Report.USER)!=null?parameters.get(Report.USER).toString():"System"));
				fw.write("&nbsp;&nbsp;&nbsp;<b>"+parameters.get(Report.SITENAME).toString()+"</b>");
				fw.write("&nbsp;&nbsp;&nbsp;"+DateUtils.date2String(new Date(System.currentTimeMillis()), "yyyy/MM/dd HH:mm") +"</p>");
	        	
				fw.write("\t<p style='font-family: Tahoma,Verdana,Arial,Helvetica,sans-serif;font-size:12px'>"+lang.getString("report", I18NFROM)+"\t"+DateUtils.date2String(new Date(((Timestamp) parameters.get(Report.START)).getTime()), "yyyy/MM/dd HH:mm"));
				fw.write("&nbsp;&nbsp;&nbsp;"+lang.getString("report", I18NTO)+"\t"+
						DateUtils.date2String(new Date(((Timestamp) parameters.get(Report.END)).getTime()), "yyyy/MM/dd HH:mm"));
				fw.write("</p>");
				
	        	fw.write("<TABLE class='table' border='0' cellpadding='4' cellspacing='1'><TR class='th'>\n");
	        	
	        	
	        	ReportBean repdetail = (ReportBean) parameters.get(REPORTDETAIL); 
	    		Integer[] v = repdetail.getVariables();
	    		Integer[] mdl = repdetail.getVarsMdl();
	    		String label = "";
	    		Map<Integer,String[]> descriptions = (Map<Integer,String[]>)parameters.get(Report.DESCRIPTIONS);
	    		fw.write("<TD  align='center'>"+lang.getString("report", I18NDEVICE)+"</TD>");
	        	for(int i=0;i<v.length;i++){
	        		fw.write("<TD nowrap align='center'>"+descriptions.get(v[i])[1]+"</TD>");//dev
	        	}
	        	fw.write("</TR>\n");
	        	fw.write("<TR class='th'>");
	        	fw.write("<TD nowrap align='center'>"+lang.getString("report", I18NVAR)+"</TD>");
	        	for(int i=0;i<v.length;i++){
	        		fw.write("<TD nowrap align='center'>"+descriptions.get(v[i])[0]+"</TD>");//var
	        	}
	        	fw.write("</TR>\n");
	        	

				if (series != null && times != null) 
				{
					for (int i = 0; i < times.length; i++) 
					{
						if( filter(times[i]) )
							continue;

						fw.write("<TR class='Row1'>\n");
						fw.write("<TD nowrap align='center'>" + DateUtils.date2String(new Timestamp(times[i]),"yyyy/MM/dd HH:mm") + "</TD>");

						for (int j = 0; j < series.length; j++) 
						{
							if(series[j][i]!=null && !series[j][i].isNaN())
							{
								label = EnumerationMgr.getInstance().getEnumCode(mdl[j].intValue(),series[j][i],(String)parameters.get(LANGUAGE));
								if (!"".equals(label))
									fw.write("<TD nowrap align='center'>"+label+"</TD>");//trasposed matrix
								else
									fw.write("<TD nowrap align='center'>"+series[j][i].toString()+"</TD>");//trasposed matrix
							}
							else
								fw.write("<TD>&nbsp;</TD>");
						}
						fw.write("</TR>\n");
					}
				}        	
				fw.write("</TABLE>\n");
				fw.write("</BODY>\n");
				fw.write("</HTML>\n");
	        	fw.flush();
	        	fw.close();
	        	return file;
			} catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
				return null;
			}
		}else{
			return null;
		}
	}

	
	private boolean filter(long currentTime)
	{
		if( timeValues == null )
			return false;
		if( timeValues.length > 0 ) {
			if( timeValues[timeK] == currentTime ) {
				timeValues[timeK] += 24 * 60 * 60 * 1000; // advance to next day
				timeK++; // advance to next time peak
				if( timeK == timeValues.length )
					timeK = 0;
				return false;
			}
		}
		return true;
	}
	public static String truncate(String str1,String connector,String str2,int maxLengthForFirst,int length)
	{
		if(str1 == null)
			str1 = "";
		if(str2 == null)
			str2 = "";
		if(connector == null)
			connector = "";
		if(connector.length()>=length)
			return "";
		if(str1.length()+str2.length()+connector.length() <=length)
			return str1+connector+str2;
		else
		{
			if(str1.length()<=maxLengthForFirst)
				return (str1+connector+str2).substring(0, length-1)+"..";
			else if(str2.length()<=length-maxLengthForFirst-connector.length())
			{
				int lengthLeft = length-connector.length()-str2.length()-1;
				return str1.substring(0, lengthLeft)+".."+connector+str2;
			}
			else
			{
				return str1.substring(0,maxLengthForFirst-1)+".."+connector+str2.substring(0,length-maxLengthForFirst-connector.length()-1)+"..";
			}
		}
	}
}