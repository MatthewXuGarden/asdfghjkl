<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.devices.AbstractDtlDevice"
	import="com.carel.supervisor.presentation.devices.ReadOnlyDtlDevice"
%>
<jsp:useBean id="devdtl" class="com.carel.supervisor.presentation.devices.DeviceDetail" scope="request"/>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);

	AbstractDtlDevice filterVariable = new ReadOnlyDtlDevice(sessionUser,sessionUser.getLanguage(),devdtl.getIdDevice());
	filterVariable.profileVariables(devdtl.getVariablesList());
%>
<%=filterVariable.renderVariables("dtlbuno")%>