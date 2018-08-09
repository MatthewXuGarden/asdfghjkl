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
	
	var chkack = document.getElementById("chkack").checked==true;
	var chkdel = document.getElementById("chkdel").checked==true;
	var chkres = document.getElementById("chkres").checked==true;
	var userack = document.getElementById("userack").value;
	var userdel = document.getElementById("userdel").value;
	var userres = document.getElementById("userres").value;
	var prioritySel = document.getElementById("priority").value;
	var xhval = document.getElementById("xhval").value;
			
	var arDati = obj.value.split(","); //array di parametri
	var groupSel = arDati[0];
	var deviceSel = arDati[1];
	var alarmSel = arDati[2];
	var reset = arDati[3];
		
	var reload = document.getElementById("reload"); //Form
	reload.method = "post";
	reload.action = "servlet/master;" + getSessId()+"?trx=alrevnsearch/SubTab1.jsp"+
		"&selectedGroup="+groupSel+"&selectedDevice="+deviceSel+"&selectedAlarm="+alarmSel+"&reset="+reset+
		"&dataselect="+ASgetOptionDate()+"&xhval="+xhval+"&datefrom="+ASgetDateFrom()+"&dateto="+ASgetDateTo()+
		"&chkack="+chkack+"&chkdel="+chkdel+"&chkres="+chkres+"&userack="+userack+
		"&userdel="+userdel+"&userres="+userres+"&prioritySel="+prioritySel;
	
	if(reload != null)
			MTstartServerComm();
	reload.submit();
}
//2010-1-11, add by Kevin
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
function ASAlSearch()
{
	if(checkHourMax() == false)
	{
		return;
	}
	if (verifyDate())
	{
		var combo = document.getElementById("alarm").value.split(",");
		
		var chkack = document.getElementById("chkack").checked==true;
		var chkdel = document.getElementById("chkdel").checked==true;
		var chkres = document.getElementById("chkres").checked==true;
		var userack = document.getElementById("userack").value;
		var userdel = document.getElementById("userdel").value;
		var userres = document.getElementById("userres").value;
		var desc = document.getElementById("alarmFound").value;
		var priority = document.getElementById("priority").value;
		var xhval = document.getElementById("xhval").value;
		
		var groupSel = combo[0];
		var deviceSel = combo[1];
		var alarmSel = combo[2];
		var reset = combo[3];
		
		MTstartServerComm();
		top.frames['manager'].loadTrx('nop&folder=searchal&bo=BSearchAl&type=click'+
			'&selectedGroup='+groupSel+'&selectedDevice='+deviceSel+'&selectedAlarm='+alarmSel+
			'&dataselect='+ASgetOptionDate()+'&xhval='+xhval+'&datefrom='+ASgetDateFrom()+'&dateto='+ASgetDateTo()+
			'&chkack='+chkack+'&chkdel='+chkdel+'&chkres='+chkres+'&userack='+userack+
			'&userdel='+userdel+'&userres='+userres+'&prioritySel='+priority+'&remote=no&desc='+desc);
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

//se scelgo Priorit�? non ha senso scegliere un Allarme specifico
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
		alert("Please, insert max 24h and min 1h");
		hours.value = 24;
		return false;
	}
	return true;
}
