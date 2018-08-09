package com.carel.supervisor.presentation.bo;

import java.util.Properties;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.presentation.bean.ReportBeanListPres;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;


public class BReportConf extends BoMaster
{
	private static final long serialVersionUID = -887846883144507898L;

	private static final int REFRESH_TIME = -1;

    public BReportConf(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeRefreshTime()
    {
        Properties p = new Properties();

        return p;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name","initialize();resizeTableReport();");
        p.put("tab2name","initialize();resizeTableReport();");
        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name","reportconf.js;dbllistbox.js;");
        p.put("tab2name","reportconf.js;dbllistbox.js;");
        return p;
    }

    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
    	String cmd = prop.getProperty("cmd");
    	int idreport = -1;
    	if ((cmd.equals("rem"))||(cmd.equals("upd")))
    		idreport = Integer.parseInt(prop.getProperty("idreport"));
    	if ((!cmd.equals("reload"))&&(!cmd.equals("set")))
    	{
    		int idsite = us.getIdSite();
    		String desc = prop.getProperty("desc");
    		String type = prop.getProperty("type_report");
        	Boolean history = new Boolean(prop.getProperty("ishaccp"));
        	Boolean haccp = new Boolean(prop.getProperty("ishaccp"));
        	String templateclass = prop.getProperty("templateclass");
        	String templatefile = prop.getProperty("templatefile");
        	int step = -1;
        	if ((prop.getProperty("step")!=null)&&(!prop.getProperty("step").equals("")))
        		step = Integer.parseInt(prop.getProperty("step"));
            //Start Andrea Modifiche
        	//String template = "";
//            String template = prop.getProperty("template");
        	String params = prop.getProperty("params");
        	if (!params.equals(""))
        	{
        		params = "pk" + params;
        		params = params.replace(",",";pk");
        	}
        	
        	if (cmd.equals("add"))
        	{
        		try { 
        			ReportBeanListPres.newReport(idsite, desc, history, haccp, step, type, templateclass, templatefile);
        		}
        		catch(Exception e) {
        			Logger logger = LoggerMgr.getLogger(this.getClass());
        			logger.error(e);
        		}
        	}
        	else if(cmd.equals("rem"))
        	{
        		String ris = null;
        		try 
        		{
        			ris = ReportBeanListPres.deleteReport(idsite,idreport);
        			if(ris != null)
        				us.setProperty("action",ris);
        		}
    			catch(Exception e) {
    				Logger logger = LoggerMgr.getLogger(this.getClass());
    				logger.error(e);
    			}
        	}
        	else if(cmd.equals("upd"))
        	{
        		try 
        		{
        			ReportBeanListPres.updateReport(idreport, idsite, desc, history, haccp, step, type, templateclass, templatefile);
        		}
        		catch(Exception e) {
        			Logger logger = LoggerMgr.getLogger(this.getClass());
        			logger.error(e);
        		}
        	}
    	}
    }
}
