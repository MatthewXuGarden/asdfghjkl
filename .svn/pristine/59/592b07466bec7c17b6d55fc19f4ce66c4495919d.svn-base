// co2saving.js
var ACTION_QUICK_RUNNING = 1;
var ACTION_SAVE	= 1;
var ACTION_MOD	= 2;
var ACTION_ADD	= 3;
var ACTION_DEL	= 4;


// dashboard
var REFRESH_INTERVAL	= 30 * 1000; // 30 seconds


function onLoadDashboard()
{
	if(document.getElementById("running") != null)
	{
		if( document.getElementById("running").value == "true" )
			enableAction(ACTION_QUICK_RUNNING);
		setTimeout("refresh()", REFRESH_INTERVAL);
	}
}


function quickRunning()
{
	onPluginCommand("quick_run");
}


function refresh()
{
	if(document.getElementById("running").value == "false")
		return;
	new AjaxRequest("servlet/ajrefresh", "POST", "action=refresh", Callback_refresh, false);
	return true;
}


function Callback_refresh(xml)
{
	var xmlResponse = xml.firstChild;
	var axmlRacks = xmlResponse.getElementsByTagName("rack");
	for(var i = 0; i < axmlRacks.length; i++) {
		var xmlRack = axmlRacks[i];
		var id = xmlRack.getAttribute("id");
		var st = xmlRack.getAttribute("st");
		setStatus("rack_" + id, st);
		var axmlGroups = xmlRack.childNodes;
		for(var j = 0; j < axmlGroups.length; j++) {
			var xmlGroup = axmlGroups[j];
			var id = xmlGroup.getAttribute("id");
			var st = xmlGroup.getAttribute("st");
			setStatus("group_" + id, st);
		}
	}
	
	setTimeout("refresh()", REFRESH_INTERVAL);
}


function setStatus(id, st)
{
	var img = document.getElementById(id);
	if( img ) {
		img.src = "images/led/L" + st + ".gif";
	}
	else {
		alert("ERROR: Element " + id + " not found");
	}
}


function onPluginCommand(cmd)
{
	document.getElementById("cmd").value = cmd;
	var form = document.getElementById("formPlugin");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


// configuration

function onLoadConfiguration()
{
	enableAction(ACTION_SAVE);
}


function saveConfiguration()
{
	var form = document.getElementById("formConfiguration");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSelectVar2(i, value)
{
	if( value ) {
		var status2Off = document.getElementById("status2Off" + i);
		if( status2Off.value == "" )
			status2Off.value = "0";
		var status2On = document.getElementById("status2On" + i);
		if( status2On.value == "" )
			status2On.value = "0";
	}
	else {
		var status2Off = document.getElementById("status2Off" + i);
		status2Off.value = "";
		var status2On = document.getElementById("status2On" + i);
		status2On.value = "";
	}
}


// backup configuration
var idBackupSelected	= 0;


function onLoadBackup()
{
	if( document.getElementById("formBackup") ) {
		if( document.getElementById("idRack").value != 0 )
			enableAction(ACTION_SAVE);
	}	
}


function saveBackup()
{
	document.getElementById("cmd").value = "save";
	var form = document.getElementById("formBackup");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}	
}


function modifyBackup()
{
	document.getElementById("cmd").value = "modify";
	document.getElementById("idRack").value = idBackupSelected;
	var form = document.getElementById("formBackup");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSelectBackup(id)
{
	if( id != idBackupSelected ) {
		idBackupSelected = id;
		enableAction(ACTION_MOD);
	}
	else {
		idBackupSelected = 0;
		disableAction(ACTION_MOD);
	}
}


function onModifyBackup(id)
{
	idBackupSelected = id;
	modifyBackup();
}


function onSelectBackupDevice(id)
{

	if( id != "0" ) {
		var offline = document.getElementById("offline");
		offline.checked = true;
		CommSend("servlet/ajrefresh", "POST", "action=" + id, "loadVariables", true);
	}
	else {
		var variable = document.getElementById("variable");
		variable.options.length = 1;
	}
}


function Callback_loadVariables(xmlResponse)
{
	var variable = document.getElementById("variable");
	variable.options.length = 1;

	var axmlVars = xmlResponse.getElementsByTagName("var");
	for(var i = 0; i < axmlVars.length; i++) {
		var xmlVar = axmlVars[i];
		var option = document.createElement('option');
		option.value = xmlVar.getAttribute("id");
		option.text = xmlVar.getAttribute("desc");
		variable.add(option);
	}
}


// association
var idRackSelected		= 0;
//aliases
var listRacks			= null;
var listAllGroups		= null;
var listRackGroups		= null;


function onLoadRackGroups()
{
	if( document.getElementById("formRack") ) {
		// set aliases
		listRacks = Lsw1;
		listAllGroups = listbox1;
		listRackGroups = listbox2;
		
		if( document.getElementById("idRack").value != 0 )
			enableAction(ACTION_SAVE);
	}
}


function saveRackGroups()
{
	document.getElementById("cmd").value = "save";
	document.getElementById("groups").value = listRackGroups2String(); 
	var form = document.getElementById("formRack");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}	
}


function modifyRack()
{
	document.getElementById("cmd").value = "modify";
	document.getElementById("idRack").value = idRackSelected;
	var form = document.getElementById("formRack");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSelectRack(id)
{
	if( id != idRackSelected ) {
		idRackSelected = id;
		enableAction(ACTION_MOD);
	}
	else {
		idRackSelected = 0;
		disableAction(ACTION_MOD);
	}
}


function onModifyRack(id)
{
	idRackSelected = id;
	modifyRack();
}


function listRackGroups2String()
{
	var groups = "";
	for(var i = 0; i < listRackGroups.length; i++) {
		if( i > 0 )
			groups += ";";
		groups += listRackGroups.options[i].value;
	}
	return groups;
}


// groups
var idGroupSelected		= 0;
var idGroupSelectedRack	= 0;
//aliases
var listGroups			= null;
var listAllDevs			= null;
var listGroupDevs		= null;


function onLoadGroups()
{
	if( document.getElementById("formGroup") ) {
		// set aliases
		listGroups = Lsw1;
		listAllDevs = listbox1;
		listGroupDevs = listbox2;
		
		if( document.getElementById("idGroup").value != 0 )
			enableAction(ACTION_SAVE);
		else
			enableAction(ACTION_ADD);
	}
}


function saveGroup()
{
	var bSubmit = document.getElementById("name").value != "";
	if( !bSubmit ) {
		alert(document.getElementById("alert_req_var").value);
		return;
	}
	var utilities = listGroupDevs2String();
	if( utilities == "" ) {
		alert(document.getElementById("alert_group_empty").value);
		return;
	}
	
	document.getElementById("utilities").value = utilities; 
	document.getElementById("cmd").value = "save";
	var form = document.getElementById("formGroup");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}	
}


function modifyGroup()
{
	document.getElementById("cmd").value = "modify";
	document.getElementById("idGroup").value = idGroupSelected;
	var form = document.getElementById("formGroup");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function addGroup()
{
	var bSubmit = document.getElementById("name").value != "";
	if( !bSubmit ) {
		alert(document.getElementById("alert_req_var").value);
		return;
	}
	var utilities = listGroupDevs2String();
	if( utilities == "" ) {
		alert(document.getElementById("alert_group_empty").value);
		return;
	}
	
	document.getElementById("utilities").value = utilities; 
	document.getElementById("cmd").value = "add";
	var form = document.getElementById("formGroup");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function removeGroup()
{
	if( !confirm(document.getElementById("confirm").value) )
		return;

	document.getElementById("cmd").value = "remove";
	document.getElementById("idGroup").value = idGroupSelected;
	var form = document.getElementById("formGroup");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSelectGroup(id)
{
	var aids = id.split(",");
	var idGroup = aids[0];
	if( idGroup != idGroupSelected ) {
		idGroupSelected = idGroup;
		idGroupSelectedRack = aids[1];
	}
	else {
		idGroupSelected = 0;
		idGroupSelectedRack = 0;
	}
	checkGroupButtons();
}


function onModifyGroup(id)
{
	var aids = id.split(",");
	idGroupSelected = aids[0];
	idGroupSelectedRack = aids[1];
	modifyGroup();
}


function listGroupDevs2String()
{
	var devs = "";
	for(var i = 0; i < listGroupDevs.length; i++) {
		if( i > 0 )
			devs += ";";
		devs += listGroupDevs.options[i].value;
	}
	return devs;
}


function checkGroupButtons()
{
	if( idGroupSelected != 0 )
		enableAction(ACTION_MOD);
	else
		disableAction(ACTION_MOD);
	if( idGroupSelected > 0 && idGroupSelectedRack == 0 )
		enableAction(ACTION_DEL);
	else
		disableAction(ACTION_DEL);
}


// racks
function onLoadRacks()
{
	if( document.getElementById("formRacks") )
		enableAction(ACTION_SAVE);
}


function saveRacks()
{
	if( !confirm(document.getElementById("confirm").value) )
		return;
	
	var form = document.getElementById("formRacks");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}
