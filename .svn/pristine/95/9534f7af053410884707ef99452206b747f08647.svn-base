
var DEV_LIMIT = 20;
var DEV_OFFSET = -20;

function redirect(iddevice)
{
	top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev='+iddevice+'&desc=ncode01');
}

function startRefresh()
{
	DEV_OFFSET = (DEV_OFFSET + DEV_LIMIT);
	CommSend("servlet/ajrefresh","GET","cmd=dtlrfh&limit="+DEV_LIMIT+"&offset="+DEV_OFFSET,1);
	setTimeout("startRefresh()",30000);
}

function Callback_1()
{
	var listDevice = xmlResponse.getElementsByTagName("device");
	var i=0;
	var j=0;
	var idDevice = -1;
	var status = 0;
	var divImg = null;
	var listVariables = null;
	var idVar = "";
	var vaVar = "";
	var item = null;
	
	if(listDevice.length < DEV_LIMIT)
		DEV_OFFSET = -20;
	
	for(i=0; i<listDevice.length; i++)
	{
		idDevice = listDevice[i].getAttribute("id");
		status = listDevice[i].getAttribute("status");
		status = "url(images/led/L"+status+".gif)";
		divImg = document.getElementById("DLed"+idDevice);
		if(divImg != null)
			divImg.style.backgroundImage = status;
		
		listVariables = listDevice[i].getElementsByTagName("var");
		
		if(listVariables != null)
		{
			for(j=0; j<listVariables.length; j++)
			{
				idVar = listVariables[j].getAttribute("id");
				item = listVariables[j].childNodes[0];
				if(item != null)
					vaVar = item.nodeValue;
				else
					vaVar = "***";
				
				if(idVar != null && idVar != "")
					document.getElementById(idVar).innerHTML = vaVar;
			}
		}
	}
}

function dwhighlight(obj,bval)
{
	if(bval)
		obj.style.backgroundColor = "#000000";
	else
		obj.style.backgroundColor = "#CCCCCC";
}