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
import="com.carel.supervisor.plugin.co2.*"
import="com.carel.supervisor.presentation.co2.*"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();
boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();

RackBean[] racks = RackBean.retrieveRacks(language);
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<%if( racks.length > 0 ) {%>
<input type='hidden' id='confirm' value='<%=lang.getString("co2", "confirm")%>'>
<form id="formRacks" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<%=RackBean.getHTMLTable(racks, language)%>
</form>
<%} else {%>
	<p class="mediumTxt">
		<b><%=lang.getString("co2", "norackavailable")%></b>
	</p>
<%}%>