<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.controller.database.TimeBandList"
import="com.carel.supervisor.controller.database.TimeBandBean"
import="com.carel.supervisor.controller.database.RuleBean"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.bean.rule.RuleBeanHelper"
import="com.carel.supervisor.dataaccess.datalog.impl.ConditionBeanList"
import="com.carel.supervisor.dataaccess.datalog.impl.ConditionBean"
import="com.carel.supervisor.presentation.bean.rule.ActionBeanList"
import="com.carel.supervisor.presentation.bean.ProfileBean"
import="java.util.*"
%>
<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();

String comment = "";
comment = lan.getString("alrsched","schedulerdash_comment");

//controllo permessi	
	String perm = "";
	int permission = sessionUser.getVariableFilter();
	if (permission==0)
		perm = "disabled";

	boolean protect = (permission==0)?true:false;

//TABELLA REGOLE
boolean isScheduled = false;
boolean isEnabled = true;
RuleBeanHelper ruleList = new RuleBeanHelper(new Integer(idsite), isScheduled, isEnabled);
Map ids = ruleList.getIds();

ruleList.setScreenH(sessionUser.getScreenHeight());
ruleList.setScreenW(sessionUser.getScreenWidth());
String ruletable = ruleList.getScheculerDashboardHTMLtable(idsite, BaseConfig.getPlantId(),sessionUser,"",140, 900, isScheduled,protect); 
String arg = ruleList.getTimeBandArg();

String monday=lan.getString("timebandsPage","monday");
String tuesday=lan.getString("timebandsPage","tuesday");
String wednesday=lan.getString("timebandsPage","wednesday");
String thursday=lan.getString("timebandsPage","thursday");
String friday=lan.getString("timebandsPage","friday");
String saturday=lan.getString("timebandsPage","saturday");
String sunday=lan.getString("timebandsPage","sunday");
%>
<script language='javascript'>
var arg = '<%=arg%>';
var weekDesc = '<%=monday%>;<%=tuesday%>;<%=wednesday%>;<%=thursday%>;<%=friday%>;<%=saturday%>;<%=sunday%>';
</script>
<p class="standardTxt"><%=comment%></p>
<table border="0" width="100%" height="90%" cellspacing="1" cellpadding="1">
<tr height="100%" valign="top" id="trSchedulerDashboardList">
		<td><%=ruletable%></td>
</tr>
</table>


	

