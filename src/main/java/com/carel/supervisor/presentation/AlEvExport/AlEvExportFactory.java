package com.carel.supervisor.presentation.AlEvExport;

public class AlEvExportFactory {
	public static IAlEvExport getExporter(String extension){
		
		IAlEvExport r;		
		if (extension.equalsIgnoreCase("pdf"))
			r = new PDFAlEvExport();
		else
			r=new CSVAlEvExport();
		
		
		r.setExtension(extension.toLowerCase());
		return r;
	}
}
