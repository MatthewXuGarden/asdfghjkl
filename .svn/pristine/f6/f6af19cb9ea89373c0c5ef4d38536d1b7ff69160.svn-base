<%@ page language="java" 
import="java.util.*"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
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

RackBean[] racks = RackBean.retrieveRacksSelected(language);
List<RackBean> tempList = new ArrayList();
for(RackBean rack:racks)
{
	if(rack.getNumGroups()>0)
		tempList.add(rack);
}
racks = (RackBean[])tempList.toArray(new RackBean[tempList.size()]);
GroupBean[] groups = GroupBean.retrieveGroups();
boolean dtlviewEnabled = sessionUser.isMenuActive("dtlview");
%>
<%if(racks!=null &&racks.length>0) {%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<input id="running" type="hidden" value="<%=CO2SavingManager.getInstance().isRunning()%>">
<form id="formPlugin" action="servlet/master;jsessionid=<%=jsession%>" method="post">
	<input type="hidden" id="cmd" name="cmd"/>
</form>
<fieldset style='width:97%;'>
<legend class='standardTxt'><%=lang.getString("co2", "plugin_status")%></legend>
<table width="100%" class='standardTxt'>
	<tr valign="middle">
		<%if( CO2SavingManager.getInstance().isRunning() ) {%>
		<td style="text-align:center;width: 5%;" >
			<img src="./images/actions/start_off.png" border="0" />
		</td>
		<td style="text-align:center;width: 5%;">
			<img src="./images/actions/stop_on_black.png" onclick="onPluginCommand('stop');" border="0" style="cursor:pointer;"  />
		</td>
		<td style="text-align:center;width: 5%;">
			<img src="./images/actions/restart_on_black.png" onclick="onPluginCommand('restart');"  border="0" style="cursor:pointer;" />
		</td>
		<td style="text-align:center;color:green;width:*;">
			<%=lang.getString("co2", "plugin_running")%>
		</td>
		<%} else {%>
		<td style="text-align:center;width: 5%;">
			<img src="./images/actions/start_on_black.png" onclick="onPluginCommand('start');"  border="0" style="cursor:pointer;" />
		</td> 
		<td style="text-align:center;width: 5%;">
			<img src="./images/actions/stop_off.png" border="0" />
		</td>
		<td style="text-align:center;width: 5%;">
			<img src="./images/actions/restart_off.png" border="0" />
		</td>
		<td style="text-align:center;color:red;width:*;">
			<%=lang.getString("co2", "plugin_not_running")%>
		</td>
		<%}%>
	</tr>	
</table>
</fieldset>

<%if( CO2SavingManager.getInstance().isRunning()) {%>
<br><br>

<table class="table" width="<%=racks.length == 1 ? "33" : racks.length == 2 ? "66" : "99"%>%" <%=racks.length < 3 ? "align='center'" :""%>>
<%for(int i = 0; i < racks.length; i++) {%>
	<%if( i == 0 ) {%>
		<tr valign="top">
	<%} else if( i % 3 == 0 ) {%>
		</tr><tr valign="top">
	<%}%>
	<td align="center">
		<table class="table" width="99%" align="center">
			<tr class="th" height="22">
				<td><b><%if(dtlviewEnabled){%><a href="javascript:void(0)" onclick="top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev=<%=racks[i].getBeanRack().getIddevice() %>&desc=ncode01')"><%=racks[i].getDescription()%></a><%}else{out.print(racks[i].getDescription());}%></b></td>
				<th width="25px"><img id="rack_<%=racks[i].getIdRack()%>" src="images/led/L<%=CO2SavingManager.getInstance().getRackStatus(racks[i].getIdRack())%>.gif"></th>
			</tr>
			<%for(int j = 0, row = 0; j < groups.length; j++)
				if( groups[j].getIdRack() == racks[i].getIdRack() ) {%>
			<tr class="Row2">
				<td class="standardTxt"><%=groups[j].getName()%></td>
				<th><img id="group_<%=groups[j].getIdGroup()%>" src="images/led/L<%=CO2SavingManager.getInstance().getGroupStatus(racks[i].getIdRack(), groups[j].getIdGroup())%>.gif"></th>
			</tr>
			<%}%>
		</table>
	</td>
<%}%>
	<%if( racks.length > 0 ) {%>
		</tr>
	<%}%>
</table>

<br>

<table>
	<tr class="standardTxt" valign="middle">
		<th width="25px"><img src="images/led/L0.gif"></th>
		<td>- <%=lang.getString("co2", "not_available")%></td>
		<th width="25px"><img src="images/led/L1.gif"></th>
		<td>- <%=lang.getString("co2", "system_running")%></td>
		<th width="25px"><img src="images/led/L2.gif"></th>
		<td>- <%=lang.getString("co2", "system_safe")%></td>
		<th width="25px"><img src="images/led/L3.gif"></th>
		<td>- <%=lang.getString("co2", "group_disabled")%></td>
	</tr>
</table>
<%}%>
<%} else {//if(racks!=null &&racks.length>0)%>
<input type="hidden" value="false" id="plgnotcnf">
<table border="0" width="100%" cellpadding="1" cellspacing="1">
	<tr>
		<td align="center"><%=lang.getString("energy","pgtobedone") %></td>
	</tr>
</table> 
<%}%>
