<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUserLoc = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	String msg = multiLanguage.getString("usrmsg","msg01");
%>
<table border="0" width="100%" cellspacing="3" cellpadding="3">
	<tr>
		<td style="height:10px"></td>
	</tr>
	<tr>
		<td class="th" style="padding-left: 20px;" align="center"><%= msg%></td>
	</tr>
</table>