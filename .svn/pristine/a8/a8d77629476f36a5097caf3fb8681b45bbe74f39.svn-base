
<%@page import="com.carel.supervisor.base.log.LoggerMgr"%>
<%@page import="com.carel.supervisor.base.log.Logger"%>
<%@page import="com.carel.supervisor.remote.engine.impl.ExpDataMgr"%>
<%@ page language="java" contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>

<%
try
{
	String force = request.getParameter("force");
	if ((force != null) && (force.equalsIgnoreCase("Y")))
	{
		ExpDataMgr.getInstance().retrieve(out, true);
	}
	else
	{
		ExpDataMgr.getInstance().retrieve(out, false);
	}
}
catch(Exception e)
{
	Logger logger = LoggerMgr.getLogger(this.getClass());
	logger.error(e);
	%><%="KO"%><%
}
%>

