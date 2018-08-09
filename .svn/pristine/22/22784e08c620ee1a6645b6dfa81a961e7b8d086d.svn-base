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
String documentPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/app/sitebooklet/SiteBooklet.jsp;jsessionid=" + jsession; 
boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
String classInput = bOnScreenKey ? "keyboardInput" : "standardTxt";
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
Integer idNewBooklet = (Integer)ut.removeAttribute("idNewBooklet");
%>
<%if( idNewBooklet == null) {%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro'>":""%>
<input type='hidden' id='confirm' value='<%=lang.getString("sitebooklet", "confirm")%>'>
<input type='hidden' id='required_fields' value='<%=lang.getString("r_sitelist", "selectallfields")%>'>

<%=SiteBookletBean.getHtmlCatalog(language, sessionUser.getScreenWidth(), sessionUser.getScreenHeight())%>

<div id="divCatalogEntry" style="display:none;">
<form id="frm_site_booklet_cat" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input id="cmd" name="cmd" type="hidden">
<input id="idsite" name="idsite" type="hidden">
<fieldset class="field"><legend class="standardTxt"><%=lang.getString("sitebooklet", "sitebooklet")%></legend>
<table class="table">
<tr>
	<td class="td" width="30%">
		<%=lang.getString("sitebooklet_cat", "name")%>:&nbsp;
		<input size="32" maxlength="64" id="name" name="name" class="<%=classInput%>">
	</td>
	<td class="td">
		<%=lang.getString("sitebooklet_cat", "description")%>:&nbsp;
		<input size="80" maxlength="256" id="description" name="description" class="<%=classInput%>">
	</td>	
</tr>
</table>
</fieldset>
</form>
</div>
<%} else {%>
<input type='hidden' id='idNewBooklet' value='<%=idNewBooklet%>'>
<%}%>
