
<%@page import="com.carel.supervisor.base.log.LoggerMgr"%>
<%@page import="com.carel.supervisor.base.log.Logger"%>
<%@page import="com.carel.supervisor.remote.engine.impl.ExpDataMgr"%>
<%@page import="com.carel.supervisor.base.io.BufferFileWriter"%>
<%@page import="java.io.File"%>
<%@page import="com.carel.supervisor.base.config.BaseConfig"%>
<%@page import="com.carel.supervisor.base.conversion.StringUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF8" pageEncoding="UTF8"%>

<%
try
{
	String structure = request.getParameter("structure");
	String descFile = BaseConfig.getCarelPath() + File.separator + "scheduler" + File.separator + "conf" + File.separator + "InitComm.conf";
	BufferFileWriter writer = new BufferFileWriter(descFile, false);
	writer.write(structure);
	writer.close();
	ExpDataMgr.getInstance().load();
	%><%="OK"%><%
}
catch(Exception e)
{
	Logger logger = LoggerMgr.getLogger(this.getClass());
	logger.error(e);
	%><%="KO"%><%
}
%>

