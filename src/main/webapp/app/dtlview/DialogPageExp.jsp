<%@ 
page language="java" import="java.util.*" pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.copydevice.PageImpExp"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>

<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
String browser  = sessionUser.getUserBrowser();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title><%=lan.getString("dtlview","filename")%></title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
       
    <link rel="stylesheet" type="text/css" href="stylesheet/plantVisor<%=browser%>.css">
    <link rel="stylesheet" href="stylesheet/keyboard.css" type="text/css" />
    <script type="text/javascript" src="scripts/app/dialog_imp_exp.js"></script>
   	<script type="text/javascript" src="scripts/app/keyboard.js"></script>
   
  </head>
  
  <body style='background-color: #EAEAEA;'>
  <%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
  	<br>
  	<TABLE>
  		<TR>
  			<TD class='standardTxt'><b><%=lan.getString("dtlview","writename")%> <%=BaseConfig.getCarelPath() + PageImpExp.DIR_EXPORT_FILE %></b></TD>
  		</TR>
  		<TR>
  			<TD>
  				<input type='text' class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' id='namefile' style='width:250px;' maxlength="30"/>		
  			</TD>
  			<TD>
  				<IMAGE src='images/actions/save_on.png' title='<%=lan.getString("dtlview","save")%>' onclick="window.returnValue=getFileName();window.close();"/>
  			</TD>
  		</TR>
  	</TABLE> 
  </body>
</html>

