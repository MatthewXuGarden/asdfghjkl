<%@ page language="java"%>
<%@ page import="com.carel.supervisor.presentation.session.UserSession" %>
<%@ page import="com.carel.supervisor.presentation.helper.ServletHelper" %>
<%@ page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@ page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%@ page import="com.carel.supervisor.presentation.ac.MasterBeanList"%>
<%@ page import="com.carel.supervisor.presentation.ac.MasterBean"%>


<%

UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();

int iddevmstr = Integer.parseInt(sessionUser.getProperty("iddevmstr"));

MasterBeanList mbl = new MasterBeanList();
MasterBean mb = mbl.getMasterById(iddevmstr);

String table = mb.getMasterDtlTable(sessionUser, sessionUser.getScreenHeight(), sessionUser.getScreenWidth());
String menu_item = lan.getString("menu","ac");

%>

<input type='hidden' id='menu_item' value='<%=menu_item%>'/>
<BR />

<FIELDSET class='field' style='width:95%'>
	<LEGEND  class="standardTxt">
		&nbsp;<span><%=lan.getString("ac","legend")%></span>&nbsp;
	</LEGEND>
	<table align='right' class="standardTxt" width='100%'>
		<tr class="standardTxt">
			<td width='7%' align='right'> *** &nbsp;</td>
			<td width='2%'> = </td>
			<td width='13%'><%=lan.getString("ac","not_read")%>;</td>
			<td width='15%'></td>
			<td width='7%' align='right'> --- &nbsp;</td>
			<td width='2%'> = </td>
			<td width='13%'><%=lan.getString("ac","not_conf")%>;</td>
			<td width='15%'></td>
			<td width='7%' align='right'><span style="color:RED"> err </span>&nbsp;</td>
			<td width='2%'> = </td>
			<td width='13%'><%=lan.getString("ac","read_error")%>;</td>
		</tr>
	</table>
</FIELDSET>
<BR /><BR />
<%=table%>
