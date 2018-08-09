function onload_tab1()
{
	enableAction(1);
}

function onload_tab2()
{
	enableAction(1);
}

function onload_tab3()
{
	enableAction(1);
}

function onload_tab4()
{
	/*
	var idgrp = document.getElementById("idGr").value;
	
	if (idgrp != -1)
	{
		document.getElementById("cmb_grps").selectedIndex = idgrp;
		loadScheduling(document.getElementById("cmb_grps"));
	}
	else
	if (document.getElementById("cmb_grps"))
	{
		showActive();
	}
	*/
	
	//var maxgr = Number(document.getElementById("maxgroups").value);
	
	loadScheduling();
	
	placeTimeCmbo();
	
	/*
	for (gr = 1; gr <= maxgr; gr++)
	{
		loadScheduling(gr);
	}
	*/
	
	enableAction(1);
}

var newWindow;

// x SubTab4:
function placeTimeCmbo()
{
	var ore = document.getElementById("ore").value;
	var timetype = Number(document.getElementById("timetype").value);
	
	var timeCmbo = "&nbsp;<select id='timeformat' onchange='setTimeFormat(this);'>";
	timeCmbo += "<option value='0'>24/"+ore+"</option>";
    timeCmbo += "<option value='1'>12/"+ore+"</option>";
	timeCmbo += "</select>";
	
	document.getElementById("sdkActionCombo").innerHTML = timeCmbo;
	document.getElementById("timeformat").selectedIndex = timetype;
}

// x SubTab4:
function copySched(grp)
{
		if (confirm(document.getElementById("copysched").value))
		{
			if (confirm(document.getElementById("confirmcopy").value))
			{
				if (ctrl_tab4(grp))
				{
					var maxgr = Number(document.getElementById("maxgroups").value);
					var indice = -1;
					
					for (igr = 1; igr <= maxgr; igr++)
					{
						if (igr != grp)
						{
							for (nd = 1; nd <= 7; nd++)
							{
								for (fon = 1; fon <= 2; fon++)
								{
									indice = -1;
									indice = document.getElementById("g"+grp+"_day"+nd+"_on"+fon).selectedIndex;
									document.getElementById("g"+igr+"_day"+nd+"_on"+fon).selectedIndex = indice;
								}
								
								for (foff = 1; foff <= 2; foff++)
								{
									indice = -1;
									indice = document.getElementById("g"+grp+"_day"+nd+"_off"+foff).selectedIndex;
									document.getElementById("g"+igr+"_day"+nd+"_off"+foff).selectedIndex = indice;
								}
							}
						}
					}
					
					//document.getElementById("copySched_g"+grp).selected = false;
				}
				else
				{
					//alert(document.getElementById("ctrlval").value);
				}
			}
		}
}

// x SubTab4:
function resetSched(idgr)
{
	if (confirm(document.getElementById("resetsched").value))
	{
		if (confirm(document.getElementById("confirmreset").value))
		{
			for (nd = 1; nd <= 7; nd++)
			{
				for (fon = 1; fon <= 2; fon++)
				{
					document.getElementById("g"+idgr+"_day"+nd+"_on"+fon).selectedIndex = 0;
				}
							
				for (foff = 1; foff <= 2; foff++)
				{
					document.getElementById("g"+idgr+"_day"+nd+"_off"+foff).selectedIndex = 0;
				}
			}
		}
	}
}

function onload_tab6()
{
	loadMdlVars(document.getElementById("cmb_devmdl"));
}

// x SubTab4:
function showActive()
{
	if (document.getElementById("cmb_grps").value == -1)
		{
			document.getElementById("act_grp").style.visibility = "hidden";
			disableAction(1);
			cmbStatus(true);
		}
		else
		{
			document.getElementById("act_grp").style.visibility = "visible";
			enableAction(1);
			cmbStatus(false);
		}
}

// x SubTab4:
function cmbStatus(stato)
{
	for (cmbi = 1; cmbi <= 7; cmbi++)
	{
		document.getElementById("day"+cmbi+"_on1").disabled = stato;
		document.getElementById("day"+cmbi+"_off1").disabled = stato;
		document.getElementById("day"+cmbi+"_on2").disabled = stato;
		document.getElementById("day"+cmbi+"_off2").disabled = stato;
	}
}

// x SubTab1:
function exeOnOff(str,cmd,ng)
{
	var group = document.getElementById("groupstr").value;
	
	if (confirm(str+" "+group+" "+ng+" ?"))
	{
		//document.getElementById("cmd").value = "";
		document.getElementById("execmd").value = cmd;
		document.getElementById("exegrp").value = ng;
		MTstartServerComm();
		document.getElementById("execmd_frm").submit();
		
		document.getElementById("execmd").value = "";
	}
}

function save_tab1()
{
	var ok_save1 = true;
	
	for (i = 0; i < 5; i++)
	{
		if (ok_save1)
		{
			if (document.getElementById("name_grp_"+i))
			{
				if ((document.getElementById("name_grp_"+i)).value == "")
				{
					ok_save1 = false;
				}
			}
		}
	}
	
	if (ok_save1)
	{
		if (confirm(document.getElementById("s_commit").value))
		{
			document.getElementById("cmd").value = "save_grps_status";
			MTstartServerComm();
			document.getElementById("lcnt_frm").submit();
			
			document.getElementById("cmd").value = "";
		}
	}
	else
	{
		alert(document.getElementById("noemptyname").value);
	}
}

function save_tab2()
{
	var ok_save2 = true;
	var msg = "";
	
	for (i = 0; i < 5; i++)
	{
		if (ok_save2)
		{
			if (document.getElementById("name_grp_"+i))
			{
				if ((document.getElementById("name_grp_"+i)).value == "")
				{
					ok_save2 = false;
					msg = document.getElementById("insgrpname").value;
				}
			}
		}
	}
	
	if (ok_save2)
	{
		var ids = document.getElementById("ids_devs").value;
		var n_groups = document.getElementById("n_groups").value;
		
		var ids_devs = document.getElementById("ids_devs").value;
				
		if (ids_devs != "")
		{
			var devs_ids = ids_devs.split(";");
					
			for (i = 0; i < devs_ids.length; i++)
			{
				if (document.getElementById("dev_"+devs_ids[i]+"__grp_na").checked == false)
					if ((document.getElementsByName("lcnt_dev_"+devs_ids[i])[0].checked == false) && (document.getElementsByName("lcnt_dev_"+devs_ids[i])[1].checked == false))
					{
						ok_save2 = false;
						msg = document.getElementById("implcnt").value;
					}
			}
		}
	}
	
	if (ok_save2)
	{
		if (confirm(document.getElementById("s_commit").value))
		{
			document.getElementById("cmd").value = "save_devsxgrps_status";
			MTstartServerComm();
			document.getElementById("lcnt_frm").submit();
			
			document.getElementById("cmd").value = "";
		}
	}
	else
	{
		alert(msg);
	}
}

function save_tab3()
{
	var ok_save3 = true;
	
	for (i = 0; i < 5; i++)
	{
		if (ok_save3)
		{
			if ((document.getElementById("devs_grp_"+i)) && (document.getElementById("devs_grp_"+i).value != -1))
			{
				if ((document.getElementById("digvar_grp_"+i)).value == -1)
				{
					ok_save3 = false;
				}
			}
		}
	}
	
	if (ok_save3)
	{
		if (confirm(document.getElementById("s_commit").value))
		{
			document.getElementById("cmd").value = "save_fieldvars";
			MTstartServerComm();
			document.getElementById("lcnt_frm").submit();
			
			document.getElementById("cmd").value = "";
		}
	}
	else
	{
		alert(document.getElementById("ctrlval").value);
	}	
}

// x SubTab4:
function ctrl_tab4(ctrl_g)
{
	var maxgrps = Number(document.getElementById("maxgroups").value);
	var ok_save4 = true;
	
	var gi = 1;
	var gf = maxgrps;
	
	if (ctrl_g > 0)
	{
		gi = ctrl_g;
		gf = ctrl_g;
	}
	
	var on1 = 0;
	var off1 = 0;
	var on2 = 0;
	var off2 = 0;
	
	var ctrlvals = "";
	var group = "";
	var day = "";
	var weekday = "";
	var bad_g = 0;
	var bad_d = 0;
	
	//per ogni gruppo
	for (g = gi; g <= gf; g++)
	{
	
		//per ogni giorno della settimana:
		for (i = 1; i <= 7; i++)
		{
			if (ok_save4)
			{
				on1 = Number(document.getElementById("g"+g+"_day"+i+"_on1").value);
				off1 = Number(document.getElementById("g"+g+"_day"+i+"_off1").value);
				on2 = Number(document.getElementById("g"+g+"_day"+i+"_on2").value);
				off2 = Number(document.getElementById("g"+g+"_day"+i+"_off2").value);
				
				//controllo 1ma fascia:
				if (on1 != -1)
				{
					if (off1 != -1)
					{
						if (on1 >= off1)
						{
							ok_save4 = false;
							bad_g = g;
							bad_d = i;
						}
					}
					else
					{
						ok_save4 = false;
						bad_g = g;
						bad_d = i;
					}
				}
				else
				{
					if (off1 != -1)
					{
						ok_save4 = false;
						bad_g = g;
						bad_d = i;
					}
				}
				
				//controllo 2nda fascia:
				if (ok_save4)
				{
					if (on2 != -1)
					{
						if (off2 != -1)
						{
							if (on2 >= off2)
							{
								ok_save4 = false;
								bad_g = g;
								bad_d = i;
							}
						}
						else
						{
							ok_save4 = false;
							bad_g = g;
							bad_d = i;
						}
					}
					else
					{
						if (off2 != -1)
						{
							ok_save4 = false;
							bad_g = g;
							bad_d = i;
						}
					}
				}
				
				//controllo integrazione 1ma + 2nda fascia:
				if (ok_save4)
				{
					if (on2 != -1)
					{
						if (off1 != -1)
						{
							if (on2 <= off1)
							{
								ok_save4 = false;
								bad_g = g;
								bad_d = i;
							}
						}
					}
				}
				
			}
		}
	}
	
	if ((bad_g > 0) && (bad_d > 0))
	{
		ctrlvals = document.getElementById("ctrlval").value;
		group = document.getElementById("group").value;
		day = document.getElementById("day").value;
		weekday = document.getElementById("g"+bad_g+"_day"+bad_d).innerHTML;
		
		alert(ctrlvals + ":\n\n"+group+"\t"+bad_g+"\n"+day+"\t"+weekday.substring(3,5));
	}
	
	return ok_save4;
}

function save_tab4()
{
	if (ctrl_tab4(0))
	{
		if (confirm(document.getElementById("s_commit").value))
		{
			document.getElementById("cmd").value = "save_scheduling";
			MTstartServerComm();
			document.getElementById("lcnt_frm").submit();
			
			document.getElementById("cmd").value = "";
		}
	}
	else
	{
		//alert(document.getElementById("ctrlval").value);
	}
}

function save_tab6()
{
	var ok_save6 = true;
	
	//almeno uno dei cmd deve essere selezionato!
	if ((document.getElementById("cmd_onoff").value == -1)&&(document.getElementById("cmd_luci").value == -1)&&(document.getElementById("cmd_notte").value == -1)
	&&(document.getElementById("st_onoff").value != -1  || document.getElementById("st_luci").value != -1 || document.getElementById("st_notte").value != -1))
	{
		ok_save6 = false;
	}
	
	
	if (ok_save6)
	{
		if (confirm(document.getElementById("s_commit").value))
		{
			document.getElementById("cmd").value = "save_models";
			MTstartServerComm();
			document.getElementById("lcnt_frm").submit();
			
			document.getElementById("cmd").value = "";
		}
	}
	else
	{
		alert(document.getElementById("selcmd").value);
	}
}

// x SubTab1:
function checkManual(num_grp)
{
	if (document.getElementById("manual_grp_"+num_grp).checked)
	{
		document.getElementById("onbtn_grp_"+num_grp).src = "images/button/on.png";
		document.getElementById("onbtn_grp_"+num_grp).onclick = function(){exeOnOff("go_On",num_grp);};
		
		document.getElementById("offbtn_grp_"+num_grp).src = "images/button/off.png";
		document.getElementById("offbtn_grp_"+num_grp).onclick = function(){exeOnOff("go_Off",num_grp);};
	}
	else
	{
		document.getElementById("onbtn_grp_"+num_grp).src = "images/button/on_off.png";
		document.getElementById("onbtn_grp_"+num_grp).onclick = "";
		
		document.getElementById("offbtn_grp_"+num_grp).src = "images/button/off_off.png";
		document.getElementById("offbtn_grp_"+num_grp).onclick = "";
	}
}

/*
// x SubTab1:
function go_off(num_grp)
{
	document.getElementById("offbtn_grp_"+num_grp).src = "images/button/off_off.png";
	document.getElementById("offbtn_grp_"+num_grp).onclick = "";
	
	document.getElementById("onbtn_grp_"+num_grp).src = "images/button/on.png";
	document.getElementById("onbtn_grp_"+num_grp).onclick = function(){go_on(num_grp);};
	
	document.getElementById("enable_grp_"+num_grp).value = "off";
}

// x SubTab1:
function go_on(num_grp)
{
	document.getElementById("onbtn_grp_"+num_grp).src = "images/button/on_off.png";
	document.getElementById("onbtn_grp_"+num_grp).onclick = "";
	
	document.getElementById("offbtn_grp_"+num_grp).src = "images/button/off.png";
	document.getElementById("offbtn_grp_"+num_grp).onclick = function(){go_off(num_grp);};
	
	document.getElementById("enable_grp_"+num_grp).value = "on";
}
*/

// x SubTab2:
function select_all_radio(idgrp, obj)
{
	var notcheck = false;
	var check = true;
	
	if (document.getElementById("st_grp_"+idgrp).value == "false")
	{
		if (confirm(document.getElementById("selectallradio").value))
		{
			var ids_devs = document.getElementById("ids_devs").value;
			
			if (ids_devs != "")
			{
				var devs_ids = ids_devs.split(";");
				
				for (i = 0; i < devs_ids.length; i++)
				{
					document.getElementById("dev_"+devs_ids[i]+"__grp_"+idgrp).checked = check;
					
					if (idgrp != -1)
					{
						document.getElementById("dev_"+devs_ids[i]+"__grp_na").checked = notcheck;
					}
				}
			}
			
			document.getElementById("st_grp_"+idgrp).value = "true";
		}
	}
	else
	{
		if (confirm(document.getElementById("deselectallradio").value))
		{
			var ids_devs = document.getElementById("ids_devs").value;
			
			if (ids_devs != "")
			{
				var devs_ids = ids_devs.split(";");
				
				for (i = 0; i < devs_ids.length; i++)
				{
					document.getElementById("dev_"+devs_ids[i]+"__grp_"+idgrp).checked = notcheck;
					ctrl_na(devs_ids[i]);
				}
			}
			
			document.getElementById("st_grp_"+idgrp).value = "false";
			//obj.checked = notcheck;
		}
	}
	
	obj.checked = notcheck;
}

// x SubTab2:
function allradio2luci(obje)
{
	var notcheck = false;
	var ids = document.getElementById("ids_devs").value;
	var n_groups = document.getElementById("n_groups").value;
	
	if (ids != "")
	{
		if (confirm(document.getElementById("selectallluci").value))
		{
			var devs_ids = ids.split(";");
			var notcheck = false;
			
			for (i = 0; i < devs_ids.length; i++)
			{
				for (j = 0; j <= 1; j++)
				{
					document.getElementsByName("lcnt_dev_"+devs_ids[i])[j].checked = notcheck;
					
					if (document.getElementsByName("lcnt_dev_"+devs_ids[i])[j].value == "luci")
						document.getElementsByName("lcnt_dev_"+devs_ids[i])[j].checked = !notcheck;
				}
			}
		}
	}
	
	obje.checked = false;
}

// x SubTab2:
function allradio2notte(obje)
{
	var notcheck = false;
	var ids = document.getElementById("ids_devs").value;
	var n_groups = document.getElementById("n_groups").value;
	
	if (ids != "")
	{
		if (confirm(document.getElementById("selectallnotte").value))
		{
			var devs_ids = ids.split(";");
			var notcheck = false;
			
			for (i = 0; i < devs_ids.length; i++)
			{
				for (j = 0; j <= 1; j++)
				{
					document.getElementsByName("lcnt_dev_"+devs_ids[i])[j].checked = notcheck;
					
					if (document.getElementsByName("lcnt_dev_"+devs_ids[i])[j].value == "notte")
						document.getElementsByName("lcnt_dev_"+devs_ids[i])[j].checked = !notcheck;
				}
			}
		}
	}
	
	obje.checked = false;
}

// x SubTab2:
function uncheck_na(iddev)
{
	document.getElementById("dev_"+iddev+"__grp_na").checked = false;
}

// x SubTab2:
function check_na(iddev)
{
	var n_groups = document.getElementById("n_groups").value;
	
	for (i = 0; i < n_groups; i++)
	{
		document.getElementById("dev_"+iddev+"__grp_"+(i+1)).checked = false;
	}
	
	document.getElementsByName("lcnt_dev_"+iddev)[0].checked = false;
	document.getElementsByName("lcnt_dev_"+iddev)[1].checked = false;
}

// x SubTab2:
function allradio2na(obje)
{
	if (confirm(document.getElementById("allradio2na").value))
	{
		var ids_devs = document.getElementById("ids_devs").value;
		var n_groups = document.getElementById("n_groups").value;
		
		if (ids_devs != "")
		{
			var devs_ids = ids_devs.split(";");
			var notcheck = false;
			var check = true;
			
			for (i = 0; i < devs_ids.length; i++)
			{
				for (j = 0; j < n_groups; j++)
				{
					document.getElementById("dev_"+devs_ids[i]+"__grp_"+(j+1)).checked = notcheck;
				}
				
				document.getElementById("dev_"+devs_ids[i]+"__grp_na").checked = check;
				
				document.getElementsByName("lcnt_dev_"+devs_ids[i])[0].checked = false;
				document.getElementsByName("lcnt_dev_"+devs_ids[i])[1].checked = false;
			}
		}
	}
	
	obje.checked = false;
}

//var globale:
var idgruppo = 0;

// x SubTab3:
function loadVars(obj,idgrp)
{
		var iddevice = obj.value;
		
		if (Number(iddevice) == -1)
		{
			fillGrpCombo(null,idgrp);
		}
		else
		{
			idgruppo = idgrp;
			CommSend("servlet/ajrefresh","POST","cmd=loadDigVars&iddevice="+iddevice+"","loadDigVars",true);
		}
}

// x SubTab3:
function Callback_loadDigVars()
{
	//variabili digitali del device x combo
	xml_vars = xmlResponse.getElementsByTagName("var");

	//creazione combo variabili digitali
	fillGrpCombo(xml_vars,idgruppo);
}

// x SubTab3:
function fillGrpCombo(xml_vars,idgroup)
{
	var combo="<select id='digvar_grp_"+idgroup+"' name='digvar_grp_"+idgroup+"'";
	combo = combo + " style='width:100%;'";
	combo = combo + " onchange='enable_stato("+idgroup+");'>\n <option value='-1'> -------------------- </option>\n";
	
	if ((xml_vars != null) && (xml_vars.length > 0))
	{
		var idvar = "";
		var descr = "";
		
		for (i = 0; i < xml_vars.length; i++)
		{
			idvar = xml_vars[i].getAttribute("id");
			descr = xml_vars[i].childNodes[0].nodeValue;

			combo = combo + "<option value='"+idvar+"' ";
			combo = combo + ">"+descr+"</option>\n";
		}
		
		combo = combo + "</select>\n";
	}
	
	document.getElementById("vars_grp_"+idgroup).innerHTML = combo;
	
	return;
}

// x SubTab4:
//function loadScheduling(obj)
function loadScheduling()
{
	/*
	var idgrp = obj.value;
	
	clearGrpCombo();
	
	var changeGrp = document.getElementById("changeGrp").value;
	
	if ((changeGrp == "false") || (confirm(document.getElementById("wantChangeDev").value)))
	{
		if (Number(idgrp) == -1)
		{
			showActive();
		}
		else
		{
			CommSend("servlet/ajrefresh","POST","cmd=loadScheduling&idgroup="+idgrp+"","loadScheduling",true);
		}
		
		document.getElementById("changeGrp").value = "false";
	}
	*/
	
	CommSend("servlet/ajrefresh","POST","cmd=loadScheduling","loadScheduling",true);
}

var hpos = 10000; //posizione ore

// x SubTab4:
function Callback_loadScheduling()
{
	//valori dello scheduling del gruppo:
	var xml_grps = xmlResponse.getElementsByTagName("group");
	
	if ((xml_grps != null) && (xml_grps.length > 0))
	{
	
		for (gr = 0; gr < xml_grps.length; gr++)
		{
			var schedgrp = xml_grps[gr].getAttribute("id");
			
			xml_sched = xml_grps[gr].getElementsByTagName("sched");
			document.getElementById("sched_grp"+schedgrp).checked = (xml_sched[0].getAttribute("id") == "on");
			
			//xml_days = xmlResponse.getElementsByTagName("day");
			xml_days = xml_grps[gr].getElementsByTagName("day");
			
			var colore = "";
			var n_fasce = 0;
			
			if ((xml_days != null) && (xml_days.length > 0))
			{
				var idday = "";
				var on_1 = "";
				var off_1 = "";
				var on_2 = "";
				var off_2 = "";
				
				for (i = 0; i < xml_days.length; i++)
				{
					n_fasce = 0;
					idday = xml_days[i].getAttribute("id");
					
					on_1 = xml_days[i].childNodes[0].nodeValue;
					if (on_1 != -1)
					{
					 	document.getElementById("g"+schedgrp+"_day"+idday+"_on1").selectedIndex = 1 + Math.round((on_1 / hpos) * 2);
					 	n_fasce = n_fasce + 1;
					}
					else document.getElementById("g"+schedgrp+"_day"+idday+"_on1").selectedIndex = 0;
					
					off_1 = xml_days[i].childNodes[1].nodeValue;
					if (off_1 != -1) document.getElementById("g"+schedgrp+"_day"+idday+"_off1").selectedIndex = 1 + Math.round((off_1 / hpos) * 2);
					else document.getElementById("g"+schedgrp+"_day"+idday+"_off1").selectedIndex = 0;
					
					on_2 = xml_days[i].childNodes[2].nodeValue;
					if (on_2 != -1)
					{
						document.getElementById("g"+schedgrp+"_day"+idday+"_on2").selectedIndex = 1 + Math.round((on_2 / hpos) * 2);
						n_fasce = n_fasce + 1;
					}
					else document.getElementById("g"+schedgrp+"_day"+idday+"_on2").selectedIndex = 0;
					
					off_2 = xml_days[i].childNodes[3].nodeValue;
					if (off_2 != -1) document.getElementById("g"+schedgrp+"_day"+idday+"_off2").selectedIndex = 1 + Math.round((off_2 / hpos) * 2);
					else document.getElementById("g"+schedgrp+"_day"+idday+"_off2").selectedIndex = 0;
					
					if (n_fasce == 1)
					{
						document.getElementById("g"+schedgrp+"_day"+idday).style.color = "BLUE";
					}
					else
					if (n_fasce == 2)
					{
						document.getElementById("g"+schedgrp+"_day"+idday).style.color = "BLACK";
					}
				}
			}
			
			/*
			//abilitazioni allo scheduler
			xml_sched = xmlResponse.getElementsByTagName("sched");
			
			//abilitazioni "force manual"
			xml_manual = xmlResponse.getElementsByTagName("manual");
			
			if ((xml_sched != null) && (xml_sched.length > 0))
			{
				document.getElementById("sched_grp").checked = (xml_sched[0].getAttribute("id") == "on");
				
				//
				//se è abilitata la modalità manuale, allora disabilito la schedulazione:
				document.getElementById("sched_grp").disabled = (xml_manual[0].getAttribute("id") == "on");
				//
			}
			*/
		}
	
	}
	
	//showActive();
}

// x SubTab4:
/*
function clearGrpCombo()
{
	for (i = 1; i <= 7; i++)
	{
		for (j = 1; j <= 2; j++)
		{
			document.getElementById("day"+i+"_on"+j).selectedIndex = 0;
			document.getElementById("day"+i+"_off"+j).selectedIndex = 0;
		}
		
		document.getElementById("day"+i).style.color = "RED";
	}
	
	document.getElementById("sched_grp").checked = false;
}
*/

function modGrp()
{
	document.getElementById("changeGrp").value = "true";
}

function clearMdlCmb()
{
	//var opzione = "<option value='-1'> -------------------- </option>\n";
	var opzione = "&nbsp;";
	
	//combo & checkbox x var di comando:
	document.getElementById("c_onoff").innerHTML = opzione;
	document.getElementById("cmd_onoff_inv").checked = false;
	document.getElementById("c_luci").innerHTML = opzione;
	document.getElementById("cmd_luci_inv").checked = false;
	document.getElementById("c_notte").innerHTML = opzione;
	document.getElementById("cmd_notte_inv").checked = false;
	
	//combo & checkbox x var di stato:
	document.getElementById("s_onoff").innerHTML = opzione;
	document.getElementById("st_onoff_inv").checked = false;
	document.getElementById("s_luci").innerHTML = opzione;
	document.getElementById("st_luci_inv").checked = false;
	document.getElementById("s_notte").innerHTML = opzione;
	document.getElementById("st_notte_inv").checked = false;
}

function modDevMdl()
{
	document.getElementById("changeDev").value = "true";
}

function loadMdlVars(obj)
{
		var iddevmdl = obj.value;
		var changeDev = document.getElementById("changeDev").value;
	
		if ((changeDev == "false") || (confirm(document.getElementById("wantChangeDev").value)))
		{
			if (Number(iddevmdl) == -1)
			{
				clearMdlCmb();
				disableAction(1);
			}
			else
			{
				CommSend("servlet/ajrefresh","POST","cmd=loadMdlVars&iddevmdl="+iddevmdl+"","loadMdlVars",true);
			}
			
			document.getElementById("changeDev").value = "false";
		}
}

// x SubTab6:
function Callback_loadMdlVars()
{
	//modelli variabili digitali del device x combo:
	xml_varmdl = xmlResponse.getElementsByTagName("varmdl");
	
	//modelli var. digitali scelti per il devmdl:
	xml_varmdldev = xmlResponse.getElementsByTagName("varmdldev");

	//creazione combo modelli variabili digitali
	fillVarCombo(xml_varmdl,xml_varmdldev);
}

var inv_logic = 1;
var dir_logic = 0;

// x SubTab6:
function fillVarCombo(xml_varmdl,xml_varmdldev)
{
	var sel_idx = 0;
	var check_inv = false;
	var varmdl = new Array();
	
	var combo_cmd = "";
	var combo_st = "";
	
	for (j = 0; j < 6; j++)
	{
		varmdl[j] = new Array();
		varmdl[j][0] = "";
		varmdl[j][1] = "";
//		varmdl[j][2] = "";
	}
	
	if ((xml_varmdldev != null) && (xml_varmdldev.length > 0))
	{
		var idvarmdl = "";
		var logica = 0;
		var tipo = 0;
		
		for (i = 0; i < xml_varmdldev.length; i++)
		{
			idvarmdl = xml_varmdldev[i].getAttribute("id");
			tipo = Number(xml_varmdldev[i].childNodes[0].nodeValue);
			logica = xml_varmdldev[i].childNodes[1].nodeValue;
			
			varmdl[tipo][0] = idvarmdl;
			varmdl[tipo][1] = logica;
//			varmdl[tipo][2] = 0;
		}
	}
	
	combo_cmd = "<option value='-1'> -------------------- </option>";
	combo_st = "<option value='-1'> -------------------- </option>";
	
	if ((xml_varmdl != null) && (xml_varmdl.length > 0))
	{
		var idvar = "";
		var descr = "";
		var readwrite = 0;
		
		for (i = 0; i < xml_varmdl.length; i++)
		{
			idvar = xml_varmdl[i].getAttribute("id");
			
//			for (j = 0; j < 6; j++)
//			{
//				if (idvar == varmdl[j][0])
//				{
//					varmdl[j][2] = i+1; //pos=0 x val='-1'
//				}
//			}
			
			descr = xml_varmdl[i].childNodes[0].nodeValue;
			readwrite = xml_varmdl[i].childNodes[1].nodeValue;

			if (readwrite != 1)
			{//vars di comando:
				combo_cmd = combo_cmd + "<option value='"+idvar+"' >"+descr+"</option>";
			}
			//else
			//{//vars di stato:
				combo_st = combo_st + "<option value='"+idvar+"' >"+descr+"</option>";
			//}
		}
	}
	
	//combo x var di comando:
	document.getElementById("c_onoff").innerHTML = "&nbsp;<select style='width:95%' id='cmd_onoff' name='cmd_onoff' onchange='modDevMdl();'>"+combo_cmd+"</select>";
//	document.getElementById("cmd_onoff").selectedIndex = Number(varmdl[0][2]);
	document.getElementById("cmd_onoff").selectedIndex = selectVarmdl(document.getElementById("cmd_onoff"), varmdl[0][0]);
	document.getElementById("cmd_onoff_inv").checked = (varmdl[0][1] == inv_logic);
	
	document.getElementById("c_luci").innerHTML = "&nbsp;<select style='width:95%' id='cmd_luci' name='cmd_luci' onchange='modDevMdl();'>"+combo_cmd+"</select>";
//	document.getElementById("cmd_luci").selectedIndex = Number(varmdl[1][2]);
	document.getElementById("cmd_luci").selectedIndex = selectVarmdl(document.getElementById("cmd_luci"), varmdl[1][0]);
	document.getElementById("cmd_luci_inv").checked = (varmdl[1][1] == inv_logic);
	
	document.getElementById("c_notte").innerHTML = "&nbsp;<select style='width:95%' id='cmd_notte' name='cmd_notte' onchange='modDevMdl();'>"+combo_cmd+"</select>";
//	document.getElementById("cmd_notte").selectedIndex = Number(varmdl[2][2]);
	document.getElementById("cmd_notte").selectedIndex = selectVarmdl(document.getElementById("cmd_notte"), varmdl[2][0]);
	document.getElementById("cmd_notte_inv").checked = (varmdl[2][1] == inv_logic);
	
	
	//combo x var di stato:
	document.getElementById("s_onoff").innerHTML = "&nbsp;<select style='width:95%' id='st_onoff' name='st_onoff' onchange='modDevMdl();'>"+combo_st+"</select>";
//	document.getElementById("st_onoff").selectedIndex = Number(varmdl[3][2]);
	document.getElementById("st_onoff").selectedIndex = selectVarmdl(document.getElementById("st_onoff"), varmdl[3][0]);
	document.getElementById("st_onoff_inv").checked = (varmdl[3][1] == inv_logic);
	
	document.getElementById("s_luci").innerHTML = "&nbsp;<select style='width:95%' id='st_luci' name='st_luci' onchange='modDevMdl();'>"+combo_st+"</select>";
//	document.getElementById("st_luci").selectedIndex = Number(varmdl[4][2]);
	document.getElementById("st_luci").selectedIndex = selectVarmdl(document.getElementById("st_luci"), varmdl[4][0]);
	document.getElementById("st_luci_inv").checked = (varmdl[4][1] == inv_logic);
	
	document.getElementById("s_notte").innerHTML = "&nbsp;<select style='width:95%' id='st_notte' name='st_notte' onchange='modDevMdl();'>"+combo_st+"</select>";
//	document.getElementById("st_notte").selectedIndex = Number(varmdl[5][2]);
	document.getElementById("st_notte").selectedIndex = selectVarmdl(document.getElementById("st_notte"), varmdl[5][0]);
	document.getElementById("st_notte_inv").checked = (varmdl[5][1] == inv_logic);
	
	enableAction(1);
	return;
}

function selectVarmdl(selectelement, idvarmdl) {
	for(var idx = 0;idx < selectelement.options.length;idx++) {
		if(selectelement.options[idx].value==idvarmdl) {
			return idx;
		}
	}
	return 0;
}

// x SubTab3:
function enable_stato(idgroup)
{
	var digvar = document.getElementById("digvar_grp_"+idgroup).value;
	if (digvar == -1)
	{
		document.getElementById("statoon_grp_"+idgroup).disabled = true;
	}
	else
	{
		document.getElementById("statoon_grp_"+idgroup).disabled = false;
	}
}

// x SubTab2:
function ctrl_na(iddev)
{
	var checked = false;
	var n_groups = document.getElementById("n_groups").value;
	
	for (j = 1; j <= n_groups; j++)
	{
		if (! checked)
			checked = (document.getElementById("dev_"+iddev+"__grp_"+j).checked == true);
	}
	
	if (! checked)
		document.getElementById("dev_"+iddev+"__grp_na").checked = true;
}

// x SubTab4:
function go2DaysException()
{
	top.frames['manager'].loadTrx('nop&folder=lcnt_days&bo=BLcNt_Days&type=click&desc=GroupDetail');
}

// x SubTab1:
function go2GrpDtl(idgrp)
{
	var linkname = document.getElementById("linkname").value;
	
	top.frames['manager'].loadTrx('nop&idgrp='+idgrp+'&folder=lcnt_grpdtl&bo=BLcNt_GrpDtl&type=click&desc='+linkname);
}

// x SubTab4:
function setTimeFormat(obj)
{
	MTstartServerComm();
	
	var itopass = obj.value;
	
	// necessario x far apparire il div container:
	setTimeout("setTimeFormatDelaied("+itopass+")",50);
}

// x SubTab4:
function setTimeFormatDelaied(valore)
{
	if (valore == 0)
	{
		AP_to_h24();
		//timeformat = timef;
	}
	else
	if (valore == 1)
	{
		h24_to_AP();
		//timeformat = timef;
	}
	
	MTstopServerComm();
}

// x SubTab4:
function cmbH24_AP(obj)
{
	var k = 1;
	var decimal = "";
	
	for (i = 0; i < 12; i++)
	{
       if (i < 10)
			decimal = "0";
		else
			decimal = "";
       
       obj.options[k].text = ""+decimal+i+".00a";
       k++;
       obj.options[k].text = ""+decimal+i+".30a";
       k++
	}
   	
   	obj.options[k].text = "12.00a";
   	k++;
   	obj.options[k].text = "00.30p";
   	k++;
   	
   	for (j = 1; j < 12; j++)
   	{
       if (j < 10)
			decimal = "0";
		else
			decimal = "";
       
       obj.options[k].text = ""+decimal+j+".00p";
       k++;
       obj.options[k].text = ""+decimal+j+".30p";
       k++;
   	}
   	
   	obj.options[k].text = "12.00p";
}

// x SubTab4:
function cmbAP_H24(obj)
{
	var k = 1;
	var decimal = "";
	
	for (i = 0; i < 24; i++)
	{
		if (i < 10)
			decimal = "0";
		else
			decimal = "";
		
       	obj.options[k].text = ""+decimal+i+".00";
       	k++;
       	obj.options[k].text = ""+decimal+i+".30";
       	k++
	}
   	
   	obj.options[k].text = "24.00";
}

// x SubTab4:
function h24_to_AP()
{
	//var newcmbAP = comboAPHours();
	var indice = -1;
	
	var maxgrps = Number(document.getElementById("maxgroups").value);
	
	for (grp = 1; grp <= maxgrps; grp++)
	{
	
		for (g = 1; g <= 7; g++)
		{
			for (fon = 1; fon <= 2; fon++)
			{
				indice = -1;
				indice = document.getElementById("g"+grp+"_day"+g+"_on"+fon).selectedIndex;
				//document.getElementById("day"+g+"_on"+fon).innerHTML = newcmbAP;
				cmbH24_AP(document.getElementById("g"+grp+"_day"+g+"_on"+fon));
				document.getElementById("g"+grp+"_day"+g+"_on"+fon).selectedIndex = indice;
			}
			
			for (foff = 1; foff <= 2; foff++)
			{
				indice = -1;
				indice = document.getElementById("g"+grp+"_day"+g+"_off"+foff).selectedIndex;
				//document.getElementById("day"+g+"_off"+foff).innerHTML = newcmbAP;
				cmbH24_AP(document.getElementById("g"+grp+"_day"+g+"_off"+foff));
				document.getElementById("g"+grp+"_day"+g+"_off"+foff).selectedIndex = indice;
			}
		}
		
		//document.getElementById("timeformat"+grp).selectedIndex = 1;
		document.getElementById("timetype").value = 1;
	}
}

// x SubTab4:
function AP_to_h24()
{
	//var newcmb24h = combo24Hours();
	var indice = -1;
	
	var maxgrps = Number(document.getElementById("maxgroups").value);
	
	for (grp = 1; grp <= maxgrps; grp++)
	{
	
		for (g = 1; g <= 7; g++)
		{
			for (fon = 1; fon <= 2; fon++)
			{
				indice = -1;
				indice = document.getElementById("g"+grp+"_day"+g+"_on"+fon).selectedIndex;
				//document.getElementById("day"+g+"_on"+fon).innerHTML = newcmb24h;
				cmbAP_H24(document.getElementById("g"+grp+"_day"+g+"_on"+fon));
				document.getElementById("g"+grp+"_day"+g+"_on"+fon).selectedIndex = indice;
			}
			
			for (foff = 1; foff <= 2; foff++)
			{
				indice = -1;
				indice = document.getElementById("g"+grp+"_day"+g+"_off"+foff).selectedIndex;
				//document.getElementById("day"+g+"_off"+foff).innerHTML = newcmb24h;
				cmbAP_H24(document.getElementById("g"+grp+"_day"+g+"_off"+foff));
				document.getElementById("g"+grp+"_day"+g+"_off"+foff).selectedIndex = indice;
			}
		}
		
		//document.getElementById("timeformat"+grp).selectedIndex = 0;
		document.getElementById("timetype").value = 0;
	}
}
