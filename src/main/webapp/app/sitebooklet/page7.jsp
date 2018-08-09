<%@ page language="java"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.session.Transaction"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.presentation.bean.SiteBookletBean"
	import="java.util.Properties"
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
bean.loadContacts();
String surname_4 = "";
String name_4 = "";
String address_4 = "";
String phone_4 = "";
String fax_4 = "";
String e_mail_4 = "";
String other_4 = "";
boolean readonly_4 = bean.isContactReadOnly(SiteBookletBean.CTYPE_HEAD_OF_AUDIT);
Properties propHeadAudit = bean.aContacts[SiteBookletBean.CTYPE_HEAD_OF_AUDIT];
if( propHeadAudit != null ) {
	surname_4 = propHeadAudit.getProperty("surname");
	name_4 = propHeadAudit.getProperty("name");
	phone_4 = propHeadAudit.getProperty("phone");
	fax_4 = propHeadAudit.getProperty("fax");
	e_mail_4 = propHeadAudit.getProperty("e_mail");
	other_4 = propHeadAudit.getProperty("other");
}
bean.loadSiteUsage();
boolean readonly = readonly_4;
String strReadOnly = readonly ? "readonly" : "";
%>
<input id="readonly" type="hidden" value="<%=readonly%>">

<table border="0">
	<tr>
		<th colspan="2">
			<h2><%=lang.getString("sitebooklet", "head_of_audit")%></h2>
		</th>
	</tr>
	<tr>
		<td width="40%"><%=lang.getString("sitebooklet", "surname")%></td>
		<td width="60%"><input size="80" maxlength="200" name="surname_4" value="<%=surname_4%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "name")%></td>
		<td><input size="80" maxlength="64" name="name_4" value="<%=name_4%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	
	<tr>
		<td><%=lang.getString("sitebooklet", "phone")%></td>
		<td><input size="80" maxlength="64" name="phone_4" value="<%=phone_4%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "fax")%></td>
		<td><input size="80" maxlength="64" name="fax_4" value="<%=fax_4%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "e_mail")%></td>
		<td><input size="80" maxlength="64" name="e_mail_4" value="<%=e_mail_4%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "other")%></td>
		<td><input size="80" maxlength="64" name="other_4" value="<%=other_4%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<th colspan="2">
			<br><br><h2><%=lang.getString("sitebooklet", "note2")%></h2>
		</th>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<textarea rows="8" cols="80" name="note2" class="<%=classInput%>" <%=strReadOnly%>><%=bean.site_usage%></textarea>
		</td>
	</tr>
</table>
