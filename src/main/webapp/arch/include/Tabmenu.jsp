<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page language="java" 
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.tabmenu.MenuBuilder"
import="com.carel.supervisor.presentation.tabmenu.Tab"
import="com.carel.supervisor.presentation.menu.configuration.MenuTabMgr"
import="com.carel.supervisor.presentation.bo.master.IMaster"
%>
<% 
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUser = sessionUser.getCurrentUserTransaction();
	IMaster bo = trxUser.getBoTrx();
	String sSection = trxUser.getProperty("folder");
	String browser = sessionUser.getUserBrowser();
	String htmlMenuTab = "";
	if(!(sessionUser.forceLogout() && MenuTabMgr.getInstance().lock(sSection)))
		htmlMenuTab = MenuBuilder.buildTabMenu(sessionUser,sSection,true);

	String resource = trxUser.getProperty("resource");	
	Tab tab = MenuBuilder.getFirstPage(sessionUser,sSection,true);		
	if((resource == null || resource.length() == 0))
		resource = tab.getPage();
		
	int pos = MenuBuilder.getPositionPage(sessionUser,sSection,resource,true);
%>
<html>
<head>
	<base href="<%=basePath%>">
	 <meta http-equiv="Content-Type" content="text/html; charset=<%=sessionUser.getEncoding()%>" >
	<link rel="stylesheet" href="stylesheet/plantVisor<%=browser%>.css" type="text/css" />
  <script type="text/javascript" src="scripts/arch/MenuTab.js"></script>
</head>
<body bgcolor="#000000" onload="MT_MouseClickDummy('<%=pos%>');" onmousedown="top.frames['manager'].checkServerCom();">
<div class="tabMenuStyle">
<%=htmlMenuTab%>
</div>
</body>
</html>