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

String combo = PageImpExp.getComboConfigImport(idsite, iddev);

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title><%=lan.getString("dtlview","importdevset")%></title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
      
   	<link rel="stylesheet" type="text/css" href="stylesheet/plantVisor<%=browser%>.css">
   	<script type="text/javascript" src="scripts/app/dialog_imp_exp.js"></script>
    
  </head>
  
  <body style='background-color:#EAEAEA;'>
  	<br>
  <% if(combo == null) {%>
  	<DIV valign='middle' align='center'>
  	<TABLE>
  		<TR>
  			<TD class='standardTxt'><b><%=lan.getString("dtlview","noconfigfiles")%></b></TD>
  		</TR>
  	</TABLE>
  	</DIV>
  <% }%>
  <% if( combo!= null) {%>
  	<TABLE>
  		<TR>
  			<TD class='standardTxt'><b><%=lan.getString("dtlview","selectfile")%></b></TD>
  		</TR>
  		<TR>
  			<TD>
  				<%=combo%>	
  			</TD>
  			<TD>
  				<IMAGE src='images/actions/save_on.png' title='<%=lan.getString("dtlview","imp")%>' onclick="window.returnValue=getFileSelected();window.close();"/>
  			</TD>
  		</TR>
  	</TABLE> 
  	<% }%>
  </body>
</html>
