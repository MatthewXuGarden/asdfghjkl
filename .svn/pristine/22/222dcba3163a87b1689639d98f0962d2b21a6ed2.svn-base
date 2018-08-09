package com.carel.supervisor.presentation.bo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.plugin.energy.*;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.VarMdlBean;
import com.carel.supervisor.presentation.bean.VarMdlBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.vscheduler.VSBase;
import com.carel.supervisor.presentation.vscheduler.VSCategory;
import com.carel.supervisor.presentation.vscheduler.VSGroup;

public class BEnergy extends BoMaster {

	public BEnergy(String lang) {
		super(lang);
	}

	private static final long serialVersionUID = -3103595264902203797L;
	private static final String START = "start";
	private static final String STOP = "stop";

	@Override
	protected Properties initializeEventOnLoad() {
		Properties p = new Properties();
		p.put("tab1name", "onLoadDashboard();");
		p.put("tab2name", "onLoadGraph();");
		p.put("tab3name", "initEnergyReport();");
		p.put("tab4name", "onLoadGroups();");
		p.put("tab5name", "onLoadModels();");
		p.put("tab6name", "onLoadProfile();");
		p.put("tab7name", "onLoadConsumerDevices();");
		return p;
	}

	@Override
	protected Properties initializeJsOnLoad() {
		
    	String virtkey = "";
        //determino se � abilitata la VirtualKeyboard:
        if (VirtualKeyboard.getInstance().isOnScreenKey())
        {
        	virtkey = "keyboard.js;";
        }	
        
		Properties p = new Properties();
		p.put("tab1name", "energy.js;energydashboard.js;");
		p.put("tab2name", "energy.js;energygraph.js;../jquery/jquery-1.11.1.min.js;");
		p.put("tab3name", "energy.js;energyexport.js;energycalendar.js;../arch/FileDialog.js;" + virtkey);
		p.put("tab4name", virtkey+"dbllistbox.js;energy.js;energygroup.js;");
		p.put("tab5name", virtkey+"energy.js;energymodel.js;");
		p.put("tab6name", virtkey+"../arch/FileDialog.js;calendar.js;energyprofile.js;");
		p.put("tab7name", virtkey+"dbllistbox.js;energydevice.js;");
		return p;
	}
	
	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab3name", DOCTYPE_STRICT);
		p.put("tab6name", DOCTYPE_STRICT);
		return p;
    }

	
	public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception {
		if( tabName.equalsIgnoreCase("tab1name") )
			executeTab1Post(us, prop);
		else if( tabName.equalsIgnoreCase("tab4name") )
			executeTab4Post(us, prop);
		else if( tabName.equalsIgnoreCase("tab5name") )
			executeTab5Post(us, prop);
		else if( tabName.equalsIgnoreCase("tab6name") )
			executeTab6Post(us, prop);
		else if( tabName.equalsIgnoreCase("tab7name") )
			executeTab7Post(us, prop);		
	}


	public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception {
		if( tabName.equals("tab1name") )
			return executeTab1Data(us, prop);
		if( tabName.equals("tab2name") )
			return executeTab2Data(us, prop);
		else if( tabName.equals("tab3name") )
			return executeTab3Data(us, prop);
		else if( tabName.equals("tab4name") )
			return executeTab4Data(us, prop);
		else if( tabName.equals("tab5name") )
			return executeTab5Data(us, prop);
		else
			return "";
	}
	

	// energy dashboard
	public void executeTab1Post(UserSession us, Properties prop) throws Exception
	{
		String cmd = prop.getProperty("cmd");
		if (START.equalsIgnoreCase(cmd)) {
			EventMgr.getInstance().info(1, us.getUserName(), "energy", "EG11", new Object[] {});
			DatabaseMgr.getInstance().executeStatement(null, "update energysite set value = 'true' where key='start';",null);
			DatabaseMgr.getInstance().executeStatement(null, "update energysite set value = 'true' where key='startactive';",null);
			DatabaseMgr.getInstance().executeStatement(null, "update energysite set value = 'true' where key='startengine';",null);
			EnergyMgr.getInstance().loadProperties();
			// test; rehook module to activate the changes
			EnergyModuleHook hook = new EnergyModuleHook();
			hook.hookModule(false);
			hook.hookModule(true);
		}
		else if (STOP.equalsIgnoreCase(cmd)) {
			EventMgr.getInstance().info(1, us.getUserName(), "energy", "EG12", new Object[] {});
			DatabaseMgr.getInstance().executeStatement(null, "update energysite set value = 'false' where key='start';",null);
			DatabaseMgr.getInstance().executeStatement(null, "update energysite set value = 'false' where key='startactive';",null);
			DatabaseMgr.getInstance().executeStatement(null, "update energysite set value = 'false' where key='startengine';",null);
			EnergyMgr.getInstance().loadProperties();
			// test; rehook module to activate the changes
			EnergyModuleHook hook = new EnergyModuleHook();
			hook.hookModule(false);
			hook.hookModule(true);
		}
	}
	// energy dashboard
	public String executeTab1Data(UserSession us, Properties prop) throws Exception
	{
		String toReturn = "<MSG>OK</MSG>";
		if ("resetenergy".equalsIgnoreCase(prop.getProperty("cmd")))
		{
			try
			{
				String variableIds = prop.getProperty("variableIds");
				if(variableIds.endsWith(";")){
					variableIds = variableIds.substring(0,variableIds.length()-1);
				}
				String meter[] = variableIds.split(";");
				String sql = "select * from energyconsumer where idgroup = ? and idconsumer = ?";
				variableIds = "";
				String meterNames="";
				String meterName="";
				for (int i = 0; i < meter.length; i++) {
					String group[] = meter[i].split(",");
					Object[] params = new Object[2];
					//reset site meter
					if(group.length == 2 && "0".equals(group[0]))
					{
						params[0] = -1;
						params[1] = -1;
					}
					else
					{
						params[0] = Math.abs(Integer.valueOf(group[0]));
						params[1] = group[1];
					}
					RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
					if(rs.size()>0){
						Integer idkwh = (Integer)rs.get(0).get("idkwh");
						if(!variableIds.contains(idkwh.toString())){
							variableIds = variableIds + idkwh+ ",";
						}
						meterName = (String)rs.get(0).get("name");
						if(!meterNames.contains(meterName)){
							meterNames += "" + rs.get(0).get("name") + ", ";
						}
					}
				}
				if(meterNames.endsWith(", ")){
					meterNames = meterNames .substring(0,meterNames.length()-2);
				}
				if(meterNames.length()>255){
					meterNames = meterNames.substring(0,252)+"...";
				}
				if(variableIds.endsWith(",")){
					variableIds = variableIds .substring(0,variableIds.length()-1);
				}
				boolean isRunning = false;
				if (EnergyMgr.getInstance().getActiveRunning()) {
					EnergyMgr.getInstance().stopActive();
					isRunning = true;
				}
				if(EnergyMgr.getInstance().getRunning()) {
					EnergyMgr.getInstance().stopEngine();
					isRunning = true;
				}
				String sqlStrLogex = "delete from energylogex"+ " where idvariable in (" + variableIds + ")";
				String sqlStrLog = "delete from energylog"+ " where idvariable in (" + variableIds + ")";
				String sqlStrLogSupport = "delete from energylogsupport"+ " where idvariable in (" + variableIds + ")";
				DatabaseMgr.getInstance().executeStatement(null, sqlStrLogex,null);
				DatabaseMgr.getInstance().executeStatement(null, sqlStrLog,null);
				DatabaseMgr.getInstance().executeStatement(null, sqlStrLogSupport,null);
				String idskwh="";
				for (Iterator<EnergyConsumer> itr = EnergyMgr.getInstance().getSiteConfiguration().getConsumerList().iterator(); itr.hasNext();) {
					EnergyConsumer ec = itr.next();
					if(variableIds.contains(ec.getIdkwh().toString()) && !idskwh.contains(ec.getIdkwh().toString())){
						DatabaseMgr.getInstance().executeStatement(null,
								"insert into energylogsupport values (?,?,?,?,?);",
								new Object[] {
								ec.getIdkwh(),
								new Timestamp(System.currentTimeMillis()),
								new Timestamp(System.currentTimeMillis()),
								new Timestamp(System.currentTimeMillis()),
								new Timestamp(System.currentTimeMillis())},
							false);
						idskwh = idskwh + ec.getIdkwh().toString()+",";
					}
				}		
				if(DatabaseMgr.getInstance().executeQuery(null, "select distinct flags from energylogex",null).size()==1){
					DatabaseMgr.getInstance().executeStatement(null, "delete from energylogex",null);
				}
				if(isRunning){
					EnergyModuleHook hook = new EnergyModuleHook();
					hook.hookModule(false);
					hook.hookModule(true);
				}
				EventMgr.getInstance().info(1, us.getUserName(), "energy", "EG13", new Object[] {meterNames});
			} catch (Exception e)
			{
				toReturn = "<MSG>ERROR</MSG>";
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		return toReturn;
	}
	
	// energy graph
	public String executeTab2Data(UserSession us, Properties prop) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		if("changeYear".equals(prop.getProperty("cmd"))){
			String language = us.getLanguage();
			LangService lan = LangMgr.getInstance().getLangService(language);
			String[] astrMonthNames = {
					lan.getString("cal", "january"),
					lan.getString("cal", "february"),
					lan.getString("cal", "march"),
					lan.getString("cal", "april"),
					lan.getString("cal", "may"),
					lan.getString("cal", "june"),
					lan.getString("cal", "july"),
					lan.getString("cal", "august"),
					lan.getString("cal", "september"),
					lan.getString("cal", "october"),
					lan.getString("cal", "november"),
					lan.getString("cal", "december")	
				};
			int year=Integer.parseInt(prop.getProperty("year"));
			int curWeek = Integer.parseInt(prop.getProperty("curWeek"));
			sb.append("<response>");
			sb.append("<CalendarOption><![CDATA[");
			for(int iWeek = 1; iWeek <= 52; iWeek++) {
				Calendar cal = Calendar.getInstance();  
				cal.set(Calendar.YEAR, year); 
		        cal.setMinimalDaysInFirstWeek(4);
				cal.setFirstDayOfWeek(Calendar.MONDAY);
				cal.set(Calendar.WEEK_OF_YEAR, iWeek);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				String strWeek = cal.get(Calendar.DAY_OF_MONTH)
						+ " " + astrMonthNames[cal.get(Calendar.MONTH)];
				cal.add(Calendar.DAY_OF_YEAR, 6);
				strWeek += " - " + cal.get(Calendar.DAY_OF_MONTH)
					+ " " + astrMonthNames[cal.get(Calendar.MONTH)];
				String strselect = iWeek==curWeek?" selected":"";
				sb.append("<option value=\""+iWeek+"\""+ strselect +">"+strWeek+"</option>");
			}
			sb.append("]]></CalendarOption>");
 			sb.append("</response>");
 			return sb.toString();
		}
		String root = prop.getProperty("root");
		String type = prop.getProperty("reporttype");
		Integer startparam = new Integer(prop.getProperty("startparam"));
		Integer inttype = -1;
		Integer specificYear = null;
		if(prop.getProperty("specificYear")!=null &&prop.getProperty("specificYear")!=""){
			specificYear= new Integer(prop.getProperty("specificYear"));
		}
		
		EnergyReport report = null;
		String[] astrNames = null;
		LangService lang = LangMgr.getInstance().getLangService(us.getLanguage());
		
		// set type code
		if("daily".equalsIgnoreCase(type)){
			inttype = 0;
		} else if("weekly".equalsIgnoreCase(type)){
			inttype = 1;
			astrNames = new String[] { "",
				lang.getString("cal", "monday"),
				lang.getString("cal", "tuesday"),
				lang.getString("cal", "wednesday"),
				lang.getString("cal", "thursday"),
				lang.getString("cal", "friday"),
				lang.getString("cal", "saturday"),
				lang.getString("cal", "sunday")
			};
		} else if ("monthly".equalsIgnoreCase(type)){
			inttype = 2;
			astrNames = new String[32];
			for(int i = 0; i < astrNames.length; i++)
				astrNames[i] = String.valueOf(i);
		} else if("yearly".equalsIgnoreCase(type)){
			inttype = 3;
			astrNames = new String[] { "",
				lang.getString("cal", "january"),
				lang.getString("cal", "february"),
				lang.getString("cal", "march"),
				lang.getString("cal", "april"),
				lang.getString("cal", "may"),
				lang.getString("cal", "june"),
				lang.getString("cal", "july"),
				lang.getString("cal", "august"),
				lang.getString("cal", "september"),
				lang.getString("cal", "october"),
				lang.getString("cal", "november"),
				lang.getString("cal", "december")	
			};
		} else if("custom".equalsIgnoreCase(type)){
			inttype = 4;
		} 
		
		if(root.equalsIgnoreCase("site")){
			switch (inttype) {
			case 0: //daily
				break;
			case 1: //weekly
				report = EnergyMgr.getInstance().getWeeklyReport(startparam,specificYear);
				break;
			case 2: //monthly
				report = EnergyMgr.getInstance().getMonthlyReport(startparam,specificYear);
				break;
			case 3: //yearly
				report = EnergyMgr.getInstance().getYearlyReport(startparam);
				break;
			default:
				break;
			}
		} else if(root.equalsIgnoreCase("group")){
			switch (inttype) {
			case 0: //daily
				break;
			case 1: //weekly
				report = EnergyMgr.getInstance().getGroupWeeklyReport(startparam, new Integer(prop.getProperty("group")),specificYear);
				break;
			case 2: //monthly
				report = EnergyMgr.getInstance().getGroupMonthlyReport(startparam, new Integer(prop.getProperty("group")),specificYear);
				break;
			case 3: //yearly
				report = EnergyMgr.getInstance().getGroupYearlyReport(startparam, new Integer(prop.getProperty("group")));
				break;
			default:
				break;
			}				
		} else if(root.equalsIgnoreCase("cons")) {
			switch (inttype) {
			case 0: //daily
				break;
			case 1: //weekly
				report = EnergyMgr.getInstance().getConsWeeklyReport(startparam, new Integer(prop.getProperty("group")), new Integer(prop.getProperty("cons")));
				break;
			case 2: //monthly
				report = EnergyMgr.getInstance().getConsMonthlyReport(startparam, new Integer(prop.getProperty("group")), new Integer(prop.getProperty("cons")));
				break;
			case 3: //yearly
				report = EnergyMgr.getInstance().getConsYearlyReport(startparam, new Integer(prop.getProperty("group")), new Integer(prop.getProperty("cons")));
				break;
			default:
				break;
			}				
		}
		DefaultPieDataset dataset = new DefaultPieDataset();
		DefaultPieDataset datasetpieB = new DefaultPieDataset();
		DefaultCategoryDataset  datasetbarA = new DefaultCategoryDataset();
		DefaultCategoryDataset  datasetbarB = new DefaultCategoryDataset();
		DefaultCategoryDataset  datasetbarC = new DefaultCategoryDataset();
		DefaultCategoryDataset  datasetbarD = new DefaultCategoryDataset();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Long timestamp = System.currentTimeMillis();
		int startReplace = 0;
		sb.append("<energyreport>");
		sb.append("<reportinfo>");
		sb.append("<rootparam>"+root+"</rootparam>");
		sb.append("<typeparam>"+type+"</typeparam>");
		sb.append("<startparam>"+startparam+"</startparam>");
		sb.append("<fromparam>" + sdf.format(report.getBegin()) + "</fromparam>");
		sb.append("<toparam>" + sdf.format(report.getEnd()) + "</toparam>");
		sb.append("<piechart>st2pie"+timestamp+"</piechart>");			
		sb.append("<piechart_ts>st2pieB"+timestamp+"</piechart_ts>");			
		sb.append("<barchartkw>st2barA"+timestamp+"</barchartkw>");			
		sb.append("<barchartkwh>st2barB"+timestamp+"</barchartkwh>");
		sb.append("<barchart_kwh_ts>st2barC"+timestamp+"</barchart_kwh_ts>");			
		sb.append("<barchart_ts_kwh>st2barD"+timestamp+"</barchart_ts_kwh>");
		if( astrNames != null ) {
			sb.append("<steps>");
			for(int i = 1; i < astrNames.length; i++)
				sb.append("<step id=\"" + i + "\" name=\"" + astrNames[i] + "\" />"); 
			sb.append("</steps>");
		}
		sb.append("</reportinfo>");
		try {
			//individuo i valori di un sito
			EnergyReportRecord record = null;
			if(root.equalsIgnoreCase("site"))
				record = report.getReportRecord("site");
			else if(root.equalsIgnoreCase("group") || root.equalsIgnoreCase("cons"))
				record = report.getReportRecord("group"+prop.getProperty("group"));
			if(record!=null){
				sb.append("<site id=\"root\">"); //pu� voler dire anche group
				sb.append("<name><![CDATA["+record.getName()+"]]></name>");
				sb.append("<id>"+record.getId()+"</id>");
				sb.append("<cost>"+
						(record.getCost().equals(Float.NaN) ? "***" : 
							EGUtils.formatcost(record.getCost())+" ").toString()+"</cost>");
				sb.append("<kgco2>"+
						((record.getKgco2().equals(Float.NaN)) ? "***"
								: EGUtils.formatkgco2(record.getKgco2())).toString()+"</kgco2>");
				sb.append("<kw>"+
						((record.getKw().equals(Float.NaN)) ? "***"
								: EGUtils.formatkw(record.getKw())).toString()+"</kw>");
				sb.append("<kwh>"+
						((record.getKwh().equals(Float.NaN)) ? "***"
								: EGUtils.formatkwh(record.getKwh())).toString()+"</kwh>");
				
				// time slot related data for the entire site/selection
				sb.append("<timeslots>");
				double anSiteKWh[] = record.getKWhTS();
				double anSiteCost[] = record.getCostTS();
				double anSiteKWhP[] = record.getKWhTSinPercent();
				double anSiteCostP[] = record.getCostTSinPercent();
				if( anSiteKWh != null && anSiteCost != null ) {
					for(int iTS = 0; iTS <= EnergyProfile.TIMESLOT_NO; iTS++) {
						sb.append("<ts i=\"" + iTS + "\" kwh=\"" + EGUtils.formatkwh(anSiteKWh[iTS]) + "\" cost=\"" + EGUtils.formatcost(anSiteCost[iTS]) + "\"");
						if( iTS != EnergyProfile.TIMESLOT_NO ) {
							sb.append(" kwh_p=\"" + EGUtils.formatperc(anSiteKWhP[iTS]) + "\" cost_p=\"" + EGUtils.formatperc(anSiteCostP[iTS]) + "\"");
							datasetpieB.setValue(EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(iTS).getName(), anSiteKWhP[iTS]);
							/* moved to T* details
							datasetbarC.addValue(anSiteKWh[iTS], record.getName(),
								EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(iTS).getName());
							*/
						}
						sb.append("/>");							
					}
				}
				sb.append("</timeslots>");
				startReplace = sb.length();
				sb.append("</steps>");
				sb.append("</site>");
			}
			EnergyReportRecord rootrecord = record;
			
			//individuo i gruppi/utenze livello 1
			String level1="";
			Integer nItems = 0;
			if(root.equalsIgnoreCase("site")){
				level1 = "group";
				nItems = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10);
			} else if (root.startsWith("group")){
				level1="cons";
				nItems = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
			} else if(root.startsWith("cons")){
				//root="group";
				level1="cons";
				nItems = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
			}
			if(level1==null||level1.equalsIgnoreCase("")){
				throw new Exception("no level1");
			}
			
			// init datasetbarD
			for(Integer iDay = 1; iDay <= report.getIntervalsNumber(); iDay++)
				for(int iTS = 0; iTS < EnergyProfile.TIMESLOT_NO; iTS++)
					datasetbarD.addValue(0, EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(iTS).getName(), astrNames[iDay]);
			
			for (int i = 1; i <= nItems; i++) {
				if( root.equalsIgnoreCase("site") )
					record = report.getReportRecord("group" + i);
				else
					record = report.getReportRecord("cons" + prop.getProperty("group") + "." + i);
				if( record == null )
					continue;
				//dataset.setValue(record.getName(), record.getKwh());
				Float datasetvalue = record.getKwh()/rootrecord.getKwh()*100;
				dataset.setValue(record.getName(), datasetvalue);

				sb.append("<group id=\""+level1+Math.abs(record.getId())+"\">"); //"+level1+" pu� voler dire anche utenza
				sb.append("<name><![CDATA["+record.getName()+"]]></name>");
				sb.append("<per>"+
						(new Float(record.getKwh()/rootrecord.getKwh()).isNaN() ? "***" : 
							EGUtils.formatperc(record.getKwh()/rootrecord.getKwh() * 100)+" ")
							.toString()+"</per>");
				sb.append("<kw>"+
						((record.getKw().equals(Float.NaN)) ? "***"
								: EGUtils.formatkw(record.getKw())).toString()+"</kw>");
				sb.append("<kwh>"+
						((record.getKwh().equals(Float.NaN)) ? "***"
								: EGUtils.formatkwh(record.getKwh())).toString()+"</kwh>");
				sb.append("<cost>"+
						(record.getCost().equals(Float.NaN) ? "***" : 
							EGUtils.formatcost(record.getCost())+" ").toString()+"</cost>");
				sb.append("<kgco2>"+
						((record.getKgco2().equals(Float.NaN)) ? "***"
								: EGUtils.formatkgco2(record.getKgco2())).toString()+"</kgco2>");
				// time slot related data for the entire period
				sb.append("<timeslots>");
				double anTotalKWh[] = record.getKWhTS();
				double anTotalCost[] = record.getCostTS();
				if( anTotalKWh != null && anTotalCost != null ) {
					for(int iTS = 0; iTS <= EnergyProfile.TIMESLOT_NO; iTS++) {
						sb.append("<ts i=\"" + iTS + "\" kwh=\"" + EGUtils.formatkwh(anTotalKWh[iTS]) + "\" cost=\"" + EGUtils.formatcost(anTotalCost[iTS]) + "\"/>");
						
						// T* details, init datasetbarC
						if( iTS != EnergyProfile.TIMESLOT_NO )
							datasetbarC.addValue(anTotalKWh[iTS], record.getName(),
								EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(iTS).getName());
					}
				}
				sb.append("</timeslots>");
				
				//individuo gli step
				Integer numcols = report.getIntervalsNumber();
				for (Integer iday = 1; iday <= numcols ; iday++) {
					String dayName ="";
					String level="";
					if( root.equalsIgnoreCase("site") )
						level = "group";
					else
						level = "cons" +prop.getProperty("group") + ".";
					dayName= "d"+(iday)+"."+level+i;
					record = report.getReportRecord(dayName);
					if (record == null) continue;
					if(!record.getKw().equals(Float.NaN))
						datasetbarA.addValue(record.getKw(), record.getName(), astrNames[iday]);
					else
						datasetbarA.addValue(0f, record.getName(), astrNames[iday]);
					datasetbarB.addValue(record.getKwh(), record.getName(), astrNames[iday]);
					
					sb.append("<step id=\""+iday+"\">");
					sb.append("<kw>"+
							((record.getKw().equals(Float.NaN)) ? "***"
									: EGUtils.formatkw(record.getKw())).toString()+
									/*+EGUtils.formatkw(record.getKw()+*/"</kw>");
					sb.append("<kwh>"+
							((record.getKwh().equals(Float.NaN)) ? "***"
									: EGUtils.formatkwh(record.getKwh())).toString()+
									/*+EGUtils.formatkw(record.getKwh()+*/"</kwh>");
					// time slot related data
					sb.append("<timeslots>");
					double anKWh[] = record.getKWhTS();
					double anCost[] = record.getCostTS();
					if( anKWh != null && anCost != null ) {
						for(int iTS = 0; iTS <= EnergyProfile.TIMESLOT_NO; iTS++) {
							sb.append("<ts i=\"" + iTS + "\" kwh=\"" + EGUtils.formatkwh(anKWh[iTS]) + "\" cost=\"" + EGUtils.formatcost(anCost[iTS]) + "\"/>");
							if( iTS != EnergyProfile.TIMESLOT_NO ) {
								double nKWhStep = (Double)datasetbarD.getValue(iTS, iday.intValue()-1);
								if( Double.isNaN(nKWhStep) )
									nKWhStep = anKWh[iTS];
								else
									nKWhStep += anKWh[iTS];

								// T* details test
								/*
								Number nVal = datasetbarC.getValue(record.getName(), EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(iTS).getName());
								if( nVal == null )
									nVal = 0.0;
								datasetbarC.addValue(nVal.doubleValue() + anKWh[iTS], record.getName(),
									EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(iTS).getName());
								*/
								
								datasetbarD.addValue(nKWhStep, EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(iTS).getName(), astrNames[iday]);
							}
						}
					}
					sb.append("</timeslots>");
					sb.append("</step>");
				}
				sb.append("</group>");//sb.append("</"+level1+">");
			}
			
			//seleziono il gruppo "altro"
			record = report.getReportRecord("other");
			if(record!=null){
				sb.append("<other id=\"other\">");
				sb.append("<name><![CDATA["+record.getName()+"]]></name>");
				sb.append("<id>"+record.getId()+"</id>");
				sb.append("<cost>"+
						(record.getCost().equals(Float.NaN) ? "***" : 
							EGUtils.formatcost(record.getCost())+" ").toString()+"</cost>");
				sb.append("<kgco2>"+
						((record.getKgco2().equals(Float.NaN)) ? "***"
								: EGUtils.formatkgco2(record.getKgco2())).toString()+"</kgco2>");
				sb.append("<kw>"+
						((record.getKw().equals(Float.NaN)) ? "***"
								: EGUtils.formatkw(record.getKw())).toString()+"</kw>");
				sb.append("<kwh>"+
						((record.getKwh().equals(Float.NaN)) ? "***"
								: EGUtils.formatkwh(record.getKwh())).toString()+"</kwh>");
				sb.append("<per>"+
						(new Float(record.getKwh()/rootrecord.getKwh()).isNaN() ? "***" : 
							EGUtils.formatperc(record.getKwh()/rootrecord.getKwh() * 100)+" ")
							.toString()+"</per>");
				dataset.setValue(lang.getString("energy", "other"), record.getKwh()/rootrecord.getKwh()*100);
				
				// time slot related data for other
				sb.append("<timeslots>");
				double anOtherKWh[] = record.getKWhTS();
				double anOtherCost[] = record.getCostTS();
				if( anOtherKWh != null && anOtherCost != null ) {
					for(int iTS = 0; iTS <= EnergyProfile.TIMESLOT_NO; iTS++) {
						sb.append("<ts i=\"" + iTS + "\" kwh=\"" + EGUtils.formatkwh(anOtherKWh[iTS]) + "\" cost=\"" + EGUtils.formatcost(anOtherCost[iTS]) + "\"/>");
						// T* details
						if( iTS != EnergyProfile.TIMESLOT_NO ) {
							datasetbarC.addValue(anOtherKWh[iTS], lang.getString("energy", "other"),
								EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(iTS).getName());
						}
					}
				}
				sb.append("</timeslots>");
				
				//individuo gli step
				Integer numcols = report.getIntervalsNumber();
				for (Integer iday = 1; iday <= numcols ; iday++) {
					String dayName ="";
					dayName= "d"+(iday)+".other";
					record = report.getReportRecord(dayName);
					if (record == null) continue;
					if(!record.getKw().equals(Float.NaN))
						datasetbarA.addValue(record.getKw(), lang.getString("energy", "other"),	astrNames[iday]);
					else
						datasetbarA.addValue(0f, lang.getString("energy", "other"), astrNames[iday]);
					datasetbarB.addValue(record.getKwh(), lang.getString("energy", "other"), astrNames[iday]);
					sb.append("<step id=\""+iday+"\">");
					sb.append("<kw>"+
							((record.getKw().equals(Float.NaN)) ? "***"
									: EGUtils.formatkw(record.getKw())).toString()+"</kw>");
					sb.append("<kwh>"+
							((record.getKwh().equals(Float.NaN)) ? "***"
									: EGUtils.formatkwh(record.getKwh())).toString()+"</kwh>");
					
					// time slot related data
					sb.append("<timeslots>");
					double anKWh[] = record.getKWhTS();
					double anCost[] = record.getCostTS();
					if( anKWh != null && anCost != null ) {
						for(int iTS = 0; iTS <= EnergyProfile.TIMESLOT_NO; iTS++) {
							sb.append("<ts i=\"" + iTS + "\" kwh=\"" + EGUtils.formatkwh(anKWh[iTS]) + "\" cost=\"" + EGUtils.formatcost(anCost[iTS]) + "\"/>");
							if( iTS != EnergyProfile.TIMESLOT_NO ) {
								double nKWhStep = (Double)datasetbarD.getValue(iTS, iday.intValue()-1);
								if( Double.isNaN(nKWhStep) )
									nKWhStep = anKWh[iTS];
								else
									nKWhStep += anKWh[iTS];
								datasetbarD.addValue(nKWhStep, EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(iTS).getName(), astrNames[iday]);
							}
						}
					}
					sb.append("</timeslots>");
					sb.append("</step>");
				}
				sb.append("</other>");
			}
			sb.append("</energyreport>");
			
			// step details for root node
			StringBuffer sb1 = new StringBuffer();
			for(Integer iday = 1; iday <= report.getIntervalsNumber() ; iday++) {
				sb1.append("<step id=\""+iday+"\">");
				sb1.append("<timeslots>");
				for(int iTS = 0; iTS < EnergyProfile.TIMESLOT_NO; iTS++) {
					double nKWhStep = (Double)datasetbarD.getValue(iTS, iday.intValue()-1);
					sb1.append("<ts i=\"" + iTS + "\" kwh=\"" + EGUtils.formatkwh(nKWhStep) + "\"/>");
				}
				sb1.append("</timeslots>");
				sb1.append("</step>");
			}
			sb.replace(startReplace, startReplace + "</steps>".length(), sb1.toString());
			
			us.getCurrentUserTransaction().setAttribute("st2pie"+timestamp, dataset);
			us.getCurrentUserTransaction().setAttribute("st2pieB"+timestamp, datasetpieB);
			us.getCurrentUserTransaction().setAttribute("st2barA"+timestamp, datasetbarA);
			us.getCurrentUserTransaction().setAttribute("st2barB"+timestamp, datasetbarB);
			us.getCurrentUserTransaction().setAttribute("st2barC"+timestamp, datasetbarC);
			us.getCurrentUserTransaction().setAttribute("st2barD"+timestamp, datasetbarD);

			// ** header **
			if(root.equalsIgnoreCase("site")){
				us.getCurrentUserTransaction().setAttribute("root","site");
			} else if (root.startsWith("group")){
				us.getCurrentUserTransaction().setAttribute("root","group");
				us.getCurrentUserTransaction().setAttribute("group",prop.getProperty("group"));
			} else if(root.startsWith("cons")){
				us.getCurrentUserTransaction().setAttribute("root","cons");
			}
			us.getCurrentUserTransaction().setAttribute("reporttype", report.getType());
			us.getCurrentUserTransaction().setAttribute("from", report.getBegin().toString());
			us.getCurrentUserTransaction().setAttribute("to", report.getEnd().toString());
			us.getCurrentUserTransaction().setAttribute("step", report.getStep());
			// ** header **

			// pdf report
			us.getCurrentUserTransaction().setAttribute("report", report);
			us.getCurrentUserTransaction().setAttribute("timevalue", timestamp);
			us.getCurrentUserTransaction().setAttribute("igroup", new Integer(prop.getProperty("group")));
			us.getCurrentUserTransaction().setAttribute("icons", new Integer(prop.getProperty("cons")));
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return "<err>Error</err>";
		}
		return sb.toString();
	}

	
	// energy export
	public String executeTab3Data(UserSession us, Properties prop) throws Exception
	{
//			for(Iterator<Entry<Object, Object>> itr=prop.entrySet().iterator();itr.hasNext();) {
//				Entry<Object, Object> e = itr.next();
//				System.out.println("exp-       "+e.getKey()+"   "+e.getValue());
//			}
//			System.out.println();
		String result = "<response>";
		BufferedWriter fos = null;
		try
		{
			String filename = "Energy_"+DateUtils.date2String(new Date(), "yyyyMMddhhmmss")+".csv";
			
			String file = "";
			String local = prop.getProperty("local");
			if("true".equalsIgnoreCase(local))
			{
				String path = prop.getProperty("path");
				file = path;
			}
			else
			{
				file = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator +filename;
			}
			File f = new File(file);
			LangService lan = LangMgr.getInstance().getLangService(us.getLanguage());
			fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), Charset.forName("UTF-8")));
			DateFormat dfin = new SimpleDateFormat("dd/MM/yyyy");
			EnergyMgr emgr = EnergyMgr.getInstance();
			EnergyConfiguration econf = emgr.getSiteConfiguration();
			Timestamp begin = new Timestamp(dfin.parse(prop.getProperty("energyfrom")).getTime());
			GregorianCalendar gcend = new GregorianCalendar();
			gcend.setTime(dfin.parse(prop.getProperty("energyto")));
			gcend.add(Calendar.DAY_OF_YEAR, 1);
			Timestamp end = new Timestamp(gcend.getTimeInMillis());
			Integer step = new Integer(prop.getProperty("sceltaGranularita"));
			EnergyReport report = null;
			if("site".equalsIgnoreCase(prop.getProperty("exporttype"))){
				report = emgr.getReport(begin, end, EnergyReport.CUSTOM, step);
			} else if("group".equalsIgnoreCase(prop.getProperty("exporttype"))){
				report = emgr.getGroupReport(new Integer(prop.getProperty("group")), begin, end, EnergyReport.CUSTOM, step);
			}
			if(report!=null){					
				fos.write(lan.getString("energy", "repuser"));
				fos.write(";");
				fos.write(us.getUserName());
				fos.write(";\n");
				fos.write(lan.getString("energy", "repsite"));
				fos.write(";");
				fos.write(us.getSiteName());
				fos.write(";\n");
				fos.write(";\n");
				fos.write(lan.getString("energy", "repenerconfig"));
				fos.write(";\n");
				fos.write(lan.getString("energy", "repcost"));
				fos.write(";");
				if( emgr.getStringProperty("active_cfg").equals("time_slot") ) {
					EnergyProfile ep = emgr.getEnergyProfile();
					for(int i = 0; i < EnergyProfile.TIMESLOT_NO; i++) {
						fos.write(EGUtils.formatcost(ep.getCost(i)));
						fos.write(";");
					}
					fos.write("\n");
				}
				else {
					fos.write(econf.getSiteProperty("cost"));
					fos.write(";\n");
				}
				fos.write(lan.getString("energy", "enerconfigurationcurrency"));
				fos.write(";");
				fos.write(econf.getSiteProperty("currency"));
				fos.write(";\n");
				fos.write(lan.getString("energy", "repco2"));
				fos.write(";");
				fos.write(econf.getSiteProperty("kgco2"));
				fos.write(";\n");
				fos.write(";\n");
				fos.write(lan.getString("energy", "reptype"));
				fos.write(";");
				if(EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))){
					fos.write(lan.getString("energy", "enersite"));
				} else {
					fos.write(lan.getString("energy", "energroup"));
					fos.write(";");
					fos.write(prop.getProperty("group"));
				}
				fos.write(";\n");
				fos.write(lan.getString("energy", "enerfrom"));
				fos.write(";");
				fos.write(begin.toString());
				fos.write(";\n");
				fos.write(lan.getString("energy", "enerto"));
				fos.write(";");
				fos.write(end.toString());
				fos.write(";\n");
				fos.write(lan.getString("energy", "enerdataexportstep"));
				fos.write(";");
				fos.write(lan.getString("energy", "report"+step));
				fos.write(";\n");
				fos.write(";\n");
				
				writeRootReport(fos, report, lan, prop );
				writeLevel1Report(fos, report, lan, prop);
				writeTimeDetailReport(begin,fos, report, lan, prop);
			}
			if( "coldrental".equalsIgnoreCase(prop.getProperty("exporttype")) ) {
				String strConsVar = prop.getProperty("consvar");
				Integer idConsVar = strConsVar.isEmpty() ? 0 : Integer.parseInt(strConsVar);
				EnergyDevice.writeConsumerReport(us, idConsVar, begin, end, fos);
			}			
			
	        fos.close();
	        result += "<file><![CDATA["+file+"]]></file></response>";
		}
		catch(Exception e)
		{
			try {
				fos.close();
			} catch (Exception e2) {
			}
			result += "<file><![CDATA[ERROR]]></file></response>";
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		return result;
	}

	private void writeTimeDetailReport(Timestamp begin,BufferedWriter fos, EnergyReport report, LangService lan, Properties prop)
	throws Exception {
	EnergyReportRecord record = null;
	//Float rootkw = 0f;
	//
	//if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
	//	rootkw=report.getReportRecord("site").getKw();
	//} else {
	//	rootkw=report.getReportRecord("group"+prop.getProperty("group")).getKw();
	//}
	
	// header
	fos.write(";\n");
	fos.write(";");
	boolean hasGroup = false;
	int num = EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))?
			EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10) :
				EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
	for (int i = 1; i <= num; i++) {
		if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
			record = report.getReportRecord("d1."+"group" + i);
		} else {
			record = report.getReportRecord("d1."+"cons"+prop.getProperty("group")+"."+ i);
		}
		if (record != null) {
			hasGroup = true;
			fos.write(record.getName());
			fos.write(";");
		}
	}
	if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
		record = report.getReportRecord("other");
		if (record != null) {
			if(!hasGroup)
				fos.write(lan.getString("energy", "enersite")+";");
			else
				fos.write(lan.getString("energy", "other")+";");
		}
	}
	fos.write(";\n");
	
	String s;
	Integer intervals = report.getIntervalsNumber();
	Calendar c = Calendar.getInstance();
	c.setTime(begin);
	for(int samples = 1;samples<=intervals;samples++){
		boolean bRecordOK = true;
		// % - titolo hour day month
		if(report.getStep().equals(EnergyReport.HOUR)){
			if(samples>1)
				c.add(Calendar.HOUR_OF_DAY, 1);
			fos.write((new SimpleDateFormat("yyyy-MM-dd HH:mm").format(c.getTime()))); //hour 0-23
			bRecordOK = false;
		}else if(report.getStep().equals(EnergyReport.DAY)){
			if(samples>1)
				c.add(Calendar.DATE, 1);
			fos.write((new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()))); //day 1 to intervals
		}else if(report.getStep().equals(EnergyReport.MONTH)){
			if(samples>1)
				c.add(Calendar.MONTH, 1);
			fos.write((new SimpleDateFormat("yyyy-MM").format(c.getTime()))); //month 1 to intervals
		}
		fos.write(";");
		for (int i = 1; i <= num; i++) {
			if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
				record = report.getReportRecord("d"+samples+".group" + i);
			} else {
				record = report.getReportRecord("d"+samples+".cons"+prop.getProperty("group")+"."+ i);
			}
			if (record != null) {
	//			s = (record.getKw().equals(Float.NaN) || rootkw.equals(Float.NaN)) ? 
	//					"***" : EGUtils.formatperc(record.getKw()/rootkw * 100)+"%";
	//			fos.write(s);
				fos.write(";");
			}
		}
		fos.write(";\n");
	
		//    kw
		fos.write(lan.getString("energy", "enerkw"));
		fos.write(";");
		for(int i = 1; i <= num;i++){
			if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
				record = report.getReportRecord("d"+samples+".group" + i);
			} else {
				record = report.getReportRecord("d"+samples+".cons"+prop.getProperty("group")+"."+ i);
			}
			if (record != null) {
				s = (record.getKw().equals(Float.NaN)) ? "***" : EGUtils.formatkw(record.getKw());
				fos.write(s);
				fos.write(";");
			}
		}
		if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
			record = report.getReportRecord("d"+samples+".other");
			if (record != null) {
				s = (record.getKw().equals(Float.NaN)) ? "***" : EGUtils.formatkw(record.getKw());
				fos.write(s);
				fos.write(";");
			}
		}
		fos.write(";\n");
	
		//    kwh
		fos.write(lan.getString("energy", "enerkwh"));
		fos.write(";");
		for(int i = 1; i<=EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10);i++){
			if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
				record = report.getReportRecord("d"+samples+".group" + i);
			} else {
				record = report.getReportRecord("d"+samples+".cons"+prop.getProperty("group")+"."+ i);
			}
			if (record != null) {
				s = (record.getKwh().equals(Float.NaN)) ? "***" : EGUtils.formatkwh(bRecordOK ? record.getKwh() : record.getKwhCsv());
				fos.write(s);
				fos.write(";");
			}
		}
		if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
			record = report.getReportRecord("d"+samples+".other");
			if (record != null) {
				s = (record.getKwh().equals(Float.NaN)) ? "***" : EGUtils.formatkwh(bRecordOK ? record.getKwh() : record.getKwhCsv());
				fos.write(s);
				fos.write(";");
			}
		}
		fos.write(";\n");
	}
	fos.write(";\n");
}

private void writeLevel1Report(BufferedWriter fos, EnergyReport report, LangService lan, Properties prop)
	throws Exception {
	EnergyReportRecord record = null;
	//String level = "";
	Float rootkw = 0f;
	if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
		rootkw=report.getReportRecord("site").getKw();
	} else {
		rootkw=report.getReportRecord("group"+prop.getProperty("group")).getKw();
	}
	
	// header
	fos.write(";\n");
	fos.write(";");
	int num = EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))?
			EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10) :
				EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
	boolean hasGroup = false;
	for (int i = 1; i <= num; i++) {
		if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
			record = report.getReportRecord("group" + i);
		} else {
			record = report.getReportRecord("cons"+prop.getProperty("group")+"."+ i);
		}
		if (record != null) {
			hasGroup = true;
			fos.write(record.getName());
			fos.write(";");
		}
	}
	if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
		record = report.getReportRecord("other");
		if (record != null) {
			if(!hasGroup)
				fos.write(lan.getString("energy", "enersite")+";");
			else
				fos.write(lan.getString("energy", "other")+";");
		}
	}
	fos.write(";\n");
	
	//%
	String s;
	fos.write(lan.getString("energy", "perc"));
	fos.write(";");
	for (int i = 1; i <= num; i++) {
		if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
			record = report.getReportRecord("group" + i);
		} else {
			record = report.getReportRecord("cons"+prop.getProperty("group")+"."+ i);
		}
		if (record != null) {
			s = (record.getKw().equals(Float.NaN) || rootkw.equals(Float.NaN)) ? 
					"***" : EGUtils.formatperc(record.getKw()/rootkw * 100);
			fos.write(s);
			fos.write("%;");
		}
	}
	if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
		record = report.getReportRecord("other");
		if (record != null) {
			s = (record.getKw().equals(Float.NaN) || rootkw.equals(Float.NaN)) ? 
					"***" : EGUtils.formatperc(record.getKw()/rootkw * 100);
			fos.write(s);
			fos.write("%;");
		}
	}
	fos.write(";\n");
	
	//    kw
	fos.write(lan.getString("energy", "enerkw"));
	fos.write(";");
	for (int i = 1; i <= num; i++) {
		if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
			record = report.getReportRecord("group" + i);
		} else {
			record = report.getReportRecord("cons"+prop.getProperty("group")+"."+ i);
		}
		if (record != null) {
			s = (record.getKw().equals(Float.NaN)) ? "***" : EGUtils.formatkw(record.getKw());
			fos.write(s);
			fos.write(";");
		}
	}
	if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
		record = report.getReportRecord("other");
		if (record != null) {
			s = (record.getKw().equals(Float.NaN)) ? "***" : EGUtils.formatkw(record.getKw());
			fos.write(s);
			fos.write(";");
		}
	}
	fos.write(";\n");
	
	//    kwh
	fos.write(lan.getString("energy", "enerkwh"));
	fos.write(";");
	for (int i = 1; i <= num; i++) {
		if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
			record = report.getReportRecord("group" + i);
		} else {
			record = report.getReportRecord("cons"+prop.getProperty("group")+"."+ i);
		}
		if (record != null) {
			s = (record.getKwh().equals(Float.NaN)) ? "***" : EGUtils.formatkwh(record.getKwh());
			fos.write(s);
			fos.write(";");
		}
	}
	if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
		record = report.getReportRecord("other");
		if (record != null) {
			s = (record.getKwh().equals(Float.NaN)) ? "***" : EGUtils.formatkwh(record.getKwh());
			fos.write(s);
			fos.write(";");
		}
	}
	fos.write(";\n");	
}

private void writeRootReport(BufferedWriter fos, EnergyReport report, LangService lan, Properties prop)
	throws Exception {
	EnergyReportRecord record = null;
	if (EnergyReport.SITE.equalsIgnoreCase(prop.getProperty("exporttype"))) {
		record = report.getReportRecord("site");
		fos.write(lan.getString("energy", "site"));
	} else {
		record = report.getReportRecord("group"+prop.getProperty("group"));
		fos.write(lan.getString("energy", "energroup"));
		fos.write(";");
		fos.write(prop.getProperty("group"));
	}
	fos.write(";\n");
	
	fos.write(lan.getString("energy", "enerkw"));
	fos.write(";");
	fos.write(record.getKw().toString());
	fos.write(";\n");
	
	fos.write(lan.getString("energy", "enerkwh"));
	fos.write(";");
	fos.write(record.getKwh().toString());
	fos.write(";\n");
	
	fos.write(lan.getString("energy", "enercost"));
	fos.write(";");
	fos.write(record.getCost().toString());
	fos.write(EnergyMgr.getInstance().getStringProperty("currency"));
	fos.write(";\n");
	
	fos.write(lan.getString("energy", "enerkgco2"));
	fos.write(";");
	fos.write(record.getKgco2().toString());
	fos.write(";\n");
}
	
	// energy groups
	public String executeTab4Data(UserSession us, Properties prop) throws Exception
	{
       	StringBuffer strbufResponse = new StringBuffer();
	    strbufResponse.append("<response>");
	    int idModel = Integer.parseInt(prop.getProperty("idModel"));
	    EnergyModel model = new EnergyModel(idModel);
	    String strCons = prop.getProperty("cons");
	    DeviceListBean beanDevList = new DeviceListBean(us.getIdSite(), us.getLanguage());
   		int idDevs[] = beanDevList.getIds();
   		if( idDevs != null ) {
   			for(int i = 0; i < idDevs.length; i++) {
   				DeviceBean beanDevice = DeviceListBean.retrieveSingleDeviceById(us.getIdSite(), idDevs[i], us.getLanguage());
   				if( beanDevice.getIddevmdl() == model.getIdDevMdl() ) {
   					int idDevice = beanDevice.getIddevice();
   					int idVarKw = model.getIdVarKw(beanDevice.getIddevice());
   					int idVarKwh = model.getIdVarKwh(beanDevice.getIddevice());
   					String strId = idDevice + "," + idVarKw + "," + idVarKwh;
   					boolean status = getCheckUnlog(us, prop,idDevice,model.getIdVarMdlKw(),model.getIdVarMdlKwh());
   					if( strCons.indexOf(strId) < 0 && status ) {
	   					strbufResponse.append("<dev id=\"");
	   					strbufResponse.append("" + idDevice);
	   					strbufResponse.append(",");
	   					strbufResponse.append(idVarKw);
	   					strbufResponse.append(",");
	   					strbufResponse.append(idVarKwh);
	   					strbufResponse.append("\" name=\"");
	   					strbufResponse.append(VSBase.xmlEscape(beanDevice.getDescription()));
	   					strbufResponse.append("\"/>\n");
   					}else{
   						strbufResponse.append("<message id=\"" + idDevice + "\" name=\"message\"/>");
   					}
   				}
   			}
   		}
       	strbufResponse.append("</response>");
       	return strbufResponse.toString();
	}
	
	//check out variable unlog in device
	private static boolean getCheckUnlog(UserSession us, Properties prop,Integer idDevice,Integer idVarKw,Integer idVarKwh)throws DataBaseException
	{
				String sql =
			            "select cfvariable.* from cfvariable where cfvariable.idsite = ? and cfvariable.idhsvariable = -1 " +
			            "and cfvariable.idhsvariable is not null and cfvariable.iddevice = ? and cfvariable.type != 4 " +
			            "and cfvariable.iscancelled=? and (cfvariable.idvarmdl = ? or cfvariable.idvarmdl = ?) order by cfvariable.idvarmdl";
				Object[] param = new Object[5];
				param[0] = new Integer(us.getIdSite());
				param[1] = idDevice;
				param[2] = "FALSE";
				param[3] = idVarKw;
				param[4] = idVarKwh;
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
				boolean status = true;
				if(rs.size() > 0){
	            	status = false;
	            }
		return status;
	}
	
	public void executeTab4Post(UserSession us, Properties prop) throws Exception
	{
		String cmd = prop.getProperty("cmd");
		if( "add".equals(cmd) ) {
			int idGroup = Integer.parseInt(prop.getProperty("idGroup"));
			EnergyGroup group = new EnergyGroup(idGroup);
			group.addGroup(us, prop);
		}
		else if( "save".equals(cmd) ) {
			int idGroup = Integer.parseInt(prop.getProperty("idGroup"));
			/* GLOBAL became a regular group
			String cons = prop.getProperty("cons");
			if(idGroup == -1 && cons.length()>0 && cons.split(",").length==3)
			{
				String[] strs = cons.split(",");
				int idVarKw = Integer.parseInt(strs[1]);
				int idVarKwh = Integer.parseInt(strs[2]);
				EnergyConfiguration energyconfig = EnergyMgr.getInstance().getSiteConfiguration();
				List<EnergyConsumer> cl = energyconfig.getConsumerList();
				for (EnergyConsumer ec:cl) 
				{
					if(!ec.getIdconsumer().equals(-1))
					{
						if(ec.getIdkw()!= null && ec.getIdkw().intValue() == idVarKw &&
							ec.getIdkwh() != null && ec.getIdkwh().intValue() == idVarKwh)
						{
							us.getCurrentUserTransaction().setProperty("duplicatedSiteMeterGroupName", energyconfig.getGroup(ec.getIdgroup()).getName());
							return;
						}
					}
				}
			}
			*/
			EnergyGroup group = new EnergyGroup(idGroup);
			group.updateGroup(us, prop);
		}
		else if( "modify".equals(cmd) ) {
			us.getCurrentUserTransaction().setAttribute("idGroup", Integer.parseInt(prop.getProperty("idGroup")));
		}
		else if( "remove".equals(cmd) ) {
			int idGroup = Integer.parseInt(prop.getProperty("idGroup"));
			EnergyGroup group = new EnergyGroup(idGroup);
			group.deleteGroup();
		}
		EnergyMgr.getInstance().loadProperties();			
		EnergyMgr.getInstance().load();
	}
	
	
	// energy models
	public String executeTab5Data(UserSession us, Properties prop) throws Exception
	{
       	StringBuffer strbufResponse = new StringBuffer();
       	int idDevMdl = Integer.parseInt(prop.getProperty("id")); 
       	strbufResponse.append("<response name=\"" + VSBase.xmlEscape(EnergyModel.getDevMdlName(us.getLanguage(), idDevMdl)) +"\">\n");
       	try {
       		VarMdlBean aVarMdl[] = VarMdlBeanList.retrieveLogOrdered(us.getIdSite(), idDevMdl, us.getLanguage());
	       	for(int i = 0; i < aVarMdl.length; i++) {
	       		strbufResponse.append("<varmdl id=\"");
	       		strbufResponse.append("" + aVarMdl[i].getIdvarmdl());
	       		strbufResponse.append("\" code=\"");
	       		strbufResponse.append("" + aVarMdl[i].getCode());
	       		strbufResponse.append("\" name=\"");
	       		strbufResponse.append("" + VSBase.xmlEscape(aVarMdl[i].getDescription()));
	       		strbufResponse.append("\" type=\"");
	       		strbufResponse.append("" + aVarMdl[i].getType());
	       		strbufResponse.append("\"/>\n");
	       	}
       	}
		catch(DataBaseException e) {
			e.printStackTrace();
		}
	    strbufResponse.append("</response>");
    	return strbufResponse.toString();
	}

	
	public void executeTab5Post(UserSession us, Properties prop) throws Exception
	{
		String cmd = prop.getProperty("cmd");
		if( "save".equals(cmd) ) {
			int idModel = Integer.parseInt(prop.getProperty("idModel"));
			EnergyModel model = new EnergyModel(idModel);
			model.setName(prop.getProperty("name"));
			model.setIdDevMdl(Integer.parseInt(prop.getProperty("idDevMdl")));
			model.setIdVarMdlKw(Integer.parseInt(prop.getProperty("idVarMdlKw")));
			model.setIdVarMdlKwh(Integer.parseInt(prop.getProperty("idVarMdlKwh")));
			model.saveModel();
		}
		else if( "modify".equals(cmd) ) {
			us.getCurrentUserTransaction().setAttribute("idModel", Integer.parseInt(prop.getProperty("idModel")));
		}
		else if( "remove".equals(cmd) ) {
			int idModel = Integer.parseInt(prop.getProperty("idModel"));
			EnergyModel model = new EnergyModel(idModel);
			model.deleteModel();
		}
	}

	
	// energy profile
	public void executeTab6Post(UserSession us, Properties prop) throws Exception
	{
		String cmd = prop.getProperty("cmd");
		if( "saveXML".equalsIgnoreCase(cmd) ) {
			String strActiveCfg = prop.getProperty("active_cfg");
			EnergyMgr.getInstance().setProperty("active_cfg", strActiveCfg);
			EnergyMgr.getInstance().setProperty("cost", prop.getProperty("kwcost"));
			EnergyMgr.getInstance().setProperty("currency", prop.getProperty("currency"));
			EnergyMgr.getInstance().setProperty("kgco2", prop.getProperty("kgco2"));
			EnergyMgr.getInstance().setProperty("header", prop.getProperty("header"));
			EnergyMgr.getInstance().setProperty("footer", prop.getProperty("footer"));
			if( strActiveCfg.equals("time_slot")) {
				EnergyMgr.getInstance().getEnergyProfile().saveXML(prop.getProperty("xml"));
				us.getCurrentUserTransaction().setProperty("selLayer", prop.getProperty("selLayer"));
				us.getCurrentUserTransaction().setProperty("iTimeTable", prop.getProperty("iTimeTable"));
			}
			EnergyMgr.getInstance().loadProperties();			
			EnergyMgr.getInstance().load();
		}
		else if( "importXML".equalsIgnoreCase(cmd) ) {
			String strFileName = prop.getProperty("impexp");
			boolean bImport = EnergyMgr.getInstance().getEnergyProfile().importXML(strFileName);
			if( bImport )
				us.setProperty("impexp_alert", LangMgr.getInstance().getLangService(us.getLanguage()).getString("energy", "import_success"));
			else
				us.setProperty("impexp_alert", LangMgr.getInstance().getLangService(us.getLanguage()).getString("energy", "import_failure") + strFileName);				
		}
		else if( "exportXML".equalsIgnoreCase(cmd) ) {
			String strFileName = prop.getProperty("impexp");
			boolean bExport = EnergyMgr.getInstance().getEnergyProfile().exportXML(strFileName);
			if( bExport )
				us.setProperty("impexp_alert", LangMgr.getInstance().getLangService(us.getLanguage()).getString("energy", "export_success"));
			else
				us.setProperty("impexp_alert", LangMgr.getInstance().getLangService(us.getLanguage()).getString("energy", "export_failure") + strFileName);				
		}
	}
	
	// cold rental
	public void executeTab7Post(UserSession us, Properties prop) throws Exception
	{
		String cmd = prop.getProperty("cmd");
		if( "save".equals(cmd) ) {
			int idConsumer = Integer.parseInt(prop.getProperty("idConsumer"));
			String devs = prop.getProperty("devs");
			EnergyDevice.updateConsumerDevices(idConsumer, devs);
		}
		else if( "modify".equals(cmd) ) {
			us.getCurrentUserTransaction().setAttribute("idConsumer", Integer.parseInt(prop.getProperty("idConsumer")));
		}
	}
}
