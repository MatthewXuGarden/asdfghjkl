var lineselected = null;
var clickState = null;
var rowColor = 0 ;

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


function Callback_paramLstInit(src) {
	
	var xml = src;
	try {
		document.getElementById("div_var").innerHTML=xml.getElementsByTagName("variableInAction")[0].childNodes[0].nodeValue;
	} catch (err) {
		alert("Callback_paramLstInit: "+err.description);
	}
	MTstopServerComm();
}

function dblClickDevList(obj) {
	
	if(document.getElementById("m").checked){
		return;
	}
	dev = obj.value;
	param = "&model=-1&dev=" + dev;
	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=paramLstInit&section=parameterslist"+param, Callback_paramLstInit, false);
}

function changeDevMdl(obj){
	
	document.getElementById("m").checked="checked";
	document.getElementById("devLst").multiple=true;
	var model=obj.value;
	if(model==-1){
		emptyComponents(obj);
		return;
	}
	param="&model="+model+"&dev=-1";
	MTstartServerComm();	
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devLstInit&section=deviceslist"+param, Callback_devLstInit, false);
	new AjaxRequest("servlet/ajrefresh", "POST", "method=paramLstInit&section=parameterslist"+param, Callback_paramLstInit, false);
}


function Callback_devMdlInit(xml) {
	try {	
		if (xml.getElementsByTagName("devMdl").length > 0) {
			var devMdl = xml.getElementsByTagName("devMdl");
			var mdl = document.getElementById("model");
			makeEmptyList(mdl);
			var idx = -1;
			mdl.options.add(new Option("-------------------", "-1"));
			for(var i = 0; i < devMdl.length; i++) {
				var value = devMdl[i].getElementsByTagName("value")[0].firstChild.nodeValue;
				var text = devMdl[i].getElementsByTagName("text")[0].firstChild.nodeValue;
				var selected = devMdl[i].getElementsByTagName("selected")[0].firstChild.nodeValue;
				idx = devMdl[i].getElementsByTagName("idx")[0].firstChild.nodeValue;
				mdl.options.add(new Option(text, value));
				if (selected != "") {
					mdl.options[i + 1].selected = true;
					document.getElementById("m").checked = true;
					document.getElementById("devLst").multiple = true;
				}
			}
			
		}
	} catch(err) {
		alert("Callback_devMdlInit: error loading xml "+err.description);
	}
	MTstopServerComm();
}

function emptyComponents(obj){
	
	document.getElementById("model").disabled =false;
	var devLst=document.getElementById("devLst");
	makeEmptyList(devLst);
	var paramLst=document.getElementById("variable_combo_dest");
	if(paramLst!=null)
		makeEmptyList(paramLst);
	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devMdlInit&section=devMdl", Callback_devMdlInit, false);
}

function makeEmptyList(list){
	while(list.options.length > 0){
		list.removeChild(list.options[list.options.length - 1]);
	}	
}


function Callback_devLstInit(src) {
	var devLst = document.getElementById("devLst");
	makeEmptyList(devLst);
	var xml = src;
	try {
		document.getElementById("div_dev").innerHTML=xml.getElementsByTagName("variableInAction")[0].childNodes[0].nodeValue;
	} catch (err) {
		alert("Callback_devLstInit: "+err.description);
	}
	MTstopServerComm();
}

function getDevList(obj){
	document.getElementById("model").value="-1";
	document.getElementById("model").disabled =true;
	document.getElementById("devLst").multiple=false;
	document.getElementById("model").disabled=true;
	var paramLst=document.getElementById("variable_combo_dest");
	if(paramLst!=null)
		makeEmptyList(paramLst);
	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devLstInit&section=deviceslist", Callback_devLstInit, false);
}

function testAction()
{
	if (document.getElementById("notremoveaction"))
		if (document.getElementById("notremoveaction").value == "NO")
			alert(document.getElementById("actionnotremoved").value);
	
	if (document.getElementById("listacomm"))
		if (document.getElementById("listacomm").value != "")
			disableAction(1);
}

function selectedLine(line,table)
{	
	var idline = line.rowIndex;
	
	if (lineselected==idline)
	{
		if ((clickState==null)||(clickState==0))
		{
			clickState=1;
			document.getElementById(table).rows[idline].className="selectedRow";
		}
		else
		{
		clickState=0;
		document.getElementById(table).rows[idline].className="Row1";
		}
	}
	else
	{
		clickState=1;
		document.getElementById(table).rows[idline].className="selectedRow";
		if (lineselected!=null)
		{
			document.getElementById(table).rows[lineselected].className="Row1";
		}
		
	}
	lineselected= idline;
}


//SEZIONE ACTION BUTTON


function save_fax()
{
	var actioncode = document.getElementById("actioncode").value;
	if (actioncode<0)
	{
		alert(document.getElementById("nomodactionfromide").value);
	}
	else
	{
		var list2 = document.getElementById("fax2");
		var param = getListValue(list2);
		document.getElementById("param").value = param;
		document.getElementById("cmd").value="F";
		var ofrm = document.getElementById("frm_set_fax");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function save_sms()
{
	var actioncode = document.getElementById("actioncode").value;
	if (actioncode<0)
	{
		alert(document.getElementById("nomodactionfromide").value);
	}
	else
	{
		var list2 = document.getElementById("sms2");
		var param = getListValue(list2);
		document.getElementById("param").value = param;
		document.getElementById("cmd").value="S";
		var ofrm = document.getElementById("frm_set_sms");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function save_email()
{
	var actioncode = document.getElementById("actioncode").value;
	if (actioncode<0)
	{
		alert(document.getElementById("nomodactionfromide").value);
	}
	else
	{
		var list2 = document.getElementById("email2");
		var param = getListValue(list2);
		document.getElementById("param").value = param;
		document.getElementById("cmd").value="E";
		var ofrm = document.getElementById("frm_set_email");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function save_relay()
{
	var ids_relay = document.getElementById("ids_relay").value.split(";");
	var actioncode = document.getElementById("actioncode").value;
	if (actioncode<0)
	{
		alert(document.getElementById("nomodactionfromide").value);
	}
	else
	{
		var table = document.getElementById("relaytable");
		
		document.getElementById("cmd").value="L";
		var param = "";
		var h_id = null;
		var h_state = null;
		/*for (i=0;i<table.rows.length;i++)
		{
			
			//param = param + table.rows[i].cells[0].innerText + "=" + table.rows[i].cells[2].innerText + ";";
			hidden_field = document.getElementById("hr_"+i).value.split("_");
			h_id = hidden_field[0];
			h_state = hidden_field[1];
			param= param + h_id + "=" + h_state + ";";
		}*/
		for (i=0;i<ids_relay.length;i++)
		{
			if (document.getElementById("hr_"+ids_relay[i])!=null)
			{
				param= param + ids_relay[i] + "=" + document.getElementById("hr_"+ids_relay[i]).value + ";";
			}
		}
		
		if (param!="")
		{
			param = param.substr(0,param.length-1);
		}
		
		document.getElementById("param").value = param;
		var ofrm = document.getElementById("frm_set_relay");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}	

function save_variable() {
	var src="";
	var actioncode = document.getElementById("actioncode").value;
	if (actioncode<0) {
		alert(document.getElementById("nomodactionfromide").value);
	} else {
		var table = document.getElementById("setactionvariables");
		var param = "";
		if(document.getElementById("nonerow")==undefined){
			for (var i = 1;i<table.rows.length;i++)
			{
				if(table.rows[i].cells[2].id!=-1) {
					src = "id"+stringtrimmer(table.rows[i].cells[2].id.split("_")[1]);
				} else {
					src = stringtrimmer(table.rows[i].cells[3].innerHTML);
				}
				if(table.rows[i].cells[0].id.indexOf("mdl")!=-1){
					param = param + stringtrimmer(table.rows[i].cells[0].id) + "=" + src + ";";
				}else{
					param = param + stringtrimmer(table.rows[i].cells[0].id.split("_")[1]) + "=" + src + ";";
				}
			}
		}
		document.getElementById("param").value = param;
		var ofrm = document.getElementById("frm_set_variable");
		if(ofrm != null){
			MTstartServerComm();
			ofrm.submit();
		}
	}
}

function save_remote()
{
	var actioncode = document.getElementById("actioncode").value;
	if (actioncode<0)
	{
		alert(document.getElementById("nomodactionfromide").value);
	}
	else
	{
		var list2 = document.getElementById("remote2");
		var param = getListValue(list2);
		document.getElementById("param").value = param;
		document.getElementById("cmd").value="D";
		var ofrm = document.getElementById("frm_set_remote");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function save_print_window()
{
	var reportSelectValue = document.getElementById("report").value ;
	var reportStoreInAchive =document.getElementById("report_store").checked;
	var reportSendInAchive =document.getElementById("report_send").checked;
	if( reportSelectValue==0 && (reportStoreInAchive==true || reportSendInAchive==true ) ){
		alert(document.getElementById("report_achive_check").value);
		document.getElementById("report").focus();
		return ;
	}
	if( reportSelectValue!=0 && reportStoreInAchive==false && reportSendInAchive==false  ){
		alert(document.getElementById("report_box_check").value);
		document.getElementById("report").focus();
		return ;
	}
	
	var actioncode = document.getElementById("actioncode").value;
	if (actioncode<0)
	{
		alert(document.getElementById("nomodactionfromide").value);
	}
	else
	{
		document.getElementById("cmd").value="save_print_window";
		var list2 = document.getElementById("email2");
		var param = getListValue(list2);
		if( reportSendInAchive==true && (param=="" || param ==null) ){
			alert(document.getElementById("report_send_check").value);
			return ;
		}
		if(reportSendInAchive==false){
			document.getElementById("param").value = "";
		}else{
			document.getElementById("param").value = param;	
		}
		var ofrm = document.getElementById("frm_set_print_window");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function save_report()
{
	var reportSelectValue = document.getElementById("report").value ;
	var reportStoreInAchive =document.getElementById("report_store").checked;
	var reportSendInAchive =document.getElementById("report_send").checked;
	if( reportSelectValue==0 && (reportStoreInAchive==true || reportSendInAchive==true ) ){
		alert(document.getElementById("report_achive_check").value);
		document.getElementById("report").focus();
		return ;
	}
	
	if( reportSelectValue!=0 && reportStoreInAchive==false && reportSendInAchive==false  ){
		alert(document.getElementById("report_box_check").value);
		document.getElementById("report").focus();
		return ;
	}
	var actioncode = document.getElementById("actioncode").value;
	if (actioncode<0)
	{
		alert(document.getElementById("nomodactionfromide").value);
	}
	else
	{
		document.getElementById("cmd").value="save_report";
		var list2 = document.getElementById("email2");
		var param = getListValue(list2);
		if( reportSendInAchive==true && (param=="" || param ==null) ){
			alert(document.getElementById("report_send_check").value);
			return ;
		}
		if(reportSendInAchive==false){
			document.getElementById("param").value = "";
		}else{
			document.getElementById("param").value = param;	
		}
		var ofrm = document.getElementById("frm_set_report");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}


//SEZIONE SPOSTAMENTO ELEMENTI LISTBOX
/*
function to2notRemove1(obj)
{
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-3));
	var list1 = document.getElementById(idlistbox+'1');
	var list2 = document.getElementById(idlistbox+'2');
	var indice1 = list1.selectedIndex; 
	
	var sizeList2 = list2.length;
	if (indice1 < 0)
	{
    	alert(document.getElementById("nullselected").value);
    	return;
    }
    	
    var a = list1.options[indice1];
    var b = list2.options[sizeList2];
    var iscontained = false;
    for (i=0;i<sizeList2;i++)
    {
	    if (list2.options[i].value==a.value)
	    {
	    	alert(document.getElementById("doubleElement").value);
	    	iscontained = true;
	    	break;
	    }
	}
    
    if (iscontained==false)
    {
	    try
	    {
			var oOption = document.createElement("OPTION");
			oOption.text=a.text;
			oOption.value=a.value;
			list2.add(oOption,b);
			list1.selectedIndex=0;
		}
	    catch (e1)
	    {
			var oOption = document.createElement("OPTION");
			oOption.text=a.text;
			oOption.value=a.value;
			list2.add(oOption);
			list1.selectedIndex=0;
	    }
	    list2.selectedIndex=sizeList2;
	    list1.selectedIndex=0;
	    list2.focus();
	    
	}	
}

function to1Rem(obj)
{
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-3));
	var list1 = document.getElementById(idlistbox+'1');
	var list2 = document.getElementById(idlistbox+'2');
	var indice2 = list2.selectedIndex; 
		
	var sizeList1 = list1.length;
	if (indice2 < 0)
	{
    	//alert(document.getElementById("nullselected").value);
    	return;
    }
    	
    var a = list2.options[indice2];
    var b = list1.options[sizeList1];
    
	list2.remove(indice2);
    list1.selectedIndex=0;	
    list1.focus();
}

*/

//20090116 issue #5289 
//no upper limit on the items' number
function addRelay() {
	// "-1" means "no upper limit"
	addRelayMaxItemsChk(-1);
}


//20090116 issue #5289 
function addRelayMaxItemsChk(maxitems)
{
	var s_active = document.getElementById("active").value;
	var s_noactive = document.getElementById("noactive").value;
	var table = document.getElementById("relaytable");
	var selected_index= document.getElementById("relay_combo").selectedIndex;
	if (selected_index==-1)
	{
		alert(document.getElementById("nullselected").value);
		return true;
	}
	var relay = Number(document.getElementById("relay_combo").value);
	var s_relay = document.getElementById("relay_combo").options[selected_index].innerHTML;
	var bool = document.getElementById("digital");
	
	
	//presentation
	var ids_relay = document.getElementById("ids_relay").value.split(";");
	var size_table = table.rows.length;
	var b = false;
	
	// check on the upper limit of the items in the table #5289
	if (maxitems != -1 && size_table >= maxitems) {
		alert(document.getElementById("maxelements").value+" "+maxitems);
		return true;
	}		
	
	for (i=0;i<ids_relay.length;i++)
	{
		if ((document.getElementById("hr_"+ids_relay[i])!=null)&&(relay==ids_relay[i]))
		{
			b=true;
			break;
		}
	}
	
	if (b==true)
	{
		alert(document.getElementById("doubleElement").value);
	}
	else
	{
		table.insertRow(size_table);
		var row_added = table.rows[size_table];
		rowColor++;
		row_added.className="Row1";
		row_added.name="rel"+relay;
		row_added.id="rel"+relay;
		row_added.style.fontSize="10pt";

		row_added.onclick= function(){selectedLine(this,'relaytable');return false;};
		row_added.ondblclick = delRelay;
			
		row_added.insertCell(0);
		row_added.insertCell(1);
		
		row_added.cells[0].width='80%';
		row_added.cells[1].width='20%';
		row_added.cells[0].innerHTML = s_relay;
		if (bool.value==1)
		{
			row_added.cells[1].innerHTML = s_active + "<input type='hidden' value='1' id='hr_"+relay+"'/>" ;
		}
		else
		{
			row_added.cells[1].innerHTML = s_noactive + "<input type='hidden' value='0' id='hr_"+relay+"'/>" ;
		}
				
		bool.selectedIndex=0;
	}
	
	return true;
}

function delRelay()
{
	var table = document.getElementById("relaytable");
	
	if ((lineselected!=null)&&(lineselected!=-1))
	{
		table.deleteRow(lineselected);
		lineselected=null;
	}
	else
	{
		alert(document.getElementById("nullselected").value);
	}
}

function selectTemplate()
{
	var variable = document.getElementById("variable_combo").value;
	var variable_split = variable.split(";");
	if (variable_split.length>1)
	{
		if (variable_split[1] == "1") //digitale
			set_digital();
		else
		{
		if  (variable_split[1] == "2") //analogica
			set_analog();
		else
			set_int(); //intera: variable_split[1] == "3"
		}
	}
	else
	{
		set_not_digital();
	}
}

// 20090116 issue #5289 
// no upper limit on the items' number
function addVariable() {
	// "-1" means "no upper limit"
	addVariableMaxItemsChk(-1);
}

// 20090116 issue #5289 
function addVariableMaxItemsChk(maxitems)
{
	//
	var table = document.getElementById("variabletable");
	var selected_index = document.getElementById("variable_combo").selectedIndex;
	
	if (selected_index == -1)
	{
		alert(document.getElementById("nullselected").value);
		return true;
	}
	
	//presentation
	var tablesize = table.rows.length;
	var b = false;
	
	// check on the upper limit of the items in the table - BUG #5289
	if (maxitems != -1 && tablesize >= maxitems) {
		alert(document.getElementById("maxelements").value+" "+maxitems);
		return true;
	}	
	
	var variable = document.getElementById("variable_combo").value;
	var s_variable = document.getElementById("variable_combo").options[selected_index].innerHTML;
	var set_value = document.getElementById("set_value");
	
	if (set_value.value == "")
	{
		alert(document.getElementById("forcesetvalue").value);
		return true;
	}
	else
	if ((set_value.value.indexOf(".") != set_value.value.lastIndexOf(".")) || (set_value.value.charAt(set_value.value.length - 1) == "."))
	{
		set_value.value = "";
		return;
	}
	else
	if ((set_value.value.charAt(0) == "-") && (set_value.value.charAt(1) == "."))
	{
		set_value.value = "";
		return;
	}
	else
	if ((set_value.value.indexOf("-") != set_value.value.lastIndexOf("-")) || (set_value.value.charAt(set_value.value.length - 1) == "-"))
	{
		set_value.value = "";
		return;
	}
	else
	if ((set_value.value.lastIndexOf("-") > 0))
	{
		set_value.value = "";
		return;
	}
	
	if (set_value.value.charAt(0) == ".")
	{
		set_value.value = "0" + set_value.value;
	}
	
	var variable_split = variable.split(";");
	
	
	//presentation
	var size_table = table.rows.length;
	var b = false;	
	
	for (i=0;i<size_table;i++)
	{
		if (variable_split[0]==Number(table.rows[i].cells[0].innerText))
		{
			b=true;
			break;
		}
	}
	
	if (b==true)
	{
		alert(document.getElementById("doubleElement").value);
	}
	else
	{
		table.insertRow(size_table);
		var row_added = table.rows[size_table];
		row_added.className="Row1";
		row_added.name="rel"+variable_split[0];
		row_added.id="rel"+variable_split[0];
		
		
		row_added.onclick= function(){selectedLine(this,'variabletable');return false;};
			
		row_added.insertCell(0);
		row_added.insertCell(1);
		row_added.insertCell(2);
		
		row_added.cells[0].width='10%';
		row_added.cells[1].width='70%';
		row_added.cells[2].width='15%';
		row_added.cells[0].className="StandardTxt";
		row_added.cells[1].className="StandardTxt";
		row_added.cells[2].className="StandardTxt";
		row_added.cells[0].innerHTML = variable_split[0];
		row_added.cells[1].innerHTML = s_variable;
		row_added.cells[2].innerHTML = set_value.value;
		
		set_value.selectedIndex=0;
	}
}

function delVariable()
{
	var table = document.getElementById("variabletable");
	
	if ((lineselected!=null)&&(lineselected!=-1))
	{
		table.deleteRow(lineselected);
		lineselected=null;
	}
	else
	{
		alert(document.getElementById("nullselected").value);
	}
}

function set_digital()
{
	var table1 = document.getElementById("table1");
	var element_to_substitute = table1.rows[1].cells[1];
	element_to_substitute.innerHTML="";
	element_to_substitute.innerHTML="<SELECT id='set_value' name='set_value' style='width:100%;'><OPTION selected value='0'>0</OPTION><OPTION value='1'>1</OPTION></SELECT>";
}

function set_analog()
{
	var table1 = document.getElementById("table1");
	var element_to_substitute = table1.rows[1].cells[1];
	element_to_substitute.innerHTML="";
	element_to_substitute.innerHTML="<INPUT id='set_value' name='set_value' class='standardText' type='text' size='3' maxlength='5' style='width:100%;' onblur='checkOnlyAnalogOnBlur(this);' onkeydown='checkOnlyAnalog(this,event);' />";
}

function set_int()
{
	var table1 = document.getElementById("table1");
	var element_to_substitute = table1.rows[1].cells[1];
	element_to_substitute.innerHTML="";
	element_to_substitute.innerHTML="<INPUT id='set_value' name='set_value' class='standardText' type='text' size='3' maxlength='3' style='width:100%;' onblur='checkOnlyAnalogOnBlur(this);' onkeydown='checkOnlyNumber(this,event);' />";
}

function set_not_digital()
{
	var table1 = document.getElementById("table1");
	var element_to_substitute = table1.rows[1].cells[1];
	element_to_substitute.innerHTML="";
	element_to_substitute.innerHTML="<INPUT id='set_value' name='set_value' class='standardText' type='text' size='3' maxlength='5' style='width:100%;' onblur='checkOnlyAnalogOnBlur(this);' onkeydown='checkOnlyAnalog(this,event);' />";
}


//UTILITY

function getListValue(obj)
{
	var values = "";
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	var list2 = document.getElementById(idlistbox+'2');
	
	for (i=0;i<list2.length;i++)
	{
		values = values + list2.options[i].value;
		values = values + ";";
	}
	values = values.substring(0,(values.length-1));
	return values;
}

function reload_actions(source, target, rw, type)
{
//	var param = "";
//	for (i=0;i<table.rows.length;i++)
//	{
//		param = param + table.rows[i].cells[0].innerText + "=" + table.rows[i].cells[2].innerText + ";";
//	}
//	if (param!="")
//	{
//		param = param.substr(0,param.length-1);
//	}
//	document.getElementById("param").value = param;
//	document.getElementById("cmd").value="reload";
//	document.getElementById("iddevice").value=document.getElementById("device").value;
//	var frm_rules_actions = document.getElementById("frm_set_variable");
	
	CommSend("servlet/ajrefresh",
			"POST",
			"cmd=loadvars&"+
			"iddevice="+(source.value)+"&target="+target+
			"&rw="+rw+
			"&type="+type,
			"loadresult", 
			true);
}

function Callback_loadresult(){
//	alert(xmlResponse.text);//.getElementsByTagName("v").length);
//	alert(xmlResponse.getElementsByTagName("target")[0].childNodes[0].nodeValue);
	var target = document.getElementById(xmlResponse.firstChild.firstChild.firstChild.nodeValue);
	for(var i=target.options.length-1;i>=0;i--){
		target.remove(i);
	}
	var xmlVars = xmlResponse.firstChild.childNodes[1].childNodes;
	for(var k=0; k < xmlVars.length; k++){
		var xmlVar = xmlVars[k];
		var opt = document.createElement("option");
		opt.value = xmlVar.getAttribute("vid");
		opt.text = xmlVar.childNodes[0].nodeValue;
		opt.vartype = xmlVar.getAttribute("tp");
		opt.className = (k%2==0)?"Row1":"Row2";
		target.options.add(opt);
	}
}

function add_variable(){
	var table = document.getElementById("setactionvariables");
	var maxNumber = document.getElementById("maxvariablesitems").value;

	if(table.rows.length-1>=maxNumber){
		alert(document.getElementById("maxvariableerror").value+" "+document.getElementById("maxvariablesitems").value);
		return;
	}
	var valsrc;
	var txtsrc;
	
	if(document.getElementById("variable_combo_dest").selectedIndex<0 || document.getElementById("devLst").selectedIndex<0){
		alert(document.getElementById("seldestinationerror").value);
		return;		
	}
	if(document.getElementById("variable_combo_source").selectedIndex<0 &&
			document.getElementById("value_source").value==""){
		alert(document.getElementById("selsourceerror").value);
		return;
	}
	
	var sr_dev = document.getElementById("device_source");
	var txtsrcdev = "";
	
	if(document.getElementById("value_source").value!=null && document.getElementById("value_source").value!=""){
		valsrc=-1;
		txtsrc=document.getElementById("value_source").value;
	}else{
		valsrc =  document.getElementById("variable_combo_source").options[document.getElementById("variable_combo_source").selectedIndex].value;
		valsrc = sr_dev.options[sr_dev.selectedIndex].value+"_"+valsrc;
		txtsrc = document.getElementById("variable_combo_source").options[document.getElementById("variable_combo_source").selectedIndex].text;
		txtsrcdev = sr_dev.options[sr_dev.selectedIndex].text;
	}

	var data1 = document.getElementById("devLst");
	var data2 = document.getElementById("variable_combo_dest");

	var devret = getAllSelectedIndexes(data1);
	var varret = getAllSelectedIndexes(data2);
	
	if((table.rows.length-1 + devret.length*varret.length)>=maxNumber){
		alert(document.getElementById("maxvariableerror").value+" "+document.getElementById("maxvariablesitems").value);
		return;
	}
	var devtex = "";
	var devval = "";
	var vartex = "";
	var varval = "";
	var vartype = "";
	var canAdd = false;
	if(devret.length>0 && varret.length>0){
		for ( var i = 0; i < devret.length; i++) {
			okvar = true;
			devval =document.getElementById(devret[i]).value;
			devtex =document.getElementById(devret[i]).text;
			for ( var j = 0; j < varret.length; j++) {
				varval = document.getElementById(varret[j]).value;
				vartex = document.getElementById(varret[j]).text;
				vartype = document.getElementById(varret[j]).getAttribute("vartype") ? document.getElementById(varret[j]).getAttribute("vartype") : document.getElementById(varret[j]).itemId ;

					canAdd = addTableRowCheck(table, devval,
							devtex,
							varval,
							vartex,
							valsrc,
							txtsrc,
							txtsrcdev,
							vartype
							);
					if(!canAdd){
						return;
					}
			}
		}
	}
		
	if(canAdd){
		if(devret.length>0 && varret.length>0){
			for(var ii=0;ii<devret.length;ii++){
				devval =document.getElementById(devret[ii]).value;
				devtex =document.getElementById(devret[ii]).text;
				for(var jj=0;jj<varret.length;jj++){
					varval = document.getElementById(varret[jj]).value;
					vartex = document.getElementById(varret[jj]).text;
					vartype = document.getElementById(varret[jj]).getAttribute("vartype") ? document.getElementById(varret[jj]).getAttribute("vartype") : document.getElementById(varret[jj]).itemId ;
					addTableRow(table, devval,
							devtex,
							varval,
							vartex,
							valsrc,
							txtsrc,
							txtsrcdev,
							vartype
							);
				}//for
			}//for
		}
	}
	
}

function addTableRowCheck(tabl, dv1, dt1, dv2, dt2,vs,tvs,tds,typedest) {
	
	if((dt1+dt2)==(tds+tvs)){
		alert(document.getElementById("sourcedesterror").value);
		return false;
	}
	var typesrc=-1;
	if(document.getElementById("variable_combo_source").selectedIndex>=0){
		typesrc	= document.getElementById("variable_combo_source").options[document.getElementById("variable_combo_source").selectedIndex].vartype;
		if(typesrc!=typedest){
			alert(document.getElementById("differenttypeerror").value);
			return false;
		}		
	}else{
		var num = document.getElementById("value_source").value;
		typedest = parseInt(typedest);
		switch (typedest) {
			case 1: //digitale
				if(num != 0 && num!=1){
					alert(document.getElementById("differenttypeerror").value);
					return false;
				}
				break;
			case 2:
				if(num != parseFloat(num)){
					alert(document.getElementById("differenttypeerror").value);
					return false;
				}
				break;
			case 3:
				if(num != parseInt(num)){
					alert(document.getElementById("differenttypeerror").value);
					return false;
				}
				break;
			default:
				alert(document.getElementById("setactionerror").value);
				return false;
		}
	}
	
	var rows = document.getElementById("setactionvariables").rows;
	var inserted = false;
	for(var i = 1; i<rows.length; i++){
		if(document.getElementById("nonerow") == undefined){
			var dev =stringtrimmer(rows[i].cells[0].innerHTML);
			var vardes =stringtrimmer(rows[i].cells[1].innerHTML);
			if((stringtrimmer(dt1)+stringtrimmer(dt2))==(dev+vardes)){
				inserted = true;
			}
		}	
	}
	if(inserted){
		alert(document.getElementById("duplicateerror").value);
		return false;
	}
	
	return true;
}

function checkduplicates(vardest){
	var rows = document.getElementById("setactionvariables").rows;
	for(var kk = 1; kk<rows.length;kk++){
		if(rows[kk].cells[0].innerHTML==vardest) {
			alert(document.getElementById("duplicateerror").value);
			return false;
		}
	}
	return true;
}

function addTableRow(tabl, dv1, dt1, dv2, dt2,vs,tvs,tds,typedest) {
	
	

	if(document.getElementById("nonerow") != undefined){
		document.getElementById("setactionvariables").deleteRow(1);
	}
	var newrow = tabl.insertRow(tabl.rows.length);
	var cell1 = newrow.insertCell(0);
	cell1.id = dv1+"_"+dv2;
	cell1.className = "standardTxt";
	var cell2 = newrow.insertCell(1);
	cell2.className = "standardTxt";
	
	var cell4 = newrow.insertCell(2);
	cell4.className = "standardTxt";
	cell4.id = vs;
	var cell5 = newrow.insertCell(3);
	cell5.className = "standardTxt";
	var cell6 = newrow.insertCell(4);
	cell6.className = "standardTxt";
	cell6.style.textAlign="center";
	cell1.innerHTML = dt1;
	cell2.innerHTML = dt2;
	cell4.innerHTML = tds;
	cell5.innerHTML = tvs;
	cell6.innerHTML="<img src=\"images/actions/removesmall_on_black.png\" alt=\""+document.getElementById("delall").value+"\" />"
	cell6.title = document.getElementById("deltooltip").value;
	cell6.style.cursor="pointer";
	cell6.onclick = function() {removerow(this,true);};
	
	for(var i=0;i<tabl.rows.length;i++){
		tabl.rows[i].className = i%2==0?"Row1":"Row2";
	}
}




function removerow(obj, conf){
	if(conf == undefined || conf){
		var yes = confirm(document.getElementById("confirmremove").value);
		if(!yes){
			return;
		}
	}
	var table = document.getElementById("setactionvariables"); 
	table.deleteRow(obj.parentNode.rowIndex);
	if(table.rows.length<2){
		var emptyrow = table.insertRow(table.rows.length);
		emptyrow.className="Row1";
		emptyrow.id="nonerow";
		var emptycell = emptyrow.insertCell(0);
		emptycell.innerHTML = document.getElementById("novarconf").value;
		emptycell.colSpan = 6;
		emptycell.className = "standardTxt";
		emptycell.style.textAlign ="center"; 
	}
	
	for(var i=0;i<table.rows.length;i++){
		table.rows[i].className = i%2==0?"Row1":"Row2";
	}
	
}

function delall(){
	var yes = confirm(document.getElementById("confirmremoveall").value);
	if(yes){
		var table = document.getElementById("setactionvariables"); 
		while(document.getElementById("nonerow")==undefined){
			removerow(table.rows[table.rows.length-1].cells[4], false);
		}
	}
}

function makeempty(){
	document.getElementById("value_source").value="";
}

function makenoselection(){
	document.getElementById("variable_combo_source").selectedIndex=-1;
}

function haccpReport(obj)
{
	if (obj.checked==true)
	{
		document.getElementById("reportHaccp").disabled=false;
		document.getElementById("reportHistorical").selectedIndex=0;
		document.getElementById("reportHistorical").disabled=true;
		document.getElementById("chk_historical").checked=false;
	}
	else
	{
		document.getElementById("reportHaccp").selectedIndex=0;
		document.getElementById("reportHaccp").disabled=true;
		document.getElementById("reportHistorical").disabled=false;
		document.getElementById("chk_historical").checked=true;
	}
}

function historicalReport(obj)
{
	if (obj.checked==true)
	{
		document.getElementById("reportHistorical").disabled=false;
		document.getElementById("reportHaccp").selectedIndex=0;
		document.getElementById("reportHaccp").disabled=true;
		document.getElementById("chk_haccp").checked=false;
	}
	else
	{
		document.getElementById("reportHistorical").selectedIndex=0;
		document.getElementById("reportHistorical").disabled=true;
		document.getElementById("reportHaccp").disabled=false;
		document.getElementById("chk_haccp").checked=true;
	}
}

function OnDivScroll(idselect,size)
{
    var lstCollegeNames = document.getElementById(idselect.id);

    
    if (lstCollegeNames.options.length > size)
    {
        lstCollegeNames.size=lstCollegeNames.options.length;
    }
    else
    {
        lstCollegeNames.size=size;
    }
}

function OnSelectFocus(obj,size)
{
    if (document.getElementById("divCollegeNames").scrollLeft != 0)
    {
        document.getElementById("divCollegeNames").scrollLeft = 0;
    }

    var lstCollegeNames = obj;
    
    if( lstCollegeNames.options.length >size)
    {
        lstCollegeNames.focus();
        lstCollegeNames.size=size;
    }
}

function GotoFaxConfigure()
{
//	top.frames['manager'].loadTrx('nop&folder=setio&bo=BSetIo&type=click
}

// used to show/hide EMail addrbook in Report tab
function showLayer(szDivId, bShow)
{
	var obj = document.layers ? document.layers[szDivId]
		: document.getElementById ? document.getElementById(szDivId).style
			: document.all[szDivId].style;
	if( obj )
		obj.visibility = document.layers ? (bShow ? "show" : "hide")
			: (bShow ? "visible" : "hidden");
}
