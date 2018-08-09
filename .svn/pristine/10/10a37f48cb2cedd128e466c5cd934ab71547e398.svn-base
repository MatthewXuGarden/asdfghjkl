
function kpiover(obj) {
	obj.style.backgroundColor = "#bbbbbb";
	obj.style.borderColor = "#ffffff";
}
function kpiout(obj) {
	obj.style.backgroundColor = "#ffffff";
	obj.style.borderColor = "#000000";
}
function openresult(grp, grpdesc) {
	top.frames["manager"].loadTrx("nop&folder=kpiresult&bo=BKpiResult&type=click&desc=" + document.getElementById("topdesc").value + "&kpigrp=" + grp + "&kpigrpdesc=" + grpdesc);
}
function groupnamecheck()
{
	var virtkey = document.getElementById("virtkeyboard").value;
	if(virtkey == "on")
	{
		var objs = document.getElementsByTagName("input");
		var texts = new Array();
		var j=0;
		for (var i = 0; i < objs.length; i++) 
		{
			if(objs[i].type != "text")
			{
				continue;
			}
			texts[j++] = objs[i];
		}
		for(j=0;j<texts.length-1;j++)
		{
			var text1 = texts[j];
			if(text1.value == "")
			{
				continue;
			}
			for(i=j+1;i<texts.length;i++)
			{
				var text2 = texts[i];
				if(text2.value == "")
				{
					continue;
				}
				if(text1.value == text2.value)
				{
					alert(document.getElementById("kpinoduplicates").value);
					return false;
				}
			}
		}
	}
	return true;
}
function kpinextpage() 
{
	document.getElementById("archtabtoload").value = "1";
	save_kpi1();
}
function save_kpi1() 
{
	if(groupnamecheck() == false)
	{
		document.getElementById("archtabtoload").value = "2";
		return;
	}
	var oFrm = document.getElementById("kpi_page2_form");
	if (oFrm != null) {
		try {
			var fields = document.getElementsByName("kpiiddev");
			for (var i = 0; i < fields.length; i++) {
				var radios = document.getElementsByName("gr_" + fields[i].value);
				for (var j = 0; j < radios.length - 1; j++) {
					if (radios[j].checked) {
						document.getElementById("kpicheckbox" + fields[i].value).value = j + 1;
					}
				}
			}
		}
		catch (err) {
			//alert(err);
		}
		MTstartServerComm();
		oFrm.submit();
	}
}
function verifyGroup(obj) {
	if (obj.value != null) {
		if (obj.value == "") {
			var idx = obj.id.substr(2);
			var rr = document.getElementById("kpidevicetable").tBodies[0].rows;
			for (var i = 0; i < rr.length; i++) {
				var cc = rr[i].cells[idx];
				cc.getElementsByTagName("input")[0].checked = false;
				cc.getElementsByTagName("input")[0].disabled = true;
			}
			document.getElementById("radio"+obj.id).disabled = true;
		}
		if (obj.value != "") {
			var idx = obj.id.substr(2);
			var rr = document.getElementById("kpidevicetable").tBodies[0].rows;
			for (var i = 0; i < rr.length; i++) {
				var cc = rr[i].cells[idx];
				cc.getElementsByTagName("input")[0].disabled = false;
			}
			document.getElementById("radio"+obj.id).disabled = false;
		}
	}
}
function disablegroups() {
	if (document.getElementById("gr1") != null) {
		verifyGroup(document.getElementById("gr1"));
	}
	if (document.getElementById("gr2") != null) {
		verifyGroup(document.getElementById("gr2"));
	}
	if (document.getElementById("gr3") != null) {
		verifyGroup(document.getElementById("gr3"));
	}
	if (document.getElementById("gr4") != null) {
		verifyGroup(document.getElementById("gr4"));
	}
	if (document.getElementById("gr5") != null) {
		verifyGroup(document.getElementById("gr5"));
	}
	if (document.getElementById("gr6") != null) {
		verifyGroup(document.getElementById("gr6"));
	}
	if (document.getElementById("gr7") != null) {
		verifyGroup(document.getElementById("gr7"));
	}
	if (document.getElementById("gr8") != null) {
		verifyGroup(document.getElementById("gr8"));
	}
	if (document.getElementById("gr9") != null) {
		verifyGroup(document.getElementById("gr9"));
	}
	if (document.getElementById("gr10") != null) {
		verifyGroup(document.getElementById("gr10"));
	}
}
function Callback_fillfields() {
	try {
	//alert("xmlResponse..n.."+xmlResponse.xml);//xmlResponse.getElementsByTagName('group').length);
		if (xmlResponse.xml != "") {
			var tmpnode = xmlResponse.getElementsByTagName("group");
			if (tmpnode == null || tmpnode.length == 0) {
				alert(document.getElementById("errmessage").value);
				return;
			}
			tmpnode = xmlResponse.getElementsByTagName("models");
			if (tmpnode == null || tmpnode.length == 0) {
				alert(document.getElementById("errmessage").value);
				return;
			}
			var devs = xmlResponse.getElementsByTagName("device");
		}
	}
	catch (err) {
		alert(err);
	}
}
function blankfields() {
	alert("sbianca");
}
function save_kpi2() {
	if (!verifyfields()) {
		return;
	}
	var oFrm = document.getElementById("kpi_page3_form");
	if (oFrm != null) {
		MTstartServerComm();
		oFrm.submit();
	}
}
function checkName(obj) 
{
//kevin comment all
//	var objs = document.getElementsByTagName("input");
//	if (obj.value == "") {
//		return;
//	}
//	for (var i = 0; i < objs.length; i++) {
//		//2010-1-11, add by Kevin
//		//only need to check text input
//		if(objs[i].type != "text")
//		{
//			continue;
//		}
//		if (obj != objs[i] && obj.value == objs[i].value) {
//			alert(document.getElementById("kpinoduplicates").value);
//		}
//	}
	if (obj.value == "") 
		return;
	for(var i=1;i<=10;i++)
	{
		var grx = document.getElementById("gr"+i);
		if(grx.type != "text")
		{
			continue;
		}
		if (obj != grx && obj.value == grx.value) {
			alert(document.getElementById("kpinoduplicates").value);
			break;
		}
	}
}
function verifyfields() {
	var tmp = document.getElementsByTagName("input");
	var min = 0;
	var max = 0;
	for (var i = 0; i < tmp.length; i++) {
		
		if ((tmp[i].id.substring(0, 4) == "minp" || tmp[i].id.substring(0, 4) == "maxp") && (tmp[i].value < 0 || tmp[i].value > 100)) {
			alert(document.getElementById("percentageerror").value);
			return false;
		}
		
		if (tmp[i].id.substring(0, 4) == "min_"){
			min = parseInt(tmp[i].value);
		}
		if (tmp[i].id.substring(0, 4) == "max_"){
			max = parseInt(tmp[i].value);
		
			if( min > max )
			{
				alert(document.getElementById("minmaxerror").value);
				return false;
			}
		}
	
	}
	return true;
}

function all2gr(idgr)
{
	var idsdevs = "";
	idsdevs = document.getElementById("idsdevs").value;
	
	var ids = idsdevs.split(";");
	
	if (ids.length > 0)
	{
		if(document.getElementById("radiogr"+idgr).checked==false)
		{
			if (confirm(document.getElementById("selall2gr").value+" "+idgr+"?"))
			{
				for (i = 0; i < ids.length; i++)
				{
					document.getElementById("gr"+idgr+"_"+ids[i]).checked = "checked";
				}
				document.getElementById("radiogr"+idgr).checked=true;
			}
		} else {
			if (confirm(document.getElementById("deselall2gr").value+" "+idgr+"?"))
			{
				for (i = 0; i < ids.length; i++)
				{
					document.getElementById("gr"+idgr+"_"+ids[i]).checked = "";
				}
				document.getElementById("radiogr"+idgr).checked=false;
			}
		}
	}
}