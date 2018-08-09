<%@ page language="java" import="java.util.*" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.*"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.presentation.bo.BAlrSched" 
import="com.carel.supervisor.presentation.timebands.TimeBands"
import="com.carel.supervisor.presentation.logicdevice.LogicDevice"
import="com.carel.supervisor.presentation.bean.LogicDeviceBeanList"
import="com.carel.supervisor.presentation.bean.LogicVariableBeanList"
import="com.carel.supervisor.presentation.bean.LogicVariableBean"
import="com.carel.supervisor.presentation.bean.DeviceListBean"
import="com.carel.supervisor.presentation.bo.BLogicDevice"
import="com.carel.supervisor.dataaccess.dataconfig.ProductInfo"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>


<%
	UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction userTransaction = userSession.getCurrentUserTransaction();
	String jsession = request.getSession().getId();
	//Lingua e Traduzioni
	String language = userSession.getLanguage();
	LangService langService = LangMgr.getInstance().getLangService(language);
	LangService lan = LangMgr.getInstance().getLangService(language);
    String logicdevicecomment = lan.getString("logicdevicePage","logicdevicecomment");

	String device = langService.getString("logicdevicePage","device");
	String desc = langService.getString("logicdevicePage","desc");
	String impVar = langService.getString("logicdevicePage","impvar");
	
	String physics   = langService.getString("logicdevicePage","physics");
	String logics    = langService.getString("logicdevicePage","logics");
	
	String physic   = langService.getString("logicdevicePage","physic");
	String logic    = langService.getString("logicdevicePage","logic");
	
	String integers  = langService.getString("logicdevicePage","integers");
	String digitals  = langService.getString("logicdevicePage","digitals");
	String analogics = langService.getString("logicdevicePage","analogics");
	String alarms    = langService.getString("logicdevicePage","alarms");
	
	String fromDevice    = langService.getString("logicdevicePage","fromdevice");
	String varList    = langService.getString("logicdevicePage","varlist");
	String varLinked    = langService.getString("logicdevicePage","varlinked");
	String variable    = langService.getString("logicdevicePage","variable");
	String type    = langService.getString("logicdevicePage","type");
	String typeOf    = langService.getString("logicdevicePage","typeof");
	
	String noRowSelectMsg =langService.getString("logicdevicePage","norowselectmsg");
	String noDescMsg =langService.getString("logicdevicePage","nodescmsg");
	String rowPresentMsg =langService.getString("logicdevicePage","rowpresentmsg");
	String noDevicePresentMsg=langService.getString("logicdevicePage","nodevicepresentmsg");
	String noVariablePresentMsg=langService.getString("logicdevicePage","novariablepresentmsg");
	String confirmdevicedelete =langService.getString("logicdevicePage","confirmdevicedelete");
	String maxLicenseLabel = langService.getString("siteview","maxlicenselabel");
	
	// Alessandro : determino se la tastiera virtuale è abilitata
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	
	//Tabella Dispositivi Logici	
	LogicDevice logicDevice=new LogicDevice();
	logicDevice.loadData(userSession);
	logicDevice.setScreenH(userSession.getScreenHeight());
	logicDevice.setScreenW(userSession.getScreenWidth());
	logicDevice.setWidth(903);
	logicDevice.setHeight(100);
	String htmlLogicDevice = logicDevice.getHTMLLogicDeviceTable("logicDeviceTable",language);
	
	//Select Combo Device Fisici
	LogicDeviceBeanList logicDeviceBeanList= new LogicDeviceBeanList();
	logicDeviceBeanList.loadDeviceDescription(null,userSession.getIdSite(),language);
	StringBuffer deviceList=new StringBuffer();
	deviceList.append("<option value=\"\" id=\"\" >---------------------");
	for(int i=0;i<logicDeviceBeanList.size();i++){
		deviceList.append("\n<option value=\"");
		deviceList.append(logicDeviceBeanList.getLogicDevice(i).getIddevice());
		deviceList.append("\">");
		deviceList.append(logicDeviceBeanList.getLogicDevice(i).getDescription());
		deviceList.append("\n</option>");
	}//for
	
	//Tabella Variabili associate al Device select sul DBClkEvent
	String descPost="";
	String buttonEnable="1";
	String checkedInput1="";
	String checkedInput2="";
	String checkedInput3="";
	String checkedInput4="";
	String checkedInput5="";
	String checkedInput6="";
	
	Properties properties= userTransaction.getLogicDeviceParameter();
	StringBuffer logicVariableTableDx= new StringBuffer();
	StringBuffer logicVariableTableSx= new StringBuffer();
	StringBuffer invisibleTableDx= new StringBuffer();
	StringBuffer invisibleTableSx= new StringBuffer();
	StringBuffer originalListVariableModify=new StringBuffer();
	
	
	StringBuffer logicVariableTableData= new StringBuffer();
	String idDevice="";
	String optionIdDevice="";

	logicVariableTableDx.append("");
	if(properties!=null){
			//recupero se ancora presente il device per la listbox
			optionIdDevice=((String)properties.get("input7"))==null?"":((String)properties.get("input7"));
			originalListVariableModify.append((String)properties.get("originalListVariableModify"));
			idDevice=((String)properties.get("idDevice"));
			for(;;){
			if(((String) properties.get("action")).equals(BLogicDevice.STATUS_ALL_ACTION_1)){//Modifica
					originalListVariableModify=new StringBuffer();
					buttonEnable="3";
					descPost=(String)properties.get("input0");
					LogicVariableBeanList variableBeanList= new LogicVariableBeanList();
					variableBeanList.loadVariableForDevice(null,new Integer((String)properties.get("idDevice")).intValue(),userSession.getIdSite(), language);
					LogicVariableBean logicVariableBean=null;
					for(int i=0;i<variableBeanList.size();i++){
							logicVariableBean=variableBeanList.getLogicVariable(i);
							logicVariableTableDx.append("<tr style=\"cursor:pointer\" class='"+(i%2==0?"Row1":"Row2")+"' onclick=\"select('dx',this);\" ondblclick=\"deleteRow();\">");
							logicVariableTableDx.append("<td class=\"tdmini2\" width=\"80%\" align=\"left\">");
							logicVariableTableDx.append(logicVariableBean.getDescription());
							logicVariableTableDx.append("</td>");
							logicVariableTableDx.append("<td class=\"tdmini\" width=\"20%\">");
							logicVariableTableDx.append(logicVariableBean.getTypeTranslate(language));
							logicVariableTableDx.append("</td>");
							//logicVariableTableDx.append("<td class=\"tdmini\" width=\"20%\">");
							//logicVariableTableDx.append(logicVariableBean.getIsDeviceLogic()==true?physic:logic);
							//logicVariableTableDx.append("</td>");
							logicVariableTableDx.append("</tr>");
							
							invisibleTableDx.append("<tr>");
							invisibleTableDx.append("<td>");
							invisibleTableDx.append(logicVariableBean.getIdVariable());
							invisibleTableDx.append("</td>");
							invisibleTableDx.append("<td>");
							invisibleTableDx.append(logicVariableBean.getIsDeviceLogic()==true?1:0);
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
							invisibleTableDx.append(logicVariableBean.getIsDeviceLogic()==true?1:0);
							invisibleTableDx.append("</td>");
							
							invisibleTableDx.append("</tr>");
							
							originalListVariableModify.append(logicVariableBean.getIdVariable());
							originalListVariableModify.append("@");
							
							
					}//for
					break;
			}//if
			if((((String) properties.get("action")).equals(BLogicDevice.STATUS_MODIFY_ACTION_INTEGER)||((String) properties.get("action")).equals(BLogicDevice.STATUS_ADD_ACTION_INTEGER))){
					descPost=(String)properties.get("input0");
					if(((String) properties.get("action")).equals(BLogicDevice.STATUS_ADD_ACTION_INTEGER))
							buttonEnable="1";
					else
							buttonEnable="3";
					String []datas=((String)properties.get("variableTable")).split("@");
					for(int i=0,j=0;(i<datas.length)&&(datas.length%6==0);j++){
							String []tmps= new String[6];
							logicVariableTableDx.append("<tr style=\"cursor:pointer\" class='"+(j%2==0?"Row1":"Row2")+"' onclick=\"select('dx',this);\" ondblclick=\"deleteRow();\">");
							logicVariableTableDx.append("<td class=\"tdmini2\" width=\"80%\" align=\"left\">");
							tmps[0]=datas[i++];
							tmps[3]=datas[i++];
							tmps[2]=datas[i++];
							tmps[1]=datas[i++];
							tmps[4]=datas[i++];
							tmps[5]=datas[i++];
							
							logicVariableTableDx.append(tmps[3]);
							logicVariableTableDx.append("</td>");
							logicVariableTableDx.append("<td class=\"tdmini\" width=\"20%\">");
							logicVariableTableDx.append(tmps[2]);
							logicVariableTableDx.append("</td>");
							//logicVariableTableDx.append("<td class=\"tdmini\" width=\"20%\">");
							//logicVariableTableDx.append(tmps[1].equals("1")?physic:logic);
							//logicVariableTableDx.append("</td>");
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
							invisibleTableDx.append("<td>");
							invisibleTableDx.append(tmps[5]);
							invisibleTableDx.append("</td>");
							
							invisibleTableDx.append("</tr>");
							
				
				}//for
				String typeGroup=(String)properties.get("typegroup");
				String typeVar=(String)properties.get("typevar");

				for(;;){
						if(typeGroup.equals("input1")){
								checkedInput1="checked";
								break;
						}//if 
						if(typeGroup.equals("input2")){
								checkedInput2="checked";
								break;
						}//if
						break;
				}//forswitch
				
				for(;;){
						if(typeVar.equals("input3")){
								checkedInput3="checked";
								break;
						}//if 
						if(typeVar.equals("input4")){
								checkedInput4="checked";
								break;
						}//if
						if(typeVar.equals("input5")){
								checkedInput5="checked";
								break;
						}//if
						if(typeVar.equals("input6")){
								checkedInput6="checked";
								break;
						}//if
						break;
				}//forswitch	
			

				LogicVariableBeanList variableBeanList= new LogicVariableBeanList();
				
				if(checkedInput1.equals("checked"))	//Variabili fisiche			
						variableBeanList.loadPhyisicalVariablesForDevice(null,new Integer((String)properties.get("input7")).intValue(),userSession.getIdSite(), language,new int[]{new Integer((String)properties.get("variableType")).intValue()});
				else//Variabili logiche
						variableBeanList.loadLogicalVariables(null,userSession.getIdSite(), language,new Integer((String)properties.get("variableType")).intValue());;
				LogicVariableBean logicVariableBean=null;
				for(int i=0;i<variableBeanList.size();i++){
							logicVariableBean=variableBeanList.getLogicVariable(i);
							logicVariableTableSx.append("<tr  style=\"cursor:pointer\" class= '"+(i%2==0?"Row1":"Row2")+"' onclick=\"select('sx',this);\" ondblclick=\"arrowAdd();\">");
							logicVariableTableSx.append("<td class=\"tdmini2\" width=\"100%\" >");
							logicVariableTableSx.append(logicVariableBean.getDescription());
							logicVariableTableSx.append("</td>");
							logicVariableTableSx.append("</tr>");
					
								
							invisibleTableSx.append("<tr>");
							invisibleTableSx.append("<td>");
							invisibleTableSx.append(logicVariableBean.getIdVariable());
							invisibleTableSx.append("</td>");
							invisibleTableSx.append("<td>");
							invisibleTableSx.append(logicVariableBean.getIsDeviceLogic()==true?1:0);
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
							invisibleTableSx.append(logicVariableBean.getIsDeviceLogic()==true?1:0);
							invisibleTableSx.append("</td>");
							
							invisibleTableSx.append("</tr>");
					
					}//for
				break;
			}//if
			
			break;
	}//forswitch

	}//if
	userTransaction.setLogicDeviceParameter(null); 
	String servererr = langService.getString("datatransfer", "servererr");//"---servererr";//
	//String serverok = langsrv.getString("logicdevice", "serverok");//"---serverok";//
	
	String control = userSession.getPropertyAndRemove("control");
	if (control==null) control="";	
	
	// Count active devices
	Integer numTotDevices=(Integer)DeviceListBean.countActiveDevice();
	ProductInfo productInfo= new ProductInfo();
	productInfo.load();
	Integer numMaxDevices= new Integer(productInfo.get(ProductInfo.LICENSE));	
%>
<input type="hidden" id="confirmdevicedelete" value="<%=confirmdevicedelete%>"/>
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>
<input type="hidden" name="control" id="control" value="<%=control%>"/>
<form name="frm_logicdevice" id="frm_logicdevice" action="servlet/master;jsessionid=<%=jsession%>" method="post">
	<input type="hidden" id="idDevice" name="idDevice" value="<%=idDevice%>"/>
	<input type="hidden" id="action" name="action" value="" />
	<input type="hidden" id="buttonEnable" name="buttonEnable" value="<%=buttonEnable%>" />
	<input type="hidden" id="variableTable" name="variableTable" value="<%=logicVariableTableData%>" />
	<input type="hidden" id="variableType" name="variableType" value="" />
	<input type="hidden" id="deviceId" name="deviceId" value="<%=optionIdDevice%>" />
	<input type="hidden" id="originalListVariableModify" name="originalListVariableModify" value="<%=originalListVariableModify%>" />
	<input type="hidden" id="variableToAddInModify" name="variableToAddInModify" value="" />
	<input type="hidden" id="variableToDeleteInModify" name="variableToDeleteInModify" value="" />
	<INPUT type="hidden" id="servererr" value="<%=servererr%>" /> 
	<!-- <INPUT type="hidden" id="serverok" value="< %=serverok%>" /> -->
  	<table cellpadding="0" cellspacing="0" width="100%" border="0">
  		<tr>
  			<td><p class="StandardTxt"><%=logicdevicecomment%></p></td>
  		</tr>
        <tr>
        	<td valign="top" width="100%"><%=htmlLogicDevice%></td>
        </tr>
        <tr>
          <td>
	          <fieldset class="field" style="margin-top:10px;">
		          <legend class="standardTxt"><%=device%></legend>
		          <table cellpadding="0" cellspacing="0" width="100%" border="0">
		          <tr>
		            <td valign="top" width="100%" align="center">
		                    <table cellpadding="0" cellspacing="0" border="0" width="100%">
		                        <tr>
		                        	<td class="tdmini2" width="10%" align="left"><%=desc%></td>
		                          <td class="tdmini2" width="*" align="left">
		                          	<input  <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> size="100" type="text" id="input0" name="input0" maxlength="70" value="<%=descPost%>"  />  
		                          </td>
		                        </tr>
		                        <tr>
		                            <td  colspan="2" align="center">
		                                <table cellpadding="0" cellspacing="0" width="100%" border="0">
		                                    <tr>
		                                        <td align="center" width="25%">
		                                            <table cellpadding="0" cellspacing="0" width="100%" border="0">
		                                                <tr>
		                                                    <td class="tdmini2"  align="left">
		                                                        <%=impVar%>
		                                                    </td>
		                                                    <td>
		                                                        <table cellpadding="0" cellspacing="0" width="100%" border="0">
		                                                            <tr>
		                                                                <td class="tdmini2">
		                                                                    <input type="radio" name="typegroup"  id="input1" value="input1" onclick="checkDevType('physic')" <%=checkedInput1%>/><%=physics%>
		                                                                </td>
		                                                            </tr>
		                                                            <tr>
		                                                                <td class="tdmini2">
		                                                                    <input  type="radio" name="typegroup"  id="input2" value="input2" onclick="checkDevType('logic')" <%=checkedInput2%>/><%=logics%>
		                                                                </td>
		                                                            </tr>
		                                                        </table>
		                                                    </td>
		                                                </tr>
		                                            </table>
		                                        </td>
		                                        <td  align="center" width="35%">
		                                            <table cellpadding="0" cellspacing="0" width="100%" border="0">
		                                        				<tr>
		                                        						<td width="30%" class="tdmini1" ><%=typeOf%></td>
		                                        						<td width="30%">
		                                        							<table cellpadding="0" cellspacing="0" width="100%" border="0">
							                                                <tr>
							                                                    <td class="tdmini2">
							                                                        <input align="left" type="radio" name="typevar" id="input3" value="input3" onclick="checkVarType(3);" <%=checkedInput3%> ><%=integers%><br>
							                                                    </td>
							                                                </tr>
							                                                <tr>
							                                                    <td class="tdmini2">
							                                                        <input align="left" type="radio" name="typevar"  id="input4"  value="input4" onclick="checkVarType(1); " <%=checkedInput4%> ><%=digitals%>
							                                                    </td>
							                                                </tr>
							                                            </table>
		                                        						</td>
		                                        						<td width="30%">
								                                            <table cellpadding="0" cellspacing="0" width="100%" border="0">
								                                                <tr>
								                                                    <td class="tdmini2">
								                                                        <input type="radio" name="typevar"  id="input5" value="input5" onclick="checkVarType(2);" <%=checkedInput5%> ><%=analogics%><br>
								                                                    </td>
								                                                </tr>
								                                                <tr>
								                                                    <td class="tdmini2">
								                                                        <input type="radio" name="typevar"  id="input6" value="input6" id="alarm" onclick="checkVarType(4);" <%=checkedInput6%> ><%=alarms%>
								                                                    </td>
								                                                </tr>
								                                            </table>
								                                        </td>
		                                        				</tr>
		                                        		</table>
		                                        </td>
		                                        <td align="center" width="*">
		                                        	<table cellpadding="0" cellspacing="0" width="100%" border="0">
			                                    				<tr>
				                                    				<td width="15%" class="tdmini1"><%=fromDevice%></td>
						                                        <td width="85" class="tdmini2" align="left">
						                                            <select class='standardTxt' name="input7" id="input7" onchange="changeDevice();" ><%=deviceList%></select>
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
				<tr style="height:5px"></tr>
		        <tr height="*">
		            <td>
		                <table border="0" width="100%">
		                    <tr>
		                        <td class="tdmini1" width="45%">
		                            <%=varList%>
		                            <div class="LogicVariableTable">
		                                <div> 
		                                    <table class="table" width="100%" cellspacing="1" cellpadding="1">
		                                        <tbody>
		                                            <tr class="th"> 
		                                            	  <td class="tdmini" width="*"><%=variable%></td>
		                                        				<TD width="13px">&nbsp;</TD>
		                                            </tr>
		                                        </tbody>
		                                    </table>
		                                </div>
		                                <div id="divLogicVariableTableSx" class="LogicVariableTableBody" style="height: 250px; overflow: auto"> 
		                                    <table class="table" width="100%" cellspacing="1" cellpadding="1">
		                                        <tbody><%=logicVariableTableSx%>
		                                        </tbody>
		                                    </table>
		                                </div>
		                            </div>
		                        </td>
		                        <td class="tdmini1" width="10%">
		                            <img onclick="arrowAdd();return false;" src="images/dbllistbox/arrowdx_on.png" align="middle" /><br>
		                            <br>
		                            <img onclick="deleteRow();return false;" src="images/dbllistbox/delete_on.png" align="middle" />
		                        </td>
		                        <td class="tdmini1" width="45%">
		                            <%=varLinked%>
		                            <div class="LogicVariableTable">
		                                <div> 
		                                    <table class="table" width="100%" cellspacing="1" cellpadding="1">
		                                        <tbody>
		                                            <tr class="th"> 
		                                                <td class="tdmini" width="77%"><%=variable%></td>
		                                                <td class="tdmini" width="*"><%=type%></td>
		                                                <TD width="13px">&nbsp;</TD>
		                                            </tr>
		                                        </tbody>
		                                    </table>
		                                </div>
		                                <div id="divLogicVariableTableDx" class="LogicVariableTableBody" style="height: 250px; overflow: auto"> 
		                                    <table width="100%" cellspacing="1" cellpadding="1">
		                                        <tbody><%=logicVariableTableDx%>
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
	     </fieldset>
     </td>
     </tr>
    </table>
</form>
<div id="data" style="display:none">
		<div id="invisibleTableDx"> 
				<table class="table" width="100%" cellspacing="1" cellpadding="1">
						<tbody>
								<%=invisibleTableDx%>
						</tbody>
				</table>
		</div>
		<div id="invisibleTableSx"> 
				<table class="table" width="100%" cellspacing="1" cellpadding="1">
						<tbody>
								<%=invisibleTableSx%>
						</tbody>
				</table>
		</div>
</div>

<div id="translate" style="display:none">
		<div id="noRowSelectMsg"><%=noRowSelectMsg%></div>
		<div id="noDescMsg"><%=noDescMsg%></div>
		<div id="rowPresentMsg"><%=rowPresentMsg%></div>
		<div id="noDevicePresentMsg"><%=noDevicePresentMsg%></div>
		<div id="noVariablePresentMsg"><%=noVariablePresentMsg%></div>
		<div id="physic"><%=physic%></div>
		<div id="logic"><%=logic%></div>
		<div id="numTotDevices"><%=numTotDevices%></div> 
		<div id="numMaxDevices"><%=numMaxDevices%></div>
		<div id="maxLicenseLabel"><%=maxLicenseLabel%></div>		
</div>
<SCRIPT type="text/javascript">
<!--
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
	}
	if(thisbodyy>550){
			//document.getElementById('divLogicVariableTableSx').style.height = thisbodyy-550;

//	alert(document.getElementById('vardiv').style.height);
			// document.getElementById('divLogicVariableTableDx').style.height = thisbodyy-550;
	}

//	document.getElementById('relaydiv').style.height = document.getElementById('divCollegeNames').style.height ;
 -->
</SCRIPT>