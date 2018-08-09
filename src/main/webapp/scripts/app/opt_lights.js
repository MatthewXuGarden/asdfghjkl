// Optimum Lights
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
	alert(document.getElementById("warn_action").value);
	
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
	document.getElementById("cmd").value = "prev15next15";
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


// chart
var tabSelected			= null;


function onSelectTab(tab)
{
	if( tab == tabSelected )
		return;
	
	if( !tabSelected ) {
		tabSelected = document.getElementById("tabChart2");
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
var ACTION_SAVE			= 1;

var LOAD_DEVICE_VAR		= 0;
var LOAD_DEVICE			= 1;
var LOAD_DEVICE_MDL		= 2;

var strSelectVar		= "";
var idSelectedDev		= 0;
var strSelectedDev		= "";
var idSelectedDevVar	= 0;
var strSelectedDevVar	= "";


function saveSettings()
{
	var bLatitude = document.getElementById("Latitude").value != "";
	var bLongitude = document.getElementById("Longitude").value != "";
	var bVarDay = document.getElementById("VarDay").value > 0;
	var bVarNight = document.getElementById("VarNight").value > 0;
	if( !(bLatitude && bLongitude && bVarDay && bVarNight) ) {
		alert(document.getElementById("alert_req_var").value);
		return;
	}
	var form = document.getElementById("frm_opt_settings");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onLoadSettings()
{
	enableAction(1);
	checkButtons();
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
	document.getElementById("btnSetVar").disabled = idSelectedDevVar == 0;
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
