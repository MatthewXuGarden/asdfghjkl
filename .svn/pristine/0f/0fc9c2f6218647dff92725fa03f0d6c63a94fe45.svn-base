<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String browser  = sessionUser.getUserBrowser();
	
	String menuHidden = "";
	if(sessionUser.getProfileNomenu())
	{
		menuHidden = "visibility:hidden;";
	}
%>
<html>
<head>
	<base href="<%=basePath%>">
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">
  <meta http-equiv="Content-Type" content="text/html; charset=<%=sessionUser.getEncoding()%>" >
  <script type="text/javascript" src="scripts/arch/SlideMenu.js"/></script>
  <script type="text/javascript" src="scripts/arch/util.js"/></script>
  <link rel="stylesheet" type="text/css" href="stylesheet/plantVisor<%=browser%>.css">
  
  <script>
  function automaticLoad(counter)
  {
  	// check if top.jsp successfully loaded
  	var objMenuVoice = top.frames["header"].document.getElementById('exit');
  	if(objMenuVoice == null || objMenuVoice == undefined)
  		setTimeout("automaticLoad()",200);
  	else
  		top.frames["header"].loadMenu();
  }
  </script>
  
</head>
<body bgcolor="#000000"  style='margin:0px;' onload="automaticLoad();" onmousedown="top.frames['manager'].checkServerCom();">
	<table width='100%' border=0 height='100%' valign='top' cellspacing="0" cellpadding="0" style="background-image:url(images/pvpro20/sfumatura.png); background-repeat:repeat-x;">
		<tr>
			<td width='82%' align='center'>
				<div id="MainMenu" style="padding-left:0;<%=menuHidden %>">
					
				</div>
			</td>
			<td width='18%' height='100%'>
				<div id="divNavi" style="width:100%;height:100%;"></div>
			</td>
		</tr>
	</table>
</body>
</html>