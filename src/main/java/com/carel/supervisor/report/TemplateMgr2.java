package com.carel.supervisor.report;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.ResourceLoader;
import com.carel.supervisor.base.log.LoggerMgr;

public class TemplateMgr2 extends InitializableBase {

	private HashMap<String,HashMap<String,JasperReport>> templates; 
	
	public TemplateMgr2(){
		templates = new HashMap<String, HashMap<String, JasperReport>>();
	}
	
	public void init(Properties properties) {
		for(Enumeration<?> en = properties.propertyNames();en.hasMoreElements();){
			String key = (String)en.nextElement();
	        try {
				URL[] urls = ResourceLoader.fromResourcePath(properties.getProperty(key), "jrxml");
				if(urls!=null){
					for(int i = 0; i<urls.length; i++){
						HashMap<String, JasperReport> tmap = templates.get(key);
						if(tmap==null){
							templates.put(key, new HashMap<String, JasperReport>());
							tmap = templates.get(key);
						}
						try {
							if(urls[i]==null)
								continue;
							String filename = new File(urls[i].getFile()).getName();
							if(!filename.startsWith("I_") && 
							   !filename.startsWith("H_") &&
							   !filename.startsWith("PDF_Alarm") &&
							   !filename.startsWith("PDF_AlEv") &&
							   !filename.startsWith("PDF_Alive")){
								continue;
							}
							JasperReport jr = JasperCompileManager.compileReport(urls[i].getFile());
							tmap.put(jr.getName(), jr);
							LoggerMgr.getLogger(this.getClass()).info("Report loaded: "+urls[i]);
							System.out.println(jr.getName());							
						} catch (Exception e) {
							LoggerMgr.getLogger(this.getClass()).error("Report error: "+urls[i]);
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
			}
		}
	}

	public String[] getTemplateList() {
		LinkedList<String> toret = new LinkedList<String>();
		for(Iterator<String> itr = templates.keySet().iterator();itr.hasNext();) {
			HashMap<String, JasperReport> tmap = templates.get(itr.next());
			for(Iterator<String> itr2 = tmap.keySet().iterator();itr2.hasNext();){
				toret.add(itr2.next());
			}
		}
		return toret.toArray(new String[toret.size()]);
	}

	public JasperReport getTemplate(String templateName) {
		for(Iterator<String> itr = templates.keySet().iterator();itr.hasNext();) {
			HashMap<String, JasperReport> tmap = templates.get(itr.next());
			if(tmap.containsKey(templateName)){
				return tmap.get(templateName);
			}
		}
		return null;
	}

	public HashMap<String, JasperReport> getTemplateCategory(String templateCategoryName) {
		return templates.get(templateCategoryName);
	}

}