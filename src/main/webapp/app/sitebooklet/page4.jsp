<%@ page language="java"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.session.Transaction"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.presentation.bean.SiteBookletBean"	
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
boolean isProtected = ut.isTabProtected();
String jsession = request.getSession().getId();
boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
String classInput = bOnScreenKey ? "keyboardInput" : "standardTxt";
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
SiteBookletBean bean = new SiteBookletBean(Integer.parseInt(ut.getProperty("idbooklet")));
bean.loadInstructions();
boolean readonly = bean.isInstructionsReadOnly();
String strReadOnly = readonly ? "readonly" : "";
if( !readonly && bean.text.isEmpty() ) {
	bean.text += "   " + lang.getString("sitebooklet", "text_1") + "\n\n";
	bean.text += "   " + lang.getString("sitebooklet", "text_2") + "\n\n";
	bean.text += "   " + lang.getString("sitebooklet", "text_3") + "\n\n";
	bean.text += "   " + lang.getString("sitebooklet", "text_4") + "\n\n";
	bean.text += "   " + lang.getString("sitebooklet", "text_5") + "\n\n";
	bean.text += "   " + lang.getString("sitebooklet", "text_6") + "\n\n";
	bean.text += "   " + lang.getString("sitebooklet", "text_7") + "\n\n";
	bean.text += "   " + lang.getString("sitebooklet", "text_8") + "\n\n";
	bean.text += "   " + lang.getString("sitebooklet", "text_9") + "\n\n";
	bean.text += "   " + lang.getString("sitebooklet", "text_10");
}
%>
<input id="readonly" type="hidden" value="<%=readonly%>">

<table class="sbtable">
	<tr><th><h2><%=lang.getString("sitebooklet", "instructions")%></h2></th></tr>
	<tr><td align="center">
		<textarea rows="30" cols="120" name="text" class="<%=classInput%>" <%=strReadOnly%>><%=bean.text%></textarea>
	</td></tr>
</table>
