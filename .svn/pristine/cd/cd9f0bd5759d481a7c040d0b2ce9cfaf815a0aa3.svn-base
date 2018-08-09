package com.carel.supervisor.presentation.https2xml;

import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.VariablesAccess;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.DefaultCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;

public class RequestSetParameters implements IXMLRequest{
	private final static String ID_VARIABLE="idVariable";
	private final static String VALUE="value";
	
	private StringBuffer response= new StringBuffer();
	private CheckUser user=null;
	private String username;
	
	public void startRequest(XMLNode node) 
	{
		Long vt = new Long(node.getAttribute("waittime")==null?"10000":node.getAttribute("waittime"));
		
		XMLNode childNode=null;
		SetContext setContext = new SetContext();
		String lang = "EN_en";
		try
		{
			lang = LangUsedBeanList.getDefaultLanguage(1);
		}
		catch (Exception e)
		{
		}
		setContext.setLanguagecode(lang);
    	setContext.setCallback(new DefaultCallBack());
		setContext.setUser(user.getUserName());
		
		int []idsVars= null;
		String[] values=null;
		boolean[] ok = null;
		
		if(node!=null){
			idsVars= new int[node.size()];
			values= new String[node.size()];
			ok = new boolean[node.size()];;
		}//if
		
		for(int i=0;i<node.size();i++)
		{
			childNode=node.getNode(i);
			idsVars[i] =new Integer(childNode.getAttribute(ID_VARIABLE)).intValue();
			values[i]=childNode.getAttribute(VALUE);
			try
			{
            	setContext.addVariable(idsVars[i], Float.parseFloat(values[i]));
        		ok[i] = true;
			}//try
			catch(Exception e){
				ok[i] = false;
			}//catch
		}//for
        SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
       	try
		{
			Thread.sleep(vt);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		for(int i=0;i<node.size();i++)
		{
			response.append("\n<variable id=\"");
			response.append(idsVars[i]);
			response.append("\" ");
			try
			{
				float v = ControllerMgr.getInstance().getFromField(idsVars[i]).getCurrentValue();
				if (ok[i])
					if(v == Float.parseFloat(values[i]))
						response.append("state=\"ok\" ");
					else
						response.append("state=\""+vt+"\"");
				else
					response.append("state=\"error\" ");
			}//try
			catch(Exception e){
				response = new StringBuffer("<error ");
			}//catch
		}
		response.append(" />");
	}//newRequest

	public String getResponse() {
		return response.toString();
	}//getResponse

	public String getNameRequest() {
		return RequestFactory.REQUEST_SET_PARAMETER;
	}//getNameRequest

	public void setUser(CheckUser user) {
		this.user=user;
	}//
	

	public void setUsername(String username) {
		this.username=username;
	}

	public String getUsername() {
		return username;
	}

}//Class RequestSetParameters
