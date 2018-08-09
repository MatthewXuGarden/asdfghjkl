package com.carel.supervisor.report;

import java.io.File;
import java.util.LinkedList;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;

public class AliveReport extends Report {
	public AliveReport() {
		super();
	}
	
	public File generate() {
		String language = ""+parameters.get(LANGUAGE);
		parameters.put(I18NDESC,LangMgr.getInstance().getLangService(language).getString("report", I18NDESC));
		parameters.put(I18NDATE,LangMgr.getInstance().getLangService(language).getString("report", I18NDATE));
		parameters.put(I18NTIME,LangMgr.getInstance().getLangService(language).getString("report", I18NTIME));
		String strAppHome = BaseConfig.getAppHome();
		
		String strLogo = ProductInfoMgr.getInstance().getProductInfo().get("alive_rep_logo");
		if(strLogo == null || strLogo.equals(""))
			strLogo = ProductInfoMgr.getInstance().getProductInfo().get("imgtop");
		
		parameters.put(Report.LOGO, strAppHome + "images\\" + strLogo);
		
		try {
			PrinterMgr2 p = PrinterMgr2.getInstance();
			LinkedList<Object> ll = new LinkedList<Object>();
			ll.add(new Object());
			JRBeanCollectionDataSource bcd = new JRBeanCollectionDataSource(ll);
			return p.export(p.fill("PDF_Alive", parameters, bcd), "PDF");
		}catch(Exception e){
			LoggerMgr.getLogger(this.getClass()).error(e);
			return null;
		}
	}
}