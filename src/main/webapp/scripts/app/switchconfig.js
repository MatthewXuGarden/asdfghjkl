function switch_change_mode(type)
{
	if (type=="blank")
	{
		document.getElementById("auto").checked=false;
		document.getElementById("manual").checked=false;
		document.getElementById("obj_starttype").value="";
		document.getElementById("obj_hour").value="";
		document.getElementById("obj_starthour").value="";
	    document.getElementById("obj_manualtype").value="";
	    document.getElementById("obj_alarm_wait").value="";
	    document.getElementById("obj_starttype").disabled=true;
		document.getElementById("obj_hour").disabled=true;
		document.getElementById("obj_starthour").value=-1;
		document.getElementById("obj_starthour").disabled=true;
	    document.getElementById("obj_manualtype").disabled=true;
	    document.getElementById("auto").disabled=true;
		document.getElementById("manual").disabled=true;
		document.getElementById("autoresume").checked=false;
		document.getElementById("autoresume").disabled=true;
		document.getElementById("obj_skipalarm").checked=false;
		document.getElementById("obj_alarm_wait").value="";
		document.getElementById("obj_alarm_wait").disabled=true;
		document.getElementById("obj_skipalarm").disabled=true;
		
		document.getElementById("power_button").innerHTML=""; //bottoni start e stop istanza switch
		document.getElementById("status").innerHTML="";
	}
	else
	{
		var is_auto = (type=="auto")?true:false;	
		
		document.getElementById("obj_starttype").disabled=!is_auto;
		document.getElementById("obj_hour").disabled=!is_auto;
		document.getElementById("obj_starthour").disabled=!is_auto;
	    document.getElementById("obj_manualtype").disabled=is_auto;
	    document.getElementById("auto").disabled=false;
		document.getElementById("manual").disabled=false;
		document.getElementById("autoresume").disabled=false;
		document.getElementById("obj_alarm_wait").disabled=false;
		document.getElementById("obj_skipalarm").disabled=false;
	}
}

//ricaricare modelli di variabili e allarmi configurati
function onChangeSwitch()
{
	var switch_id = document.getElementById("switch_id").value;
	if (switch_id==-1)
	{
		disableAction(1);
		switch_change_mode("blank");
	}
	else
	{
		enableAction(1);	
		CommSend("servlet/ajrefresh","POST","cmd=load_conf&switch_id="+switch_id+"","onChangeSwitch");
	}
}

function Callback_onChangeSwitch()
{
	var auto = xmlResponse.getElementsByTagName("auto")[0];
	var manual = xmlResponse.getElementsByTagName("manual")[0];
	var autorest = xmlResponse.getElementsByTagName("autoresume")[0];
	var skip = xmlResponse.getElementsByTagName("skipalarm")[0];
	var run = xmlResponse.getElementsByTagName("running")[0];
	
	var autoswitch = auto.childNodes[0].nodeValue;
	var hour = auto.childNodes[1].nodeValue;
	var starthour = auto.childNodes[2].nodeValue;
	var starttype=auto.childNodes[3].nodeValue;
	var alarmwait = auto.childNodes[4].nodeValue;
	var manualtype=manual.childNodes[0].nodeValue;
	var autoresume = autorest.childNodes[0].nodeValue;
	var skipalarm = skip.childNodes[0].nodeValue;
	var running = run.childNodes[0].nodeValue;
	
	starthour = starthour.substring(11,13);  //dal timestamp ricavo solo l'ora, con 2 cifre
	if (starthour=="") starthour = -1;	
	document.getElementById("auto").checked=true;
	document.getElementById("obj_hour").value=hour/3600;
	document.getElementById("obj_starthour").value=starthour;
	document.getElementById("obj_alarm_wait").value=alarmwait/60;
	document.getElementById("obj_starttype").value=starttype;
	document.getElementById("obj_manualtype").value=manualtype;
		
	if (autoswitch=="TRUE")
	{
		switch_change_mode("auto");
		document.getElementById("auto").checked=true;
	}
	else
	{
		switch_change_mode("manual");
		document.getElementById("manual").checked=true;
	}
	
	if (autoresume=="TRUE")
		document.getElementById("autoresume").checked=true;
	else
		document.getElementById("autoresume").checked=false;
		
	if (skipalarm=="TRUE")
		document.getElementById("obj_skipalarm").checked=true;
	else
		document.getElementById("obj_skipalarm").checked=false;
	
	var buttons = "";
	var s_start = document.getElementById("power_on").value;
	var s_stop = document.getElementById("power_off").value;
	var status = document.getElementById("status");
	if (running=="FALSE")
	{	
		status.innerHTML = document.getElementById("s_stopped").value;
		document.getElementById("status").style.color="RED";
		buttons = "<img title="+s_start+" onclick='start_switch();' name='startbtn' style='cursor:pointer' src='images/actions/start_on_black.png'/>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<img title="+s_stop+"  style='cursor:pointer' src='images/actions/stop_off.png'/>";
	}
	else
	{
		status.innerHTML = document.getElementById("s_started").value;
		document.getElementById("status").style.color="GREEN";
		buttons = "<img title="+s_start+"  name='stopbtn' style='cursor:pointer' src='images/actions/start_off.png'/>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<img title="+s_stop+" onclick='stop_switch();' style='cursor:pointer' src='images/actions/stop_on_black.png'/>";
	}
	
	
	document.getElementById("power_button").innerHTML=buttons;
}

function save_conf()
{
	document.getElementById("cmd").value="save_conf";
	var switch_id = document.getElementById("switch_id").value;
	if (switch_id==-1)
	{
		alert(document.getElementById("selectswitchalert").value);
		return false;
	}
	else if (document.getElementById("auto").checked)
	{
		if (document.getElementById("obj_hour").value==(""||0))
		{
			alert(document.getElementById("s_alerthour").value);
			return false;
		}
		else if (document.getElementById("obj_starthour").value==-1)
		{
			alert(document.getElementById("s_alertstarthour").value);
			return false;
		}
	}
	
	var ofrm = document.getElementById("frm_config");
	var isauto = document.getElementById("isauto");
	if (document.getElementById("auto").checked==true)
		isauto.value="TRUE";
	else
		isauto.value="FALSE";
		
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();

}


function start_switch()
{
	if (confirm(document.getElementById("s_canstart").value))
	{
		document.getElementById("cmd").value="start";
		var ofrm = document.getElementById("frm_config");
		var isauto = document.getElementById("isauto");
		if (document.getElementById("auto").checked==true)
			isauto.value="TRUE";
		else
			isauto.value="FALSE";
			
		if(ofrm != null)
				MTstartServerComm();
		ofrm.submit();
	}
}

function stop_switch()
{
	document.getElementById("cmd").value="stop";
	var ofrm = document.getElementById("frm_config");
	var isauto = document.getElementById("isauto");
	if (document.getElementById("auto").checked==true)
		isauto.value="TRUE";
	else
		isauto.value="FALSE";
		
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function load_switchconfig()
{
	var switch_obj = document.getElementById("switch_id");
	var selected_switch_id = document.getElementById("selected_switch_id").value;
	if(selected_switch_id == "")
	{
		for (i=0;i<switch_obj.options.length;i++)
		{
			if (switch_obj.options[i].value!=-1)
			{
				switch_obj.selectedIndex=i;
				//onChangeSwitch();
				break;
			}
		}
	}
	else
	{
		for (i=0;i<switch_obj.options.length;i++)
		{
			if (switch_obj.options[i].value == selected_switch_id)
			{
				switch_obj.selectedIndex=i;
				//onChangeSwitch();
				break;
			}
		}
	}
	onChangeSwitch();
}

function onClickAutoresume()
{
	if (document.getElementById("obj_skipalarm").checked)
	{
		document.getElementById("obj_skipalarm").checked=false;
	}
}

function onClickSkipAlarm()
{
	if (document.getElementById("autoresume").checked)
	{
		document.getElementById("autoresume").checked=false;
	}
}


