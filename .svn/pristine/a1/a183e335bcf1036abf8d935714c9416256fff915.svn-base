function AScheckbox(obj,texta)   //controllo checkbox per textarea
{
	var chk= document.getElementById(obj);
	var text = document.getElementById(texta);
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
	var period = document.data.period;
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


function ASreloadPage(obj)
{
	
	var userres = document.getElementById("userevent").value;
	var xhval = document.getElementById("xhval").value;
	var categ = document.getElementById("EScategory").value;
			
	var arDati = obj.value.split(","); //array di parametri
	var groupSel = arDati[0];
	var deviceSel = arDati[1];
	var alarmSel = arDati[2];
	var reset = arDati[3];
		
	var reload = document.getElementById("reload"); //Form
	reload.method = "post";
	reload.action = "servlet/master;" + getSessId()+"?trx=alrevnsearch/SubTab3.jsp"+
		"&selectedGroup="+groupSel+"&selectedDevice="+deviceSel+"&selectedAlarm="+alarmSel+"&reset="+reset+
		"&dataselect="+ASgetOptionDate()+"&xhval="+xhval+"&datefrom="+ASgetDateFrom()+"&dateto="+ASgetDateTo()+
		"&userres="+userres+'&EScategory='+categ;
	
	if(reload != null)
			MTstartServerComm();
	reload.submit();
}

//2010-1-11, add by kevin
function checkHourMax()
{
	var virtkey = document.getElementById("virtkeyboard").value;
	if(virtkey == "on")
	{
		var radios = document.getElementsByName("period");
		if(radios[0].checked == true)
		{
			var xhval = document.getElementById("xhval");
			if(checkMax(xhval) == false)
			{
				return false;
			}
		}
	}
	return true;
}
//end

function AlEvSearch()
{
	if(checkHourMax() == false)
	{
		return;
	}
	if (verifyDate())
	{
		var combo = document.getElementById("device").value.split(",");
		var userres = document.getElementById("userevent").value;
		var desc = document.getElementById("alarmFound").value;
		var xhval = document.getElementById("xhval").value;
		var categ = document.getElementById("EScategory").value;
		
		var groupSel = combo[0];
		var deviceSel = combo[1];
		var alarmSel = combo[2];
		var reset = combo[3];
		
		MTstartServerComm();
		top.frames['manager'].loadTrx('nop&folder=searchalev&bo=BSearchAlEv&type=click'+
			'&selectedGroup='+groupSel+'&selectedDevice='+deviceSel+'&selectedAlarm='+alarmSel+
			'&dataselect='+ASgetOptionDate()+'&xhval='+xhval+'&datefrom='+ASgetDateFrom()+'&dateto='+ASgetDateTo()+
			'&userevent='+userres+'&remote=no&desc='+desc+'&EScategory='+categ);
		}
}

function ASonLoad()
{
	if (document.getElementById("fromto")!=null)
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
}

//se scelgo un Allarme specifico, implicitamente scelgo anche la priorit�
function checkPriorityCmb(valore)
{
	var elems = valore.split(",");
	var valoreFinale = elems[elems.length - 1];
	
	if (valoreFinale != "")
		document.getElementById("priority").disabled = true;
	else
		document.getElementById("priority").disabled = false;
	
	return;	
}

//se scelgo Priority, non ha senso scegliere un Allarme specifico
function checkAlarmsCmb(valore)
{
	if (valore != "")
		document.getElementById("alarm").disabled = true;
	else
		document.getElementById("alarm").disabled = false;
	
	return;	
}

function checkMax(hours)
{
	if ((hours.value > 24) || (hours.value < 1))
	{
		alert(document.getElementById("notValidHours").value);
		hours.value = 24;
		return false;
	}
	return true;
}
