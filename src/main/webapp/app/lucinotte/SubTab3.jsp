<%@ page language="java" 
import="java.util.*"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.lucinotte.LNField"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);

int idsite= sessionUser.getIdSite();

String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);

String jsession = request.getSession().getId();

String table = LNField.getHtmlFieldConf(language, idsite);
%>
<input type="hidden" id="ctrlval" value="<%=lan.getString("lucinotte","ctrlval")%>" />
<p class='standardTxt'><%=lan.getString("lucinotte","comment3")%></p>	

<FORM name="lcnt_frm" id="lcnt_frm" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
	<input type='hidden' id='cmd' name='cmd'/>
	<%=table%>
</FORM>
