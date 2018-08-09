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
bean.loadSiteType();
boolean readonly = bean.isSiteTypeReadOnly();
String strReadOnly = readonly ? "readonly" : "";
String strDisabled = readonly ? "disabled" : "";
%>
<input id="readonly" type="hidden" value="<%=readonly%>">

<table border="0">
	<tr>
		<th colspan="2"><h2><%=lang.getString("sitebooklet", "site_type")%></h2>
	</tr>
	<tr height="4px"><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><input type="checkbox" name="cb_direct_expansion" <%=bean.checkSiteTypeFlag(SiteBookletBean.ST_direct_expandion)%> <%=strDisabled%> onClick="onClickSiteType(this)">&nbsp;<%=lang.getString("sitebooklet", "direct_expansion")%></td>
		<td width="50%">&nbsp;</td>
	</tr>
	<tr height="4px"><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_secondary_fluid" <%=bean.checkSiteTypeFlag(SiteBookletBean.ST_secondary_fluid)%> <%=strDisabled%> onClick="onClickSiteType(this)">&nbsp;<%=lang.getString("sitebooklet", "secondary_fluid")%></td>
		<td>&nbsp;</td>
	</tr>
	<tr height="4px"><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_site_type_other" <%=bean.checkSiteTypeFlag(SiteBookletBean.ST_other)%> <%=strDisabled%> onClick="onClickSiteType(this);onClickSiteTypeOther(this.checked)">&nbsp;<%=lang.getString("sitebooklet", "other")%></td>
		<td><input size="32" maxlength="32" id="site_type_other" name="site_type_other" value="<%=bean.site_type_other%>" class="<%=classInput%>" <%=strReadOnly%> <%=bean.checkSiteTypeFlag(SiteBookletBean.ST_other).isEmpty() ? "disabled" : ""%>></td>
	</tr>
	<tr>
		<th colspan="3"><br><br><h2><%=lang.getString("sitebooklet", "refrigerant_type")%></h2>
	</tr>
	<tr height="4px"><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_ref_type_1" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_1)%> <%=strDisabled%> onClick="onClickRefType(this)">&nbsp;<%=lang.getString("sitebooklet", "ref_type_1")%></td>
		<td><input type="checkbox" name="cb_ref_type_2" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_2)%> <%=strDisabled%> onClick="onClickRefType(this)">&nbsp;<%=lang.getString("sitebooklet", "ref_type_2")%></td>
	</tr>
	<tr height="4px"><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_ref_type_3" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_3)%> <%=strDisabled%> onClick="onClickRefType(this)">&nbsp;<%=lang.getString("sitebooklet", "ref_type_3")%></td>
		<td><input type="checkbox" name="cb_ref_type_4" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_4)%> <%=strDisabled%> onClick="onClickRefType(this)">&nbsp;<%=lang.getString("sitebooklet", "ref_type_4")%></td>
	</tr>
	<tr height="4px"><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_ref_type_5" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_5)%> <%=strDisabled%> onClick="onClickRefType(this)">&nbsp;<%=lang.getString("sitebooklet", "ref_type_5")%></td>
		<td><input type="checkbox" name="cb_ref_type_6" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_6)%> <%=strDisabled%> onClick="onClickRefType(this)">&nbsp;<%=lang.getString("sitebooklet", "ref_type_6")%></td>
	</tr>
	<tr height="4px"><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_ref_type_7" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_7)%> <%=strDisabled%> onClick="onClickRefType(this)">&nbsp;<%=lang.getString("sitebooklet", "ref_type_7")%></td>
		<td><input type="checkbox" name="cb_ref_type_8" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_8)%> <%=strDisabled%> onClick="onClickRefType(this)">&nbsp;<%=lang.getString("sitebooklet", "ref_type_8")%></td>
	</tr>
	<tr height="4px"><td colspan="2">&nbsp;</td>
	<tr>
		<td><input type="checkbox" name="cb_ref_type_0" <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_0)%> <%=strDisabled%> onClick="onClickRefType(this);onClickRefTypeOther(this.checked)">&nbsp;<%=lang.getString("sitebooklet", "ref_type_0")%></td>
		<td><input size="32" maxlength="32" id="ref_type_0" name="ref_type_0" value="<%=bean.ref_type_other%>" class="<%=classInput%>" <%=strReadOnly%> <%=bean.checkRefrigerantTypeFlag(SiteBookletBean.REFT_0).isEmpty() ? "disabled" : ""%>></td>
	</tr>
</table>
