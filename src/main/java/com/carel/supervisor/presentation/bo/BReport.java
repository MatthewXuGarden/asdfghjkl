package com.carel.supervisor.presentation.bo;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.dataaccess.datalog.impl.ReportBean;
import com.carel.supervisor.dataaccess.datalog.impl.ReportBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.ReportExportConfigList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.FileDialogBean;
import com.carel.supervisor.presentation.bean.VarMdlBean;
import com.carel.supervisor.presentation.bean.VarMdlBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.report.Report;



public class BReport extends BoMaster
{
	private static final long serialVersionUID = 7496721535377011061L;
	private static final int REFRESH_TIME = -1;

    public BReport(String l)
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
        p.put("tab1name", "initialize();enableCal();openOrSave();");
        p.put("tab2name", "initialize();");
        p.put("tab3name", "initializeSubtab3();");
        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "report.js;calendar.js;dbllistbox.js;dataselect.js;timetable.js;keyboard.js;../arch/FileDialog.js;");
        p.put("tab2name", "report.js;calendar.js;dbllistbox.js;dataselect.js;timetable.js;keyboard.js;");
        p.put("tab3name", "report.js;keyboard.js;");
        return p;
    }
	// DOCTYPE STRICT is necessary to have FileDialog correctly functioning
	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab1name", DOCTYPE_STRICT);
		return p;
    }
    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
    	if("tab1name".equals(tabName) || "tab2name".equals(tabName))
    	{
	    	String command=us.getPropertyAndRemove("command");
	    	
	    	if(command.compareTo("add_template")==0)
	    		ReportBeanList.newReport(us, prop);
	    	else if(command.compareTo("rem_template")==0)
	    		ReportBeanList.deleteReport(us,prop);
	    	else if(command.compareTo("mod_template")==0)
	    		ReportBeanList.modifyReport(us, prop);
	    	else if(command.compareTo("printReport")==0)
	    	{
	    		Report r = ReportBeanList.printReport(us, prop);
				us.getCurrentUserTransaction().setProperty("commit_btn", "print");
	    		if (r!=null)
	    		{
	        		r.setToTempFolder(true);
	    			String filename = r.generate().getPath();
	    			us.getCurrentUserTransaction().setProperty("r_path", filename);
	    		}
	    		else
	    			us.getCurrentUserTransaction().setProperty("maxlimit", "over");
	    	}else if(command.compareTo("exportReport")==0){
	    		
	    		Report r = ReportBeanList.printReport(us, prop);
				us.getCurrentUserTransaction().setProperty("commit_btn", "export");
	    		if (r!=null){
	        		r.setToTempFolder(true);
	    			String filename = r.generate().getPath();
	    			us.getCurrentUserTransaction().setProperty("r_path", filename);
	    		}else
	    			us.getCurrentUserTransaction().setProperty("maxlimit", "over");
	    	}
    	}
    	else if("tab3name".equals(tabName))
    	{
    		String separator = prop.getProperty(ReportExportConfigList.SEPARATOR);
    		String decimalLength = prop.getProperty(ReportExportConfigList.DECIMAL_LENGTH);
    		String groupSeparator = prop.getProperty(ReportExportConfigList.GROUP_SEPARATOR);
    		String decimalSeparator = prop.getProperty(ReportExportConfigList.DECIMAL_SEPARATOR);
    		String[] keys = {ReportExportConfigList.SEPARATOR,ReportExportConfigList.DECIMAL_LENGTH,
    				ReportExportConfigList.GROUP_SEPARATOR,ReportExportConfigList.DECIMAL_SEPARATOR};
    		String[] values = {separator,decimalLength,groupSeparator,decimalSeparator};
    		ReportExportConfigList conf = new ReportExportConfigList();
    		conf.save(keys, values);
    	}
    }

	@Override
	public String executeDataAction(UserSession us, String tabName,
			Properties prop) throws Exception {
		// added to manage file dialog functioning
		String cmd = prop.getProperty("cmd");
		if( cmd != null && cmd.equalsIgnoreCase(FileDialogBean.CMD) ) {
			String path = prop.getProperty("path");
			String filter = prop.getProperty("filter");
			return (new FileDialogBean(us.getLanguage())).cmdResponse(path, filter);
		}
		Integer action=Integer.parseInt((String) prop.get("action"));
		Integer idDevice=Integer.parseInt((String) prop.get("iddevice"));
		String isHaccp=((String) prop.get("ishaccp")).toUpperCase();
		Integer interval=Integer.parseInt((String) prop.get("interval"));
		StringBuilder response= new StringBuilder();
		response.append("<response>");
		
		switch(action.intValue()){
			case 0: // LOAD_DEVICE_VAR
				response.append("<![CDATA[");
				VarphyBeanList varlist = new VarphyBeanList();
				VarphyBean[] vars ;
				
				if (interval==0)  //BIOLO: instant report show all variables
					vars= varlist.getAllVarsOfDevice(us.getLanguage(),us.getIdSite(),idDevice,isHaccp,interval);
				else
					vars= varlist.getLogVarsOfDevice(us.getLanguage(),us.getIdSite(),idDevice,isHaccp,interval);
				response.append("<select ondblclick=\"addDevVar();\"  name='sections' multiple size='10' id='var' class='standardTxt' style='width: 100%; font-size:10pt;'>");
				for (int i=0;i<vars.length;i++)
					{
						VarphyBean aux = vars[i];
						response.append("<option "+"class='"+(i%2==0?"Row1'":"Row2'")+" value=\"" );
						response.append(String.valueOf(aux.getId()));
						response.append("\"");
						response.append(">");
						response.append(aux.getShortDescription());
						response.append("</option>");
					}
				response.append("</select>");
				response.append("]]>");	
				
			break;
			case 1: // LOAD_DEVICE
				response.append("<![CDATA[");
				DeviceListBean devs = new DeviceListBean(us.getIdSite(),us.getLanguage());
				DeviceBean tmp_dev = null;
				int[] ids = devs.getIds();
				response.append("<select onclick=\"reload_actions(0);\" id=\"dev\" name='sections' size='10' class='standardTxt' style='width:100%;' >");
				int device=0;
				for (int i=0;i<devs.size();i++){
					tmp_dev = devs.getDevice(ids[i]);
					response.append("<OPTION "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"' class='"+(i%2==0?"Row1":"Row2")+"' >"+tmp_dev.getDescription()+"</OPTION>\n");
				}
				response.append("</select>");
				response.append("]]>");	
			break;
			case 2: // LOAD_DEVICE_MDL
				response=new StringBuilder();
				response.append("<response>");
				response.append("<variable>");
				response.append("<![CDATA[");
				VarMdlBeanList varMdlBeanList = new VarMdlBeanList();
				VarMdlBean[] varMdlBeans ;
				
				varMdlBeans= varMdlBeanList.retrieveOrderedIfDevIsPresent(us.getLanguage(),us.getIdSite(),idDevice,isHaccp,interval);
				
				response.append("<select ondblclick=\"addDevVar();\" name='sections' multiple size='10' id='var' class='standardTxt' style='width: 100%;'>");
				for (int i=0;i<varMdlBeans.length;i++)
					{
						response.append("<option value=\"");
						//response.append(String.valueOf(varMdlBeans[i].getIddevmdl()));
						//response.append("-");
						response.append(String.valueOf(varMdlBeans[i].getIdvarmdl()));
						response.append("\"");
						response.append(" class='"+(i%2==0?"Row1":"Row2")+"'>");
						response.append(varMdlBeans[i].getDescription());
						response.append("</option>");
					}
				response.append("</select>");
				response.append("]]>");	
				response.append("</variable>");
				response.append("<device>");
				response.append("<![CDATA[");
				
				devs = new DeviceListBean(us.getIdSite(),us.getLanguage(),idDevice,1);
				tmp_dev = null;
				ids = devs.getIds();
				StringBuffer div_dev = new StringBuffer();
				div_dev.append("<select multiple id=\"dev\" name='sections' size='10' class='standardTxt' style='width: 100%;' >");
				device=0;
				for (int i=0;i<devs.size();i++){
					tmp_dev = devs.getDevice(ids[i]);
					div_dev.append("<OPTION "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"' class='"+(i%2==0?"Row1":"Row2")+"'>"+tmp_dev.getDescription()+"</OPTION>\n");
				}
				div_dev.append("</select>");
				response.append(div_dev.toString());
				response.append("]]>");	
				response.append("</device>");
			break;	
			case 3: // RESOLVE_ROWS_FROM_IDVARMDL
				
				boolean haccp = isHaccp.equalsIgnoreCase("TRUE")?true:false;
				response.append("<![CDATA[");
				response.append(VarphyBeanList.getIdsVarsHistOrHaccp((String) prop.get("variables"),haccp,interval));
				response.append("]]>");	
			break;	
			case 4: // LOAD_REPORT_TO_MODIFY
				ReportBean reportBean=ReportBeanList.retrieveReportById(us.getIdSite(), Integer.parseInt((String) prop.get("idreport")));
				
				// Alessandro : check if idreport is one of the idreports scheduled by some action
				Map<Integer, ReportBean> report = ReportBeanList.retrieveReportsLinkedToScheduledAction(us.getIdSite());
				Integer idReport = Integer.parseInt((String) prop.get("idreport"));

				Iterator<Integer> iter = report.keySet().iterator();
				Integer key = null;
				boolean repFound = false;
				while (iter.hasNext())
				{
					key = (Integer) iter.next();
					if (idReport.intValue()==key.intValue()){
						repFound = true; // exists one report scheduled by an action
						break;
					}
				}				
				
				response.append("<name>");
				response.append("<![CDATA[");
				response.append(reportBean.getCode());
				response.append("]]>");	
				response.append("</name>");
				
				response.append("<layout>");
				response.append("<![CDATA[");
				response.append(reportBean.getTemplatefile());
				response.append("]]>");	
				response.append("</layout>");
				
				response.append("<timelength>");
				response.append("<![CDATA[");
				response.append(reportBean.getTimelength());
				response.append("]]>");	
				response.append("</timelength>");
				
				// Alessandro : tag added for tracking if idreport is scheduled or not
				response.append("<scheduled>");
				response.append("<![CDATA[");
				response.append(repFound);
				response.append("]]>");	
				response.append("</scheduled>");				
				
				response.append("<step>");
				response.append("<![CDATA[");
				response.append(reportBean.getStep());
				response.append("]]>");	
				response.append("</step>");
				
				response.append("<haccp>");
				response.append("<![CDATA[");
				response.append(reportBean.getHaccp());
				response.append("]]>");	
				response.append("</haccp>");
				
				response.append("<outputtype>");
				response.append("<![CDATA[");
				response.append(reportBean.getOutputtype());
				response.append("]]>");	
				response.append("</outputtype>");

				// variables
				response.append("<table>");
				response.append("<![CDATA[");
				
				Integer []variables=reportBean.getVariables();
				String sql= "select cfvariable.iddevice,cfvariable.idvariable, b.description,a.description as description1 " +
							"from cfvariable, cftableext as a, cftableext as b "+
							"where cfvariable.idvariable=a.tableid "+
							"and a.tablename='cfvariable' "+
							"and a.languagecode='"+us.getLanguage()+"' "+
							"and cfvariable.iddevice=b.tableid "+
							"and b.tablename='cfdevice'  "+
							"and b.languagecode='"+us.getLanguage()+"' "+
							"and cfvariable.idvariable= ? "+
							"and cfvariable.iscancelled='FALSE' "+
							"and cfvariable.idsite="+us.getIdSite()+" and a.idsite="+us.getIdSite()+" and b.idsite="+us.getIdSite()+"";
				RecordSet rs=null;
				for(int i=0;i<variables.length;i++){
				
					rs=DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{variables[i]});
					
					String myIdVar=rs.get(0).get(0)+"-"+rs.get(0).get(1);
					
					response.append("<tr id='");
					response.append(myIdVar);
					response.append("' class='tdGlobalVarsNoColor "+(i%2==0?"Row1":"Row2")+"'>"); 
					response.append("<td style='display:none'>");
					response.append(myIdVar);
					response.append("</td>");
					response.append("<td><div style='width:250px'>");
					response.append(rs.get(0).get(2));
					response.append("</div></td>");
					response.append("<td><div style='width:580px'>");
					response.append(rs.get(0).get(3));
					response.append("</div></td>");
					response.append("<td align='center'><div style='width:70px'><img src=\"images/actions/removesmall_on_black.png\" style=\"cursor:pointer;\" onclick=\"deleteRow('table_global_vars','");
					response.append(myIdVar);
					response.append("')\"/></div><td>");
					response.append("</tr>");
				
				}
				response.append("]]>");	
				response.append("</table>");
				
				// time values
				Integer timeValues[] = reportBean.getTimeValues();
				response.append("<timeTableValue>");
				response.append("<![CDATA[");
				for(int i=0; i < timeValues.length; i++) {
					if( i > 0 )
						response.append(";");
					response.append(timeValues[i].toString());
				}
				response.append("]]>");	
				response.append("</timeTableValue>");

				break;	
		}
		
		response.append("</response>");
		return  response.toString();// super.executeDataAction(us, tabName, prop);
	}
    
    
}
