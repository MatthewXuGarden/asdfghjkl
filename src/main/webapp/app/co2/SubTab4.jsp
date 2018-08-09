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

// rack
Integer idRack = (Integer)ut.removeAttribute("idRack");
if( idRack == null )
	idRack = 0;
String rackName = "";
RackBean[] racks = RackBean.retrieveRacksSelected(language);
for(int i = 0; i < racks.length; i++)
	if( racks[i].getIdRack() == idRack )
		rackName = racks[i].getDescription();
GroupBean[] groups = GroupBean.retrieveGroups();
// groups
ArrayList leftGroups = new ArrayList();
ArrayList rightGroups = new ArrayList();
for(int i = 0; i < groups.length; i++) {
	GroupBean group = groups[i];
	if( group.getIdRack() == 0 )
		leftGroups.add(new ListBoxElement(group.getName(), String.valueOf(group.getIdGroup())));
	else if( idRack > 0 && group.getIdRack() == idRack )
		rightGroups.add(new ListBoxElement(group.getName(), String.valueOf(group.getIdGroup())));
}
DblListBox dblListBox = new DblListBox(leftGroups, rightGroups, false, true, true, null, true);
dblListBox.setHeaderTable1(lang.getString("co2", "all_groups"));
dblListBox.setHeaderTable2(lang.getString("co2", "rack_groups"));
dblListBox.setScreenW(sessionUser.getScreenWidth());
dblListBox.setScreenH(sessionUser.getScreenHeight());
dblListBox.setWidthListBox(400);
dblListBox.setHeight(450);
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<%if( racks.length > 0 && groups.length > 0 ) {%>
<%=RackBean.getHTMLRackTable(sessionUser)%>
<div style="display:<%=idRack > 0 ? "block" : "none"%>">
<form id="formRack" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd">
<input type="hidden" id="idRack" name="idRack" value="<%=idRack%>">
<input type="hidden" id="groups" name="groups">
<table class="table" width="98%">
	<tr><td style="font-size:4pt;">&nbsp;</td></tr>
	<tr>
		<td class="standardTxt"><%=lang.getString("co2", "selected_rack")%>&nbsp;
			<input id="name" name="name" type="text" size="55" value="<%=rackName%>" readonly>
		</td>
		<td class="td" width="*">&nbsp;</td>		
	</tr>
</table>
</form>

<fieldset class='field' style="width: 97%;">
<legend class='standardTxt'><%=lang.getString("co2", "association")%></legend>
<table width="100%">
 <tr>
	<td height="300px;">
		<%=dblListBox.getHtmlDblListBox()%>
	</td>
 </tr>
</table>
</fieldset>
</div>
<%} else {%>
	<p class="mediumTxt">
		<%if( racks.length == 0 ) {%>
		<b><%=lang.getString("co2", "no_rack_selected")%></b>
		<%}	if( groups.length == 0 ) {%>
		<b><%=lang.getString("co2", "no_group_defined")%></b>
		<%}%>
	</p>
<%}%>