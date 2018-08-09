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
String classInput = bOnScreenKey ? "keyboardInput" : "strandardTxt";
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
String selectedBooklet = ut.getProperty("idbooklet");
SiteBookletBean bean = new SiteBookletBean(Integer.parseInt(selectedBooklet));
bean.loadSiteType();
String strPage = ut.getProperty("page");
int selectedPage = strPage != null && !strPage.isEmpty() ? Integer.parseInt(strPage) : 3;
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro'>":""%>
<input type='hidden' id='documentPath' value='<%=documentPath%>'>
<input type='hidden' id='booklet' value='<%=bean.isBooklet()%>'>
<input type='hidden' id='reset' value='<%=bean.isReset()%>'>
<input type='hidden' id='confirm' value='<%=lang.getString("sitebooklet", "confirm")%>'>
<input type='hidden' id='limit2000' value='<%=lang.getString("sitebooklet", "limit2000")%>'>
<input type='hidden' id='required_fields' value='<%=lang.getString("r_sitelist", "selectallfields")%>'>
<input type='hidden' id='secondary_fluid' value='<%=bean.isSecondaryFluid()%>'>
<table class="standardTxt" width="99%">
	<tr>
		<td><%=lang.getString("sitebooklet", "page_selection")%></td>
		<td>
			<select name="selectPage" onChange="onPageSelect(this.value)" <%=bean.isBooklet() ? "" : "disabled"%>>
				<option value="3" <%=selectedPage == 3 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page3")%></option>
				<option value="4" <%=selectedPage == 4 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page4")%></option>
				<option value="5" <%=selectedPage == 5 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page5")%></option>
				<option value="6" <%=selectedPage == 6 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page6")%></option>
				<option value="7" <%=selectedPage == 7 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page7")%></option>
				<option value="8" <%=selectedPage == 8 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page8")%></option>
<%if( bean.isSecondaryFluid() ) {%>
				<option value="9" <%=selectedPage == 9 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page9")%></option>
<%}%>
				<option value="10" <%=selectedPage == 10 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page10")%></option>
				<option value="11" <%=selectedPage == 11 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page11")%></option>
				<option value="12" <%=selectedPage == 12 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page12")%></option>
				<option value="26" <%=selectedPage == 26 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page26")%></option>
				<option value="30" <%=selectedPage == 30 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page30")%></option>
				<option value="31" <%=selectedPage == 31 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page31")%></option>
				<option value="71" <%=selectedPage == 71 ? "selected" : ""%>><%=lang.getString("sitebooklet", "page71")%></option>
			</select>
		</td>
		<td width="*" align="right">
			<%=bean.isBooklet() ? (bean.isReset() ? "<img src='images/actions/reset_on_black.png'>" : "&nbsp;") : "<img src='images/actions/Edit_on_black.png'>"%>
		</td>
	</tr>
</table>

<form id="frm_site_booklet" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="idsite" name="idsite" value="<%=selectedBooklet%>">
<input type="hidden" id="page" name="page" value="<%=selectedPage%>">
<p align="center">
<%switch( selectedPage ) {
	case 3:%>
		<jsp:include page="page3.jsp" flush="true" />
<%	break; case 4: %>		
		<jsp:include page="page4.jsp" flush="true" />
<%	break; case 5: %>		
		<jsp:include page="page5.jsp" flush="true" />
<%	break; case 6: %>		
		<jsp:include page="page6.jsp" flush="true" />
<%	break; case 7: %>		
		<jsp:include page="page7.jsp" flush="true" />
<%	break; case 8: %>		
		<jsp:include page="page8.jsp" flush="true" />
<%	break; case 9: %>		
		<jsp:include page="page9.jsp" flush="true" />
<%	break; case 10: %>		
		<jsp:include page="page10.jsp" flush="true" />
<%	break; case 11: %>		
		<jsp:include page="page11.jsp" flush="true" />
<%	break; case 12: %>		
		<jsp:include page="page12.jsp" flush="true" />
<%	break; case 26: %>		
		<jsp:include page="page26.jsp" flush="true" />
<%	break; case 30: %>		
		<jsp:include page="page30.jsp" flush="true" />
<%	break; case 31: %>		
		<jsp:include page="page31.jsp" flush="true" />
<%	break; case 71: %>		
		<jsp:include page="page71.jsp" flush="true" />
<%  break; } // switch%>
</p>	
</form>

<form id="frm_page_select" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="new_page" name="new_page">
</form>
