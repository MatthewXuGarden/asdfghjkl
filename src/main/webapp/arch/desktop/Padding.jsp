<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
</head>
<body bgcolor="#000000">
<div style="position:absolute;top:0px;left:0px;">
<table width="120%" height="50px" style="background-image:url(images/pvpro20/sfumatura.png); background-repeat:repeat-x;">
  <tr/>
    <td/> 	
  </tr>
</table>
</div>
</body>
</html>