<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.carel.supervisor.presentation.helper.ServletHelper" %>
<%@ page language="java" import="com.carel.supervisor.presentation.session.UserSession"%>
<%@ page import="com.carel.supervisor.presentation.session.Transaction"%>
<%@ page import="com.carel.supervisor.presentation.session.UserTransaction" %>
<%@ page import="com.carel.supervisor.director.graph.FlashObjParameters" %>
<%@ page import="com.carel.supervisor.presentation.bean.ConfigurationGraphBeanList" %>
<%@ page import="com.carel.supervisor.presentation.bean.ConfigurationGraphBean" %>
<%@ page import="com.carel.supervisor.director.DirectorMgr"%>
<%@ page import="com.carel.supervisor.dataaccess.language.LangService" %>
<%@ page import="com.carel.supervisor.dataaccess.language.LangMgr" %>
<%@ page import="com.carel.supervisor.director.graph.GraphConstant" %>
<%@ page import="com.carel.supervisor.presentation.devices.UtilDevice" %>
<%@ page import="com.carel.supervisor.presentation.bean.GraphBeanList"%>
<%@ page import="com.carel.supervisor.presentation.bean.GraphBean"%>
<%@ page import="com.carel.supervisor.presentation.helper.VirtualKeyboard" %>

<%
	//FUSIONE DEI DI TRE FRAMES ULTIMA EVOLUTIVA
	
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String jsession = request.getSession().getId();
	String browser  = userSession.getUserBrowser();
	Transaction transaction=userSession.getTransaction();
	UserTransaction trxUserLoc = userSession.getCurrentUserTransaction();
	String group = trxUserLoc.getProperty("group");
	
	// ENHANCEMENT 20090224
	// If the time-window parameters are stored in the user's session, we use them. Else, we shall use the default,
	// which is related only to the TimePeriod parameter (stored in the database)
	String trxTimePeriod = trxUserLoc.getProperty("sesHACCPTimePeriod");
	String trxMainTime = trxUserLoc.getProperty("sesHACCPMainTime");

	String language = userSession.getLanguage();
	
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
	
	
	String dataerrormsg = langService.getString("ConfGraph","dataerrormsg");
	
	String nodeName=userSession.getSiteName();
	String userName=userSession.getUserName();
	
	Long servermaintime = System.currentTimeMillis();

	//Impostazioni relative al gruppo distinte dal dettaglio visto l'ingresso in architettura
	ConfigurationGraphBeanList configurationGraphBeanList= new ConfigurationGraphBeanList(GraphConstant.TYPE_HACCP);
	Integer idGroup=null;
	Integer idDevice=null;
	if(!group.equals(""))
		idGroup= new Integer(group);
	else
		idDevice= new Integer(userSession.getProperty("iddev"));
	
	configurationGraphBeanList.loadConfigurationPage(null,language,new Integer(userSession.getIdSite()),new Integer(userSession.getProfile()),
																									 idGroup,
																									 idDevice);
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey(); 																					 
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
				tableMerged.append("<td><input id ='chkbox").append(configurationGraphBeans[i].getIdVariable())
					.append("' class='bigcheck' type='checkbox' checked onclick=\"toggleVariable('").append(configurationGraphBeans[i].getIdVariable())
					.append("')\" /></td>");
				
				// cell[1] device description
				tableMerged.append("<td>").append(configurationGraphBeans[i].getDeviceDescription()).append("</td>");
				
				// cell[2] color (Visible column) (Alessandro: id attribute added for using with color picker)
				tableMerged	.append("<td><table style='border-radius:14px;border:2px solid black;height:19px;width:100%;' cellspacing='0'><tbody><tr><td id='txtcolor")
							.append(configurationGraphBeans[i].getIdVariable())
							.append("' style='border-radius:14px;background-color:#")
							.append(configurationGraphBeans[i].getColor())
							.append("'></td></tr></tbody></table></td>");
				
				// cell[3] variable description
				tableMerged.append("<td>").append(configurationGraphBeans[i].getVariableDescription()).append("</td>");
				
				// cell[4] u.m.
				tableMerged.append("<td align='center'>")
					.append(configurationGraphBeans[i].getUnitOfmeasure()==null? " ":configurationGraphBeans[i].getUnitOfmeasure())
					.append("</td>"); // unit of measure
					
				// cell[5] y min value	
				tableMerged.append("<td align='center'>--</td>");
				
				// cell[6] y max value
				tableMerged.append("<td align='center'>--</td>");
				
				// cell[7] y average value
				tableMerged.append("<td align='center'>--</td>");			
	
				// 20090310 - We write "autoscale" instead of blank text
				if (configurationGraphBeans[i].getVariableType() != 1 && configurationGraphBeans[i].getVariableType() != 4
					&& ("".equals(yMin) || "NaN".equals(yMin))) {
					// if yMin = "", yMax too should be "". We write "autoscale" (untranslated!) in the field
					// That's true only for analog or integer variables
					yMin = "autoscale";
					yMax = "autoscale";
				}				
				
				// cell[8] lower range value
				tableMerged.append("<td align='center'>").append(yMin).append("</td>");
				
				// cell[9] upper range value
				tableMerged.append("<td align='center'>").append(yMax).append("</td>");
	
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
	configurationGraphBeanList= new ConfigurationGraphBeanList(GraphConstant.TYPE_HACCP);
	configurationGraphBeanList.loadConfigurationPageCosmeticGraph(null,language,new Integer(userSession.getIdSite()),new Integer(userSession.getProfile()),idGroup,idDevice);
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
	String s_iddev = userSession.getProperty("iddev");
	transaction.setGraphParameter(null);
	String sessionName="graph";
	configurationGraphBeanList= new ConfigurationGraphBeanList(GraphConstant.TYPE_HACCP);
	configurationGraphBeanList.loadConfigurationPagePeriod(null,language,new Integer(userSession.getIdSite()),new Integer(userSession.getProfile()),idGroup,idDevice);
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

  
	int[] devicesId = transaction.getIdDevices();
	if(devicesId!=null)
		if(devicesId.length==1)
			if(s_iddev!=null)
				if(!s_iddev.equals("")){
					devicesId[0]=(new Integer(s_iddev)).intValue();
					transaction.setIdDevices(devicesId);
				}//if
	
    StringBuffer deviceList = new StringBuffer();
    GraphBeanList graphBeanList = new GraphBeanList();
    graphBeanList.loadDeviceList(null,userSession.getIdSite(),devicesId,language); 
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
		int[] ids = transaction.getIdDevices();
		if (ids.length>0)
		{		
			devices_combo = UtilDevice.getDeviceCombo(userSession);
		}	
	}
    //String automaticPlot=folder.equals("dtlview")?"true":"false";
    String automaticPlot="true"; 
    int currScreenWidth = userSession.getScreenWidth();
    int currScreenHeight = userSession.getScreenHeight();
    FlashObjParameters flashObjParameters = null;
    if (currScreenWidth == 1024)
    	flashObjParameters= new FlashObjParameters(0,0,currScreenHeight,currScreenWidth,240,35);
    else
    	flashObjParameters= new FlashObjParameters(0,0,currScreenHeight,currScreenWidth,420,35);
      	 
	// RESTART ENGINE HANDLING 20090312
	if (DirectorMgr.getInstance().isMustCreateProtocolFile()) { // we don't show the page if the engine needs to be restarted %>
		<p class='standardTxt'><%=langService.getString("dtlview","restarttoview")%></p>
		<input type='hidden' id='js_control'/>
	<% 
	}else { // If the engine doesn't need to be restarted, we display the page
	%>	
<input type="hidden" id="virtkey" name="virtkey" value="<%=OnScreenKey%>" />
<input type="hidden" id="servermaintime" name="servermaintime" value="<%=servermaintime%>" />
<input type="hidden" id="searchTitle" value="<%=langService.getString("graphvariable","opengraphdialog")%>" />
<input type="hidden" id="customizeTitle" value="<%=langService.getString("ConfGraph","confgraph")%>" />
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
	                				<td><img src='images/event/alert.gif'></td>
	                				<td style='font-size:10px;color:#ff0000;padding-left:10px'>The font used in this graphic object is not installed on your computer, the texts may not display correctly.<br>Please, install <i>"Arial Unicode MS"</i> from operating system's CD.</td>
	                			</tr>
                			</table>
            			</div>
            			<!--fine fontInfo-->					
					<div id="divFlashObj" align="left" style="height:<%=flashObjParameters.getHEIGHT()%>px;width:100%;">
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
																	<select name="deviceList" id="deviceList" style="FONT-SIZE: 18px; WIDTH: 100%; align: left" onchange="changeDevice(this.value);">
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
									<table width="100%">
										<tr valign="middle">	
											<td style="height:30px;width:*;">										
										        <div>
										        	<div style="float:right; width: 20%;">&nbsp;</div>
										    	    <div class="groupCategory_small" style='height:30px; float:right; width:20%; ' onclick="openGraphLayoutConfig('<%=GraphConstant.TYPE_HACCP%>');">
										    	    	<div style="padding-top: 8px;"><b><%=langService.getString("graphvariable","config")%></b></div>
										    	    </div>
										       		
											        <div style="float:right; padding-top:5px;" ><img src="images/graph/dxdx2_on.png" style="cursor:pointer;" onclick="nextPeriod();" alt="<%= langService.getString("graphbutton","nextpage")%>" />&nbsp;</div>
											        <div style="float:right; padding-top:5px;" ><img src="images/graph/dx2_on.png" style="cursor:pointer;" onclick="nextStep();" alt="<%= langService.getString("graphbutton","next")%>" /></div>
											        <div style="float:right; padding-top:5px;" ><img src="images/graph/sx2_on.png" style="cursor:pointer;" onclick="previeousStep();" alt="<%= langService.getString("graphbutton","previous")%>" /></div>
											        <div style="float:right; padding-top:5px;" ><img src="images/graph/sxsx2_on.png" style="cursor:pointer;" onclick="previousPeriod();" alt="<%= langService.getString("graphbutton","previouspage")%>" /></div>
									    	        <div style="float:right;">&nbsp;</div>
										    	    <div style="float:right; padding-top:5px;" ><img id="zoomOutImg" src="images/graph/zoomout_on.png"  style="cursor:pointer;" onclick="backZoom();" alt="<%= langService.getString("graphbutton","zoomout")%>" /></div>
									   
										       		<div class="groupCategory_small" style='height:30px;float:right; width:20%;' onclick="OpenGraphDialog();">
										       			<div style="padding-top: 8px;"><b><%=langService.getString("graphvariable","opengraphdialog")%></b></div>
										       		</div>
									        	</div>
											</td>	
										</tr>
					
									</table>
									</div>
								</td>
							</tr>	
							<tr><td>&nbsp;</td></tr>
							<tr>
    							<td>
    								<table width="100%" cellspacing="0" cellpadding="0" border="0">
    									<tr>
    										<td>
        										<div id="selectDeviceResponse">
        											<div id="idTableVariableDx">
        												<table width="100%" cellspacing="2" cellpadding="2" border="0">
        													<tbody>
        														<tr class='th'>
        															<th id="THgraphVar00"></th>
        															<th id="THgraphVar01"><%=thDevLabel %></th>
        															<th id="THgraphVar02"><%=thColorLabel%></th>
        															<th id="THgraphVar03"><%=thVarLabel%></th>
        															<th id="THgraphVar04"><%=thUnitOfMeasureLabel%></th>
        															<th id="THgraphVar05"><%=thMinLabel%></th>
        															<th id="THgraphVar06"><%=thMaxLabel%></th>
        															<th id="THgraphVar07"><%=thAvgLabel%></th>
        															<th id="THgraphVar08"><%=thLowerLimitLabel%></th>
        															<th id="THgraphVar09"><%=thUpperLimitLabel%></th>
        														</tr>
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
    	<div id="typeGraph"><%=GraphConstant.TYPE_HACCP%></div>
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
	</div>
			
	<div id="translate" style="display:none">
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
	    <input type="hidden" name="typeGraphCosmetic" id="typeGraphCosmetic" value="<%=GraphConstant.TYPE_HACCP%>"/>
	    <input type="hidden" name="cosmeticGraphInformation" id="cosmeticGraphInformation" value=""/> 
	    <!--  time window parameters to store for the current session -->
	    <input type="hidden" name="sesTimePeriod" id="sesTimePeriod" value="<%=trxTimePeriod%>" />
	    <input type="hidden" name="sesMainTime" id="sesMainTime" value="<%=trxMainTime%>" />
	</form>	
	
<div id="divPrint" class="uploadWin" style="position:absolute;">
	<div id="divPrintHeader" class="uploadWinHeader">
		<div class="uploadWinClose" onclick="document.getElementById('divPrint').style.display='none';">X</div>
		<%=langService.getString("graph", "print_properties")%>
	</div>
	<div id="divPrintBody" class="uploadWinBody">
		<div>
			<br>
			<div id="divPrintProperties">
				<table border="0" width="100%">
					<tr>
						<td align="center" width="50%" class="standardTxt"><input id="ppGrid" type="checkbox">&nbsp;<%=langService.getString("graph", "print_grid")%></td>
						<td align="center" width="50%" class="standardTxt"><input id="ppMMA" type="checkbox">&nbsp;<%=langService.getString("graph", "print_mma")%></td>
					</tr>
				</table>		
			</div>
			<br>
			<div id="divPrintButtons">
				<center><table border="0">
					<tr>
						<td class="groupCategory_small" style="width:110px;height:30px;" onclick="document.getElementById('divPrint').style.display='none';onPrintGraph();"><%=langService.getString("filedialog", "ok")%></td>
						<td class="groupCategory_small" style="width:110px;height:30px;" onclick="document.getElementById('divPrint').style.display='none'"><%=langService.getString("filedialog", "cancel")%></td>
					</tr>
				</table></center>
			</div>
		</div>	
	</div>
</div>
<% } // end "else". If the engine doesn't need to be restarted, we display the page %>	
