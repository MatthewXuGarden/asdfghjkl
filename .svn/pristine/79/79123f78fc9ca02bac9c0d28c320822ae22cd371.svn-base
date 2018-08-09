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
NightFreeCoolingBean bean = OptimumManager.getInstance().getNightFreeCooling(idAlgorithm); 

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

String strComputedTimeOn = (bean.getComputedTimeOnHour() < 10 ? "0" : "") + bean.getComputedTimeOnHour()
	+ ":" + (bean.getComputedTimeOnMinute() < 10 ? "0" : "") + bean.getComputedTimeOnMinute();
String strTimeOff = (bean.getTimeOffHour() < 10 ? "0" : "") + bean.getTimeOffHour()
	+ ":" + (bean.getTimeOffMinute() < 10 ? "0" : "") + bean.getTimeOffMinute();

Calendar calendar = new GregorianCalendar();
//Current Day of Year
int curDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
calendar.setMinimalDaysInFirstWeek(1);
calendar.setFirstDayOfWeek(Calendar.MONDAY);
//Current Week
int curWeek = calendar.get(Calendar.WEEK_OF_YEAR);
//Current Month
int curMonth = calendar.get(Calendar.MONTH);
curMonth++;
//Current Year
int curYear = calendar.get(Calendar.YEAR);
//graph requested
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

String xlabel = lang.getString("opt_nightfreecooling", "day");
String w1 = bean.getSlaveNo() > 0 ? "5px" : "5%";
String w2 = bean.getSlaveNo() > 0 ? "1%" : "0%";
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<input type='hidden' id='Enabled' value='<%=bean.isEnabled()%>'>

<fieldset class="field"><legend class="standardTxt"><%=lang.getString("opt_nightfreecooling", "plugin_status")%></legend>
<form id="frm_opt_dashboard" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input id="cmd" name="cmd" type="hidden">
<input id="year" name="year" type="hidden" value="<%=curYear%>">
<input id="day" name="day" type="hidden" value="<%=curDayOfYear%>">
<% if( bean.isConfigured() ) { %>
<input id="Configured" name="Configured" type="hidden" value="true">
<table class="table" width="100%" cellpadding="0" cellspacing="0">
	<tr class="Row1" style="height:30px">
		<td class="td" width="<%=w1%>">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "alg_name")%></span></td>
		<td class="td" colspan="13">
			<select name="idalg" onChange="onRefresh()">
				<%=sbAlgList.toString()%>
			</select>
			&nbsp;&nbsp;&nbsp;
			<% if(bean.isOutsideWorkingDates() && bean.isEnabled()) { %><span class="standardTxt" ><font color="red"><%=lang.getString("opt_nightfreecooling", "NotWorkingPeriod")%></font></span> <% } %>
		</td>
		<td class="td" width="<%=w1%>">&nbsp;</td>
	</tr>
	<tr class="Row1" style="height:30px">
		<td class="td" width="<%=w1%>">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "TemperatureSetpoint")%></span></td>
		<td class="td"><span id="spanTemperatureSetpoint" class="standardTxtBold"><%=bean.isEnabled() ? bean.getTemperatureSetpoint() : "***"%></span> <%=lang.getString("opt_nightfreecooling", "degrees")%></td>
		<td class="td" width="<%=w1%>">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "InternalTemperature")%></span></td>
		<td class="td"><span id="spanInternalTemperature" class="standardTxtBold"><%=bean.isEnabled() ? bean.getInternalTemperature() : "***"%></span> <%=lang.getString("opt_nightfreecooling", "degrees")%></td>
		<td class="td" width="<%=w1%>">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "ExternalTemperature")%></span></td>
		<td class="td"><span id="spanExternalTemperature" class="standardTxtBold"><%=bean.isEnabled() ? bean.getExternalTemperature() : "***"%></span> <%=lang.getString("opt_nightfreecooling", "degrees")%></td>
		<td class="td" width="<%=w1%>">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "UnitOnOff_master")%></span></td>
		<td class="td"><span id="spanUnitOnOff" class="standardTxtBold"><%=bean.isEnabled() ? lang.getString("opt_nightfreecooling", "UnitStatus"+bean.getUnitOnOff()) : "***"%></span></td>
		<td class="td" width="<%=w2%>">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%if( bean.getSlaveNo() >= 1 ) {%><%=lang.getString("opt_startstop", "Slave")%> 1 - <%=lang.getString("opt_startstop", "status")%></span><%} else {%>&nbsp;<%}%></td>
		<td class="td"><%if( bean.getSlaveNo() >= 1 ) {%><span id="spanUnitOnOff1" class="standardTxtBold"><%=bean.isEnabled() && bean.getSlaveNo() >= 1 ? lang.getString("opt_startstop", "UnitStatus"+bean.getUnitOnOff(1)) : "&nbsp;***"%></span><%} else {%>&nbsp;<%}%></td>
		<td class="td" width="<%=w1%>">&nbsp;</td>
	</tr>
	<tr class="Row1" style="height:30px">
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "HumiditySetpoint")%></span></td>
		<td class="td"><span id="spanHumiditySetpoint" class="standardTxtBold"><%=bean.isEnabled() ? bean.getHumiditySetpoint() : "***"%></span> <%=lang.getString("opt_nightfreecooling", "percentage")%></td>
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "InternalHumidity")%></span></td>
		<td class="td"><span id="spanInternalHumidity" class="standardTxtBold"><%=bean.isEnabled() ? bean.getInternalHumidity() : "***"%></span> <%=lang.getString("opt_nightfreecooling", "percentage")%></td>
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "ExternalHumidity")%></span></td>
		<td class="td"><span id="spanExternalHumidity" class="standardTxtBold"><%=bean.isEnabled() ? bean.getExternalHumidity() : "***"%></span> <%=lang.getString("opt_nightfreecooling", "percentage")%></td>
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "TimeOn")%></span></td>
		<td class="td"><table><tr>
			<td class="standardTxt">
				<span id="spanTimeOn"><%=(bean.isEnabled() && bean.existAnticipatedStartTime() && !bean.isOutsideWorkingDates()) ? strComputedTimeOn : "***"%></span>
			</td>
			<td>
				<%if (bean.isEnabled() && bean.existAnticipatedStartTime()) {
					if( bean.isStartCalcInProgress() ) { %><img src="images/optimum/calcinprogress.gif">
				<%} else {%>
					<img src="<%=bean.isStartVentilation() ? "images/optimum/ok.gif" : "images/optimum/not_ok.gif"%>">
				<%} }%>
			</td>
		</tr></table></td>
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%if( bean.getSlaveNo() >= 2 ) {%><%=lang.getString("opt_startstop", "Slave")%> 2 - <%=lang.getString("opt_startstop", "status")%></span><%} else {%>&nbsp;<%}%></td>
		<td class="td"><span id="spanUnitOnOff2" class="standardTxtBold"><%if( bean.getSlaveNo() >= 2 ) {%><%=bean.isEnabled() && bean.getSlaveNo() >= 2 ? lang.getString("opt_startstop", "UnitStatus"+bean.getUnitOnOff(2)) : "&nbsp;***"%></span><%} else {%>&nbsp;<%}%></td>
		<td class="td">&nbsp;</td>
	</tr>
	<tr class="Row1" style="height:30px">
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "EnthalpySetpoint")%></span></td>
		<td class="td"><span id="spanEnthalpySetpoint" class="standardTxtBold"><%=bean.isEnabled() ? bean.getEnthalpySetpoint() : "***"%></span> <%=lang.getString("opt_nightfreecooling", "KJ_Kg")%></td>
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "InternalEnthalpy")%></span></td>
		<td class="td"><span id="spanInternalEnthalpy" class="standardTxtBold"><%=bean.isEnabled() ? bean.getInternalEnthalpy() : "***"%></span> <%=lang.getString("opt_nightfreecooling", "KJ_Kg")%></td>
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "ExternalEnthalpy")%></span></td>
		<td class="td"><span id="spanExternalEnthalpy" class="standardTxtBold"><%=bean.isEnabled() ? bean.getExternalEnthalpy() : "***"%></span> <%=lang.getString("opt_nightfreecooling", "KJ_Kg")%></td>
		<td class="td">&nbsp;</td>
		<td class="td"><span class="standardTxt"><%=lang.getString("opt_nightfreecooling", "TimeOff")%></span></td>
		<td class="td"><table><tr><td class="standardTxt"><span id="spanTimeOff"><%=(bean.isEnabled() && !bean.isOutsideWorkingDates() && (bean.existsSunriseTime() || !bean.isSunriseSchedule())) ? strTimeOff : "***"%></span></td><td><% if (bean.isEnabled() && bean.isSunriseSchedule()) { %> &nbsp;<img src="images/optimum/sunrise.png"> <% } %></td></tr></table>
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
	String title = lang.getString("opt_nightfreecooling", "last_week")
		+ ": "
		+ calBegin.get(Calendar.DAY_OF_MONTH) + " " + astrMonthNames[calBegin.get(Calendar.MONTH)]
		+ " - "
		+ calEnd.get(Calendar.DAY_OF_MONTH) + " " + astrMonthNames[calEnd.get(Calendar.MONTH)]
		+ ", "
		+ calBegin.get(Calendar.YEAR);
	DefaultCategoryDataset datasetA = (DefaultCategoryDataset)ut.getAttribute("datasetA");
	DefaultCategoryDataset datasetB = (DefaultCategoryDataset)ut.getAttribute("datasetB");
%>
<br>&nbsp;
<table border="0" cellpadding="0" cellspacing="0" width="<%=sessionUser.getScreenWidth() - 50%>" style="border:3px solid #000000">
	<tr>
		<td width="*" align="center" class="standardTxt">
			<%=title%>
		</td>
	</tr>
	<tr>
		<td><img id="imgPlotLine" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?charttype=linechart_optimum&width=<%=sessionUser.getScreenWidth() - 55%>&height=250&imgid=datasetA&xlabel=<%=xlabel%>&um=<%=lang.getString("opt_nightfreecooling", "KJ_Kg")%>"></td>
	</tr>
	<tr>
		<td><img id="imgPlotBar" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?charttype=simplebarchart&width=<%=sessionUser.getScreenWidth() - 55%>&height=250&imgid=datasetB&xlabel=<%=xlabel%>&series0color=00FF00&um=<%=lang.getString("opt_nightfreecooling", "minutes")%>"></td>
	</tr>
</table>
<%} else {%>
<script language="javascript">
onDashboardReport();
</script>
<%}%>

<% } else { %>
<input id="Configured" name="Configured" type="hidden" value="false">
<p> <h4 align="center"><%=lang.getString("opt_nightfreecooling", "noconf")%></h4></p>
<% } %>
