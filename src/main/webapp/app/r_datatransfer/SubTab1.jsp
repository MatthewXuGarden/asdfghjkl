<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.dataaccess.dataconfig.*"
	import="com.carel.supervisor.presentation.bo.BRDataTransfer"
%>

<%	

UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
LangService l = LangMgr.getInstance().getLangService(language);

BRDataTransfer.setScreenH(sessionUser.getScreenHeight());
BRDataTransfer.setScreenW(sessionUser.getScreenWidth());
String table = ((BRDataTransfer)sessionUser.getCurrentUserTransaction().getBoTrx()).getSiteTable(language);
%>
<table border="0" width="100%" height="93%" cellspacing="1" cellpadding="1">
<tr><td class='standardTxt'>
	<%=l.getString("r_datatransfer","tab1comment")%>
	</td>
</tr>
<tr><td>&nbsp;</td></tr>
<tr height="100%" valign="top" id="trSiteList"><td>	
	<form action="servlet/master;jsessionid=<%=jsession%>" method="post" id='site_form' name='site_form'>
		<%=table%>
	</form>
	</td>
</tr>
</table>
