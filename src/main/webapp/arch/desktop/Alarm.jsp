<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.bo.master.IMaster"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.device.DeviceStatusMgr"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr"
	import="com.carel.supervisor.controller.setfield.NotificationParam"
  	import="java.util.Date"
	import="java.util.Calendar"
	import="java.util.GregorianCalendar"
  	import="com.carel.supervisor.base.conversion.DateUtils"
%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String jsession = request.getSession().getId();
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String menuHidden = "";
	if(sessionUser.getProfileNomenu())
	{
		menuHidden = "visibility:hidden;";
	}
	
	boolean restart_on = sessionUser.isButtonActive("mgr","tab1name","restartEngine");
	boolean guardian_on = sessionUser.isTabActive("mgr","tab5name");
	boolean alarms_on = sessionUser.isMenuActive("alrglb");
	boolean multimessage_on = sessionUser.isTabActive("mgr","tab6name");
%>
<html>
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">
  <script type="text/javascript" src="scripts/arch/Top.js"></script>
  <script type="text/javascript" src="scripts/arch/Timer.js"></script>
  <script type="text/javascript" src="scripts/arch/PM.js"></script>
  <script type="text/javascript" src="scripts/arch/comm/Communication.js"></script>
  <script type="text/javascript" src="scripts/arch/util.js"/></script>
 <script>
 var timeobject = null;
 function genCall4logout()
 {
		document.getElementById("forceLogoutAl").submit();
 }
 function alarmAJAXRefresh()
 {
	 if(timeobject != null)
	 	 clearTimeout(timeobject);
	 timeobject = setTimeout("alarmAJAXRefresh()",<%=((int)SystemConfMgr.getInstance().get("autorefresh").getValueNum())*1000%>); 	
	 new AjaxRequest("servlet/ajrefresh", "GET","cmd=alarm", callback_alarmAJAXRefresh, true);
 }
 function callback_alarmAJAXRefresh(xml)
 {
	 if(xml.getElementsByTagName("usernull")[0] != null)
	 {
		 var usernull = xml.getElementsByTagName("usernull")[0].childNodes[0].nodeValue;
		 if(usernull == "1")
		 {
			 genCall4logout();
		 }
	 }
	 if(xml.getElementsByTagName("refresh")[0] != null)
	 {
		 var refresh = xml.getElementsByTagName("refresh")[0].childNodes[0].nodeValue;
		 if(refresh != "-1")
		 {
			 top.frames['body'].frames['Poller'].startThreadPoller();
		 }
	 }
	 if(xml.getElementsByTagName("status")[0] != null)
	 {
		var tdalarmimg = document.getElementById("tdalarmimg");
		var status = xml.getElementsByTagName("status")[0].childNodes[0].nodeValue;
		var multimessage_on = document.getElementById('multimessage_on').value;
		var alarm_on = document.getElementById('alarm_on').value;
		if(status == "red")
		{
			if(xml.getElementsByTagName("onlyalarm")[0] == null)
			{
				tdalarmimg.innerHTML = "<img id='alarmImg' src='images/menusx/multi_icon.png' title=''  border='0' " +(multimessage_on=='true'?"style='cursor:pointer;' onclick=top.frames['manager'].loadTrx('nop&folder=mgr&bo=BSystem&type=menu&resource=SubTab6.jsp&curTab=tab6name');":"")+ " >";
			}
			else
			{
				tdalarmimg.innerHTML = "<img id='alarmImg' src='images/menusx/multi_icon.png' title=''  border='0' " +(alarm_on=='true'?"style='cursor:pointer;' onclick=top.frames['manager'].loadTrx('nop&folder=alrglb&bo=BAlrGlb&type=menu');":"")+ ">";
			}
		}
		else if(status == "yellow")
		{
			tdalarmimg.innerHTML = "<img id='alarmImg' src='images/menusx/multi_icon_orange.png' title='' border='0' " +(multimessage_on=='true'?"style='cursor:pointer;' onclick=top.frames['manager'].loadTrx('nop&folder=mgr&bo=BSystem&type=menu&resource=SubTab6.jsp&curTab=tab6name');":"")+ " >";
		}
		else if(status == "ok")
		{
			tdalarmimg.innerHTML = "";
		}
	 }

	 var xmlRestartEngine = xml.getElementsByTagName("restart_engine")[0];
	 if( xmlRestartEngine != null) {
		 if( xmlRestartEngine.childNodes[0].nodeValue == "true" ) {
			var message = "<%=lan.getString("top", "message1")%>";
			var restart_on = document.getElementById('restart_on').value;
			var action = "";
			if (restart_on=='true')
				action = "<img src='images/actions/restart_on_black.png' onClick='restartEngine()' class='ballonMsgImage'/>";
			showBalloon("", message, action);
		 }
	 }
	 var xmlLoggingOverload = xml.getElementsByTagName("logging_overload")[0];
	 if( xmlLoggingOverload != null) {
		 var n = parseInt(xmlLoggingOverload.childNodes[0].nodeValue, 10);
		 var message1 = "<%=lan.getString("mgr", "sysoverload1")%>";
		 var message2 = "<%=lan.getString("mgr", "sysoverload2")%>" + n;
			showBalloon("", message1, message2);
	 }
	 var xmlDeviceDetection = xml.getElementsByTagName("device_detection")[0];
	 if( xmlDeviceDetection != null && xmlDeviceDetection.childNodes[0].nodeValue ) {
		 var message = "<%=lan.getString("mgr", "device_detection")%>";
			showBalloon("", message, "", 10);
	 }
	 var xmlNotify = xml.getElementsByTagName("notify")[0];
	 if( xmlNotify != null ) {
		 var message = xmlNotify.childNodes[0].nodeValue;
		 showBalloon("", message, "", 10);
	 } 
	 var xmlBackupTool = xml.getElementsByTagName("backup_tool")[0];
	 if( xmlBackupTool != null ) {
		 var message = "<%=lan.getString("multimsg", "BTAlert1")%>" + xmlBackupTool.childNodes[0].nodeValue;
		 showBalloon("<%=lan.getString("multimsg", "BTname")%>", message, "");
	 }
 }
 function initiateAlarmRefresh()
 {

	 if( top.frames["manager"].jsessionid /*typeof(top.frames["manager"].getSessionId) == "function"*/)
	 {
		 alarmAJAXRefresh();
	 }
	 else
	 {
		 setTimeout("initiateAlarmRefresh()",500);
	 }

 }
</script>
</head>
<body bgcolor="#000000" onload="initiateAlarmRefresh();" style="margin:0px" onmousedown="top.frames['manager'].checkServerCom();">

<input type='hidden' id='restart_on' value='<%=restart_on%>' />
<input type='hidden' id='guardian_on' value='<%=guardian_on%>' />
<input type='hidden' id='alarm_on' value='<%=alarms_on%>' />
<input type='hidden' id='multimessage_on' value='<%=multimessage_on%>' />

<table width="100%" height="100%" border=0 style="background-image:url(images/pvpro20/sfumatura.png); background-repeat:repeat-x;">
  <tr>   
    <td align="center" valign="top" id="tdalarmimg" style="<%=menuHidden %>">
    </td>
  </tr>
</table>
<form name="forceLogoutAl" id="forceLogoutAl" method="post" action="servlet/master;jsessionid=<%=jsession%>">
</form>		
</body>
</html>