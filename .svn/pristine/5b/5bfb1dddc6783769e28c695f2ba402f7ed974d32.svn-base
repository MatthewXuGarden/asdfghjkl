package com.carel.supervisor.presentation.bo;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.io.Zipper;
import com.carel.supervisor.dataaccess.datalog.impl.HsReportBean;
import com.carel.supervisor.dataaccess.datalog.impl.ReportBeanList;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.presentation.bean.FileDialogBean;
import com.carel.supervisor.presentation.bean.ReportBeanListPres;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.refresh.IRefresh;
import com.carel.supervisor.presentation.refresh.RefreshBean;
import com.carel.supervisor.presentation.refresh.RefreshBeanList;
import com.carel.supervisor.presentation.session.UserSession;

public class BHsreport extends BoMaster
{
	private static final long serialVersionUID = -6702397128237782082L;

	private static final int REFRESH_TIME = -1;
	private final static String ZIP_NAME = "LogReports_";
	
	public BHsreport(String lang) {
		super(lang,REFRESH_TIME);
	}
	
	protected Properties initializeRefreshTime()
    {
        Properties p = new Properties();
        return p;
    }
	
	protected Map initializeRefresh()
    {
        Map map = new HashMap();
        
        RefreshBean[] rb1 = {new RefreshBean("TA", IRefresh.R_REPORT, 0)};
        RefreshBeanList rbl1 = new RefreshBeanList(REFRESH_TIME, 1);
        rbl1.setRefreshObj(rb1);
        map.put("tab1name", rbl1);;

        return map;
    }
	protected Properties initializeEventOnLoad() {
		Properties p = new Properties();
		p.put("tab1name","initHsReport();");

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
//	public String createHistoryTable(int idsite,String language,String title,int width,int height,int screenh,int screenw)
//	{
////		ReportBeanListPres reportBean = new ReportBeanListPres();
//		ReportBeanListPres.setScreenH(screenh);
//		ReportBeanListPres.setScreenW(screenw);
//		String ret = ReportBeanListPres.getHTMLHSReportTable(idsite,language,title,width,height);
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
			String fil = prop.getProperty("fil");
			// added to manage file dialog functioning
			String cmd = prop.getProperty("cmd");
			//add by Kevin, if cmd == null, only set filter
			if(cmd == null)
			{
				us.getCurrentUserTransaction().setProperty("filter", fil);
			}
			if( cmd != null && cmd.equalsIgnoreCase(FileDialogBean.CMD) ) {
				String path = prop.getProperty("path");
				String filter = prop.getProperty("filter");
				return (new FileDialogBean(us.getLanguage())).cmdResponse(path, filter);
			}else if(cmd != null && cmd.equalsIgnoreCase("DEL")){
				String ids = prop.getProperty("id");
			 	if(ids != null)
			 	{
			 		String[] idStr = ids.split("\\|");
	            	int[] id = new int[idStr.length];
	            	for(int i=0;i<id.length;i++)
	            	{
	            		id[i] = Integer.valueOf(idStr[i]);
	            	}
	            	HsReportBean[] bean = ReportBeanList.retrieveHsReportByIds(us.getIdSite(),id);
					boolean noerror = ReportBeanListPres.deleteReportHistory(ids);
					if(noerror)
					{
						deleteFile(bean);
					}
			 	}
			 	int currentpage = Integer.parseInt(prop.getProperty("currentpage"));
				xmlResp.append(createXmlTable(us.getIdSite(),us.getLanguage(),fil,currentpage));
			}else if(cmd != null && cmd.equalsIgnoreCase("savefile")){
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
	            	HsReportBean[] bean = ReportBeanList.retrieveHsReportByIds(us.getIdSite(),id);
	            	String result = compressFile(bean,path,basename);
	            	if(result != null)
	            		return result;
	            }
	            return "<response><file><![CDATA[ERROR]]></file></response>";
			}
			else if("delall".equals(cmd))
			{
				HsReportBean[] bean = ReportBeanListPres.loadReportHistory(us.getIdSite(),fil);
				boolean noerror = ReportBeanList.deleteReportHistory(us.getIdSite(),fil);
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
	            	basename = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator  
	            		+ ZIP_NAME+DateUtils.date2String(new Date(), "yyyyMMddhhmmss");
	            }
            	HsReportBean[] bean = ReportBeanListPres.loadReportHistory(us.getIdSite(),fil);
				String result = compressFile(bean,path,basename);
            	if(result != null)
            		return result;
	            return "<response><file><![CDATA[ERROR]]></file></response>";
			}
		}
		return xmlResp.toString();
    }
	private void deleteFile(HsReportBean[] bean)
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
	private String compressFile(HsReportBean[] bean,String path,String basename) throws Exception
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
	private String createXmlTable(int idSite, String language, String filter, int currentpage)
	{
//		LangService lang = LangMgr.getInstance().getLangService(language);
//		String s_now = lang.getString("reportconf", "now");
//		String s_daily = lang.getString("reportconf", "daily");
//		String s_weekly = lang.getString("reportconf", "weekly");
        
		StringBuffer ret = new StringBuffer("<response>");
//		ReportBeanListPres reportBean = new ReportBeanListPres();
		HsReportBean[] rows = ReportBeanListPres.loadReportHistory(idSite,filter,currentpage);
		HsReportBean tmp = null;
		if(rows != null)
		{
			for(int i=0; i<rows.length; i++)
			{
				tmp = rows[i];
				if(tmp != null)
				{
					ret.append("<row>");
					ret.append("<id><![CDATA["+tmp.getIdhs()+"]]></id>");
					ret.append("<code><![CDATA["+tmp.getCode()+"]]></code>");
					
					if(tmp.getStep()==0) {
						ret.append("<from><![CDATA["+DateUtils.date2String(tmp.getCreation(),"yyyy/MM/dd HH:mm")+"]]></from>");
						ret.append("<to><![CDATA[]]></to>");
					} else {
//						Timestamp from = new Timestamp(tmp.getCreation().getTime()-86400000l);
						ret.append("<from><![CDATA["+DateUtils.date2String(tmp.getFrom(),"yyyy/MM/dd HH:mm")+"]]></from>");
						ret.append("<to><![CDATA["+DateUtils.date2String(tmp.getTo(),"yyyy/MM/dd HH:mm")+"]]></to>");						
					}
					
//					if (tmp.getType().equals("N"))
//					{
//						ret.append("<type><![CDATA["+s_now+"]]></type>");
//						ret.append("<from><![CDATA["+DateUtils.date2String(tmp.getCreation(),"yyyy/MM/dd")+"]]></from>");
//						ret.append("<to><![CDATA[]]></to>");
//					}
//		            else if (tmp.getType().equals("D"))
//		            {
//		            	ret.append("<type><![CDATA["+s_daily+"]]></type>");
//		            	ret.append("<from><![CDATA["+DateUtils.date2String(tmp.getCreation(),"yyyy/MM/dd")+"]]></from>");
//		            	ret.append("<to><![CDATA[]]></to>");
//		            }
//		            else if (tmp.getType().equals("W"))
//		            {
//		            	ret.append("<type><![CDATA["+s_weekly+"]]></type>");
//		            	long ltime = tmp.getCreation().getTime();
//		            	long a = ltime - 604800000L;
//		            	ret.append("<from><![CDATA["+DateUtils.date2String(new Timestamp(a),"yyyy/MM/dd")+"]]></from>");
//						ret.append("<to><![CDATA["+DateUtils.date2String(tmp.getCreation(),"yyyy/MM/dd")+"]]></to>");
//		            }
					ret.append("<type><![CDATA["+tmp.getType()+"]]></type>");
					
					if(tmp.getIshaccp().equalsIgnoreCase("TRUE"))
						ret.append("<style><![CDATA[HACCP]]></style>");
		            else
		            	ret.append("<style><![CDATA[Log]]></style>");
					
					ret.append("<step><![CDATA["+(tmp.getStep()/60)+"]]></step>");
					ret.append("<path><![CDATA["+tmp.getPath()+"]]></path>");
					ret.append("</row>");
				}
			}
		}
		
		ret.append("</response>");
		return ret.toString();
	}
}
