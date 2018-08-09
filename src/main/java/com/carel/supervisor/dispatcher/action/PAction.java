package com.carel.supervisor.dispatcher.action;

import java.io.File;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.ReportBean;
import com.carel.supervisor.dataaccess.datalog.impl.ReportBeanList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.VariableHelper;
import com.carel.supervisor.dispatcher.bean.HSActionBeanList;
import com.carel.supervisor.dispatcher.engine.print.DispPDFPrinter;
import com.carel.supervisor.report.AlarmReport;
import com.carel.supervisor.report.Report;
import com.carel.supervisor.report.ReportFactory;

public class PAction extends DispatcherAction {
//    private static final long DAY = (60L * 60L * 24L * 1000L);
    protected String siteName = "";
    protected String reportName = null;
    protected Timestamp reportbegin;
    protected Timestamp reportend;
	private Report r;
	private boolean bStore		= true;
	private boolean bSend		= false;
	private EAction eAction		= null;

    public PAction(Integer key, Integer id, Integer pri, Integer sta, String rec, Timestamp itime,
        Timestamp utime, String tmpl, String type, Boolean isalarm, Integer idvar, Timestamp start,
        Timestamp end) {
        super(key, id, pri, sta, rec, itime, utime, tmpl, type, isalarm, idvar, start, end);
        if( type.compareToIgnoreCase("Pr") == 0 ) {
        	type = "P";
        	this.typeAction = type;
        }
        if( type.compareToIgnoreCase("P") == 0 ) {
	        String params[] = rec.split(";");
	        if( params.length >= 2 ) {
	        	bStore		= Integer.parseInt(params[0]) == 1;
	        	bSend		= Integer.parseInt(params[1]) == 1;
	        	if( bSend ) {
		        	String erec	= "";
		        	int i = 2;
		        	if( i < params.length ) {
		        		while( true ) {
			        		erec += params[i];
			        		if( ++i < params.length )
			        			erec += ";";
			        		else
			        			break;
		        		}
		        		eAction = new EAction(key, id, pri, sta, erec, itime, utime, tmpl, "E", isalarm, idvar, start, end);
		        	}
	        	}
	        }
        }
    }

    protected String[] initializedRecepients(String recepient) {
		String recRet[] = new String[0];

		if( recepient != null && recepient.length() > 0 ) {
			String params[] = recepient.split(";");
			if( params.length >= 2 ) {
				recRet = new String[params.length - 2];
				for(int i = 2; i < params.length; i++)
					recRet[i - 2] = params[i];
				Arrays.sort(recRet);
			}
		}

        return recRet;
    }

    public void buildTemplate(String pathDir) throws Exception {
        String[] infoSender = this.getInfoSender(this.getIdSite());
		if ((infoSender != null) && (infoSender.length > 0)) {
			siteName = infoSender[0];
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Report.IDSITE, new Integer(getIdSite()));
		String strAppHome = BaseConfig.getAppHome();
		String strLogo = ProductInfoMgr.getInstance().getProductInfo().get("imgtop");
		params.put(Report.LOGO, strAppHome + "images\\" + strLogo);
		params.put(Report.LANGUAGE, LangUsedBeanList.getDefaultLanguage(1));
		int[] idvars = new int[this.getIdVariable().size()];
		
		ReportBean rb = this.getReportdetail();
		params.put(Report.SITENAME, siteName);
		params.put(Report.CODE, rb.getCode());
		params.put(Report.REPORTDETAIL, rb);
		for (int i = 0; i < idvars.length; i++) {
			idvars[i] = ((Integer) this.getIdVariable().get(i)).intValue();
		}
		if(idvars!=null && idvars.length>0 && idvars[0]!=0){
			params.put(Report.VARIABLES, idvars);
		}
		//Kevin Ge, if there is report variables, prepare the data report needs
		if(rb.getVariables() != null && rb.getVariables().length>0)
		{
			Integer[] idtmp = rb.getVariables();
			idvars = new int[idtmp.length];
			for (int k = 0; k < idtmp.length; k++) {
				idvars[k] = idtmp[k];
			}
			params.put(Report.VARIABLES, idvars);
			params.put(Report.VARS_MDL, rb.getVarsMdl());
		}
        
        int interval = rb.getTimelength();
        
        
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(System.currentTimeMillis());
        Timestamp tstart,tend;
        
        IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
        String dailyReportMidnightStyle =  p_info.get("daily_report_midnight_style");
        if(interval == 86400 && "false".equalsIgnoreCase(dailyReportMidnightStyle))
        {
        	int minute = gc.get(Calendar.MINUTE);
        	minute = (minute/10)*10;
        	gc.set(Calendar.MINUTE, minute);
        	gc.set(Calendar.SECOND, 0);
        }else{
        	int day = gc.get(Calendar.DAY_OF_MONTH);
            int month = gc.get(Calendar.MONTH);
            int year = gc.get(Calendar.YEAR);
            gc = new GregorianCalendar(year, month, day, 0, 0, 0);
        }
        tend = new Timestamp(gc.getTimeInMillis());
    	tstart = getDataStart(tend, interval);
        
        params.put(Report.START, tstart);
        params.put(Report.END, tend);
        
        /*alarm add*/
        Timestamp[] alr_tstart = new Timestamp[this.getStartTime().size()];
        Timestamp[] alr_tend = new Timestamp[this.getEndTime().size()];
        for (int i = 0; i < alr_tstart.length; i++) {
        	alr_tstart[i] = (Timestamp) this.getStartTime().get(i);
        	alr_tend[i] = (Timestamp) this.getEndTime().get(i);
        }
        params.put(Report.ALR_START, alr_tstart);
        params.put(Report.ALR_END, alr_tend);
        /**/        
        
        
        params.put(Report.DESCRIPTIONS, VariableHelper.getDescriptions(infoSender[2], this.getIdSite(), idvars));
        
         r = ReportFactory.buildReport(getReportdetail().getTemplateclass(), params);
         File document = r.generate();
         String filename = document.getName();
        
         // AUTOMATIC PRINT   configuration in Printer.ini
         if( typeAction.compareToIgnoreCase("P") == 0 ) {
	         if( rb.getTemplateclass().equalsIgnoreCase(AlarmReport.class.getName()) )
	        	 DispPDFPrinter.printAlarmFile(document.getAbsolutePath());
	         else
	        	 DispPDFPrinter.printReportFile(BaseConfig.getCarelPath()+"PvPro"+File.separator+filename);
         }
        addPathFile(filename);
        
        if( bSend && eAction != null ) {
   			eAction.setIdSite(this.getIdSite());
   			eAction.setSiteName(siteName);
   			eAction.setReportName(rb.getCode());
   			eAction.addPathFile(filename);
        }
    }

    protected ReportBean getReportdetail() {
    	try {
//			String[] rec = getRecepients();
//			if (rec != null && rec.length > 0) {
//				Integer idreport = Integer.parseInt(rec[0]);
//				return ReportBeanList.retrieveReportById(getIdSite(), idreport);
//			}
			String tmpl = getTemplate();
			if (tmpl != null) {
				Integer idreport = Integer.parseInt(tmpl);
				return ReportBeanList.retrieveReportById(getIdSite(), idreport);
			}
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		return null;
	}

	public int[] putActionInQueue() throws Exception
    {
        List<Integer> keyact = this.getKeyAction();
        int[] ret = new int[0];
        String[] listFile = this.getPathFiles();
        boolean allOk = true;

        if (listFile != null)
        {
            for (int i = 0; i < listFile.length; i++)
            {
            	if(this.isAlarm())
            		allOk = savePrintInHs();
            	else if( bStore )
            		allOk = saveReportInHs();
            	else
            		allOk = true;

                if (!allOk)
                    break;
            }

            ret = new int[keyact.size()];

            for (int i = 0; i < ret.length; i++)
            {
                ret[i] = ((Integer) keyact.get(i)).intValue();
            }

            HSActionBeanList actionbean = new HSActionBeanList();

            if (allOk)
            {
                actionbean.updateToSendActionList(ret);
            }
            else
            {
                actionbean.updateToDiscardActionList(ret);
            }

            // Clear array
            ret = new int[0];
        }

        if( bSend && eAction != null )
        	eAction.putActionInQueue();
        
        return ret;
    }

    private boolean saveReportInHs()
    {
    	boolean ris = false;
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	String template = this.getTemplate();
        int idReport = 0;
        try {
        	idReport = Integer.parseInt(template);
        }
        catch (Exception e){}
        
        if (idReport > 0)
        {
            ReportBean rBean = null;

            try
            {
                rBean = ReportBeanList.retrieveReportById(this.getIdSite(), idReport);
                long currentms = System.currentTimeMillis();
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTimeInMillis(currentms);
                
                IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
                String dailyReportMidnightStyle =  p_info.get("daily_report_midnight_style");
                if(rBean.getTimelength() == 86400 && "false".equalsIgnoreCase(dailyReportMidnightStyle))
                {
                	int minute = gc.get(Calendar.MINUTE);
                	minute = (minute/10)*10;
                	gc.set(Calendar.MINUTE, minute);
                	gc.set(Calendar.SECOND, 0);
                }else{
                	int day = gc.get(Calendar.DAY_OF_MONTH);
                    int month = gc.get(Calendar.MONTH);
                    int year = gc.get(Calendar.YEAR);
                    gc = new GregorianCalendar(year, month, day, 0, 0, 0);
                }
                reportend = new Timestamp(gc.getTimeInMillis());
                reportbegin = new Timestamp(reportend.getTime()-rBean.getTimelength()*1000);
                if(rBean != null)
                {
                	Integer key = SeqMgr.getInstance().next(null, "hsreport", "idhsreport");
                	String sql = "insert into hsreport values(?,?,?,?,?,?,?,?,?,?)";
                	Object[] param = {key,new Integer(this.getIdSite()),rBean.getCode(),rBean.getOutputtype(),
                					  UtilBean.writeBoolean(rBean.getHaccp()),rBean.getStep(),now,this.getPathFiles()[0],
                					  reportbegin, reportend};
                	DatabaseMgr.getInstance().executeStatement(null,sql,param);
                	ris = true;
                }
            }
            catch(Exception e) {
            	Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
        }
        
        return ris;
    }
    
    private boolean savePrintInHs()
    {
    	boolean ris = false;
    	Timestamp now = new Timestamp(System.currentTimeMillis());
        
        try
        {
        	Integer key = SeqMgr.getInstance().next(null, "hsprint", "idprint");
        	String sql = "insert into hsprint values(?,?,?,?,?)";
        	Object[] param = {key,new Integer(this.getIdSite()),now,this.getActionName(),this.getPathFiles()[0]};
        	DatabaseMgr.getInstance().executeStatement(null,sql,param);
        	ris = true;
        }
        catch(Exception e) {
        	Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
        
        return ris;
    }
    
    public static Timestamp getDataStart(Timestamp dataEnd, int interval)
	{
		Timestamp ret = null;	
		GregorianCalendar gc = new GregorianCalendar();
		switch (interval) {
		case 0:      //instant report
			break;
		case 86400:   //daily report
//			ret = new Timestamp(dataEnd.getTime()-86400000);
			gc.setTimeInMillis(dataEnd.getTime());
			gc.add(Calendar.DAY_OF_MONTH, -1);
			ret = new Timestamp(gc.getTimeInMillis());
			break;
		case 604800:    //weekly report
//			ret = new Timestamp(dataEnd.getTime()-604800000);
			gc.setTimeInMillis(dataEnd.getTime());
			gc.add(Calendar.WEEK_OF_YEAR, -1);
			ret = new Timestamp(gc.getTimeInMillis());
			break;
		default:
			break;
		}
		
		return ret;
	}

	public Report getReport() {
		return r;
	}
	
	// Razvan: to be clarified what is the difference between nameSite (DispatcherAction)
	// and siteName (PAction); both variable names creates confusion
	public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }
	
	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}
}
