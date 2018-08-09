// actions
var ACTION_ADD			= 1;
var ACTION_MOD			= 2;
var ACTION_DEL			= 3;
var ACTION_SAVE			= 4;

var idModelSelected		= 0;
var strSelectVar		= "";
var idDevMdlSelected	= 0;
var strDevMdlSelected	= "";
var idVarMdlSelected	= 0;
var strVarMdlSelected	= "";

// aliases
var listDevMdl			= null;
var COL_DEVMDL_ID		= 1;
var COL_DEVMDL_NAME		= 3;
var listVarMdl			= null;
var COL_VARMDL_ID		= 1;
var COL_VARMDL_CODE		= 3;
var COL_VARMDL_NAME		= 4;
var COL_VARMDL_TYPE		= 5;
var COL_VARMDL_TYPENO	= 6;
//var types
var TYPE_DIGITAL		= "1";	
var TYPE_ANALOGIC		= "2";	
var TYPE_INTEGER		= "3";
var TYPE_ALARM			= "4";


function onLoadModels()
{
	// set aliases
	listModels = Lsw1;
	listDevMdl = Lsw3;
	listVarMdl = Lsw4;
	
	disableAllAction();
	enableAction(ACTION_ADD);
	if( document.getElementById("idModel").value > 0 )
		enableAction(ACTION_SAVE);
	checkButtons();
}


function addModel()
{
	idModelSelected = 0;
	document.getElementById("idModel").value = "0";
	document.getElementById("name").value = "";
	clearVar("idDevMdl");
	clearVar("idVarMdlKw");
	clearVar("idVarMdlKwh");
	document.getElementById("divModel").style.display = "block";
	disableAction(ACTION_ADD);
	enableAction(ACTION_SAVE);
}


function modifyModel()
{
	document.getElementById("cmd").value = "modify";
	document.getElementById("idModel").value = idModelSelected;
	var form = document.getElementById("frm_model");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function removeModel()
{
	if( !confirm(document.getElementById("confirm").value) )
		return;
	
	document.getElementById("cmd").value = "remove";
	document.getElementById("idModel").value = idModelSelected;
	var form = document.getElementById("frm_model");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function saveModel()
{
	var bSubmit = document.getElementById("name").value != ""
		&& document.getElementById("idDevMdl").value > 0
		&& document.getElementById("idVarMdlKw").value > 0
		&& document.getElementById("idVarMdlKwh").value > 0;
	if( !bSubmit ) {
		alert(document.getElementById("alert_req_var").value);
		return;
	}
	
	document.getElementById("cmd").value = "save";
	var form = document.getElementById("frm_model");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function onSelectModel(id)
{
	if( id != idModelSelected )
		idModelSelected = id;
	else
		idModelSelected = 0;
	checkButtons();
}


function onModifyModel(id)
{
	idModelSelected = id;
	modifyModel();
}


function selectVar(name)
{
	if( name == strSelectVar )
		return;
	
	if( strSelectVar != "" )
		document.getElementById("btn_" + strSelectVar).disabled = false;
	strSelectVar = name;
	document.getElementById("btn_" + strSelectVar).disabled = true;
	document.getElementById("divVarSelection").style.display = "block";
}


function clearVar(name)
{
	document.getElementById(name).value = 0;
	document.getElementById("span_" + name).innerHTML = "------";
}


function onSelCancel()
{
	document.getElementById("btn_" + strSelectVar).disabled = false;	
	strSelectVar = "";
	document.getElementById("divVarSelection").style.display = "none";
}


function setVar()
{	
	if( strSelectVar != "" && idVarMdlSelected > 0 ) {
		document.getElementById(strSelectVar).value = idVarMdlSelected;
		document.getElementById("span_" + strSelectVar).innerHTML = strVarMdlSelected;
		document.getElementById("btn_" + strSelectVar).disabled = false;

		if( document.getElementById("idDevMdl").value == "0" ) {
			if( document.getElementById("name").value == "" )
				document.getElementById("name").value = strDevMdlSelected;
			document.getElementById("idDevMdl").value = idDevMdlSelected;
			document.getElementById("span_idDevMdl").innerHTML = strDevMdlSelected;
		} else if( document.getElementById("idDevMdl").value != idDevMdlSelected ) {
			document.getElementById("name").value = strDevMdlSelected;
			document.getElementById("idDevMdl").value = idDevMdlSelected;
			document.getElementById("span_idDevMdl").innerHTML = strDevMdlSelected;
			clearVar(strSelectVar == "idVarMdlKw" ? "idVarMdlKwh" : "idVarMdlKw");
		}
		
		strSelectVar = "";
	}
	document.getElementById("divVarSelection").style.display = "none";
	checkButtons();
}


function onSelectDevModel(id)
{
	idDevMdlSelected = id;
	idVarMdlSelected = 0;
	CommSend("servlet/ajrefresh", "POST", "action=var&id="+id,	"onVarModels", true);
}


function checkButtons()
{
	if( idModelSelected > 0 ) {
		enableAction(ACTION_MOD);
		enableAction(ACTION_DEL);
	}
	else {
		disableAction(ACTION_MOD);
		disableAction(ACTION_DEL);
	}
	document.getElementById("btnSetVar").disabled = idVarMdlSelected == 0;
}


function Callback_onVarModels(response)
{
	var xmlResponse = response.getElementsByTagName("response")[0];
	strDevMdlSelected = xmlResponse.getAttribute("name");
	var aXmlVarMdls = response.getElementsByTagName("varmdl");
	var aValue = new Array();
	for(var i = 0; i < aXmlVarMdls.length; i++) {
		var aData = new Array("",
			aXmlVarMdls[i].getAttribute("id"),
			aXmlVarMdls[i].getAttribute("id"),
			aXmlVarMdls[i].getAttribute("code"),
			aXmlVarMdls[i].getAttribute("name"),
			document.getElementById("type" + aXmlVarMdls[i].getAttribute("type")).value,
			// hidden column
			aXmlVarMdls[i].getAttribute("type")
		);
		aValue[i] = aData;
	}
	listVarMdl.mData = aValue;
	listVarMdl.numRows = aValue.length;
	listVarMdl.render();
	checkButtons();
}


function onSelectVarModel(id)
{
	if( id != idVarMdlSelected ) {
		idVarMdlSelected = id;
		for(var i = 0; i < listVarMdl.mData.length; i++) {
			if( listVarMdl.mData[i][COL_VARMDL_ID] == idVarMdlSelected ) {
				strVarMdlSelected = listVarMdl.mData[i][COL_VARMDL_NAME];
				break;
			}
		}
	}
	else
		idVarMdlSelected = 0;
	checkButtons();
}


function onAddCatMap(id)
{
	idVarMdlSelected = id;
	setVar();
}
