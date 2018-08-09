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
bean.loadSafetyDevices2();

String safety_valve_pos							= "";
String safety_valve_series						= "";
String safety_valve_pressure_calibration		= "";
String replacement_type							= "";
String replacement_series						= "";
String replacement_pressure_calibration			= "";
String replacement_ped_category					= "";
String replacement_expires						= "";
String[] aDate									= null;
boolean readonly								= false;
String cmd										= ut.remProperty("cmd");
if( cmd.isEmpty() )
	cmd = "mod";
String idrecord									= ut.remProperty("idrecord");
Properties[] aRecords							= bean.aSafetyDevices2;  
if( cmd.equals("mod") && aRecords != null && aRecords.length > 0 ) {
	if( idrecord.isEmpty() )
		idrecord = aRecords[aRecords.length - 1].getProperty("idrecord");
	for(int i = 0; i < aRecords.length; i++) {
		if( aRecords[i].getProperty("idrecord").equals(idrecord) ) {
			Properties prop						= aRecords[i];
			safety_valve_pos					= prop.getProperty("safety_valve_pos");
			safety_valve_series					= prop.getProperty("safety_valve_series");
			safety_valve_pressure_calibration	= prop.getProperty("safety_valve_pressure_calibration");
			replacement_type					= prop.getProperty("replacement_type");
			replacement_series					= prop.getProperty("replacement_series");
			replacement_pressure_calibration	= prop.getProperty("replacement_pressure_calibration");
			replacement_ped_category			= prop.getProperty("replacement_ped_category");
			replacement_expires					= prop.getProperty("replacement_expires");
			aDate								= prop.getProperty("replacement_date").split("/");
			readonly							= bean.isSafetyDevices2ReadOnly(Integer.parseInt(prop.getProperty("idrecord"))); 
			break;
		}
	}	
}

// records table
StringBuffer sbTable = new StringBuffer();
if( aRecords != null ) {
	sbTable.append("<br><table id='tabRecords' align='center'><tr><td align='center' style='cursor:arrow;'>#</td>");
	for(int i = 0; i < aRecords.length; i++) {
		String strIdRecord = aRecords[i].getProperty("idrecord"); 
		sbTable.append("<td align='center' style='cursor:hand;'"
			+ " class='" + (strIdRecord.equals(idrecord) ? "sbselected" : "sbnotselected")
			+ "' onClick='onSelectRecord(this, "
			+ strIdRecord
			+ ")' onDblClick='onModify()'>" + (i + 1) + "</td>");
	}
	sbTable.append("</tr></table>");
}

//expires selection
StringBuffer sbSelExpires = new StringBuffer();
for(int i = 1; i <= 10; i++)
	sbSelExpires.append("<option value=\"" + i + "\" " + (replacement_expires.equals("" + i) ? "selected" : "") + ">" + i + "</option>\n");

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

String strReadOnly = readonly ? "readonly" : "";
String strDisabled = readonly ? "disabled" : "";
%>
<input id="readonly" type="hidden" value="<%=readonly%>">

<input type="hidden" id="cmd" name="cmd" value="<%=cmd%>">
<input type="hidden" id="idrecord" name="idrecord" value="<%=idrecord%>">
<table>
	<tr>
		<th colspan="2"><h1><%=lang.getString("sitebooklet", "safety_devices")%></h1>
		<%=sbTable.toString()%>
		<br><h2><%=lang.getString("sitebooklet", "pressure_relief")%><br><%=lang.getString("sitebooklet", "safety_valve")%></h2>
		</th>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "pos_on_plant")%></td>
		<td><input size="64" maxlength="64" name="safety_valve_pos" value="<%=safety_valve_pos%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "type_brand_mod")%></td>
		<td><input size="64" maxlength="128" name="safety_valve_series" value="<%=safety_valve_series%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "pressure_calibration_ped")%></td>
		<td><input size="64" maxlength="32" name="safety_valve_pressure_calibration" value="<%=safety_valve_pressure_calibration%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr height="4px"><td colspan="2">&nbsp;</td>
	<tr>
		<th colspan="2"><h2><%=lang.getString("sitebooklet", "replacement")%></h2></th>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "sefety_device_type")%></td>
		<td><input size="64" maxlength="64" name="replacement_type" value="<%=replacement_type%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "brand_mod_series")%></td>
		<td><input size="64" maxlength="128" name="replacement_series" value="<%=replacement_series%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "pressure_calibration")%></td>
		<td><input size="64" maxlength="32" name="replacement_pressure_calibration" value="<%=replacement_pressure_calibration%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "ped_category")%></td>
		<td><input size="64" maxlength="64" name="replacement_ped_category" value="<%=replacement_ped_category%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "replacement_expires")%></td>
		<td>
			<select name="replacement_expires" <%=strDisabled%>>
			<%=sbSelExpires.toString()%>
			</select>
		</td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "replacement_date")%></td>
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
