<%@ page language="java" import="java.util.*" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.*"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.presentation.bo.BAlrSched" 
import="com.carel.supervisor.presentation.timebands.TimeBands"
import="com.carel.supervisor.presentation.bean.LogicDeviceBeanList"
import="com.carel.supervisor.presentation.logicdevice.LogicVariable"
import="com.carel.supervisor.presentation.bo.BLogicDevice"
import="com.carel.supervisor.presentation.bean.LogicVariableBeanList"
import="com.carel.supervisor.presentation.bean.LogicVariableBean"
import="com.carel.supervisor.controller.function.FunctionMgr"
import="com.carel.supervisor.controller.function.FunctionInfo"
import="com.carel.supervisor.controller.database.FunctionList" 
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%@ page import="com.carel.supervisor.dataaccess.dataconfig.VariableInfo" %>
<%@ page import="com.carel.supervisor.presentation.bean.UnitOfMeasurementBeanList" %>
<%@ page import="com.carel.supervisor.presentation.bean.UnitOfMeasurementBean" %>


<%
  UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
  UserTransaction userTransaction = userSession.getCurrentUserTransaction();
  
  //Lingua e Traduzioni
  String language = userSession.getLanguage();
  LangService langService = LangMgr.getInstance().getLangService(language);
  LangService lan = LangMgr.getInstance().getLangService(language);
  String logicvariablecomment = lan.getString("logicvariablePage","logicvariablecomment");
  String jsession = request.getSession().getId();
  String desc = langService.getString("logicvariablePage","desc");
  String type = langService.getString("logicvariablePage","type");
  String integer = langService.getString("logicvariablePage","integer");
  String analogic = langService.getString("logicvariablePage","analogic");
  String digital = langService.getString("logicvariablePage","digital");
  String alarm = langService.getString("logicvariablePage","alarm");
  String operation = langService.getString("logicvariablePage","operation");
  String constant = langService.getString("logicvariablePage","constant");
  String varDevice = langService.getString("logicvariablePage","vardevice");
  String varList    = langService.getString("logicvariablePage","varlist");
  String varLinked = langService.getString("logicvariablePage","varlinked");
  String decimals = langService.getString("logicvariablePage","decimals");

	// Alessandro : determino se la tastiera virtuale è abilitata
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();  
  
  String unitMeasurement = langService.getString("logicvariablePage","unitmeasurement");

  String variable = langService.getString("logicvariablePage","variable");
  String device = langService.getString("logicvariablePage","device");
  
  String rowPresentMsg = langService.getString("logicvariablePage","rowpresentmsg");
  String deleteRowTableMsg = langService.getString("logicvariablePage","deleterowtablemsg");
  String deleteRowTable1Msg = langService.getString("logicvariablePage","deleterowtable1msg");
  String operatorOverflowMsg = langService.getString("logicvariablePage","operatoroverflowmsg");
  String noDescMsg =langService.getString("logicvariablePage","nodescmsg");
  String noVariablePresentMsg=langService.getString("logicvariablePage","novariablepresentmsg");
  String operatorInsertLessMsg=langService.getString("logicvariablePage","operatorinsertlessmsg");
  String noDeleteVariablePresentMsg=langService.getString("logicvariablePage","nodeletevariablepresentmsg");
  String confirmvardelete = langService.getString("logicvariablePage","confirmvardelete");  
    
  LogicVariable logicVariable=new LogicVariable();

  logicVariable.loadData(userSession);
  logicVariable.setScreenH(userSession.getScreenHeight());
  logicVariable.setScreenW(userSession.getScreenWidth());
  logicVariable.setWidth(900);
  logicVariable.setHeight(100);
  
  String htmlLogicVariable = logicVariable.getHTMLLogicVariableTable("logicVariableTable",language);
  
  
  //Select Combo Device Fisici
  LogicDeviceBeanList logicDeviceBeanList= new LogicDeviceBeanList();
  logicDeviceBeanList.loadDeviceDescription(null,userSession.getIdSite(),language,false);
  StringBuffer deviceList=new StringBuffer();
  deviceList.append("<option value=\"\" id=\"\" >---------------------");
  for(int i=0;i<logicDeviceBeanList.size();i++){
    deviceList.append("\n<option  value=\"");
    deviceList.append(logicDeviceBeanList.getLogicDevice(i).getIddevice());
    deviceList.append("\" id=\"");
    deviceList.append(logicDeviceBeanList.getLogicDevice(i).getIddevice());
    deviceList.append("\">");
    deviceList.append(logicDeviceBeanList.getLogicDevice(i).getDescription());
    deviceList.append("\n</option>");
  }//for
  
  
  //operation
  String operationSelect="0";
  FunctionMgr functionMgr= FunctionMgr.getInstance();
  List functionList=  functionMgr.getFunctionList(); //funzione dall'XML
  StringBuffer operationList = new StringBuffer();  //funzione per la JSP 
  StringBuffer invisibleTableOperation=new StringBuffer();
  for(int i=0,j=0;i<functionList.size();i++){
    FunctionInfo info=(FunctionInfo)functionList.get(i);
    if(!info.getSymbol().equals("I")){
        operationList.append("\n<option value=\"");
        operationList.append(j);
        operationList.append("\">");
        String tmpSymbol = info.getSymbol();
        if (null != tmpSymbol && tmpSymbol.startsWith("<")) {
        	tmpSymbol = "&lt;"+tmpSymbol.substring(1);
        }
        operationList.append(tmpSymbol);
        operationList.append("\n</option>");
        invisibleTableOperation.append("<tr>");
        invisibleTableOperation.append("<td>");
        invisibleTableOperation.append(info.getCode());
        invisibleTableOperation.append("</td>");
        invisibleTableOperation.append("<td>");
        invisibleTableOperation.append(info.getPars());
        invisibleTableOperation.append("</td>");
        invisibleTableOperation.append("</tr>");
        j++;
    }//if
  }//for
  
  
  String typeCheck = "checkOnlyAnalogOnBlur(this);";
  

  StringBuffer logicVariableTableDx= new StringBuffer();
  StringBuffer logicVariableTableSx= new StringBuffer();
  StringBuffer invisibleTableDx= new StringBuffer();
  StringBuffer invisibleTableSx= new StringBuffer();
  
  String checkedInputRadioInteger="";
  String checkedInputRadioAnalogic="";
  String checkedInputRadioDigital="";
  String checkedInputRadioAlarm="";
  String buttonEnable="1";  //di default è selzionato il bottone add 
  String idDevice="";
  
  String idVariable="";
  String description="";
  String variableType="";
  String functionCode="";
  String variabledeviceid="";
  
  
  String unitMeasurementSelect="0";
  StringBuffer unitMeasurementList=new StringBuffer();
  UnitOfMeasurementBeanList unitOfMeasurementBeanList= new UnitOfMeasurementBeanList();
  unitOfMeasurementBeanList.loadAllUnitOfMeasurement();
  UnitOfMeasurementBean unitOfMeasurementBean=null;

  for(int i=0;i<unitOfMeasurementBeanList.size();i++){
    unitOfMeasurementBean=unitOfMeasurementBeanList.getUnitOfMeasurement(i);
    unitMeasurementList.append("\n<option value=\"");
    unitMeasurementList.append(unitOfMeasurementBean.getIdUnitOfMeasurement());
    unitMeasurementList.append("\">");
    unitMeasurementList.append(unitOfMeasurementBean.getDescription());
    unitMeasurementList.append("\n</option>");
  
  }//for

  //decimals
  String decimalsSelect="0";
  StringBuffer decimalsList=new StringBuffer();
  for(int i=0; i<= 10; i++) {
    decimalsList.append("<option value=\"" + i + "\">" + i + "</option>\n");
  }
  
    Properties properties= userTransaction.getLogicVariableParameter();
    if(properties!=null){
      idVariable=(String)properties.get("idVariable");    
      idDevice=(String)properties.get("idDevice");    
      operationSelect=(String)properties.get("operationSelect");    
      unitMeasurementSelect=(String)properties.get("unitMeasurementSelect");    
      decimalsSelect=(String)properties.get("decimalsSelect");    
      description=(String)properties.get("description");  
      buttonEnable=(String)properties.get("buttonEnable");
      variableType=  (String)properties.get("variableType");
      functionCode=  (String)properties.get("functionCode");
      variabledeviceid = (String)properties.get("variabledeviceid");
      if(((String)properties.get("action")).equals(BLogicDevice.STATUS_ADD_ACTION_VARIABLE_TYPE)){
          LogicVariableBeanList variableBeanList= new LogicVariableBeanList();
          int []vt=null;
          if(variableType.equals("2"))//analogiche
            vt= new int[]{2};
          else if(variableType.equals("3"))//intere, split the ananlog and integer by Kevin, 2011-1-17
        	vt = new int[]{3};
          else if(variableType.equals("1")) //digitali
            vt= new int[]{1};
          else if(variableType.equals("4")) //digitali
              vt= new int[]{4};
            
          //variableBeanList.loadPhyisicalVariablesForDevice(null,new Integer((String)properties.get("deviceList")).intValue(),userSession.getIdSite(), language,vt);
          variableBeanList.loadVariablesListForDevice(null,new Integer((String)properties.get("deviceList")).intValue(),userSession.getIdSite(), language,vt);
          LogicVariableBean logicVariableBean=null;
          for(int i=0;i<variableBeanList.size();i++){
              logicVariableBean=variableBeanList.getLogicVariable(i);  
              logicVariableTableSx.append("<tr  style=\"cursor:pointer\" class='"+(i%2==0?"Row1":"Row2")+"'  onclick=\"select('sx',this);\" ondblclick=\"arrowAdd();\">");
              logicVariableTableSx.append("<td class=\"tdmini2\" width=\"100%\" >");
              logicVariableTableSx.append(logicVariableBean.getDescription());
              logicVariableTableSx.append("</td>");
              logicVariableTableSx.append("</tr>");
          
              invisibleTableSx.append("<tr>");
              invisibleTableSx.append("<td>");
              invisibleTableSx.append(logicVariableBean.getIdVariable());
              invisibleTableSx.append("</td>");
              invisibleTableSx.append("<td>");
              invisibleTableSx.append(logicVariableBean.getTypeTranslate(language));
              invisibleTableSx.append("</td>");
              invisibleTableSx.append("<td>");
              invisibleTableSx.append(logicVariableBean.getDescription());
              invisibleTableSx.append("</td>");
              invisibleTableSx.append("<td>");
              invisibleTableSx.append(logicVariableBean.getType());
              invisibleTableSx.append("</td>");
              invisibleTableSx.append("<td>");
              invisibleTableSx.append("DISPOSITIVO");
              invisibleTableSx.append("</td>");
              invisibleTableSx.append("</tr>");
          }//for
          String typeVar=(String)properties.get("typevar");
            
              do{
                  if(typeVar.equals("inputRadioInteger")){
                    checkedInputRadioInteger="checked";
                    functionList=functionMgr.getFunctionList(FunctionMgr.REAL);
                  break;
                  }//if 
                  if(typeVar.equals("inputRadioAnalogic")){
                    checkedInputRadioAnalogic="checked";
                    functionList=functionMgr.getFunctionList(FunctionMgr.REAL);
                    break;
                  }//if
                  if(typeVar.equals("inputRadioDigital")){
                    checkedInputRadioDigital="checked";
                    functionList=functionMgr.getFunctionList(FunctionMgr.BOOLEAN);
                    typeCheck = "checkOnlyDigitOnBlur(this);";
                    break;
                  }//if
                  if(typeVar.equals("inputRadioAlarm")){
                      checkedInputRadioAlarm="checked";
                      functionList=functionMgr.getFunctionList(FunctionMgr.BOOLEAN);
                      typeCheck = "checkOnlyDigitOnBlur(this);";
                      break;
                  }                  
              }while(false);//doWhileSwitch
              operationList=new StringBuffer();
              invisibleTableOperation=new StringBuffer();
              for(int i=0,j=0;i<functionList.size();i++){
                  FunctionInfo info=(FunctionInfo)functionList.get(i);
                  if(!info.getSymbol().equals("I")){
                      operationList.append("\n<option value=\"");
                      operationList.append(j);
                      operationList.append("\">");
                      String tmpSymbol = info.getSymbol();
                      if (null != tmpSymbol && tmpSymbol.startsWith("<")) {
                      	tmpSymbol = "&lt;"+tmpSymbol.substring(1);
                      }
                      operationList.append(tmpSymbol);
                      operationList.append("\n</option>");
                      invisibleTableOperation.append("<tr>");
                      invisibleTableOperation.append("<td>");
                      invisibleTableOperation.append(info.getCode());
                      invisibleTableOperation.append("</td>");
                      invisibleTableOperation.append("<td>");
                      invisibleTableOperation.append(info.getPars());
                      invisibleTableOperation.append("</td>");
                      invisibleTableOperation.append("</tr>");
                      j++;
                  }//if
              }//for
              
              String []datas=((String)properties.get("variableTable")).split("@",-1);
              for(int i=0,j=0;(i<datas.length)&&(datas.length%5==0);j++){
                    String []tmps= new String[5];
                    logicVariableTableDx.append("<tr style=\"cursor:pointer\" class='"+(j%2==0?"Row1":"Row2")+"' onclick=\"select('dx',this);\" ondblclick=\"deleteRow();\">");
                    logicVariableTableDx.append("<td class=\"tdmini2\" width=\"60%\" align=\"left\">");
                    tmps[0]=datas[i++];
                    tmps[1]=datas[i++];
                    tmps[2]=datas[i++];
                    tmps[3]=datas[i++];
                    tmps[4]=datas[i++];
                                  
                    logicVariableTableDx.append(tmps[2]);
                    logicVariableTableDx.append("</td>");
                    logicVariableTableDx.append("<td class=\"tdmini2\" width=\"40%\" align=\"left\">");
                    logicVariableTableDx.append(tmps[4]);
                    logicVariableTableDx.append("</td>");
                     logicVariableTableDx.append("</tr>");
                    
                    invisibleTableDx.append("<tr>");
                    invisibleTableDx.append("<td>");
                    invisibleTableDx.append(tmps[0]);
                    invisibleTableDx.append("</td>");
                    invisibleTableDx.append("<td>");
                    invisibleTableDx.append(tmps[1]);
                    invisibleTableDx.append("</td>");
                    invisibleTableDx.append("<td>");
                    invisibleTableDx.append(tmps[2]);
                    invisibleTableDx.append("</td>");
                    invisibleTableDx.append("<td>");
                    invisibleTableDx.append(tmps[3]);
                    invisibleTableDx.append("</td>");
                    invisibleTableDx.append("<td>");
                    invisibleTableDx.append(tmps[4]);
                    invisibleTableDx.append("</td>");
                    invisibleTableDx.append("</tr>");
              }//for
          
          }//if
          
          else{//Modify
              LogicVariableBeanList variableBeanList= new LogicVariableBeanList();
              variableBeanList.loadLogicVariablesModifiable(null,new Integer((String)properties.get("idVariable")).intValue(),userSession.getIdSite(),language);
              
              //Set Tabella di destra
              LogicVariableBean logicVariableBean=null;
              for(int i=0;i<variableBeanList.size();i++){
                logicVariableBean=variableBeanList.getLogicVariable(i);  
                logicVariableTableDx.append("<tr  style=\"cursor:pointer\" class='"+(i%2==0?"Row1":"Row2")+"'  onclick=\"select('dx',this);\" ondblclick=\"deleteRow();\">");
                logicVariableTableDx.append("<td class=\"tdmini2\" width=\"60%\" >");
                logicVariableTableDx.append((logicVariableBean.getDescription()==null?"":logicVariableBean.getDescription()));
                logicVariableTableDx.append("</td>");
                logicVariableTableDx.append("<td class=\"tdmini2\" width=\"40%\" >");
                logicVariableTableDx.append((logicVariableBean.getDeviceDescription()==null?"":logicVariableBean.getDeviceDescription()));
                logicVariableTableDx.append("</td>");
                
                logicVariableTableDx.append("</tr>");
            
                invisibleTableDx.append("<tr>");
                invisibleTableDx.append("<td>");
                invisibleTableDx.append((logicVariableBean.getIdVariable()==-1?"":logicVariableBean.getIdVariable()));
                invisibleTableDx.append("</td>");
                invisibleTableDx.append("<td>");
                invisibleTableDx.append(logicVariableBean.getTypeTranslate(language));
                invisibleTableDx.append("</td>");
                invisibleTableDx.append("<td>");
                invisibleTableDx.append(logicVariableBean.getDescription());
                invisibleTableDx.append("</td>");
                invisibleTableDx.append("<td>");
                invisibleTableDx.append(logicVariableBean.getType());
                invisibleTableDx.append("</td>");
                invisibleTableDx.append("<td>");
                invisibleTableDx.append((logicVariableBean.getDeviceDescription()==null?"":logicVariableBean.getDeviceDescription()));
                invisibleTableDx.append("</td>");
                invisibleTableDx.append("</tr>");
            }//for
            
            logicVariableBean=variableBeanList.getLogicVariable(0);
            String operType=logicVariableBean.getOpertype();
            unitMeasurementSelect=new Integer(logicVariableBean.getIdUnitOfMeasurement()).toString();
            decimalsSelect = new Integer(logicVariableBean.getDecimal()).toString(); 
            switch(logicVariableBean.getMasterType()){
                case VariableInfo.TYPE_INTEGER:
                    checkedInputRadioInteger="checked";
                    functionList=functionMgr.getFunctionList(FunctionMgr.REAL);
                break;
                case VariableInfo.TYPE_ANALOGIC:
                    checkedInputRadioAnalogic="checked";
                    functionList=functionMgr.getFunctionList(FunctionMgr.REAL);
                break;
                case VariableInfo.TYPE_ALARM:
                	   checkedInputRadioAlarm="checked";
                       functionList=functionMgr.getFunctionList(FunctionMgr.BOOLEAN);
                       break;
                case VariableInfo.TYPE_DIGITAL:
                    checkedInputRadioDigital="checked";
                    functionList=functionMgr.getFunctionList(FunctionMgr.BOOLEAN);
                break;
          }//switch
          operationList=new StringBuffer();
          invisibleTableOperation=new StringBuffer();
          for(int i=0,j=0;i<functionList.size();i++){
              FunctionInfo info=(FunctionInfo)functionList.get(i);
              if(!info.getSymbol().equals("I")){
                  operationList.append("\n<option value=\"");
                  operationList.append(j);
                  operationList.append("\">");
                  String tmpSymbol = info.getSymbol();
                  if (null != tmpSymbol && tmpSymbol.startsWith("<")) {
                  	tmpSymbol = "&lt;"+tmpSymbol.substring(1);
                  }
                  operationList.append(tmpSymbol);
                  operationList.append("\n</option>");
                  invisibleTableOperation.append("<tr>");
                  invisibleTableOperation.append("<td>");
                  invisibleTableOperation.append(info.getCode());
                  invisibleTableOperation.append("</td>");
                  invisibleTableOperation.append("<td>");
                  invisibleTableOperation.append(info.getPars());
                  invisibleTableOperation.append("</td>");
                  invisibleTableOperation.append("</tr>");
                  if(operType.equals(info.getCode()))
                      operationSelect=new Integer(j).toString();
                  j++;
              }//if
            
          }//for


          }//else
    
  }//if

  userTransaction.setLogicVariableParameter(null);   
%>
<input type="hidden" id="confirmvardelete" value="<%=confirmvardelete%>" />
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>

<form name="frm_logicvariable" id="frm_logicvariable" action="servlet/master;jsessionid=<%=jsession%>" method="post">
	<input type="hidden" id="buttonEnable" name="buttonEnable" value="<%=buttonEnable%>" />
	<input type="hidden" id="action" name="action" value="" />
	<input type="hidden" id="idVariable" name="idVariable" value="<%=idVariable%>" />
	<input type="hidden" id="idDevice" name="idDevice" value="<%=idDevice%>" />
	<input type="hidden" id="functionCode" name="functionCode" value="<%=functionCode%>" />
	
	<input type="hidden" id="variableType" name="variableType" value="<%=variableType%>" />
	<input type="hidden" id="variableTable" name="variableTable" value="" />
	<input type="hidden" id="operationSelect" name="operationSelect" value="<%=operationSelect%>" />
	<input type="hidden" id="operationSimbol" name="operationSimbol" value="" />
	
	<input type="hidden" id="unitMeasurementSelect" name="unitMeasurementSelect" value="<%=unitMeasurementSelect%>" />
	<input type="hidden" id="unitMeasurementDescription" name="unitMeasurementDescription" value="" />
	<input type="hidden" id="decimalsSelect" name="decimalsSelect" value="<%=decimalsSelect%>" />
	<input type="hidden" id="variabledeviceid" name="variabledeviceid" value="<%=variabledeviceid %>"/>
	
<p class="StandardTxt"><%=logicvariablecomment%></p>
<%=htmlLogicVariable%>
<div id="lgcvarcreationtable" style="width:98%;">
    	<fieldset class="standardTxt">
      		<legend><%=variable%></legend>
      		<div>
      			<div class="lgcvarcreationtablerow">
					<table cellpadding="0" cellspacing="0" width="100%" border="0">
					<tr>
						<td class="tdmini2" width="10%" align="left"><%=desc%></td>
						<td class="tdmini2" align="left">
							<input <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> maxlength="150" size="100" type="text" id="description" name="description" value="<%=description%>"> 
						</td>
					</tr>
					</table>     			
      			</div>
      			<div class="lgcvarcreationtablerow">
	      			<table cellpadding="0" cellspacing="0" width="800px;"   border="0">
					<tr>
					  <td class="tdmini2" align="left" ><%=type%></td>
					  <td width="10%" class="tdmini2"  align="left">
					    <input  type="radio" name="typevar" id="inputRadioInteger" value="inputRadioInteger" onclick="checkVarType(3);" <%=checkedInputRadioInteger%> /><%=integer%>
					  </td>
					  <td width="10%" class="tdmini2" align="left" >
					    <input type="radio" name="typevar"  id="inputRadioAnalogic" value="inputRadioAnalogic" onclick="checkVarType(2);" <%=checkedInputRadioAnalogic%> /><%=analogic%>
					  </td>
					  <td width="10%" class="tdmini2" align="left" >
					    <input align="left" type="radio" name="typevar"  id="inputRadioDigital"  value="inputRadioDigital" onclick="checkVarType(1); " <%=checkedInputRadioDigital%> /><%=digital%>
					  </td>
					  <td width="10%" class="tdmini2" align="left" >
					    <input align="left" type="radio" name="typevar"  id="inputRadioAlarm"  value="inputRadioAlarm" onclick="checkVarType(4); " <%=checkedInputRadioAlarm%> /><%=alarm%>
					  </td>
					  <td width="30%" class="tdmini2" align="left" ><%=unitMeasurement%>&nbsp;<select class="standardTxt" name="unitMeasurementList" id="unitMeasurementList" onchange="changeUnitMeasurement();" ><%=unitMeasurementList%></select></td>
					  <td width="3%" class="tdmini2" align="left">&nbsp;</td>
					  <td class="tdmini2" align="left" ><%=decimals%>&nbsp;
					    <select class="standardTxt" name="decimals" id="decimals" onchange="changeDecimals()"><%=decimalsList%></select>
					  </td>
					</tr>
	            	</table>
      			</div>
      			<div class="lgcvarcreationtablerow">
      				<div style="width:100%" class="tdmini2"><span style="vertical-align:middle;margin-right:5px"><%=operation%></span><select class="standardTxt" name="operationList" id="operationList"  onchange="changeOperation();" style="width:60px"><%=operationList%></select></div>
      			</div>
      			<div class="lgcvarcreationtablerow">
      				<div class="tdmini2"><%=langService.getString("logicvariablePage","operands")%>:</div>
      			</div>
      			<div class="lgcvarcreationtablerow" style="height:250px;width:98%">
      				<div class="tdmini2">
						<div id="lgcvarselvarlleftcol">
							<table width="100%" cellpadding="1" cellspacing="1"  border="0">
								<tr>
				          			<td class="tdmini2" style="width:40%;text-align:left"><%=constant%></td>
				          			<td style="width:60%;"><input type="text" style="width:98%" maxlength="15" id="constant" disabled name="constant" <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> onblur="<%=typeCheck %>"  onkeydown="checkOnlyAnalog(this,event);" onclick="unselectAllRow('sx');"></td>
				          		</tr>
				          		<tr>
				          			<td class="tdmini2" style="width:40%;text-align:left"><%=varDevice%></td>
				          			<td style="width:60%"><select class="standardTxt" style="width:98%;overflow:hidden" name="deviceList" id="deviceList" onchange="changeDevice();" ><%=deviceList%></select></td>
				          		</tr>							
				          	</table>
							<div class="tdmini1" style="height: 200px; overflow: auto"><%=varList%>
								<div class="LogicVariableTable">
									<table class="table" width="100%" cellspacing="1" cellpadding="1">
									  	<tbody>
									    <tr class="th"> 
									      <td class="tdmini" width="*%"><%=variable%></td>
									      <td width="13px">&nbsp;</td>
									     </tr>
									  	</tbody>
									</table>
									<div id="divLogicVariableTableSx" class="LogicVariableTableBody"> 
										<table class="table" width="100%" cellspacing="1" cellpadding="1">
										  	<tbody><%=logicVariableTableSx%></tbody>
										</table>
									</div>
							    </div>		
							</div>
						</div>
						<div id="lgcvarselvarlrightcol">
							<div class="tdmini1" style="height: 250px; overflow: auto">
								<%=varLinked%>
					            <div class="LogicVariableTable"> 
									<table class="table" width="100%" cellspacing="1" cellpadding="1">
									  	<tbody>
									    <tr class="th"> 
									      <td class="tdmini" width="58%"><%=variable%></td>
									      <td class="tdmini" width="*"><%=device%></td>
									      <TD width="13px">&nbsp;</TD>
									    </tr>
									  	</tbody>
									</table>
									<div id="divLogicVariableTableDx" class="LogicVariableTableBody"> 
									  	<table width="100%" cellspacing="1" cellpadding="1">
									    	<tbody><%=logicVariableTableDx%></tbody>
									  	</table>
									</div>
					            </div>
					          </div>
						</div>
			            <img onclick="arrowAdd();return false;" src="images/dbllistbox/arrowdx_on.png" align="middle" />
						<br/><br/>
			            <img onclick="deleteRow();return false;" src="images/dbllistbox/delete_on.png" align="middle" />
      				</div>
      			</div>
			</div> 
      	</fieldset>		
		</div>
</form>
    
<div id="data" style="display:none">
  <div id="invisibleTableDx"> 
    <table class="table" width="100%" cellspacing="1" cellpadding="1">
       <tbody><%=invisibleTableDx%></tbody>
    </table>
  </div>
  <div id="invisibleTableSx">
    <table class="table" width="100%" cellspacing="1" cellpadding="1">
      <tbody><%=invisibleTableSx%></tbody>
    </table>
  </div>
  <div id="invisibleTableOperation"> 
    <table class="table" width="100%" cellspacing="1" cellpadding="1">
      <tbody><%=invisibleTableOperation%></tbody>
    </table>
  </div>
</div>      
<div id="translate" style="display:none">
    <div id="deleteRowTableMsg"><%=deleteRowTableMsg%></div>
    <div id="rowPresentMsg"><%=rowPresentMsg%></div>
    <div id="deleteRowTable1Msg"><%=deleteRowTable1Msg%></div>
    <div id="operatorOverflowMsg"><%=operatorOverflowMsg%></div>
    <div id="operatorInsertLessMsg"><%=operatorInsertLessMsg%></div>
    <div id="noDescMsg"><%=noDescMsg%></div>
    <div id="noVariablePresentMsg"><%=noVariablePresentMsg%></div>
    <div id="noDeleteVariablePresentMsg"><%=noDeleteVariablePresentMsg%></div>
</div>
<SCRIPT type="text/javascript">
	var thisbodyx,thisbodyy;
	if (self.innerHeight) // all except Explorer
	{
		thisbodyx = self.innerWidth;
		thisbodyy = self.innerHeight;
	}
	else if (document.documentElement && document.documentElement.clientHeight) // Explorer 6 Strict Mode
	{
		thisbodyx = document.documentElement.clientWidth;
		thisbodyy = document.documentElement.clientHeight;
	}
	else if (document.body) // other Explorers
	{
		thisbodyx = document.body.clientWidth;
		thisbodyy = document.body.clientHeight;
	};
	if(thisbodyy>550){
			//document.getElementById('divLogicVariableTableSx').style.height = thisbodyy-550;
			//document.getElementById('divLogicVariableTableDx').style.height = thisbodyy-550;
	};
	var variableType = document.getElementById("variableType").value;
	var constant = document.getElementById("constant");
	if(variableType == 1)
	{
		constant.disabled = false;
		constant.onkeydown=new Function("checkOnlyDigit(this,event)");
		constant.onblur = new Function("checkOnlyDigitOnBlur(this)");
	}
	else if(variableType == 2)
	{
		constant.disabled = false;
		constant.onkeydown=new Function("checkOnlyAnalog(this,event)");
		constant.onblur = new Function("checkOnlyAnalogOnBlur(this)");
	}
	else if(variableType == 3)
	{
		constant.disabled = false;
		constant.onkeydown=new Function("checkOnlyNumber(this,event)");
		constant.onblur = new Function("onlyNumberOnBlur(this)");
	}
</SCRIPT>