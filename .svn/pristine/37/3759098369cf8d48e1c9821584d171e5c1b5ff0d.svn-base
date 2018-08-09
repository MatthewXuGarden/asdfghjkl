<%@ page language="java" pageEncoding="UTF-8" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+request.getServerName()+":"+request.getServerPort()+path + "/";
	String jsession = request.getSession().getId();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
		<base href="<%=basePath%>">
		<title>PlantVisorPRO</title>
		  <script type="text/javascript" src="scripts/app/graph.js"></script>
	</head>
	<frameset  rows="30%,70%" frameborder="no" framespacing="0" border="0" style="z-index:2">
	   <frame src="app/mgr/Devices.jsp;jsessionid=<%=jsession%>" name="Devices" noResize frameborder="0" marginwidth="0" marginheight="0"  scrolling="no">
       <frame src="app/mgr/Variables.jsp;jsessionid=<%=jsession%>" name="Variables" noResize frameborder="0" marginwidth="0" marginheight="0"  scrolling="no">
	</frameset>
</html>


