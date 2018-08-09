var ACTION_PREVIEW		= 1;
var ACTION_PRINT		= 2;
var ACTION_PRINT_ALL	= 3;

// tab selection
var selectedTab = null;
var aTabs = {
	// standard reports
	tab_group: { 
		report: 	"energy_reportpercent_1",
		preview: 	"hrfCakeZoom",
		print:		"hrfCakePrint"
	},
	tab_kw: {
		report:		"energy_reportdetail_1",
		preview:	"lineZoom",
		print:		"linePrint"
	},
	tab_kwh: {
		report:		"energy_reportdetail_2",
		preview:	"barZoom",
		print:		"barPrint"
	},
	// timeslot reports
	tab_timeslot: {
		report:		"energy_reportpercent_2",
		preview:	"hrfCakeZoomB",
		print:		"hrfCakePrintB"
	},
	tab_kwh_ts: {
		report:		"energy_reportdetail_3",
		preview:	"barZoomC",
		print:		"barPrintC"
	},
	tab_ts_kwh: {
		report:		"energy_reportdetail_4",
		preview:	"barZoomD",
		print:		"barPrintD"
	}
};
// store links used to preview/print reports
var aHref = {
};


function onLoadGraph()
{
	var selectedTabName = "tab_group";
	var tabs = document.getElementById("table_tabs").rows[0]; 
	for(var i = 0; i < tabs.cells.length; i++) {
		if( tabs.cells[i].id == selectedTabName ) {
			selectedTab = tabs.cells[i];
			break;
		}
	}
	//getReport('monthly','day','m_year',document.getElementById('month').value);
	getReport('weekly','day','w_year',document.getElementById('week').value);
	enableAction(ACTION_PREVIEW);
	enableAction(ACTION_PRINT);
	enableAction(ACTION_PRINT_ALL);
}


function preview()
{
	var basePath = document.getElementById("basepath").value;
	window.open(basePath + aHref[aTabs[selectedTab.id].preview],
		"_blank", "directories=no,location=no,menubar=no,status=no,toolbar=no");
}


function print()
{
	var basePath = document.getElementById("basepath").value;
	window.open(basePath + aHref[aTabs[selectedTab.id].print],
		"_blank", "directories=no,location=no,menubar=no,status=no,toolbar=no");
}


function printAll()
{
	var basePath = document.getElementById("basepath").value;
	window.open(basePath + "app/energy/report.jsp;jsessionid=" + document.getElementById("sessionid").value
		+ "?print=y",
		"_blank", "directories=no,location=no,menubar=no,status=no,toolbar=no");
}


function energtabyover(obj) {
	if (obj.className == "egtabselected2") {
		return;
	}
	obj.className = "egtabmouseover2";
}


function energytabout(obj) {
	if (obj.className == "egtabselected2") {
		return;
	}
	obj.className = "egtabnotselected2";
}


function switchTab(obj) {
	if( obj == selectedTab )
		return; // tab already selected
	// hide tab
	selectedTab.className = "egtabnotselected2";
	var report = document.getElementById(aTabs[selectedTab.id].report);
	report.style.visibility = "hidden";
	report.style.display = "none";
	// show tab
	selectedTab = obj;
	selectedTab.className = "egtabselected2";
	report = document.getElementById(aTabs[selectedTab.id].report);
	report.style.visibility = "visible";
	report.style.display = "block";
}


function getReport(reporttype, reportstep, specificYear , startparam) {
	// highlight selection
	var table = document.getElementById("tableQuery");
	var row = table.rows[3];
	switch( reporttype ) {
	case "weekly":
		row.cells[0].bgColor = "YELLOW";
		row.cells[1].bgColor = "WHITE";
		row.cells[2].bgColor = "WHITE";
		break;
	case "monthly":
		row.cells[0].bgColor = "WHITE";
		row.cells[1].bgColor = "YELLOW";
		row.cells[2].bgColor = "WHITE";
		break;
	case "yearly":
		row.cells[0].bgColor = "WHITE";
		row.cells[1].bgColor = "WHITE";
		row.cells[2].bgColor = "YELLOW";
		break;
	}

	var groupnum = document.getElementById("selectedgroup").value;
	var consnum = document.getElementById("selectedcons").value;
	var root = groupnum == 0 ? "site" : consnum == 0 ? "group" : "cons";

	if(specificYear!=null &&specificYear!=''){
		specificYear = document.getElementById(specificYear).value;
	}
	
	CommSend("servlet/ajrefresh", 
		"POST", 
		"cmd=loadreport&root="+root+"&reporttype="+reporttype+"&reportstep="+reportstep+"&specificYear="+specificYear+"&startparam="+startparam+
		"&group="+groupnum + "&cons=" + consnum,
		"energy_loadreport", true);
}

function Callback_energy_loadreport(xmlResponse) 
{
	document.getElementById("datafromgraph").value = xmlResponse.getElementsByTagName("fromparam")[0].childNodes[0].nodeValue;
	document.getElementById("datatograph").value = xmlResponse.getElementsByTagName("toparam")[0].childNodes[0].nodeValue;
	
	// Render data into cake tab
	renderCakeTable(xmlResponse);
	
	// Render cake graph
	renderCakeGraph(xmlResponse);
	
	// Render line graph
	renderLineGraph(xmlResponse);
	
	// Render bar graph
	renderBarGraph(xmlResponse);
	
	// Render kw table
	renderKTableGeneral(xmlResponse,"tablekw");
	
	// Render kwh table
	renderKTableGeneral(xmlResponse,"tablekwh");

	// new time slot related reports
	renderKTableTSPerc(xmlResponse, "table_ts_perc");
	renderKTableKWhTS(xmlResponse, "table_kwh_ts");
	renderKTableTSKWh(xmlResponse, "table_ts_kwh");
}

function deleteRows(tableGroups, limitRows) {
	if (tableGroups.rows.length > limitRows) {
		var tableRowsLength = tableGroups.rows.length;
		var tableRowsLengthTemp = tableRowsLength - 1;
		for ( var indexGroups = limitRows; indexGroups < tableRowsLength; indexGroups++) {
			tableGroups.deleteRow(tableRowsLengthTemp);
			tableRowsLengthTemp--;
		}
	}
}

function addRowsToTable(tableGroups, indexRow, numCells) {
	// rigenero la tabella
	tableGroups.insertRow(indexRow);
	tableGroups.rows[indexRow].className = "Row1";
	tableGroups.rows[indexRow].insertCell(0).className = "standardTxt";
	tableGroups.rows[indexRow].cells[0].align = "center";
	tableGroups.rows[indexRow].cells[0].style.height = "21px";

	numCells--;
	for ( var indexValue = 1; indexValue <= numCells; indexValue++) {
		tableGroups.rows[indexRow].insertCell(indexValue).className = "standardTxt";
		tableGroups.rows[indexRow].cells[indexValue].align = "center";
	}
	// popolo la tabella la tabella
}


function renderCakeTable(xmldata)
{
	var i=0;
	var j=0;
	var sName = "";
	var sPer = "";
	var sKw = "";
	var sKwh = "";
	var sKgCO2 = "";
	var sCost = "";
	
	var tableGroups = document.getElementById("tableGroups");
	var objList = xmldata.getElementsByTagName("group");
	deleteRows(tableGroups, 2);
	
	for(i=1; i<=objList.length; i++)
	{
		sName = (objList[i-1].getElementsByTagName("name")[0]).childNodes[0].nodeValue;
		sPer = (objList[i-1].getElementsByTagName("per")[0]).childNodes[0].nodeValue;
		sKw = (objList[i-1].getElementsByTagName("kw")[0]).childNodes[0].nodeValue;
		sKwh = (objList[i-1].getElementsByTagName("kwh")[0]).childNodes[0].nodeValue;
		sKgCO2 = (objList[i-1].getElementsByTagName("kgco2")[0]).childNodes[0].nodeValue;
		sCost = (objList[i-1].getElementsByTagName("cost")[0]).childNodes[0].nodeValue;
		
		tableGroups.insertRow(i+1);
		tableGroups.rows[i+1].className = "Row1";
		
		for(j=1; j<=6; j++) 
		{
			tableGroups.rows[i+1].insertCell(0).className = "standardTxt";
			tableGroups.rows[i+1].cells[0].style.height = "21px";
			switch(j)
			{
				case 6:
					tableGroups.rows[i+1].cells[0].align = "left";	
					tableGroups.rows[i+1].cells[0].innerHTML = sName;
					break;
				case 5:
					tableGroups.rows[i+1].cells[0].align = "center";	
					tableGroups.rows[i+1].cells[0].innerHTML = sPer;
					break;
				case 4:
					tableGroups.rows[i+1].cells[0].align = "center";	
					tableGroups.rows[i+1].cells[0].innerHTML = sKw;
					break;
				case 3:
					tableGroups.rows[i+1].cells[0].align = "center";	
					tableGroups.rows[i+1].cells[0].innerHTML = sKwh;
					break;
				case 2:
					tableGroups.rows[i+1].cells[0].align = "center";	
					tableGroups.rows[i+1].cells[0].innerHTML = sKgCO2;
					break;
				case 1:
					tableGroups.rows[i+1].cells[0].align = "center";	
					tableGroups.rows[i+1].cells[0].innerHTML = sCost;
					break;
			}
		}
	}

	//render other
	var objList_other = xmldata.getElementsByTagName("other");

	if(objList_other!=null && objList_other.length>0){
		sName = document.getElementById("egotherdesc").value;
		sPer = (objList_other[0].getElementsByTagName("per")[0]).childNodes[0].nodeValue;
		sKw = (objList_other[0].getElementsByTagName("kw")[0]).childNodes[0].nodeValue;
		sKwh = (objList_other[0].getElementsByTagName("kwh")[0]).childNodes[0].nodeValue;
		sKgCO2 = (objList_other[0].getElementsByTagName("kgco2")[0]).childNodes[0].nodeValue;
		sCost = (objList_other[0].getElementsByTagName("cost")[0]).childNodes[0].nodeValue;
		
		tableGroups.insertRow(tableGroups.rows.length);
		tableGroups.rows[tableGroups.rows.length-1].className = "Row1";
		
		var w = tableGroups.rows.length-1;
		for(j=1; j<=6; j++) 
		{
			tableGroups.rows[w].insertCell(0).className = "standardTxt";
			tableGroups.rows[w].cells[0].style.height = "21px";
			switch(j)
			{
				case 6:
					tableGroups.rows[w].cells[0].align = "left";	
					tableGroups.rows[w].cells[0].innerHTML = sName;
					break;
				case 5:
					tableGroups.rows[w].cells[0].align = "center";	
					tableGroups.rows[w].cells[0].innerHTML = sPer;
					break;
				case 4:
					tableGroups.rows[w].cells[0].align = "center";	
					tableGroups.rows[w].cells[0].innerHTML = sKw;
					break;
				case 3:
					tableGroups.rows[w].cells[0].align = "center";	
					tableGroups.rows[w].cells[0].innerHTML = sKwh;
					break;
				case 2:
					tableGroups.rows[i+1].cells[0].align = "center";	
					tableGroups.rows[i+1].cells[0].innerHTML = sKgCO2;
					break;
				case 1:
					tableGroups.rows[i+1].cells[0].align = "center";	
					tableGroups.rows[i+1].cells[0].innerHTML = sCost;
					break;
			}
		}
	}
}

function renderCakeGraph(xmldata)
{
	var globalNode = xmldata.getElementsByTagName("reportinfo");
	var idgraph = ((globalNode[0]).getElementsByTagName("piechart")[0]).childNodes[0].nodeValue;
	var dimwidth = document.getElementById("imgwidthcake").value;
	document.getElementById("imgCake").src = "SRVLCharts;jsessionid="+
	document.getElementById("sessionid").value+"?charttype=piechart&width="+dimwidth+"&height=300&imgid="+idgraph;
	
	changeHrefCakeGraph(idgraph,"n","hrfCakeZoom","piechart");
	changeHrefCakeGraph(idgraph,"y","hrfCakePrint","piechart");

	// time slot percentage
	idgraph = ((globalNode[0]).getElementsByTagName("piechart_ts")[0]).childNodes[0].nodeValue;
	document.getElementById("imgCakeB").src = "SRVLCharts;jsessionid="+
		document.getElementById("sessionid").value+"?charttype=piechart_timeslot_color&width="+dimwidth+"&height=300&imgid="+idgraph;
	changeHrefCakeGraph(idgraph,"n","hrfCakeZoomB","piechart_timeslot_color");
	changeHrefCakeGraph(idgraph,"y","hrfCakePrintB","piechart_timeslot_color");
}

function changeHrefCakeGraph(idgraph,sPrint,sRif,stype)
{
	var ww = getWidth(sPrint);
	var hh = getHeight(sPrint);
	//document.getElementById(sRif).href = "app/energy/pieChart.jsp;jsessionid="
	aHref[sRif] = "app/energy/pieChart.jsp;jsessionid="
		+ document.getElementById("sessionid").value
		+ "?print=" + sPrint + "&charttype=" + stype + "&width=" + ww + "&height=" + hh + "&imgid=" + idgraph;
}

function renderLineGraph(xmldata)
{
	var dtf = document.getElementById("datafromgraph").value;
	var dtt = document.getElementById("datatograph").value;
	
	var globalNode = xmldata.getElementsByTagName("reportinfo");
	var idgraph = ((globalNode[0]).getElementsByTagName("barchartkw")[0]).childNodes[0].nodeValue;
	var dimwidth = document.getElementById("imgwidthbar").value;
	document.getElementById("imgLine").src = "SRVLCharts;jsessionid="+
	document.getElementById("sessionid").value+"?dtf="+dtf+"&dtt="+dtt+"&charttype=line&width="+dimwidth+"&height=300&imgid="+idgraph;
	changeHrefGraph("lineZoom",idgraph,"n","line");
	changeHrefGraph("linePrint",idgraph,"y","line");
}

function renderBarGraph(xmldata)
{
	var dtf = document.getElementById("datafromgraph").value;
	var dtt = document.getElementById("datatograph").value;
	var globalNode = xmldata.getElementsByTagName("reportinfo");
	var idgraph = ((globalNode[0]).getElementsByTagName("barchartkwh")[0]).childNodes[0].nodeValue;
	var dimwidth = document.getElementById("imgwidthbar").value;
	document.getElementById("imgBar").src = "SRVLCharts;jsessionid="+
		document.getElementById("sessionid").value+"?dtf="+dtf+"&dtt="+dtt+"&charttype=barchart&width="+dimwidth+"&height=300&imgid="+idgraph;
	changeHrefGraph("barZoom",idgraph,"n","barchart");
	changeHrefGraph("barPrint",idgraph,"y","barchart");
	// kwh / time slot
	idgraph = ((globalNode[0]).getElementsByTagName("barchart_kwh_ts")[0]).childNodes[0].nodeValue;
	document.getElementById("imgBarC").src = "SRVLCharts;jsessionid="+
		document.getElementById("sessionid").value+"?dtf="+dtf+"&dtt="+dtt+"&charttype=barchart&width="+dimwidth+"&height=300&imgid="+idgraph;
	changeHrefGraph("barZoomC",idgraph,"n","barchart");
	changeHrefGraph("barPrintC",idgraph,"y","barchart");
	// time slot / kwh
	idgraph = ((globalNode[0]).getElementsByTagName("barchart_ts_kwh")[0]).childNodes[0].nodeValue;
	document.getElementById("imgBarD").src = "SRVLCharts;jsessionid="+
		document.getElementById("sessionid").value+"?dtf="+dtf+"&dtt="+dtt+"&charttype=barchart_timeslot_color&width="+dimwidth+"&height=300&imgid="+idgraph;
	changeHrefGraph("barZoomD",idgraph,"n","barchart_timeslot_color");
	changeHrefGraph("barPrintD",idgraph,"y","barchart_timeslot_color");
}

function changeHrefGraph(idHref,idgraph,strprint,stype)
{
	var ww = getWidth(strprint);
	var hh = getHeight(strprint);
	var dtf = document.getElementById("datafromgraph").value;
	var dtt = document.getElementById("datatograph").value;
	//document.getElementById(idHref).href = "app/energy/barChart.jsp;jsessionid="+
	aHref[idHref] = "app/energy/barChart.jsp;jsessionid="
		+ document.getElementById("sessionid").value
		+ "?print=" + strprint + "&dtf=" + dtf + "&dtt=" + dtt + "&charttype=" + stype + "&width=" + ww + "&height=" + hh +"&imgid=" + idgraph;
}

function getWidth(sPrint){
	if(sPrint=="y"){
		return 1500;
	} else if(sPrint=="n"){
		return 800;
	}
}

function getHeight(sPrint){
	if(sPrint=="y"){
		return 800;
	} else if(sPrint=="n"){
		return 430;
	}
}

function renderKTableGeneral(xmldata,sTableId)
{
	var i=0;
	var j=0;
	var xmlReportInfo = xmldata.getElementsByTagName("reportinfo")[0];
	var axmlStepNames = xmlReportInfo.getElementsByTagName("step");
	var objStepNames = null;
	if( axmlStepNames ) {
		objStepNames = new Object();
		for(var iStep = 0; iStep < axmlStepNames.length; iStep++)
			objStepNames[axmlStepNames[iStep].getAttribute("id")] = axmlStepNames[iStep].getAttribute("name");
	}
	var objList = xmldata.getElementsByTagName("group");
	var objStep = null;
	var tableGroups = document.getElementById(sTableId);
	var sValue = "";
	
	var tipoTipo= "kw";
	var lastCol = document.getElementById("avgmultilang").value;
	if(sTableId == "tablekwh")
	{
		tipoTipo= "kwh";
		lastCol = document.getElementById("totmultilang").value;
	}

	// Clear table
	deleteRows(tableGroups, 0);
	
	// Write header table
	if(objList[0] != null)
	{
		// Insert row
		tableGroups.insertRow(0);
		objStep = objList[0].getElementsByTagName("step");
		
		tableGroups.rows[0].insertCell(0);
		tableGroups.rows[0].cells[j].className = "th";
		tableGroups.rows[0].cells[j].style.height = "30px";
		tableGroups.rows[0].cells[j].innerHTML = "Group";
		
		for(j=0; j<objStep.length; j++)
		{
			tableGroups.rows[0].insertCell(j+1);
			tableGroups.rows[0].cells[j+1].className = "th";
			tableGroups.rows[0].cells[j+1].style.height = "30px";
			tableGroups.rows[0].cells[j+1].innerHTML = objStepNames
				? objStepNames[objStep[j].getAttribute("id")]
				: objStep[j].getAttribute("id");
		}
		
		// Insert TOT or AVG column
		tableGroups.rows[0].insertCell(j+1);
		tableGroups.rows[0].className = "th";
		tableGroups.rows[0].cells[j+1].style.height = "30px";
		tableGroups.rows[0].cells[j+1].innerHTML = lastCol;
	}
	
	
	// Write table data
	for(i=0; i<objList.length; i++)
	{
		tableGroups.insertRow(i+1);
		tableGroups.rows[i+1].className="Row1";
		
		objStep = objList[i].getElementsByTagName("step");
		
		tableGroups.rows[i+1].insertCell(0);
		tableGroups.rows[i+1].cells[0].className = "standardTxt";
		tableGroups.rows[i+1].cells[0].style.height = "21px";
		tableGroups.rows[i+1].cells[0].innerHTML = "<nobr>"+
			(objList[i].getElementsByTagName("name")[0]).childNodes[0].nodeValue+"</nobr>";
		
		for(j=0; j<objStep.length; j++)
		{
			tableGroups.rows[i+1].insertCell(j+1);
			
			tableGroups.rows[i+1].cells[j+1].className = "standardTxt";
			tableGroups.rows[i+1].cells[j+1].style.height = "21px";
			tableGroups.rows[i+1].cells[j+1].innerHTML ="<nobr>"+ 
				(objStep[j].getElementsByTagName(tipoTipo)[0]).childNodes[0].nodeValue+"</nobr>";
		}
		
		// Insert TOT or AVG data
		tableGroups.rows[i+1].insertCell(j+1);
		tableGroups.rows[i+1].cells[j+1].className = "standardTxt";
		tableGroups.rows[i+1].cells[j+1].style.height = "21px";
		tableGroups.rows[i+1].cells[j+1].innerHTML ="<nobr>"+ 
			(objList[i].getElementsByTagName(tipoTipo)[0]).childNodes[0].nodeValue+"</nobr>";
	}

	// Write "other" record data
	objList = xmldata.getElementsByTagName("other");

	if(objList!=null && objList.length>0){
		tableGroups.insertRow(tableGroups.rows.length);
		tableGroups.rows[tableGroups.rows.length-1].className="Row1";
		
		objStep = objList[0].getElementsByTagName("step");
		
		var w = tableGroups.rows.length-1;
		tableGroups.rows[w].insertCell(0);
		tableGroups.rows[w].cells[0].className = "standardTxt";
		tableGroups.rows[w].cells[0].style.height = "21px";
		tableGroups.rows[w].cells[0].innerHTML = "<nobr>"+
			document.getElementById("egotherdesc").value+"</nobr>";
		
		for(j=0; j<objStep.length; j++)
		{
			tableGroups.rows[w].insertCell(j+1);
			
			tableGroups.rows[w].cells[j+1].className = "standardTxt";
			tableGroups.rows[w].cells[j+1].style.height = "21px";
			tableGroups.rows[w].cells[j+1].innerHTML ="<nobr>"+ 
				(objStep[j].getElementsByTagName(tipoTipo)[0]).childNodes[0].nodeValue+"</nobr>";
		}
		
		// Insert TOT or AVG data
		tableGroups.rows[w].insertCell(j+1);
		tableGroups.rows[w].cells[j+1].className = "standardTxt";
		tableGroups.rows[w].cells[j+1].style.height = "21px";
		tableGroups.rows[w].cells[j+1].innerHTML ="<nobr>"+ 
			(objList[0].getElementsByTagName(tipoTipo)[0]).childNodes[0].nodeValue+"</nobr>";
	}
}


function renderKTableTSPerc(xmlData, sTableId)
{
	var table = document.getElementById(sTableId).tBodies[0];
	var xmlSite = xmlData.getElementsByTagName("site")[0];
	var xmlTimeSlots = xmlSite.getElementsByTagName("timeslots")[0];
	var axmlTimeSlots = xmlTimeSlots.getElementsByTagName("ts");
	for(var i = 0; i < axmlTimeSlots.length - 1; i++) {
		var iTS = parseInt(axmlTimeSlots[i].getAttribute("i"), 10);
		// it must be iTS + 1 but there is a dummy row before table header
		table.rows[iTS + 2].cells[1].innerHTML = axmlTimeSlots[i].getAttribute("kwh_p");
		table.rows[iTS + 2].cells[2].innerHTML = axmlTimeSlots[i].getAttribute("cost_p");
	}
}


function renderKTableKWhTS(xmlData, sTableId)
{
	var table = document.getElementById(sTableId).tBodies[0];
	var xmlSite = xmlData.getElementsByTagName("site")[0];
	var xmlTimeSlots = xmlSite.getElementsByTagName("timeslots")[0];
	var axmlTimeSlots = xmlTimeSlots.getElementsByTagName("ts");
	for(var i = 0; i < axmlTimeSlots.length; i++) {
		var iTS = parseInt(axmlTimeSlots[i].getAttribute("i"), 10);
		table.rows[1].cells[iTS + 1].innerHTML = axmlTimeSlots[i].getAttribute("kwh");
		table.rows[2].cells[iTS + 1].innerHTML = axmlTimeSlots[i].getAttribute("cost");
	}
}


function renderKTableTSKWh(xmlData, sTableId)
{
	var table = document.getElementById(sTableId);
	deleteRows(table, 0);
	var xmlReportInfo = xmlData.getElementsByTagName("reportinfo")[0];
	var axmlStepNames = xmlReportInfo.getElementsByTagName("step");
	var objStepNames = null;
	if( axmlStepNames ) {
		objStepNames = new Object();
		for(var iStep = 0; iStep < axmlStepNames.length; iStep++)
			objStepNames[axmlStepNames[iStep].getAttribute("id")] = axmlStepNames[iStep].getAttribute("name");
	}
	var xmlSite = xmlData.getElementsByTagName("site")[0];
	var xmlSiteTimeSlots = xmlSite.getElementsByTagName("timeslots")[0];
	var axmlSiteTimeSlots = xmlSiteTimeSlots.getElementsByTagName("ts");
	var axmlSteps = xmlSite.getElementsByTagName("step");
	var nSteps = axmlSteps.length;
	table.insertRow(0);
	table.rows[0].insertCell(0);
	table.rows[0].cells[0].className = "th";
	table.rows[0].cells[0].style.height = "30px";
	table.rows[0].cells[0].align = "center";
	table.rows[0].cells[0].innerHTML = document.getElementById("time_slot").value;
	for(var i = 0; i < nSteps; i++)	{
		table.rows[0].insertCell(i+1);
		table.rows[0].cells[i+1].className = "th";
		table.rows[0].cells[i+1].style.height = "30px";
		table.rows[0].cells[i+1].align = "center";
		table.rows[0].cells[i+1].innerHTML = objStepNames
			? objStepNames[axmlSteps[i].getAttribute("id")]
			: axmlSteps[i].getAttribute("id");
	}
	table.rows[0].insertCell(i+1);
	table.rows[0].className = "th";
	table.rows[0].cells[i+1].style.height = "30px";
	table.rows[0].cells[i+1].align = "center";
	table.rows[0].cells[i+1].innerHTML = document.getElementById("totmultilang").value;
	
	var nTimeSlots = parseInt(document.getElementById("TIMESLOT_NO").value, 10);
	for(var i = 0; i < nTimeSlots; i++) {
		table.insertRow(i+1);
		table.rows[i+1].insertCell(0);
		table.rows[i+1].cells[0].className = "standardTxt";
		table.rows[i+1].cells[0].style.height = "21px";
		table.rows[i+1].cells[0].innerHTML = "T" + (i+1);
		for(var j = 0; j < nSteps; j++)	{
			var xmlTimeSlots = axmlSteps[j].getElementsByTagName("timeslots")[0];
			var axmlTimeSlots = xmlTimeSlots.getElementsByTagName("ts");
			table.rows[i+1].insertCell(j+1);
			table.rows[i+1].cells[j+1].className = "standardTxt";
			table.rows[i+1].cells[j+1].style.height = "21px";
			table.rows[i+1].cells[j+1].align = "center";
			table.rows[i+1].cells[j+1].innerHTML = axmlTimeSlots[i].getAttribute("kwh");
		}
		table.rows[i+1].insertCell(j+1);
		table.rows[i+1].cells[j+1].className = "standardTxt";
		table.rows[i+1].cells[j+1].style.height = "21px";
		table.rows[i+1].cells[j+1].align = "center";
		table.rows[i+1].cells[j+1].innerHTML = axmlSiteTimeSlots[i].getAttribute("kwh");
	}	
}


function format2dec(val)
{
	var str = "" + val;
	if( str.indexOf(".") > 0 )
		str = val.toFixed(2);
	return str;
}


function energyselectgroups(idGroup){
	var selectedcons = document.getElementById("selectedcons");
	selectedcons.length = 0;
	var group = "group" + idGroup;
	for(var i = 0; i < objGroups[group].length; i++) {
		var option = document.createElement('option');
		option.value = objGroups[group][i][0];
		option.text = objGroups[group][i][1];
		selectedcons.add(option);
	}
}
