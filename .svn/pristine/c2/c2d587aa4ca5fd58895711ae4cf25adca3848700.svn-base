<%@ page language="java" 
import="java.util.*"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.session.Transaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.base.log.LoggerMgr"
import="com.carel.supervisor.base.config.ProductInfoMgr"
import="java.text.SimpleDateFormat"
import="org.jfree.data.category.DefaultCategoryDataset"
import="java.sql.Timestamp"
import="com.carel.supervisor.plugin.optimum.*"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();
Integer idAlgorithm = (Integer)ut.getAttribute("idalg");
if( idAlgorithm == null )
	idAlgorithm = 1;

//combo algorithms
StringBuffer sbAlgList = new StringBuffer(); 
NightFreeCoolingBean[] aAlg = NightFreeCoolingBean.getNightFreeCoolingList();
for(int i = 0; i < aAlg.length; i++) {
	sbAlgList.append("<option value=\"");
	sbAlgList.append(String.valueOf(aAlg[i].getAlgorithId()));
	if( aAlg[i].getAlgorithId() == idAlgorithm )
		sbAlgList.append("\" selected>");
	else
		sbAlgList.append("\">");
	sbAlgList.append(aAlg[i].getAlgorithmName());
	sbAlgList.append("</option>\n");
}

//requested interval
String timeInterval = (String)ut.removeAttribute("timeInterval");
if( timeInterval == null )
	timeInterval = "";
Calendar calBegin = (Calendar)ut.removeAttribute("dtBegin");
Calendar calEnd = (Calendar)ut.removeAttribute("dtEnd");

Calendar calendar = new GregorianCalendar();
calendar.setMinimalDaysInFirstWeek(1);
calendar.setFirstDayOfWeek(Calendar.MONDAY);
//Current Week
int curWeek = calendar.get(Calendar.WEEK_OF_YEAR);
//Current Month
int curMonth = calendar.get(Calendar.MONTH);
curMonth++;
//Current Year
int curYear = calendar.get(Calendar.YEAR);
calendar.set(Calendar.HOUR_OF_DAY, 0);
calendar.set(Calendar.MINUTE, 0);
calendar.set(Calendar.SECOND, 0);
calendar.set(Calendar.MILLISECOND, 0);

if( timeInterval.equals("weekly") ) {
	curWeek = calBegin.get(Calendar.WEEK_OF_YEAR);
	curYear = calBegin.get(Calendar.YEAR);
}
else if( timeInterval.equals("monthly") ) {
	curMonth = calBegin.get(Calendar.MONTH) + 1;
	curYear = calBegin.get(Calendar.YEAR);
}
else if( timeInterval.equals("yearly") ) {
	curYear = calBegin.get(Calendar.YEAR);
}

Timestamp tsFirst = StartStopLog.getFirstTimestamp();
int firstYear = tsFirst != null ? tsFirst.getYear() + 1900 : curYear;
Timestamp tsLast = StartStopLog.getLastTimestamp();
int lastYear = tsLast != null ? tsLast.getYear() + 1900 : curYear;
StringBuffer yearOptions = new StringBuffer();
for(int year = firstYear; year <= lastYear; year++) {
	yearOptions.append("<option value='" + year + "'");
	if( year == curYear )
		yearOptions.append(" selected");
	yearOptions.append(">" + year + "</option>\n");
}

SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
String[] astrMonthNames = {
	lang.getString("cal", "january"),
	lang.getString("cal", "february"),
	lang.getString("cal", "march"),
	lang.getString("cal", "april"),
	lang.getString("cal", "may"),
	lang.getString("cal", "june"),
	lang.getString("cal", "july"),
	lang.getString("cal", "august"),
	lang.getString("cal", "september"),
	lang.getString("cal", "october"),
	lang.getString("cal", "november"),
	lang.getString("cal", "december")	
};
String[] astrDayNames = { "",
	lang.getString("cal", "sun"),
	lang.getString("cal", "mon"),
	lang.getString("cal", "tue"),
	lang.getString("cal", "wed"),
	lang.getString("cal", "thu"),
	lang.getString("cal", "fri"),
	lang.getString("cal", "sat")	
};
String[] astrParamNames = NightFreeCoolingLog.getLogParamNames(language);
boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();

String xlabel = lang.getString("opt_nightfreecooling", "day");
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>

<form id="frm_opt_report" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input id="cmd" name="cmd" type="hidden">
<fieldset class="field"><legend class="standardTxt"><%=lang.getString("opt_nightfreecooling", "report_period")%></legend>
<table class="table" width="100%">
	<tr>
		<td class="standardTxt" colspan="3"><%=lang.getString("opt_startstop", "alg_name")%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<select name="idalg">
				<%=sbAlgList.toString()%>
			</select>
		</td>		
	</tr>
	<tr>
		<td class="th" width="33%"><%=lang.getString("opt_nightfreecooling", "weekly")%></td>
		<td class="th" width="34%"><%=lang.getString("opt_nightfreecooling", "monthly")%></td>
		<td class="th" width="33%"><%=lang.getString("opt_nightfreecooling", "yearly")%></td>
	</tr>
	<tr class="Row1">
		<td class="td" bgcolor="<%=timeInterval.equals("weekly") ? "YELLOW" : "WHITE"%>">
			<select name="week" id="week">
				<%for(int iWeek = 1; iWeek <= 52; iWeek++) {
					calendar.set(Calendar.WEEK_OF_YEAR, iWeek);
					calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					String strWeek = calendar.get(Calendar.DAY_OF_MONTH)
						+ " " + astrMonthNames[calendar.get(Calendar.MONTH)];
					calendar.add(Calendar.DAY_OF_YEAR, 6);
					strWeek += " - " + calendar.get(Calendar.DAY_OF_MONTH)
						+ " " + astrMonthNames[calendar.get(Calendar.MONTH)];
				%>
				<option value="<%=iWeek%>" <%=((iWeek==curWeek)?"selected":"") %>><%=strWeek%></option>
				<%}%>
			</select >
			&nbsp;
			<select name="w_year"><%=yearOptions.toString()%></select>
			&nbsp;
			<img src="images/actions/calculate_black.png" style="cursor: pointer" onclick="onReport('weekly');"/>
		</td>
		<td class="td" bgcolor="<%=timeInterval.equals("monthly") ? "YELLOW" : "WHITE"%>">
			<select name="month" id="month">
				<option value="1" <%=((curMonth==1)?"selected":"")%>><%=lang.getString("cal", "january")%></option>
				<option value="2" <%=((curMonth==2)?"selected":"")%>><%=lang.getString("cal", "february")%></option>
				<option value="3" <%=((curMonth==3)?"selected":"")%>><%=lang.getString("cal", "march")%></option>
				<option value="4" <%=((curMonth==4)?"selected":"")%>><%=lang.getString("cal", "april")%></option>
				<option value="5" <%=((curMonth==5)?"selected":"")%>><%=lang.getString("cal", "may")%></option>
				<option value="6" <%=((curMonth==6)?"selected":"")%>><%=lang.getString("cal", "june")%></option>
				<option value="7" <%=((curMonth==7)?"selected":"")%>><%=lang.getString("cal", "july")%></option>
				<option value="8" <%=((curMonth==8)?"selected":"")%>><%=lang.getString("cal", "august")%></option>
				<option value="9" <%=((curMonth==9)?"selected":"")%>><%=lang.getString("cal", "september")%></option>
				<option value="10" <%=((curMonth==10)?"selected":"")%>><%=lang.getString("cal", "october")%></option>
				<option value="11" <%=((curMonth==11)?"selected":"")%>><%=lang.getString("cal", "november")%></option>
				<option value="12" <%=((curMonth==12)?"selected":"")%>><%=lang.getString("cal", "december")%></option>
			</select>
			&nbsp;
			<select name="m_year"><%=yearOptions.toString()%></select>
			&nbsp;
			<img src="images/actions/calculate_black.png" style="cursor: pointer" onclick="onReport('monthly');"/>
		</td>
		<td class="td" bgcolor="<%=timeInterval.equals("yearly") ? "YELLOW" : "WHITE"%>">
			<select name="year"><%=yearOptions.toString()%></select>
			&nbsp;
			<img src="images/actions/calculate_black.png" style="cursor: pointer" onclick="onReport('yearly');"/>
		</td>
	</tr>
</table>
</fieldset>
</form>
<%if( calBegin != null && calEnd != null ) {
	calEnd.add(Calendar.DAY_OF_MONTH, -1);
	String title = lang.getString("opt_nightfreecooling", timeInterval)
	+ ": "
	+ calBegin.get(Calendar.DAY_OF_MONTH) + " " + astrMonthNames[calBegin.get(Calendar.MONTH)]
	+ " - "
	+ calEnd.get(Calendar.DAY_OF_MONTH) + " " + astrMonthNames[calEnd.get(Calendar.MONTH)]
	+ ", "
	+ calBegin.get(Calendar.YEAR)
	+ " "
	+ (timeInterval.equals("yearly") ? "("+lang.getString("opt_nightfreecooling", "avg_report")+")" : "");
	
	DefaultCategoryDataset datasetA = (DefaultCategoryDataset)ut.getAttribute("datasetA");
	DefaultCategoryDataset datasetB = (DefaultCategoryDataset)ut.getAttribute("datasetB");
	DefaultCategoryDataset datasetT = (DefaultCategoryDataset)ut.getAttribute("datasetT");
%>

<%if( "yearly".equals(timeInterval) ) xlabel = lang.getString("opt_nightfreecooling", "week");  else xlabel = lang.getString("opt_nightfreecooling", "day");%>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tbody>
		<tr>
			<td width="25px">
				&nbsp;
			</td>
			<td id="tabChart" name="Chart" class="egtabselected2" style="width: 60px;text-align: center; cursor: pointer;" onclick="onSelectTab(this)">
				<%=lang.getString("opt_nightfreecooling", "chart") %>
			</td>
			<td width="5px">
				&nbsp;
			</td>
			<td id="tabTable" name="Table" class="egtabnotselected2" style="width: 60px; text-align: center; cursor: pointer;" onclick="onSelectTab(this)">
				<%=lang.getString("opt_nightfreecooling", "table") %>
			</td>
			<td width="*" align="center" class="standardTxt">
				&nbsp;
			</td>
		</tr>
	</tbody>
</table>
<div id="divChart">
<table border="0" cellpadding="0" cellspacing="0" width="<%=sessionUser.getScreenWidth() - 50%>" style="border:3px solid #000000">
	<tr>
		<td width="*" align="center" class="standardTxt">
			<%=title%>
		</td>
	</tr>
	<tr>
		<td><img id="imgPlotLine" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?dtf=<%=dateFormat.format(calBegin.getTimeInMillis())%>&dtt=<%=dateFormat.format(calEnd.getTimeInMillis())%>&charttype=linechart_optimum&width=<%=sessionUser.getScreenWidth() - 55%>&height=250&imgid=datasetA&xlabel=<%=xlabel%>&um=<%=lang.getString("opt_nightfreecooling", "KJ_Kg")%>"></td>
	</tr>
	<tr>
		<td><img id="imgPlotBar" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?dtf=<%=dateFormat.format(calBegin.getTimeInMillis())%>&dtt=<%=dateFormat.format(calEnd.getTimeInMillis())%>&charttype=simplebarchart&width=<%=sessionUser.getScreenWidth() - 55%>&height=250&imgid=datasetB&xlabel=<%=xlabel%>&series0color=00FF00&um=<%=lang.getString("opt_nightfreecooling", "minutes")%>"></td>
	</tr>
</table>
</div>
<div id="divTable" style="display:none;">
<table border="0" cellpadding="0" cellspacing="0" width="<%=sessionUser.getScreenWidth() - 50%>" style="border:3px solid #000000">
	<tr><td>
<table class="standardTxt" width="100%">
	<tr>
		<td class="th" width="10%"><%="yearly".equals(timeInterval) ? lang.getString("opt_nightfreecooling", "week") : lang.getString("opt_nightfreecooling", "day")%></td>
		<td class="th" width="12%"><%=lang.getString("opt_nightfreecooling", "AnticipatedTimeStart") + " (" +lang.getString("opt_nightfreecooling", "minutes")+")"%></td>
		<td class="th" width="13%"><%=lang.getString("opt_nightfreecooling", "InternalTemperature") + " (" +lang.getString("opt_nightfreecooling", "degrees")+")"%></td>
		<td class="th" width="13%"><%=lang.getString("opt_nightfreecooling", "InternalHumidity") + " (" +lang.getString("opt_nightfreecooling", "percentage_rh")+")"%></td>
		<td class="th" width="13%"><%=lang.getString("opt_nightfreecooling", "InternalEnthalpy") + " (" +lang.getString("opt_nightfreecooling", "KJ_Kg")+")"%></td>
		<td class="th" width="13%"><%=lang.getString("opt_nightfreecooling", "ExternalTemperature") + " (" +lang.getString("opt_nightfreecooling", "degrees")+")"%></td>
		<td class="th" width="13%"><%=lang.getString("opt_nightfreecooling", "ExternalHumidity") + " (" +lang.getString("opt_nightfreecooling", "percentage_rh")+")"%></td>
		<td class="th" width="13%"><%=lang.getString("opt_nightfreecooling", "ExternalEnthalpy") + " (" +lang.getString("opt_nightfreecooling", "KJ_Kg")+")"%></td>
	</tr>
<%if( "yearly".equals(timeInterval) )
	for(int i = 1; i <= 52; i++) {
		try {
		Float fEExtStart = (Float)datasetA.getValue(astrParamNames[NightFreeCoolingLog.LOG_ExternalEnthalpy], String.valueOf(i));
		Float fEIntStart = (Float)datasetA.getValue(astrParamNames[NightFreeCoolingLog.LOG_InternalEnthalpy], String.valueOf(i));
		Float fTExtStart = (Float)datasetT.getValue(astrParamNames[NightFreeCoolingLog.LOG_ExternalTemperature], String.valueOf(i));
		Float fTIntStart = (Float)datasetT.getValue(astrParamNames[NightFreeCoolingLog.LOG_InternalTemperature], String.valueOf(i));
		Float fHExtStart = (Float)datasetT.getValue(astrParamNames[NightFreeCoolingLog.LOG_ExternalHumidity], String.valueOf(i));
		Float fHIntStart = (Float)datasetT.getValue(astrParamNames[NightFreeCoolingLog.LOG_InternalHumidity], String.valueOf(i));
		Float fPreStart = (Float)datasetB.getValue(astrParamNames[NightFreeCoolingLog.LOG_AnticipatedTimeStart], String.valueOf(i));
		
%>
	<tr class="Row<%=i % 2 == 0 ? 2 : 1%>">
		<td><%=i%></td>
		<td><%=fPreStart != null ? String.valueOf(fPreStart.intValue()) : "***"%></td>
		<td><%=fTIntStart != null ? String.valueOf(fTIntStart) : "***"%></td>
		<td><%=fHIntStart != null ? String.valueOf(fHIntStart) : "***"%></td>
		<td><%=fEIntStart != null ? String.valueOf(fEIntStart) : "***"%></td>
		<td><%=fTExtStart != null ? String.valueOf(fTExtStart) : "***"%></td>
		<td><%=fHExtStart != null ? String.valueOf(fHExtStart) : "***"%></td>
		<td><%=fEExtStart != null ? String.valueOf(fEExtStart) : "***"%></td>
	</tr>
<%	} catch(Exception e) { LoggerMgr.getLogger(this.getClass()).error(e); }
	}
else { int i = 1;
	while( !calBegin.after(calEnd)) {
		try {
		String col = dayFormat.format(calBegin.getTime());
		Float fEExtStart = (Float)datasetA.getValue(astrParamNames[NightFreeCoolingLog.LOG_ExternalEnthalpy], col);
		Float fEIntStart = (Float)datasetA.getValue(astrParamNames[NightFreeCoolingLog.LOG_InternalEnthalpy], col);
		Float fTExtStart = (Float)datasetT.getValue(astrParamNames[NightFreeCoolingLog.LOG_ExternalTemperature], col);
		Float fTIntStart = (Float)datasetT.getValue(astrParamNames[NightFreeCoolingLog.LOG_InternalTemperature], col);
		Float fHExtStart = (Float)datasetT.getValue(astrParamNames[NightFreeCoolingLog.LOG_ExternalHumidity], col);
		Float fHIntStart = (Float)datasetT.getValue(astrParamNames[NightFreeCoolingLog.LOG_InternalHumidity], col);
		Float fPreStart = (Float)datasetB.getValue(astrParamNames[NightFreeCoolingLog.LOG_AnticipatedTimeStart], String.valueOf(col));
%>
	<tr class="Row<%=i % 2 == 0 ? 2 : 1%>">
		<td><%=col + " " + astrDayNames[calBegin.get(Calendar.DAY_OF_WEEK)]%></td>
		<td><%=fPreStart != null ? String.valueOf(fPreStart.intValue()) : "***"%></td>
		<td><%=fTIntStart != null ? String.valueOf(fTIntStart) : "***"%></td>
		<td><%=fHIntStart != null ? String.valueOf(fHIntStart) : "***"%></td>
		<td><%=fEIntStart != null ? String.valueOf(fEIntStart) : "***"%></td>
		<td><%=fTExtStart != null ? String.valueOf(fTExtStart) : "***"%></td>
		<td><%=fHExtStart != null ? String.valueOf(fHExtStart) : "***"%></td>
		<td><%=fEExtStart != null ? String.valueOf(fEExtStart) : "***"%></td>
	</tr>
<%	} catch(Exception e) {
		LoggerMgr.getLogger(this.getClass()).error(e);
	}
		calBegin.add(Calendar.DAY_OF_MONTH, 1);
		i++;	
	}	}%>
</table>
</td></tr>
</table>
</div>
<%} else {%>
<script language="javascript">
onReport("weekly");
</script>
<%} %>
