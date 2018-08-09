function onDevPageLoad()
{
	if( document.getElementById("restart_required").value == 1 )
		alert(document.getElementById("alert_restart").value);
}
function addDevice()
{
	var v_switch = document.getElementById("switch_id").value;
	if (v_switch==-1)
	{
		alert("seleziona un istanza di switch");
		return false;
	}
	else
	{
		to2(document.getElementById('iddev_conf1'));
		return false;
	}
}

function save_dev()
{
	var par_to_post = getList2Value(document.getElementById('iddev_conf1'));
	//controllo selezione switch
	if (document.getElementById("switch_id").value==-1)
	{
		alert("seleziona un istanza di switch");
		return false;
	}
	
	//controllo decrizione
	if (document.getElementById("switch_description").value=="")
	{
		alert(document.getElementById("missingdescription").value);
		return false;
	}
	
	var ofrm = document.getElementById("frm_switch_dev");
	
	
	document.getElementById("param").value = par_to_post;
	
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function onChangeSwitch()
{
	var idswitch = document.getElementById("switch_id").value;
	
	if (idswitch==-1)
	{
		disableAction(1);
		//pulire device
		document.getElementById("iddev_conf1").innerHTML="";
		document.getElementById("iddev_conf2").innerHTML="";
		document.getElementById("switch_description").value="";
	}
	else
	{
		enableAction(1);
		document.getElementById("iddev_conf1").innerHTML="";
		document.getElementById("iddev_conf2").innerHTML="";
		CommSend("servlet/ajrefresh","POST","cmd=load_devices&idswitch="+idswitch+"","onChangeSwitch",true);
	}
}

function Callback_onChangeSwitch()
{
	//descrizione
	var xml_desc = xmlResponse.getElementsByTagName("description");
	document.getElementById("switch_description").value = xml_desc[0].childNodes[0].nodeValue;
	//dispositivi lista sinistra
	var xml_devices_sx = xmlResponse.getElementsByTagName("device_sx");
			
	var combo = document.getElementById("iddev_conf1");
	var cont=0;
	if (xml_devices_sx.length>0)
	{
		for (i=0;i<xml_devices_sx.length;i++)
		{
			var iddev = String(xml_devices_sx[i].getAttribute("id"));
			var desc = xml_devices_sx[i].childNodes[0].nodeValue;
			combo.options[cont++] = new Option(desc,iddev);
		}
	}	
	//dispositivi lista destra
	var xml_devices = xmlResponse.getElementsByTagName("device");
			
	var combo = document.getElementById("iddev_conf2");
	var cont=0;
	if (xml_devices.length>0)
	{
		for (i=0;i<xml_devices.length;i++)
		{
			var iddev = String(xml_devices[i].getAttribute("id"));
			var desc = xml_devices[i].childNodes[0].nodeValue;
			combo.options[cont++] = new Option(desc,iddev);
		}
	}	
}

function load_switchdev()
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
}