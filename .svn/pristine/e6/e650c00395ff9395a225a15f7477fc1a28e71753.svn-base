package com.carel.supervisor.presentation.https2xml;

import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.presentation.bean.XMLBean;
import com.carel.supervisor.presentation.bean.XMLBeanList;

public class RequestAlarmList implements IXMLRequest{
	
	private final static String ID_DEVICE="idDevice";
	private final static String START="start";
	private final static String LENGTH="length";
		
	private StringBuffer response= new StringBuffer();
	private String username;
	
	public void startRequest(XMLNode node) throws DataBaseException 
	{
		XMLNode childNode=null;
		String idDev=null;
		String start=null;
		String length=null;
		
		for(int i=0;i<node.size();i++)
		{
			childNode=node.getNode(i);
			idDev=childNode.getAttribute(ID_DEVICE);
			start=childNode.getAttribute(START);
			length=childNode.getAttribute(LENGTH);
			
			XMLBeanList beanList= new XMLBeanList(idDev, start, length, node.getAttribute("language"));
			XMLBean [] beans=beanList.getXMLBean();
			if(beans!=null){
				for(int j=0;j<beans.length;j++){
					response.append("\n<alarm name=\""+beans[j].getAlDesc()+"\" ");
					response.append("starttime=\""+beans[j].getStarttime()+"\" ");
					response.append("endtime=\""+(beans[j].getEndtime()==null?"":beans[j].getEndtime())+"\" ");
					response.append("ackuser=\""+(beans[j].getAckUser()==null?"":beans[j].getAckUser())+"\" ");
					response.append("acktime=\""+(beans[j].getAckTime()==null?"":beans[j].getAckTime())+"\" ");
					response.append("deluser=\""+(beans[j].getDelUser()==null?"":beans[j].getDelUser())+"\" ");
					response.append("deltime=\""+(beans[j].getDelTime()==null?"":beans[j].getDelTime()+"")+"\" ");
					response.append("resetuser=\""+(beans[j].getResetUser()==null?"":beans[j].getResetUser())+"\" ");
					response.append("resettime=\""+(beans[j].getResetTime()==null?"":beans[j].getResetTime())+"\" ");
					response.append("iddevice=\""+beans[j].getIdDevice()+"\" ");
					response.append("devicedescription=\""+beans[j].getDevDesc()+"\" ");
					response.append("idalarm=\""+beans[j].getIdAlarm()+"\" ");
					response.append("idvariable=\""+beans[j].getIdVariable()+"\" ");
					response.append("priority=\""+beans[j].getPrioDesc()+"\" ");
					response.append("islogic=\""+beans[j].getIslogic()+"\" ");
					response.append("/>");
				}//for
			}//if
		}//for
		
		
	}//newRequest

	public String getResponse() {
		return response.toString();
	}//getResponse

	public void newRequest(XMLNode node, String name) {
	}//

	public String getNameRequest() {
		return RequestFactory.REQUEST_ALARMS_LIST;
	}//getNameRequest


	public void setUsername(String username) {
		this.username=username;
	}

	public String getUsername() {
		return username;
	}
}//Class RequestAlarmList
