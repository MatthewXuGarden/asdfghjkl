<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"
	import="com.carel.supervisor.dataaccess.db.RecordSet"
	import="com.carel.supervisor.dataaccess.db.DatabaseMgr"
%>
<%
	int ret = 0;
	try
	{
		String sql = "select count(*) as number from hsaction where status=4";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,null);
		if(rs != null)
			ret = ((Integer)rs.get(0).get("number")).intValue();

		sql = "update hsaction set status=5 where status=4";
		DatabaseMgr.getInstance().executeStatement(null,sql,null);
	}
	catch(Exception e) {
		ret = -1;
	}	
%>
<%=ret%>
<% } else { response.sendError(404); } %>