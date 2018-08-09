<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"
import="java.util.Properties"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.ldap.DBProfiler"
import="com.carel.supervisor.presentation.bo.helper.GuardianHelper"
%>
<%
String ret = "KO";
try
{
	Properties properties = ServletHelper.retrieveParameters(request);
	String u = properties.getProperty("param0");
	String p = properties.getProperty("param1");
	try 
	{
		new DBProfiler().checkUser(u,p);
		ret = "OK";
		GuardianHelper.writeMsgWinClose(u);
	}
	catch(Exception e) {
	}
}
catch(Exception e){}	
%>
<%=ret%>
<% } else { response.sendError(404); } %>