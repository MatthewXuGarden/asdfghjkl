var lineSelected = null;
var clickState = 0;

function selectedLine(idLine)
{
	if (idLine==null) return true;
	if (lineSelected==idLine) 
	{
		if ((clickState==null)||(clickState==0))
		{
			clickState=1;
			enableAction(2);
			disableAction(1);
			disableAction(3);
			reset_page();
			disableAll(true);
		}
		else
		{
		clickState=0;
		enableAction(1);
		disableAction(2);
		disableAction(3);
		reset_page();
		disableAll(false);
		document.getElementById("name").focus();
		
		}
	}
	else
	{
		clickState=1;
		enableAction(2);
		disableAction(3);
		disableAction(1);
		reset_page();
		disableAll(true);
	}
	lineSelected= idLine;
	
}


function r_site_onload()
{
	var cmd = document.getElementById("cmd").value;
	if ((cmd!="reload")&&(cmd!="double"))
	{
		enableAction(1);
		document.getElementById("name").focus();
		document.getElementById("phonenumber").disabled=true;
	}
	else if (cmd=="reload")
	{
		//enableAction(1);  reload non permette di ricreare sito da info precedente
		enableAction(3);
		onchange_connection();
		document.getElementById("name").focus();
	}
	else if (cmd=="double")
	{
		alert(document.getElementById("s_doubleid").value);
		enableAction(1);
		document.getElementById("identify").focus();
		document.getElementById("phonenumber").disabled=false;
	}
}

function reset_page()
{
	document.getElementById("name").value="";
	document.getElementById("type").selectedIndex=0;
	document.getElementById("identify").value="";
	document.getElementById("password").value="";
	document.getElementById("connectiontype").selectedIndex=0;
	document.getElementById("phonenumber").value="";
}

function disableAll(bool)
{
	document.getElementById("name").disabled=bool;
	document.getElementById("type").disabled=bool;
	document.getElementById("identify").disabled=bool;
	document.getElementById("password").disabled=bool;
	document.getElementById("connectiontype").disabled=bool;
	document.getElementById("phonenumber").disabled=bool;
}

function modify_site(idsite)
{
	document.getElementById("cmd").value="reload";
	document.getElementById("site_to_modify").value=lineSelected;
	var ofrm = document.getElementById("frm_r_sitelist");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function r_site_save()
{
	var curRows = document.getElementById("currentrows").value;
	var totRows = document.getElementById("totalrows").value;
	if((Number(totRows) > Number(curRows)))
	{
		if (can_save())
		{
			document.getElementById("cmd").value="save";
			var ofrm = document.getElementById("frm_r_sitelist");
			if(ofrm != null)
			{
				MTstartServerComm();
				ofrm.submit();
			}
		}
	}
	else
	{
		try {
			alert(document.getElementById("licenselimit").value);
		}catch(e){}
	}
}

function r_site_remove()
{
	if (confirm(document.getElementById("confirmsitedel").value))
	{
		document.getElementById("cmd").value="remove";
		document.getElementById("site_to_modify").value=lineSelected;
		var ofrm = document.getElementById("frm_r_sitelist");
		if(ofrm != null)
				MTstartServerComm();
		ofrm.submit();
	}
}

function r_site_savemodify()
{
	if (can_save())
	{
		document.getElementById("cmd").value="savemodify";
		var ofrm = document.getElementById("frm_r_sitelist")
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function can_save()
{
	if ((document.getElementById("name").value=="")||(document.getElementById("type").selectedIndex==0)||(document.getElementById("identify").value=="")||(document.getElementById("password").value=="")||(document.getElementById("connectiontype").selectedIndex==0)||((document.getElementById("phonenumber").value=="")&&(document.getElementById("connectiontype").value=="MODEM")))
	{
		alert(document.getElementById("selectallfields").value);
		return false;
	}
	else
	{
		return true;
	}
}

function onchange_connection()
{
	if (document.getElementById("connectiontype").value=="MODEM")
	{
		document.getElementById("phonenumber").disabled=false;
		document.getElementById("conn_desc").innerText=document.getElementById("s_phonenumber").value;
		//document.getElementById("phonenumber").value="";
	}
	else if (document.getElementById("connectiontype").value=="LAN")
	{
		//document.getElementById("phonenumber").value="";
		document.getElementById("phonenumber").disabled=false;
		document.getElementById("conn_desc").innerText=document.getElementById("s_ip").value;
	}
	else
	{
		document.getElementById("phonenumber").value="";
		document.getElementById("phonenumber").disabled=true;
	}	
}

function changeType()
{
	var type = document.getElementById("type").value;
	var connection = document.getElementById("connectiontype");
		
	if (type=="PVP")
	{
		if (connection.options.length<3)
		{
			connection.options[2]= new Option();
			connection.options[2].value="LAN";
			connection.options[2].innerText=document.getElementById("s_lan").value;
		}
	}
	else if (type=="PVE")
	{
		connection.options.length=2;
		connection.selectedIndex=0;
	}
}

/**
* Per ridimensionare la tabella configurazioni siti
*/
function resizeTableSiteConfig()
{
	var hdev = MTcalcObjectHeight("trSiteList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-25);
	}
}