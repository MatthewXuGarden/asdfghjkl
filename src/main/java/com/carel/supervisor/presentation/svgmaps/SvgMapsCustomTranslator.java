package com.carel.supervisor.presentation.svgmaps;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.VDMappingMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


public class SvgMapsCustomTranslator
{	
	private static String GET_DEV_LINK = "get_device_link";
	private static String GET_DEV_PARAMS_LINK = "get_device_params_link";
	private static String GET_DEV_ALARMS_LINK = "get_device_alarms_link";
	private static String GET_DEV_HACCP_LINK = "get_device_haccp_link";
	private static String GET_DEV_TRENDS_LINK = "get_device_trends_link";
	private static String GET_EVENTS_LINK = "get_events_link";
	private static String GET_ALARMS_LINK = "get_alarms_link";
	private static String GET_RELAY_LINK = "get_relay_link";
	private static String GET_REPORT_LINK = "get_report_link";
	private static String GET_NODE_DESCR = "get_node_descr";
	private static String GET_VAR_UM = "get_var_um";
	private static String GET_NODE_STATUS = "get_node_status";
	private static String GET_ACCEESS_RIGHT = "get_access_right";
	
		
	private static Map<String,String> LinksMap = new HashMap<String,String>(){
   
		private static final long serialVersionUID = -1266756980157435007L;

		{
            put(GET_DEV_LINK, "top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&resource=SubTab1.jsp&curTab=tab1name&maps=y&type=click&iddev="+"$linenum$"+"-"+"$devicenum$"+"&desc=');");
            put(GET_DEV_PARAMS_LINK, "top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&resource=SubTab2.jsp&curTab=tab2name&maps=y&type=click&iddev="+"$linenum$"+"-"+"$devicenum$"+"&desc=');");
            put(GET_DEV_ALARMS_LINK, "top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&resource=SubTab3.jsp&curTab=tab3name&maps=y&type=click&iddev="+"$linenum$"+"-"+"$devicenum$"+"&desc=');");
            put(GET_DEV_HACCP_LINK,  "top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&resource=SubTab4.jsp&curTab=tab4name&maps=y&type=click&iddev="+"$linenum$"+"-"+"$devicenum$"+"&desc=');");
            put(GET_DEV_TRENDS_LINK, "top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&resource=SubTab5.jsp&curTab=tab5name&maps=y&type=click&iddev="+"$linenum$"+"-"+"$devicenum$"+"&desc=');");
            put(GET_EVENTS_LINK, "top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=evnview&bo=BEvnView&type=click');");
            put(GET_ALARMS_LINK, "top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=alrglb&bo=BAlrGlb&resource=SubTab1.jsp&curTab=tab1&type=click');");
            put(GET_RELAY_LINK, "top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=relaymgr&bo=BRelayMgr&resource=SubTab1.jsp&curTab=tab1name&maps=y&type=click');");
            put(GET_REPORT_LINK, "top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=report&bo=BReport&resource=SubTab1.jsp&curTab=tab1name&maps=y&type=click');"); 
        };
    };
    
  	public static String manageJsonFncRequest(HttpServletRequest request) throws IOException, ServletException
	{
		UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
		
		Gson gson = new Gson();
		
		Map request_params = request.getParameterMap();
		String jsonresp = "";
		
		
		/*************************************************************
 		 * 				CUSTOM FUNCIONALITIES
 		 *************************************************************/
		
		// LINKS Management: return the url corresponding to the type of link requested
		
		String req = null;
		if(request_params.containsKey(GET_DEV_LINK))
			req= userSession.isTabActive("dtlview","tab1name")?GET_DEV_LINK:"noaccess";
		else if(request_params.containsKey(GET_DEV_ALARMS_LINK))
			req = userSession.isTabActive("dtlview","tab3name")?GET_DEV_ALARMS_LINK:"noaccess";
		else if(request_params.containsKey(GET_DEV_HACCP_LINK))
			req = userSession.isTabActive("dtlview","tab4name")?GET_DEV_HACCP_LINK:"noaccess";
		else if(request_params.containsKey(GET_DEV_PARAMS_LINK))
			req = userSession.isTabActive("dtlview","tab2name")?GET_DEV_PARAMS_LINK:"noaccess";
		else if(request_params.containsKey(GET_DEV_TRENDS_LINK))
			req = userSession.isTabActive("dtlview","tab5name")?GET_DEV_TRENDS_LINK:"noaccess";
		else if(request_params.containsKey(GET_EVENTS_LINK))
			req = userSession.isMenuActive("evnview")?GET_EVENTS_LINK:"noaccess";
		else if(request_params.containsKey(GET_ALARMS_LINK))
			req = userSession.isMenuActive("alrglb")?GET_ALARMS_LINK:"noaccess";
		else if(request_params.containsKey(GET_RELAY_LINK))
			req = userSession.isMenuActive("relaymgr")?GET_RELAY_LINK:"noaccess";
		else if(request_params.containsKey(GET_REPORT_LINK))
			req = userSession.isMenuActive("report")?GET_REPORT_LINK:"noaccess";
				
		if(req!=null)
		{
			String link = req;
			if(!req.equals("noaccess"))
			{
				String device_server = request.getParameter("node");
				String linenum = "";
			    String deviceaddr = "";
				if(!device_server.equals("undefined") && !device_server.equals(""))
			    {
			    	String device= device_server.split("/")[1];
				    
			    	String [] vals = (device.split("\\"+SvgMapsUtils.VARCODE_SEPARATOR))[0].split("\\"+SvgMapsUtils.SEPARATOR);
				    linenum = vals[0];
				    deviceaddr = vals[1];
			    }
				
			    link = LinksMap.get(req);
			    if(link.contains("$linenum$"))
			    	link=link.replace("$linenum$", linenum);
			    if(link.contains("$devicenum$"))
			    	link=link.replace("$devicenum$", deviceaddr);
			    
			}

		    Map<String,String> output = new HashMap<String,String>();
		    output.put("result", link);
		    jsonresp = gson.toJson(output);	   
		    
		    return jsonresp;
		}
		
		if(request_params.containsKey(GET_NODE_DESCR))
		{
			String device_server = request.getParameter("node");
			String sDescr_limit = request.getParameter("limit");
			int descr_limit = 0;
			
			if(sDescr_limit!=null && !sDescr_limit.equals(""))
				descr_limit = Integer.parseInt(request.getParameter("limit"));
			
		    String passedPath= device_server.substring(device_server.indexOf("/")+1);
		    
		    String [] vals = (passedPath.split("\\"+SvgMapsUtils.VARCODE_SEPARATOR))[0].split("\\"+SvgMapsUtils.SEPARATOR);
		    
		    String linenum = vals[0];
		    String deviceaddr = vals[1];
		    
		    String returnDescr = "******";
		    
		    if(vals.length==SvgMapsUtils.devAddrLength)
		    {
			    try
			    {
			    	DeviceBean devBean = DeviceListBean.retrieveSingleDeviceByCode(1, linenum+SvgMapsUtils.SEPARATOR+deviceaddr, userSession.getLanguage());
			    	returnDescr = devBean.getDescription();
			    }catch(Exception e)
			    {
			    	LoggerMgr.getLogger(SvgMapsCustomTranslator.class).error(e);
			    }
		    }
		    
		    else if(passedPath.indexOf(SvgMapsUtils.VARCODE_SEPARATOR)>0) //check if the address contains the varcode
		    {	    	
		    	String varcode = passedPath.substring(passedPath.indexOf(SvgMapsUtils.VARCODE_SEPARATOR)+2); 
		    	
				try
			    {

					int idDevice = VDMappingMgr.getInstance().getIdDevice(Integer.valueOf(linenum), Integer.valueOf(deviceaddr));
					
					int idVariable = VDMappingMgr.getInstance().getIdVariable(idDevice, varcode);
			    	
					returnDescr = VarphyBeanList.retrieveVarById(1,idVariable, userSession.getLanguage()).getShortDescription();
			    	
			    }catch(Exception e)
			    {
			    	LoggerMgr.getLogger(SvgMapsCustomTranslator.class).error(e);
			    }
		    }
		    
		    if(sDescr_limit!=null && !sDescr_limit.equals("") && returnDescr.length()>descr_limit)
		    	returnDescr = returnDescr.substring(0, descr_limit);
		    
		    Map<String,String> output = new HashMap<String,String>();
		    output.put("result", returnDescr);
		    jsonresp = gson.toJson(output);	  
		    
		    return jsonresp;
		}
		
		if(request_params.containsKey(GET_VAR_UM))
		{
			String device_server = request.getParameter("node");

		    String passedPath= device_server.substring(device_server.indexOf("/")+1);
		    String [] vals = (passedPath.split("\\"+SvgMapsUtils.VARCODE_SEPARATOR))[0].split("\\"+SvgMapsUtils.SEPARATOR);
		    
		    String linenum = vals[0];
		    String deviceaddr = vals[1];
		    
		    String returnUM = "";

	    	if(passedPath.indexOf(SvgMapsUtils.VARCODE_SEPARATOR)>0) //check if the address contains the varcode
	    	{
	    		String varcode = passedPath.substring(passedPath.indexOf(SvgMapsUtils.VARCODE_SEPARATOR)+2);
				 
				try
			    {
					int idDevice = VDMappingMgr.getInstance().getIdDevice(Integer.valueOf(linenum), Integer.valueOf(deviceaddr));
					
					int idVariable = VDMappingMgr.getInstance().getIdVariable(idDevice, varcode);
			    	
					returnUM = VarphyBeanList.retrieveVarById(1,idVariable, userSession.getLanguage()).getMeasureunit();
			    	
			    }catch(Exception e)
			    {
			    	LoggerMgr.getLogger(SvgMapsCustomTranslator.class).error(e);
			    }	
	    	}
		    	    
		    Map<String,String> output = new HashMap<String,String>();
		    output.put("result", returnUM);
		    jsonresp = gson.toJson(output);	  
		    
		    return jsonresp;
		}
		
		// NODE Status functionality: return the status of the NODE requested (DEVICE)
		// Status	0: Online
		//			1: Alarm
		//			2: Offline
		//			3: Disabled
		// returns the SITE status (presence of active alarms or not) when it is called with an array containing ""
		
		if(request_params.containsKey(GET_NODE_STATUS))
		{
			String [] nodes = request.getParameterValues("nodes[]");
		    
			Map<String,JsonElement> output = new HashMap<String,JsonElement>();
      		JsonArray jsarray = new JsonArray();
					
			for (int i=0; i<nodes.length; i++) {
		    	
				String passedPath= nodes[i].substring(nodes[i].indexOf("/")+1);
				String [] vals = (passedPath.split("\\"+SvgMapsUtils.VARCODE_SEPARATOR))[0].split("\\"+SvgMapsUtils.SEPARATOR);

		    	int returnVal = 0;
			    
			    if(vals.length==1 && vals[0].equals("")) // get site status (if there are active alarms or not)
			    										 // when the function is called without parameters
			    {
			    	GroupListBean groupListBean = userSession.getGroup();			
        			int[] groups = groupListBean.getIds();
        			DeviceStructureList deviceStructureListMaster = groupListBean.getDeviceStructureList(); 
        			int[] ids = deviceStructureListMaster.retrieveIdsByGroupsId(groups);
			    	
			    	boolean alarms = DeviceStatusMgr.getInstance().existAlarm(ids);
			    	if(alarms)
			    		returnVal = 1;

			    }
			    if(vals.length==SvgMapsUtils.devAddrLength) // get device status
			    {
			    	String linenum = vals[0];
					String deviceaddr = vals[1];
			    	
			    	try
				    {
			    		DeviceBean devBean = DeviceListBean.retrieveSingleDeviceByCode(1, linenum+SvgMapsUtils.SEPARATOR+deviceaddr, userSession.getLanguage());
			    		if(DeviceStatusMgr.getInstance().isAlarm(devBean.getIddevice()))
			    			returnVal = 1;
			    		if(DeviceStatusMgr.getInstance().isOffLineDevice(devBean.getIddevice()))
			    			returnVal = 2;
			    		if(DeviceStatusMgr.getInstance().isDisabled(devBean.getIddevice()))
			    			returnVal = 3;
				    }
			    	catch (Exception e)
			    	{
			    		LoggerMgr.getLogger(SvgMapsCustomTranslator.class).error(e);
			    		returnVal = 1; //set response to 'ALARM' if an exception occurs
			    	}
			    	
			    }
			    
		      	jsarray.add(gson.toJsonTree(returnVal));
	      		
		    }
			
		    output.put("result", gson.toJsonTree(jsarray));
      		jsonresp = gson.toJson(output);
      		return jsonresp;

		}
		
		if(request_params.containsKey(GET_ACCEESS_RIGHT))
		{
			String access_rights = request.getParameter("rights");
			String profile = userSession.getProfileCode();
			boolean access = false;
			if (access_rights.contains(profile))
				access = true;
			
			Map<String,Boolean> output = new HashMap<String,Boolean>();
		    output.put("result", access);
		    jsonresp = gson.toJson(output);	 
		}
		
		return jsonresp;
	}
}