<%@ page language="java" pageEncoding="UTF-8" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.session.Transaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr" 
import="com.carel.supervisor.director.graph.GraphConstant" 
import="java.util.Properties"
import="com.carel.supervisor.presentation.bean.ConfigurationGraphBeanList"
import="com.carel.supervisor.presentation.bean.ConfigurationGraphBean"
import="com.carel.supervisor.presentation.graph.SelectColor"
%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String browser  = userSession.getUserBrowser();
	String jsession = request.getSession().getId();
	Transaction transaction=userSession.getTransaction();
	UserTransaction trxUserLoc = userSession.getCurrentUserTransaction();
	String group = trxUserLoc.getProperty("group");
	String s_iddev = userSession.getProperty("iddev");
	String language = userSession.getLanguage();
	LangService langService = LangMgr.getInstance().getLangService(language);
	
	String numberOfColorSel = "0";
	
	//TRADUZIONI 
	String descConfGraph = langService.getString("ConfGraph","descconfgraph");
	String confGraph =  langService.getString("ConfGraph","confgraph");
	String viewFinder =  langService.getString("ConfGraph","viewfinder");
	String viewFinderBackgroundColor =  langService.getString("ConfGraph","viewfinderbackgroundcolor");
	String viewFinderForegroundColor =  langService.getString("ConfGraph","viewfinderforegroundcolor");
	String horizontalGrid =  langService.getString("ConfGraph","horizontalgrid");
	String verticalGrid =  langService.getString("ConfGraph","verticalgrid");
	String gridColor =  langService.getString("ConfGraph","gridcolor");
	String backgroundGraphColor =  langService.getString("ConfGraph","backgroundgraphcolor");
	String axisColor =  langService.getString("ConfGraph","axiscolor");
	String graph =  langService.getString("ConfGraph","graph");
	String haccp =  langService.getString("ConfGraph","haccp");
	String historical =  langService.getString("ConfGraph","historical");
	String dataErrorMsg =  langService.getString("ConfGraph","dataerrormsg");
	
	
	boolean isdevice= false;
	String typeGraph="";
	
	Properties properties=transaction.getGraphParameter();

	SelectColor colors= new SelectColor("color","");
	
	
	SelectColor colorViewFinderBg= new SelectColor("colorViewFinderBg","changed=true");
	SelectColor colorViewFinderFg= new SelectColor("colorViewFinderFg","changed=true");
	SelectColor colorGrid= new SelectColor("colorGrid","changed=true");
	SelectColor colorGraphBg= new SelectColor("colorGraphBg","changed=true");
	SelectColor colorAxis= new SelectColor("colorAxis","changed=true");
	
	
	//CARICO LA CONFIGURAZIONE DEI COLORI E LE IMPOSTAZIONI DEL GRAFO
	Integer idGroup=null;
	Integer idDevice=null;
	if(!group.equals(""))
		idGroup= new Integer(group);	
	else
	{
		idDevice= new Integer(s_iddev);
		//transaction.setIdDevices(new int[]{idDevice.intValue()});
	}	
	
	String onLoadCosmeticGraphInfoDefault    = "true;true;true;"+GraphConstant.V_FINDER_BG_COLOR+";"+GraphConstant.V_FINDER_FG_COLOR+";"+GraphConstant.GRID_COLOR+";"+GraphConstant.GRAPH_BG_COLOR+";"+GraphConstant.AXIS_COLOR+";";//"true;true;true;FFFF00;000000;C0C0C0;000000;0000FF;";
	String onLoadCosmeticGraphInfoHaccp      = onLoadCosmeticGraphInfoDefault;
	String onLoadCosmeticGraphInfoHistorical = onLoadCosmeticGraphInfoDefault;
	
	
	ConfigurationGraphBeanList configurationGraphBeanList= new ConfigurationGraphBeanList(GraphConstant.TYPE_HACCP);
	configurationGraphBeanList.loadConfigurationPageCosmeticGraph(null,language,new Integer(userSession.getIdSite()),new Integer(userSession.getProfile()),idGroup,idDevice);

	ConfigurationGraphBean []configurationGraphBeans= configurationGraphBeanList.getConfigurationGraphBeans();  
	
	if(configurationGraphBeans!=null){
	if(configurationGraphBeans[0].getViewFinderCheck()!=null){
		onLoadCosmeticGraphInfoHaccp= (configurationGraphBeans[0].getViewFinderCheck().trim().equals("TRUE")?new String("true"):new String("false")) +";"+
									  (configurationGraphBeans[0].getXGridCheck().trim().equals("TRUE")?new String("true"):new String("false")) +";"+
									  (configurationGraphBeans[0].getYGridCheck().trim().equals("TRUE")?new String("true"):new String("false")) +";"+
									  configurationGraphBeans[0].getViewFinderColorBg() +";"+
									  configurationGraphBeans[0].getViewfinderColorFg() +";"+
									  configurationGraphBeans[0].getGridColor() +";"+
									  configurationGraphBeans[0].getBgGraphColor() +";"+
									  configurationGraphBeans[0].getAxisColor();
									  }//if
	}//if
 	configurationGraphBeanList= new ConfigurationGraphBeanList(GraphConstant.TYPE_HISTORICAL);
	configurationGraphBeanList.loadConfigurationPageCosmeticGraph(null,language,new Integer(userSession.getIdSite()),new Integer(userSession.getProfile()),idGroup,idDevice);

	configurationGraphBeans= null;
	configurationGraphBeans=configurationGraphBeanList.getConfigurationGraphBeans();  
	
	if(configurationGraphBeans!=null){
		if(configurationGraphBeans[0].getViewFinderCheck()!=null){
			onLoadCosmeticGraphInfoHistorical= (configurationGraphBeans[0].getViewFinderCheck().trim().equals("TRUE")?new String("true"):new String("false")) +";"+
		         							   (configurationGraphBeans[0].getXGridCheck().trim().equals("TRUE")?new String("true"):new String("false")) +";"+
									  		   (configurationGraphBeans[0].getYGridCheck().trim().equals("TRUE")?new String("true"):new String("false")) +";"+
									  		   configurationGraphBeans[0].getViewFinderColorBg() +";"+
									  		   configurationGraphBeans[0].getViewfinderColorFg() +";"+
											   configurationGraphBeans[0].getGridColor() +";"+
											   configurationGraphBeans[0].getBgGraphColor() +";"+
											   configurationGraphBeans[0].getAxisColor();
									  }//if
	}//if
	
	transaction.setGraphParameter(null);
	
%>


   

 	 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>" />
    <title><%=confGraph%></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="description" content="Graph Layout Config Dialog Window">
	<!-- >script type="text/javascript" src="scripts/app/graphconf.js"></script>
	<script type="text/javascript" src="scripts/arch/jscolor/jscolor.js"></script -->
			
	<script type="text/javascript"><!--
		var subtabgraphconfx,subtabgraphconfy;
		if (self.innerHeight) // all except Explorer
		{
			subtabgraphconfx = self.innerWidth;
			subtabgraphconfy = self.innerHeight;
		}
		else if (document.documentElement && document.documentElement.clientHeight)
			// Explorer 6 Strict Mode
		{
			subtabgraphconfx = document.documentElement.clientWidth;
			subtabgraphconfy = document.documentElement.clientHeight;
		}
		else if (document.body) // other Explorers
		{
			subtabgraphconfx = document.body.clientWidth;
			subtabgraphconfy = document.body.clientHeight;
		}
	//-->
	</script>
			
  </head>

<body style="background-color:#FFFFFF">

<input type="hidden" id="hiddenfocus" value=""/>
<input type="hidden" id="jsession" value="<%=jsession%>"/>

<table border="0" height="100%" width="96%" >
  <tr>
  	<td class="standardTxt" style="padding-left:4px; height: 40px;"><%=descConfGraph %></td>
  </tr>
  <tr valign='top'>  	
    <td class="standardTxt" height="180px;">
    <div style="padding-left:10px;">
      <fieldset class="field" style="width: 100%;">
      	<legend><%=confGraph%></legend>
        <table border="0" width="100%" >
          <tr>
            <td>
              <table border="0" width="100%">
                <tr>
                  <td class="standardTxt" align="left" width="20%">
                    <input type="checkbox" checked id="viewFinderCheckedBox" name="viewFinderCheckedBox" onclick="checkBoxType('viewFinderCheckedBox')">  <%=viewFinder%>
                  </td>
                  <td class="standardTxt" align="left" width="26%"><%=viewFinderBackgroundColor%></td>
                  <td width="14%"><input id="colorViewFinderBg" class="jscolor" type="text" style='width:60%;border:1px solid black'/><input type="hidden" id="colorViewFinderBgHiddenValue"/><% //=colorViewFinderBg.getHTMLSelectColor() %></td>
                  <td class="standardTxt" align="left" width="26%"><%=viewFinderForegroundColor%></td>
                  <td width="14%"><input id="colorViewFinderFg" class="jscolor" type="text" style='width:60%;border:1px solid black'/><input type="hidden" id="colorViewFinderFgHiddenValue"/></td>
                </tr>
                <tr>
                	<td colspan="5" style="height:8px;"/></td>
                </tr>
                <tr>
                  <td class="standardTxt" align="left">
                    <input type="checkbox" checked id="xgridCheckedBox" name="xgridCheckedBox" onclick="checkBoxType('xgridCheckedBox')"> <%=horizontalGrid%>
                  </td>
                  <td class="standardTxt" align="left"><%=gridColor%></td>
                  <td align="left"><input id="colorGrid" class="jscolor" type="text" style='width:60%;border:1px solid black'/><input type="hidden" id="colorGridHiddenValue" /></td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                	<td colspan="5" style="height:8px;" /></td>
                </tr>
                <tr>
                  <td class="standardTxt" align="left">
                    <input type="checkbox" checked id="ygridCheckedBox" name="ygridCheckedBox" onclick="checkBoxType('ygridCheckedBox')"> <%=verticalGrid%>
                  </td>
                  <td class="standardTxt" align="left"><%=backgroundGraphColor%></td>
                  <td class="standardTxt" align="left"><input id="colorGraphBg" class="jscolor" type="text" style='width:60%;border:1px solid black'/><input type="hidden" id="colorGraphBgHiddenValue"/></td>
                  <td class="standardTxt" align="left"><%=axisColor%></td>
                  <td class="standardTxt" align="left"><input id="colorAxis" class="jscolor" type="text" style='width:60%;border:1px solid black'/><input type="hidden" id="colorAxisHiddenValue"/></td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </fieldset>   
      </div>
    </td>
  </tr> 
  <tr valign='top'>
  	<td>
  		<table width='100%' border="0">
  			<tr>
				<td>&nbsp;</td>
				<td align="center" class="groupCategory" style="width:175px;height:30px;font-size:15px"" onclick="window.returnValue=buildReturnParams();window.close();"><b><%=langService.getString("button","save")%></b></td>
				<td/>
				<td align="center" class="groupCategory" style="width:175px;height:30px;font-size:15px"" onclick="closeWindow();"><b><%=langService.getString("graphvariable","cancel")%></b></td>
				<td>&nbsp;</td>
			</tr>		 
		</table>
	</td>
  </tr>
</table>

<div style="display:none"> 
 
  	<div  id="onLoadCosmeticGraphInfoDefault"><%=onLoadCosmeticGraphInfoDefault%></div>
	<div  id="onLoadCosmeticGraphInfoHaccp" ><%=onLoadCosmeticGraphInfoHaccp%></div>
	<div  id="onLoadCosmeticGraphInfoHistorical" ><%=onLoadCosmeticGraphInfoHistorical%></div>	
	<div  id="dataErrorMsg" ><%=dataErrorMsg%></div>	
</div>

<script type="text/javascript">
<!--

// binding automatico
//jscolor.install();

// bind color picker for any input tag 
	document.getElementById("colorViewFinderBg").style.backgroundColor;
	document.getElementById("colorViewFinderFg").style.backgroundColor;
	document.getElementById("colorGrid").style.backgroundColor;
	document.getElementById("colorGraphBg").style.backgroundColor;
	document.getElementById("colorAxis").style.backgroundColor;

pickerColorViewFinderBg = new jscolor.color(document.getElementById('colorViewFinderBg'), {required:false,valueElement:'colorViewFinderBgHiddenValue'});
pickerColorViewFinderBg.fromString(document.getElementById("colorViewFinderBg").style.backgroundColor);  // now you can access API via 'myPicker' variable

pickerColorViewFinderFg = new jscolor.color(document.getElementById('colorViewFinderFg'), {required:false,valueElement:'colorViewFinderFgHiddenValue'});
pickerColorViewFinderFg.fromString(document.getElementById("colorViewFinderFg").style.backgroundColor);  // now you can access API via 'myPicker' variable

pickerColorGrid = new jscolor.color(document.getElementById('colorGrid'), {required:false,valueElement:'colorGridHiddenValue'});
pickerColorGrid.fromString(document.getElementById("colorGrid").style.backgroundColor);  // now you can access API via 'myPicker' variable

pickerColorGraphBg = new jscolor.color(document.getElementById('colorGraphBg'), {required:false,valueElement:'colorGraphBgHiddenValue'});
pickerColorGraphBg.fromString(document.getElementById("colorGraphBg").style.backgroundColor);  // now you can access API via 'myPicker' variable

pickerColorAxis = new jscolor.color(document.getElementById('colorAxis'), {required:false,valueElement:'colorAxisHiddenValue'});
pickerColorAxis.fromString(document.getElementById("colorAxis").style.backgroundColor);  // now you can access API via 'myPicker' variable

//-->
</script>

</body>
</html>
