var LOAD_DEVICE_VAR=0;
var LOAD_DEVICE=1;
var LOAD_DEVICE_MDL=2;
var RESOLVE_ROWS_FROM_IDVARMDL=3;
var LOAD_REPORT_TO_MODIFY=4;
var NEW_ADD_VARS = 5;

var MAX_REPORT_VARS=5000;

var deviceSelected="";
var idDeviceSelected="";
var tab=0;
var idReportSelect="";
var counterNumVar = 0;
var counterNumDevice = 0;
var initRows = 0;
var hm = 0;
var datas = new Array();
var rowColor =0;

function setMaxVar(maxVar){
	MAX_REPORT_VARS=maxVar;
}

function onParamLoaded()
{
	var divContainer = document.getElementById("container");
	if( divContainer ) {
		divContainer.style.height = top.frames["body"].document.body.clientHeight - 50;
	}
}

function initialize()
{
	onParamLoaded();
	if(document.getElementById("tab").innerHTML=="tab1name"){
		tab=1;
	}
	else{
		tab=2;
		enableAction(1);
	}
}

function selectedLineReport(idLine){
	idReportSelect=idLine;
	enableAction(1);
	if(tab==2){
		enableAction(1);
		enableAction(2);
		disableAction(3);
	}
}
function modifyReport(idLine){
	idReportSelect=idLine;
	if(tab==2){
		reload_actions(LOAD_REPORT_TO_MODIFY);
		disableAction(1);
		disableAction(2);
		enableAction(3);
	}
}

function reload_actions(action) {
	var cmbDevice = document.getElementById('dev');
	var cmdDeviceModel = document.getElementById('model');
	var toSend = "";
	var functionToRecall = "";

	if (action == LOAD_DEVICE_VAR) {
		deviceSelected = cmbDevice.options[cmbDevice.selectedIndex].innerHTML;
		idDeviceSelected = cmbDevice.options[cmbDevice.selectedIndex].value;
		toSend = LOAD_DEVICE_VAR + "&iddevice=" + cmbDevice.options[cmbDevice.selectedIndex].value;
		functionToRecall = "loadVariable";
	}
	if (action == LOAD_DEVICE) {
		emptySelect("var");
		cmdDeviceModel.selectedIndex = 0;
		toSend = LOAD_DEVICE + "&iddevice=-1";
		functionToRecall = "loadDevice";
	}
	if (action == LOAD_DEVICE_MDL) {
		// deviceSelected=" [
		// "+cmdDeviceModel.options[cmdDeviceModel.selectedIndex].innerHTML+ "
		// ]";
		toSend = LOAD_DEVICE_MDL + "&iddevice=" + cmdDeviceModel.options[cmdDeviceModel.selectedIndex].value;
		functionToRecall = "loadDeviceAndVariable";
	}
	if (action == LOAD_REPORT_TO_MODIFY) {
		// deviceSelected=" [
		// "+cmdDeviceModel.options[cmdDeviceModel.selectedIndex].innerHTML+ "
		// ]";
		toSend = LOAD_REPORT_TO_MODIFY + "&iddevice=" + cmdDeviceModel.options[cmdDeviceModel.selectedIndex].value;
		functionToRecall = "loadReport";
	}
	CommSend("servlet/ajrefresh", "POST", "action=" + toSend, functionToRecall, true);
}

function Callback_loadVariable(){
	document.getElementById("div_var").innerHTML=xmlResponse.getElementsByTagName("response")[0].childNodes[0].nodeValue;
}

function Callback_loadDevice(){
	document.getElementById("model").disabled=true;
	document.getElementById("div_dev").innerHTML=xmlResponse.getElementsByTagName("response")[0].childNodes[0].nodeValue;
}

function Callback_loadDeviceAndVariable(){
	document.getElementById("div_var").innerHTML=xmlResponse.getElementsByTagName("variable")[0].childNodes[0].nodeValue;
	document.getElementById("div_dev").innerHTML=xmlResponse.getElementsByTagName("device")[0].childNodes[0].nodeValue;
}

function Callback_loadReport(){
	var divtable = document.getElementById("div_table_global_vars");
	divtable.innerHTML = xmlResponse.getElementsByTagName("rsp")[0].childNodes[0].nodeValue;

	var listaVar="";
	var trArray = document.getElementById("table_global_vars").getElementsByTagName("tr");
	for (var i=0; i<trArray.length;i++){
		listaVar+=trArray[i].id+";";
	}
}

function cleanAllSettingTables(){
	emptySelect("var");
	divBody=document.getElementById("table_global_vars").tBodies[0].rows[0].innerHTML
	emptyTable("table_global_vars",0);
}//cleanAllSettingTables

function enableModelSelection(){
	document.getElementById("model").disabled=false;
	emptySelect("dev");
	emptySelect("var");
}//enableModelSelection


function Callback_insertRowsPostAction(){
	initRows=document.getElementById("table_global_vars").rows.length;
	var rowsCode="";
	MTstartServerComm();
		
	var tablecodeA="<table class='table' id='table_global_vars' width='100%'>";
		
	var tablecodeB="</table>";
	//devvar
		
	if(xmlResponse.getElementsByTagName("k").length > 0){
			var vecchie;
			try{
				vecchie =  document.getElementById("table_global_vars").tBodies[0].innerHTML;
			} catch(err)
			{vecchie= "";}

			var nuove;
			try{
				nuove = xmlResponse.getElementsByTagName("k")[0].childNodes[0].nodeValue;
			} catch(err)
			{nuove= "";}

			var totale = tablecodeA+	vecchie+nuove+tablecodeB;
			try{
				document.getElementById("div_table_global_vars").innerHTML = totale;
				
				initRows=document.getElementById("table_global_vars").rows.length;
				if(initRows>MAX_REPORT_VARS){
					MTstopServerComm();
					alert(document.getElementById("div_warning_max_variables").innerHTML);
					return;
				}
			} catch(err){alert(err.description);}
		}
	MTstopServerComm();
}

function addDevVar(){
	MTstartServerComm();
	setTimeout("addDevVarPRV();",100);
}

function getAllSelectedIndexes(combo){
	var i=0;
	var ret=new Array();
	while(combo.selectedIndex!=-1){
		ret[i++]=combo.options[combo.selectedIndex].id;
		var tmp = combo.selectedIndex;
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

function addDevVarPRV(){
	initRows=document.getElementById("table_global_vars").rows.length;
	if(!document.getElementById("d").checked){
		// if more than one device (of the same model!!) are selected
		var devret = getAllSelectedIndexes(document.getElementById("dev"));
		var devicesids = "";
		for(var i=0;i<devret.length;i++){
			devicesids+=document.getElementById(devret[i]).value+"-";
		}//for
		var varret = getAllSelectedIndexes(document.getElementById("var"));
		var variablesids = "";
		for(var j=0;j<varret.length;j++){
			variablesids+=document.getElementById(varret[j]).value+"-";
		}//for
		
		if (varret.length==0 || devret.length==0){
			MTstopServerComm();
		}
		
		var trows = document.getElementById("table_global_vars").rows;
		var count = trows.length;
		var exclude = "";
		addVariableToSend("currentvarlist");
		var excludeList=(document.getElementById("currentvarlist").value).split(";");
		for(var c = 0;c<excludeList.length;c++){
			var iid = excludeList[c].split("-");
			if(iid.length>1)
				exclude+=iid[1]+"-";
		}
		if(variablesids!=""&&devicesids!=""){
			CommSend("servlet/ajrefresh",
					"POST",
					"action="+RESOLVE_ROWS_FROM_IDVARMDL+"&devices="+devicesids+"&variables="+variablesids+"&exclude="+exclude,
					"insertRowsPostAction", 
					true);
		}
		//insertRows(rowsCode);
	}else{
		//else if only one device is selected
		if(document.getElementById("var")!=null){
			var devret = getAllSelectedIndexes(document.getElementById("dev"));
			var varret = getAllSelectedIndexes(document.getElementById("var"));
			if (varret.length==0 || devret.length==0){
				MTstopServerComm();
			}
			for(var ii=0;ii<varret.length;ii++) {
				if(document.getElementById(devret[0]+"-"+varret[ii])==null) {
					initRows++;
					if(initRows>MAX_REPORT_VARS+1){
						MTstopServerComm();
						alert(document.getElementById("div_warning_max_variables").innerHTML);
						return;
					}
					
					createRowCode(document.getElementById("table_global_vars"),
							devret[0].substring(3)+"-"+varret[ii].substring(3),
							document.getElementById(varret[ii]).childNodes[0].nodeValue,
							document.getElementById(devret[0]).childNodes[0].nodeValue);
				}
			}
		}
		MTstopServerComm();
	}
}

function isAllRowPresent(idvar){
	var e = document.getElementById(idvar);
	if (e!=null)
		return true;
	else return false;
}

function addVariableToSend(destination){
	var n=document.getElementById("table_global_vars").rows.length;
	var inputToSend="";
	var turboarray = document.getElementById("table_global_vars").getElementsByTagName("td");
	for(var i=0;i<turboarray.length;i=i+4){
		inputToSend+=turboarray[i].innerHTML+";";
	}//for
	document.getElementById(destination).value=inputToSend;
}

function createRowCode(table,idvar,varDesc,deviceSelectedDesc){
	var trovato = document.getElementById(idvar);
	if (trovato) return true;
	else{
		if(table.rows.length%2!=0 && rowColor==0 ){
			rowColor++;
		}
		var row = table.insertRow(table.rows.length);
		row.className = rowColor%2==0?"Row1":"Row2";
		rowColor++;
		row.id = idvar;
		var cell = row.insertCell(0);
		cell.className="standardTxt";
		cell.innerHTML  = "<div style=''><img src=\"images/actions/removesmall_on_black.png\" style=\"cursor:pointer;\" onclick=\"setModUser();deleteRow('table_global_vars','"+idvar+"');\"/></div>" 
		cell = row.insertCell(0);
		cell.className="standardTxt";
		cell.innerHTML  = "<div style=''>"+varDesc+"</div>";
		cell = row.insertCell(0);
		cell.className="standardTxt";
		cell.innerHTML  = "<div style=''>"+deviceSelectedDesc+"</div>";
		cell = row.insertCell(0);
		cell.className="standardTxt";
		cell.innerHTML  = idvar;
		cell.style.display = "none";
		return row;
	}
}

function deleteRow(tableId,rowId){
	var tb = document.getElementById(tableId).tBodies[0];
	var rw = document.getElementById(rowId);
	tb.removeChild(rw);
	var trs = tb.rows.length;
	for(i=0;i<trs;i++){
		tb.rows[i].className = (i%2==0?"Row1":"Row2");
	}
	
}//deleteRow

function emptyTable(idTable,fromRow){
	var tb = document.getElementById(idTable).tBodies[0];
	num=tb.rows.length;
	for(var i=0;i<num-1;i++){
		rw = tb.rows[1];	
		tb.removeChild(rw);
	}
}

function selectRowByLabel(idobject,label){
	
	if(document.getElementById(idobject)!=null){
		num_option=document.getElementById(idobject).options.length;
		for(i=1;i<num_option;i++){
			if(document.getElementById(idobject).options[i].value==label)
				document.getElementById(idobject).options[i].selected=true;
				
		}//for
	}//if
}//emptySelect


function emptySelect(idobject){
	
	if(document.getElementById(idobject)!=null){
		num_option=document.getElementById(idobject).options.length;
		for(i=num_option;i>=0;i--){
			document.getElementById(idobject).options[i]=null;
		}//for
	}//if
}//emptySelect

function add_template(){
	if(checkInsertModify()!=0)
		return;

	document.getElementById("command").value="add_template";
	addVariableToSend("variables");
	var ofrm = document.getElementById("frm_report");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function parameters_modify_variables(){
	MTstartServerComm();
	setTimeout(" parameters_modify_variablesPRV()",100);
}
function parameters_modify_variablesPRV(){
	if(checkInsertModify()!=0)
		return;
	document.getElementById("command").value="modifica_variabili";
	addVariableToSend("variables");

	var ofrm = document.getElementById("parameters_variable_form");
	if(ofrm == null)
		MTstopServerComm();
	ofrm.submit();
	
}

function parameters_delete_all_variables(){
var s = document.getElementById('deleteallparamquestion').value;
if (confirm(s) ){
	document.getElementById("command").value="cancella_tutto";

	var ofrm = document.getElementById("parameters_variable_form");
	if(ofrm == null)
		MTstopServerComm();
	ofrm.submit();
	}
}

function checkInsertModify(){

	return 0;
}
