<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8" import="com.carel.supervisor.base.system.SystemInfoExt"%>
<%
String usedS = SystemInfoExt.getInstance().getRamPerUsage();
usedS = usedS.substring(0, usedS.length() -1);
usedS = usedS.trim();
long used = Long.parseLong(usedS);
%>
<%=(100-used)%>
<% } else { response.sendError(404); } %>