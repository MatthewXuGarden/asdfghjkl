package com.carel.supervisor.presentation.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import supervisor.Login;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.system.SystemInfoExt;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.database.RuleBean;
import com.carel.supervisor.controller.database.RuleListBean;
import com.carel.supervisor.controller.database.TimeBandBean;
import com.carel.supervisor.controller.database.TimeBandList;
import com.carel.supervisor.controller.setfield.BackGroundCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerDirectly;
import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfo;
import com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList;
import com.carel.supervisor.dataaccess.datalog.impl.ConditionBean;
import com.carel.supervisor.dataaccess.datalog.impl.ConditionBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.ReportBean;
import com.carel.supervisor.dataaccess.datalog.impl.ReportBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.Event;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dataaccess.support.Information;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.dispatcher.main.DispatcherDirectly;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.sim.DispSimMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.presentation.bean.DevMdlBeanList;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.rule.ActionBean;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;
import com.carel.supervisor.presentation.bean.rule.ConditionBeanListPres;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.copydevice.PageImpExp;
import com.carel.supervisor.presentation.devices.UtilDevice;
import com.carel.supervisor.presentation.io.CioFAX;
import com.carel.supervisor.presentation.io.CioMAIL;
import com.carel.supervisor.presentation.io.CioPrinter;
import com.carel.supervisor.presentation.io.CioSMS;
import com.carel.supervisor.presentation.session.UserSession;

public class BWizard extends BoMaster 
{
	private static final int REFRESH_TIME = -1;
	public static final String HACCPREPORTACTIONNMEBYWIZARD = "HACCPReportActionByWizard";
	public static final String ALARMACTIONNMEBYWIZARD = "AlarmActionByWizard";
	public static final String ALARMRULEBYWIZARD = "AlarmRuleByWizard";
	public static final String HACCPREPORTRULEBYWIZARD = "HACCPReportRuleByWizard";
	public static final String CONDITIONBYWIZARD = "ConditionByWizard";
	public static final String HACCPREPORTTIMEBINDBYWIZARD = "TimebandByWizard";
	
	public static final String ALARM_RULEBYWIZARD = "RuleByWizard";
	public static final String ALARM_ACTIONNMEBYWIZARD = "ActionByWizard_Alarm";
	public static final String ALARM_CONDITIONBYWIZARD = "ConditionByWizard";
	private String[] priors=new String[]{"_ALL","_Highest","_High","_Medium","_Low"};
	
	public static final String ACTIONEXIST = "actionexist";
	public static final String TIMEBANDEXIST = "timebandexist";
	
	public static final int MAXIMUM_DEVICE = 50;
	private String[] wizardDeviceCode = null;
	
	private static final int CONFIG_INTERNAL_MODEM = -20;
	private static final int CONFIG_GSM_MODEM = -30;
	public BWizard(String l)
	{
		super(l);
	}
	
	
	protected Properties initializeEventOnLoad()
	{
		Properties p = new Properties();
		p.setProperty("tab1name", "wizard_tab1_init();");
		p.setProperty("tab2name", "wizard_tab2_init();");
		p.setProperty("tab3name", "onload_siteview();wizard_tab3_init();");
		p.setProperty("tab4name", "wizard_tab4_init();");
		p.setProperty("tab5name", "wizard_tab5_init();");
		p.setProperty("tab6name", "wizard_tab6_init();");
		p.setProperty("tab7name", "wizard_tab7_init();");
		return p;
	}
	
	
	protected Properties initializeJsOnLoad()
	{
		Properties p = new Properties();
		p.setProperty("tab1name", "wizard.js;keyboard.js;");
		p.setProperty("tab2name", "wizard.js;keyboard.js;");
		p.setProperty("tab3name", "line.js;wizard.js;../arch/actb/actb.js;../arch/actb/common.js;keyboard.js;");
		p.setProperty("tab4name", "../arch/table/sorttable.js;wizard.js;keyboard.js;dtlview_param.js;");
		p.setProperty("tab5name", "wizard.js;keyboard.js;evnview.js");
		p.setProperty("tab6name", "wizard.js;keyboard.js;");
		p.setProperty("tab7name", "dbllistbox.js;guardian.js;wizard.js;keyboard.js;");
		return p;
	}
	
	
	public String executeDataAction(UserSession us, String tabName,
            Properties prop) throws Exception
    {
    	StringBuffer result = new StringBuffer();
    	try
    	{
    	String cmd = prop.getProperty("cmd");
			if((cmd!=null) && (cmd.equals("rest"))){
				stopPvEngine(us);
	            startPvEngine(us);
	            result.append("<wizard><error><![CDATA[ok]]></error></wizard>");
	            return result.toString();
			}
	    	if(tabName.equals("tab1name") == true)
	    	{
	    		SubTab1DataAction(us,prop);
	    	}
	    	if(tabName.equals("tab2name"))
	    	{
	    		result.append(SubTab2DataAction(us,prop));
	    	}
	    	else if(tabName.equals("tab4name") == true)
	    	{
	    		result.append(SubTab4DataAction(us,prop));
	    	}
	    	else if(tabName.equals("tab5name") == true)
	    	{
	    		result.append(SubTab5DataAction(us,prop));
	    	}
	    	else if(tabName.equals("tab6name") == true)
	    	{
	    		result.append(this.SubTab6DataAction(us, prop));
	    	}
	    	else if(tabName.equals("tab7name") == true)
	    	{
	    		BSystem bsystem=new BSystem(us.getLanguage());
	    		result.append(bsystem.executeDataAction(us, "tab5name", prop));
	    	}
    	}
    	catch(Exception ex)
    	{
    		Logger logger = LoggerMgr.getLogger(this.getClass());
    		logger.error(ex);
    	}
    	return result.toString();
    }
	
	
	public void executePostAction(UserSession us, String tabName,
	        Properties prop) throws Exception
	{
		if(tabName.equals("tab3name")){
			BSiteView bsiteview=new BSiteView(us.getLanguage());
			bsiteview.executePostAction(us, tabName, prop);
    	}else if(tabName.equals("tab7name")){
    		BSystem bsystem=new BSystem(us.getLanguage());
    		bsystem.executePostAction(us, "tab5name", prop);
    		//set save flag
    		us.setProperty("saveclick", "true");
    	}
    	else if(tabName.equals("tab2name"))
    	{
			BSystem bsystem = new BSystem(us.getLanguage());
			bsystem.activePacket(us, prop);
			BSiteView bsiteview = new BSiteView(us.getLanguage());
			bsiteview.saveSiteinfo(us, prop);
    	}
	}
	
	
	public String SubTab1DataAction(UserSession us, Properties prop)
		throws Exception
	{
		StringBuffer result = new StringBuffer();
		String checked = prop.getProperty(Login.WIZARDDONE);
		if(checked == null)
			return "";
		IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
		String wizarddone = product.get(Login.WIZARDDONE);
		if(checked.equals("1") &&  wizarddone != null && !wizarddone.equals("1"))
		{
			product.set(Login.WIZARDDONE, "1");
		}
		if(checked.equals("0") && wizarddone != null && !wizarddone.equals("0"))
		{
			product.set(Login.WIZARDDONE, "0");
		}

		return result.toString();
	}
	
	
	public String SubTab2DataAction(UserSession us, Properties prop)
	{
		String cmd = prop.getProperty("cmd");
		String toReturn = "";
		if("cpuperusage".equalsIgnoreCase(cmd))
		{
			toReturn = "<response>";
			toReturn += "<cpu><![CDATA["+SystemInfoExt.getInstance().getCpuPerUsage()+"]]></cpu></response>";
		}
		return toReturn;
	}
	
	
	public String SubTab4DataAction(UserSession us, Properties prop)
		throws Exception
	{
		String result = "";
		String cmd = prop.getProperty("cmd");
		if(cmd.equals("init") || cmd.equals("refresh"))
		{
			result += initParametersSetting(us,prop);
		}
		else if(cmd.equals("set"))
		{
			result += setValue(us,prop);
		}else if(cmd.equals("deviceSettingShow"))
		{
			result += getDevSetFile(us,prop);
		}
		return result;
	}
	
	
	public String SubTab6DataAction(UserSession us, Properties prop)
		throws Exception
	{
		StringBuffer result = new StringBuffer();
		String cmd = prop.getProperty("cmd");
		if(cmd.equals("insert"))
		{
			try
			{
				String error = insertHACCPReport(us, prop);
				if(error.equals("") == false)
				{
					result.append("<wizard><cmd><![CDATA[insert]]></cmd><error><![CDATA["+error+"]]></error></wizard>");
				}
				else
				{
					result.append("<wizard><cmd><![CDATA[insert]]></cmd><error><![CDATA[ok]]></error></wizard>");
				}
			}
			catch(Exception ex)
			{
				result.append("<wizard><cmd><![CDATA[insert]]></cmd><error><![CDATA["+ex.toString()+"]]></error></wizard>");
			}
		}
		else if(cmd.equals("update"))
		{
			try
			{
				updateHACCPReport(us, prop);
				result.append("<wizard><cmd><![CDATA[update]]></cmd><error><![CDATA[ok]]></error></wizard>");
			}
			catch(Exception ex)
			{
				result.append("<wizard><cmd><![CDATA[update]]></cmd><error><![CDATA["+ex.toString()+"]]></error></wizard>");
			}
			
		}
		else if(cmd.equals("init"))
		{
			try
			{
				result.append(this.initHACCPReport(us, prop));
			}
			catch(Exception ex)
			{
				result.append("<wizard><cmd><![CDATA[init]]></cmd><error><![CDATA["+ex.toString()+"]]></error></wizard>");
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(ex);
			}
		}
//		else if(cmd.equals("rest")){
//			stopPvEngine(us);
//            startPvEngine(us);
//            result.append("<wizard><error><![CDATA[ok]]></error></wizard>");
//		}
		return result.toString();
	}
	
	
	private String getDevSetFile(UserSession us, Properties prop) throws Exception{
		String result="<devset>";
		String filename = prop.getProperty("devsetfile");
		int iddev = Integer.parseInt(prop.getProperty("iddev"));
		if (filename != null && filename.length() > 0) {
			PageImpExp pageImpExp = new PageImpExp();
			ArrayList<String> params=new ArrayList<String>();
			params=pageImpExp.getDeviceSettingFile(filename);
			String[]keys=new String[params.size()];
			Map filevalues = new HashMap();
			for(int i=0;i<params.size();i++){
				String tmp=(String)params.get(i);
				String var_code = tmp.split("=")[0];
                String var_val = tmp.split("=")[1];
                keys[i]=var_code;
                filevalues.put(var_code, var_val);
			}
			VarphyBean[] varphys = null;
			if (keys != null && keys.length > 0) {
				varphys = VarphyBeanList.getByiddevice_devmdlcodes(us.getLanguage(), us.getIdSite(), iddev, keys);
			}

			for(int i=0;i<keys.length;i++){
				for(int j=0;j<varphys.length;j++){
					if(keys[i].equals(varphys[j].getCodeVar())){
						result+="<param><name><![CDATA["+varphys[j].getCodeVar()+"]]></name><value><![CDATA["+filevalues.get(varphys[j].getCodeVar())+"]]></value><shortDescription><![CDATA["+varphys[j].getShortDescription()+"]]></shortDescription></param>";
						break;
					}
				}
			}
		}
		result+="</devset>";
		return result;
	}
	
	
	private String setValue(UserSession us, Properties prop)
		throws Exception
	{
		StringBuffer result = new StringBuffer();
		BDtlView bDtlView = new BDtlView(us.getLanguage());
		Map filevalues = null;
		String filename = prop.getProperty("filename");
		int iddev = Integer.parseInt(prop.getProperty("iddev"));
		int[] idvars = getVariableInfoByProperties(prop);
		if(filename != null && filename.length()>0)
		{
		  filevalues = bDtlView.getValueFromDefaultPathFile(filename);
		}
		String[] devmdlcodes = null;
		if(filevalues != null)
		{
			devmdlcodes = getDevmdlcodes(filevalues);
		}
		VarphyBean[] varphys = null;
		if(devmdlcodes != null && devmdlcodes.length>0)
		{
			varphys =  VarphyBeanList.getByiddevice_devmdlcodes(us.getLanguage(),us.getIdSite(),iddev,devmdlcodes);
		}
		if(varphys != null)
		{
			for(int i=0;i<varphys.length;i++)
			{
				VarphyBean var = varphys[i];
				String idvar = BDtlView.PREX+var.getId();
				if(prop.containsKey(idvar) == false)
				{
					prop.setProperty(idvar, filevalues.get(var.getCodeVar()).toString());
				}
			}
		}
		bDtlView.setValue(us, prop, iddev);
		Thread.sleep(1000);
		
		if(idvars != null && idvars.length>0)
		{
			Variable[] vars = ControllerMgr.getInstance().retrieve(idvars);
			result.append("<vars>");
			for(int i=0;i<vars.length;i++)
			{
				result.append("<var id='"+vars[i].getInfo().getId()+"'>");
				result.append("<![CDATA["+vars[i].getFormattedValue()+"]]>");
				result.append("</var>");
			}
			result.append("</vars>");
		}
		return result.toString();
	}
	
	
	private int[] getVariableInfoByProperties(Properties prop)
	{
		StringBuffer buffer = new StringBuffer();
		Iterator params = prop.keySet().iterator();
		while(params.hasNext())
		{
			String key = (String)params.next();
			if(key.startsWith(BDtlView.PREX))
			{
				String idvar = key.split("_")[1];
				buffer.append(idvar+";");
			}
		}
		String result = buffer.toString();
		if(result != null && result.length()>0)
		{
			result = result.substring(0,result.length()-1);
			String[] temp = result.split(";");
			int[] idvars = new int[temp.length];
			for(int i=0;i<idvars.length;i++)
			{
				idvars[i] = Integer.parseInt(temp[i]);
			}
			return idvars;
		}
		return null;
	}
	
	
	private String[] getDevmdlcodes(Map values)
	{
		if(values != null)
		{
			StringBuffer codes = new StringBuffer();
			Iterator code = values.keySet().iterator();
			while(code.hasNext())
			{
				codes.append((String)code.next()+";");
			}
			String temp = codes.toString();
			if(temp != null && temp.length()>0)
			{
				temp = temp.substring(0,temp.length()-1);
			}
			return temp.split(";");
		}
		return null;
	}
	
	
	private String initParametersSetting(UserSession us, Properties prop)
		throws Exception
	{
		String cmd = prop.getProperty("cmd");
		int limit = Integer.parseInt(prop.getProperty("limit"));
		int offset = Integer.parseInt(prop.getProperty("offset"));
		int current_page = Integer.parseInt(prop.getProperty("current_page"));
		StringBuffer result = new StringBuffer();
		
		// method invocation changed to hide Internal IO device
		// Nicola Compagno 26032010
		DeviceListBean deviceList = new DeviceListBean(us.getIdSite(),us.getLanguage(), true);
		
		DeviceBean device = null;
        VarphyBean varphy = null;
        int[] idsAll = deviceList.getIds();
        ArrayList online = new ArrayList();
       
        updateWizard_paramsettingDevice();
        DeviceInfoList deviceInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");
        for(int i=0;i<idsAll.length;i++)
        {
//        	String color = UtilDevice.getLedColor(idsAll[i]);
//        	if(color.equals("1") || color.equals("2") || color.equals("3"))
//          2010-9-20,Fix up  by longbow.liu,  just display online devices!
        	if((!DeviceStatusMgr.getInstance().isOffLineDevice(idsAll[i])))
        	{
        		DeviceInfo deviceInfo = deviceInfoList.getByIdDevice(idsAll[i]);
        		if(deviceInfo != null)
        		{
        			//By Kevin. The device must: 1. online 2. has variable in wizard_paramsetting
        			if(isInwizard_paramsetting(deviceInfo.getDescription()))
        			{
        				online.add(idsAll[i]);
        			}
        		}
        	}
        }
        Integer[] idsOnline = (Integer[])online.toArray(new Integer[0]);
        int actualsize = 0;
        if(((current_page-1)*MAXIMUM_DEVICE+limit+offset)<=idsOnline.length && (limit+offset)<=MAXIMUM_DEVICE)
        {
        	actualsize = limit;
        }
        else if(current_page*MAXIMUM_DEVICE<idsOnline.length)
        {
        	actualsize = MAXIMUM_DEVICE-offset;
        }
        else
        {
        	actualsize = idsOnline.length-(current_page-1)*MAXIMUM_DEVICE-offset;
        }
        int[] ids = new int[actualsize];
        for(int i=0;i<actualsize;i++)
        {
        	ids[i] = idsOnline[(current_page-1)*MAXIMUM_DEVICE+offset+i];
        }
        String min;
        String max;
        int pk_id;
        result.append("<response>");
        result.append("<devices>");
        PageImpExp pageImpExp = new PageImpExp();
         
        Map varphys =  VarphyBeanList.getByiddeviceFromWizard_paramsetting(us.getLanguage(),us.getIdSite(),ids,true);
        DevMdlBeanList mdlList = new DevMdlBeanList();
        mdlList.retrieve(us.getIdSite(), us.getLanguage());
        //List pa
		for(int i=0;i<actualsize;i++)
		{
			device = deviceList.getDevice(ids[i]);
			if(cmd.equals("init"))
			{
				result.append("<device id='"+device.getIddevice()+"' iddevmdl='"+device.getIddevmdl()+"' name='"+device.getDescription()+"' code='"+device.getCode()+"' status='" +
                    UtilDevice.getLedColor(new Integer(device.getIddevice())) +"'>");
				result.append(pageImpExp.getComboXML(us.getIdSite(),us.getLanguage(), device.getIddevmdl(),mdlList));
			}
			else if(cmd.equals("refresh"))
			{
				result.append("<device id='"+device.getIddevice()+"' status='" +UtilDevice.getLedColor(new Integer(device.getIddevice())) +"'>");
			}
			if(varphys == null)
			{
				result.append("</device>");
				continue;
			}
			List varList = (List)varphys.get(ids[i]);
			if(varList != null && varList.size()>0)
			{
					for(int j=0;j<varList.size();j++)
					{
						varphy = (VarphyBean)varList.get(j);
						result.append("<P id='"+varphy.getId()+"'>");
						String value = ControllerMgr.getInstance().getFromField(varphy).getFormattedValue();
						result.append("<![CDATA["+value+"]]>");
						if(cmd.equals("init"))
						{
							result.append("<![CDATA["+truncateDescription(varphy.getShortDescription())+"]]>");
							min = varphy.getMinValue(); 
			                if (min!=null && min.contains("pk"))
			                {
			                	pk_id = Integer.parseInt(min.substring(2,min.length()));
			                	min = ControllerMgr.getInstance().getFromField(pk_id).getFormattedValue();
			                }
							result.append("<![CDATA["+min+"]]>");
							max = varphy.getMaxValue();
			                if (max!=null && max.contains("pk"))
			                {
			                	pk_id = Integer.parseInt(max.substring(2,max.length()));
			                	max = ControllerMgr.getInstance().getFromField(pk_id).getFormattedValue();
			                }
							result.append("<![CDATA["+max+"]]>");
							result.append("<![CDATA["+varphy.getType()+"]]>");
							result.append("<![CDATA["+varphy.getDecimal()+"]]>");
						}
						result.append("</P>");
					}
			}
			result.append("</device>");
		}
		result.append("</devices>");
		int total_page = idsOnline.length%MAXIMUM_DEVICE==0?idsOnline.length/MAXIMUM_DEVICE:idsOnline.length/MAXIMUM_DEVICE+1;
		result.append("<page><![CDATA["+total_page+"]]></page>");
		result.append("</response>");
		return result.toString();
	}
	private void updateWizard_paramsettingDevice() throws Exception
	{
		 if(wizardDeviceCode == null)
	        {
	        	String sql = "select distinct devmdlcode from wizard_paramsetting";
	        	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
	        	if(rs != null && rs.size()>0)
	        	{
	        		wizardDeviceCode = new String[rs.size()];
	        		Record record = null;
	        		for(int i=0;i<rs.size();i++)
	        		{
	        			record = rs.get(i);
	        			wizardDeviceCode[i] = (String)record.get(0);
	        		}
	        	}
	        }
	}
	private boolean isInwizard_paramsetting(String code)
	{
		if(wizardDeviceCode == null || wizardDeviceCode.length == 0 || code == null)
		{
			return false;
		}
		for(int i=0;i<wizardDeviceCode.length;i++)
		{
			if(code.equals(wizardDeviceCode[i]))
			{
				return true;
			}
		}
		return false;
	}
	private String truncateDescription(String longdes)
	{
		int maxlength = 40;
		String shortdes = longdes;
		if(longdes.length()>maxlength+3)
		{
			shortdes = longdes.substring(0,maxlength)+"...";
		}
		return shortdes;
	}
	
	
	private String createRow(String params)
	{
//		boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
		DispatcherBookList bookList = DispatcherBookList.getInstance();
        StringBuffer result = new StringBuffer();
		if(params.length()>4)
		{
			String mail = params.substring(4);
			String[] mails = mail.split(";");
			for(int i=0;i<mails.length;i++)
			{
//				result.append("<tr><td>"+"<input id='BAH_"+i+"' "+ (OnScreenKey?"class='keyboardInput'":"")+" type='text' size='40' maxlength='40' style=\"font-family:Verdana;font-size:8pt;\" onblur=\"checkOnlyMail(this);\" value='"+bookList.getReceiver(Integer.parseInt(mails[i])).getAddress()+"'/></td><td align='right'><IMG onclick='deleteItem(this);' src='images/actions/removesmall_on.png'/></td></tr>");
				if(!"-1".equals(mails[i])){
					DispatcherBook tmpAddr = null;
					tmpAddr = bookList.getReceiver(Integer.parseInt(mails[i]));
					if(tmpAddr != null)
					result.append(tmpAddr.getAddress()+";");
				}
			}
			return result.toString();
		}
		else
		{
			return "";
		}
	}
	
	
	private String initHACCPReport(UserSession us, Properties prop)
		throws Exception
	{
		LangService lan = LangMgr.getInstance().getLangService(us.getLanguage());
		StringBuffer result = new StringBuffer();
		RuleListBean ruleList = new RuleListBean();
		RuleBean rule = ruleList.loadWizardRulesByRulecode(us.getIdSite(),BWizard.HACCPREPORTRULEBYWIZARD);
		ActionBean printAction = null;
		
		CioPrinter cio = new CioPrinter(us.getIdSite());
		String strReportPrinter = cio.getReportPrinter();
		String strDefaultPrinter = CioPrinter.GetDefaultPrinter();
		result.append("<wizard>");
		//if "print report" checked in setIO
		if(cio.isReportPrinter())
		{
			if(strReportPrinter.length()>0)
			{
				result.append("<printer><![CDATA["+strReportPrinter+"]]></printer>");
			}
			else
			{
				result.append("<printer><![CDATA["+strDefaultPrinter+"]]></printer>");
			}
		}
		else//"print report" not checked, return --
		{
			result.append("<printer><![CDATA[--]]></printer>");
		}
		if(rule != null)
		{
			result.append("<rule>");
			ActionBeanList actionList = new ActionBeanList();
			printAction = actionList.getActionBeansByActionCode(us.getIdSite(),rule.getActionCode(),us.getLanguage()).get("P");
			if(printAction != null)
			{
				result.append("<activerule><![CDATA["+rule.getIsenabled()+"]]></activerule>");
				result.append("<idaction><![CDATA["+printAction.getIdAction()+"]]></idaction>");
				//result.append("<action_param><![CDATA["+printAction.getParameters()+"]]></action_param>");
				String reportid = printAction.getTemplate();
				if(reportid != null && reportid.equals("") == false)
				{
					ReportBean report = ReportBeanList.retrieveReportById(us.getIdSite(),Integer.parseInt(reportid));
					if(report != null)
					{
						result.append("<report_name><![CDATA["+report.getCode()+"]]></report_name>");
						result.append("<idreport><![CDATA["+report.getIdreport()+"]]></idreport>");
						result.append("<layout><![CDATA["+report.getTemplatefile()+"]]></layout>");
						result.append("<interval><![CDATA["+report.getTimelength()+"]]></interval>");
						result.append("<intervalText><![CDATA["+intervalText(report.getTimelength(),lan)+"]]></intervalText>");
						result.append("<output><![CDATA["+report.getOutputtype()+"]]></output>");
						result.append("<is_haccp><![CDATA["+(report.getHaccp()?"1":"0")+"]]></is_haccp>");
						result.append("<frequency><![CDATA["+report.getStep()+"]]></frequency>");
						result.append("<frequencyText><![CDATA["+frequencyText(report.getStep())+"]]></frequencyText>");
					}
				}
				String params = printAction.getParameters();
				result.append("<mailrows><![CDATA["+createRow(params)+"]]></mailrows>");
			}
			if (rule.getIdTimeband().intValue() != 0)
			{
				TimeBandList timebandslist = new TimeBandList(null, BaseConfig.getPlantId(),new Integer(us.getIdSite()));
				TimeBandBean timeBandBean = timebandslist.get(rule.getIdTimeband());
				if(timeBandBean != null)
				{
					result.append("<timebandtype><![CDATA["+timeBandBean.getTimetype()+"]]></timebandtype>");
					if(timeBandBean.getTimetype() == 1)
					{
						result.append("<timeBandValue><![CDATA["+timeBandBean.getTimeband()+"]]></timeBandValue>");
						result.append("<idtimeBand><![CDATA["+timeBandBean.getIdtimeband()+"]]></idtimeBand>");
					}
					else
					{
						result.append("<timeBandCode><![CDATA["+timeBandBean.getTimecode()+"]]></timeBandCode>");
					}
				}
			}
			result.append("</rule>");
		}
		result.append("</wizard>");
		return result.toString();
	}
	
	
	private String insertHACCPReport(UserSession us, Properties prop)
		throws Exception
	{	
		//CHECK Action and delete
		ActionBeanList actionList = new ActionBeanList();
		int actioncode = getActionCode(us.getIdSite(),HACCPREPORTACTIONNMEBYWIZARD,"TRUE");
		if(actioncode != -1)
		{
			if(!ActionBeanList.isActionInRule(us.getIdSite(), actioncode))
			{
				actionList.deleteAllActionByActioncode(us.getIdSite(), actioncode);
			}
			if(ActionBeanList.isActionInRule(us.getIdSite(), actioncode))
			{
				return ACTIONEXIST;
			}
		}
		//check timeband and delete
		String desc = HACCPREPORTTIMEBINDBYWIZARD;
		TimeBandBean timeband= TimeBandList.getTimebandByCode(us.getIdSite(),desc);
		if(timeband != null)
		{
			int idtimeband = timeband.getIdtimeband();
			if(!TimeBandList.isTimebandInRule(us.getIdSite(), idtimeband))
			{
				TimeBandList.deleteRecord(new Object[]
                     {
                         idtimeband, new Integer(us.getIdSite())
                     });
			}
			else
			{
				return TIMEBANDEXIST;
			}
		}
		//1. printer
		String strReportPrinter = prop.getProperty("sel_report_printer");
		CioPrinter printer = new CioPrinter(us.getIdSite());
		if("--".equals(strReportPrinter))
		{
			printer.SaveConfigurationReportPrinter(false,printer.getReportPrinter());
		}
		else
		{
			printer.SaveConfigurationReportPrinter(true, strReportPrinter);
		}
		//2. Save new email
		Properties newEmail = AbstractNewemail(prop);
		BSetIo setIo = new BSetIo(us.getLanguage());
		if(newEmail.size()>0)
		{
			setIo.updateBookAddress(us, newEmail, "E");
			DispatcherBookList bookList = DispatcherBookList.getInstance();
			bookList.reloadReceivers();
		}
		//3. Create report
		VarphyBeanList varList = new VarphyBeanList();
		VarphyBean[] vars = varList.getAllVarNotAlarmsHACCP(us.getLanguage(),us.getIdSite());
		StringBuffer vBuffer = new StringBuffer();
		for(int i=0;i<vars.length;i++)
		{
			vBuffer.append(vars[i].getDevice()+"-"+vars[i].getId()+";");
		}
		String variables = vBuffer.toString();
		prop.setProperty("variables", variables);
		int reportid = ReportBeanList.newReport(us, prop);
		
		//4. Create ation
		String s_param = "";
		String emails = getEmailIds(prop);
		if(emails.equals(""))
		{
			s_param = "1;0";
		}
		else
		{
			s_param = "1;1;"+emails;
		}
		actioncode = actionList.insertActionX(us.getIdSite(), HACCPREPORTACTIONNMEBYWIZARD, true);
		actionList.replaceXActionWithAction(us.getIdSite(), actioncode, "P",String.valueOf(reportid),s_param); 
		
		//5. Create timebind
		prop.put("desc", desc);
		BActSched actSched = new BActSched(us.getLanguage());
		int idTimeband = actSched.AddTimebind(us, prop);
		
		//6. Create rule
		String activerule = prop.getProperty("activerule");
		RuleBean new_rule = new RuleBean();
		//new_rule.setIsenabled("TRUE");
		if("true".equals(activerule))
		{
			new_rule.setIsenabled("TRUE");
		}
		else
		{
			new_rule.setIsenabled("FALSE");
		}
        new_rule.setIdsite(new Integer(us.getIdSite()));
        new_rule.setRuleCode(HACCPREPORTRULEBYWIZARD);
        new_rule.setActionCode(actioncode);
        new_rule.setIdCondition(null);
        new_rule.setIdTimeband(new Integer(idTimeband));
        //remove delay
        //new_rule.setDelay(5*60);
        new_rule.saveRule();
        String msg = HACCPREPORTRULEBYWIZARD + " (enabled)";
        EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),"Config", "W025", new Object[] { msg });

        DirectorMgr.getInstance().mustRestart();
        return "";
	}
	
	
	private void updateHACCPReport(UserSession us, Properties prop)
		throws Exception
	{
		//1. printer
		String strReportPrinter = prop.getProperty("sel_report_printer");
		CioPrinter printer = new CioPrinter(us.getIdSite());
		if("--".equals(strReportPrinter))
		{
			printer.SaveConfigurationReportPrinter(false,printer.getReportPrinter());
		}
		else
		{
			printer.SaveConfigurationReportPrinter(true, strReportPrinter);
		}
		//2. Save new email
		Properties newEmail = AbstractNewemail(prop);
		BSetIo setIo = new BSetIo(us.getLanguage());
		if(newEmail.size()>0)
		{
			setIo.updateBookAddress(us, newEmail, "E");
			DispatcherBookList bookList = DispatcherBookList.getInstance();
			bookList.reloadReceivers();
		}
		//3. update action emails
		int idSite = us.getIdSite();
		int idaction = Integer.parseInt(prop.getProperty("idaction"));
		String s_param = "";
		String emails = getEmailIds(prop);
		if(emails.equals(""))
		{
			s_param = "1;0";
		}
		else
		{
			s_param = "1;1;"+emails;
		}
		ActionBeanList actionList = new ActionBeanList();
		actionList.updateParametersOfAction(idSite, idaction, s_param);
		
		//4. update timeband
		if(prop.get("idtimeBand") != null && prop.get("idtimeBand").equals("") == false)
		{
			Object[] objects = new Object[3];
			objects[0] = prop.get("timeBandValue");
			objects[1] = new Timestamp(System.currentTimeMillis());
			objects[2] = new Integer((String)prop.get("idtimeBand"));
			TimeBandList.updateTimebandonly(objects);
		}
		
		//5. update report name
		String reportName = prop.getProperty("report_name");
		int idreport = Integer.parseInt(prop.getProperty("idreport"));
		ReportBeanList.modifyReportCode(idSite, idreport, reportName);
		
		//6. update rule active
		RuleListBean ruleList = new RuleListBean();
		RuleBean rule = ruleList.loadWizardRulesByRulecode(us.getIdSite(),BWizard.HACCPREPORTRULEBYWIZARD);
		if(rule != null)
		{
			String activerule = prop.getProperty("activerule");
			if("true".equals(activerule) && !"TRUE".equals(rule.getIsenabled()))
			{
				RuleListBean.updateIdeRule(idSite, rule.getIdRule(), "TRUE");
			}
			else if("false".equals(activerule) && !"FALSE".equals(rule.getIsenabled()))
			{
				RuleListBean.updateIdeRule(idSite, rule.getIdRule(), "FALSE");
			}
		}
		
		DirectorMgr.getInstance().mustRestart();
	}
	
	
	private String intervalText(int interval,LangService lan)
	{
		switch(interval)
		{
			case 0:
				return lan.getString("report","now");
			case 86400:
				return lan.getString("report","daily");
			case 604800:
				return lan.getString("report","weekly");
			case -1:
				return lan.getString("report","custom");
		}
		return "";
	}
	
	
	private String frequencyText(int frequency)
	{
		switch(frequency)
		{
		case 5:
			return "5 s";
		case 30:
			return "30 s";
		case 60:
			return "1 m";
		case 300:
			return "5 m";
		case 600:
			return "10 m";
		case 900:
			return "15 m";
		case 1800:
			return "30 m";
		case 3600:
			return "1 h";
		case 43200:
			return "12 h";
		case 86400:
			return "24 h";
		default:
			return "------";
		}
	}
	
	
	//leave return new
	private Properties AbstractNewemail(Properties prop)
	{
		Properties result = new Properties();
		Iterator i = prop.keySet().iterator();
        String[] arr = null;
        String key = "";
        String val = "";

        DispatcherBookList bookList = DispatcherBookList.getInstance();
        while (i.hasNext())
        {
            key = (String) i.next();

            if ((key != null) && key.startsWith("BAH"))
            {
                val = prop.getProperty(key);

                if (val != null)
                {
                    arr = val.split(",");

                    if( bookList.getAddressId("E", arr[2]) == -1)
                    {
                        result.put(key, val);
                    }
                }
            }
        }
        return result;
	}
	
	
	private String getEmailIds(Properties prop)
	{
		String result = "";
		Iterator i = prop.keySet().iterator();
        String[] arr = null;
        String key = "";
        String val = "";

        DispatcherBookList bookList = DispatcherBookList.getInstance();
        while (i.hasNext())
        {
            key = (String) i.next();

            if ((key != null) && key.startsWith("BAH"))
            {
                val = prop.getProperty(key);

                if (val != null)
                {
                    arr = val.split(",");
                    result = bookList.getAddressId("E", arr[2])+";"+result;
                }
            }
        }
        if(result.equals("") == false)
        {
        	result = result.substring(0, result.length()-1);
        }
        return result;
	}
	
	
    /*
     * Metodo invocato per lo STOP del motore
     */
    private void stopPvEngine(UserSession us)
        throws Exception
    {
        long sleep = 1000L;
        // Stop motore
        if (DirectorMgr.getInstance().isStarted())
        {
            DirectorMgr.getInstance().stopEngine(us.getUserName());
            sleep = 10000L;
        }

        // Rest macchina a stati
        ControllerMgr.getInstance().reset();

        // Stop dispatcher
        if (DispatcherMgr.getInstance().isServiceRunning())
        {
            DispatcherMgr.getInstance().stopService();
            sleep = 10000L;
        }

        Thread.sleep(sleep);
    }
    
    
    private void startPvEngine(UserSession us)
        throws Exception
    {
        // Start if trial period or license correct
        if (Information.getInstance().canStartEngine())
        {
            if (DirectorMgr.getInstance().isStopped())
            {
                DirectorMgr.getInstance().reloadConfiguration(us.getUserName());
                DirectorMgr.getInstance().startEngine(us.getUserName());

                if (!DirectorMgr.getInstance().isStopped())
                {
                    if (!DispatcherMgr.getInstance().isServiceRunning())
                    {
                        DispatcherMgr.getInstance().startService(true);
                    }
                }
            }
        } 
        else
            EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S028",null);
        
        Thread.sleep(1000L);
    }
    
    
	/**
	 *------begin--- Subtab-5
	 * @param us
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	public String SubTab5DataAction(UserSession us, Properties prop) throws Exception{
		String cmd=prop.getProperty("cmd");
		String result="";
		if(cmd!=null && "subtab5_init".equalsIgnoreCase(cmd)){
			result=subtab5_init(us,prop);
			return result;
		}else if(cmd!=null && "testio".equalsIgnoreCase(cmd)){
			String[][] temp=testIO(us,prop);
			result="<wizard>";
			result+="<tio><![CDATA["+prop.getProperty("tio")+"]]></tio>";
			for(int i=0;i<temp.length;i++){
				if(temp[i][0]!=null){
					result+="<testresult>";
					result+="<fields><![CDATA["+temp[i][0]+"]]></fields>";
					result+="<values><![CDATA["+temp[i][1]+"]]></values>";
					result+="<status><![CDATA["+temp[i][2]+"]]></status>";
					result+="</testresult>";
					
				}
			}
			result+="</wizard>";
			return result;
		}else if(cmd!=null && "save".equalsIgnoreCase(cmd)){
			 int idsite = us.getIdSite();
			 String sameActionTimeband=prop.getProperty("sameActionTimeband");
             String[] idRule=prop.getProperty("idRule").split(";");
             //Timebands
        	 String[] idTimeBand=prop.getProperty("idTimeBand").split(";");
     		 String idt=prop.getProperty("standardTimeBand");
     		 if(!("D".equals(sameActionTimeband))){
        		 for(int i=0;i<idTimeBand.length;i++){
        			if((idTimeBand[i]!=null) && (!"-1".equals(idTimeBand[i]))){
        				idt=idTimeBand[i];
        				break;
        			}
        		 }
     		 }
             //action
             int actionCode=getAction(us,  prop,ALARM_ACTIONNMEBYWIZARD);
             //if the actioncode is used by another rule,it can not be empty.
             if(actionCode==-11) return "<wizard><error><![CDATA[emptyactionslist]]></error><errorAction><![CDATA["+ALARM_ACTIONNMEBYWIZARD+"]]></errorAction></wizard>";
             if(actionCode==-1) return "";
             if(actionCode == CONFIG_INTERNAL_MODEM) return "<wizard><error><![CDATA[internal_modem]]></error></wizard>";
             if(actionCode == CONFIG_GSM_MODEM) return "<wizard><error><![CDATA[GSM_modem]]></error></wizard>";
             //condition 
        	 String[][] idcondition=getCondition(us,prop,ALARM_CONDITIONBYWIZARD);
        	 
        	 for(int i=0;i<idcondition.length;i++){
        		 if(("".equals(idcondition[i][1]))){
    				 //delete a rule because priority is empty 
        			 RuleListBean.deleteRule(us.getIdSite(),ALARM_RULEBYWIZARD+idcondition[i][0]);
        			 continue;	 
    			 }else if(("D".equals(sameActionTimeband))){
    				 //or user want to overwrite it for they have different action/timeband
    				 RuleListBean.deleteRule(us.getIdSite(),ALARM_RULEBYWIZARD+idcondition[i][0]);
    			 }
        		 
        		 if((idRule[i]!=null) && (!"".equals(idRule[i])) && (!"-1".equals(idRule[i]))){
        			//update a wizard rule 
        			 RuleListBean.updateRule(new Integer(idsite), new Integer(idRule[i]), 
	                		(actionCode>0 && !"".equals(idcondition[i][1]))?"TRUE":"FALSE", ALARM_RULEBYWIZARD+idcondition[i][0],
	                		new Integer(idcondition[i][1]), new Integer(idt), 
	                		actionCode, 0, us.getUserName());
            		}else{
		            	 //add a wizard rule
		                 RuleBean new_rule = new RuleBean();
		                 new_rule.setIdsite(new Integer(idsite));
		                 new_rule.setRuleCode(ALARM_RULEBYWIZARD+idcondition[i][0]);
		                 new_rule.setActionCode(actionCode);
		                 new_rule.setIdCondition(new Integer(idcondition[i][1]));
		                 new_rule.setIdTimeband(new Integer(idt));
		                 new_rule.setDelay(0);
		                 new_rule.setIsenabled((actionCode>0 && !"".equals(idcondition[i][1]))?"TRUE":"FALSE");
		                 new_rule.saveRule();
		             }
        	 }
             String msg = ALARM_RULEBYWIZARD + " (enabled)";
             EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(),"Config", "W025", new Object[] { msg });
             DirectorMgr.getInstance().mustRestart();
             return "<error><![CDATA[ok]]></error>";
		}
		return result;
	}
	
	private String subtab5_init(UserSession us, Properties prop){
		/**----page init--**/
		StringBuffer result=new StringBuffer();
		result.append("<wizard>");
		try {
			int idSite = us.getIdSite();
			RuleListBean ruleList = new RuleListBean();
			String idCondition="";
			String idTimeband="";
			String actionCode="";
			String idRule="";
			boolean isP=false;
			String highest="";
			String hight="";
			String medium="";
			String low="";
			String emails="";
			String faxs="";
			String smss="";
			String act_mail="";
			String act_fax="";
			String act_relay="";
			String act_sms="";
			String smtp="";
			String port="";
			String user="";
			String sender="";
			String pwd="";
			String encryption = "";
			String[][] relays=new String[0][2];
			String tempAction="",tempTimeBand="";
			boolean diff=false;
			
			//wizard Timebands
			TimeBandList timeBandList = new TimeBandList(null,BaseConfig.getPlantId(), idSite);
			//default timeband "Sempre/Always/Toujours/Immer"
			String standardTimeBand="1";
			result.append("<standardTimeBand><![CDATA["+standardTimeBand+"]]></standardTimeBand>");
			//email smtp
			CioMAIL mail = new CioMAIL(idSite);
			mail.loadConfiguration();
			smtp=mail.getSmtp();
			port = "" + mail.getPort();
			user=mail.getUser();
			pwd=mail.getPass();
			sender=mail.getSender();
			encryption = mail.getEncryption();
			result.append("<smtp><![CDATA["+smtp+"]]></smtp>");
			result.append("<port><![CDATA["+port+"]]></port>");
			result.append("<user><![CDATA["+user+"]]></user>");
			result.append("<sender><![CDATA["+sender+"]]></sender>");
			result.append("<pwd><![CDATA["+pwd+"]]></pwd>");
			result.append("<encryption><![CDATA["+encryption+"]]></encryption>");
			//init rule
			for(int i=0;i<priors.length;i++){
				RuleBean rule=ruleList.loadWizardRulesByRulecode(idSite,BWizard.ALARM_RULEBYWIZARD+priors[i]);
				String idc="",idr="",idt="",ida="";
				if(rule!=null){
					idc=String.valueOf(rule.getIdCondition());
					idt=String.valueOf(rule.getIdTimeband());
					ida=String.valueOf(rule.getActionCode());
					idr=String.valueOf(rule.getIdRule());
					
					//if there are some different action/timeband,show the page empty.
					if(("".equals(tempAction)) || ("".equals(tempTimeBand))){
						if(("".equals(tempAction)))	tempAction=ida;
						if(("".equals(tempTimeBand)))	tempTimeBand=idt;
					}else if((!ida.equals(tempAction)) || (!idt.equals(tempTimeBand))){
						idCondition="";
						idTimeband="";
						actionCode="";
						idRule="";
						for(int j=0;j<priors.length;j++){
							idCondition+="-1;";
							idTimeband+="-1;";
							actionCode+="-1;";
							idRule+="-1;";
						}
						diff=true;
						break;
					}
				}
				idCondition+=("".equals(idc)?"-1":idc)+";";
				idTimeband+=("".equals(idt)?"-1":idt)+";";
				actionCode+=("".equals(ida)?"-1":ida)+";";
				idRule+=("".equals(idr)?"-1":idr)+";";
			}
			result.append("<sameActionTimeband><![CDATA["+((diff==true)?"D":"S")+"]]></sameActionTimeband>");
			//if there is not different action/timeband do below:
			if(diff==false){
				//wizard condition
				String[] idc=idCondition.split(";");
				for(int i=0;i<idc.length;i++){
					if((idc[i]!=null) && (!"-1".equals(idc[i]))){
						ConditionBeanList conditionList=new ConditionBeanList(idSite,us.getLanguage());
						ConditionBean cond = conditionList.loadGeneralCondition(new Integer(idc[i]));
						if(cond!=null){
							String typeCond=cond.getTypeCondition();
							if("V".equalsIgnoreCase(typeCond)){
								isP=false;
							}else if("P".equalsIgnoreCase(typeCond)){
								isP=true;
								int[] varLst=conditionList.retriveConditionVariables(new Integer(idc[i])); 
								for(int j=0;j<varLst.length;j++){
									if(varLst[j]==1) highest="checked";
									if(varLst[j]==2) hight="checked";
									if(varLst[j]==3) medium="checked";
									if(varLst[j]==4) low="checked";
								}
							}
						}
					}
				}
				//wizard action
				String[] ida=actionCode.split(";");
				for(int i=0;i<ida.length;i++){
					if((ida[i]!=null) && (!"-1".equals(ida[i]))){
						//reload the info
						DispatcherBookList bookList = DispatcherBookList.getInstance();
						bookList.reloadReceivers();
						
						ActionBeanList actionlist = new ActionBeanList();
					    String param = actionlist.getActionParameters(idSite, Integer.parseInt(ida[i]), "E");
					    if ((param!=null) && (!param.equals(""))){
					    	String[] ids = param.split(";");
					    	act_mail="checked";
							for(int ii=0;ii<ids.length;ii++){
								emails+=bookList.getReceiver(Integer.parseInt(ids[ii])).getAddress()+((ii<ids.length-1)?";":"");
							}
					    }
					   //fax
					   param = actionlist.getActionParameters(idSite, Integer.parseInt(ida[i]), "F");
					   if ((param!=null) && (!param.equals(""))){
					    	String[] ids = param.split(";");
					    	act_fax="checked";
							for(int ii=0;ii<ids.length;ii++){
								faxs+=bookList.getReceiver(Integer.parseInt(ids[ii])).getAddress()+((ii<ids.length-1)?";":"");
							}
					    }
					   //Relay
					   param = actionlist.getActionParameters(idSite, Integer.parseInt(ida[i]), "L");
					    if ((param!=null) && (!param.equals(""))){
					        String[] paramArray = param.split(";");
					        if(paramArray!=null && paramArray.length>0) act_relay="checked";
					        relays=new String[paramArray.length][2];
					        for (int ii = 0; ii < paramArray.length; ii++){
					        	//---please modify the relay address according to the physical relay address
					        	relays[ii][0]=paramArray[ii].split("=")[0];
					        	relays[ii][1]=paramArray[ii].split("=")[1];
					        }
					    }
					    //SMS
					    param = actionlist.getActionParameters(idSite, Integer.parseInt(ida[i]), "S");
					    if ((param!=null) && (!param.equals(""))){
					    	String[] ids = param.split(";");
							act_sms="checked";
							for(int ii=0;ii<ids.length;ii++){
								smss+=bookList.getReceiver(Integer.parseInt(ids[ii])).getAddress()+((ii<ids.length-1)?";":"");
							}
					    }
					    break;
					}
				}
			}
			result.append("<idTimeBand><![CDATA["+idTimeband+"]]></idTimeBand>");
			result.append("<idCondition><![CDATA["+idCondition+"]]></idCondition>");
			result.append("<actionCode><![CDATA["+actionCode+"]]></actionCode>");
			result.append("<idRule><![CDATA["+idRule+"]]></idRule>");

			result.append("<isP><![CDATA["+isP+"]]></isP>");
			result.append("<highest><![CDATA["+highest+"]]></highest>");
			result.append("<hight><![CDATA["+hight+"]]></hight>");
			result.append("<medium><![CDATA["+medium+"]]></medium>");
			result.append("<low><![CDATA["+low+"]]></low>");

			result.append("<act_mail><![CDATA["+act_mail+"]]></act_mail>");
		    result.append("<emails><![CDATA["+emails+"]]></emails>");
		    result.append("<act_fax><![CDATA["+act_fax+"]]></act_fax>");
		    result.append("<faxs><![CDATA["+faxs+"]]></faxs>");
		    result.append("<act_relay><![CDATA["+act_relay+"]]></act_relay>");
		    for(int i=0;i<relays.length;i++){
		    	result.append("<relays><addr><![CDATA["+relays[i][0]+"]]></addr><valu><![CDATA["+relays[i][1]+"]]></valu></relays>");
		    }
		    result.append("<act_sms><![CDATA["+act_sms+"]]></act_sms>");
		    result.append("<smss><![CDATA["+smss+"]]></smss>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.append("</wizard>");
		return result.toString();
	}
	
	
	private String[][] testIO(UserSession us, Properties prop){
		//test I/O function
		String pvcode = BaseConfig.getPlantId();
        String devy = prop.getProperty("tio");
        String dest = "";
        String valu = "0";
        String[][] result=new String[0][3];
        int idEvent=-1;
        //mail
        if("E".equalsIgnoreCase(devy)){
	    	String[] temp=prop.getProperty("emails").split(";");
        	if(temp!=null){
        		result=new String[temp.length][3];
        		for(int i=0;i<temp.length;i++){
    	    		if(temp[i]!=null && !"".equals(temp[i])){
    	    			dest=getIdAddrBookByAddr(us.getIdSite(),"E",temp[i]);
    	    			if(!"0".equals(dest)){
    	    				idEvent=DispSimMgr.getInstance().setTestIo(pvcode, devy, dest, valu, us.getUserName(),us.getLanguage());
        	    			Event event=EventMgr.getInstance().getEventById(idEvent, us.getIdSite(), us.getLanguage());
        	    			result[i][0]=temp[i];
        	    			result[i][1]=event.getMessage();
        	    			result[i][2]=String.valueOf(event.getType());
    	    			}
    	    		}
    	    	}
        	}
        }
        //fax
        if("F".equalsIgnoreCase(devy)){
        	CioFAX fax = new CioFAX(us.getIdSite());
            fax.loadConfiguration();
        	DispatcherDirectly dd=new DispatcherDirectly(fax.getModemId());
        	String[] temp=prop.getProperty("faxs").split(";");
        	if(temp!=null){
        		result=new String[temp.length][3];
        		for(int i=0;i<temp.length;i++){
    	    		if(temp[i]!=null && !"".equals(temp[i])){
    	    			dest=getIdAddrBookByAddr(us.getIdSite(),"F",temp[i]);
    	    			if(!"0".equals(dest)){
    	    				idEvent=DispSimMgr.getInstance().setTestIo(pvcode, devy, dest, valu, us.getUserName(),us.getLanguage());
    	    				idEvent=dd.doTestIO(us.getIdSite(), "F", dest, "1", "-1");
	    	    			Event event=EventMgr.getInstance().getEventById(idEvent, us.getIdSite(), us.getLanguage());
        	    			result[i][0]=temp[i];
        	    			result[i][1]=event.getMessage();
        	    			result[i][2]=String.valueOf(event.getType());
    	    			}
    		    	}
    	    	}
        	}
        }
        //relay
        if("L".equalsIgnoreCase(devy)){
        	int lcount=0;
        	if(prop.getProperty("relay_1")!=null) lcount++;
        	if(prop.getProperty("relay_2")!=null) lcount++;
        	if(prop.getProperty("relay_3")!=null) lcount++;
        	result=new String[lcount][3];
        	lcount=0;
        	if(prop.getProperty("relay_1")!=null){
        		dest=prop.getProperty("relay_1_addr");
	    		valu = prop.getProperty("relay_1");
    			//do the relay test directly
    	    	try {
					SetDequeuerDirectly sd=new SetDequeuerDirectly();
					int idvariable = getIdvariableByIdRelay(us.getIdSite(),dest);
					SetContext setContext = new SetContext();
					setContext.setLanguagecode(us.getLanguage());
					// physical status of internal IO Relays is always the opposite of the 'logical' status
					// that is set on the 'cfaction' table
					setContext.addVariable(idvariable, valu.equals("0")?1:0);
					setContext.setCallback(new BackGroundCallBack());
					setContext.setUser(us.getUserName());
					setContext.setIsTest(true);
					idEvent=sd.tryToSend(setContext);
					Event event=EventMgr.getInstance().getEventById(idEvent, us.getIdSite(), us.getLanguage());
	    			result[lcount][0]=dest;
	    			result[lcount][1]=event.getMessage();
	    			result[lcount][2]=String.valueOf(event.getType());
	    			++lcount;
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
	    	if(prop.getProperty("relay_2")!=null){
	    		dest=prop.getProperty("relay_2_addr");
	    		valu = prop.getProperty("relay_2");
    			//do the relay test directly
	    		try {
					SetDequeuerDirectly sd=new SetDequeuerDirectly();
					int idvariable = getIdvariableByIdRelay(us.getIdSite(),dest);
					SetContext setContext = new SetContext();
					setContext.setLanguagecode(us.getLanguage());
					// physical status of internal IO Relays is always the opposite of the 'logical' status
					// that is set on the 'cfaction' table
					setContext.addVariable(idvariable, valu.equals("0")?1:0);
					setContext.setCallback(new BackGroundCallBack());
					setContext.setUser(us.getUserName());
					setContext.setIsTest(true);
					idEvent=sd.tryToSend(setContext);
					Event event=EventMgr.getInstance().getEventById(idEvent, us.getIdSite(), us.getLanguage());
	    			result[lcount][0]=dest;
	    			result[lcount][1]=event.getMessage();
	    			result[lcount][2]=String.valueOf(event.getType());
	    			++lcount;
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
	    	if(prop.getProperty("relay_3")!=null){
	    		dest=prop.getProperty("relay_3_addr");
	    		valu = prop.getProperty("relay_3");
    			//do the relay test directly
	    		try {
					SetDequeuerDirectly sd=new SetDequeuerDirectly();
					int idvariable = getIdvariableByIdRelay(us.getIdSite(),dest);
					SetContext setContext = new SetContext();
					setContext.setLanguagecode(us.getLanguage());
					// physical status of internal IO Relays is always the opposite of the 'logical' status
					// that is set on the 'cfaction' table
					setContext.addVariable(idvariable, valu.equals("0")?1:0);
					setContext.setCallback(new BackGroundCallBack());
					setContext.setUser(us.getUserName());
					setContext.setIsTest(true);
					idEvent=sd.tryToSend(setContext);
					Event event=EventMgr.getInstance().getEventById(idEvent, us.getIdSite(), us.getLanguage());
	    			result[lcount][0]=dest;
	    			result[lcount][1]=event.getMessage();
	    			result[lcount][2]=String.valueOf(event.getType());
	    			++lcount;
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
	    	
		}
        //sms
        if("S".equalsIgnoreCase(devy)){
        	CioSMS sms = new CioSMS(us.getIdSite());
            sms.loadConfiguration();
            DispatcherDirectly dd=new DispatcherDirectly(sms.getLabelModem());
        	String[] temp=prop.getProperty("smss").split(";");
        	if(temp!=null){
        		result=new String[temp.length][3];
        		for(int i=0;i<temp.length;i++){
    	    		if(temp[i]!=null && !"".equals(temp[i])){
    	    			dest=getIdAddrBookByAddr(us.getIdSite(),"S",temp[i]);
    	    			if(!"0".equals(dest)){
    	    				idEvent=DispSimMgr.getInstance().setTestIo(pvcode, devy, dest, valu, us.getUserName(),us.getLanguage());
    	    				idEvent=dd.doTestIO(us.getIdSite(), "S", dest, "1", "-1");
    	    			}
    	    			Event event=EventMgr.getInstance().getEventById(idEvent, us.getIdSite(), us.getLanguage());
    	    			result[i][0]=temp[i];
    	    			result[i][1]=event.getMessage();
    	    			result[i][2]=String.valueOf(event.getType());
        		    }
    	    	}
        	}
        }
        return result;
	}
	
	
    private String[][] getCondition(UserSession us, Properties prop,String desc){
    	String[][] result=new String[5][2];
    	try {
			ConditionBeanListPres condList = new ConditionBeanListPres(us.getIdSite(),us.getLanguage());
			String type=prop.getProperty("condtype");
			for(int i=0;i<priors.length;i++){
				result[i][0]=priors[i];
				result[i][1]="";
			}
			
			String[] prio=new String[]{("1".equals(type))?"0":null,prop.getProperty("priority1"),prop.getProperty("priority2"),prop.getProperty("priority3"),prop.getProperty("priority4")};
			
			String idconds=prop.getProperty("idCondition");
			String[] iCons=idconds.split(";");
			for(int i=0;i<result.length;i++){
				if((prio!=null) && (prio[i]!=null) && (!"".equals(prio[i]))){
					int existIdcond=getCfConditionIdByCondcode(us.getIdSite(),desc+result[i][0]);
				    if ((iCons[i] != null) && (!"".equals(iCons[i])) && (!"-1".equals(iCons[i]))){
				    	//the custom's condition is exist
				        condList.updateAlarmCondition(iCons[i], "", type, prio[i]);
				        result[i][1]=iCons[i];
				    }else if(existIdcond>0){
				    	//the standard condition is exist
				    	 condList.updateAlarmCondition(String.valueOf(existIdcond), "", type,prio[i]);
				    	 result[i][1]=String.valueOf(existIdcond);
				    }else {
				    	//it is empty
				    	condList.insertAlarmCondition(BaseConfig.getPlantId(), desc+result[i][0], type, "",prio[i]);
				    	result[i][1]=String.valueOf(getCfConditionIdByCondcode(us.getIdSite(),desc+result[i][0]));
				    }
				}
			}    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
   
    
    private int getAction(UserSession us, Properties prop,String desc){
    	int runActionCode=-1;
    	String runCodeDesc="";
    	
		String ccode=prop.getProperty("actionCode");
		String[]cd=ccode.split(";");
		ccode="";
		for(int i=0;i<cd.length;i++){
			if((cd[i]!=null) && (!"-1".equals(cd[i]))){
				ccode=cd[i];
				break;
			}
		}
		if ((ccode != null) && (!"".equals(ccode))){
			runActionCode=Integer.parseInt(ccode);
			runCodeDesc=getCodeDescByActionCode(us.getIdSite(),ccode);
		}else{
			runActionCode=getActionCode(us.getIdSite(),desc,"FALSE");
			runCodeDesc=desc;
		}
		boolean esy = false;
		
    	try {
			ActionBeanList abl = new ActionBeanList();
			
			//save smtp configuration
			String strSmtp = prop.getProperty("smtp");
			String strPort = prop.getProperty("port");
			String strSender = prop.getProperty("sender");			
			if( (strSmtp != null && strSmtp.trim().length() > 0)
				&& (strSender != null && strSender.trim().length() > 0) )
			{
		    	CioMAIL mail = new CioMAIL(us.getIdSite());
		    	mail.loadConfiguration();
		    	
	            mail.setUser(prop.getProperty("user"));
	            mail.setPass(prop.getProperty("pwd"));
	            String strEncryption = prop.getProperty("encryption");
	            mail.setEncryption(strEncryption);
	            try {
	            	mail.setPort(Integer.parseInt(strPort));
	            }
	            catch(Exception e) {
	            	mail.setPort(strEncryption.equals("TLS") ? 587 : 25);
	            }
	            esy = mail.saveConfiguration(mail.getIdConf(), prop.getProperty("smtp"), prop.getProperty("sender"), "L", "", 0,0);
	            if (esy){
	                DispMemMgr.getInstance().storeConfiguration("E");
	            }
			}
  
            //check if the actions are all deleted     
            if(runActionCode!=-1){
            	
            	if(!ActionBeanList.isActionInRule(us.getIdSite(), runActionCode))
            	{
            		abl.deleteAllActionByActioncode(us.getIdSite(), runActionCode);
            	}
            	else
            	{
		            int addCount=0;
		            if(prop.getProperty("act_mail")!=null && prop.getProperty("act_mail").equals("1")){
		            	addCount++;
		            }
		            if(prop.getProperty("act_fax")!=null && prop.getProperty("act_fax").equals("1")){
		            	addCount++;
		            }
		            if(prop.getProperty("act_relay")!=null && prop.getProperty("act_relay").equals("1")){
		            	addCount++;
		            }
		            if(prop.getProperty("act_sms")!=null && prop.getProperty("act_sms").equals("1")){
		            	addCount++;
		            }
		            if(addCount==0){
		            	//if it will delete all actions ,return -11
		            	return -11;
		            }
            	}
            }
            
            //mail
            String ms="";
	    	String[] temp=prop.getProperty("emails").split(";");
	    	if(temp!=null && temp.length>0){
		    	for(int i=0;i<temp.length;i++){
		    		if(temp[i]!=null && !"".equals(temp[i])){
			    		ms+=String.valueOf(appendAddrBook(us.getIdSite(),"E",temp[i]))+";";
			    	}
		    	}
		    }
	    	//append mail action
	    	if(prop.getProperty("act_mail")!=null && prop.getProperty("act_mail").equals("1")){
	    		abl.insertAutomaticAction(us.getIdSite(), "E", ms, BSetAction.UNSCHEDULERTEMPLATE, runCodeDesc);
	 	    }else{
	    		abl.deleteAction(us.getIdSite(), runActionCode, "E");
	    	}
	    	
	    	
	    	//fax
	    	ms="";
	    	temp=prop.getProperty("faxs").split(";");
	    	if(temp!=null && temp.length>0){
		    	for(int i=0;i<temp.length;i++){
		    		if(temp[i]!=null && !"".equals(temp[i])){
			    		ms+=String.valueOf(appendAddrBook(us.getIdSite(),"F",temp[i]))+";";
			    	}
		    	}
	    	}
	    	//save fax configuration
	    	if(ms.length()>0)
	    	{
	            CioFAX fax = new CioFAX(us.getIdSite());
	            fax.loadConfiguration();
	            if(fax.getIdconf()<0){
			        String modem= BaseConfig.getHardwareProperty(CioFAX.INTERNAL_MODEM);
			        if(modem == null || !fax.checkMedemExist(modem))
			        {
			        	return CONFIG_INTERNAL_MODEM;
			        }
			        String type="A",centra="0";
			        int trynum=0,retry=0;
		        
			        esy = fax.saveConfiguration(fax.getIdconf(), modem, type, trynum, retry, centra);
		            if (esy){
		                DispMemMgr.getInstance().storeConfiguration("F");
		            }
		        }
	    	}
	    	//append fax action
	    	if(prop.getProperty("act_fax")!=null && prop.getProperty("act_fax").equals("1")){
	    		abl.insertAutomaticAction(us.getIdSite(), "F", ms, BSetAction.UNSCHEDULERTEMPLATE, runCodeDesc);
	 	    }else{
	    		abl.deleteAction(us.getIdSite(), runActionCode, "F");
	    	}
	    	
	    	//relay
	    	ms="";
	    	if(prop.getProperty("relay_1")!=null){
	    		ms+=prop.getProperty("relay_1_addr")+"="+prop.getProperty("relay_1")+";";
	    	}
	    	if(prop.getProperty("relay_2")!=null){
	    		ms+=prop.getProperty("relay_2_addr")+"="+prop.getProperty("relay_2")+";";
		    }
	    	if(prop.getProperty("relay_3")!=null){
	    		ms+=prop.getProperty("relay_3_addr")+"="+prop.getProperty("relay_3")+";";
		    }
	    	//append relay action
	    	if(prop.getProperty("act_relay")!=null && prop.getProperty("act_relay").equals("1")){
	    		abl.insertAutomaticAction(us.getIdSite(), "L", ms, BSetAction.UNSCHEDULERTEMPLATE, runCodeDesc);
	 	    }else{
	    		abl.deleteAction(us.getIdSite(), runActionCode, "L");
	    	}
	    	
	    	//sms
	    	ms="";
	    	temp=prop.getProperty("smss").split(";");
	    	if(temp!=null && temp.length>0){
		    	for(int i=0;i<temp.length;i++){
		    		if((temp[i]!=null) && (!"".equals(temp[i]))){
			    		ms+=String.valueOf(appendAddrBook(us.getIdSite(),"S",temp[i]))+";";
			    	}
		    	}
	    	}
	    	//save sms configuration
	    	if(ms.length()>0)
	    	{
	            CioSMS sms = new CioSMS(us.getIdSite());
	            sms.loadConfiguration();
	            if(sms.getIdConf()<0){
		            String[][] p=sms.getProvider();
		            String labprovider="";
		            String modem=BaseConfig.getHardwareProperty(CioSMS.GSM_MODEM);
		            if(modem == null || !sms.checkMedemExist(modem))
			        {
			        	return CONFIG_GSM_MODEM;
			        }
		            String gsm_provider = BaseConfig.getHardwareProperty(CioSMS.GSM_PROVIDER);
		            if(gsm_provider == null || gsm_provider.trim().length()==0)
		            {
		            	return CONFIG_GSM_MODEM;
		            }
		            String type="A",call="0",centra="0";
		            int provider=0,trynum=0,retry=0;
		            boolean providerExist = false;
		            for(int i=0;i<p.length;i++){
		            	if(p[i][1].equalsIgnoreCase(gsm_provider)){
		            		provider=Integer.parseInt(p[i][0]);
		            		labprovider=p[i][1];
		            		providerExist = true;
		            		break;
		            	}
		            }
		            if(!providerExist)
		            	return CONFIG_GSM_MODEM;
		            esy = sms.saveConfiguration(sms.getIdConf(), modem, type, provider, call, trynum, retry,centra, labprovider);
		            if (esy){
		                DispMemMgr.getInstance().storeConfiguration("S");
		            }
	            }
	    	}
	    	//append sms action
	    	if(prop.getProperty("act_sms")!=null && prop.getProperty("act_sms").equals("1")){
	    		abl.insertAutomaticAction(us.getIdSite(), "S", ms, BSetAction.UNSCHEDULERTEMPLATE, runCodeDesc);
	 	    }else{
	    		abl.deleteAction(us.getIdSite(), runActionCode, "S");
	    	}
	    	//reload the info
			DispatcherBookList bookList = DispatcherBookList.getInstance();
			bookList.reloadReceivers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getActionCode(us.getIdSite(),runCodeDesc,"FALSE");
    }
    
    
    private int getCfConditionIdByCondcode(int idsite,String desc){
        int idcond = 0;
        String sql = "select * from cfcondition where idsite=? and condcode=?";
        try{
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idsite),desc});
            if ((rs != null) && (rs.size() > 0)){
            	// Alessandro : I need only the first row
        		Record r = rs.get(0);
            	idcond = ((Integer) r.get("idcondition")).intValue();
            }
        } catch(Exception e){
        	e.printStackTrace();
        }
        return idcond; 
    }
    
    
    private int getActionCode(int idsite,String desc,String isSched){
		int find = -1;
		try {
			String sql = "select actioncode from cfaction where idsite=? and code=? and isscheduled=?";
			RecordSet rs = null;
			Record r = null;
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idsite),desc,isSched});
			if(rs != null && rs.size() > 0)
				r = rs.get(0);
			if(r != null)
				find = ((Integer)r.get("actioncode")).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return find;
    }
    
    
    private int getCountByActionCode(Integer idSite,Integer actioncode){
		try {
			String sql = "select * from cfaction where idsite=? and actioncode=?";
			RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,new Object[] { idSite,actioncode});
			return recordSet.size();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return 0;
    }
    
    
    private String getCodeDescByActionCode(int idsite,String actioncode){
    	String find = "";
		try {
			String sql = "select code from cfaction where idsite=? and actioncode=? and isscheduled=?";
			RecordSet rs = null;
			Record r = null;
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idsite),actioncode,"FALSE"});
			if(rs != null && rs.size() > 0)
				r = rs.get(0);
			if(r != null)
				find = ((String)r.get("code")).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return find;
    }
    
    
    private int getUsedActionCountNotWizardRuleByActionCode(Integer idSite,Integer actioncode){
		try {
			Object[] obj=new Object[priors.length+2];
			obj[0]=idSite;
			obj[1]=actioncode;
			String sqlTemp="";
			for(int i=0;i<priors.length;i++){
				sqlTemp+=(("".equals(sqlTemp))?"":",")+"?";
				obj[i+2]=ALARM_RULEBYWIZARD+priors[i];
			}
			String sql = "select * from cfrule where idsite = ? and actioncode = ? "+(("".equals(sqlTemp))?"":"and rulecode not in (?,?,?,?,?)");
			
			RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null, sql,obj);
			return recordSet.size();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return 0;
    }
    
    
    private String getIdAddrBookByAddr(int idsite,String type,String addr){
		String find = "0";
		try {
			String sql = "select idaddrbook from cfaddrbook where idsite=? and type=? and address in (?)";
			RecordSet rs = null;
			Record r = null;
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{idsite,type,addr});
			if(rs != null && rs.size() > 0)
				r = rs.get(0);
			if(r != null)
				find = r.get("idaddrbook").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return find;
    }
    
    
    private int appendAddrBook(int idsite,String type,String addr){
		String dest=getIdAddrBookByAddr(idsite,type,addr);
		if(dest==null || "0".equals(dest)){
			return DispatcherBookList.getInstance().addNewRecepient(BaseConfig.getPlantId(), String.valueOf(idsite),type,addr,addr);
		}
		return Integer.parseInt(dest);
    }
    
    
    private Integer getIdvariableByIdRelay(int idsite,String idrelay){
		Integer find=-1;
		try {
			Object[] obj=new Object[]{idsite,idrelay};
			String sql = "select idvariable from cfrelay where idsite=?  and idrelay=?";
			RecordSet rs = null;
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,obj);
			if ((rs != null) && (rs.size() > 0)){
				Record r = rs.get(0);
				find=((Integer) r.get("idvariable")).intValue();
			}
		} catch (DataBaseException e) {
			e.printStackTrace();
		}
		return find;
    }
}
/**
*--------End-----Subtab-5
**/