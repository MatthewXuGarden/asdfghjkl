lineSelected = null;
var maxindex=0;
var old_passw = "";
//var fistmodify = false;  //per sapere se ? stata modificata la password
var bAmbiguousDevice = false;
var beginAddr = null;
var endAddr = null;


var clickState = 0;

var actbDeviceSearch = null;

function selectedLine(idLine)
{
	if( idLine==null )
		return true;
	
	if( lineSelected==idLine ) 
	{
		if( (clickState==null) || (clickState==0) )
		{
			clickState=1;
			enableAction(2); // modify
			enableAction(4); // delete
		}
		else
		{
			clickState=0;
			disableAction(2); // modify
			disableAction(4); // delete
		}
	}
	else
	{
		clickState=1;
		enableAction(2); // modify
		enableAction(4); // delete
	}
	lineSelected=idLine;
}

function line_toTable()
{
	var templateVal = document.getElementById("device").options[document.getElementById("device").selectedIndex].value;
	if( templateVal < 0 )
		return;
	var templateDesc = document.getElementById("device").options[document.getElementById("device").selectedIndex].innerHTML;
	
	var from = parseInt(document.getElementById("from").value);
	var to = parseInt(document.getElementById("to").value);
	
	var table=document.getElementById("template");
	if (to<from) alert(document.getElementById("wrongindex").value);
	
	//Kevin, add virtual keyboard
	var vk_state = document.getElementById("vk_state").value;
	var keyboardInputCSS = vk_state=="1"?" keyboardInput ":"";
	
	for(i=from; i<=to; i++)
	{
		var bkgrStyleRow = ((i-1)%2==0) ? "" : " oddDevLine";
		var bkgrStyleDescInput = ((i-1)%2==0) ? " evenDevDesc"  : " oddDevDesc";
		
		row=table.rows[i-1];
		hiddenval= document.getElementById("var"+i);
		table.rows[i-1].className="Row1"+bkgrStyleRow;
		table.rows[i-1].cells[1].className="standardTxt";
		row.cells[1].innerHTML = templateDesc;
		row.cells[2].innerHTML = "<input name=\"desc_"+i+"\" id=\"desc_"+i+"\" class=\"standardTxt"+bkgrStyleDescInput+keyboardInputCSS+"\" style='width:100%;' type='text' maxlength='100' value='"+templateDesc +" - "+i+"' onblur='MioNoAtOnBlur(this);'/>"; //cont end  sostituito cont con i
		//row.cells[3].innerHTML = "<input name=\"disab_"+i+"\" id=\"disab_"+i+"\" class=\"standardTxt\" type='checkbox' />"; 
		row.cells[3].innerHTML = "<input name=\"disab_"+i+"\" id=\"disab_"+i+"\" class=\"standardTxt\" type='checkbox' />"; 
		row.cells[4].innerHTML = "<img src=\"images/button/settings_off.png\">";
		hiddenval.value=templateVal;
	}
	var strAddr = from == to ? from : from + " .. " + to;
	showBalloon("", templateDesc + ">> ", document.getElementById("devaddmsg").value + " " + strAddr);
	if(vk_state == "1")
		buildKeyboardInputs();
}

function line_remFromTable()
{
	var table=document.getElementById("template");
	var from = parseInt(document.getElementById("from").value);
	var to = parseInt(document.getElementById("to").value);
	var bkgrStyleRow = "";
	
	if (clickState==0)
	{
		var string = document.getElementById("removeall").value;
		string = string.replace("xx",from);
		string = string.replace("yy",to);
		if (confirm(string))
		{
			for(i=from; i<=to; i++)
			{
				bkgrStyleRow = ((i-1)%2==0) ? "" : " oddDevLine";
				// var bkgrStyleDescInput = ((i-1)%2==0) ? " evenDevDesc"  : " oddDevDesc";				
				
				hiddenval = document.getElementById("var"+i);
				row=table.rows[i-1];
				row.className="Row1"+bkgrStyleRow;
				row.cells[1].innerHTML="&nbsp;";
				row.cells[2].innerHTML="&nbsp;";
				row.cells[3].innerHTML="&nbsp;";
				row.cells[4].innerHTML="&nbsp;";
				hiddenval.value="empty";
			}
			lineSelected=null;
		}
		return true;
	}
	else
	{
		hiddenval = document.getElementById("var"+(lineSelected+1));
		bkgrStyleRow = ((lineSelected)%2==0) ? "" : " oddDevLine";
		if (hiddenval.value=="empty")
		{
			alert(document.getElementById("nullselected").value);
			lineSelected=null;
			clickState=0;	
		}
		else
		{
			table.rows[lineSelected].cells[1].innerHTML="&nbsp;";
			table.rows[lineSelected].cells[2].innerHTML="&nbsp;";
			table.rows[lineSelected].cells[3].innerHTML="&nbsp;";
			table.rows[lineSelected].cells[4].innerHTML="&nbsp;";
			hiddenval.value="empty";
			table.rows[lineSelected].className="Row1"+bkgrStyleRow;
			lineSelected=null;	//annullo la selezione
			clickState=0;
		}
	}
}


function setAddressTo() //se seleziono da combo indirizzo di partenza, quello di arrivo ? =
{
	from = document.getElementById("from").value;
	document.getElementById("to").selectedIndex=from-1;
}


// on device row selected
function selectLine(idLine)
{
	var table=document.getElementById("template");
	var oddRow = ((idLine)%2==0) ? "" : " oddDevLine";
	var oddDescInput = " oddDevDesc";
	var evenDescInput = " evenDevDesc";
	var devDescId = document.getElementById("desc_"+(idLine+1)); // +1 because devices starts from 1 while table rows from 0
	var devDescSelId = document.getElementById("desc_"+(lineSelected+1));
	
	rows = table.rows;
	
	document.getElementById("from").selectedIndex=idLine;
	document.getElementById("to").selectedIndex=idLine;
	
	if (lineSelected==idLine)   //riseleziono stessa riga
	{
		if ((clickState==null)||(clickState==0))
		{
			clickState=1;
			rows[idLine].className="selectedRow";
			if(devDescId) devDescId.className = "standardTxt"+ evenDescInput; //always white
		}
		else
		{
			clickState=0;
			rows[idLine].className="Row1"+oddRow;
			if(devDescId) devDescId.className = "standardTxt"+ ((idLine%2==0) ? evenDescInput : oddDescInput); // white or gray based on row parity
		}
	}
	else
	{
		clickState=1;
		rows[idLine].className="selectedRow";
		if(devDescId) devDescId.className = "standardTxt"+ evenDescInput;
		if (lineSelected!=null) { 
			var oddSelRow = ((lineSelected)%2==0) ? "" : " oddDevLine";
			rows[lineSelected].className="Row1"+oddSelRow;
			if(devDescSelId) devDescSelId.className = "standardTxt"+ ((lineSelected%2==0) ? evenDescInput : oddDescInput); // white or gray based on previous selected row parity
		}
	}
	lineSelected=idLine;
	
	// link device combo with the row selected
	var deviceVar = document.getElementById("var" + (idLine+1));
	if( deviceVar != null && deviceVar.value < 0 ) {
		onAmbiguousDevice(-deviceVar.value);
		bAmbiguousDevice = true;
	}
	else if( bAmbiguousDevice == true ) {
		onProtocolChanged(document.getElementById("protocol").value);
		bAmbiguousDevice = false;
	}
}

function undoclick()
{
	clickState = 0;
}

function remove_line()
{
	if( confirm(document.getElementById("noline").value) )
	{
		document.getElementById("cmd").value="remove_line";
		document.getElementById("idline").value = lineSelected;
		var form = document.getElementById("frm_set_line");
		if( form != null ) {
			MTstartServerComm();
			form.submit();
		}
	}
}


function isLineAddress(listAddress, editAddress, address, protocol)
{
	var nCount = 0;
	for(var i = 0; i < listAddress.length; i++) {
		if( listAddress[i] != editAddress && listAddress[i] == address )
			nCount++;
		
		//if( protocol == "AK255.XML" ) {
		//	if( nCount > 1 )
		//		return true;
		//}
		//else 
		if( nCount > 0 ) {
		return true;
		}
	}
	return false;
}


function save_line()
{
	document.getElementById("cmd").value = "save_line";
	
	var idline	= document.getElementById("idline").value;
	var linetype	= document.getElementById("linetype").value;
	var comport		= document.getElementById("comport").value;
	var baudrate	= document.getElementById("baudrate").value;
	var protocol	= document.getElementById("protocol").value;
	var proto		= linetype=="lan" ? document.getElementById("proto").value : "";
	var address		= linetype=="lan" ? document.getElementById("address").value : "";
	
	var alarmenable = "";
	var smart_modbusNO = linetype=="lan"?1:0;
	if((protocol.indexOf("MODBUS")!=-1 || proto.indexOf("MODBUS")!=-1)){
		if (typeof document.all("smart_modbus").length === 'undefined'){
			alarmenable = document.getElementById("smart_modbus").checked ? "enable" : "disable";
		}else{
			alarmenable = document.all("smart_modbus")[smart_modbusNO].checked ? "enable" : "disable";
		}
	}
	document.getElementById("smart_modbus_enable").value=alarmenable;
	
	//check if COM is already used
	var com_used_list = document.getElementById("com_used_list").innerHTML;
	var com_split = com_used_list.split(";"); 
	
	if( linetype == "lan" ) {
		var editAddress = document.getElementById("com").value;
		if( isLineAddress(com_split, editAddress, address, proto) ) {
			//address already in use
			alert(document.getElementById("dupaddress").value);
			return false;
		}
	}
	else {
		var com_of_line_edit = document.getElementById("com").value;
		for (var i=0;i<com_split.length;i++)
		{
			if (com_split[i]!=(com_of_line_edit) && com_split[i]==("COM"+comport))
			{
				//COM already in use
				alert(document.getElementById("s_com_used").innerHTML);
				return false;
			}
		}
	}
	
	if( (linetype=="serial"	&& ( (comport=="") || (baudrate=="") || (protocol=="") ) )
		|| (linetype=="lan"	&& ( (proto=="") || (address=="") ) ) ) {
		alert(document.getElementById("selcaract").value);
		return false;
	}
	
	/* hostname also allowed
	if( linetype=="lan" && !isIP(address) ) {
		alert(document.getElementById("wrongaddress").value);
		return false;
	}
	*/

	var table = document.getElementById("template");
	var devices			= "";
	var serials			= "";
	var numDeviceInsert = 0;  //Contatore dispositivi
	var protocoldiff	= "";
	var fixaddress		= "";
	var devtmpl			= null;
	for(var i=1; i <= table.rows.length; i++) {
		deviceVar	= document.getElementById("var"+i);
		serials		= serials + i + ",";
		devices		= devices + deviceVar.value + ",";
		if( deviceVar.value != "empty" ) {
			numDeviceInsert++;
			if( devtmpl == null || devtmpl.iddevmdl != deviceVar.value )
				devtmpl = getDeviceTemplate(deviceVar.value);
			if( devtmpl != null ) {
				if( devtmpl.filters.length > 0 // device could be filtered
					&& !( (linetype == "serial" && devtmpl.matchProtocolFilter(protocol) )
						|| (linetype == "lan" && devtmpl.matchProtocolFilter(proto) ) ) )
					protocoldiff += "\n" + devtmpl.desc;
			}
			else {
				if( deviceVar.value <= 0 )
					fixaddress += "\n" + i;
			}
		}
	}
	
	// in case of unrecognized devices or ambiguous detection
	if( fixaddress.length > 0 ) {
		fixaddress = document.getElementById("fixaddress").value + "\n" + fixaddress;
		alert(fixaddress);
		return false;
	}
	// in case of protocol mismatches
	if( protocoldiff.length > 0 ) {
		protocoldiff = document.getElementById("protocoldiff").value + "\n" + protocoldiff;
		alert(protocoldiff);
		return false;
	}
		
	serials = serials.substring(0,(serials.length-1));
	devices = devices.substring(0,(devices.length-1));
	devs=devices.split(",");

	b=false;
	for(i=0; i<devs.length; i++) {
		if( devs[i] != "empty" ) {
			b = true;
			break;
		}
	}
	if( b==false ) {
		alert(document.getElementById("insertdev").value);
		return true;
	} 
	document.getElementById("serials").value = serials;
	document.getElementById("devices").value = devices;
	var numTotDevices = new Number(document.getElementById("numTotDevices").innerHTML);
	var numMaxDevices = new Number(document.getElementById("numMaxDevices").innerHTML);
	if( numDeviceInsert+numTotDevices > numMaxDevices ) {
		alert(document.getElementById("maxLicenseLabel").innerHTML);
		return false;
	}

	var form = document.getElementById("frm_set_line");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}

function onLineLoaded()
{
	var divContainer = document.getElementById("container");
	if( divContainer ) {
		divContainer.style.height = top.frames["body"].document.body.clientHeight - 240;
	}
}

function onload_siteview()
{
	//onLineLoaded();
	//resizeColsSetlineTab();

	enableAction(3); // add line
	if (document.getElementById("can_CAN").value=="KO")
	{
		alert(document.getElementById("s_can_alert").value);
	}
	if (document.getElementById("control").value!="")
	{
		if( confirm(document.getElementById("control").value) ){
			jump2Tab("tab_5");
		}
	}
	if (document.getElementById("lineerror").value!="")
	{
		alert(document.getElementById("lineerror").value);
		return false;
	}

	var cmd = document.getElementById("cmd").value;
	if( cmd == "auto_detect" ) {
		onLineTypeChanged(document.getElementById("autodetect_linetype").value);
		enableAction(1); // save line
		setModUser();
		var nDevicesDetected = parseInt(document.getElementById("devices_detected").value, 10);
		if( nDevicesDetected > 0 )
			showBalloon("", document.getElementById("detectmsg2").value + " " + nDevicesDetected, "", 10);
		else
			showBalloon("", document.getElementById("detectmsg1").value, "", 10);
	}
	else if( cmd == "port_detect1" ) {
		onLineTypeChanged("serial");
		enableAction(1); // save line
		port_detect(2);
	}
	else if( cmd == "port_detect2" ) {
		onLineTypeChanged("serial");
		enableAction(1); // save line
		if( document.getElementById("comport").value == "" )
			alert(document.getElementById("portmsg3").value);
	}
	else if(cmd == "add_lineselect")
	{
		onLineTypeChanged("serial");
		enableAction(1); // save
		disableAction(3); // add
	}
	else if( document.getElementById("idline").value > 0 ) {
		onLineTypeChanged(document.getElementById("linetype").value);
		enableAction(1); // save line
		setModUser();
	}

	return false;
}

function jump2Tab(pg){
	top.frames['body'].frames['TabMenu'].document.getElementById(pg).onclick();
}

function setAlarmMng() {
	if (document.getElementById("resend_enable").checked) {
		var priority_checked = document.getElementById("resend_priority_1").checked
				|| document.getElementById("resend_priority_2").checked
				|| document.getElementById("resend_priority_3").checked
				|| document.getElementById("resend_priority_4").checked;
		var chanel_checke = document.getElementById("resend_channel_E").checked
				|| document.getElementById("resend_channel_S").checked;
		var freq_times = document.getElementById("resend_frequency").value == ''
				|| document.getElementById("resend_times").value == '';
			
		if ( !(priority_checked && chanel_checke) || freq_times) 
		{
			alert(document.getElementById("missingfield").value);
			return;
		}
	}
	if(!maxminvaluecheck("resend_times",1,25)){
		return ;
	}
	document.getElementById("cmd").value = "save_alarm_conf";
	var ofrm = document.getElementById("frm_alarm_mgr");
	if (ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}

function restoreDefault()
{
	document.getElementById("cmd").value = "restore_default";
	var ofrm = document.getElementById("frm_alarm_mgr");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}


/**
* Per ridimensionare la tabella che visualizza linea
*/
function resizeTableLineView()
{
	var hdev = MTcalcObjectHeight("trDevList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-50);
	}
}


function resizeColsSetlineTab()
{
	var wid0 = document.getElementById('THsetline00').offsetWidth - 3;
	var wid1 = document.getElementById('THsetline01').offsetWidth;
	var wid2 = document.getElementById('THsetline02').offsetWidth;
	var wid3 = document.getElementById('THsetline03').offsetWidth;
/*
	var table = document.getElementById("template"); 
	table.rows[0].cells[0].width = wid0 + "px";
	table.rows[0].cells[1].width = wid1 + "px";
	table.rows[0].cells[2].width = wid2 + "px";
	table.rows[0].cells[3].width = wid3 + "px";
*/
}


function initAlrMng()
{
	var permission = document.getElementById("permission").value;
	if (permission!=1)
	{
		enableAction(1);
		enableAction(2);
	}
}


function add_line()
{
	//Kevin add "add_lineselect" because when double select a line
	//then the user click "add", need to check the USB port
	if( document.getElementById("idline").value > 0 ) 
	{
		document.getElementById("cmd").value="add_lineselect";
		var form = document.getElementById("frm_set_line");
		if( form != null ) {
			MTstartServerComm();
			// to make sure the serial port was added/removed to/from the system
			setTimeout(onDelayedSubmit, 3000);
		}
	}
	else
	{
		enableAction(1); // save
		disableAction(3); // add
		onLineTypeChanged("serial");
	}
}


function modify_line()
{
	top.frames['manager'].loadTrx('nop&folder=siteview&bo=BSiteView&type=redirect&line=' + lineSelected + '&desc=ncode03');
}


function device_config(iddevice)
{
	var idline = document.getElementById("idline").value;
	top.frames['manager'].loadTrx('nop&folder=devdetail&bo=BDevDetail&type=click&iddev=' + iddevice + '&line=' + idline + '&desc=ncode04');	
}


function auto_detect()
{
	var linetype	= document.getElementById("linetype").value;
	if( linetype == "serial" ) {
		var protocol	= document.getElementById("protocol").value;
		var comport		= document.getElementById("comport").value;
		var baudrate	= document.getElementById("baudrate").value;
	
		if( comport == "" || baudrate == "" || protocol == "" ) {
			alert(document.getElementById("selcaract").value);
			return false;
		}
	
		if( confirm(document.getElementById("autowarning").value) ) {
			document.getElementById("cmd").value="auto_detect";
			var form = document.getElementById("frm_set_line");
			if( form != null ) {
				MTstartServerComm();
				form.submit();
			}
		}
	}
	else if( linetype == "lan" ) {
		var protocol	= document.getElementById("proto").value;
		var address		= document.getElementById("address").value;
		if( address == "" || protocol == "" ) {
			alert(document.getElementById("selcaract").value);
			return false;
		}

		document.getElementById("cmd").value="auto_detect";
		var form = document.getElementById("frm_set_line");
		if( form != null ) {
			MTstartServerComm();
			form.submit();
		}
	}
}


function port_detect(i)
{
	if( confirm(document.getElementById("portmsg" + i).value) ) {
		document.getElementById("cmd").value="port_detect" + i;
		var form = document.getElementById("frm_set_line");
		if( form != null ) {
			MTstartServerComm();
			// to make sure the serial port was added/removed to/from the system
			setTimeout(onDelayedSubmit, 2000);
		}
	}
}


function onDelayedSubmit()
{
	var form = document.getElementById("frm_set_line");
	if( form != null )
		form.submit();
}


function onLineTypeChanged(type)
{
	document.getElementById("linetype").value = type;
	var slinetype = document.getElementById("slinetype");
	if( slinetype )
		slinetype.selectedIndex = 0;
	var llinetype = document.getElementById("llinetype");
	if( llinetype )
		llinetype.selectedIndex = 1;

	if( type == "serial" ) {
		document.getElementById("comport").disabled		= false;
		document.getElementById("baudrate").disabled	= false;
		document.getElementById("protocol").disabled	= false;
		if( llinetype ) {
			document.getElementById("proto").disabled		= true;
			document.getElementById("address").disabled		= true;
			showLayer("layer_lan", false);
		}
		showLayer("layer_serial", true);
		onProtocolChanged(document.getElementById("protocol").value);
	}	
	else if( type == "lan" ) {
		document.getElementById("comport").disabled		= true;
		document.getElementById("baudrate").disabled	= true;
		document.getElementById("protocol").disabled	= true;
		document.getElementById("proto").disabled		= false;
		document.getElementById("address").disabled		= false;
		showLayer("layer_serial", false);
		showLayer("layer_lan", true);	
		onProtocolChanged(document.getElementById("proto").value);	
	}
	
	showLayer("layer_devices", true);
}


function onProtocolChanged(protocol)
{
	// device list
	var device_list = document.getElementById("device");
	var selvalue = device_list.value;
	device_list.options.length = 1;
	for(var i = 0; i < device_templates.length; i++) {
		if (protocol != "") {
			if( device_templates[i].matchProtocolFilter(protocol) ) {
				device_list.appendChild(newOption(device_templates[i].desc,device_templates[i].iddevmdl,(device_templates[i].iddevmdl == selvalue)));
			}
		}else {
			device_list.appendChild(newOption(device_templates[i].desc,device_templates[i].iddevmdl,(device_templates[i].iddevmdl == selvalue)));			
		}
	}
	
	// smart input box
	var device_search = document.getElementById("device_search"); 
	if( device_search ) {
		device_search.setAttribute("autocomplete","off"); // disable IE auto complete
		device_search.style.width = device_list.offsetWidth - WIDTH_CORRECTION;
		var device_patterns = new Array();
		for(var i = 1; i < device_list.options.length; i++)
			device_patterns[i-1] = device_list.options[i].text;
		var topheight = top.frames["body"].frames["TabMenu"].document.body.clientHeight-2;
		actbDeviceSearch = actb(document.getElementById('device_search'), device_patterns, onDeviceFound,topheight);
	}
	device_search.value = device_list.options[device_list.selectedIndex].text;
	
	// autodetect button
	//document.getElementById("autodetect").disabled =
	//	protocol != "CAREL_RS485" && protocol != "CAREL_RS485N" && protocol != "AK255.XML";
	document.getElementById("autodetect").disabled = (protocol != "CAREL_RS485" && protocol != "CAREL_RS485N"); 
	// protocol address space
	var objProtocolTemplate = getProtocolTemplate(protocol);
	if( objProtocolTemplate ) {
		if( beginAddr != null && endAddr != null ) {
			if( objProtocolTemplate.beginAddr != beginAddr || objProtocolTemplate.endAddr != endAddr )
				changeAddressSpace(objProtocolTemplate.beginAddr, objProtocolTemplate.endAddr);
		}
		else {
			beginAddr = objProtocolTemplate.beginAddr;
			endAddr = objProtocolTemplate.endAddr;
		}
	}
	else if( !(beginAddr != null && endAddr != null) ) {
		// default address space
		beginAddr = 1;
		endAddr = 207;
	}
	
	var comport = document.getElementById("comport").value;
	//CAN connector has not a COM associated: COM99
	if (protocol=="CAN_EDRONIC")
	{
		document.getElementById("combo_sel").innerHTML="<select id=\"comport\" name=\"comport\" class=\"standardTxt\"><option value=\"99\">----------</option></select>";
	}
	else
	{
		document.getElementById("combo_sel").innerHTML="<select id=\"comport\" name=\"comport\" class=\"standardTxt\"><option value=\"0\">----------</option>"+getOptions();+"</select>";
		if (comport==99)
			comport=0;
		document.getElementById("comport").value = comport;
	}
	
	//if(document.getElementById("layer_AK255") != null)
	//	document.getElementById("layer_AK255").style.display = (protocol == "AK255.XML" ? "block" : "none");
	//Alarm priority enabled
	document.getElementById("smart_modbus_div").style.display = (protocol.indexOf("MODBUS")!=-1 ? "block" : "none");
}
function getOptions(){
	var result = "";
	var source = document.getElementById("com_hidden").innerHTML;
	if(source.length>1){
		source = source.substring(0, source.length-1);
	}
		
	var opts = source.split(";");
	for(var i = 0; i < opts.length; i++){
		var opt = opts[i].split(",");
		if(opt.length>2){
			result += "<option "+ opt[0] +" value=\""+opt[1]+"\" >" +opt[2] + "</option>\n";
		}
	}
	return result;
}



function onDeviceSelected(deviceName)
{
	document.getElementById("device_search").value = deviceName;
}


function onDeviceFound(deviceName)
{
	var bFound = false;
	var obj = document.getElementById("device");
	for(var i = 1; i < obj.options.length; i++) {
		if( obj.options[i].text == deviceName ) {
			obj.selectedIndex = i;
			bFound = true;
			break;
		}
	}
	if( !bFound ) {
		obj.selectedIndex = 0;
	}
}


function onAmbiguousDevice(appCode)
{
	// device list
	var device_list = document.getElementById("device");
	var selvalue = device_list.value;
	device_list.options.length = 1;
	for(var i = 0; i < device_templates.length; i++) {
		if( device_templates[i].matchAppCode(appCode) ) {
			device_list.appendChild(newOption(device_templates[i].desc,device_templates[i].iddevmdl,(device_templates[i].iddevmdl == selvalue)));
		}
	}
	
	// smart input box
	var device_search = document.getElementById("device_search"); 
	if( device_search ) {
		device_search.setAttribute("autocomplete","off"); // disable IE auto complete
		device_search.style.width = device_list.offsetWidth - WIDTH_CORRECTION;
		var device_patterns = new Array();
		for(var i = 1; i < device_list.options.length; i++)
			device_patterns[i-1] = device_list.options[i].text;
		var topheight = top.frames["body"].frames["TabMenu"].document.body.clientHeight-2;
		actbDeviceSearch = actb(document.getElementById('device_search'), device_patterns, onDeviceFound,topheight);
	}
	device_search.value = device_list.options[device_list.selectedIndex].text;
}


function changeAddressSpace(begin, end)
{
	// from/to select options
	// devices table
	var from = document.getElementById("from");
	var to = document.getElementById("to");
	var table = document.getElementById("template");
	if( lineSelected != null ) {
		selectLine(lineSelected);
		lineSelected = null;
	}
	if( end > endAddr ) {
		for(var i = endAddr + 1; i <= end; i++) {
			from.options.add(new Option(i,i));
			to.options.add(new Option(i,i));
			var oldRow = table.rows[table.rows.length-1];
			var newRow = table.insertRow(table.rows.length);
			newRow.className = oldRow.className;
			for(var k in oldRow.style)
				if(oldRow.style[k])
					newRow.style[k] = oldRow.style[k];
			newRow.setAttribute("valign", oldRow.valign);
			newRow.onclick = new Function("selectLine(" + (i-1) + ")");
			for(var j = 0; j < oldRow.cells.length; j++) {
				var oldCell = oldRow.cells[j];
				var newCell = newRow.insertCell(j);
				newCell.className = oldCell.className;
				for(var k in oldCell.style)
					if( oldCell.style[k] )
						newCell.style[k] = oldCell.style[k];
				newCell.setAttribute("align", oldCell.getAttribute("align"));
			}
			newRow.cells[0].innerHTML = i + "<input type=\"hidden\" id=\"var" + i + "\" name=\"var" + i + "\" value=\"empty\">";
		}
	}
	else {
		from.length = end;
		to.length = end;
		while( table.rows.length > end )
			table.deleteRow(table.rows.length-1);
	}
	beginAddr = begin;
	endAddr = end;
}


function getDeviceTemplate(iddevmdl)
{
	for(var i = 0; i < device_templates.length; i++)
		if( device_templates[i].iddevmdl == iddevmdl )
			return device_templates[i];
	return null;
}


function getProtocolTemplate(code)
{
	for(var i = 0; i < protocol_templates.length; i++)
		if( protocol_templates[i].code == code )
			return protocol_templates[i];
	return null;
}

//////////////
function DeviceTemplate(iddevmdl, desc, filters, codes)
{
	this.iddevmdl	= iddevmdl;
	this.desc		= desc;
	this.filters	= filters;
	this.codes		= codes;
}


DeviceTemplate.prototype.matchProtocolFilter = function(protocol)
{
	if( protocol.length <= 0 )
		return true;
	
	for(var i = 0; i < this.filters.length; i++)
		if( protocol.indexOf(this.filters[i]) >= 0 )
			return true;
	
	return false;
};


DeviceTemplate.prototype.matchAppCode = function(code)
{
	if( code <= 0 )
		return true;

	for(var i = 0; i < this.codes.length; i++)
		if( this.codes[i] == code )
			return true;
	
	return false;
};


function ProtocolTemplate(code, name, connectionType, typeProtocol, protocol, beginAddr, endAddr)
{
	this.code			= code;
	this.name			= name;
	this.connectionType = connectionType;
	this.typeProtocol 	= typeProtocol;
	this.beginAddr		= beginAddr;
	this.endAddr		= endAddr;
}
