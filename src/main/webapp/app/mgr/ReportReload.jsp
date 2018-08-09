<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.carel.supervisor.report.PrinterMgr2"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Report Reload</title>
</head>
<body>
<%
try{
	PrinterMgr2.getInstance().reload();
	out.print("Reload OK");
}catch(Exception e){
	out.print("Reload fail");
}
%>
</body>
</html>