<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.carel.supervisor.dataaccess.db.RecordSet" %>
<%@ page import="com.carel.supervisor.dataaccess.db.DatabaseMgr" %>
<%
int retur = 0;
try
{
	String sql = "select count(1) from livestatus";
	RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null,sql,null);
	int size = ((Integer)recordset.get(0).get(0)).intValue();
}
catch(Exception e)
{
	retur = 1;
}
%>
<%=retur%>
<% } else { response.sendError(404); } %>