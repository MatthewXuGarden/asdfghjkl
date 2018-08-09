var ACTION_EXPORT = 1;
var ACTION_COPY = 2;

var address = "";
var code = "";
function initialize()
{
	var scr_h = document.getElementById("scr_h").value;
	var loadxmlerror = document.getElementById("loadxmlerror").value;
	/* in case of import error disable the Copy action and the checkboxes */
	if(loadxmlerror != "")
	{
		alert(loadxmlerror);
	}
	var propagated = document.getElementById("propagated").value;
	if (propagated != "")
	{
		alert(document.getElementById("propagated").value);
	}
	var importdevmdlcode = document.getElementById("importdevmdlcode").value;
	/* only if a device code related to the imported one exists and there's no error on import, load the slaves table */
	if((importdevmdlcode != "") && (loadxmlerror == ""))
	{
		reload_son_importdevice();
	}
	enableFunction(true);
	
	if(importdevmdlcode == "")
	{
		document.getElementById("combsource").checked = true;
		combsourceclick();
	}
	else
	{
		document.getElementById("filesource").checked = true;
		filesourceclick();
	}
}


// caricamento tabella dispositivi slaves
function reload_son()
{
	document.getElementById('s_all').checked=false;
	var id_master = document.getElementById("id_master2").value;
	document.getElementById("id_master").value = id_master;
	if(id_master == "-1")
	{
		disableAction(ACTION_EXPORT);
	}
	else
	{
		enableAction(ACTION_EXPORT);
	}
	CommSend("servlet/ajrefresh","POST","cmd=load_slaves&id_master="+id_master+"","load_slaves",true);	
}


function reload_son_importdevice()
{
	document.getElementById('s_all').checked=false;
	var importdevmdlcode = document.getElementById("importdevmdlcode").value;
	CommSend("servlet/ajrefresh","POST","cmd=load_slaves_importdevice&devmdlcode="+importdevmdlcode+"","load_slaves",true);	
}


function Callback_load_slaves()
{
	var xmlFileInfo = xmlResponse.getElementsByTagName("fileInfo");
	if(xmlFileInfo.length>0)
	{
		address = xmlFileInfo[0].getAttribute("addr");
		code = xmlFileInfo[0].getAttribute("code");
	}
	var xmlDevice = xmlResponse.getElementsByTagName("device");
	var html = "<table id='body_table' class='table' width='100%' cellspacing='1'>";
	var ids= "";
	if (xmlDevice.length>0)
	{
		for (i=0;i<xmlDevice.length;i++)
		{
			var id = String(xmlDevice[i].getAttribute("id"));
			var desc = xmlDevice[i].childNodes[0].nodeValue;
			var line = xmlDevice[i].childNodes[1].nodeValue;
			html = html + "<tr class='Row1'>";
			html = html + "<td class='standardTxt' width='80%' align='left'>" + desc + "</td>";
			html = html + "<td class='standardTxt' width='10%' align='center'>"+line+"</td>";
			html = html + "<td width='10%' align='center'><input onclick='check_dev(this)' type='checkbox' class='standardTxt' id='ch_"+id+"' name='ch_"+id+"'></td>";
			html = html + "</tr>";
			if( i > 0 )
				ids += ";";
			ids += id;
		}
	}
	html = html + "</table>";
	document.getElementById('div_devices').innerHTML = html;
	document.getElementById('ids_slaves').value = ids;
	if (ids!="")
	{
		document.getElementById('s_all').disabled=false;
		enableFunction(false);
	}
	else
	{
		document.getElementById('s_all').disabled=true;
		enableFunction(true);
	}

	disableActionCopy();
}


function checklanguage(obj)
{
	var importdevmdlcode = document.getElementById("importdevmdlcode").value;
	if(importdevmdlcode != "")
	{
		var checked = obj.checked;
		if(checked == false)
		{
			var langexist = document.getElementById("langexist").value;
			if(langexist == "false")
			{
				var langnotexist = document.getElementById("langnotexist").value;
				alert(langnotexist);
			}
		}
	}
}


function selectAll(obj)
{
	var checked = obj.checked;
	var ids = document.getElementById('ids_slaves').value.split(";");
	for (i=0;i<ids.length;i++)
	{
		document.getElementById("ch_"+ids[i]).checked=checked;
	}
	
	if( checked && document.getElementById("permission").value != 1 )
		enableAction(ACTION_COPY);
	else
		disableActionCopy();
}


function check_dev(obj)
{
	var checked = obj.checked;
	if (!checked)
	{
		document.getElementById('s_all').checked=checked;
		disableActionCopy();
	}
	else
	{
		var all_checked = true;
		var ids = document.getElementById('ids_slaves').value.split(";");
		for (i=0;i<ids.length;i++)
		{
			if (!document.getElementById("ch_"+ids[i]).checked)
			{
				all_checked = false;
				break;
			}
		}
		if (all_checked)
		{
			document.getElementById('s_all').checked=true;
		}
		
		if( document.getElementById("permission").value != 1 )
			enableAction(ACTION_COPY);
	}
}


//abilitazione e disabilitazione funzionalit? 
function enableFunction(disabled)
{
	if (disabled)
	{
		document.getElementById("alr").checked=false;
		document.getElementById("descr").checked=false;
		document.getElementById("um").checked=false;
		document.getElementById("haccp").checked=false;
		document.getElementById("hist").checked=false;
		document.getElementById("prior").checked=false;
		document.getElementById("graphconf").checked=false;
		document.getElementById("images").checked=false;
	}
	document.getElementById("alr").disabled=disabled;
	document.getElementById("descr").disabled=disabled;
	document.getElementById("um").disabled=disabled;
	document.getElementById("haccp").disabled=disabled;
	document.getElementById("hist").disabled=disabled;
	document.getElementById("prior").disabled=disabled;
	document.getElementById("graphconf").disabled=disabled;
	document.getElementById("images").disabled=disabled;
}


//BOTTONE PROPAGAZIONE
function fn_propagate()
{
	var maxslaves = 50;
	
	var alr= document.getElementById("alr").checked;
	var descr= document.getElementById("descr").checked;
	var um= document.getElementById("um").checked;
	var haccp= document.getElementById("haccp").checked;
	var hist= document.getElementById("hist").checked;
	var prior= document.getElementById("prior").checked;
	var graphconf= document.getElementById("graphconf").checked;
	var images= document.getElementById("images").checked;
	
	var maxselect = document.getElementById("maxselectstr").value;
	
	var ids = document.getElementById("ids_slaves").value.split(";");
	var selected = false;
	var selectedvars = 0;
	
	try
	{
		for (i=0;i<ids.length;i++)
		{
			if (document.getElementById("ch_"+ids[i]).checked)
			{
				selectedvars++;
			}
		}
	}
	catch(e){}
	
	if(selectedvars > 0)
		selected = true;
	
	if(selectedvars > maxslaves)
	{
		maxselect = maxselect.replace("$1",maxslaves);
		maxselect = maxselect.replace("$2",selectedvars);
		alert(maxselect);
		return;
	}
	var importdevmdlcode = document.getElementById("importdevmdlcode").value;
	var id_master = document.getElementById("id_master2");
	if(importdevmdlcode != "" && id_master.selectedIndex == 0)
	{
		if(!selected)
		{
			alert(document.getElementById("selectslave").value);
			return;
		}
		document.getElementById("cmd").value="propagate_importdevice";
		var graphchecked = document.getElementById("graphconf").checked;
		var sameprofcode = document.getElementById("sameprofcode").value;
		var adminprofileupdate = document.getElementById("adminprofileupdate").value;
		var confirmsameprofilecodeupdate = document.getElementById("confirmsameprofilecodeupdate").value;
		var profiles = sameprofcode.split("^^^");
		for(var i=0;i<profiles.length;i++)
		{
			confirmsameprofilecodeupdate += "\r"+profiles[i];
		}
		if(graphchecked == true)
		{
			if(sameprofcode == "")
			{
				alert(adminprofileupdate);
			}
			else 
			{
				var message = adminprofileupdate+"\r\r"+confirmsameprofilecodeupdate;
				if(confirm(message))
				{
					document.getElementById("updatesameprofcode").value = "true";
				}
				else
				{
					document.getElementById("updatesameprofcode").value = "false";
				}
			}
		}
	}
	//select device but without copy type
	if(selected && !alr&&!descr&&!haccp&&!hist&&!prior&&!graphconf&&!um&&!images)
	{
		alert(document.getElementById("selectpropag").value);
		return false;
	}
	if (!selected)
	{
		//alert(document.getElementById("noslaves").value);
		//return false;
		document.getElementById("cmd").value="propagate_plus";
	}
	
	if (!alr&&!descr&&!haccp&&!hist&&!prior&&!graphconf&&!um&&!images)
	{
		//alert(document.getElementById("selectpropag").value);
		//return false;
		document.getElementById("cmd").value="propagate_plus";
	}
	else
	{
		if(!selected)
		{
			alert(document.getElementById("selectslave").value);
			return;
		}
	}
	var oFrm = document.getElementById("frm_propagate");
	if (oFrm!=null) {
		MTstartServerComm();
		oFrm.submit();
	}
}


/**
 *	Gestione propagazione configurazione grafico su N profili
 */
function loadTbProf(obj)
{
		document.getElementById("p_all").checked = false;
		var prSel = document.getElementById("prolist");
		var curSel = prSel.value;
		var ids = "";
		var html = "";
		if(curSel != -9999)
		{
				html = "<table id='body_table' class='table' width='100%' cellspacing='1'>";
					
			for (i=0;i<prSel.options.length;i++)
			{
					if((prSel.options[i].value != -9999) && (prSel.options[i].value != curSel))
					{
						ids += prSel.options[i].value + ";"; 
						html = html + "<tr class='Row1'>";
						var getval = "";
						if (prSel.options[i] != null && 'innerText' in prSel.options[i])
							getval = prSel.options[i].innerText;
						else
							getval = prSel.options[i].textContent;
						html = html + "<td class='standardTxt' width='80%' align='left'>" + getval + "</td>";
						html = html + "<td width='10%' align='center'><input onclick='check_prof(this);' type='checkbox' class='standardTxt' id='chp_"+prSel.options[i].value+"' name='chp_"+prSel.options[i].value+"'></td>";
						html = html + "</tr>";
					}
			}
			html = html + "</table>";
		}
		document.getElementById('div_profiles').innerHTML = html;
		
		if(ids != "")
		{
			ids = ids.substring(0,ids.length-1);
			document.getElementById("p_all").disabled=false;
		}
		else
			document.getElementById("p_all").disabled=true;
		
		document.getElementById("ids_profiles").value = ids;
		
		disableActionCopy();
}


function check_prof(obj)
{
	var checked = obj.checked;
	
	if (!checked) {
		document.getElementById('p_all').checked=checked;
		disableActionCopy();
	}
	else
	{
		var all_checked = true;
		var ids = document.getElementById('ids_profiles').value.split(";");
		for (i=0;i<ids.length;i++)
		{
			if (!document.getElementById("chp_"+ids[i]).checked)
			{
				all_checked = false;
				break;
			}
		}
		
		if (all_checked)
			document.getElementById('p_all').checked=true;
		
		if( document.getElementById("permission").value != 1 )
			enableAction(ACTION_COPY);
	}
}


function selectAllP(obj)
{
	var checked = obj.checked;
	var ids = document.getElementById('ids_profiles').value.split(";");
	for (i=0;i<ids.length;i++)
		document.getElementById("chp_"+ids[i]).checked=checked;
	
	if( checked && document.getElementById("permission").value != 1 )
		enableAction(ACTION_COPY);
	else
		disableActionCopy();
}

function propagate_fdSaveFile()
{
	fdSaveFile('','dcfg',devexp_savefile);
	var date = new Date();
	fdSetFile("Config_"+code+"_"+address+"_"+date.format("yyyyMMddhhmmss"));
}
function devexp_savefile(local,path,filename)
{
	var id_master = document.getElementById("id_master2");
	if(id_master.selectedIndex == 0)
	{
		alert(document.getElementById("selectmaster").value);
		return;
	}
	path += ".DCFG";
	filename += ".DCFG";
	MTstartServerComm();
	CommSend("servlet/ajrefresh", "POST", "cmd=export&id_master="+id_master.value+"&path="+path+"&filename="+filename+"&local="+local,"devexp_back("+local+")",true);
}


function Callback_devexp_back(local)
{
	MTstopServerComm();
	if (xmlResponse!=null && xmlResponse.getElementsByTagName("file")[0] != null)
	{
		var filename = xmlResponse.getElementsByTagName("file")[0].childNodes[0].nodeValue;
		var msg = "";
		if(filename == "ERROR")
		{
			msg = document.getElementById("save_error").value;
			alert(msg);
		}
		else
		{
			if(local == true)
			{
					msg = document.getElementById("save_confirm").value;
					alert(msg + filename);
			}
			else
			{
				var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
				window.open(sUrl);
			}
		}
	}
}


function importdevice(inputfile,button)
{
	var obj = document.getElementById(button);
	var img = obj.src.split("/");
	var tmp = img[img.length-1].split("_")[1];
	var is_on = tmp.split(".")[0];
	if (is_on=="on")
	{
		var oFrm =document.getElementById("deviceuploadform");
		if(oFrm != null)
		{
			MTstartServerComm();
			oFrm.submit();
		}
	}
}


function combsourceclick()
{
	combsourcechecked(true);
	if( document.getElementById("id_master").value != "-1" )
		enableAction(ACTION_EXPORT);
}


function filesourceclick()
{
	combsourcechecked(false);
	disableAction(ACTION_EXPORT);
}


function combsourcechecked(checked)
{
	document.getElementById("combsource").checked = checked;
	document.getElementById("filesource").checked = !checked;
	
	document.getElementById("id_master2").disabled = !checked;
	fileDialogEnable('importconf',!checked,true,'dcfg');
}


function disableActionCopy()
{
	var ids_slaves = document.getElementById('ids_slaves').value;
	var ids = ids_slaves.length > 0 ? ids_slaves.split(";") : new Array();
	for(var i=0; i<ids.length; i++)
		if( document.getElementById("ch_"+ids[i]).checked )
			return;
	var ids_profiles = document.getElementById('ids_profiles').value;
	var ids = ids_profiles.length > 0 ? ids_profiles.split(";") : new Array();
	for(var i=0; i<ids.length; i++)
		if( document.getElementById("chp_"+ids[i]).checked )
			return;
	disableAction(ACTION_COPY);
}
