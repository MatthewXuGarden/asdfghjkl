var ACTION_SAVE			= 1;
var ACTION_IMPORT		= 2;
var ACTION_EXPORT		= 3;

var iMenuSelection		= 0;
var selLayer			= 'time_slot';
var iTimeTable			= 0;
var iTimeTableCopy		= -1;

var TIMEPERIOD_NO		= 0;
var TIMESLOT_NO			= 0;
var TIME_SLOTS_ROW_BASE	= 1;
var TIME_SLOTS_COL_BASE	= 1;

var COPY_ON				= "images/energy/copy_normal.png";
var COPY_OFF			= "images/energy/copy_disabled.png";
var PASTE_ON			= "images/energy/paste_normal.png";
var PASTE_OFF			= "images/energy/paste_disabled.png";

var listExDays			= null;
var COL_EXDAYS_I1		= 1;
var COL_EXDAYS_I2		= 2;
var COL_EXDAYS_DAY		= 3;
var COL_EXDAYS_STRMONTH	= 4;
var COL_EXDAYS_MONTH	= 5;
var iExDaySelection		= -1;

var ext					= "etsc";
var xml_export_callback	= null;

var anMonthCalendar		= new Array(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);

var bTimeslotCfg		= false;


function onActiveConfiguration(value)
{
	var bPrevCfg = bTimeslotCfg;
	bTimeslotCfg = value == "time_slot";
	// cost
	document.getElementById("valCost").disabled = bTimeslotCfg;
	document.getElementById("tabActiveCfg").rows[2].cells[0].style.visibility = bTimeslotCfg ? "hidden" : "visible";
	document.getElementById("tabActiveCfg").rows[2].cells[1].style.visibility = bTimeslotCfg ? "hidden" : "visible";
	//document.getElementById("valValue").disabled = disabled;
	//document.getElementById("valkgco2").disabled = disabled;
	// timeslot configuration
	document.getElementById("divTimeslotCfg").style.display = bTimeslotCfg ? "block" : "none";
	if( bTimeslotCfg != bPrevCfg )
		alert(document.getElementById(bTimeslotCfg ? "fc2ts" : "ts2fc").value);
}


function saveXML()
{
	var strXML = "<?xml version=\"1.0\"?>\n";
	// energy profile
	strXML += "<EnergyProfile>\n";
	
	// time slots
	strXML += "\t<TimeSlots>\n";
	for(var iTimeSlot = 0; iTimeSlot < TIMESLOT_NO; iTimeSlot++) {
		var cost = document.getElementById("ts" + iTimeSlot + "cost").value;
		if( isNaN(parseFloat(cost)) ) {
			var alertCost = document.getElementById("alert_cost").value;
			var tableTimeSlots = document.getElementById("tableTimeSlotDefs").tBodies[0];
			alert(alertCost + tableTimeSlots.rows[iTimeSlot + 1].cells[0].innerHTML);
			return;
		}
		strXML += "\t\t<TimeSlot i=\"" + iTimeSlot + "\" cost=\""
			+ cost + "\" />\n";
	}
	strXML += "\t</TimeSlots>\n";

	// time periods
	strXML += "\t<TimePeriods>\n";
	for(var iTimePeriod = 0; iTimePeriod < TIMEPERIOD_NO + 1; iTimePeriod++) {
		strXML += "\t\t<TimePeriod i=\"" + (iTimePeriod < TIMEPERIOD_NO ? iTimePeriod : "ex") + "\"";
		var obj = aTimePeriods[iTimePeriod];
		if( iTimePeriod < TIMEPERIOD_NO ) {
			if( parseInt(obj.monthBegin, 10) > parseInt(obj.monthEnd, 10) ||
				(parseInt(obj.monthBegin, 10) == parseInt(obj.monthEnd, 10) && parseInt(obj.dayBegin, 10) >= parseInt(obj.dayEnd, 10)) ) {
				var alertPeriod = document.getElementById("alert_period").value;
				var tableMenu = document.getElementById("tableMenu").tBodies[0];
				alert(alertPeriod + tableMenu.rows[0].cells[1 + iTimePeriod].innerHTML);
				return;
			}	
			strXML += " monthBegin=\"" + obj.monthBegin + "\"";
			strXML += " dayBegin=\"" + obj.dayBegin + "\"";
			strXML += " monthEnd=\"" + obj.monthEnd + "\"";
			strXML += " dayEnd=\"" + obj.dayEnd + "\"";
		}
		strXML += ">\n";
		for(var iRow = 0; iRow < obj.aaiTimeSlots.length; iRow++) {
			strXML += "\t\t\t<Day i=\"" + iRow + "\"";			
			for(var iCol = 0; iCol < obj.aaiTimeSlots[iRow].length; iCol++)
				strXML += " h" + iCol + "=\"" + obj.aaiTimeSlots[iRow][iCol] + "\"";
			strXML += "/>\n";
		}
		strXML += "\t\t</TimePeriod>\n";	
	}
	strXML += "\t</TimePeriods>\n";
	
	// exception days
	strXML += "\t<ExceptionDays>\n";
	for(var i = 0; i < listExDays.mData.length; i++) {
		strXML += "\t\t<ExceptionDay month=\"" + listExDays.mData[i][COL_EXDAYS_MONTH]
			+ "\" day=\"" +	listExDays.mData[i][COL_EXDAYS_DAY] + "\" />\n";
	}
	strXML += "\t</ExceptionDays>\n";
	
	strXML += "</EnergyProfile>\n";
	
	document.getElementById("cmd").value = "saveXML";
	document.getElementById("xml").value = strXML;
	document.getElementById("selLayer").value = selLayer;
	document.getElementById("iTimeTable").value = iTimeTable;
	var form = document.getElementById("frm_energy_impexp");
	if( form != null ) {
		MTstartServerComm();
		form.submit();
	}
}


function importXML(fdLocal, path, fileName)
{
	if( fdLocal ) {
		//if( confirm(document.getElementById("warning").value) ) {
			document.getElementById("cmd").value = "importXML";
			var form = document.getElementById("frm_energy_impexp");
			if( form != null ) {
				MTstartServerComm();
				form.submit();
			}
		//}
	}
	else {
		var win = document.getElementById("uploadwin");
		document.getElementById("uploadwin").style.display = "block";
		win.style.left = (document.body.clientWidth - win.clientWidth) / 2;
		win.style.top = (document.body.clientHeight - win.clientHeight) / 2;
	}
}


function submit_file()
{
	//if( confirm(document.getElementById("warning").value) ) {
		var form = document.getElementById("frm_upload_xml");
		if( form != null ) {
			MTstartServerComm();
			form.submit();
		}
	//}
}


function exportXML(fdLocal, path, fileName)
{
	if( fdLocal ) {	
		document.getElementById("cmd").value = "exportXML";
		if( !strEndsWith(path, "." + ext) )
			document.getElementById("impexp").value = path + "." + ext;
		var form = document.getElementById("frm_energy_impexp");
		if( form != null ) {
			MTstartServerComm();
			form.submit();
		}
	}
	else {
		var date = new Date();
		fileName = "energy_ts_" + date.format("yyyyMMddhhmmss") + "." + ext;
		xml_export_callback = fileName;
		path = document.getElementById("remoteFolder").value + fileName;
		new AjaxRequest("servlet/master", "POST", "cmd=exportXML&impexp="+path, Callback_exportXML, true);
	}
}


function init_fd_file()
{
	var date = new Date();
	fdSetFile("energy_ts_" + date.format("yyyyMMddhhmmss"));
}


function Callback_exportXML()
{
	MTstopServerComm();
	var path = document.getElementById("remoteFolder").value + xml_export_callback;
	window.open(top.frames["manager"].getDocumentBase() + "app/report/popup.html?" + path);
}


function onLoadProfile()
{
	bTimeslotCfg = document.getElementById("active_cfg").value == "time_slot"; 
	TIMEPERIOD_NO = parseInt(document.getElementById("TIMEPERIOD_NO").value, 10);	
	TIMESLOT_NO = parseInt(document.getElementById("TIMESLOT_NO").value, 10);
	listExDays = Lsw1;
	enableAction(ACTION_SAVE);
	if( bTimeslotCfg ) {
		enableAction(ACTION_IMPORT);
		enableAction(ACTION_EXPORT);
	}
	// alert
	var strAlert = document.getElementById("alert").value;
	if( strAlert.length > 0 )
		alert(strAlert);
	// subtab selection
	var selLayerPrev = document.getElementById("selLayer").value;
	var iTimeTablePrev = parseInt(document.getElementById("iTimeTable").value, 10);
	if( selLayerPrev )
		onSelectLayer(selLayerPrev, iTimeTablePrev);
}


function onSelectLayer(layer, i)
{
	var iSelection;
	switch( layer ) {
	case 'time_slot':		iSelection = 0;		break;
	case 'exception_days':	iSelection = 14;	break;
	default:				iSelection = i + 1;	break;
	}
	
	if( iSelection != iMenuSelection ) {
		var tableMenu = document.getElementById("tableMenu").tBodies[0];
		tableMenu.rows[0].cells[iMenuSelection].className = 'egtabnotselected2';
		tableMenu.rows[0].cells[iSelection].className = 'egtabselected2';
		iMenuSelection = iSelection;
		
		if( layer != selLayer ) {
			showLayer(selLayer, false).display = "none";
			showLayer(layer, true).display = "block";
			selLayer = layer;
		}
		
		if( layer == 'time_table' ) {
			if( i != 12 )
				showLayer('time_period', true).display = "block";
			else
				showLayer('time_period', false).display = "none";
			updateTimeTable(i);
		}
	}
}


function onDayBeginChanged(value)
{
	var obj = aTimePeriods[iTimeTable];
	obj.dayBegin = value;
}


function onMonthBeginChanged(value)
{
	var obj = aTimePeriods[iTimeTable];
	obj.monthBegin = value;
	var sel = document.getElementById("dayBegin");
	sel.length = 0;
	for(var i = 1; i <= anMonthCalendar[value]; i++) {
		var option = document.createElement('option');
		option.value = i;
		option.text = ("" + i).length == 1 ? "0" + i : i;
		sel.add(option);
	}
}


function onDayEndChanged(value)
{
	var obj = aTimePeriods[iTimeTable];
	obj.dayEnd = value;
}


function onMonthEndChanged(value)
{
	var obj = aTimePeriods[iTimeTable];
	obj.monthEnd = value;
	var sel = document.getElementById("dayEnd");
	sel.length = 0;
	for(var i = 1; i <= anMonthCalendar[value]; i++) {
		var option = document.createElement('option');
		option.value = i;
		option.text = ("" + i).length == 1 ? "0" + i : i;
		sel.add(option);
	}
}


function updateTimeTable(i)
{
	iTimeTable = i;
	var obj = aTimePeriods[iTimeTable];
	var	sel = document.getElementById("monthBegin");
	sel.value = obj.monthBegin;
	onMonthBeginChanged(obj.monthBegin);
	sel = document.getElementById("dayBegin");
	sel.value = obj.dayBegin;
	sel = document.getElementById("monthEnd");
	sel.value = obj.monthEnd;
	onMonthEndChanged(obj.monthEnd);
	sel = document.getElementById("dayEnd");
	sel.value = obj.dayEnd;
	var tableTimeSlots = document.getElementById("tableTimeSlots").tBodies[0];
	for(var iRow = 0; iRow < obj.aaiTimeSlots.length; iRow++) {
		for(var iCol = 0; iCol < obj.aaiTimeSlots[iRow].length; iCol++) {
			var iSlot = obj.aaiTimeSlots[iRow][iCol];
			var color = aColors[iSlot];
			tableTimeSlots.rows[TIME_SLOTS_ROW_BASE + iRow].cells[TIME_SLOTS_COL_BASE + iCol].bgColor = color;
			document.getElementById("slot_" + iRow + "_" + iCol).value = iSlot;
			// reset cmd slot
			document.getElementById("slot_col_" + iCol).value = -1;
		}
		document.getElementById("slot_row_" + iRow).value = -1;
	}
	document.getElementById("slot_table").value = -1;
}


function onSlotChanged(iRow, iCol, iSlot)
{
	aTimePeriods[iTimeTable].aaiTimeSlots[iRow][iCol] = iSlot;
	var color = aColors[iSlot];
	var tableTimeSlots = document.getElementById("tableTimeSlots").tBodies[0];
	tableTimeSlots.rows[TIME_SLOTS_ROW_BASE + iRow].cells[TIME_SLOTS_COL_BASE + iCol].bgColor = color;
	// reset cmd slots
	document.getElementById("slot_row_" + iRow).value = -1;
	document.getElementById("slot_col_" + iCol).value = -1;
	document.getElementById("slot_table").value = -1;
}


function onSlotRowChanged(iRow, iSlot)
{
	if( iSlot < 0 )
		return;

	var obj = aTimePeriods[iTimeTable];
	var tableTimeSlots = document.getElementById("tableTimeSlots").tBodies[0];
	var color = aColors[iSlot];
	for(var iCol = 0; iCol < obj.aaiTimeSlots[iRow].length; iCol++) {
		obj.aaiTimeSlots[iRow][iCol] = iSlot;
		tableTimeSlots.rows[TIME_SLOTS_ROW_BASE + iRow].cells[TIME_SLOTS_COL_BASE + iCol].bgColor = color;
		document.getElementById("slot_" + iRow + "_" + iCol).value = iSlot;
		// reset cmd slot
		document.getElementById("slot_col_" + iCol).value = -1;
	}
	document.getElementById("slot_table").value = -1;
}


function onSlotColChanged(iCol, iSlot)
{
	if( iSlot < 0 )
		return;

	var obj = aTimePeriods[iTimeTable];
	var tableTimeSlots = document.getElementById("tableTimeSlots").tBodies[0];
	var color = aColors[iSlot];
	for(var iRow = 0; iRow < obj.aaiTimeSlots.length; iRow++) {
		obj.aaiTimeSlots[iRow][iCol] = iSlot;
		tableTimeSlots.rows[TIME_SLOTS_ROW_BASE + iRow].cells[TIME_SLOTS_COL_BASE + iCol].bgColor = color;
		document.getElementById("slot_" + iRow + "_" + iCol).value = iSlot;
		// reset cmd slot
		document.getElementById("slot_row_" + iRow).value = -1;
	}
	document.getElementById("slot_table").value = -1;
}


function onSlotTableChanged(iSlot)
{
	if( iSlot < 0 )
		return;
	
	var obj = aTimePeriods[iTimeTable];
	var tableTimeSlots = document.getElementById("tableTimeSlots").tBodies[0];
	var color = aColors[iSlot];
	for(var iRow = 0; iRow < obj.aaiTimeSlots.length; iRow++) {
		for(var iCol = 0; iCol < obj.aaiTimeSlots[iRow].length; iCol++) {
			obj.aaiTimeSlots[iRow][iCol] = iSlot;
			tableTimeSlots.rows[TIME_SLOTS_ROW_BASE + iRow].cells[TIME_SLOTS_COL_BASE + iCol].bgColor = color;
			document.getElementById("slot_" + iRow + "_" + iCol).value = iSlot;
			// reset cmd slot
			document.getElementById("slot_col_" + iCol).value = -1;
		}
		document.getElementById("slot_row_" + iRow).value = -1;
	}
}


function onSlotTableCopy()
{
	iTimeTableCopy = iTimeTable;
	var img = document["btnPaste"];
	if( img != null )
		img.src = iTimeTableCopy < 0 ? PASTE_OFF : PASTE_ON;
}


function onSlotTablePaste()
{
	if( iTimeTableCopy < 0 || iTimeTableCopy == iTimeTable )
		return;
	
	var src = aTimePeriods[iTimeTableCopy];
	var obj = aTimePeriods[iTimeTable];
	for(var iRow = 0; iRow < obj.aaiTimeSlots.length; iRow++)
		for(var iCol = 0; iCol < obj.aaiTimeSlots[iRow].length; iCol++)
			obj.aaiTimeSlots[iRow][iCol] = src.aaiTimeSlots[iRow][iCol];
	updateTimeTable(iTimeTable);
}


function onSelectExDay(i)
{
	iExDaySelection = i;
}


function onAddExDay()
{
	var nDay = parseInt(document.getElementById("tester_day").value, 10);
	var month = document.getElementById("tester_month");
	var nMonth = parseInt(month.value, 10);
	var strMonth = month.options[month.selectedIndex].text;
	if( isExDay(nMonth, nDay) )
		return;
	var bAdd = true;
	var newExDay = new Array("", i, i, nDay, strMonth, nMonth);
	for(var i = 0; bAdd && i < listExDays.mData.length; i++) {
		if( nMonth < parseInt(listExDays.mData[i][COL_EXDAYS_MONTH], 10)
			|| (nMonth == parseInt(listExDays.mData[i][COL_EXDAYS_MONTH], 10)
				&& nDay < parseInt(listExDays.mData[i][COL_EXDAYS_DAY], 10)) ) {
			listExDays.mData.splice(i, 0, newExDay);
			bAdd = false;
		}
	}
	if( bAdd ) {
		var i = listExDays.mData.length;
		listExDays.mData[i] = newExDay;
	}
	iExDaySelection = -1;
	listExDays.rowSelected = null;
	listExDays.numRows = listExDays.mData.length;
	listExDays.render();
}


function onDeleteExDay()
{
	if( iExDaySelection < 0 )
		return;
	
	listExDays.mData.splice(iExDaySelection, 1);
	// update items index
	for(var i = 0; i < listExDays.mData.length; i++) {
		listExDays.mData[i][COL_EXDAYS_I1] = i;
		listExDays.mData[i][COL_EXDAYS_I2] = i;
	}
	iExDaySelection = -1;
	listExDays.rowSelected = null;
	listExDays.numRows = listExDays.mData.length;
	listExDays.render();
}


function isExDay(nMonth, nDay)
{
	for(var i = 0; i < listExDays.mData.length; i++)
		if( nMonth == listExDays.mData[i][COL_EXDAYS_MONTH]
			&& nDay == listExDays.mData[i][COL_EXDAYS_DAY] )
			return true;
	return false;
}
