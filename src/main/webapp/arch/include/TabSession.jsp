<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <base href="<%=basePath%>">
  <script>
  var tabsess = 0;
  function sessionTimeout()
  {
  	try
  	{
  		top.frames["manager"].Redirect("servlet/logout");
  	}
  	catch(e)
  	{
  		tabsess++;
  		if(tabsess != 3)
  			top.frames["manager"].Redirect("servlet/logout");	
  	}
  }
  </script>  
  </head>
  <body onload="sessionTimeout()" bgcolor="#000000">
  </body>
</html>
