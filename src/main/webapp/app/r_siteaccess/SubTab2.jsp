<%@ page language="java" pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.bo.BRSiteAcces"
	import="com.carel.supervisor.dataaccess.dataconfig.SiteInfo"
	import="com.carel.supervisor.base.conversion.DateUtils"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
%>
<%@ page import="com.carel.supervisor.base.conversion.DateUtils" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.carel.supervisor.presentation.session.UserTransaction" %>
<%@ page import="com.carel.supervisor.presentation.polling.PollingStatusMrg" %>
<%
	
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();	
	String jsession = request.getSession().getId();
	
	LangService lan = LangMgr.getInstance().getLangService(language);
	
	String ledrosso = lan.getString("rmpollingstatus","red");
	String ledblu = lan.getString("rmpollingstatus","blue");
	String ledgrigio = lan.getString("rmpollingstatus","gray");
	String ledverde = lan.getString("rmpollingstatus","green");
	String data = lan.getString("rmpollingstatus","date");
	String sitename = lan.getString("rmtimesite","sitename");
	String fascianame = lan.getString("rmtimetable","fascianame");
	String slot = lan.getString("rmtimetable","signal");
	String motorefermo = lan.getString("rmtimetable","stopengineplugin");
	String noSiteConfig = lan.getString("rmtimetable","nositeconfig");

  
  String tableHTML = PollingStatusMrg.getInstance().createHTMLTable(motorefermo,noSiteConfig);

  String now = DateUtils.date2String(Calendar.getInstance().getTime(),"yyyy-MM-dd");
  
%>
<script type="text/javascript">
function doRefresh() {  // refresh della pagina corrente  
	document.location.reload();
}	
// imposto il timeout per il prossimo refresh// espresso in millisecondi (1000 = 1 secondo)
	window.setTimeout("doRefresh();", 1000*60);
</script>
<div id="contlistsite" style="height:360px;overflow:auto;">
<table name="legendaheader" class='table' border="0" width="100%" cellspacing="1" cellpadding="1">
	<TR>
		<td width="3%" class="standardTxt" align="center"><img src=images/led/L2.gif></td>
		<td class="standardTxt"><b><%=ledrosso%></td>
		<td width="3%" class="standardTxt" align="center"><img src=images/led/L3.gif></td>
		<td class="standardTxt"><b><%=ledblu%></b></td>
		<td width="30%" class="standardTxt" align="center"><b><%=data%>&nbsp;&nbsp;<%=now%></td>
	</TR>
	<tr>	
		<td width="3%" class="standardTxt" align="center"><img src=images/led/L0.gif></td>
		<td class="standardTxt"><b><%=ledgrigio%></b></td>
		<td width="3%" class="standardTxt" align="center"><img src=images/led/L1.gif></td>
		<td class="standardTxt"><b><%=ledverde%></b></td>
		<td width="30%" class="standardTxt" align="center">&nbsp;</td>
	</tr>
</table>
<br>

<table name="listasitiheader" border="0" width="100%" cellspacing="1" cellpadding="1">
	<tr>
		<td width="40%" class="standardTxt" align="center" colspan="2"><b><%=sitename%></b></td>
		<td width="29%" class="standardTxt" align="center"><b><%=fascianame%></td>
		<td width="8%" class="standardTxt" align="center"><b><%=slot%><br>1</b></td>
		<td width="8%" class="standardTxt" align="center"><b><%=slot%><br>2</b></td>
		<td width="8%" class="standardTxt" align="center"><b><%=slot%><br>3</b></td>
		<td width="8%" class="standardTxt" align="center"><b><%=slot%><br>4</b></td>
	</tr>
</table>

<table name="listasiti" border="0" width="100%" cellspacing="1" cellpadding="1">
<%= tableHTML%>
</table>
</div>
<SCRIPT type="text/javascript">
<!--
	var thisbodyx,thisbodyy;
	if (self.innerHeight) // all except Explorer
	{
		thisbodyx = self.innerWidth;
		thisbodyy = self.innerHeight;
	}
	else if (document.documentElement && document.documentElement.clientHeight) // Explorer 6 Strict Mode
	{
		thisbodyx = document.documentElement.clientWidth;
		thisbodyy = document.documentElement.clientHeight;
	}
	else if (document.body) // other Explorers
	{
		thisbodyx = document.body.clientWidth;
		thisbodyy = document.body.clientHeight;
	}
	document.getElementById('contlistsite').style.height = thisbodyy-130;
//-->
</SCRIPT>