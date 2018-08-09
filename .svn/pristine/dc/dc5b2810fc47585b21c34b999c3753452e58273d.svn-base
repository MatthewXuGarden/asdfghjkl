<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"
	import="com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl"
%>
<%
	String size = "NULL";
	Integer[] idVariables = AlarmCtrl.retriveNotifyExpired();
	if (null != idVariables)
	{
		int siz = idVariables.length;
		if(siz > 0)
		{
			for(int i=0; i<idVariables.length; i++)
				AlarmCtrl.notifyGuardianNotify(idVariables[i]);
		}
		size = String.valueOf(siz);
	}
%><%=size%>
<% } else { response.sendError(404); } %>
