<%@page language="java"%>
<%@page import="java.util.*"%>
<%@page import="com.carel.supervisor.plugin.energy.*"%>
<%@page import="com.carel.supervisor.presentation.session.*"%>
<%@page import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%@page import="com.carel.supervisor.presentation.helper.VirtualKeyboard"%>
<%@page import="com.carel.supervisor.presentation.dbllistbox.*"%>
<%@page import="com.carel.supervisor.plugin.base.Plugin"%>
<%@page import="com.carel.supervisor.dataaccess.language.*"
		import="java.text.MessageFormat"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);

int maxNumGrp = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10);
int maxNumCons = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
int maxConsumers = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.MAXCONSUMERS, 200);

String strSiteCounters = "";
EnergyGroup siteGroup = new EnergyGroup(-1,language);
List<EnergyConsumer> siteCounters = siteGroup.getConsumers();
if( !siteCounters.isEmpty() ) {
	for(Iterator<EnergyConsumer> itr = siteCounters.iterator(); itr.hasNext();) {
		EnergyConsumer siteCounter = itr.next();
		if( !strSiteCounters.isEmpty() )
			strSiteCounters += ";";
		strSiteCounters += siteCounter.getIdkw() + "," + siteCounter.getIdkwh();
	}
}
	
Integer idGroup = (Integer)ut.removeAttribute("idGroup");
if( idGroup == null )
	idGroup = 0;
EnergyGroup group = new EnergyGroup(idGroup,language);

List<EnergyConsumer> listConsumers = group.getConsumers();
ArrayList arrayConsumers = new ArrayList();
for(Iterator<EnergyConsumer> it = listConsumers.iterator(); it.hasNext();) {
	EnergyConsumer ec = it.next();
	String meterModel = EnergyModel.getMeterModel(ec.getIdkw(),ec.getIdkwh());
	ListBoxElement el = new ListBoxElement(ec.getName() + meterModel, ec.getIdDev() + "," + ec.getIdkw() + "," + ec.getIdkwh());
	arrayConsumers.add(el);
}
DblListBox dblListBox = new DblListBox(new ArrayList(), arrayConsumers, false, true, true, null, true);
dblListBox.setHeaderTable1(lang.getString("energy", "models_match"));
dblListBox.setHeaderTable2(lang.getString("energy", "group_devices"));
dblListBox.setScreenW(sessionUser.getScreenWidth());
dblListBox.setScreenH(sessionUser.getScreenHeight());
dblListBox.setWidthListBox(400);
dblListBox.setHeight(450);

String duplicatedSiteMeterGroupName = ut.remProperty("duplicatedSiteMeterGroupName");
duplicatedSiteMeterGroupName = duplicatedSiteMeterGroupName==null?"":duplicatedSiteMeterGroupName;

String maxconsumerreached = ut.remProperty("maxconsumerreached");
maxconsumerreached = maxconsumerreached == null?"":maxconsumerreached;
String maxconsumerreachedStr = lang.getString("energy","maxconsumerreached");
if(maxconsumerreached != null  && maxconsumerreached.length()>0)
	maxconsumerreached = MessageFormat.format(maxconsumerreachedStr, new Object[]{maxconsumerreached,maxConsumers});
else
	maxconsumerreached = "";

boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();	
%>
<%= (bOnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>
<input type='hidden' id='confirm' value='<%=lang.getString("energy", "confirm")%>'>
<input type='hidden' id='alert_req_var' value='<%=lang.getString("energy", "alert_req_var")%>'>
<input type='hidden' id='one_site_counter' value='<%=lang.getString("energy", "one_site_counter")%>'>
<input type='hidden' id='dup_site_counter' value='<%=lang.getString("energy", "dup_site_counter")%>'>
<input type='hidden' id='dup_group_name' value='<%=lang.getString("energy", "dup_group_name")%>'>
<input type='hidden' id='emeterusedingroup' value='<%=lang.getString("energy", "emeterusedingroup")%>'>
<input type='hidden' id='duplicatedSiteMeterGroupName' value='<%=duplicatedSiteMeterGroupName%>'>
<input type="hidden" id="maxconsumerreached" value='<%=maxconsumerreached %>'>
<input type='hidden' id='doubleElement' value='<%=lang.getString("energy", "dup_device_group")%>'>
<input id="maxnumgroup" type="hidden" value="<%=maxNumGrp %>">
<input id="maxnumconsumer" type="hidden" value="<%=maxNumCons %>">
<input type='hidden' id='alert_max_group' value='<%=lang.getString("energy", "alert_max_group")%>'>
<input type='hidden' id='alert_max_cons' value='<%=lang.getString("energy", "alert_max_cons")%>'>
<input type="hidden" id="serveralert" value="<%=lang.getString("energy", "serveralert")%>" />
<input id="sitecounter" type="hidden" value="<%=strSiteCounters%>">

<fieldset class='field'>		
<legend class='standardTxt'><%=lang.getString("energy","groups_config")%></legend>
<br>
<%=EnergyGroup.getHtmlGroupTable(sessionUser)%>
</fieldset>

<form id="frm_set_group" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd">
<input type="hidden" id="idGroup" name="idGroup" value="<%=idGroup%>">
<input type="hidden" id="cons" name="cons">
<table class="table" width="98%">
	<tr>
		<td class="td" width="40%"><%=lang.getString("energy", "group_name")%>&nbsp;
			<input id="name" name="name" type="text" size="35" maxlength="100" value="<%=group.getName()%>"></td>
		<td class="td" width="15%"><%=lang.getString("energy", "enabled")%>&nbsp;
			<input id="enabled" name="enabled" type="checkbox" <%=group.isEnabled() ? "checked" : ""%>></td>
		<td class="td" width="45%" align="right"><%=lang.getString("energy", "energy_meter_model")%>&nbsp;
			<select id="model" style="width: 70%;" name="model" onChange="onSelectModel(this.value)"><option value="0"></option>
				<%=EnergyModel.getHtmlModelOptions()%>
			</select>
		</td>
	</tr>
</table>
</form>

<fieldset class='field' style="height:300px">
<legend class='standardTxt'><%=lang.getString("energy","group_settings")%></legend>
<table width="100%" height="100%">
 <tr>
	<td>
		<%=dblListBox.getHtmlDblListBox()%>
	</td>
 </tr>
</table>
</fieldset>
