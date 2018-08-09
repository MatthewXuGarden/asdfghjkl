<%@ page language="java" %>
<html>
<head>

	<meta http-equiv="pragma" content="no-cache">
  	<meta http-equiv="cache-control" content="no-cache">
  	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script>
function loadAction()
{
	document.getElementById('stamparep').submit();
}
</script>
</head>
<body onload="loadAction();">
<form action="../../servlet/document" id="stamparep" method="post">
<input type="hidden" name="path" id="path" value="<%=request.getParameter("filepath") %>"/>
<input type="hidden" name="delete" id="delete" value="<%=request.getParameter("delete") %>"/>	
</form>	
</body>
</html>
