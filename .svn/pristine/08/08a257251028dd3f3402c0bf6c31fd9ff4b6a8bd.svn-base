package com.carel.supervisor.presentation.bo;

import java.util.*;
import java.text.*;
import java.sql.Timestamp;
import org.jfree.data.category.DefaultCategoryDataset;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.*;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.plugin.optimum.*;


public class BOptNightFreeCooling extends BoMaster {
	private static final long serialVersionUID = 3066896753879494510L;
	private static final int REFRESH_TIME = 5000;
	
	
	public BOptNightFreeCooling(String l)
    {
        super(l, REFRESH_TIME);
    }
	
	
	protected Properties initializeEventOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "onLoadDashboard();");
		p.put("tab3name", "onLoadSettings();");
		return p;
	}
	
	
	protected Properties initializeJsOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "opt_nightfreecooling.js;keyboard.js;");
		p.put("tab2name", "opt_nightfreecooling.js;keyboard.js;");
		p.put("tab3name", "opt_nightfreecooling.js;keyboard.js;");
		return p;
	}
	
	
    public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
    {
		if( tabName.equalsIgnoreCase("tab1name") ) {
			String cmd = prop.getProperty("cmd");
			int idAlgorithm = Integer.parseInt(prop.getProperty("idalg"));
			if( "start".equals(cmd) )
				OptimumManager.getInstance().getNightFreeCooling(idAlgorithm).setEnabled(true);
			else if( "stop".equals(cmd) )
				OptimumManager.getInstance().getNightFreeCooling(idAlgorithm).setEnabled(false);
			else if( "refresh".equals(cmd) )
				us.getCurrentUserTransaction().setAttribute("idalg", idAlgorithm);
			else
				executeTab2Post(us, prop);
		}
		else if( tabName.equalsIgnoreCase("tab2name") ) {
			executeTab2Post(us, prop);
		}
		else if( tabName.equalsIgnoreCase("tab3name") ) {
			executeTab3Post(us, prop);
		}
    }
    
    
    public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
    {
    	if( tabName.equalsIgnoreCase("tab1name") )
    		return executeTab1Data(us, prop);
    	else if( tabName.equalsIgnoreCase("tab3name") )
    		return executeTab3Data(us, prop);
    	String strResponse = "<response/>";
    	return strResponse;
    }
    
    
    public void executeTab2Post(UserSession us, Properties prop) throws Exception
	{
		// create data sets
		DefaultCategoryDataset datasetA = new DefaultCategoryDataset();
		us.getCurrentUserTransaction().setAttribute("datasetA", datasetA);
		DefaultCategoryDataset datasetB = new DefaultCategoryDataset();
		us.getCurrentUserTransaction().setAttribute("datasetB", datasetB);
		DefaultCategoryDataset datasetT = new DefaultCategoryDataset();
		us.getCurrentUserTransaction().setAttribute("datasetT", datasetT);
		
		// time interval
		Calendar calBegin = new GregorianCalendar();
		calBegin.setMinimalDaysInFirstWeek(1);
		calBegin.setFirstDayOfWeek(Calendar.MONDAY);
		calBegin.set(Calendar.HOUR_OF_DAY, 0);
		calBegin.set(Calendar.MINUTE, 0);
		calBegin.set(Calendar.SECOND, 0);
		calBegin.set(Calendar.MILLISECOND, 0);
		Calendar calEnd = null;
		
		String cmd = prop.getProperty("cmd");
		if( "weekly".equals(cmd) ) {
			int nYear = Integer.parseInt(prop.getProperty("w_year"));
			int nWeek = Integer.parseInt(prop.getProperty("week"));
			calBegin.set(Calendar.YEAR, nYear);
			calBegin.set(Calendar.WEEK_OF_YEAR, nWeek);
			calBegin.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			calEnd = (Calendar)calBegin.clone();
			calEnd.add(Calendar.DAY_OF_WEEK, 7);
		}
		else if( "monthly".equals(cmd) ) {
			int nYear = Integer.parseInt(prop.getProperty("m_year"));
			int nMonth = Integer.parseInt(prop.getProperty("month"));
			calBegin.set(Calendar.YEAR, nYear);
			calBegin.set(Calendar.MONTH, nMonth - 1);
			calBegin.set(Calendar.DAY_OF_MONTH, 1);
			calEnd = (Calendar)calBegin.clone();
			calEnd.add(Calendar.MONTH, 1);
		}
		else if( "yearly".equals(cmd) ) {
			int nYear = Integer.parseInt(prop.getProperty("year"));
			calBegin.set(Calendar.YEAR, nYear);
			calBegin.set(Calendar.MONTH, Calendar.JANUARY);
			calBegin.set(Calendar.DAY_OF_MONTH, 1);			
			calBegin.set(Calendar.YEAR, nYear);
			calEnd = (Calendar)calBegin.clone();
			calEnd.add(Calendar.YEAR, 1);
		}
		else if( "prev7".equals(cmd) ) {
			int nYear = Integer.parseInt(prop.getProperty("year"));
			int nDayOfYear = Integer.parseInt(prop.getProperty("day"));
			calBegin.set(Calendar.YEAR, nYear);
			calBegin.set(Calendar.DAY_OF_YEAR, nDayOfYear - 7);
			calEnd = (Calendar)calBegin.clone();
			calEnd.set(Calendar.DAY_OF_YEAR, nDayOfYear);
		}

		int idAlgorithm = Integer.parseInt(prop.getProperty("idalg"));
		us.getCurrentUserTransaction().setAttribute("idalg", idAlgorithm);
		us.getCurrentUserTransaction().setAttribute("timeInterval", cmd);
		us.getCurrentUserTransaction().setAttribute("dtBegin", calBegin);
		us.getCurrentUserTransaction().setAttribute("dtEnd", calEnd);
		
		// retrieve data sets
		NightFreeCoolingLog.retriveDataSets(idAlgorithm, calBegin, calEnd, cmd, us.getLanguage(), datasetA, datasetB, datasetT);
	}
    
	
    public void executeTab3Post(UserSession us, Properties prop) throws Exception
	{
		String cmd = prop.getProperty("cmd");
		prop.remove("cmd");
		int idAlgorithm = Integer.parseInt(prop.getProperty("AlgorithmId"));
		if( "save".equals(cmd) ) {
			NightFreeCoolingBean bean = OptimumManager.getInstance().getNightFreeCooling(idAlgorithm); 
			bean.saveParameters(prop);
			if( idAlgorithm == 0 )
				OptimumManager.getInstance().addNightFreeCooling(bean);
			else
				bean.resetAllFlags();
			us.getCurrentUserTransaction().setAttribute("idalg", new Integer(bean.getAlgorithId()));
		}
		else if( "add".equals(cmd) ) {
			us.getCurrentUserTransaction().setAttribute("idalg", new Integer(0));
		}
		else if( "modify".equals(cmd) ) {
			us.getCurrentUserTransaction().setAttribute("idalg", new Integer(idAlgorithm));
		}
		else if( "del".equals(cmd) ) {
			NightFreeCoolingBean bean = OptimumManager.getInstance().getNightFreeCooling(idAlgorithm);
			bean.delete();
			OptimumManager.getInstance().delNightFreeCooling(bean);
			us.getCurrentUserTransaction().removeAttribute("idalg");
		}
	}
    
    
    public String executeTab1Data(UserSession us, Properties prop) throws Exception
	{
    	StringBuilder response= new StringBuilder();
    	/*
    	NightFreeCoolingBean bean = OptimumManager.getInstance().getNightFreeCooling();
		response.append("<response>");
		response.append("<UnitOnOff value=\"" + (bean.isEnabled() ? bean.getUnitOnOff() : "***") + "\"/>");
		response.append("<TemperatureSetpoint value=\"" + (bean.isEnabled() ? bean.getTemperatureSetpoint() : "***") + "\"/>");
		response.append("<InternalTemperature value=\"" + (bean.isEnabled() ? bean.getInternalTemperature() : "***") + "\"/>");
		response.append("<ExternalTemperature value=\"" + (bean.isEnabled() ? bean.getExternalTemperature() : "***") + "\"/>");
		response.append("<HumiditySetpoint value=\"" + (bean.isEnabled() ? bean.getHumiditySetpoint() : "***") + "\"/>");
		response.append("<InternalHumidity value=\"" + (bean.isEnabled() ? bean.getInternalHumidity() : "***") + "\"/>");
		response.append("<ExternalHumidity value=\"" + (bean.isEnabled() ? bean.getExternalHumidity() : "***") + "\"/>");
		String strComputedTimeOn = (bean.getComputedTimeOnHour() < 10 ? "0" : "") + bean.getComputedTimeOnHour()
		+ ":" + (bean.getComputedTimeOnMinute() < 10 ? "0" : "") + bean.getComputedTimeOnMinute();
		response.append("<TimeOn value=\"" + (bean.isEnabled() ? strComputedTimeOn : "***") + "\"/>");
		String strTimeOff = (bean.getTimeOffHour() < 10 ? "0" : "") + bean.getTimeOffHour()
		+ ":" + (bean.getTimeOffMinute() < 10 ? "0" : "") + bean.getTimeOffMinute();
		response.append("<TimeOff value=\"" + (bean.isEnabled() ? strTimeOff : "***") + "\"/>");
		response.append("</response>");
		*/
		return response.toString();
	}
    
    
    public String executeTab3Data(UserSession us, Properties prop) throws Exception
	{
		StringBuilder response= new StringBuilder();
		response.append("<response>");

		Integer action = Integer.parseInt((String) prop.get("action"));
		Integer idDevice = action < 10 ? Integer.parseInt((String) prop.get("iddevice")) : 0;
		
		switch( action.intValue() ){
			case 0: // LOAD_DEVICE_VAR
				response.append("<![CDATA[");
				VarphyBeanList varlist = new VarphyBeanList();
				VarphyBean[] vars = varlist.getAllVarOfDevice(us.getLanguage(),us.getIdSite(),idDevice);
				StringBuffer strbufTypeCSV = new StringBuffer();
				strbufTypeCSV.append("<input type='hidden' id='csvVarType' name='csvVarType' value='");
				response.append("<select onclick=\"onSelectDevVar(this.selectedIndex)\" ondblclick=\"setVar(this.selectedIndex);\" id='devvar' name='devvar' multiple size='10' class='standardTxt' style='width: 100%;'>");
				for (int i=0;i<vars.length;i++)	{
					VarphyBean aux = vars[i];
					response.append("<option value=\"");
					response.append(String.valueOf(aux.getId()));
					response.append("\"");
					response.append(" class = '"+( (i%2==0)?"Row1":"Row2" )+"'>");
					response.append(aux.getShortDescription());
					response.append("</option>");
					if( i > 0 )
						strbufTypeCSV.append(",");
					strbufTypeCSV.append(aux.getType());
					if( aux.getType() == 3 && aux.getVarLength() == 32 && aux.getVarDimension() == 32 )
						strbufTypeCSV.append("2");
				}
				response.append("</select>");
				strbufTypeCSV.append("'>");
				response.append(strbufTypeCSV);
				response.append("]]>");	
			break;

			case 1: // LOAD_DEVICE
				response.append("<![CDATA[");
				DeviceListBean devs = new DeviceListBean(us.getIdSite(),us.getLanguage());
				DeviceBean tmp_dev = null;
				int[] ids = devs.getIds();
				response.append("<select onclick=\"reload_actions(0);\" ondblclick=\"reload_actions(0);\" id=\"dev\" name='dev' size='10' class='standardTxt' style='width:100%;' >");
				int device=0;
				for (int i=0;i<devs.size();i++){
					tmp_dev = devs.getDevice(ids[i]);
					response.append("<OPTION "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"' class = '"+( (i%2==0)?"Row1":"Row2" )+"'>"+tmp_dev.getDescription()+"</OPTION>\n");
				}
				response.append("</select>");
				response.append("]]>");	
			break;
			
			case 2: // LOAD_DEVICE_MDL
				response=new StringBuilder();
				response.append("<response>");
				response.append("<device>");
				response.append("<![CDATA[");
				
				devs = new DeviceListBean(us.getIdSite(),us.getLanguage(),idDevice,1);
				tmp_dev = null;
				ids = devs.getIds();
				StringBuffer div_dev = new StringBuffer();
				div_dev.append("<select multiple id=\"dev\" name='dev' onclick=\"reload_actions(0);\" ondblclick=\"reload_actions(0);\" size='10' class='standardTxt' style='width: 100%;' >");
				device=0;
				for (int i=0;i<devs.size();i++){
					tmp_dev = devs.getDevice(ids[i]);
					div_dev.append("<OPTION "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"' class = '"+((i%2==0)?"Row1":"Row2")+"'>"+tmp_dev.getDescription()+"</OPTION>\n");
				}
				div_dev.append("</select>");
				response.append(div_dev.toString());
				response.append("]]>");	
				response.append("</device>");
			break;
		}
		
		response.append("</response>");
		return response.toString();// super.executeDataAction(us, tabName, prop);
	}
    
}
