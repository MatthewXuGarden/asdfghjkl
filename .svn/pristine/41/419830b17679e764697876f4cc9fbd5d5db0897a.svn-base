package com.carel.supervisor.presentation.AlEvExport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dispatcher.engine.print.DispPDFPrinter;
import com.carel.supervisor.report.PrinterMgr2;
import com.carel.supervisor.report.Report;

public class PDFAlEvExport extends Report implements IAlEvExport  {
	private String ext; 
	private String filename;
	private String title;
	private String filepath;

	private LinkedList<AlEvBean> ll;

	private ArrayList<Object[]> table;

	public static final String I18NTYPE = "i18ntype";
	public static final String I18NTIMESTAMP = "i18ntimestamp";
	public static final String I18NDEVICE = "i18ndevice";
	//public static final String I18NALR = "i18nalr";
	public static final String I18NDESCALEV = "i18ndescalev";
	public static final String I18NENDALARM = "i18nendalarm";
	

//	private static final String TIME = null;

	private static final String I18NTIMELABEL = "i18ntimelabel";
	private static final String I18NUSERNAME = "i18nusername";
	private static final String I18NPRIORITY = "i18npriority";
	
	public PDFAlEvExport(){
		table= new ArrayList<Object[]>();
	}
	
	public void endExport() throws IOException {
		File f=generate();
		setFile(f);
	}

	public String getExtension() {
		return ext;
	}

	public void setExtension(String ext) {
		this.ext=ext;
	}

	public void startExport(String file, String language) throws FileNotFoundException {
		parameters.put(LANGUAGE, language);
		this.filename=file;
	}

	public void writeRow(String[] row) throws IOException {
		String[] s = new String[row.length];
		for (int i = 0; i < s.length; i++) {
			s[i] = new String(row[i]);
		}
		table.add(s);
	}

	




	//	@Override
	public File generate() {
		String language = ""+parameters.get(LANGUAGE);
		LangService lang = LangMgr.getInstance().getLangService(parameters.get(LANGUAGE).toString());
//		parameters.put(TIME, new Timestamp(System.currentTimeMillis()));
		parameters.put(TITLE, lang.getString("report", "alevtitle"));
		ll = new LinkedList<AlEvBean>();
		for(int i=0; i<table.size();i++){
			Object[] o = table.get(i);
			ll.add(new AlEvBean(o[0].toString(),o[1].toString(),o[2].toString(),
					o[3].toString(),o[4].toString(),o[5].toString()));
		}
		
		parameters.put(I18NDESCALEV,title);
		parameters.put(I18NDESCALEV,lang.getString("report", "alevtitle"));
		parameters.put(I18NDATE,lang.getString("report", I18NDATE));
		parameters.put(I18NTIME,lang.getString("report", I18NTIME));
		parameters.put(I18NTYPE,lang.getString("report", I18NTYPE));
		parameters.put(I18NTIMESTAMP,lang.getString("report", I18NTIMESTAMP));
		parameters.put(I18NDEVICE,lang.getString("report", I18NDEVICE));
		parameters.put(I18NPAGE,lang.getString("report", I18NPAGE));
		parameters.put(I18NOF,lang.getString("report", I18NOF));
		String strAppHome = BaseConfig.getAppHome();
		String strLogo = ProductInfoMgr.getInstance().getProductInfo().get("imgtop");
		parameters.put(Report.LOGO, strAppHome + "images"+File.separator+ strLogo);
		try {
			PrinterMgr2 p = PrinterMgr2.getInstance();
			JRBeanCollectionDataSource bcd = new JRBeanCollectionDataSource(ll);
			
			// change filepath for report creation
			if(filepath!=null)
			{
				p.changeSavePath(filepath);
			}
			
			File f = p.export(p.fill("PDF_AlEv", parameters, bcd), "PDF", filename);
			
			// restore custom filepath to "". Default filepath is considered from report generation mechanism
			p.changeSavePath("");

			//2014-2-24, bug id:10579 By Kevin remove auto-print for Alarm Event export 
            //DispPDFPrinter.printAlarmFile(f.getAbsolutePath());

			return f; 
		}catch(Exception e){
			LoggerMgr.getLogger(this.getClass()).error(e);
			return null;
		}
	}

	public String getFileName() {
		return getFile().getAbsolutePath();
	}

	public ArrayList<Object[]> getTable() {
		return table;
	}

	public void setColumnName(String[] colNames) throws IOException {
		parameters.put(I18NTIMELABEL,new String(colNames[0]));
		parameters.put(I18NDESC ,new String(colNames[1]));
		parameters.put(I18NDEVICE,new String(colNames[2]));
		parameters.put(I18NUSERNAME,new String(colNames[3]));
		parameters.put(I18NENDALARM,new String(colNames[4]));
		parameters.put(I18NPRIORITY,new String(colNames[5]));
		
	}

	public void setSiteName(String sitename) {
		parameters.put(SITENAME,  sitename);
	}

	public void setTitle(String title) {
		this.title=title;
		
	}
	
	public void setFilePath(String path)
	{
		filepath = path;
	}
}
