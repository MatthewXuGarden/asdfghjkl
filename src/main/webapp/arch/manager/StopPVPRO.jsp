<%	
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	com.carel.supervisor.presentation.session.UserSession sessionUser = com.carel.supervisor.presentation.helper.ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	if( basePath.startsWith("http://localhost:80/") || (sessionUser != null && sessionUser.getProfileCode().equals("System Administrator")) ) {
%>
<%@ page import="com.carel.supervisor.director.DirectorMgr" %>
<%@ page import="com.carel.supervisor.dispatcher.DispatcherMgr" %>
<%
  	DispatcherMgr.getInstance().stopService();  
  		
  	try
  	{
  		Thread.sleep(5000L);
  	}	
  	catch(Exception e)
  	{
  	}
  	DirectorMgr.getInstance().stopPVPRO(); 
      
%>
<%} else if( sessionUser != null ) response.sendError(403);
	else response.sendRedirect(basePath);
%>