function AScheckbox(obj,text)   //controllo checkbox per textarea
{
	var chk= document.getElementById(obj);
	var text = document.getElementById(text);
	if (chk.checked==true)
	{
		text.disabled=false;
		text.focus();
	}
	else
	{
		text.value="";
		text.disabled=true;
	}
	
}
 
function ASgetOptionDate()
{
	var dataselect = null;
	var period = document.frm_r_alrsearch.period;
	for(var i=0; i<period.length; i++)
	{
		if (period[i].checked)
		{
			return period[i].id;
		}	
	}
}

function ASgetDateFrom()
{
	return document.getElementById("tester").value;
}

function ASgetDateTo()
{
	return document.getElementById("tester2").value;
}

function R_AlrSearch()
{
	if (verifyDate())
	{
		var id_site = document.getElementById("id_site").value;
		var category = document.getElementById("category").value;
		var desc = document.getElementById("alarmFound").value;
		
		top.frames['manager'].loadTrx('nop&folder=searchal&bo=BSearchAl&type=click&'+
			'dataselect='+ASgetOptionDate()+'&datefrom='+ASgetDateFrom()+
			'&dateto='+ASgetDateTo()+'&id_site='+id_site+'&category='+category+
			'&remote=yes&desc='+desc);
	}
}

function R_AlrOnLoad()
{
	var isSelected = document.getElementById("fromto").checked==true;
	if (isSelected==true)
	{
		enableCal();
		var date_from = document.getElementById("reload_from").value;
		var date_to = document.getElementById("reload_to").value;
		setDate(date_from,date_to);
	}
	else disableCal();
}