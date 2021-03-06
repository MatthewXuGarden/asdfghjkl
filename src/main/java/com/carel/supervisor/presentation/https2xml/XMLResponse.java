package com.carel.supervisor.presentation.https2xml;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.profiling.InvalidCredentialException;
import com.carel.supervisor.base.profiling.ProfilingMgr;
import com.carel.supervisor.base.profiling.UserCredential;
import com.carel.supervisor.base.xml.XMLNode;


public class XMLResponse
{
	private final static String LOGIN_NODE = "login";
	private StringBuffer response = new StringBuffer();
	static Logger logger = LoggerMgr.getLogger(XMLResponse.class);
	public XMLResponse(XMLNode mainNode) throws Exception
	{
		init(mainNode, false);
	}
	
	public XMLResponse(XMLNode mainNode, boolean forceVerifyUserCredential) throws Exception
	{
		init(mainNode, forceVerifyUserCredential);
	} //XMLResponse

	private void init(XMLNode mainNode, boolean forceVerifyUserCredential) throws InvalidCredentialException, Exception{
		CheckUser checkUser = new CheckUser();
		XMLNode loginnode = mainNode.getNode(LOGIN_NODE);
		mainNode.removeNode(LOGIN_NODE);
		if (checkUser.verifyUserCredential(loginnode) || forceVerifyUserCredential)
		{
			for (int i = 0; i < mainNode.size(); i++)
			{
				if (ProfilingMgr.getInstance().getUserProfile(
					new UserCredential(loginnode.getAttribute("userName"), loginnode.getAttribute("password"),"")) != null)
				{
					IXMLRequest request = RequestFactory.newRequest(mainNode.getNode(i), checkUser);
					response.append("\n<response type=\"");
					response.append(request.getNameRequest());
					logger.info("getNameRequest!!!");
					response.append("\">\n");
					response.append(request.getResponse());
					logger.info("getResponse!!!");
					response.append("</response>");
					logger.info(response.toString());
				}
			} //for
		} //if

	}
	
	public String getResponse()
	{
		StringBuffer response = new StringBuffer();
		response.append(getStartingTag());
		response.append(getInnerXML());
		response.append(getEndingTag());

		return response.toString();
	} //getResponse
	
	public static String  getStartingTag(){
		return "\n<responses>";
	}
	
	public static String getEndingTag(){
		return "\n</responses>";
	}
	
	public String getInnerXML(){
		return this.response.toString();
	}
} //XMLResponse
