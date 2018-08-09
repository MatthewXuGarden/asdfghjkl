<%@ page language="java"  pageEncoding="UTF-8" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.presentation.tabmenu.Tab"
import="com.carel.supervisor.presentation.tabmenu.MenuBuilder"
import="com.carel.supervisor.presentation.bo.helper.DeviceStatus"
import="com.carel.supervisor.presentation.bo.helper.PortInfo"
import="com.carel.supervisor.presentation.bo.helper.LineConfig"
import="com.carel.supervisor.base.config.*"
import="com.carel.supervisor.director.packet.*"
import="java.util.*"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	boolean isProtected = ut.isTabProtected();
	int idsite = sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();
	SiteInfoList sites = new SiteInfoList();
	SiteInfo site = sites.getById(idsite);
	boolean bLanProt = PacketMgr.getInstance().isFunctionAllowed("lanprot");

	String err = sessionUser.getPropertyAndRemove("lineerror");
	String error = err!=null?lan.getString("siteview",err):"";
	String caract = lan.getString("siteview","caract");
	String tabletitle = lan.getString("linetable", "title");
	String noline = lan.getString("siteview","noline");
	String sitecomment = lan.getString("siteview","sitecomment");
	String maxLicenseLabel=lan.getString("siteview","maxlicenselabel");
	String connection = lan.getString("siteview","connection");
	String defaultvalue = lan.getString("siteview", "defaultvalue");
	String linetype = lan.getString("siteview","linetype");
	String serial=lan.getString("siteview","serial");
	String lanline=lan.getString("siteview","lanline");
	String comport = lan.getString("siteview","comport");
	String baudrate = lan.getString("siteview","baudrate");
	String protocol = lan.getString("siteview","protocol");
	String alarm_priority_enabled = lan.getString("siteview","alarm_priority_enabled");
	String enable = BaseConfig.getProductInfo("smart_modbus_enable")==null?"enable":BaseConfig.getProductInfo("smart_modbus_enable");
	String ipaddress=lan.getString("siteview","ipaddress");
	String device = lan.getString("siteview","device");
	String devicename = lan.getString("siteview","devicename");
	String autodetect = lan.getString("siteview","autodetect");
	String portdetect = lan.getString("siteview","portdetect");
	String fromaddress = lan.getString("siteview", "fromaddress");
	String toaddress = lan.getString("siteview", "toaddress");
	String serialaddress = lan.getString("siteview","serialaddress");
	String typedev = lan.getString("siteview","typedev");
	String descrdev = lan.getString("siteview","descrdev");
	String disable=lan.getString("siteview","disable");
	String devconfig=lan.getString("siteview","devconfig");
	String s_com_used=lan.getString("siteview","s_com_used");
	
	// Alessandro : aggiunta tastiera virtuale
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	String cssVirtualKeyboardClass = (OnScreenKey ? ' '+VirtualKeyboard.getInstance().getCssClass() : "");
	
	int idline = sessionUser.getProperty("line") != null ? Integer.parseInt(sessionUser.getProperty("line")) : -1;
	sessionUser.removeProperty("line");

	String auto_detect_linetype = "";	
	int auto_detect_comport		= 0;
	int auto_detect_baudrate	= 0;
	String auto_detect_protocol	= "";
	String auto_detect_address	= "";
	String cmd					= ut.getProperty("cmd");
	boolean bAutoDetect			= cmd == "auto_detect";
	boolean bPortDetect			= cmd.indexOf("port_detect") >= 0;	
	if( bAutoDetect || bPortDetect ) {
		ut.remProperty("cmd");
		// fetch autodetect parameters
		auto_detect_linetype = ut.getProperty("linetype");
		ut.remProperty("linetype");
		if( auto_detect_linetype.equals("serial") ) {
			auto_detect_comport = ut.getProperty("comport").length() > 0 ? Integer.parseInt(ut.getProperty("comport")) : 0;
			ut.remProperty("comport");
			auto_detect_baudrate = ut.getProperty("baudrate").length() > 0 ? Integer.parseInt(ut.getProperty("baudrate")) : 0;
			ut.remProperty("baudrate");
			auto_detect_protocol = ut.getProperty("protocol").length() > 0 ? ut.getProperty("protocol").split("_")[1] : "";
			ut.remProperty("protocol");
		}
		else if( auto_detect_linetype.equals("lan") ) {
			auto_detect_address = ut.getProperty("address");
			ut.remProperty("address");
			auto_detect_protocol = ut.getProperty("proto");
			ut.remProperty("proto");
		}
	}
	if(cmd == "add_lineselect")
	{
		ut.remProperty("cmd");
	}
	
	// lines
	LineBeanList lineList = new LineBeanList();
	LineBean line = idline > 0 ? lineList.retrieveLineById(idsite,idline) : null;
	lineList.retrieveLines(idsite);
	lineList.setScreenW(sessionUser.getScreenWidth());
	lineList.setScreenH(sessionUser.getScreenHeight());
	String tableline = lineList.getHTMLtable(language,tabletitle,100,900);
	// disable line edit if package is not registered 
	if( !bLanProt && line != null && line.getProtocol().startsWith("LAN") ) {
		idline = -1;
		line = null;
	}

	// protocols
	String prot = bAutoDetect || bPortDetect ? auto_detect_protocol : line != null ? line.getProtocol() : "RS485N";//kevin change from "" to RS485N as default protocol
	ProtocolBean protBean = new ProtocolBean();
	Vector<String> protocols = protBean.getProtocolList("serial");
	StringBuffer sprot_options = new StringBuffer();
	for(int i = 0; i < protocols.size(); i++) {
		String code = protocols.get(i); 
		sprot_options.append("<option value='");
		sprot_options.append(code);
		sprot_options.append("' ");
		sprot_options.append(prot.equals(protBean.getProtocol(code)) ? "selected" : "");
		sprot_options.append(">");
		sprot_options.append(protBean.getName(code));
		sprot_options.append("</option>\n");
	}
	protocols = protBean.getProtocolList("lan");
	StringBuffer lprot_options = new StringBuffer();
	for(int i = 0; i < protocols.size(); i++) {
		String code = protocols.get(i); 
		lprot_options.append("<option value='");
		lprot_options.append(code);
		lprot_options.append("' ");
		lprot_options.append(prot.equals(protBean.getProtocol(code)) ? "selected" : "");
		lprot_options.append(">");
		lprot_options.append(protBean.getName(code));
		lprot_options.append("</option>\n");
	}	
	protocols = protBean.getProtocolList("");
	StringBuffer protocol_templates = new StringBuffer();
	protocol_templates.append("var protocol_templates = new Array(\n");
	for(int i = 0; i < protocols.size(); i++) {
		String code = protocols.get(i);
		if( i > 0 )
			protocol_templates.append(",\n");
		protocol_templates.append("new ProtocolTemplate('" + code
			+ "', '" + protBean.getName(code)
			+ "', '" + protBean.getConnectionType(code)
			+ "', '" + protBean.getTypeProtocol(code)
			+ "', '" + protBean.getProtocol(code)
			+ "', " + protBean.getBeginAddr(code) + ", " + protBean.getEndAddr(code)
			+ ")");
	}
	protocol_templates.append(");");
	
	// serial ports
	PortInfo pi = new PortInfo();
	int aiPortNumbers[] = pi.listPortNumbers();
	String port_detect = pi.getDetectString(aiPortNumbers);
	
	Collection<String> comports = lineList.getComPortsUsed(idsite);
	
	String com_used_list = "";
	for (Iterator<String> iter = comports.iterator();iter.hasNext();)
	{
		String strPort = iter.next();
		if( strPort.length() > 0 && !strPort.equalsIgnoreCase("COM0") )
			com_used_list += strPort + ";";
	}
	
	String com_edit_line = line != null?line.getComport():"";
	String com = bAutoDetect || bPortDetect ? ("COM" + auto_detect_comport) : line != null && line.isSerial() ? line.getComport() : "";
	String icom = "";	
	Map<Integer,Integer> serial485COM = LineConfig.getSerial485COM();
	StringBuffer comport_options = new StringBuffer();
	StringBuffer compOpts_hid = new StringBuffer();
	for(int i=0; i<aiPortNumbers.length; i++) {
		icom = new String("COM"+aiPortNumbers[i]);
		boolean bPortOk = BaseConfig.isDemo() ? true : !(icom.equals("COM1") || icom.equals("COM2") || icom.equals("COM5"));
		if( (!comports.contains(icom) || icom.equals(com)) && bPortOk ) {
			comport_options.append("<option ");
			comport_options.append(icom.equalsIgnoreCase(com)?"selected":"");
			compOpts_hid.append(icom.equalsIgnoreCase(com)?"selected,":" ,");
			comport_options.append(" value=\"");
			comport_options.append(aiPortNumbers[i]);
			compOpts_hid.append(aiPortNumbers[i]+",");
			comport_options.append("\">");
			if(serial485COM.containsKey(aiPortNumbers[i]))
			{
		//		comport_options.append("\" label=\"RS485 - ");
		//		comport_options.append(serial485COM.get(aiPortNumbers[i]));
				comport_options.append("RS485 - ");
				comport_options.append(serial485COM.get(aiPortNumbers[i]));
				compOpts_hid.append("RS485 - ");
				compOpts_hid.append(serial485COM.get(aiPortNumbers[i]));
				compOpts_hid.append(";");
			}
			else
			{
		//		comport_options.append("\" label=\"");
				comport_options.append(icom);
				compOpts_hid.append(icom);
				compOpts_hid.append(";");
			}		
			
		//	comport_options.append("\">");
		//	comport_options.append(icom);
			comport_options.append("</option>\n");
		}
	}
	
	// baudrate
	StringBuffer baudrate_options = new StringBuffer();
	int bauds = bAutoDetect || bPortDetect ? auto_detect_baudrate : line != null && line.isSerial() ? line.getBaudrate() : 19200;//kevin change the last 0 to 19200 as default braudrate
	
	int[] baud_list = new int[]{1200,2400,4800,9600,19200,38400,62500,76800,125000};
	for(int i = 0; i < baud_list.length; i++) {
		baudrate_options.append("<option ");
		baudrate_options.append(baud_list[i] == bauds ? "selected" : "");
		baudrate_options.append(" value=\"");
		baudrate_options.append(baud_list[i]);
		baudrate_options.append("\">");
		if(baud_list[i] == 19200)
			baudrate_options.append(baud_list[i]+"*");
		else
			baudrate_options.append(baud_list[i]);
		baudrate_options.append("</option>\n");
	}
	
	DevMdlBeanList devmdllist = new DevMdlBeanList();
	DevMdlExtBean devmdlext = new DevMdlExtBean();
	DeviceDetectBean devcarel = new DeviceDetectBean(sessionUser);
	DevMdlBean[] devmdl = devmdllist.retrieve(idsite,language);
	StringBuffer device_templates = new StringBuffer();
	device_templates.append("var device_templates = new Array(\n");
	HashMap<Integer, DevMdlBean> devmdl_map = new HashMap<Integer, DevMdlBean>();
	for (int i=0;i<devmdl.length;i++) {
		DevMdlBean tmp = devmdl[i];
		
		// Devices related to Internal IO board and Supervisor are excluded
		// from device selection list
		if( !(tmp.getCode().equals("Internal IO") || tmp.getCode().equals("Supervisor")) )
		{
			if( i != 0 )
				device_templates.append(",\n");
			device_templates.append("new DeviceTemplate(" 
				+ tmp.getIddevmdl()
				+ ",'" + tmp.getDescription() + "',"
				+ devmdlext.getProtocolFilter(tmp.getIddevmdl()) + ","
				+ devcarel.getAppCodes(tmp.getIddevmdl()).toString() + ")");		
			devmdl_map.put(new Integer(tmp.getIddevmdl()),tmp);
		}
	}
	device_templates.append(");");
	
	// protocol address space
	StringBuffer address_options = new StringBuffer();
	int beginAddr = 1; 
	int endAddr = 207;
	if( prot.length() > 0 ) {
		String code = protBean.getProtocolCode(prot);
		beginAddr = protBean.getBeginAddr(code);
		endAddr = protBean.getEndAddr(code);
	}
	for(int i = beginAddr; i <= endAddr; i++) {
		address_options.append("<option value=\""+i+"\">"+i+"</option>\n");
	}	

	StringBuffer table = new StringBuffer();
	Map<Integer, DeviceBean> devices;
	if( bAutoDetect ) {
		devices = (Map<Integer, DeviceBean>)ut.getAttribute("auto_detect");
		ut.removeAttribute("auto_detect");
	}
	else {
		devices = lineList.loadDeviceByAddress(idsite,idline,language);
	}
	
	//2011-6-13, Kevin Ge, check if the user can see the devdetail folder
	String devdetailFolder = "devdetail";
	Tab tab = MenuBuilder.getFirstPage(sessionUser,devdetailFolder,true);
	//--
	
	int numActualDevices=0;
	String oddDevLine = "";
	String devDescClass = "";
	for (int i = beginAddr; i <= endAddr; i++){
		oddDevLine = ((i-1)%2==0) ? "" : "oddDevLine";
		devDescClass = ((i-1)%2==0) ? "evenDevDesc" : "oddDevDesc";
		DeviceBean deviceBean = (DeviceBean)devices.get(new Integer(i));
		if( deviceBean==null ) {
			table.append("<tr style='height:29px;' valign='middle' class=\"Row1 "+ oddDevLine +"\" onclick=\"selectLine("+(i-1)+")\">\n");
			table.append("<td id=\"TLsetline00\" class=\"standardTxt\">"+i+"</td>\n");
			table.append("<td id=\"TLsetline01\" class=\"standardTxt\"></td>\n");
			table.append("<td id=\"TLsetline02\">&nbsp;</td>");
			table.append("<td id=\"TLsetline03\" align='center'>&nbsp;</td>");
			table.append("<input type=\"hidden\" name=\"var"+i+"\" id=\"var"+i+"\" value=\"empty\"/>\n");
			table.append("<td id=\"TLsetline04\" align=\"center\" class=\"standardTxt\">&nbsp;</td>");
			table.append("</tr>\n");  
		}
		else {
			String isenabled = deviceBean.getIddevice() < 0 ? "TRUE" : DeviceStatus.isEnabled(deviceBean.getIddevice());
			table.append("<tr style='height:29px;' valign='middle' class=\"Row1 "+ oddDevLine +"\" onclick=\"selectLine("+(i-1)+")\" >\n");
			table.append("<td id=\"TLsetline00\" class=\"standardTxt\">"+i+"</td>\n");
			table.append("<td id=\"TLsetline01\" class='standardTxt'>" + (deviceBean.getIddevice() > 0
				? ((DevMdlBean)devmdl_map.get(new Integer (deviceBean.getIddevmdl()))).getDescription()
				: deviceBean.getDescription()) +"</td>");
			table.append("<td id=\"TLsetline02\"><input name=\"desc_"+i+"\" id=\"desc_"+i+"\" class=\"standardTxt "+ oddDevLine + cssVirtualKeyboardClass+"\" type='text'  maxlength='100' value=\""+deviceBean.getDescription()+"\"   onblur='MioNoAtOnBlur(this);'/></td>");
			table.append("<td id=\"TLsetline03\" align=\"center\"><input name=\"disab_"+i+"\" id=\"disab_"+i+"\" class=\"standardTxt\" type=\"checkbox\" "+ ((isenabled.equalsIgnoreCase("FALSE"))?"checked":"")+"/></td>");
			table.append("<input type=\"hidden\" name=\"var"+i+"\" id=\"var"+i+"\" value=\""+deviceBean.getIddevmdl()+"\"/>\n");
			if( bAutoDetect || tab == null)
				table.append("<td id=\"TLsetline04\" align=\"center\" class=\"standardTxt\"><img src=\"images/button/settings_off.png\"></td>");
			else
				table.append("<td id=\"TLsetline04\" align=\"center\" class=\"standardTxt\"><img src=\"images/button/settings_on_black.png\" onClick=\"device_config(" + deviceBean.getIddevice() + ")\"></td>");
			table.append("</tr>\n");
			numActualDevices++;
		}
	}
	//Contare i device
	Integer numTotDevices=(Integer)DeviceListBean.countActiveDevice(idline);
	ProductInfo productInfo= new ProductInfo();
	productInfo.load();
	Integer numMaxDevices= new Integer(productInfo.get(ProductInfo.LICENSE));
	
	//messaggio da dare se non posso fare rimozione
	String control = sessionUser.getPropertyAndRemove("control");
	if (control==null) control="";	
	
	// Per generazione combo in dettaglio
	GroupListBean groups = sessionUser.getGroup();
	int[] gids = groups.getIds();
	DeviceStructureList deviceStructureList = groups.getDeviceStructureList(); 
	int[] ids = deviceStructureList.retrieveIdsByGroupsId(gids);
	sessionUser.getTransaction().setIdDevices(ids);
	sessionUser.getTransaction().setIdDevicesCombo(ids);
%>
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>
<input type='hidden' id='vk_state' value="<%= (OnScreenKey) ? "1" : "0" %>"/>
<input type="hidden" name="wrongindex" id="wrongindex" value="<%=lan.getString("siteview","wrongindex")%>"/>
<input type="hidden" name="selcaract" id="selcaract" value="<%=lan.getString("siteview","selcaract")%>"/>
<input type="hidden" name="nullselected" id="nullselected" value="<%=lan.getString("siteview","nullselected")%>"/>
<input type="hidden" name="wrongaddress" id="wrongaddress" value="<%=lan.getString("siteview","wrongaddress")%>"/>
<input type="hidden" name="protocoldiff" id="protocoldiff" value="<%=lan.getString("siteview","protocoldiff")%>"/>
<input type="hidden" name="fixaddress" id="fixaddress" value="<%=lan.getString("siteview","fixaddress")%>"/>
<input type="hidden" name="dupaddress" id="dupaddress" value="<%=lan.getString("siteview","dupaddress")%>"/>
<input type="hidden" name="removeall" id="removeall" value="<%=lan.getString("siteview","removeall")%>"/>
<input type="hidden" name="insertdev" id="insertdev" value="<%=lan.getString("siteview","insertdev")%>"/>
<input type="hidden" name="attention" id="attention" value="<%=lan.getString("setline","attention")%>"/>
<input type="hidden" name="s_can_alert" id="s_can_alert" value="<%=lan.getString("can","check")%>"/>
<input type="hidden" name="can_CAN" id="can_CAN" value="<%=ut.remProperty("can_CAN")%>"/>
<input type="hidden" name="lineerror" id="lineerror" value="<%=error%>"/>
<input type="hidden" name="autowarning" id="autowarning" value="<%=lan.getString("siteview","autowarning")%>"/>
<input type="hidden" name="portmsg1" id="portmsg1" value="<%=lan.getString("siteview","portmsg1")%>"/>
<input type="hidden" name="portmsg2" id="portmsg2" value="<%=lan.getString("siteview","portmsg2")%>"/>
<input type="hidden" name="portmsg3" id="portmsg3" value="<%=lan.getString("siteview","portmsg3")%>"/>
<input type="hidden" name="detectmsg1" id="detectmsg1" value="<%=lan.getString("siteview","detectmsg1")%>"/>
<input type="hidden" name="detectmsg2" id="detectmsg2" value="<%=lan.getString("siteview","detectmsg2")%>"/>
<input type="hidden" name="devaddmsg" id="devaddmsg" value="<%=lan.getString("siteview","devaddmsg")%>"/>
<input type="hidden" name="prot" id="prot" value="<%=prot%>"/>
<input type="hidden" name="control" id="control" value="<%=control%>"/>
<input type="hidden" name="serials" id="serials"/>
<input type="hidden" name="devices" id="devices"/>
<input type="hidden" name="devices_detected" id="devices_detected" value="<%=bAutoDetect ? numActualDevices : -1%>"/>
<input type="hidden" name="autodetect_linetype" id="autodetect_linetype" value="<%=auto_detect_linetype%>">

<p class="StandardTxt"><%=sitecomment%></p>

<%=tableline%>

<script language="javascript">
<%=protocol_templates%>
<%=device_templates%>
</script>

<div style='visibility:hidden;display:none;' id="com_hidden" ><%=compOpts_hid%></div>

<div id="layer_set_line" style="visibility:<%=line!=null?"visible":"hidden"%>">
	<form id="frm_set_line" name="frm_set_line" action="servlet/master;jsessionid=<%=jsession%>" method="post">
		<input type="hidden" id="cmd" name="cmd" value="<%=cmd%>"/>
		<input type="hidden" id="noline" name="noline" value="<%=noline%>"/>
		<input type="hidden" id="idline" name="idline" value="<%=idline%>"/>
		<input type="hidden" id="com" name="com" value="<%=com_edit_line%>"/>
		<input id="linetype" name="linetype" type="hidden" value="<%=(line!=null && line.isLan()) ? "lan" : "serial"%>">
		<input type="hidden" name="portXdetect" id="portXdetect" value="<%=port_detect%>">
		<input type="hidden" name="smart_modbus_enable" id="smart_modbus_enable" value="<%=enable%>">
<%if( bLanProt ) { %>		
		<div id="layer_serial" style="position:relative; left:0px; top:0px; visibility:<%=(line != null && line.isSerial()) || auto_detect_linetype.equals("serial") ? "visible":"hidden"%>;">
			<fieldset class='field'><legend class="standardTxt"><%=connection%></legend>
			<table width="100%" border='0'>
			<tr>
				<td width="12%" class="standardTxt"><%=linetype%>:&nbsp;
					<select id="slinetype" name="slinetype" class="standardTxt" onChange="onLineTypeChanged(options[selectedIndex].value);">
					<option value="serial" selected><%=serial%></option>
					<option value="lan"><%=lanline%></option>
					</select>
				</td>
				<td align="center" class="standardTxt">
					<table>
					<tr valign="middle">
						<td align="right" class="standardTxt" nowrap>
							<%=comport%>:&nbsp;<span id="combo_sel" >
								<select id="comport" name="comport" class="standardTxt">
									<option value="0">----------</option>
									<%=comport_options%>
								</select>
							</span>
						</td>
						<td align="left" class="groupCategory_small" id="btn_port_detect" style="width: 110px; height: 30px;" onClick="port_detect(1);">
							<b><%=portdetect%></b>
						</td>		
					</tr>
					</table>
				</td>
				<td style="white-space:nowrap" align="center" class="standardTxt"><%=baudrate%>:&nbsp;
					<select id="baudrate" name="baudrate" class="standardTxt">
						<option value=""></option>
						<%=baudrate_options%>
					</select>
				</td> 
				<td align="center" class="standardTxt"><%=protocol%>:&nbsp;
					<select id="protocol" name="protocol" style="width:150px;" class="standardTxt" onChange="onProtocolChanged(options[selectedIndex].value);">
						<option value=""></option>
						<%=sprot_options%>
					</select>
				</td>
				<td align="right" class="standardTxt" width="*">
				<div id="smart_modbus_div">
				<%=alarm_priority_enabled%>:&nbsp;
				<input id="smart_modbus" name="smart_modbus" type="checkbox" <%=enable.equals("enable") ? "checked" : ""%>>
				</div>
				</td>	
			</tr>
			</table>
			</fieldset>
		</div>
	
		<div id="layer_lan" style="position:relative; left:0px; top:-63px; visibility:<%=(line != null && line.isLan()) || auto_detect_linetype.equals("lan") ? "visible" : "hidden"%>;">
			<fieldset class='field'><legend class="standardTxt"><%=connection%></legend>
			<table width="100%">
			<tr height="35">
				<td class="standardTxt"><%=linetype%>:&nbsp;
					<select id="llinetype" name="llinetype" class="standardTxt" onChange="onLineTypeChanged(options[selectedIndex].value);">
					<option value="serial"><%=serial%></option>
					<option value="lan" selected><%=lanline%></option>
					</select>
				</td>
				<td style="white-space:nowrap" align="center" class="standardTxt"><%=ipaddress %>:&nbsp;
					<input type="text" id="address" name="address" size="32" maxlength="128" style="width:120px;" value="<%=(line != null && line.isLan()) ? line.getIpAddress() : auto_detect_address%>" class="standardTxt">
				</td>
			
				<td align="right" class="standardTxt"><%=protocol%>:<select id="proto" name="proto" style="width:150px;" class="standardTxt" onChange="onProtocolChanged(options[selectedIndex].value);">
						<option value=""></option>
						<%=lprot_options%>
					</select>
				</td>
				<td align="right" class="standardTxt" width="160px;">
				<div id="smart_modbus_div">
				<%=alarm_priority_enabled%>:<input id="smart_modbus" name="smart_modbus" type="checkbox" <%=enable.equals("enable") ? "checked" : ""%>>
				</div>
				</td>
			</tr>
			</table>
			</fieldset>
		</div>
<%} else { %>
		<div id="layer_serial" style="position:relative; left:0px; top:0px; visibility:<%=line!=null && line.isSerial() ? "visible":"hidden"%>;">
			<fieldset class='field'><legend class="standardTxt"><%=connection%></legend>
			<table width="100%" border='0'>
			<tr>
				<td class="standardTxt">
					<table>
					<tr valign="middle">
						<td align="right" class="standardTxt" nowrap >
							<%=comport%>:&nbsp;<span id="combo_sel" >
							<select id="comport" name="comport" class="standardTxt">
							<option value="0">----------</option>
							<%=comport_options%>
							</select>
							</span>
						</td>
						<td align="left" class="groupCategory_small" id="btn_port_detect" style="width: 110px; height: 30px;" onClick="port_detect(1);">
							<b><%=portdetect%></b>
						</td>		
					</tr>
					</table>
				</td>
				<td style="white-space:nowrap" align="center" class="standardTxt"><%=baudrate%>:&nbsp;
					<select id="baudrate" name="baudrate" class="standardTxt">
						<option value=""></option>
						<%=baudrate_options%>
					</select>
				</td>
				<td align="center" class="standardTxt"><%=protocol%>:&nbsp;
					<select id="protocol" name="protocol" style="width:150px;" class="standardTxt" onChange="onProtocolChanged(options[selectedIndex].value);">
						<option value=""></option>
						<%=sprot_options%>
					</select>
				</td>
				<td align="right" class="standardTxt" width="*">
				<div id="smart_modbus_div">
				<%=alarm_priority_enabled%>:<input id="smart_modbus" name="smart_modbus" type="checkbox" <%=enable.equals("enable") ? "checked" : ""%>>
				</div></td>	
			</tr>
			</table>
			</fieldset>
		</div>
<%}%>		
		<div id="layer_devices" style="position:relative; top:<%=bLanProt ? -47 : 4%>;">
			<fieldset class='field'><legend class="standardTxt"><%=device%></legend>
			<table border="0" cellspacing="1" cellpadding="1" width="100%">
				<tr>
					<td width="15%" class="standardTxt"><%=devicename%></td>
					<td class="standardTxt" width="76%">
						<div id="layer_device" style="position:relative; left:0px; top:10px;">
							<select id="device" name="device" style="width:500px;" class="standardTxt" onChange="onDeviceSelected(options[selectedIndex].text)">
								<option value="-1"></option>
							</select>
						</div>
						<div id="layer_device_search" class="standardTxt layerDeviceSearch">
							<input type='text' class="standardTxt<%=cssVirtualKeyboardClass %>" name="device_search" id='device_search' value="">
						</div>
					</td>
					<td align="right">
						<button id="autodetect"  onClick="auto_detect();"  class="autodetect"><%=autodetect%></button>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<table border="0" cellspacing="1" cellpadding="1" width="100%">
							<tr>
								<td class="standardTxt"><%=fromaddress%></td>
								<td><select onchange="setAddressTo();" id="from" name="from" class="standardTxt">
									<%=address_options%>
									</select>
								</td>
							</tr>
							<tr><td style="height:10px;"></td></tr>
							<tr>
								<td class="standardTxt"><%=toaddress%></td>
								<td><select onchange="undoclick();" id="to" name="to" class="standardTxt">
									<%=address_options%>
									</select>
								</td>
							</tr>
							<tr><td style="height:10px;"></td></tr>
							<tr>
								<td align="center" colspan="2">
									<img onclick="line_toTable();" src="images/dbllistbox/arrowdx_on.png" align="middle"/>
								</td>
							</tr>
							<tr><td style="height:10px;"></td></tr>
							<tr>
								<td align="center" colspan="2">
								  <img onclick="line_remFromTable()" src="images/dbllistbox/delete_on.png" align="middle" />
								</td>
							</tr>
						</table>
					</td>
					<td colspan="2">
						<table class="devTableExt">
							<thead>
								<tr>
									<th id="THsetline00" align="center" class="th"><%=serialaddress%></th>
									<th id="THsetline01" align="center" class="th" nowrap><%=typedev%></th>
									<th id="THsetline02" align="center" class="th"><%=descrdev%></th>
									<th id="THsetline03" align="center" class="th"><%=disable%></th>
									<th id="THsetline04" align="center" class="th"><%=devconfig%></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td colspan="5">
										<div id="divlineTable" class="divlineTable">
											<table class="devTableInt" id="template">
												<%=table%>
											</table>
										</div>									
									</td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr>
			</table>
			</fieldset>
		</div>
	</form>
	<div style="display:none">
		<div id="numTotDevices"><%=numTotDevices%></div> 
		<div id="numMaxDevices"><%=numMaxDevices%></div>
		<div id="numActualDevices"><%=numActualDevices%></div> 
		<div id="maxLicenseLabel"><%=maxLicenseLabel%></div>
		<div id="com_used_list"><%=com_used_list%></div>
		<div id="s_com_used"><%=s_com_used%></div>
	</div>
</div>
