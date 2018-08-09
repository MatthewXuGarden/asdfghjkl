package com.carel.supervisor.presentation.bo;

import java.util.*;
import org.jfree.data.category.DefaultCategoryDataset;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.plugin.optimum.*;


public class BOptLights extends BoMaster {
	private static final long serialVersionUID = 5454748676207893289L;
	private static final int REFRESH_TIME = -1;
	
	
	public BOptLights(String l)
    {
        super(l, REFRESH_TIME);
    }
	
	
	protected Properties initializeEventOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "onLoadDashboard();");
		p.put("tab2name", "onLoadSettings();");
		return p;
	}
	
	
	protected Properties initializeJsOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "opt_lights.js;keyboard.js;");
		p.put("tab2name", "opt_lights.js;keyboard.js;");
		return p;
	}
	

	
    public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
    {
		if( tabName.equalsIgnoreCase("tab1name") ) {
			String cmd = prop.getProperty("cmd");
			if( "start".equals(cmd) )
				OptimumManager.getInstance().getLights().setEnabled(true);
			else if( "stop".equals(cmd) )
				OptimumManager.getInstance().getLights().setEnabled(false);
			else if( "refresh".equals(cmd) ); // nothing to do; just refresh the page
			else
				executeTab1Post(us, prop);
		}
		else if( tabName.equalsIgnoreCase("tab2name") ) {
			OptimumManager.getInstance().getLights().saveParameters(prop);
		}
    }
    
    
    public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
    {
      	if( tabName.equalsIgnoreCase("tab2name") )
    		return executeTab2Data(us, prop);
    	String strResponse = "<response/>";
    	return strResponse;
    }
    

	public void executeTab1Post(UserSession us, Properties prop) throws Exception
	{
		// series labels
		LangService lang = LangMgr.getInstance().getLangService(us.getLanguage());
		String strSunrise = lang.getString("opt_lights", "sunrise");
		String strSunset = lang.getString("opt_lights", "sunset");
		
		// create data sets
		DefaultCategoryDataset datasetA = new DefaultCategoryDataset();
		us.getCurrentUserTransaction().setAttribute("datasetA", datasetA);
		DefaultCategoryDataset datasetB = new DefaultCategoryDataset();
		us.getCurrentUserTransaction().setAttribute("datasetB", datasetB);
		
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
		if( "prev15next15".equals(cmd) ) {
			int nYear = Integer.parseInt(prop.getProperty("year"));
			int nDayOfYear = Integer.parseInt(prop.getProperty("day"));
			calBegin.set(Calendar.YEAR, nYear);
			calBegin.set(Calendar.DAY_OF_YEAR, nDayOfYear - 15);
			calEnd = (Calendar)calBegin.clone();
			calEnd.set(Calendar.DAY_OF_YEAR, nDayOfYear + 16);
		}

		us.getCurrentUserTransaction().setAttribute("timeInterval", cmd);
		us.getCurrentUserTransaction().setAttribute("dtBegin", calBegin);
		us.getCurrentUserTransaction().setAttribute("dtEnd", calEnd);
		
		// compute datasets data sets
		LightsBean bean = OptimumManager.getInstance().getLights();
		Calendar cal = (Calendar)calBegin.clone();
		while( cal.before(calEnd) ) {
			Date dtSunrise = SunriseSunset.getSunrise(bean.getLatitude(), bean.getLongitude(), cal.getTime());
			Date dtSunset = SunriseSunset.getSunset(bean.getLatitude(), bean.getLongitude(), cal.getTime());
			datasetA.addValue((float)(dtSunrise.getHours() * 60 + dtSunrise.getMinutes()) / (24 * 60) * 24, strSunrise, "" + cal.get(Calendar.DAY_OF_MONTH));
			datasetA.addValue((float)(dtSunset.getHours() * 60 + dtSunset.getMinutes()) / (24 * 60) * 24, strSunset, "" + cal.get(Calendar.DAY_OF_MONTH));
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		for(int i = 1; i < 366; i++) {
			cal.set(Calendar.DAY_OF_YEAR, i);
			Date dtSunrise = SunriseSunset.getSunrise(bean.getLatitude(), bean.getLongitude(), cal.getTime());
			Date dtSunset = SunriseSunset.getSunset(bean.getLatitude(), bean.getLongitude(), cal.getTime());
			datasetB.addValue((float)(dtSunrise.getHours() * 60 + dtSunrise.getMinutes()) / (24 * 60) * 24, strSunrise, "" + cal.get(Calendar.DAY_OF_YEAR));
			datasetB.addValue((float)(dtSunset.getHours() * 60 + dtSunset.getMinutes()) / (24 * 60) * 24, strSunset, "" + cal.get(Calendar.DAY_OF_YEAR));
		}
	}
    
	
 	public String executeTab2Data(UserSession us, Properties prop) throws Exception
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
					if( aux.getType() != com.carel.supervisor.dataaccess.dataconfig.VariableInfo.TYPE_DIGITAL )
						continue; // show only digital variables
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
		return response.toString();
	}
}
