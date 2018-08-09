// device.js



function onSelectPage(page)
{
	var whereURL = page;
	var param = "";
	if(page.indexOf("?")>0)
	{
		whereURL = page.substring(0,page.indexOf("?"));
		param = page.substring(page.indexOf("?"),page.length);
	}
	top.frames['body'].frames['bodytab'].location.href = top.frames['manager'].getDocumentBase()
		+ "mobile/" + whereURL + ";"
		+ top.frames['manager'].getSessionId()+param;
}


function onSelectVarTable(vt)
{
	top.frames['body'].frames['bodytab'].location.href = top.frames['manager'].getDocumentBase()
		+ "mobile/DeviceMain.jsp;"
		+ top.frames['manager'].getSessionId()
		+ "?vt=" + vt;
}


function onLoadDeviceParameters()
{
	unlockModUser();

	var select = document.getElementById("selectParamTable");
	if( select.options.length >= 2 ) {
		onSelectParamTable(select.options[1].value);
		select.options[1].selected = true;
	}
	else if( select.options.length == 1 ) {
		onSelectParamTable(select.options[0].value);
	}
}


function onSelectParamTable(pt)
{
	if( checkModUser() ) {
		load_params(pt);
		document.getElementById("pt").value = pt;
	}
}


function onSelectAlarmTable(at)
{
	if( at == 1 ) {
		document.getElementById("divActiveAlarms").style.display = "none";
		document.getElementById("divResetAlarms").style.display = "block";
	}
	else {
		document.getElementById("divActiveAlarms").style.display = "block";
		document.getElementById("divResetAlarms").style.display = "none";
	}
}
