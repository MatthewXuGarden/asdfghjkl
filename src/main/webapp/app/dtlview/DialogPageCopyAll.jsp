<%@ 
page language="java" import="java.util.*" pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.copydevice.PageImpExp"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
%>


<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
String browser  = sessionUser.getUserBrowser();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
int idsite = sessionUser.getIdSite();
String s_iddev = sessionUser.getProperty("iddev");
int iddev = Integer.parseInt(s_iddev);


String hmtltable = PageImpExp.getTableAllDeviceSameModel(iddev,sessionUser);


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    
    <title><%=lan.getString("dtlview","selectpage")%></title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
      
   	<link rel="stylesheet" type="text/css" href="stylesheet/plantVisor<%=browser%>.css">
   	<!-- script type="text/javascript" src="scripts/app/dialog_imp_exp.js"></script -->
</head>
  
<body style='background-color:#EAEAEA;'>
	<div class="dialogPageCopyAllUp">
		<table align="right" border="0" cellpadding="0" cellspacing="0" style='height:29px'>
			<tr>
 				<td id="subtab1name" valign="top" title="<%=lan.getString("dtlview","icon3tooltip")%>">
 					<!-- div id="ActBttDivEn1" style="cursor:pointer;" onclick="window.returnValue=getDevicesSelected();window.close();" -->
 					<div id="ActBttDivEn1" style="cursor:pointer;" onclick="getDevicesSelected();">
 						<img border="0" height='25px' width='25px' id="ActBttImgEn1" src="images/actions/params_on.png"/>
 					</div>
 					<div id="ActBttDivDs1" style="visibility:hidden;display:none;">
 						<img border="0" height='25px' width='25px' id="ActBttImgDs1" src="images/actions/params_off.png"/>
 					</div>
 				</td>
			</tr>
		</table>
 	</div>
 	<div class="dialogPageCopyAllDown">&nbsp;</div>
 	<br>
 	<div align='center' style='height:360px;overflow:auto;'>
  		<%=hmtltable%>
 	</div>
</body>
</html>
