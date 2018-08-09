// parameters priorities
var ACTION_SAVE			= 1;
var idDevMdl			= 0;
var idVarGroup			= 0;
var actbDeviceSearch	= null;
// aliases
var aaPriorities		= null;
var COL_IDVARMDL		= 1;


function onLoadParameters()
{
	idDevMdl = parseInt(document.getElementById("idDevMdl").value, 10);
	idVarGroup = parseInt(document.getElementById("idVarGroup").value, 10);
	if( idDevMdl > 0 ) {
		aaPriorities = Lsw1.mData;
		enableAction(ACTION_SAVE);
	}
	unlockModUser();
	// smart input box
	var device_list = document.getElementById("idDevMdl");
	var device_search = document.getElementById("device_search"); 
	if( device_search ) {
		device_search.setAttribute("autocomplete", "off"); // disable IE auto complete
		device_search.style.width = device_list.offsetWidth - WIDTH_CORRECTION;
		var device_patterns = new Array();
		for(var i = 1; i < device_list.options.length; i++)
			device_patterns[i-1] = device_list.options[i].text;
		var topheight = top.frames["body"].frames["TabMenu"].document.body.clientHeight-2;
		actbDeviceSearch = actb(document.getElementById('device_search'), device_patterns, onDevMdlFound, topheight);
	}
	device_search.value = device_list.options[device_list.selectedIndex].text;
}


function onSelectDevMdl(id, idVarGroup)
{
	document.getElementById("cmd").value = "select";
	document.getElementById("idVarGroup").value = idVarGroup;
	var form = document.getElementById("frm_var_pri");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onDevMdlFound(deviceName)
{
	var bFound = false;
	var obj = document.getElementById("idDevMdl");
	for(var i = 1; i < obj.options.length; i++) {
		if( obj.options[i].text == deviceName ) {
			obj.selectedIndex = i;
			bFound = true;
			break;
		}
	}
	if( bFound )
		onSelectDevMdl(document.getElementById("idDevMdl").value, 0);
	else
		obj.selectedIndex = 0;
}


function loadParams(id)
{
	if( getModUser() && !MioAskModUser() )
		return;
	onSelectDevMdl(idDevMdl, id);
}


function save_priorities()
{
	var listVar = ""; // csv idVarMdl
	var listPri = ""; // csv nPriority
	for(var i = 0; i < aaPriorities.length; i++) {
		var idVarMdl = aaPriorities[i][COL_IDVARMDL];
		var nPriority = document.getElementById("pri" + idVarMdl).value;
		if( listVar != "" ) {
			listVar += ",";
			listPri += ",";
		}
		listVar += idVarMdl;
		listPri += nPriority;
	}
	
	document.getElementById("cmd").value = "save";
	document.getElementById("listVar").value = listVar;
	document.getElementById("listPri").value = listPri;
	var form = document.getElementById("frm_var_pri");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSelectPriority(nPriority)
{
	if( !confirm(document.getElementById("confirm_all_pri").value) )
		return;
	
	for(var i = 0; i < aaPriorities.length; i++) {
		var idVarMdl = aaPriorities[i][COL_IDVARMDL];
		document.getElementById("pri" + idVarMdl).value = nPriority;
	}
}
