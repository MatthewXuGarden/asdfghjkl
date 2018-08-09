<%@ page language="java" pageEncoding="UTF-8"
	import="java.util.Locale"	
%>
<%
	Locale.setDefault(Locale.ENGLISH);
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String idsite = request.getParameter("idsite").toString();
	String lang = request.getParameter("language").toString();
	String user = "roffline";
	String passw = "roffline";
%>

<html>
<head>
  <title>PlantVisorPRO</title>
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">
  <script type="text/javascript" src="../../scripts/arch/Login.js"></script>
</head>
<body bgcolor="#cccccc" onload="detectBrowserType();document.getElementById('frmauth').submit();" tabindex="-1">
<form action="../../servlet/login" method="post" target="_top" id="frmauth">

<input type="hidden" name="txtUser" id="txtUser" value='<%=user%>' />
<input type="hidden" name="txtPassword" id="txtPassword" value='<%=passw%>'/>
<input type='hidden' name="txtLanguage" id="txtLanguage" value='<%=lang%>'>
<INPUT type='hidden' name='remote' value='yes'>
<INPUT type='hidden' name='idsite' value='<%=idsite%>'>
<input type="hidden" name="browser" id="browser">

</form>
</body>
</html>