<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"
		import="java.util.Properties"
	import="com.carel.supervisor.dataaccess.db.DatabaseMgr"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
%>
<%
	int ret = 0;
	try
	{
		Properties properties = ServletHelper.retrieveParameters(request);
		String num = properties.getProperty("param0");
		String sql = 
		"delete from hsaction where (status=3 or status=5) and inserttime < (current_timestamp - interval '"+num+" days')";
		DatabaseMgr.getInstance().executeStatement(null,sql,null);
	}
	catch(Exception e) {
		ret = 1;
	}	
%>
<%=ret%>
<% } else { response.sendError(404); } %>