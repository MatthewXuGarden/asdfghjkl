<%	com.carel.supervisor.presentation.session.UserSession sessionUser = com.carel.supervisor.presentation.helper.ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	if( sessionUser != null && sessionUser.getProfileCode().equals("System Administrator") ) {
%>
<html>
<head>
<title>MasterXML Commander</title>
</head>
<body>

<!--<requests>-->
<!--<login userName="admin" password="admin" />-->
<!--<request type="devicesList" language="IT_it" >-->
<!--<element iddevices="-1" />-->
<!--</request>-->
<!--</requests>-->

<!--<requests>-->
<!--<login userName="admin" password="admin" />-->
<!--<request type="parametersList" language="IT_it" >-->
<!--<element idDevice="id_dispositivo" idsVariable="-1" />-->
<!--</request>-->
<!--</requests-->

<!--<requests>-->
<!--<login userName="admin" password="admin" />-->
<!--<request type="alarmList" language="IT_it" >-->
<!--<element start="0" length="10"/>-->
<!--</request>-->
<!--</requests>-->

<form action="sendrequest();return false;">
	<textarea id="input" rows="15" cols="100">&lt;xml&gt;</textarea>
	<br>
	<input type="button" value="Send" onclick="sendrequest();return false;">
</form>

<textarea id='results' rows="30" cols="150">
</textarea>

</body>

<script type="text/javascript">

	function sendrequest() {
		if (window.XMLHttpRequest)
			xmlCommReq = new XMLHttpRequest();
		else if (window.ActiveXObject)
			xmlCommReq = new ActiveXObject("Microsoft.XMLHTTP");

		if (xmlCommReq != null) {
			try {
				xmlCommReq.open("POST", "/PlantVisorPRO/servlet/MasterXML", true);
				xmlCommReq.onreadystatechange = callbackFunction;
				xmlCommReq.send(document.getElementById("input").value);
			} catch (e) {
				alert("err");
			}
		}
	}

	function callbackFunction() {
		if (xmlCommReq.readyState == 4) {
			if (xmlCommReq.status == 200) {
				try {
					xmlResponse = xmlCommReq.responseXML;
					txtResponse = xmlCommReq.responseText;
					// --- gestione risposta
					document.getElementById("results").value=xmlResponse+"\n"+txtResponse;
					xmlResponse = null;
				} catch (e) {
					alert("err");
				}
			}
		}
	}
</script>
</html>
<%} else if( sessionUser != null ) response.sendError(403);
	else response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
%>