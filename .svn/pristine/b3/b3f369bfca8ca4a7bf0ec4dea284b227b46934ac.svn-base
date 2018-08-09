<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.carel.supervisor.dataaccess.db.DatabaseMgr" %>
<%
try
{
	String sql = "update lastreindex set lastupdate = current_timestamp";
	DatabaseMgr.getInstance().executeStatement(null,sql,null);
}
catch(Exception e)
{
}
%>
<% } else { response.sendError(404); } %>