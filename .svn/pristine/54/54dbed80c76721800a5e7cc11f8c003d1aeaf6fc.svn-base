
var PointerObj = null;
var OldColor = "#CCCCCC";

function redirect(iddevice)
{
	top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev='+iddevice+'&desc=ncode01');
}

function startRefresh()
{
	CommSend("servlet/ajrefresh","GET","cmd=smpldtl",1);
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
	var item = null;
	
	for(i=0; i<listDevice.length; i++)
	{
		idDevice = listDevice[i].getAttribute("id");
		status = listDevice[i].getAttribute("status");
		status = "url(images/led/L"+status+".gif)";
		divImg = document.getElementById("DLed"+idDevice);
		if(divImg != null)
		{
			divImg.style.backgroundImage = status;
		
		}
	}
	
	var listCHinfo = xmlResponse.getElementsByTagName("info");
	var divstyle = "";
	for (i=0;i<listCHinfo.length;i++)
	{
		
		idDevice = listCHinfo[i].getAttribute("id");
		status = listCHinfo[i].getAttribute("status");
				
		if (status=="nop")
		{
			document.getElementById("P_"+idDevice).style.background="";
		}
		else if (status=="C")
		{
			document.getElementById("P_"+idDevice).style.background="MEDIUMBLUE";		
		}
		else
		{
			document.getElementById("P_"+idDevice).style.background="ORANGERED";
		}
	}
}

function dwhighlight(obj,bval)
{
	if(bval)
	{
		if(PointerObj != null)
			PointerObj.style.backgroundColor = OldColor;
		
		PointerObj = obj;
		OldColor = PointerObj.style.backgroundColor;
		PointerObj.style.backgroundColor = "#EEEEEE";
	}
	else
	{
		if(PointerObj != null)
			PointerObj.style.backgroundColor = OldColor;
		//obj.style.backgroundColor = "#CCCCCC";
	}
}