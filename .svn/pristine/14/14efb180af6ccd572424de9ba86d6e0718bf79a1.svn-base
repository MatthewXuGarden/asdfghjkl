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
bean.loadRefrigerantRecovery();

String recovered_ref_type						= "";
String recovered_qantity						= "";
String[] aDate1									= null;
String replaced_ref_type						= "";
String replaced_qantity							= "";
String[] aDate2									= null;
String recovery_equipment						= "";
String iso_equipment							= lang.getString("sitebooklet", "iso_11650");
String operator_stamp							= "";
String user_name								= sessionUser.getUserName();
String[] aDate									= null;
boolean readonly								= false;
String cmd										= ut.remProperty("cmd");
if( cmd.isEmpty() )
	cmd = "mod";
String idrecord									= ut.remProperty("idrecord");
Properties[] aRecords 							= bean.aRefrigerantRecovery; 
if( cmd.equals("mod") && aRecords != null && aRecords.length > 0 ) {
	if( idrecord.isEmpty() )
		idrecord = aRecords[aRecords.length - 1].getProperty("idrecord");
	for(int i = 0; i < aRecords.length; i++) {
		if( aRecords[i].getProperty("idrecord").equals(idrecord) ) {
			Properties prop						= aRecords[i];
			recovered_ref_type					= prop.getProperty("recovered_ref_type");
			recovered_qantity					= prop.getProperty("recovered_qantity");
			aDate1								= prop.getProperty("recovery_date").split("/");
			replaced_ref_type					= prop.getProperty("replaced_ref_type");
			replaced_qantity					= prop.getProperty("replaced_qantity");
			aDate2								= prop.getProperty("intervention_date").split("/");
			recovery_equipment					= prop.getProperty("recovery_equipment");
			iso_equipment						= prop.getProperty("iso_equipment");
			operator_stamp						= prop.getProperty("operator_stamp");
			user_name							= prop.getProperty("user_name");
			aDate								= prop.getProperty("date").split("/");
			readonly							= bean.isRefrigerantRecoveryReadOnly(Integer.parseInt(prop.getProperty("idrecord"))); 
			break;
		}
	}
}

//records table
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

//dates
GregorianCalendar gc = new GregorianCalendar();
if( aDate == null || aDate.length < 3 ) {
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
if( aDate1 == null || aDate1.length < 3 ) {
	aDate1 = new String[3];
	aDate1[0] = "" + gc.get(GregorianCalendar.YEAR);
	aDate1[1] = "" + (gc.get(GregorianCalendar.MONTH) + 1);
	if( aDate1[1].length() == 1 )
		aDate1[1] = "0" + aDate1[1];
	aDate1[2] = "" + gc.get(GregorianCalendar.DAY_OF_MONTH);
	if( aDate1[2].length() == 1 )
		aDate1[2] = "0" + aDate1[0];
}
//year selection
StringBuffer sbSelYear1 = new StringBuffer();
for(int i = 2000; i < 2100; i++)
	sbSelYear1.append("<option value=\"" + i + "\" " + (aDate1[0].equals("" + i) ? "selected" : "") + ">" + i + "</option>\n");
if( aDate2 == null || aDate2.length < 3 ) {
	aDate2 = new String[3];
	aDate2[0] = "" + gc.get(GregorianCalendar.YEAR);
	aDate2[1] = "" + (gc.get(GregorianCalendar.MONTH) + 1);
	if( aDate2[1].length() == 1 )
		aDate2[1] = "0" + aDate2[1];
	aDate2[2] = "" + gc.get(GregorianCalendar.DAY_OF_MONTH);
	if( aDate2[2].length() == 1 )
		aDate2[2] = "0" + aDate2[0];
}
//year selection
StringBuffer sbSelYear2 = new StringBuffer();
for(int i = 2000; i < 2100; i++)
	sbSelYear2.append("<option value=\"" + i + "\" " + (aDate2[0].equals("" + i) ? "selected" : "") + ">" + i + "</option>\n");

String strReadOnly = readonly ? "readonly" : "";
String strDisabled = readonly ? "disabled" : "";
%>
<input id="readonly" type="hidden" value="<%=readonly%>">

<input type="hidden" id="cmd" name="cmd" value="<%=cmd%>">
<input type="hidden" id="idrecord" name="idrecord" value="<%=idrecord%>">
<table>
	<tr>
		<th colspan="3"><h1><%=lang.getString("sitebooklet", "ref_recovery")%></h1>
		<%=sbTable.toString()%>
	</tr>
	<tr height="4px"><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "ref_type_used")%></td>
		<td width="50%"><input size="64" maxlength="128" name="recovered_ref_type" value="<%=recovered_ref_type%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "quantity")%></td>
		<td><input size="64" maxlength="32" name="recovered_qantity" value="<%=recovered_qantity%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "recovery_date")%></td>
		<td>
			<select name="date1_day" <%=strDisabled%>>
				<option value="01" <%=aDate1[2].equals("01") ? "selected" : ""%>>01</option>
				<option value="02" <%=aDate1[2].equals("02") ? "selected" : ""%>>02</option>
				<option value="03" <%=aDate1[2].equals("03") ? "selected" : ""%>>03</option>
				<option value="04" <%=aDate1[2].equals("04") ? "selected" : ""%>>04</option>
				<option value="05" <%=aDate1[2].equals("05") ? "selected" : ""%>>05</option>
				<option value="06" <%=aDate1[2].equals("06") ? "selected" : ""%>>06</option>
				<option value="07" <%=aDate1[2].equals("07") ? "selected" : ""%>>07</option>
				<option value="08" <%=aDate1[2].equals("08") ? "selected" : ""%>>08</option>
				<option value="09" <%=aDate1[2].equals("09") ? "selected" : ""%>>09</option>
				<option value="10" <%=aDate1[2].equals("10") ? "selected" : ""%>>10</option>
				<option value="11" <%=aDate1[2].equals("11") ? "selected" : ""%>>11</option>
				<option value="12" <%=aDate1[2].equals("12") ? "selected" : ""%>>12</option>
				<option value="13" <%=aDate1[2].equals("13") ? "selected" : ""%>>13</option>
				<option value="14" <%=aDate1[2].equals("14") ? "selected" : ""%>>14</option>
				<option value="15" <%=aDate1[2].equals("15") ? "selected" : ""%>>15</option>
				<option value="16" <%=aDate1[2].equals("16") ? "selected" : ""%>>16</option>
				<option value="17" <%=aDate1[2].equals("17") ? "selected" : ""%>>17</option>
				<option value="18" <%=aDate1[2].equals("18") ? "selected" : ""%>>18</option>
				<option value="19" <%=aDate1[2].equals("19") ? "selected" : ""%>>19</option>
				<option value="20" <%=aDate1[2].equals("20") ? "selected" : ""%>>20</option>
				<option value="21" <%=aDate1[2].equals("21") ? "selected" : ""%>>21</option>
				<option value="22" <%=aDate1[2].equals("22") ? "selected" : ""%>>22</option>
				<option value="23" <%=aDate1[2].equals("23") ? "selected" : ""%>>23</option>
				<option value="24" <%=aDate1[2].equals("24") ? "selected" : ""%>>24</option>
				<option value="25" <%=aDate1[2].equals("25") ? "selected" : ""%>>25</option>
				<option value="26" <%=aDate1[2].equals("26") ? "selected" : ""%>>26</option>
				<option value="27" <%=aDate1[2].equals("27") ? "selected" : ""%>>27</option>
				<option value="28" <%=aDate1[2].equals("28") ? "selected" : ""%>>28</option>
				<option value="29" <%=aDate1[2].equals("29") ? "selected" : ""%>>29</option>
				<option value="30" <%=aDate1[2].equals("30") ? "selected" : ""%>>30</option>
				<option value="31" <%=aDate1[2].equals("31") ? "selected" : ""%>>31</option>
			</select>
			/
			<select name="date1_month" <%=strDisabled%>>
				<option value="01" <%=aDate1[1].equals("01") ? "selected" : ""%>><%=lang.getString("cal","january")%></option>
				<option value="02" <%=aDate1[1].equals("02") ? "selected" : ""%>><%=lang.getString("cal","february")%></option>
				<option value="03" <%=aDate1[1].equals("03") ? "selected" : ""%>><%=lang.getString("cal","march")%></option>
				<option value="04" <%=aDate1[1].equals("04") ? "selected" : ""%>><%=lang.getString("cal","april")%></option>
				<option value="05" <%=aDate1[1].equals("05") ? "selected" : ""%>><%=lang.getString("cal","may")%></option>
				<option value="06" <%=aDate1[1].equals("06") ? "selected" : ""%>><%=lang.getString("cal","june")%></option>
				<option value="07" <%=aDate1[1].equals("07") ? "selected" : ""%>><%=lang.getString("cal","july")%></option>
				<option value="08" <%=aDate1[1].equals("08") ? "selected" : ""%>><%=lang.getString("cal","august")%></option>
				<option value="09" <%=aDate1[1].equals("09") ? "selected" : ""%>><%=lang.getString("cal","september")%></option>
				<option value="10" <%=aDate1[1].equals("10") ? "selected" : ""%>><%=lang.getString("cal","october")%></option>
				<option value="11" <%=aDate1[1].equals("11") ? "selected" : ""%>><%=lang.getString("cal","november")%></option>
				<option value="12" <%=aDate1[1].equals("12") ? "selected" : ""%>><%=lang.getString("cal","december")%></option>
			</select>
			/
			<select name="date1_year" <%=strDisabled%>><%=sbSelYear1.toString()%></select>			
		</td>		
	</tr>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "replaced_ref_type")%></td>
		<td width="50%"><input size="64" maxlength="128" name="replaced_ref_type" value="<%=replaced_ref_type%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "replaced_ref_quantity")%></td>
		<td><input size="64" maxlength="32" name="replaced_qantity" value="<%=replaced_qantity%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td><%=lang.getString("sitebooklet", "intervention_date")%></td>
		<td>
			<select name="date2_day" <%=strDisabled%>>
				<option value="01" <%=aDate2[2].equals("01") ? "selected" : ""%>>01</option>
				<option value="02" <%=aDate2[2].equals("02") ? "selected" : ""%>>02</option>
				<option value="03" <%=aDate2[2].equals("03") ? "selected" : ""%>>03</option>
				<option value="04" <%=aDate2[2].equals("04") ? "selected" : ""%>>04</option>
				<option value="05" <%=aDate2[2].equals("05") ? "selected" : ""%>>05</option>
				<option value="06" <%=aDate2[2].equals("06") ? "selected" : ""%>>06</option>
				<option value="07" <%=aDate2[2].equals("07") ? "selected" : ""%>>07</option>
				<option value="08" <%=aDate2[2].equals("08") ? "selected" : ""%>>08</option>
				<option value="09" <%=aDate2[2].equals("09") ? "selected" : ""%>>09</option>
				<option value="10" <%=aDate2[2].equals("10") ? "selected" : ""%>>10</option>
				<option value="11" <%=aDate2[2].equals("11") ? "selected" : ""%>>11</option>
				<option value="12" <%=aDate2[2].equals("12") ? "selected" : ""%>>12</option>
				<option value="13" <%=aDate2[2].equals("13") ? "selected" : ""%>>13</option>
				<option value="14" <%=aDate2[2].equals("14") ? "selected" : ""%>>14</option>
				<option value="15" <%=aDate2[2].equals("15") ? "selected" : ""%>>15</option>
				<option value="16" <%=aDate2[2].equals("16") ? "selected" : ""%>>16</option>
				<option value="17" <%=aDate2[2].equals("17") ? "selected" : ""%>>17</option>
				<option value="18" <%=aDate2[2].equals("18") ? "selected" : ""%>>18</option>
				<option value="19" <%=aDate2[2].equals("19") ? "selected" : ""%>>19</option>
				<option value="20" <%=aDate2[2].equals("20") ? "selected" : ""%>>20</option>
				<option value="21" <%=aDate2[2].equals("21") ? "selected" : ""%>>21</option>
				<option value="22" <%=aDate2[2].equals("22") ? "selected" : ""%>>22</option>
				<option value="23" <%=aDate2[2].equals("23") ? "selected" : ""%>>23</option>
				<option value="24" <%=aDate2[2].equals("24") ? "selected" : ""%>>24</option>
				<option value="25" <%=aDate2[2].equals("25") ? "selected" : ""%>>25</option>
				<option value="26" <%=aDate2[2].equals("26") ? "selected" : ""%>>26</option>
				<option value="27" <%=aDate2[2].equals("27") ? "selected" : ""%>>27</option>
				<option value="28" <%=aDate2[2].equals("28") ? "selected" : ""%>>28</option>
				<option value="29" <%=aDate2[2].equals("29") ? "selected" : ""%>>29</option>
				<option value="30" <%=aDate2[2].equals("30") ? "selected" : ""%>>30</option>
				<option value="31" <%=aDate2[2].equals("31") ? "selected" : ""%>>31</option>
			</select>
			/
			<select name="date2_month" <%=strDisabled%>>
				<option value="01" <%=aDate2[1].equals("01") ? "selected" : ""%>><%=lang.getString("cal","january")%></option>
				<option value="02" <%=aDate2[1].equals("02") ? "selected" : ""%>><%=lang.getString("cal","february")%></option>
				<option value="03" <%=aDate2[1].equals("03") ? "selected" : ""%>><%=lang.getString("cal","march")%></option>
				<option value="04" <%=aDate2[1].equals("04") ? "selected" : ""%>><%=lang.getString("cal","april")%></option>
				<option value="05" <%=aDate2[1].equals("05") ? "selected" : ""%>><%=lang.getString("cal","may")%></option>
				<option value="06" <%=aDate2[1].equals("06") ? "selected" : ""%>><%=lang.getString("cal","june")%></option>
				<option value="07" <%=aDate2[1].equals("07") ? "selected" : ""%>><%=lang.getString("cal","july")%></option>
				<option value="08" <%=aDate2[1].equals("08") ? "selected" : ""%>><%=lang.getString("cal","august")%></option>
				<option value="09" <%=aDate2[1].equals("09") ? "selected" : ""%>><%=lang.getString("cal","september")%></option>
				<option value="10" <%=aDate2[1].equals("10") ? "selected" : ""%>><%=lang.getString("cal","october")%></option>
				<option value="11" <%=aDate2[1].equals("11") ? "selected" : ""%>><%=lang.getString("cal","november")%></option>
				<option value="12" <%=aDate2[1].equals("12") ? "selected" : ""%>><%=lang.getString("cal","december")%></option>
			</select>
			/
			<select name="date2_year" <%=strDisabled%>><%=sbSelYear2.toString()%></select>			
		</td>		
	</tr>
	<tr>
		<td colspan="2"><%=lang.getString("sitebooklet", "recovery_equipment")%></td>
	</tr>
	<tr>
		<td><input size="64" maxlength="64" name="iso_equipment" value="<%=iso_equipment%>" class="<%=classInput%>" <%=strReadOnly%>></td>
		<td><input size="64" maxlength="128" name="recovery_equipment" value="<%=recovery_equipment%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr height="4px"><td colspan="2">&nbsp;</td>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "company_name")%></td>
		<td width="50%"><input size="64" maxlength="128" name="operator_stamp" value="<%=operator_stamp%>" class="<%=classInput%>" <%=strReadOnly%>></td>
	</tr>
	<tr>
		<td width="50%"><%=lang.getString("sitebooklet", "user_name")%></td>
		<td width="50%"><input size="64" maxlength="128" name="user_name" value="<%=user_name%>" class="standardTxt" readonly></td>
	</tr>
	<tr height="4px"><td colspan="2">&nbsp;</td>
	<tr>
		<td><%=lang.getString("sitebooklet", "date")%></td>
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
