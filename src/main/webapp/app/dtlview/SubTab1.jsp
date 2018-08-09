<%@ page language="java"%>
<%@ page import="com.carel.supervisor.presentation.session.UserSession" %>
<%@ page import="com.carel.supervisor.presentation.helper.ServletHelper" %>
<%@ page import="com.carel.supervisor.presentation.bean.DeviceStructureList" %>
<%@ page import="com.carel.supervisor.presentation.bean.DeviceStructure" %>
<%@ page import="com.carel.supervisor.plugin.customview.CustomViewMgr" %>
<%@ page import="com.carel.supervisor.plugin.customview.CustomView" %>
<%@ page import="com.carel.supervisor.director.DirectorMgr"%>
<%@ page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@ page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%@ page import="com.carel.supervisor.base.config.*"%>	

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String sitename = sessionUser.getSiteName();
	CustomView view = null;
	int idDev = -1;
	String target = "Default.jsp";
	
	try {
		idDev  = Integer.parseInt(sessionUser.getProperty("iddev"));
	}
	catch(NumberFormatException e){}	
	DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
	DeviceStructure deviceStructure = deviceStructureList.get(idDev);
		
	view = CustomViewMgr.getInstance().hasDeviceCustomView(deviceStructure.getIdDevMdl());
	if(view != null)
		target = view.getTarget();
		
	LangService lan = LangMgr.getInstance().getLangService(sessionUser.getLanguage());
	
	
	//check set permission of parameters
	int permission = 0;
	if (sessionUser.getVariableFilter()==ProfileBean.FILTER_SERVICES)
	{
		permission = 2;	
	}
	// set value with note
	String strValueNote = ProductInfoMgr.getInstance().getProductInfo().get("value_note");
	
%>
<% if (DirectorMgr.getInstance().isMustCreateProtocolFile()) {%>
	
<%@page import="com.carel.supervisor.presentation.bean.ProfileBean"%><p class='standardTxt'><%=lan.getString("dtlview","restarttoview")%></p>
	<input type='hidden' id='js_control'/>
<%}else { %>
	<% try { %>
			<jsp:include page="<%=target%>" flush="true" />
	<% } catch(Exception e) { %>
			<jsp:include page="../../arch/include/Error.jsp" flush="true" />
	<%}%>
<%}%>

<INPUT type="hidden" id="sitename" value="<%=sitename%>" />
<INPUT type="hidden" id="permission" value="<%=permission%>" />
<input type='hidden' id='strValueNote' value="<%= strValueNote%>"/>
