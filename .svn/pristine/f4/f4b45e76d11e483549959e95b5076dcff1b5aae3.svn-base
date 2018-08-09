package com.carel.supervisor.presentation.https2xml;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.presentation.bean.XMLBean;
import com.carel.supervisor.presentation.bean.XMLBeanList;
import com.carel.supervisor.presentation.devices.UtilDevice;

public class RequestDeviceCode implements IXMLRequest
{
	private final static String ID_SITE="idSite";
	
	private StringBuffer response= new StringBuffer();

	private String username;
	
	public void startRequest(XMLNode node) 
		throws Exception 
	{
		int idSite = -1;
		Integer[] ids = null;
		XMLNode childNode = null;
		
		for(int i=0;i<node.size();i++)
		{
			
			childNode = node.getNode(i);
			try {
				idSite = Integer.parseInt(childNode.getAttribute(ID_SITE));
			}catch(Exception e){idSite=1;}
			try
			{
				ArrayList<Integer> al = new ArrayList<Integer>();
				if(childNode.getAttribute("iddevices").contains(",")||Integer.parseInt(childNode.getAttribute("iddevices"))>=0)
				{
					StringTokenizer st = new StringTokenizer(childNode.getAttribute("iddevices"),",");
					while (st.hasMoreTokens())
					{
						al.add(new Integer(st.nextToken()));
					}
				}
				else
				{
					al.add(new Integer(-1));
				}
				ids = al.toArray(new Integer[al.size()]);
			}
			catch(Exception e)
			{
				ids=null;
			}
			if(idSite != -1 && ids!=null)
			{
				XMLBeanList beanList = new XMLBeanList(idSite, ids);
				XMLBean [] beans = beanList.getXMLBean();
				if(beans!=null)
				{
					for(int j=0;j<beans.length;j++)
					{
						response.append("\n<device idDevice=\""+beans[j].getIdDevice()+"\" ");
						response.append("code=\""+beans[j].getCode()+"\" ");
						response.append(" />");
						//response.append("\n</device>");
					}
				}
			}
		}
	}

	public String getResponse() {
		return response.toString();
	}

	public String getNameRequest() {
		return RequestFactory.REQUEST_DEVICE_CODE;
	}
	

	public void setUsername(String username) {
		this.username=username;
	}

	public String getUsername() {
		return username;
	}
}
