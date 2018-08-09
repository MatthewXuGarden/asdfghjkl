<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.IProductInfo"
import="com.carel.supervisor.base.config.ProductInfoMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard" 
import="com.carel.supervisor.dataaccess.dataconfig.SiteInfoList"
import="com.carel.supervisor.dataaccess.dataconfig.SiteInfo"
import="com.carel.supervisor.base.system.SystemInfoExt"
import="com.carel.supervisor.base.system.NetworkInfo"
import="com.carel.supervisor.base.system.NetworkInfoList"
import="com.carel.supervisor.base.system.PvproInfo"
import="com.carel.supervisor.director.packet.PacketMgr"
import="java.text.NumberFormat"
import="java.text.DecimalFormat"
import="com.carel.supervisor.base.conversion.StringUtility"
import="org.apache.commons.io.FileSystemUtils"
import="com.carel.supervisor.base.system.SystemInfoExt"
import="java.util.*"
%>

<%
	UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = userSession.getCurrentUserTransaction();
	int idsite= userSession.getIdSite();
	boolean isProtected = ut.isTabProtected();
	String language = userSession.getLanguage();
	String jsession = userSession.getSessionId();
	LangService lan = LangMgr.getInstance().getLangService(language); 
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	
	SiteInfoList sites = new SiteInfoList();
	SiteInfo site = sites.getById(idsite);
	String site_name = site.getName();
	String site_id = site.getCode();
	String site_phone = site.getPhone();
	String site_password = site.getPassword();
	
	
	String registrationcomment = lan.getString("wizard","registrationcomment");
	String registration = lan.getString("wizard","registration");
	String sysinfo = lan.getString("wizard","sysinfo");
	String regconnectdata = lan.getString("wizard","regconnectdata");
	String pvpcode = lan.getString("mgr","pvpcode");
	String actcode = lan.getString("mgr","actcode");
	String caract = lan.getString("siteview","caract");
	String name = lan.getString("siteview","name");
	String identify = lan.getString("siteview","identify");
	String telephone = lan.getString("siteview","telephone");
	String password = lan.getString("siteview","password");
	String confirmpassword= lan.getString("siteview","confirmpassword");
	String next = lan.getString("wizard","next");
	String back = lan.getString("wizard","back");
	String compilename = lan.getString("siteview","compilename");
	String shortpassw = lan.getString("siteview","shortpassw");
	String wrongpassw = lan.getString("siteview","wrongpassw");
	String msg = lan.getString("mgr","msglko");
	
	String fixver = "";
	try{
	fixver = BaseConfig.getProductInfo("fix");
	if(fixver!=null && !fixver.equalsIgnoreCase(""))
	{
		fixver="."+fixver;
	}
	else
	{
		fixver="";
	}
	}catch(Exception e){}
	String custom = BaseConfig.getProductInfo("custom");
	if (custom==null) custom="";
	
	String ips="";
	for(int i=0;i<NetworkInfoList.getInstance().size();i++){
		if((NetworkInfoList.getInstance().getNetworkInfo(i).getIp().equals("127.0.0.1"))&&(NetworkInfoList.getInstance().size()>1)){
			//if there are more than 1 ipaddress, do not show the 127.0.0.1
		}else{
			ips+=(("".equals(ips))?"":"/")+NetworkInfoList.getInstance().getNetworkInfo(i).getIp();
		}
	}
	
	// List Not Registered Packet
	String[] packets = PacketMgr.getInstance().getPacketComboList();
	
	/* Static registration strings */
	Map<String,String> pkgDescriptions = PacketStaticData.packet_descr;
		
	String registeredModuleTable = PacketMgr.getInstance().getPacketTable(language,pkgDescriptions,userSession.getScreenWidth(),userSession.getScreenHeight(),880,120);
	
	String disk = BaseConfig.getProperty("dbdisk");
	if ((null == disk) || (disk.equals("")))
	{
		disk = "c:";
	}
	String ret = SystemInfoExt.getInstance().getDiskUsage(disk);
	String[] usage = {"",""};
	if(ret != null)
		usage = StringUtility.split(ret,";");

	if(usage.length < 2)
		usage = new String[]{"",""};
		
	long free = FileSystemUtils.freeSpace(BaseConfig.getProperty("dbdisk"));
	free = free /(1024*1024);
	DecimalFormat d = new DecimalFormat();
	d.setGroupingUsed(false	);
	String freeSpace = d.format(free) + " MB";

	String msgusrlnc = "";
	String licensestatus = userSession.getPropertyAndRemove("licensestatus");
	if(licensestatus != null)
		msgusrlnc = lan.getString("mgr",licensestatus);
	String usedspace = usage[1]+" MB";
	String sysram = SystemInfoExt.getInstance().getTotalRam() +" KB";
	
	PvproInfo pvpi = PvproInfo.getInstance();
    int nTotalDevs = pvpi.getLicense();
    int nActiveDevs = pvpi.getActiveDevices();
    int nLoggingThreshold = pvpi.getLoggingThreshold();
	int nLoggedVars = pvpi.getLoggedVariables();
	
	String customversion = BaseConfig.getProductInfo("customversion");
	customversion = customversion==null?"":customversion.trim();
	String display = "none";
	if(!"".equals(customversion))
		display = "block";
%>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>



<%@page import="com.carel.supervisor.director.packet.PacketStaticData"%><input type="hidden" id="isrestart" value="true">
<input type="hidden" id="msg" value="<%=msg%>"/>
<input type="hidden" id="compilename" name="compilename" value="<%=compilename%>"/>
<input type="hidden" id="shortpassw" name="shortpassw" value="<%=shortpassw%>"/>
<input type="hidden" id="wrongpassw" name="wrongpassw" value="<%=wrongpassw%>"/>
<input type="hidden" id="msgtouserforlicenze" value="<%=msgusrlnc%>"/>

<form id="frm_registration_siteinfo" name="frm_registration_siteinfo" action="servlet/master;jsessionid=<%=jsession%>" method="post">
	<table width="99%" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<fieldset class="field" style="width: 97%;">
						<legend class="standardTxt"><%=sysinfo%></legend>
						<table width="100%" cellspacing=1 cellpadding=1>
							<tr>
								<td class='standardTxt' width="25%"><b><%=lan.getString("mgr","sysram")%></b></td>
								<td align='left' class='standardTxt' width="25%"><%=sysram%></td>
								<td class='standardTxt'  width="25%"><b><%=lan.getString("mgr","freespace")%></b></td>
								<td align='left' class='standardTxt' width="25%"><%=freeSpace%></td>
							</tr>
							<tr>
								<td class='standardTxt'><b><%=lan.getString("mgr","usedcpu")%></b></td>
								<td align='left' class='standardTxt' id="cpuperusage"></td>
								<td class='standardTxt'><b><%=lan.getString("mgr","usedspace")%></b></td>
								<td align='left' class='standardTxt'><%=usedspace%></td>
							</tr>
							<tr>
                                <td class='standardTxt'  width="13%"><b><%=lan.getString("mgr","total_devs")%></b></td>
                                <td align='left' class='standardTxt' width="12%"><%=nTotalDevs%></td>
                                <td class='standardTxt'  width="13%"><b><%=lan.getString("mgr","used_devs")%></b></td>
                                <td align='left' class='standardTxt' width="12%"><%=nActiveDevs%></td>
							</tr>
							<tr>
                                <td class='standardTxt'><b><%=lan.getString("mgr","logging_threshold")%></b></td>
                                <td align='left' class='standardTxt'><%=nLoggingThreshold%></td>
                                <td class='standardTxt'><b><%=lan.getString("mgr","logged_vars")%></b></td>
                                <td align='left' class='standardTxt'><%=nLoggedVars%></td>
							</tr>
						</table>
				</fieldset>
				<input type="hidden" id="cmd" name="cmd"/>
			</td>
		</tr>
		<tr><td height="10px"></td></tr>
		<tr>
			<td>
				<fieldset class="field" style="width: 97%;">
					<legend class="standardTxt"><%=registration%></legend>
					<table border="0" width="100%" cellspacing="1" cellpadding="1" >
						<tr height='20px'>
							<td class="standardTxt" width='200px'><%=lan.getString("wizard","ipaddress")%></td>
							<td class='standardTxt'><%=ips%> </td>
						</tr>
						<tr height='20px'>
							<td class="standardTxt" width='200px'><%=lan.getString("mgr","macaddress")%></td>
							<td class='standardTxt'><%=SystemInfoExt.getInstance().getMacAddrees()%> </td>
						</tr>
						<tr height='20px'>
							<td class="standardTxt" width='200px'><%=lan.getString("mgr","hwversion")%></td>
							<td class='standardTxt'><%=BaseConfig.getHardwareVersion()%> </td>
						</tr>
						<tr height='20px'>
							<td class="standardTxt"><%=lan.getString("mgr","pvpversion")%></td>
							<td class='standardTxt'><%=BaseConfig.getProductInfo("version")%><%=fixver%></td>
						</tr>
						<tr height="20px" style="display:<%=display %>">
							<td class="standardTxt"><%=lan.getString("mgr","customversion")%></td>
							<td class='standardTxt'><%=customversion%></td>
						</tr>
						<tr>
							<td class='standardTxt'>
								<B><%=lan.getString("mgr", "plugindescription")%></B>
							</td>
							<td align='left' class='standardTxt'>
								<SELECT name="code" onchange="">
									<OPTION value="----------">----------</OPTION>
									<% for(int i=0; i<packets.length; i++) { %>
									<OPTION value="<%=packets[i]%>"><%=packets[i]%> - <%=pkgDescriptions.get(packets[i]) %></OPTION>
									<%} %>									
								</SELECT>
							</td>
						</tr>
						<tr>
							<td class='standardTxt'><%=pvpcode%></td>
							<td align='left' class='standardTxt'>
								<input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="serial" class="standardTxt" value="" maxlength="30" size="30"/>
							</td>
						</tr>
						<tr>
							<td class='standardTxt'><%=actcode%></td>
							<td align='left' class='standardTxt'>
								<input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="activation" class="standardTxt" value="" maxlength="30" size="30"/>
							</td>
							
						</tr>
						<tr><td height="5px"></td></tr>
						<tr>
							<td colspan="2">
								<%=registeredModuleTable%>
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
		<tr><td height="10px"></td></tr>
		<tr>
			<td>
				<table width="99%" cellspacing="0" cellpadding="0">
					<tr>
						<td width="49.5%">
							<fieldset style='height:110px;' class="field" >
								<legend class="standardTxt"><%=caract%></legend>
								<table border="0" width="100%" cellspacing="1" cellpadding="1" >
									<tr>
										<td class="standardTxt" width='200px'><%=name%></td>
										<td class='standardTxt'><input id="site_name" name="site_name" <%=(isProtected?"disabled":"")%> <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> maxlength="30" size="30" value="<%=site_name%>" onblur="noBadCharOnBlur(this);" onkeydown="MioSiteName(event);" onpaste="MioBlockEvent(event);"> * </td>
									</tr>
									<tr>
										<td class="standardTxt"><%=telephone%></td>
										<td><input id="site_phone" name="site_phone" <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> <%=(isProtected?"disabled":"")%> maxlength="30" size="30" value="<%=site_phone%>" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'></td>
									</tr>
								</table>
							</fieldset>
						</td>
						<td width="1%">&nbsp;</td>
						<td width="49.5%">
							<fieldset style='height:110px;' class="field">
								<legend class="standardTxt"><%=regconnectdata %></legend>
								<table border="0" width="100%" cellspacing="1" cellpadding="1" >
									<tr>
										<td class="standardTxt" width="200px"><%=identify%></td>
										<td><input id="site_id" name="site_id" <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> <%=(isProtected?"disabled":"")%> maxlength="30" size="30" value="<%=site_id%>" onblur="MioOnlyCharNumOnBlur(this);" onkeypress='return filterInput(2, event);'></td>
									</tr>
									<tr>
										<td class="standardTxt"><%=password%></td>
										<td class='standardTxt'><input id="site_password" <%=(isProtected?"disabled":"")%> name="site_password" <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> type="password" maxlength="16" size="30" value="<%=site_password%>" onblur="noBadCharOnBlur(this);" onkeydown='checkBadChar(this,event);'></td>
									</tr>
									<tr>
										<td class="standardTxt"><%=confirmpassword%></td>
										<td><input id="site_confirm_password" name="site_confirm_password" <%=(isProtected?"disabled":"")%> <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> type="password" maxlength="16" size="30" value="" onblur="noBadCharOnBlur(this);" onkeydown='checkBadChar(this,event);'></td>
									</tr>		
								</table>
							</fieldset>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="standardTxt" align="left">(* = <%=lan.getString("alrsched", "allfields")%>)</td>
		</tr>	
	</table>

</form>