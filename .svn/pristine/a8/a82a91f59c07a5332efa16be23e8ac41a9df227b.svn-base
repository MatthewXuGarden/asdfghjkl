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
String surname_0 = "";
String address_0 = "";
String phone_0 = "";
String fax_0 = "";
String e_mail_0 = "";
boolean readonly_0 = bean.isContactReadOnly(SiteBookletBean.CTYPE_OWNER);
Properties propOwner = bean.aContacts[SiteBookletBean.CTYPE_OWNER];
if( propOwner != null ) {
	surname_0 = propOwner.getProperty("surname");
	address_0 = propOwner.getProperty("address");
	phone_0 = propOwner.getProperty("phone");
	fax_0 = propOwner.getProperty("fax");
	e_mail_0 = propOwner.getProperty("e_mail");
}
String surname_1 = "";
String phone_1 = "";
String fax_1 = "";
String e_mail_1 = "";
boolean readonly_1 = bean.isContactReadOnly(SiteBookletBean.CTYPE_MANAGER);
Properties propManager = bean.aContacts[SiteBookletBean.CTYPE_MANAGER];
if( propManager != null ) {
	surname_1 = propManager.getProperty("surname");
	phone_1 = propManager.getProperty("phone");
	fax_1 = propManager.getProperty("fax");
	e_mail_1 = propManager.getProperty("e_mail");
}
boolean readonly = readonly_0 || readonly_1;
String strReadOnly = readonly ? "readonly" : "";
%>
<input id="readonly" type="hidden" value="<%=readonly%>">

<table border="0">
	<tr>
		<th colspan="2"><h2><%=lang.getString("sitebooklet", "owner_details")%></h2>
	</tr>
	<tr>
		<td width="40%"><%=lang.getString("sitebooklet", "owner_company")%></td>
		<td width="60%"><input size="80" maxlength="200" name="surname_0" value="<%=surname_0%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "address")%></td>
		<td><input size="80" maxlength="128" name="address_0" value="<%=address_0%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "phone")%></td>
		<td><input size="80" maxlength="64" name="phone_0" value="<%=phone_0%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "fax")%></td>
		<td><input size="80" maxlength="64" name="fax_0" value="<%=fax_0%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "e_mail")%></td>
		<td><input size="80" maxlength="64" name="e_mail_0" value="<%=e_mail_0%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<th colspan="2">
			<br><br><h2><%=lang.getString("sitebooklet", "manager_details")%></h2>
			<br><h3><%=lang.getString("sitebooklet", "manager_diff")%></h3>
		</th>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "manager_company")%></td>
		<td><input size="80" maxlength="200" name="surname_1" value="<%=surname_1%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "phone")%></td>
		<td><input size="80" maxlength="64" name="phone_1" value="<%=phone_1%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "fax")%></td>
		<td><input size="80" maxlength="64" name="fax_1" value="<%=fax_1%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "e_mail")%></td>
		<td><input size="80" maxlength="64" name="e_mail_1" value="<%=e_mail_1%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
</table>