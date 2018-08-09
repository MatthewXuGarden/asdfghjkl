package com.carel.supervisor.presentation.https2xml;

import java.util.Hashtable;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;

public class RequestFactory 
{
	protected static final String REQUEST_PARAMETERS_LIST="parametersList";
	protected static final String REQUEST_SET_PARAMETER="setParameters";
	protected static final String REQUEST_ALARMS_LIST="alarmList";
	static Logger logger = LoggerMgr.getLogger(RequestFactory.class);
	// Mirko
	// Aggiunti ulteriori dati per Matteo
	protected static final String REQUEST_DEVICES_LIST="devicesList";
	protected static final String REQUEST_SITE_STATUS="siteStatus";
	
	// requested from modbus slave application
	protected static final String REQUEST_PARAM_BRIEF_LIST = "paramBriefList";
	
	private static final String REQUEST_TYPE="type";
	
	protected static final String REQUEST_DEVICE_CODE = "deviceCode";
	protected static final String REQUEST_ENGINE_STATUS = "engineStatus";
	
	private static Hashtable<Integer,Integer> vartypeHash = new Hashtable<Integer,Integer>();
	
	public static IXMLRequest newRequest(XMLNode requestNode,CheckUser user) 
		throws Exception
	{
		IXMLRequest request=null;
		String requestType=requestNode.getAttribute(REQUEST_TYPE);
		//backward compatibility
		if(requestType==null){
			requestType=requestNode.getAttribute("t");
		}
		//backward compatibility
		for(;;)
		{
			if(requestType.equals(REQUEST_PARAM_BRIEF_LIST))
			{
				request=new RequestParametersList(true,vartypeHash);
				break;
			}
			if(requestType.equals(REQUEST_PARAMETERS_LIST))
			{
				request=new RequestParametersList();
				break;
			}
			if(requestType.equals(REQUEST_DEVICES_LIST))
			{
				request=new RequestDevicesList();
				break;
			}
			if(requestType.equals(REQUEST_SITE_STATUS))
			{
				request=new RequestSiteStatus();
				break;
			}
			if(requestType.equals(REQUEST_SET_PARAMETER))
			{
				request=new RequestSetParameters();
				((RequestSetParameters)request).setUser(user);
				break;
			}
			if(requestType.equals(REQUEST_ALARMS_LIST))
			{
				request=new RequestAlarmList();
				logger.info("4");
				break;
			}
			if(requestType.endsWith(REQUEST_DEVICE_CODE))
			{
				request = new RequestDeviceCode();
				break;
			}
			if(requestType.endsWith(REQUEST_ENGINE_STATUS))
			{
				request = new RequestEngineStatus();
				break;
			}
			break;
		}
		//backward compatibility
		if(request==null){
			try{
				request = (IXMLRequest) Class.forName("com.carel.supervisor.presentation.https2xml."+requestType.toUpperCase()+"Request").newInstance();
				
			}catch (Exception e) {
			}
		}
		//backward compatibility
		request.setUsername(user.getUserName());
		request.startRequest(requestNode);
		
		return request;
		
	}
}
