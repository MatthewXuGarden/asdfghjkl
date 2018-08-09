<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl" %>
<%
	Integer[] idVariables = AlarmCtrl.retriveExpired();
	if (null != idVariables)
	{
		int size = idVariables.length;
		
		for(int i = 0; i < size; i++)
		{
			AlarmCtrl.notifyGuardian(idVariables[i]);
		}
	}
%>
<% } else { response.sendError(404); } %>