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
bean.loadSecondaryFluid();
String[] aDate = bean.secondary_fluid_date.split("/");
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

boolean readonly = bean.isSecondaryFluidReadOnly();
String strReadOnly = readonly ? "readonly" : "";
String strDisabled = readonly ? "disabled" : "";
%>
<input id="readonly" type="hidden" value="<%=readonly%>">

<table border="0">
	<tr>
		<th colspan="3"><h2><%=lang.getString("sitebooklet", "secondary_fluid_used")%></h2>
	</tr>
	<tr height="4px"><td colspan="3">&nbsp;</td>
	<tr>
		<td colspan="2" width="50%"><input type="checkbox" name="cb_water_ethyleneglycol" <%=bean.checkSecondaryFluidTypeFlag(SiteBookletBean.SFT_water_ethyleneglycol)%> onClick="onClickSecondaryFluid(this)" <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "W_EG")%></td>
		<td width="50%"><input size="50" maxlength="32" name="water_ethyleneglycol" value="<%=bean.water_ethyleneglycol%>" class="<%=classInput%>" <%=strReadOnly%>>&nbsp;(%)</td>
	</tr>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_water_propyleneglycol" <%=bean.checkSecondaryFluidTypeFlag(SiteBookletBean.SFT_water_propyleneglycol)%> onClick="onClickSecondaryFluid(this)" <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "W_PG")%></td>
		<td><input size="50" maxlength="32" name="water_propyleneglycol" value="<%=bean.water_propyleneglycol%>" class="<%=classInput%>" <%=strReadOnly%>>&nbsp;(%)</td>
	</tr>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_organic_mixtures" <%=bean.checkSecondaryFluidTypeFlag(SiteBookletBean.SFT_organic_mixtures)%> onClick="onClickSecondaryFluid(this)" <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "organic_mixtures")%></td>
		<td><input size="64" maxlength="64" name="organic_mixtures" value="<%=bean.organic_mixtures%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><%=lang.getString("sitebooklet", "brand")%></td>
		<td><input size="64" maxlength="64" name="organic_mixtures_brand" value="<%=bean.organic_mixtures_brand%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td>&nbsp;</td>	
		<td><%=lang.getString("sitebooklet", "type")%></td>
		<td><input size="64" maxlength="64" name="organic_mixtures_type" value="<%=bean.organic_mixtures_type%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td colspan="2"><input type="checkbox" name="cb_refrigerant_type" <%=bean.checkSecondaryFluidTypeFlag(SiteBookletBean.SFT_refrigerant_type)%> onClick="onClickSecondaryFluid(this)" <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "refrigerant_type")%>:</td>
		<td><input size="64" maxlength="64" name="refrigerant_type" value="<%=bean.refrigerant_type%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td colspan="3"><input type="checkbox" name="cb_secondary_fluid_other" <%=bean.checkSecondaryFluidTypeFlag(SiteBookletBean.SFT_secondary_fluid_other)%> onClick="onClickSecondaryFluid(this)" <%=strDisabled%>>&nbsp;<%=lang.getString("sitebooklet", "other_specify")%></td>
	</tr>
	<tr>
		<td colspan="3" align="center">
			<textarea rows="6" cols="80" name="secondary_fluid_other" class="<%=classInput%>" <%=strReadOnly%>><%=bean.secondary_fluid_other%></textarea>
		</td>
	</tr>
	<tr>
		<th colspan="3"><br><br><h2><%=lang.getString("sitebooklet", "first_charge_ref")%></h2>
	</tr>
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "kg")%></td>
		<td><input size="64" maxlength="32" name="kg" value="<%=bean.secondary_fluid_kg%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "date")%></td>
		<td>
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
</table>
