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
bean.loadSiteBooklet();
String header = bean.header.isEmpty() ? lang.getString("sitebooklet", "dpr_date") : bean.header;
%>

<table border="0">
	<tr>
		<th colspan="3"><h1><%=lang.getString("sitebooklet", "sitebooklet")%></h1>
		<br><input size="64" maxlength="128" name="header" value="<%=header%>" class="<%=classInput%>"></th>
	</tr>
	<tr>
		<td colspan="3" align="center">
			<textarea rows="6" cols="80" name="note" class="<%=classInput%>"><%=bean.note%></textarea>
		</td>
	</tr>
	<tr><td colspan="3">&nbsp;</td>
	<tr>
		<td width="50%" colspan="2"><input type="checkbox" name="cb_ref_low_temp_name" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_ref_low_temp_name)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_low_temp_name")%></td>
		<td width="50%"><input size="64" maxlength="64" name="ref_low_temp_name" value="<%=bean.ref_low_temp_name%>" class="<%=classInput%>"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="checkbox" name="cb_ref_low_temp_id" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_ref_low_temp_id)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_low_temp_id")%></td>
		<td><input size="64" maxlength="64" name="ref_low_temp_id" value="<%=bean.ref_low_temp_id%>" class="<%=classInput%>"></td>
	</tr>
	<tr height="4px"><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_ref_avg_temp_name" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_ref_avg_temp_name)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_avg_temp_name")%></td>
		<td><input size="64" maxlength="64" name="ref_avg_temp_name" value="<%=bean.ref_avg_temp_name%>" class="<%=classInput%>"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="checkbox" name="cb_ref_avg_temp_id" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_ref_avg_temp_id)%>>&nbsp;<%=lang.getString("sitebooklet", "ref_avg_temp_id")%></td>
		<td><input size="64" maxlength="64" name="ref_avg_temp_id" value="<%=bean.ref_avg_temp_id%>" class="<%=classInput%>"></td>
	</tr>
	<tr height="4px"><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_cond_supermarket_name" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_cond_supermarket_name)%>>&nbsp;<%=lang.getString("sitebooklet", "cond_supermarket_name")%></td>
		<td><input size="64" maxlength="64" name="cond_supermarket_name" value="<%=bean.cond_supermarket_name%>" class="<%=classInput%>"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="checkbox" name="cb_cond_supermarket_id" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_cond_supermarket_id)%>>&nbsp;<%=lang.getString("sitebooklet", "cond_supermarket_id")%></td>
		<td><input size="64" maxlength="64" name="cond_supermarket_id" value="<%=bean.cond_supermarket_id%>" class="<%=classInput%>"></td>
	</tr>
	<tr height="4px"><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_cond_units_name" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_cond_units_name)%>>&nbsp;<%=lang.getString("sitebooklet", "cond_units_name")%></td>
		<td><input size="64" maxlength="64" name="cond_units_name" value="<%=bean.cond_units_name%>" class="<%=classInput%>"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="checkbox" name="cb_cond_units_id" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_cond_units_id)%>>&nbsp;<%=lang.getString("sitebooklet", "cond_units_id")%></td>
		<td><input size="64" maxlength="64" name="cond_units_id" value="<%=bean.cond_units_id%>" class="<%=classInput%>"></td>
	</tr>
	<tr height="4px"><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_heat_pump_name" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_heat_pump_name)%>>&nbsp;<%=lang.getString("sitebooklet", "heat_pump_name")%></td>
		<td><input size="64" maxlength="64" name="heat_pump_name" value="<%=bean.heat_pump_name%>" class="<%=classInput%>"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="checkbox" name="cb_heat_pump_id" <%=bean.checkSiteBookletFlag(SiteBookletBean.FLAG_heat_pump_id)%>>&nbsp;<%=lang.getString("sitebooklet", "heat_pump_id")%></td>
		<td><input size="64" maxlength="64" name="heat_pump_id" value="<%=bean.heat_pump_id%>" class="<%=classInput%>"></td>
	</tr>
</table>
