<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.bo.BRDataTransfer"
%>

<%	

UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);

String language = sessionUser.getLanguage();
LangService l = LangMgr.getInstance().getLangService(language);

String txt = ((BRDataTransfer)sessionUser.getCurrentUserTransaction().getBoTrx()).readFromLogFile();

%>


<html>
<body>
	<table style='width: 100%;height: 100%'>
	<tr style='width: 100%;height: 100%'><td style='width: 100%;height: 100%'>
	<!-- TEXTAREA COLS=100 ROWS=23 wrap=off readonly onfocus="unlockModUser();" onblur="unlockModUser();"><%=txt %></TEXTAREA -->
	<TEXTAREA wrap=off readonly onfocus="unlockModUser();" onblur="unlockModUser();"  style='width: 100%;height: 100%'><%=txt %></TEXTAREA>
	
	<!-- <input type='textarea' style='width:750px;height:350px;' value='<%=txt %>'>-->
	</td></tr>
	</table>
</body>
</html>
