package com.carel.supervisor.report;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;

public class PrinterMgr2 extends InitializableBase {
	private static final String VALUE = "value";
	private static final String TYPE = "type";
	private static PrinterMgr2 mePrinterMgr = new PrinterMgr2();
	private TemplateMgr2 templateMgr = new TemplateMgr2();
	private boolean initialized = false;
	private XMLNode mynode;
	private Properties properties = new Properties();
	private String custompath = "";

	private PrinterMgr2() {
	}

	public static PrinterMgr2 getInstance() {
		return mePrinterMgr;
	}

	public synchronized void init(XMLNode node) throws InvalidConfigurationException {
		if (!initialized) {
			XMLNode component = node.getNode("component");
			properties = retrieveProperties(component, TYPE, VALUE, "BSSE0002");
			templateMgr.init(properties);
			initialized = true;
			mynode = node;
		}
	}

	public JasperPrint fill(String templateName, Map<String, Object> parameters, JRBeanCollectionDataSource datasource) {
		JasperReport report = templateMgr.getTemplate(templateName);
		try {
			return JasperFillManager.fillReport(report, parameters, datasource);
		} catch (JRException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
		}
		return null;
	}

	public File export(JasperPrint print, String type){
		List<JasperPrint> prints = new LinkedList<JasperPrint>();
		prints.add(print);
		return export(prints, type, print.getName());
	}
	
	public File export(JasperPrint print, String type, String filename){
		List<JasperPrint> prints = new LinkedList<JasperPrint>();
		prints.add(print);
		return export(prints, type, filename);
	}
	public File export(JasperPrint print, String type, String filename,String path){
		List<JasperPrint> prints = new LinkedList<JasperPrint>();
		prints.add(print);
		return export(prints, type, filename,path);
	}	
	@SuppressWarnings("unchecked")
	public File export(List<JasperPrint> prints, String type, String filename){
		String path=custompath.equals("")?BaseConfig.getCarelPath()+File.separator+properties.getProperty("savePath"):custompath;
		return export(prints, type, filename,path);
	}
	@SuppressWarnings("unchecked")
	public File export(List<JasperPrint> prints, String type, String filename,String lpath){
		if(prints.isEmpty()){
			return null;
		}
		try {
			Iterator<JasperPrint> itr = prints.iterator();
			JasperPrint jpmaster = itr.next();
			JasperPrint jpson;
			for (;itr.hasNext();){
				jpson = itr.next();
				List<JRPrintPage> l = (List<JRPrintPage>)jpson.getPages();
				for (JRPrintPage printPage : l) {
					jpmaster.addPage(printPage);
				}
			}
			
			File path = new File(lpath);
			
			path.mkdirs();
			ReportExporter exporter = (ReportExporter)FactoryObject.newInstance("com.carel.supervisor.report."+type+"Exporter", null, null);
			return exporter.export(jpmaster, path, filename);
		} catch (JRException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			e.printStackTrace();
			return null;
		}
//		return new File(path.getAbsolutePath()+"/"+fil);
	}
	public void reload() {
		initialized = false;
		try {
			init(mynode);
		} catch (InvalidConfigurationException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	public String[] getTemplates(){
		return templateMgr.getTemplateList();
	}
	
	public TemplateMgr2 getTemplateMgr(){
		return templateMgr;
	}
	
    public String fillData(String templateFileName, Map<Object, Object> data, String pathPDFFile){
    	return null;
    }
    
    public String getSavePath(){
    	if(!custompath.equals(""))
    		return custompath;
    	else
    		return properties.getProperty("savePath");
    }
    
    public void changeSavePath(String path){
    	custompath = path;
    }
}
