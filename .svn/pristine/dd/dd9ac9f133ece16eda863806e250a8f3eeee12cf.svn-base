<%@page language="java"%>
<%@page import="java.util.*"%>
<%@page import="com.carel.supervisor.plugin.energy.*"%>
<%@page import="com.carel.supervisor.presentation.session.*"%>
<%@page import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%@page import="com.carel.supervisor.presentation.helper.VirtualKeyboard"%>
<%@page import="com.carel.supervisor.presentation.dbllistbox.*"%>
<%@page import="com.carel.supervisor.plugin.base.Plugin"%>
<%@page import="com.carel.supervisor.dataaccess.language.*"%>
<%@page import="com.carel.supervisor.director.packet.PacketMgr"%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);

String strConsumer = "";
Integer idConsumer = (Integer)ut.removeAttribute("idConsumer");
ArrayList arrayAvailableDevices = new ArrayList();
ArrayList arrayConsumerDevices = new ArrayList();
if( idConsumer == null ) {
	idConsumer = 0;
}
else {
	EnergyDevice[] aEnergyDevices = EnergyDevice.getAvailableDevices(language);
	for(int i = 0; i < aEnergyDevices.length; i++) {
		if( aEnergyDevices[i] != null ) {
			EnergyDevice ed = aEnergyDevices[i]; 
			ListBoxElement el = new ListBoxElement(ed.getDescription(), ed.getIdDevice() + "," + ed.getIdSolenoidVarMdl());
			arrayAvailableDevices.add(el);
		}
	}
	EnergyConsumer ec = EnergyMgr.getInstance().consumerLookupByVariable(idConsumer);
	if( ec != null ) {
		strConsumer = ec.getName();
		aEnergyDevices = EnergyDevice.getConsumerDevices(idConsumer, language);
		for(int i = 0; i < aEnergyDevices.length; i++) {
			if( aEnergyDevices[i] != null ) {
				EnergyDevice ed = aEnergyDevices[i]; 
				ListBoxElement el = new ListBoxElement(ed.getDescription() + ", " + ed.getWeight(),
					ed.getIdDevice() + "," + ed.getIdSolenoidVarMdl() + "," + ed.getWeight());
				arrayConsumerDevices.add(el);
			}
		}
	}
}

DblListBox dblListBox = new DblListBox(arrayAvailableDevices, arrayConsumerDevices, false, true, true, null, true);
dblListBox.setHeaderTable1(lang.getString("energy", "kpi_devices"));
dblListBox.setHeaderTable2(lang.getString("energy", "consumer_devices"));
dblListBox.setScreenW(sessionUser.getScreenWidth());
dblListBox.setScreenH(sessionUser.getScreenHeight());
dblListBox.setWidthListBox(400);
dblListBox.setHeight(450);

boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
if( PacketMgr.getInstance().isFunctionAllowed("kpi") ) {
%>
<p class="StandardTxt"><%=lang.getString("energy", "comment_reg")%></p>
<%= (bOnScreenKey? "<input type='hidden' id='vkeytype' value='Numbers' />" : "") %>
<input type='hidden' id='alert_req_weight' value='<%=lang.getString("energy", "alert_req_weight")%>'>
<input type='hidden' id='alert_ambiguous' value='<%=lang.getString("energy", "alert_ambiguous")%>'>

<fieldset class='field'>		
<legend class='standardTxt'><%=lang.getString("energy","coldrental_config")%></legend>
<br>
<%=EnergyConsumer.getHtmlConsumerTable(sessionUser)%>
</fieldset>

<form id="frm_set_consumer" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd">
<input type="hidden" id="idConsumer" name="idConsumer" value="<%=idConsumer%>">
<input type="hidden" id="devs" name="devs" value="">
</form>
<%if( idConsumer > 0 ) {%>
<table class="table" width="100%">
	<tr>
		<td class="td" width="40%"><%=lang.getString("energy", "selected_meter")%>&nbsp;
			<input id="name" name="name" type="text" size="35" maxlength="100" value="<%=strConsumer%>">
		</td>
		<td class="td" width="40%"><%=lang.getString("energy", "device_weight")%>&nbsp;
			<input id="weight" name="weight" type="text" size="15" maxlength="15" onkeydown="checkOnlyAnalog(this,event);" class="<%=bOnScreenKey?"keyboardInput":"standardTxt"%>">
		</td>
		<td>&nbsp;</td>
	</tr>
</table>
<fieldset class='field' style="height:300px">
<legend class='standardTxt'><%=lang.getString("energy","coldrental_settings")%></legend>
<table width="100%" height="100%">
 <tr>
	<td>
		<%=dblListBox.getHtmlDblListBox()%>
	</td>
 </tr>
</table>
</fieldset>
<%}%>
<%} else {%>
<p class="StandardTxt"><%=lang.getString("energy", "comment_unreg")%></p>
<%}%>