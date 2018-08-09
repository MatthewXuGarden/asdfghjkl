function onload_tab5()
{
	/*
	var max_dates = Number(document.getElementById("max_id").value);
	
	if (max_dates > 0)
		enableAction(1);
	else
		disableAction(1);
	*/
	
	enableAction(1); //add
	disableAction(2); //save
}

function save_tab5()
{
	var ok_save4a = true;
	
	var max_dates = 999999; //infinite
	if(document.getElementById("max_id")!=null) {
		max_dates = document.getElementById("max_id").value;
	}
	
	//se non completa, non considero l'ultima riga:
	if ((document.getElementById("month_"+max_dates).value == "-1") || (document.getElementById("day_"+max_dates).value == "-1"))
	{
		max_dates = max_dates - 1;
	}
	
	if (max_dates < 0)
		ok_save4a = false;
	else
	{
		for (i = 0; i <= max_dates; i++)
		{
			if (!checkDate(i))
				ok_save4a = false;
		}
	}

	if (ok_save4a)
	{	
		var on1 = 0;
		var off1 = 0;
		var on2 = 0;
		var off2 = 0;
		
		//per ogni data inserita:
		for (i = 0; i <= max_dates; i++)
		{
			if (ok_save4a)
			{
				ok_save4a = checkTime(i);
			}
		}
	}
	
	if (ok_save4a)
	{
		if (confirm(document.getElementById("s_commit").value))
		{
			document.getElementById("max_id").value = max_dates;
			document.getElementById("cmd").value = "save_daysexcept";
			MTstartServerComm();
			document.getElementById("lcntdays_frm").submit();
		}
	}
	else
	{
		alert(document.getElementById("ctrlval").value);
	}
}

function checkDate(idrow)
{
	controllo = true;
	
	var n_month = Number(document.getElementById("month_"+idrow).value);
	var n_day = Number(document.getElementById("day_"+idrow).value);
	
	var max_days_of_month = 30;
	
	switch (n_month)
	{
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12: max_days_of_month = 31;
				break;
		case 2: max_days_of_month = 29;
				break;
		default : max_days_of_month = 30;
	}
	
	if (n_day > max_days_of_month)
		controllo = false;
	
	return controllo;
}

function check2Date(irw)
{
	// valori ultima data inserita:
	var i_month = Number(document.getElementById("month_"+irw).value);
	var i_day = Number(document.getElementById("day_"+irw).value);
	
	var ok_2date = true;
	
	// controllo date duplicate:
	for (d = 0; d < irw; d++)
	{
		curr_month = Number(document.getElementById("month_"+d).value);
		curr_day = Number(document.getElementById("day_"+d).value);
		
		if ((curr_month == i_month) && (curr_day == i_day))
			ok_2date = false;
	}
	
	return ok_2date;
}

function checkTime(irow)
{
	var time_ok = true;
	
	var on1 = Number(document.getElementById("on1_day_"+irow).value);
	var off1 = Number(document.getElementById("off1_day_"+irow).value);
	var on2 = Number(document.getElementById("on2_day_"+irow).value);
	var off2 = Number(document.getElementById("off2_day_"+irow).value);
	
	// ctrl su valori orari:
	if (((on1 != -1) && (off1 != -1)) || ((on2 != -1) && (off2 != -1)))
	{
		
		// ctrl su date duplicate:
		if (check2Date(irow))
		{
		
			//controllo 1ma fascia:
			if (on1 != -1)
			{
				if (off1 != -1)
				{
					if (on1 >= off1)
						time_ok = false;
				}
				else
				{
					time_ok = false;
				}
			}
			else
			{
				if (off1 != -1)
					time_ok = false;
			}
						
			//controllo 2nda fascia:
			if (time_ok)
			{
				if (on2 != -1)
				{
					if (off2 != -1)
					{
						if (on2 >= off2)
							time_ok = false;
					}
					else
					{
						time_ok = false;
					}
				}
				else
				{
					if (off2 != -1)
						time_ok = false;
				}
			}
						
			//controllo eventuale integrazione 1ma + 2nda fascia:
			if (time_ok)
			{
				if (on2 != -1)
				{
					if (off1 != -1)
					{
						if (on2 <= off1)
							time_ok = false;
					}
				}
			}
		}
		else
			time_ok = false;
	}
	else
		time_ok = false;
	
	return time_ok;
}

function addDate()
{
	var last_row = document.getElementById("max_id").value;
	
	if ((document.getElementById("month_"+last_row).value != "-1") && (document.getElementById("day_"+last_row).value != "-1"))
	{
		if (checkDate(last_row))
		{
			if (checkTime(last_row))
			{
				var combo_grps = document.getElementById("cmb_grps").value;
				var combo_months = document.getElementById("cmb_months").value;
				var combo_days = document.getElementById("cmb_days").value;
				var timeformat = document.getElementById("timeformat").value;
				
				var combo_time = "";
				if (timeformat == 0)
					combo_time = newTimes24h();
				else
					combo_time = newTimesAP();
				
				var last_new_row = Number(last_row) + 1;
				
				var tabella = document.getElementById("tbl_date");
				
				var new_row = document.createElement("tr");
				new_row.className = "Row1";
				
				var new_col0 = document.createElement("td");
				new_col0.className = "standardTxt";
				new_col0.align = "center";
				new_col0.innerHTML = "<select id='grp_"+last_new_row+"' name='grp_"+last_new_row+"' onchange='enableAction(2);'>"+combo_grps+"</select>\n";
				
				var new_col1 = document.createElement("td");
				new_col1.className = "standardTxt";
				new_col1.style.width = "10%";
				new_col1.align = "center";
				new_col1.innerHTML = "<select id='month_"+last_new_row+"' name='month_"+last_new_row+"' onchange='enableAction(2);'>"+combo_months+"</select>\n";
				
				var new_col2 = document.createElement("td");
				new_col2.className = "standardTxt";
				new_col2.style.width = "7%";
				new_col2.align = "center";
				new_col2.innerHTML = "<select style='width:75%' id='day_"+last_new_row+"' name='day_"+last_new_row+"' onchange='enableAction(2);'>"+combo_days+"</select>\n";
				
				var new_col3 = document.createElement("td");
				new_col3.className = "standardTxt";
				new_col3.style.width = "33%";
				new_col3.align = "center";
				new_col3.innerHTML = "<input type='text' style='width:100%' id='descr_"+last_new_row+"' name='descr_"+last_new_row+"' value=''/>\n";
				
				var new_col4 = document.createElement("td");
				new_col4.className = "standardTxt";
				new_col4.style.width = "10%";
				new_col4.align = "center";
				new_col4.innerHTML = "<select id='on1_day_"+last_new_row+"' name='on1_day_"+last_new_row+"' onchange='enableAction(2);'>"+combo_time+"</select>\n";
				
				var new_col5 = document.createElement("td");
				new_col5.className = "standardTxt";
				new_col5.style.width = "10%";
				new_col5.align = "center";
				new_col5.innerHTML = "<select id='off1_day_"+last_new_row+"' name='off1_day_"+last_new_row+"' onchange='enableAction(2);'>"+combo_time+"</select>\n";
				
				var new_col6 = document.createElement("td");
				new_col6.className = "standardTxt";
				new_col6.style.width = "10%";
				new_col6.align = "center";
				new_col6.innerHTML = "<select id='on2_day_"+last_new_row+"' name='on2_day_"+last_new_row+"' onchange='enableAction(2);'>"+combo_time+"</select>\n";
				
				var new_col7 = document.createElement("td");
				new_col7.className = "standardTxt";
				new_col7.style.width = "10%";
				new_col7.align = "center";
				new_col7.innerHTML = "<select id='off2_day_"+last_new_row+"' name='off2_day_"+last_new_row+"' onchange='enableAction(2);'>"+combo_time+"</select>\n";
				
				var new_col8 = document.createElement("td");
				new_col8.className = "standardTxt";
				new_col8.style.width = "10%";
				new_col8.align = "center";
				new_col8.innerHTML = "<input type='checkbox' id='del_"+last_new_row+"' name='del_"+last_new_row+"' onclick='enableAction(2);'/>\n";
				
				new_row.appendChild(new_col0);
				new_row.appendChild(new_col1);
				new_row.appendChild(new_col2);
				new_row.appendChild(new_col3);
				new_row.appendChild(new_col4);
				new_row.appendChild(new_col5);
				new_row.appendChild(new_col6);
				new_row.appendChild(new_col7);
				new_row.appendChild(new_col8);
				
				tabella.tBodies[0].appendChild(new_row);
				
				document.getElementById("max_id").value = last_new_row;
				
				enableAction(2);
			}
			else
			{
				alert(document.getElementById("ctrlval").value);
			}
		}
		else
		{
			alert(document.getElementById("datanotincal").value);
		}
	}
	else
	{
		alert(document.getElementById("lastrowempty").value);
	}
}

function goBackToLN()
{
	var menu_item = document.getElementById("menu_item").value;
	
	top.frames['manager'].loadTrx('lucinotte/SubTab4.jsp&folder=lucinotte&bo=BLuciNotte&type=menu&desc='+menu_item+'');
}

function selectAll()
{
	var last_row = document.getElementById("max_id").value;
	var tabella = document.getElementById("tbl_date");
	
	for (i = 0; i <= last_row; i++)
	{
		document.getElementById("del_"+i).checked = true;
	}
	
	enableAction(2); //save
}

var hpos = 10000; //posizione ore
var mpos = 100; //posizione minuti

function newTimes24h()
{
	var orario = "<option 'selected' value='-1'> --- </option>";
    var i = 0;
    for (i = 0; i < 24; i++)
    {
        orario = orario + "<option value='"+(i*hpos)+"'>"+i+".00</option>";
        orario = orario + "<option value='"+(i*hpos + 30*mpos)+"'>"+i+".30</option>";
    }

    orario = orario + "<option value='"+(24*hpos)+"'>24.00</option>";
    
    return orario;
}

function newTimesAP()
{
	var orario = "<option 'selected' value='-1'> --- </option>";
    var i = 0;
    for (i = 0; i < 12; i++)
    {
        orario = orario + "<option value='"+(i*hpos)+"'>"+i+".00a</option>";
        orario = orario + "<option value='"+(i*hpos + 30*mpos)+"'>"+i+".30a</option>";
    }

	orario = orario + "<option value='"+(12*hpos)+"'>12.00a</option>";
	orario = orario + "<option value='"+(12*hpos + 30*mpos)+"'>0.30p</option>";

	for (i = 1; i < 12; i++)
	{
		orario = orario + "<option value='"+((i+12)*hpos)+"'>"+i+".00p</option>";
        orario = orario + "<option value='"+((i+12)*hpos + 30*mpos)+"'>"+i+".30p</option>";
	}

    orario = orario + "<option value='"+(24*hpos)+"'>12.00p</option>";
    
    return orario;
}