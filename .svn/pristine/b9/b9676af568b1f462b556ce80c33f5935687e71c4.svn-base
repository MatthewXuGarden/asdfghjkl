// TAB 1
function start_ac()
{
	document.getElementById("cmd").value = "start_ac";
	
	MTstartServerComm();
	
	document.getElementById("ac_frm").submit();
	disableAction(1);
	enableAction(2);
}

function stop_ac()
{
	document.getElementById("cmd").value = "stop_ac";
	
	MTstartServerComm();
	
	document.getElementById("ac_frm").submit();
	enableAction(1);
	disableAction(2);
}

// TAB 2
function onload_tab2()
{
	//se c'è la form allora ho almeno un master configurato:
	if (document.getElementById("ac_frm"))
	{
		enableAction(3);
		
		//controllo permessi utente per gestione motore di propagazione:
		if (document.getElementById("gest_motore").value == "true")
		{
			//ok gestione motore:
			if (document.getElementById("s_engine").value == "on")
			{
				enableAction(2);
				disableAction(1);
			}
			else
			{
				enableAction(1);
				disableAction(2);
			}
		}
		else
		{
			//no gestione motore:
			disableAction(1);
			disableAction(2);
		}
		
		var msg = document.getElementById("save_slaves_ris").value;
		if ( msg != "") alert(msg);
	}
	else //altrimenti, se non ho nessun master configurato:
	{
		disableAction(1);
		disableAction(2);
		disableAction(3);
	}
}

function save_tab2()
{

	var control_time = document.getElementById("ver_time").value;
	var tempo_min = document.getElementById("tempo_min").value;
	
	if (control_time == "" || Number(control_time) < Number(tempo_min))
	{
		alert(document.getElementById("mintime").value + "\n\n   " + tempo_min);
		document.getElementById("ver_time").value = tempo_min;
		return false;
	}
	else
	{
		//controllo validità nomi gruppi:
		var masters = document.getElementById("masters").value;
		var id_mstrs = masters.split(";");
		var ok = true;
		
		for (var i = 0; i < id_mstrs.length; i++)
		{
			var idm = id_mstrs[i];
			if (document.getElementById("grp_"+idm).value == "")
			{
				ok = false;
				break;
			}
		}
		
		if (ok)
		{
			document.getElementById("cmd").value = "save_slave"; // + abilitazione alla propagazione x i master
			MTstartServerComm();
			document.getElementById("ac_frm").submit();
		}
		else
		{
			alert(document.getElementById("inputno_ok").value);
			return false;
		}
	}
}

///////// TAB 3
function onload_tab3()
{
	if (document.getElementById("ids_master") != null)
	{
		var ids = document.getElementById("ids_master").value;
		
		if (ids!="")
		{
			//enableAction(3);
	
			var s_ids = ids.split(";");
			var id = "";
			var check = false;
			
			for (var i = 0; i < s_ids.length; i++)
			{
				id = s_ids[i];
				
				//visto che posso avere master già settati come slave:
				if (document.getElementById("ch_"+id))
				{
					check = document.getElementById("ch_"+id).checked;
					var vars = document.getElementById("vars_"+id).value.split(";");
					
					for (var j = 0; j < vars.length; j++)
					{
						document.getElementById("def_"+vars[j]).disabled = !check;
					}
				}
			}
		}
	}
}

function check_dev(obj,iddev)
{
	var MAX_LIMIT = document.getElementById("max_master").value;
	var vars = document.getElementById("vars_"+iddev).value.split(";");
	var check = obj.checked;
	var ids = document.getElementById("ids_master").value;
	
	if (ids!="")
	{
		var spl_ids = ids.split(";");
		var count = 0;
		
		for (var i = 0; i < spl_ids.length; i++)
		{
			if (document.getElementById("ch_"+spl_ids[i]))
			{
				if (document.getElementById("ch_"+spl_ids[i]).checked)
				{
					count++;
				}
			}
		}
		
		if (count > MAX_LIMIT)
		{
			obj.checked = false;
			alert(document.getElementById("maxl").value+" "+ MAX_LIMIT);
			return false;
		}
		else
		{
			for (i = 0 ; i < vars.length; i++)
			{
				document.getElementById("def_"+vars[i]).disabled = !check;
			}
		}
	}
	
	enableAction(1);
}

function save_tab3()
{
	var ids = document.getElementById("ids_master").value;
	
	if (ids != "")
	{
		var s_ids = ids.split(";");
		var iddev = "";
		var idvar = "";
		var def = "";
		var min = "";
		var max = "";
		var selectOne=false;
		for (var i = 0; i < s_ids.length; i++)
		{
			iddev = s_ids[i];
			var vars = document.getElementById("vars_"+iddev).value.split(";");
			
			for (var j = 0; j < vars.length; j++)
			{
				idvar = vars[j];
				
				if (document.getElementById("ch_"+iddev))
				{
					if (document.getElementById("ch_"+iddev).checked)
					{
						selectOne=true;
						document.getElementById("def_"+idvar).style.background = "#FFFFFF";
						def = document.getElementById("def_"+idvar).value;
						if (def == "")
						{
							document.getElementById("def_"+idvar).style.background = "#FF0000";
							alert(document.getElementById("missingdefault").value);
							return false;
						}
						else
						{
							def = def.replace(",",".");
							document.getElementById("def_"+idvar).value = def;
							min = Number(document.getElementById("min_"+idvar).value);
							max = Number(document.getElementById("max_"+idvar).value);
							
							if (def < min)
							{
								document.getElementById("def_"+idvar).style.background = "#FF0000";
								alert(document.getElementById("undermin").value+" " +min);
								return false;
							}
							
							if (def > max)
							{
								document.getElementById("def_"+idvar).style.background = "#FF0000";
								alert(document.getElementById("overmax").value+" " + max);
								return false;
							}
						}
					}
				}
			}// chiuso for ciclo variabili per dispositivo
		}
		//simon add at 2010-1-20 for Bug 6564
		if((selectOne==false)&&(document.getElementById("s_engine").value == "on")){
			alert(document.getElementById("stopservice").value);
			return false;
		}
	}
	document.getElementById("cmd").value = "save_master";
	MTstartServerComm();
	document.getElementById("ac_frm").submit();
}

// x SubTab2:
function select_all_radio(idmstrdev, mstr)
{
	var ids = document.getElementById("ids_slave").value;
	var n_master = document.getElementById("n_master").value;
	var obj = document.getElementById("radio_mstr"+mstr);
	
	if (idmstrdev == -1) idmstrdev = "na";
	
	if (ids != "")
	{
		var slaves_ids = ids.split(";");
		var notcheck = false;
		
		if (obj.value == "false")
		{
			if (confirm(document.getElementById("selectallradio").value))
			{
				for (var i = 0; i < slaves_ids.length; i++)
				{
					for (var j = 0; j <= n_master; j++)
					{
						document.getElementsByName("sl_"+slaves_ids[i])[j].checked = notcheck;
						
						if (document.getElementsByName("sl_"+slaves_ids[i])[j].value == idmstrdev)
						{
							document.getElementsByName("sl_"+slaves_ids[i])[j].checked = !notcheck;
						}
						else
						{	
							document.getElementById("radio_mstr"+(j+1)).value = false;
						}
					}
				}
				
				obj.value = "true";
			}
		}
		else
		{
			if ((mstr <= n_master) && (confirm(document.getElementById("deselectallradio").value)))
			{
				for (i = 0; i < slaves_ids.length; i++)
				{
					for (j = 0; j <= n_master; j++)
					{
						if (document.getElementsByName("sl_"+slaves_ids[i])[j].value == "na")
							document.getElementsByName("sl_"+slaves_ids[i])[j].checked = !notcheck;
					}
				}
				
				if (mstr <= n_master)
					obj.value = "false";
			}
		}
		
		document.getElementsByName("Master")[mstr-1].checked = false;
	}
	
	//obj.checked = notcheck;
}

function submitSlaveConf()
{
	//if (confirm(document.getElementById("confermasave").value))
	if (confirm(document.getElementById("s_commit").value))
	{
		save_tab2();
		return true;
	}
	else return false;
}

function openMasterDtl(iddevmstr)
{
	var linkname = "";
	linkname = document.getElementById("linkname").value;
	
	top.frames['manager'].loadTrx('nop&iddevmstr='+iddevmstr+'&folder=acdtlview&bo=BACDtlView&type=click&desc='+linkname);
}

function noslaves()
{
	var msg = document.getElementById("noslaves").value;
	alert(msg);
}
