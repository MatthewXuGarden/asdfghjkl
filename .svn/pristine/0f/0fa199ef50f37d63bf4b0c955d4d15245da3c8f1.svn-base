var row_class = -1;
var exp_id="";

function checkExport(obj1,obj2)
{
	var o1=document.getElementById(obj1);
	var o2=document.getElementById(obj2);
	if(o1.checked==true||o2.checked==true){
		if(o1.checked==true) exp_id="A";
		if(o2.checked==true) exp_id="E";
		if(o1.checked==true&&o2.checked==true) exp_id="AE";
		enableAction(1);
		enableAction(2);
		enableAction(3);
	}else{
		disableAction(1);
		disableAction(2);
		disableAction(3);
	}
}

function goToAlarmOrEvent(param,data)
{	
	var id = param;//.split("_")[1];
	if (data==1)
		top.frames['manager'].loadTrx('nop&folder=alrview&bo=BAlrView&type=click&id='+id+'&desc=ncode07');
	else
		top.frames['manager'].loadTrx('nop&folder=evndtl&bo=BEvnDtl&type=click&id='+id+'&desc=ncode09');
}

function exportResultSel(){
	exportResultSel('csv');
}

function exportResultSel(format)
{
	var alarm = document.getElementById("alarm_chk").checked;
	var event = document.getElementById("event_chk").checked;
	MTstartServerComm();
	CommSend("servlet/ajrefresh", "POST", "alarm_chk="+alarm+"&event_chk="+event+"&format="+format,"exportEVSEARCH",true);
}

function Callback_exportEVSEARCH()
{
	if (xmlResponse!=null && xmlResponse.getElementsByTagName("file")[0] != null)
	{
		var filename = xmlResponse.getElementsByTagName("file")[0].childNodes[0].nodeValue;
	
		var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
		window.open(sUrl);
		MTstopServerComm();
	}
}

function searchal_fdSaveFile()
{
	fdSaveFile('','pdf',saveALpdffile);
	var date = new Date();
	fdSetFile("Alarms_"+date.format("yyyyMMddhhmmss"));
}
function saveALpdffile(local, path, filename){
	MTstartServerComm();
	CommSend("servlet/ajrefresh", "POST", "cmd=exportpdf&format=pdf&path="+path+"&filename="+filename+"&local="+local,"exportFile("+local+")",true);
}

function searchev_fdSaveFile()
{
	fdSaveFile('','pdf',saveEVpdffile);
	var date = new Date();
	fdSetFile("Events_"+date.format("yyyyMMddhhmmss"));
}
function saveEVpdffile(local, path, filename){
	MTstartServerComm();
	CommSend("servlet/ajrefresh", "POST", "cmd=exportpdf&format=pdf&path="+path+"&filename="+filename+"&local="+local,"exportFile("+local+")",true);
}

function searchalev_pdf_fdSaveFile()
{
	fdSaveFile('','pdf',savepdffile);
	var date = new Date();
	fdSetFile("Alarmevents_"+date.format("yyyyMMddhhmmss"));
}
function savepdffile(local, path, filename)
{
	var alarm = document.getElementById("alarm_chk").checked;
	var event = document.getElementById("event_chk").checked;
	MTstartServerComm();
	CommSend("servlet/ajrefresh", "POST", "cmd=exportpdf&alarm_chk="+alarm+"&event_chk="+event+"&format=pdf&path="+path+"&filename="+filename+"&local="+local,"exportFile("+local+")",true);
}

function searchalev_csv_fdSaveFile()
{
	fdSaveFile('','csv',savecsvfile);
	var date = new Date();
	fdSetFile("Alarmevents_"+date.format("yyyyMMddhhmmss"));
}
function savecsvfile(local, path, filename)
{
	var alarm = document.getElementById("alarm_chk").checked;
	var event = document.getElementById("event_chk").checked;
	MTstartServerComm();
	CommSend("servlet/ajrefresh", "POST", "cmd=exportpdf&alarm_chk="+alarm+"&event_chk="+event+"&format=csv&path="+path+"&filename="+filename+"&local="+local,"exportFile("+local+")",true);
}

function Callback_exportFile(local)
{
	MTstopServerComm();
	if(xmlResponse!=null && xmlResponse.getElementsByTagName("limitSerach")[0].childNodes[0].nodeValue == "ko")
		alert(document.getElementById("s_overlimit").value);
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

function check_limit()
{
	var limit = document.getElementById("limit_search").value;
	if (limit=="ko")
	{
		disableAction(1);
		disableAction(2);
		alert(document.getElementById("s_overlimit").value);
	}
}