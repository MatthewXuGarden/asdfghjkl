<%@ page language="java" %>
<%@ page import="com.carel.supervisor.presentation.devices.StateDtlDevice" %>
<%@ page import="com.carel.supervisor.presentation.session.UserSession" %>
<%@ page import="com.carel.supervisor.presentation.helper.ServletHelper" %>
<jsp:useBean id="devdtl" class="com.carel.supervisor.presentation.devices.DeviceDetail" scope="request"/>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	StateDtlDevice sdd = new StateDtlDevice(sessionUser,sessionUser.getLanguage(),devdtl.getIdDevice());
	sdd.profileVariables(devdtl.getVariablesList());
	out.print(sdd.renderVariables("dtlstatetable"));
%>
