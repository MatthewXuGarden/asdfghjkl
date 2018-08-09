// Sfacciato copia/incolla da Internet dell' HashMap onore e merito a Francesco Mele

// ------------------------------------------------- //
// ------------------- HASHMAP --------------------- //
// @description 
//			javascript implementation of a java HashMap
// @author Francesco Mele
// @version 2004-04-16
//
// ---> DEPENDS ON:
// no dependences
// ------------------------------------------------- //

// ---> INDEX OF METHODS:
// clear; containsKey; containsValue; get; isEmpty; put; remove; size; values

// ------------------------- //
// ---> defining Object

var rowcolor=0;
function HashMap()
{
	var arr = this;
	arr = new Array();
	this.size = arr.length;
}
// ------------------------- //
// ---> resets HashMap
HashMap.prototype.clear = function()
{
	HashMap = new Array();
	this.size = 0;
}
// ------------------------- //
// ---> return true if the HashMap has an object set with that key
HashMap.prototype.containsKey = function(key)
{
	if (HashMap[key]!=undefined) return true;
	else return false;
}
// ------------------------- //
// ---> return true if the HashMap has an object set with that value
HashMap.prototype.containsValue = function(val)
{
	for(var idx=0; idx<this.size; idx++){
		if (this[idx]==val) return true;
	}
	return false;
}
// ------------------------- //
// ---> returns the value to which the map maps specified key
HashMap.prototype.get = function(key) { return HashMap[key]; }

// ---> returns true if HashMap has no value(s)
HashMap.prototype.isEmpty = function()
{
	if (this.size==0) return true;
	else return false;
}
// ------------------------- //
// ---> sets a new value in the HashMap or overwrites an existing one
HashMap.prototype.put = function(key, val)
{
	if (HashMap[key]==undefined) this.size++;
	this.key = HashMap[key] = this[this.size-1] = val;
}
// ------------------------- //
// ---> removes an item with specified key (if it exists)
HashMap.prototype.remove = function(key)
{
	if (HashMap[key]==undefined) return;
	for (var idx=0; idx<this.size; idx++){
		if (this[idx]==HashMap[key]){
			this[idx] = undefined;
			for (var idx2=idx+1; idx2<this.size; idx2++){
				this[idx] = this[idx2];
			}
			break;
		}
	}
	this.size--;
	this.key = HashMap[key] = undefined;
}
// ------------------------- //
// ---> returns how many items HashMap has
HashMap.prototype.size = function() { return this.size; }
// ------------------------- //
// ---> returns an (unsorted) array of values contained in the HashMap
HashMap.prototype.values = function()
{
	var res = new Array();
	for (var idx=0; idx<this.size; idx++){
		res[idx] = this[idx];
	}
	return res;
}
// ---> HashMap - end
// ---------------------------------------------------- //


var LOAD_DEVICE_VAR=0;
var LOAD_DEVICE=1;
var LOAD_DEVICE_MDL=2;
var RESOLVE_ROWS_FROM_IDVARMDL=3;
var LOAD_REPORT_TO_MODIFY=4;

var MAX_REPORT_VARS=203; //Big Fabione Zizza perfetta!!!

var deviceSelected="";
var idDeviceSelected="";
var tab=0;
var idReportSelect="";
var lineSelected = null;
var clickState = 0;
var interval_sel = -99;
var bCustom = false;

// Alessandro : for removing custom option from combo interval of reports configuration

var optionCustomValue = "";
var optionCustomText = "";

function initialize()
{
	if(document.getElementById("tab").innerHTML=="tab1name"){
		tab=1;
	}
	else{
		tab=2;
		enableAction(1);
	}
	var r_involved = document.getElementById("r_involved");
	if (r_involved.value!="")
	{
		alert(document.getElementById("r_action").value +" "+ r_involved.value);
	}
}

function selectedLineReport(idLine)
{
	//alert(idLine);
	lineStatus(idLine);
	idReportSelect=idLine;
	//enableAction(1);
	if(tab==2){
		enableAction(1);
		enableAction(2);
		disableAction(3);
		enableAction(4);
	}
	if(tab==1){
		if((fdLocal==true)&&(document.getElementById("otype_"+idLine).value.toUpperCase()=="CSV")){
			disableAction(1);
		}
		change_layout(idLine);
		setFrequency(idLine);
	}
	//20091127-simon add
	//it will disable the calendar input box when the virtual Keyboard is open
	if(tab==1 && document.getElementById("vkeytype")!=null){
		document.getElementById("tester_day").readonly=true;
		document.getElementById("tester2_day").readonly=true;
		document.getElementById("tester_month").readonly=true;
		document.getElementById("tester2_month").readonly=true;
		document.getElementById("tester_year").readonly=true;
		document.getElementById("tester2_year").readonly=true;
	}
}
function modifyReport(idLine){
	idReportSelect=idLine;
	if(tab==2){
		reload_actions(LOAD_REPORT_TO_MODIFY);
		disableAction(1);
		disableAction(2);
		enableAction(3);
		disableAction(4);
	}
}


function reload_actions(action)
{	
	var cmbDevice=document.getElementById('dev');
	var cmdDeviceModel=document.getElementById('model');
	var toSend="";
	var functionToRecall="";

	if(action==LOAD_DEVICE_VAR){
		deviceSelected=cmbDevice.options[cmbDevice.selectedIndex].innerHTML;
		idDeviceSelected=cmbDevice.options[cmbDevice.selectedIndex].value;
		//alert("id dev selected "+idDeviceSelected);
		toSend=LOAD_DEVICE_VAR+"&iddevice="+cmbDevice.options[cmbDevice.selectedIndex].value+"&ishaccp="+document.getElementById("is_haccp").checked+"&interval="+document.getElementById("interval").value;
		functionToRecall="loadVariable";
	}
	else if(action==LOAD_DEVICE){
		emptySelect("var");
		cmdDeviceModel.selectedIndex=0;
		toSend=LOAD_DEVICE+"&iddevice=-1"+"&ishaccp="+document.getElementById("is_haccp").checked+"&interval="+document.getElementById("interval").value;
		functionToRecall="loadDevice";
	}
	else if(action==LOAD_DEVICE_MDL){
		//deviceSelected=" [ "+cmdDeviceModel.options[cmdDeviceModel.selectedIndex].innerHTML+ " ]";
		toSend=LOAD_DEVICE_MDL+"&iddevice="+cmdDeviceModel.options[cmdDeviceModel.selectedIndex].value+"&ishaccp="+document.getElementById("is_haccp").checked+"&interval="+document.getElementById("interval").value;
		functionToRecall="loadDeviceAndVariable";
	}
	else if(action==LOAD_REPORT_TO_MODIFY){
		//deviceSelected=" [ "+cmdDeviceModel.options[cmdDeviceModel.selectedIndex].innerHTML+ " ]";
		toSend=LOAD_REPORT_TO_MODIFY+"&iddevice="+cmdDeviceModel.options[cmdDeviceModel.selectedIndex].value+"&ishaccp="+document.getElementById("is_haccp").checked+"&interval="+document.getElementById("interval").value+"&idreport="+idReportSelect;
		functionToRecall="loadReport";
	}
		
	CommSend("servlet/ajrefresh",
			"POST",
			"action="+toSend,
			functionToRecall, 
			true);
}

function Callback_loadDeviceAndVariable(xmlResponse)
{
	document.getElementById("div_var").innerHTML=xmlResponse.getElementsByTagName("variable")[0].childNodes[0].nodeValue;
	document.getElementById("div_dev").innerHTML=xmlResponse.getElementsByTagName("device")[0].childNodes[0].nodeValue;
}

function Callback_loadVariable(xmlResponse)
{
	document.getElementById("div_var").innerHTML = xmlResponse.firstChild.firstChild.nodeValue;
}

function Callback_loadDevice(xmlResponse)
{
	document.getElementById("model").disabled=true;
	document.getElementById("div_dev").innerHTML = xmlResponse.firstChild.firstChild.nodeValue;
}

function Callback_loadReport(xmlResponse)
{
	document.getElementById("report_name").value=xmlResponse.getElementsByTagName("name")[0].childNodes[0].nodeValue;
	selectRowByLabel("layout",xmlResponse.getElementsByTagName("layout")[0].childNodes[0].nodeValue);
	selectRowByLabel("interval",xmlResponse.getElementsByTagName("timelength")[0].childNodes[0].nodeValue);
	selectRowByLabel("output",xmlResponse.getElementsByTagName("outputtype")[0].childNodes[0].nodeValue);
	
	var step=parseInt(xmlResponse.getElementsByTagName("step")[0].childNodes[0].nodeValue);
	var timelength=xmlResponse.getElementsByTagName("timelength")[0].childNodes[0].nodeValue; //interval
	// Alessandro : boolean value to use for setting options in the timelentgh combobox
	var scheduled = xmlResponse.getElementsByTagName("scheduled")[0].childNodes[0].nodeValue;	
	var haccp=xmlResponse.getElementsByTagName("haccp")[0].childNodes[0].nodeValue;
	var layout = xmlResponse.getElementsByTagName("layout")[0].childNodes[0].nodeValue;
	if (layout=="null")
		layout=-99;
	
	switch( step ) {
		case 0:
			document.getElementById("frequency").options[0].selected=true;
			document.getElementById("frequency").disabled=true;
			break;
		case 5:
			document.getElementById("frequency").options[1].selected=true;
			break;
		case 30:
			document.getElementById("frequency").options[2].selected=true;
			break;
		case 60:
			document.getElementById("frequency").options[3].selected=true;
			break;
		case 300:
			document.getElementById("frequency").options[4].selected=true;
			break;
		case 600:
			document.getElementById("frequency").options[5].selected=true;
			break;
		case 900:
			document.getElementById("frequency").options[6].selected=true;
			break;
		case 1800:
			document.getElementById("frequency").options[7].selected=true;
			break;
		case 3600:
			document.getElementById("frequency").options[8].selected=true;
			break;
		case 43200:
			document.getElementById("frequency").options[9].selected=true;
			break;
		case 86400:
			document.getElementById("frequency").options[10].selected=true;
			break;
		case -1:
			document.getElementById("frequency").options[11].selected=true;
			break;
	}
	onSelectFrequency(step);
	
	// Alessandro : if scheduled == true removes last option from timelength combobox
	var originalTimeLengthOptionsCount = document.getElementById("numintervaloptions").value;
	var timeLengthSelect = document.getElementById("interval");
	optionCustomValue = document.getElementById("customIntervalValue").value;
	optionCustomText = document.getElementById("customIntervalText").value;
	// only if the last option was been removed previously you need to add it again
	if (originalTimeLengthOptionsCount > timeLengthSelect.options.length){
		// add option of "custom reports"		
		timeLengthSelect.options.add(new Option(optionCustomText,optionCustomValue));
	}
	// remove last index option
	if (scheduled == 'true') timeLengthSelect.remove(timeLengthSelect.options.length-1); 		
	// if report was of type custom it must be selected
	if (timelength == -1) timeLengthSelect.selectedIndex = timeLengthSelect.options.length-1;

	
	// time table
	timeTableLoad(xmlResponse.getElementsByTagName("timeTableValue")[0].childNodes[0].nodeValue);
	
	if(timelength!=-99){
		document.getElementById("is_haccp").disabled=false;
		if (timelength!=0)
			document.getElementById("frequency").disabled=false;
		if(haccp=="false"){
			document.getElementById("is_haccp").checked=false;
		}
		else{
			document.getElementById("is_haccp").checked=true;
		}
	}
	
	
	
	emptyTable("table_global_vars",0);
	insertRows(xmlResponse.getElementsByTagName("table")[0].childNodes[0].nodeValue);
	
	// �BIOLO 
	//filter layout list
	
	filterLayouts(document.getElementById("interval").value);
	changeOutput();
	
	//restore layout value 
	document.getElementById('layout').value = layout;
	// END BIOLO
	interval_sel = document.getElementById("interval").value;
	
}


function changeDevice(obj){
	//alert(obj.options[obj.selectedIndex].value);
}//changeDevice



function enableModelSelection(){
	document.getElementById("model").disabled=false;
	emptySelect("dev");
	emptySelect("var");
}//enableModelSelection


function Callback_insertRowsPostAction(xmlResponse)
{
	//alert(xmlResponse.text);
	var initRows=document.getElementById("table_global_vars").rows.length;
	var hm = new HashMap();
	var data = xmlResponse.firstChild.firstChild.nodeValue;
	var datas=data.split("-");
	
	for(var i=0;i< datas.length-1;i++){
		//alert(datas[i]+"-"+datas[i+1]+"-->"+datas[i]+"-"+datas[i+2])
		hm.put(datas[i]+"-"+datas[i+1],datas[i]+"-"+datas[i+2]);
		i+=2;
	}
	var numDevice=document.getElementById("dev").options.length;
	var numVar=document.getElementById("var").options.length;
	var rowsCode="";
	for(i=0;i<numDevice;i++){
		if(document.getElementById("dev").options[i].selected==true){
			for(var j=0;j<numVar;j++){
				if(document.getElementById("var").options[j].selected==true){
					var devVarMapped=hm.get(document.getElementById("dev").options[i].value+"-"+document.getElementById("var").options[j].value);
					if (devVarMapped==null)   // BIOLO: exit if not valid code for the variable
							break;
					if(!isAllRowPresent(devVarMapped)){
						initRows++;
						if(initRows>MAX_REPORT_VARS+1){
							alert(document.getElementById("div_warning_max_variables").innerHTML);
							if(rowsCode!="")
								insertRows(rowsCode);
							return;
						}//if
						rowsCode+=createRowCode(devVarMapped,document.getElementById("var").options[j].innerHTML,document.getElementById("dev").options[i].innerHTML);
					}//if
				}//if
			}//for
		}//if
	}//for
	if(rowsCode!="")
		insertRows(rowsCode);
}

function addDevVar()
{
	var ishaccp = document.getElementById("is_haccp").checked;
	var initRows=document.getElementById("table_global_vars").rows.length;
	var devTab = document.getElementById("dev");
	var numDevice=devTab.options.length;
	var varTab = document.getElementById("var");
	var numVar= varTab.options.length;
	var rowsCode="";
	if(!document.getElementById("d").checked){
		for(var i=0;i<numDevice;i++){
			if(devTab.options[i].selected==true){
				for(var j=0;j<numVar;j++){
					if(varTab.options[j].selected==true){
						if(!isAllRowPresent(devTab.options[i].value+"-"+varTab.options[j].value)){
							rowsCode+=devTab.options[i].value+"-"+varTab.options[j].value+"-";
						}//if
					}//if
				}//for
			}//if
		}//for
//		alert("Riga Inserita "+rowsCode);
		if(rowsCode!=""){
			CommSend("servlet/ajrefresh",
					"POST",
					"action="+RESOLVE_ROWS_FROM_IDVARMDL+"&iddevice=-1"+"&ishaccp="+ishaccp+"&variables="+rowsCode+"&interval="+document.getElementById("interval").value,
					"insertRowsPostAction", 
					true);
		}
		//insertRows(rowsCode);
	}//if
	else{
		if(varTab!=null){
			numVar=varTab.options.length;
			for(i=0;i<numVar;i++){
				if(varTab.options[i].selected==true){
					if(!isAllRowPresent(idDeviceSelected+"-"+varTab.options[i].value)){
						initRows++;
						if(initRows>MAX_REPORT_VARS+1){
							alert(document.getElementById("div_warning_max_variables").innerHTML);
							if(rowsCode!="")
								insertRows(rowsCode);
							return;
						}//if
						rowsCode+=createRowCode(idDeviceSelected+"-"+varTab.options[i].value,varTab.options[i].innerHTML,deviceSelected);
					}//if
				}//if adding row
			}//for
			insertRows(rowsCode);
		}//if 
		}
}//addDevVar

function isAllRowPresent(idvar)
{
	//Biolo optimization
	if (document.getElementById(idvar)!=null)
		return true;
	else
		return false;
		
}

function addVariableToSend(){
	var n=document.getElementById("table_global_vars").rows.length;
	var inputToSend="";
	for(var i=0;i<n;i++){
		inputToSend+=document.getElementById("table_global_vars").tBodies[0].rows[i].cells[0].innerHTML+";";
			
	}//for
	document.getElementById("variables").value=inputToSend;
}

function createRowCode(idvar,varDesc,deviceSelectedDesc){
	
	if(document.getElementById("table_global_vars").rows.length%2!=0 && rowcolor==0){
		rowcolor++;
	}
	var row=
	"<tr id='"+idvar+"' class='tdGlobalVarsNoColor "+(rowcolor%2==0?"Row1":"Row2")+"'>" 
	+ "<td style='display:none'>"+idvar+"</td>"
	+ "<td><div style='width:250px'>"+deviceSelectedDesc+"</div></td>"
	+ "<td><div style='width:580px'>"+varDesc+"</div></td>"
	+ "<td align='center'><div style='width:70px'><img src=\"images/actions/removesmall_on_black.png\" style=\"cursor:pointer;\" onclick=\"deleteRow('table_global_vars','"+idvar+"')\"/></div><td>"
	+ "</tr>";
	rowcolor++;
	//alert(row);
	return row;
}

function deleteRow(tableId,rowId){
	var tb = document.getElementById(tableId).tBodies[0];
	var rw = document.getElementById(rowId);
	tb.removeChild(rw);
	var trs = tb.rows.length;
	for(i=0;i<trs;i++){
		tb.rows[i].className = "tdGlobalVarsNoColor "+(i%2==0?"Row1":"Row2");
	}
}//deleteRow


function deleteAllRows()
{
	if( confirm(document.getElementById("confirmremoveall").value) )
		emptyTable("table_global_vars", 1);
}


function insertRows(rows){
	document.getElementById("div_table_global_vars").innerHTML=
		"<table id='table_global_vars' cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"
		+"<tbody>"+document.getElementById("table_global_vars").tBodies[0].innerHTML+rows+"</tbody></table>";
}//insertRows

function emptyTable(idTable,fromRow){
	var tb = document.getElementById(idTable).tBodies[0];
	num=tb.rows.length;
	for(var i=0;i<num;i++){
		rw = tb.rows[0];	
		tb.removeChild(rw);
	}
}

function selectRowByLabel(idobject,label){
	
	if(document.getElementById(idobject)!=null){
		num_option=document.getElementById(idobject).options.length;
		for(i=0;i<num_option;i++){
			if(document.getElementById(idobject).options[i].value==label)
				document.getElementById(idobject).options[i].selected=true;
				
		}//for
	}//if
}//emptySelect


function emptySelect(idobject){
	
	if(document.getElementById(idobject)!=null){
		num_option=document.getElementById(idobject).options.length;
		//alert(""+num_option)
		for(i=num_option;i>=0;i--){
			document.getElementById(idobject).options[i]=null;
		}//for
	}//if
}//emptySelect


function checkDates()
{
	var y1 = document.getElementById("tester_year");
	var m1 = document.getElementById("tester_month");
	var d1 = document.getElementById("tester_day");
	var y2 = document.getElementById("tester2_year");
	var m2 = document.getElementById("tester2_month");
	var d2 = document.getElementById("tester2_day");
	if( parseInt(y2.value,10) < parseInt(y1.value,10)
		|| (parseInt(m2.value,10) < parseInt(m1.value,10) && parseInt(y2.value,10) == parseInt(y1.value,10))
		|| (parseInt(d2.value,10) < parseInt(d1.value,10) && parseInt(m2.value,10) == parseInt(m1.value,10) && parseInt(y2.value,10) == parseInt(y1.value,10))
		) {
		var aux = y1.value;
		y1.value = y2.value;
		y2.value = aux;
		aux = m1.value;
		m1.value = m2.value;
		m2.value = aux;
		aux = d1.value;
		d1.value = d2.value;
		d2.value = aux;
	}
}


function printReport()
{
	document.getElementById("command").value="printReport";
	document.getElementById("idReportSelect").value=idReportSelect;
	document.getElementById("timeTableValue").value = timeTableValue();
	
	var ofrm = document.getElementById("frm_report");
	if(ofrm != null) {
		MTstartServerComm();
		if( bCustom )
			checkDates();
		ofrm.submit();
	}
}

function add_template()
{
	//check existing description
	var report_name = document.getElementById("report_name").value;
	if (document.getElementById("code_"+report_name)!=null)
	{
		alert(document.getElementById("doublecode").value);
		return;
	}
	
	if(checkInsertModify()!=0)
		return;

	//check 1 variable needed
	var n_var=document.getElementById("table_global_vars").rows.length;
	if (n_var<1)
	{
		alert(document.getElementById("insert_var").value);
		return;
	}
	
	document.getElementById("command").value="add_template";
	addVariableToSend();
	document.getElementById("timeTableValue").value = timeTableValue();
	var ofrm = document.getElementById("frm_report");
	if(ofrm != null) {
		MTstartServerComm();
		ofrm.submit();
	}
}

function rem_template()
{
	if (confirm(document.getElementById("s_delete").value))
	{
		document.getElementById("command").value="rem_template";
		document.getElementById("idReportSelect").value=idReportSelect;
		var ofrm = document.getElementById("frm_report");
		if(ofrm != null) {
			MTstartServerComm();
			ofrm.submit();
		}
	}
}




function mod_template(){
	//check existing description
	var report_name = document.getElementById("report_name").value;
	if (document.getElementById("code_"+report_name)!=null )
	{
		if (document.getElementById("codebyid_"+idReportSelect).value!=report_name)
		{
			alert(document.getElementById("doublecode").value);
			return;
		}
	}
	
	if(checkInsertModify()!=0)
		return;
	
	//check 1 variable needed
	var n_var=document.getElementById("table_global_vars").rows.length;
	
	if(n_var>MAX_REPORT_VARS+1)
	{
		alert(document.getElementById("div_warning_max_variables").innerHTML);
		return;
	}	
	
	if (n_var<1)
	{
		alert(document.getElementById("insert_var").value);
		return;
	}
	
	document.getElementById("command").value="mod_template";
	document.getElementById("idReportSelect").value=idReportSelect;
	addVariableToSend();
	document.getElementById("timeTableValue").value = timeTableValue();
	var ofrm = document.getElementById("frm_report");
	if(ofrm != null) {
		MTstartServerComm();
		ofrm.submit();
	}
}

function cleanAllSettingTables()
{
	var idtable = "table_global_vars";
	var tb = document.getElementById(idtable).tBodies[0];
	var checked = !document.getElementById("is_haccp").checked;
	num=tb.rows.length;
	emptySelect("var");
	if (num>1)
	{
		if (confirm(document.getElementById("rusure").value))
		{
			divBody=document.getElementById(idtable).tBodies[0].rows[0].innerHTML;
			emptyTable(idtable,0);
		}
		else
		{
			document.getElementById("is_haccp").checked= checked;
		}
	}
}//cleanAllSettingTables

function changeInterval()
{
	var idtable = "table_global_vars";
	var tb = document.getElementById(idtable).tBodies[0];
	var num= tb.rows.length;
	emptySelect("var");
	var filter = false;
	
	if (num>1)  // "edit report" case and variable>0
	{
		if (confirm(document.getElementById("rusure").value))
		{
			reload_actions(1);
			document.getElementById("d").checked=true;
			emptySelect("var");
			divBody=document.getElementById(idtable).tBodies[0].rows[0].innerHTML;
			emptyTable(idtable,0);
			filter=true;
		}
		else
		{
			document.getElementById("interval").value = interval_sel;
		}
	}
	else  // "new report" case
		filter=true;
	
	interval_sel = document.getElementById("interval").value;
	if (filter)
		filterLayouts(document.getElementById("interval").value);
	
	// Frequency disable
	if (document.getElementById("interval").selectedIndex<2)
	{
		document.getElementById("frequency").selectedIndex=0;
		document.getElementById("frequency").disabled=true;
	}
	else
	{
		document.getElementById("frequency").disabled=false;
	}
	onSelectFrequency(document.getElementById("frequency").value);
	
	// HACCP disable
	if(document.getElementById("interval").value==-99){
		document.getElementById("is_haccp").disabled=true;
	}
	else{
		document.getElementById("is_haccp").disabled=false;
	}
	
}

function filterLayouts(interval){
	var optionsarray = document.getElementById("fulllayoutlist").options;
	var layouts = document.getElementById("layout");
	while(layouts.options.length>0){
		layouts.remove(0);
	}
	
	// BIOLO add "----------" option
	layouts.options.add(new Option(optionsarray[0].text,optionsarray[0].value));
	
	for(var idx=0; idx < optionsarray.length; idx++){
		var l=document.createElement('option');
		l.text=optionsarray[idx].value;
		l.value=optionsarray[idx].value;
		if(interval==0 && optionsarray[idx].value.indexOf("I_")==0){
			layouts.options.add(new Option(optionsarray[idx].value,optionsarray[idx].value));
		}
		if(interval!=0 && optionsarray[idx].value.indexOf("H_")==0){
			layouts.options.add(new Option(optionsarray[idx].value,optionsarray[idx].value));
		}
	}
}

function checkInsertModify()
{
	if(document.getElementById("report_name").value==""){
		alert(document.getElementById("div_warning_name").innerHTML);
		return -1;
	}
	
	if (document.getElementById("interval").value==-99)
	{
		alert(document.getElementById("div_warning_interval").innerHTML);
		return -1;
	}
	
	if((document.getElementById("interval").value!=0)&&(document.getElementById("frequency").selectedIndex==0))
	{
		alert(document.getElementById("div_warning_frequency").innerHTML);
		return -1;
	}
	
	if (document.getElementById("output").value=="PDF" && document.getElementById("layout").value==-99)
	{
		alert(document.getElementById("div_warning_layout").innerHTML);
		return -1;
	}
	
	if (!limit_ok_tab2())
		return -1;
		
	return 0;
}

function changeOutput()
{
	if (document.getElementById('output').value=="CSV" || document.getElementById('output').value=="HTML")
	{
		document.getElementById('layout').value=-99;
		document.getElementById('layout').disabled=true;
		
	}
	else
	{
		document.getElementById('layout').disabled=false;
		//changeInterval();
	}
}

function change_layout(idLine)
{
	/* case 0 : 	now;
       case -1: 	custom;
       case 86400:	daily;
	   case 604800:	weekly;
	 */
	bCustom = false;
	if (clickState==1)
	{
		var type = document.getElementById("type_"+idLine).value;
		if (type==0)
		{
			hide_all();
		}
		else if (type==-1)
		{
			bCustom = true;
			show_all();
		}
		else if(type == 86400)
		{
			show_dailycalendar();
		}
		else if(type == 604800)
		{
			show_weeklycalendar();
		}
	}
	else
		hide_all();
}

function setFrequency(idLine)
{
	if (clickState==1)
	{
		var freq = document.getElementById("freq_"+idLine).value;
		if(freq==0)
			document.getElementById("frequency").options[0].selected=true;
		else if(freq==5)
			document.getElementById("frequency").options[1].selected=true;
		else if(freq==30)
			document.getElementById("frequency").options[2].selected=true;
		else if(freq==60)
			document.getElementById("frequency").options[3].selected=true;
		else if(freq==300)
			document.getElementById("frequency").options[4].selected=true;
		else if(freq==600)
			document.getElementById("frequency").options[5].selected=true;
		else if(freq==900)
			document.getElementById("frequency").options[6].selected=true;
		else if(freq==1800)
			document.getElementById("frequency").options[7].selected=true;
		else if(freq==3600)
			document.getElementById("frequency").options[8].selected=true;
		else if(freq==43200)
			document.getElementById("frequency").options[9].selected=true;
		else if(freq==86400)
			document.getElementById("frequency").options[10].selected=true;
		else if(freq==-1) {
			document.getElementById("frequency").options[11].selected=true;
			var obj = document.getElementById("time_"+idLine);
			if( obj != null ) {
				timeTableLoad(obj.value);
			}
		}
		onSelectFrequency(freq);
	}
	else
	{
		document.getElementById("frequency").options[0].selected=true;
	}
}

function hide_all()
{
	//hide freq
	document.getElementById('freq_to_hide').style.visibility='hidden';
	
	//hide calendar
	document.getElementById('calendar1_to_hide').style.visibility='hidden';
	document.getElementById('calendar1_to_hide').style.display='none';
	document.getElementById('calendar2_to_hide').style.visibility='hidden';
	document.getElementById('calendar2_to_hide').style.display='none';
	document.getElementById('to_to_hide').style.visibility='hidden';
	document.getElementById('to_to_hide').style.display='none';
	
	//hide layer_time_table
	document.getElementById('layer_timeTable').style.visibility='hidden';	
	document.getElementById('layer_timeTable').style.display='none';
}

function show_all()
{
	renderCalendar1();
	//show freq
	document.getElementById('freq_to_hide').style.visibility='visible';
	
	//show calendar
	document.getElementById('calendar1_to_hide').style.visibility='visible';
	document.getElementById('calendar1_to_hide').style.display='block';
	document.getElementById('calendar2_to_hide').style.visibility='visible';
	document.getElementById('calendar2_to_hide').style.display='block';
	document.getElementById('to_to_hide').style.visibility='visible';
	document.getElementById('to_to_hide').style.display='table-cell';
	
	document.getElementById('s_to').style.visibility='hidden';
	document.getElementById('s_to').style.display='none';
	document.getElementById('s_from').style.visibility='visible';
	document.getElementById('s_from').style.display='block';
	
	//show layer_time_table
	document.getElementById('layer_timeTable').style.visibility='visible';	
	document.getElementById('layer_timeTable').style.display='block';	
}

function show_dailycalendar()
{
	renderCalendar1();
	//hide freq
	document.getElementById('freq_to_hide').style.visibility='visible';
	
	//hide calendar
	document.getElementById('calendar1_to_hide').style.visibility='visible';
	document.getElementById('calendar1_to_hide').style.display='block';
	document.getElementById('calendar2_to_hide').style.visibility='hidden';
	document.getElementById('calendar2_to_hide').style.display='none';
	document.getElementById('to_to_hide').style.visibility='hidden';
	document.getElementById('to_to_hide').style.display='none';
	
	document.getElementById('s_from').style.visibility='hidden';
	document.getElementById('s_from').style.display='none';
	document.getElementById('s_to').style.visibility='hidden';
	document.getElementById('s_to').style.display='none';
	
}
function show_weeklycalendar()
{
	renderCalendar1ForWeekly();
	//hide freq
	document.getElementById('freq_to_hide').style.visibility='visible';
	
	//hide calendar
	document.getElementById('calendar1_to_hide').style.visibility='visible';
	document.getElementById('calendar1_to_hide').style.display='block';
	document.getElementById('calendar2_to_hide').style.visibility='hidden';
	document.getElementById('calendar2_to_hide').style.display='none';
	document.getElementById('to_to_hide').style.visibility='hidden';
	document.getElementById('to_to_hide').style.display='none';
	
	document.getElementById('s_from').style.visibility='visible';
	document.getElementById('s_from').style.display='block';
	document.getElementById('s_to').style.visibility='hidden';
	document.getElementById('s_to').style.display='none';
	
}
function lineStatus(idLine)
{
	if (idLine==null) return true;
	if (lineSelected==idLine) 
	{
		if ((clickState==null)||(clickState==0))
		{
			clickState=1;
			enableAction(1);
			enableAction(2);
		}
		else
		{
			clickState=0;
			disableAction(1);
			disableAction(2);
		}
		document.getElementById("rdio"+lineSelected).checked = false;
		lineSelected = null;
	}
	else
	{
		if(lineSelected != null)
		{
			document.getElementById("rdio"+lineSelected).checked = false;
		}
		document.getElementById("rdio"+idLine).checked = true;
		clickState=1;
		enableAction(1);
		enableAction(2);
		lineSelected= idLine;
	}
}

function openOrSave(){
	var maxlimit = document.getElementById("maxlimit").value;
	
	if (maxlimit=="over")
	{
		alert(document.getElementById("over_limits").innerHTML);
		return false;
	}
	var commit_btn = document.getElementById("commit_btn").value;
	var path = document.getElementById("r_path").value;
	var msg = document.getElementById("save_confirm").value;
	
	if(commit_btn!=""){
		if( (commit_btn=="export") &&(fdLocal == true)){
			if(path == ""){
				msg = document.getElementById("save_error").value;
				alert(msg);
			}else{
				alert(msg + path);
			}
		}else{
			openDoc(path);
		}
	}

	return false;
}

function openDoc(sPath){
	var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+sPath;
	window.open(sUrl);
//	enableAction(2);
	MTstopServerComm();
}

function limit_ok_tab2()
{
	var MAX_SAMPLE = Number(document.getElementById("maxsamples").value);
	var idtable = "table_global_vars";
	var tb = document.getElementById(idtable).tBodies[0];
	var var_num= tb.rows.length -1;
	
	var freq = getFreq(document.getElementById("frequency").value);
	
	var timelenght = document.getElementById("interval").value;
	
	var samples = var_num*timelenght/freq;
	
	if (freq!=0)
	{
		if (samples<=MAX_SAMPLE)
			return true;
		else
		{
			alert(document.getElementById("over_limits").innerHTML);
			return false;
		}
	}
	return true;
}

function getFreq(index)
{
	if(index==0)
		return 0;
	else if(index==1)
		return 5;
	else if(index==2)
		return 30;
	else if(index==3)
		return 60;
	else if(index==4)
		return 300;
	else if(index==5)
		return 600;
	else if(index==6)
		return 900;
	else if(index==7)
		return 1800;
	else if(index==8)
		return 3600;
	else if(index==9)
		return 43200;
	else if(index==10)
		return 86400;
	else if(index==11)
		return -1;
	else return 0;
}


function onSelectFrequency(value)
{
	showLayer("layer_timeTable", parseInt(value) == -1);
}
function exportReport(){
	var v=document.getElementById("otype_"+idReportSelect);
	
	if((v!=null)){
		fdSaveFile('',(v.value).toLowerCase(),exportFile);
	}
}
function exportFile(local, path, filename){
//	alert(local+"-"+path+"-"+filename);
	document.getElementById("islocal").value=local;
	document.getElementById("exportfilepath").value=path;
	document.getElementById("exportfilename").value=filename;
	document.getElementById("command").value="exportReport";
	document.getElementById("idReportSelect").value=idReportSelect;
	document.getElementById("timeTableValue").value = timeTableValue();
	
	var ofrm = document.getElementById("frm_report");
	if(ofrm != null) {
		MTstartServerComm();
		if( bCustom )
			checkDates();
		ofrm.submit();
	}
}

//------------------------------subtab3.jsp-------------------------------------------------
function initializeSubtab3()
{
	enableAction(1);
}
function saveExportConfigure()
{
	var ofrm = document.getElementById("frm_export_configure");
	if(ofrm != null) {
		MTstartServerComm();
		ofrm.submit();
	}
}