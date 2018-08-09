<%@page language="java" trimDirectiveWhitespaces="true"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%@page import="com.carel.supervisor.presentation.session.UserSession"%>
<%@page import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%@page import="com.carel.supervisor.presentation.session.UserTransaction"%>
<%@page import="com.carel.supervisor.plugin.energy.*"%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trans = sessionUser.getCurrentUserTransaction();
	LangService lang = LangMgr.getInstance().getLangService(sessionUser.getLanguage());
	// Set standard HTTP/1.1 no-cache headers.
	//response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
	// Set standard HTTP/1.0 no-cache header.
	//response.setHeader("Pragma", "no-cache");
	response.setContentType("application/pdf");
	EnergyReportPDF pdfReport = new EnergyReportPDF(sessionUser, response.getOutputStream());
%>