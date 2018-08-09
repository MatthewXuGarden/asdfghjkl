package com.carel.supervisor.presentation.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.io.Zipper;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.util.DecimalFormatter;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.datalog.impl.ReportExportConfigList;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.BookletDevVarBean;
import com.carel.supervisor.presentation.bean.BookletListBean;
import com.carel.supervisor.presentation.bean.BookletSiteInfoBean;

public class ReportBeanHelper 
{
//	private static final long DAY = (60L*60L*24L*1000L);
	/*
	public static String buildReport(int idSite,String lang,String user,int dd,int mm,int yy,
			   					   long step,int[] vars,boolean haccp,int dd1,int mm1,int yy1,boolean prompt, String templateName)
		throws Exception
	{
		//dd1--;
		
		GregorianCalendar endData  = new GregorianCalendar(yy,(mm-1),dd,0,0,0);
		GregorianCalendar staData  = new GregorianCalendar(yy1,(mm1-1),dd1,0,0,0);
		long diffDays = 0;
        
		if(endData != null && staData != null)
			diffDays = endData.getTimeInMillis() - staData.getTimeInMillis();
		
		//diffDays = (diffDays/DAY);
        diffDays = (diffDays/DAY) + 1; //al posto di "dd1--;"
        
        //gestione presunto bug classe GregorianCalendar se var staData = 31/08/xxxx.
        //if ( (dd1 == 31) && (mm1 == 8) ) diffDays++;
        
        if (diffDays == 7L)
            //gestione report generico come report settimanale:
            return ReportBeanHelper.buildReport(idSite,lang,user,dd1,mm1,yy1,diffDays,step,vars,haccp,prompt, templateName);
        else
            return ReportBeanHelper.buildReport(idSite,lang,user,dd,mm,yy,diffDays,step,vars,haccp,prompt, templateName);
	}
	
	public static String buildReport(int idSite,String lang,String user,int dd,int mm,int yy,
								   long days,long step,int[] vars,boolean haccp,boolean prompt, String templateName)
		throws Exception
	{
		String fileName = "";
		
		// In GregorianCalendar 0 = Gennaio
		// Sistemato per stampare il giorno corrente
		mm--;
		dd++;

        GregorianCalendar theData  = new GregorianCalendar(yy,mm,dd,0,0,0);
		String siteName = "";
		long lStartData = 0;
		long lEndData = 0;
		long totDays = days*DAY;
		
		// Tech for Instantly
		if(totDays == 0)
			totDays = 3600000L;
		
		long lStep = (step*60L);
		lStep = lStep*1000L;
		
		try {
			SiteInfo site = SiteInfoList.retrieveSiteById(idSite);
			if(site != null)
				siteName = site.getName();
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(ReportBeanHelper.class);
			logger.error(e);
		}

        //se data valida:
        if(theData != null)
		{
			
            if (days == 7L)
            {//in abbinamento a modifica su ReportLoader x gestione data con report settimanale
                lStartData = theData.getTimeInMillis() - 24L*60L*60L*1000L;
                lEndData = lStartData + totDays;
            }
            else
            {
                lEndData = theData.getTimeInMillis();
                lStartData = lEndData - totDays;
            }
            
			DailyReport dReport = new DailyReport(idSite,siteName,user,lStartData,lEndData,lang,vars,lStep,haccp);
			fileName = dReport.generateDocument(templateName);
			
			if(!prompt)
			{
				if(!fileName.equalsIgnoreCase(""))
				{
					try {
						ExternalPrint ePrint = new ExternalPrint(fileName);
						ePrint.send();
						fileName = "";
					}
					catch(Exception e){
						Logger logger = LoggerMgr.getLogger(ReportBeanHelper.class);
						logger.error(e);
					}
				}
			}
		}
		
		return fileName;
	}
	
	public static String buildReport(int idSite,String lang,String user,int[] vars,boolean prompt, String templateName)
		throws Exception
	{
		String siteName = "";
		String fileName = "";
		long lToday = System.currentTimeMillis();
		
		try {
			SiteInfo site = SiteInfoList.retrieveSiteById(idSite);
			if(site != null)
				siteName = site.getName();
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(ReportBeanHelper.class);
			logger.error(e);
		}
		
		IstantReport dReport = new IstantReport(idSite,siteName,user,lToday,lToday,lang,vars,0L,false);
		fileName = dReport.generateDocument(templateName); 
		
		if(!prompt)
		{
			if(!fileName.equalsIgnoreCase(""))
			{
				try {
					ExternalPrint ePrint = new ExternalPrint(fileName);
					ePrint.send();
					fileName = "";
				}
				catch(Exception e){
					Logger logger = LoggerMgr.getLogger(ReportBeanHelper.class);
					logger.error(e);
				}
			}
		}
		
		return fileName;
	}
	*/
	
	@SuppressWarnings({ "unused", "unchecked" })
	public static String buildBookletReport(int idSite,String language,String user,String prompt,String isopen, String reportType, String fullpath)throws Exception{
		String siteName = "";
		String fileName = "";
		String separator = ";";
		ReportExportConfigList conf = new ReportExportConfigList();
		Map<String,String> map = conf.loadMap();
		separator = map.get(ReportExportConfigList.SEPARATOR);
		separator = separator == null || separator.length()==0?";":separator;
		String filePath = "";
//		long lToday = System.currentTimeMillis();
		
		try {
			LangService lang = LangMgr.getInstance().getLangService(language);
			SiteInfo site = SiteInfoList.retrieveSiteById(idSite);
			if(site != null)
				siteName = site.getName();
			BookletListBean cfg=new BookletListBean(idSite,user);
			cfg.searchBookletConfBySiteId();
			BookletListBean bdv;
			List<BookletListBean> ll=new ArrayList<BookletListBean>();
			
			//site information
			if(cfg.getBcf().isSiteinfo()==true){
				bdv=new BookletListBean(idSite,user);
				bdv.setLanguage(language);
				bdv.searchBookletSiteInfo(siteName,user,language);
				bdv.setBeanName(lang.getString("booklet","siteinfo"));
				ll.add(bdv);
			}
			//device variables
			String[][] files = null;
			if(cfg.getBcf().isDevparam()==true){
				bdv=new BookletListBean(idSite,user);
				bdv.setLanguage(language);
				if (reportType.equalsIgnoreCase("standard"))
				{
					bdv.searchBookletDevVarBySiteId();
					files = bdv.searchFiles();
				}
				else if (reportType.equalsIgnoreCase("allrw"))
				{
					bdv.searchBookletDevVarOfAllSiteId();
					files = bdv.searchFilesOfAll();
				}
				bdv.setBeanName(lang.getString("booklet","devparam"));
				if(files == null || files.length == 0)
				{
					bdv.setField1(lang.getString("booklet","devices"));
					bdv.setField2(lang.getString("booklet","variables"));
					bdv.setField3(lang.getString("booklet","varscode"));
					bdv.setField4(lang.getString("booklet","value"));
				}
				else
				{
					bdv.setField2(lang.getString("booklet","devices"));
					bdv.setField3(lang.getString("booklet","variables"));
					bdv.setField4(lang.getString("booklet","varscode"));
					bdv.setField5(lang.getString("booklet","value"));
					bdv.setField1(lang.getString("booklet","cabinet"));
				}
				ll.add(bdv);
			}
			
			fileName="Commissioning_"+DateUtils.date2String(new Date(), "yyyyMMddhhmmss");
			
			if(fullpath!=null && !fullpath.equals(""))
			{
				filePath = fullpath;
			}
			else
				filePath = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder();
			
			String postfix = "";
			if(prompt!=null && prompt.equalsIgnoreCase("csv")){
	        		int[] vars=new int[3];
	        		vars[0]=45;
	        		vars[1]=46;
	        		vars[2]=47;
	        		postfix=".csv";
	        		String newLine="\n";
	        		if("1".equals(isopen)){
	        			postfix=".htm";
	        			newLine="<br>";
	        		}

	        		if(fullpath!=null && !fullpath.equals(""))
	        			filePath += postfix;
	        		else
	        			filePath += File.separator+fileName+postfix;

	        		
	        		BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
	        		DecimalFormat formatter = DecimalFormatter.getCSVFormatter();
	        		DecimalFormat nodecimal = DecimalFormatter.getCSVFormatterNoDecimal();
	        		for(int i=0;i<ll.size();i++){
	    				BookletListBean temp=ll.get(i);
	    				//site info
	    				if(temp.getSiteinfoList()!=null){
	    					fw.write(newLine+"["+temp.getBeanName()+"]"+newLine);
		    				Iterator it=temp.getSiteinfoList().getData().iterator();
	    					while(it.hasNext()){
	    						BookletSiteInfoBean siteinfo=(BookletSiteInfoBean)it.next();
	    						fw.write(siteinfo.getSiteName()+separator+siteinfo.getSiteValue()+newLine);
	    					}
	    				}
	    				//device param
	    				if(temp.getDevparamList()!=null){
	    					fw.write(newLine+"["+temp.getBeanName()+"]"+newLine);
		    				Iterator it=temp.getDevparamList().getData().iterator();
	    					while(it.hasNext()){
	    						BookletDevVarBean devvar=(BookletDevVarBean)it.next();
	    						if(files == null || files.length == 0)
	    							fw.write(devvar.getDevDesc()+separator+devvar.getVarDesc()+separator+devvar.getVarCode()+separator+DecimalFormatter.getValue(formatter,nodecimal, devvar.getVarValue())+newLine);
	    						else
	    							fw.write(devvar.getCabinet()+separator+devvar.getDevDesc()+separator+devvar.getVarDesc()+separator+devvar.getVarCode()+separator+DecimalFormatter.getValue(formatter,nodecimal, devvar.getVarValue())+newLine);
	    					}
	    				}
	    				//here add other module code. simon note 2009-04-10
	    			}
	    			fw.flush();
	    			fw.close();
	    			
			}else if(prompt!=null && prompt.equalsIgnoreCase("pdf")){
				JasperReport report;
				try {

					postfix=".pdf";
					// NOTE: font path for pdf report is specified INSIDE JRXML template files!! Pay attention when template has to be modified.
					if(files == null || files.length == 0)
						report = JasperCompileManager.compileReport(getAppPath(ReportBeanHelper.class)+"/template/dispatcher/booklet_report.jrxml");
					else
						report = JasperCompileManager.compileReport(getAppPath(ReportBeanHelper.class)+"/template/dispatcher/booklet_reportNew.jrxml");
					
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					//parameters.put("report.title",lang.getString("booklet","report"));
					parameters.put("report.user",user);
					DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        String datetime = format1.format(new Date());				
					parameters.put("report.datetime",datetime);
					parameters.put("report.title",lang.getString("booklet","report")+"_"+datetime);
					parameters.put("report.image.logo", getAppPath(ReportBeanHelper.class)+"/../../images/top/left.png");
					parameters.put("SUBREPORT_DIR", getAppPath(ReportBeanHelper.class)+"/template/dispatcher/");
					
//					parameters.put("MyDatasource", new JRBeanCollectionDataSource(ll));//
					JasperPrint print= JasperFillManager.fillReport(report, parameters,new JRBeanCollectionDataSource(ll));
					DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			        String datetimename = format2.format(new Date());	
					
					if(fullpath!=null && !fullpath.equals(""))
						filePath += postfix;
					else
						filePath += File.separator+fileName+postfix;
					
					JasperExportManager.exportReportToPdfFile(print, filePath);
				} catch (Exception e) {
					Logger logger = LoggerMgr.getLogger(ReportBeanHelper.class);
					logger.error(e);
				}
			}
			if(files != null && files.length>0)
			{
				String[] fileExport = {filePath};
				filePath = filePath.replace(postfix, "");
				Zipper.zipFiles(false, filePath, fileExport,files);
				File file = new File(fileExport[0]);
				if(file.exists())
					file.delete();
				filePath = filePath + ".zip";
			}
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(ReportBeanHelper.class);
			logger.error(e);
			return "ERROR";
		}
		return filePath;
	}
	public static String getAppPath(Class<ReportBeanHelper> cls){    
	    if(cls==null)    
	     throw new java.lang.IllegalArgumentException("");    
	     ClassLoader loader=cls.getClassLoader();    
	     String clsName=cls.getName()+".class";    
	     Package pack=cls.getPackage();    
	     String path="";    
	    if(pack!=null){    
	         String packName=pack.getName();  
	       if(packName.startsWith("java.")||packName.startsWith("javax."))    
	          throw new java.lang.IllegalArgumentException("system class.");    
	         clsName=clsName.substring(packName.length()+1);    
	        if(packName.indexOf(".")<0) path=packName+"/";    
	        else{
	            int start=0,end=0;    
	             end=packName.indexOf(".");    
	            while(end!=-1){    
	                 path=path+packName.substring(start,end)+"/";    
	                 start=end+1;    
	                 end=packName.indexOf(".",start);    
	             }    
	             path=path+packName.substring(start)+"/";    
	         }    
	     }    
	     java.net.URL url =loader.getResource(path+clsName);    
	     String realPath=url.getPath();    
	    int pos=realPath.indexOf("file:");    
	    if(pos>-1) realPath=realPath.substring(pos+5);    
	     pos=realPath.indexOf(path+clsName);    
	     realPath=realPath.substring(0,pos-1);    
	    if(realPath.endsWith("!"))    
	         realPath=realPath.substring(0,realPath.lastIndexOf("/"));    
	  try{    
	     realPath=java.net.URLDecoder.decode(realPath,"utf-8");    
	    }catch(Exception e){throw new RuntimeException(e);}    
	   return realPath;    
	}
}
