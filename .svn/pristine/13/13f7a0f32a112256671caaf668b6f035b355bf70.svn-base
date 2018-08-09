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
Integer idAlgorithm = (Integer)ut.getAttribute("idalg");
if( idAlgorithm == null )
	idAlgorithm = 1;
StartStopBean bean = OptimumManager.getInstance().getStartStop(idAlgorithm);

//combo algorithms
StringBuffer sbAlgList = new StringBuffer(); 
StartStopBean[] aAlg = StartStopBean.getStartStopList();
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

String strComputedTimeOn = (bean.getComputedTimeOnHour() < 10 ? "0" : "") + bean.getComputedTimeOnHour()
	+ ":" + (bean.getComputedTimeOnMinute() < 10 ? "0" : "") + bean.getComputedTimeOnMinute();
String strComputedTimeOff = (bean.getComputedTimeOffHour() < 10 ? "0" : "") + bean.getComputedTimeOffHour()
	+ ":" + (bean.getComputedTimeOffMinute() < 10 ? "0" : "") + bean.getComputedTimeOffMinute();

Calendar calendar = new GregorianCalendar();
// Current Day of Year
int curDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
calendar.setMinimalDaysInFirstWeek(1);
calendar.setFirstDayOfWeek(Calendar.MONDAY);
// Current Week
int curWeek = calendar.get(Calendar.WEEK_OF_YEAR);
// Current Month
int curMonth = calendar.get(Calendar.MONTH);
curMonth++;
// Current Year
int curYear = calendar.get(Calendar.YEAR);
// graph requested
String timeInterval = (String)ut.removeAttribute("timeInterval");
Calendar calBegin = (Calendar)ut.removeAttribute("dtBegin");
Calendar calEnd = (Calendar)ut.removeAttribute("dtEnd");
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

boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();

String xlabel = lang.getString("opt_startstop", "day");
String w1 = bean.getSlaveNo() > 0 ? "5" : "15";
String w2 = bean.getSlaveNo() > 0 ? "5" : "0";
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<input type='hidden' id='Enabled' value='<%=bean.isEnabled()%>'>
<input type='hidden' id='confirm_reset' value='<%=lang.getString("opt_startstop", "confirm_reset")%>'>

<fieldset class="field"><legend class="standardTxt"><%=lang.getString("opt_startstop", "plugin_status")%></legend>
<form id="frm_opt_dashboard" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input id="cmd" name="cmd" type="hidden">
<input id="year" name="year" type="hidden" value="<%=curYear%>">
<input id="day" name="day" type="hidden" value="<%=curDayOfYear%>">

<% if( bean.isConfigured() ) { %>
<input id="Configured" name="Configured" type="hidden" value="true">
<table class="table" width="100%" cellpadding="0" cellspacing="0">
	<tr class="Row1" style="height:30px">
		<td class="td" width="5%">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_startstop", "alg_name")%></span></td>
		<td class="td" colspan="4">
			<select name="idalg" onChange="onRefresh()">
				<%=sbAlgList.toString()%>
			</select>
		</td>
		<td colspan="5" align="right">
		<%if( !bean.getCounterDate().isEmpty() ) {%>
		<b><font color="<%=bean.getCounterValue() < 0 ? "red" : "green"%>">
			<%=lang.getString("opt_startstop", "minutes_saved")%>
			<%=bean.getCounterValue() < 0 ? "-" : ""%><%=Math.abs(bean.getCounterValue() / 60)%><%=lang.getString("opt_startstop", "hh")%>
			<%=Math.abs(bean.getCounterValue() % 60)%><%=lang.getString("opt_startstop", "mm")%>
		</font></b>
		<%=lang.getString("opt_startstop", "since")%>
		<%=bean.getCounterDate()%>
		<%}%>		
		</td>
	</tr>
	<tr class="Row1" style="height:30px">
		<td class="td" width="5%">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_startstop", "TemperatureSetpoint")%></span></td>
		<td class="td"><span id="spanTemperatureSetpoint" class="standardTxtBold"><%=bean.isEnabled() ? bean.getTemperatureSetpoint() : "***"%></span> <%=lang.getString("opt_startstop", "degrees")%></td>
		<td class="td" width="<%=w1%>%">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_startstop", "UnitOnOff_master")%></span></td>
		<td class="td"><span id="spanUnitOnOff" class="standardTxtBold"><%=bean.isEnabled() ? lang.getString("opt_startstop", "UnitStatus"+bean.getUnitOnOff()) : "&nbsp;***"%></span></td>
		<td class="td" width="<%=w2%>%">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%if( bean.getSlaveNo() >= 1 ) {%><%=lang.getString("opt_startstop", "Slave")%> 1 - <%=lang.getString("opt_startstop", "status")%></span><%} else {%>&nbsp;<%}%></td>
		<td class="td"><%if( bean.getSlaveNo() >= 1 ) {%><span id="spanUnitOnOff1" class="standardTxtBold"><%=bean.isEnabled() && bean.getSlaveNo() >= 1 ? lang.getString("opt_startstop", "UnitStatus"+bean.getUnitOnOff(1)) : "&nbsp;***"%></span><%} else {%>&nbsp;<%}%></td>
		<td class="td" width="5%">&nbsp;</td>
	</tr>
	<tr class="Row1" style="height:30px">
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_startstop", "InternalTemperature")%></span></td>
		<td class="td"><span id="spanInternalTemperature" class="standardTxtBold"><%=bean.isEnabled() ? bean.getInternalTemperature() : "***"%></span> <%=lang.getString("opt_startstop", "degrees")%></td>
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_startstop", "TimeOn")%></span></td>
		<td class="td"><table><tr><td class="standardTxtBold"><span id="spanTimeOn"><%=(bean.isEnabled() && bean.existAnticipatedStartTime()) ? strComputedTimeOn : "***"%></span></td><td><% if (bean.isEnabled() && bean.existAnticipatedStartTime()) { if(bean.getStartCalcInProgress()){ %><img src="images/optimum//calcinprogress.gif"><%} if(bean.getStartActionDone()) {%><img src="images/optimum/ok.gif"><%} }%></td></tr></table></td>
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%if( bean.getSlaveNo() >= 2 ) {%><%=lang.getString("opt_startstop", "Slave")%> 2 - <%=lang.getString("opt_startstop", "status")%></span><%} else {%>&nbsp;<%}%></td>
		<td class="td"><span id="spanUnitOnOff2" class="standardTxtBold"><%if( bean.getSlaveNo() >= 2 ) {%><%=bean.isEnabled() && bean.getSlaveNo() >= 2 ? lang.getString("opt_startstop", "UnitStatus"+bean.getUnitOnOff(2)) : "&nbsp;***"%></span><%} else {%>&nbsp;<%}%></td>
		<td class="td">&nbsp;</td>
	</tr>
	<tr class="Row1" style="height:25px">
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_startstop", "ExternalTemperature")%></span></td>
		<td class="td"><span id="spanExternalTemperature" class="standardTxtBold"><%=bean.isEnabled() ? bean.getExternalTemperature() : "***"%></span> <%=lang.getString("opt_startstop", "degrees")%></td>
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_startstop", "TimeOff")%></span></td>
		<td class="td"><table><tr><td class="standardTxtBold"><span id="spanTimeOff"><%=(bean.isEnabled() && bean.existAnticipatedStopTime()) ? strComputedTimeOff : "***"%></span></td><td><% if (bean.isEnabled() && bean.existAnticipatedStopTime()) { if(bean.getStopCalcInProgress()){ %><img src="images/optimum//calcinprogress.gif"><%} if(bean.getStopActionDone()) {%><img src="images/optimum/ok.gif"><%} }%></td></tr></table></td>
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%if( bean.getSlaveNo() >= 3 ) {%><%=lang.getString("opt_startstop", "Slave")%> 3 - <%=lang.getString("opt_startstop", "status")%></span><%} else {%>&nbsp;<%}%></td>
		<td class="td"><span id="spanUnitOnOff3" class="standardTxtBold"><%if( bean.getSlaveNo() >= 3 ) {%><%=bean.isEnabled() && bean.getSlaveNo() >= 3 ? lang.getString("opt_startstop", "UnitStatus"+bean.getUnitOnOff(3)) : "&nbsp;***"%></span><%} else {%>&nbsp;<%}%></td>
		<td class="td">&nbsp;</td>
	</tr>
</table>
</form>
</fieldset>
<%if( calBegin != null && calEnd != null ) {
	calEnd.add(Calendar.DAY_OF_MONTH, -1);
	String title = lang.getString("opt_startstop", "last_week")
		+ ": "
		+ calBegin.get(Calendar.DAY_OF_MONTH) + " " + astrMonthNames[calBegin.get(Calendar.MONTH)]
		+ " - "
		+ calEnd.get(Calendar.DAY_OF_MONTH) + " " + astrMonthNames[calEnd.get(Calendar.MONTH)]
		+ ", "
		+ calBegin.get(Calendar.YEAR)
		+ " "
		+ (timeInterval.equals("yearly") ? "("+lang.getString("opt_startstop", "avg_report")+")" : "");
	DefaultCategoryDataset datasetA = (DefaultCategoryDataset)ut.getAttribute("datasetA");
	DefaultCategoryDataset datasetB = (DefaultCategoryDataset)ut.getAttribute("datasetB");
%>
<br>&nbsp;<br>&nbsp;
<table border="0" cellpadding="0" cellspacing="0" width="<%=sessionUser.getScreenWidth() - 50%>" style="border:3px solid #000000">
	<tr>
		<td width="*" align="center" class="standardTxt">
			<%=title%>
		</td>
	</tr>
	<tr>
		<td><img id="imgPlotLine" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?charttype=linechart_optimum&width=<%=sessionUser.getScreenWidth() - 55%>&height=250&imgid=datasetA&xlabel=<%=xlabel%>&um=<%=lang.getString("opt_startstop", "degrees")%>"></td>
	</tr>
	<tr>
		<td><img id="imgPlotBar" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?charttype=simplebarchart&width=<%=sessionUser.getScreenWidth() - 55%>&height=250&imgid=datasetB&xlabel=<%=xlabel%>&um=<%=lang.getString("opt_startstop", "minutes")%>"></td>
	</tr>
</table>
<%} else {%>
<script language="javascript">
onDashboardReport();
</script>
<%}%>

<% } else { %>
<input id="Configured" name="Configured" type="hidden" value="false">
<p> <h4 align="center"><%=lang.getString("opt_startstop", "noconf")%></h4></p>
<% } %>
