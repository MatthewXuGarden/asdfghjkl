<%@ page language="java" %>
<%@ page import="com.carel.supervisor.presentation.kpi.KpiMgr" %>
<%@ page import="com.carel.supervisor.dataaccess.language.LangService" %>
<%@ page import="com.carel.supervisor.dataaccess.language.LangMgr" %>
<%@ page import="com.carel.supervisor.presentation.session.UserSession" %>
<%@ page import="com.carel.supervisor.presentation.helper.ServletHelper" %>
<%@ page import="com.carel.supervisor.dataaccess.db.RecordSet" %>

<% 
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	LangService multiLanguage = LangMgr.getInstance().getLangService(sessionUser.getLanguage());
%>

<input type="hidden" id="topdesc" value="<%=multiLanguage.getString("navbar","ncodekpi")%>"/>
<INPUT type="hidden" id="errmessage" name="errorerror" value="<%=sessionUser.getPropertyAndRemove("error")%>" />
<p class='standardTxt'><%=multiLanguage.getString("kpi","comment1")%></p>
<table border="0" width="100%" height="90%" cellspacing="1" cellpadding="1">
<%
RecordSet rs = KpiMgr.getGroups();
if(rs!=null && rs.size()>0)
{
%>
<TBODY><TR><TD valign="top">
	<table width="97%" border="0" cellspacing="20" cellpadding="20">
		<TBODY>
<%
for(int i=0;i<KpiMgr.grpNum();i+=2)
{
%>
			<TR height="60px" valign="middle">
				<TD width="50%" align="center" bgcolor="#ffffff" style="border: solid black 1px" style="cursor: pointer;" onmouseover='kpiover(this,true);' onmouseout='kpiout(this,false);' onclick="openresult(<%=rs.get(i).get("idgrp")%>, '<%=rs.get(i).get("name")%>');" title="<%=multiLanguage.getString("kpi","click") %>" ><%=rs.get(i).get("name")%></TD>
<%
if(i+1<KpiMgr.grpNum())
{
%>
				<TD width="50%" align="center" bgcolor="#ffffff" style="border: solid black 1px" style="cursor: pointer;" onmouseover='kpiover(this,true);' onmouseout='kpiout(this,false);' onclick="openresult(<%=rs.get(i+1).get("idgrp")%>, '<%=rs.get(i+1).get("name")%>');"  title="<%=multiLanguage.getString("kpi","click") %>"><%=rs.get(i+1).get("name")%></TD>
<%
}
%>
			</TR>
<%
}
%>
		</TBODY>
	</table>
</TD> </TR> </TBODY>
<%
}
else
{
%>
	<tr height="95%" valign="top">
		<td class="tdTitleTable" align="center"><%=multiLanguage.getString("kpi","noconfigured")%></td>
	</tr>
<%
}
%>
</table>