<%@ page language="java"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.session.Transaction"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.presentation.bean.SiteBookletBean"
	import="java.util.GregorianCalendar"
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
bean.loadPreventionPlan();

String[] aDate = bean.prevention_plan_date.split("/");
if( aDate == null || aDate.length < 3 ) {
	GregorianCalendar gc = new GregorianCalendar();
	aDate = new String[3];
	aDate[0] = "" + gc.get(GregorianCalendar.YEAR);
	aDate[1] = "" + (gc.get(GregorianCalendar.MONTH) + 1);
	if( aDate[1].length() == 1 )
		aDate[1] = "0" + aDate[1];
	aDate[2] = "" + gc.get(GregorianCalendar.DAY_OF_MONTH);
	if( aDate[2].length() == 1 )
		aDate[2] = "0" + aDate[0];
}
//year selection
StringBuffer sbSelYear = new StringBuffer();
for(int i = 2000; i < 2100; i++)
	sbSelYear.append("<option value=\"" + i + "\" " + (aDate[0].equals("" + i) ? "selected" : "") + ">" + i + "</option>\n");

boolean readonly = bean.isPreventionPlanReadOnly();
String strReadOnly = readonly ? "readonly" : "";
String strDisabled = readonly ? "disabled" : "";
%>
<input id="readonly" type="hidden" value="<%=readonly%>">

<table class="sbtable">
	<tr>
		<td>&nbsp;</td>
		<th colspan="2"><h1><%=lang.getString("sitebooklet", "prevention_plan")%></h1>
		<br>
		<h2><%=lang.getString("sitebooklet", "preventive_measures")%></h2>
		</th>
		<td>&nbsp;</td>
	</tr>
	<tr height="4px"><td width="25%">&nbsp;</td><td width="25%">&nbsp;</td><td width="25%">&nbsp;</td><td width="25%">&nbsp;</td></tr>
	<tr>
		<td colspan="4"><%=lang.getString("sitebooklet", "first_charge_ref_ver")%></td>
	</tr>
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "date")%></td>
		<td colspan="2">
			<select name="date_day" <%=strDisabled%>>
				<option value="01" <%=aDate[2].equals("01") ? "selected" : ""%>>01</option>
				<option value="02" <%=aDate[2].equals("02") ? "selected" : ""%>>02</option>
				<option value="03" <%=aDate[2].equals("03") ? "selected" : ""%>>03</option>
				<option value="04" <%=aDate[2].equals("04") ? "selected" : ""%>>04</option>
				<option value="05" <%=aDate[2].equals("05") ? "selected" : ""%>>05</option>
				<option value="06" <%=aDate[2].equals("06") ? "selected" : ""%>>06</option>
				<option value="07" <%=aDate[2].equals("07") ? "selected" : ""%>>07</option>
				<option value="08" <%=aDate[2].equals("08") ? "selected" : ""%>>08</option>
				<option value="09" <%=aDate[2].equals("09") ? "selected" : ""%>>09</option>
				<option value="10" <%=aDate[2].equals("10") ? "selected" : ""%>>10</option>
				<option value="11" <%=aDate[2].equals("11") ? "selected" : ""%>>11</option>
				<option value="12" <%=aDate[2].equals("12") ? "selected" : ""%>>12</option>
				<option value="13" <%=aDate[2].equals("13") ? "selected" : ""%>>13</option>
				<option value="14" <%=aDate[2].equals("14") ? "selected" : ""%>>14</option>
				<option value="15" <%=aDate[2].equals("15") ? "selected" : ""%>>15</option>
				<option value="16" <%=aDate[2].equals("16") ? "selected" : ""%>>16</option>
				<option value="17" <%=aDate[2].equals("17") ? "selected" : ""%>>17</option>
				<option value="18" <%=aDate[2].equals("18") ? "selected" : ""%>>18</option>
				<option value="19" <%=aDate[2].equals("19") ? "selected" : ""%>>19</option>
				<option value="20" <%=aDate[2].equals("20") ? "selected" : ""%>>20</option>
				<option value="21" <%=aDate[2].equals("21") ? "selected" : ""%>>21</option>
				<option value="22" <%=aDate[2].equals("22") ? "selected" : ""%>>22</option>
				<option value="23" <%=aDate[2].equals("23") ? "selected" : ""%>>23</option>
				<option value="24" <%=aDate[2].equals("24") ? "selected" : ""%>>24</option>
				<option value="25" <%=aDate[2].equals("25") ? "selected" : ""%>>25</option>
				<option value="26" <%=aDate[2].equals("26") ? "selected" : ""%>>26</option>
				<option value="27" <%=aDate[2].equals("27") ? "selected" : ""%>>27</option>
				<option value="28" <%=aDate[2].equals("28") ? "selected" : ""%>>28</option>
				<option value="29" <%=aDate[2].equals("29") ? "selected" : ""%>>29</option>
				<option value="30" <%=aDate[2].equals("30") ? "selected" : ""%>>30</option>
				<option value="31" <%=aDate[2].equals("31") ? "selected" : ""%>>31</option>
			</select>
			/
			<select name="date_month" <%=strDisabled%>>
				<option value="01" <%=aDate[1].equals("01") ? "selected" : ""%>><%=lang.getString("cal","january")%></option>
				<option value="02" <%=aDate[1].equals("02") ? "selected" : ""%>><%=lang.getString("cal","february")%></option>
				<option value="03" <%=aDate[1].equals("03") ? "selected" : ""%>><%=lang.getString("cal","march")%></option>
				<option value="04" <%=aDate[1].equals("04") ? "selected" : ""%>><%=lang.getString("cal","april")%></option>
				<option value="05" <%=aDate[1].equals("05") ? "selected" : ""%>><%=lang.getString("cal","may")%></option>
				<option value="06" <%=aDate[1].equals("06") ? "selected" : ""%>><%=lang.getString("cal","june")%></option>
				<option value="07" <%=aDate[1].equals("07") ? "selected" : ""%>><%=lang.getString("cal","july")%></option>
				<option value="08" <%=aDate[1].equals("08") ? "selected" : ""%>><%=lang.getString("cal","august")%></option>
				<option value="09" <%=aDate[1].equals("09") ? "selected" : ""%>><%=lang.getString("cal","september")%></option>
				<option value="10" <%=aDate[1].equals("10") ? "selected" : ""%>><%=lang.getString("cal","october")%></option>
				<option value="11" <%=aDate[1].equals("11") ? "selected" : ""%>><%=lang.getString("cal","november")%></option>
				<option value="12" <%=aDate[1].equals("12") ? "selected" : ""%>><%=lang.getString("cal","december")%></option>
			</select>
			/
			<select name="date_year" <%=strDisabled%>><%=sbSelYear.toString()%></select>			
		</td>		
	</tr>
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "quantity")%></td>
		<td colspan="2"><input size="64" maxlength="32" name="prevention_plan_quantity" value="<%=bean.prevention_plan_quantity%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td colspan="4"><%=lang.getString("sitebooklet", "inspections_intervals")%></td>
	</tr>
<%if( bean.isBooklet() ) {%>	
	<tr valign="top">
		<td><%=bean.pp_label0%></td>
		<td align="center">
			<input type="checkbox" name="cb_interval_quarterly" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_interval_quarterly)%> onClick="onClickInspectionInterval(this)" <%=strDisabled%>>
			&nbsp;<%=bean.pp_label1_1%>
			<br><%=bean.pp_label1_2%>
			<br><%=bean.pp_label1_3%>
		</td>
		<td align="center">
			<input type="checkbox" name="cb_interval_semiannual" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_interval_semiannual)%> onClick="onClickInspectionInterval(this)" <%=strDisabled%>>
			&nbsp;<%=bean.pp_label2_1%>
			<br><%=bean.pp_label2_2%>
			<br><%=bean.pp_label2_3%>
		</td>
		<td align="center">
			<input type="checkbox" name="cb_interval_annual" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_interval_annual)%> onClick="onClickInspectionInterval(this)" <%=strDisabled%>>
			&nbsp;<%=bean.pp_label3_1%>
			<br><%=bean.pp_label3_2%>
			<br><%=bean.pp_label3_3%>
		</td>
	</tr>
<%} else {%>
	<tr valign="top">
		<td>
			<input size="50" maxlength="128" name="pp_label0" value="<%=lang.getString("sitebooklet", "reg_eu_842")%>" class="<%=classInput%>" <%=strReadOnly%>>
		</td>
		<td align="center">
			<input type="checkbox" name="cb_interval_quarterly" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_interval_quarterly)%> onClick="onClickInspectionInterval(this)" <%=strDisabled%>>
			&nbsp;<input size="20" maxlength="64" name="pp_label1_1" value="<%=lang.getString("sitebooklet", "quarterly")%>" class="<%=classInput%>" <%=strReadOnly%>>
			<br><input size="24" maxlength="64" name="pp_label1_2" value="<%=lang.getString("sitebooklet", "over_300_kg")%>" class="<%=classInput%>" <%=strReadOnly%>>
			<br><input size="24" maxlength="64" name="pp_label1_3" value="" class="<%=classInput%>" <%=strReadOnly%>>
		</td>
		<td align="center">
			<input type="checkbox" name="cb_interval_semiannual" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_interval_semiannual)%> onClick="onClickInspectionInterval(this)" <%=strDisabled%>>
			&nbsp;<input size="20" maxlength="64" name="pp_label2_1" value="<%=lang.getString("sitebooklet", "semiannual")%>" class="<%=classInput%>" <%=strReadOnly%>>
			<br><input size="24" maxlength="64" name="pp_label2_2" value="<%=lang.getString("sitebooklet", "between_30_300_kg")%>" class="<%=classInput%>" <%=strReadOnly%>>
			<br><input size="24" maxlength="64" name="pp_label2_3" value="<%=lang.getString("sitebooklet", "over_100_kg")%>" class="<%=classInput%>" <%=strReadOnly%>>
		</td>
		<td align="center">
			<input type="checkbox" name="cb_interval_annual" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_interval_annual)%> onClick="onClickInspectionInterval(this)" <%=strDisabled%>>
			&nbsp;<input size="20" maxlength="64" name="pp_label3_1" value="<%=lang.getString("sitebooklet", "annual")%>" class="<%=classInput%>" <%=strReadOnly%>>
			<br><input size="24" maxlength="64" name="pp_label3_2" value="<%=lang.getString("sitebooklet", "between_3_30_kg")%>" class="<%=classInput%>" <%=strReadOnly%>>
			<br><input size="24" maxlength="64" name="pp_label3_3" value="<%=lang.getString("sitebooklet", "between_3_100_kg")%>" class="<%=classInput%>" <%=strReadOnly%>>
		</td>
	</tr>
<%}%>
	<tr height="4px"><td colspan="4">&nbsp;</td></tr>
	<tr>
		<td>&nbsp;</td>
		<th colspan="2">
		<h2><%=lang.getString("sitebooklet", "control_actions")%></h2>
		</th>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td colspan="4"><input type="checkbox" name="cb_check_ref_charge" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_check_ref_charge)%> <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "check_ref_charge")%></td>
	</tr>
	<tr>
		<td colspan="4"><input type="checkbox" name="cb_check_loss_central" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_check_loss_central)%> <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "check_loss_central")%></td>
	</tr>
	<tr>
		<td colspan="4"><input type="checkbox" name="cb_check_critical_points" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_check_critical_points)%> onClick="onClickCriticalPoints(this.checked)" <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "check_critical_points")%></td>
	</tr>
	<tr>
		<td colspan="2" style="padding-left:20%;"><input type="checkbox" name="cb_cp_electricity_supply" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_cp_electricity_supply)%> <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "electricity_supply")%></td>
		<td><input type="checkbox" name="cb_cp_capacitors" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_cp_capacitors)%> <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "capacitors")%></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2" style="padding-left:20%;"><input type="checkbox" name="cb_cp_hi_pressure_pipe" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_cp_hi_pressure_pipe)%> <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "hi_pressure_pipe")%></td>
		<td><input type="checkbox" name="cb_cp_evaporators" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_cp_evaporators)%> <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "evaporators")%></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td colspan="2" style="padding-left:20%;"><input type="checkbox" name="cb_cp_lo_pressure_pipe" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_cp_lo_pressure_pipe)%> <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "lo_pressure_pipe")%></td>
		<td><input type="checkbox" name="cb_cp_filters_valves" <%=bean.checkPreventionPlanFlag(SiteBookletBean.PP_cp_filters_valves)%> <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "filters_valves")%></td>
		<td>&nbsp;</td>
	</tr>
</table>
