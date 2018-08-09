var OPERANDS_SIZE = 255;	// size of cffunction.parameters db field

var selectRowMasterTable=-1;  //Id della riga selezionata
var selectRowSlaveDxTable=-1;  //riga selezionata in tabella
var selectRowSlaveSxTable=-1;  //riga selezionata in tabella

var deviceForDeleteRow="";

var ERROR_CODE=0;
var OK_CODE=1;
var rowColor = 0;

function initialize(){
	if(document.getElementById("buttonEnable")!=null)
		enableAction(document.getElementById("buttonEnable").value);
	if(document.getElementById("idDevice").value!="")
		document.getElementById("deviceList").value=document.getElementById("idDevice").value;
	if(document.getElementById("operationSelect").value!="")
		document.getElementById("operationList").value=document.getElementById("operationSelect").value;
	if(document.getElementById("unitMeasurementSelect").value!="")
		document.getElementById("unitMeasurementList").value=document.getElementById("unitMeasurementSelect").value;
	if(document.getElementById("decimalsSelect").value!="")
		document.getElementById("decimals").value=document.getElementById("decimalsSelect").value;
	if( document.getElementById("inputRadioAnalogic").checked )
		document.getElementById("decimals").disabled = false;
	else
		document.getElementById("decimals").disabled = true;
		
	document.getElementById("description").focus();
}//initialize

function loadVariable(info){
	enableAllElementPage(true);
	var tmp= new Array();
	tmp=info.split(";;");
	enableAction(3);
	disableAction(1);
	disableAction(2);
	disableAction(4);
	selectRowMasterTable=-1;
	document.getElementById("buttonEnable").value=3;
	document.getElementById("idVariable").value=tmp[0];
	document.getElementById("description").value=Lsw1.mData[tmp[1]][3];
	document.getElementById("variableType").value=tmp[2];
	document.getElementById("functionCode").value=tmp[4];
	document.getElementById("variabledeviceid").value = tmp[5];
	document.getElementById("action").value="statusModActionVariableType";
	var ofrm = document.getElementById("frm_logicvariable");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}//loadVariable

function selectVariable(info){
	var tmp= new Array();
	tmp=info.split(";;");
	document.getElementById("description").value="";
	deviceForDeleteRow=tmp[5];

	if(selectRowMasterTable==tmp[0]){
		document.getElementById("buttonEnable").value=1;
		enableAction(1);
		disableAction(2);
		disableAction(3);
		disableAction(4);
		selectRowMasterTable=-1;
		enableAllElementPage(true);
		document.getElementById("description").focus();
	}//if
	else{
		document.getElementById("buttonEnable").value=2;
		enableAction(2);
		disableAction(1);
		disableAction(3);
		enableAction(4);
		selectRowMasterTable=tmp[0];
		document.getElementById("idVariable").value=tmp[0];
		enableAllElementPage(false);
	}//else
	document.getElementById("divLogicVariableTableDx").innerHTML="<table class='table' width='100%' cellspacing='1' cellpadding='1'><tbody></tbody></table>";
	document.getElementById("divLogicVariableTableSx").innerHTML="<table class='table' width='100%' cellspacing='1' cellpadding='1'><tbody></tbody></table>";
	document.getElementById("invisibleTableDx").innerHTML="<table><tbody></tbody></table>";
	document.getElementById("invisibleTableSx").innerHTML="<table><tbody></tbody></table>";
}//selectVariable


function enableAllElementPage(arg){
	arg=!arg;
	document.getElementById("description").disabled=arg;
	document.getElementById("inputRadioInteger").disabled=arg;
	document.getElementById("inputRadioAnalogic").disabled=arg;
	document.getElementById("inputRadioDigital").disabled=arg;
	document.getElementById("deviceList").disabled=arg;
	document.getElementById("operationList").disabled=arg;
	document.getElementById("unitMeasurementList").disabled=arg;
	document.getElementById("inputRadioInteger").checked=false;
	document.getElementById("inputRadioAnalogic").checked=false;
	document.getElementById("inputRadioDigital").checked=false;
}//enableAllElementPage

function checkVarType(type){
	var constant = document.getElementById("constant");
	if(type == 1)
	{
		constant.disabled = false;
		constant.onkeydown=new Function("checkOnlyDigit(this,event)");
		constant.onblur = new Function("checkOnlyDigitOnBlur(this)");
	}
	else if(type == 2)
	{
		constant.disabled = false;
		constant.onkeydown=new Function("checkOnlyAnalog(this,event)");
		constant.onblur = new Function("checkOnlyAnalogOnBlur(this)");
	}
	else if(type == 3)
	{
		constant.disabled = false;
		constant.onkeydown=new Function("checkOnlyNumber(this,event)");
		constant.onblur = new Function("onlyNumberOnBlur(this)");
	}
	var oldType=document.getElementById("variableType").value;
	var bodyTableInvisibleDx=document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];	
	if(   (((oldType==3)||(oldType==2))&& ( (type==1)||(type==4)) )   
			||  ( ((type==3)||(type==2))&& ((oldType==1)||(oldType==4)) ) 
			      ){
		document.getElementById("operationSelect").value=0;
		if(bodyTableInvisibleDx.rows.length!=0){
			if(confirm(document.getElementById("deleteRowTable1Msg").innerHTML)==false){
				if(oldType==1)document.getElementById("inputRadioDigital").checked=true;
				if(oldType==2)document.getElementById("inputRadioAnalogic").checked=true;
				if(oldType==3)document.getElementById("inputRadioInteger").checked=true;
				return;
			}//if
			else{
				document.getElementById("divLogicVariableTableDx").innerHTML="<table class='table' width='100%' cellspacing='1' cellpadding='1'><tbody></tbody></table>";
				document.getElementById("invisibleTableDx").innerHTML="<table><tbody></tbody></table>";
			}//else
		}//if
	}//if cambio classe di tipo variabile

	if(document.getElementById("deviceList").selectedIndex!=0){
		document.getElementById("idDevice").value=document.getElementById("deviceList").value;
		document.getElementById("action").value="statusAddActionVariableType";
		document.getElementById("variableType").value=type;
		prepareToSubmit();
		var ofrm = document.getElementById("frm_logicvariable");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}//if
}//checkVarType

function changeDevice(){
	if(document.getElementById("deviceList").selectedIndex!=0){
		if((document.getElementById("inputRadioDigital").checked)
			||(document.getElementById("inputRadioAlarm").checked)
			||(document.getElementById("inputRadioAnalogic").checked)
			||(document.getElementById("inputRadioInteger").checked)){
			for(;;){
				if((document.getElementById("inputRadioInteger").checked)){
					document.getElementById("variableType").value=3;
					break;
				}//if
				if((document.getElementById("inputRadioAnalogic").checked)){
					document.getElementById("variableType").value=2;
					break;
				}//if
				if((document.getElementById("inputRadioDigital").checked)){
					document.getElementById("variableType").value=1;
					break;
				}//if
				if((document.getElementById("inputRadioAlarm").checked)){
					document.getElementById("variableType").value=4;
					break;
				}//if
				break;
			}//for
			document.getElementById("idDevice").value=document.getElementById("deviceList").value;
			document.getElementById("divLogicVariableTableSx").innerHTML="<table class='table' width='100%' cellspacing='1' cellpadding='1'><tbody></tbody></table>";
			document.getElementById("invisibleTableSx").innerHTML="<table><tbody></tbody></table>";
			document.getElementById("action").value="statusAddActionVariableType";
			prepareToSubmit();
			var ofrm = document.getElementById("frm_logicvariable");
			if(ofrm != null)
				MTstartServerComm();
			ofrm.submit();
		}//if
	}//if
	else{
		document.getElementById("divLogicVariableTableSx").innerHTML="<table class='table' width='100%' cellspacing='1' cellpadding='1'><tbody></tbody></table>";
		document.getElementById("invisibleTableSx").innerHTML="<table><tbody></tbody></table>";
		document.getElementById("operationSelect").value=0;
		document.getElementById("operationList").value=0;
		// Fix bug 7746: with no device in the combo the selected variable index must be re-initialized to -1
		selectRowSlaveSxTable = -1;
	}//else
}//changeDevice

function changeOperation(){
	var bodyTableInvisibleDx=document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];	
	if(bodyTableInvisibleDx.rows.length!=0){
		if(confirm(document.getElementById("deleteRowTableMsg").innerHTML)==false){
		document.getElementById("operationList").value=document.getElementById("operationSelect").value;
			return;
		}//if
	}//if
	var bodyTableInvisible=document.getElementById("invisibleTableOperation").getElementsByTagName("tbody")[0];
	
	document.getElementById("divLogicVariableTableDx").innerHTML="<table class='table' width='100%' cellspacing='1' cellpadding='1'><tbody></tbody></table>";
	document.getElementById("invisibleTableDx").innerHTML="<table><tbody></tbody></table>";
	document.getElementById("operationSelect").value=document.getElementById("operationList").value;
}//changeOperation

function changeUnitMeasurement(){
	document.getElementById("unitMeasurementSelect").value=document.getElementById("unitMeasurementList").value;
}//changeUnitOfMeasurement

function changeDecimals(){
	document.getElementById("decimalsSelect").value=document.getElementById("decimals").value;
}//changeUnitOfMeasurement


function checkOperandsOverflow(next)
{
	var bodyTableInvisible = document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];
	var szOperands = "";
	for(var i=0; i < bodyTableInvisible.rows.length; i++) {
		var operand = bodyTableInvisible.rows[i].cells[0].firstChild != null ? bodyTableInvisible.rows[i].cells[0].firstChild.nodeValue : "";
		if( operand != "" ) {
			if( szOperands.length > 0 )
				szOperands += ";";
			szOperands += "pk" + operand;
		}
		else {
			operand = bodyTableInvisible.rows[i].cells[2].firstChild != null ? bodyTableInvisible.rows[i].cells[2].firstChild.nodeValue : "";			
			if( operand != "" ) {
				if( szOperands.length > 0 )
					szOperands += ";";
				szOperands += operand;
			}
		}
	}
	if( szOperands.length > 0 )
		szOperands += ";";
	szOperands += next;
	return szOperands.length >= OPERANDS_SIZE;
}


function arrowAdd(){
	var constantO = document.getElementById("constant");
	var constant = document.getElementById("constant").value;
	
	if(selectRowSlaveSxTable!=-1){
		if(constant != "")
		{
			constantO.value = "";
		}
		var invisibleTable=document.getElementById("invisibleTableOperation").getElementsByTagName("tbody")[0];	
		var maxVariableNumber=invisibleTable.rows[document.getElementById("operationList").value].cells[1].firstChild.nodeValue;
		var bodyTableInvisibleSx=document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0];	
		var bodyTableInvisibleDx=document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];	
		var bodyTableDx=document.getElementById("divLogicVariableTableDx").getElementsByTagName("tbody")[0];

		if(maxVariableNumber!="n"){
			if(bodyTableInvisibleDx.rows.length==maxVariableNumber){
				alert(document.getElementById("operatorOverflowMsg").innerHTML);
				return;
			}//if	
		}//if
		//controllo non sia presente la riga
		var idVariable=	bodyTableInvisibleSx.rows[selectRowSlaveSxTable].cells[0].firstChild.nodeValue;
		for(var i=0;i<bodyTableInvisibleDx.rows.length;i++){
			if(bodyTableInvisibleDx.rows[i].cells[0].firstChild != null && bodyTableInvisibleDx.rows[i].cells[0].firstChild.nodeValue==idVariable){
				alert(document.getElementById("rowPresentMsg").innerHTML);
				return;
			}//if
		}//for
		
		// check operands size
		if( checkOperandsOverflow("pk" + bodyTableInvisibleSx.rows[selectRowSlaveSxTable].cells[0].firstChild.nodeValue) ) {
			alert(document.getElementById("operatorOverflowMsg").innerHTML);
			return;
		}
		
		//aggiungo nella tabella nascosta
		var row=bodyTableInvisibleDx.insertRow(bodyTableInvisibleDx.rows.length);
		var i=0;
		for(;i<bodyTableInvisibleSx.rows[selectRowSlaveSxTable].cells.length;i++){
			var cell = row.insertCell(i);
			var text=bodyTableInvisibleSx.rows[selectRowSlaveSxTable].cells[i].firstChild.nodeValue;
			cell.appendChild(document.createTextNode(text));
		}//for
	
		var text= document.getElementById(document.getElementById("deviceList").value).innerHTML;
		bodyTableInvisibleDx.rows[bodyTableInvisibleDx.rows.length-1].cells[4].firstChild.nodeValue=text;

		var text0=bodyTableInvisibleSx.rows[selectRowSlaveSxTable].cells[2].firstChild.nodeValue;
		var text1=text;
		rowColor++;
		var rowHTML="<tr  style=\"cursor:pointer\" class='"+(rowColor%2==0?"Row1":"Row2")+"' onclick=\"select('dx',this);\" ondblclick=\"deleteRow();\">"+
					"<td class=\"tdmini2\" width=\"60%\" align=\"left\">" +text0+
					"</td>"+"<td class=\"tdmini\" width=\"40%\" align=\"left\">"+text1+"</td>"+
					"</tr>";
		
		document.getElementById("divLogicVariableTableDx").innerHTML="<table  class='table' width=\"100%\"><tbody>"+bodyTableDx.innerHTML+""+rowHTML+"</tbody></table>";
	}//if
	else if(constant != "")
	{
		var typeO = document.getElementById("inputRadioInteger");
		if(typeO.checked == true)
		{
			onlyNumberOnBlur(constantO);
		}
		typeO = document.getElementById("inputRadioAnalogic");
		if(typeO.checked == true)
		{
			checkOnlyAnalogOnBlur(constantO);
		}
		typeO = document.getElementById("inputRadioDigital");
		if(typeO.checked == true)
		{
			checkOnlyDigitOnBlur(constantO);
		}
		if(document.getElementById("constant").value == "")
		{
			return;
		}
		invisibleTable=document.getElementById("invisibleTableOperation").getElementsByTagName("tbody")[0];	
		maxVariableNumber=invisibleTable.rows[document.getElementById("operationList").value].cells[1].firstChild.nodeValue;
		bodyTableInvisibleDx=document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];	
		bodyTableDx=document.getElementById("divLogicVariableTableDx").getElementsByTagName("tbody")[0];
		
		if(maxVariableNumber!="n"){
			if(bodyTableInvisibleDx.rows.length==maxVariableNumber){
				alert(document.getElementById("operatorOverflowMsg").innerHTML);
				return;
			}	
		}
		
		// check operands size
		if( checkOperandsOverflow("" + constant) ) {
			alert(document.getElementById("operatorOverflowMsg").innerHTML);
			return;
		}
		
		row=bodyTableInvisibleDx.insertRow(bodyTableInvisibleDx.rows.length);
		cell = row.insertCell(0);
		cell.appendChild(document.createTextNode(""));
		cell = row.insertCell(1);
		cell.appendChild(document.createTextNode(""));
		cell = row.insertCell(2);
		cell.appendChild(document.createTextNode(constant));
		cell = row.insertCell(3);
		cell.appendChild(document.createTextNode(""));
		cell = row.insertCell(4);
		cell.appendChild(document.createTextNode(""));
		rowColor++;
		rowHTML="<tr  style=\"cursor:pointer\" class='"+(rowColor%2==0?"Row1":"Row2")+"' onclick=\"select('dx',this);\" ondblclick=\"deleteRow();\">"+
		"<td class=\"tdmini2\" width=\"60%\" align=\"left\">"+constant+
		"</td>"+"<td class=\"tdmini\" width=\"40%\" align=\"left\"></td>"+
		"</tr>";
		document.getElementById("divLogicVariableTableDx").innerHTML="<table  class='table' width=\"100%\"><tbody>"+bodyTableDx.innerHTML+""+rowHTML+"</tbody></table>";
	}
	
	var dx = document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];
	for (i = 0; i < dx.rows.length; i++){
		dx.rows[i].className = i%2==0?"Row1":"Row2";
	}
}//arrowAdd


function deleteRow(){
	if(selectRowSlaveDxTable!=-1){
		var bodyTable=document.getElementById("divLogicVariableTableDx").getElementsByTagName("tbody")[0];
		var bodyTableInvisible=document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];
		bodyTable.deleteRow(selectRowSlaveDxTable);	
		bodyTableInvisible.deleteRow(selectRowSlaveDxTable);
		selectRowSlaveDxTable=-1;
		for (i = 0; i < bodyTable.rows.length; i++){
			bodyTable.rows[i].className = i%2==0?"Row1":"Row2";
		}
	}//if
}//deleteRow


function select(table,obj){
	var rowSelected=-1;
	rowSelected=(table=='dx')?selectRowSlaveDxTable:rowSelected=selectRowSlaveSxTable;
	var bodyTable=document.getElementById((table=='dx')?"divLogicVariableTableDx":"divLogicVariableTableSx").getElementsByTagName("tbody")[0];
	
	for (i = 0; i < bodyTable.rows.length; i++){
		if(bodyTable.rows[i]==obj){
			rowSelected=i;
		}	
		bodyTable.rows[i].className = i%2==0?"Row1":"Row2";
	}
	obj.className="selectedRow";
	
//	if(obj.className=="Row1"){
//		for (i = 0; i < bodyTable.rows.length; i++){
//			if(bodyTable.rows[i]==obj){
//				if(rowSelected!=-1)
//					bodyTable.rows[rowSelected].className="Row1";
//					rowSelected=i;
//				break;
//			}//if	
//		}//for
//		obj.className="selectedRow";
//	}//if
//	else{
//		obj.className="Row1";
//		rowSelected=-1;
//	}//else 
	
	selectRowSlaveDxTable=(table=='dx')?rowSelected:selectRowSlaveDxTable;
	selectRowSlaveSxTable=(table=='sx')?rowSelected:selectRowSlaveSxTable;
}//select

function unselectAllRow(table)
{
	var rowSelected=-1;
	rowSelected=(table=='dx')?selectRowSlaveDxTable:rowSelected=selectRowSlaveSxTable;
	var bodyTable=document.getElementById((table=='dx')?"divLogicVariableTableDx":"divLogicVariableTableSx").getElementsByTagName("tbody")[0];
	if(rowSelected != -1)
	{
		bodyTable.rows[rowSelected].className = "Row1";
	}
	rowSelected = -1;
	selectRowSlaveDxTable=(table=='dx')?rowSelected:selectRowSlaveDxTable;
	selectRowSlaveSxTable=(table=='sx')?rowSelected:selectRowSlaveSxTable;
}
function prepareToSubmit(){
	var bodyTableInvisible=document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];
	var tableData= new String();
	for(var i=0;i<bodyTableInvisible.rows.length;i++){
		tableData+=
		(bodyTableInvisible.rows[i].cells[0].firstChild!=null?bodyTableInvisible.rows[i].cells[0].firstChild.nodeValue:"")+"@"+
		(bodyTableInvisible.rows[i].cells[1].firstChild!=null?bodyTableInvisible.rows[i].cells[1].firstChild.nodeValue:"")+"@"+
		(bodyTableInvisible.rows[i].cells[2].firstChild!=null?bodyTableInvisible.rows[i].cells[2].firstChild.nodeValue:"")+"@"+
		(bodyTableInvisible.rows[i].cells[3].firstChild!=null?bodyTableInvisible.rows[i].cells[3].firstChild.nodeValue:"")+"@"+
		(bodyTableInvisible.rows[i].cells[4].firstChild!=null?bodyTableInvisible.rows[i].cells[4].firstChild.nodeValue:"")+"@";
	}//for
	if(tableData.length>0)
	{
		tableData = tableData.substring(0,tableData.length-1);
	}
	document.getElementById("variableTable").value=tableData;
}//prepareToSubmit




function logicVariableAction(actionType){
	var bodyTableInvisible=document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];
	var invisibleTableOperation=document.getElementById("invisibleTableOperation").getElementsByTagName("tbody")[0];	
	var maxVariableNumber=invisibleTableOperation.rows[document.getElementById("operationList").value].cells[1].firstChild.nodeValue;
	for(;;){
		if(actionType=="add"){
			if(checkDataToSend(bodyTableInvisible,maxVariableNumber)==ERROR_CODE)
				return;
			document.getElementById("operationSimbol").value=invisibleTableOperation.rows[document.getElementById("operationList").value].cells[0].firstChild.nodeValue;
			//document.getElementById("operationSimbol").value=document.getElementById("operationList").options[document.getElementById("operationList").selectedIndex].innerHTML;
			document.getElementById("unitMeasurementDescription").value=document.getElementById("unitMeasurementList").options[document.getElementById("unitMeasurementList").selectedIndex].innerHTML;
			document.getElementById("action").value="add";
			break;
		}//if
		if(actionType=="del"){
			if (confirm(document.getElementById("confirmvardelete").value))
			{
				if(deviceForDeleteRow!="0"){
					alert(document.getElementById("noDeleteVariablePresentMsg").innerHTML);
				return;
				}//if
				document.getElementById("action").value="del";
				break;
			}
			else return false;
		}//if
		if(actionType=="mod"){
			if(checkDataToSend(bodyTableInvisible,maxVariableNumber)==ERROR_CODE)
				return;
			//By Kevin
			//document.getElementById("operationSimbol").value=document.getElementById("operationList").options[document.getElementById("operationList").selectedIndex].innerHTML;
			document.getElementById("operationSimbol").value=invisibleTableOperation.rows[document.getElementById("operationList").value].cells[0].firstChild.nodeValue;
			document.getElementById("unitMeasurementDescription").value=document.getElementById("unitMeasurementList").options[document.getElementById("unitMeasurementList").selectedIndex].innerHTML;
			document.getElementById("action").value="mod";
			break;
		}//if
		if(actionType=="load")
		{
			var idvariable = document.getElementById("idVariable").value;
			for(var i=0;i<aValue.length;i++)
			{
				var info = aValue[i][2];
				if(idvariable == info.split(";;")[0])
				{
					loadVariable(info);
					break;
				}
			}
			break;
		}
		break;
	}//for
	prepareToSubmit();
	var ofrm = document.getElementById("frm_logicvariable");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}//logicDeviceAction

function checkDataToSend(tableData,maxVariableNumber){
	if((document.getElementById("description").value=="")||(document.getElementById("description").value==null)){
		alert(document.getElementById("noDescMsg").innerHTML);
		return ERROR_CODE;
	}//if
	if(tableData.rows.length==0){
		alert(document.getElementById("noVariablePresentMsg").innerHTML);
		return ERROR_CODE;
	}//if
	if(maxVariableNumber=="n"){
		if(tableData.rows.length<2){
			alert(document.getElementById("operatorInsertLessMsg").innerHTML);
    		return ERROR_CODE;
    	}//if
    }//if
   	else{
    	if(tableData.rows.length<Number(maxVariableNumber)){
			alert(document.getElementById("operatorInsertLessMsg").innerHTML);
    		return ERROR_CODE;
    	}//if
    }//else	
	return OK_CODE;
}//checkDataToSend
