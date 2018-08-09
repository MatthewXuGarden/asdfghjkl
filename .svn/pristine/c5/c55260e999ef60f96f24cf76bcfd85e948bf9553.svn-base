function changereporttype(obj){
	var combos = obj.parentNode.parentNode.getElementsByTagName("select");
	document.getElementById("repgrplistgrp").disabled="disabled";
	document.getElementById("repconslistcons").disabled="disabled";
	if(obj.checked){
		for(var k=0;k<combos.length;k++){
			combos[k].disabled="";
		}
	}
	document.getElementById("sceltaGranularita").disabled = obj.value == "coldrental" ? "disabled" : ""; 
}

function energyexportreport(){
	document.getElementById("energyexportreport").submit();
//	alert('Export report');
}

function initEnergyReport()
{
	// Check plg rgt
	if(!checkEnergyRegistered() || !checkEnergyConfigured())
	{
		disableAction(1);
		return;
	}
	else {
		enableAction(1);		
	}
}

function energy_fdSaveFile()
{
	fdSaveFile('','csv',energy_savefile);
	var date = new Date();
	fdSetFile("Energy_"+date.format("yyyyMMddhhmmss"));
}
function energy_savefile(local,path,filename)
{
	path += ".csv";
	var param = "cmd=energyexportreport&path="+path+"&filename="+filename+"&local="+local;
	var energyfrom = document.getElementById("energyfrom").value;
	param += "&energyfrom="+energyfrom;
	var energyto = document.getElementById("energyto").value;
	param += "&energyto="+energyto;
	var sceltaGranularita = document.getElementById("sceltaGranularita").value;
	param += "&sceltaGranularita="+sceltaGranularita;
	var exporttype = "";
	var l=document.getElementsByName("exporttype")  
	for(var i=0;i<l.length;i++)  
	{  
		if(l[i].checked)
		{
			exporttype = l[i].value;
			break;
		}
	}
	param += "&exporttype="+exporttype;
	var group = document.getElementById("repgrplistgrp").value;
	param += "&group="+group;
	var cons = document.getElementById("repconslistcons").value;
	param += "&consvar="+cons;
	CommSend("servlet/ajrefresh", "POST",param ,"energyexport_back("+local+")",true);
}
function Callback_energyexport_back(local)
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