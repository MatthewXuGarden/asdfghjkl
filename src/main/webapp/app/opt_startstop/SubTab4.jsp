<%@ page language="java"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.session.Transaction"
import="com.carel.supervisor.dataaccess.language.*"
import="com.carel.supervisor.plugin.optimum.*"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
boolean isProtected = ut.isTabProtected();
String jsession = request.getSession().getId();
boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
Integer idAlgorithm = (Integer)ut.getAttribute("idalg");
if( idAlgorithm == null )
	idAlgorithm = 1;
StartStopBean bean = OptimumManager.getInstance().getStartStop(idAlgorithm);
boolean bAdvancedSchedule = bean.isAdvancedSchedule();
%>

<script language="javascript">
// external initialization (executed onLoad)
function OptimumSchedulerInit()
{
	if( !<%=bAdvancedSchedule%> )
		return;
	
	obj = document.objVisualScheduler;
	if( !obj || !obj.LogLevel ) {
		setTimeout(OptimumSchedulerInit, 250);
		return;
	}
	VisualSchedulerWait(true);
	// log level (n - none, i - info, w - warning, e - error)
	obj.LogLevel("e");
	// preload dependencies
	obj.SetCache(false);
	// max number of intervals
	obj.SetIntervalNo(2);
	// hide dashboard
	obj.SetDashboard(false);
	// category
	obj.SelectCategory("<%=bean.getCategoryId()%>");
	// srvr/data url
	obj.SetSrvrURL("servlet/vs;jsessionid=<%=jsession%>?app=opt_startstop");
	obj.SetDataURL("servlet/vs;jsessionid=<%=jsession%>?app=opt_startstop");
	// skin url (used to get external bitmaps)
	obj.SetSkinURL("images/scheduler/skin/");
	// symbol url (used to get category symbols)
	obj.SetSymbolURL("images/scheduler/symbols/");
	// language
	var lang = "<%=language%>".split("_");
	var code = lang.length == 2 ? lang[1] : "en";
	obj.SetLanguage("flash/scheduler/", code);
	// virtual keyboard
	obj.VirtualKeyboard(<%=bOnScreenKey%>);
	// enable actions
	enableAction(1);
}
// override to check the object save state
function MioAskModUser()
{
	if( obj && obj.IsDirty() ) {
		var msg = document.getElementById("msg03").value;
		return confirm(msg);
	}
	return true;
}
</script>

<%if( bAdvancedSchedule ) {%>
<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0" width="900" height="600" id="objVisualScheduler" align="middle">
<param name="allowScriptAccess" value="always" />
<param name="allowFullScreen" value="false" />
<param name="movie" value="flash/scheduler/VisualScheduler.swf" /><param name="menu" value="false" /><param name="quality" value="high" /><param name="bgcolor" value="#ffffff" />
<param name="wmode" value="transparent">
<embed src="flash/scheduler/VisualScheduler.swf" menu="false" quality="high" bgcolor="#ffffff" width="900" height="600" name="objVisualScheduler" align="middle" allowScriptAccess="sameDomain" allowFullScreen="false" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" wmode="transparent"/>
</object>
<div id="layerVK" style="position:absolute;left:20px;top:40px;width:0px;height:0px;background-color:transparent;display:<%=bOnScreenKey ? "block" : "none"%>;">
</div>
<%} else {%>
<p class='standardTxt'><%=lang.getString("opt_startstop", "no_advanced_schedule")%></p>
<%}%>
