<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8" import="com.carel.supervisor.base.system.SystemInfoExt"%>
<%
String val = SystemInfoExt.getInstance().getCpuUsage();
int iVal = 0;
try {	iVal = Integer.parseInt(val); }catch(Exception e){}
iVal = 100 - iVal;
%>
<%=iVal%>
<% } else { response.sendError(404); } %>