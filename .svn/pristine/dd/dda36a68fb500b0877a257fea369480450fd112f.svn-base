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
String address_2 = "";
String phone_2 = "";
String fax_2 = "";
String e_mail_2 = "";
boolean readonly_2 = bean.isContactReadOnly(SiteBookletBean.CTYPE_PLANT);
Properties propPlant = bean.aContacts[SiteBookletBean.CTYPE_PLANT];
if( propPlant != null ) {
	address_2 = propPlant.getProperty("address");
	phone_2 = propPlant.getProperty("phone");
	fax_2 = propPlant.getProperty("fax");
	e_mail_2 = propPlant.getProperty("e_mail");
}
String surname_3 = "";
String phone_3 = "";
String fax_3 = "";
String e_mail_3 = "";
String name_3 = "";
boolean readonly_3 = bean.isContactReadOnly(SiteBookletBean.CTYPE_HEAD_OF_PLANT);
Properties propHeadPlant = bean.aContacts[SiteBookletBean.CTYPE_HEAD_OF_PLANT];
if( propHeadPlant != null ) {
	surname_3 = propHeadPlant.getProperty("surname");
	phone_3 = propHeadPlant.getProperty("phone");
	fax_3 = propHeadPlant.getProperty("fax");
	e_mail_3 = propHeadPlant.getProperty("e_mail");
	name_3 = propHeadPlant.getProperty("name");
}
boolean readonly = readonly_2 || readonly_3;
String strReadOnly = readonly ? "readonly" : "";
%>
<input id="readonly" type="hidden" value="<%=readonly%>">

<table border="0">
	<tr>
		<th colspan="2"><h2><%=lang.getString("sitebooklet", "plant_details")%></h2>
	</tr>
	<tr>
		<td width="40%"><%=lang.getString("sitebooklet", "address")%></td>
		<td width="60%"><input size="80" maxlength="128" name="address_2" value="<%=address_2%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "phone")%></td>
		<td><input size="80" maxlength="64" name="phone_2" value="<%=phone_2%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "fax")%></td>
		<td><input size="80" maxlength="64" name="fax_2" value="<%=fax_2%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "e_mail")%></td>
		<td><input size="80" maxlength="64" name="e_mail_2" value="<%=e_mail_2%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<th colspan="2">
			<br><br><h2><%=lang.getString("sitebooklet", "head_of_plant")%></h2>
		</th>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "surname")%></td>
		<td><input size="80" maxlength="200" name="surname_3" value="<%=surname_3%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "phone")%></td>
		<td><input size="80" maxlength="64" name="phone_3" value="<%=phone_3%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "fax")%></td>
		<td><input size="80" maxlength="64" name="fax_3" value="<%=fax_3%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "e_mail")%></td>
		<td><input size="80" maxlength="64" name="e_mail_3" value="<%=e_mail_3%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "name")%></td>
		<td><input size="80" maxlength="64" name="name_3" value="<%=name_3%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
</table>