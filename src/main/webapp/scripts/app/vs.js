// VisualScheduler
var ACTION_SAVE		= 1;
var ACTION_SET		= 2;
var ACTION_MODIFY	= 2;
var ACTION_ADD		= 3;
var ACTION_REMOVE	= 4;
var ACTION_IMPORT	= 5;
var ACTION_EXPORT	= 6;
var SYMBOL_PATH		= "images/scheduler/symbols/";
var NO_SYMBOL		= "images/scheduler/skin/icon_no_symbol.png";
var ADD_ON			= "images/actions/add_on_black.png";
var ADD_OFF			= "images/actions/add_off.png";
var REMOVE_ON		= "images/actions/remove_on_black.png";
var REMOVE_OFF		= "images/actions/remove_off.png";
var nVarThreshold	= 0;


// scheduler ////////////////////////////////////////////////////////////////////////////////////////////////
var obj = null; // flash object; initialized by VisualSchedulerInit 
var timestamp = null; // used to compute load/save/set elapsed time


function save_schedule()
{
	if( obj )
		obj.SaveData();
}


function set_variables()
{
	if( obj )
		obj.SendCommand();
}


function VisualSchedulerWait(bWait)
{
	var objDate = new Date();
	if( bWait ) {
		timestamp = objDate;
		MTstartServerComm();
	}
	else if( timestamp != null && objDate.valueOf() - timestamp.valueOf() > 1000 ) {
		timestamp = null;
		MTstopServerComm();
	}
	else {
		setTimeout("MTstopServerComm()", 1000);
	}
}


// virtual keyboard /////////////////////////////////////////////////////////////////////////////////////////
var htmlTextBoxes = "";


function TextBox(strName, strValue, x, y, nWidth, nHeight)
{
	var layerVK = document.getElementById("layerVK");
	if( layerVK ) {
		var htmlTextBox = "<input id='" + strName + "' name='" + strName + "' type='text' value='" + strValue + "' class='keyboardInput'" 
		+ " style='position:absolute;left:" + x + "px;top:" + y + "px;width:" + nWidth + "px;height:" + nHeight + "px;'>";
		htmlTextBoxes += htmlTextBox; 
	}
}


function RemoveTextBoxes()
{
	var layerVK = document.getElementById("layerVK");
	if( layerVK ) {
		layerVK.innerHTML = "";
		buildKeyboardInputs();
	}
}


function RequestTextBox(strName)
{
	var textBox = document.getElementById(strName);
	if( textBox )
		obj.SetTextBox(strName, textBox.value);
}


function ShowTextBox(strName, bShow)
{
	var textBox = document.getElementById(strName);
	if( textBox )
		textBox.style.visibility = bShow ? "visible"  : "hidden";
}


function BuildKeyboardInputs()
{
	var layerVK = document.getElementById("layerVK");
	if( layerVK ) {
		layerVK.innerHTML = htmlTextBoxes;
		buildKeyboardInputs();
		htmlTextBoxes = "";
	}
}


function HideVirtualKeyboard()
{
	buildKeyboardInputs();
}


// groups ///////////////////////////////////////////////////////////////////////////////////////////////////

var idGroupSelected 	= 0;
var idDevSelected		= 0;
var idVarSelected		= 0;
var idCmdVar			= 0;
var nameDevSelected		= "";
var nameVarSelected		= "";
var bCmdVar				= false;
var objDeviceVars		= null;
// aliases
var MAX_GROUP_NO		= 10;
var listGroups			= null;
var listAllDevs			= null;
var listGroupDevs		= null;
var listDevs			= null;
var listVars			= null;
var COL_GROUPS_ID		= 1;
var COL_GROUPS_CATNAME	= 5;
var COL_GROUPS_VARSNO	= 6;
var COL_DEVS_NAME		= 3;
var COL_DEVS_ID			= 4;
var COL_VARS_NAME		= 4;
var COL_VARS_ID			= 6;


function add_group()
{
	if(	document.getElementById("name").value == ""
		|| document.getElementById("category").value == "" ) {
		alert(document.getElementById("reqfields").value);
		return;
	}

	if( getGroupNo(document.getElementById("category").value) >= MAX_GROUP_NO) {
		alert(document.getElementById("checkgroup").value);
		return;
	}
	
	if( listGroupDevs.length <= 0 ) {
		alert(document.getElementById("group_empty").value);
		return;		
	}
	else if( getGroupsVariablesNo() > nVarThreshold ) {
		alert(document.getElementById("alert_var_threshold").value);
		return;
	}
	
	document.getElementById("cmd").value = "add";
	document.getElementById("devs").value = listGroupDevs2Devs();
	var form = document.getElementById("frm_set_group");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function modify_group()
{
	if( idGroupSelected != 0 )	
		CommSend("servlet/ajrefresh", "POST", "action=group&id="+idGroupSelected, "onGroup", true);
}


function remove_group()
{
	if( idGroupSelected == 0 )
		return;
	
	if( !confirm(document.getElementById("delgroup_conf").value) )
		return;
	
	document.getElementById("cmd").value = "remove";
	document.getElementById("id").value = idGroupSelected;
	var form = document.getElementById("frm_set_group");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function save_group()
{
	if(	document.getElementById("name").value == ""
		|| document.getElementById("category").value == "" ) {
		alert(document.getElementById("reqfields").value);
		return;
	}
	
	if( listGroupDevs.length <= 0 ) {
		alert(document.getElementById("group_empty").value);
		return;		
	}
	else if( getGroupsVariablesNo() > nVarThreshold ) {
		alert(document.getElementById("alert_var_threshold").value);
		return;
	}
	
	document.getElementById("cmd").value = "save";
	document.getElementById("devs").value = listGroupDevs2Devs();
	var form = document.getElementById("frm_set_group");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function setCatSymbol(id)
{
	var img = document["imgSymbol"]; 
	if( id > 0 ) {
		var fileName = document.getElementById("cat" + id).value;
		img.src = SYMBOL_PATH + fileName;
	}
	else {
		img.src = NO_SYMBOL;
	}
}


function listGroupDevs2Devs()
{
	var devs = "";
	for(var i = 0; i < listGroupDevs.length; i++) {
		if( i > 0 )
			devs += ",";
		devs += listGroupDevs.options[i].value;
	}
	return devs;
}


function getCatId(name)
{
	var category = document.getElementById("category");
	for(var i = 1; i < category.options.length; i++) {
		if( category.options[i].text == name )
			return category.options[i].value;
	}
	return -1;
}


function getGroupNo(idCat)
{
	var n = 0;
	for(var i = 0; i < listGroups.mData.length; i++)
		if( getCatId(listGroups.mData[i][COL_GROUPS_CATNAME]) == idCat )
			n++;
	return n;
}


function getGroupsVariablesNo()
{
	var nCount = 0;
	var idGroupEdited = document.getElementById("id").value;
	
	for(var i = 0; i < listGroups.mData.length; i++)
		if( listGroups.mData[i][COL_GROUPS_ID] != idGroupEdited )
			nCount += parseInt(listGroups.mData[i][COL_GROUPS_VARSNO], 10);
	
	for(var i = 0; i < listGroupDevs.length; i++)
		nCount += objDeviceVars[listGroupDevs.options[i].value];
	
	return nCount;
}


function addCmdVar()
{
	if( idDevSelected <= 0 || idVarSelected <= 0 )
		return;
	
	idCmdVar = idVarSelected;
	document.getElementById("cmd_var").value = idCmdVar;
	document.getElementById("cmd_reverse_logic").value = "false";
	document.getElementById("name_cmd_variable").value = nameDevSelected + " - " + nameVarSelected; 
	document.getElementById("cb_reverse_logic").checked = false;
	document.getElementById("cb_reverse_logic").disabled = false;
	checkGroupButtons();
	setModUser();
}


function removeCmdVar()
{
	if( idCmdVar <= 0 )
		return;

	idCmdVar = 0;
	document.getElementById("cmd_var").value = idCmdVar;
	document.getElementById("name_cmd_variable").value = ""; 
	document.getElementById("cb_reverse_logic").disabled = true;
	checkGroupButtons();
	setModUser();
}


function checkGroupButtons()
{
	var img = document["imgAddCmdVar"]; 
	if( img != null )
		img.src = idDevSelected > 0 && idVarSelected > 0 ? ADD_ON : ADD_OFF;

	var img = document["imgRemoveCmdVar"]; 
	if( img != null )
		img.src = idCmdVar > 0 ? REMOVE_ON : REMOVE_OFF;
}


function onGroupsLoad()
{
	bCmdVar = document.getElementById("vscommandvar").value == "true";
	nVarThreshold = parseInt(document.getElementById("vs_var_threshold").value, 10);
	// set aliases
	listGroups = Lsw1;
	listAllDevs = listbox1;
	listGroupDevs = listbox2;
	if( bCmdVar ) {
		listDevs = Lsw2;
		listVars = Lsw3;
	}
	enableAction(ACTION_ADD);
}


function onSelectGroup(id)
{
	idGroupSelected = id;
	enableAction(ACTION_MODIFY);
	enableAction(ACTION_REMOVE);
}


function onModifyGroup(id)
{
	idGroupSelected = id;
	modify_group();
}


function onCategoryChanged(id)
{
	if( listGroupDevs.length > 0 && !confirm(document.getElementById("checkdevs").value) ) {
		if( idCatSelected > 0 ) 
			document.getElementById("category").value = idCatSelected;
		else
			document.getElementById("category").selectedIndex = 0;
		return;
	}
	
	idCatSelected = id;
	setCatSymbol(id);

	listGroupDevs.length = 0;
	if( idCatSelected > 0 )
		CommSend("servlet/ajrefresh", "POST", "action=dev&id=" + idCatSelected, "onGroupDevs", true);
	else
		listAllDevs.length = 0;
}


function onSelectDevice(i)
{
	idDevSelected = listDevs.mData[i][COL_DEVS_ID];
	nameDevSelected = listDevs.mData[i][COL_DEVS_NAME];
	if( idDevSelected > 0 )
		CommSend("servlet/ajrefresh", "POST", "action=var&id=" + idDevSelected, "onDeviceVars", true);
	checkGroupButtons();
}


function onSelectVariable(i)
{
	idVarSelected = listVars.mData[i][COL_VARS_ID];
	nameVarSelected = listVars.mData[i][COL_VARS_NAME];
	checkGroupButtons();	
}


function onAddVariable(i)
{
	onSelectVariable(i);
	addCmdVar();
}


function onClickRLogic(value)
{
	document.getElementById("cmd_reverse_logic").value = value;
}


function Callback_onGroupDevs(response)
{
	objDeviceVars = new Object();
	listAllDevs.length = 0;
	var aXmlDevs = response.getElementsByTagName("dev");
	for(var i = 0; i < aXmlDevs.length; i++) {
		listAllDevs.options.add(new Option(aXmlDevs[i].getAttribute("name"),aXmlDevs[i].getAttribute("id")));
		objDeviceVars[aXmlDevs[i].getAttribute("id")] = parseInt(aXmlDevs[i].getAttribute("var_no"), 10);
	}
	for(i=0;i<listAllDevs.options.length;i++){
		listAllDevs.options[i].className = i%2==0?"Rwo1":"Row2";
	}
	
}


function Callback_onGroup(response)
{
	objDeviceVars = new Object();
	var xmlGroup = response.getElementsByTagName("group")[0];
	document.getElementById("id").value = xmlGroup.getAttribute("id");
	document.getElementById("name").value = xmlGroup.getAttribute("name");
	var idCatSelected = xmlGroup.getAttribute("idcat");
	document.getElementById("category").value = idCatSelected;
	setCatSymbol(idCatSelected);
	var aXmlDevs = response.getElementsByTagName("groupdev");
	listGroupDevs.length = 0;
	for(var i = 0; i < aXmlDevs.length; i++) {
		listGroupDevs.options.add(new Option(aXmlDevs[i].getAttribute("name"),aXmlDevs[i].getAttribute("id")));
		objDeviceVars[aXmlDevs[i].getAttribute("id")] = parseInt(aXmlDevs[i].getAttribute("var_no"), 10);
	}

	if( bCmdVar ) {
		var aXmlCmds = response.getElementsByTagName("cmdvar");
		if( aXmlCmds.length > 0 ) {
			idCmdVar = aXmlCmds[0].getAttribute("id");
			document.getElementById("cmd_var").value = idCmdVar;
			document.getElementById("name_cmd_variable").value = aXmlCmds[0].getAttribute("name");
			document.getElementById("cmd_reverse_logic").value = aXmlCmds[0].getAttribute("id");
			document.getElementById("cb_reverse_logic").checked = aXmlCmds[0].getAttribute("reverse_logic") == "true";
			document.getElementById("cb_reverse_logic").disabled = false;
		}
		else {
			idCmdVar = 0;
			document.getElementById("cmd_var").value = idCmdVar;
			document.getElementById("name_cmd_variable").value = "";
			document.getElementById("cb_reverse_logic").disabled = true;
		}
		checkGroupButtons();
	}
	
	aXmlDevs = response.getElementsByTagName("dev");
	listAllDevs.length = 0;
	for(var i = 0; i < aXmlDevs.length; i++) {
		var element = document.createElement('option');
		element.value = aXmlDevs[i].getAttribute("id");
		element.text = aXmlDevs[i].getAttribute("name");
		if( !containsElement(listGroupDevs, element) ) {
			listAllDevs.options.add(new Option(aXmlDevs[i].getAttribute("name"),aXmlDevs[i].getAttribute("id")));
			objDeviceVars[aXmlDevs[i].getAttribute("id")] = parseInt(aXmlDevs[i].getAttribute("var_no"), 10);
		}
	}
	disableAction(ACTION_ADD);
	enableAction(ACTION_SAVE);
}


function Callback_onDeviceVars(response)
{
	var aXmlVars = response.getElementsByTagName("var");
	var aValue = new Array();
	for(var i = 0; i < aXmlVars.length; i++) {
		var aData = new Array("", i, i,
			aXmlVars[i].getAttribute("code"),
			aXmlVars[i].getAttribute("name"),
			document.getElementById("type" + aXmlVars[i].getAttribute("type")).value,
			// hidden column
			aXmlVars[i].getAttribute("id"),
			aXmlVars[i].getAttribute("type")
		);
		aValue[i] = aData;
	}
	listVars.mData = aValue;
	listVars.numRows = aValue.length;
	listVars.render();
	
	idVarSelected = 0;
	checkGroupButtons();
}


// categories ///////////////////////////////////////////////////////////////////////////////////////////////

var idCatSelected		= 0;
var idDevMdlSelected	= 0;
var idVarMdlSelected	= 0;
var iCatMapSelected		= -1;
var aDevMdls			= new Array();	// device models assigned to edited category
var nCatVars			= 0;			// number of variables for categories (other than edited category)
// aliases
var MAX_CAT_NO			= 6;
var listCat				= null;
var COL_CAT_ID			= 1;
var COL_CAT_VARSNO		= 6;
var listDevMdl			= null;
var COL_DEVMDL_ID		= 1;
var COL_DEVMDL_NAME		= 3;
var listVarMdl			= null;
var COL_VARMDL_ID		= 1;
var COL_VARMDL_CODE		= 3;
var COL_VARMDL_NAME		= 4;
var COL_VARMDL_TYPE		= 5;
var COL_VARMDL_TYPENO	= 6;
var listCatMap = null;
var COL_CATMAP_I1		= 1;
var COL_CATMAP_I2		= 2;
var COL_CATMAP_RLOGICCB	= 6;
var COL_CATMAP_ARESETCB	= 7;
var COL_CATMAP_IDDEVMDL	= 8;
var COL_CATMAP_IDVARMDL	= 9;
var COL_CATMAP_TYPENO	= 10;
var COL_CATMAP_RLOGICV	= 11;
var COL_CATMAP_ARESETV	= 12;
// var types
var TYPE_DIGITAL		= "1";	
var TYPE_ANALOGIC		= "2";	
var TYPE_INTEGER		= "3";
var TYPE_ALARM			= "4";
// imp/exp extension
var ext					= "pcal";
var cat_export_callback	= null;


function add_category()
{
	if( listCat.mData.length >= MAX_CAT_NO) {
		alert(document.getElementById("checkcat").value);
		return;
	}
		
	if(	document.getElementById("name").value == ""
		|| document.getElementById("symbol").value == "" ) {
		alert(document.getElementById("reqfields").value);
		return;
	}
	
	if( listCatMap.mData.length <= 0 ) {
		alert(document.getElementById("map_empty").value);
		return;		
	}
	
	document.getElementById("cmd").value = "add";
	document.getElementById("map").value = listCatMap2Map();
	var form = document.getElementById("frm_set_category");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function modify_category()
{
	if( idCatSelected != 0 )	
		CommSend("servlet/ajrefresh", "POST", "action=map&id="+idCatSelected, "onCategoryMap", true);
}


function remove_category()
{
	if( idCatSelected == 0 )
		return;
	
	if( !confirm(document.getElementById("delcat_conf").value) )
		return;
	
	document.getElementById("cmd").value = "remove";
	document.getElementById("id").value = idCatSelected;
	var form = document.getElementById("frm_set_category");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function save_category()
{
	if(	document.getElementById("name").value == ""
		|| document.getElementById("symbol").value == "" ) {
		alert(document.getElementById("reqfields").value);
		return;
	}
	
	if( listCatMap.mData.length <= 0 ) {
		alert(document.getElementById("map_empty").value);
		return;		
	}
	
	document.getElementById("cmd").value = "save";
	document.getElementById("map").value = listCatMap2Map();
	var form = document.getElementById("frm_set_category");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


// called from file dialog
function import_category(fdLocal, path, fileName)
{
	if( fdLocal ) {
		if( confirm(document.getElementById("warning").value) ) {
			document.getElementById("cmd").value = "import";
			var form = document.getElementById("frm_set_category");
			if( form != null ) {
				MTstartServerComm();
				form.submit();
			}
		}
	}
	else {
		var win = document.getElementById("uploadwin");
		document.getElementById("uploadwin").style.display = "block";
		win.style.left = (document.body.clientWidth - win.clientWidth) / 2;
		win.style.top = (document.body.clientHeight - win.clientHeight) / 2;
	}
}


function submit_file()
{
	if( confirm(document.getElementById("warning").value) ) {
		var form = document.getElementById("frm_upload_categories");
		if( form != null ) {
			MTstartServerComm();
			form.submit();
		}
	}
}


// called from file dialog
function vs_fdSaveFile()
{
	fdSaveFile('impexp', ext, export_category);
	var date = new Date();
	fdSetFile("vs_categories_"+ date.format("yyyyMMddhhmmss"));
}
function export_category(fdLocal, path, fileName)
{
	if( fdLocal ) {	
		document.getElementById("cmd").value = "export";
		if( !strEndsWith(path, "." + ext) )
			document.getElementById("impexp").value = path + "." + ext;
		var form = document.getElementById("frm_set_category");
		if( form != null ) {
			MTstartServerComm();
			form.submit();
		}
	}
	else {
		var date = new Date();
		fileName = "vs_categories_"+ date.format("yyyyMMddhhmmss")+"." + ext;
		cat_export_callback = fileName;
		path = document.getElementById("remoteFolder").value + fileName;
		new AjaxRequest("servlet/master", "POST", "cmd=export&impexp="+path, Callback_export_category, true);
	}
}


function Callback_export_category()
{
	MTstopServerComm();
	path = document.getElementById("remoteFolder").value + cat_export_callback;
	window.open(top.frames["manager"].getDocumentBase() + "app/report/popup.html?" + path);
}


function strEndsWith(str, sufix)
{
	if( str.length >= sufix.length )
		return str.substr(str.length - sufix.length, sufix.length).toLowerCase() == sufix.toLowerCase();
	return false;
}


function updateCatMapFlags()
{
	// update flags status (destroyed by list render)
	for(var i = 0; i < listCatMap.mData.length; i++) {
		var rlogic = document.getElementById("rlogic" + i);
		if( rlogic )
			rlogic.checked = listCatMap.mData[i][COL_CATMAP_RLOGICV];
		var areset = document.getElementById("areset" + i);
		if( areset )
			areset.checked = listCatMap.mData[i][COL_CATMAP_ARESETV];
	}
	
}


function addDevVarMdl()
{
	if( idDevMdlSelected == 0 || idVarMdlSelected == 0 )
		return;
	
	if( !checkCatMap(idDevMdlSelected, idVarMdlSelected) ) {
		return;
	}
	
	var i = listCatMap.mData.length;
	var aType = typeVarMdl(idVarMdlSelected);
	listCatMap.mData[i] = new Array("", i, i,
		nameDevMdl(idDevMdlSelected),
		nameVarMdl(idVarMdlSelected),
		aType[0],
		aType[1] == TYPE_DIGITAL ? "<input id='rlogic" + i + "' name='rlogic" + i + "' type='checkbox' onClick='onRLogicClicked(" + i + ")'>" : "&nbsp;",
		aType[1] == TYPE_DIGITAL ? "<input id='areset" + i + "' name='areset" + i + "' type='checkbox' onClick='onAResetClicked(" + i + ")'>" : "&nbsp;",
		// hidden columns
		idDevMdlSelected,
		idVarMdlSelected,
		aType[1],
		false,
		false);
	iCatMapSelected = -1;
	listCatMap.rowSelected = null;
	listCatMap.numRows = listCatMap.mData.length;
	listCatMap.render();
	updateCatMapFlags();
	checkCatButtons();
}


function removeDevVarMdl()
{
	if( iCatMapSelected < 0 )
		return;
	
	listCatMap.mData.splice(iCatMapSelected, 1);
	// update items index
	for(var i = 0; i < listCatMap.mData.length; i++) {
		listCatMap.mData[i][COL_CATMAP_I1] = i;
		listCatMap.mData[i][COL_CATMAP_I2] = i;
	}
	iCatMapSelected = -1;
	listCatMap.rowSelected = null;
	listCatMap.numRows = listCatMap.mData.length;
	listCatMap.render();
	updateCatMapFlags();
	checkCatButtons();
}


function nameDevMdl(id)
{
	for(var i = 0; i < listDevMdl.mData.length; i++)
		if( listDevMdl.mData[i][COL_DEVMDL_ID] == id )
			return listDevMdl.mData[i][COL_DEVMDL_NAME];
	return "";
}


function nameVarMdl(id)
{
	for(var i = 0; i < listVarMdl.mData.length; i++)
		if( listVarMdl.mData[i][COL_VARMDL_ID] == id )
			return listVarMdl.mData[i][COL_VARMDL_NAME];
	return "";
}


function typeVarMdl(id)
{
	for(var i = 0; i < listVarMdl.mData.length; i++)
		if( listVarMdl.mData[i][COL_VARMDL_ID] == id )
			return new Array(listVarMdl.mData[i][COL_VARMDL_TYPE], listVarMdl.mData[i][COL_VARMDL_TYPENO]);
	return new Array("", "0");
}


function checkCatButtons()
{
	var img = document["imgAddDevVarMdl"]; 
	if( img != null )
		img.src = idDevMdlSelected != 0 && idVarMdlSelected != 0 ? ADD_ON : ADD_OFF;

	var img = document["imgRemoveDevVarMdl"]; 
	if( img != null )
		img.src = iCatMapSelected >= 0 ? REMOVE_ON : REMOVE_OFF;
}


function getCatsVariablesNo()
{
	var nCount = 0;
	var idCatEdited = document.getElementById("id").value;
	for(var i = 0; i < listCat.mData.length; i++)
		if( listCat.mData[i][COL_CAT_ID] != idCatEdited )
			nCount += parseInt(listCat.mData[i][COL_CAT_VARSNO], 10);
	return nCount;
}


function checkCatMap(idDevMdl, idVarMdl)
{
	// check if variable model already present
	for(var i = 0; i < listCatMap.mData.length; i++) {
		if( idVarMdl == listCatMap.mData[i][COL_CATMAP_IDVARMDL] ) {
			alert(document.getElementById("checkmap").value);
			return false;
		}
	}
	
	// check variable threshold
	var nCount = nCatVars;
	for(var i = 0; i < aDevMdls.length; i++) {
		for(var j = 0; j < listCatMap.mData.length; j++) {
			if( listCatMap.mData[j][COL_CATMAP_IDDEVMDL] == aDevMdls[i] )
				nCount++;
		}
		if( idDevMdl == aDevMdls[i] )
			nCount++;
	}
	if( nCount <= nVarThreshold ) {
		return true;
	}
	else {
		alert(document.getElementById("alert_var_threshold").value);
		return false;
	}
}


function listCatMap2Map()
{
	var map = "";
	for(var i = 0; i < listCatMap.mData.length; i++) {
		if( i > 0 )
			map += ";";
		map += listCatMap.mData[i][COL_CATMAP_IDDEVMDL];
		map += ",";
		map += listCatMap.mData[i][COL_CATMAP_IDVARMDL];
		map += ",";
		map += listCatMap.mData[i][COL_CATMAP_RLOGICV];
		map += ",";
		map += listCatMap.mData[i][COL_CATMAP_ARESETV];
	}
	return map;
}


function onCategoriesLoad()
{
	nVarThreshold = parseInt(document.getElementById("vs_var_threshold").value, 10);
	MAX_CAT_NO = parseInt(document.getElementById("vs_cat_no").value, 10);
	
	// set aliases
	listCat = Lsw1;
	listDevMdl = Lsw3;
	listVarMdl = Lsw4;
	listCatMap = Lsw2;
	
	disableAllAction();
	enableAction(ACTION_ADD);
	enableAction(ACTION_IMPORT);
	enableAction(ACTION_EXPORT);
	checkCatButtons();
	
	// alert
	var strAlert = document.getElementById("alert").value;
	if( strAlert.length > 0 )
		alert(strAlert);
}


function onSelectCategory(id)
{
	idCatSelected = id;
	enableAction(ACTION_MODIFY);
	enableAction(ACTION_REMOVE);
}


function onModifyCategory(id)
{
	idCatSelected = id;
	modify_category();
}


function onSymbolChanged(fileName)
{
	var img = document["imgSymbol"]; 

	if( img != null )
		if( fileName.length > 0 )
			img.src = SYMBOL_PATH + fileName;
		else
			img.src = NO_SYMBOL;
}


function onSelectDevModel(id)
{
	idDevMdlSelected = id;
	idVarMdlSelected = 0;
	checkCatButtons();	
	CommSend("servlet/ajrefresh", "POST", "action=var&id="+id,	"onVarModels", true);
}


function onSelectVarModel(id)
{
	idVarMdlSelected = id;
	checkCatButtons();	
}


function onAddCatMap(id)
{
	idVarMdlSelected = id;
	addDevVarMdl();
}


function onSelectCatMap(i)
{
	iCatMapSelected = i;
	checkCatButtons();
}


function onDeleteCatMap(i)
{
	iCatMapSelected = i;
	removeDevVarMdl();
}


function onRLogicClicked(i)
{
	listCatMap.mData[i][COL_CATMAP_RLOGICV] = document.getElementById("rlogic" + i).checked;	
}


function onAResetClicked(i)
{
	listCatMap.mData[i][COL_CATMAP_ARESETV] = document.getElementById("areset" + i).checked;	
}


function Callback_onVarModels(response)
{
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
}


function Callback_onCategoryMap(response)
{
	var xmlCat = response.getElementsByTagName("cat")[0];
	document.getElementById("id").value = xmlCat.getAttribute("id");
	document.getElementById("name").value = xmlCat.getAttribute("name");
	var symbol = xmlCat.getAttribute("symbol");
	document.getElementById("symbol").value = symbol;
	onSymbolChanged(symbol);
	var devmdls = xmlCat.getAttribute("devmdls");
	aDevMdls = devmdls.split(",");
	var aXmlCatMaps = response.getElementsByTagName("catmap");
	var aValue = new Array();
	for(var i = 0; i < aXmlCatMaps.length; i++) {
		var type = aXmlCatMaps[i].getAttribute("type");
		var reverselogic = aXmlCatMaps[i].getAttribute("reverselogic");
		var rlogic = reverselogic && reverselogic == "true"; 
		var autoreset = aXmlCatMaps[i].getAttribute("autoreset");
		var areset = autoreset && autoreset == "true"; 
		var aData = new Array("", i, i,
			aXmlCatMaps[i].getAttribute("devname"),
			aXmlCatMaps[i].getAttribute("varname"),
			document.getElementById("type" + aXmlCatMaps[i].getAttribute("type")).value,
			type == TYPE_DIGITAL
				? "<input id='rlogic" + i + "' name='rlogic" + i + "' type='checkbox' onClick='onRLogicClicked(" + i + ")'" + (rlogic ? "checked" : "") + ">"
				: "&nbsp;",
			type == TYPE_DIGITAL
				? "<input id='areset" + i + "' name='areset" + i + "' type='checkbox' onClick='onAResetClicked(" + i + ")'" + (areset ? "checked" : "") + ">"
				: "&nbsp;", 
			// hidden columns
			aXmlCatMaps[i].getAttribute("iddevmdl"),
			aXmlCatMaps[i].getAttribute("idvarmdl"),
			type,
			rlogic,
			areset
		);
		aValue[i] = aData;
	}
	disableAction(ACTION_ADD);
	enableAction(ACTION_SAVE);
	listCatMap.mData = aValue;
	listCatMap.numRows = aValue.length;
	listCatMap.render();
	nCatVars = getCatsVariablesNo();
}
