<%@ page language="java"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.session.Transaction"
import="com.carel.supervisor.dataaccess.language.LangMgr"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
boolean isProtected = ut.isTabProtected();
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
%>

<script language="javascript">
// external initialization (requested from flash obj)
function VisualSchedulerInit()
{
	var obj = document.objVisualScheduler;

	// base url (used to get external dependencies)
	obj.SetFlashURL("flash/scheduler/");
	// data url
	obj.SetDataURL("flash/scheduler/VisualScheduler.xml");
	obj.SetSrvrURL("servlet/master;jsessionid=<%=jsession%>");
	// language
	var lang = "<%=language%>".split("_");
	var code = lang.length == 2 ? lang[1] : "en";
	obj.SetLanguage(code);

	enableAction(1); // save action
}

function save_schedule()
{
	var obj = document.objVisualScheduler;
	obj.SaveData();
}
</script>
<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0" width="900" height="600" id="objVisualScheduler" align="middle">
<param name="allowScriptAccess" value="always" />
<param name="allowFullScreen" value="false" />
<param name="movie" value="flash/scheduler/VisualScheduler.swf" /><param name="menu" value="false" /><param name="quality" value="high" /><param name="bgcolor" value="#ffffff" />
<param name="wmode" value="transparent">
<embed src="flash/scheduler/VisualScheduler.swf" menu="false" quality="high" bgcolor="#ffffff" width="900" height="600" name="objVisualScheduler" align="middle" allowScriptAccess="sameDomain" allowFullScreen="false" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" wmode="transparent"/>
</object>
