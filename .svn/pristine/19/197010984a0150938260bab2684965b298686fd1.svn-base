<%@ page language="java"  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="com.carel.supervisor.director.guardian.GuardianCheck"
%>
<%
	String jsession = request.getSession().getId();
	String msgs = GuardianCheck.getEnableWin();
%>
<html>
<head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/> 
	<title>guardianPRO notification window</title>
	<script>
		function snoozeGp()
		{
			document.getElementById("cmd").value="GPSND";
			document.getElementById("gpaction").submit();
		}
		function ackGp()
		{
			document.getElementById("cmd").value="GPACK";
			document.getElementById("gpaction").submit();
		}
	</script>
	<style type="text/css">
		.gproInput {
			border: 0px;
			cursor: hand;
			width: 57px;
			height: 20px;
			background-image: url('../../images/login_bt.png');
			background-color: transparent;
			color:white;
		}	
	</style>
	<base target=_self />
</head>
<body onload="window.name='gpfinestrella'">
<table border="0" width="100%" height="100%" cellpadding="1" cellspacing="1">
	<tr height="*">
		<td colspan="5">
			<textarea id="msgs" style="width:100%;" rows="10" readonly><%=msgs %></textarea>
		</td>
	</tr>
	<tr height="5%">
		<td width="20%" valign="middle" align="center"><input type="button" name="btnLogin" class="gproInput" value="ACK" onclick="ackGp();"/></td>
		<td width="20%" valign="middle" align="left" ><input type="button" name="btnLogin" class="gproInput" value="snooze" onclick="snoozeGp();"/></td>
		<td width="*"></td>
		<td width="20%" valign="middle" align="right"><input type="button" name="btnLogin" class="gproInput" value="close" onclick="window.close();"/></td>
	</tr>
</table>
<form id="gpaction" action="../../servlet/ajrefresh;jsessionid=<%=jsession%>" method="GET" >
<input type="hidden" name="cmd" id="cmd" value="GPSND"/>
</form>
</body>
</html>
