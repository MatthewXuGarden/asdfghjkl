var DEV_LIMIT = 3;
var DEV_OFFSET = 0;
var PAGE_LIMIT = 1000;
var REFRESHTIME = 100;
var FIRSTREFRESH = 100;
var NEXTREFRESH = 5000;
var first = true;
var MAXLENGTH = 32;
var PointerObj = null;
var OldColor = "#FFFFFF";

var nDevices = 0;
var currentPage = 0;
var lineIdFilter = 0;


function redirect(iddevice)
{
	//top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev='+iddevice+'&desc=ncode01');
	top.frames['body'].frames['bodytab'].location.href = top.frames['manager'].getDocumentBase()
		+ "mobile/DeviceMain.jsp;"
		+ top.frames['manager'].getSessionId()
		+ "?iddev=" + iddevice;
}


function onLoadDevices()
{
	updatePrevNext(false, false);
	startRefresh(first);
}


function onBtnPrev()
{
	if( currentPage <= 0 )
		return;
	waitCursor(true);
	var maintable = document.getElementById("deviceviewcontainer");
	while( maintable.rows.length > 0 )
		maintable.deleteRow(0);
	nDevices = 0;
	currentPage--;
	DEV_OFFSET = currentPage * PAGE_LIMIT;
	REFRESHTIME = FIRSTREFRESH;
	first = true;
	startRefresh(first);
}


function onBtnNext()
{
	waitCursor(true);
	var maintable = document.getElementById("deviceviewcontainer");
	while( maintable.rows.length > 0 )
		maintable.deleteRow(0);
	nDevices = 0;
	currentPage++;
	DEV_OFFSET = currentPage * PAGE_LIMIT;
	REFRESHTIME = FIRSTREFRESH;
	first = true;
	startRefresh(first);
}


function waitCursor(bWait)
{
	adjustContainer();
	var tt = document.getElementById("deviceviewloading");
	if( tt )
		tt.style.display = bWait ? 'block' : 'none';
}


function onSelectLine(idLine)
{
	lineIdFilter = idLine;
	waitCursor(true);
	var maintable = document.getElementById("deviceviewcontainer");
	while( maintable.rows.length > 0 )
		maintable.deleteRow(0);
	nDevices = 0;
	currentPage = 0;
	DEV_OFFSET = currentPage * PAGE_LIMIT;
	REFRESHTIME = FIRSTREFRESH;
	first = true;
	startRefresh(first);	
}


function startRefresh(first)
{
	if(document.getElementById("deviceviewcontainer")==null)
		return;
	//DEV_OFFSET = (DEV_OFFSET + DEV_LIMIT);
	CommSend("servlet/ajrefresh", "POST", "cmd=mixedrfh&limit="+DEV_LIMIT+"&offset="+DEV_OFFSET+"&first="+first + "&idline=" + lineIdFilter, "deviceviewn");
}

function Callback_deviceviewn()
{
	var maintable = document.getElementById("deviceviewcontainer");
	var listDevice = xmlResponse.getElementsByTagName("device");

	var i=0;
	var idDevice = -1;
	var status;
	
	for (i = 0; i < listDevice.length; i++)
	{
		var devicecells = retrieveDeviceCellsNumber();
		idDevice = listDevice[i].getAttribute("id");
		if(isWinaries(idDevice)) continue; // skip
		var deviceTable = document.getElementById("d_"+idDevice);
		if(deviceTable==null){ //vero --> manca riquadro --> aggiungo, sto facendo il primo giro
			if(devicecells%3==0){
				var newrow = maintable.insertRow(maintable.rows.length);
				var newcell = newrow.insertCell(0);
				newcell.style.width="33%";
				newcell = newrow.insertCell(0);
				newcell.style.width="33%";
				newcell = newrow.insertCell(0);
				newcell.style.width="33%";
			}
			var x = maintable.rows.length-1;
			var y = devicecells%3;
			maintable.rows[x].cells[y].innerHTML = createDeviceTable(listDevice[i]);;
		} else {
			//gi� fatto un giro, devo solo aggiornare i valori
			updateDeviceTable(listDevice[i]);
		}
		nDevices++;
	}
	
	var infotable = document.getElementById("winariescontainer");
	var listCHinfo = xmlResponse.getElementsByTagName("info");
	if (listCHinfo != null)
	{
		for (i = 0; i < listCHinfo.length; i++)
		{
			var wncells = retrieveWnCellsNumber();
			idDevice = listCHinfo[i].getAttribute("id");
			status = listCHinfo[i].getAttribute("status");
			var deviceWn = document.getElementById("W_"+idDevice);
			if(deviceWn==null){
				if(wncells%8==0){
					newrow = infotable.insertRow(infotable.rows.length);
					newcell = newrow.insertCell(0);
					newcell.style.width="13%";
					newcell = newrow.insertCell(0);
					newcell.style.width="12%";
					newcell = newrow.insertCell(0);
					newcell.style.width="13%";
					newcell = newrow.insertCell(0);
					newcell.style.width="12%";
					newcell = newrow.insertCell(0);
					newcell.style.width="13%";
					newcell = newrow.insertCell(0);
					newcell.style.width="12%";
					newcell = newrow.insertCell(0);
					newcell.style.width="13%";
					newcell = newrow.insertCell(0);
					newcell.style.width="12%";
				}
				x = infotable.rows.length-1;
				y = wncells%8;
				infotable.rows[x].cells[y].innerHTML = createWnTable(listCHinfo[i]);;
			} else {
				//gi� fatto un giro, devo solo aggiornare i valori
				updateWnTable(listCHinfo[i]);
			}
		}
	}
	
	if( listDevice.length < DEV_LIMIT ) {
		if(first) {
			waitCursor(false);
			first = false;
		}
		REFRESHTIME=NEXTREFRESH;
		DEV_OFFSET = currentPage * PAGE_LIMIT;
		nDevices = 0;
		if( PAGE_LIMIT < 1000 )
			updatePrevNext(currentPage > 0, false);
	}
	else {
		if( nDevices < PAGE_LIMIT )
			DEV_OFFSET += DEV_LIMIT;
		else {
			REFRESHTIME=NEXTREFRESH;
			if(first) {
				waitCursor(false);
				first = false;
			}
			DEV_OFFSET = currentPage * PAGE_LIMIT;
			nDevices = 0;
		}
		if( PAGE_LIMIT < 1000 )
			updatePrevNext(currentPage > 0, true);
	}
	setTimeout("startRefresh("+first+")", REFRESHTIME);
}

function createDeviceTable(deviceXml){
	var iddev=deviceXml.getAttribute("id");
	var stringreturntable="<table id=\"d_"+iddev+"\" width=\"100%\" height=\"100%\" cellspacing=\"1\" "+
	"cellpadding=\"0\" onmouseout=\"dwhighlight(this,false);\" onmouseover=\"dwhighlight(this,true);\" " +
	"onclick=\"redirect("+iddev+");\" class=\"ajaxdevtable\"><tr>" +
	"<td width=\"8%\" align=\"left\">" +
		"<div style=\"background-image: url(images/led/L"+deviceXml.getAttribute("status")+".gif); " +
		"background-repeat: no-repeat; background-position: center center;\" id=\"DLed"+iddev+"\">" +
			"<div style=\"visibility: hidden;\">"+deviceXml.getAttribute("status")+"</div>" +
		"</div>" +
	"</td>" +
	"<td width=\"*\" class=\"standardTxt\">" +
		"<b>"+deviceXml.getElementsByTagName("dn")[0].childNodes[0].nodeValue+"</b>" +
	"</td>" +
	"<td width=\"15%\" class=\"standardTxt\">" +
	"<b>"+deviceXml.getElementsByTagName("c")[0].childNodes[0].nodeValue+"</b>" +
	"</td>" +
	"</tr>";
	for(var kk=0;kk<deviceXml.getElementsByTagName("var").length;kk++){
		var varelem=deviceXml.getElementsByTagName("var")[kk];
		var varstr=varelem.getAttribute("id");
//		idvar = varstr.split("_");//v_iddev_idvar
		stringreturntable+="<tr>" +
		"<td width=\"8%\">&nbsp;</td>" +
		"<td colspan=\"2\">" +
		"<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">";
		if (kk%2==0) {
			stringreturntable+="<tr class='Row1'>";
		}
		else {
			stringreturntable+="<tr class='Row2'>";
		}
		stringreturntable+="<td width=\"*\" align=\"left\" class=\"td\"><nobr>";
		var totlength = deviceXml.getElementsByTagName("n"+varstr)[0].childNodes[0].nodeValue;
		totlength+=varelem.childNodes[0]!=null?varelem.childNodes[0].nodeValue.length:0;
		totlength+=deviceXml.getElementsByTagName("m"+varstr)[0].childNodes[0]!=null?
				deviceXml.getElementsByTagName("m"+varstr)[0].childNodes[0].length:0;
		stringreturntable+=deviceXml.getElementsByTagName("n"+varstr)[0].childNodes[0].nodeValue.substr(0,MAXLENGTH);
		if(totlength>MAXLENGTH)
			stringreturntable+=" <a title=\""+deviceXml.getElementsByTagName("n"+varstr)[0].childNodes[0].nodeValue+"\">...</a>";
		stringreturntable+="</nobr>" +
		"</td>" +
		"<td id=\""+varstr+"\" width=\"10%\" align=\"right\" class=\"td\"><nobr>";
		var varval = varelem.childNodes[0];
		stringreturntable+=varval!=null?varval.nodeValue:"***";
		stringreturntable+="</nobr></td>" +
		"<td width=\"10%\" align=\"right\" class=\"td\"><nobr>";
		var varms = deviceXml.getElementsByTagName("m"+varstr)[0].childNodes[0];
		stringreturntable+=varms!=null?varms.nodeValue:"&nbsp;";
		stringreturntable+="</nobr></td>" +
		"</tr>" +
		"</table>" +
		"</td>" +
		"</tr>";
	}
	stringreturntable+="</table>";
	return stringreturntable;
}

function createWnTable(infoXml){
	var iddev=infoXml.getAttribute("id");
	var stringreturntable=
	"<table id='W_"+iddev+"' width='100%' height='40' cellspacing='1' cellpadding='0' border='0'" +
	"onmouseout='dwhighlight(this,false);' " +
	"onmouseover='dwhighlight(this,true);' " +
	"onclick='redirect("+iddev+");' "+
	"class='ajaxdevtable'>" +
	"<tr>" +
	"<td width='8%' align='left'>" +
	"<div style='background: url(images/led/L"+(infoXml.getAttribute("status")=='nop'?0:1)+".gif) " +
	"no-repeat center center;' id='WLed"+iddev+"'>" +
	"<div style='visibility: hidden;'>"+infoXml.getAttribute("status")+"</div>" +
	"</div>" +
	"</td>" +
	"<td width='*' class='standardTxt'>" +
	"<b><nobr>"+infoXml.getElementsByTagName("dn")[0].childNodes[0].nodeValue.substr(0,8)+
	(infoXml.getElementsByTagName("dn")[0].childNodes[0].nodeValue.length>8?
			"<a title='"+infoXml.getElementsByTagName("dn")[0].childNodes[0].nodeValue+"'>...</a>":"")+
	"</nobr></b>" +
	"</td>" +
	"<td width='15%' class='standardTxt'>";
	if ( infoXml.getAttribute("status")=="nop"){  //se processo non attivo
		stringreturntable+="<div id='wv_" + iddev + "' style='width: 10px;'><br></div>"; 
    } else if (infoXml.getAttribute("status")=="H") { //riscaldamento
        stringreturntable+="<div id='wv_" + iddev + "' style='width: 7px;background-color:ORANGERED'><br></div>";
    } else { //raffreddamento
    	stringreturntable+="<div id='wv_" + iddev + "' style='width: 7px;background-color:MEDIUMBLUE'><br></div>";
    }
	stringreturntable+="</td>" +
	"</tr>" +
	"</table>";
	return stringreturntable;
}

function updateDeviceTable(deviceXml){
	var iddev=deviceXml.getAttribute("id");
	var led = document.getElementById("DLed"+iddev);
	led.style.background = "url(images/led/L"+deviceXml.getAttribute("status")+".gif) no-repeat center center";
	led.childNodes[0].innerHTML=deviceXml.getAttribute("status");
	for(var kk=0;kk<deviceXml.getElementsByTagName("var").length;kk++){
		var varelem=deviceXml.getElementsByTagName("var")[kk];
		var varstr=varelem.getAttribute("id");
		var vartd = document.getElementById(varstr);
		var varval = varelem.childNodes[0];
		vartd.innerHTML = varval!=null?"<nobr>"+varval.nodeValue+"</nobr>":"***";
	}
}

function updateWnTable(infoXml){
	var iddev=infoXml.getAttribute("id");
	var led = document.getElementById("WLed"+iddev);
	var status = infoXml.getAttribute("status")=="nop"?0:1;
	led.style.background = "url(images/led/L"+status+".gif) no-repeat center center";
	led.childNodes[0].innerHTML=infoXml.getAttribute("status");
	var statuscolor = infoXml.getAttribute("status")=="nop"?"":
		infoXml.getAttribute("status")=="H"?"ORANGERED":
			infoXml.getAttribute("status")=="C"?"MEDIUMBLUE":"";
	document.getElementById("wv_"+iddev).style.backgroundColor = statuscolor;
}

function dwhighlight(obj,bval)
{
	if (bval)
	{		
		PointerObj = obj;
		OldColor = PointerObj.style.backgroundColor;
		//PointerObj.style.backgroundColor = "#BBBBBB";
		PointerObj.style.borderColor = "#000000";
	}
	else
	{
		if (PointerObj != null)
		PointerObj.style.borderColor = OldColor;
	}
}

function retrieveDeviceCellsNumber(){
	var counter=0;
	var rows = document.getElementById("deviceviewcontainer").rows;
	if(rows==null) return 0;
	for(var kk = 0;kk<rows.length;kk++){
		for(var jj = 0;jj<rows[kk].cells.length;jj++){
			if(rows[kk].cells[jj].childNodes[0]!=null &&
					rows[kk].cells[jj].childNodes[0].tagName.toUpperCase()=="TABLE")
				counter++;
		}
	}
	return counter;
}

function retrieveWnCellsNumber(){
	var counter=0;
	var rows = document.getElementById("winariescontainer").rows;
	if(rows==null) return 0;
	for(var kk = 0;kk<rows.length;kk++){
		for(var jj = 0;jj<rows[kk].cells.length;jj++){
			if(rows[kk].cells[jj].childNodes[0]!=null &&
					rows[kk].cells[jj].childNodes[0].tagName.toUpperCase()=="TABLE")
				counter++;
		}
	}
	return counter;
}

function isWinaries(iddev) {
	var infos = xmlResponse.getElementsByTagName("info");	
	for(var jj=0;jj<infos.length;jj++) {
		if(infos[jj].getAttribute("id")==iddev){
			return true;
		}
	}
	return false;
}