<%@page import="com.carel.supervisor.base.config.ProductInfoMgr"%>
<%@ page language="java"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLDiv"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLTable"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList"
	import="com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean"
	import="com.carel.supervisor.presentation.dbllistbox.ListBoxElement"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.bo.BDataTransfer"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="java.util.*" 
	import="com.carel.supervisor.presentation.bo.helper.PortInfo"
	import="com.carel.supervisor.presentation.helper.ModbusSlaveCommander"
	import="java.text.MessageFormat"
	import="com.carel.supervisor.presentation.bean.MbSlaveConfBean"
%>
	
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	BDataTransfer bdt = (BDataTransfer) ut.getBoTrx();
	String language = sessionUser.getLanguage();
	LangService lang = LangMgr.getInstance().getLangService(language);
	int idsite = sessionUser.getIdSite();
	String jsession = request.getSession().getId();
	
	String strIdMbDev = ut.remProperty("idmbdev");
	int idmbdev = strIdMbDev.isEmpty() ? 0 : Integer.parseInt(strIdMbDev);
	
	// service state
	int[] aiStatus = ModbusSlaveCommander.getStateAndStarttype();
	boolean bServiceRunning = aiStatus != null && aiStatus.length >= 1 && aiStatus[0] == ModbusSlaveCommander.E_STATE_RUNNING;
	
	// mbslave configuration
	MbSlaveConfBean beanMbSlave = new MbSlaveConfBean(sessionUser);
	if( idmbdev > 0 )
		beanMbSlave.loadDevice(idmbdev);
	
	// serial ports
	LineBeanList lineList = new LineBeanList();
	Collection<String> comports = lineList.getComPortsUsed(idsite);	
	String com_used_list = "";
	for (Iterator<String> iter = comports.iterator();iter.hasNext();)
	{
		String strPort = iter.next();
		if( strPort.length() > 0  && !strPort.equalsIgnoreCase("COM0") )
			com_used_list += strPort + ";";
	}
	PortInfo pi = new PortInfo();
	int aiPortNumbers[] = pi.listPortNumbers();
	String com = "COM" + beanMbSlave.getPort();
	String icom = "";	
	StringBuffer comport_options = new StringBuffer();
	for(int i=0; i<aiPortNumbers.length; i++) {
		icom = new String("COM"+aiPortNumbers[i]);
		boolean bPortOk = BaseConfig.isDemo() ? true : !(icom.equals("COM1") || icom.equals("COM2") || icom.equals("COM5"));
		if( (!comports.contains(icom) || icom.equals(com)) && bPortOk ) {
			comport_options.append("<option ");
			comport_options.append(icom.equalsIgnoreCase(com)?"selected":"");
			comport_options.append(" value=\"");
			comport_options.append(aiPortNumbers[i]);
			comport_options.append("\" label=\"");
			comport_options.append(icom);
			comport_options.append("\">");
			comport_options.append(icom);
			comport_options.append("</option>\n");
		}
	}	
	
	// baudrate
	StringBuffer baudrate_options = new StringBuffer();
	int bauds = beanMbSlave.getSpeed();
	int[] baud_list = new int[]{1200,2400,4800,9600,19200,38400,62500,76800,125000};
	for(int i = 0; i < baud_list.length; i++) {
		baudrate_options.append("<option ");
		baudrate_options.append(baud_list[i] == bauds ? "selected" : "");
		baudrate_options.append(" value=\"");
		baudrate_options.append(baud_list[i]);
		baudrate_options.append("\">");
		baudrate_options.append(baud_list[i]);
		baudrate_options.append("</option>\n");
	}
	
	// combo devices/models
	DeviceListBean devs = new DeviceListBean(idsite,language);
	DeviceBean tmp_dev = null;
	int[] ids = devs.getIds();
	StringBuffer div_dev = new StringBuffer();
	int device=0;
	for (int i=0;i<devs.size();i++){
		tmp_dev = devs.getDevice(ids[i]);
		div_dev.append("<option "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"' class= '"+((i%2==0)?"Row1":"Row2")+"'>"+tmp_dev.getDescription()+"</option>\n");
	}
	
	DevMdlBeanList devmdllist = new DevMdlBeanList();
	DevMdlBean[] devmdl = devmdllist.retrieveDevMdl(idsite,language);
	StringBuffer combodev = new StringBuffer();
	StringBuffer typedev = new StringBuffer();
	combodev.append("<option value='-1'>-------------------</option>");
	String ss="";
	for (int i=0;i<devmdl.length;i++)
	{
		DevMdlBean tmp = devmdl[i];
		combodev.append("<option value="+tmp.getIddevmdl()+" "+ss+">"+tmp.getDescription()+"</option>\n");
	}	

	// device address
	StringBuffer address_options = new StringBuffer();
	Collection<Integer> addresses = beanMbSlave.getDevAddresses();	
	int address = beanMbSlave.getDeviceAddress();
	for(int i = 1; i <= 247; i++) {
		if( i == address || !addresses.contains(i) ) {
			address_options.append("<option ");
			address_options.append(i == address ? "selected" : "");
			address_options.append(" value=\"");
			address_options.append(i);
			address_options.append("\">");
			address_options.append(i);
			address_options.append("</option>\n");
		}
	}
	
	// write enable
	String write_enable = beanMbSlave.getWriteEnable() ? "checked" : "";
	String sitename = sessionUser.getSiteName();
	boolean displayPrintIcon = addresses.size()>0;
	String needConfirm = ut.remProperty("needConfirm");
	if(needConfirm == null || needConfirm.length()==0)
		needConfirm = "0";
	String varSource = ut.remProperty("varSource");
	if(varSource == null || varSource.length() == 0)
		varSource = "";
	String invalidType = ut.remProperty("invalidType");
	if(invalidType == null || invalidType.length() == 0)
		invalidType = "0";
	String deviceAdded = ut.remProperty("deviceAdded");
	if(deviceAdded == null || deviceAdded.length() == 0)
		deviceAdded = "0";
	
	String alert_maximumvarnum = lang.getString("datatransfer", "maximumvarnum");
	if("1".equals(invalidType))
		alert_maximumvarnum = MessageFormat.format(alert_maximumvarnum, new Object[]{deviceAdded,ProductInfoMgr.getInstance().getProductInfo().get("mdslave_threshold")});
	String alert_maximumaddress = lang.getString("datatransfer", "maximumaddress");
	if("2".equals(invalidType))
		alert_maximumaddress = MessageFormat.format(alert_maximumaddress, new Object[]{deviceAdded,MbSlaveConfBean.MAXADDRESS});
	String alert_deviceadded = lang.getString("datatransfer", "deviceadded");
	if(deviceAdded != "0")
		alert_deviceadded = MessageFormat.format(alert_deviceadded, new Object[]{deviceAdded});
	
	boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<input type="hidden" id="alert_req_fields" name="alert_req_fields" value="<%=lang.getString("datatransfer", "alert_req_fields")%>">
<input type="hidden" id="alert_var_exists" name="alert_var_exists" value="<%=lang.getString("datatransfer", "alert_var_exists")%>">
<input type="hidden" id="alert_var_limit" name="alert_var_limit" value="<%=lang.getString("datatransfer", "alert_var_limit")%>">
<input type="hidden" id="alert_dev_empty" name="alert_dev_empty" value="<%=lang.getString("datatransfer", "alert_dev_empty")%>">
<input type="hidden" id="alert_mb_restart" name="alert_mb_restart" value="<%=lang.getString("datatransfer", "alert_mb_restart")%>">
<input type="hidden" id="alert_mb_restart2" name="alert_mb_restart2" value="<%=lang.getString("datatransfer", "alert_mb_restart2")%>">
<input type="hidden" id="alert_coil_used" name="alert_coil_used" value="<%=lang.getString("datatransfer", "alert_coil_used")%>">
<input type="hidden" id="alert_reg_used" name="alert_reg_used" value="<%=lang.getString("datatransfer", "alert_reg_used")%>">
<input type="hidden" id="variables" name="variables" value="<%=beanMbSlave.getVariablesNo()%>">
<input type="hidden" id="mb_restart" name="mb_restart" value="<%=ut.remProperty("mb_restart_required")%>">
<input type="hidden" id="variables_limit" name="variables_limit" value="<%=ProductInfoMgr.getInstance().getProductInfo().get("mdslave_threshold")%>">
<input type="hidden" id="service_running" name="service_running" value="<%=bServiceRunning?1:0%>">
<input type="hidden" id="type1" name="type1" value="<%=lang.getString("vs", "type1")%>">
<input type="hidden" id="type2" name="type2" value="<%=lang.getString("vs", "type2")%>">
<input type="hidden" id="type3" name="type3" value="<%=lang.getString("vs", "type3")%>">
<input type="hidden" id="type4" name="type4" value="<%=lang.getString("vs", "type4")%>">
<input type="hidden" id="type32" name="type32" value="<%=lang.getString("vs", "type32")%>">
<input type="hidden" id="slaveDeviceDelete" name="slaveDeviceDelete" value="<%= lang.getString("datatransfer", "slaveDeviceDelete") %>">
<INPUT type="hidden" id="sitename" value="<%=sitename%>" />
<INPUT type="hidden" id="displayPrintIcon" value="<%=displayPrintIcon%>" />

<p class="standardTxt"><%=lang.getString("datatransfer", "tab2desc")%></p>
<form id="frm_modbus_slave" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd">
<input type="hidden" id="idmbdev" name="idmbdev" value="<%=idmbdev%>">
<input type="hidden" id="defmbdev" name="defmbdev" value="">
<fieldset class="field">
	<legend class="standardTxt"><%=lang.getString("datatransfer", "conn_cfg")%></legend>
	<table width="100%">
	<tr class="standardTxt">
		<td width="25%"><input id="connection" name="connection" type="radio" value="RS485" onClick="onConnection('RS485')" <%=beanMbSlave.getConnection().equals(MbSlaveConfBean.RS485)?"checked":""%>>&nbsp;RS485</td>
		<td width="25%"><%=lang.getString("datatransfer", "com_port")%>
			<select id="comport" name="comport" class="standardTxt">
				<option value="">----------</option>
				<%=comport_options%>
			</select>
		</td>
		<td width="25%"><%=lang.getString("datatransfer", "baud_rate")%>
			<select id="baudrate" name="baudrate" class="standardTxt">
				<option value="">----------</option>
				<%=baudrate_options%>
			</select>
		</td>
		<td width="*" align="center"><%=lang.getString("datatransfer", "write_enable")%>&nbsp;<input type="checkbox" id="writeenable" name="writeenable" <%=write_enable%>></td>
	</tr>
	<tr class="standardTxt">
		<td><input id="connection" name="connection" type="radio" value="TCP/IP" onClick="onConnection('TCP/IP')" <%=beanMbSlave.getConnection().equals(MbSlaveConfBean.TCP_IP)?"checked":""%>>&nbsp;TCP/IP</td>
		<td colspan="3"><%=lang.getString("datatransfer", "tcp_port")%></td>
	</tr>
	</table>
</fieldset>

<br>
<br>

<fieldset class="field">
<legend class="standardTxt"><%=lang.getString("datatransfer", "mb_devices")%></legend>
<br>
<table class='standardTxt' width="100%">
<tr>
<td width="50%"><%=lang.getString("datatransfer","vars_supported")%>&nbsp;<%=ProductInfoMgr.getInstance().getProductInfo().get("mdslave_threshold")%></td>
<td width="50%"><%=lang.getString("datatransfer","vars_configured")%>&nbsp;<%=beanMbSlave.getMbVariablesNo()%></td>
</tr>
</table>
<br>
<%=beanMbSlave.getHTMLDeviceTable()%>
</fieldset>

<br>
<br>

<table width="98%">
	<tr class="standardTxt">
		<td><%=lang.getString("datatransfer", "device_name")%>&nbsp;
			<input type="text" id="device_name" name="device_name" value="<%=beanMbSlave.getDeviceName()%>" maxlength="128" size="40" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>">
		</td>
		<td width="60%" align="left"><%=lang.getString("datatransfer", "device_address")%>&nbsp;<select id="device_address" name="device_address">
			<option value="">----------</option>
			<%=address_options%>
		</td>
	</tr>
</table>
</form>

<fieldset class="field">
	<legend class="standardTxt"><%=lang.getString("datatransfer", "var_list")%></legend>
		<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" align="center">
			<TR class='standardTxt'>
				<TD width="12%" align="left">
					<INPUT onclick='reload_actions(1);' type='radio' id='d' name='devmodl' checked="checked"/><%=lang.getString("datatransfer","devices")%></TD>
				<TD width="*" align="left">
					<INPUT onclick="enableModelSelection();" type='radio' id='m' name='devmodl'/><%=lang.getString("datatransfer","device_models")%>
					&nbsp;
					<SELECT onchange='reload_actions(2)' class='standardTxt' id='model' name='model' disabled style='width:275px;'>
					<%=combodev.toString()%>
					</SELECT>
				</TD>
				<TD align="center"><%=lang.getString("datatransfer", "next_coil_addr")%>&nbsp;
				<input id="next_coil_addr" type="text" class="standardTxt" size="10"></TD>
				<TD align="center"><%=lang.getString("datatransfer", "next_reg_addr")%>&nbsp;
				<input id="next_register_addr" type="text" class="standardTxt" size="10"></TD>
				<TD align="right">
					<img id="imgAddVar" name="imgAddVar" src="images/actions/addsmall_off.png" style="cursor:pointer;" onclick="addDevVar()">
				</TD>
			</TR>
		</TABLE>
		<table width="100%" cellpadding="0" cellspacing="2">
			<tr height="10px"></tr>
			<tr>
				<td  class='th' width="49%"><%=lang.getString("datatransfer","devices")%></td>
				<td  class='th' width="49%" ><%=lang.getString("datatransfer","variables")%></td>
				<th width='2%'>&nbsp;</th>
			</tr>
			<tr>
			<td width="49%" >
				<div id="div_dev">
					<select onclick="reload_actions(0);" ondblclick="reload_actions(0);" id="dev" name='sections'  size='10' class='selectB'>
					<%=div_dev.toString()%>
					</select>
				</div>
			 </td>
			<td width="49%">
				<div id="div_var">
					<select name=sections id='var' multiple size=10  class='selectB'>
					</select>
				</div>
			 </td>
			<td width="2%">&nbsp;</td>
		 </tr>
		</table>
</fieldset>
<br><br>
<fieldset class="field">
	<legend class="standardTxt"><%=lang.getString("datatransfer", "curr_conf")%></legend>
<table width="100%">
 <tr>
  <td rowspan="4"><%=beanMbSlave.getHTMLVariableTable()%></td>
  <td align="right" valign="top"><img id="imgRemoveVar" name="imgRemoveVar" src="images/actions/removesmall_off.png" style="cursor:pointer;" onclick="onDeleteMbVar(-1)"></td>
 </tr>
 <tr><td>&nbsp;</td></tr>
 <tr>
   <td align="right" valign="top"><img id="imgUpVar" name="imgUpVar" src="images/actions/arrowupsmall_off.png" style="cursor:pointer;" onclick="moveVar(-1)"></td>
 </tr>
 <tr>
   <td align="right" valign="top"><img id="imgDownVar" name="imgDownVar" src="images/actions/arrowdnsmall_off.png" style="cursor:pointer;" onclick="moveVar(1)"></td>
 </tr>
</table>
	
</fieldset>
<div id="typewin" class="uploadWin">
	<div id="uploadwinheader" class="uploadWinHeader">
		<div class="uploadWinClose" onclick="document.getElementById('typewin').style.display='none';">X</div><%=lang.getString("datatransfer","wintitle")%>
	</div>
	<div id="uploadwinbody" class="uploadWinBody">
		<div id="win_type"></div>
		<div>
			<form name="uploadfrm1" id="uploadfrm1" style="display:block;" action="servlet/master;jsessionid=<%=jsession%>" method="post">
				<input type="hidden" name="cmd" value="defaultdevice" />
				<input type="hidden" name="deleteConfirm" id="deleteConfirm" value="0"/>
				<input type="hidden" id="needConfirm" value="<%=needConfirm%>"/>
				<input type="hidden" name="varsource" id="varsource" value="<%=varSource%>"/>
				<input type="hidden" id="invalidType" value="<%=invalidType%>"/>
				<input type="hidden" id="deviceAdded" value="<%=deviceAdded%>"/>
				<input type="hidden" id="alert_choosevarsource" value="<%=lang.getString("datatransfer", "choosevarsource")%>">
				<input type="hidden" id="alert_confirmremove" value="<%=lang.getString("datatransfer", "confirmremove")%>">
				<input type="hidden" id="alert_maximumvarnum" value="<%=alert_maximumvarnum%>">
				<input type="hidden" id="alert_maximumaddress" value="<%=alert_maximumaddress%>">
				<input type="hidden" id="alert_deviceadded" value="<%=alert_deviceadded%>">
				<div id="uploadwinlocalpath">
					<div id="lpradio"><input type="checkbox" name="varsourceInput" value="log"></div>
					<div class="rdesc"><%=lang.getString("datatransfer", "logtype")%></div>
				</div>
				<div id="uploadwinlocalpath">
					<div id="lpradio"><input type="checkbox" name="varsourceInput" value="highestalarm"></div>
					<div class="rdesc"><%=lang.getString("datatransfer", "highestalarmtype")%></div>
				</div>
				<div id="uploadwinbuttons">
					<table border="0">
						<tr>
							<td class="groupCategory_small" style="width:110px;height:30px;" onclick="submit_template_file();"><%=lang.getString("dtlview","submit")%></td>
							<td class="groupCategory_small" style="width:110px;height:30px;" onclick="document.getElementById('typewin').style.display='none'"><%=lang.getString("dtlview","cancel")%></td>
						</tr>
					</table>
				</div>
			</form>
		</div>	
	</div>
</div>