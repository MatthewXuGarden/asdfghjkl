var last_sel = -1;
var row_class = -1;
var CELL_HACCP = 3;
var CELL_HIST = 4;
var CELL_PROF = 5;
var CELL_DELTA = 6;
var CELL_FREQ = 7;
var logCheckedBefore = true;
var logChecked = false;



function mod_hist(line)
{
	if( line == last_sel )
		return;
	var n_line = line; 
	//al click disabilito una eventuale seconda selezione
	hide_set(last_sel);
	
	// Alessandro : check the state for the virtual keyboard
	var vk_state = document.getElementById("vk_state").value;
	var table = document.getElementById("TableData1");
	var idvar = document.getElementById("val"+line).value;
	var type = document.getElementById("type"+idvar).value;
	var decimals = document.getElementById("dec"+idvar).value;
	
	//haccp 
	var haccp = document.getElementById("haccp"+idvar).value;
	var checked = "";
	if (haccp=="true") checked = "checked";
	table.rows[n_line].cells[CELL_HACCP].innerHTML="<input id='set_haccp' "+checked+" type='checkbox' />"
	
	//hist 
	var hist = document.getElementById("hist"+idvar).value;
	var checked = "";
	if (hist=="true") checked = "checked";
	table.rows[n_line].cells[CELL_HIST].innerHTML="<input id='set_hist' "+checked+" type='checkbox' />"
	
	if(haccp == "true" || hist == "true")
		logCheckedBefore = true;
	else
		logCheckedBefore = false;
	//prof hist 
	var prof = document.getElementById("prof"+idvar).value;
	table.rows[n_line].cells[CELL_PROF].innerHTML=getComboProf(prof);
	
	var cssVirtualKeyboardClass = (vk_state == '1') ? 'keyboardInput,' : '' ;
	//delta
	var delta = document.getElementById("delta"+idvar).value;
	if (type!=1)
	{
		if (decimals>0)  // modifica per gestire il vecchio tipo=23 (intera sul pco ma analogica a supervisione)
		{
			table.rows[n_line].cells[CELL_DELTA].innerHTML="<input id='set_delta' style='width:50px' type='textarea' size='5' maxlength='5' class='"+cssVirtualKeyboardClass+"'standardTxt' value='"+delta+"' onkeydown='checkOnlyAnalog(this,event);'/>";
		}
		else
		{
			table.rows[n_line].cells[CELL_DELTA].innerHTML="<input id='set_delta' style='width:50px' type='textarea' size='5' maxlength='5' class='"+cssVirtualKeyboardClass+"'standardTxt' value='"+delta+"' onkeydown='checkOnlyNumber(this,event);'/>";			
		}
	}
	
	//freq
	var freq = document.getElementById("freq"+idvar).value;
	table.rows[n_line].cells[CELL_FREQ].innerHTML=getComboFreq(freq);
	
	last_sel = line;
}


function hide_set(line)
{
	var line_to_set = last_sel;
	var can_hist = true;
	if (line==last_sel)
	{
		select_row(line);
		return;
	}
	if (last_sel!=-1&&document.getElementById("set_haccp")!=null)
	{
		can_hist = canSaveHist();
		if (can_hist)
		{
			var table = document.getElementById("TableData1");
			var idvar = document.getElementById("val"+last_sel).value;
			
			//haccp
			var haccp = document.getElementById("set_haccp").checked;
			if (haccp==true)
			{
				document.getElementById("haccp"+idvar).value="true";
				table.rows[line_to_set].cells[CELL_HACCP].innerHTML="X";
			}
			else
			{
				document.getElementById("haccp"+idvar).value="false";
				table.rows[line_to_set].cells[CELL_HACCP].innerHTML="";
			}
			
			//hist
			var hist = document.getElementById("set_hist").checked;
			if (hist==true)
			{
				document.getElementById("hist"+idvar).value="true";
				table.rows[line_to_set].cells[CELL_HIST].innerHTML="X";
			}
			else
			{
				document.getElementById("hist"+idvar).value="false";
				table.rows[line_to_set].cells[CELL_HIST].innerHTML="";
			}
			
			//prof
			if (hist)
			{
				var prof = document.getElementById("set_prof").value;
				document.getElementById("prof"+idvar).value = prof;
				table.rows[line_to_set].cells[CELL_PROF].innerHTML=document.getElementById("set_prof").options[document.getElementById("set_prof").selectedIndex].innerHTML;
			}
			else table.rows[line_to_set].cells[CELL_PROF].innerHTML="";
			
			//delta
			if (hist)
			{
				if (document.getElementById("set_delta")!=null)  //se ? una variabile digitale non c'? il delta
				{
					var delta = document.getElementById("set_delta").value;
					document.getElementById("delta"+idvar).value=delta;
					table.rows[line_to_set].cells[CELL_DELTA].innerHTML=delta;
				}
			}
			else table.rows[line_to_set].cells[CELL_DELTA].innerHTML="";
			
			//freq
			if (hist)
			{
				var freq = document.getElementById("set_freq").value;
				document.getElementById("freq"+idvar).value = freq;
				table.rows[line_to_set].cells[CELL_FREQ].innerHTML=document.getElementById("set_freq").options[document.getElementById("set_freq").selectedIndex].innerHTML;
			}
			else table.rows[line_to_set].cells[CELL_FREQ].innerHTML="";
			// MODIFICA LUCA 15/10/2007
			last_sel=-1;
			// FINE MODIFICA
		}
		else
		{
			return false;
		}
	}
	select_row(line,can_hist);
	return true;
}

function select_row(row,boolean)
{
	if (boolean)
	{
		var n_row = row;
		var table = document.getElementById("TableData1");
		if (row_class>=0)
		{
			table.rows[row_class].className = row_class % 2 == 0 ? "Row1" : "Row2";
		}
		row_class = n_row;
		if (row_class>=0)
		{
			table.rows[row_class].className="selectedRow";
		}
	}
}


// depth
var week1 = 604800;
var month1 = 2592000;
var month2 = 5184000;
var month6 = 15552000;
var month12 = 31536000;
var month18 = 46656000;
var month24 = 62208000;
function getComboProf(seconds)
{
	var html = "<select id='set_prof' class='standardTxt'>";
	html+="<option value='" + week1 + "' " + (seconds==week1?"selected":"")+">"+document.getElementById("s_week").value+"</option>";
	html+="<option value='" + month1 + "'  " + (seconds==month1?"selected":"")+">"+document.getElementById("s_month").value+"</option>";
	html+="<option value='" + month2 + "'  " + (seconds==month2?"selected":"")+">"+document.getElementById("s_month2").value+"</option>";
	html+="<option value='" + month6 + "' " + (seconds==month6?"selected":"")+">"+document.getElementById("s_month6").value+"</option>";
	html+="<option value='" + month12 + "' " + (seconds==month12?"selected":"")+">"+document.getElementById("s_year").value+"</option>";
	html+="<option value='" + month18 + "' " + (seconds==month18?"selected":"")+">"+document.getElementById("s_year15").value+"</option>";
	html+="<option value='" + month24 + "' " + (seconds==month24?"selected":"")+">"+document.getElementById("s_year2").value+"</option>";
	html+="</select>";
	return html;
}


// frequency
var s5 = 5;
var s10 = 10;
var s15 = 15;
var s30 = 30;
var m1 = 60;
var m2 = 120;
var m5 = 300;
var m15 = 900;
var m30 = 1800;
var h1 = 3600;
function getComboFreq(seconds)
{
	var html = "<select id='set_freq' class='standardTxt'>";
	html+="<option value='" + s5 + "' " +(seconds==s5?"selected":"")+">5s</option>";
	html+="<option value='" + s10 + "' " +(seconds==s10?"selected":"")+">10s</option>";
	html+="<option value='" + s15 +"' " +(seconds==s15?"selected":"")+">15s</option>";
	html+="<option value='" + s30 + "' " +(seconds==s30?"selected":"")+">30s</option>";
	html+="<option value='" + m1 + "' " +(seconds==m1?"selected":"")+">1m</option>";
	html+="<option value='" + m2 + "' " +(seconds==m2?"selected":"")+">2m</option>";
	html+="<option value='" + m5 + "' " +(seconds==m5?"selected":"")+">5m</option>";
	html+="<option value='" + m15 + "' " +(seconds==m15?"selected":"")+">15m</option>";
	html+="<option value='" + m30 + "' " +(seconds==m30?"selected":"")+">30m</option>";
	html+="<option value='" + h1 + "' " +(seconds==h1?"selected":"")+">1h</option>";
	html+="</select>";
	return html;
}


// based on FREQ-DEPTH PVPRO variables.xls
function canSaveHist()
{
	if (document.getElementById("set_hist").checked)
	{
		prof = Number(document.getElementById("set_prof").value);
		freq = Number(document.getElementById("set_freq").value);

		if( (prof <= week1)
			|| (prof <= month2 && freq >= s30 )
			|| (prof <= month6 && freq >= m1 )
			|| (prof <= month18 && freq >= m5 )
			|| (prof <= month24 && freq >= m30) ) {
			return true;			
		}
		else {
			alert(document.getElementById("configerror").value);
			return false;
		}
	}
	return true;
}

function needShowConfirm()
{
	if(last_sel != -1 && logCheckedBefore == false)
	{
		var haccp = document.getElementById("set_haccp").checked;
		var hist = document.getElementById("set_hist").checked;
		if(haccp == false && hist == false)
			return true;
		else
			return false;
	}
	else
		return false;
}