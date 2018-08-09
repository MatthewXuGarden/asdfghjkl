package com.carel.supervisor.dataaccess.datalog.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.time.DateUtils;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.VariableHelper;
import com.carel.supervisor.dispatcher.action.PAction;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.report.HistoryReport;
import com.carel.supervisor.report.InstantReport;
import com.carel.supervisor.report.Report;
import com.carel.supervisor.report.ReportFactory;

public class ReportBeanList {
//	private ArrayList<ReportBean> reportList = new ArrayList<ReportBean>();
//	private HashMap<Integer, ReportBean> reportMap = new HashMap<Integer, ReportBean>();

//	 public ReportBeanList(){
//			String sql = "select * from cfreportkernel where idsite = ? and idreport=?";
//		 
//	 }

	
//Start Ing. Gilioli
	private final static int NUM_REPORTS_4_PAGE = 50;
	private static int pageTotal = 0;
	
	public static int getTotalPage()
	{
		return pageTotal;
	}
	public static Map<Integer, ReportBean> retrieveReport(int idsite) throws DataBaseException {
		HashMap<Integer, ReportBean> reportMap = new HashMap<Integer, ReportBean>();
		
		String sql = "select * from cfreportkernel where idsite = ? and idreport>0 order by code";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite) });
		ReportBean tmp = null;
		for (int i = 0; i < rs.size(); i++) {
			tmp = new ReportBean(rs.get(i));
			reportMap.put(new Integer(tmp.getIdreport()), tmp);
		}
		return reportMap;
	}
	
	public static Map<Integer, ReportBean> retrieveReportForScheduledAction(int idsite) throws DataBaseException {
		HashMap<Integer, ReportBean> reportMap = new HashMap<Integer, ReportBean>();
		
		String sql = "select * from cfreportkernel where idsite = ? and idreport>0 and timelength != -1 order by code";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite) });
		ReportBean tmp = null;
		for (int i = 0; i < rs.size(); i++) {
			tmp = new ReportBean(rs.get(i));
			reportMap.put(new Integer(tmp.getIdreport()), tmp);
		}
		return reportMap;
	}
	
	// Alessandro : retrieve reports that are still linked to some action through a rule
	public static Map<Integer, ReportBean> retrieveReportsLinkedToScheduledAction(int idsite) throws DataBaseException {
		HashMap<Integer, ReportBean> reportMap = new HashMap<Integer, ReportBean>();
		
		String sql = "select * from cfreportkernel where idsite = ? and idreport>0 and timelength != -1 and idreport IN (select CAST(template as numeric) from cfaction where actiontype = 'P' and isscheduled = 'TRUE') order by code";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite) });
		ReportBean tmp = null;
		for (int i = 0; i < rs.size(); i++) {
			tmp = new ReportBean(rs.get(i));
			reportMap.put(new Integer(tmp.getIdreport()), tmp);
		}
		return reportMap;
	}	
	
	public static String deleteReport(UserSession us, Properties prop) throws DataBaseException {
		Integer idReport=Integer.parseInt(prop.getProperty("idReportSelect"));
		Integer idSite=new Integer(us.getIdSite());
		String sAction = checkIfReportUsed(idReport);
		
		if (sAction == null) {
			DatabaseMgr.getInstance().executeStatement("delete from cfreportkernel where idsite = ? and idreport = ?", 
					new Object[]{idSite,idReport});
    		DatabaseMgr.getInstance().executeStatement("delete from cfreportdetail where idreport = ?", 
					new Object[]{idReport});
    		// db constraint will remove values linked to idreport from cfreporttime
    		// no need to remove them explicitly
		}
		else
		{
			us.getCurrentUserTransaction().setProperty("r_involved", sAction);
		}
		return sAction;
	}
	
	//20091209-Kevin
	//I need to return the newReport id. 
	public static int newReport(UserSession us, Properties prop)
	throws DataBaseException {

		Integer idReport=SeqMgr.getInstance().next(null, "cfreportkernel", "idreport");
    	Integer idSite=us.getIdSite();
    	String reportName=prop.getProperty("report_name");
		String templateFile=prop.getProperty("layout");
		String output=prop.getProperty("output");
		
		Boolean isHaccp=prop.getProperty("is_haccp")!=null; 
		Boolean isHistor=!isHaccp;
		Integer step=new Integer(0);
		Integer timeLength=Integer.parseInt(prop.getProperty("interval"));
		String templateClass="";
		String variables=prop.getProperty("variables");
		String timeTableValue = prop.getProperty("timeTableValue");
		
		switch(timeLength){
			case 0:
				//isHaccp=isHistor=false;
				templateClass=InstantReport.class.getName();
				break;
			case -1:
			case 86400:
			case 604800:
					templateClass=HistoryReport.class.getName();
				int freq = Integer.parseInt(prop.getProperty("frequency"));
				step = mapFrequency(freq);
			break;
		}//switch
		
		
		DatabaseMgr.getInstance().executeStatement("insert into cfreportkernel values(?,?,?,?,?,?,?,?,?,?,?)", 
				new Object[] {idReport,idSite,reportName,isHistor,isHaccp,step,timeLength,output,templateClass,templateFile,new Timestamp(System.currentTimeMillis())});
		if(variables.compareTo("")!=0){
			String datas[]=variables.split(";");
			for(int i=0;i<datas.length;i++){
				DatabaseMgr.getInstance().executeStatement("insert into cfreportdetail values(?,?)", 
					new Object[]{idReport,Integer.parseInt(datas[i].split("-")[1])});
			}					
		}
		
		if( step == -1 && timeTableValue.length() > 0 ) {
			String timeValues[] = timeTableValue.split(";");
			for(int i = 0; i < timeValues.length; i++) {
				DatabaseMgr.getInstance().executeStatement("insert into cfreporttime values(?,?)", 
					new Object[] { idReport, Integer.parseInt(timeValues[i]) });
			}
		}

		return idReport.intValue();
	}

	
	public static void modifyReport(UserSession us, Properties prop)
	throws DataBaseException {

		
		Integer idSite=us.getIdSite();
		Integer idReport=Integer.parseInt(prop.getProperty("idReportSelect"));
		
	
    	String reportName=prop.getProperty("report_name");
		String templateFile=prop.getProperty("layout");
		String output=prop.getProperty("output");
		
		Boolean isHaccp=prop.getProperty("is_haccp")!=null; 
		Boolean isHistor=!isHaccp;
		Integer step=new Integer(0);
		Integer timeLength=Integer.parseInt(prop.getProperty("interval"));
		String templateClass="";
		String variables=prop.getProperty("variables");
		String timeTableValue = prop.getProperty("timeTableValue");

		
		switch(timeLength)
		{
			case 0:
				//isHaccp=isHistor=false;
				templateClass=InstantReport.class.getName();
				break;
			case -1:
			case 86400:
			case 604800:
				templateClass=HistoryReport.class.getName();
				int freq = Integer.parseInt(prop.getProperty("frequency"));
				step = mapFrequency(freq);
			break;
		}//switch
		
		
		
		DatabaseMgr.getInstance().executeStatement("update cfreportkernel set " +
				" code          = ?,"+
				" history       = ?,"+
				" haccp         = ?,"+
				" step          = ?,"+
				" timelength    = ?,"+
				" outputtype    = ?,"+
				" templateclass = ?,"+
				" templatefile  = ?,"+
				" lastupdate    = ? "+
				" where idsite  = ? "+
				" and idreport  = ? ",
				new Object[]{reportName,isHistor,isHaccp,step,timeLength,output,templateClass,templateFile,new Timestamp(System.currentTimeMillis()),idSite,idReport});
		
	
		DatabaseMgr.getInstance().executeStatement("delete from cfreportdetail where idreport = ?", 
				new Object[]{idReport});
		if(variables.compareTo("")!=0){
			String datas[]=variables.split(";");
			for(int i=0;i<datas.length;i++){
				DatabaseMgr.getInstance().executeStatement("insert into cfreportdetail values(?,?)", 
						new Object[]{idReport,Integer.parseInt(datas[i].split("-")[1])});
			}					
		}
		
		DatabaseMgr.getInstance().executeStatement("delete from cfreporttime where idreport = ?", 
				new Object[] { idReport } );
		if( step == -1 && timeTableValue.length() > 0 ) {
			String timeValues[] = timeTableValue.split(";");
			for(int i = 0; i < timeValues.length; i++) {
				DatabaseMgr.getInstance().executeStatement("insert into cfreporttime values(?,?)", 
					new Object[] { idReport, Integer.parseInt(timeValues[i]) });
			}
		}
		
	}
	public static void modifyReportCode(int idSite, int idReport, String reportName)
		throws Exception
	{
    	DatabaseMgr.getInstance().executeStatement("update cfreportkernel set " +
				" code  		= ?" +
				" where idSite	= ? and" +
				" idreport=?",
				new Object[]{reportName,idSite,idReport});
	}
	public static Report printReport(UserSession us, Properties prop) throws Exception{
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Report.IDSITE, new Integer(us.getIdSite()));
		String strAppHome = BaseConfig.getAppHome();
		String strLogo = ProductInfoMgr.getInstance().getProductInfo().get("imgtop");
		params.put(Report.LOGO, strAppHome + "images\\" + strLogo);
		params.put(Report.LANGUAGE, us.getLanguage());
		params.put(Report.USER, us.getUserName());
		params.put(Report.ExportFileName, prop.getProperty("exportfilename"));
		params.put(Report.ExportPath, prop.getProperty("exportfilepath"));
		params.put(Report.ISLOCAL, prop.getProperty("islocal"));
		ReportBean rb = ReportBeanList.retrieveReportById(us.getIdSite(), new Integer(prop.getProperty("idReportSelect")));
		params.put(Report.SITENAME, us.getSiteName());
		params.put(Report.CODE, rb.getCode());
		int freq = Integer.parseInt(prop.getProperty("frequency"));
		rb.setStep(mapFrequency(freq));
		
		// time values
		if( freq == -1 ) {
			Integer timeValues[] = null;
			String timeTableValue = prop.getProperty("timeTableValue");
			if( timeTableValue.length() > 0 ) {
				String stringValues[] = timeTableValue.split(";");
				timeValues = new Integer[stringValues.length];
				for(int i = 0; i < stringValues.length; i++)
					timeValues[i] = new Integer(Integer.parseInt(stringValues[i]));
			}
			else {
				 timeValues = new Integer[0];
			}
			rb.setTimeValues(timeValues);
		}
		
		params.put(Report.REPORTDETAIL, rb);
		Integer[] idtmp = rb.getVariables();
		int[] idvars = new int[idtmp.length];
		for (int k = 0; k < idtmp.length; k++) {
			idvars[k] = idtmp[k];
		}
		params.put(Report.VARIABLES, idvars);
		params.put(Report.VARS_MDL, rb.getVarsMdl());
		params.put(Report.TIMEVALUES, rb.getTimeValues());
		Timestamp dataStart = getDataStart(prop,rb.getTimelength());
		Timestamp dataEnd = getDataEnd(prop,rb.getTimelength());
		IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
        String dailyReportMidnightStyle =  p_info.get("daily_report_midnight_style");
        if(rb.getTimelength() == 86400 && "false".equalsIgnoreCase(dailyReportMidnightStyle) && DateUtils.isSameDay(dataStart, new Date()))
        {
        	GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(System.currentTimeMillis());
        	int minute = gc.get(Calendar.MINUTE);
        	minute = (minute/10)*10;
        	gc.set(Calendar.MINUTE, minute);
        	gc.set(Calendar.SECOND, 0);
        	dataEnd = new Timestamp(gc.getTimeInMillis());
        	dataStart = PAction.getDataStart(dataEnd, rb.getTimelength());
        }

		// check report limits for PVPRO box only
		if( rb.getTimelength() !=0 && !BaseConfig.isDemo() )
		{
			int n_vars = idvars.length;
			long timelenght = (dataEnd.getTime() - dataStart.getTime())/1000;
			int n_freq = mapFrequency(freq);
			if( n_freq == -1 ) n_freq = 300; // custom frequency request the same amount of samples as 5min
			long n_samples = n_vars*timelenght/n_freq;
			if (n_samples>= Report.MAX_SAMPLES)
			{
				return null;
			}
		}

		String s_haccp = rb.getHaccp()?"true":"false";
		params.put(Report.HACCP, s_haccp);
		params.put(Report.START, dataStart);
        params.put(Report.END, dataEnd);
        params.put(Report.DESCRIPTIONS, VariableHelper.getDescriptions(us.getLanguage(), us.getIdSite(), idvars));
		return ReportFactory.buildReport(rb.getTemplateclass(), params);
	}
//End Ing. Gilioli	
	
	
	
	
	
	public static ReportBean retrieveReportById(int idsite, int idreport) throws Exception {
		ReportBean rb = null;
		String sql = "select * from cfreportkernel where idsite = ? and idreport=?";
		Object[] par = { new Integer(idsite), new Integer(idreport) };
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, par);
		Record r = null;
		if (rs != null && rs.size() > 0)
			r = rs.get(0);
		if (r != null)
			rb = new ReportBean(r);
		return rb;
	}

	public static Map<Integer, ReportBean> retrieveHistoricalReport(int idsite) throws DataBaseException {
		HashMap<Integer, ReportBean> reportMap = new HashMap<Integer, ReportBean>();
		
		String sql = "select * from cfreportkernel where idsite = ? and history=true and haccp=false order by idreport";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite) });
		ReportBean tmp = null;
		for (int i = 0; i < rs.size(); i++) {
			tmp = new ReportBean(rs.get(i));
			reportMap.put(new Integer(tmp.getIdreport()), tmp);
		}
		return reportMap;
	}

	public static Map<Integer, ReportBean> retrieveHaccpReport(int idsite) throws DataBaseException {
		HashMap<Integer, ReportBean> reportMap = new HashMap<Integer, ReportBean>();

		String sql = "select * from cfreportkernel where idsite = ? and haccp=true order by idreport";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { new Integer(idsite) });
		ReportBean tmp = null;

		for (int i = 0; i < rs.size(); i++) {
			tmp = new ReportBean(rs.get(i));
			reportMap.put(new Integer(tmp.getIdreport()), tmp);
		}
		return reportMap;
	}

	public static void newReport(int idsite, String desc, boolean history, boolean haccp, int step, String type, String templateclass,  String templatefile)
			throws DataBaseException {
		String sql = "insert into cfreportkernel values (?,?,?,?,?,?,?,?,?,?)";
		SeqMgr o = SeqMgr.getInstance();
		Object[] params = new Object[10];
		params[0] = o.next(null, "cfreportkernel", "idreport");
		params[1] = new Integer(idsite);
		params[2] = desc;
		params[3] = history;
		params[4] = haccp;
		params[5] = new Integer(step);
		params[6] = type;
		params[7] = templateclass;
		params[8] = templatefile;
		params[9] = new Timestamp(System.currentTimeMillis());

		DatabaseMgr.getInstance().executeStatement(null, sql, params);
	}

	public static String deleteReport(int idsite, int idreport) throws DataBaseException {
		String sAction = checkIfReportUsed(idreport);
		if (sAction == null) {
			String sql = "delete from cfreportkernel where idsite = ? and idreport = ?";
			DatabaseMgr.getInstance()
					.executeStatement(
							null,
							sql,
							new Object[] { new Integer(idsite),
									new Integer(idreport) });
		}
		return sAction;
	}

	public static void updateReport(int idreport, int idsite, String desc, 
			boolean history, boolean haccp, int step, String type, String templateclass,  String templatefile)
			throws DataBaseException {
		String sql = "update cfreportkernel set code=?,history=?,haccp=?,step=?,type=?,templateclass=?,templatefile=?,lastupdate=? where idsite=? and idreport=?";

		Object[] params = new Object[10];
		params[0] = desc;
		params[1] = history;
		params[2] = haccp;
		params[3] = new Integer(step);
		params[4] = type;
		params[5] = templateclass;
		params[6] = templatefile;
		params[7] = new Timestamp(System.currentTimeMillis());
		params[8] = new Integer(idsite);
		params[9] = new Integer(idreport);
		DatabaseMgr.getInstance().executeStatement(null, sql, params);
	}

//	public static ReportBean getReport(int i) {
//		return reportList.get(i);
//	}
//
//	public static ReportBean getReportById(int id) {
//		return reportMap.get(new Integer(id));
//	}
//
//	public int size() {
//		return reportList.size();
//	}

	private static String checkIfReportUsed(int idReport) {
		String sql = "select code from cfaction where ( (actiontype=? and isscheduled=?) or (actiontype=? and isscheduled=?) ) and template=?";
		RecordSet rs = null;
		Record r = null;
		String actionCode = null;
		try {
			rs = DatabaseMgr.getInstance().executeQuery(null, sql,
				new Object[] { "P", "TRUE", "Pr", "FALSE", String.valueOf(idReport) });
			if (rs != null && rs.size() > 0)
				r = rs.get(0);
			if (r != null)
				actionCode = UtilBean.trim(r.get("code"));
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(ReportBeanList.class);
			logger.error(e);
		}
		return actionCode;
	}

//	public static HsReportBean[] loadReportHistory(int idsite, String filter) {
//		HsReportBean[] ret = null;
//		RecordSet rs = null;
//
//		String sql = "select * from hsreport where idsite = ? ";
//		if (filter != null && filter.length() > 0)
//			sql += "and ishaccp=? ";
//		sql += "order by creationtime desc";
//
//		try {
//			Object[] params = null;
//			if (filter != null && filter.length() > 0)
//				params = new Object[] { new Integer(idsite), filter };
//			else
//				params = new Object[] { new Integer(idsite) };
//
//			rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
//			if (rs != null) {
//				ret = new HsReportBean[rs.size()];
//				for (int i = 0; i < ret.length; i++)
//					ret[i] = new HsReportBean(rs.get(i));
//			}
//		} catch (Exception e) {
//			Logger logger = LoggerMgr.getLogger(ReportBeanList.class);
//			logger.error(e);
//		}
//		return ret;
//	}
	public static HsReportBean[] loadReportHistory(int idsite,String filter)
	{
		HsReportBean[] ret = null;
		String sql = "select * from hsreport where idsite = ? ";
		Object[] params = null;
		if (filter != null && filter.length() > 0)
		{
			sql += "and ishaccp=? ";
			params = new Object[]{new Integer(idsite),filter};
		}
		else
			params = new Object[]{new Integer(idsite)};
		try
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
			if (rs != null && rs.size()>0) 
			{	
				ret = new HsReportBean[rs.size()];
				for (int i = 0; i < ret.length; i++)
				{
					ret[i] = new HsReportBean(rs.get(i));
				}
			}
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(ReportBeanList.class);
			logger.error(e);
		}
		return ret;
	}
	public static HsReportBean[] loadReportHistory(int idsite, String filter,int numPage) {
		HsReportBean[] ret = null;
		RecordSet rs = null;

		String sql = "select count(*) from hsreport where idsite = ? ";
		if (filter != null && filter.length() > 0)
			sql += "and ishaccp=? ";

		try {
			Object[] params = null;
			if (filter != null && filter.length() > 0)
			{
				params = new Object[] {new Integer(idsite),filter};
			}
			else
			{
				params = new Object[] {new Integer(idsite)};
			}
			rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
			if(rs != null && rs.size()>0)
			{
				pageTotal =  ((Integer) rs.get(0).get("count")).intValue() / NUM_REPORTS_4_PAGE;
				int remainder = ((Integer) rs.get(0).get("count")).intValue() % NUM_REPORTS_4_PAGE;
				if(remainder != 0)
				{
					pageTotal++;
				}
			}
			sql = "select * from hsreport where idsite = ? ";
			if (filter != null && filter.length() > 0)
				sql += "and ishaccp=? ";
			sql += "order by creationtime desc limit ? offset ?";
			if (filter != null && filter.length() > 0)
			{
				params = new Object[] { new Integer(idsite),filter, new Integer(NUM_REPORTS_4_PAGE), new Integer((numPage - 1) * NUM_REPORTS_4_PAGE)};
			}
			else
			{
				params = new Object[] { new Integer(idsite), new Integer(NUM_REPORTS_4_PAGE), new Integer((numPage - 1) * NUM_REPORTS_4_PAGE)};
			}

			rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
			if (rs != null && rs.size()>0) 
			{	
				ret = new HsReportBean[rs.size()];
				for (int i = 0; i < ret.length; i++)
				{
					ret[i] = new HsReportBean(rs.get(i));
				}
			}
			else
			{
				pageTotal = 1;
			}
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(ReportBeanList.class);
			logger.error(e);
		}
		return ret;
	}

	public static boolean deleteReportHistory(String ids) {
		boolean ris = false;

		String sql = "delete from hsreport where idhsreport=?";
		String[] id = ids.split("\\|");
		Object[][] value = new Object[id.length][1];
		for(int i=0;i<id.length;i++)
		{
			value[i] = new Object[]{id[i]};
		}
		try {
			DatabaseMgr.getInstance().executeMultiStatement(null, sql, value);
			ris = true;
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(ReportBeanList.class);
			logger.error(e);
		}

		return ris;
	}
	public static boolean deleteReportHistory(int idsite,String filter) {
		boolean ris = false;
		String sql = "delete from hsreport where idsite = ? ";
		Object[] params = null;
		if (filter != null && filter.length() > 0)
		{
			sql += "and ishaccp=? ";
			params = new Object[]{new Integer(idsite),filter};
		}
		else
			params = new Object[]{new Integer(idsite)};
		try {
			DatabaseMgr.getInstance().executeStatement(null, sql, params);
			ris = true;
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(ReportBeanList.class);
			logger.error(e);
		}

		return ris;
	}
	public static HsPrintBean[] loadPrintHistory(int idsite,int numPage) {
		HsPrintBean[] ret = null;
		RecordSet rs = null;
		
		String sql = "select count(*) from hsprint where idsite = ? ";
		try {
			Object[] params = null;
			params = new Object[] {new Integer(idsite)};
			rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
			if(rs != null && rs.size()>0)
			{
				pageTotal =  ((Integer) rs.get(0).get("count")).intValue() / NUM_REPORTS_4_PAGE;
				int remainder = ((Integer) rs.get(0).get("count")).intValue() % NUM_REPORTS_4_PAGE;
				if(remainder != 0)
				{
					pageTotal++;
				}
			}
			sql = "select * from hsprint where idsite = ? order by creationtime desc limit ? offset ?";
			params = new Object[] { new Integer(idsite), new Integer(NUM_REPORTS_4_PAGE), new Integer((numPage - 1) * NUM_REPORTS_4_PAGE)};

			rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
			if (rs != null && rs.size()>0) 
			{	
				ret = new HsPrintBean[rs.size()];
				for (int i = 0; i < ret.length; i++)
				{
					ret[i] = new HsPrintBean(rs.get(i));
				}
			}
			else
			{
				pageTotal = 1;
			}
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(ReportBeanList.class);
			logger.error(e);
		}
		return ret;
	}
	public static HsPrintBean[] loadPrintHistory(int idsite)
	{
		HsPrintBean[] ret = null;
		String sql = "select * from hsprint where idsite = ? ";
		Object[] params = new Object[]{new Integer(idsite)};
		try
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
			if (rs != null && rs.size()>0) 
			{	
				ret = new HsPrintBean[rs.size()];
				for (int i = 0; i < ret.length; i++)
				{
					ret[i] = new HsPrintBean(rs.get(i));
				}
			}
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(ReportBeanList.class);
			logger.error(e);
		}
		return ret;
	}
	public static HsReportBean[] retrieveHsReportByIds(int idsite, int[] ids) throws Exception {
		ReportBean rb = null;
		String sql = "select * from hsreport where idsite = ? and idhsreport in (";
		for(int i=0;i<ids.length;i++)
		{
			if(i == 0)
				sql += "?";
			else
				sql += ",?";
		}
		sql += ")";
		Object[] par = new Object[ids.length+1];
		par[0] = new Integer(idsite);
		for(int i=0;i<ids.length;i++)
		{
			par[i+1] = new Integer(ids[i]);
		}
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, par);
		HsReportBean[] ret = null;
		if (rs != null && rs.size() > 0)
		{
			ret = new HsReportBean[rs.size()];
			for (int i = 0; i < ret.length; i++)
			{
				ret[i] = new HsReportBean(rs.get(i));
			}
		}
		return ret;
	}
	public static boolean deletePrintHistory(String ids) {
		boolean ris = false;
		String sql = "delete from hsprint where idprint=?";
		String[] id = ids.split("\\|");
		Object[][] value = new Object[id.length][1];
		for(int i=0;i<id.length;i++)
		{
			value[i] = new Object[]{id[i]};
		}
		try {
			DatabaseMgr.getInstance().executeMultiStatement(null, sql, value);
			ris = true;
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(ReportBeanList.class);
			logger.error(e);
		}

		return ris;
	}
	public static boolean deletePrintHistory(int idsite) {
		boolean ris = false;
		String sql = "delete from hsprint where idsite = ? ";
		Object[] params = new Object[]{new Integer(idsite)};
		try {
			DatabaseMgr.getInstance().executeStatement(null, sql, params);
			ris = true;
		} catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(ReportBeanList.class);
			logger.error(e);
		}

		return ris;
	}
	public static HsPrintBean[] retrieveHsPrintByIds(int idsite, int[] ids) throws Exception {
		ReportBean rb = null;
		String sql = "select * from hsprint where idsite = ? and idprint in (";
		for(int i=0;i<ids.length;i++)
		{
			if(i == 0)
				sql += "?";
			else
				sql += ",?";
		}
		sql += ")";
		Object[] par = new Object[ids.length+1];
		par[0] = new Integer(idsite);
		for(int i=0;i<ids.length;i++)
		{
			par[i+1] = new Integer(ids[i]);
		}
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, par);
		HsPrintBean[] ret = null;
		if (rs != null && rs.size() > 0)
		{
			ret = new HsPrintBean[rs.size()];
			for (int i = 0; i < ret.length; i++)
			{
				ret[i] = new HsPrintBean(rs.get(i));
			}
		}
		return ret;
	}
	public static int getDeviceByParam(int idvar) {
		int iddev = 0;

		if (idvar > 0) {
			String sql = "select iddevice from cfvariable where idvariable=?";
			Object[] param = new Object[] { new Integer(idvar) };
			RecordSet rs = null;

			try {
				rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

				if ((rs != null) && (rs.size() > 0)) {
					iddev = ((Integer) (rs.get(0).get(0))).intValue();
				}
			} catch (DataBaseException e) {
				Logger logger = LoggerMgr.getLogger(ReportBeanList.class);
				logger.error(e);
			}
		}

		return iddev;
	}
	
	private static int mapFrequency(int freq)
	{
		int ret = 3600;
		switch(freq)
		{
		case -1:
			ret = new Integer(-1);
			break;
		case 0:
			ret =new Integer(0);
		break;
		case 1:
			ret=new Integer(5);
		break;
		case 2:
			ret=new Integer(30);
		break;
		case 3:
			ret=new Integer(60);
		break;
		case 4:
			ret=new Integer(300);
		break;
		
		case 5:
			ret=new Integer(600);
			break;
		case 6:
			ret=new Integer(900);
			break;
		case 7:
			ret=new Integer(1800);
		break;
		case 8:
			ret=new Integer(3600);
		break;
		case 9:
			ret=new Integer(43200);
		break;
		case 10:
			ret=new Integer(86400);
		break;
		default:
			ret=new Integer(3600);
		}
		
		return ret;
	}
	
	private static Timestamp getDataStart(Properties prop, int interval)
	{
		int y,m,d = 0;
		Timestamp ret = null;		
		switch (interval) {
		case 0:      //instant report
			
			break;
		case 86400:   //daily report
			y=Integer.parseInt((String)prop.get("tester_year"));
			m=Integer.parseInt((String)prop.get("tester_month"))-1;
			d=Integer.parseInt((String)prop.get("tester_day"));
			ret = new Timestamp(new GregorianCalendar(y,m,d,00,00).getTimeInMillis());
			break;
		case 604800:    //weekly report
			
			y=Integer.parseInt((String)prop.get("tester_year"));
			m=Integer.parseInt((String)prop.get("tester_month"))-1;
			d=Integer.parseInt((String)prop.get("tester_day"));
			ret = new Timestamp(new GregorianCalendar(y,m,d,00,00).getTimeInMillis());

			
			break;
		case -1:       //custom report
			y=Integer.parseInt((String)prop.get("tester_year"));
			m=Integer.parseInt((String)prop.get("tester_month"))-1;
			d=Integer.parseInt((String)prop.get("tester_day"));
			
			ret = new Timestamp(new GregorianCalendar(y,m,d,00,00).getTimeInMillis()); 
		break;
		default:
			break;
		}
		
		return ret;
	}
	
	private static Timestamp getDataEnd(Properties prop, int interval)
	{
		int y,m,d = 0;
		Timestamp ret = null;	
		GregorianCalendar gc = null;
		switch (interval) {
		case 0:      //instant report
			
			break;
		case 86400:   //daily report
			y=Integer.parseInt((String)prop.get("tester_year"));
			m=Integer.parseInt((String)prop.get("tester_month"))-1;
			d=Integer.parseInt((String)prop.get("tester_day"));
//			ret = new Timestamp(new GregorianCalendar(y,m,d,24,00).getTimeInMillis());
//			ret = new Timestamp(new GregorianCalendar(y,m,d,0,0).getTimeInMillis()+86400000);
			gc = new GregorianCalendar(y,m,d,0,0);
			gc.add(Calendar.DAY_OF_MONTH, 1);
			ret = new Timestamp(gc.getTimeInMillis());
			break;
		case 604800:    //weekly report
			
			y=Integer.parseInt((String)prop.get("tester_year"));
			m=Integer.parseInt((String)prop.get("tester_month"))-1;
			d=Integer.parseInt((String)prop.get("tester_day"));
			//ret = new Timestamp(new GregorianCalendar(y,m,d,24,00).getTimeInMillis());
//			ret = new Timestamp(new GregorianCalendar(y,m,d,0,0).getTimeInMillis()+604800000);
			gc = new GregorianCalendar(y,m,d,0,0);
			gc.add(Calendar.WEEK_OF_YEAR, 1);
			ret = new Timestamp(gc.getTimeInMillis());
			break;
		case -1:       //custom report
			y=Integer.parseInt((String)prop.get("tester2_year"));
			m=Integer.parseInt((String)prop.get("tester2_month"))-1;
			d=Integer.parseInt((String)prop.get("tester2_day"));
			
			ret = new Timestamp(new GregorianCalendar(y,m,d,24,00).getTimeInMillis()); 
		break;
		default:
			break;
		}
		
		return ret;
	}
}
