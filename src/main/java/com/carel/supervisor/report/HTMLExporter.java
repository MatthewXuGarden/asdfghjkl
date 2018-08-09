package com.carel.supervisor.report;

import java.io.File;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

public class HTMLExporter implements ReportExporter {

	public HTMLExporter(){
	}

	public File export(JasperPrint print, File path, String filename) throws Exception{
		File f = new File(path.getAbsolutePath()+"/"+filename+".html"); 
		JasperExportManager.exportReportToHtmlFile(print, 
				path.getAbsolutePath()+"/"+filename+".html");
		return f;
	}
}
