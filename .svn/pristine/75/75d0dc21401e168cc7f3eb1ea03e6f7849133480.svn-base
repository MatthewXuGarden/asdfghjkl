function ackOnload()
{
	enableAction(1);
}


function ackallalarms()
{
	var lsw = oLswContainer.getLsw('LWCtDataName1');
	var idvett = new Array(lsw.mData.length);
	for(var i = 0;i<idvett.length; i++)
	{
		idvett[i] = lsw.mData[i][2];
	}
	if(confirm(document.getElementById('confirmackall').value))
		CommSend("servlet/ajrefresh","POST","ids="+idvett+"&cmd=ack_all","AlrReload", true);
}

function Callback_AlrReload()
{
	var result = xmlResponse.getElementsByTagName('message')[0].childNodes[0].nodeValue;
	window.location.reload(true);
}

function resizeAlarmTableAct()
{
	var h1 = MTcalcObjectHeight("trAlrList");
	if(h1 != 0)
		MTresizeHtmlTable(1,h1-60);	
}

function resizeAlarmTableRcl()
{
	var h1 = MTcalcObjectHeight("trAlrList");
	if(h1 != 0)
		MTresizeHtmlTable(2,h1-60);	
}

function updNumAlrAck()
{
	return document.getElementById("num_ack").value;;
}

function delete_allarms()
{
	var number = updNumAlrAck();
		if (number > 0)
		{
			var lsw = oLswContainer.getLsw('LWCtDataName1');
			var idvett = new Array(lsw.mData.length);
			for(var i = 0;i<idvett.length; i++)
			{
				idvett[i] = lsw.mData[i][2];
			}
			if(confirm(document.getElementById('confirmdeleteall').value))
				CommSend("servlet/ajrefresh","POST","ids="+idvett+"&cmd=delete_all","AlrReload", true);
		}
		else
		{
			alert(document.getElementById('noalarmtodelete').value);
		}
}

function reset_allarms()
{
	var number = updNumAlrAck();
	if (number > 0)
	{
		var lsw = oLswContainer.getLsw('LWCtDataName1');
		var idvett = new Array(lsw.mData.length);
		for(var i = 0;i<idvett.length; i++)
		{
			idvett[i] = lsw.mData[i][2];
		}
		if(confirm(document.getElementById('confirmresetall').value))
			CommSend("servlet/ajrefresh","POST","ids="+idvett+"&cmd=reset_all","AlrReload", true);
	}
	else
	{
		alert(document.getElementById('noalarmtoreset').value);
	}
}