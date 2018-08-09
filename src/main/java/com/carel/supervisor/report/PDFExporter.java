package com.carel.supervisor.report;

import java.io.File;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

public class PDFExporter implements ReportExporter {

	public PDFExporter(){
	}

	public File export(JasperPrint print, File path, String filename) throws Exception{
		File f = new File(path.getAbsolutePath()+"/"+filename+".pdf"); 
		JasperExportManager.exportReportToPdfFile(print, 
				path.getAbsolutePath()+"/"+filename+".pdf");
		return f;
	}
}
