function onChangeSwitch()
{
	var switch_id = document.getElementById("switch_id").value;
	if (switch_id==-1)
	{
		clean_status();
	}
	else
	{
		CommSend("servlet/ajrefresh","POST","cmd=load_status&switch_id="+switch_id+"","onChangeSwitch");
	}
}

function Callback_onChangeSwitch()
{
	var info = xmlResponse.getElementsByTagName("info")[0];
	var status = info.childNodes[0].nodeValue;
	var tech = info.childNodes[1].nodeValue;
	var autoswitch = info.childNodes[2].nodeValue;
	var hours = info.childNodes[3].nodeValue;
	var lastswitch = info.childNodes[4].nodeValue;
	var nextswitch = info.childNodes[5].nodeValue;
	var n_device = info.childNodes[6].nodeValue;
	var enabled = info.childNodes[7].nodeValue;;
	
	if (autoswitch=="TRUE")
	{
		document.getElementById("times").style.display='';
		document.getElementById("range").style.display='';
		document.getElementById("range2").style.display='';
		document.getElementById("typeswitch").innerHTML=document.getElementById("s_autoswitch").value;
		document.getElementById("autoswitch").style.display='';
	}
	else
	{
		document.getElementById("times").style.display='none';	
		document.getElementById("range").style.display='none';
		document.getElementById("range2").style.display='none';
		document.getElementById("typeswitch").innerHTML=document.getElementById("s_manualswitch").value;
		document.getElementById("autoswitch").innerHTML="";
		document.getElementById("autoswitch").style.display='none';
	}
	
	if (status=="TRUE")
		document.getElementById("status").style.color="GREEN";
	else
		document.getElementById("status").style.color="RED";
	
	if (status=="TRUE")
	{	
		enabled = (enabled=="TRUE")?document.getElementById("s_enabled").value:"<font color='red'>"+document.getElementById("s_disabled").value+"</font>";
	}
	else
	{
		enabled = "";
	}
	
	status = (status=="TRUE")?document.getElementById("s_started").value:document.getElementById("s_stopped").value; 
	
	tech = tech.toUpperCase(); 
		
		
	if (lastswitch=="null") lastswitch = "------------";
		else lastswitch = lastswitch.substring(0,16)
	if (nextswitch=="null") nextswitch = "------------";
		else nextswitch = nextswitch.substring(0,16)
	
	document.getElementById("status").innerHTML=status;
	document.getElementById("tech").innerHTML=tech;
	document.getElementById("autoswitch").innerHTML=enabled;
	document.getElementById("hour").innerHTML=(hours/3600) + " " +document.getElementById("s_hours").value;
	document.getElementById("lastswitch").innerHTML=lastswitch;
	var hasAlarm = document.getElementById("s_hasAlarm").value;
	if(hasAlarm == "true")
	{
		nextswitch += " &nbsp;<font color='red'>("+document.getElementById("s_nextswitchalarm").value+")</font>";
	}
	document.getElementById("nextswitch").innerHTML=nextswitch;
	document.getElementById("devicenumber").innerHTML=n_device;
	
	
	
}

function clean_status()
{
	document.getElementById("status").innerHTML="";
	document.getElementById("tech").innerHTML="";
	document.getElementById("autoswitch").innerHTML="";
	document.getElementById("hour").innerHTML="";
	document.getElementById("lastswitch").innerHTML="";
	document.getElementById("nextswitch").innerHTML="";
	document.getElementById("devicenumber").innerHTML="";
	document.getElementById("times").style.display='none';
	document.getElementById("range").style.display='none';
	document.getElementById("autoswitch").innerHTML="";
	document.getElementById("typeswitch").innerHTML="-----------";
}

function load_switchstatus()
{
	var switch_obj = document.getElementById("switch_id");
	for (i=0;i<switch_obj.options.length;i++)
	{
		if (switch_obj.options[i].value!=-1)
		{
			switch_obj.selectedIndex=i;
			onChangeSwitch();
			break;
		}
	}
	setTimeout("onChangeSwitch();",5000);
}