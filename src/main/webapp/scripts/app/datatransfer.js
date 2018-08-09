var currentDevice = -1;
var selvarscount = 0;
var selectedvariables = new Array(10);
var deviceids;
var devicedesc;
var variablecurrdev;
var vardesccurrdev;
var currentselectedvariables = new Array(1);
var lineselected_var;
var clickState_var; 
var ACTION_SAVE			= 1;
var ACTION_MODIFY		= 2;
var ACTION_ADD			= 3;
var ACTION_REMOVE		= 4;
var ACTION_RESTART		= 5;
var ACTION_PRINT        = 6;

var LOAD_DEVICE_VAR		= 0;
var LOAD_DEVICE			= 1;
var LOAD_DEVICE_MDL		= 2;
var LOAD_VARMDL		= 5;
var RESTART_SERVICE		= 10;

//selected items
var idSelectedMbDev		= 0;
var idSelectedDev		= 0;
var idSelectedDevVar	= 0;
var iSelectedMbVar		= -1;
var strSelectedDev		= "";
var strSelectedDevVar	= "";
var insertMode			= "singleDevice"; // or multipleDevices
var aaDevVarCache		= null; // devices/variables cache 

//aliases
var listRMVar			= null;
var COL_MBVAR_I1		= 1;
var COL_MBVAR_I2		= 2;
var COL_MBVAR_IDVARIABLE = 6;

var rowColor =0;

var requests = 0;
function addRequest() {
	requests++;
}

function deleteThisRow(obj){
	var id = obj.parentNode.parentNode.parentNode.id;
	var len = id.length;
	var rowNum = id.substring(5,id.length);
	deleteRMVar(rowNum-1);
}



function onSelectRMVar(str)
{
	var i = parseInt(str, 10);
	if(	iSelectedMbVar != i )
		iSelectedMbVar = i;
	else
		iSelectedMbVar = -1;

}

function onDeleteRMVar(i)
{
	if( i >= 0 )
		deleteRMVar(i);
	else if( iSelectedMbVar >= 0 )
		deleteRMVar(iSelectedMbVar);
}
function deleteAllRows()
{
	var len = listRMVar.mData.length;
	for(var i=len-1;i>=0;i--){
		listRMVar.mData.splice(i, 1);
	}
	iSelectedMbVar = -1;
	listRMVar.rowSelected = null;
	listRMVar.numRows = listRMVar.mData.length;
	listRMVar.render();
}

function deleteRMVar(i)
{
	listRMVar.mData.splice(i, 1);
	for(; i < listRMVar.mData.length; i++) {
		
		listRMVar.mData[i][COL_MBVAR_I1] = i;
		listRMVar.mData[i][COL_MBVAR_I2] = i;
	}
	iSelectedMbVar = -1;
	listRMVar.rowSelected = null;
	listRMVar.numRows = listRMVar.mData.length;
	listRMVar.render();
}



function onSelectDevVar(i)
{
	var objDevVar = document.getElementById("devvar");
	idSelectedDevVar = objDevVar.options[i].value;
	strSelectedDevVar = objDevVar.options[i].text;
}

function restart_service()
{
	reload_actions(RESTART_SERVICE);
}

function datatransferOnLoad()
{
	enableActionDataTransfer();
	currentDevice = -1;
	
	tablecodeA = "<table class='table' width='100%' id='devvarTab'>"
		+ "<thead><tr><th class='th'>" + document.getElementById("_devices").value + "</th>"
		+ "<th class='th'>"	+ document.getElementById("_variables").value + "</th>"
		+ "<th class='th' width='2%' onclick='delete_all_variables();'"
		+ " style='text-align: center;cursor: pointer;' title='"
		+ document.getElementById("delall").value+"'><img src='images/dbllistbox/delete_on.png' alt='"
		+ document.getElementById("delall").value+"' /></th></tr></thead>";
	tablecodeB = "<tbody></tbody></table>";	
	CommSend("servlet/ajrefresh", "POST", "cmd=loaddevvar", "devvarInit", false);
}
function Callback_devvarInit(src) {
	var xml = src;
	try {
		if (xml.getElementsByTagName("variables").length > 0) {
			document.getElementById("divdevvarTab").innerHTML = tablecodeA
			+ xml.getElementsByTagName("variables")[0].childNodes[0].nodeValue
			+ tablecodeB;
		}
	} catch (err) {
		alert("Callback_devvarInit: "+err.description);
	}
	subtractRequest();
}

/*delitem*/
function deleteItem(obj) {
	var tr=obj.parentNode.parentNode;
	var tab=tr.parentNode.deleteRow(tr.rowIndex-1);
	var table = document.getElementById("devvarTab");
	var trs = table.rows.length;
	for(i=0;i<trs;i++){
		table.rows[i].className = (i%2==0)?"Row1":"Row2";
	}
	setModUser();
}

/* dellall */
function delete_all_variables() {
	var s = document.getElementById('deleteallparamquestion').value;
	
		if(confirm(s))
		{
			document.getElementById("divdevvarTab").innerHTML = tablecodeA+tablecodeB;
			setModUser();
		}
}

function addTableRow(tabl, dv1, dt1, dv2, dt2) {
	rowColor++;
	var row = document.createElement("tr");
	row.className = (rowColor%2==0)?"Row2":"Row1";
	row.id = "" + dv1 + "_" + dv2 + "";

	var cell0 = document.createElement("td");
	cell0.className = "standardTxt";
	cell0.innerHTML = dt1;

	var cell1 = document.createElement("td");
	cell1.className = "standardTxt";
	cell1.innerHTML = dt2;
	var cell2 = document.createElement("td");
	cell2.className = "standardTxt";
	cell2.style.textAlign = "center";
	cell2.style.cursor = "pointer";
	cell2.innerHTML = "<IMG onclick='deleteItem(this);' "+
	"src='images/actions/removesmall_on_black.png'/>";

	row.appendChild(cell0);
	row.appendChild(cell1);
	row.appendChild(cell2);

	tabl.tBodies[0].appendChild(row);
	setModUser();
	
}

function getVariablesFromCache(idDevice)
{
	for(var i = 0; i < aaDevVarCache.length; i++) {
		if( aaDevVarCache[i].idDevice == idDevice )
			return aaDevVarCache[i].aVariables;
	}
	return null;
}

function reload_device(){
	var cmbDevice = document.getElementById('dev');
	var cmdDeviceModel = document.getElementById('model');
	var toSend = "";
	var functionToRecall = "";
	toSend=LOAD_VARMDL+"&iddevice="+cmdDeviceModel.options[cmdDeviceModel.selectedIndex].value;
	functionToRecall="loadVariable";
	if( toSend != "" )
		CommSend("servlet/ajrefresh",
			"POST",
			"action="+toSend,
			functionToRecall, 
			true);
}

function reload_actions(action)
{	
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
			return false;
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
		setTimeout("reload_device(0)",100);

	}
	else if( action == RESTART_SERVICE ) {
		toSend = RESTART_SERVICE;
		functionToRecall = "restartService";
	}
		
	if( toSend != "" )
		CommSend("servlet/ajrefresh",
			"POST",
			"action="+toSend,
			functionToRecall, 
			true);
}

function isVariable(idVariable)
{
	for(var i = 0; i < listRMVar.mData.length; i++)
		if( listRMVar.mData[i][6] == idVariable )
			return true;
	return false;
}

function enableModelSelection()
{
	document.getElementById("model").disabled = false;
	emptySelect("dev");
	emptySelect("devvar");
	idSelectedDevVar = 0;
	astrVarTypes = null;
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

function insertDeviceOnCache()
{
	var obj = new Object();
	obj.idDevice = idSelectedDev;
	obj.strDevice = strSelectedDev;
	obj.aVariables = new Array();
	var objDevVar = document.getElementById("devvar");
	for(var i = 0; i < objDevVar.options.length; i++)
		obj.aVariables[i] = objDevVar.options[i].value;
	aaDevVarCache.push(obj);
}

//cache the rest of selected devices
function retrieveDevicesForCache()
{
	var cmbDevice = document.getElementById('dev');
	for(var i = 0; i < cmbDevice.options.length; i++) {
		if( cmbDevice.options[i].selected && !isDeviceOnCache(cmbDevice.options[i].value) ) {
			idSelectedDev = cmbDevice.options[i].value;
			strSelectedDev = cmbDevice.options[i].text;
	
			toSend = LOAD_DEVICE_VAR + "&iddevice=" + idSelectedDev;
			functionToRecall = "loadVariable";

			CommSend("servlet/ajrefresh", "POST", "action=" + toSend, functionToRecall, true);
			break;
		}
	}
}

function isDeviceOnCache(idDevice)
{
	for(var i = 0; i < aaDevVarCache.length; i++)
		if( aaDevVarCache[i].idDevice == idDevice )
			return true;
	return false;
}

function addDevVar()
{
	var objDevVar = document.getElementById("devvar");
	var bVarExists = false;
	var strVarExists = "";
	
	var data1 = document.getElementById("dev");
	var data2 = document.getElementById("devvar");
	var tab = document.getElementById("devvarTab");
	var isSame = false;

	var nvar = 0;
	var nop = 0;
	var okvar = false;
	var extra = "";
	
	if( insertMode == "singleDevice" ) {
		var initrows = tab.rows.length;
//		var maxvars = document.getElementById("maxvariables").value
		// gestione MULTIVALUE per lista devs:
		for ( var i = 0; i < data1.options.length; i++) {
			if (data1.options[i].selected == true) {
				if (data2.selectedIndex != -1) {
					okvar = true;
					// gestione MULTIVALUE per lista vars:
					for ( var j = 0; j < data2.options.length; j++) {
						if (data2.options[j].selected == true) {
							isSame = false;
							var elem = document.getElementById(data1.options[i].value+ "_" + data2.options[j].value);
							isSame = (elem != null);
							if (isSame == false) {
								addTableRow(tab, data1.options[i].value,
										data1.options[i].text,
										data2.options[j].value,
										data2.options[j].text);
								nvar = nvar + 1;
								initrows = tab.rows.length;
							}
							nop = nop + 1;
						}
					}
				}
			}
		}
		if (nvar > 0) {
			if (document.getElementById("operationend")) {
				var done = document.getElementById("operationend").value;
				if (nvar > 1)
					extra = "\n\n\t" + nvar + " vars";
				//alert(done + extra);
			}
		}
		var trs = tab.rows.length;
		for(i=0;i<trs;i++){
			tab.rows[i].className = (i%2==0)?"Row1":"Row2";
		}
	}
	
	if( insertMode == "multipleDevices" ) {
		//dev * varmdl
		var devret = getAllSelectedIndexes(document.getElementById("dev"));
		var devicesids = "";
		for(var ii=0;ii<devret.length;ii++){
			devicesids+=document.getElementById(devret[ii]).value+"-";
		}//for
		var varret = getAllSelectedIndexes(document.getElementById("devvar"));
		var variablesids = "";
		for(var jj=0;jj<varret.length;jj++){
			variablesids+=document.getElementById(varret[jj]).value+"-";
		}//for
		var trows = document.getElementById("devvarTab").rows;
		var count = trows.length;
		var exclude = "";
		for(var c = 1;c<trows.length;c++){
			var iid = trows[c].id.split("_");
			if(iid.length>1)
				exclude+=iid[1]+"-";
		}
		if(variablesids!=""&&devicesids!=""){
			MTstartServerComm();
			addRequest();
			CommSend("servlet/ajrefresh", "POST", "cmd=addDevVar&devices="+devicesids+"&variables="+variablesids+"&exclude="+exclude,
					"addDevVar", false);
		}
		setModUser();
	}
		
//	if( bVarExists )
//		alert(document.getElementById("alert_var_exists").value + strVarExists);
}

function Callback_addDevVar(xml) {
		if (xml.getElementsByTagName("variables").length > 0) {
			var previousvars;
			try {
				previousvars = document.getElementById("devvarTab").tBodies[0].innerHTML;			
			} catch (err) {
				previousvars = "";
			}
			var newvars;
			try {
				newvars = xml.getElementsByTagName("variables")[0].childNodes[0].nodeValue;
			} catch (err) {
				newvars = "";
			}
			var tablecode = tablecodeA + previousvars + newvars + tablecodeB;
			try{
				MTstopServerComm();
				document.getElementById("divdevvarTab").innerHTML = tablecode;
				//initRows=document.getElementById("devvarTab").rows.length;
				if(xml.getElementsByTagName("added")!=null && xml.getElementsByTagName("added").length>0) {
					var done = document.getElementById("operationend").value;
					var added = "";
					if(xml.getElementsByTagName("added")!=null && xml.getElementsByTagName("added").length>0) {
						added = xml.getElementsByTagName("added")[0].childNodes[0].nodeValue;
					}
					if (added != "")
						extra = "\n\n\t" + added + " vars";
					//if(added!="" && added>0)
						//alert(done+extra);
				}
			} catch(err){alert(err.description);}
		}
		var tab = document.getElementById("devvarTab");
		initRows=tab.rows.length;
		for(i=0;i<initRows;i++){
			tab.rows[i].className = (i%2==0)?"Row1":"Row2";
		}
		subtractRequest();
		
}

function getAllSelectedIndexes(combo){
	var i=0;
	var ret=new Array();
	while(combo.selectedIndex!=-1){
		ret[i++]=combo.options[combo.selectedIndex].id;
		if(combo.multiple){
			combo.options[combo.selectedIndex].selected=false;
		} else {
			combo.selectedIndex=-1;
			break;
		}
	}
	for(var k=0;k<ret.length;k++){
		document.getElementById(ret[k]).selected=true;
	}
	return ret;
}


function enableActionDataTransfer()
{
	enableAction(1);
}

function onDivScroll()
{
}

function submitSendVariables()
{
	if(document.getElementById("maxdimension").value==null || 
			document.getElementById("maxdimension").value==0 || 
			document.getElementById("maxdimension").value=='')
	{
		document.getElementById("maxdimension").value = 50;
	}
	var submitmsg = "";
	submitmsg+="<submitted>";
	
	var trows = document.getElementById("devvarTab").rows;
	var count = trows.length;
	var exclude = "";
	for(var c = 1;c<trows.length;c++){
		var iid = trows[c].id.split("_");
		if(iid.length>1)
			submitmsg+="<id>"+iid[1]+"</id>";
	}
//	for(var i = 0; i < listRMVar.mData.length; i++)
//	{
//		submitmsg+="<id>"+listRMVar.mData[i][6]+"</id>";
//	}
	submitmsg+="<chunksize>"+document.getElementById("maxdimension").value+"</chunksize>";
	submitmsg+="</submitted>";
	maxdim = document.getElementById("maxdimension").value;
	//add new
	/*if(listRMVar.mData.length!=0){
		new CommSend("servlet/ajrefresh","POST","cmd=confirmMaxdimension&msg="+submitmsg+"&maxdim="+document.getElementById("maxdimension").value,"getResult", true);
	}else{*/
		new CommSend("servlet/ajrefresh","POST","cmd=submitVars&msg="+submitmsg+"&maxdim="+document.getElementById("maxdimension").value,"DTEmptyMethod", true);
	/*}*/
}
/*
function Callback_getResult()
{
	rsp = xmlResponse.getElementsByTagName("MSG")[0].childNodes[0].nodeValue;
	if(rsp=="OK" || rsp=="BAD"){
		if(rsp=="OK")
			alert(document.getElementById("serverok").value);
		if(rsp=="BAD")
			alert(document.getElementById("servererr").value);
	}else{
		if(rsp=="ERROR"){
			alert(document.getElementById("servererr").value);
		}else{
			setTimeout("submitSendVariablesConfirm("+rsp+")",500);
		}			
	}
}
function submitSendVariablesConfirm(maxVal)
{
	var maxValue = document.getElementById("maxdimension").value;
	var submitmsg = "";
	submitmsg+="<submitted>";
	for(var i = 0; i < listRMVar.mData.length; i++)
	{
		submitmsg+="<id>"+listRMVar.mData[i][6]+"</id>";
	}
	submitmsg+="<chunksize>"+maxValue+"</chunksize>";
	submitmsg+="</submitted>";
	var serveralert = document.getElementById("serveralert").value;
	serveralert = serveralert.replace("{maxVal}",maxVal);
	if(confirm(serveralert)){
		new CommSend("servlet/ajrefresh","POST","cmd=submitVars&msg="+submitmsg+"&maxdim="+maxValue,"DTEmptyMethod", true);
	}	
}
*/	
function Callback_DTEmptyMethod()
{
	rsp = xmlResponse.getElementsByTagName("MSG")[0].childNodes[0].nodeValue;
	if(rsp=="OK")
		alert(document.getElementById("serverok").value);
	if(rsp=="BAD")
		alert(document.getElementById("servererr").value);
}
	
function Callback_loadDeviceAndVariable(xmlResponse)
{
	document.getElementById("div_dev").innerHTML = xmlResponse.getElementsByTagName("device")[0].childNodes[0].nodeValue;
	idSelectedDevVar = 0;
	astrVarTypes = null;
	
}


function Callback_loadVariable(xmlResponse)
{
	document.getElementById("div_var").innerHTML = xmlResponse.firstChild.firstChild.nodeValue;
	idSelectedDevVar = 0;
	astrVarTypes = document.getElementById("csvVarType").value.split(",");
	
	
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

}


function Callback_restartService()
{
	alert(document.getElementById("alert_mb_restart2").value);
}	
	
	
