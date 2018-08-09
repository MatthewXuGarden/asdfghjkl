var old_passw = "";
var mailnumber = 0;

var columnNumber = 2;
var columnPer = 100 / columnNumber;
var CURRENT_PAGE = 1;
var TOTAL_PAGE = null;

var PointerObj = null;
var OldColor = "#CCCCCC";

var DEV_PREFIX = "d_";
var DLED_PREFIX = "DLed_";
var VAR_PREFIX = "dtlst_";
var IMGSET_PREFIX = "imgset_";

var DEV_LIMIT = 10;
var DEV_OFFSET = 0;
var REFRESHTIME = 100;
var first = true;
var refreshfirst = true;
var mX,mY,popupDIV;
var _IsMousedown = 0;
var _ClickLeft = 0;
var _ClickTop = 0;
var prev_devmdl="";
function restart_engine(){
	var param= "&cmd=rest";
	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "POST",param, Callback_restart_engine, true);
	return true;
}
function Callback_restart_engine(xml){
	MTstopServerComm();
}

function wizard_tab1_init() {
	enableAction(1);
	enableAction(2);
	
}

function wizarddone() {
	var checked = document.getElementById("wizarddonebox").checked;
	checked = checked == true ? "1" : "0";
	new AjaxRequest("servlet/ajrefresh", "POST", "wizarddone=" + checked,
			Callback_a, true);
}

function Callback_a() 
{
}
function wizard_tab2_init(){
	new AjaxRequest("servlet/ajrefresh", "POST", "cmd=cpuperusage",
			Callback_CPUUsage, false);
	enableAction(1);
	enableAction(2);
	enableAction(3);
	var msg = document.getElementById("msgtouserforlicenze").value;
	if(msg != null && msg != "")
		alert(msg);
	//2010-7-8, Kevin Ge, get old password when init
	old_passw = document.getElementById("site_password").value;
}
function Callback_CPUUsage(xml)
{
	var cpu = xml.getElementsByTagName("cpu")[0].childNodes[0].nodeValue;
	document.getElementById("cpuperusage").innerHTML = cpu;
}
function wizard_tab3_init(){
//	enableAction(1);
	enableAction(5);
	enableAction(6);
}
function subtab2_registration_siteinfo_save() // SALVATAGGIO INFORMAZIONI SITO
{
	var site_name = document.getElementById("site_name").value;
	var site_password = document.getElementById("site_password").value;
	var site_confirm_password = document
			.getElementById("site_confirm_password").value;

	if (site_name == "") {
		alert(document.getElementById("compilename").value);
		return true;
	}
	if ((site_password != "") && (site_password.length < 6)) {
		alert(document.getElementById("shortpassw").value);
		return true;
	}
	if (old_passw != site_password) {
		if (site_password != site_confirm_password) {
			alert(document.getElementById("wrongpassw").value);
			return true;
		}
	}
	var ofrm = document.getElementById("frm_registration_siteinfo");
	
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}
function wizard_tab4_init() 
{
	enableAction(2);
	enableAction(3);
	gotoPage();
	var height = document.getElementById("scr_h").value-38-25-87-150;
	document.getElementById("param_div").style.height = height+"px";
}
function wizard_tab4_fresh()
{
	if(refreshfirst == true)
	{
		disableAction(0);
		var tt = document.getElementById("deviceviewloading");
		tt.style.display='block';
		tt.style.visibility='visible';
		refreshfirst = false;
	}
	var param = "cmd=refresh&current_page="+CURRENT_PAGE+"&limit="+DEV_LIMIT+"&offset="+DEV_OFFSET;
	new AjaxRequest("servlet/ajrefresh", "POST", param,
			Callback_wizard_subtab4_init, false);
}
function Callback_wizard_subtab4_init(xml) 
{
	var container = document.getElementById("tablecontainer");
	var listdevice = xml.getElementsByTagName("device");
	var deviceNumber = retrieveDeviceCellsNumber();
	for ( var i = 0; i < listdevice.length; i++) 
	{	//hidden some devices,which are disabled or not templates.
		if((listdevice[i].getAttribute("status")=="0")||(listdevice[i].getElementsByTagName("P").length<1)){
			continue;
		}
		var iddev = listdevice[i].getAttribute("id");
		var devicetable = document.getElementById(DEV_PREFIX + iddev);
		if (devicetable == null){
			//simon add for make groups according to device types.
			if(prev_devmdl!= listdevice[i].getAttribute("iddevmdl")){
				if(prev_devmdl!=""){
					var r=container.insertRow(container.rows.length);
					var c = r.insertCell();
					c.colSpan=2;
					c.innerHTML="<hr>";
					deviceNumber=0;
				}
				prev_devmdl= listdevice[i].getAttribute("iddevmdl")
			}
			if (deviceNumber % columnNumber == 0){
				var newTr = container.insertRow(container.rows.length);
				var newCell = null;
				for ( var j = 0; j < columnNumber; j++) 
				{
					newCell = newTr.insertCell(-1);
					newCell.style.width = columnPer + "%";
					newCell.style.height = 1;
				}
			}
			var x = container.rows.length - 1;
			var y = deviceNumber % columnNumber;
			container.rows[x].cells[y].innerHTML = createDeviceTable(listdevice[i]);
		}
		else
		{
			var status = listdevice[i].getAttribute("status");
			//1. device staus
			var ledobj = document.getElementById(DLED_PREFIX+iddev);			
			ledobj.style.backgroundImage = "url(images/led/L"+status+".gif)";
			ledobj.innerHTML="&nbsp;&nbsp;";
			//2. set button
			var setobj = document.getElementById(IMGSET_PREFIX+iddev);
			var imgsrc = setobj.src;
			//offline
			if(status == "0")
			{
				if(imgsrc.indexOf("off.png") == -1)
				{
					setobj.src = "images/actions/params_off.png";
					setobj.onclick = new Function("");
				}
			}
			else//online
			{
				if(imgsrc.indexOf("off.png") != -1)
				{
					setobj.src = "images/actions/params_on_black.png";
					setobj.onclick = new Function("wizard_setparams(this)");
				}
			}
			
			//3. var 
			var params = listdevice[i].getElementsByTagName("P");
			for(j=0;j<params.length;j++)
			{
				var idvar = params[j].getAttribute("id");
				var varobj = document.getElementById(VAR_PREFIX+idvar);
				varobj.value = params[j].childNodes[0].nodeValue;
			}
		}
		deviceNumber++;
	}
	DEV_OFFSET+=DEV_LIMIT;
	if(listdevice.length<DEV_LIMIT)
	{
		var virtkeyboard = document.getElementById("virtkeyboard").value;
		if(virtkeyboard == "on")
		{
			buildKeyboardInputs();
		}
		first = false;
		DEV_OFFSET = 0;
		enableAction(1);
		var tt = document.getElementById("deviceviewloading");
		tt.style.visibility='hidden';
		tt.style.display='none';
		refreshfirst = true;
		TOTAL_PAGE = xml.getElementsByTagName("page")[0].childNodes[0].nodeValue;
		updatePageBrowse();
	}
	else
	{
		if(first == true)
		{
			setTimeout("wizard_tab4_init()", REFRESHTIME);
		}
		else
		{
			setTimeout("wizard_tab4_fresh()",REFRESHTIME);
		}
	}
}
function updatePageBrowse()
{
	if(TOTAL_PAGE>0)
	{
		document.getElementById("pagestatus").innerHTML = CURRENT_PAGE+"/"+TOTAL_PAGE;
		if(CURRENT_PAGE==1)
		{
			setPageButton("tdfirstpage","sxsx",false,true,true);
			setPageButton("tdprepage","sx",false,false,true);
		}
		else
		{
			setPageButton("tdfirstpage","sxsx",true,true,true);
			setPageButton("tdprepage","sx",true,false,true);
		}
		if(CURRENT_PAGE == TOTAL_PAGE)
		{
			setPageButton("tdnextpage","dx",false,false,false);
			setPageButton("tdlastpage","dxdx",false,true,false);
		}
		else
		{
			setPageButton("tdnextpage","dx",true,false,false);
			setPageButton("tdlastpage","dxdx",true,true,false);
		}
	}
	else
	{
		document.getElementById("pagestatus").innerHTML = "";
		setPageButton("tdfirstpage","sxsx",false);
		setPageButton("tdprepage","sx",false);
		setPageButton("tdnextpage","dx",false);
		setPageButton("tdlastpage","dxdx",false);
	}
}
function setPageButton(tdid,imagename,enabled,isToend,isToleft)
{
	var tdobj = document.getElementById(tdid);
	var fixed = "<img src='images/lsw/";
	if(enabled)
	{
		tdobj.innerHTML = fixed+imagename+"_on.png' onclick='browsePage("+isToend+","+isToleft+")'/>";
	}
	else
	{
		tdobj.innerHTML = fixed+imagename+"_off.png'/>";
	}
}
function browsePage(isToend,isToleft)
{
	if(top.frames['body'] != null)
	{
		if(top.frames['body'].frames['bodytab'] != null)
		{
			try {
				if(!top.frames['body'].frames['bodytab'].MioAskModUser())
					return;
			}
			catch(e){}
		}
	}
	var imgsrc = "";
	if(isToend)
	{
		if(isToleft)//left end
		{ 
			imgsrc = document.getElementById("tdfirstpage").innerHTML;
			if(imgsrc.indexOf("_on.png") != -1 && CURRENT_PAGE>1)
			{
				CURRENT_PAGE = 1;
				resetParamTable();
				gotoPage();
			}
		}
		else//right end
		{
			imgsrc = document.getElementById("tdlastpage").innerHTML;
			if(imgsrc.indexOf("_on.png") != -1 && CURRENT_PAGE<TOTAL_PAGE)
			{
				CURRENT_PAGE = TOTAL_PAGE;
				resetParamTable();
				gotoPage();
			}
		}
	}
	else
	{
		if(isToleft)//pre
		{ 
			imgsrc = document.getElementById("tdprepage").innerHTML;
			if(imgsrc.indexOf("_on.png") != -1 && CURRENT_PAGE>1)
			{
				CURRENT_PAGE--;
				resetParamTable();
				gotoPage();
			}
		}
		else//next
		{
			imgsrc = document.getElementById("tdnextpage").innerHTML;
			if(imgsrc.indexOf("_on.png") != -1 && CURRENT_PAGE<TOTAL_PAGE)
			{
				CURRENT_PAGE++;
				resetParamTable();
				gotoPage();
			}
		}
	}
}
function gotoPage()
{
	var param = "cmd=init&current_page="+CURRENT_PAGE+"&limit="+DEV_LIMIT+"&offset="+DEV_OFFSET;
	new AjaxRequest("servlet/ajrefresh", "POST", param,
			Callback_wizard_subtab4_init, false);
}
function resetParamTable()
{
	updatePageBrowse();
	first = true;
	DEV_OFFSET = 0;
	var artTable = document.getElementById("tablecontainer");
	while(artTable.rows.length>0)     
		artTable.deleteRow(artTable.rows.length-1);
	var tt = document.getElementById("deviceviewloading");
	tt.style.display='block';
	tt.style.visibility='visible';
	disableAction(1);
}
function createDeviceTable(deviceXML) {
	var iddev = deviceXML.getAttribute("id");
	var status = deviceXML.getAttribute("status");
	var params = deviceXML.getElementsByTagName("P");
	var result = "<table id='"
			+DEV_PREFIX+ iddev
			+ "' width='100%' height=\"100%\" border=0  cellspacing='0' "
			+ "cellpadding='0' "
			+ "style='height:100%' class='ajaxdevtable'>"
			+ "<tr><td>"
			+ "<table width='100%' height='100%' border=0 class='ajaxdevtable'><tr>"
			+ "<td width='5%' align='left'><div style='background-image: url(images/led/L"
			+ status
			+ ".gif); "
			+ "background-repeat:no-repeat; background-position: center center;' id='"
			+ DLED_PREFIX+ iddev
			+ "'>"
			+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div></td>"
			+ "<td width='65%' align='left'><table cellspan=0 paddingspan=0 border=0><tr><td class='standardTxt' align='center'><b>"
			+ deviceXML.getAttribute("name")
			+ "</b></td><td></td></tr>"
			+ "<tr><td class='standardTxt'>"
			+ createTemplateCombo(deviceXML)
			+ "</td><td class='standardTxt'>&nbsp;&nbsp;&nbsp;&nbsp;<img src='images/actions/"+(status == "0" ? "report_off.png" : "report_on_black.png")+"' style='cursor:pointer;' title='"+document.getElementById("details").value+"' "+(status == "0" ? "" : "onclick='showme(this,\""+iddev+"\");'")+"/>"
			+ "</td></tr></table>"
			+ "</td>"
			+ "<td width='*' class='standardTxt' align='right'><b>"
			+ deviceXML.getAttribute("code")
			+ "</b></td>"
			+ "<td width='10%' align='right'><img style='background-color:white;cursor:pointer;' title='"+document.getElementById("setting").value+"' id='"+IMGSET_PREFIX+iddev+"' src='images/actions/params_"
			+ (status == "0" ? "off.png'"
					: "on_black.png' onclick='wizard_setparams(this);'")
			+ " width='25px'/></td>" + "</tr></table></td></tr>" + "<tr><td>"
			+ "<table width='100%' height='100%' border=0 cellspacing='0' cellpadding='0'>";
	var rowflag=false;
	for ( var i = 0; i < params.length; i++) {
		var idvar = params[i].getAttribute("id");
		var varname = params[i].childNodes[1].nodeValue;
		var varvalue = params[i].childNodes[0].nodeValue;
		var varmin = params[i].childNodes[2].nodeValue;
		var varmax = params[i].childNodes[3].nodeValue;
		var vartype = params[i].childNodes[4].nodeValue;
		var vardecimal = params[i].childNodes[5].nodeValue;
		var disabed = status == "0" ? "true" : "false";
		if (i % 2 == 0) {
			result += "<tr "+((rowflag==true)?"class='Row2'":"class='Row1'")+">";
			rowflag=!rowflag;
		}
		result += "<td width='40%' align='left' class='smallerTxt1'>"
				+ varname
				+ "</td>"
				+ "<td width='10%' align='right' class='standardTxt'>"
				+ createInputType(idvar, vartype, varvalue, disabed, vardecimal)
				+ "</td>";
		if (i % 2 != 0) {
			result += "</tr>";
		}
		result += createMinMaxHiddenControl(idvar, varmin, varmax);
	}
	result+="</table>";
	result += "</td></tr></table>";
	return result;
}
function showme(obj,iddev){
	var rect = obj.getBoundingClientRect();
	mX = parseInt(rect.right, 10);
	mY = parseInt(rect.bottom, 10);
	if(popupDIV!=null){
		popupDIV.innerHTML="";
		popupDIV.style.display='none';	
	}
	var dev=obj.parentNode.parentNode.parentNode.childNodes[1].childNodes[0].childNodes[0];
	if(dev!=null && dev.value!=null && dev.value!="null"){
		var param = "cmd=deviceSettingShow&devsetfile="+dev.value+"&iddev="+iddev;
		new AjaxRequest("servlet/ajrefresh", "POST", param,Callback_param_deviceSettingShow, false);
	}
}
function Callback_param_deviceSettingShow(xml) {	
	var params = xml.getElementsByTagName("param");
	if(popupDIV==null){
		popupDIV=document.createElement("div");
		popupDIV.id="popupDIV";
	}else{
		popupDIV.innerHTML="";
		popupDIV.style.display='block';	
	}
	
    var tab=document.createElement("table");
    tab.id="devsetTab";
    tab.className="sortable";
    tab.width="100%";
	tab.setAttribute("cellSpacing", "1");
	tab.setAttribute("cellPadding", "0"); 
	var tb=document.createElement("thead");
	tab.appendChild(tb);
	
	var headrow = document.createElement("tr");
//	headrow.className = "tr";
	headrow.id="headrow";
	tb.appendChild(headrow);
	var cel0 = document.createElement("th");
	cel0.className = "th";
	cel0.innerHTML="<div style='width:70;OVERFLOW: hidden;CURSOR: pointer;'>"+document.getElementById("varcode").value+"</div>";
	headrow.appendChild(cel0);
	var cel1 = document.createElement("th");
	cel1.className = "th";
	cel1.innerHTML="<div style='width:50;OVERFLOW: hidden;CURSOR: pointer;'>"+document.getElementById("value").value+"</div>";
	headrow.appendChild(cel1);
	var cel2 = document.createElement("th");
	cel2.className = "th";
	cel2.innerHTML="<div style='width:130;OVERFLOW: hidden;CURSOR: pointer;'>"+document.getElementById("description").value+"</div>";
	headrow.appendChild(cel2);
//	headrow.style.cursor="move";
	
	var datatab=document.createElement("table");
	datatab.className="table";
	datatab.id="dataTab";
	datatab.width="100%";
	datatab.setAttribute("cellSpacing", "1");
	datatab.setAttribute("cellPadding", "0"); 
	var tbody=document.createElement("tbody");
	datatab.appendChild(tbody);
    for(var i=0;i<params.length;i++){
    	var row = document.createElement("tr");
    	row.className = "Row1";
    	tbody.appendChild(row);
    	var cel0 = document.createElement("td");
    	cel0.className = "td";
    	cel0.innerHTML="<div style='width:70;OVERFLOW: hidden;'>"+params[i].childNodes[0].childNodes[0].nodeValue+"&nbsp;"+"</div>";
    	row.appendChild(cel0);
    	var cel1 = document.createElement("td");
    	cel1.className = "td";
    	cel1.innerHTML="<div style='width:50;OVERFLOW: hidden;'>"+params[i].childNodes[1].childNodes[0].nodeValue+"&nbsp;"+"</div>";
    	row.appendChild(cel1);
    	var cel2 = document.createElement("td");
    	cel2.className = "td";
    	cel2.innerHTML="<div style='width:130;OVERFLOW: hidden;'>"+params[i].childNodes[2].childNodes[0].nodeValue+"&nbsp;"+"</div>";
    	row.appendChild(cel2);
    	
	}
	var bottomtab=document.createElement("table");
	bottomtab.className="table";
	bottomtab.border=0;
	bottomtab.width="100%";
	var btb=document.createElement("tbody");
	bottomtab.appendChild(btb);
	var row = document.createElement("tr");
	row.className = "tr";
	btb.appendChild(row);
	var cel0 = document.createElement("td");
	cel0.className = "td";
	cel0.align="right";
	cel0.innerHTML="<table th='100%' border=0><tr><td width=110 height=30  class=\"groupCategory_small\" align='center' onclick=\"popupDIV.style.display='none';\">"+document.getElementById("close").value+"</td></tr></table>";
	row.appendChild(cel0);
	
	popupDIV.appendChild(tab);
	popupDIV.appendChild(datatab);
	document.body.appendChild(popupDIV);
    with(popupDIV.style){   
        position="absolute";
        width="310px";   
        height="310px";  
        border="1px solid black";   
        backgroundColor="#FFFFFF";   
        zIndex="100";
    }    
	generateSortTable(tab,datatab);
	popupDIV.appendChild(bottomtab);
	popupDIV.style.top=Math.min(mY,document.body.clientHeight-popupDIV.clientHeight);
	popupDIV.style.left=Math.min(mX,document.body.clientWidth-popupDIV.clientWidth);
}
function generateSortTable(headtab,datatab){
	var w=310;
	var h=270;
	var pa=headtab.parentNode;
	var framDIV=newDIV("LWborder0","relative","#cacaca",w,h,0,0,false,false,false);
	var headDIV=newDIV("LWCtHead0","relative","#cacaca",w-15,0,0,0,false,false,true);
	var dataDIV=newDIV("LWCtDataName0","relative","#cacaca",w,h-20,0,0,false,true,false);
	pa.appendChild(framDIV);
	framDIV.appendChild(headDIV);
	framDIV.appendChild(dataDIV);
	headDIV.appendChild(headtab);
	dataDIV.appendChild(datatab);
//	addEvent(dataDIV,"scroll",lvscroll);

}
function newDIV(idx,posi,backcolor,w,h,t,l,s_x,s_y,overf){
	var div=document.createElement("div");
	div.id=idx;
	div.align="left";
	with(div.style){
		position=posi;
        if(l>0) left=l;  
        if(t>0) top=t;
        if(w>0) width=w;   
        if(h>0) height=h;
        backgroundColor=backcolor; 
        if(overf==true) overflow="hidden";
        if(s_x==true) overflowX="scroll";
        if(s_y==true) overflowY="scroll";
    	
    }  
    return div;
}
function wizard_setparams(obj) {
	var row1table = obj.parentNode.parentNode.parentNode.parentNode;
	var row2table = row1table.parentNode.parentNode.parentNode.childNodes[1].childNodes[0].childNodes[0];
	var devicetable = row1table.parentNode.parentNode.parentNode.parentNode;
	var iddev = devicetable.id.split("_")[1];
	var templateObj = document.getElementById('file_' + iddev);

	var param = "cmd=set&iddev=" + iddev;
	if (templateObj != null) {
		if (templateObj.value != "null") {
			param += "&filename=" + templateObj.value;
		}
	}
	for ( var i = 0; i < row2table.rows.length; i++) {
		if (row2table.rows[i].cells[1] != null) {
			var inputobj = row2table.rows[i].cells[1].childNodes[0];
			if (inputobj.value != "") {
				param += "&" + inputobj.id + "=" + inputobj.value;
			}
		}
		if (row2table.rows[i].cells[3] != null) {
			inputobj = row2table.rows[i].cells[3].childNodes[0];
			if (inputobj.value != "") {
				param += "&" + inputobj.id + "=" + inputobj.value;
			}
		}
	}
	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "POST", param,
			Callback_wizard_subtab4_set, true);
	var setimgObj = document.getElementById(IMGSET_PREFIX+iddev);
	setimgObj.src = "images/actions/params_on_black.png";
}
function Callback_wizard_subtab4_set(xml) 
{
	MTstopServerComm();
	var vars = xml.getElementsByTagName("var");
	for(var i=0;i<vars.length;i++)
	{
		var variable = vars[i];
		var idvar = "dtlst_"+variable.getAttribute("id");
		var value = variable.childNodes[0].nodeValue;
		var obj = document.getElementById(idvar);
		obj.value = value;
	}
}
function createTemplateCombo(deviceXML) {
	var combo = deviceXML.getElementsByTagName("template");
	var iddev = deviceXML.getAttribute("id");
	var status = deviceXML.getAttribute("status");
	var result = "";
	if (combo.length > 0) {
		result = "<select class='standardTxt' "
				+ (status == "0" ? "disabled" : "") + " id='file_" + iddev
				+ "' style='width:260px;'>";
		for ( var i = 0; i < combo.length; i++) {
			var file = combo[i].childNodes[0].childNodes[0].nodeValue;
			var temp=combo[i].childNodes[1].childNodes[0].nodeValue;
//			var dflag=false;
//			if(temp=="CAREL_DEFAULT"){
//				temp=document.getElementById("careldefault").value;
//				dflag=true;
//			}
			result += "<option " + (i==0 ? "selected='selected'" : "")
					+ " value='" + file + "'>" + temp
					+ "</option>"; 
		}
		result += "<option  value='null'>----</option>";
		result += "</select>";
	} else {
		result = "<select disabled class='standardTxt' style='width:260px;'/>";
	}
	return result;
}
function dwhighlight(obj, bval) {
	if (bval) {
		if (PointerObj != null)
			PointerObj.style.backgroundColor = OldColor;

		PointerObj = obj;
		OldColor = PointerObj.style.backgroundColor;
		PointerObj.style.backgroundColor = "#EEEEEE";
	} else {
		if (PointerObj != null)
			PointerObj.style.backgroundColor = OldColor;
		// obj.style.backgroundColor = "#CCCCCC";
	}
}
function retrieveDeviceCellsNumber() {
	var counter = 0;
	var rows = document.getElementById("tablecontainer").rows;
	if (rows == null)
		return 0;
	for ( var kk = 0; kk < rows.length; kk++) {
		for ( var jj = 0; jj < rows[kk].cells.length; jj++) {
			if (rows[kk].cells[jj].childNodes[0] != null
					&& rows[kk].cells[jj].childNodes[0].tagName.toUpperCase() == "TABLE")
				counter++;
		}
	}
	return counter;
}
function wizard_tab6_init() {
	enableAction(1);
	enableAction(2);
	enableAction(3);
	var param = "";
	param += "cmd=init";
	new AjaxRequest("servlet/ajrefresh", "POST", param, Callback_subtab6_init,
			true);
}
function wizard_tab7_init() {
	enableAction(1);
	enableAction(2);
	//2010-6-2, by Kevin, move restart engine from HACCP report to last tab(GURDIAN config)
	if(document.getElementById("saveclick").value != "")
	{
		if(confirm(document.getElementById("restartengine").value)){
			restart_engine();
		}
	}
//	var param = "";
//	param += "cmd=init";
//	new AjaxRequest("servlet/ajrefresh", "POST", param, Callback_subtab6_init,
//			true);
	var list1=document.getElementById("list1");
	var list2=document.getElementById("list2");
	if((list1.options.length>0) && (list2.options.length>0)){
		for(var j=0;j<list2.options.length;j++){
			for(var i=0;i<list1.options.length;i++){
				if(list1.options[i].value==list2.options[j].value){
					list1.remove(i);
					break;
				}
			}
		}
	}

	for(var i=0; i < list1.options.length; i++)
		list1.options[i].className = "Row" + (i % 2 ? "2" : "1"); 
	for(var i=0; i < list2.options.length; i++)
		list2.options[i].className = "Row" + (i % 2 ? "2" : "1"); 
		
	var variable1=document.getElementById("variable1");
	var variable2=document.getElementById("variable2");
	if((variable1.options.length>0) && (variable2.options.length>0)){
		for(var j=0;j<variable2.options.length;j++){
			for(var i=0;i<variable1.options.length;i++){
				if(variable1.options[i].value==variable2.options[j].value){
					variable1.remove(i);
					break;
				}
			}
		}
	}

	for(var i=0; i < variable2.options.length; i++)
		variable2.options[i].className = "Row" + (i % 2 ? "2" : "1");	
}
function Callback_subtab6_init(xml) {
	var err = xml.getElementsByTagName("error");
	if(err[0] != null)
	{
		alert(err[0].childNodes[0].nodeValue);
	}
	if (xml.getElementsByTagName("printer") != null) {
		var report_printerSelect = document
				.getElementById("report_printerSelect");
		report_printerSelect.value = xml.getElementsByTagName("printer")[0].childNodes[0].nodeValue;
	}
	if (xml.getElementsByTagName("rule").length > 0) {
		document.getElementById("cmd").value = "update";
		document.getElementById("report_name").value = xml
				.getElementsByTagName("report_name")[0].childNodes[0].nodeValue;
		if (xml.getElementsByTagName("timebandtype")[0].childNodes[0].nodeValue == "1") {
			document.getElementById("idtimeBand").value = xml
					.getElementsByTagName("idtimeBand")[0].childNodes[0].nodeValue
			var timeBandValue = xml.getElementsByTagName("timeBandValue")[0].childNodes[0].nodeValue;
			var timeFrom = timeBandValue.split("-");
			var hour = timeFrom[0].split(":")[0];
			var minut = timeFrom[0].split(":")[1];
			document.getElementById("hour_from").value = hour;
			document.getElementById("minut_from").value = minut;
			showDaily(true);
		} else {
			document.getElementById("otherTimebind").innerHTML = xml
					.getElementsByTagName("timeBandCode")[0].childNodes[0].nodeValue;
			showDaily(false);
		}
		document.getElementById("idreport").value = xml
				.getElementsByTagName("idreport")[0].childNodes[0].nodeValue;
		// document.getElementById("layout").value =
		// xml.getElementsByTagName("layout")[0].childNodes[0].nodeValue;
		var layoutSelect = document.getElementById("layoutSelect");
		layoutSelect.options.add(new Option(
				xml.getElementsByTagName("layout")[0].childNodes[0].nodeValue,
				""));
		// document.getElementById("interval").value =
		// xml.getElementsByTagName("interval")[0].childNodes[0].nodeValue;
		var intervalSelect = document.getElementById("intervalSelect");
		intervalSelect.options.add(new Option(
						xml.getElementsByTagName("intervalText")[0].childNodes[0].nodeValue,
						""));
		// document.getElementById("output").value =
		// xml.getElementsByTagName("output")[0].childNodes[0].nodeValue;
		var outputSelect = document.getElementById("outputSelect");
		outputSelect.options.add(new Option(
				xml.getElementsByTagName("output")[0].childNodes[0].nodeValue,
				""));

		document.getElementById("is_haccp").value =
		 xml.getElementsByTagName("is_haccp")[0].childNodes[0].nodeValue;
		if (document.getElementById("is_haccp").value == "1") {
			document.getElementById("is_haccpCheckbox").checked = true;
		} else {
			document.getElementById("is_haccpCheckbox").checked = false;
		}
		// document.getElementById("frequency").value =
		// xml.getElementsByTagName("frequency")[0].childNodes[0].nodeValue;
		var frequencySelect = document.getElementById("frequencySelect");
		frequencySelect.options.add(new Option(
						xml.getElementsByTagName("frequencyText")[0].childNodes[0].nodeValue,
						""));
		document.getElementById("idaction").value = xml
				.getElementsByTagName("idaction")[0].childNodes[0].nodeValue;
		var activerule = xml
		.getElementsByTagName("activerule")[0].childNodes[0].nodeValue;
		if(activerule == "TRUE")
		{
			document.getElementById("activerule").checked = true;
		}
//		document.getElementById("action_param").value = xml
//				.getElementsByTagName("action_param")[0].childNodes[0].nodeValue;
//		var mailrows = xml.getElementsByTagName("mailrows")[0].childNodes[0].nodeValue;
//		emptyTable();
//		insertRows(mailrows);
		document.getElementById("sev_emails").value=xml.getElementsByTagName("mailrows")[0].childNodes[0].nodeValue;
		initIORow("mailbody","emails","sev_emails");
	} else {
		document.getElementById("cmd").value = "insert";
		showDaily(true);
		document.getElementById("layout").value = "I_InstantReport1";
		layoutSelect = document.getElementById("layoutSelect");
		layoutSelect.options.add(new Option("I_InstantReport1", ""));
		document.getElementById("interval").value = "0";
		intervalSelect = document.getElementById("intervalSelect");
		intervalSelect.options.add(new Option(
				document.getElementById("now").value, ""));

		document.getElementById("output").value = "PDF";
		outputSelect = document.getElementById("outputSelect");
		outputSelect.options.add(new Option("PDF", ""));

		document.getElementById("is_haccp").value = "1";
		document.getElementById("is_haccpCheckbox").checked = "true";

		document.getElementById("frequency").value = "0";
		frequencySelect = document.getElementById("frequencySelect");
		frequencySelect.options.add(new Option("------", ""));

//		var OnScreenKey = document.getElementById("OnScreenKey").value;
//		var rows = "";
//		if (OnScreenKey == 'true') {
//			rows = "<tr><td><input id='BAH_0' class='keyboardInput' type='text' size='40' maxlength='40' style='font-family:Verdana;font-size:8pt;' onblur='checkOnlyMail(this);'/></td>"
//					+ "<td align='right'><IMG onclick='deleteItem(this);' src='images/actions/removesmall_on.png'/></td></tr>";
//		} else {
//			rows = "<tr><td><input id='BAH_0' type='text' size='40' maxlength='40' style='font-family:Verdana;font-size:8pt;' onblur='checkOnlyMail(this);'/></td>"
//					+ "<td align='right'><IMG onclick='deleteItem(this);' src='images/actions/removesmall_on.png'/></td></tr>";
//		}
//		emptyTable();
//		insertRows(rows);
	}
//	mailnumber = document.getElementById("mailtable").rows.length;
//	buildKeyboardInputs();
	
}
function insertRows(rows) {
	document.getElementById("div_mailtable").innerHTML = "<table id='mailtable' width='100%' cellpadding=0 cellspacing=0 border=0>"
			+ "<tbody id='body_mailtable'>"
			+ document.getElementById("mailtable").tBodies[0].innerHTML
			+ rows
			+ "</tbody></table>";
}
function showDaily(show) {
	if (show == true) {
		document.getElementById("dailyTimebind").style.display = "block";
		document.getElementById("otherTimebind").style.display = "none";
	} else {
		document.getElementById("dailyTimebind").style.display = "none";
		document.getElementById("otherTimebind").style.display = "block";
	}
}

function subtab6_reportprint_save() {
	// var = document.getElementById("").value;
	var cmd = document.getElementById("cmd").value;
	var idreport = document.getElementById("idreport").value;
	var idaction = document.getElementById("idaction").value;
	var report_name = document.getElementById("report_name").value;
	if (report_name == "") {
		alert(document.getElementById("div_warning_name").value);
		return;
	}
	var layout = document.getElementById("layout").value;
	var interval = document.getElementById("interval").value;
	var output = document.getElementById("output").value;
	var is_haccp = document.getElementById("is_haccp").value;
	var sel_report_printer = document.getElementById("report_printerSelect").value;
	//var action_param = document.getElementById("action_param").value;
	var hour_from = document.getElementById("hour_from").value;
	var minut_from = document.getElementById("minut_from").value;
	var idtimeBand = document.getElementById("idtimeBand").value;
	
	var activerule = false;
	var checked = document.getElementById("activerule").checked;
	if(checked)
		activerule = true;
	var param = "";
	param += "cmd=" + cmd;
	if (cmd == "insert") {
		param += "&layout=" + layout;
		param += "&interval=" + interval;
		param += "&output=" + output;
		param += "&type=1";
		param += "&activerule=" + activerule;
		param += "&timeBandValue=" + getTimebindValue(hour_from, minut_from);
		if (is_haccp == "1") {
			param += "&is_haccp=" + is_haccp;
		}
	} else if (cmd == "update") {
		if (document.getElementById("dailyTimebind").style.display == "block") {
			param += "&idtimeBand=" + idtimeBand;
			param += "&timeBandValue="
					+ getTimebindValue(hour_from, minut_from);
		}
		param += "&idreport=" + idreport;
		param += "&idaction=" + idaction;
		//param += "&action_param=" + action_param;
	}
	param += "&report_name=" + report_name;
	param += "&sel_report_printer=" + sel_report_printer;
	param += "&activerule=" + activerule;
	var emails=document.getElementsByName("emails");
	for(var i=0;i<emails.length;i++){
		var BAH = "C_" + i + "," + emails[i].value + "," + emails[i].value + "," + "2";
		param += "&BAH_" + i + "=" + BAH;
	}
//	for ( var i = 0; i < mailnumber; i++) {
//		if (document.getElementById("BAH_" + i) == null) {
//			continue;
//		}
//		var mail = document.getElementById("BAH_" + i).value;
//		if (mail == "")
//			continue;
//		var BAH = "C_" + i + "," + mail + "," + mail + "," + "2";
//		param += "&BAH_" + i + "=" + BAH;
//	}
	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "POST", param,
			Callback_reportprint_save, true);
}

function getTimebindValue(hour_from, minut_from) {
	var hour_to;
	var minut_to;
	if (minut_from == "50") {
		if(hour_from.startsWith('0'))
		{
			var tmp = hour_from.substring(1,hour_from.length);
			hour_to = parseInt(tmp) + 1;
		}
		else
		{
			hour_to = parseInt(hour_from) + 1;
		}
		minut_to = "00";
		if (hour_to < 10) {
			hour_to = "0" + hour_to;
		}
	} else {
		hour_to = hour_from;
		minut_to = parseInt(minut_from) + 10;
	}
	return hour_from + ":" + minut_from + "-" + hour_to + ":" + minut_to;
}
function Callback_reportprint_save(xml) {
	var err = xml.getElementsByTagName("error");
	
	if(err[0] != null)
	{
		if (err[0].childNodes[0].nodeValue == "ok") 
		{
			wizard_tab6_init();
		}
		else if(err[0].childNodes[0].nodeValue == "timebandexist")
		{
			var noremovemsg = document.getElementById("noremovemsg").value;
			alert("TimebandByWizard -- "+noremovemsg);
		}
		else if(err[0].childNodes[0].nodeValue == "actionexist")
		{
			var actionnotremoved = document.getElementById("actionnotremoved").value;
			alert("HACCPReportActionByWizard -- "+actionnotremoved);
		}
		else
		{
			alert(err[0].childNodes[0].nodeValue);
		}
	}
	MTstopServerComm();
}

/**
 * -----begin----Subtab-5
 * @return
 */
function wizard_tab5_init(){
	var param = "";
	param += "&cmd=subtab5_init";
	
	enableAction(1);
	enableAction(2);
	enableAction(3);
	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "POST",param, Callback_tab5_init, true);
}
function Callback_tab5_init(xml){
	document.getElementById("idRule").value=xml.getElementsByTagName("idRule")[0].childNodes[0].nodeValue;
	document.getElementById("idCondition").value=xml.getElementsByTagName("idCondition")[0].childNodes[0].nodeValue;
	document.getElementById("actionCode").value=xml.getElementsByTagName("actionCode")[0].childNodes[0].nodeValue;
	document.getElementById("idTimeBand").value=xml.getElementsByTagName("idTimeBand")[0].childNodes[0].nodeValue;
	document.getElementById("standardTimeBand").value=xml.getElementsByTagName("standardTimeBand")[0].childNodes[0].nodeValue;
	document.getElementById("sev_emails").value=xml.getElementsByTagName("emails")[0].childNodes[0].nodeValue;
	document.getElementById("sev_faxs").value=xml.getElementsByTagName("faxs")[0].childNodes[0].nodeValue;
	document.getElementById("sev_smss").value=xml.getElementsByTagName("smss")[0].childNodes[0].nodeValue;
	document.getElementById("condtype1").checked=true;
	document.getElementById("priority1").checked=xml.getElementsByTagName("highest")[0].childNodes[0].nodeValue;
	document.getElementById("priority2").checked=xml.getElementsByTagName("hight")[0].childNodes[0].nodeValue;
	document.getElementById("priority3").checked=xml.getElementsByTagName("medium")[0].childNodes[0].nodeValue;
	document.getElementById("priority4").checked=xml.getElementsByTagName("low")[0].childNodes[0].nodeValue;
	document.getElementById("priority1").disabled=(xml.getElementsByTagName("isP")[0].childNodes[0].nodeValue=="true")?false:true;
	document.getElementById("priority2").disabled=(xml.getElementsByTagName("isP")[0].childNodes[0].nodeValue=="true")?false:true;
	document.getElementById("priority3").disabled=(xml.getElementsByTagName("isP")[0].childNodes[0].nodeValue=="true")?false:true;
	document.getElementById("priority4").disabled=(xml.getElementsByTagName("isP")[0].childNodes[0].nodeValue=="true")?false:true;
	document.getElementById("condtype6").checked=(xml.getElementsByTagName("isP")[0].childNodes[0].nodeValue=="true")?true:false;
	document.getElementById("smtp").value=xml.getElementsByTagName("smtp")[0].childNodes[0].nodeValue;
	document.getElementById("iomailport").value=xml.getElementsByTagName("port")[0].childNodes[0].nodeValue;
	document.getElementById("user").value=xml.getElementsByTagName("user")[0].childNodes[0].nodeValue;
	document.getElementById("sender").value=xml.getElementsByTagName("sender")[0].childNodes[0].nodeValue;
	document.getElementById("pwd").value=xml.getElementsByTagName("pwd")[0].childNodes[0].nodeValue;
	document.getElementById("iomailencryption").checked=xml.getElementsByTagName("encryption")[0].childNodes[0].nodeValue != "NONE";
	
	document.getElementById("relay_1").checked=false;
	document.getElementById("relay_1_o").disabled=true;
	document.getElementById("relay_1_c").disabled=true;
	document.getElementById("relay_2").checked=false;
	document.getElementById("relay_2_o").disabled=true;
	document.getElementById("relay_2_c").disabled=true;
	document.getElementById("relay_3").checked=false;
	document.getElementById("relay_3_o").disabled=true;
	document.getElementById("relay_3_c").disabled=true;
	var relays=xml.getElementsByTagName("relays");
	for(var i=0;i<relays.length;i++){
		if(document.getElementById("relay_1").value==relays[i].childNodes[0].childNodes[0].nodeValue){
			document.getElementById("relay_1").checked=true;
			document.getElementById("relay_1_o").checked=((relays[i].childNodes[1].childNodes[0].nodeValue)=="1")?true:false;
			document.getElementById("relay_1_c").checked=!document.getElementById("relay_1_o").checked;
			document.getElementById("relay_1_o").disabled=false;
			document.getElementById("relay_1_c").disabled=false;
		}else if(document.getElementById("relay_2").value==relays[i].childNodes[0].childNodes[0].nodeValue){
			document.getElementById("relay_2").checked=true;
			document.getElementById("relay_2_o").checked=((relays[i].childNodes[1].childNodes[0].nodeValue)=="1")?true:false;
			document.getElementById("relay_2_c").checked=!document.getElementById("relay_2_o").checked;
			document.getElementById("relay_2_o").disabled=false;
			document.getElementById("relay_2_c").disabled=false;
		}else if(document.getElementById("relay_3").value==relays[i].childNodes[0].childNodes[0].nodeValue){
			document.getElementById("relay_3").checked=true;
			document.getElementById("relay_3_o").checked=((relays[i].childNodes[1].childNodes[0].nodeValue)=="1")?true:false;
			document.getElementById("relay_3_c").checked=!document.getElementById("relay_3_o").checked;
			document.getElementById("relay_3_o").disabled=false;
			document.getElementById("relay_3_c").disabled=false;
		}
	}

	initIORow("mailbody","emails","sev_emails","mail_img","E");
	initIORow("faxBody","faxs","sev_faxs","fax_img","F");
	initIORow("smsBody","smss","sev_smss","sms_img","S");
	initTestIcon("mail_img","emails","E");
	initTestIcon("fax_img","faxs","F");
	initTestIcon("relay_img","","L");
	initTestIcon("sms_img","smss","S");
	document.getElementById("sameActionTimeband").value=xml.getElementsByTagName("sameActionTimeband")[0].childNodes[0].nodeValue;
	if(document.getElementById("sameActionTimeband").value=="D"){
		alert(document.getElementById("diffrules").value);
	}
	MTstopServerComm();
}
function checkPage()
{
	var senderInput = document.getElementById("sender");
	if(senderInput != null)
	{
		if(checkOnlyMail(senderInput) == false)
		{
			return false;
		}
	}
	if(document.getElementById("condtype6").checked==true){
		if(document.getElementById("priority1").checked==false&&
			document.getElementById("priority2").checked==false&&
			document.getElementById("priority3").checked==false&&
			document.getElementById("priority4").checked==false){
			alert(document.getElementById("choosepriority").value);
			return false;
		}
	}
	if(document.getElementById("smtp").value!="" ||
		document.getElementById("sender").value!=""){
		
		if(document.getElementById("smtp").value==""){
			alert(document.getElementById("fillSMTP").value);
			return false;
		}
//		if(document.getElementById("user").value==""){
//			alert(document.getElementById("fillUSER").value);
//			return false;
//		}
		if(document.getElementById("sender").value==""){
			alert(document.getElementById("fillSENDER").value);
			return false;
		}
//		if(document.getElementById("pwd").value==""){
//			alert(document.getElementById("fillPWD").value);
//			return false;	
//		}
	}
	return true;
}
	
function prepareParam(types){
	var param = "";
	param+="&idRule="+document.getElementById("idRule").value;
	param+="&idCondition="+document.getElementById("idCondition").value;
	param+="&actionCode="+document.getElementById("actionCode").value;
	param+="&idTimeBand="+document.getElementById("idTimeBand").value;
	param+="&sameActionTimeband="+document.getElementById("sameActionTimeband").value;
	param+="&standardTimeBand="+document.getElementById("standardTimeBand").value;
	if(document.getElementById("condtype1").checked==true){
		param+="&condtype="+document.getElementById("condtype1").value;
	}else{
		param+="&condtype="+document.getElementById("condtype6").value;
		if(document.getElementById("priority1").checked==true){
			param+="&priority1="+document.getElementById("priority1").value;
		}
		if(document.getElementById("priority2").checked==true){
			param+="&priority2="+document.getElementById("priority2").value;
		}
		if(document.getElementById("priority3").checked==true){
			param+="&priority3="+document.getElementById("priority3").value;
		}
		if(document.getElementById("priority4").checked==true){
			param+="&priority4="+document.getElementById("priority4").value;
		}
	}
	//smtps/faxs/relays/smss
	param+="&smtp="+document.getElementById("smtp").value;
	param+="&port="+document.getElementById("iomailport").value;
	param+="&user="+document.getElementById("user").value;
	param+="&sender="+document.getElementById("sender").value;
	param+="&pwd="+document.getElementById("pwd").value;
	param += "&encryption=" + (document.getElementById("iomailencryption").checked ? "TLS" : "NONE");
	var emails=document.getElementsByName("emails");
	var temp="";
	for(var i=0;i<emails.length;i++){
		temp+=emails[i].value+";";
	}
	param+="&emails="+temp;
	var faxs=document.getElementsByName("faxs");
	temp="";
	for(var i=0;i<faxs.length;i++){
		temp+=faxs[i].value+";";
	}
	param+="&faxs="+temp;
	
	if(document.getElementById("relay_1").checked==true){
		param+="&relay_1_addr="+document.getElementById("relay_1").value;
		param+="&relay_1="+(document.getElementById("relay_1_o").checked==true?document.getElementById("relay_1_o").value:document.getElementById("relay_1_c").value);
	}
	if(document.getElementById("relay_2").checked==true){
		param+="&relay_2_addr="+document.getElementById("relay_2").value;
		param+="&relay_2="+(document.getElementById("relay_2_o").checked==true?document.getElementById("relay_2_o").value:document.getElementById("relay_2_c").value);
	}
	if(document.getElementById("relay_3").checked==true){
		param+="&relay_3_addr="+document.getElementById("relay_3").value;
		param+="&relay_3="+(document.getElementById("relay_3_o").checked==true?document.getElementById("relay_3_o").value:document.getElementById("relay_3_c").value);
	}

	var smss=document.getElementsByName("smss");
	temp="";
	for(var i=0;i<smss.length;i++){
		temp+=smss[i].value+";";
	}
	param+="&smss="+temp;
	
	if(emails.length>0){
		param+="&act_mail=1";
	}
	if(faxs.length>0){
		param+="&act_fax=1";
	}
	if(document.getElementById("relay_1").checked==true ||
		document.getElementById("relay_2").checked==true ||
		document.getElementById("relay_3").checked==true){
		param+="&act_relay=1";
	}
	if(smss.length>0){
		param+="&act_sms=1";
	}
	
	return param;
}

function subtab5_Alarm_Management_save(){
	if(document.getElementById("sameActionTimeband").value=="D"){
		if(confirm(document.getElementById("diffrules").value+document.getElementById("overwrite").value)==false)
			return false;
	}
	if(checkPage()==false) return false;
	var param=prepareParam();
	param += "&cmd=save";
	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "POST",param, Callback_subtab5_Alarm_Management_save, true);
	return true;
}

function Callback_subtab5_Alarm_Management_save(xml){
	var err = xml.getElementsByTagName("error");
	if(err[0].childNodes[0].nodeValue == "emptyactionslist"){
		var action=xml.getElementsByTagName("errorAction")[0].childNodes[0].nodeValue;
		alert(document.getElementById("emptyactionslist").value);
	}
	else if(err[0].childNodes[0].nodeValue == "internal_modem")
	{
		alert(document.getElementById("internal_modem").value);
	}
	else if(err[0].childNodes[0].nodeValue == "GSM_modem")
	{
		alert(document.getElementById("gsm_modem").value);
	}
	wizard_tab5_init();
	MTstopServerComm();
}

function testio(type){
		if(checkPage()==false) return false;
		var param=prepareParam(type);
		param += "&tio="+type+"&cmd=testio";
		
		var tabs=document.getElementById("feedbackArea");
		while(tabs.hasChildNodes()){
			tabs.removeChild(tabs.firstChild);
		}
		var rows = document.createElement("tr");
		var cel0 = document.createElement("td");
		cel0.style.width="100%";
		cel0.className = "th";
//		cel0.bgColor="gray";
		cel0.innerHTML ="<B>"+document.getElementById('inprocess').value+"<B>:<img src=\"images/ajax-loader_white.gif\">";
		rows.appendChild(cel0);
		tabs.appendChild(rows);
		
		// get the result timer
//		timer= setInterval(testiotimer, 2000); 
		new AjaxRequest("servlet/ajrefresh", "POST", param, Callback_testio, false);
		new AjaxRequest('servlet/ajrefresh', 'GET','cmd=IOTEST', '', false);
}

function Callback_testio(xml){
	var tabs=document.getElementById("feedbackArea");
	while(tabs.hasChildNodes()){
		tabs.removeChild(tabs.firstChild);
	}
	var tio= xml.getElementsByTagName("tio");
	var err = xml.getElementsByTagName("testresult");
	var rows = document.createElement("tr");
	var cel0 = document.createElement("td");
	cel0.style.width="100%";
	cel0.className = "th";
//	cel0.bgColor="gray";
	var test_type="";
	if(tio[0].childNodes[0].nodeValue=="E") test_type=document.getElementById('p_email').value;
	if(tio[0].childNodes[0].nodeValue=="F") test_type=document.getElementById('p_fax').value;
	if(tio[0].childNodes[0].nodeValue=="L") test_type=document.getElementById('p_relay').value;
	if(tio[0].childNodes[0].nodeValue=="S") test_type=document.getElementById('p_sms').value;
	cel0.innerHTML ="<B>"+test_type+" "+document.getElementById('conntestresult').value+"<B>:";
	rows.appendChild(cel0);
	tabs.appendChild(rows);
	for(var i=0;i<err.length;i++){
		rows = document.createElement("tr");
		tabs.appendChild(rows);
		cel0 = document.createElement("td");
		cel0.style.width="100%";
		cel0.className = "standardTxt";
		var dest=err[i].childNodes[0].childNodes[0].nodeValue;
		var message=err[i].childNodes[1].childNodes[0].nodeValue;
		var tStatus=err[i].childNodes[2].childNodes[0].nodeValue;
		var result=tStatus;
		if(tStatus=="3"){
			//info
			cel0.bgColor="#00ff00";
			result=message;
		}else if(tStatus=="1"){
			//fail
			cel0.bgColor="#ff0000";	
			result=message;
		}else if(tStatus=="2"){
			//alert
			cel0.bgColor="#ff0000";	
			result=message;
		}else{
//			cel0.bgColor="gray";	
			result=document.getElementById('inprocess').value;
		}
		if(tio[0].childNodes[0].nodeValue=="L"){
			if(document.getElementById("relay_1").value==dest)
				cel0.innerHTML ="<li>"+document.getElementById('relay01').value+"1="+result+"</li>";
			if(document.getElementById("relay_2").value==dest)
				cel0.innerHTML ="<li>"+document.getElementById('relay01').value+"2="+result+"</li>";
			if(document.getElementById("relay_3").value==dest)
				cel0.innerHTML ="<li>"+document.getElementById('relay01').value+"3="+result+"</li>";
		}else{
			cel0.innerHTML ="<li>"+dest+"="+result+"</li>";
		}
		rows.appendChild(cel0);
	}

}
function changeType(flag){
	document.getElementById('priority1').disabled=flag;
	document.getElementById('priority2').disabled=flag;
	document.getElementById('priority3').disabled=flag;
	document.getElementById('priority4').disabled=flag;
}
function changeArea(area){
	var temp=document.getElementById(area);
	if(temp.style.display=="none"){
		temp.style.display="block";
	}else{
		temp.style.display="none";
	}
}
function changeStatus(sf,o,c){
	document.getElementById(o).disabled=!(sf.checked);
	document.getElementById(c).disabled=!(sf.checked);
//	initTestIcon("relay_img","","L");
	disableTestIcon("relay_img");
}
function jump2Mainpage(){
	top.frames['menuPVPRO2'].document.getElementById('dvm2').onclick();
}
function jump2Tab(pg){
	top.frames['body'].frames['TabMenu'].document.getElementById(pg).onclick();
}
function initIORow(tb,ioName,sevtype,imgs,types){
	var tab=document.getElementById(tb);
	while(tab.hasChildNodes()){
		tab.removeChild(tab.firstChild);
	}
	var temp=document.getElementById(sevtype).value.split(";");
	for(var i=0;i<temp.length;i++){
		appendNewRow(tb,ioName,temp[i],imgs,types);
	}
}
function appendNewRow(tb,ioName,desc,imgs,types){
	desc=trim(desc);
	if(desc==null || desc=="") return false;
	var tab=document.getElementById(tb);
	 row = document.createElement("tr");
	 tab.appendChild(row);
	
	var cel0 = document.createElement("td");
	row.className = "Row1";
	cel0.className = "standardTxt";
	cel0.style.width="100%";
	cel0.innerHTML = "<input type=\"hidden\"  name=\""+ioName+"\" value=\""+desc+"\">"+desc;
	row.appendChild(cel0);
	
	var cel1 = document.createElement("td");
	cel1 = document.createElement("td");
	cel1.className = "standardTxt";
	cel1.style.width="30";
	cel1.align="center";
	if(imgs==null){
		cel1.innerHTML=" <IMG onclick='deleteCurrRow(this);' style=\"cursor:pointer;\" src='images/actions/removesmall_on_black.png'/>";
	}else{
		cel1.innerHTML=" <IMG onclick='deleteCurrRow(this,\""+imgs+"\");' style=\"cursor:pointer;\" src='images/actions/removesmall_on_black.png'/>"; //initTestIcon(\""+imgs+"\",\""+ioName+"\",\""+types+"\");
	}
	row.appendChild(cel1);
	return true;
}
function addNewRow(tb,ioName,inputbox,imgs,types){
	var box=document.getElementById(inputbox);
	if(types == "E" && box != null){
		if(checkOnlyMail(box) == false){
			return;
		}
	}
	var list=document.getElementsByName(ioName);
	if(box.value==""){
		box.focus();
		return false;
	}
	for(var i=0;i<list.length;i++){
		//if it is exist,return back.
		if(trim(list[i].value)==trim(box.value))
			return false;
	}
	
	appendNewRow(tb,ioName,box.value,imgs,types);
	//subtab5_Alarm_Management_save();
//	initTestIcon(imgs,ioName,types);
	if(imgs != null){
		disableTestIcon(imgs);
	}
	box.value="";
	
	////2010-12-24, Kevin Ge, HACCP report, after modified, check Active rule
	checkActiverule();
}
function deleteCurrRow(obj,imgs){
	var tr=obj.parentNode.parentNode;
	tr.parentNode.removeChild(tr);
	if(imgs!=null){
		disableTestIcon(imgs);
	}
	//subtab5_Alarm_Management_save();
	
	//2010-12-24, Kevin Ge, HACCP report, after modified, check Active rule
	checkActiverule();
}
function initTestIcon(imgs,ioName,types){
	var testall = document.getElementById("testall").value;
	if(imgs!=null){
		var temp=0;
		if(types=="L"){
			if(document.getElementById("relay_1").checked==true ||
				document.getElementById("relay_2").checked==true ||
				document.getElementById("relay_3").checked==true)
				temp=1;
		}else{
			var list=document.getElementsByName(ioName);
			if(list.length>0) temp=1;
		}
		if(temp==1){
			document.getElementById(imgs).innerHTML="<img src=\"images/actions/params_on_black.png\" title=\""+testall+"\" width=\"25px\" style=\"cursor:pointer;\" onclick=\"testio('"+types+"');\">";
		}else{
			disableTestIcon(imgs);
		}
	}
}
function disableTestIcon(imgs){
	if(imgs != null){
		document.getElementById(imgs).innerHTML="<img src=\"images/actions/params_off.png\" width=\"25px\">";
	}
}
function trim(str) {   
	return str.replace(/(^[\s\xA0]+|[\s\xA0]+$)/g, '');   
}
/**
 * -----end----Subtab-5
 * @return
 */
function DIVmoveInit(divID, evt) {
	_IsMousedown = 1;
	if (getBrowserType() == "NSupport") {
		return;
	}
	var obj = getObjById(divID);
	if (getBrowserType() == "fox") {
		_ClickLeft = evt.pageX - parseInt(obj.style.left);
		_ClickTop = evt.pageY - parseInt(obj.style.top);
	} else {
		_ClickLeft = evt.x - parseInt(obj.style.left);
		_ClickTop = evt.y - parseInt(obj.style.top);
	}
}
function DIVMove(divID, evt) {
	if (_IsMousedown == 0) {
		return;
	}
	var objDiv = getObjById(divID);
	if (getBrowserType() == "fox") {
		objDiv.style.left = evt.pageX - _ClickLeft;
		objDiv.style.top = evt.pageY - _ClickTop;
	} else {
		objDiv.style.left = evt.x - _ClickLeft;
		objDiv.style.top = evt.y - _ClickTop;
	}
}
function stopDIVMove() {
	_IsMousedown = 0;
}
function getObjById(id) {
	return document.getElementById(id);
}
function getBrowserType() {
	var browser = navigator.appName
	var b_version = navigator.appVersion
	var version = parseFloat(b_version)
	if ((browser == "Netscape")) {
		return "fox";
	} else if (browser == "Microsoft Internet Explorer") {
		if (version >= 4) {
			return "ie4+";
		} else {
			return "ie4-";
		}
	} else {
		return "NSupport";
	}
}

//2010-12-24, Kevin Ge, check active rule
function checkActiverule()
{
	var activerule = document.getElementById("activerule");
	if(activerule != null && activerule != "undefined")
	{
		activerule.checked = true;
	}
}

function IoTechEEnc(bTls)
{
	document.getElementById("iomailport").value = bTls ? 587 : 25;
}
