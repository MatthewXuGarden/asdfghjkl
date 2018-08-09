<%@ page language="java" 
import="java.util.*"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.dbllistbox.*"
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

// group
Integer idGroup = (Integer)ut.removeAttribute("idGroup");
if( idGroup == null )
	idGroup = 0;
GroupBean group = new GroupBean(idGroup);
group.loadGroup();
// utilities
UtilityBean[] utilities = UtilityBean.retrieveUtilities(language);
ArrayList leftUtilities = new ArrayList();
ArrayList rightUtilities = new ArrayList();
for(int i = 0; i < utilities.length; i++) {
	UtilityBean utility = utilities[i];
	if( utility.getIdGroup() == 0 )
		leftUtilities.add(new ListBoxElement(utility.getDescription(), String.valueOf(utility.getIdDevice())));
	else if( idGroup > 0 && utility.getIdGroup() == idGroup )
		rightUtilities.add(new ListBoxElement(utility.getDescription(), String.valueOf(utility.getIdDevice())));
}
DblListBox dblListBox = new DblListBox(leftUtilities, rightUtilities, false, true, true, null, true);
dblListBox.setHeaderTable1(lang.getString("co2", "co2_devices"));
dblListBox.setHeaderTable2(lang.getString("co2", "group_devices"));
dblListBox.setScreenW(sessionUser.getScreenWidth());
dblListBox.setScreenH(sessionUser.getScreenHeight());
dblListBox.setWidthListBox(400);
dblListBox.setHeight(450);
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<%if( utilities.length > 0 ) {%>
<input type='hidden' id='confirm' value='<%=lang.getString("co2", "confirm")%>'>
<input type='hidden' id='alert_req_var' value='<%=lang.getString("co2", "alert_req_var")%>'>
<input type='hidden' id='alert_group_empty' value='<%=lang.getString("co2", "alert_group_empty")%>'>
<fieldset class='field' style="width: 97%;">		
<legend class='standardTxt'><%=lang.getString("co2", "group_config")%></legend>

<%=GroupBean.getHTMLGroupTable(sessionUser)%>
</fieldset>

<form id="formGroup" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd">
<input type="hidden" id="idGroup" name="idGroup" value="<%=idGroup%>">
<input type="hidden" id="utilities" name="utilities">
<table class="table" width="98%">
	<tr>
		<tr><td style="font-size:4pt;">&nbsp;</td></tr>
		<td class="standardTxt" width="45%"><%=lang.getString("co2", "group_name")%>&nbsp;
			<input id="name" name="name" type="text" size="50" maxlength="64" value="<%=group.getName()%>" class="<%=(bOnScreenKey ? "keyboardInput" : "standardTxt")%>">
		</td>
		<td class="standardTxt" width="15%"><%=lang.getString("co2", "enabled")%>&nbsp;
			<input id="enabled" name="enabled" type="checkbox" <%=group.isEnabled() ? "checked" : ""%>>
		</td>
		<td class="standardTxt" width="*">&nbsp;</td>		
	</tr>
</table>
</form>

<fieldset class='field' style="width: 97%;">
<legend class='standardTxt'><%=lang.getString("co2", "group_settings")%></legend>
<table width="100%">
 <tr>
	<td height="300px">
		<%=dblListBox.getHtmlDblListBox()%>
	</td>
 </tr>
</table>
</fieldset>

<%} else {%>
	<p class="mediumTxt">
		<b><%=lang.getString("co2", "noutilityavailable")%></b>
	</p>
<%}%>