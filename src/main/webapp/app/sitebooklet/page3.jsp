<%@ page language="java"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.session.Transaction"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.presentation.bean.SiteBookletBean"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
boolean isProtected = ut.isTabProtected();
String jsession = request.getSession().getId();
boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
String classInput = bOnScreenKey ? "keyboardInput" : "standardTxt";
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
SiteBookletBean bean = new SiteBookletBean(Integer.parseInt(ut.getProperty("idbooklet")));
bean.loadCover();
boolean readonly = bean.isCoverReadOnly();
String header = !readonly && bean.header.isEmpty() ? lang.getString("sitebooklet", "dpr_date") : bean.header;
String strReadOnly = readonly ? "readonly" : "";
%>
<input id="readonly" type="hidden" value="<%=readonly%>">

<table border="0">
	<tr>
		<th colspan="2"><h1><%=lang.getString("sitebooklet", "sitebooklet")%></h1>
		<br><input size="64" maxlength="128" name="header" value="<%=header%>" class="<%=classInput%>" <%=strReadOnly%>></th>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "smk")%></td>
		<td width="50%"><input size="64" maxlength="128" name="smk" value="<%=bean.smk%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "system_type")%></td>
		<td width="50%"><input size="64" maxlength="128" name="system_type" value="<%=bean.system_type%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "circuit_id")%></td>
		<td width="50%"><input size="64" maxlength="128" name="circuit_id" value="<%=bean.circuit_id%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
</table>
