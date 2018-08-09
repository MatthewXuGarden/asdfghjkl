<%@ page language="java"  pageEncoding="UTF-8"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.controller.pagelinks.PageLinksMgr"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.tabmenu.Tab"
import="com.carel.supervisor.presentation.tabmenu.MenuBuilder"
%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String jsession = request.getSession().getId();
%>
<html>
  <head>
 	<base href="<%=basePath%>">
 	<meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=<%=sessionUser.getEncoding()%>" >
  </head>
  
  <frameset rows = "25,*,0,0" frameborder="no" framespacing="0" border="0">
  	<frame name="TabMenu"  noResize frameborder="0" marginwidth="0" marginheight="0" scrolling="no" src="arch/include/Tabmenu.jsp;jsessionid=<%=jsession%>">
  	<frame name="bodytab"	 noResize frameborder="0" marginwidth="0" marginheight="0" scrolling="no" src="arch/include/Tabbody.jsp;jsessionid=<%=jsession%>">
  	<frame name="Poller"   noResize frameborder="0" marginwidth="0" marginheight="0" scrolling="no" src="arch/manager/Poller.jsp;jsessionid=<%=jsession%>">
  	<frame name="Receiver" noResize frameborder="0" marginwidth="0" marginheight="0" scrolling="no" src="arch/manager/Receiver.jsp;jsessionid=<%=jsession%>">
  </frameset>
  
</html>
