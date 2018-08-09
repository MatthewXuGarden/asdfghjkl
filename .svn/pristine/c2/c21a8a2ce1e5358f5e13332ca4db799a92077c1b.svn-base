<%@ page language="java"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%@page import="com.carel.supervisor.presentation.session.UserSession"%>
<%@page import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%@page import="com.carel.supervisor.presentation.session.UserTransaction"%>
<%@page import="com.carel.supervisor.plugin.energy.EnergyReport"%>

<html>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trans = sessionUser.getCurrentUserTransaction();
	LangService lan = LangMgr.getInstance().getLangService(sessionUser.getLanguage());
	String sp = request.getParameter("print");
	String onLoadAction = "";
	if(sp!= null && sp.equalsIgnoreCase("y"))
		onLoadAction = "window.print();";
%>
<head>
<style type="text/css" media="print">
body 
{
	margin-top: 20px;
	margin-bottom: 20px;
}
</style>
</head>
<body onload="<%=onLoadAction %>">
<%=lan.getString("energy", "reptype")%>: 
<%
String root = ""+trans.getAttribute("root"); 
if(root.equalsIgnoreCase("site")){
	out.write(lan.getString("energy", "enersite"));
} else if (root.startsWith("group")){
	out.write(lan.getString("energy", "energroup"));
	out.write(""+trans.getAttribute("group"));
} else if(root.startsWith("cons")){
}
%>
<br>
<%=lan.getString("energy", "enerfrom")%>: <%=trans.getAttribute("from") %>
<%=lan.getString("energy", "enerto")%>: <%=trans.getAttribute("to") %> - 
<%=lan.getString("energy", "enerdataexportstep")%>: <%=lan.getString("energy", "report"+trans.getAttribute("step"))%>
<br>
<%=lan.getString("energy", "repuser")%>: <%=sessionUser.getUserName()%>
<br>
<%=lan.getString("energy", "repsite")%>: <%=sessionUser.getSiteName()%>
<img alt="Graph" 
	 align="middle" 
	 src="/PlantVisorPRO/SRVLCharts;jsessionid=<%=request.getRequestedSessionId() %>?charttype=<%=request.getParameter("charttype")%>&width=<%=request.getParameter("width")%>&height=<%=request.getParameter("height")%>&imgid=<%=request.getParameter("imgid")%>" />
</body>
</html>

