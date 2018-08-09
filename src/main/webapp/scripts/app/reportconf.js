var selectedRow = -1;
var selectType="";

var checkboxList = null;
function initialize()
{
	var cmd = document.getElementById("cmd").value;
	enableAction(1);
	disableAction(2);
	disableAction(3);
	enableAll();
	if (document.getElementById("type_report").selectedIndex==1)
	{
		document.getElementById("step").disabled=true;
	}
	else
	{
		document.getElementById("step").disabled=false;
	}
		
	if ((cmd=="set")||(cmd=="set_reload"))
	{
		enableAction(3);
		disableAction(1);
	}
	
	var oActionVal = document.getElementById("codiceaz");
	var sActionVal = null;
	if(oActionVal != null)
		sActionVal = oActionVal.value;
	
	if(sActionVal != null &&  sActionVal != "null")
	{
		sActionVal = document.getElementById("messagedel").value + sActionVal;
		alert(sActionVal);
	}
}

function selectedLineReport(idreport)
{
	if(selectedRow==idreport)
	{
		initialize();
		selectedRow=-1;
	}
	else
	{
		enableAction(2);
		disableAction(1);
		disableAction(3);
		selectedRow=idreport;
		resetPage();
		document.getElementById("cmd").value="";
	}		
	document.getElementById("idreport").value=idreport;
}

function resetPage()
{
	document.getElementById("desc").value="";
	document.getElementById("desc").disabled=true;
	document.getElementById("type_report").selectedIndex=0;
	document.getElementById("type_report").disabled=true;
	document.getElementById("report1").innerHTML="";
	document.getElementById("report2").innerHTML="";
	document.getElementById("dev").selectedIndex=0;
	document.getElementById("dev").disabled=true;
	document.getElementById("report1").disabled=true;
	document.getElementById("report2").disabled=true;
	if (document.getElementById("ishistorical")!=null)
	{
		document.getElementById("step").value="";
		document.getElementById("step").disabled=true;
	}
}

function enableAll()
{
	document.getElementById("desc").disabled=false;
	document.getElementById("desc").focus();
	document.getElementById("type_report").disabled=false;
	document.getElementById("dev").disabled=false;
	document.getElementById("report1").disabled=false;
	document.getElementById("report2").disabled=false;
	if (document.getElementById("ishistorical")!=null)
	{
		document.getElementById("step").disabled=false;
	}
	
}

function countVars(num)
{
	var lenght = document.getElementById("report2").length;
	if (lenght>num)
	{
		alert(document.getElementById("toovars").value);
		return false;
	}
	else
	{
		to2notRemove1(document.getElementById("report2"));  //funzione di dbllistbox.js
	}
}

function modifyReport(idreport)
{
	document.getElementById("cmd").value="set";
	document.getElementById("idreport").value=idreport;
	var ofrm = document.getElementById("frm_reportconf");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}


function add_template(ishaccp)
{
	if (checkFields())
	{
		document.getElementById("ishaccp").value=ishaccp;
		var params = document.getElementById("params");
		params.value = getList2Value(document.getElementById("report2"));
		document.getElementById("cmd").value="add";
		var ofrm = document.getElementById("frm_reportconf");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function rem_template()
{
	if (confirm(document.getElementById("confirmdelete").value))
	{
		document.getElementById("idreport").value=selectedRow;
		document.getElementById("cmd").value="rem";
		var ofrm = document.getElementById("frm_reportconf");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function mod_template(ishaccp)
{
	if (checkFields())
	{
		document.getElementById("ishaccp").value=ishaccp;
		var params = document.getElementById("params");
		params.value = getList2Value(document.getElementById("report2"));
		document.getElementById("cmd").value="upd";
		var ofrm = document.getElementById("frm_reportconf");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}	

function reload()
{
	var cmd = document.getElementById("cmd").value;
	var ofrm = document.getElementById("frm_reportconf");
	var params = document.getElementById("params");
	params.value = getList2Value(document.getElementById("report2"));
	if (cmd=="set")
	{
		document.getElementById("cmd").value="set_reload";
	}
	else if (cmd=="set_reload")
	{
		document.getElementById("cmd").value="set_reload";
	}
	else 
	{
		document.getElementById("cmd").value="reload";
	}
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}

function checkFields()
{
	if (document.getElementById("desc").value=="")
	{
		alert(document.getElementById("insertdesc").value);
		return false;
	}
	if (document.getElementById("type_report").selectedIndex=="0")
	{
		alert(document.getElementById("selecttype").value);
		return false;
	}
	if ((document.getElementById("ishistorical")!=null)&&(document.getElementById("type_report").selectedIndex!=1))
	{
		if (document.getElementById("step").value=="")
		{
			alert(document.getElementById("insertpass").value);
			return false;
		}
		//if (document.getElementById("step").value<15)
		var combo = document.getElementById("template");
		var combotext = combo.options[combo.selectedIndex].text;
		if((document.getElementById("step").value<5 && combotext=='CSV')||
			(document.getElementById("step").value<15 && combotext!='CSV'))
		{
			alert(document.getElementById("lowpass").value);
			return false;
		}
	}
	if (document.getElementById("report2").length==0)
	{
		alert(document.getElementById("insertavariable").value);
		return false;
	}
	return true;
}

function changeType(obj)
{
	if (obj.selectedIndex==1)
	{
		document.getElementById("step").disabled = true;
	}
	else
	{
		document.getElementById("step").disabled = false;	
	}
}

/*
	Sezione HsPrint
*/
function initHsPrint(){
	var obj = document.getElementById("0div0");
	obj.style.textAlign = "center";
	obj.innerHTML = "<input type='checkbox' id='checkAllCheckbox' onclick='checkAllPrint(this)'/>";
	if(hasDataRow())
	{
		enableAction(4);
		enableAction(5);
	}
}
function hsSelectRow(idr)
{
	var chk = document.getElementById("chk"+idr);
	chk.checked = !chk.checked;
	if(chk.checked)
		addToCheckboxList(idr);
	else
	{
		document.getElementById("checkAllCheckbox").checked = false;
		removeFromCheckboxList(idr);
	}
	updatePrintActionButton();
}
function updatePrintActionButton()
{
	var numberChecked = 0;
	if(checkboxList != null)
	{
		numberChecked = checkboxList.length;
		if(numberChecked == 1)
		{
			var id = checkboxList[0];
		}
	}
	var isLocal = document.getElementById("isLocal").value;
	
	//for delete,export button
	if(numberChecked == 0)
		disableAll();
	else
	{
		enableAction(1);
		enableAction(3);
	}
	
	//for print
	if(numberChecked == 1)
		enableAction(2);
	else
		disableAction(2);
	
}
function hsDbSelectRow(idr)
{
	if(document.getElementById("currentrow").value != -1)
	{
		document.getElementById("filename").value = idr;
		enableAction(1);
	}
	else
		disableAction(1);
}

function hsClearRef()
{
	document.getElementById("currentrow").value = "-1";
	document.getElementById("filename").value = "";
	disableAction(1);
	disableAction(2);
	disableAction(3);
}

function buildPrintSelectId()
{
	var id = "";
	if(checkboxList == null)
		return id;
	for(var i=0;i<checkboxList.length;i++)
	{
		if(id == "")
			id = checkboxList[i];
		else
			id += "|"+checkboxList[i];
	}
	return id;
}
function deleteHsPrint()
{
	var id = buildPrintSelectId();
	if(id != "")
	{
		if(confirm(document.getElementById("msgdelete").value))
		{
			var oLswTmp = top.frames['body'].frames['bodytab'].oLswContainer.getLswByIdx(0);
			if(oLswTmp)
  			{
  				var params = "cmd=del&id="+id+"&currentpage="+oLswTmp.currentPage;;
  				CommSend("servlet/ajrefresh","POST",params,"hsRefreshPrint", true);
  			}
			uncheckByCheckboxList();
		}
	}
}
function Callback_hsRefreshPrint()
{
	var i=0;
	var xmlDevice = xmlResponse.getElementsByTagName("response");
	
	var sId   = "";
	var date  = "";
	var state = "";
	var code  = "";
	var path = "";
	
	var idx = 0;
	
	var arValues = new Array();
	var arTmp = new Array();
	
	if(xmlDevice.length != 0)
	{
		var rows = xmlDevice[0].getElementsByTagName("row");
		for(i=0; i<rows.length; i++)
		{
			idx = 0;
			arTmp = new Array();
			
			sId   = rows[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
			date  = rows[i].getElementsByTagName("creation")[0].childNodes[0].nodeValue;
			code  = rows[i].getElementsByTagName("action")[0].childNodes[0].nodeValue;
			path  = rows[i].getElementsByTagName("path")[0].childNodes[0].nodeValue;
			
			arTmp[idx++] = "";
			arTmp[idx++] = sId;
			arTmp[idx++] = "";
			
			arTmp[idx++] = "<div class='tableTouchCellImg'><input type=checkbox id='chk"+sId+"' onclick='chkClick(this)' value='"+path+"'></div>";
			arTmp[idx++] = "<div class='tableTouchCell'>"+date+"</div>";
			arTmp[idx++] = "<div class='tableTouchCell'>"+code+"</div>";
			arTmp[idx++] = "<div class='tableTouchCell'>"+path+"</div>";
						
			arValues[i] = arTmp;
		}
	}
	
	hsRefreshPrintTable(arValues);
}

function hsRefreshPrintTable(aValue)
{
	var oLswRef = null;
	try 
	{ 
		if(top.frames['body'].frames['bodytab'].oLswContainer != null)
			oLswRef = top.frames['body'].frames['bodytab'].oLswContainer.getLsw('LWCtDataName0');
		
		if(oLswRef != null)
			oLswRef.refresh(aValue.length,4,aValue,"hsSelectRow('$1')","","0");
	} 
	catch(e){}
	
	hsClearRef();
}
function checkAllPrint(obj)
{
	var checked = obj.checked;
	var objs = document.getElementsByTagName('input'); 
	for(var i=0;i<objs.length;i++)
	{ 
		if(objs[i].type == 'checkbox' && objs[i].id != null && objs[i].id.indexOf("chk") == 0)
		{ 
			objs[i].checked = checked;
			var id = objs[i].id;
			id = id.substring(3,id.length);
			if(checked)
				addToCheckboxList(id);
			else
				removeFromCheckboxList(id)
		}
	} 
	updatePrintActionButton();
}
function printHsPrint()
{
	var objs = document.getElementsByTagName('input');
	for(var i=0;i<objs.length;i++)
	{ 
		if(objs[i].type == 'checkbox' && objs[i].id != null && objs[i].id.indexOf("chk") == 0)
		{ 
			if(objs[i].checked)
			{
				openDoc(document.getElementById("filepath").value+objs[i].value);
			}
		}
	} 
}
function openDoc(strDoc){
	var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+strDoc;
	window.open(sUrl);
}
function exportHsPrint()
{
	fdSaveFile('','zip',hsprint_savefile);
	var date = new Date();
	fdSetFile("AlarmReports_"+date.format("yyyyMMddhhmmss"));
}
function hsprint_savefile(local, path, filename)
{
	var params="local="+local+"&cmd=exportpdf&newfilename="+filename+"&newpath="+path+"&ids="+buildPrintSelectId();
	CommSend("servlet/ajrefresh", "POST", params,"exportFile("+local+")",true);
	uncheckByCheckboxList();
}
function deleteAllHsPrint()
{
	if(confirm(document.getElementById("msgdeleteall").value))
	{
		var params = "cmd=delall";
		CommSend("servlet/ajrefresh","POST",params,"deleteAllHsPrint()", true);
	}
}
function Callback_deleteAllHsPrint()
{
	var ofrm = document.getElementById("form_print");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}
function exportAllHsPrint()
{
	if(confirm(document.getElementById("msgexportall").value))
	{
		fdSaveFile('','zip',exportAllPrintFile);
		var date = new Date();
		fdSetFile("AlarmReports_"+date.format("yyyyMMddhhmmss"));
	}
}
function exportAllPrintFile(local, path, filename)
{
	var params = "cmd=exportall&local="+local+"&newfilename="+filename+"&newpath="+path;
	CommSend("servlet/ajrefresh","POST",params,"exportFile("+local+")", true);
}
/**
* Per ridimensionare la tabella HACCP e Storico
*/
function resizeTableReport()
{
	var hdev = MTcalcObjectHeight("trReportList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev);
	}
}


//--------------------------------------HS Report---------------------------------------------------
function initHsReport(){
	var obj = document.getElementById("0div0");
	obj.style.textAlign = "center";
	obj.innerHTML = "<input type='checkbox' id='checkAllCheckbox' onclick='checkAll(this)'/>";
	if(hasDataRow())
	{
		enableAction(4);
		enableAction(5);
	}
}
function hsFilterReport(oradio)
{
	document.getElementById("hsfilter").value = oradio.value;
	var params = "fil="+oradio.value;
	CommSend("servlet/ajrefresh","POST",params,"hsRefreshRadio", true);
	uncheckByCheckboxList();
}
function Callback_hsRefreshRadio()
{
	var oLswTmp = top.frames['body'].frames['bodytab'].oLswContainer.getLswByIdx(0);
	oLswTmp.currentPage = 1;
	oLswTmp.updatePage("pagenumber");
}
function buildSelectId()
{
	var id = "";
	if(checkboxList == null)
		return id;
	for(var i=0;i<checkboxList.length;i++)
	{
		var str = checkboxList[i];
		var parameters = parseParameters(str);
		if(id == "")
			id = parameters[0];
		else
			id += "|"+parameters[0];
	}
	return id;
}
function deleteHsReport()
{
	var id = buildSelectId();
	if(id != "")
	{
		if(confirm(document.getElementById("msgdelete").value))
		{
			var oLswTmp = top.frames['body'].frames['bodytab'].oLswContainer.getLswByIdx(0);
			var params = "cmd=del&id="+id+"&fil="+document.getElementById("hsfilter").value+"&currentpage="+oLswTmp.currentPage;
			CommSend("servlet/ajrefresh","POST",params,"hsRefreshTable", true);
			uncheckByCheckboxList();
		}
	}
}
function selectHSRow(str){
	var chk = document.getElementById("chk"+str);
	chk.checked = !chk.checked;
	if(chk.checked)
		addToCheckboxList(str);
	else
	{
		document.getElementById("checkAllCheckbox").checked = false;
		removeFromCheckboxList(str);
	}
	updateActionButton();
}
function printHsReport(){
	var objs = document.getElementsByTagName('input');
	for(var i=0;i<objs.length;i++)
	{ 
		if(objs[i].type == 'checkbox' && objs[i].id != null && objs[i].id.indexOf("chk") == 0)
		{ 
			if(objs[i].checked)
			{
				var parameters = parseParameters(objs[i].id);
				openDoc(document.getElementById("filepath").value+parameters[2]);
			}
		}
	} 
}
function exportHsReport(){
	fdSaveFile('','zip',exportFile);
	var date = new Date();
	fdSetFile("LogReports_"+date.format("yyyyMMddhhmmss"));
}
function exportFile(local, path, filename){
	var params="local="+local+"&cmd=savefile&newfilename="+filename+"&newpath="+path+"&ids="+buildSelectId();
	CommSend("servlet/ajrefresh", "POST", params,"exportFile("+local+")",true);
	uncheckByCheckboxList();
}
function Callback_exportFile(local){
	MTstopServerComm();
	if (xmlResponse!=null && xmlResponse.getElementsByTagName("file")[0] != null)
	{
		var filename = xmlResponse.getElementsByTagName("file")[0].childNodes[0].nodeValue;
		var msg = "";
		if(filename == "ERROR")
		{
			msg = document.getElementById("save_error").value;
			alert(msg);
		}
		else
		{
			if(local == true)
			{
					msg = document.getElementById("save_confirm").value;
					alert(msg + filename);
			}
			else
			{
				var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
				window.open(sUrl);
			}
		}
	}
}
function Callback_hsRefreshTable()
{
	selectType="";
	disableAction(1);
	disableAction(2);
	disableAction(3);
	var i=0;
	var xmlDevice = xmlResponse.getElementsByTagName("response");
	
	var sId = "";
	var code = "";
	var type = "";
	var step = "";
	var style = "";
	var from  = "";
	var to  = "";
	var path = "";
	var idx = 0;
	var arValues = new Array();
	var arTmp = new Array();
	if(xmlDevice.length != 0)
	{
		var rows = xmlDevice[0].getElementsByTagName("row");
		for(i=0; i<rows.length; i++)
		{
			idx = 0;
			arTmp = new Array();
			sId = rows[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
			code = rows[i].getElementsByTagName("code")[0].childNodes[0].nodeValue;
			type = rows[i].getElementsByTagName("type")[0].childNodes[0].nodeValue;
			step = rows[i].getElementsByTagName("step")[0].childNodes[0].nodeValue;
			style = rows[i].getElementsByTagName("style")[0].childNodes[0].nodeValue;
			from = rows[i].getElementsByTagName("from")[0].childNodes[0].nodeValue;
			to = rows[i].getElementsByTagName("to")[0].childNodes[0].nodeValue;
			path = rows[i].getElementsByTagName("path")[0].childNodes[0].nodeValue;
			
			arTmp[idx++] = "";
			arTmp[idx++] = sId+"|"+style+"|"+path;
			arTmp[idx++] = "";
			arTmp[idx++] = "<div class='tableTouchCellImg'><input type='checkbox' id='chk"+sId+"|"+style+"|"+path+"' onclick='chkClick(this)'></div>";
			arTmp[idx++] = "<div class='tableTouchCell'>"+code+"</div>";
			arTmp[idx++] = "<div class='tableTouchCell'>"+from+"</div>";
			arTmp[idx++] = "<div class='tableTouchCell'>"+to+"</div>";
			
			if(style=='Log')
			{
				arTmp[idx++] = "<div class='tableTouchCell'>"+document.getElementById("log_str").value+"</div>";
			}
			if(style=='HACCP')
			{
				arTmp[idx++] = "<div class='tableTouchCell'>"+document.getElementById("haccp_str").value+"</div>";
			}
			
			
			if(type.toUpperCase()=='PDF'){
				arTmp[idx++] = "<div class='tableTouchCellImg'><img src=\"images/actions/pdf2_on.png\"></div>";
			}else if(type.toUpperCase()=='HTML'){
				arTmp[idx++] = "<div class='tableTouchCellImg'><img src=\"images/actions/html_img.png\"></div>";
			}else if(type.toUpperCase()=='CSV'){
				arTmp[idx++] = "<div class='tableTouchCellImg'><img src=\"images/actions/csv_img.png\"></div>";
			}else{
				arTmp[idx++] = "<div class='tableTouchCellImg'><img src=\"images/actions/report_on.png\"></div>";
			}			
			arValues[i] = arTmp;
		}
	}
	
	hsRefreshReportTable(arValues);
}

function hsRefreshReportTable(aValue)
{
	var oLswRef = null;
	try 
	{ 
		if(top.frames['body'].frames['bodytab'].oLswContainer != null)
			oLswRef = top.frames['body'].frames['bodytab'].oLswContainer.getLsw('LWCtDataName0');
		
		if(oLswRef != null)
			oLswRef.refresh(aValue.length,6,aValue,"selectHSRow('$1')","","0");
	} 
	catch(e){}
	
	hsClearRef();
}
function checkAll(obj)
{
	var checked = obj.checked;
	var objs = document.getElementsByTagName('input'); 
	for(var i=0;i<objs.length;i++)
	{ 
		if(objs[i].type == 'checkbox' && objs[i].id != null && objs[i].id.indexOf("chk") == 0)
		{ 
			objs[i].checked = checked;
			var id = objs[i].id;
			id = id.substring(3,id.length);
			if(checked)
				addToCheckboxList(id);
			else
				removeFromCheckboxList(id)
		}
	} 
	updateActionButton();
}
function parseParameters(str)
{
	if(str.indexOf("chk") == 0)
	{
		var result = str.split("|");
		result[0] = result[0].substring(3,result[0].length);
		return result;
	}
	else
	{
		return str.split("|");
	}
}
function updateActionButton()
{
	var numberChecked = 0;
	var type = "";
	if(checkboxList != null)
	{
		numberChecked = checkboxList.length;
		if(numberChecked == 1)
		{
			var id = checkboxList[0];
			var parameters = parseParameters(id);
			type = parameters[1];
		}
	}
	var isLocal = document.getElementById("isLocal").value;
	
	//for delete,export button
	if(numberChecked == 0)
		disableAll();
	else
	{
		enableAction(1);
		enableAction(3);
	}
	
	//for print
	if(numberChecked == 1 && type != "CSV")
		enableAction(2);
	else
		disableAction(2);
	
}
function disableAll()
{
	disableAction(1);
	disableAction(2);
	disableAction(3);
}
function chkClick(obj)
{
	obj.checked = !obj.checked;
}
function addToCheckboxList(id)
{
	if(checkboxList == null)
	{
		checkboxList = new Array();
	}
	if(checkboxList.indexOf(id) == -1)
	{
		checkboxList.push(id);
	}
}
function removeFromCheckboxList(id)
{
	if(checkboxList == null)
		return;
	var position = checkboxList.indexOf(id);
	if(position != -1)
	{
		checkboxList.splice(position, 1);
	}
}
function uncheckByCheckboxList()
{
	document.getElementById("checkAllCheckbox").checked = false;
	if(checkboxList != null && checkboxList.length>0)
	{
		for(var i=0;i<checkboxList.length;i++)
		{
			var id = checkboxList[i];
			var obj = document.getElementById("chk"+id);
			if(obj != null)
			{
				obj.checked = false;
			}
		}
	}
	checkboxList = null;
	if(typeof(disableAll) == "function")
	{
		disableAll();
	}
}
function updateCheckAllCheckbox()
{
	var obj = document.getElementById("checkAllCheckbox");
	if(obj == null)
		return;
	if(checkboxList == null || checkboxList.length ==0)
	{
		obj.checked = false;
		return;
	}
	var objs = document.getElementsByTagName('input'); 
	for(var i=0;i<objs.length;i++)
	{ 
		if(objs[i].type == 'checkbox' && objs[i].id != null && objs[i].id.indexOf("chk") == 0)
		{ 
			var id = objs[i].id;
			id = id.substring(3,id.length);
			var position = checkboxList.indexOf(id);
			if(position == -1)
			{
				obj.checked = false;
				return;
			}
		}
	}
	obj.checked = true;
}
function deleteAllHsReport()
{
	if(confirm(document.getElementById("msgdeleteall").value))
	{
		var params = "cmd=delall&fil="+document.getElementById("hsfilter").value;
		CommSend("servlet/ajrefresh","POST",params,"deleteAllHsReport()", true);
	}
}
function Callback_deleteAllHsReport()
{
	hsFilterReport(document.getElementById("hsfilter"));
}
function exportAllHsReport()
{
	if(confirm(document.getElementById("msgexportall").value))
	{
		fdSaveFile('','zip',exportAllFile);
		var date = new Date();
		fdSetFile("LogReports_"+date.format("yyyyMMddhhmmss"));

	}
}
function exportAllFile(local, path, filename)
{
	var params = "cmd=exportall&fil="+document.getElementById("hsfilter").value+"&local="+local+"&newfilename="+filename+"&newpath="+path;
	CommSend("servlet/ajrefresh","POST",params,"exportFile("+local+")", true);
}
//----------------------------------------------------------------------------------
function hasDataRow()
{
	var objs = document.getElementsByTagName('input'); 
	for(var i=0;i<objs.length;i++)
	{ 
		if(objs[i].type == 'checkbox' && objs[i].id != null && objs[i].id.indexOf("chk") == 0)
		{ 
			return true;
		}
	}
	return false;
}