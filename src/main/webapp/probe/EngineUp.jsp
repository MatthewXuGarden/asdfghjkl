<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<%@page import="com.carel.supervisor.director.MainThreadMonitor"%>
<%
	long DELTA_LIMIT = 600000L;
	// Evito la cache della JSP
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires", 0);
	
	boolean ris = true;
	if(MainThreadMonitor.getInstance().isRunning)
	{
		long delta = MainThreadMonitor.getInstance().getLoopTime();
		if(delta > DELTA_LIMIT)
			ris = false;
	}
	else
		ris = true;
%>
<%if(ris){%>0<%}else{%>1<%}%>
<% } else { response.sendError(404); } %>