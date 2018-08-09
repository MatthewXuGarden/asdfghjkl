package com.carel.supervisor.presentation.https2xml;

import com.carel.supervisor.base.xml.XMLNode;

public interface IXMLRequest {

	public abstract void startRequest(XMLNode node) throws Exception;
	public abstract String getResponse();
	public abstract String getNameRequest();
	public void setUsername(String username);
	public String getUsername();
	
	
}//IXMLRequest
