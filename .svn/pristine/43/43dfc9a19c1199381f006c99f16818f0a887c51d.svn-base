var idRackSelected	= -1;
var nUtilUsed		= 0;
// aliases
var listRacks		= null;
var listAllUtils	= null;
var listRackUtils	= null;
var COL_RACKS_ID	= 2;
var COL_RACKS_NAME	= 3;


function save_utils()
{
	var ids_util = "";
	for(var i = 0; i < listRackUtils.options.length; i++) {
		if( i > 0 )
			ids_util += ";";
		ids_util += listRackUtils.options[i].value;
	}
	document.getElementById('ids_util').value = ids_util;
		
	if (verify_maxutil())
	{
		var form = document.getElementById("frm_util");
		if(form != null) {
			MTstartServerComm();
			form.submit();
		}
	}
}


function verify_maxutil()
{
	var max_util = Number(document.getElementById('max_util').value);
	var ids_util = document.getElementById('ids_util').value;
	var aids_util = ids_util.split(";");
	var num_util = nUtilUsed + (ids_util.length > 0 ? aids_util.length : 0);
	if (num_util > max_util)
	{
		if (num_util>max_util)
		{
				alert(document.getElementById("maxnumutil").value + ": " + max_util);  
				return false;
		}
	}
	return true;
}


function init_t2()
{
	disableAction(1);
	// set aliases
	listRacks = Lsw1;
	listAllUtils = listbox1;
	listRackUtils = listbox2;
}


function onSelectRack(idRack)
{
	CommSend("servlet/ajrefresh", "POST", "action=select_rack&id=" + idRack, "onRackSelected", true);
}


function Callback_onRackSelected(response)
{
	var xmlRack = response.getElementsByTagName("rack")[0];
	
	idRackSelected = xmlRack.getAttribute("id");
	document.getElementById("idRackSelected").value = idRackSelected;
	var bNewAlg = xmlRack.getAttribute("new_alg") == "true";
	document.getElementById("new_alg").checked = bNewAlg;
	
	for(var i = 0; i < listRacks.mData.length; i++) {
		if( listRacks.mData[i][COL_RACKS_ID] == idRackSelected ) {
			document.getElementById("rack_name").value = listRacks.mData[i][COL_RACKS_NAME]; 
			break;
		}
	}
	
	listRackUtils.length = 0;
	var aXmlUtils = xmlRack.getElementsByTagName("util");
	for(i = 0; i < aXmlUtils.length; i++) {
		var xmlUtil = aXmlUtils[i];
		listRackUtils.options.add(new Option(xmlUtil.getAttribute("name"), xmlUtil.getAttribute("id")));
	}
	
	listAllUtils.length = 0;
	var xmlUtilities = response.getElementsByTagName("utilities")[0];
	nUtilUsed = parseInt(xmlUtilities.getAttribute("used"), 10);
	aXmlUtils = xmlUtilities.getElementsByTagName("util");
	for(i = 0; i < aXmlUtils.length; i++) {
		var xmlUtil = aXmlUtils[i];
		listAllUtils.options.add(new Option(xmlUtil.getAttribute("name"), xmlUtil.getAttribute("id")));
	}
	for(i=0;i<listAllUtils.options.length;i++){
		listAllUtils.options[i].className = i%2==0?"Row1":"Row2";
	}
	
	enableAction(1);
}
