var rowColor =0;
var tablecodeA = "<table class='table' width='100%' id='devvarTab'>"
	+ "<thead><tr><th class='th' style='width:50%;'>Device</th>"
	+ "<th class='th'>Variable</th>"
	+ "<th class='th' width='2%' onclick='booklet_delete_all_variables()' style='text-align: center;cursor: pointer;' "
	+ "title='Delete all'><img src='images/dbllistbox/delete_on.png' alt='Delete all' /></th></tr></thead>";
var tablecodeB = "<tbody></tbody></table>";
var requests = 0;

var profile_booklet = 'false';//document.getElementById("profile_booklet").value;

//BIOLO: mapping button function with progressive number
var act_default = 1;
var act_print = 2;
var act_export_csv = 3;
var act_export_pdf = 4;
var act_save = 5;
var act_export_all = 6;

// end button mapping

function addRequest() {
	requests++;
}

function subtractRequest() {
	requests--;
	if (requests <= 0) {
		MTstopServerComm();
		requests = 0;
	}
}

function initialize() {
	//retrieve profile permission for booklet  true: can save, false:can't save
	profile_booklet = document.getElementById("profile_booklet").value;
	tablecodeA = "<table class='table' width='100%' id='devvarTab'>"
		+ "<thead><tr><th class='th' style='width:50%;'>" + document.getElementById("_devices").value + "</th>"
		+ "<th class='th'>"	+ document.getElementById("_variables").value + "</th>"
		+ "<th class='th' width='2%' "+
		(profile_booklet=='true'?"onclick='booklet_delete_all_variables()'":"")
		+ " style='text-align: center;cursor: pointer;' title='"
		+ document.getElementById("delall").value+"'><img src='images/dbllistbox/delete_on.png' alt='"
		+ document.getElementById("delall").value+"' /></th></tr></thead>";
	tablecodeB = "<tbody></tbody></table>";
	
	enableAction(act_default);	
	disableAction(act_print);	
	disableAction(act_export_csv);	
	disableAction(act_export_pdf);
	enableAction(act_save);	
	enableAction(act_export_all);	

	MTstartServerComm();
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=confInit&section=conf", Callback_confInit, false);
	MTstartServerComm();
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devMdlInit&section=devMdl", Callback_devMdlInit, false);
	MTstartServerComm();
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devLstInit&section=deviceslist", Callback_devLstInit, false);
	MTstartServerComm();
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=paramLstInit&section=parameterslist", Callback_paramLstInit, false);
	MTstartServerComm();
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devvarInit&section=devvar", Callback_devvarInit, false);
}

function Callback_confInit(src) {
	var xml = src;	
	try {
		// conf
		if (xml.getElementsByTagName("conf").length > 0) {
			document.getElementById("devparam").checked = (xml.getElementsByTagName("dv")[0].childNodes[0].nodeValue == 1) ? true: false;
			document.getElementById("siteinfo").checked = (xml.getElementsByTagName("si")[0].childNodes[0].nodeValue == 1) ? true: false;
			document.getElementById("siteconf").checked = (xml.getElementsByTagName("sc")[0].childNodes[0].nodeValue == 1) ? true: false;
			document.getElementById("schedash").checked = (xml.getElementsByTagName("sd")[0].childNodes[0].nodeValue == 1) ? true: false;
			document.getElementById("userconf").checked = (xml.getElementsByTagName("uc")[0].childNodes[0].nodeValue == 1) ? true: false;
			document.getElementById("activealarm").checked = (xml.getElementsByTagName("aa")[0].childNodes[0].nodeValue == 1) ? true: false;
			document.getElementById("plugmodule").checked = (xml.getElementsByTagName("pm")[0].childNodes[0].nodeValue == 1) ? true: false;
			document.getElementById("notes").checked = (xml.getElementsByTagName("ns")[0].childNodes[0].nodeValue == 1) ? true: false;
		}
	} catch (err) {
		alert("Callback_confInit: "+err.description);
	}
	subtractRequest();
}

function Callback_devMdlInit(xml) {
	try {
		// devMdl		
		if (xml.getElementsByTagName("devMdl").length > 0) {
			var devMdl = xml.getElementsByTagName("devMdl");
			var mdl = document.getElementById("model");
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
	subtractRequest();
}

//shared by subtab1 and subtab2
function Callback_devLstInit(src) {
	var devLst = document.getElementById("devLst");
	makeEmptyList(devLst);
	var xml = src;
	try {
		document.getElementById("div_dev").innerHTML=xml.getElementsByTagName("booklet")[0].childNodes[0].nodeValue;
	} catch (err) {
		alert("Callback_devLstInit: "+err.description);
	}
	subtractRequest();
	//
	if (profile_booklet=='false')
		document.getElementById("devLst").disabled=true;
	//for subtab2, allow multiple select
	if(isSubtab2())
	{
		devLst = document.getElementById("devLst");
		devLst.multiple=true;
		var paramLst = document.getElementById("paramLst");
		removeDuplicated(paramLst,devLst);
	}
}

function Callback_paramLstInit(src) {
	var paramLst = document.getElementById("paramLst");
	makeEmptyList(paramLst);
	var xml = src;
	try {
		// param list
		document.getElementById("div_var").innerHTML=xml.getElementsByTagName("booklet")[0].childNodes[0].nodeValue;
	} catch (err) {
		alert("Callback_paramLstInit: "+err.description);
	}
	subtractRequest();
	if (profile_booklet=='false')
		document.getElementById("paramLst").disabled=true;
}

function Callback_devvarInit(src) {
	var xml = src;
	try {
		if (xml.getElementsByTagName("booklet").length > 0) {
			document.getElementById("divdevvarTab").innerHTML = tablecodeA
//			+ document.getElementById("devvarTab").tBodies[0].innerHTML
			+ xml.getElementsByTagName("booklet")[0].childNodes[0].nodeValue
			+ tablecodeB;
		}
		if (document.getElementById("devvarTab").rows.length <= 1)
		{	
			disableAction(act_print);
			disableAction(act_export_csv);
			disableAction(act_export_pdf);
		}
		else
		{
			enableAction(act_print);
			enableAction(act_export_csv);
			enableAction(act_export_pdf);
		}
	} catch (err) {
		alert("Callback_devvarInit: "+err.description);
	}
	subtractRequest();
}

/* buttons */
/* btt1 */
function loadDefault() {
	var attention = "";
	if (document.getElementById("attention"))
		attention = document.getElementById("attention").value;
	
	if (confirm(attention)) {
		document.getElementById("divdevvarTab").innerHTML = tablecodeA+tablecodeB;
		MTstartServerComm();
		addRequest();
		new AjaxRequest("servlet/ajrefresh", "POST", "method=loadDefault&section=loadDefault", Callback_loadDefault, false);
	}
}
function Callback_loadDefault(src){
	MTstartServerComm();
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devvarInit&section=devvar", Callback_devvarInit, false);
	subtractRequest();
}

/* btt2 */
function printBooklet() {
	printBookletAsReport("standard",false); // current booklet
}

/* btt3 */ 
function booklet_csv_fdSaveFile()
{
	fdSaveFile('','csv',savecsvfile);
	var date = new Date();
	fdSetFile("Commissioning_"+date.format("yyyyMMddhhmmss"));
}
function savecsvfile(local, path, filename)
{
	MTstartServerComm();
	CommSend("servlet/ajrefresh", "POST", "method=devvarInit&section=report&isopen=0&prompt=csv&reptype=standard&path="+path, "exportFile("+local+")", false);
}

/* btt4 */
function booklet_pdf_fdSaveFile()
{
	fdSaveFile('','pdf',savepdffile);
	var date = new Date();
	fdSetFile("Commissioning_"+date.format("yyyyMMddhhmmss"));
}
function savepdffile(local, path, filename)
{
	MTstartServerComm();
	CommSend("servlet/ajrefresh", "POST", "method=devvarInit&section=report&isopen=0&prompt=pdf&reptype=standard&path="+path, "exportFile("+local+")", false);
}


/* btt5 */
function saveBooklet() {
	MTstartServerComm();
	addRequest();
	setTimeout(realSaveBooklet, 50);
}

function Callback_exportFile(local) {
	
	MTstopServerComm();
	
	if (xmlResponse!=null && xmlResponse.text!="")
	{
		var filename = "";
		
		if(xmlResponse.getElementsByTagName("path").length > 0)	
		{
			filename = xmlResponse.getElementsByTagName("path")[0].childNodes[0].nodeValue;
		}
		
		var msg = document.getElementById("save_confirm").value;
		
		if(local == true)
		{
			if(filename == "ERROR")
			{
				msg = document.getElementById("save_error").value;
				alert(msg);
			}
			else
			{
				alert(msg + filename);
			}
		}
		else
		{
			var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
			window.open(sUrl);
		}
	}
}


function realSaveBooklet() {
	
	var bookconf="true,true,true,true,true,true,true,true";
	var tab = document.getElementById("devvarTab");
	var values;
	var repdev="";
	var repvar="";
	for (var i=1; i < tab.rows.length; i++)
	{
		if(tab.rows[i].id=="0") continue;
		values = tab.rows[i].id.split("_");
		repdev += values[0] + ",";
		repvar += values[1] + ",";
	}
	new AjaxRequest("servlet/ajrefresh", "POST", 
			"method=confInit&section=saveBooklet&bookconf="+bookconf+"&repdev="+repdev+"&repvar="+repvar, 
			Callback_saveBooklet, false);	
}
function Callback_saveBooklet(src){
	subtractRequest();
	var err = src.getElementsByTagName("error");
	if(err!=null && err.length>0) {
		alert(err[0].childNodes[0].nodeValue);
	} else {
		var done = document.getElementById("operationend").value;
		alert(done);
	}
	if(document.getElementById("devvarTab").rows.length>1)
	{
		enableAction(act_print);
		enableAction(act_export_csv);
		enableAction(act_export_pdf);
	}
	else
	{
		disableAction(act_print);
		disableAction(act_export_csv);
		disableAction(act_export_pdf);
	}
}
/* btt6 */
function booklet_exportAll_fdSaveFile()
{
	fdSaveFile('','csv',exportAllBooklet);
	var date = new Date();
	fdSetFile("Commissioning_"+date.format("yyyyMMddhhmmss"));
}
function exportAllBooklet(local, path, filename) {
	printBookletAsReport("allrw",local, path, filename); // all the site parameters
}

/*end-buttons*/

/* *********components*/
/*devlistopt*/
//shared by subtab1 and subtab2
function getDevList(obj){
	document.getElementById("model").value="-1";
	document.getElementById("model").disabled =true;
	document.getElementById("devLst").multiple=false;
	document.getElementById("model").disabled=true;
	var paramLst=document.getElementById("paramLst");
	//not subtab2,clear list
	if(!isSubtab2())
		makeEmptyList(paramLst);
	MTstartServerComm();
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devLstInit&section=deviceslist", Callback_devLstInit, false);
}

/*devmdllistopt*/
//shared subtab1 and subtab2
function emptyComponents(obj){
	if(isSubtab2())
	{
		emptyComponents_subtab2(obj);
		return;
	}
	document.getElementById("model").disabled =false;
	var devLst=document.getElementById("devLst");
	makeEmptyList(devLst);
	var paramLst=document.getElementById("paramLst");
	makeEmptyList(paramLst);
}

/*combodevmdl*/
//shared by subtab1 and subtab2
function changeDevMdl(obj){
	//if subtab2
	if(isSubtab2())
	{
		changeDevMdl_subtab2(obj);
		return;
	}
	document.getElementById("m").checked="checked";
	document.getElementById("devLst").multiple=true;
	model=obj.value;
	if(model==-1){
		emptyComponents(obj);
		return;
	}
	param="&model="+model+"&dev=-1";
	MTstartServerComm();	
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devLstInit&section=deviceslist"+param, Callback_devLstInit, false);
	MTstartServerComm();
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=paramLstInit&section=parameterslist"+param, Callback_paramLstInit, false);
}

/*selectdevormdl*/
//shared by subtab1 and subtab2
function dblClickDevList(obj) {
	//if subtab2, go to the code of subtab2
	if(isSubtab2())
	{
		dblClickDevList_subtab2(obj);
		return;
	}
	if(document.getElementById("m").checked){
		return;
	}
	loadConf = false;
	loadDevMdl = false;
	loadDev = false;
	loadParam = true;
	loadDevVar = false;
	dev = obj.value;
	param = "&model=-1&dev=" + dev;
	MTstartServerComm();
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=paramLstInit&section=parameterslist"+param, Callback_paramLstInit, false);
}

/*selectvariableorvarmdl*/
//shared by subtab1 and subtab2
function dblClickParamList(obj) {
	if(isSubtab2())
	{
		dblClickParamList_subtab2(obj);
		return;
	}
	var data1 = document.getElementById("devLst");
	var data2 = document.getElementById("paramLst");
	var tab = document.getElementById("devvarTab");
	var isSame = false;

	var nvar = 0;
	var nop = 0;
	var okvar = false;
	var extra = "";
	
	if (data1.selectedIndex != -1) {
		var initrows = tab.rows.length;
		var maxvars = document.getElementById("maxvariables").value
		if(document.getElementById("d").checked){
			// gestione MULTIVALUE per lista devs:
			for ( var i = 0; i < data1.options.length; i++) {
				if (data1.options[i].selected == true) {
					if (data2.selectedIndex != -1) {
						okvar = true;
						// gestione MULTIVALUE per lista vars:
						for ( var j = 0; j < data2.options.length && initrows <= maxvars; j++) {
							if (data2.options[j].selected == true) {
								isSame = false;
								var elem = document.getElementById(data1.options[i].value+ "_" + data2.options[j].value);
								isSame = (elem != null);
								if (isSame == false) {
									addTableRow(tab, data1.options[i].value,
											data1.options[i].text,
											data2.options[j].value,
											data2.options[j].text);
									nvar = nvar + 1;
									initrows = tab.rows.length;
								}
								nop = nop + 1;
							}
						}
					}
				}
			}
			if (nvar > 0) {
				if (document.getElementById("operationend")) {
					var done = document.getElementById("operationend").value;
					if (nvar > 1)
						extra = "\n\n\t" + nvar + " vars";
					//alert(done + extra);
				}
			}
			if(initrows>maxvars){
				alert(document.getElementById("warning_max_variables").value);
			}
		}
		if(document.getElementById("m").checked) {
			//dev * varmdl
			var devret = getAllSelectedIndexes(document.getElementById("devLst"));
			var devicesids = "";
			for(var ii=0;ii<devret.length;ii++){
				devicesids+=document.getElementById(devret[ii]).value+"-";
			}//for
			var varret = getAllSelectedIndexes(document.getElementById("paramLst"));
			var variablesids = "";
			for(var jj=0;jj<varret.length;jj++){
				variablesids+=document.getElementById(varret[jj]).value+"-";
			}//for
			var trows = document.getElementById("devvarTab").rows;
			var count = trows.length;
			var exclude = "";
			for(var c = 1;c<trows.length;c++){
				var iid = trows[c].id.split("_");
				if(iid.length>1)
					exclude+=iid[1]+"-";
			}
			if(variablesids!=""&&devicesids!=""){
				MTstartServerComm();
				addRequest();
				new AjaxRequest("servlet/ajrefresh",
						"POST",
						"method=addDevVar&section=addDevVar&devices="+devicesids+"&variables="+variablesids+"&exclude="+exclude,
						Callback_addDevVar, false);
			}
			setModUser();
		}
	}
}
function Callback_addDevVar(xml){
	var tab = document.getElementById("devvarTab");
	initRows=tab.rows.length;
	if(xml.getElementsByTagName("booklet").length > 0){
		var previousvars;
		try {
			previousvars = document.getElementById("devvarTab").tBodies[0].innerHTML;			
		} catch (err) {
			previousvars = "";
		}
		var newvars;
		try {
			newvars = xml.getElementsByTagName("booklet")[0].childNodes[0].nodeValue;
		} catch (err) {
			newvars = "";
		}
		var tablecode = tablecodeA + previousvars + newvars + tablecodeB;
		try{
			MTstopServerComm();
			document.getElementById("divdevvarTab").innerHTML = tablecode;
			initRows=tab.rows.length;
			if(xml.getElementsByTagName("added")!=null && xml.getElementsByTagName("added").length>0) {
				var done = document.getElementById("operationend").value;
				var added = "";
				if(xml.getElementsByTagName("added")!=null && xml.getElementsByTagName("added").length>0) {
					added = xml.getElementsByTagName("added")[0].childNodes[0].nodeValue;
				}
				if (added != "")
					extra = "\n\n\t" + added + " vars";
				//if(added!="" && added>0)
					//alert(done+extra);
			}
			if(initRows>document.getElementById("maxvariables").value){
				alert(document.getElementById("warning_max_variables").value);
			}
		} catch(err){alert(err.description);}
		tab = document.getElementById("devvarTab");
		trs = tab.rows.length;
		for(i=0;i<trs;i++){
			tab.rows[i].className = (i%2==0)?"Row1":"Row2";
		}	
	}
	subtractRequest();
}
/* additem */

/* dellall */
function booklet_delete_all_variables() {
	var s = document.getElementById('deleteallparamquestion').value;
	if (profile_booklet=='true')
	{
		if(confirm(s))
		{
			document.getElementById("divdevvarTab").innerHTML = tablecodeA+tablecodeB;
			disableAction(act_print);
			disableAction(act_export_csv);
			disableAction(act_export_pdf);
			setModUser();
		}
	}
}

/*delitem*/
function deleteItem(obj) {
	var tr=obj.parentNode.parentNode;
	var tab=tr.parentNode.deleteRow(tr.rowIndex-1);
	var table = document.getElementById("devvarTab");
	var trs = table.rows.length;
	for(i=0;i<trs;i++){
		table.rows[i].className = (i%2==0)?"Row1":"Row2";
	}
	disableAction(act_print);
	disableAction(act_export_csv);
	disableAction(act_export_pdf);
	setModUser();
}
/*end-components*/

/* ***************************util*/
function setExportAll(enable){
	if (profile_booklet=='true') 
	if(enable) {
		enableAction(act_export);
	} else {
		disableAction(act_export);
	}
}

function printBookletAsReport(reptype,local, path, filename) {
	var prompt="pdf";
	
	if(reptype=="allrw")
		prompt = "csv";
	
	MTstartServerComm();
	addRequest();
	new CommSend("servlet/ajrefresh", "POST", 
			"method=reportInit&section=report&isopen=0&prompt="+prompt+"&reptype="+reptype+"&path="+path, 
			"reportInit("+local+")", false);
}
//function Callback_reportInit(xml){
//	//devMdl
//	if(xml.getElementsByTagName("report").length>0){
//		var report = xml.getElementsByTagName("report");
//		for(var i=0;i<report.length;i++){
//			var path=report[i].childNodes[0].childNodes[0].nodeValue;
//			printDocument(path);
//		}
//	}
//	subtractRequest();
//}
function Callback_reportInit(local) {
	
	if (xmlResponse!=null && xmlResponse.text!="")
	{
		var filename = "";
		
		if(xmlResponse.getElementsByTagName("path").length > 0)	
		{
			filename = xmlResponse.getElementsByTagName("path")[0].childNodes[0].nodeValue;
		}
		
		var msg = document.getElementById("save_confirm").value;
		
		if(local == true)
		{
			if(filename == "ERROR")
			{
				msg = document.getElementById("save_error").value;
				alert(msg);
			}
			else
			{
				alert(msg + filename);
			}
		}
		else
		{
			var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
			window.open(sUrl);
		}
	}
	subtractRequest();
}
function printDocument(sPath){
	var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+sPath;
	window.open(sUrl);
	MTstopServerComm();
}

function addTableRow(tabl, dv1, dt1, dv2, dt2) {
	rowColor++;
	var row = document.createElement("tr");
	row.className = (rowColor%2==0)?"Row2":"Row1";
	row.id = "" + dv1 + "_" + dv2 + "";

	var cell0 = document.createElement("td");
	cell0.className = "standardTxt";
	cell0.innerHTML = dt1;

	var cell1 = document.createElement("td");
	cell1.className = "standardTxt";
	cell1.innerHTML = dt2;
	var cell2 = document.createElement("td");
	cell2.className = "standardTxt";
	cell2.style.textAlign = "center";
	cell2.style.cursor = "pointer";
	cell2.innerHTML = "<IMG "+
	(profile_booklet=='true'?"onclick='deleteItem(this);'":"")+
	"src='images/actions/removesmall_on_black.png'/>";

	row.appendChild(cell0);
	row.appendChild(cell1);
	row.appendChild(cell2);

	tabl.tBodies[0].appendChild(row);
	setModUser();
	
	disableAction(act_print);
	disableAction(act_export_csv);
	disableAction(act_export_pdf);
}

function makeEmptyList(list){
	while(list.options.length > 0){
		list.removeChild(list.options[list.options.length - 1]);
	}	
}

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

/* endutil */

//------------------------------------------------------------------------------------------
//--------------------------------------subtab2---------------------------------------------
//------------------------------------------------------------------------------------------
function subtab2_initialize() {
	profile_booklet = document.getElementById("profile_booklet").value;
	enableAction(1);	

	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devMdlInit&section=devMdl", Callback_devMdlInit, false);
	MTstartServerComm();
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devLstInit&section=deviceslist", Callback_devLstInit, false);
	MTstartServerComm();
	addRequest();
}
function isSubtab2()
{
	var subtab = document.getElementById("subtab");
	if(subtab != null && subtab.value == "subtab2" )
		return true;
	else
		return false;
}
//single add device
function dblClickDevList_subtab2(obj)
{
	var div_dev = document.getElementById("devLst");
	var div_var = document.getElementById("paramLst");
	to2New(div_dev,div_var);
}
//multiple add device
function multipleAddDevice()
{
	var div_dev = document.getElementById("devLst");
	var div_var = document.getElementById("paramLst");
	multipleto2New(div_dev,div_var,null);
}
//device double click add
function dblClickParamList_subtab2(obj)
{
	var div_dev = document.getElementById("devLst");
	var div_var = document.getElementById("paramLst");
	multipleto1New(div_dev,div_var,null);
}
//model click
function emptyComponents_subtab2()
{
	document.getElementById("model").disabled =false;
	var devLst=document.getElementById("devLst");
	makeEmptyList(devLst);
}
function changeDevMdl_subtab2(obj)
{
	document.getElementById("m").checked="checked";
	document.getElementById("devLst").multiple=true;
	model=obj.value;
	param="&model="+model+"&dev=-1";
	MTstartServerComm();	
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devLstInit&section=deviceslist"+param, Callback_devLstInit, false);
	MTstartServerComm();
}
function booklet_cabinet_save()
{
	var cabinet = document.getElementById("cabinet").value;
	if(cabinet == "")
	{
		alert(document.getElementById("cabinet_validate").value);
		return;
	}
	var iddevices = document.getElementById("iddevices");
	var div_var = document.getElementById("paramLst");
	var iddevices_str = "";
	for(var i=0;i<div_var.length;i++)
	{
		iddevices_str += div_var.options[i].value+";";
	}
	if(iddevices_str == "")
	{
		alert(document.getElementById("devices_validate").value);
		return;
	}
	if(iddevices_str.length>0)
		iddevices_str = iddevices_str.substring(0, iddevices_str.length-1);
	iddevices.value = iddevices_str;
	var id = document.getElementById("id").value;
	var fileName = document.getElementById("fileupload").value;
	if(id == "" && fileName == "")
	{
		alert(document.getElementById("file_validate").value);
		return;
	}
	var form = document.getElementById("uploadform");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}
var selectedCabinet = null;
function selectedBookletCabinet(idCabinet)
{
	if (idCabinet==null) return true;
	if (selectedCabinet==idCabinet) 
	{
		disableAction(2);
		disableAction(3);
		document.getElementById("rdio"+selectedCabinet).checked = false;
		selectedCabinet = null;
	}
	else
	{
		if(selectedCabinet != null)
		{
			document.getElementById("rdio"+selectedCabinet).checked = false;
		}
		document.getElementById("rdio"+idCabinet).checked = true;
		enableAction(2);
		enableAction(3);
		selectedCabinet= idCabinet;
	}
}

function modifyBookletCabinet(idCabinet)
{
	new AjaxRequest("servlet/ajrefresh", "POST", "method=devLstInit&section=deviceslist", Callback_devLstInit, false);
	MTstartServerComm();
	addRequest();
	new AjaxRequest("servlet/ajrefresh", "POST", "cmd=edit&id="+idCabinet, Callback_editCabinet, false);
	MTstartServerComm();
	addRequest();
}
function Callback_editCabinet(xml)
{
	document.getElementById("id").value = xml.getElementsByTagName("id")[0].childNodes[0].nodeValue;
	document.getElementById("cabinet").value = xml.getElementsByTagName("cabinet")[0].childNodes[0].nodeValue;
	document.getElementById("fileNameTD").innerHTML = xml.getElementsByTagName("filename")[0].childNodes[0].nodeValue;
	document.getElementById("div_var").innerHTML  = xml.getElementsByTagName("devices")[0].childNodes[0].nodeValue;
	var source = document.getElementById("paramLst");
	var target = document.getElementById("devLst");
	removeDuplicated(source,target);
	subtractRequest();
}
function remove_booklet_cabinet()
{
	document.getElementById("post_cmd").value = "delete";
	document.getElementById("post_id").value = selectedCabinet;
	MTstartServerComm();
	document.getElementById("post_form").submit();
}