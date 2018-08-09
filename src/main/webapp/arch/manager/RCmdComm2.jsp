<%	com.carel.supervisor.presentation.session.UserSession sessionUser = com.carel.supervisor.presentation.helper.ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	if( sessionUser != null && sessionUser.getProfileCode().equals("System Administrator") ) {
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">
</head>

<frameset rows="100" frameborder="no" border="0" framespacing="0">
	<frame name="bodytab" src="arch/manager/DBCommander.jsp" scrolling="no" frameborder="0">
</frameset>

</html>
<%} else if( sessionUser != null ) response.sendError(403);
	else response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
%>