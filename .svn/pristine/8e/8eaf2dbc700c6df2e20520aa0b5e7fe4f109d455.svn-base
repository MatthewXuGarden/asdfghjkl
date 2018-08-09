<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.carel.supervisor.base.log.LoggerMgr" %>
<%@ page import="com.carel.supervisor.base.log.Logger" %>
<%@ page import="com.carel.supervisor.base.script.ScriptInvoker" %>
<%@ page import="com.carel.supervisor.base.config.BaseConfig" %>
<%@ page import="java.io.File" %>
<%@ page import="com.carel.supervisor.dataaccess.event.EventMgr" %>
<%

	try
	{
		EventMgr.getInstance().warning(new Integer(1), "GUARDIAN", "Action", "G002","");
		ScriptInvoker script = new ScriptInvoker();
		String name = BaseConfig.getCarelPath() + "log" + File.separator + System.currentTimeMillis() + ".process";
        script.execute(new String[] {BaseConfig.getCarelPath() + File.separator + "guardian" + File.separator +
        				 "ProcessList.exe"},
            name);		
	}
	catch(Exception e)
	{
		Logger logger = LoggerMgr.getLogger("Reboot");
		logger.error(e);
	}
	
%>
<% } else { response.sendError(404); } %>





