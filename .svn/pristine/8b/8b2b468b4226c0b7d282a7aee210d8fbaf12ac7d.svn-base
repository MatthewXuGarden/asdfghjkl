var site_type = "PVP";

function initialize()
{
	disableAll(true);
}

function r_site_savesync()
{
	//creazione sync_params
	var sync_params="";
	if (document.getElementById("opt1").checked==true)
	{
		sync_params = "1,2,3,4,";
	}
	if (document.getElementById("opt2").checked==true)
	{
		sync_params = sync_params + "5,";
	}
	if (document.getElementById("opt3").checked==true)
	{
		sync_params = sync_params + "6,";
	}
	if (document.getElementById("opt4").checked==true)
	{
		sync_params = sync_params + "7,";
	}
	if (sync_params!="")
	{
		sync_params	= sync_params.substring(0,sync_params.length-1);
	}
	document.getElementById("sync_params").value=sync_params;
	var ofrm = document.getElementById("frm_sync");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function sync_reload(param)
{
	disableAll(false);
	decheckAll();
	param_array= param.split(";");
	var idsite_to_set = param_array[0];
	var site_name = param_array[1];
	var sync_param = param_array[2];
	site_type = param_array[3];
	
	if (site_type=="PVE")
	{
		document.getElementById("opt3").disabled=true;
		document.getElementById("opt4").disabled=true;
	}
	else
	{
		document.getElementById("opt3").disabled=false;
		document.getElementById("opt4").disabled=false;
	}
	
	document.getElementById("idsite_to_set").value=idsite_to_set;
	document.getElementById("sitename").innerHTML=site_name;
	if (sync_param=="")
	{
		document.getElementById("opt0").checked=true;
		no_sync(document.getElementById("opt0"));
	}
	if (sync_param.indexOf("1")!=-1)   //check impianto
	{
		document.getElementById("opt1").checked=true;
	}
	if (sync_param.indexOf("5")!=-1)   //check allarmi
	{
		document.getElementById("opt2").checked=true;
	}
	if (sync_param.indexOf("6")!=-1)
	{
		document.getElementById("opt3").checked=true;
	}
	if (sync_param.indexOf("7")!=-1)
	{
		document.getElementById("opt4").checked=true;
	}
	
	enableAction(1);

}

function no_sync(obj)
{
	if (obj.checked==true)
	{
		for (i=1;i<5;i++)
		{
			document.getElementById("opt"+i).checked=false;
		}
	}
}

function plant_sync(obj)
{
	if (obj.checked==false)
	{
		document.getElementById("opt0").checked=true;
		document.getElementById("opt2").checked=false;
		document.getElementById("opt3").checked=false;
		document.getElementById("opt4").checked=false;
	}
	else
	{
		document.getElementById("opt0").checked=false;
		if (site_type=="PVE")
		{
			document.getElementById("opt2").checked=true;
		} 
	}
}

function alr_evn_note(obj)
{
	if (obj.checked==true)
	{
		document.getElementById("opt0").checked=false;
		document.getElementById("opt1").checked=true;
	}
	else
	{
		if (site_type=="PVE")
		{
			document.getElementById("opt1").checked=false;
			document.getElementById("opt0").checked=true;
		} 
	}
}

function decheckAll()
{
	for (i=0;i<5;i++)
	{	
		document.getElementById("opt"+i).checked=false;
	}
}

function disableAll(bool)
{
	for (i=0;i<5;i++)
	{	
		document.getElementById("opt"+i).disabled=bool;
	}
}


/**
* Per ridimensionare la tabella Sincronizzazione
*/
function resizeTableDataSync()
{
	var hdev = MTcalcObjectHeight("trDataSync");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-30);
	}
}

