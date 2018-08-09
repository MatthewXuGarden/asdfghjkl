package com.carel.supervisor.presentation.https2xml;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.presentation.bean.XMLBean;
import com.carel.supervisor.presentation.bean.XMLBeanList;
import com.carel.supervisor.presentation.devices.UtilDevice;
import com.carel.supervisor.presentation.helper.ServletHelper;

public class RequestEngineStatus implements IXMLRequest
{
	private final static String ID_SITE="idSite";
	
	private StringBuffer response= new StringBuffer();

	private String username;
	
	public void startRequest(XMLNode node) 
		throws Exception 
	{
		response.append("\n<status value=\""+ServletHelper.messageToNotify()+"\" />");
	}

	public String getResponse() {
		return response.toString();
	}

	public String getNameRequest() {
		return RequestFactory.REQUEST_ENGINE_STATUS;
	}
	

	public void setUsername(String username) {
		this.username=username;
	}

	public String getUsername() {
		return username;
	}
}
