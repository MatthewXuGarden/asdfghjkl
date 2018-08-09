package com.carel.supervisor.presentation.https2xml;

import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.device.DeviceStatusMgr;

public class RequestSiteStatus implements IXMLRequest 
{
	private final static String ID_SITE="idSite";
	
	private StringBuffer response= new StringBuffer();

	private String username;
	
	public void startRequest(XMLNode node) throws Exception 
	{
		int idSite = -1;
		XMLNode childNode = null;
		if(node.size()==0)
		{
			node.addNode("element","");
			node.getNode(0).setAttribute("idSite","1");
		}
		for(int i=0;i<node.size();i++)
		{
			childNode = node.getNode(i);
			try {
				idSite = Integer.parseInt(childNode.getAttribute(ID_SITE));
			}catch(Exception e){}
			
			if(idSite != -1)
			{
				response.append("\n<site id=\""+idSite+"\" status=\"");
				if(DeviceStatusMgr.getInstance().existAlarm())
					response.append("2\" />");
				else
					response.append("1\" />");
			}
		}
	}

	public String getResponse() {
		return response.toString();
	}

	public String getNameRequest() {
		return RequestFactory.REQUEST_SITE_STATUS;
	}
	

	public void setUsername(String username) {
		this.username=username;
	}

	public String getUsername() {
		return username;
	}
}
