var PLOT=1;
var ZOOM_IN=2;

var activePrintGraph=false;
var plotInProgress = false;
var actualMode=PLOT;
var GRAPH_STEP=7;

var ERROR_CODE=0;
var OK_CODE=1;

//Variabili utili per le frecce di scorrimento in fase di zoom-in
var zoomDeltaTime=0;
var startZoomInTime=0;
var endZoomInTime=0;

var autoscale=false;
var AUTOSCALE_TEXT = "autoscale";

var mainTime= null; //tempo utile per evitare il loop nel cambio ora solare ora legale 2 GTM+1 --> 2 GTM+2

var last_edited_row = -1;
var lastColor = ''; // Alessandro

var myPicker; // Alessandro : variable keeping the color picker

function initialize(){
	try {
		var flashObj = getFlashObject();
		if( flashObj ) {
			flashObj.SetVariable("_root.preLoadData.preLoadData.text",document.getElementById("waitPlotMsg").innerHTML);
			flashObj.setParameters("sysTimeLabelUser",document.getElementById("sysTimeLabelUser").innerHTML);
			flashObj.setParameters("sysTimeLabelSO",document.getElementById("sysTimeLabelSO").innerHTML);
			flashObj.setParameters("rangeMonthLabel",document.getElementById("rangeMonthLabel").innerHTML);
			flashObj.setParameters("rangeDaysLabel",document.getElementById("rangeDaysLabel").innerHTML);
			flashObj.setParameters("rangeHoursLabel",document.getElementById("rangeHoursLabel").innerHTML);
			flashObj.setParameters("rangeMinLabel",document.getElementById("rangeMinLabel").innerHTML);
			flashObj.setParameters("loadErrLabel",document.getElementById("loadErrLabel").innerHTML);
		}
		else {
			// adjust flot placeholders
			computeViewport();
			var divA = document.getElementById("placeholder_a");
			if( divA )
				divA.style.width = (viewportWidth - 40) + "px"; 
			var divD = document.getElementById("placeholder_d");
			if( divD )
				divD.style.width = (viewportWidth - 40) + "px";
		}
		
		var data = new Date();
		var sesMainTime = document.getElementById("sesMainTime").value;
		if (sesMainTime == null || sesMainTime == "") {
			data.setTime(document.getElementById("servermaintime").value);
			data.setTime(data.getTime()-document.getElementById("time"+document.getElementById("periodCode").innerHTML).innerHTML);
		}
		else {
			data.setTime(sesMainTime);
		}
		
		var sesTimePeriod = document.getElementById("sesTimePeriod").value;
		if (sesTimePeriod == null || sesTimePeriod == "") {
			sesTimePeriod = document.getElementById("periodCode").innerHTML;
			document.getElementById("timeperiod").selectedIndex = sesTimePeriod;
		}
		else {
			document.getElementById("timeperiod").selectedIndex = sesTimePeriod;
		}
		document.getElementById("year").value=data.getFullYear();
		document.getElementById("month").value=data.getMonth()+1;
		document.getElementById("day").value=data.getDate();
		document.getElementById("hour").value=data.getHours();
		document.getElementById("minut").value=data.getMinutes()-(data.getMinutes()%10);
		
		mainTime= new Date(data.getTime() -((data.getMinutes()%10)*1000*60)); // 10 minutes rounding
		mainTime.setSeconds(0);
		
		// we store the parameters "mainTime" and "TimePeriod", so we can use them in further transactions
		document.getElementById("sesMainTime").value = mainTime.getTime();
		document.getElementById("sesTimePeriod").value = sesTimePeriod;
		
		if(document.getElementById("deviceList").length == 1)
		{
			changeDevice(document.getElementById("deviceList").value);
		}//if
		
		if( objFlash && document.getElementById("automaticPlot").innerHTML == "true")
			communication(PLOT);
		
		if (document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0].rows.length > 0) {
			enableAction(1);
		}
		else {
			disableAction(1);
		}		
	} catch (e) {
	}
	
	try 
	{
		// Retrieve label for multi graph
		retrieveBlockLabel();
	}
	catch(e){}
	
}//initialize


function resetMainTime()
{
	var mTyear = document.getElementById("year").value;
	var mTmonth = document.getElementById("month").value;
	var mTday = document.getElementById("day").value;
	var mThour = document.getElementById("hour").value;
	var mTminuts = document.getElementById("minut").value;
	
	mainTime = new Date(mTyear, mTmonth - 1, mTday, mThour, mTminuts, 0);
	
}//resetMainTime


//ENHANCEMENT 20090212
function OpenGraphDialog()
{
	var jsessionid = document.getElementById("jsession").innerHTML;
	var dialogUrl = top.frames["manager"].getDocumentBase() + "mobile/GraphDialog.jsp;jsessionid=" + jsessionid;
	
	var destArgs = new Array();
	destArgs[0] = document.getElementById("listaDevices").innerHTML;
	destArgs[1] = document.getElementById("timeperiod").selectedIndex;
	destArgs[2] = document.getElementById("year").value;
	destArgs[3] = document.getElementById("month").value;
	destArgs[4] = document.getElementById("day").value;
	destArgs[5] = document.getElementById("hour").value;
	destArgs[6] = document.getElementById("minut").value;
	destArgs[7] = window.self;
	
	// x problema di dimensioni area visibile fra IE6 ed IE7:
	var dHeight = 360;
	var dWidth = 400;
	var version = 0;
	if (navigator.appVersion.indexOf("MSIE")!=-1)
	{
		dati = navigator.appVersion.split("MSIE");
		version = parseFloat(dati[1]);
	}
	
	if (version < 7.0) //NON IE browser will return 0
	{
		dWidth += 4; // left and right frame edge
		dHeight += 29 + 25 + 4; // title + status bar
	}
	

	var dialog = document.getElementById("GraphDialog");
	if( dialog ) {
		computeViewport();
		dialog.style.display = "block";
		dialog.style.left = (viewportWidth - dialog.clientWidth) / 2 + "px";
		dialog.style.top = (viewportHeight - dialog.clientHeight) / 2 + "px";
		doInit(destArgs);
	}
}


function onCloseGraphDialog(resArgs)
{
	var dialog = document.getElementById("GraphDialog");
	if( dialog )
		dialog.style.display = "none";
	
	if (resArgs != null)
	{
		document.getElementById("timeperiod").selectedIndex = Number(resArgs[1]);
		document.getElementById("year").value = Number(resArgs[2]);
		document.getElementById("month").value = Number(resArgs[3]);
		document.getElementById("day").value = Number(resArgs[4]);
		document.getElementById("hour").value = Number(resArgs[5]);
		document.getElementById("minut").value = Number(resArgs[6]);
		
		//se ho cambiato dev nella combo della win popup eseguo l'azione relativa nella win chiamante:
		if (document.getElementById("deviceList").selectedIndex != Number(resArgs[0]))
		{
			document.getElementById("deviceList").selectedIndex = Number(resArgs[0]);
			changeDevice(document.getElementById("deviceList").value);
		}
		//solo se ho selezionato un "timeperiod" eseguo aggiornamento gfx:
		if (document.getElementById("timeperiod").value != -1)
		{
			resetMainTime(); //forzo aggiornamento parametri
			// if we're in the Global view (group != null), we don't want to redraw the graph at this point.
			//if (document.getElementById("groupId").value == null || document.getElementById("groupId").value == "null") {
				communication(PLOT);//forzo ri-disegno autom. gfx
			//}
		}
	}
}


//ENHANCEMENT 20090211
function saveAndPlot()
{
	curveSelection(false);
	if (document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0].rows.length > 0) {
		// If there's a row in edit, we terminate the edit mode.
		// If there are any validation error, we can't plot so we exit.
		if (endEditRow(-2)) { // -2: the user clicked on the Plot button
			communication(PLOT);
			buildVariablesCheckBoxList(null);
		}
	}
	else {
		return;
	}
}

function communication(arg){

	var bodyTableInvisibleSx=document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0];
	if (bodyTableInvisibleSx == null || bodyTableInvisibleSx.rows.length == 0) {
		// there are no variables to plot. exit.
		return;
	}
	// A request is already being processed.
	if (plotInProgress) {
		return;
	}

	var flashObj=getFlashObject();
	
	switch(arg){
		case PLOT:
			activePrintGraph=true;
			// Print action enabled
			enableAction(2);
			prepareDataToPost();
			actualMode=PLOT;	
			if( flashObj ) {
				plotInProgress = true;
				flashObj.loadDati();
				flashObj.SetVariable("_root.preLoadData.preLoadData.text","");
				flashObj.SetVariable("actualPlotMode",actualMode);
			}
		break;
		case ZOOM_IN:
			if(prepareDataZoomInToPost()==0){
				return;
			}//if
			actualMode=ZOOM_IN;
			if( flashObj ) {
				plotInProgress = true;
				flashObj.loadDati();
				flashObj.SetVariable("_root.preLoadData.preLoadData.text","");
				flashObj.SetVariable("actualPlotMode",actualMode);
			}
			//enablePlot(false);
		break;
	}//switch
}//comunication


//ENHANCEMENT 20090212
function editRow(varId) {
	
	// if the selected row is not checked, we have to check it before editing
	var currChkBox = document.getElementById("chkbox"+varId);
	if (!currChkBox.getAttribute("checked")) {
		if(!addVariable(varId)) {
			// if the 20 variables limit has been reached,
			// we can't proceed any further. exit.
			return;
		}
		currChkBox.checked=true;
	}
	if (last_edited_row != varId) {
		last_edited_row = varId;
	}
	else {
		// row already in edit mode. Exit.
		return;
	}
	
	var rowInEdit = document.getElementById(varId); 
	var currEditCell = '';
	var oldValue = '';
	var oldColorValue = '';
	var inputType = 'text';
	
	// We may set min and max range only for analog devices
	var varType = rowInEdit.cells[11].innerHTML;
	if (varType == 1 || varType == 4) { // if the var is digital or an alarm:
		inputType="hidden";
	}

	currEditCell = rowInEdit.cells[8];
	oldValue = currEditCell.innerHTML;
	if (varType == 1 || varType == 4) {
		oldValue = '';
	}
	if (oldValue == AUTOSCALE_TEXT) {
		oldValue='';
	}		
	var virtualkeyboard = "";
	var virtkey = false;
	if (virtkey == "true")
	{
		virtualkeyboard = "class=\"keyboardInput\"";
	}
	rowInEdit.cells[8].innerHTML="<input type='"+inputType+"' "+virtualkeyboard+" value='" +oldValue+ "' size='6' maxlength='8' onkeydown='checkOnlyAnalog(this,event);'/>";

	currEditCell = rowInEdit.cells[9];
	oldValue = currEditCell.innerHTML;
	if (varType == 1 || varType == 4) {
		oldValue = '';
	}
	if (oldValue == AUTOSCALE_TEXT) {
		oldValue='';
	}		
	rowInEdit.cells[9].innerHTML="<input type='"+inputType+"' "+virtualkeyboard+" value='" +oldValue+ "' size='6' maxlength='8' onkeydown='checkOnlyAnalog(this,event);'/>";
	
	// color selection
	currEditCell = rowInEdit.cells[2];
		
	// this way we restore the previously saved color. 
	//alert('varId: '+varId); // Debug Alessandro
	if (document.getElementById('is'+varId)){
		//alert('Numero nodi: '+document.getElementById('is'+varId).childNodes.length); // Debug Alessandro
		oldColorValue = "#"+document.getElementById('is'+varId).childNodes[6].firstChild.nodeValue.toUpperCase();
	}
	
	currEditCell.innerHTML = document.getElementById("colorsSelectDiv").innerHTML;
	// bind color picker
	myPicker = new jscolor.color(document.getElementById('colorsSelect'), {required:false});
	myPicker.fromString(oldColorValue.slice(1));  // now you can access API via 'myPicker' variable
	myPicker.showPicker();
	/*
	// find the currently (old) selected color 
	var colorsSelect = document.getElementById("colorsSelect");
	var idxSelect = 0; // best to use an existing default, since there are customized colors in the DB
	for(var i = 0; i < colorsSelect.options.length; i++) {
		if (colorsSelect.options[i].value == oldColorValue) {
			//alert('colorsSelect.options[i].value: '+colorsSelect.options[i].value); // Debug Alessandro
			idxSelect=new Number(i);
			break;
		}
	}	
	colorsSelect.selectedIndex = idxSelect;
	*/
	
	// Alessandro : buildKeyboardInputs method must be called only when Virtual Keyboard is enabled otherwise IE returns a javascript error
	if (virtkey == "true") buildKeyboardInputs();
}

//ENHANCEMENT 20090212
function endEditRow(varId) {
	
	// Yes, I could have written the following 2 "if" clauses using a single if (clause1 || clause2). But this
	// way it's much neater.
	
	if (last_edited_row == -1) {
		// nothing to do. exit
		return true;
	}
	
	if (last_edited_row == varId && document.getElementById("chkbox"+varId).getAttribute("checked")) {
		// the user selected with a single click the row that was already in edit mode.
		// nothing to do. exit
		return true;
	}

	//hide the color picker
	if (myPicker) myPicker.hidePicker();
	
	// the user edited a row (not the currently selected one),
	// and then clicked on another row (the one identified by varId)
	// So, now we should end the editing mode and copy the data in
	// tableInvisibleSx, then re-set the input cells, last_edited_row, colours, etc.

	var yLowerLimit = document.getElementById(last_edited_row).cells[8];
	var yUpperLimit = document.getElementById(last_edited_row).cells[9];
	
	if (yLowerLimit.firstChild != null && yUpperLimit.firstChild != null) {
		// Fields validation
		var returnCode = controlMinMaxValue(yLowerLimit.firstChild.value, yUpperLimit.firstChild.value);
		if (returnCode != OK_CODE) {
			alert(document.getElementById("dataerrormsg").innerHTML);
			return false;
		}
		
		yLowerLimit.innerHTML = yLowerLimit.firstChild.value;
		yUpperLimit.innerHTML = yUpperLimit.firstChild.value;
		
		// Stores the new values in the SX table
		document.getElementById("is"+last_edited_row).cells[7].innerHTML = yLowerLimit.innerHTML;
		document.getElementById("is"+last_edited_row).cells[8].innerHTML = yUpperLimit.innerHTML;
		if (yLowerLimit.innerHTML == '' && (document.getElementById(last_edited_row).cells[11].firstChild.nodeValue != 1 
				&& document.getElementById(last_edited_row).cells[11].firstChild.nodeValue != 4)) {
			// if the upper and lower limits are '', we set "autoscale" as text, but only for analog and int vars
			yLowerLimit.innerHTML = AUTOSCALE_TEXT;
			yUpperLimit.innerHTML = AUTOSCALE_TEXT;
		}
	}

	var newColor = document.getElementById(last_edited_row).cells[2].getElementsByTagName("input")[0].value;
	lastColor = newColor;
	document.getElementById(last_edited_row).cells[2].innerHTML="<table border='1' cellspacing='0' cellpadding='0' style='border-color:black;height:14px;width:100%;'><tr><td style='height:40%;background-color:#"+newColor+";'></td></tr></table>";
	
	/* Alessandro */
	var dxRows = document.getElementById("invisibleTableDx").getElementsByTagName("tr");
	for (var h=0; h < dxRows.length; h++) {
		var currRow = dxRows[h];
		if (currRow.cells[0].firstChild.nodeValue == last_edited_row){ 
			currRow.cells[4].firstChild.nodeValue = lastColor;
			break;
		}
	}
	/* fine Alessandro */
	
	try {
		document.getElementById("is"+last_edited_row).cells[6].innerHTML = newColor;
	}
	catch (err) {
	}
	// ends the edit mode
	last_edited_row = -1;
	unlockModUser();
	return true;
}

//ENHANCEMENT 20090213
function dumpSXTable() {
	if (document.getElementById("invisibleTableSx") != null) {
		var bodyTableInvisibleSx=document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0];
		for(var i=0;i<bodyTableInvisibleSx.rows.length;i++){
			 var variableList=bodyTableInvisibleSx.rows[i].cells[2].firstChild.nodeValue;//idVar
			variableList+=";";
			variableList+=encodeURIComponent(bodyTableInvisibleSx.rows[i].cells[3].firstChild.nodeValue);//Desc
			//variableList+=bodyTableInvisibleSx.rows[i].cells[3].firstChild.nodeValue;//Desc
			variableList+=";";
			variableList+=bodyTableInvisibleSx.rows[i].cells[4].firstChild.nodeValue;//Type
			variableList+=";";
			variableList+=cleanUnitMeasurement(bodyTableInvisibleSx.rows[i].cells[5].firstChild==null?" ":escape(bodyTableInvisibleSx.rows[i].cells[5].firstChild.nodeValue));//unit measure
			variableList+=";";
			variableList+=bodyTableInvisibleSx.rows[i].cells[6].firstChild.nodeValue;//Color
			variableList+=";\n";
		}
		alert(variableList);
		
	}
	else {
		alert("Table is null.");
	}
}

//ENHANCEMENT 20090212
function toggleVariable(varId){
	if (last_edited_row == varId && !document.getElementById("chkbox"+varId).getAttribute("checked")) {
		// the use un-selected the current row while it's in still in edit mode. We re-check the row and exit.
		document.getElementById("chkbox"+varId).checked=true;
		return;
	}
	markTableRow(varId);
	if (document.getElementById("chkbox"+varId).checked) {
		addVariable(varId);
	}
	else {
		deleteRow(varId);
	}
	unlockModUser();
}

//ENHANCEMENT 20090212
function addVariable(varId){
	enableAction(1);
	var bodyTableDx=document.getElementById("idTableVariableDx").getElementsByTagName("tbody")[0];
	var bodyTableInvisibleSx=document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0];

	//check max Rows
	if(bodyTableInvisibleSx.rows.length==20){
		alert(document.getElementById("variableMaxMsg").innerHTML);
		// un-check the checkbox.
		document.getElementById("chkbox"+varId).checked=false;
		return false;		
	}//if
	
	//check duplicate row
	if (document.getElementById("is"+varId) != null) {
		// no need to add the variable since it's already in.
		//alert('Variabile gi� presente'); // Debug Alessandro
		return true;		
	}
	
	for(var i=0;i<bodyTableInvisibleSx.rows.length;i++){
		if(bodyTableInvisibleSx.rows[i].cells[2].firstChild.nodeValue==varId){
			alert(document.getElementById("variablePresentMsg").innerHTML);
		}//if
	}//for	
	
	var deviceId =  document.getElementById(varId).cells[10].firstChild.nodeValue;
	var deviceDescr = document.getElementById(varId).cells[1].firstChild.nodeValue;
	var varDesc = document.getElementById(varId).cells[3].firstChild.nodeValue;
	var type = document.getElementById(varId).cells[11].firstChild.nodeValue;
	var um = document.getElementById(varId).cells[4].firstChild==null ? " " : document.getElementById(varId).cells[4].firstChild.nodeValue;//unit of measure
	var color = document.getElementById(varId).cells[12].firstChild.nodeValue;
	var yLoweLimit = document.getElementById(varId).cells[8].firstChild == null ? "" : document.getElementById(varId).cells[8].firstChild.nodeValue;
	var yUpperLimit = document.getElementById(varId).cells[9].firstChild == null ? "" : document.getElementById(varId).cells[9].firstChild.nodeValue;
	
	var invisibleRowHTML="<tr id='is" +varId+ "'>"+
				"<td >" +deviceDescr+ "</td>"+
				"<td >" +deviceId+ "</td>"+ 
				"<td >" +varId+ "</td>"+
				"<td >" +varDesc+ "</td>"+
				"<td >" +type+ "</td>"+
				"<td >" +um+ "</td>"+
				"<td >" +color+ "</td>"+
				"<td >" +yLoweLimit+ "</td>"+
				"<td >" +yUpperLimit+ "</td>"+
				"</tr>";
				
	//search proper position  
	var splitRow=-1;
	var sxInvisibleTableTmp="";

	if(type!=1){
		for(var j=0;j<bodyTableInvisibleSx.rows.length;j++){
			var currtype=bodyTableInvisibleSx.rows[j].cells[4].firstChild.nodeValue;
			if(currtype == 1){
				splitRow=j;
				break;
			}//if
		}//for	
	}//if
	
	for(var ii=0;ii<bodyTableInvisibleSx.rows.length;ii++){
		var idSx=bodyTableInvisibleSx.rows[ii].cells[2].firstChild.nodeValue;
		var colorSx=bodyTableInvisibleSx.rows[ii].cells[6].firstChild.nodeValue;
		var desVarSx=bodyTableInvisibleSx.rows[ii].cells[3].firstChild.nodeValue;
		var desDevSx=bodyTableInvisibleSx.rows[ii].cells[0].firstChild.nodeValue;
		// insert new row in the splitRow-th position in both the tables
		if(splitRow == ii){
			sxInvisibleTableTmp+=invisibleRowHTML;
		}//if
		// the other rows are simply copied out

		sxInvisibleTableTmp+="<tr id='is"+idSx+"'>"+bodyTableInvisibleSx.rows[ii].innerHTML+"</tr>";
	}//for
	// insert the new row at the end of both the tables
	if(splitRow == -1){
		sxInvisibleTableTmp+=invisibleRowHTML;
	}//if
	document.getElementById("invisibleTableSx").innerHTML="<table class='table' width=\"100%\"><tbody>"+sxInvisibleTableTmp+"</tbody></table>";

	return true;
}//addVariable

//ENHANCEMENT 20090211
function deleteRow(varId){
	var bodyTableInvisibleSx=document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0];

	document.getElementById("is"+varId).parentNode.removeChild(document.getElementById("is"+varId));
	document.getElementById("chkbox"+varId).checked=false;

	var flashObj=getFlashObject();
	if( flashObj )
		flashObj.deleteCurve(varId);
	
	if (document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0].rows.length == 0) {
		disableAction(1);
	}
}//deleteRow

function changeDevice(deviceId){
	last_edited_row = -1;
	if(deviceId!=0){
		var ajax=getXMLHttpRequest();
		var path=document.getElementById("basePath").innerHTML+"servlet/MasterGraph;jsessionid="+document.getElementById("jsession").innerHTML;
		ajax.open("post",path,"true");
		ajax.setRequestHeader("content-type", "application/x-www-form-urlencoded");
		ajax.send("deviceList="+escape(deviceId)+"&actionType="+escape(1)+"&typeGraph="+
				  escape(document.getElementById("typeGraph").innerHTML));
		
		ajax.onreadystatechange = function(){
			if(ajax.readyState == 4) { //4=Loaded 
				if(ajax.status == 200){
			     	buildVariablesCheckBoxList(ajax);
		     		communication(PLOT); // moved from initialize to make sure the curve data arrays are loaded 
			     }
				else
					alert("Communication error number: " + ajax.status);
			}//if
		}//onreadystatechange
		
	}//if 	ajax.setRequestHeader("connection", "close");
	else
		document.getElementById("selectDeviceResponse").innerHTML ="<div id=\"idTableVariableDx\" style=\"display:none;\"><table><tbody></tbody></table></div><div id=\"invisibleTableDx\" style=\"display:none;\"><table><tbody></tbody></table>";
}//changeDevice

//ENHANCEMENT 20090210
function buildVariablesCheckBoxList(ajax) {
	var tableVariableDXRows = "";
	// invisible table for storing the current device variables (both selected and unselected)
	var tableInvisibleDXRows = "";	
	var currDeviceDescr=document.getElementById("deviceList").options[document.getElementById("deviceList").selectedIndex].innerHTML;
	var currDeviceId = document.getElementById("deviceList").value;
	
	// Variables table header
	tableVariableDXRows += "";
	
	// canvas
	aobjAnalogCurves = new Array();
	aobjDigitalCurves = new Array();
	// puts the already stored variables (from invisible SX table) at the top of the table
	var bodyTableInvisibleSx=document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0];
	for(var i=0;i<bodyTableInvisibleSx.rows.length;i++){
		var currSxRow = bodyTableInvisibleSx.rows[i];	
		var sxDeviceId = currSxRow.cells[1].firstChild.nodeValue;
		var sxDeviceDescr = currSxRow.cells[0].firstChild.nodeValue;
		var varId = currSxRow.cells[2].firstChild.nodeValue;
		var varDescr = null;
		if(currSxRow.cells[3].firstChild != null)
		{
			varDescr = currSxRow.cells[3].firstChild.nodeValue;
		}
		var type = currSxRow.cells[4].firstChild.nodeValue;
		var um = bodyTableInvisibleSx.rows[i].cells[5].firstChild==null?" ":bodyTableInvisibleSx.rows[i].cells[5].firstChild.nodeValue;
		var colour = currSxRow.cells[6].firstChild.nodeValue;
		var yLowerLimit = currSxRow.cells[7].firstChild == null ? "" : currSxRow.cells[7].firstChild.nodeValue; // lower limit
		var yUpperLimit = currSxRow.cells[8].firstChild == null ? "" : currSxRow.cells[8].firstChild.nodeValue; // upper limit		
		
		tableVariableDXRows += buildMergedRow(true, sxDeviceId, sxDeviceDescr, varId, varDescr, type, um, colour, yLowerLimit, yUpperLimit);
		// canvas
		var objCurve = new Object();
		objCurve.DeviceId = sxDeviceId;
		objCurve.DeviceDescr = sxDeviceDescr;
		objCurve.VarId = varId;
		objCurve.VarDescr = varDescr;
		objCurve.Type = type;
		objCurve.UM = um;
		objCurve.Color = colour;
		objCurve.LowerLimit = yLowerLimit;
		objCurve.UpperLimit = yUpperLimit;
		if( objCurve.Type == 1 )
			aobjDigitalCurves.push(objCurve);
		else
			aobjAnalogCurves.push(objCurve);
	}//for
	
	// we received a new list of variables for a newly chosen device.
	// Now we parse the device's variables, store them in the invisible DX table and
	// append them at the bottom of the visible list
	if (ajax != null) {
		// current device variables (DX)
		var xmlRows = ajax.responseXML.getElementsByTagName('r');
		for (var i=0; i < xmlRows.length; i++) {
			var currRow = xmlRows[i];
			var varId = currRow.childNodes[0].childNodes[0].nodeValue;
			var varDescr = currRow.childNodes[1].childNodes[0].nodeValue;
			var type = currRow.childNodes[2].childNodes[0].nodeValue;
			var um = currRow.childNodes[3].childNodes[0] ? currRow.childNodes[3].childNodes[0].nodeValue : "";
			var colour = currRow.childNodes[4].childNodes[0].nodeValue;
			var yLowerLimit = currRow.childNodes[5].childNodes[0].nodeValue; // lower limit
			var yUpperLimit = currRow.childNodes[6].childNodes[0].nodeValue; // upper limit
			
			// if the variable isn't already in the SX table, we put it in the DX.
			// we append the unselected variables after the already plotted ones.
			if (document.getElementById("is"+varId) == null) {
				tableVariableDXRows += buildMergedRow(false, currDeviceId, currDeviceDescr, varId, varDescr, type, um, colour, yLowerLimit, yUpperLimit);
			}
			
			var invisibleTableDxRow = "<tr>";
					invisibleTableDxRow += "<td>" +varId+ "</td>";
					invisibleTableDxRow += "<td>" +varDescr+ "</td>";
					invisibleTableDxRow += "<td>" +type+ "</td>";
					invisibleTableDxRow += "<td>" +um+ "</td>";
					invisibleTableDxRow += "<td>" +colour+ "</td>";
					invisibleTableDxRow += "<td>" +yLowerLimit+ "</td>";
					invisibleTableDxRow += "<td>" +yUpperLimit+ "</td>";
				invisibleTableDxRow += "</tr>";
	
			// new row in the invisible Dx table
			tableInvisibleDXRows += invisibleTableDxRow;	
		}
	}	
	else {	
		// appends the current device unselected variables after a plot (no new device chosen)
		var dxRows = document.getElementById("invisibleTableDx").getElementsByTagName("tr");
		for (var i=0; i < dxRows.length; i++) {
			var currRow = dxRows[i];
			var varId = currRow.cells[0].firstChild.nodeValue;
			var varDescr = currRow.cells[1].firstChild.nodeValue;
			var type = currRow.cells[2].firstChild.nodeValue;
			var um = currRow.cells[3].firstChild != null ? currRow.cells[3].firstChild.nodeValue : "";
			var colour = currRow.cells[4].firstChild.nodeValue;
			var yLowerLimit = currRow.cells[5].firstChild != null ? currRow.cells[5].firstChild.nodeValue : ""; // lower limit
			var yUpperLimit = currRow.cells[6].firstChild != null ? currRow.cells[6].firstChild.nodeValue : ""; // upper limit
			
			// if the variable isn't already in the SX table, we put it in the DX.
			// we append the unselected variables after the already plotted ones.
			if (document.getElementById("is"+varId) == null) {
				tableVariableDXRows += buildMergedRow(false, currDeviceId, currDeviceDescr, varId, varDescr, type, um, colour, yLowerLimit, yUpperLimit);
			}	
		}//for
	}//else	
	
	// visible variable table header
	var tableVariableDxHTML = 
		"<div id='device'>" +
			"<div style='width:100%;background-color:#FFFFFF;'>" +
				"<div id=\"idTableVariableDx\" style=\"width:100%;overflow:auto;\">" +
					"<table cellspacing='1' cellpadding='0' align='left'>" +
							"<tbody>";
	tableVariableDxHTML += tableVariableDXRows;
	// visible table footer
	tableVariableDxHTML += "</tbody>" +
	    			"</table>" +
	    		"</div>";
	
	// If we received new device variables data, the invisible dx table is overwritten
	var tableInvisibleDxHTML = "";
	if (ajax == null) {
		tableInvisibleDXRows = document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0].innerHTML;
	}

	// invisible Dx table
	tableInvisibleDxHTML = "<div id='data' style='display:none;'>" +
						       "<div id='invisibleTableDx'>" +
							       "<table>" +
								       "<tbody>";	
	tableInvisibleDxHTML += tableInvisibleDXRows;
	// invisible table footer
	tableInvisibleDxHTML += 		   "</tbody>"+
								   "</table>" +
							   "</div>" +
						   "</div>";
					   
	var footer = "</div></div>";

    document.getElementById("selectDeviceResponse").innerHTML = tableVariableDxHTML + tableInvisibleDxHTML + footer;
	return true;
}

//ENHANCEMENT 20090215
function buildMergedRow (checked, devId, devDescr, varId, varDescr, type, um, colour, yLowerLimit, yUpperLimit) {
	var row = "";
	row += "<tr height='35px' id=" + varId + " title='" + document.getElementById("dblclickLabel").innerHTML + "' class=\"graphVarTableRow\" onclick=\"evidenceCurve('" +varId+ "')\" ondblclick=\"editRow('"+varId+"');\" >";
	row += 	"<td class='td' style='width:60px'><input class='bigcheck' id ='chkbox" + varId +"' type='checkbox' " +(checked?"checked":"")+ " onclick=\"toggleVariable('"+varId+"')\" /></td>";
	row += 	"<td class='td' style='width:150px'>" +devDescr+ "</td>";
	row +=	"<td class='td' style='width:70px'><table border='1' cellspacing='0' cellpadding='0' style='border-color:black;height:14px;width:100%;'><tr><td style='height:40%;background-color:#"+colour+"'></td></tr></table></td>";
	row +=	"<td class='td' style='width:310px'>" +varDescr+ "</td>";
	row +=	"<td align='center' class='td' style='width:70px'>" +um+ "</td>"; // unit of measure
	row +=	"<td align='center' class='td' style='width:128px'>--</td>"; // y min.
	row +=	"<td align='center' class='td' style='width:128px'>--</td>"; // y max.
	row +=	"<td align='center' class='td' style='width:70px'>--</td>"; // average value
	
	if (yLowerLimit == yUpperLimit) {
		if (type != 1 && type != 4) {
			yLowerLimit = AUTOSCALE_TEXT;
			yUpperLimit = AUTOSCALE_TEXT;
		}
		else {
			yLowerLimit = '';
			yUpperLimit = '';
		}
	}
	
	row +=	"<td align='center' class='td' style='width:100px'>" +yLowerLimit+ "</td>";
	row +=	"<td align='center' class='td' style='width:100px'>" +yUpperLimit+ "</td>";
	row +=	"<td style='display:none'>" +devId+ "</td>"; // hidden
	row +=	"<td style='display:none'>" +type+ "</td>"; // hidden
	row +=	"<td style='display:none'>" +colour+ "</td>"; // hidden
	row += "</tr>";	
	return row;
}

function prepareDataToPost(){
	
	var bodyTableInvisibleSx=document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0];
	
	var variableList=""; 
	var saveData = "";
	
	var anaNum = 0;
	var digNum = 0;
	
	for(var i=0;i<bodyTableInvisibleSx.rows.length;i++){
		// we have to count the digital variables number
		if (bodyTableInvisibleSx.rows[i].cells[4].firstChild.nodeValue == 1 || bodyTableInvisibleSx.rows[i].cells[4].firstChild.nodeValue == 4) {
			digNum++;
		}
		else {
			anaNum++;
		}
		
		saveData += bodyTableInvisibleSx.rows[i].cells[2].firstChild.nodeValue;//idVar
		saveData += ";";
		
		variableList+=bodyTableInvisibleSx.rows[i].cells[2].firstChild.nodeValue;//idVar
		variableList+=";";
		if(bodyTableInvisibleSx.rows[i].cells[3].firstChild != null)
		{
			variableList+=bodyTableInvisibleSx.rows[i].cells[3].firstChild.nodeValue;//Desc
		}
		else
		{
			variableList+="";
		}
		variableList+=";";
		variableList+=bodyTableInvisibleSx.rows[i].cells[4].firstChild.nodeValue;//Type
		variableList+=";";
		variableList+=cleanUnitMeasurement(bodyTableInvisibleSx.rows[i].cells[5].firstChild==null?" ":escape(bodyTableInvisibleSx.rows[i].cells[5].firstChild.nodeValue));//unit measure
		variableList+=";";
		variableList+=bodyTableInvisibleSx.rows[i].cells[6].firstChild.nodeValue;//Color
		variableList+=";";
		variableList+=encodeURIComponent(bodyTableInvisibleSx.rows[i].cells[0].firstChild.nodeValue);//Descrizione Device
		//variableList+=bodyTableInvisibleSx.rows[i].cells[0].firstChild.nodeValue;//Descrizione Device 
		variableList+=";";
		if(bodyTableInvisibleSx.rows[i].cells[7].innerHTML == null 
				|| isNaN(bodyTableInvisibleSx.rows[i].cells[7].innerHTML)
				|| (bodyTableInvisibleSx.rows[i].cells[7].innerHTML == ''				
					&& bodyTableInvisibleSx.rows[i].cells[7].firstChild==bodyTableInvisibleSx.rows[i].cells[8].firstChild))
			variableList+="null;null";
		else
			variableList+=Number(bodyTableInvisibleSx.rows[i].cells[7].firstChild.nodeValue)+";"+Number(bodyTableInvisibleSx.rows[i].cells[8].firstChild.nodeValue); //ymin ymax
		variableList+=";";
	}//for

	var flashObj = getFlashObject();
	if( flashObj ) {
		flashObj.setParameters("srvSource",document.getElementById("basePath").innerHTML +"servlet/MasterGraph;jsessionid="+ document.getElementById("jsession").innerHTML);
		flashObj.setParameters("actionType","2");
		flashObj.setParameters("variableList",variableList);
		flashObj.setParameters("timeperiod",document.getElementById("timeperiod").value);
		flashObj.setParameters("mainTime",mainTime.getTime());//Add
		flashObj.setParameters("typeGraph",document.getElementById("typeGraph").innerHTML);
		flashObj.setParameters("output","");
		flashObj.setParameters("deviceLabel",document.getElementById("deviceLabel").innerHTML);	
		flashObj.setParameters("printDataLabel",document.getElementById("printDataLabel").innerHTML);	
		flashObj.setParameters("userDataLabel",document.getElementById("userDataLabel").innerHTML);	
		flashObj.setParameters("userName",document.getElementById("userName").innerHTML);	
		flashObj.setParameters("startDateLabel",document.getElementById("startDateLabel").innerHTML);	
		flashObj.setParameters("rangeLabel",document.getElementById("rangeLabel").innerHTML);	
		flashObj.setParameters("nodeName",document.getElementById("nodeName").innerHTML);	
		flashObj.setParameters("autoscale",autoscale);
		
		// ENHANCEMENT 20090213
		// semicolon separated list of varIds to save. Flash call to server should ship this param only with the first curve request,
		// in order to avoid redundant config updates.
		flashObj.setParameters("save_data",saveData);
		
		// ENHANCEMENT 20090218 New parameters "anaNum" and "digNum". Using the latter params,
		// the flash application is aware of much space it has to reserve for the digital variables drawing.
		flashObj.setParameters("anaNum", anaNum); // # analogical variables (future use)
		flashObj.setParameters("digNum", digNum); // # digital variables
	
		if (document.getElementById("combo_dev"))
			flashObj.setParameters("deviceList",document.getElementById("combo_dev").value);
	}
	else {
		var objParams = new Object();
		objParams.communication = PLOT;
		objParams.srvSource = document.getElementById("basePath").innerHTML +"servlet/MasterGraph;jsessionid="+ document.getElementById("jsession").innerHTML;
		objParams.actionType = "2";
		objParams.variableList = variableList;
		objParams.timeperiod = parseInt(document.getElementById("timeperiod").value);
		objParams.mainTime = mainTime.getTime();
		objParams.typeGraph = document.getElementById("typeGraph").innerHTML;
		objParams.deviceLabel = document.getElementById("deviceLabel").innerHTML;	
		objParams.printDataLabel = document.getElementById("printDataLabel").innerHTML;	
		objParams.userDataLabel = document.getElementById("userDataLabel").innerHTML;	
		objParams.userName = document.getElementById("userName").innerHTML;	
		objParams.startDateLabel = document.getElementById("startDateLabel").innerHTML;	
		objParams.rangeLabel = document.getElementById("rangeLabel").innerHTML;	
		objParams.nodeName = document.getElementById("nodeName").innerHTML;	
		objParams.autoscale = autoscale;
		objParams.save_data = saveData;
		objParams.anaNum = anaNum;
		objParams.digNum = digNum;
		objParams.xWidth = parseInt(document.getElementById("xWidth").innerHTML);
		loadData(objParams);
	}

}//prepareDataToPost


function prepareDataZoomInToPost(){
	
	var bodyTableInvisibleSx=document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0];
	
	var variableList="";
	var saveData = "";
	
	var anaNum = 0;
	var digNum = 0;
	for(var i=0;i<bodyTableInvisibleSx.rows.length;i++){
		// we have to count the digital variables number
		if (bodyTableInvisibleSx.rows[i].cells[4].firstChild.nodeValue == 1 || bodyTableInvisibleSx.rows[i].cells[4].firstChild.nodeValue == 4) {
			digNum++;
		}
		else {
			anaNum++;
		}		
		saveData += bodyTableInvisibleSx.rows[i].cells[2].firstChild.nodeValue;//idVar
		saveData += ";";
		
		variableList+=bodyTableInvisibleSx.rows[i].cells[2].firstChild.nodeValue;//idVar
		variableList+=";";
//		variableList+=encodeURIComponent(bodyTableInvisibleSx.rows[i].cells[3].firstChild.nodeValue);//Desc
		variableList+=bodyTableInvisibleSx.rows[i].cells[3].firstChild.nodeValue;//Desc
		variableList+=";";
		variableList+=bodyTableInvisibleSx.rows[i].cells[4].firstChild.nodeValue;//Type
		variableList+=";";
		variableList+=cleanUnitMeasurement(bodyTableInvisibleSx.rows[i].cells[5].firstChild==null?" ":escape(bodyTableInvisibleSx.rows[i].cells[5].firstChild.nodeValue));//unit measure
		variableList+=";";
		variableList+=bodyTableInvisibleSx.rows[i].cells[6].firstChild.nodeValue;//Color
		variableList+=";";
		variableList+=encodeURIComponent(bodyTableInvisibleSx.rows[i].cells[0].firstChild.nodeValue);//Descrizione Device
		//variableList+=bodyTableInvisibleSx.rows[i].cells[0].firstChild.nodeValue;//Descrizione Device 
		variableList+=";";
		
		if(bodyTableInvisibleSx.rows[i].cells[7].innerHTML == null 
				|| isNaN(bodyTableInvisibleSx.rows[i].cells[7].innerHTML)
				|| (bodyTableInvisibleSx.rows[i].cells[7].innerHTML == ''				
					&& bodyTableInvisibleSx.rows[i].cells[7].firstChild==bodyTableInvisibleSx.rows[i].cells[8].firstChild))
			variableList+="null;null";
		else
			variableList+=Number(bodyTableInvisibleSx.rows[i].cells[7].firstChild.nodeValue)+";"+Number(bodyTableInvisibleSx.rows[i].cells[8].firstChild.nodeValue); //ymin ymax
		variableList+=";";
		
	}//for
	
	var flashObj=getFlashObject();
	if( flashObj ) {
		var correctTimeZoom=flashObj.GetVariable("correctTimeZoom");
		dateStartTime=new Date(new Number(flashObj.GetVariable("timeFrom")));
		dateEndTime=new Date(new Number(flashObj.GetVariable("timeTo")));
			
		var deltaTime=new Number((dateEndTime.getTime()-dateStartTime.getTime())/1000);
		zoomDeltaTime=new Number(dateEndTime.getTime()-dateStartTime.getTime());
		startZoomInTime=dateStartTime.getTime();
		endZoomInTime=dateEndTime.getTime();
		
		// Dopesn't zoom in if the zoom time-window is < 60 sec.
		if(deltaTime<=60)
			return 0;
		for(;;){
			if((deltaTime>60)&&(deltaTime<=86400)){
				flashObj.setParameters("timeperiod","-1");		
				break;
			}//if
			if((deltaTime>86400)&&(deltaTime<=1209600)){
				flashObj.setParameters("timeperiod","-2");		
				break;
			}//if
			if(deltaTime>1209600){
				flashObj.setParameters("timeperiod","-3");		
				break;
			}//if
		break;
		}//forswitch
		flashObj.setParameters("srvSource",document.getElementById("basePath").innerHTML +"servlet/MasterGraph;jsessionid="+ document.getElementById("jsession").innerHTML);
		flashObj.setParameters("actionType","2");
		flashObj.setParameters("variableList",variableList);
		
		//Zoom In Parameter
		flashObj.setParameters("startXZoomIn",dateStartTime.getTime());
		flashObj.setParameters("endXZoomIn",dateEndTime.getTime());
		//Zoom In Parameter
		
		flashObj.setParameters("typeGraph",document.getElementById("typeGraph").innerHTML);
		flashObj.setParameters("output","");
		flashObj.setParameters("deviceLabel",document.getElementById("deviceLabel").innerHTML);	
		flashObj.setParameters("printDataLabel",document.getElementById("printDataLabel").innerHTML);	
		flashObj.setParameters("userDataLabel",document.getElementById("userDataLabel").innerHTML);	
		flashObj.setParameters("userName",document.getElementById("userName").innerHTML);	
		flashObj.setParameters("startDateLabel",document.getElementById("startDateLabel").innerHTML);	
		flashObj.setParameters("rangeLabel",document.getElementById("rangeLabel").innerHTML);	
		flashObj.setParameters("nodeName",document.getElementById("nodeName").innerHTML);	
		flashObj.setParameters("autoscale",autoscale);
		// ENHANCEMENT 20090213
		// semicolon separated list of varIds to save. Flash call to server should ship this param only with the first curve request,
		// in order to avoid redundant config updates.
		flashObj.setParameters("save_data", saveData);	
		
		// ENHANCEMENT 20090218 New parameters "anaNum" and "digNum". Using the latter params,
		// the flash application is aware of much space it has to reserve for the digital variables drawing.
		flashObj.setParameters("anaNum", anaNum); // # analogical variables (future use)
		flashObj.setParameters("digNum", digNum); // # digital variables
	}
	else {
		var objParams = new Object();
		objParams.communication = ZOOM_IN;		
		var dateStartTime=new Date(gobjParams.timeFrom);
		var dateEndTime=new Date(gobjParams.timeTo);
		var deltaTime=new Number((dateEndTime.getTime()-dateStartTime.getTime())/1000);
		zoomDeltaTime=new Number(dateEndTime.getTime()-dateStartTime.getTime());
		
		if( deltaTime<=60 )
			return 0;
		else if( deltaTime>60 && deltaTime<=86400 )
			objParams.timeperiod = "-1";
		else if( deltaTime>86400 && deltaTime<=1209600 )
			objParams.timeperiod = "-2";
		else
			objParams.timeperiod = "-3";		

		objParams.srvSource = document.getElementById("basePath").innerHTML +"servlet/MasterGraph;jsessionid="+ document.getElementById("jsession").innerHTML;
		objParams.actionType = "2";
		objParams.variableList = variableList;
		objParams.mainTime = mainTime.getTime();		
		objParams.startXZoomIn = gobjParams.timeFrom;
		objParams.endXZoomIn = gobjParams.timeTo;
		objParams.typeGraph = document.getElementById("typeGraph").innerHTML;
		objParams.deviceLabel = document.getElementById("deviceLabel").innerHTML;	
		objParams.printDataLabel = document.getElementById("printDataLabel").innerHTML;	
		objParams.userDataLabel = document.getElementById("userDataLabel").innerHTML;	
		objParams.userName = document.getElementById("userName").innerHTML;	
		objParams.startDateLabel = document.getElementById("startDateLabel").innerHTML;	
		objParams.rangeLabel = document.getElementById("rangeLabel").innerHTML;	
		objParams.nodeName = document.getElementById("nodeName").innerHTML;	
		objParams.autoscale = autoscale;
		objParams.save_data = saveData;
		objParams.anaNum = anaNum;
		objParams.digNum = digNum;
		objParams.xWidth = parseInt(document.getElementById("xWidth").innerHTML);
		loadData(objParams);
	}
	return 1;
}//prepareDataZoomInToPost

function getFlashObject(){
	  var IE = navigator.appName.indexOf("Microsoft") != -1;
	  try {
		  return IE ? flashObj : document.flashObj;		
	} catch (e) {		
		return null;
	}
}//getFlashObject

// ENHANCEMENT 20090211
// opens a popup window for setting general graph properties: grids, axis colors, ViewFinder properties, etc.
//function openGraphLayoutConfig(graphType) {
//	var jsessionid = document.getElementById("jsession").innerHTML;
//	var dialogUrl = top.frames["manager"].getDocumentBase() + "app/dtlview/GraphLayoutConfigDialog.jsp;jsessionid=" + jsessionid;
//	
//	var destArgs = new Array();
//	destArgs[0] = graphType;
//	
//	// x problema di dimensioni area visibile fra IE6 ed IE7:
//	var dHeight = 260;
//	var dWidth = 850;
//	var version = 0;
//	if (navigator.appVersion.indexOf("MSIE")!=-1)
//	{
//		dati = navigator.appVersion.split("MSIE");
//		version = parseFloat(dati[1]);
//	}
//	
//	if (version < 7.0) //NON IE browser will return 0
//	{
//		dWidth += 4; // left and right frame edge
//		dHeight += 29 + 25 + 4; // title + status bar
//	}
//	
//	var winSettings = "dialogWidth:"+dWidth+"px;dialogHeight:"+dHeight+"px;dialogTop:100px;dialogLeft:100px;scroll:no;border:thick;help:off;status:off;resizable:on;";
//	var resArgs = new Array();
//	resArgs = window.showModalDialog(dialogUrl, destArgs, winSettings);
//		
//	// output management
//	if (resArgs != null)
//	{
//		document.getElementById("cosmeticGraphInformation").value = resArgs;
//		
//		var ofrm = document.getElementById("frmCosmeticGraph");
//		MTstartServerComm();
//		ofrm.submit();
////		communication(PLOT);//forzo ri-disegno autom. gfx 
//	}
//}	


// show print properties
function printGraphFlash(){
	var win = document.getElementById("divPrint");
	win.style.display = "block";
	win.style.left = (document.body.clientWidth - win.clientWidth) / 2;
	win.style.top = (document.body.clientHeight - win.clientHeight) / 2;
}//printGraphFlash


// request flash print
function onPrintGraph()
{
	if(!activePrintGraph)
		return;

	var bGrid = document.getElementById("ppGrid").checked;
	var bMMA = document.getElementById("ppMMA").checked;

	var flashObj=getFlashObject();
	if( flashObj )
		flashObj.printGraphProp(bGrid, bMMA);
}


function nextPeriod(){
//	if(activePrintGraph==false)
//		return;
	if(actualMode<0){
		nextTimePeriodZoomIn(1);
		return;
	}//if
	if(actualMode==ZOOM_IN){
		nextTimePeriodZoomIn(1);
		return;
	}//if
	if(actualMode==PLOT){
		nextTimePeriod(1);
		return;
	}//if
}//nextPeriod

function previousPeriod(){
//	if(activePrintGraph==false)
//		return;
	if(actualMode<0){
		previeousTimePeriodZoomIn(1);
		return;
	}//if
	if(actualMode==ZOOM_IN){
		previeousTimePeriodZoomIn(1);
		return;
	}//if
	if(actualMode==PLOT){
		previeousTimePeriod(1);
		return;
	}//if
}//previousPeriod


function previeousStep(){
//	if(activePrintGraph==false)
//		return;
	if(actualMode<0){
		previeousTimePeriodZoomIn(GRAPH_STEP);
		return;
	}//if
	if(actualMode==ZOOM_IN){
		previeousTimePeriodZoomIn(GRAPH_STEP);
		return;
	}//if
	if(actualMode==PLOT){
		previeousTimePeriod(GRAPH_STEP);
		return;
	}//if
}//previeousStep

function nextStep(){
//	if(activePrintGraph==false)
//		return;
	if(actualMode<0){
		nextTimePeriodZoomIn(GRAPH_STEP);
		return;
	}//if
	if(actualMode==ZOOM_IN){
		nextTimePeriodZoomIn(GRAPH_STEP);
		return;
	}//if
	if(actualMode==PLOT){
		nextTimePeriod(GRAPH_STEP);
		return;
	}//if
}//nextStep

function backZoom(){
	if (!plotInProgress) {
		var flashObj=getFlashObject();
		if( flashObj ) {
			flashObj.backZoom();
			actualMode=flashObj.GetVariable("actualPlotMode");
		}
		else {
			if( anZoom.length > 1 ) {
				var delta = anZoom.pop();
				gobjParams.timeFrom = gobjParams.startXZoomIn - new Number(delta/2);
				gobjParams.timeTo = gobjParams.endXZoomIn + new Number(delta/2);
				communication(ZOOM_IN);
			}
			else if( anZoom.length == 1 ) {
				var timeperiod = anZoom.pop();
				communication(PLOT);
			}
		}
	}
}//backZoom

function nextTimePeriod(segments){
	var flashObj=getFlashObject();
	if( flashObj )
		flashObj.SetVariable("arrowClicked","true");	

	mainTime= new Date(mainTime.getTime()+getTimePeriod(document.getElementById("timeperiod").value)/segments);
 	document.getElementById("year").value=mainTime.getFullYear();
	document.getElementById("month").value=mainTime.getMonth()+1;
	document.getElementById("day").value=mainTime.getDate();
	document.getElementById("hour").value=mainTime.getHours();
	document.getElementById("minut").value=mainTime.getMinutes();

	communication(PLOT);
}//nextTimePeriod

function previeousTimePeriod(segments){
	var flashObj=getFlashObject();
	if( flashObj )
		flashObj.SetVariable("arrowClicked","true");

	mainTime= new Date(mainTime.getTime()-getTimePeriod(document.getElementById("timeperiod").value)/segments);
	document.getElementById("year").value=mainTime.getFullYear();
	document.getElementById("month").value=mainTime.getMonth()+1;
	document.getElementById("day").value=mainTime.getDate();
	document.getElementById("hour").value=mainTime.getHours();
	document.getElementById("minut").value=mainTime.getMinutes();
	
	communication(PLOT);
}//previeousTimePeriod

function nextTimePeriodZoomIn(segments){
	var flashObj=getFlashObject();
	if( flashObj ) {
		startZoomInTime=flashObj.GetVariable("startXZoomIn");
		zoomDeltaTime=flashObj.GetVariable("endXZoomIn")-startZoomInTime;

		var dateStart=new Date(new Number(startZoomInTime)+new Number(zoomDeltaTime)/segments);
		var dateEnd= new Date(dateStart.getTime()+zoomDeltaTime);

		flashObj.SetVariable("timeFrom",dateStart.getTime());
		flashObj.SetVariable("timeTo",dateEnd.getTime());
		flashObj.SetVariable("correctTimeZoom",1);
	}
	else {
		var delta = new Number((gobjParams.endXZoomIn - gobjParams.startXZoomIn)/segments);
		gobjParams.timeFrom = gobjParams.startXZoomIn + delta;
		gobjParams.timeTo = gobjParams.endXZoomIn + delta;
	}
	communication(ZOOM_IN);
}//nextTimePeriod

function previeousTimePeriodZoomIn(segments){
	var flashObj=getFlashObject();
	if( flashObj ) {
		startZoomInTime=flashObj.GetVariable("startXZoomIn");
		zoomDeltaTime=flashObj.GetVariable("endXZoomIn")-startZoomInTime;
	
		var dateStart=new Date(startZoomInTime-parseInt(zoomDeltaTime/segments));
		var dateEnd= new Date(dateStart.getTime()+zoomDeltaTime);
		
		flashObj.SetVariable("timeFrom",dateStart.getTime());  
		flashObj.SetVariable("timeTo",dateEnd.getTime());
		flashObj.SetVariable("correctTimeZoom",1);            //Per le eleganti motivazioni sovracitate
	}
	else {
		var delta = new Number((gobjParams.endXZoomIn - gobjParams.startXZoomIn)/segments);
		gobjParams.timeFrom = gobjParams.startXZoomIn - delta;
		gobjParams.timeTo = gobjParams.endXZoomIn - delta;
	}
	communication(ZOOM_IN);
}//previeousTimePeriod


// ENHANCEMENT 20090215 - Now we set also the average value.
function setMaxMin(list){
	// this call is a useful callback: when the flash obj has finished plotting,
	// we re-enable the user's requests
	plotInProgress = false;
	
	var datas= list.split(";");
	for(var i=0;i<datas.length/6;i++){
		//datas[i*6]=idVar datas[i*6+1]=max datas[i*6+2]=timeMax datas[i*6+3]=min datas[i*6+4]=timeMin datas[i*6+5]=Avg
		var rowToModify = document.getElementById(datas[i*6]);
		if (rowToModify != null) {
			rowToModify.cells[5].innerHTML= datas[i*6+3]==""?"--":(datas[i*6+3] + " " +datas[i*6+4]); // minY 
			rowToModify.cells[6].innerHTML= datas[i*6+1]==""?"--":(datas[i*6+1] + " " +datas[i*6+2]); // maxY						                  
			rowToModify.cells[7].innerHTML=(datas[i*6+5]==""?"--":roundNumber(datas[i*6+5],2)); // Rounded average with 2 decimal digits					                  
		}
	}//for
}//setMaxMin


//I valori si riferiscono alle costanti su GraphConstant.java nel Director
function getTimePeriod(timePeriod){	
	for(;;){
		if(timePeriod==document.getElementById("graphConstantMinutes5").innerHTML){
			return 300000;
		}//if
		if(timePeriod==document.getElementById("graphConstantMinutes15").innerHTML){
			return 900000;
		}//if
		if(timePeriod==document.getElementById("graphConstantMinutes30").innerHTML){
			return 1800000;
		}//if
		if(timePeriod==document.getElementById("graphConstantHour").innerHTML){
			return 3600000;
		}//if
		if(timePeriod==document.getElementById("graphConstantHour6").innerHTML){
			return 21600000;
		}//if
		if(timePeriod==document.getElementById("graphConstantHour12").innerHTML){
			return 43200000;
		}//if
		if(timePeriod==document.getElementById("graphConstantDay").innerHTML){
			return 86400000;
		}//if
		if(timePeriod==document.getElementById("graphConstantWeek").innerHTML){
			return 604800000;
		}//if
		if(timePeriod==document.getElementById("graphConstantMonth").innerHTML){
			return 35 * 24 * 60 * 60 * 1000;//instead of 2592000000;
		}//if
		return 0;
	}//for
}//getTimePeriod


function changeTimePeriod(){
	activePrintGraph=false;
	if(document.getElementById("timeperiod").value==-1){
		disableAction(1); 
		actualMode=ZOOM_IN;
	}//if
	else{
		enableAction(1);	

		actualMode=PLOT;
	}//else
}//changeTimePeriod


function setTimePeriodFromFlash(valuePeriod,startTime){
	mainTime=new Date(startTime);
	
	document.getElementById("timeperiod").value=valuePeriod;
	enableAction(1);
	
	document.getElementById("year").value=mainTime.getFullYear();
	document.getElementById("month").value=mainTime.getMonth()+1;
	document.getElementById("day").value=mainTime.getDate();
	document.getElementById("hour").value=mainTime.getHours();
	document.getElementById("minut").value=mainTime.getMinutes();

	actualMode=PLOT;	
	
}//setTimePeriodFromFlash


function setAutoscale(){
	autoscale=!autoscale;
	document.getElementById("autoscaleImg").src=autoscale?"images/graph/autoscale_on.png":"images/graph/autoscale_off.png";
}//setAutoscale

function setZoomOutImage(active){
	if(active==1)
		document.getElementById("plotImage").scr="images/graph/autoscale_on.png";
	else
		document.getElementById("plotImage").scr="images/graph/autoscale_off.png";
}//setZoomOutImage

function zoomOutIsActive(){
	if(document.getElementById("plotImage").scr=="images/graph/autoscale_on.png")
		return true;
	return false;
}//setZoomOutImage


function getXMLHttpRequest()
{
	if (window.XMLHttpRequest){
	  return new XMLHttpRequest();
  	}//if
	if (window.ActiveXObject){
	  return new ActiveXObject("Microsoft.XMLHTTP");
  	}//if
  	return null;
}//getXMLHttpRequest

//ENHANCEMENT 20090210
function markTableRow(varId){
	// check if there's a row in edit mode
	if (!endEditRow(varId)) {
		// if there's a validation error we can't end the editing mode.
		// the marked row becomes the one currently in edit
		varId = last_edited_row;
	}
	var bodyTableDx=document.getElementById("idTableVariableDx").getElementsByTagName("tbody")[0];
	
	for(var i=1;i<bodyTableDx.rows.length;i++){ 
		bodyTableDx.rows[i].style.backgroundColor="#FFFFFF";
	}
	document.getElementById(varId).style.backgroundColor="#E5E5E5"; 	
}//markTableRow


function cleanUnitMeasurement(string){
	var strTmp="";
	
	if (string != " ")
	{
		for(var i=0;i<string.length;i++){
			if(string.charAt(i)==document.getElementById("andDeg").innerHTML){
				strTmp+="&deg";
			}//if
			else if(string.charAt(i)==document.getElementById("andMicro").innerHTML){
				strTmp+="&micro";
			}//if
			else 
				strTmp+=string.charAt(i);
		}//for
	}
	return strTmp;
}//cleanUnitMeasurement

/**
 * Function that could be used to round a number to a given decimal points. Returns the answer
 * Arguments :  number - The number that must be rounded
 *				decimal_points - The number of decimal points that should appear in the result
 */
function roundNumber(number,decimal_points) {
	if(!decimal_points) return Math.round(number);
	/*
	if(number == 0) {
		var decimals = "";
		for(var i=0;i<decimal_points;i++) decimals += "0";
		return "0."+decimals;
	}

	var exponent = Math.pow(10,decimal_points);
	var num = Math.round((number * exponent)).toString();
	// "0" padding before the number if abs(number) < 1
	var zeroLeftPadding = (Math.abs(number) < 1) ? "0" : "";
	return num.slice(0,-1*decimal_points) + zeroLeftPadding + "." + num.slice(-1*decimal_points);
	*/
	var d = Math.pow(10,decimal_points);
	var n = Math.round(number * d) / d;
	return n;
}


function controlMinMaxValue(min,max){
	if(min==max)
		if(min=="")
			return OK_CODE;	
	if ((min == "" && max != "") || (min != "" && max == "")) {
		return ERROR_CODE;
	}
	if (!isNumeric(min)) {
		return ERROR_CODE;
	}
	if (!isNumeric(max)) { 
		 return ERROR_CODE;
	}
	if(Number(min)>=Number(max))
		return ERROR_CODE; 
	return OK_CODE;	
}//controlMinMaxValue


function isNumeric(num) {
  	if (num == null || !num.toString().match(/^[-]?\d*\.?\d*$/)){
  	  	 return false;
  	  	}	
  	return true;
}
//****************************************************************//
//                                                                //
//        Script per il controllo GRAFICO FLASH                   //
//                                                                //
//****************************************************************//

//--lista funzioni usate sulla pagina del grafico

function getVarVal(myvar){
   var IE = navigator.appName.indexOf("Microsoft") != -1;
   var flashObj = IE ? window.flashObj : window.document.flashObj;
}

function PlotXAxis(myValue) {
   var IE = navigator.appName.indexOf("Microsoft") != -1;
   var flashObj = IE ? window.flashObj : window.document.flashObj;
   flashObj.PlotXAxis(myValue);
}

function selectCurve(curveId, curveDescr, curveColor){
  curveColor = curveColor.substr(2);
  document.getElementById('var'+curveId).style.background='#'+curveColor;
}

/*-funzione che mostra/nasconde le curve nel grafico-*/
function displayCurve(curveId,status){
   var IE = navigator.appName.indexOf("Microsoft") != -1;
   var flashObj = IE ? window.flashObj : window.document.flashObj;
   flashObj.displayCurve(curveId,status);
}

/*-funzione che mostra/nasconde la griglia-*/
function displayGrid(status){
   var IE = navigator.appName.indexOf("Microsoft") != -1;
   var flashObj = IE ? window.flashObj : window.document.flashObj;
   flashObj.displayGrid(status);
}

/*---funzione x selezionare curve---*/
function evidenceCurve(curveToEvidence){
	markTableRow(curveToEvidence);
    var flashObj = getFlashObject();
    if( flashObj )
    	flashObj.evidenceCurve(curveToEvidence);
}

/*---funzione x caricare i dati---*/
function loadDati(){
	var flashObj = getFlashObj();
	if( flashObj ) {
		flashObj.loadDati();
	}
	else {
		
	}
}



/***********************
 * MultiGraphic Section 
 ***********************
 */

function retrieveBlockLabel() 
{
	if(isVisibleComboBlocks())
		CommSend("servlet/ajrefresh","POST","cmd=get_blocks","mgRenderGroup",true);
}

function isVisibleComboBlocks()
{
	var sVal = "F";
	try {
		sVal = document.getElementById("displayComboBlocks").innerHTML;
	}catch(e){
		sVal = "F";
	}
	
	if(sVal == "T")
		return true;
	else
		return false;
}

function mgManageEditSel()
{
	var objTxt = document.getElementById("grouplabelText");
	var objSel = document.getElementById("grouplabel");
	
	if(objSel != null)
		objTxt.value = objSel.options[objSel.selectedIndex].text;
	
	document.getElementById("grouplabelText").style.width = document.getElementById("grouplabel").offsetWidth - 18;
}

function mgDelGroup()
{
	if (confirm(document.getElementById("blockdel").innerHTML))
	{
		var blkName = "";
		var objSel = document.getElementById("grouplabel");
		
		if(objSel != null)
		{
			if(objSel.value != "0")
			{
				blkName = document.getElementById("grouplabelText").value;
				// Send info to server
				CommSend("servlet/ajrefresh","POST","cmd=del_block&block_name="+blkName,"mgRenderGroup",true);
			}
		}
	}
}

function mgAddGroup()
{
	var blkName = "";
	var objTxt = document.getElementById("grouplabelText");
	
	if(objTxt != null)
	{
		blkName = objTxt.value;
		
		if(blkName != null && blkName != "")
		{
			// Retrieve variables selected
			var selectedRows = mgRetrieveCheckedVariable();
			if(selectedRows != null && selectedRows != "")
			{	
				// Send info to server
				CommSend("servlet/ajrefresh","POST","cmd=add_block&block_name="+blkName+"&block_data="+selectedRows,"mgRenderGroup",true);
			}
		}
	}
}

/*
 * Return IDDEV-IDVAR selected
 */
function mgRetrieveCheckedVariable()
{
	var oBodyTable = document.getElementById("idTableVariableDx").getElementsByTagName("tbody")[0];
	var arRet = new Array();
	var idVar = "";
	var idDev = "";
	
	for(var i=1; i<oBodyTable.rows.length; i++)
	{
		idVar = oBodyTable.rows[i].id;
		if( (document.getElementById("chkbox"+idVar) != null) && 
			(document.getElementById("chkbox"+idVar).checked))
		{
			idDev = oBodyTable.rows[1].cells[10].firstChild.nodeValue;
			arRet.push(idDev+"-"+idVar);
		}
	}	
	return arRet.join(";");
}

function Callback_mgRenderGroup()
{
	var oSelect = document.getElementById("grouplabel");
	// Clear
	oSelect.length = 0;
	
	oSelect.options.add(new Option("","0"));
	
	var sName = "";
	var sVal  = "" ;
	var sChk  = "" ;
	var bChk  = false;
	
	var xmlBlock = xmlResponse.getElementsByTagName("block");
	for(var i=0; i<xmlBlock.length; i++)
	{
		sName = xmlBlock[i].getElementsByTagName("name")[0].childNodes[0].nodeValue;
		sVal  = xmlBlock[i].getElementsByTagName("data")[0].childNodes[0].nodeValue;
		sChk  = xmlBlock[i].getElementsByTagName("check")[0].childNodes[0].nodeValue;
		if(sChk != null && sChk == "T")
			bChk = true;
		else
			bChk = false;
		
		oSelect.appendChild(newOption(sName,sVal,bChk));
	}
	
	mgManageEditSel();
}

function applyBlockCheckbox(oSel)
{
	var sValSel = oSel.options[oSel.selectedIndex].value;
	
	mgDecheckAllVar();
	if(sValSel == "0")
		return;
	
	var datas = sValSel.split(";");
	
	var idVar = "";
	var aData = null;
	for(var i=0; i<datas.length; i++)
	{
		aData = datas[i].split("-");
		idVar = aData[1];
		try
		{
			document.getElementById("chkbox"+idVar).checked=true;
			toggleVariable(idVar);
		}catch(e){}
	}
}

function mgDecheckAllVar()
{
	var oBodyTable = document.getElementById("idTableVariableDx").getElementsByTagName("tbody")[0];
	for(var i=1; i<oBodyTable.rows.length; i++)
	{
		idVar = oBodyTable.rows[i].id;
		if( (document.getElementById("chkbox"+idVar) != null))
		{
			if(document.getElementById("chkbox"+idVar).checked)
			{	
				document.getElementById("chkbox"+idVar).checked = false;
				toggleVariable(idVar);
			}
		}
	}
}


// canvas
var plotAnalogs = null;
var plotDigitals = null;
var gobjParams = null;
var intRenderLegend = null;
var anZoom = null;

// curve object
//	objCurve.DeviceId
//	objCurve.DeviceDescr
//	objCurve.VarId
//	objCurve.VarDescr
//	objCurve.Type
//	objCurve.UM
//	objCurve.Color
//	objCurve.LowerLimit
//	objCurve.UpperLimit
//	objCurve.MinValue
//	objCurve.MaxValue
//	objCurve.AvgValue
//	objCurve.FlotData
var aobjAnalogCurves = new Array();
var aobjDigitalCurves = new Array();

// fn loadCurve
// parameters object
//	objParams.communication
//	objParams.srvSource
//	objParams.actionType
//	objParams.variableList
//	objParams.timeperiod
//	objParams.mainTime
//	objParams.startXZoomIn
//	objParams.endXZoomIn
//	objParams.typeGraph
//	objParams.deviceLabel	
//	objParams.printDataLabel	
//	objParams.userDataLabel	
//	objParams.userName	
//	objParams.startDateLabel	
//	objParams.rangeLabel	
//	objParams.nodeName	
//	objParams.autoscale
//	objParams.save_data
//	objParams.anaNum
//	objParams.digNum
//	objParams.xWidth
//	objParams.beginTime
//	objParams.endTime
//	objParams.qTime
function loadData(objParams)
{
	var ajax = getXMLHttpRequest();
	ajax.open("post", objParams.srvSource, "true");
	ajax.setRequestHeader("content-type", "application/x-www-form-urlencoded");
	var params = "noFlash=true" 
		+ "&actionType=" + escape(objParams.actionType)
		+ "&typeGraph=" + escape(objParams.typeGraph)
		+ "&mainTime=" + escape(objParams.mainTime)
		+ "&timeperiod=" + escape(objParams.timeperiod)
		+ (objParams.communication == ZOOM_IN
				? "&startXZoomIn=" + escape(objParams.startXZoomIn) + "&endXZoomIn=" + escape(objParams.endXZoomIn)
				: "")
		+ "&variableList=" + encodeURI(objParams.variableList);
		+ "&anaNum" + escape(objParams.anaNum)
		+ "&digNum" + escape(objParams.digNum);
	ajax.send(params);
	ajax.onreadystatechange = function() {
		if(ajax.readyState == 4) { // complete 
			if(ajax.status == 200)
				onDataLoaded(objParams, ajax.responseXML);
			else
				alert("Communication error number: " + ajax.status);
		}
	};
}


function onDataLoaded(objParams, xml)
{
	var axmlGraphs = xml.getElementsByTagName("graphs");
	if( !axmlGraphs )
		return;
	var xmlGraphs = axmlGraphs[0];

	if( objParams.communication == PLOT )
		anZoom = new Array();
	
	var beginTime = objParams.communication == PLOT ? objParams.mainTime : objParams.startXZoomIn;
	var endTime = objParams.communication == PLOT ? (new Date(beginTime + getTimePeriod(objParams.timeperiod))).getTime() : objParams.endXZoomIn;
	var flotTimeShift = -(new Date()).getTimezoneOffset() * 60 * 1000;
	beginTime += flotTimeShift;
	endTime += flotTimeShift;
	var qTime = (endTime - beginTime) / objParams.xWidth;
	
	// render begin/end time
	var aux = new Date(beginTime); 
	document.getElementById("divBeginTime").innerHTML = (new Date(aux.getUTCFullYear(), aux.getUTCMonth(), aux.getUTCDate(),  aux.getUTCHours(), aux.getUTCMinutes(), aux.getUTCSeconds()))
		.format("dd/MM/yyyy hh:mm");
	aux = new Date(endTime);
	document.getElementById("divEndTime").innerHTML = (new Date(aux.getUTCFullYear(), aux.getUTCMonth(), aux.getUTCDate(),  aux.getUTCHours(), aux.getUTCMinutes(), aux.getUTCSeconds()))
		.format("dd/MM/yyyy hh:mm");
	
	axmlGraphs = xmlGraphs.getElementsByTagName("graph");
	var iDig = 0;
	if( axmlGraphs ) for(var i = 0; i < axmlGraphs.length; i++) {
		var xmlGraph = axmlGraphs[i];
		var id = xmlGraph.getAttribute("id");
		var type = xmlGraph.getAttribute("type");
		var objCurve = getCurve(id, type);
		if( objCurve ) {
			// get curve properties
			objCurve.Color = "#" + xmlGraph.getAttribute("color").substring(2, 8);
			objCurve.LowerLimit = parseFloat(xmlGraph.getAttribute("minScale"));
			objCurve.UpperLimit = parseFloat(xmlGraph.getAttribute("maxScale"));
			objCurve.MinValue = parseFloat(xmlGraph.getAttribute("minValue"));
			objCurve.MaxValue = parseFloat(xmlGraph.getAttribute("maxValue"));
			objCurve.AvgValue = parseFloat(xmlGraph.getAttribute("avgValue"));			
			// get curve data
			var xmlGData = xmlGraph.getElementsByTagName("gdata")[0];
			var txtGData = xmlGData.firstChild.nodeValue;
			var aPeeks = txtGData.split("|");
			// make flot data
			var aFlot = [ [beginTime, "***", "***"] ]; // time bracketing {
			// first element discarded because the string begin with |
			var startIndex = 0;
			var aDataSetIndex = 1;
			for(var k = 1; k < aPeeks.length; k++) {
				var aDataSet = aPeeks[k].split("@");
				//if value is null, insert null
				if( aDataSet[0] == "n") {
					aFlot[aDataSetIndex++] = null;
				}
				else {
					if( type == "D" ) {
						var dValue = parseFloat(aDataSet[0]);
						if( dValue > 0 )
							dValue = 0.5;
						//if type is digital, add two points
						//first point is previous value's time, with the same value
						//second point is current value's time, with the same value
						aFlot[aDataSetIndex++] = [ beginTime + qTime *startIndex, dValue + iDig];
						aFlot[aDataSetIndex++] = [ beginTime + qTime * parseInt(aDataSet[2]), dValue + iDig];
					}
					else {
						//if the first value is not null, will add two points to paint a line
						if(k == 1)
						{
							aFlot[aDataSetIndex++] = [ beginTime, parseFloat(aDataSet[0]), parseFloat(aDataSet[1]) ];
							aFlot[aDataSetIndex++] = [ beginTime + qTime * parseInt(aDataSet[2]), parseFloat(aDataSet[0]), parseFloat(aDataSet[1]) ];
						}
						else
						{
							var aDataSetBefore = aPeeks[k-1].split("@");
							//before is a null, at two points to paint a line
							if( aDataSetBefore[0] == "n") {
								aFlot[aDataSetIndex++] = [ beginTime + qTime * startIndex, parseFloat(aDataSet[0]), parseFloat(aDataSet[1]) ];
								aFlot[aDataSetIndex++] = [ beginTime + qTime * parseInt(aDataSet[2]), parseFloat(aDataSet[0]), parseFloat(aDataSet[1]) ];
							}
							//other wise, just add one point to paint polyline
							else
								aFlot[aDataSetIndex++] = [ beginTime + qTime * parseInt(aDataSet[2]), parseFloat(aDataSet[0]), parseFloat(aDataSet[1]) ];
						}
					}
				}
				startIndex = parseInt(aDataSet[2]);
			}
			aFlot[aDataSetIndex++] = [endTime, "***", "***"]; // time bracketing }	
			objCurve.FlotData = aFlot;
			if( type == "D" )
				iDig += 0.7;
		}
		else {
			alert("ERROR: Curve " + id + " not found in js mem");
		}
	}
	
	renderGraph(objParams);
}


function getCurve(id, type)
{
	var aobjCurves = type == "D" ? aobjDigitalCurves : aobjAnalogCurves;
	for(var i = 0; aobjCurves.length; i++)
		if( aobjCurves[i].VarId == id )
			return aobjCurves[i];
	return null;
}


function plotSelected(event, ranges)
{
	if( !gobjParams.timeFrom && !gobjParams.timeTo ) { 
		gobjParams.timeFrom = parseInt(ranges.xaxis.from.toFixed(0), 10); 
		gobjParams.timeTo = parseInt(ranges.xaxis.to.toFixed(0), 10);
		var flotTimeShift = (new Date()).getTimezoneOffset() * 60 * 1000;
		gobjParams.timeFrom += flotTimeShift;
		gobjParams.timeTo += flotTimeShift;
		if( anZoom.length > 0 )
			anZoom.push(gobjParams.timeTo - gobjParams.timeFrom);
		else
			anZoom.push(gobjParams.timeperiod);
		communication(ZOOM_IN);
	}
}


function plotHover(event, pos, item)
{
	if( plotAnalogs )
		plotAnalogs.setCrosshair(pos);
	if( plotDigitals )
		plotDigitals.setCrosshair(pos);
	gobjParams.latestPos = pos;
}

// fn renderGraph
// flot object    
//	objFlot.color: color or number
//	objFlot.data: rawdata
//	objFlot.label: string
//	objFlot.lines: specific lines options
//	objFlot.bars: specific bars options
//	objFlot.points: specific points options
//	objFlot.xaxis: number
//	objFlot.yaxis: number
//	objFlot.clickable: boolean
//	objFlot.hoverable: boolean
//	objFlot.shadowSize: number
function renderGraph(objParams)
{
	var graphAnalogs = $("#placeholder_a");
	var aAnalogs = [];
	var aYAxis = [ { position: "left" } ];
	var iYAxis = 1;
	for(var i = 0, k = 0; i < aobjAnalogCurves.length; i++) {
		if( aobjAnalogCurves[i].FlotData ) {
			var objCurve = aobjAnalogCurves[i];
			var objFlot = new Object();
			objFlot.objCurve = objCurve; // to be able to access curve data from flot handlers
			objFlot.color = objCurve.Color;
			objFlot.data = objCurve.FlotData;
			objFlot.label = objCurve.VarDescr + " = ***";
			objFlot.shadowSize = 0;
			objFlot.xaxis = 1;
			/*
			if( objCurve.LowerLimit != objCurve.UpperLimit ) {
				objFlot.yaxis = iYAxis + 1;
				aYAxis[iYAxis++] = {
					show: true,
					color: objFlot.color,
					min: objCurve.LowerLimit,
					max: objCurve.UpperLimit
				};
			}
			else {
				objFlot.yaxis = 1;
			}
			*/
			objFlot.yaxis = 1;			
			aAnalogs[k++] = objFlot;
		}
	}
	for(var i = 0; i < aobjDigitalCurves.length; i++) {
		if( aobjDigitalCurves[i].FlotData ) {
			var objCurve = aobjDigitalCurves[i];
			var objFlot = new Object();
			objFlot.data = [];
			objFlot.color = objCurve.Color;
			objFlot.label = objCurve.VarDescr;
			objFlot.shadowSize = 0;
			aAnalogs[k++] = objFlot;
		}
	}
	
	graphAnalogs.bind("plothover", plotHover);
	graphAnalogs.bind("plotselected", plotSelected);
	plotAnalogs = $.plot(graphAnalogs, aAnalogs, {
		legend: {
			labelFormatter: function(label, series) {
				return '<span class="standardTxt">' + label + '</span>';
			},			
			position: "nw",
			margin: [0, 15],
			backgroundOpacity: 0.66
		},
		grid: { hoverable: true },		
		selection: { mode: "x" },
		crosshair: { mode: "x" },
		xaxis: { mode: "time",
				labelWidth:40
		},
		//yaxis: aYAxis
	} );
	
	var aDigitals = [];
	var graphDigitals = $("#placeholder_d");
	for(var i = 0, k = 0; i < aobjDigitalCurves.length; i++) {
		if( aobjDigitalCurves[i].FlotData ) {
			var objCurve = aobjDigitalCurves[i];
			var objFlot = new Object();
			objFlot.objCurve = objCurve; // to be able to access curve data from flot handlers
			objFlot.color = objCurve.Color;
			objFlot.data = objCurve.FlotData;
			objFlot.label = objCurve.VarDescr;
			objFlot.shadowSize = 0;
			objFlot.xaxis = 1;
			aDigitals[k++] = objFlot;
		}
	}
	
	graphDigitals.bind("plothover", plotHover);
	graphDigitals.bind("plotselected", plotSelected);
	plotDigitals = $.plot(graphDigitals, aDigitals, {
		legend: {
			show: false
			/* moved to analogs
			labelFormatter: function(label, series) {
				return '<span class="standardTxt">' + label + '</span>';
			},			
			position: "nw",
			backgroundOpacity: 0.66
			*/
		},
		grid: { hoverable: true },			
		selection: { mode: "x" },
		crosshair: { mode: "x" },
		xaxis: { mode: "time",
			min: objParams.beginTime,
			max: objParams.endTime
		},
		yaxis: { show: false,
			reserveSpace:40}
	} );
	
	buildCrosshairDate();
	// make objParams accessible to flot handlers
	gobjParams = objParams;
	if( !intRenderLegend )
		intRenderLegend = setInterval("renderLegend()", 100);
}


function renderLegend()
{
	var pos = gobjParams.latestPos;
	if( !pos )
		return;
	
	if( plotAnalogs ) {
	    var thisDate = buildDate(pos.x);
	    $("#crosshairDate").text(thisDate);
		
		var legends = $("#placeholder_a .legendLabel");
		var dataset = plotAnalogs.getData();
		for(var i = 0; i < dataset.length; i++) {
			var objFlot = dataset[i];
			// find the nearest point
			for(var j = 0; j < objFlot.data.length; j++)
				if( objFlot.data[j] && objFlot.data[j][0] > pos.x )
					break;
			var strLabel = ""; 
			if( objFlot.data[j - 1] ) {
				if( objFlot.data[j - 1][1] != objFlot.data[j - 1][2] )
					strLabel += "min:" + objFlot.data[j - 1][1] + ", max:" + objFlot.data[j - 1][2];
				else
					strLabel += objFlot.data[j - 1][1];
				strLabel += " " + objFlot.objCurve.UM;
			}
			else {
				strLabel += "***";
			}
			var htmLabel = '<span class="standardTxt">' + objFlot.label.replace(/=.*/, "= " + strLabel) + '</span>';
			legends.eq(i).html(htmLabel);
		}
	}
	
//	if( plotDigitals ) {
//		var legends = $("#placeholder_d .legendLabel");
//		var dataset = plotDigitals.getData();
//		for(var i = 0; i < dataset.length; i++) {
//			var objFlot = dataset[i];
//			// find the nearest point
//			for(var j = 0; j < objFlot.data.length; j++)
//				if( objFlot.data[j] && objFlot.data[j][0] > pos.x )
//					break;
//			legends.eq(i).html(objFlot.label.replace(/=.*/, "= "
//				+ (objFlot.data[j - 1] ? objFlot.data[j - 1][1] : "***")));
//		}
//	}
}


function buildCrosshairDate()
{
	var crosshairDate = $("#crosshairDate");
	if( crosshairDate.length == 0 ) {
		var graphAnalogs = $("#placeholder_a");		
		var legend = graphAnalogs.children(".legend");
		var legendDiv = legend.children("div").first();
		var crosshairDate = $('<div id="crosshairDate" style="position:relative;top:-15px;">DATE/TIME</div>');
	    legendDiv.append(crosshairDate);
	    legend.children("table").click(function() { moveLegend(); });
	}
}


function buildDate(date, timeformat)
{
	if( !timeformat )
		timeformat = "%0d/%0m %0H:%0M";
	var d = new Date(Number(date));
	return $.plot.formatDate(d, timeformat); 
}


function moveLegend()
{
	var legend = plotAnalogs.getOptions().legend;
	var pos = legend.position;
	legend.position = (pos == 'ne') ? 'nw' : 'ne';
	plotAnalogs.setupGrid();
	buildCrosshairDate();
}


function curveSelection(bSelect)
{
	computeViewport();
	var divGraph = document.getElementById("divGraphCanvas");
	if( divGraph )
		divGraph.style.display = bSelect ? "none" : "block";
	var divButtons = document.getElementById("buttonTable");
	if( divButtons )
		divButtons.style.display = bSelect ? "none" : "block";
	var divTable = document.getElementById("selectDeviceResponse");
	if( divTable )
		divTable.style.display = bSelect ? "block" : "none";
	var table = document.getElementById("idTableVariableDx");
	if( table )
		table.style.height = (viewportHeight - 70) + "px";
}
