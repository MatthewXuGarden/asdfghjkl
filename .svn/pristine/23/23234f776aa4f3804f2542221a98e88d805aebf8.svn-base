<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.sdk.util.CustomChecker"
%>
<jsp:useBean id="CurrUnit" class="com.carel.supervisor.presentation.sdk.obj.CurrUnit" scope="session"/>
<%
	String fileName = "secB.jsp";
	//CurrUnit.setCurrentSession(ServletHelper.retrieveSession(request.getRequestedSessionId(),request));
	String path = CustomChecker.isSectionCustomFor(CurrUnit.getIdMdl(),fileName);
	if(path != null && !path.equalsIgnoreCase("NOP"))
	{
		path = "../../custom/dtlview_section/"+path+"/"+fileName;
		try {
%>
	<jsp:include page="<%=path%>" flush="true" />
<%
		} catch(Exception e) {}		
	}
%>
