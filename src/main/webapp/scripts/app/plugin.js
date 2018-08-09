var currentPlugin = new Array(12);

function pluginOnLoad()
{
	disableActionPlugin();
}

function loadPluginInfo(code)
{
	disableActionPlugin();
	if (code == "----------"){
			var tablecells = document.getElementById('detailTable').getElementsByTagName('td');
		//tablecells[2].innerHTML = "";
		tablecells[4].innerHTML = "";
		tablecells[6].innerHTML = "";
		tablecells[8].getElementsByTagName('input')[0].value = "";
		tablecells[10].getElementsByTagName('input')[0].value = "";
		tablecells[12].innerHTML = "";
		currentPlugin = null;
	}
	else
		CommSend("servlet/ajrefresh","POST","cmd=loadInfo&code="+code+"","PopulateFields", true);
}

function Callback_PopulateFields()
{
	currentplugin = new Array(12);
	var e = document.getElementById('detailTable');
	var tablecells = e.getElementsByTagName('td');
	var attributes = xmlResponse.getElementsByTagName('attrib');
	currentPlugin[0] = attributes[0].childNodes[0].nodeValue;
	currentPlugin[1] = attributes[1].childNodes[0].nodeValue; 
	currentPlugin[2] = attributes[2].childNodes[0].nodeValue;
	if(attributes[3].childNodes[0]!=null)
		currentPlugin[3] = attributes[3].childNodes[0].nodeValue;
	else
		currentPlugin[3] = "";
	if(attributes[4].childNodes[0]!=null)
		currentPlugin[4] = attributes[4].childNodes[0].nodeValue;
	else
		currentPlugin[4] = "";
	if(attributes[5].childNodes[0]!=null)
		currentPlugin[5] = attributes[5].childNodes[0].nodeValue;
	else
		currentPlugin[5] = "";
	if(attributes[6].childNodes[0]!=null)
		currentPlugin[6] = attributes[6].childNodes[0].nodeValue;
	else
		currentPlugin[6] = "";
	currentPlugin[7] = attributes[7].childNodes[0].nodeValue;
	currentPlugin[8] = attributes[8].childNodes[0].nodeValue;
	currentPlugin[9] = attributes[9].childNodes[0].nodeValue;
	currentPlugin[10] = attributes[10].childNodes[0].nodeValue;
	if(attributes[11].childNodes[0]!=null)
		currentPlugin[11] = attributes[11].childNodes[0].nodeValue;
	else
		currentPlugin[11] = "";
	tablecells[4].innerHTML = currentPlugin[2];
	tablecells[6].innerHTML = currentPlugin[3];
	tablecells[8].getElementsByTagName('input')[0].value = currentPlugin[5];
	tablecells[10].getElementsByTagName('input')[0].value = currentPlugin[6];
	tablecells[12].innerHTML = currentPlugin[8];
	enableActionPlugin();
}

function sendRegistrationInfo()
{
	var code = currentPlugin[1];
	var plugincode = document.getElementsByName('pluginsn')[0].value;
	var activationcode = document.getElementsByName('pluginact')[0].value;
	CommSend("servlet/ajrefresh","POST","cmd=regPlugin&code="+code+"&plugincode="+plugincode+"&activation="+activationcode+"","RegistrationResult", true);
}

function Callback_RegistrationResult()
{
	var result = xmlResponse.getElementsByTagName('message')[0].childNodes[0].nodeValue;
	if (result == "OK")
	{
		alert(document.getElementsByName("pluginok")[0].value);
		//document.getElementsByName("pluginList")[0].selectedIndex = 0;
		//loadPluginInfo("----------");
		location.reload(true);
	}
	else
		alert(document.getElementsByName("pluginbad")[0].value);
}

function enableActionPlugin()
{
	enableAction(1);
}

function disableActionPlugin()
{
	disableAction(1);
}