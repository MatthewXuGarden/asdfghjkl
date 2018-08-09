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
import="com.carel.supervisor.base.config.ProductInfoMgr"
import="com.carel.supervisor.plugin.optimum.*"
import="java.text.SimpleDateFormat"
import="org.jfree.data.category.DefaultCategoryDataset"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();
LightsBean bean = OptimumManager.getInstance().getLights(); 

Float day = bean.getDay();
String strDay = day != null ? day.floatValue() != 0.0 ? lang.getString("opt_lights", "on") : lang.getString("opt_lights", "off") : "***";
Float night = bean.getNight();
String strNight = night != null ? night.floatValue() != 0.0 ? lang.getString("opt_lights", "on") : lang.getString("opt_lights", "off") : "***";

Calendar calendar = new GregorianCalendar();
calendar.setMinimalDaysInFirstWeek(1);
calendar.setFirstDayOfWeek(Calendar.MONDAY);
// Current Day of Year
int curDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
calendar.setMinimalDaysInFirstWeek(3);
calendar.setFirstDayOfWeek(Calendar.MONDAY);
// Current Week
int curWeek = calendar.get(Calendar.WEEK_OF_YEAR);
// Current Month
int curMonth = calendar.get(Calendar.MONTH);
curMonth++;
// Current Year
int curYear = calendar.get(Calendar.YEAR);
// graph requested
//String timeInterval = (String)ut.removeAttribute("timeInterval");
//Calendar calBegin = (Calendar)ut.removeAttribute("dtBegin");
//Calendar calEnd = (Calendar)ut.removeAttribute("dtEnd");
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


// series labels
SimpleDateFormat fmtDay = new SimpleDateFormat("dd");
SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm");
String strSunrise = lang.getString("opt_lights", "sunrise");
String strSunset = lang.getString("opt_lights", "sunset");

// create data sets
DefaultCategoryDataset datasetA = new DefaultCategoryDataset();
sessionUser.getCurrentUserTransaction().setAttribute("datasetA", datasetA);
DefaultCategoryDataset datasetB = new DefaultCategoryDataset();
sessionUser.getCurrentUserTransaction().setAttribute("datasetB", datasetB);

// time interval
Calendar calBegin = new GregorianCalendar();
calBegin.setMinimalDaysInFirstWeek(1);
calBegin.setFirstDayOfWeek(Calendar.MONDAY);
calBegin.set(Calendar.HOUR_OF_DAY, 0);
calBegin.set(Calendar.MINUTE, 0);
calBegin.set(Calendar.SECOND, 0);
calBegin.set(Calendar.MILLISECOND, 0);
Calendar calEnd = null;
int nYear = curYear;
int nDayOfYear = curDayOfYear;
calBegin.set(Calendar.YEAR, nYear);
calBegin.set(Calendar.DAY_OF_YEAR, nDayOfYear - 15);
calEnd = (Calendar)calBegin.clone();
calEnd.set(Calendar.DAY_OF_YEAR, nDayOfYear + 16);

// compute datasets data sets
Calendar cal = (Calendar)calBegin.clone();
while( cal.before(calEnd) ) {
	Date dtSunrise = SunriseSunset.getSunrise(bean.getLatitude(), bean.getLongitude(), cal.getTime());
	Date dtSunset = SunriseSunset.getSunset(bean.getLatitude(), bean.getLongitude(), cal.getTime());
	if(dtSunrise == null)
		datasetA.addValue(null, strSunrise, "" + cal.get(Calendar.DAY_OF_MONTH));
	else
		datasetA.addValue((float)(dtSunrise.getHours() * 60 + dtSunrise.getMinutes()) / (24 * 60) * 24, strSunrise, "" + cal.get(Calendar.DAY_OF_MONTH));
	if(dtSunset == null)
		datasetA.addValue(null, strSunset, "" + cal.get(Calendar.DAY_OF_MONTH));
	else
		datasetA.addValue((float)(dtSunset.getHours() * 60 + dtSunset.getMinutes()) / (24 * 60) * 24, strSunset, "" + cal.get(Calendar.DAY_OF_MONTH));
	
	cal.add(Calendar.DAY_OF_MONTH, 1);
}

// titles
calEnd.add(Calendar.DAY_OF_MONTH, -1);
String strMonthTitle = ""
	+ calBegin.get(Calendar.DAY_OF_MONTH) + " " + astrMonthNames[calBegin.get(Calendar.MONTH)]
	+ " - "
	+ calEnd.get(Calendar.DAY_OF_MONTH) + " " + astrMonthNames[calEnd.get(Calendar.MONTH)]
	+ ", "
	+ calBegin.get(Calendar.YEAR);
String strYearTitle = ""
	+ calendar.get(Calendar.YEAR)
	+ " - " + lang.getString("opt_lights", "avg_report")
	+ " (" + lang.getString("opt_lights", "curr_week") + ":" + calendar.get(Calendar.WEEK_OF_YEAR) + ")"; 
	
cal.set(Calendar.YEAR, nYear);
cal.set(Calendar.DAY_OF_YEAR, 1);
for(int i = 1; i <= 52; i++) {
	cal.set(Calendar.WEEK_OF_YEAR, i);
	cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
	Date dtSunrise = SunriseSunset.getSunrise(bean.getLatitude(), bean.getLongitude(), cal.getTime());
	Date dtSunset = SunriseSunset.getSunset(bean.getLatitude(), bean.getLongitude(), cal.getTime());
	if(dtSunrise == null)
		datasetB.addValue(null, strSunrise, "" + cal.get(Calendar.WEEK_OF_YEAR));
	else
		datasetB.addValue((float)(dtSunrise.getHours() * 60 + dtSunrise.getMinutes()) / (24 * 60) * 24, strSunrise, "" + cal.get(Calendar.WEEK_OF_YEAR));
	if(dtSunset == null)
		datasetB.addValue(null, strSunset, "" + cal.get(Calendar.WEEK_OF_YEAR));
	else
		datasetB.addValue((float)(dtSunset.getHours() * 60 + dtSunset.getMinutes()) / (24 * 60) * 24, strSunset, "" + cal.get(Calendar.WEEK_OF_YEAR));
}

cal.set(Calendar.YEAR, nYear);
cal.set(Calendar.DAY_OF_YEAR, 1);
calEnd = (Calendar)cal.clone();
calEnd.add(Calendar.YEAR, 1);
int iDay = 0;
String[] astrDay = new String[368];
String[] astrSunrise = new String[368];
String[] astrSunset = new String[368];
// fill the last entries with empty strings
for(int i = 365; i < astrDay.length; i++) {
	astrDay[i] = "";
	astrSunrise[i] = "";
	astrSunset[i] = "";
}
while( cal.before(calEnd) ) {
	Date dtSunrise = SunriseSunset.getSunrise(bean.getLatitude(), bean.getLongitude(), cal.getTime());
	Date dtSunset = SunriseSunset.getSunset(bean.getLatitude(), bean.getLongitude(), cal.getTime());
	
	astrDay[iDay] = fmtDay.format(cal.getTime()) + " " + astrMonthNames[cal.get(Calendar.MONTH)];
	if(dtSunrise==null)
		astrSunrise[iDay] = "---";
	else
		astrSunrise[iDay] = fmtTime.format(dtSunrise);
	if(dtSunset==null)
		astrSunset[iDay] = "---";
	else
		astrSunset[iDay] = fmtTime.format(dtSunset);
	
	cal.add(Calendar.DAY_OF_YEAR, 1);
	iDay++;
}


boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();

String xlabel_day = lang.getString("opt_lights", "day");
String xlabel_week = lang.getString("opt_lights", "week");
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<input type='hidden' id='Enabled' value='<%=bean.isEnabled()%>'>
<form id="frm_opt_dashboard" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input id="cmd" name="cmd" type="hidden">
<input id="year" name="year" type="hidden" value="<%=curYear%>">
<input id="day" name="day" type="hidden" value="<%=curDayOfYear%>">
</form>

<fieldset class="field"><legend class="standardTxt"><%=lang.getString("opt_lights", "plugin_status")%></legend>

<%if( bean.isConfigured()  ) {%>
<input id="Configured" name="Configured" type="hidden" value="true">
<table class="table" width="100%" cellpadding="0" cellspacing="0">
	<tr class="Row1" style="height:10px">
		<td class="td" colspan="7">&nbsp;</td>
	</tr>
	<tr class="Row1" style="height:30px">
		<td class="td" width="5%">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_lights", "lat")%></span></td>
		<td class="td"><span id="spanLatitude" class="standardTxtBold"><%=bean.isEnabled() ? bean.getLatitude() : "***"%></span> <%=lang.getString("opt_lights", "degrees")%></td>
		<td class="td" width="40%">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_lights", "status")%></span></td>
		<td class="td"><span id="spanStatus" class="standardTxtBold"><%=bean.isEnabled() ? lang.getString("opt_lights", bean.isDay() ? "day" : "night") : "&nbsp;***"%></span></td>
		<td class="td" width="5%">&nbsp;</td>
	</tr>
	<tr class="Row1" style="height:30px">
		<td class="td" width="5%">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_lights", "long")%></span></td>
		<td class="td"><span id="spanLongitude" class="standardTxtBold"><%=bean.isEnabled() ? bean.getLongitude() : "***"%></span> <%=lang.getString("opt_lights", "degrees")%></td>
		<td class="td" width="40%">&nbsp;</td>
		<td class="td">&nbsp;</td>
		<td class="td">&nbsp;</td>
		<td class="td" width="5%">&nbsp;</td>
	</tr>
	<tr class="Row1" style="height:25px">
		<td class="td" width="5%">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_lights", "sunrise")%></span></td>
		<td class="td"><span id="spanSunrise" class="standardTxtBold"><%=bean.isEnabled() ? bean.getSunriseString() : "***"%></span></td>
		<td class="td" width="40%">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_lights", "day_var")%></span></td>
		<td class="td"><span id="spanDay"><%=bean.isEnabled() ? strDay : "***"%></span></td>
		<td class="td" width="5%">&nbsp;</td>
	</tr>
	<tr class="Row1" style="height:25px">
		<td class="td" width="5%">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_lights", "sunset")%></span></td>
		<td class="td"><span id="spanSunset" class="standardTxtBold"><%=bean.isEnabled() ? bean.getSunsetString() : "***"%></span></td>
		<td class="td" width="40%">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_lights", "night_var")%></span></td>
		<td class="td"><span id="spanNight"><%=bean.isEnabled() ? strNight : "***"%></span></td>
		<td class="td" width="5%">&nbsp;</td>
	</tr>
</table>
</fieldset>
<br>&nbsp;<br>&nbsp;
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tbody>
		<tr>
			<td width="25px">
				&nbsp;
			</td>
			
			<!-- WEEKLY GRAPH HIDDEN -->
			<!-- td id="tabChart1" name="Chart1" class="egtabselected2" style="width: 60px;text-align: center; cursor: pointer;" onclick="onSelectTab(this)">
				<%=lang.getString("opt_lights", "chart1")%>
			</td>
			<td width="5px">&nbsp;</td-->
			
			<td id="tabChart2" name="Chart2" class="egtabselected2" style="width: 60px; text-align: center; cursor: pointer;" onclick="onSelectTab(this)">
				<%=lang.getString("opt_lights", "chart2")%>
			</td>
			<td width="5px">&nbsp;</td>
			<td id="tabReport" name="Report" class="egtabnotselected2" style="width: 60px;text-align: center; cursor: pointer;" onclick="onSelectTab(this)">
				<%=lang.getString("opt_lights", "report")%>
			</td>
			<td width="*">&nbsp;</td>
		</tr>
	</tbody>
</table>

<!-- WEEKLY GRAPH HIDDEN -->
<!-- div id="divChart1">
<table border="0" cellpadding="0" cellspacing="0" width="<%=sessionUser.getScreenWidth() - 50%>" style="border:3px solid #000000">
	<tr>
		<td width="*" align="center" class="standardTxt">
			<%=strMonthTitle%>
		</td>
	</tr>
	<tr>
		<td><img src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?charttype=linechart_l&width=<%=sessionUser.getScreenWidth() - 55%>&height=400&imgid=datasetA&xlabel=<%=xlabel_day%>&um=<%=lang.getString("opt_lights", "hours")%>"></td>
	</tr>
</table>
</div-->

<div id="divChart2">
<table border="0" cellpadding="0" cellspacing="0" width="<%=sessionUser.getScreenWidth() - 50%>" style="border:3px solid #000000">
	<tr>
		<td width="*" align="center" class="standardTxt">
			<%=strYearTitle%>
		</td>
	</tr>
	<tr>
		<td><img src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?charttype=linechart_l&width=<%=sessionUser.getScreenWidth() - 55%>&height=400&imgid=datasetB&xlabel=<%=xlabel_week%>&um=<%=lang.getString("opt_lights", "hours")%>"></td>
	</tr>
</table>
</div>

<div id="divReport" style="display:none;">
<table border="0" cellpadding="0" cellspacing="0" width="<%=sessionUser.getScreenWidth() - 50%>" style="border:3px solid #000000">
	<tr><td>
<table class="standardTxt" width="100%">
	<tr>
		<td class="th"><%=lang.getString("opt_lights", "day")%></td>
		<td class="th"><%=lang.getString("opt_lights", "sunrise")%></td>
		<td class="th"><%=lang.getString("opt_lights", "sunset")%></td>
		<td class="th"><%=lang.getString("opt_lights", "day")%></td>
		<td class="th"><%=lang.getString("opt_lights", "sunrise")%></td>
		<td class="th"><%=lang.getString("opt_lights", "sunset")%></td>
		<td class="th"><%=lang.getString("opt_lights", "day")%></td>
		<td class="th"><%=lang.getString("opt_lights", "sunrise")%></td>
		<td class="th"><%=lang.getString("opt_lights", "sunset")%></td>
		<td class="th"><%=lang.getString("opt_lights", "day")%></td>
		<td class="th"><%=lang.getString("opt_lights", "sunrise")%></td>
		<td class="th"><%=lang.getString("opt_lights", "sunset")%></td>
	</tr>
<%for(int i = 0; i < 92; i++) {%>
	<tr class="Row<%=i % 2 == 0 ? 2 : 1%>">
		<td><b><%=astrDay[i]%></b></td>
		<td><%=astrSunrise[i]%></td>
		<td><%=astrSunset[i]%></td>
		<td><b><%=astrDay[i + 92]%></b></td>
		<td><%=astrSunrise[i + 92]%></td>
		<td><%=astrSunset[i + 92]%></td>
		<td><b><%=astrDay[i + 184]%></b></td>
		<td><%=astrSunrise[i + 184]%></td>
		<td><%=astrSunset[i + 184]%></td>
		<td><b><%=astrDay[i + 276]%></b></td>
		<td><%=astrSunrise[i + 276]%></td>
		<td><%=astrSunset[i + 276]%></td>
	</tr>
<%}%>	
</table>
</td></tr>
</table>
</div>
<% } else { %>
<input id="Configured" name="Configured" type="hidden" value="false">
<p> <h4 align="center"><%=lang.getString("opt_lights", "noconf")%></h4></p>
<% } %>
<input type='hidden' id='warn_action' value='<%=lang.getString("opt_lights","warn_action")%>'>


