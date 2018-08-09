<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.dataaccess.dataconfig.SiteInfoList"
	import="com.carel.supervisor.dataaccess.dataconfig.SiteInfo"
	%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String jsession = request.getSession().getId();
	SiteInfo site =  new SiteInfoList().getById(sessionUser.getIdSite());
	String title = site != null?site.getName():null;
	if(title == null || title.length() == 0)
		title = "PlantVisorPRO";
	String userAgent = request.getHeader("user-agent");
	boolean bIE = userAgent.indexOf("MSIE") >= 0;
%>

<html>
<head>
	<base href="<%=basePath%>">
	<title><%=title%></title>
	<%=bIE ? "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=9\">" : ""%>
	<meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
  	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=<%=sessionUser.getEncoding()%>" >
</head>

<frameset rows="0,38,*" frameborder="no" border="0" framespacing="0">
	<frame name="manager" src="arch/desktop/Manager.jsp;jsessionid=<%=jsession%>" scrolling="no" noresize frameborder="0">
	<frame name="header" src="arch/desktop/Top.jsp;jsessionid=<%=jsession%>" scrolling="no" noresize frameborder="0">
    <frameset rows="*,87">
	  <frame name="body" src="arch/desktop/Body.jsp;jsessionid=<%=jsession%>" noResize scrolling="no" frameborder="0">
      <frameset cols="15%,*" frameborder="no">
	      <frame name="allarm" src="arch/desktop/Alarm.jsp;jsessionid=<%=jsession%>" noResize scrolling="no" frameborder="0">
	      <frame name="menuPVPRO2" src="arch/desktop/Menu2.jsp;jsessionid=<%=jsession%>" noResize scrolling="no" frameborder="0">
      </frameset>
    </frameset>
</frameset>
</html>
