<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"
	import="com.carel.supervisor.dispatcher.main.DispatcherMonitor"
%>
<%
	// Evito la cache della JSP
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires", 0);
	
	boolean ris = false;
	ris = DispatcherMonitor.getInstance().checkFirstQueue(System.currentTimeMillis());
	if(ris)
		ris = DispatcherMonitor.getInstance().checkSecondQueue(System.currentTimeMillis());
%>
<%if(ris){%>0<%}else{%>1<%}%>
<% } else { response.sendError(404); } %>