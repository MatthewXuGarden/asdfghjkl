// Modbus Slave Configuration
var ACTION_SAVE			= 1;
var ACTION_MODIFY		= 2;
var ACTION_ADD			= 3;
var ACTION_REMOVE		= 4;
var ACTION_RESTART		= 5;
var ACTION_PRINT        = 6;
var ACTION_DEFAULTDEVICE= 7;

var LOAD_DEVICE_VAR		= 0;
var LOAD_DEVICE			= 1;
var LOAD_DEVICE_MDL		= 2;
var RESTART_SERVICE		= 10;

var VARIABLES_LIMIT		= 0; // value loaded from productinfo/mdslave_threshold

// selected items
var idSelectedMbDev		= 0;
var idSelectedDev		= 0;
var idSelectedDevVar	= 0;
var iSelectedMbVar		= -1;
var strSelectedDev		= "";
var strSelectedDevVar	= "";
var insertMode			= "singleDevice"; // or multipleDevices
var aaDevVarCache		= null; // devices/variables cache 

// address counters
var nAddrCoil			= 0;
var nAddrRegister		= 0;

//var types
var astrVarTypes		= null;
var TYPE_DIGITAL		= "1";	
var TYPE_ANALOGIC		= "2";	
var TYPE_INTEGER		= "3";
var TYPE_ALARM			= "4";
var TYPE_LONG			= "32";

// aliases
var listMbVar			= null;
var COL_MBVAR_I1		= 1;
var COL_MBVAR_I2		= 2;
var COL_MBVAR_PVTYPE	= 5;
var COL_MBVAR_TYPE		= 6;
var COL_MBVAR_ADDRESS	= 7;
var COL_MBVAR_READ_ONLY	= 8;
var COL_MBVAR_IDDEVICE	= 9;
var COL_MBVAR_IDVARIABLE= 10;
var COL_MBVAR_FLAGS		= 11;
var COL_MBVAR_PVNTYPE	= 12;
var MBTYPE_COIL			= "coil";
var MBTYPE_REGISTER		= "register";

// flags
var FLAG_UNDEFINED		= 0x00;
var FLAG_READ_ONLY		= 0x01;

// img buttons
var ADD_ON				= "images/actions/addsmall_on_black.png";
var ADD_OFF				= "images/actions/addsmall_off.png";
var REMOVE_ON			= "images/actions/removesmall_on_black.png";
var REMOVE_OFF			= "images/actions/removesmall_off.png";
var UP_ON				= "images/actions/arrowupsmall_on_black.png";
var UP_OFF				= "images/actions/arrowupsmall_off.png";
var DOWN_ON				= "images/actions/arrowdnsmall_on_black.png";
var DOWN_OFF			= "images/actions/arrowdnsmall_off.png";

function create_default_device()
{
	document.getElementById("typewin").style.display="block";
	setPopupWinCenter("typewin");
}

function submit_template_file(){
	var varSourceInput = document.getElementsByName("varsourceInput");
	var varSource = document.getElementById("varsource");
	var flag = false ;
	var str = "";
    for(var i=0;i<varSourceInput.length;i++){ 
        if(varSourceInput[i].checked){ 
            flag = true ;
            str += varSourceInput[i].value+",";
        } 
    } 
    if(!flag){ 
    	alert(document.getElementById("alert_choosevarsource").value);
        return false ; 
    }
    varSource.value = str;
	document.getElementById("uploadfrm1").submit();
}

function add_device()
{
	if( document.getElementById("device_name").value == ""
		|| document.getElementById("device_address").value == "" ) {
		alert(document.getElementById("alert_req_fields").value);
		return;
	}

	var nMbVars = parseInt(document.getElementById("variables").value, 10) + listMbVar.mData.length;
	if( nMbVars > VARIABLES_LIMIT ) {
		alert(document.getElementById("alert_var_limit").value + " " + (nMbVars - VARIABLES_LIMIT));
		return;
	}
	
	var strMbDev = defMbDev();
	if( strMbDev.length <= 0 ) {
		alert(document.getElementById("alert_dev_empty").value);
		return;
	}

	document.getElementById("cmd").value = "add";
	document.getElementById("defmbdev").value = strMbDev;
	var form = document.getElementById("frm_modbus_slave");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function remove_device()
{
	document.getElementById("cmd").value = "remove";
	document.getElementById("idmbdev").value = idSelectedMbDev;
	var form = document.getElementById("frm_modbus_slave");
	if( form != null ) {
		var goDeleteStr = document.getElementById("slaveDeviceDelete").value;
		var sureDelete = confirm(goDeleteStr);
		if(sureDelete){
			MTstartServerComm();
			form.submit();
		}
	}
}


function modify_device()
{
	document.getElementById("cmd").value = "modify";
	document.getElementById("idmbdev").value = idSelectedMbDev;
	var form = document.getElementById("frm_modbus_slave");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function save_device()
{
	if( document.getElementById("connection").checked
		&& (document.getElementById("comport").value == ""
		|| document.getElementById("baudrate").value == "") ) {
		alert(document.getElementById("alert_req_fields").value);
		return;
	}
	
	var idmbdev = document.getElementById("idmbdev").value;
	if( idmbdev != 0 &&	(document.getElementById("device_name").value == ""
		|| document.getElementById("device_address").value == "") ) {
		alert(document.getElementById("alert_req_fields").value);
		return;
	}

	var nMbVars = parseInt(document.getElementById("variables").value, 10) + listMbVar.mData.length;
	if( nMbVars > VARIABLES_LIMIT ) {
		alert(document.getElementById("alert_var_limit").value + " " + (nMbVars - VARIABLES_LIMIT));
		return;
	}
	
	var strMbDev = defMbDev();
	if( idmbdev != 0 && strMbDev.length <= 0 ) {
		alert(document.getElementById("alert_dev_empty").value);
		return;
	}
		
	document.getElementById("cmd").value = "save";
	document.getElementById("defmbdev").value = strMbDev;
	var form = document.getElementById("frm_modbus_slave");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function restart_service()
{
	reload_actions(RESTART_SERVICE);
}


function defMbDev()
{
	var def = "";
	for(var i = 0; i < listMbVar.mData.length; i++) {
		if( i > 0 )
			def += ";";
		def += listMbVar.mData[i][COL_MBVAR_IDDEVICE];
		def += ",";
		if( listMbVar.mData[i][COL_MBVAR_IDVARIABLE] < 0 )
			def += -listMbVar.mData[i][COL_MBVAR_IDVARIABLE];
		else
			def += listMbVar.mData[i][COL_MBVAR_IDVARIABLE];
		def += ",";
		def += listMbVar.mData[i][COL_MBVAR_TYPE];
		def += ",";
		def += listMbVar.mData[i][COL_MBVAR_ADDRESS];
		def += ",";
		def += listMbVar.mData[i][COL_MBVAR_FLAGS];
	}
	return def;
}


function onConfLoad()
{
	if( "true" == document.getElementById("displayPrintIcon").value)
		enableAction(ACTION_PRINT);
	// set alias
	listMbVar = Lsw2;
	VARIABLES_LIMIT = parseInt(document.getElementById("variables_limit").value, 10);

	onConnection(document.getElementById("connection").checked ? "RS485" : "TCP/IP");
	var idmbdev = document.getElementById("idmbdev").value;
	if( idmbdev == 0 ) {
		enableAction(ACTION_ADD);
	}
	else {
		// update address counters
		var nLastCoil = 0;
		var nLastRegister = 0;
		for(i = 0; i < listMbVar.mData.length; i++) {
			if( listMbVar.mData[i][COL_MBVAR_TYPE] == MBTYPE_COIL ) {
				nAddrCoil = parseInt(listMbVar.mData[i][COL_MBVAR_ADDRESS], 10);
				nLastCoil = 1;
			}
			else if( listMbVar.mData[i][COL_MBVAR_TYPE] == MBTYPE_REGISTER ) {
				nAddrRegister = parseInt(listMbVar.mData[i][COL_MBVAR_ADDRESS], 10);
				nLastRegister = listMbVar.mData[i][COL_MBVAR_PVNTYPE] == TYPE_ANALOGIC || listMbVar.mData[i][COL_MBVAR_PVNTYPE] == TYPE_LONG
					? 2 : 1;
			}
		}
		nAddrCoil += nLastCoil;
		nAddrRegister += nLastRegister;
		setModUser();
	}
	showAddrCounters();
	enableAction(ACTION_SAVE);
	enableAction(ACTION_DEFAULTDEVICE);
	
	if( document.getElementById("service_running").value == 1 )
		enableAction(ACTION_RESTART);
	
	if( document.getElementById("mb_restart").value == 1 )
		alert(document.getElementById("alert_mb_restart").value);
	
	var needConfirm = document.getElementById("needConfirm").value;
	if(needConfirm != "0")
	{
		if(confirm(document.getElementById("alert_confirmremove").value))
		{
			document.getElementById("deleteConfirm").value = "1";
			document.getElementById("uploadfrm1").submit();
		}
	}
	var invalidType = document.getElementById("invalidType").value;
	if(invalidType == "1")
		alert(document.getElementById("alert_maximumvarnum").value);
	else if(invalidType == "2")
		alert(document.getElementById("alert_maximumaddress").value);
	else if(invalidType == "0")
	{
		var deviceAdded = document.getElementById("deviceAdded").value;
		if(deviceAdded != "0")
			alert(document.getElementById("alert_deviceadded").value);
	}
}
function printModbusDevices(){
	CommSend("servlet/ajrefresh","POST","action=11","printTableModbusDevice",true);
}
function Callback_printTableModbusDevice(xml){
	var sitename = document.getElementById("sitename");
	var name = "";
	if(sitename != null)
	{
		name = sitename.value;
	}
	var mytitles = xml.getElementsByTagName("mytitle");
	var t = mytitles[0];
	var timestamp = new Date();
	var strTime = timestamp.getFullYear()+"/"+(timestamp.getMonth()+1).toString()+"/"+timestamp.getDate().toString()+" "+timestamp.toTimeString() + " - " + name;
	var str="<b style=\"font-size:18pt;\">"+t.childNodes[0].nodeValue+"</b><br><br>"+strTime;
	
	var combs = xml.getElementsByTagName("combined");
	for( var a =0;a<combs.length;a++){
		str += "<br><br>\n<table border='0' cellspacing=\"0\" cellpadding=\"0\">"
		var com = combs[a];
		var modbusdevices =com.getElementsByTagName("mdsdev");
		var mdsdev = modbusdevices[0];
		var devheaders = mdsdev.getElementsByTagName("trh");
		var devdatas = mdsdev.getElementsByTagName("tr");
		var caption = "";
		var devth = devheaders[0];
		var devdata = devdatas[0];
		for( var b =0;b<devth.childNodes.length;b++){
			var h = devth.childNodes[b].childNodes[0].nodeValue;
			var d = devdata.childNodes[b].childNodes[0].nodeValue;
			caption += h+" : "+"<b>"+d+"</b>"+"&nbsp;&nbsp;";
		}
		str += "\n<caption style=\"font-size:10pt;\" >"+caption+"</caption>";
				
		var mbsdevvars =com.getElementsByTagName("devvars");
		var mbsdevvar = mbsdevvars[0];
		str+="\n<thead>";
		var ths = mbsdevvar.getElementsByTagName("trh");
		var th = ths[0];
		for( var k =0;k<th.childNodes.length;k++){
			str+="\n<th class=\"thprint\">"+th.childNodes[k].childNodes[0].nodeValue+"</th>";
		}
		str+="\n</thead>";
		str += "\n<tbody>";
		var trs = mbsdevvar.getElementsByTagName("tr");
		for( var i =0;i<trs.length;i++){
			var tr = trs[i];
			str+="\n<tr style=\" valign:bottom\" >";
			for (var td=0;td<tr.childNodes.length ;td++){
				var cellContext;
				cellContext =tr.childNodes[td].childNodes[0].nodeValue;
				str+="\n<td  class=\"tdprint\"  >"+cellContext+"&nbsp;</td>";
			}
			str+="\n</tr>";
		}
		str+="\n</tbody>";
		str+="\n</table>";
	}
	
	str+="<br><br>";		
	openPrintPage(str);
}


function onConnection(type)
{
	var disabled = type == "TCP/IP";
	document.getElementById("comport").disabled = disabled;
	document.getElementById("baudrate").disabled = disabled;
}


function onSelectDevice(idDevice)
{
	idSelectedMbDev = idDevice;
	enableAction(ACTION_MODIFY);
	enableAction(ACTION_REMOVE);
}


function onModifyDevice(idDevice)
{
	idSelectedMbDev = idDevice;
	modify_device();
}


function onSelectDevVar(i)
{
	var objDevVar = document.getElementById("devvar");
	idSelectedDevVar = objDevVar.options[i].value;
	strSelectedDevVar = objDevVar.options[i].text;
	checkButtons();
}


function onReadOnly(i, value)
{
	listMbVar.mData[i][COL_MBVAR_FLAGS] ^= FLAG_READ_ONLY;
}


function addDevVar()
{
	var objDevVar = document.getElementById("devvar");
	var bVarExists = false;
	var strVarExists = "";
	
	updateAddrCounters();

	if( insertMode == "singleDevice" ) {
		for(var i = 0; i < objDevVar.options.length; i++) {
			if( objDevVar.options[i].selected ) {
				idSelectedDevVar = objDevVar.options[i].value;
				strSelectedDevVar = objDevVar.options[i].text;
				if( isVariable(idSelectedDevVar) ) {
					bVarExists = true;
					strVarExists += "\n- " + strSelectedDevVar;
				}
				else
					addMbVar(idSelectedDev, strSelectedDev, idSelectedDevVar, strSelectedDevVar, astrVarTypes[i]);
			}
		}
	}
	
	if( insertMode == "multipleDevices" ) {
		var objDev = document.getElementById("dev");
		var bBreak = false;
		for(var i = 0; i < objDev.options.length; i++) {
			if( objDev.options[i].selected ) {
				var idDevice = objDev.options[i].value;
				var strDevice = objDev.options[i].text;
				var aVariables = getVariablesFromCache(idDevice);
				for(var j = 0; j < objDevVar.options.length; j++) {
					if( objDevVar.options[j].selected ) {
						var idDevVar = aVariables[j];
						var strDevVar = objDevVar.options[j].text;
						if( isVariable(idDevVar) ) {
							bVarExists = true;
							strVarExists += "\n- " + strDevVar;
						}
						else if( !addMbVar(idDevice, strDevice, idDevVar, strDevVar, astrVarTypes[j]) ) {
							bBreak = true;
							break;
						}
					}
				}
			}
			if( bBreak )
				break;
		}
	}
	
	showAddrCounters();
	
	if( bVarExists )
		alert(document.getElementById("alert_var_exists").value + strVarExists);

	checkButtons();
}


function onSelectMbVar(str)
{
	var i = parseInt(str, 10);
	if(	iSelectedMbVar != i )
		iSelectedMbVar = i;
	else
		iSelectedMbVar = -1;
	checkButtons();
}


function onDeleteMbVar(i)
{
	if( i >= 0 )
		deleteMbVar(i);
	else if( iSelectedMbVar >= 0 )
		deleteMbVar(iSelectedMbVar);
	checkButtons();
}


function addMbVar(idDevice, strDevName, idVariable, strVarName, strVarType)
{
	var strMbVarType = typeMbVar(strVarType);
	var index = checkFreeAddr(strVarType, strMbVarType, strMbVarType == MBTYPE_COIL ? nAddrCoil : nAddrRegister); 

	if( index == -1 ) {
		if( strMbVarType == MBTYPE_COIL )
			alert(document.getElementById("alert_coil_used").value + ": " + nAddrCoil);
		else
			alert(document.getElementById("alert_reg_used").value + ": " + nAddrRegister);
		return false;
	} 
	
	var element = new Array("", 0, 0,
		strDevName,
		strVarName,
		typePvVar(strVarType),
		strMbVarType,
		strMbVarType == MBTYPE_COIL ? nAddrCoil : nAddrRegister,
		"<input type=''checkbox'>", // dummy
		// hidden columns
		idDevice,
		idVariable,
		FLAG_UNDEFINED,
		strVarType);
	var i = listMbVar.mData.length;

	/*
	if( i == 0 || strMbVarType == MBTYPE_REGISTER ) {
		listMbVar.mData[i] = element;
	}
	else {
		for(i = 0; i < listMbVar.mData.length; i++)
			if( listMbVar.mData[i][COL_MBVAR_TYPE] == MBTYPE_REGISTER )
				break;
		listMbVar.mData.splice(i, 0, element);
	}
	*/
	listMbVar.mData.splice(index, 0, element);
	
	// update address counters
	if( strMbVarType == MBTYPE_COIL )
		nAddrCoil++;
	else if( strVarType == TYPE_ANALOGIC || strVarType == TYPE_LONG )
		nAddrRegister += 2;
	else
		nAddrRegister++;

	// update items index
	for(var i = 0; i < listMbVar.mData.length; i++) {
		var bRO = listMbVar.mData[i][COL_MBVAR_IDVARIABLE] < 0;
		listMbVar.mData[i][COL_MBVAR_I1] = i;
		listMbVar.mData[i][COL_MBVAR_I2] = i;
		listMbVar.mData[i][COL_MBVAR_READ_ONLY] = "<input type='checkbox' onClick='onReadOnly(" + i + ", this.checked)'"
			+ (listMbVar.mData[i][COL_MBVAR_FLAGS] & FLAG_READ_ONLY == FLAG_READ_ONLY || bRO ? " checked" : "")
			+ (bRO ? " disabled" : "")
			+ ">";
	}
	iSelectedMbVar = -1;
	listMbVar.rowSelected = null;
	listMbVar.numRows = listMbVar.mData.length;
	listMbVar.render();
	
	return true;
}


function checkFreeAddr(strVarType, strMbVarType, nNewAddr)
{
	var nNewOffset = nNewAddr;
	if(	strVarType == TYPE_ANALOGIC || strVarType == TYPE_LONG )
		nNewOffset++;
	for(var i = 0; i < listMbVar.mData.length; i++) {
		if(	listMbVar.mData[i][COL_MBVAR_TYPE] == strMbVarType ) {
			var nAddr = parseInt(listMbVar.mData[i][COL_MBVAR_ADDRESS], 10);
			var nOffset = nAddr;
			if( listMbVar.mData[i][COL_MBVAR_PVNTYPE] == TYPE_ANALOGIC || listMbVar.mData[i][COL_MBVAR_PVNTYPE] == TYPE_LONG )
				nOffset++;
			if( nNewAddr >= nAddr && nNewAddr <= nOffset )
				return -1;
			if( nNewOffset >= nAddr && nNewOffset <= nOffset )
				return -1;
			if( nAddr > nNewOffset )
				return i;
		}
		else if( strMbVarType == MBTYPE_COIL )
			return i;
	}
	return i;
}


function deleteMbVar(i)
{
	/*
	var nAddr = parseInt(listMbVar.mData[i][COL_MBVAR_ADDRESS], 10);
	var nDelSize = listMbVar.mData[i][COL_MBVAR_PVNTYPE] == TYPE_ANALOGIC || listMbVar.mData[i][COL_MBVAR_PVNTYPE] == TYPE_LONG
		? 2 : 1;
	var strDelType = listMbVar.mData[i][COL_MBVAR_TYPE];
	*/
	listMbVar.mData.splice(i, 1);
	// update items index; no need to update items address
	for(; i < listMbVar.mData.length; i++) {
		var bRO = listMbVar.mData[i][COL_MBVAR_IDVARIABLE] < 0;
		
		listMbVar.mData[i][COL_MBVAR_I1] = i;
		listMbVar.mData[i][COL_MBVAR_I2] = i;
		/*
		if( listMbVar.mData[i][COL_MBVAR_TYPE] == strDelType ) {
			if( parseInt(listMbVar.mData[i][COL_MBVAR_ADDRESS], 10) - nAddr <= nDelSize ) { // no gap detected
				if( listMbVar.mData[i][COL_MBVAR_TYPE] == MBTYPE_COIL ) {
					listMbVar.mData[i][COL_MBVAR_ADDRESS] = nAddr++;
				}
				else {
					listMbVar.mData[i][COL_MBVAR_ADDRESS] = nAddr;
					nAddr += listMbVar.mData[i][COL_MBVAR_PVNTYPE] == TYPE_ANALOGIC || listMbVar.mData[i][COL_MBVAR_PVNTYPE] == TYPE_LONG
						? 2 : 1;
				}
			}
		}
		*/
		listMbVar.mData[i][COL_MBVAR_READ_ONLY] = "<input type='checkbox' onClick='onReadOnly(" + i + ", this.checked)'"
			+ (listMbVar.mData[i][COL_MBVAR_FLAGS] & FLAG_READ_ONLY == FLAG_READ_ONLY || bRO ? " checked" : "")
			+ (bRO ? " disabled" : "")
			+ ">";
	}
	iSelectedMbVar = -1;
	listMbVar.rowSelected = null;
	listMbVar.numRows = listMbVar.mData.length;
	listMbVar.render();
}


function isVariable(idVariable)
{
	for(var i = 0; i < listMbVar.mData.length; i++)
		if( listMbVar.mData[i][COL_MBVAR_IDVARIABLE] == idVariable )
			return true;
	return false;
}


function typeMbVar(strVarType)
{
	if( strVarType == TYPE_DIGITAL || strVarType == TYPE_ALARM )
		return MBTYPE_COIL;
	else if( strVarType == TYPE_ANALOGIC || strVarType == TYPE_INTEGER || strVarType == TYPE_LONG )
		return MBTYPE_REGISTER;
	else
		return "undefined";
}


function typePvVar(strVarType)
{
	var obj = document.getElementById("type" + strVarType);
	if( obj )
		return obj.value;
	else
		return "";
}


function moveVar(dir)
{
	if( iSelectedMbVar == -1 )
		return;
	if( dir > 0 && document["imgDownVar"].src.indexOf(DOWN_ON) < 0 )
		return;
	if( dir < 0 && document["imgUpVar"].src.indexOf(UP_ON) < 0 )
		return;
	
	// detect 1 register gap
	var nSmallGap = 0;
	if( dir > 0 
		&& listMbVar.mData[iSelectedMbVar + 1][COL_MBVAR_PVNTYPE] == TYPE_INTEGER
		&& iSelectedMbVar + 2 < listMbVar.mData.length
		&& parseInt(listMbVar.mData[iSelectedMbVar + 2][COL_MBVAR_ADDRESS], 10) - parseInt(listMbVar.mData[iSelectedMbVar + 1][COL_MBVAR_ADDRESS], 10) == 2 )
		nSmallGap = 1;
		
	// elements interchange
	var auxElement = listMbVar.mData[iSelectedMbVar + dir];
	listMbVar.mData[iSelectedMbVar + dir] = listMbVar.mData[iSelectedMbVar];
	listMbVar.mData[iSelectedMbVar] = auxElement;
	// addresses interchange
	var auxAddress = listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_ADDRESS];
	listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_ADDRESS] = listMbVar.mData[iSelectedMbVar][COL_MBVAR_ADDRESS];
	listMbVar.mData[iSelectedMbVar][COL_MBVAR_ADDRESS] = auxAddress;
	// update items index 
	var bRO = listMbVar.mData[iSelectedMbVar][COL_MBVAR_IDVARIABLE] < 0;
	listMbVar.mData[iSelectedMbVar][COL_MBVAR_I1] = iSelectedMbVar;
	listMbVar.mData[iSelectedMbVar][COL_MBVAR_I2] = iSelectedMbVar;
	listMbVar.mData[iSelectedMbVar][COL_MBVAR_READ_ONLY] = "<input type='checkbox' onClick='onReadOnly(" + iSelectedMbVar + ", this.checked)'"
		+ (listMbVar.mData[iSelectedMbVar][COL_MBVAR_FLAGS] & FLAG_READ_ONLY == FLAG_READ_ONLY || bRO ? " checked" : "")
		+ (bRO ? " disabled" : "")
		+ ">";
	 bRO = listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_IDVARIABLE] < 0;
	listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_I1] = iSelectedMbVar + dir;
	listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_I2] = iSelectedMbVar + dir;
	listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_READ_ONLY] = "<input type='checkbox' onClick='onReadOnly(" + (iSelectedMbVar + dir) + ", this.checked)'"
		+ (listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_FLAGS] & FLAG_READ_ONLY == FLAG_READ_ONLY || bRO ? "checked" : "")
		+ (bRO ? " disabled" : "")
		+ ">";
	// fix the address of the 2nd register
	if( listMbVar.mData[iSelectedMbVar][COL_MBVAR_TYPE] == MBTYPE_REGISTER ) {
		if( iSelectedMbVar + dir > iSelectedMbVar ) {
			var nLength = listMbVar.mData[iSelectedMbVar][COL_MBVAR_PVNTYPE] == TYPE_ANALOGIC
				|| listMbVar.mData[iSelectedMbVar][COL_MBVAR_PVNTYPE] == TYPE_LONG
				? 2 : 1;
			if( parseInt(listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_ADDRESS], 10) - parseInt(listMbVar.mData[iSelectedMbVar][COL_MBVAR_ADDRESS], 10) <= (nLength+1-nSmallGap) )
				listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_ADDRESS] = parseInt(listMbVar.mData[iSelectedMbVar][COL_MBVAR_ADDRESS], 10) + nLength;
			else
				fixAddrRegister(iSelectedMbVar + dir);
		}
		else {
			var nLength = listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_PVNTYPE] == TYPE_ANALOGIC
				|| listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_PVNTYPE] == TYPE_LONG
				? 2 : 1;
			if( parseInt(listMbVar.mData[iSelectedMbVar][COL_MBVAR_ADDRESS], 10) - parseInt(listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_ADDRESS], 10) <= nLength )
				listMbVar.mData[iSelectedMbVar][COL_MBVAR_ADDRESS] = parseInt(listMbVar.mData[iSelectedMbVar + dir][COL_MBVAR_ADDRESS],10) + nLength;
			else
				fixAddrRegister(iSelectedMbVar);
		}
	}
	listMbVar.rowSelected = null;
	listMbVar.render();
	iSelectedMbVar += dir;
	listMbVar.selectRow(document.getElementById("2TDtr" + (iSelectedMbVar + 1)));
	checkButtons();
}


function fixAddrRegister(index)
{
	var nAddr = parseInt(listMbVar.mData[index][COL_MBVAR_ADDRESS], 10);
	var nLength = listMbVar.mData[index][COL_MBVAR_PVNTYPE] == TYPE_ANALOGIC
		|| listMbVar.mData[index][COL_MBVAR_PVNTYPE] == TYPE_LONG
		? 2 : 1;
	for(var i = index + 1; i < listMbVar.mData.length && parseInt(listMbVar.mData[i][COL_MBVAR_ADDRESS], 10) - parseInt(listMbVar.mData[i-1][COL_MBVAR_ADDRESS],10) <= nLength; i++) {
		nAddr += nLength;
		listMbVar.mData[i][COL_MBVAR_ADDRESS] = nAddr;
		nLength = listMbVar.mData[i][COL_MBVAR_PVNTYPE] == TYPE_ANALOGIC
			|| listMbVar.mData[i][COL_MBVAR_PVNTYPE] == TYPE_LONG
			? 2 : 1;
	}
}


function checkButtons()
{
	var img = document["imgAddVar"]; 
	if( img != null )
		img.src = idSelectedDevVar != 0 ? ADD_ON : ADD_OFF;

	img = document["imgRemoveVar"]; 
	if( img != null )
		img.src = iSelectedMbVar >= 0 ? REMOVE_ON : REMOVE_OFF;
		
	var imgUp = document["imgUpVar"];
	var imgDown = document["imgDownVar"];
	if( iSelectedMbVar >= 0 ) {
		if( iSelectedMbVar == 0 ) {
			imgUp.src = UP_OFF;
			if( iSelectedMbVar < listMbVar.mData.length - 1 )
				imgDown.src = listMbVar.mData[iSelectedMbVar][COL_MBVAR_TYPE] == listMbVar.mData[iSelectedMbVar + 1][COL_MBVAR_TYPE]
					? DOWN_ON : DOWN_OFF;
		}
		else if( iSelectedMbVar == listMbVar.mData.length - 1 ) {
			imgDown.src = DOWN_OFF;
			if( iSelectedMbVar > 0 )
				imgUp.src = listMbVar.mData[iSelectedMbVar][COL_MBVAR_TYPE] == listMbVar.mData[iSelectedMbVar - 1][COL_MBVAR_TYPE]
					? UP_ON : UP_OFF;
		}
		else {
			imgDown.src = listMbVar.mData[iSelectedMbVar][COL_MBVAR_TYPE] == listMbVar.mData[iSelectedMbVar + 1][COL_MBVAR_TYPE]
				? DOWN_ON : DOWN_OFF;
			imgUp.src = listMbVar.mData[iSelectedMbVar][COL_MBVAR_TYPE] == listMbVar.mData[iSelectedMbVar - 1][COL_MBVAR_TYPE]
				? UP_ON : UP_OFF;
		}
	}
	else {
		imgUp.src = UP_OFF;
		imgDown.src = DOWN_OFF;
	}
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


function getVariablesFromCache(idDevice)
{
	for(var i = 0; i < aaDevVarCache.length; i++) {
		if( aaDevVarCache[i].idDevice == idDevice )
			return aaDevVarCache[i].aVariables;
	}
	return null;
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


function getNextAddr(strMbType)
{
	return strMbType == MBTYPE_COIL ? nAddrCoil : nAddrRegister;
}


function getUserAddr(strMbType)
{
	return parseInt(document.getElementById(strMbType == MBTYPE_COIL ? "next_coil_addr" : "next_register_addr").value, 10);
}


function showAddrCounters()
{
	document.getElementById("next_coil_addr").value = nAddrCoil;
	document.getElementById("next_register_addr").value = nAddrRegister;
}


function updateAddrCounters()
{
	// coil address
	var value = getUserAddr("coil");
	if( !isNaN(value) && value >= 0 )
		nAddrCoil = value;
	// register address
	value = getUserAddr("register");
	if( !isNaN(value) && value >= 0 )
		nAddrRegister = value;
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


function Callback_restartService()
{
	alert(document.getElementById("alert_mb_restart2").value);
}
