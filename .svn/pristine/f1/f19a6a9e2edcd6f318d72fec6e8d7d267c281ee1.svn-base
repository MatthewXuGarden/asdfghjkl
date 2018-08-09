<%@ page language="java" 
	
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.base.system.SystemInfoExt"
	import="com.carel.supervisor.base.config.BaseConfig"
	
	import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersList"
	import="com.carel.supervisor.plugin.parameters.dataaccess.Parameter"	
	import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersCFG"	
	
	import="com.carel.supervisor.presentation.dbllistbox.DblListBox"
	import="java.util.ArrayList"
	
	import="com.carel.supervisor.presentation.dbllistbox.ListBoxElement"
	import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersEventsList"	
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	
%>
<%	
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
LangService l = LangMgr.getInstance().getLangService(language);
int idsite = sessionUser.getIdSite();

String pevnPag = "";
pevnPag = sessionUser.getPropertyAndRemove("PEvnPage");
int npagina=1;
if (pevnPag!=null)
{
	npagina = Integer.parseInt(pevnPag);
}

UserTransaction userTrx = sessionUser.getCurrentUserTransaction();

String iddeviceS = sessionUser.getPropertyAndRemove("param_device");
String iddeviceSUT = userTrx.getProperty("param_device"); 

 

int iddevice;
if (iddeviceSUT!=null && !iddeviceSUT.trim().equals(""))
{
	iddevice=new Integer(iddeviceSUT).intValue();
}
else
{	
	iddevice=-1;
	
}
if (iddeviceS!=null && !iddeviceS.trim().equals(""))
{
	iddevice=new Integer(iddeviceS).intValue();
}
else
{	
	iddevice=-1;
	
}

String canModifyS = sessionUser.getPropertyAndRemove("param_canmodify");
String canModifySUT = userTrx.getProperty("param_canmodify");

userTrx.setProperty("param_device",new Integer(iddevice).toString() );

Boolean canModify = ParametersMgr.canModify(sessionUser);
userTrx.setProperty("param_canmodify",new Boolean(canModify).toString() );

ParametersEventsList pel = new ParametersEventsList(language,null,1,npagina,false,iddevice);
pel.setPageNumber(npagina);
pel.setHeight(300);
pel.setWidth(920);
pel.setScreenW(sessionUser.getScreenWidth());
pel.setScreenH(sessionUser.getScreenHeight());

String title  =l.getString("bparameters","event_list_title");
pel.setTitle(title);
String htmlTable1= pel.getHTMLTable("PEventTable",sessionUser,canModify);


ParametersConfigDeviceList pcdl = new ParametersConfigDeviceList(language);

Map<Integer, String> devices =  pcdl.getDevicesMap();
String devicesSelect ="<select name=\"param_device\""+ 
								"id=\"param_device\""+ 
								"onchange=\"document.getElementById('param_change_disp_form').submit();return false;\">";
devicesSelect+="<option value=\"-1\"> "+l.getString("parameters","alldevices")+" </option>";

// method invocation changed to hide 'Internal IO' device
DeviceListBean devs = new DeviceListBean(sessionUser.getIdSite(),sessionUser.getLanguage(), true);
DeviceBean tmp_dev = null;
int[] ids = devs.getIds();
int device=0;
for (int i=0;i<devs.size();i++){
	tmp_dev = devs.getDevice(ids[i]);
	devicesSelect+="<OPTION "+((iddevice==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"'>"+tmp_dev.getDescription()+"</OPTION>\n";
}

devicesSelect+="</select>";

boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey(); 
%>

<%@page import="com.carel.supervisor.presentation.session.UserTransaction"%>
<%@page import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersConfigDeviceList"%>
<%@page import="java.util.Map"%>


<%@page import="java.util.Iterator"%>
<%@page import="com.carel.supervisor.presentation.bean.DeviceListBean"%>
<%@page import="com.carel.supervisor.presentation.bean.DeviceBean"%>
<%@page import="com.carel.supervisor.plugin.parameters.ParametersMgr"%>

<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>
<FIELDSET style='width:97%'><LEGEND class='standardTxt'><%=l.getString("parameters","event_table_title") %></LEGEND>
<p class='standardTxt'><%=l.getString("parameters","event_list_comment") %></p>
	
	<table class='standardTxt' cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="100%">
				<FORM id="param_change_disp_form" action="servlet/master;jsessionid=<%=jsession%>" method="post">
					<%=l.getString("parameters","device") %>:&nbsp;&nbsp;&nbsp;<%=devicesSelect %>
				</FORM>
			</td>
		</tr>	

		<TR>
			<TD width="900%" valign="top" id="trPEnvList"><%=htmlTable1 %></TD>
		</TR>
	</table>

	<FORM id="param_rollback_form" action="servlet/master;jsessionid=<%=jsession%>" method="post">
		<input type="hidden" name="idevent" id="idevent" />
		<input type="hidden" name="CMD" id="CMD" value="rollback_event"> </input>
	</FORM>
</FIELDSET>
