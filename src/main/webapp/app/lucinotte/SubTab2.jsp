<%@ page language="java"
import="java.util.*"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.lucinotte.LNGroups"
import="com.carel.supervisor.presentation.lucinotte.LNDevsGroup"
%>

<%
UserSession sessUsr = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);

int idsite= sessUsr.getIdSite();

String language = sessUsr.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);

String jsession = request.getSession().getId();

String legenda = LNGroups.getLNGroupsLegend(language);
String table = LNDevsGroup.getLNDevsxGroup(language, idsite, sessUsr.getScreenHeight(), sessUsr.getScreenWidth());
%>

<input type="hidden" id="selectallradio" value="<%=lan.getString("lucinotte","selectallradio")%>" />
<input type="hidden" id="deselectallradio" value="<%=lan.getString("lucinotte","deselectallradio")%>" />
<input type="hidden" id="selectallluci" value="<%=lan.getString("lucinotte","selectallluci")%>" />
<input type="hidden" id="selectallnotte" value="<%=lan.getString("lucinotte","selectallnotte")%>" />
<input type="hidden" id="allradio2na" value="<%=lan.getString("lucinotte","allradio2na")%>" />
<input type="hidden" id="insgrpname" value="<%=lan.getString("lucinotte","insgrpname")%>" />
<input type="hidden" id="implcnt" value="<%=lan.getString("lucinotte","implcnt")%>" />
<!-- Alessandro : default value for the virtual keyboard type -->
<input type='hidden' id='vkeytype' value='PVPro' />

<p class='standardTxt'><%=lan.getString("lucinotte","comment2")%></p>	
<FORM name="lcnt_frm" id="lcnt_frm" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
	<input type='hidden' id='cmd' name='cmd'/>
	<FIELDSET class='field' style='width:97%'>
		<LEGEND class="standardTxt">&nbsp;<span><%=lan.getString("ac","legend")%></span>&nbsp;
		</LEGEND>
			<%=legenda%>
	</FIELDSET>
	<br /><br />
	<%=table%>
</FORM>