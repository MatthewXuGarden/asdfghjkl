package com.carel.supervisor.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.util.DecimalFormatter;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.ReportBean;
import com.carel.supervisor.dataaccess.datalog.impl.ReportExportConfigList;
import com.carel.supervisor.dataaccess.event.VariableHelper;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.report.bean.VarValueBean;
import com.carel.supervisor.script.EnumerationMgr;

public class InstantReport extends Report {
	private LinkedList<VarValueBean> ll;

	@Override
	public File generate() {
		LangService lang = LangMgr.getInstance().getLangService(parameters.get(LANGUAGE).toString());
		Variable[] vars = new Variable[0];
		String lang_c = (String)parameters.get(LANGUAGE);
		Integer[] v = ((ReportBean)parameters.get(REPORTDETAIL)).getVariables();
		Integer[] mdl = ((ReportBean)parameters.get(REPORTDETAIL)).getVarsMdl();
		int[] toread = new int[v.length];
		for (int i = 0; i < v.length; i++) {
			toread[i] = v[i];
		}
		try {
			vars = ControllerMgr.getInstance().getFromField(toread);
		} catch (Exception e1) {
			LoggerMgr.getLogger(this.getClass()).error(e1);
		}
		Map<Integer, String[]> map = VariableHelper.getDescriptions(lang_c, (Integer)parameters.get(IDSITE), toread);
		parameters.put(TITLE, "@@title@@");//lang.getString("report", "alarmtit"));
		ll = new LinkedList<VarValueBean>();
		String label= "";
		for(int i=0; i<vars.length;i++){
			label = EnumerationMgr.getInstance().getEnumCode(mdl[i], vars[i].getCurrentValue(), lang_c);
			if (!"".equals(label))
			{
				ll.add(new VarValueBean(map.get(toread[i])[1], map.get(toread[i])[0], label, null));
			}
			else
			{
				ll.add(new VarValueBean(map.get(toread[i])[1], map.get(toread[i])[0], vars[i].getFormattedValue(), null));
			}
		}
		parameters.put(I18NDESC,lang.getString("report", I18NDESC));
		parameters.put(I18NDATE,lang.getString("report", I18NDATE));
		parameters.put(I18NTIME,lang.getString("report", I18NTIME));
		parameters.put(I18NPAGE,lang.getString("report", I18NPAGE));
		parameters.put(I18NOF,lang.getString("report", I18NOF));
		parameters.put(I18NSTAMP, lang.getString("report", I18NSTAMP));
		parameters.put(I18NSIGN1, lang.getString("report", I18NSIGN1));
		parameters.put(I18NSIGN2, lang.getString("report", I18NSIGN2));
		parameters.put(I18NDEVICE, lang.getString("report", I18NDEVICE));
		parameters.put(I18NVAR, lang.getString("report", I18NVAR));
		parameters.put(I18NVALUE, lang.getString("report", I18NVALUE));
		parameters.put(IMAGE, BaseConfig.getAppHome()+"images"+File.separator+"top"+File.separator+"left.png");
			
		if(((ReportBean)parameters.get(Report.REPORTDETAIL)).getOutputtype().equalsIgnoreCase("CSV"))
			return exportCSV();
		else if (((ReportBean)parameters.get(Report.REPORTDETAIL)).getOutputtype().equalsIgnoreCase("HTML"))
			return exportHTML();
		else
			return exportPDF();
		
		
	}
	
	private File exportPDF() 
	{
		try {
			String reportName = HistoryReport.truncate(parameters.get(Report.SITENAME).toString(), " - ",parameters.get(Report.CODE).toString() ,15,40);
			parameters.put(Report.SITENAME, reportName);
			PrinterMgr2 p = PrinterMgr2.getInstance();
			JRBeanCollectionDataSource bcd = new JRBeanCollectionDataSource(ll);
			String islocal=(String)parameters.get(ISLOCAL);
			String filename="";
			String path="";
			if("true".equalsIgnoreCase(islocal)){
				path=(String)parameters.get(ExportPath);
				filename=(String)parameters.get(ExportFileName);
				path=path.substring(0, path.lastIndexOf(filename));
				return p.export(p.fill(((ReportBean)parameters.get(REPORTDETAIL)).getTemplatefile(), parameters, bcd), "PDF", filename,path);
			}else{
				if(this.isToTempFolder())
				{
					path = BaseConfig.getCarelPath()+BaseConfig.getTemporaryFolder();
					return p.export(p.fill(((ReportBean)parameters.get(REPORTDETAIL)).getTemplatefile(), parameters, bcd), "PDF", "REPORT_"+String.valueOf(System.currentTimeMillis()),path);
				}
				else
				{
					return p.export(p.fill(((ReportBean)parameters.get(REPORTDETAIL)).getTemplatefile(), parameters, bcd), "PDF", "REPORT_"+String.valueOf(System.currentTimeMillis()));
				}
			}
			
		}catch(Exception e){
			LoggerMgr.getLogger(this.getClass()).error(e);
			return null;
		}
	}

	private File exportCSV() 
	{
		String separator = ";";
		ReportExportConfigList conf = new ReportExportConfigList();
		Map<String,String> map = conf.loadMap();
		separator = map.get(ReportExportConfigList.SEPARATOR);
		separator = separator == null || separator.length()==0?";":separator;
		
		ReportBean repdetail = (ReportBean) parameters.get(REPORTDETAIL); 
		Integer[] v = repdetail.getVariables();
		
		//LangService lang = LangMgr.getInstance().getLangService(parameters.get(LANGUAGE).toString());
		Variable[] vars = new Variable[0];
		String label = "";
		int[] toread = new int[v.length];
		for (int i = 0; i < v.length; i++)
		{
			toread[i] = v[i];
		}
		try 
		{
			vars = ControllerMgr.getInstance().getFromField(toread);
		} catch (Exception e1)
		{
			LoggerMgr.getLogger(this.getClass()).error(e1);
		}
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
		try
		{
			File file = new File(path.getAbsolutePath()+File.separator+filename+".csv");
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
			
			fw.write(parameters.get(Report.CODE).toString()+separator+"\n");
			fw.write(DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm")+separator+"\n");
	    	fw.write(parameters.get(Report.SITENAME).toString()+separator+"\n");
	    	fw.write((parameters.get(Report.USER)!=null?parameters.get(Report.USER).toString():"System")+separator+"\n");
	    	
			Map<Integer,String[]> descriptions = (Map<Integer,String[]>)parameters.get(Report.DESCRIPTIONS);
	    	
	    	fw.write("\n");
	    	for(int i=0;i<v.length;i++)
	    	{
	    		fw.write(descriptions.get(v[i])[1]+separator);//device name
	    	}
	    	fw.write("\n");
	    	for(int i=0;i<v.length;i++)
	    	{
	    		fw.write(descriptions.get(v[i])[0]+separator); //variable name
	    	}
	    	fw.write("\n");
	    	DecimalFormat formatter = DecimalFormatter.getCSVFormatter();
	    	DecimalFormat nodecimal = DecimalFormatter.getCSVFormatterNoDecimal();
	    	for(int i=0;i<v.length;i++)
	    	{
	    		label = EnumerationMgr.getInstance().getEnumCode(vars[i].getInfo().getModel(), vars[i].getCurrentValue(),(String)parameters.get(LANGUAGE));
	    		if (!"".equals(label))
	    			fw.write(label+separator);//variable values
	    		else
	    			fw.write(DecimalFormatter.getValue(formatter,nodecimal, String.valueOf(vars[i].getFormattedValue()))+separator);//variable values
	    	}
	    	fw.write("\" \""+separator);
	    	fw.write("\n");
	    	
	    	fw.flush();
	    	fw.close();
	    	return file;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	private File exportHTML() 
	{
		ReportBean repdetail = (ReportBean) parameters.get(REPORTDETAIL); 
		Integer[] v = repdetail.getVariables();
		
		LangService lang = LangMgr.getInstance().getLangService(parameters.get(LANGUAGE).toString());
		Variable[] vars = new Variable[0];
		int[] toread = new int[v.length];
		for (int i = 0; i < v.length; i++)
		{
			toread[i] = v[i];
		}
		try 
		{
			vars = ControllerMgr.getInstance().getFromField(toread);
		} catch (Exception e1)
		{
			LoggerMgr.getLogger(this.getClass()).error(e1);
		}
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
		try
		{
			File file = new File(path.getAbsolutePath()+File.separator+filename+".html");
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
			
			fw.write("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body>\n");
			
			//style
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
			
			//end style
			
			fw.write("<p class='tdTitleTable'>"+(parameters.get(Report.USER)!=null?parameters.get(Report.USER).toString():"System"));
			fw.write("&nbsp;&nbsp;&nbsp;<b>"+parameters.get(Report.SITENAME).toString()+"</b>");
			fw.write("&nbsp;&nbsp;&nbsp;"+DateUtils.date2String(new Date(System.currentTimeMillis()), "yyyy/MM/dd hh:mm") +"</p>");
	    	
	    	
			Map<Integer,String[]> descriptions = (Map<Integer,String[]>)parameters.get(Report.DESCRIPTIONS);
	    	
			fw.write("<table class='table' border='0' cellpadding='4' cellspacing='1'>\n");
			//header
			
			
			fw.write("<tr class='th'><td>"+lang.getString("report", I18NDEVICE)+"</td><td>"+lang.getString("report", I18NVAR)+"</td><td>"+lang.getString("report", I18NVALUE)+"</td></tr>");
			String label = "";
			Float curr_v = null;
	    	for(int i=0;i<v.length;i++)
	    	{
	    		curr_v = vars[i].getCurrentValue();
	    		label = EnumerationMgr.getInstance().getEnumCode(vars[i].getInfo().getModel(), vars[i].getCurrentValue(),(String)parameters.get(LANGUAGE));
	    		if (!"".equals(label))
	    			fw.write("<tr class='Row1'><td nowrap>"+descriptions.get(v[i])[1]+"</td><td nowrap>"+descriptions.get(v[i])[0]+"</td><td nowrap>"+label+"</td></tr>");//device name
	    		else
	    			fw.write("<tr class='Row1'><td nowrap>"+descriptions.get(v[i])[1]+"</td><td nowrap>"+descriptions.get(v[i])[0]+"</td><td nowrap>"+curr_v+"</td></tr>");//device name
	    	}
	    	fw.write("</tr>\n</table></body></html>");
	    	fw.flush();
	    	fw.close();
	    	return file;
		}
		catch (Exception e) {
			return null;
		}
	}
	
}
