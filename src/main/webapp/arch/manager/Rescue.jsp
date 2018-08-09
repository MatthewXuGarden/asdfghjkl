<%	com.carel.supervisor.presentation.session.UserSession sessionUser = com.carel.supervisor.presentation.helper.ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	if( sessionUser != null && sessionUser.getProfileCode().equals("System Administrator") ) {
%>
<%@ page language="java" pageEncoding="UTF-8"

	import="com.carel.supervisor.base.script.ScriptInvoker"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	String disable_act = "";
	boolean disab_ok = false;
	
	if (session != null)
	{
		disable_act = request.getParameter("disable_act");

		if(Boolean.parseBoolean(disable_act))
		{
			ScriptInvoker script = new ScriptInvoker();
			try
			{
			    script.execute(new String[] { "net", "stop", "SharedAccess" },"C:\\RESULT.TXT" );
			    disab_ok = true;
			}
			catch (Exception e)
			{ }		
		}	
	}
	
%>

<html>
<head>
<base href="<%=basePath%>">
	<script>
	function stopService()
	{
		document.getElementById("disable_act").value = "true";
		document.getElementById("frmdisabfirew").submit();
	}

	function gopage()
	{
		if(event.keyCode == 13)
			document.getElementById("frmdisabfirew").submit();
	}
	</script>
</head>

<body>
<form id="frmdisabfirew" name="frmdisabfirew" action="arch/manager/Rescue.jsp;jsessionid=<%=session.getId()%>" method="post">
<table border="0" cellspacing="1" cellpadding="1" width="100%">
	<tr>
		<td><button onclick='stopService()'>Stop</button></td>
		<% if(Boolean.parseBoolean(disable_act) && disab_ok) {%>
		<td>Service stopped</td>
		<% 
			}
		if(Boolean.parseBoolean(disable_act) && !disab_ok) {
		%>
		<td>Service stop failure</td>
		<% } 
		%>
		<td width='80%'></td>
	</tr>	
</table>

<input type="hidden" name="disable_act" id="disable_act" value="false" />

</form>
</body>
</html>
<%} else if( sessionUser != null ) response.sendError(403);
	else response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
%>