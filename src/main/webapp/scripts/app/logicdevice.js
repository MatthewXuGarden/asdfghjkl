var selectRowMasterTable=-1;  //Id della riga selezionata
var selectRowSlaveDxTable=-1;  //riga selezionata in tabella
var selectRowSlaveSxTable=-1;  //riga selezionata in tabella
var rowColor = 0 ;

function initialize(){
	if (document.getElementById("control").value!="")
	{
		alert(document.getElementById("control").value);
	}
	if(document.getElementById("buttonEnable")!=null)
		enableAction(document.getElementById("buttonEnable").value);
	if(document.getElementById("deviceId").value!="")
		document.getElementById("input7").value=document.getElementById("deviceId").value;
	if(document.getElementById("input1")!=null)
		if(document.getElementById("input1").checked)
			return;
	if(document.getElementById("input2")!=null)
		if(document.getElementById("input2").checked){
			enableInput("7",false);
			return;
		}//if phy 
	enableInput("3,4,5,6,7",false);
	document.getElementById("input0").focus();
}//initialize


function loadDevice(info){
	var tmp= new Array();
	tmp=info.split(";;");
	document.getElementById("buttonEnable").value=3;
	enableAction(3);
	disableAction(1);
	disableAction(2);
	disableAction(4);
	selectRowMasterTable=-1;
	enableInput("0",true);
	document.getElementById("input0").value=Lsw1.mData[tmp[1]][3];
	document.getElementById("idDevice").value=tmp[0];
	document.getElementById("action").value="statusAllAction1";
	prepareToSubmit();
	var ofrm = document.getElementById("frm_logicdevice");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}//loadDevice

function selectDevice(info){
	var tmp = info.split(";;");
	if(selectRowMasterTable==tmp[0]){
		document.getElementById("buttonEnable").value=1;
		enableAction(1);
		disableAction(2);
		disableAction(3);
		disableAction(4);
		selectRowMasterTable=-1;
		enableInput("0,1,2",true);
		document.getElementById("input0").focus();
	}//if
	else{
		document.getElementById("buttonEnable").value=2;
		enableAction(2);
		disableAction(1);
		disableAction(3);
		enableAction(4);
		selectRowMasterTable=tmp[0];
		enableInput("0,1,2,3,4,5,6,7",false);
	}//else
	
	document.getElementById("input0").value="";
	document.getElementById("idDevice").value=tmp[0];
	document.getElementById("divLogicVariableTableDx").innerHTML="<table class='table' width='100%' cellspacing='1' cellpadding='1'><tbody></tbody></table>";
	document.getElementById("divLogicVariableTableSx").innerHTML="<table class='table' width='100%' cellspacing='1' cellpadding='1'><tbody></tbody></table>";
	document.getElementById("invisibleTableDx").innerHTML="<table><tbody></tbody></table>";
	document.getElementById("invisibleTableSx").innerHTML="<table><tbody></tbody></table>";
	document.getElementById("input1").checked=false;
	document.getElementById("input2").checked=false;
	document.getElementById("input3").checked=false;
	document.getElementById("input4").checked=false;
	document.getElementById("input5").checked=false;
	document.getElementById("input6").checked=false;
	document.getElementById("variableTable").value="";
}//loadDevice


function logicDeviceAction(actionType){
	var bodyTableInvisible=document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];
	for(;;){
		if(actionType=="add"){
			if((document.getElementById("input0").value=="")||(document.getElementById("input0").value==null)){
				alert(document.getElementById("noDescMsg").innerHTML);
				return;
			}//if
			if(bodyTableInvisible.rows.length==0){
				alert(document.getElementById("noVariablePresentMsg").innerHTML);
				return;
			}//if
			var numTotDevices = parseInt(document.getElementById("numTotDevices").innerHTML, 10);
			var numMaxDevices = parseInt(document.getElementById("numMaxDevices").innerHTML, 10);
			if( numTotDevices + 1 > numMaxDevices ) {
				alert(document.getElementById("maxLicenseLabel").innerHTML);
				return;
			}
			document.getElementById("action").value="add";
			break;
		}//if
		if(actionType=="del"){
			if (confirm(document.getElementById("confirmdevicedelete").value))
			{
				//check for dependencies
				CommSend("servlet/ajrefresh","POST","cmd=chkdeplogicdev&iddev="+document.getElementById("idDevice").value,"CheckLogicDev", true);
				//end check	
				document.getElementById("action").value="del";
				return;
			}
			else return false;
		
		}//if
		if(actionType=="mod"){
			if((document.getElementById("input0").value=="")||(document.getElementById("input0").value==null)){
				alert(document.getElementById("noDescMsg").innerHTML);
				return;
			}//if
			if(bodyTableInvisible.rows.length==0){
				alert(document.getElementById("noVariablePresentMsg").innerHTML);
				return;
			}//if
			createListModify();
			document.getElementById("action").value="mod";
			break;
		}//if
		if(actionType=="load")
		{
			var iddevice = document.getElementById("idDevice").value;
			for(var i=0;i<aValue.length;i++)
			{
				var info = aValue[i][2];
				if(iddevice == info.split(";;")[0])
				{
					loadDevice(info);
					break;
				}
			}
			break;
		}
		break;
	}//for
	prepareToSubmit();
	var ofrm = document.getElementById("frm_logicdevice");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}//logicDeviceAction


function enableInput(numberList,enable){
	var  inputList= new Array();
	inputList=numberList.split(",");
	for(var i=0;i<inputList.length;i++){
		if(document.getElementById("input"+inputList[i])!=null)
			document.getElementById("input"+inputList[i]).disabled=(enable==true?false:true);
	}//for
}//disableRadioButton

function checkDevType(type){
	for(;;){
		if(type=="physic"){
			enableInput("3,4,5,6,7",true);
			document.getElementById("input1").checked=true;
			document.getElementById("input3").checked=false;
			document.getElementById("input4").checked=false;
			document.getElementById("input5").checked=false;
			document.getElementById("input6").checked=false;
			break;
		}//if
		if(type=="logic"){
			enableInput("3,4,5",true);
			//enableInput("6,7",false);
			enableInput("6",true);
			enableInput("7",false);
			document.getElementById("input2").checked=true;
			document.getElementById("input3").checked=false;
			document.getElementById("input4").checked=false;
			document.getElementById("input5").checked=false;
			document.getElementById("input6").checked=false;
			break;
		}//if
		break;
	}//forswitch
	document.getElementById("divLogicVariableTableSx").innerHTML="<table class='table' width='100%' cellspacing='1' cellpadding='1'><tbody></tbody></table>";
}//checkDevType


function checkedInput(numberList,enable){
	var  inputList= new Array();
	inputList=numberList.split(",");
	for(var i=0;i<inputList.length;i++){
		document.getElementById("input"+inputList[i]).checked=(enable==true?false:true);
	}//for
}//checkedInput

function changeDevice(){
	if(!document.getElementById("input1").checked){
		return ;
	}
	var type ;
	if(document.getElementById("input3").checked){
		type = 3;
	}else if(document.getElementById("input4").checked){
		type = 1;
	}else if(document.getElementById("input5").checked){
		type = 2; 
	}else if(document.getElementById("input6").checked){
		type = 4;
	}
	else{
		return ;
	}
	
	getVars(type);
}//changeDevice

function checkVarType(type){
	if(document.getElementById("input1").checked){
		if((document.getElementById("input7").value=="")||(document.getElementById("input7").value==null)){
			//alert(document.getElementById("noDevicePresentMsg").innerHTML);
			return;
		}//if
	}//if
	
	getVars(type);
}//checkVarType

function getVars(type){
	document.getElementById("variableType").value=type;
	switch(Number(document.getElementById("buttonEnable").value)){
		case 1:
			document.getElementById("action").value="statusAddActionInteger";  //pressione dell'integer in modalit� +
		break;
		case 3:
			document.getElementById("action").value="statusModifyActionInteger";  //pressione dell'integer in modalit� v
		break;
	}//switch
	
	prepareToSubmit();
	var ofrm = document.getElementById("frm_logicdevice");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

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

function arrowAdd(){
	if(selectRowSlaveSxTable!=-1){
		var bodyTableInvisibleSx=document.getElementById("invisibleTableSx").getElementsByTagName("tbody")[0];	
		var bodyTableInvisibleDx=document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];	
		var bodyTableDx=document.getElementById("divLogicVariableTableDx").getElementsByTagName("tbody")[0];
		
		//controllo non sia presente la riga
		var idVariable=	bodyTableInvisibleSx.rows[selectRowSlaveSxTable].cells[0].firstChild.nodeValue;
		//controllo sul nome
		//var descrVar = bodyTableInvisibleSx.rows[selectRowSlaveSxTable].cells[0].firstChild.nodeValue;
		var descrVar = document.getElementById("divLogicVariableTableSx").getElementsByTagName("tbody")[0].rows[selectRowSlaveSxTable].cells[0].firstChild.nodeValue;
		var descdev = document.getElementById('input7').options[document.getElementById('input7').selectedIndex ].text;
		if(descdev!=null && descdev!='')
			descdev+=' - ';
		descrVar = descdev + descrVar;
		for(var i=0;i<bodyTableInvisibleDx.rows.length;i++)
		{
			var descrVar2 = bodyTableDx.rows[i].cells[0].firstChild.nodeValue;
			if(bodyTableInvisibleDx.rows[i].cells[0].firstChild.nodeValue==idVariable || descrVar==descrVar2)
			{
				alert(document.getElementById("rowPresentMsg").innerHTML);
				return;
			}//if
		}//for
	
		//aggiungo nella tabella nascosta
		var row=bodyTableInvisibleDx.insertRow(bodyTableInvisibleDx.rows.length);
		var textdesc;
		for(var i=0;i<bodyTableInvisibleSx.rows[selectRowSlaveSxTable].cells.length;i++){
			var cell = row.insertCell(i);
			var text=bodyTableInvisibleSx.rows[selectRowSlaveSxTable].cells[i].firstChild.nodeValue;
			//intervento tecnico
			if(i==3 && !document.getElementById('input7').disabled && document.getElementById('input1').checked)
			{
				//text = document.getElementById('').value
				textdesc = document.getElementById('input7').options[document.getElementById('input7').selectedIndex ].text +' - '+ text;
				text = textdesc;
				//alert(text);
			}
			//--intervento tecnico
			cell.appendChild(document.createTextNode(text));
		}//for
		
		//intervento tecnico
		var text0;
		if((!document.getElementById('input7').disabled) && (document.getElementById('input1').checked))
		{
			text0=textdesc;
		}
		else
		{
			text0=bodyTableInvisibleSx.rows[selectRowSlaveSxTable].cells[3].firstChild.nodeValue;
		}
		//--intervento tecnico
		var text1=bodyTableInvisibleSx.rows[selectRowSlaveSxTable].cells[2].firstChild.nodeValue;
		var text2=bodyTableInvisibleSx.rows[selectRowSlaveSxTable].cells[1].firstChild.nodeValue;
		
		text2=text2==0?document.getElementById("physic").innerHTML:document.getElementById("logic").innerHTML;
		rowColor++;
		var rowHTML="<tr  style=\"cursor:pointer\" class='"+(rowColor%2==0?"Row1":"Row2")+"' onclick=\"select('dx',this);\" ondblclick=\"deleteRow();\">"+
					"<td class=\"tdmini2\" width=\"80%\" align=\"left\">" +text0+
					"</td>"+"<td class=\"tdmini\" width=\"20%\">"+text1+"</td>"+"</tr>";
		document.getElementById("divLogicVariableTableDx").innerHTML="<table  class='table' width=\"100%\"><tbody>"+bodyTableDx.innerHTML+""+rowHTML+"</tbody></table>";
		var dx = document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];
		for (i = 0; i < dx.rows.length; i++){
			dx.rows[i].className = i%2==0?"Row1":"Row2";
		}
	}//if	
}//arrowAdd


function prepareToSubmit(){
	var bodyTableInvisible=document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];
	var tableData= new String();
	for(var i=0;i<bodyTableInvisible.rows.length;i++){
		tableData+=bodyTableInvisible.rows[i].cells[0].firstChild.nodeValue+"@"+
		bodyTableInvisible.rows[i].cells[3].firstChild.nodeValue+"@"+
		bodyTableInvisible.rows[i].cells[2].firstChild.nodeValue+"@"+
		bodyTableInvisible.rows[i].cells[1].firstChild.nodeValue+"@"+
		bodyTableInvisible.rows[i].cells[4].firstChild.nodeValue+"@"+
		bodyTableInvisible.rows[i].cells[5].firstChild.nodeValue+"@";
	}//for
	document.getElementById("variableTable").value=tableData;
}//prepareToSubmit

//Crea le 2 liste di variabili per la modifica
//Dato l'insieme originale degl'idsVar A es A={1,2,3,4} e dato quello di arrivo B={1,3,8}
//Abbiamo che le variabili da cancellare sono 1� A\(A intersecato B) e quelle da aggiungere sono 2� B\(A intersecato B)
//dall'esempio (A intersecato B)={1,3}  quindi la 1� Cancellare={2,4} e la 2� Aggiungere={8}

function createListModify(){
	var arrayToCancelled= new Array();
	var arrayToAdd= new Array();
	var arrayTypeToAdd= new Array();
	var arrayDescsToAdd= new Array();

	var bodyTableInvisibleDx=document.getElementById("invisibleTableDx").getElementsByTagName("tbody")[0];	
	
	arrayToCancelled=new String(document.getElementById("originalListVariableModify").value).split("@");
	
	
	for(var i=0;i<bodyTableInvisibleDx.rows.length;i++){
		arrayToAdd[i]=new String(bodyTableInvisibleDx.rows[i].cells[0].firstChild.nodeValue);
		arrayTypeToAdd[i]=new String(bodyTableInvisibleDx.rows[i].cells[5].firstChild.nodeValue);
		arrayDescsToAdd[i]=new String(bodyTableInvisibleDx.rows[i].cells[3].firstChild.nodeValue);
	}//for
	
	for(var i=0;i<arrayToAdd.length;i++){
		for(var j=0;j<arrayToCancelled.length;j++){
			if(arrayToAdd[i]==arrayToCancelled[j]){
				arrayToAdd[i]=arrayToCancelled[j]=-1;
				break;
			}//if
		}//for
	}//for

	var add= new String();
	var cancel= new String();

	for(var i=0;i<arrayToAdd.length;i++)
		if((arrayToAdd[i]!=-1)&&(arrayToAdd[i]!=""))
			add+=arrayToAdd[i]+"@"+arrayTypeToAdd[i]+"@"+arrayDescsToAdd[i]+"@";
	for(var i=0;i<arrayToCancelled.length;i++)
		if((arrayToCancelled[i]!=-1)&&(arrayToCancelled[i]!=""))
			cancel+=arrayToCancelled[i]+"@";
	
	document.getElementById("variableToAddInModify").value=add;
	document.getElementById("variableToDeleteInModify").value=cancel;
}//createListModify

function Callback_CheckLogicDev()
{
	rsp = xmlResponse.getElementsByTagName("MSG")[0].childNodes[0].nodeValue;
//	alert("rsp: "+rsp);
	if(rsp=="BAD")
		alert(document.getElementById("servererr").value);
	else if(rsp!="OK")
	{
		alert(rsp);
	}
	else if(rsp=="OK")
	{
		prepareToSubmit();
		var ofrm = document.getElementById("frm_logicdevice");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function logic_device_config(iddevice)
{
	top.frames['manager'].loadTrx('nop&folder=devdetail&bo=BDevDetail&type=click&iddev=' + iddevice + '&desc=ncode10');	
}
