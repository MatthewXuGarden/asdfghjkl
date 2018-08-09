package com.carel.supervisor.presentation.https2xml;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.xml.XMLNode;

public class Test {

	public static void main(String[] args) throws Exception {
		BaseConfig.init();
		XMLNode node= null;
		
		String xml =
            "<requests>" +
	            "<login userName=\"admin\" password=\"admin\" />" +
	            
	            "<request type=\"parametersList\" language=\"IT_it\" >" +
	             	"<element idDevice=\"2\" idsVariable=\"4;5\" />" +
	             	"<element idDevice=\"2\" idsVariable=\"\" />" +
	            "</request>" +
	            
	            "<request type=\"setParameters\" language=\"IT_it\" >" +
	            	"<element idVariable=\"3\" value=\"10\" />" +
	            "</request>" +
	            
	            "<request type=\"alarmList\" language=\"IT_it\" >" +
	            	"<element idDevice=\"3\" start=\"0\" length=\"10\" />" +
	            	"<element idDevice=\"\"  start=\"0\" length=\"10\" />" +
	            	"<element idDevice=\"3\" start=\"\" length=\"\" />" +
	            "</request>" +
	            
            "</requests>";
		
		node = XMLNode.parse(xml);
		XMLResponse response= new XMLResponse(node);
		response.getResponse();
        System.out.println(response.getResponse());
      
	}

}
