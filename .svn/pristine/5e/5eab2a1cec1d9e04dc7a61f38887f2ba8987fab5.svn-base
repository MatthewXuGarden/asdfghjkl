<%@ page language="java"%>
<%@page import="com.carel.supervisor.presentation.session.*"%>
<%@page import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%@page import="java.util.*"%>
<%@page import="com.carel.supervisor.plugin.base.Plugin"%>
<%@page import="com.carel.supervisor.presentation.bean.*"%>
<%@page import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBean"%>
<%@page import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
int idsite= sessionUser.getIdSite();
String jsession = request.getSession().getId();	
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
ClockBeanList clockBeanList = new ClockBeanList();
String deviceCombo = clockBeanList.getDeviceCombo(language);
%>
<script type="text/javascript" src="mobile/scripts/jquery/jquery-1.6.4.min.js"></script>
<style>
#editDiv{margin-top:10px;display:none;}
.divrow{height:22px; margin-bottom:8px;}
.label{float:left;width:150px;text-align:left;margin-right:10px;height: 22px;}
.dev .comboDiv select{width:400px;}
.dev .labelDiv select{width:400px;}
.divFloat{float:left;}
.combo{vertical-align: top;position: relative;height: 22px;width:410px;}
.combo .comboDiv select{width:400px;}
.comboDiv{display:none;}
.addbtn{margin-left:50px;}
.btn{display:absolute;height: 22px;width:30px;}
.descr{display: inline-block;width:10px;height: 22px;line-height: 22px;vertical-align: top;margin-left:100px;position: relative;}
</style>
<input type='hidden' id='confirm' value='<%=lang.getString("energy","confirm")%>'>
<input type='hidden' id='masterLabel' value='<%=lang.getString("clock","master")%>'>
<input type='hidden' id='yearLabel' value='<%=lang.getString("clock","year")%>'>
<input type='hidden' id='monthLabel' value='<%=lang.getString("clock","month")%>'>
<input type='hidden' id='dayLabel' value='<%=lang.getString("clock","day")%>'>
<input type='hidden' id='weekdayLabel' value='<%=lang.getString("clock","weekday")%>'>
<input type='hidden' id='hourLabel' value='<%=lang.getString("clock","hour")%>'>
<input type='hidden' id='minuteLabel' value='<%=lang.getString("clock","minute")%>'>
<input type='hidden' id='atleastone' value='<%=lang.getString("devdetail","atleastone")%>'>
<%=clockBeanList.getHtmlModelTable(sessionUser) %>
<div id="editDiv">
<FORM name="clock_frm" id="clock_frm" action="servlet/master;jsessionid=<%=jsession%>" method="post">
	<input type='hidden' id='cmd' name='cmd' value=""/>
	<input id="iddevmdl" name="iddevmdl" type="hidden" value="">
	<div class="divrow">
		<span class="label standardTxt"><%=lang.getString("clock","device") %></span>
		<div class="dev combo standardTxt divFloat">
			<div class="comboDiv"><%=deviceCombo%></div><div class="labelDiv"></div>
		</div>
	</div>
</FORM>
</div>

