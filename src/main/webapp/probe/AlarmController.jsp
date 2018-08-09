<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.carel.supervisor.dataaccess.event.EventMgr" %>
<%@ page import="com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl" %>
<%
	String size = "NULL";
	Integer[] idVariables = AlarmCtrl.retriveExpired();
	if (null != idVariables)
	{
		int siz = idVariables.length;
		EventMgr.getInstance().warning(new Integer(1), "GUARDIAN", "Action", "G001", new Integer(siz));
		size = String.valueOf(siz);
		%>
		<%
	}
%><%=size%>
<% } else { response.sendError(404); } %>