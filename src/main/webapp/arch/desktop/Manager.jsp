<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.menu.MenuLoader"
	import="com.carel.supervisor.presentation.menu.MainMenu"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.presentation.menu.configuration.MenuConfigMgr"
	import="com.carel.supervisor.presentation.menu.MenuVoce"
%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	MainMenu mainMenu = MenuLoader.create(sessionUser);
	MenuVoce voce = MenuConfigMgr.getInstance().getMenuVoce("section4","wizard");
	String wizardmenutext = "nop&folder=wizard&bo=BWizard&type=menu";
	if(voce != null)
	{
		wizardmenutext = voce.getLink();
	}
	voce = MenuConfigMgr.getInstance().getMenuVoce("section5","mstrmaps");
	String mapmenutext = "nop&folder=mstrmaps&bo=BMasterMaps&type=menu";
	if(voce != null)
	{
		mapmenutext = voce.getLink();
	}
	voce = MenuConfigMgr.getInstance().getMenuVoce("section5","deviceview");
	String devicesmenutext = "nop&folder=deviceview&bo=BDeviceView&type=menu";
	if(voce != null)
	{
		devicesmenutext = voce.getLink();
	}
	String msg02 = multiLanguage.getString("usrmsg","msg02");
	String getResolution="";
	if(sessionUser.getProfileRedirect().isReDirect()==true){
		getResolution="collectResolution();";
	}
	int MENU_HEIGHT = MainMenu.MENU_HEIGHT;
  	int MENU_WIDTH = MainMenu.MENU_WIDTH;

  	int HEAD_HEIGHT = MainMenu.HEAD_HEIGHT;
  	int ITEM_HEIGHT = MainMenu.ITEM_HEIGHT;
%>

<html>
  <head>
  <base href="<%=basePath%>">
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="Content-Type" content="text/html; charset=<%=sessionUser.getEncoding()%>" >
  <meta http-equiv="expires" content="0">
  <script type="text/javascript" src="scripts/arch/comm/Communication.js"></script>
  <script type="text/javascript" src="scripts/arch/Manager.js"></script>
  <script>
  var MENU_HEIGHT = <%=MENU_HEIGHT%>;
  var MENU_WIDTH = <%=MENU_WIDTH%>;

  var HEAD_HEIGHT = <%=HEAD_HEIGHT%>;
  var ITEM_HEIGHT = <%=ITEM_HEIGHT%>;
  <%=mainMenu.createJavaScript()%>
  function CreateMenuHtml2()
	{
		var maxvoicenumber = 0;
		var wizardmenutext = '<%=wizardmenutext%>';
		var mapmenutext = '<%=mapmenutext%>';
		var devicesmenutext = '<%=devicesmenutext%>';
		var globalmenutext = 'nop&group=1&folder=grpview&bo=BGrpView&type=menu';
		var html ="";
		var lbl = "";
		var a;
		var voicenumberWizard = -1;
		var voicenumberMap = -1;
		var voicenumberDevices = -1;
		var voicenumberGlobal = -1;
		init();

		html+="<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
		html+="<tr id=\"menutr\">";
		var i,j;
		for(i=0; i<menuMain.menu.length; i++)
		{
			html+="<td style='font-family:Tahoma,Verdana;font-size:9pt;color:white;background-image:url(images/menusx/"+menuMain.menu[i].img+");background-repeat: no-repeat;background-position: center top;' width='90' height='80' align='center' valign='bottom' onmouseover=\"SmMouseOver2(this);\" onmouseout=\"SmMouseOut2(this);\" onclick=SmClick2(this,'divChild"+i+"');>";
			html += "<div style='width:58;height:60;cursor:pointer;'></div>";
			html += menuMain.menu[i].head;
			html+="<div id=\"divChild"+i+"\" class=\"divMenuLeftL2\" style='display:none;'>";
			html+="<table border=\"0\" width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\">";
			html+="<tbody>";
			html+="<tr><td colspan=2 align=\"center\" class='itemHeadPVPRO2' height='"+HEAD_HEIGHT+"px'>"+menuMain.menu[i].head+"</td></tr>";
			for(j=0; j<menuMain.menu[i].item.length; j++)
			{
				lbl = menuMain.menu[i].item[j].label.replace("'", "%27"); // replace ' character with its url encoded value to avoid breaking js and pass it correct to server
				if(j==0)
				{
					html+="<tr class=\"itemPVPRO2\" onmouseover=\"top.frames[\'manager\'].evid(this,'itemoverPVPRO2')\" onmouseout=\"top.frames[\'manager\'].evid(this,'itemPVPRO2')\"><td width='5%'></td><td height='"+ITEM_HEIGHT+"px' width='95%'>";
				}
				else
				{
					html+="<tr class=\"itemPVPRO2\" onmouseover=\"top.frames[\'manager\'].evid(this,'itemoverPVPRO2')\" onmouseout=\"top.frames[\'manager\'].evid(this,'itemPVPRO2')\"><td></td><td height='"+ITEM_HEIGHT+"px'>";
				}
				html+="<div id=\"dvm"+maxvoicenumber+"\"  onclick=\"top.frames[\'menuPVPRO2\'].SmHideMemu();top.frames[\'manager\'].canChangeLink('";
				html+=menuMain.menu[i].item[j].link;
				<%if(sessionUser.getProfileRedirect().isReDirect()){
					String refolder=sessionUser.getProfileRedirect().getReFolder();
				%>
				if(menuMain.menu[i].item[j].link.indexOf("folder=<%=refolder%>") >0){
					document.getElementById("redirectNum").value=maxvoicenumber;
				}
				<%}%>
				if(menuMain.menu[i].item[j].link.indexOf(wizardmenutext) != -1)
				{
					voicenumberWizard = maxvoicenumber;
				}
				else if(menuMain.menu[i].item[j].link.indexOf(mapmenutext) != -1)
				{
					voicenumberMap = maxvoicenumber;
				}
				else if(menuMain.menu[i].item[j].link.indexOf(devicesmenutext) != -1)
				{
					voicenumberDevices = maxvoicenumber;
				}
				else if(menuMain.menu[i].item[j].link.indexOf(globalmenutext) != -1)
				{
					voicenumberGlobal = maxvoicenumber;
				}
				html+="&desc="+lbl+"&comefrom=Menu');\" ><nobr>";
				html+=menuMain.menu[i].item[j].label;
				html+="</nobr></div>";
				html+="</td></tr>";
				maxvoicenumber++;
			}
			if(menuMain.menu[i].bgImg != null && menuMain.menu[i].bgImg != undefined)
			{
				html+="<tr><td colspan=2 height='"+(MENU_HEIGHT-HEAD_HEIGHT-j*ITEM_HEIGHT)+"px' align='center' valign='bottom'><table cellspacing='0' cellpadding='0' style='margin-bottom:20px;'><tr><td width='31px' height='3px' style='background-image:url(images/menusx/bg/"+menuMain.menu[i].bgImg+"_l.png);background-repeat: repeat-y;'></td><td style='background-color:#"+menuMain.menu[i].bgColor+";width:160px;'></td><td width='31px' style='background-image:url(images/menusx/bg/"+menuMain.menu[i].bgImg+"_r.png);background-repeat: repeat-y;'></td></tr></table></td></tr>";
			}
			html+="</tbody>";
			html+="</table>";
			html+="</div>";
			html+="</td>";
		}
		html+="</tr></table>";
		if(voicenumberWizard != -1)
		{
			top.frames["header"].document.getElementById("dvm_wizard").value = voicenumberWizard;
		}
		if(voicenumberMap != -1)
		{
			top.frames["header"].document.getElementById("dvm_map").value = voicenumberMap;
		}
		if(voicenumberDevices != -1)
		{
			top.frames["header"].document.getElementById("dvm_devices").value = voicenumberDevices;
		}
		if(voicenumberGlobal != -1)
		{
			top.frames["header"].document.getElementById("dvm_global").value = voicenumberGlobal;
		}
		return html;
	}
  </script>
</head>
<body onload="setSession('<%=request.getSession().getId()%>');<%=getResolution%>">
<input type="hidden" id="msg02" value="<%=msg02%>"/>
<input type="hidden" id="redirectNum" value=""/>
</body>
</html>