<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"
	import="java.util.Properties"
	import="com.carel.supervisor.dispatcher.DispatcherMgr"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
%>
<%
boolean ris = false;
try 
{
	Properties properties = ServletHelper.retrieveParameters(request);
	String num = properties.getProperty("param0");
	int limit = 4;
	try{
		limit = Integer.parseInt(num);
	}
	catch(Exception e){}
	
	ris = DispatcherMgr.getInstance().pingExceded(limit);
}
catch(Exception e){
}
%>
<%if(ris){%>
1
<%}else{%>
0
<%}%>
<% } else { response.sendError(404); } %>