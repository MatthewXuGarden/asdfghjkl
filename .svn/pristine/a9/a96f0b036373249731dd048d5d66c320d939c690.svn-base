package com.carel.supervisor.presentation.https2xml;

import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.director.bms.BMSConfiguration;
import com.carel.supervisor.director.bms.BmsMgr;

public class ARRequest implements IXMLRequest {

	private StringBuffer response;
	private String username;
	
	public String getNameRequest() {
		return "AR";
	}

	public String getResponse() {
		return response.toString();
	}

	public void startRequest(XMLNode node) throws Exception {
		response = new StringBuffer ();
		
		BMSConfiguration conf = BmsMgr.getInstance().getConfig();

		XMLNode nodeXalarms = XMLNode.parse(conf.getAlarmsXML());
		ALRequest alReq = new ALRequest();
		alReq.startRequest(nodeXalarms);
		
		XMLNode nodeXevents = XMLNode.parse(conf.getEventsXML());
		ELRequest elReq = new ELRequest();
		elReq.startRequest(nodeXevents);
		
		response.append(alReq.getResponse() );
		response.append("\n\r");
		response.append(elReq.getResponse() );
	}



	public void setUsername(String username) {
		this.username=username;
	}

	public String getUsername() {
		return username;
	}
	
	
}
