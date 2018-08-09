<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.devices.AbstractDtlDevice"
	import="com.carel.supervisor.presentation.devices.ButtonDtlDevice"
%>
<jsp:useBean id="devdtl" class="com.carel.supervisor.presentation.devices.DeviceDetail" scope="request"/>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);

	AbstractDtlDevice filterVariable = new ButtonDtlDevice(sessionUser,sessionUser.getLanguage(),devdtl.getIdDevice());
	filterVariable.setIdDevMdl(devdtl.getIdDevMdl());
	filterVariable.profileVariables(devdtl.getVariablesList());
	boolean can_set = sessionUser.isButtonActive("dtlview","tab1name","subtab2name");
%>
<input type='hidden' id='can_set' value='<%=can_set%>' />
<%=filterVariable.renderVariables("")%>