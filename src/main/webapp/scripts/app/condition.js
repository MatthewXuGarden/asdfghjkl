var GLB_COND_SELVAR = null;
var GLB_COND_SELDATA = null;
var GLB_COND_A = "@@";
var GLB_COND_B = "$?";
var GLB_COND_DATAS = "";
var listVar = null;
function CondloadTrx()
{	
	if(document.getElementById("hcondactcmd").value == "GET")
	{
		enableAction(3);
		disableAction(1);
	}
	else
	{
		enableAction(1);
		disableAction(2);
		disableAction(3);
		disableAction(4);
	}
	listVar = Lsw2;
	if(listVar.mData.length>0)
		document.getElementById("imgRemoveVar").src = REMOVE_ON;
	var hcondtype = document.getElementById("hcondtype").value;
	if(hcondtype == "3")
	{
		reload_actions(LOAD_DEVICE);
	}
}

function isNumeric() {
	var value=document.getElementById('condgecostva').value;
  	if (value == null || !value.toString().match(/^[-]?\d*\.?\d*$/)){
  	 alert(document.getElementById('consterror').value);
  	 return false;
  	}
  return true;
}


function selectCondition(idcond)
{
	if(!isEnableAction(3))
	{
		if(document.getElementById("hcondid").value == idcond)
		{
			document.getElementById("hcondid").value = 0;
			disableAction(2);
			disableAction(4);
		}
		else
		{
			document.getElementById("hcondid").value = idcond;
			enableAction(2);
			enableAction(4);
		}		
	}
	else
	{
		enableAction(1);	
		enableAction(2);
		disableAction(3);
		enableAction(4);
		
		document.getElementById("hcondid").value = idcond;
		document.getElementById("conddesc").value = "";
		document.getElementById("hcondactcmd").value = "";
		document.getElementById("condtype1").checked = true;
	}
	
	return false;
}


function modifyAlarmCondition()
{
	var idcond = document.getElementById("hcondid").value;
	CondgetForUpd(idcond);
}


function CondgetForUpd(idcond)
{
	document.getElementById("hcondactcmd").value = "GET";
	document.getElementById("hcondid").value = idcond;
	if (idcond<0)	
	{
		alert(document.getElementById("nomodcondfromide").value);   //luca
	}
	else
	{
		var ofrm = document.getElementById("frmcond");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function changeType(obj)
{
	var objId = obj.id;
	if(objId == "condtype3")
	{
		document.getElementById('model').disabled = true;
		reload_actions(LOAD_DEVICE);
		return false;
	}
	else if(objId == "condtype3_model")
	{
		document.getElementById('model').disabled = false;
		reload_actions(LOAD_DEVICE_MDL);
		return false;
	}
	document.getElementById("hcondcmd").value = "changetype";
	document.getElementById("hcondtype").value = obj.value;
	document.getElementById("hconddesc").value = document.getElementById("conddesc").value;
	var ofrm = document.getElementById("frmcond");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
	
	return false;
}
var LOAD_DEVICE_VAR		= 0;
var LOAD_DEVMDL_VAR		= 10;
var LOAD_DEVICE			= 1;
var LOAD_DEVICE_MDL		= 2;
var ADD_ON				= "images/actions/addsmall_on_black.png";
var ADD_OFF				= "images/actions/addsmall_off.png";
var REMOVE_ON			= "images/actions/removesmall_on_black.png";
var REMOVE_OFF			= "images/actions/removesmall_off.png";
var strSelectedDev		= "";
var idSelectedDevVar	= 0;
var IdFromVarMdl 	= false;
function reload_actions(action)
{	
	var devSel = document.getElementById('devsel');
	var cmdDeviceModel = document.getElementById('model');
	var toSend = "";
	var functionToRecall = "";
	document.getElementById("imgAddVar").src = ADD_OFF;
	if(action==LOAD_DEVICE_VAR){
		emptySelect("varsel");
		IdFromVarMdl = false;
		var deviceSelected = 0;
		for(var i = 0; i < devSel.options.length; i++) {
			if( devSel.options[i].selected) 
			{
				deviceSelected++;
				if(deviceSelected>1)
					break;
			}
		}
		if(deviceSelected == 1)
		{
			idSelectedDev = devSel.options[devSel.selectedIndex].value;
			strSelectedDev = devSel.options[devSel.selectedIndex].text;
			toSend=LOAD_DEVICE_VAR+"&iddevice="+idSelectedDev;
		}
		else if(deviceSelected>1)
		{
			idSelectedDev = cmdDeviceModel.value;
			strSelectedDev = devSel.options[devSel.selectedIndex].text;
			toSend=LOAD_DEVMDL_VAR+"&iddevice="+idSelectedDev;
			IdFromVarMdl = true;
		}
		functionToRecall="loadVariable";
	}
	else if(action==LOAD_DEVICE){
		emptySelect("varsel");
		cmdDeviceModel.selectedIndex=0;
		toSend=LOAD_DEVICE+"&iddevice=-1";
		functionToRecall="loadDevice";
	}
	else if(action==LOAD_DEVICE_MDL){
		aaDevVarCache = new Array();
		
		emptySelect("varsel");
		emptySelect("devsel");
		toSend=LOAD_DEVICE_MDL+"&iddevice="+cmdDeviceModel.options[cmdDeviceModel.selectedIndex].value;
		functionToRecall="loadDeviceAndVariable";
	}
		
	if( toSend != "" )
		CommSend("servlet/ajrefresh",
			"POST",
			"action="+toSend,
			functionToRecall, 
			true);
}
function emptySelect(idobject)
{
	if(document.getElementById(idobject)!=null){
		num_option=document.getElementById(idobject).options.length;
		for(i=num_option;i>=0;i--){
			document.getElementById(idobject).options[i]=null;
		}
	}
}
function Callback_loadVariable()
{
	document.getElementById("div_var").innerHTML = xmlResponse.getElementsByTagName("variable")[0].childNodes[0].nodeValue;
}
function Callback_loadDevice()
{
	document.getElementById("model").disabled = true;
	document.getElementById("div_dev").innerHTML = xmlResponse.getElementsByTagName("device")[0].childNodes[0].nodeValue;
	idSelectedDevVar = 0;
}
function Callback_loadDeviceAndVariable()
{
	document.getElementById("div_dev").innerHTML = xmlResponse.getElementsByTagName("device")[0].childNodes[0].nodeValue;
	idSelectedDevVar = 0;
}

var COL_I1		= 1;
var COL_I2		= 2;
var COL_ALARM	= 3;
var COL_DELETE	= 4;
function addAlarmToVarSel()
{
	var devSel = document.getElementById('devsel');
	var varSel = document.getElementById('varsel');
	var duplicated = false;
	for(var i = 0; i < devSel.options.length; i++) {
		if( devSel.options[i].selected) 
		{
			var devDescr = devSel.options[i].text;
			var devId = devSel.options[i].value;
			
			for(var j = 0; j < varSel.options.length; j++) 
			{
				if(varSel.options[j].selected)
				{
					var varDescr = varSel.options[j].text;
					var varId = varSel.options[j].value;
					if(IdFromVarMdl)
						varId = devId+"-"+varId;
					if(CondCheckData(varId)) {
						var element = new Array("", 0, varId,
								devDescr+" -&gt; "+varDescr,
								""
								);
						listVar.mData.splice(0, 0, element);
					}
					else
					{
						 alert(document.getElementById("variabledouble").value);
						 duplicated = true;
						 break;
					}
				}
			}
			if(duplicated)
				break;
		}
	}
	for(j = 0; j < varSel.options.length; j++) 
	{
		varSel.options[j].selected = false; 
	}
	document.getElementById("imgAddVar").src = ADD_OFF;
	// update items index
	for(var i = 0; i < listVar.mData.length; i++) {
		listVar.mData[i][COL_DELETE] = "<img src='images/actions/removesmall_on_black.png' style='cursor:pointer;' onclick='deleteAlarm("+i+")'>";
	}
	if(listVar.mData.length>0)
		document.getElementById("imgRemoveVar").src = REMOVE_ON;
	listVar.rowSelected = null;
	listVar.numRows = listVar.mData.length;
	listVar.render();
}
function onSelectAlarms()
{
	document.getElementById("imgAddVar").src = ADD_ON;
}
function deleteAlarm(i)
{
	listVar.mData.splice(i, 1);
	// update items index; no need to update items address
	for(; i < listVar.mData.length; i++) 
	{
		listVar.mData[i][COL_DELETE] = "<img src='images/actions/removesmall_on_black.png' style='cursor:pointer;' onclick='deleteAlarm("+i+")'>";
	}
	if(listVar.mData.length == 0)
		document.getElementById("imgRemoveVar").src = REMOVE_OFF;
	listVar.rowSelected = null;
	listVar.numRows = listVar.mData.length;
	listVar.render();
}
function removeAllAlarms()
{
	listVar.mData = [];
	listVar.rowSelected = null;
	listVar.numRows = listVar.mData.length;
	listVar.render();
	document.getElementById("imgRemoveVar").src = REMOVE_OFF;
}

function CondAdd()
{
	CondBuildData();
	var description = document.getElementById("conddesc").value;
	if( description != "" && !(document.getElementById("condtype3").checked && GLB_COND_DATAS == ""))
	{
		// check for duplicates
		var listConditions = Lsw1.mData;
		for(var i = 0; i < listConditions.length; i++) {
			if( description == listConditions[i][4] ) {
				alert(document.getElementById("duplicatecodemsg").value);
				return;
			}
		}
		
		document.getElementById("hcondactcmd").value = "ADD";
		document.getElementById("hcondvariabili").value = GLB_COND_DATAS;
		var ofrm = document.getElementById("frmcond");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
	else
	{
		alert(document.getElementById("allfields").value);
	}
}

function CondRem()
{
	if (confirm(document.getElementById("confirmconddel").value))
	{
		document.getElementById("hcondactcmd").value = "REM";
		var idcond = Number(document.getElementById("hcondid").value);
		if (idcond<0)
		{
			alert(document.getElementById("noremcondfromide").value);   
		}
		else
		{
			var ofrm = document.getElementById("frmcond");
			if(ofrm != null)
				MTstartServerComm();
			ofrm.submit();
		}
	}
}

function CondUpd()
{
	CondBuildData();
	if(document.getElementById("conddesc").value != "" && !(document.getElementById("condtype3").checked && GLB_COND_DATAS == ""))
	{
		document.getElementById("hcondactcmd").value = "UPD";
		document.getElementById("hcondvariabili").value = GLB_COND_DATAS;
		var ofrm = document.getElementById("frmcond");
		if(ofrm != null)
				MTstartServerComm();
		ofrm.submit();
	}
	else
	{
		alert(document.getElementById("allfields").value);
	}
}

function CondBuildData()
{
	if(document.getElementById("condtype3_model").checked)
	{
		document.getElementById("condtype3").checked = true;
	}
	GLB_COND_DATAS = "";
	for(var i=0;i < listVar.mData.length; i++)
	{
		if(GLB_COND_DATAS != "")
			GLB_COND_DATAS = GLB_COND_DATAS+GLB_COND_A+listVar.mData[i][COL_I2]+GLB_COND_B+listVar.mData[i][COL_ALARM];
		else
			GLB_COND_DATAS = listVar.mData[i][COL_I2]+GLB_COND_B+listVar.mData[i][COL_ALARM];
	}
}

function CondCheckData(strid)
{
	for(var i=0;i < listVar.mData.length; i++)
	{
		if(strid == listVar.mData[i][COL_I2])
		{
			return false;
		}
	}
	return true;
}

// General
function CondGeLoad()
{
	enableAction(1);
	
	var objck = document.getElementById("condgetypev");
	if(objck.checked)
		changeGeType(objck);
		
	var isDigit = document.getElementById("hcondgeisdigit").value;
	
	if((isDigit=="T")) {
		changeLookAndFee1(true);
	}
	else {
		changeLookAndFee1(false);
	}
	
	if(document.getElementById("hcondgecmd").value == "get")
	{
		enableAction(3);
		disableAction(1);
	}
	else
	{
		if(document.getElementById("hcondgeid").value == "")
		{
			enableAction(1);
			disableAction(2);
			disableAction(3);
		}
		else
		{
			disableAction(1);
			disableAction(2);
			enableAction(3);
		}
	}
}

function selectConditionGen(idcond)
{
	if(!isEnableAction(3))
	{
		if(document.getElementById("hcondgeid").value == idcond)
		{
			document.getElementById("hcondgeid").value = 0;
			disableAction(2);
			disableAction(4);
		}
		else
		{
			document.getElementById("hcondgeid").value = idcond;
			enableAction(2);
			enableAction(4);
		}		
	}
	else
	{
		enableAction(1);	
		enableAction(2);
		disableAction(3);
		enableAction(4);
	}
	return false;
}


function modifyEventCondition()
{
	var idcond = document.getElementById("hcondgeid").value;
	CondgetForUpdGen(idcond);
}


function CondgetForUpdGen(idcond)
{
	document.getElementById("hcondgecmd").value = "get";
	document.getElementById("hcondgeid").value = idcond;
	if (Number(idcond)<0)
	{
		alert(document.getElementById("nomodcondfromide").value);  
	}
	else
	{
		var ofrm = document.getElementById("frmcondge");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();	
	}
}

function trim(stringa){
	return stringa.replace(/^\s+|\s+$/g,"");
}

function CondGeAdd()
{
	document.getElementById("hcondgecmd").value = "add";
	var ofrm = document.getElementById("frmcondge");
	if(check_save()){
	  if(ofrm != null)
			MTstartServerComm();
	  ofrm.submit();
	}
}

function check_save(){
	var description = trim(document.getElementById("conddesc").value);
	if(document.getElementById("condgedev").value == '0' || 
	   document.getElementById("condgevar").value == '0' ||
	   description == ""){
		alert(document.getElementById("requiredfield").value);
		return false;
	}
	
	if(document.getElementById("condgetypev").checked){
		if(document.getElementById("condgedev2").value == '0' ||
		     document.getElementById("condgevar2").value == '0'){
			 alert(document.getElementById("setrequiredvar").value);
			 return false;
	    }
	}
	
	if(document.getElementById("condgetdivoptd").style.display != "none"){
			if(document.getElementById("idcondgeoptdigit").value == '0'){
				alert(document.getElementById("setoperationreq").value);
				return false;
			}
		}
	if(document.getElementById("condgetdivopt").style.display != "none"){
			if(document.getElementById("idcondgeoptanal").value == '0'){
				alert(document.getElementById("setoperationreq").value);
				return false;
			}
		}
		
	if(document.getElementById("condgetypec").checked){
	  if(document.getElementById("condgedivcosta").style.display != "none"){
			if(document.getElementById("condgecostva").value == ""){
				alert(document.getElementById("setcostantreq").value);
				return false;
			}
			return isNumeric();
	   }
	}
	
	// check for duplicates
	var listConditions = Lsw1.mData;
	for(var i = 0; i < listConditions.length; i++) {
		if( description == listConditions[i][4] ) {
			alert(document.getElementById("duplicatecodemsg").value);
			return false;
		}
	}
	
	return true;
}


function CondGeRem()
{
	if (confirm(document.getElementById("confirmconddel").value))
	{
		var id = Number(document.getElementById("hcondgeid").value);
		if (id<0)
		{
			alert(document.getElementById("noremcondfromide").value);  
		}
		else
		{
			document.getElementById("hcondgecmd").value = "rem";
			var ofrm = document.getElementById("frmcondge");
			if(ofrm != null)
				MTstartServerComm();
			ofrm.submit();
		}
	}
}

function CondGeUpd()
{
	document.getElementById("hcondgecmd").value = "upd";
	var ofrm = document.getElementById("frmcondge");
	if(check_save()){
	if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function changeDeviceGen(obj)
{
	document.getElementById("hcondgecmd").value = "dev";
	var ofrm = document.getElementById("frmcondge");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function changeVariableGen(obj)
{
	var idvar = obj.value;	
	var isDigit = false;
	document.getElementById("hcondgeisdigit").value = "F";
	if(idvar.indexOf("D") != -1) {
		isDigit = true;
		idvar = idvar.substring(0,idvar.length-1);
		document.getElementById("hcondgeisdigit").value = "T";
	}
	
	document.getElementById("hcondgecurvar").value = idvar
	changeLookAndFee1(isDigit);
	
	document.getElementById("condgedev2").value = 0;
	document.getElementById("condgevar2").value = 0;
}

function changeLookAndFee1(bstate)
{
	if(bstate) 
	{
		// Select Operator Digit
		document.getElementById("condgetdivoptd").style.visibility="visible";
		document.getElementById("condgetdivoptd").style.display="block";
		// Select Operator Analogic
		document.getElementById("condgetdivopt").style.visibility="hidden";
		document.getElementById("condgetdivopt").style.display="none";
		// Value Box
		document.getElementById("condgedivcosta").style.display="none";
		document.getElementById("condgedivcostd").style.display="block";
	}
	else 
	{
		// Select Operator Digit
		document.getElementById("condgetdivoptd").style.visibility="hidden";
		document.getElementById("condgetdivoptd").style.display="none";
		// Select Operator Analogic
		document.getElementById("condgetdivopt").style.visibility = "visible";
		document.getElementById("condgetdivopt").style.display = "block";
		// Value Box
		document.getElementById("condgedivcostd").style.display="none";
		document.getElementById("condgedivcosta").style.display="block";
	}
}

function changeGeType(obj)
{
	
	var permict = document.getElementById("permict").value;
	if (permict=="true")
	{
		switch(Number(obj.value))
		{
			case 1:
				document.getElementById("condgedev2").disabled=false;
				document.getElementById("condgevar2").disabled=false;
				document.getElementById("condgecostva").disabled=true;
				document.getElementById("condgecostvd").disabled=true;
				break;
			case 5:
				document.getElementById("condgedev2").selectedIndex=0;
				document.getElementById("condgevar2").selectedIndex=0;
				document.getElementById("condgedev2").disabled=true;
				document.getElementById("condgevar2").disabled=true;
				
				
				document.getElementById("condgecostva").disabled=false;
				document.getElementById("condgecostvd").disabled=false;
				break;
		}
	}
}

function changeDeviceGenFun(obj)
{
	document.getElementById("hcondgecmd").value = "devfun";
	var ofrm = document.getElementById("frmcondge");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function isremoved()
{
	if (document.getElementById("s_removed").value=="NO")
	{
		alert(document.getElementById("conditionnotremoved").value);
	}
	if (document.getElementById("s_removed").value=="NoDefault")
	{
		alert(document.getElementById("defaultConditionRemove").value);
	}
}

/**
* Per ridimensionare la tabella Condizioni di Allarme
*/
function resizeTableAlrmCond()
{
	var hdev = MTcalcObjectHeight("trAlrmCondList");
	if(hdev != 0){
		MTresizeHtmlTable(1,hdev-30);
	}
}


/**
* Per ridimensionare la tabella Condizioni Evento
*/
function resizeTableEventCond()
{
	var hdev = MTcalcObjectHeight("trCondEvn");
	if(hdev != 0){
		MTresizeHtmlTable(1,hdev);
	}
}

