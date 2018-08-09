package com.carel.supervisor.presentation.bo;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.io.FileSystemUtils;
import com.carel.supervisor.base.io.Zipper;
import com.carel.supervisor.dataaccess.datalog.impl.HsPrintBean;
import com.carel.supervisor.dataaccess.datalog.impl.HsReportBean;
import com.carel.supervisor.dataaccess.datalog.impl.ReportBeanList;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.presentation.bean.ReportBeanListPres;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.refresh.IRefresh;
import com.carel.supervisor.presentation.refresh.RefreshBean;
import com.carel.supervisor.presentation.refresh.RefreshBeanList;
import com.carel.supervisor.presentation.session.UserSession;

public class BHsprint extends BoMaster
{
	private static final int REFRESH_TIME = -1;
	private final static String ZIP_NAME = "AlarmReports_";
	
	public BHsprint(String lang) {
		super(lang,REFRESH_TIME);
	}
	
	protected Map initializeRefresh()
    {
        Map map = new HashMap();
        
        RefreshBean[] rb1 = {new RefreshBean("TA", IRefresh.R_PRINT, 0)};
        RefreshBeanList rbl1 = new RefreshBeanList(REFRESH_TIME, 1);
        rbl1.setRefreshObj(rb1);
        map.put("tab1name", rbl1);;

        return map;
    }
	protected Properties initializeRefreshTime()
    {
        Properties p = new Properties();
        return p;
    }
	
	protected Properties initializeEventOnLoad() {
		Properties p = new Properties();
		p.put("tab1name","initHsPrint();resizeTableArchAlrReport();");
        return p;
	}

	protected Properties initializeJsOnLoad()
    {
		String virtkey = VirtualKeyboard.getInstance().isOnScreenKey() ? ";keyboard.js;" : "";
        
		Properties p = new Properties();
        p.put("tab1name", "reportconf.js;resizeTable.js;../arch/FileDialog.js" + virtkey);
        return p;
    }
	
	// DOCTYPE STRICT is necessary to have FileDialog correctly functioning
	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab1name", DOCTYPE_STRICT);
		return p;
    }
	
//	public String createPrintTable(int idsite,String language,String title,int width,int height,int screenh,int screenw)
//	{
//		ReportBeanListPres reportBean = new ReportBeanListPres();
//		reportBean.setScreenH(screenh);
//		reportBean.setScreenW(screenw);
//		String ret = reportBean.getHTMLHSPrintTable(idsite,language,title,width,height);
//		return ret;
//	}
	
	public String getRepositoryPath() {
		return DispatcherMgr.getInstance().getRepositoryPath();
	}
	
	public String executeDataAction(UserSession us, String tabName,
            Properties prop) throws Exception
    {
		StringBuffer xmlResp = new StringBuffer();
		
		if(tabName != null && tabName.equalsIgnoreCase("tab1name"))
		{
			String cmd = prop.getProperty("cmd");
			
			if(cmd != null && cmd.equalsIgnoreCase("DEL"))
			{
				String ids = prop.getProperty("id");
			 	if(ids != null)
			 	{
			 		String[] idStr = ids.split("\\|");
	            	int[] id = new int[idStr.length];
	            	for(int i=0;i<id.length;i++)
	            	{
	            		id[i] = Integer.valueOf(idStr[i]);
	            	}
	            	HsPrintBean[] bean = ReportBeanList.retrieveHsPrintByIds(us.getIdSite(),id);
					boolean noerror = ReportBeanListPres.deletePrintHistory(ids);
					if(noerror)
	            	{
						deleteFile(bean);
	            	}
			 	}
			 	int currentpage = Integer.parseInt(prop.getProperty("currentpage"));
			 	xmlResp.append(createXmlTable(us.getIdSite(),currentpage));
			}
			else if("exportpdf".equalsIgnoreCase(cmd))
			{
				String newpath = prop.getProperty("newpath");
				String ids=prop.getProperty("ids");
				String path = getRepositoryPath();
				String local = prop.getProperty("local");
	            String basename = "";
	            if("true".equalsIgnoreCase(local))
	            {
	            	basename = newpath;
	            }
	            else
	            {
	            	basename = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator  + 
	            		ZIP_NAME+DateUtils.date2String(new Date(), "yyyyMMddhhmmss");
	            }
	            if(ids != null && ids != "")
	            {
	            	String[] idStr = ids.split("\\|");
	            	int[] id = new int[idStr.length];
	            	for(int i=0;i<id.length;i++)
	            	{
	            		id[i] = Integer.valueOf(idStr[i]);
	            	}
	            	HsPrintBean[] bean = ReportBeanList.retrieveHsPrintByIds(us.getIdSite(),id);
	            	String result = compressFile(bean,path,basename);
	            	if(result != null)
	            		return result;
	            }
	            return "<response><file><![CDATA[ERROR]]></file></response>";
			}
			else if("delall".equals(cmd))
			{
				HsPrintBean[] bean = ReportBeanListPres.loadPrintHistory(us.getIdSite());
				boolean noerror = ReportBeanList.deletePrintHistory(us.getIdSite());
				if(noerror)
					deleteFile(bean);
			}
			else if("exportall".equals(cmd))
			{
				String newpath = prop.getProperty("newpath");
				String path = getRepositoryPath();
				String local = prop.getProperty("local");
	            String basename = "";
	            if("true".equalsIgnoreCase(local))
	            {
	            	basename = newpath;
	            }
	            else
	            {
	            	basename = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator  +
	            			ZIP_NAME+DateUtils.date2String(new Date(), "yyyyMMddhhmmss");
	            }
            	HsPrintBean[] bean = ReportBeanListPres.loadPrintHistory(us.getIdSite());
				String result = compressFile(bean,path,basename);
            	if(result != null)
            		return result;
	            return "<response><file><![CDATA[ERROR]]></file></response>";
			}
		}
		return xmlResp.toString();
    }
	private void deleteFile(HsPrintBean[] bean)
	{
		if(bean != null && bean.length>0)
		{
			String path = getRepositoryPath();
			for(int i=0;i<bean.length;i++)
			{
				String filePath = path+bean[i].getPath();
				File f = new File(filePath);
				if(f.exists())
					f.delete();
			}
		}
	}
	private String compressFile(HsPrintBean[] bean,String path,String basename) throws Exception
	{
		if(bean != null && bean.length>0)
    	{
    		String[] filePath = new String[bean.length];
    		for(int i=0;i<bean.length;i++)
    		{
    			filePath[i] = path+bean[i].getPath();
    		}
    		Zipper.zipFiles(false,basename, filePath);
    		return "<response><file><![CDATA[" + basename+".zip"+ "]]></file></response>";
    	}
		return null;
	}
	private String createXmlTable(int idSite, int numPage)
	{
		StringBuffer ret = new StringBuffer("<response>");
		HsPrintBean[] rows = ReportBeanList.loadPrintHistory(idSite, numPage);
		HsPrintBean tmp = null;
		if(rows != null)
		{
			for(int i=0; i<rows.length; i++)
			{
				tmp = rows[i];
				if(tmp != null)
				{
					ret.append("<row>");

					ret.append("<id><![CDATA["+tmp.getId()+"]]></id>");
					ret.append("<creation><![CDATA["+DateUtils.date2String(tmp.getData(),"yyyy/MM/dd HH:mm:ss")+"]]></creation>");
					ret.append("<action><![CDATA["+tmp.getActioncode()+"]]></action>");
					ret.append("<path><![CDATA["+tmp.getPath()+"]]></path>");
					
					ret.append("</row>");
				}
			}
		}
		
		ret.append("</response>");
		return ret.toString();
	}
}
