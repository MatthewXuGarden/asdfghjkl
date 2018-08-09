<%@ page language="java"%>
<%@ page import="com.carel.supervisor.presentation.session.UserSession" %>
<%@ page import="com.carel.supervisor.presentation.helper.ServletHelper" %>
<%@ page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@ page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%@page import="com.carel.supervisor.presentation.lucinotte.LNDevsGroup;"%>

<%

UserSession sessUsr = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
int idsite= sessUsr.getIdSite();
String language = sessUsr.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();

int idgrp = Integer.parseInt(sessUsr.getProperty("idgrp"));

String table = LNDevsGroup.getGroupDtl(language, idsite, idgrp, sessUsr.getScreenHeight(), sessUsr.getScreenWidth());
String menu_item = lan.getString("menu","lucinotte");

%>

<input type='hidden' id='menu_item' value='<%=menu_item%>'/>
<div class='standardTxt'><%=lan.getString("lucinotte","comment1a")%></div>	
<BR />

<FIELDSET class='field' style='width:80%'>
	<LEGEND  class="standardTxt">
		&nbsp;<span><%=lan.getString("ac","legend")%></span>&nbsp;
	</LEGEND>
	<table align='center' class="standardTxt" width='85%'>
		<tr class="standardTxt">
			<td width='15%' align='right'> <img src="images/event/error.gif" /> &nbsp;</td>
			<td width='5%'> = </td>
			<td width='20%'><%=lan.getString("ac","read_error")%>;</td>
			<td width='20%'>&nbsp;</td>
			<td width='15%' align='right'> <img src="images/event/alert.gif" /> &nbsp;</td>
			<td width='5%'> = </td>
			<td width='20%'><%=lan.getString("ac","not_conf")%>;</td>
		</tr>
	</table>
</FIELDSET>
<BR />
<BR />
<%=table%>
