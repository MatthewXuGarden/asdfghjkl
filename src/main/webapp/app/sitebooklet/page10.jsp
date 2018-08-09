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
bean.loadSiteReference();

boolean readonly = bean.isSiteReferenceReadOnly();
String strReadOnly = readonly ? "readonly" : "";
String strDisabled = readonly ? "disabled" : "";
%>
<input id="readonly" type="hidden" value="<%=readonly%>">

<table>
	<tr>
		<th colspan="3"><h1><%=lang.getString("sitebooklet", "site_reference")%></h1></th>
	</tr>
	<tr height="4px"><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2" width="50%"><%=lang.getString("sitebooklet", "type_model")%></td>
		<td width="50%"><input size="64" maxlength="64" name="type_model" value="<%=bean.type_model%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td colspan="2" width="50%"><%=lang.getString("sitebooklet", "reg_num")%></td>
		<td width="50%"><input size="64" maxlength="64" name="registration_number" value="<%=bean.registration_number%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td colspan="2" width="50%"><%=lang.getString("sitebooklet", "built_year")%></td>
		<td width="50%"><input size="64" maxlength="64" name="built_year" value="<%=bean.built_year%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr height="4px"><td colspan="3">&nbsp;</td>
	<tr>
		<th colspan="3"><h2><%=lang.getString("sitebooklet", "fluid_level")%></h2>
	</tr>
	<tr>
		<td colspan="3"><input type="checkbox" name="cb_level_sensors" onClick="onClickFluidLevel(this)" <%=bean.checkSiteReferenceFlag(SiteBookletBean.SREF_level_sensors)%> <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "level_sensors")%></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "brand_model")%></td>
		<td><input size="64" maxlength="64" name="brand_model" value="<%=bean.brand_model%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "min_max_val")%></td>
		<td><input size="28" maxlength="32" name="min_value" value="<%=bean.min_value%>" class="<%=classInput%>" <%=strReadOnly%>> /
		 <input size="28" maxlength="32" name="max_value" value="<%=bean.max_value%>" class="<%=classInput%>" <%=strReadOnly%>>
		</td>
	</tr>
	<tr height="4px"><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="3"><input type="checkbox" name="cb_level_indicators" onClick="onClickFluidLevel(this)" <%=bean.checkSiteReferenceFlag(SiteBookletBean.SREF_level_indicators)%> <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "level_indicators")%></td>
	</tr>
	<tr>
		<td width="5%">&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "level_estimation")%></td>
		<td><input size="64" maxlength="32" name="level_estimation" value="<%=bean.level_estimation%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "compared_to")%></td>
		<td><input size="64" maxlength="32" name="level_reference" value="<%=bean.level_reference%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
</table>
