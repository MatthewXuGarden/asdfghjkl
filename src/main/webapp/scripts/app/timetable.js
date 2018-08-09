var rowSelected		= -1;
var CELL_INDEX		= 0;
var CELL_TIME		= 1;
var MAX_TIME_ROWS	= 6;


function timeAdd()
{
	var bodyTable = document.getElementById("timeTable").getElementsByTagName("table")[0];
	var rowsNumber = 0;
	// Alessandro: if bodyTable.rows is undefined it means timeTable empty
	if (bodyTable.rows) {
		if (bodyTable.rows.length >= MAX_TIME_ROWS) {
			alert(document.getElementById('maxelements').value + " " + MAX_TIME_ROWS);
			return;
		} else
			rowsNumber = bodyTable.rows.length;
	}
		
	var wid0 = document.getElementById('THtimeTable00').offsetWidth;
	var timeString = document.getElementById("timeTableHour").value + ":" +	document.getElementById("timeTableMinute").value;
	var htmlRow = "<tr style='cursor:pointer' class='Row1' onclick='timeSelect(this)' ondblclick='timeDelete(this)'>";
	htmlRow += "<td class='tdmini' style='width:" + wid0 + "px'>&nbsp;</td>";
	htmlRow += "<td class='tdmini' style='width: auto'>" + timeString + "</td></tr>";
	var stopAdd = false;
	var timeVal = timeString2Val(timeString);
	var htmlTable = "<table class='table' style='table-layout:fixed;' width='100%'><tbody>";	
	// Alessandro: write all the already defined times adding the new one
	for(var i = 0; i < rowsNumber; i++) {
		var tableVal = timeString2Val(bodyTable.rows[i].cells[CELL_TIME].firstChild.nodeValue);
		if( timeVal == tableVal ) {
			break;
		}
		else if( timeVal < tableVal && !stopAdd) {
			htmlTable += htmlRow;
			stopAdd = true;
		}

		htmlTable += "<tr style='cursor:pointer' class='Row1' onclick='timeSelect(this);' ondblclick='timeDelete(this)'>";
		htmlTable += bodyTable.rows[i].innerHTML;
		htmlTable += "</tr>";
	}
	
	if( !stopAdd == true )
		htmlTable += htmlRow; 
	htmlTable += "</tbody></table>";
	
	document.getElementById("timeTable").innerHTML = htmlTable;
	timeIndex();
}


function timeDelete(obj)
{
	if( rowSelected != -1 ) {
		var bodyTable = document.getElementById("timeTable").getElementsByTagName("table")[0];
		bodyTable.deleteRow(rowSelected);	
		rowSelected = -1;
		if( bodyTable.rows.length <= 0 )
			document.getElementById("timeTable").innerHTML = "<table class='table' style='table-layout:fixed;' width='100%'></table>";
		else
			timeIndex();
	}
}


function timeSelect(obj)
{
	var bodyTable = document.getElementById("timeTable").getElementsByTagName("table")[0];
	if( obj.className=="Row1" ) {
		for(var i = 0; i < bodyTable.rows.length; i++) {
			if( bodyTable.rows[i]==obj ) {
				if( rowSelected != -1 )
					bodyTable.rows[rowSelected].className="Row1";
				rowSelected = i;
				break;
			}
		}
		obj.className = "selectedRow";
	}
	else{
		obj.className = "Row1";
		rowSelected = -1;
 	}
}


function timeTableLoad(timeTableValue)
{
	var wid0 = document.getElementById('THtimeTable00').width;	
	var htmlTable = "<table class='table' width='100%'><tbody>";
	if( timeTableValue != null && timeTableValue.length > 0 ) {
		var timeValues = timeTableValue.split(";");
		var rowStyle = '';
		for(var i = 0; i < timeValues.length; i++) {
			rowStyle = (i % 2 == 0) ? 'Row1' : 'Row2';
			htmlTable += "<tr style='cursor:pointer' class='" + rowStyle + "' onclick='timeSelect(this);' ondblclick='timeDelete(this)'>";
			htmlTable += "<td class='tdmini' style='width:" + wid0 + "'>";
			htmlTable += (i+1).toString();
			htmlTable += "</td><td class='tdmini' style='width: auto'>";
			htmlTable += timeVal2String(timeValues[i]);
			htmlTable += "</td></tr>";
		}
	}
	htmlTable += "</tbody></table>";
	document.getElementById("timeTable").innerHTML = htmlTable;
}


function isTime(timeString)
{
	var timeVal = timeString2Val(timeString);
	var table = document.getElementById("timeTable").getElementsByTagName("table")[0];
	for(var i = 0; i < table.rows.length; i++) {
		if( timeString2Val(table.rows[i].cells[CELL_TIME].firstChild.nodeValue) == timeVal )
			return true;
	}
	return false;
}


function timeIndex() {
	var bodyTable = document.getElementById("timeTable").getElementsByTagName("table")[0];
	for(var i = 0; i < bodyTable.rows.length; i++) {
		bodyTable.rows[i].cells[CELL_INDEX].firstChild.nodeValue = i+1;
	}	
}


function timeString2Val(timeString)
{
	var timeHHMM = timeString.split(":");
	var timeVal = parseInt(timeHHMM[0], 10) * 60 * 60 + parseInt(timeHHMM[1], 10) * 60;
	return timeVal;
}


function timeVal2String(timeValue)
{
	var timeVal = parseInt(timeValue, 10);
	var hours = parseInt(timeVal / (60 * 60), 10);
	var minutes = parseInt(timeVal % (60 * 60) / 60, 10);
	var hh = hours.toString();
	if( hh.length <= 1 )
		hh = "0" + hh;
	var mm = minutes.toString();
	if( mm.length <= 1 )
		mm = "0" + mm;
	var timeString = hh + ":" + mm;
	return timeString;
}


function timeTableValue()
{
	var timeTableValue = "";
	var bodyTable = document.getElementById("timeTable").children[0];
	for(var i = 0; i < bodyTable.rows.length; i++) {
		if( i > 0 )
			timeTableValue += ";";
		timeTableValue += timeString2Val(bodyTable.rows[i].cells[CELL_TIME].firstChild.nodeValue);
	}
	return timeTableValue;
}
