<%	com.carel.supervisor.presentation.session.UserSession sessionUser = com.carel.supervisor.presentation.helper.ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	if( sessionUser != null && sessionUser.getProfileCode().equals("System Administrator") ) {
%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	import="java.io.*"
%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String filepath = request.getParameter("pathval");
String result = "";
try {
	FileInputStream fis = new FileInputStream(filepath);
	byte[] buffer = new byte[fis.available()];
	fis.read(buffer);
	result = new String(buffer);
}
catch(Exception e) {
	e.printStackTrace();
}

%>

<html>
  <head>
    <base href="<%=basePath%>">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
  <script>

function golog(event)
{
	if(event.keyCode == 13)
		document.getElementById("frmlog").submit();
}
</script>  
  </head>
  
  <body onkeydown="golog(event);">
 <form id="frmlog" name="frmlog" action="arch/manager/LogReader.jsp" method="post">
  <table border="1" cellspacing="1" cellpadding="1" width="100%">
  	<tr>
  		<td><input type="text" name="pathval" id="pathval"/></td>
  	</tr>	
  	<tr>
		<td>
			<textarea style="width:100%;" rows="30" name="logresult" id="logresult"><%=result%></textarea>
		</td>
	</tr>
  </table>
 </form>
  </body>
</html>
<%} else if( sessionUser != null ) response.sendError(403);
	else response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
%>