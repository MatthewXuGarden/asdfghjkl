var PLOT=1;
var ZOOM_IN=2;

var activePrintGraph=false;
var plotInProgress = false;
var actualMode=PLOT;
var GRAPH_STEP=7;
var rowClass=0;

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

		if (typeof flashObj === 'undefined')
			return;
		if(!flashObj.setParameters ) {
			setTimeout(initialize, 250);
			return;
		} 
		flashObj.SetVariable("_root.preLoadData.preLoadData.text",document.getElementById("waitPlotMsg").innerHTML);
		flashObj.setParameters("sysTimeLabelUser",document.getElementById("sysTimeLabelUser").innerHTML);
		flashObj.setParameters("sysTimeLabelSO",document.getElementById("sysTimeLabelSO").innerHTML);
		flashObj.setParameters("rangeMonthLabel",document.getElementById("rangeMonthLabel").innerHTML);
		flashObj.setParameters("rangeDaysLabel",document.getElementById("rangeDaysLabel").innerHTML);
		flashObj.setParameters("rangeHoursLabel",document.getElementById("rangeHoursLabel").innerHTML);
		flashObj.setParameters("rangeMinLabel",document.getElementById("rangeMinLabel").innerHTML);
		flashObj.setParameters("loadErrLabel",document.getElementById("loadErrLabel").innerHTML);
		
		disableAction(2); // print is not enabled on start
		
		var data = new Date();
		
		var sesMainTime = document.getElementById("sesMainTime").value;
		if (sesMainTime == null || sesMainTime == "") {
			//	data.setTime(new Date().getTime()-document.getElementById("time"+document.getElementById("periodCode").innerHTML).innerHTML);
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
		
		if(document.getElementById("automaticPlot").innerHTML == "true")
			communication(PLOT);
		
		if (document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0].rows.length > 0) {
			enableAction(1);
		}
		else {
			disableAction(1);
		}		
	} catch (e) {
		alert("Exception: " + e.toString() + " (graph.js)");
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
	var dialogUrl = top.frames["manager"].getDocumentBase() + "/app/dtlview/GraphDialog.jsp;jsessionid=" + jsessionid;
	
	var destArgs = new Array();
	destArgs[0] = document.getElementById("listaDevices").innerHTML;
	destArgs[1] = document.getElementById("timeperiod").selectedIndex;
	destArgs[2] = document.getElementById("year").value;
	destArgs[3] = document.getElementById("month").value;
	destArgs[4] = document.getElementById("day").value;
	destArgs[5] = document.getElementById("hour").value;
	destArgs[6] = document.getElementById("minut").value;
	
	// x problema di dimensioni area visibile fra IE6 ed IE7:
	var dHeight = 360;
	var dWidth = 400;
	var version = 0;
	if (navigator.appVersion.indexOf("MSIE")!=-1)
	{
		dati = navigator.appVersion.split("MSIE");
		version = parseFloat(dati[1]);
		dHeight += 30;
	}
	
	if (version < 7.0) //NON IE browser will return 0
	{
		dWidth += 4; // left and right frame edge
		dHeight += 29 + 25 + 4; // title + status bar
	}
	
//	var winSettings = "dialogWidth:"+dWidth+"px;dialogHeight:"+dHeight+"px;dialogTop:255px;dialogLeft:515px;scroll:no;border:thick;help:off;status:off;resizable:off;";
//	var resArgs = new Array();
	DivWindowOpen(dWidth,dHeight,document.getElementById("searchTitle").value,dialogUrl,destArgs,"graphDialog");
//	resArgs = window.showModalDialog(dialogUrl, destArgs, winSettings);
}

function OpenGraph(resArgs){
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
function saveAndPlot() {
	
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
	plotInProgress = true;
	var flashObj=getFlashObject();
	
	switch(arg){
		case PLOT:
			activePrintGraph=true;
			// Print action enabled
			enableAction(2);
			prepareDataToPost();
			plotInProgress = true;
			flashObj.loadDati();
			flashObj.SetVariable("_root.preLoadData.preLoadData.text","");
			actualMode=PLOT;	
			flashObj.SetVariable("actualPlotMode",actualMode);
		break;
		case ZOOM_IN:
			if(prepareDataZoomInToPost()==0){
				return;
			}//if
			plotInProgress = true;
			flashObj.loadDati();
			flashObj.SetVariable("_root.preLoadData.preLoadData.text","");
			actualMode=ZOOM_IN;
			flashObj.SetVariable("actualPlotMode",actualMode);
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
	var virtkey = document.getElementById("virtkey").value;
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
	if (last_edited_row == varId && document.getElementById("chkbox"+varId).checked) {
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
	document.getElementById(last_edited_row).cells[2].innerHTML="<table cellspacing='0' style='border-radius:14px;border:2px solid black;height:19px;width:100%;'><tr><td style='border-radius:14px;background-color:"+newColor+"'></td></tr></table>";
	
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
	var flashObj=getFlashObject();
	var bodyTableInvisibleSx=document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0];

	document.getElementById("is"+varId).parentNode.removeChild(document.getElementById("is"+varId));
	document.getElementById("chkbox"+varId).checked=false;
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
	tableVariableDXRows += "<tr class='th'><th id=\"THgraphVar00\"></th><th id=\"THgraphVar01\">" + document.getElementById("thDevLabel").innerHTML +
		"</th><th id=\"THgraphVar02\">" + document.getElementById("thColorLabel").innerHTML +
		"</th><th id=\"THgraphVar03\">" + document.getElementById("thVarLabel").innerHTML +
		"</th><th id=\"THgraphVar04\">" + document.getElementById("thUnitOfMeasureLabel").innerHTML +
		"</th><th id=\"THgraphVar05\">" + document.getElementById("thMinLabel").innerHTML + 
		"</th><th id=\"THgraphVar06\">" + document.getElementById("thMaxLabel").innerHTML +
		"</th><th id=\"THgraphVar07\">" + document.getElementById("thAvgLabel").innerHTML + 
		"</th><th id=\"THgraphVar08\">" + document.getElementById("thLowerLimitLabel").innerHTML +
		"</th><th id=\"THgraphVar09\">" + document.getElementById("thUpperLimitLabel").innerHTML +"</th></tr>";
	
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
	}//for
	
	// we received a new list of variables for a newly chosen device.
	// Now we parse the device's variables, store them in the invisible DX table and
	// append them at the bottom of the visible list
	if (ajax != null) {
		// current device variables (DX)
		try {
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
		}catch(e) {};
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
				"<div id=\"idTableVariableDx\">" +
					"<table width='100%' cellspacing='2' cellpadding='2' border='0'>" +
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
	rowClass++;
	row += "<tr height='35px' id=" + varId + " title='" + document.getElementById("dblclickLabel").innerHTML + "' class=\"graphVarTableRow "+(rowClass%2==0 ? "Row1":"Row2")+"\" onclick=\"evidenceCurve('" +varId+ "')\" ondblclick=\"editRow('"+varId+"');\" >";
	row += 	"<td><input class='bigcheck' id ='chkbox" + varId +"' type='checkbox' " +(checked?"checked":"")+ " onclick=\"toggleVariable('"+varId+"')\" /></td>";
	row += 	"<td>" +devDescr+ "</td>";
	row +=	"<td><table cellspacing='0' style='border-radius:14px;border:2px solid black;height:19px;width:100%;'><tr><td style='border-radius:14px;background-color:#"+colour+"'></td></tr></table></td>";
	row +=	"<td>" +varDescr+ "</td>";
	row +=	"<td align='center'>" +um+ "</td>"; // unit of measure
	row +=	"<td align='center'>--</td>"; // y min.
	row +=	"<td align='center'>--</td>"; // y max.
	row +=	"<td align='center'>--</td>"; // average value
	
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
	
	row +=	"<td align='center'>" +yLowerLimit+ "</td>";
	row +=	"<td align='center'>" +yUpperLimit+ "</td>";
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

	var flashObj=getFlashObject();
	
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

	return 1;
}//prepareDataZoomInToPost

function getFlashObject()
{
	/*
	var IE = navigator.appName.indexOf("Microsoft") != -1;
	  try {
		  return IE ? flashObj : document.flashObj;		
	} catch (e) {		
		return null;
	}
	*/
	return document.flashObj;
}//getFlashObject

// ENHANCEMENT 20090211
// opens a popup window for setting general graph properties: grids, axis colors, ViewFinder properties, etc.
function openGraphLayoutConfig(graphType) {
	var jsessionid = document.getElementById("jsession").innerHTML;
	var dialogUrl = top.frames["manager"].getDocumentBase() + "app/dtlview/GraphLayoutConfigDialog.jsp;jsessionid=" + jsessionid;
	
	var destArgs = new Array();
	destArgs[0] = graphType;
	
	// x problema di dimensioni area visibile fra IE6 ed IE7:
	var dHeight = 260;
	var dWidth = 850;
	var version = 0;
	if (navigator.appVersion.indexOf("MSIE")!=-1)
	{
		dati = navigator.appVersion.split("MSIE");
		version = parseFloat(dati[1]);
		dHeight += 45;
	}
	
	if (version < 7.0) //NON IE browser will return 0
	{
		dWidth += 4; // left and right frame edge
		dHeight += 29 + 25 + 4; // title + status bar
	}
//	var winSettings = "dialogWidth:"+dWidth+"px;dialogHeight:"+dHeight+"px;dialogTop:100px;dialogLeft:100px;scroll:no;border:thick;help:off;status:off;resizable:on;";
//	var resArgs = new Array();
//	resArgs = window.showModalDialog(dialogUrl, destArgs, winSettings);
	DivWindowOpen(dWidth,dHeight,document.getElementById("customizeTitle").value,dialogUrl,destArgs,"graphLayout");
		
	
}	

function openGraphLayout(resArgs){
	// output management
	if (resArgs != null)
	{
		document.getElementById("cosmeticGraphInformation").value = resArgs;
		
		var ofrm = document.getElementById("frmCosmeticGraph");
		MTstartServerComm();
		ofrm.submit();
//		communication(PLOT);//forzo ri-disegno autom. gfx 
	}	
	}



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
	var flashObj=getFlashObject();
	if (!plotInProgress) {
		flashObj.backZoom();
		actualMode=flashObj.GetVariable("actualPlotMode");
	}
}//backZoom

function nextTimePeriod(segments){
	var flashObj=getFlashObject();
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
	startZoomInTime=flashObj.GetVariable("startXZoomIn");
	zoomDeltaTime=flashObj.GetVariable("endXZoomIn")-startZoomInTime;

	var dateStart=new Date(new Number(startZoomInTime)+new Number(zoomDeltaTime)/segments);
	var dateEnd= new Date(dateStart.getTime()+zoomDeltaTime);

	flashObj.SetVariable("timeFrom",dateStart.getTime());
	flashObj.SetVariable("timeTo",dateEnd.getTime());
	flashObj.SetVariable("correctTimeZoom",1);
	communication(ZOOM_IN);
}//nextTimePeriod

function previeousTimePeriodZoomIn(segments){
	var flashObj=getFlashObject();
	startZoomInTime=flashObj.GetVariable("startXZoomIn");
	zoomDeltaTime=flashObj.GetVariable("endXZoomIn")-startZoomInTime;

	var dateStart=new Date(startZoomInTime-parseInt(zoomDeltaTime/segments));
	var dateEnd= new Date(dateStart.getTime()+zoomDeltaTime);
	
	flashObj.SetVariable("timeFrom",dateStart.getTime());  
	flashObj.SetVariable("timeTo",dateEnd.getTime());
	flashObj.SetVariable("correctTimeZoom",1);            //Per le eleganti motivazioni sovracitate
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
		if(datas[i*6].length>0){
			var rowToModify = document.getElementById(datas[i*6]);
			if (rowToModify != null) {
				rowToModify.cells[5].innerHTML= datas[i*6+3]==""?"--":(datas[i*6+3] + " " +datas[i*6+4]); // minY 
				rowToModify.cells[6].innerHTML= datas[i*6+1]==""?"--":(datas[i*6+1] + " " +datas[i*6+2]); // maxY						                  
				rowToModify.cells[7].innerHTML=(datas[i*6+5]==""?"--":roundNumber(datas[i*6+5],2)); // Rounded average with 2 decimal digits					                  
			}
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
  // var flashObj = IE ? window.flashObj : window.document.flashObj;
   var flashObj=getFlashObject();
}

function PlotXAxis(myValue) {
   var IE = navigator.appName.indexOf("Microsoft") != -1;
  //var flashObj = IE ? window.flashObj : window.document.flashObj;
   var flashObj=getFlashObject();
   flashObj.PlotXAxis(myValue);
}

function selectCurve(curveId, curveDescr, curveColor){
  curveColor = curveColor.substr(2);
  document.getElementById('var'+curveId).style.background='#'+curveColor;
}

/*-funzione che mostra/nasconde le curve nel grafico-*/
function displayCurve(curveId,status){
   var IE = navigator.appName.indexOf("Microsoft") != -1;
  // var flashObj = IE ? window.flashObj : window.document.flashObj;
   var flashObj=getFlashObject();
   flashObj.displayCurve(curveId,status);
}

/*-funzione che mostra/nasconde la griglia-*/
function displayGrid(status){
   var IE = navigator.appName.indexOf("Microsoft") != -1;
  // var flashObj = IE ? window.flashObj : window.document.flashObj;
   var flashObj=getFlashObject();
   flashObj.displayGrid(status);
}

/*---funzione x selezionare curve---*/
function evidenceCurve(curveToEvidence){
	markTableRow(curveToEvidence);
    var IE = navigator.appName.indexOf("Microsoft") != -1;
  //  var flashObj = IE ? window.flashObj : window.document.flashObj;
    var flashObj = getFlashObject();
    flashObj.evidenceCurve(curveToEvidence);
}

function escapeXml(str) {
	return str.replace(/&/g, "&amp;").replace(/\</g, "&lt;").replace(/>/g, "&gt;");
}

/*---funzione x caricare i dati---*/
function loadDati(){
    var IE = navigator.appName.indexOf("Microsoft") != -1;
   // var flashObj = IE ? window.flashObj : window.document.flashObj;
    var flashObj=getFlashObject();
    flashObj.loadDati();
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
	
//	var browser = navigator.userAgent.toLowerCase();
	
//	if (browser.indexOf('chrome') > -1) {
//		document.getElementById("layer_select_block").style.top=8;
//	}
//	else if (browser.indexOf('firefox') > -1) {
//		document.getElementById("layer_select_block").style.top=8;
//	}

//	document.getElementById("grouplabelText").style.width = document.getElementById("grouplabel").offsetWidth - 14;
//	document.getElementById("grouplabelText").style.fontSize="8pt";

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