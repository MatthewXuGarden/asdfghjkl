<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.bo.master.BoMaster"
	import="com.carel.supervisor.base.factory.FactoryObject"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.devices.*"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.devices.DeviceList"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.director.graph.GraphConstant"
	import="com.carel.supervisor.presentation.bean.ConfigurationGraphBeanList"
	import="com.carel.supervisor.presentation.bean.ConfigurationGraphBean"
	import="com.carel.supervisor.presentation.bean.GraphBeanList"
	import="com.carel.supervisor.presentation.bean.GraphBean"
	import="com.carel.supervisor.director.graph.FlashObjParameters"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	if( !ServletHelper.validateSession(sessionUser) )
		response.sendRedirect("Logout.jsp");
	String jsession = request.getSession().getId();
	LangService lang = LangMgr.getInstance().getLangService(sessionUser.getLanguage());

	// create transaction
	UserTransaction ut = new UserTransaction("BDtlView", "");
	BoMaster oMaster = (BoMaster)FactoryObject.newInstance("com.carel.supervisor.presentation.bo." + ut.getTrxName(),
		new Class[] { String.class }, new Object[] { sessionUser.getLanguage() });
	ut.setBoTrx(oMaster);
	ut.setProperty("curTab", "tab5name");
	ut.setProperty("resource", "/mobile/DeviceGraph.jsp");
	sessionUser.addNewUserTransaction(ut);
	
	int idDev = Integer.parseInt(sessionUser.getProperty("iddev"));
	sessionUser.getTransaction().setIdDevices(new int[] { idDev });
	DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
	DeviceStructure deviceStructure = deviceStructureList.get(idDev);
	String strDeviceDescription = "";
	if( deviceStructure != null ) {
		strDeviceDescription = deviceStructure.getDescription();
	}
	
	UserTransaction trxUserLoc = sessionUser.getCurrentUserTransaction();
	String group = trxUserLoc.getProperty("group");
	
	// ENHANCEMENT 20090224
	// If the time-window parameters are stored in the user's session, we use them. Else, we shall use the default,
	// which is related only to the TimePeriod parameter (stored in the database)
	String trxTimePeriod = trxUserLoc.getProperty("sesHistTimePeriod");
	String trxMainTime = trxUserLoc.getProperty("sesHistMainTime");

	String language = sessionUser.getLanguage();
	
	//////////////////////////////////FRAME 1/////////////////////////////////////
	StringBuffer tableMerged = new StringBuffer();
	StringBuffer tableInvisibleSx= new StringBuffer();
	
	//Traduzioni in lingua
	LangService langService = LangMgr.getInstance().getLangService(language);

  	String variablePresentMsg = langService.getString("GraphVariablePage","variablepresentmsg");
	String variableMaxMsg = langService.getString("GraphVariablePage","variablemaxmsg");
	String variableDeleteMsg = langService.getString("GraphVariablePage","variabledeletemsg");

	String deviceLabel = langService.getString("graphvariable","device");
	String variableLabel = langService.getString("graphvariable","variable");
	
	String printDataLabel=langService.getString("graphvariable","printdatalabel");
	String userDataLabel=langService.getString("graphvariable","userdatalabel");

	String startDateLabel=langService.getString("graphvariable","startdatelabel");
	String rangeLabel=langService.getString("graphvariable","rangelabel");
	
	String waitPlotMsg=langService.getString("graphvariable","waitplotmsg");
		
	String sysTimeLabelUser = langService.getString("graphvariable","systimelabeluser");
	String sysTimeLabelSO = langService.getString("graphvariable","systimelabelso");
	String rangeMonthLabel =langService.getString("graphvariable","rangemonthlabel");
	String rangeDaysLabel = langService.getString("graphvariable","rangedayslabel");
	String rangeHoursLabel = langService.getString("graphvariable","rangehourslabel");
	String rangeMinLabel = langService.getString("graphvariable","rangeminlabel");
	String loadErrLabel = langService.getString("graphvariable","loaderrlabel");
	String dblclickLabel = langService.getString("graphvariable","dblclick");
	String thDevLabel = langService.getString("graphvariable","thdev");
	String thColorLabel = langService.getString("graphvariable","thcolor");
	String thVarLabel = langService.getString("graphvariable","thvar");
	String thUnitOfMeasureLabel = langService.getString("graphvariable","thum");
	String thMinLabel = langService.getString("graphvariable","thmin");
	String thMaxLabel = langService.getString("graphvariable","thmax");
	String thAvgLabel = langService.getString("graphvariable","thavg");
	String thLowerLimitLabel = langService.getString("graphvariable","thrangemin");
	String thUpperLimitLabel = langService.getString("graphvariable","thrangemax");
	
	String blockdel = langService.getString("graphvariable","blockdel");
	String blockfilter = langService.getString("graphvariable","blockfilter");
	String blockttsav = langService.getString("graphvariable","blockfilterttsave");
	String blockttdel = langService.getString("graphvariable","blockfilterttdel");
	
	String dataerrormsg = langService.getString("ConfGraph","dataerrormsg");
	
	// graph search dialog
	String title = langService.getString("graphvariable","opengraphdialog");
	String timePeriodLabel = langService.getString("graphdata1","timeperiod");
	String yearLabel = langService.getString("graphdata1","year");
	String month1Label = langService.getString("graphdata1","month1");
	String day1Label = langService.getString("graphdata1","day1");
	String hour1Label = langService.getString("graphdata1","hour1");
	String deviceListLabel = langService.getString("graphdata1","deviceList");

	String gen = langService.getString("cal","january");
	String feb = langService.getString("cal","february");
	String mar = langService.getString("cal","march");
	String apr = langService.getString("cal","april");
	String mag = langService.getString("cal","may");
	String giu = langService.getString("cal","june");
	String lug = langService.getString("cal","july");
	String ago = langService.getString("cal","august");
	String set = langService.getString("cal","september");
	String ott = langService.getString("cal","october");
	String nov = langService.getString("cal","november");
	String dic = langService.getString("cal","december");
	
	String nodeName=sessionUser.getSiteName();
	String userName=sessionUser.getUserName();
	
	Long servermaintime = System.currentTimeMillis();
	servermaintime -= (servermaintime % (60 * 1000)); // minute alignment
	
	String graphType = request.getParameter("type");
	String haccpSelected = "";
	String historicalSelected = "";
	if(graphType == null || !graphType.startsWith(GraphConstant.TYPE_HACCP))
	{
		graphType = GraphConstant.TYPE_HISTORICAL;
		historicalSelected = "selected";
	}
	else
	{
		graphType = GraphConstant.TYPE_HACCP;
		haccpSelected = "selected";
	}

	//Impostazioni relative al gruppo distinte dal dettaglio visto l'ingresso in architettura
	ConfigurationGraphBeanList configurationGraphBeanList= new ConfigurationGraphBeanList(graphType);
	Integer idGroup=null;
	Integer idDevice=null;
	if(!group.equals(""))
		idGroup= new Integer(group);
	else
		idDevice= new Integer(sessionUser.getProperty("iddev"));
	
	// If in global graph, don't display combo blocks
	String displayComboBlocks = "T";
	if(idDevice == null)
		displayComboBlocks = "F";	
	
	configurationGraphBeanList.loadConfigurationPage(null,language,new Integer(sessionUser.getIdSite()),new Integer(sessionUser.getProfile()),
																									 idGroup,
																									 idDevice);
																						 
	ConfigurationGraphBean []configurationGraphBeans= configurationGraphBeanList.getConfigurationGraphBeans();  
	if(configurationGraphBeans!=null){
		for(int i=0;i<configurationGraphBeans.length;i++){
			if (configurationGraphBeans[i] != null) {
				// ENHANCEMENT 20090212 Each row is identified by the "id" attribute. This attribute contains
				// a unique identifier ("is"+varId)
				
				String yMin="";
				String yMax="";
				if((configurationGraphBeans[i].getYMin()!=null)&&(configurationGraphBeans[i].getYMax()!=null)
					&& (configurationGraphBeans[i].getYMin().floatValue()!=configurationGraphBeans[i].getYMax().floatValue())){
					yMin=configurationGraphBeans[i].getYMin().toString();
					yMax=configurationGraphBeans[i].getYMax().toString();					
				}//if						
				
				tableInvisibleSx.append("<tr id='is"+configurationGraphBeans[i].getIdVariable()+"'>");
				tableInvisibleSx.append("<td>");
				tableInvisibleSx.append(configurationGraphBeans[i].getDeviceDescription());
				tableInvisibleSx.append("</td>");
				tableInvisibleSx.append("<td>");
				tableInvisibleSx.append(configurationGraphBeans[i].getIdDevice());
				tableInvisibleSx.append("</td>");
				tableInvisibleSx.append("<td>");
				tableInvisibleSx.append(configurationGraphBeans[i].getIdVariable());
				tableInvisibleSx.append("</td>");
				tableInvisibleSx.append("<td>");
				tableInvisibleSx.append(configurationGraphBeans[i].getVariableDescription());
				tableInvisibleSx.append("</td>");
				tableInvisibleSx.append("<td>");
				tableInvisibleSx.append(configurationGraphBeans[i].getVariableType());
				tableInvisibleSx.append("</td>");
				tableInvisibleSx.append("<td>");
				tableInvisibleSx.append(configurationGraphBeans[i].getUnitOfmeasure());
				tableInvisibleSx.append("</td>");
				tableInvisibleSx.append("<td>");
				tableInvisibleSx.append(configurationGraphBeans[i].getColor());
				tableInvisibleSx.append("</td>");
				tableInvisibleSx.append("<td>");
				tableInvisibleSx.append(yMin);
				tableInvisibleSx.append("</td>");
				tableInvisibleSx.append("<td>");
				tableInvisibleSx.append(yMax);
				tableInvisibleSx.append("</td>");
				
				tableInvisibleSx.append("</tr>"); 
				
				// Merged visible table			
				
				// row with id=varId
				tableMerged.append("<tr id='").append(configurationGraphBeans[i].getIdVariable())
					.append("' title='"+dblclickLabel+"' class=\"graphVarTableRow\" onclick=\"evidenceCurve('")
					.append(configurationGraphBeans[i].getIdVariable()).append("')\" ondblclick=\"editRow('")
					.append(configurationGraphBeans[i].getIdVariable()).append("');\" >");
				
				// cell[0] checkbox
				tableMerged.append("<td class='td' style='width:60px'><input id ='chkbox").append(configurationGraphBeans[i].getIdVariable())
					.append("' class='bigcheck' type='checkbox' checked onclick=\"toggleVariable('").append(configurationGraphBeans[i].getIdVariable())
					.append("')\" /></td>");
				
				// cell[1] device description
				tableMerged.append("<td class='td' style='width:150px'>").append(configurationGraphBeans[i].getDeviceDescription()).append("</td>");
				
				// cell[2] color (Visible column) (Alessandro: id attribute added for using with color picker)
				tableMerged	.append("<td class='td' style='width:70px'><table style='border-color: black;height:14px;width:100%;' border='1' celpadding='0' cellspacing='0'><tbody><tr><td id='txtcolor")
							.append(configurationGraphBeans[i].getIdVariable())
							.append("' style='height:40%;background-color:#")
							.append(configurationGraphBeans[i].getColor())
							.append("'></td></tr></tbody></table></td>");
				
				// cell[3] variable description
				tableMerged.append("<td class='td' style='width:310px'>").append(configurationGraphBeans[i].getVariableDescription()).append("</td>");
				
				// cell[4] u.m.
				tableMerged.append("<td align='center' class='td' style='width:70px'>")
					.append(configurationGraphBeans[i].getUnitOfmeasure()==null? " ":configurationGraphBeans[i].getUnitOfmeasure())
					.append("</td>"); // unit of measure
					
				// cell[5] y min value	
				tableMerged.append("<td align='center' class='td' style='width:128px'>--</td>");
				
				// cell[6] y max value
				tableMerged.append("<td align='center' class='td' style='width:128px'>--</td>");
				
				// cell[7] y average value
				tableMerged.append("<td align='center' class='td' style='width:70px'>--</td>");			
	
				// 20090310 - We write "autoscale" instead of blank text
				if (configurationGraphBeans[i].getVariableType() != 1 && configurationGraphBeans[i].getVariableType() != 4
					&& ("".equals(yMin) || "NaN".equals(yMin))) {
					// if yMin = "", yMax too should be "". We write "autoscale" (untranslated!) in the field
					// That's true only for analog or integer variables
					yMin = "autoscale";
					yMax = "autoscale";
				}			
				
				// cell[8] lower range value
				tableMerged.append("<td align='center' class='td' style='width:100px'>").append(yMin).append("</td>");
				
				// cell[9] upper range value
				tableMerged.append("<td align='center' class='td' style='width:100px'>").append(yMax).append("</td>");
	
				// cell[10] device ID. hidden column
				tableMerged.append("<td style='display:none;'>").append(configurationGraphBeans[i].getIdDevice()).append("</td>");
				
				// cell[11] type. hidden column
				tableMerged.append("<td style='display:none;'>").append(configurationGraphBeans[i].getVariableType()).append("</td>");
	
				// cell[12] Color. hidden column.
				tableMerged.append("<td style='display:none;'>").append(configurationGraphBeans[i].getColor()).append("</td>");
			} // end "if (configurationGraphBeans[i] != null)"
		}//for

	}//if
	
	//Impostazioni di default del grafico
	String vf="true";
	String vfbc=GraphConstant.V_FINDER_BG_COLOR;
	String vffc=GraphConstant.V_FINDER_FG_COLOR;
	String xgrd="true";
	String ygrd="true";
	String grdc=GraphConstant.GRID_COLOR;
	String grfc=GraphConstant.GRAPH_BG_COLOR;
	String axisc=GraphConstant.AXIS_COLOR;
	configurationGraphBeanList= new ConfigurationGraphBeanList(graphType);
	configurationGraphBeanList.loadConfigurationPageCosmeticGraph(null,language,new Integer(sessionUser.getIdSite()),new Integer(sessionUser.getProfile()),idGroup,idDevice);
	configurationGraphBeans= configurationGraphBeanList.getConfigurationGraphBeans();
	if(configurationGraphBeans!=null){
		if(configurationGraphBeans[0].getViewFinderCheck()!=null){
		vf=(configurationGraphBeans[0].getViewFinderCheck().trim().equals("TRUE")?new String("true"):new String("false"));
		xgrd=(configurationGraphBeans[0].getXGridCheck().trim().equals("TRUE")?new String("true"):new String("false"));
		ygrd=(configurationGraphBeans[0].getYGridCheck().trim().equals("TRUE")?new String("true"):new String("false"));
		vfbc=configurationGraphBeans[0].getViewFinderColorBg();
		vffc=configurationGraphBeans[0].getViewfinderColorFg();
		grdc=configurationGraphBeans[0].getGridColor();
		grfc=configurationGraphBeans[0].getBgGraphColor() ;
		axisc=configurationGraphBeans[0].getAxisColor();
		}//if	
	}//if
	
	//////////////////////////////////FRAME 2/////////////////////////////////////
	String periodCode="0";
	String s_iddev = sessionUser.getProperty("iddev");
	ut.setGraphParameter(null);
	String sessionName="graph";
	configurationGraphBeanList= new ConfigurationGraphBeanList(graphType);
	configurationGraphBeanList.loadConfigurationPagePeriod(null,language,new Integer(sessionUser.getIdSite()),new Integer(sessionUser.getProfile()),idGroup,idDevice);
	configurationGraphBeans= configurationGraphBeanList.getConfigurationGraphBeans();  
	if(configurationGraphBeans!=null){
		short periodCodeStart=8;
		periodCodeStart-=configurationGraphBeans[0].getPeriodCode().shortValue();
			periodCode= new Short(periodCodeStart).toString();
	}//if

	String minut5Label=langService.getString("graphdata1","minut5");
	String minut15Label=langService.getString("graphdata1","minut15");
	String minut30Label=langService.getString("graphdata1","minut30");
	String hourLabel =langService.getString("graphdata1","hour");
	String hour6Label =langService.getString("graphdata1","hour6");
	String hour12Label =langService.getString("graphdata1","hour12");
	String dayLabel=langService.getString("graphdata1","day");
	String weekLabel=langService.getString("graphdata1","week");
	String monthLabel=langService.getString("graphdata1","month");

  
	int[] devicesId = ut.getIdDevices();
	if(devicesId!=null)
		if(devicesId.length==1)
			if(s_iddev!=null)
				if(!s_iddev.equals("")){
					devicesId[0]=(new Integer(s_iddev)).intValue();
					ut.setIdDevices(devicesId);
				}//if
	
    StringBuffer deviceList = new StringBuffer();
    GraphBeanList graphBeanList = new GraphBeanList();
    graphBeanList.loadDeviceList(null,sessionUser.getIdSite(),devicesId,language); 
    GraphBean[] graphBeans = graphBeanList.getGraphBeans();
    
    if(graphBeans!=null){
      int dim = graphBeans.length;
      if (dim>1)
      {
        deviceList.append("<option value=\"0\" >--------------------</option>\n");
      }//if
      for(int i = 0; i < dim; i++)
      {
        deviceList.append("<option value=\"");
        deviceList.append(graphBeans[i].getIdDevice());
        deviceList.append("\">");
        deviceList.append(graphBeans[i].getDeviceDescription());
        deviceList.append("</option>\n");
      }//for   
    }//if
    else{
    	deviceList.append("<option value=\"0\" >--------------------</option>\n");
    }//else
    
    String folder = trxUserLoc.getProperty("folder");
	String devices_combo = null;
	if (folder.equals("dtlview"))
	{
		int[] ids = ut.getIdDevices();
		if (ids.length>0)
		{		
			devices_combo = UtilDevice.getDeviceCombo(sessionUser);
		}	
	}
    //String automaticPlot=folder.equals("dtlview")?"true":"false";
    String automaticPlot="true"; 
    int currScreenWidth = sessionUser.getScreenWidth();
    int currScreenHeight = sessionUser.getScreenHeight();
    FlashObjParameters flashObjParameters = null;
    if (currScreenWidth == 1024)
    	flashObjParameters= new FlashObjParameters(0,0,currScreenHeight,currScreenWidth,260,35);
    else
    	flashObjParameters= new FlashObjParameters(0,0,currScreenHeight,currScreenWidth,420,35);
	
	// graph dialog
	StringBuffer year = new StringBuffer();
	for(int i = 2000; i < 2050; i++){
		year.append("<option value=\"");
		year.append(i);
		year.append("\">");
		year.append(i);
		year.append("</option>");
	}//for
	
	StringBuffer day = new StringBuffer();
	for(int i = 1; i < 32; i++){
		day.append("<option value=\"");
		day.append(i);
		day.append("\">");
		day.append(i<10?"0"+i:""+i);
		day.append("</option>");
	}//for
	
	StringBuffer hour = new StringBuffer();
	for(int i = 0; i < 24; i++){
		hour.append("<option value=\"");
		hour.append(i);
		hour.append("\">");
		hour.append(i<10?"0"+i:""+i);
		hour.append("</option>");
	}//for
	
	StringBuffer minut = new StringBuffer();
	for(int i = 0; i < 60; i++){
		minut.append("<option value=\"");
		minut.append(i);
		minut.append("\">");
		minut.append(i<10?"0"+i:""+i);
		minut.append("</option>");
	}//for
    
    
%>
<html>

<head>
	<title>PVPRO</title>
	<base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
	<meta name="format-detection" content="telephone=no">
	<link rel="stylesheet" href="mobile/stylesheet/mobile.css" type="text/css">
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
	<script type="text/javascript" src="mobile/scripts/arch/util.js"/></script>
    <script type="text/javascript" src="mobile/scripts/flot/jquery.js"></script>
    <script type="text/javascript" src="mobile/scripts/flot/jquery.flot.js"></script>
	<script type="text/javascript" src="mobile/scripts/flot/jquery.flot.crosshair.js"></script>
	<script type="text/javascript" src="mobile/scripts/flot/jquery.flot.selection.js"></script>
	<!--[if lte IE 8]><script language="javascript" type="text/javascript" src="mobile/scripts/flot/flashcanvas/flashcanvas.js"></script><![endif]-->
	<script type="text/javascript" src="mobile/scripts/app/graphdialog_support.js"></script>
	<script type="text/javascript" src="mobile/scripts/app/graph.js"></script>
	<script type="text/javascript" src="mobile/scripts/device.js"></script>
</head>

<body onLoad="MTstopServerComm(); initialize(); forceReloadPoller();">
<table id="tableTop" class="tableTop">
	<tr>
		<td>
			<select id="selectPage" name="selectPage" class="selectTop" onChange="onSelectPage(this.value)">
				<option value="DeviceMain.jsp"><%=lang.getString("mobile", "main")%></option>
				<option value="DeviceReadOnly.jsp"><%=lang.getString("mobile", "all_RO")%></option>
				<option value="DeviceParameters.jsp"><%=lang.getString("mobile", "parameters")%></option>
				<option value="DeviceAlarms.jsp"><%=lang.getString("mobile", "alarms")%></option>
				<option value="DeviceGraph.jsp?type=<%=GraphConstant.TYPE_HACCP%>" <%=haccpSelected%>><%=lang.getString("dtlview", "tab4name")%></option>
				<option value="DeviceGraph.jsp?type=<%=GraphConstant.TYPE_HISTORICAL%>" <%=historicalSelected%>><%=lang.getString("dtlview", "tab5name")%></option>
			</select>
		</td>
	</tr>
</table>
<table width="100%">
	<tr>
		<td width="*" class="standardTxt"><b><%=strDeviceDescription%></b></td>
		<td width="24px"><img src="mobile/images/actions/graph_on_black.png" style="cursor:pointer;" onClick="saveAndPlot();"></td>
		<td width="60px" class="standardTxt"><b><%=lang.getString("mobile", "plot")%></b></td>
		<td width="24px"><img src="mobile/images/actions/graph_settings_on_black.png" style="cursor:pointer;" onClick="curveSelection(true);"></td>
		<td width="60px" class="standardTxt"><b><%=lang.getString("mobile", "curve_selection")%></b></td>		
	</tr>
</table>

<input type="hidden" id="servermaintime" name="servermaintime" value="<%=servermaintime%>" />
<table width="100%" cellspacing="0" cellpadding="0">
	<tr valign="top" align="left">
		<td align="left" >
			<table width="100%" cellspacing="0" cellpadding="0">
				<tr valign="top" align="left" >
					<td align="left">
						<!--inizio fontInfo-->
						<div id='fontInfo' style='display:none;'>
					    	<table cellspacing="0" cellpadding="0">
						      	<tr>
	                				<td><img src='\PlantVisorPRO\images\event\alert.gif'></td>
	                				<td style='font-size:10px;color:#ff0000;padding-left:10px'>The font used in this graphic object is not installed on your computer, the texts may not display correctly.<br>Please, install <i>"Arial Unicode MS"</i> from operating system's CD.</td>
	                			</tr>
                			</table>
            			</div>
            			<!--fine fontInfo-->
            			
            		<!-- graph canvas -->
            		<div id="divGraphCanvas" style="display:block;width:100%;">
						<div id="placeholder_a" style="height:375px;width:640px;">
						</div>
						<div id="placeholder_d" style="height:75px;width:640px;">
						</div>
					</div>					
					<!--
					<div id="divFlashObj" align="left" style="height:*;width:100%;">
						<object 
							    id="flashObj" width="100%" height="<%=flashObjParameters.getHEIGHT()%>"
							    classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" 
					 	    	codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0">
								<param name="allowScriptAccess" value="sameDomain" />
								<param name="movie" value="flash/graph/grafico.swf?vf=<%=vf%>&xgrd=<%=xgrd%>&ygrd=<%=ygrd%>&vfbc=<%=vfbc%>&grdc=<%=grdc%>&vffc=<%=vffc%>&grfc=<%=grfc%>&axisc=<%=axisc%>&flashwidth=<%=flashObjParameters.getWIDTH()%>&flashheight=<%=flashObjParameters.getHEIGHT()%>"/>
								<param name="quality" value="high" />
								<param name="bgcolor" value="#ffffff" />
								<param name="wmode" value="transparent"/>
								<param name="menu" value="false" />
								<embed menu="false" swliveconnect="true" name="flashObj" src="flash/graph/grafico.swf?vf=<%=vf%>&xgrd=<%=xgrd%>&ygrd=<%=ygrd%>&vfbc=<%=vfbc%>&grdc=<%=grdc%>&vffc=<%=vffc%>&grfc=<%=grfc%>&axisc=<%=axisc%>&flashwidth=<%=flashObjParameters.getWIDTH()%>&flashheight=<%=flashObjParameters.getHEIGHT()%>" style='z-index:1;'
								wmode='transparent' quality="high" bgcolor="#ffffff" width="100%" 
								height="<%=flashObjParameters.getHEIGHT()%>" name="Grafico" swLiveConnect="true" align="middle" valign="center"
								allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
						</object>
					</div>
					 -->
					</td>
				</tr>
				<tr>
					<td width="100%" >
						<table width="100%" cellspacing="0" cellpadding="0">
							<tr valign="top" align="left">
								<td>
									<table width="100%" cellspacing="0" cellpadding="0">
										<tr>
											<td width="100%">
												<div id='timeTable' style='display:none;'>
													<table>
														<tr>
															<td>
															    <select id="timeperiod" name="timeperiod" onchange="changeTimePeriod()">
																	<option value="<%=GraphConstant.MINUTS5%>"><%=minut5Label%></option>
																	<option value="<%=GraphConstant.MINUTS15%>"><%=minut15Label%></option>
																	<option value="<%=GraphConstant.MINUTS30%>"><%=minut30Label%></option>
																	<option value="<%=GraphConstant.HOUR%>"><%=hourLabel%></option>
																	<option value="<%=GraphConstant.HOURS6%>"><%=hour6Label%></option>
																	<option value="<%=GraphConstant.HOURS12%>"><%=hour12Label%></option>
																	<option value="<%=GraphConstant.DAY%>"><%=dayLabel%></option>
																	<option value="<%=GraphConstant.WEEK%>"><%=weekLabel%></option>
																	<option value="<%=GraphConstant.MONTH%>"><%=monthLabel%></option>
																	<option value="-1">--------------------</option>
																</select>
														  		<input type="hidden" name="year" id="year" onchange="resetMainTime()" />                
																<input type="hidden" name="month" id="month" onchange="resetMainTime()" />
																<input type="hidden" name="day" id="day" onchange="resetMainTime()" />
																<input type="hidden" name="hour" id="hour" onchange="resetMainTime()" />
																<input type="hidden" name="minut" id="minut" onchange="resetMainTime()" />     
																<div id='listaDevices'>
																	<select name="deviceList" id="deviceList" onchange="changeDevice(this.value);">
																		<%=deviceList.toString()%>
																	</select>
																</div>
															</td>
														</tr>  
													</table>
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<div id='buttonTable'>
									<table border="0" width="100%">
										
										<tr valign="middle">
											
											<td style='width:100px;height:35px'>
												<div id="divBeginTime">&nbsp;</div>
												<!--
												<div id="blockLabelContainer" style="height:35px;display:block;visibility:<%= (displayComboBlocks.equalsIgnoreCase("T")?"visible":"hidden") %>">
													<table border="0" cellpadding="0" cellspacing="10" width="70%">
														<tr>
															<td class="standardTxt">
																<div class="graphComboOther">
																<nobr><%=blockfilter%></nobr>
																</div>
															</td>
															<td class="standardTxt">
																<div id="layer_select_block" class="graphComboSelect">
																	<select id="grouplabel" style='width:110px;' onchange="mgManageEditSel();applyBlockCheckbox(this);">
																		<option value="0"></option>
																	</select>
																</div>
																<div id="layer_select_block_text" class="graphComboInput">
																	<input type="text" class="standardTxt" name="grouplabelText" id='grouplabelText' maxlength="15">
																</div>
															</td>
															<td>
																<div class="graphComboOther">
																<img alt="<%=blockttsav %>" src="images/actions/okbig_on.png" style="cursor:pointer;" onclick="mgAddGroup();">
																</div>
															</td>
															<td>
																<div class="graphComboOther">
																<img alt="<%=blockttdel %>" src="images/actions/removebig_on.png" style="cursor:pointer;" onclick="mgDelGroup();">
																</div>
															</td>
														</tr>
													</table>
												</div>
												 -->
											</td>
											
											<td class="groupCategory_small" style='width:110px;height:30px' onclick="OpenGraphDialog();">
												<b><%=langService.getString("graphvariable","opengraphdialog")%></b>
											</td>
											
											<td style="height:25px;width:320px;">										
										        <table width="100%">
										        	<tr>
											    	    <td align="center"><img id="zoomOutImg" src="images/graph/zoomout_on.png"  style="cursor:pointer;" onclick="backZoom();" alt="<%= langService.getString("graphbutton","zoomout")%>"></td>
										    	        <td align="center">&nbsp;</td>
												        <td align="center"><img src="images/graph/sxsx2_on.png" style="cursor:pointer;" onclick="previousPeriod();" alt="<%= langService.getString("graphbutton","previouspage")%>"></td>
												        <td align="center"><img src="images/graph/sx2_on.png" style="cursor:pointer;" onclick="previeousStep();" alt="<%= langService.getString("graphbutton","previous")%>"></td>
												        <td align="center"><img src="images/graph/dx2_on.png" style="cursor:pointer;" onclick="nextStep();" alt="<%= langService.getString("graphbutton","next")%>"></td>
												        <td align="center"><img src="images/graph/dxdx2_on.png" style="cursor:pointer;" onclick="nextPeriod();" alt="<%= langService.getString("graphbutton","nextpage")%>"></td>
											    	</tr>
									        	</table>
											</td>	
											<!-- 
											<td class="groupCategory_small" style='width:110px;height:30px' onclick="openGraphLayoutConfig('<%=graphType%>');">
												<b><%=langService.getString("graphvariable","config")%></b>
											</td>																	
											-->
											<td align="right" style='width:100px;height:35px'>
												<div id="divEndTime">&nbsp;</div>
											</td>						
										</tr>
					
									</table>
									</div>
								</td>
							</tr>
							<tr>
    							<td>
									<div id="selectDeviceResponse" style="display:none;margin-left:5px;">
										<div id="idTableVariableDx" style="width:100%;">
											<table cellspacing="1" cellpadding="0" align="left">
												<tbody>
													<%=tableMerged%>
												</tbody>
											</table>
										</div>     											
										<div id="invisibleTableDx" style="display:none;">
											<table>
												<tbody>
												</tbody>
											</table>
										</div>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>		
			
<div id="data" style="display:none;">
		<div id="invisibleTableSx">
			<table>
		 		<tbody>
		      		<%=tableInvisibleSx%>
				</tbody>
			</table>
		</div>
		<div id="basePath"><%=basePath%></div>
		<div id="jsession" ><%=jsession%></div>
    	<div id="typeGraph"><%=graphType%></div>
		<div id="periodCode" ><%=periodCode%></div>
	    <div id="automaticPlot"><%=automaticPlot%></div>
		<div id="graphConstantMinutes5"><%=GraphConstant.MINUTS5%></div>
		<div id="graphConstantMinutes15"><%=GraphConstant.MINUTS15%></div>
	    <div id="graphConstantMinutes30"><%=GraphConstant.MINUTS30%></div>
		<div id="graphConstantHour"><%=GraphConstant.HOUR%></div>
		<div id="graphConstantHour6"><%=GraphConstant.HOURS6%></div>
		<div id="graphConstantHour12"><%=GraphConstant.HOURS12%></div>
		<div id="graphConstantDay"><%=GraphConstant.DAY%></div>
		<div id="graphConstantWeek"><%=GraphConstant.WEEK%></div>
		<div id="graphConstantMonth"><%=GraphConstant.MONTH%></div>
		<div id="time0"><%=GraphConstant.PERIOD[GraphConstant.MINUTS5]%></div>
		<div id="time1"><%=GraphConstant.PERIOD[GraphConstant.MINUTS15]%></div>
		<div id="time2"><%=GraphConstant.PERIOD[GraphConstant.MINUTS30]%></div>
		<div id="time3"><%=GraphConstant.PERIOD[GraphConstant.HOUR]%></div>
		<div id="time4"><%=GraphConstant.PERIOD[GraphConstant.HOURS6]%></div>
		<div id="time5"><%=GraphConstant.PERIOD[GraphConstant.HOURS12]%></div>
		<div id="time6"><%=GraphConstant.PERIOD[GraphConstant.DAY]%></div>
		<div id="time7"><%=GraphConstant.PERIOD[GraphConstant.WEEK]%></div>
		<div id="time8"><%=GraphConstant.PERIOD[GraphConstant.MONTH]%></div>
		<div id="xWidth"><%=flashObjParameters.getXWidth()%></div>
	</div>
			
	<div id="translate" style="display:none">
		<div id="displayComboBlocks"><%=displayComboBlocks%></div>
		<div id="blockdel"><%=blockdel%></div>
	    <div id="variableLabel"><%=variableLabel%></div>
		<div id="variablePresentMsg"><%=variablePresentMsg%></div>
		<div id="variableMaxMsg"><%=variableMaxMsg%></div>
		<div id="variableDeleteMsg"><%=variableDeleteMsg%></div>
		<div id="printDataLabel"><%=printDataLabel%></div>
		<div id="userDataLabel"><%=userDataLabel%></div>
		<div id="userName"><%=userName%></div>
		<div id="startDateLabel"><%=startDateLabel%></div>
		<div id="rangeLabel"><%=rangeLabel%></div>
		<div id="nodeName"><%=nodeName%></div>
		<div id="waitPlotMsg"><%=waitPlotMsg%></div>
		<div id="deviceLabel"><%=deviceLabel%></div>
		<div id="sysTimeLabelUser"><%=sysTimeLabelUser%></div>
		<div id="sysTimeLabelSO"><%=sysTimeLabelSO%></div>
		<div id="rangeMonthLabel"><%=rangeMonthLabel%></div>
		<div id="rangeDaysLabel"><%=rangeDaysLabel%></div>
		<div id="rangeHoursLabel"><%=rangeHoursLabel%></div>
		<div id="rangeMinLabel"><%=rangeMinLabel%></div>
		<div id="loadErrLabel"><%=loadErrLabel%></div>
		<div id="dataerrormsg"><%=dataerrormsg%></div>
		<div id="andDeg">&deg;</div>
		<div id="andMicro">&micro;</div>
		<div id="dblclickLabel"><%=dblclickLabel%></div>
		<div id="thDevLabel"><%=thDevLabel%></div>
		<div id="thColorLabel"><%=thColorLabel%></div>
		<div id="thVarLabel"><%=thVarLabel%></div>
		<div id="thUnitOfMeasureLabel"><%=thUnitOfMeasureLabel%></div>
		<div id="thMinLabel"><%=thMinLabel%></div>
		<div id="thMaxLabel"><%=thMaxLabel%></div>
		<div id="thAvgLabel"><%=thAvgLabel%></div>
		<div id="thLowerLimitLabel"><%=thLowerLimitLabel%></div>
		<div id="thUpperLimitLabel"><%=thUpperLimitLabel%></div>
	</div>
	
	<div id="colorsSelectDiv" style='display:none;'>
		<input id="colorsSelect" type="text" style='width:100%;border:1px solid black;background-color:#FFFFFF'/>
	</div>		
	
	<form style='display:none;' name="frmCosmeticGraph" id="frmCosmeticGraph" action="servlet/master;jsessionid=<%=jsession%>" method="post" >
	  	<input type="hidden" name="save" id="save" value="save" />
	    <input type="hidden" name="groupId" id="groupId" value="<%=idGroup%>"/>
	    <input type="hidden" name="deviceId" id="deviceId" value="<%=idDevice%>"/>
	    <input type="hidden" name="infoSave" id="infoSave" value=""/>
	    <input type="hidden" name="typeGraphCosmetic" id="typeGraphCosmetic" value="<%=graphType%>"/>
	    <input type="hidden" name="cosmeticGraphInformation" id="cosmeticGraphInformation" value=""/> 
	    <!--  time window parameters to store for the current session -->
	    <input type="hidden" name="sesTimePeriod" id="sesTimePeriod" value="<%=trxTimePeriod%>" />
	    <input type="hidden" name="sesMainTime" id="sesMainTime" value="<%=trxMainTime%>" />
	</form>	
	
<div id="GraphDialog" class="divGraphDialog">
<center>
<table border="0" width="100%" cellspacing="0" cellpadding="5px">
	<tr style="height:25px" class="th">
		<th width="*" align="center"><%=title%></th>
		<td align="right"><img src="images/close.png" style="cursor:pointer;" onClick='document.getElementById("GraphDialog").style.display="none"'/></td>
	</tr>
	<tr height="5px"><td colspan="2">&nbsp;</td></tr>
	<tr height="30px">
		<td width="40%"><%=timePeriodLabel%></td>
		<td width="60%" align="left">
			<select style="font-size:18px" id="GDtimeperiod" name="GDtimeperiod" onchange="GDchangeTimePeriod()">
				<option value="<%=GraphConstant.MINUTS5%>"><%=minut5Label%></option>
				<option value="<%=GraphConstant.MINUTS15%>"><%=minut15Label%></option>
				<option value="<%=GraphConstant.MINUTS30%>"><%=minut30Label%></option>
				<option value="<%=GraphConstant.HOUR%>"><%=hourLabel%></option>
				<option value="<%=GraphConstant.HOURS6%>"><%=hour6Label%></option>
				<option value="<%=GraphConstant.HOURS12%>"><%=hour12Label%></option>
				<option value="<%=GraphConstant.DAY%>"><%=dayLabel%></option>
				<option value="<%=GraphConstant.WEEK%>"><%=weekLabel%></option>
				<option value="<%=GraphConstant.MONTH%>"><%=monthLabel%></option>
				<option value="-1"> -------------------- </option>
			</select>
		</td>
	</tr>
	<tr style="height:15px">
		<td colspan=2>&nbsp;</td>
	</tr>
	<tr height="30px">
		<td><%=yearLabel%>
		</td>
		<td align="left">
			<select name="GDyear" id="GDyear" style="font-size:18px"><%=year.toString()%></select>                
		</td>
	</tr>
	<tr style="height:15px">
		<td colspan=2>&nbsp;</td>
	</tr>
	<tr height="30px">
		<td><%=month1Label%></td>
		<td align="left">
			<select name="GDmonth" id="GDmonth" style="font-size:18px">
				<option value="1"><%=gen%></option>
				<option value="2"><%=feb%></option>
				<option value="3"><%=mar%></option>
				<option value="4"><%=apr%></option>
				<option value="5"><%=mag%></option>
				<option value="6"><%=giu%></option>
				<option value="7"><%=lug%></option>
				<option value="8"><%=ago%></option>
				<option value="9"><%=set%></option>
				<option value="10"><%=ott%></option>
				<option value="11"><%=nov%></option>
				<option value="12"><%=dic%></option>
			</select>
		</td>
	</tr>
	<tr style="height:15px">
		<td colspan=2>&nbsp;</td>
	</tr>
	<tr height="30px">
		<td><%=day1Label%>
		</td>
		<td align="left">
			<select name="GDday" id="GDday" style="font-size:18px"><%=day.toString()%></select>      
		</td>
	</tr>
	<tr style="height:15px">
		<td colspan=2>&nbsp;</td>
	</tr>
	<tr height="30px">
		<td><%=hour1Label%></td>
		<td align="left">
			<select name="GDhour" id="GDhour" style="font-size:18px" ><%=hour.toString()%></select> : <select name="GDminut" id="GDminut" style="font-size:18px"><%=minut.toString()%></select>     
		</td>
	</tr>
	<tr style="height:15px">
		<td colspan=2>&nbsp;</td>
	</tr>
	<tr height="30px">
		<td colspan="2" align="center" class="groupCategory" style="font-size:15px"" onclick="goBack2Graph();"><b><%=langService.getString("graphvariable","goback2graph")%></b></td>
	</tr>
	<tr style="height:15px">
		<td colspan=2>&nbsp;</td>
	</tr>
</table>
</center>
</div>

<jsp:include page="Messages.jsp" flush="true" />
</body>
</html>
