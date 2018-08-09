<%@page import="com.carel.supervisor.presentation.svgmaps.SvgMapsUtils"%>
<%@page import="com.carel.supervisor.presentation.sdk.util.CustomChecker"%>
<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.bean.DeviceStructureList"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.bean.DeviceStructure"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.devices.UtilDevice"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.controller.ControllerMgr"
	import="com.carel.supervisor.device.DeviceStatusMgr"
	import="com.carel.supervisor.device.DeviceStatus"
	import="com.carel.supervisor.controller.setfield.SetContext"
  	import="com.carel.supervisor.controller.setfield.SetDequeuerMgr"
  	import="com.carel.supervisor.controller.setfield.DefaultCallBack"
  	import="com.carel.supervisor.device.DeviceStatusMgr"
  	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
  	import="java.io.File"
  	import="org.apache.commons.io.FileUtils"
%>

<jsp:useBean id="devdtl" class="com.carel.supervisor.presentation.devices.DeviceDetail" scope="request"/>
<jsp:useBean id="CurrUnit" class="com.carel.supervisor.presentation.sdk.obj.CurrUnit" scope="session"/>

<%
	CurrUnit.setCurrentSession(ServletHelper.retrieveSession(request.getRequestedSessionId(),request));
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	LangService multiLanguage = LangMgr.getInstance().getLangService(sessionUser.getLanguage());
	String rw_vars = multiLanguage.getString("dtlview","rw_vars");
	String morevars = multiLanguage.getString("dtlview","morevars");
	String jsession = request.getSession().getId();
	
	int idDev = -1;
	try {
		idDev  = Integer.parseInt(sessionUser.getProperty("iddev"));
	}
	catch(NumberFormatException e){}
	
	sessionUser.getTransaction().setIdDevices(new int[]{idDev});
	
	if (!ut.getProperty("cmd").equals(""))
	{
		//From hiddens we retrieve variable indexes, from inputs new values
		String[] var = ServletHelper.retrieveVector(sessionUser.getProperties(),"hidden");
		int[] idVar = new int[var.length];
		for(int i = 0; i < var.length; i++)
			idVar[i] = Integer.parseInt(var[i]);
			
		String[] value = ServletHelper.retrieveVector(sessionUser.getProperties(),"input");
		
		SetContext setContext = new SetContext();
		setContext.setLanguagecode(sessionUser.getLanguage());
	   	setContext.addVariable(idVar, value);
	   	setContext.setCallback(new DefaultCallBack());
	   	setContext.setUser(sessionUser.getUserName());
	   	SetDequeuerMgr.getInstance().add(setContext);
	}
	
	DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
	DeviceStructure deviceStructure = deviceStructureList.get(idDev);
	
	if(deviceStructure != null)
	{
		devdtl.setIdDevice(idDev);
		devdtl.setIdDevMdl(deviceStructure.getIdDevMdl());
		devdtl.setTitle(deviceStructure.getDescription());
		devdtl.setImage(deviceStructure.getImageDevice());
		devdtl.loadDeviceVariable(sessionUser.getLanguage(),sessionUser.getIdSite());
	}
	
	ut.getBoTrx().loadFilter(idDev,devdtl.getIdDevMdl(),sessionUser.getLanguage());
	
	DeviceStatus status = DeviceStatusMgr.getInstance().getDeviceStatus(new Integer(idDev));
	boolean offline = false;
	if (status==null)
	{
		offline = true;
	}
	else
	{
		offline = (!status.getStatus());
	}
	int[] ii = {Integer.parseInt(sessionUser.getProperty("iddev"))};
	
	// Tabella W aperta dopo post
	String openTab = "N";
	try 
	{
		openTab = ut.getProperty("openwt");
		if((openTab == null))
			openTab = "N";
	}
	catch(Exception e){}
	// Fine
	
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	String virtkey = OnScreenKey?"on":"off";
	
	
	//START DTLVIEW SVGMAPS
	
	//check if exist the page for the device model
	boolean exist_svg_devdetail = false;
	String dev_folder = CustomChecker.isDevCustomFor(deviceStructure.getIdDevMdl(),"dtlview",  "webmi.js");
	if (!dev_folder.equalsIgnoreCase("NOP"))  // if svg detail page exist
	{
		exist_svg_devdetail = true;
		//replace webMI String into webMI.js
		String maps_path = System.getenv("PVPRO_HOME")+"/engine/webapps/PlantVisorPRO/custom/dtlview/"+dev_folder;
		SvgMapsUtils.modifyWebMIContext(maps_path+"/webmi.js");
		//unzip maps 
		SvgMapsUtils.unzipMaps(maps_path);
	}
    //END DTLVIEW ATVISE
    
    
%>

<head>
	<base id="basePath" href="<%=basePath%>">
</head>
<!--  AutoRefresh for customized parts secA.jsp and secB.jsp -->
<script type="text/javascript" src="scripts/arch/arkustom.js"></script>
<script>
PVPK_ActiveRefresh();	

function hide_change_view()
{
	var td_button = document.getElementById("changeview");
	td_button.style.visibily="hidden";
	td_button.style.display="none";
}

function change_dtl_view()
{
	var svg = document.getElementById("svg_detail");
	var std = document.getElementById("std_detail");
	//alert(svg.style.visibility);
	if (svg.style.visibility=="hidden")
		{
			svg.style.visibility="visible";
			svg.style.display="block";
			std.style.visibility="hidden";
			std.style.display="none";
		}
	else
		{
		svg.style.visibility="hidden";
		svg.style.display="none";
		std.style.visibility="visible";
		std.style.display="block";
		}
}
</script>
<!-- end -->

<input type='hidden' id='open' value="<%=multiLanguage.getString("htmlfisa","open")%>"/>
<input type='hidden' id='close' value="<%=multiLanguage.getString("htmlfisa","close")%>"/>
<input type="hidden" id="statusoffline" value="<%=offline%>"/>
<input type="hidden" id="topdesc" value="<%=multiLanguage.getString("navbar","ncode11")%>"/>
<input type='hidden' id='vkeytype' value='Numbers' />
<input type='hidden' id='s_maxval' value="<%=multiLanguage.getString("dtlview","s_maxval")%>"/>
<input type='hidden' id='s_minval' value="<%=multiLanguage.getString("dtlview","s_minval")%>"/>
<input type='hidden' id='noteRequired' value="<%=multiLanguage.getString("alrmng","noteRequiredMessage")%>"/>
<input type='hidden' id='virtkeyboard' value='<%=virtkey%>' />


<!--  dtlview svgmaps start -->
<%if(exist_svg_devdetail) {%>

<!-- <button onclick='change_dtl_view()'>change view</button> -->
<div id='svg_detail' style='visibility:visible;display:block'/>
	<iframe id="map" src="./custom/dtlview/<%=dev_folder%>/index.htm?language=en&autofit=true&displaytype=svg&scrolling=no" width="100%" height="100%" frameborder="0" ></iframe>
</div>
<%} else {%>
<script>hide_change_view();</script>
<%} %>
<!--  dtlview svgmaps end -->

<div id='std_detail' <%=exist_svg_devdetail?"style='display:none;visibility:hidden'":""%>/>
<table border="0" cellpadding="1" cellspacing="1" width="97%">
	<tr>
		<td valign="middle" align="left">
			<table border="0" width="100%" cellspacing="0" cellpadding="1">
				<tr>
					<td width="70%" valign="top" align="left" class="tdTitleTable">
					
						<table border="0" width="100%" cellspacing="1" cellpadding="1">
							<tr>
								<td>
									<!-- titolo -->
									<%=CurrUnit.getRefreshableStatusAssint("<img valign='middle' src='images/led/LB0.gif'/>;<img valign='middle' src='images/led/LB1.gif'/>;<img valign='middle' src='images/led/LB2.gif'/>;<img valign='middle' src='images/led/LB3.gif'/>")%>
									<%=devdtl.getTitle()%>
									<table border="0" cellpadding="0" cellspacing="0" width="100%">

										<!-- secA custom area-->
										<tr>
											<td align="center" valign="middle" colspan='3'>
												<jsp:include page="7b.jsp" flush="true" />
											</td>								
										</tr>
										<!-- read only vars -->
										<tr>
											<td align="center" valign="middle" colspan='2'>
												<jsp:include page="1.jsp" flush="true" />
											</td>															
										</tr>
										<tr>
											<td>&nbsp;</td>
										</tr>
									</table>
								</td>
										
								<!-- pic & R/O link -->
								<td width="18%" align="center" valign="top" id="tdImgDev">
									<jsp:include page="2.jsp" flush="true" />
									<!-- ALL VARS: TO REPLACE WITH AN ICON IN THE TOP READ VARS HEADER -->
									<table onclick="openmorevars();" style="cursor: pointer;">
										<!-- <tr height='37px'><td>&nbsp;</td></tr> -->
										<tr>
											<td rowspan="*" width="140px" height="30px" class="groupCategory_medium135" align="center">
												<%=morevars%>
											</td>
										</tr>
									</table>
								</td>
						
							</tr>	
						</table>
						<table border="0" width="100%" cellspacing="0" cellpadding="0">				
							<!-- variabili in scrittura a scomparsa -->
							<tr>
								<td valign="top">
									<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center">
									<tr>
										<td width="*">
											<table border="0" class="table" cellpadding="2" cellspacing="1">
												<tr>
													<th class="writableVarsTH">
														<%=rw_vars%>
													</th>
												</tr>
											</table>
										</td>
									</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td align="center" valign="top">
									<div id="writeTabContainer" style="">
										<FORM name="frmdtl" id="frmdtl" action="servlet/master;jsessionid=<%=jsession%>" method="post" onsubmit="return false;">
											<input type="hidden" id="openwt" name="openwt" value="<%=openTab%>"/>
											<input type='hidden' id='noteInfo' name='noteInfo' value="" />
											<jsp:include page="3.jsp" flush="true" />
										</FORM>
									</div>
								</td>
							</tr>
						</table>
					</td>					
					<!-- BLACK DASHBOARD -->
					<td width="30%" valign="top">
						<div class="dashboard">
							<!-- rounded top -->
							<div class="topleftcorner"></div><div class="toprightcorner"></div>
							
							<!-- secB custom area (opt.)-->
							<div class="customSxArea">
								<jsp:include page="7a.jsp" flush="true" />
							</div>

							<!-- STATUS AREA (LEDS) MULTI-ROW BAR -->
							<div class="statusArea">
								<jsp:include page="8.jsp" flush="true" />
							</div>
						
							<!-- HOME VARS -->
							<div class="homeVars">
								<jsp:include page="6.jsp" flush="true" />
							</div>
							
							<!-- BUTTONS (COMMANDS) MULTI-ROW BAR -->
							<div id="bttContainer" class="bttCommands">
								<jsp:include page="4.jsp" flush="true" />
								<div class='clr'></div>
							</div>
											
							<!-- rounded bottom -->
							<div class="bottomleftcorner"></div><div class="bottomrightcorner"></div>
						</div>
					</td>	
				</tr>
			</table>
		</td>
	</tr>
	<!-- lista allarmi -->
	<tr>
		<td align="center">
			<jsp:include page="5.jsp" flush="true" />
		</td>
	</tr>
</table>
</div>
<div id="textareaWin" class="uploadWin" style="width:446px">
	<div id="uploadwinheader" class="uploadWinHeader">
		<div class="uploadWinClose" onclick="document.getElementById('textareaWin').style.display='none';">X</div><%=multiLanguage.getString("note","insertnote")%> 
	</div>
	<div id="uploadwinbody" class="uploadWinBody">
		<div>
				<div style="width:100%;height:80%;align:center;padding:3px">
					<textarea id="note4setvalue" style="width: 100%;height: 105;Overflow:auto;align:center;" onkeydown="note_filterInput(this,event);" rows="6"></textarea>
				</div>
				<div id="uploadwinbuttons">
					<table border="0">
						<tr>
							<td class="groupCategory_small" style="width:110px;height:30px;" onclick="writeNote();"><%=multiLanguage.getString("dtlview","submit")%></td>
							<td class="groupCategory_small" style="width:110px;height:30px;" onclick="document.getElementById('textareaWin').style.display='none'"><%=multiLanguage.getString("dtlview","cancel")%></td>
						</tr>
					</table>
				</div>
		</div>	
	</div>
</div>