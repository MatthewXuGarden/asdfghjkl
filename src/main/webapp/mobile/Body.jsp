<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.bean.LoginRedirectBean"
	import="com.carel.supervisor.base.config.BaseConfig"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN"
        "http://www.w3.org/TR/html4/frameset.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String jsession = request.getSession().getId();
	String mobilePage = "Devices.jsp";
	LoginRedirectBean loginRedirect = new LoginRedirectBean(sessionUser);
	if( loginRedirect.isMobileRedirect() ) {
		loginRedirect.setMobileSessionProperties();
		mobilePage = loginRedirect.getMobilePage();
	}
%>

<html>
<head>
	<title>PVPRO</title>
	<base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
</head>
 
<frameset rows="*, 0px, 0px" frameborder="no" border="0" framespacing="0">
	<frame name="bodytab" src="mobile/<%=mobilePage%>;jsessionid=<%=jsession%>" noResize frameborder="0">
	<frame name="Poller" src="arch/manager/Poller.jsp;jsessionid=<%=jsession%>" scrolling="no" noresize frameborder="0">
	<frame name="Receiver" src="arch/desktop/Receiver.jsp;jsessionid=<%=jsession%>" scrolling="no" noresize frameborder="0">
</frameset>

</html>
