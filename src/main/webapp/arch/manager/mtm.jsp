<%	com.carel.supervisor.presentation.session.UserSession sessionUser = com.carel.supervisor.presentation.helper.ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	if( sessionUser != null && sessionUser.getProfileCode().equals("System Administrator") ) {
%>
<%@ page language="java" contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>
<%@page import="com.carel.supervisor.director.MainThreadMonitor"%>
<%
 String crt = MainThreadMonitor.getInstance().getCurrent();
 long delta = MainThreadMonitor.getInstance().getLoopTime();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>MainThreadMonitor</title>
</head>
<body>
<table border="1" width="100%" cellspacing="1" cellpadding="1">
	<tr>
		<td colspan="2">MainThread Monitor Page</td>
	</tr>
	<tr>
		<td>Current activity: </td>
		<td><%=crt %></td>
	</tr>
	<tr>
		<td>Global Time: </td>
		<td><%=delta %> mSec</td>
	</tr>
	<tr>
		<td colspan="2">Activity List and Time</td>
	</tr>
	<%for (int i=0; i<MainThreadMonitor.getInstance().getActivitySize(); i++) { %>
	<tr>
		<td><%=MainThreadMonitor.getInstance().getActivityCode(i) %></td>
		<td><%=MainThreadMonitor.getInstance().getActivityTime(MainThreadMonitor.getInstance().getActivityCode(i)) %></td>
	</tr>
	<%} %>
</table>
</body>
</html>
<%} else if( sessionUser != null ) response.sendError(403);
	else response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
%>