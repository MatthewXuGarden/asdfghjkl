<%@ page language="java"  pageEncoding="UTF-8"
	import="java.util.Date"
	import="java.util.Calendar"
	import="java.util.GregorianCalendar"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.menu.MainMenu"
	import="com.carel.supervisor.presentation.menu.MenuLoader"
	import="com.carel.supervisor.dataaccess.dataconfig.ProductInfo"
	import="com.carel.supervisor.base.conversion.DateUtils"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.support.Information"
	import="com.carel.supervisor.presentation.assistance.GuardianConfig"
	import="com.carel.supervisor.base.config.ProductInfoMgr"
%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String jsession = sessionUser.getSessionId();
	String language = sessionUser.getLanguage();
	String browser  = sessionUser.getUserBrowser();
	MainMenu mainMenu = MenuLoader.create(sessionUser);
	long curTimeMs = System.currentTimeMillis();
	
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	
	// Logo
	IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
	String left_top_img = p_info.get("imgtop");
	
	if (left_top_img==null) 
		left_top_img = "images/top/left.png";
	else
		left_top_img = "images/" + left_top_img.replace("\\","/");
	
	// Default Home
	String default_home = p_info.get("home");
	if (default_home==null||default_home.equals(""))
		default_home = "0";
	String wizarddone = p_info.get("wizarddone");
	if(wizarddone == null || wizarddone.equals("0") == false)
	{
		wizarddone = "1";
	}
	boolean canStartEnginge = false;
	String licenseStatus = "";
	
  try 
  {
  	canStartEnginge = Information.getInstance().valid();
  	if(!canStartEnginge)
  	{
  		canStartEnginge = Information.getInstance().isTrialValid();
  		if(canStartEnginge)
  			licenseStatus = multiLanguage.getString("top","licenseOn");
  		else
  			licenseStatus = multiLanguage.getString("top","licenseOff");
  	}
  }
  catch(Exception e){}
  	
	// Creazione link in System
	String click = "";
	String cursor = "";
	String desc = multiLanguage.getString("menu","mgr");
	
	if(sessionUser.isMenuActive("mgr") && sessionUser.isTabActive("mgr","tab2name"))
	{
		cursor = "cursor:pointer;";
		click = "nop&folder=mgr&bo=BSystem&type=menu&desc="+desc+"&resource=SubTab1.jsp&curTab=tab1name";
	}
	
	// Controllo user manual
	// Valido solo nel caso di EN come default
	String usManLang = p_info.get("usmanlang");
	if(usManLang == null || usManLang.length()== 0)
		usManLang = "EN";
	
	if(!usManLang.equalsIgnoreCase("EN"))
	{
		String curManLang = language.substring(0,2);
		if(curManLang.equalsIgnoreCase("EN"))
			usManLang = "EN";
		else	
			if(curManLang != null && curManLang.equalsIgnoreCase(usManLang))
				usManLang = curManLang;
	}
	//add multi-language support for button hint
	String changepsw = multiLanguage.getString("top","changepsw");
	String help = multiLanguage.getString("top","help");
	String logout = multiLanguage.getString("top","logout");
	String changepswconfirm = multiLanguage.getString("top","changepswconfirm");
	String winclose = multiLanguage.getString("top","exit");
	String wincloseconfirm = multiLanguage.getString("top","exitconfirm");
	// End
%>


<%@page import="com.carel.supervisor.base.config.IProductInfo"%><html>
<head>
	<base href="<%=basePath%>">
  	<title>PlantVisorPRO</title>
  	<meta http-equiv="pragma" content="no-cache">
  	<meta http-equiv="cache-control" content="no-cache">
  	<meta http-equiv="Content-Type" content="text/html; charset=<%=sessionUser.getEncoding()%>">
  	<meta http-equiv="expires" content="0">
  	<link rel="stylesheet" type="text/css" href="stylesheet/plantVisor<%=browser%>.css">
	<script type="text/javascript" src="scripts/arch/Top.js"></script>
	<script type="text/javascript" src="scripts/arch/Timer.js"></script>
</head>
<body bgcolor="#000000" onunload="TiStopClock();" onload="topMove(); TiStartClock('pvproora',<%=curTimeMs%>);" onmousedown="top.frames['manager'].checkServerCom();"  onresize="topMove()">
<input type='hidden' id='jsession' value='<%=jsession%>'/>
<input type='hidden' id='default_home' value='<%=default_home%>'/>
<input type='hidden' id='dvm_wizard' value='-1'/>
<input type='hidden' id='dvm_map' value='-1'/>
<input type='hidden' id='dvm_devices' value='-1'/>
<input type='hidden' id='dvm_global' value='-1'/>
<input type='hidden' id='wizarddone' value='<%=wizarddone %>'/>
<input type='hidden' id='guardian_nav' value='<%=desc%>'/>
<input type='hidden' id='broad_confirm' value='<%=multiLanguage.getString("broad","confirm")%>'/>
<input type="hidden" id="rest"  value="<%=multiLanguage.getString("mgr","restmsg")%>" />
<input type='hidden' id='changepswconfirm' value='<%=changepswconfirm %>'/>
<input type='hidden' id='wincloseconfirm' value='<%=wincloseconfirm %>'/>

<form name="lingua" id="lingua">
	<div style="position: absolute; left: 20px; top: 1px">
		<img src="<%=left_top_img%>" border="0">
	</div>
	<div id="logo" style="position: absolute; top: 0px">
	</div>
	<div class='standardTxt' style="position: absolute; top: 5px; left:160px;" id="loadingMaskContainer"></div>	
	<div id="buy" style="position:absolute;top:1px;" class="standardTxtTop">
		<!--  b><font color="#FFFFFF"><%=licenseStatus%></font></b -->
	</div>
	<div id="guip" title="guardianPRO" style="position:absolute;top:1px;cursor:pointer;visibility:hidden;" onclick="openWinGp();">
		<img id="imgguip" src="images/gpro/gpro_act_nosnd.gif" border="0" />
	</div>
	<div id="changepsw" title="<%=changepsw %>" style="position:absolute;top:1px;cursor:pointer" onclick="changePsw();">
		<img id="imgguip" src="images/top/changepsw.png" border="0" />
	</div>
  	<a id="help" title="<%=help%>" style="position: absolute;top:1px;cursor:pointer;" href="LOC_<%=usManLang%>/PVPROloc_index.htm" target="_blank">
  		<img src="images/top/help_on.png" border="0">
  	</a>
	<div id="exit" title="<%=logout%>" style="position:absolute;top:1px;cursor:pointer" onclick="TopCloseBrowser();">
		<img src="images/top/logout_on.png" border="0" />
	</div>
	<div id="winclose" title="<%=winclose%>" style="position:absolute;top:1px;cursor:pointer" onclick="WinCloseBrowser();">
		<img src="images/top/exit.png" border="0" />
	</div>
</form>
<div style="position:absolute;left:400px;top:0px;color:#000000;width:45%;">
<table border="0" width="100%" cellspacing="1" cellpadding="1">
	<tr>
		<td class="standardTxtTop" width="17%" align="left"><%=(!sessionUser.getUserName().equals("roffline"))?sessionUser.getUserName():""%></td>
		<td class="standardTxtTop" width="27%" align="left"><div id="sitename"><%=sessionUser.getSiteName()%></div></td>
		<td class="standardTxtTop" width="23%" align="center"><%=DateUtils.date2String(new Date(), "yyyy/MM/dd")%></td>
		<td class="standardTxtTop" width="23%" align="center"><div id="pvproora"></div></td>
		<td class="standardTxtTop" width="10%" align="center" rowspan="2"><div id="pvppmstatus"></div></td>
	</tr>
	<tr>
		<td align="center" colspan="2" width="50%" class="warningTxtTop">
			<div id="notify" style="<%=cursor%>" onclick="startEngine('<%=click%>');"></div>
		</td>
		<td align="center" colspan="2" width="50%" class="warningTxtTop">
			<div id="guardian"></div>
		</td>
	</tr>
</table>
</div>
</body>
</html>
