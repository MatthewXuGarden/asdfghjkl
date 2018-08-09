<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.bo.master.IMaster"
	import="com.carel.supervisor.presentation.tabmenu.MenuBuilder"
	import="com.carel.supervisor.presentation.tabmenu.Tab"
	import="com.carel.supervisor.presentation.devices.UtilDevice"
	import="com.carel.supervisor.presentation.menu.configuration.MenuTabMgr"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.base.log.Logger"
	import="com.carel.supervisor.base.log.LoggerMgr"	
	import="com.carel.supervisor.presentation.devices.UtilDevice"
	import="com.carel.supervisor.plugin.customview.CustomViewMgr"
	import="com.carel.supervisor.plugin.customview.CustomView"
	import="com.carel.supervisor.presentation.bean.DeviceStructureList"
	import="com.carel.supervisor.presentation.bean.DeviceStructure"
	import="com.carel.supervisor.presentation.bean.GroupListBean"
	import="com.carel.supervisor.presentation.bean.CustomBeanList" 
	import="com.carel.supervisor.presentation.sdk.util.CustomChecker"
  	import="com.carel.supervisor.presentation.sdk.obj.CurrUser"
	import="com.carel.supervisor.presentation.sdk.obj.CurrUnit"
	import="com.carel.supervisor.controller.pagelinks.PageLinksMgr"
	import="com.carel.supervisor.director.DirectorMgr"
	import="com.carel.supervisor.presentation.menu.configuration.MenuConfigMgr"
	import="com.carel.supervisor.presentation.bo.master.BoMaster"
%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUser = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	IMaster bo = trxUser.getBoTrx();
	String browser  = sessionUser.getUserBrowser();
	String folder = trxUser.getProperty("folder");
	Tab tab = MenuBuilder.getFirstPage(sessionUser,folder,true);
	
	// Vista per tipi di device
	CustomView view = null;
	
	String curTabName = trxUser.getProperty("curTab");
	if ((null == curTabName) || (curTabName.equals("")))
	{
		curTabName = tab.getTabId();
	}
	
	//sessionUser.removeProperty("curTab");
	trxUser.setProtectedTab(sessionUser.isTabProtected(folder, curTabName));
	String resource = trxUser.getProperty("resource");			
	if((resource == null || resource.length() == 0))
	{
		resource = tab.getPage();
		//Kevin add this because in tabmenu.xml, the resource is "note.jsp&tablenote=xxx"
		//(only for) note.jsp is the first page, the server could not find note.jsp&tablenote=xxx
		//the right one is: note.jsp?tablenote=xxx
		if(resource.indexOf("note.jsp&") != -1)
		{
			resource = resource.replaceFirst("note.jsp&","note.jsp?");
		}
	}
	
	// Id Device in sessione
	int idDev = -1;
	boolean isDevLogic = false;
	
	try {
		idDev  = Integer.parseInt(sessionUser.getProperty("iddev"));
	}
	catch(NumberFormatException e){}
	
	// Id DevMdl
	int idDevMdl = -1;
	try
	{
		DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
		DeviceStructure deviceStructure = deviceStructureList.get(idDev);
		if(deviceStructure != null)
		{
			idDevMdl = deviceStructure.getIdDevMdl();
			isDevLogic = deviceStructure.isLogic();
		}
	}
	catch(Exception e){}	
		
	if(folder != null && folder.equalsIgnoreCase("dtlview") &&
		 resource != null && resource.equalsIgnoreCase("SubTab1.jsp"))
	{
		view = CustomViewMgr.getInstance().hasDeviceCustomView(idDevMdl);
	}
		
	// Resource	
	String pathToLoad = "";
	if(sessionUser.forceLogout() && MenuTabMgr.getInstance().lock(folder))
		pathToLoad = "../../arch/include/Message.jsp";
	else
		pathToLoad = "../../app/"+folder+"/"+resource;
	
	// Event
	String eventOnLoad = bo.getEventOnLoad(curTabName);
	if(view != null)
		eventOnLoad = view.getEvents();
	if(eventOnLoad == null)
		eventOnLoad = "";
			
	// Javascript
	String[] javascript = bo.getJavascript(curTabName);
	if(view != null)
		javascript = view.getJavascript();
	
	// DocType
	String docType = bo.getDocType(curTabName);
		
	// ActionBar
	String actionBar = "";
	if ((resource.length() > 14) && (resource.substring(11, 15).equals("note")))
    {
		actionBar = MenuBuilder.buildActionNoteMenu(sessionUser,folder,curTabName);
	}
	else
	{
		if(view != null)
			actionBar = CustomBeanList.getCustomButton(language,view.getIdMdl());
		else	
			actionBar = MenuBuilder.buildActionMenu(sessionUser,folder,curTabName);
	}
	int numCurTabAction = MenuBuilder.getNumActionMenu();
	
	// TabToLoad
	String tab2l = trxUser.getTabToLoad();
	trxUser.resetTabToLoad();
	// IsIEBrowser
	boolean isIE = browser.equalsIgnoreCase("IE");
	
	String totEventOnLoad = "";
	if(tab2l != null && !tab2l.trim().equalsIgnoreCase("-1"))
		totEventOnLoad = eventOnLoad + " MT_BodyClickTab("+tab2l+");return false;";
	else
		totEventOnLoad = eventOnLoad + " forceReloadPoller();";
	
	String wait = multiLanguage.getString("usrmsg","msg02");
	String msg03 = multiLanguage.getString("usrmsg","msg03");
	//combo dispositivi se sono in dettaglio dispositivo
	String devices_combo = "";
	if (folder.equals("dtlview"))
	{
		int[] ids = sessionUser.getTransaction().getIdDevices();
	
		//Intervento Tecnico dovuto al link grafico da LE
		if(ids != null){
			// Alessandro V. : 
			// moved the fix to the upper level because an idDev could be selected also if:
			// - ids is empty (coming from a direct map link to the graph)
			// - ids is not empty (coming from the main page of a device)
			//Nicola Compagno 20102201 Fix on graph plot from map link
			int [] iddevs = new int[] {idDev};
			sessionUser.getTransaction().setIdDevices(iddevs);
			//end fix			
			
			// Alessandro V. : ids array empty
			if (ids.length == 0){
				UserTransaction userTransaction = sessionUser.getCurrentUserTransaction();
			
				String group = "1";
		
				GroupListBean groups = sessionUser.getGroup();
				int[] gids = groups.getIds();
				DeviceStructureList deviceStructureList = groups.getDeviceStructureList(); 
				ids = deviceStructureList.retrieveIdsByGroupsId(gids);
				
				sessionUser.getTransaction().setIdDevicesCombo(ids);
		
			}else{ //Alessandro V. : ids array not empty
				devices_combo = UtilDevice.getDeviceCombo(sessionUser);
			}
		}
	}
	else if(folder.equals("grpview"))
	{
		String group = trxUser.getProperty("group");
		
		int gid = Integer.valueOf(group);
		
		GroupListBean groups = sessionUser.getGroup();
		DeviceStructureList deviceStructureList = groups.getDeviceStructureList(); 
		//2010-5-26, Kevin, if gid is global, set all devices while not left devices(groupid=1)
		int[] ids = null;
		if(gid == 1)
		{
			ids = deviceStructureList.retrieveIdsByGroupsId(groups.getIds());
		}
		else
		{
			ids = deviceStructureList.retrieveIdsByGroupId(gid);
		}
		
		sessionUser.getTransaction().setIdDevices(ids);
		sessionUser.getTransaction().setIdDevicesCombo(ids);
	}
	
	// Commit Action
	String actCommit = bo.getCommitAction(curTabName);
	// If custom no post with key
	if(view != null)
		actCommit = "NOP";
	
	/*
	 * SDK  	
	 * Inizio parte per gestione SDK
	 */
	 
	boolean isCustom = false;
	String devFolder = "NOP";
	if((CustomChecker.useCustom(folder,resource)) || 
		 (!(devFolder = CustomChecker.isDevCustomFor(idDevMdl,folder,resource)).equalsIgnoreCase("NOP")) ||
		 (!(devFolder = CustomChecker.isLogicCustomFor(isDevLogic,idDev,folder,resource)).equalsIgnoreCase("NOP"))
		)
	{
		// Se entro qui dentro allora vuol dire che devo sovrascrivere
		isCustom = true; 
		
		// Riscrivo il path to load
		if(devFolder.equalsIgnoreCase("NOP"))
			pathToLoad = "../../custom/"+folder+"/"+resource;
		else
			pathToLoad = "../../custom/"+folder+"/"+devFolder+"/"+resource;
		
		// Intervento per SDK DEV LOGIC
		if(folder != null && folder.equalsIgnoreCase("dtlview") && isDevLogic)
			pathToLoad = "../../custom/"+folder+"/devicelogic/"+devFolder;
		
		// Cancello la barra dei pulsanti
		actionBar = "";
		
		// Cancello Javascript
		javascript = new String[0];
		
		// Cancello EventOnLoad
		totEventOnLoad = "PVPK_SetCustom('"+folder+"','"+resource+"','"+devFolder+"');PVP_OnLoad('"+folder+"','"+resource+"','"+devFolder+"');";
		if(tab2l != null && !tab2l.trim().equalsIgnoreCase("-1"))
			totEventOnLoad += " MT_BodyClickTab("+tab2l+");return false;";
		else
			totEventOnLoad += " forceReloadPoller();return false;";
		
		// Non so ma pulisco
		numCurTabAction = 0;
	}
	String groupCombox = "";
	if(folder.equals("grpview"))
	{
		String group = trxUser.getProperty("group");
		groupCombox = MenuConfigMgr.getInstance().getGroupCombox(Integer.valueOf(group),sessionUser);
	}
	
	boolean nomenu = sessionUser.getProfileNomenu();
	String quickLinkDiv = "";
	if (!nomenu) 
	{
		quickLinkDiv = PageLinksMgr.getInstance().getPageLinksDiv(folder, resource, sessionUser);
	}
	String backOnclick = sessionUser.getBack();
	boolean hasPadding = true;
	if(folder.equals("mstrmaps"))
		hasPadding = false;
%>
<%=docType%>
<jsp:useBean id="CurrUnit" class="com.carel.supervisor.presentation.sdk.obj.CurrUnit" scope="session"/>
<jsp:useBean id="CurrNode" class="com.carel.supervisor.presentation.sdk.obj.CurrNode" scope="session"/>
<jsp:useBean id="CurrUser" class="com.carel.supervisor.presentation.sdk.obj.CurrUser" scope="session"/>

<%
if(isCustom)
{
	try {
		if(sessionUser.getProperty("iddev")!=null && CurrUnit.getId()!=Integer.parseInt(sessionUser.getProperty("iddev")))
		{
			CurrUnit newCurrUnit = new CurrUnit(Integer.parseInt(sessionUser.getProperty("iddev")));
			session.setAttribute("CurrUnit", newCurrUnit);
		}			
		CurrUnit.setCurrentSession(sessionUser);
	}
	catch(Exception e){}
	
	try {
		CurrNode.setCurrentSession(sessionUser);
	}
	catch(Exception e){}
	
	try {
		CurrUser.setCurrentSession(sessionUser);
	}
	catch(Exception e){}
}
%>
<html>
<head>
	<base href="<%=basePath%>">
  	<meta http-equiv="pragma" content="no-cache">
  	<meta http-equiv="cache-control" content="no-cache">
  	<meta http-equiv="expires" content="0">
  	<meta http-equiv="Content-Type" content="text/html; charset=<%=sessionUser.getEncoding()%>" >
  	  	
  	<link rel="stylesheet" href="stylesheet/plantVisor<%=browser%>.css" type="text/css" />
 	<link rel="stylesheet" href="stylesheet/keyboard.css" type="text/css" />
<%if(isCustom) {%>
	<link rel="stylesheet" href="stylesheet/custom<%=browser%>.css" type="text/css" />
<%}%>

  	<script type="text/javascript" src="scripts/arch/jscolor/jscolor.js"></script>
  	<script type="text/javascript" src="scripts/arch/MenuTab.js"></script>
  	<script type="text/javascript" src="scripts/arch/MenuAction.js"></script>
  	<script type="text/javascript" src="scripts/arch/Refresh.js"></script>
  	<script type="text/javascript" src="scripts/arch/MaskInOut.js"></script>
  	<script type="text/javascript" src="scripts/arch/table/ListView.js"></script>
  	<script type="text/javascript" src="scripts/arch/table/ListViewFisa.js"></script>
  	<script type="text/javascript" src="scripts/arch/table/ListViewDyn.js"></script>
  	<script type="text/javascript" src="scripts/arch/PM.js"></script>
  	<script type="text/javascript" src="scripts/arch/table/ListViewSort.js"></script>
  	<script type="text/javascript" src="scripts/arch/comm/Communication.js"></script>
  	<script type="text/javascript" src="scripts/app/applmask.js"></script>
  	<script type="text/javascript" src="scripts/arch/jt_.js"/></script>
  	<script type="text/javascript" src="scripts/arch/util.js"/></script>
  	
	
<%for(int i=0; i < javascript.length; i++){%>
	<%="<script type=\"text/javascript\" src=\"scripts/app/"+javascript[i]+"\"></script>"%>
<%}%>
	
<%if(isCustom) {%>
	<script type="text/javascript" src="scripts/arch/arkustom.js"></script>
	<script type="text/javascript" src="scripts/custom/custom.js"></script>
<%}%>
	
</head>

<body bgcolor="#FFFFFF" onkeyup="MioCheckModUser(event);MApostWithKey('<%=actCommit%>',event,<%=isCustom%>);" onkeydown="MioLimitUser(event);" onload="MTstopServerComm();MTresizeDivContainer(<%=hasPadding%>); MAButtonNum(<%=numCurTabAction%>);writeBack(); <%=totEventOnLoad%>" onMouseMove="R_resize(event, 2, 0, 0, null, null, null);" onMouseUp="R_resize(event, 0, 0, 0, null, null, null);" onmousedown="top.frames['manager'].checkHideMenuDiv();top.frames['manager'].checkServerCom();MioCheckModUserMouse(event);">

<script>

var quickLinkDiv = "<%=quickLinkDiv%>";
var backOnclick = "<%=backOnclick%>";
function writeBack()
{
	var cContainer = top.frames["menuPVPRO2"].document.getElementById("divNavi");
    var table = "<table height='100%' border=0><tr>";
    if(quickLinkDiv != "")
    {
    	table += "<td style='width:90px;background-image:url(images/menusx/new/home2.png);background-repeat:no-repeat;background-position: center top;cursor:pointer;' onclick=SmClick2(this,'divQuickLink'); >&nbsp;</td>";
    }
    if(backOnclick != "")
    {
        table += "<td style='width:90px;background-image:url(images/menusx/new/back2.png);background-repeat:no-repeat;background-position: center top;cursor:pointer;' onclick=\""+backOnclick+"\">&nbsp;</td>";
    }
    table += "</tr></table>"+quickLinkDiv;
    cContainer.innerHTML = table;
}

function showMenuVeil()
{
	jt_BodyZ.toTop(document.getElementById("crossDivNew"));
	jt_Veil.show(true);
}
function hideMenuVeil()
{
	jt_Veil.show(false);
}
function quicklink(pos)
{
	var link = top.frames['menuPVPRO2'].document.getElementById("pos_" + pos).value;

	if (link == '')
		alert('link not available');
	else
		top.frames['manager'].loadTrx(link);
}
top.frames["allarm"].alarmAJAXRefresh();
</script>
<div id="crossDivNew" class="divMenuContainerPVPRO20" onmousedown="top.frames['manager'].crossDivNewClick();"></div>
<div id="messageDiv" class="divMenuContainer">
	<table border="0" width="100%" cellspacing="1" cellpadding="1">
		<tr>
			<td align="center">Loading...</td>
		</tr>
		<tr>
			<td >&nbsp;</td>
		</tr>		
		<tr>
			<td align="center"><img src="images/progbar.gif"></td>
		</tr>
	</table>
</div>
<!-- add by Kevin, for imposta i18n -->
<%
if(isCustom)
{	String imposta = multiLanguage.getString("button","params");
	String refresha = multiLanguage.getString("button","refresha");
	boolean write_permission = false;
	if(sessionUser!=null)
		write_permission = sessionUser.isButtonActive("dtlview","tab1name","subtab2name");
%>
	<input type="hidden" id="imposta" value="<%=imposta %>"/>
	<input type="hidden" id="refresha" value="<%=refresha %>"/>
	<input type="hidden" id="write_permission" value="<%=write_permission %>"/>
<% } %>
<% if(isIE){ %>
<iframe id="crossDivI" class="iMenuContainer"></iframe>
<iframe id="messageDivI" class="iMessageContainer"></iframe>
<% } %>

<table border="0" width="100%" height="100%" cellspacing="0" cellpadding="0" >
	<tr>
		<td width="100%" align="right" valign="top">
			
			<div class="tabBodyStyle">
			<%if(devices_combo != "") {%>
				<div id='sdkActionCombo' class="tabDevicesCombo"><%=devices_combo%></div>
			<%}%>
			<%if(groupCombox != "") {%>
				<div id='sdkActionCombo' class="tabGroupCombo"><%=groupCombox%></div>
			<%}%>
			<%if( ("vs".equalsIgnoreCase(folder) && "SubTab1.jsp".equalsIgnoreCase(resource))
				|| ("opt_startstop".equalsIgnoreCase(folder) && "SubTab4.jsp".equalsIgnoreCase(resource)) ) {%>
				<div id='sdkActionCombo' class="tabDevicesCombo">
					<table>
						<tr>
							<td>
								<img id="selCategory_icon" style="width:24px;height:24px;background-color:white;">
							</td>
							<td style="padding-left:10px;">
								<select id="selCategory" class="devices_combo"></select>
							</td>
							<td style="padding-left:10px;">
								<select id="selScheduleType" class="devices_combo"></select>
							</td>
						</tr>
					</table>				
				</div>
			<%}%>
			<%if(actionBar!= null) {%>
		  		<div id='sdkActionButton' class="tabActBtn10"><%=actionBar%></div>
			<%}%>
			</div>
			
    		<div class="tabBodyBottomShadow">&nbsp;</div>
	  </td>
	</tr>
	
	<tr height="100%" id="trContainer">
		<td>
			<div id="container" class="divContainerTrx <%=hasPadding?"divContainerTrxLeft":""%>">
				<% try { %>
					<jsp:include page="<%=pathToLoad%>" flush="true" />
				<% }
				catch(Exception e)
				{
					Logger logger = LoggerMgr.getLogger(this.getClass());
					logger.error(e);
					%>
					<jsp:include page="Error.jsp" flush="true" />
					<%
				}
				trxUser.setProtectedTab(false);
				%>
			</div>
		</td>
	</tr>
</table>

<form id="frmtabbody">
	<input type="hidden" id="msg03" value="<%= msg03%>"/>
	<input type="hidden" id="s_commit" value="<%=multiLanguage.getString("main","s_commit")%>"/>
	<input type="hidden" id="s_notallowedchar" value="<%=multiLanguage.getString("mask","notallowedchar")%>"/>
</form>		
	<%if(sessionUser.getProfileRedirect().isReDirect()){
		String reTabId=sessionUser.getProfileRedirect().getReTabId();
		sessionUser.getProfileRedirect().setReDirect(false);
		if((reTabId!=null) && (!"".equals(reTabId))){
	%>		
	<script>
		top.frames["body"].frames['TabMenu'].document.getElementById("<%=reTabId%>").onclick();	
	</script>
	<%}}%>

<div id="divBalloon" class="divBalloon">
	<table id="tableBalloon" class="tableBalloon">
		<tr valign="middle">
			<th class="StandardTxt">&nbsp;</th>
			<th align="right"><img src="images/close.png" onClick="hideBalloon()"/></th>
		</tr>
		<tr valign="middle">
			<td colspan="2" class="StandardTxt">&nbsp;</td>
		</tr>
		<tr valign="middle">
			<td colspan="2" class="StandardTxt">&nbsp;</td>
		</tr>
	</table>
</div>

</body>
</html>
