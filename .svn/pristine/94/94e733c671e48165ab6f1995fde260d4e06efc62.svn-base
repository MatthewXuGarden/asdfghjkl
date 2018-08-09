<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.carel.supervisor.dataaccess.event.EventMgr" %>
<%
	String message = EventMgr.getInstance().getMessageAndReboot();
	if (null == message)
	{
		message = "NULL";
	}
%><%=message%>
<% } else { response.sendError(404); } %>