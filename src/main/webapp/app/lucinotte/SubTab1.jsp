<%@ page language="java" 
import="java.util.*"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.session.Transaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.lucinotte.LNGroups"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();

int idsite= sessionUser.getIdSite();

String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);

String jsession = request.getSession().getId();

String noemptyname = lan.getString("lucinotte","noemptyname");
String grpstr = lan.getString("lucinotte","gruppo");
String linkname = lan.getString("lcnt_grpdtl", "link");

String table = LNGroups.getLNGroupsTable(language);

transaction.setSystemParameter(null);
%>
<input type="hidden" id="noemptyname" value="<%=noemptyname%>"/>
<input type="hidden" id="groupstr" value="<%=grpstr%>"/>
<input type="hidden" id="linkname" value="<%=linkname%>"/>

<p class="standardTxt"><%=lan.getString("lucinotte","comment1")%></p>	
<br />
<FORM name="lcnt_frm" id="lcnt_frm" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
	<input type="hidden" id="cmd" name="cmd" value=""/>
	<%=table%>
</FORM>

<FORM name="execmd_frm" id="execmd_frm" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="execmd" name="execmd" value=""/>
<input type="hidden" id="exegrp" name="exegrp" value=""/>
</FORM>
