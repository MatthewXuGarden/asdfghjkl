<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.carel.supervisor.presentation.session.UserSession"%>
<%@ page language="java" import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%@ page import="com.carel.supervisor.remote.engine.connection.ActiveConnections" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String jsession = request.getSession().getId();
	
	String localUrl = ActiveConnections.getInstance().getIpLocalInOut();
	String localType = ActiveConnections.getInstance().getTypeLocalOut();
	
	if((localUrl != null) && (!localUrl.equalsIgnoreCase("")))
	{
		if((localType != null) && (localType.equalsIgnoreCase("PVP")))
			localUrl = request.getScheme()+"://"+localUrl+":"+request.getServerPort()+"/PlantVisorPRO/servlet/login;jsessionid="+jsession;
		else
			localUrl = "http://"+localUrl;
	}
	else
		localUrl = null;
		
	boolean isConnClient = ActiveConnections.getInstance().isConnections();
%>

<html>
  <head>
    <base href="<%=basePath%>">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=<%=sessionUser.getEncoding()%>" >
    <script>
    var idreload = 0;
    function browsLocal()
    {
    	var sUrl = "";
    	<%if(localUrl != null) {%>
    	sUrl = "<%=localUrl%>";
    	<%}%>
    	
    	if(sUrl != "")
    	{
	    	var owin = window.open(sUrl,"","");	
	    	if(top.frames["body"] != null)
	    	{
	    		if(top.frames["body"].frames["bodytab"] != null)
	    		try {
	    			top.frames["body"].frames["bodytab"].RE_updateRefWindow(owin);
	    		}
	    		catch(e){}
	    	}
	    }
    }
    
    function reloadConnState()
    {
    	clearTimeout(idreload);
    	idreload = setTimeout("reloadConnStatePrv()",5000); 	
    }
    
    function reloadConnStatePrv()
 		{
 			top.frames["allarm"].location.reload();
 		}
    
    </script>
  </head>
  <body onload="reloadConnState();">
  <table border="0" width="100%" cellpadding="1" cellspacing="1">
  	<tr>
  		<td valign="top" align="center">
  			<%if(isConnClient) {%>
  				<img src="images/remote/online.gif" onclick="browsLocal();">
  			<%}%>
  		</td>
  	</tr>
  </table>
  </body>
</html>
