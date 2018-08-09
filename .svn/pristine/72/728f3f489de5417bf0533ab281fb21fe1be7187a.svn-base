// Optimum NightFreeCooling
// dashboard
// actions
var ACTION_START 		= 1;
var ACTION_STOP			= 2;
var ACTION_REFRESH		= 3;
// algorithm enabled
var bEnabled = false;
// refresh interval
var REFRESH_INTERVAL	= 30 * 1000; // 30 seconds


function onLoadDashboard()
{
	bEnabled = document.getElementById("Enabled").value == 'true';
	bConfigured = document.getElementById("Configured").value == 'true';
	if( bEnabled )
		enableAction(ACTION_STOP);
	else
		if(bConfigured)
			enableAction(ACTION_START);
	enableAction(ACTION_REFRESH);
	//setTimeout("refresh()", REFRESH_INTERVAL);
}


function onStart()
{
	document.getElementById("cmd").value = "start";
	var form = document.getElementById("frm_opt_dashboard");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onStop()
{
	document.getElementById("cmd").value = "stop";
	var form = document.getElementById("frm_opt_dashboard");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onRefresh()
{
	document.getElementById("cmd").value = "refresh";
	var form = document.getElementById("frm_opt_dashboard");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onDashboardReport()
{
	document.getElementById("cmd").value = "prev7";
	var form = document.getElementById("frm_opt_dashboard");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function refresh()
{
	new AjaxRequest("servlet/ajrefresh", "POST", "action=refresh", Callback_refresh, false);
	return true;
}


function Callback_refresh(xml)
{
	var axmlParams = xml.firstChild.childNodes;
	for(var i = 0; i < axmlParams.length; i++)
		document.getElementById("span" + axmlParams[i].nodeName).innerHTML = axmlParams[i].getAttribute("value");
	setTimeout("refresh()", REFRESH_INTERVAL);
}


// report
var tabSelected			= null;


function onReport(type)
{
	document.getElementById("cmd").value = type;
	var form = document.getElementById("frm_opt_report");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSelectTab(tab)
{
	if( tab == tabSelected )
		return;
	

	if( !tabSelected ) {
		tabSelected = document.getElementById("tabChart");
	}

	tabSelected.className = "egtabnotselected2";
	var id = tabSelected.id;
	var name = id.replace("tab","");
	document.getElementById("div" + name).style.display = "none";
	
	tab.className = "egtabselected2";
	id = tab.id;
	name = id.replace("tab","");
	document.getElementById("div" + name).style.display = "block";
	
	tabSelected = tab;
}


// settings
// actions
var ACTION_ADD			= 1;
var ACTION_DEL			= 2;
var ACTION_SAVE			= 3;

var LOAD_DEVICE_VAR		= 0;
var LOAD_DEVICE			= 1;
var LOAD_DEVICE_MDL		= 2;

var MAX_SLAVES			= 3;

var strSelectVar		= "";
var idSelectedDev		= 0;
var strSelectedDev		= "";
var idSelectedDevVar	= 0;
var strSelectedDevVar	= "";

var bLightsConfigured	= false;


function saveSettings()
{	
	// check required params
	var bName = document.getElementById("Name").value != "";
	var bVarInternalTemperature = document.getElementById("VarInternalTemperature").value > 0;
	var bVarExternalTemperature = document.getElementById("VarExternalTemperature").value > 0;
	var bVarUnitOnOff = document.getElementById("VarUnitOnOff").value > 0;
	var bVarUnitOnOffCmd = document.getElementById("VarUnitOnOffCmd").value > 0;
	var bSlave = true;
	var nSlaveNo = document.getElementById("SlaveNo").value;
	for(var i = 0; i < MAX_SLAVES; i++) {
		if( i < nSlaveNo ) {
			if( !(document.getElementById("VarUnitOnOff" + (i+1)).value > 0 
				&& document.getElementById("VarUnitOnOffCmd" + (i+1)).value > 0) )
				bSlave = false;
		}
		else {
			document.getElementById("VarUnitOnOff" + (i+1)).value = "0";
			document.getElementById("VarUnitOnOffCmd" + (i+1)).value = "0";
		}
	}
	if( !(bName
		&& bVarInternalTemperature && bVarExternalTemperature
		&& bVarUnitOnOff && bVarUnitOnOffCmd
		&& bSlave) ) {
		alert(document.getElementById("alert_req_var").value);
		return;
	}
	
	var nSummerStartMonth = parseInt(document.getElementById("SummerStartMonth").value, 10);
	var nSummerStartDay = parseInt(document.getElementById("SummerStartDay").value, 10);
	var nSummerEndMonth = parseInt(document.getElementById("SummerEndMonth").value, 10);
	var nSummerEndDay = parseInt(document.getElementById("SummerEndDay").value, 10);
	
	if((nSummerStartMonth==2 && nSummerStartDay>28) || ((nSummerStartMonth==4 || nSummerStartMonth==6 || nSummerStartMonth==9 || nSummerStartMonth==11) && nSummerStartDay==31))
	{
		alert(document.getElementById("alert_err_start_date").value);
		return;
	}
		
	if((nSummerEndMonth==2 && nSummerEndDay>28) || ((nSummerEndMonth==4 || nSummerEndMonth==6 || nSummerEndMonth==9 || nSummerEndMonth==11) && nSummerEndDay==31))
	{
		alert(document.getElementById("alert_err_end_date").value);
		return;
	}
	
	// submit
	document.getElementById("cmd").value = "save";
	var form = document.getElementById("frm_opt_nightfc_settings");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onLoadSettings()
{
	onSlaveNo(document.getElementById("SlaveNo").value);
	if( document.getElementById("configured").value == "true" )
		enableAction(ACTION_ADD);
	if( document.getElementById("AlgorithmId").value > 1 )
		enableAction(ACTION_DEL);
	enableAction(ACTION_SAVE);
	bLightsConfigured = document.getElementById("LightsConfigured").value == 'true';
	checkButtons();
	MAX_INSTANCES = parseInt(document.getElementById("max_instances").value, 10);
}


function onSelectDevVar(i)
{
	var objDevVar = document.getElementById("devvar");
	idSelectedDevVar = objDevVar.options[i].value;
	strSelectedDevVar = objDevVar.options[i].text;
	checkButtons();
}


function selectVar(name)
{
	if( strSelectVar != "" )
		document.getElementById("btn" + strSelectVar).disabled = false;
	strSelectVar = name;
	document.getElementById("btn" + strSelectVar).disabled = true;
	document.getElementById("divVarSelection").style.display = "block";
}


function clearVar(name)
{
	document.getElementById("Var" + name).value = 0;
	document.getElementById("Name" + name).innerHTML = "------";
	document.getElementById("Name" + name).style.display = "none";
	document.getElementById(name).style.display = "inline";
	checkButtons();
}


function onSelCancel()
{
	document.getElementById("btn" + strSelectVar).disabled = false;	
	strSelectVar = "";
	document.getElementById("divVarSelection").style.display = "none";
}


function setVar()
{
	if( strSelectVar != "" && idSelectedDevVar > 0 ) {
		document.getElementById("Var" + strSelectVar).value = idSelectedDevVar;
		document.getElementById("Name" + strSelectVar).innerHTML = strSelectedDev + " --> " + strSelectedDevVar;
		document.getElementById("btn" + strSelectVar).disabled = false;	
		document.getElementById("Name" + strSelectVar).style.display = "inline";
		if((document.getElementById(strSelectVar))!=null)	
			document.getElementById(strSelectVar).style.display = "none";
		strSelectVar = "";
	}
	document.getElementById("divVarSelection").style.display = "none";
	checkButtons();
}


function enableModelSelection()
{
	document.getElementById("model").disabled = false;
	emptySelect("dev");
	emptySelect("devvar");
	idSelectedDevVar = 0;
	astrVarTypes = null;
	checkButtons();
}


function emptySelect(idobject)
{
	if( document.getElementById(idobject) != null ) {
		var num_option = document.getElementById(idobject).options.length;
		for(var i=num_option; i>=0; i--) {
			document.getElementById(idobject).options[i]=null;
		}
	}
}


function checkButtons()
{
	var bVarInternalHumidity = document.getElementById("VarInternalHumidity").value > 0;
	if(bVarInternalHumidity)
		document.getElementById("InternalHumidity").style.display = "none";
	document.getElementById("btnClearInternalHumidity").disabled = !bVarInternalHumidity;
	
	var bVarExternalHumidity = document.getElementById("VarExternalHumidity").value > 0;
	if(bVarExternalHumidity)
		document.getElementById("ExternalHumidity").style.display = "none";
	document.getElementById("btnClearExternalHumidity").disabled = !bVarExternalHumidity;
	
	var bVarTemperatureSetpoint = document.getElementById("VarTemperatureSetpoint").value > 0;
	if(bVarTemperatureSetpoint)
		document.getElementById("TemperatureSetpoint").style.display = "none";
	document.getElementById("btnClearTemperatureSetpoint").disabled = !bVarTemperatureSetpoint;
	
	var bVarHumiditySetpoint = document.getElementById("VarHumiditySetpoint").value > 0;
	if(bVarHumiditySetpoint)
		document.getElementById("HumiditySetpoint").style.display = "none";
	document.getElementById("btnClearHumiditySetpoint").disabled = !bVarHumiditySetpoint;
	
	document.getElementById("btnSetVar").disabled = idSelectedDevVar == 0;
	
	document.getElementById("btnAutoOff").disabled = !bLightsConfigured;	
}


function reload_actions(action)
{	
	var insertMode = "singleDevice";
	var cmbDevice = document.getElementById('dev');
	var cmdDeviceModel = document.getElementById('model');
	var toSend = "";
	var functionToRecall = "";

	if(action==LOAD_DEVICE_VAR){
		if( insertMode == "singleDevice" ) {
			emptySelect("devvar");
			deviceSelected=cmbDevice.options[cmbDevice.selectedIndex].innerHTML;

			idSelectedDev = cmbDevice.options[cmbDevice.selectedIndex].value;
			strSelectedDev = cmbDevice.options[cmbDevice.selectedIndex].text;
			
			toSend=LOAD_DEVICE_VAR+"&iddevice="+idSelectedDev;
			functionToRecall="loadVariable";
		}
		if( insertMode == "multipleDevices" ) {
			for(var i = 0; i < cmbDevice.options.length; i++) {
				if( cmbDevice.options[i].selected && !isDeviceOnCache(cmbDevice.options[i].value) ) {
					idSelectedDev = cmbDevice.options[i].value;
					strSelectedDev = cmbDevice.options[i].text;

					emptySelect("devvar");
					deviceSelected=cmbDevice.options[cmbDevice.selectedIndex].innerHTML;

					toSend=LOAD_DEVICE_VAR+"&iddevice="+idSelectedDev;
					functionToRecall="loadVariable";
					break;
				}
			}
		}
	}
	else if(action==LOAD_DEVICE){
		insertMode = "singleDevice";
		emptySelect("devvar");
		cmdDeviceModel.selectedIndex=0;
		toSend=LOAD_DEVICE+"&iddevice=-1";
		functionToRecall="loadDevice";
	}
	else if(action==LOAD_DEVICE_MDL){
		insertMode = "multipleDevices";
		aaDevVarCache = new Array();
		
		emptySelect("devvar");
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


function Callback_loadDeviceAndVariable(xmlResponse)
{
	document.getElementById("div_dev").innerHTML = xmlResponse.getElementsByTagName("device")[0].childNodes[0].nodeValue;
	idSelectedDevVar = 0;
	astrVarTypes = null;
	checkButtons();
}


function Callback_loadVariable(xmlResponse)
{
	document.getElementById("div_var").innerHTML = xmlResponse.firstChild.firstChild.nodeValue;
	idSelectedDevVar = 0;
	astrVarTypes = document.getElementById("csvVarType").value.split(",");
	checkButtons();
	
	if( insertMode == "multipleDevices" ) {
		insertDeviceOnCache();
		setTimeout("retrieveDevicesForCache()", 10); // call it after callback ends
	}
}


function Callback_loadDevice(xmlResponse)
{
	document.getElementById("model").disabled = true;
	document.getElementById("div_dev").innerHTML = xmlResponse.firstChild.firstChild.nodeValue;
	idSelectedDevVar = 0;
	astrVarTypes = null;
	checkButtons();
}


function onSlaveNo(nSlaveNo)
{
	var tabParams = document.getElementById("tabParams");
	var nBase = 4; // 1st row of 1st slave
	for(var i = 0; i < MAX_SLAVES; i++) {
		strDisplay = i < nSlaveNo ? "block" : "none";
		tabParams.rows[nBase + i * 2].style.display = strDisplay;
		tabParams.rows[nBase + i * 2 + 1].style.display = strDisplay;
	}
}


function addAlgorithm()
{
	if( document.getElementById("AlgorithmId").options.length >= MAX_INSTANCES ) {
		alert( document.getElementById("max_alert").value );
		return;
	}
	document.getElementById("cmd").value = "add";
	var form = document.getElementById("frm_opt_nightfc_settings");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function removeAlgorithm()
{
	if( !confirm(document.getElementById("confirm").value) )
		return;
	
	document.getElementById("cmd").value = "del";
	var form = document.getElementById("frm_opt_nightfc_settings");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSelectAlgorithm(id)
{
	document.getElementById("cmd").value = "modify";
	var form = document.getElementById("frm_opt_nightfc_settings");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSunriseSchedule(b)
{
	if( !bLightsConfigured )
		return;
	if(b)
	{
		document.getElementById("TimeOffSelection").style.display = "none";
		document.getElementById("AutoOffIcon").style.display = "inline";
		document.getElementById("SunriseSchedule").value = 1;
	}
	else
	{
		document.getElementById("TimeOffSelection").style.display = "inline";
		document.getElementById("AutoOffIcon").style.display = "none";
		document.getElementById("SunriseSchedule").value = 0;
	}
}
