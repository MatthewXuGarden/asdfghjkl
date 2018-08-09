function resetRelay()
{
	var ofrm = document.getElementById("frm_reset_relay");
	if(ofrm!= null)
		MTstartServerComm();
	ofrm.submit();
}

function onload()
{
	enableAction(1);
	if (document.getElementById("isEmpty").value == "false")
    enableAction(2);
}

function refresh_relay_page()
{
	top.frames['manager'].loadTrx('nop&folder=relaymgr&bo=BRelayMgr&type=menu');
}

function startRelayRefresh()
{
	CommSend("servlet/ajrefresh","GET","cmd=relayrfh",1);
	setTimeout("startRelayRefresh()",5000);
}

// this callback to refresh ralaymgr table
function Callback_1()
{
	var xmlRelayList = xmlResponse.getElementsByTagName("relay");
	
	var val = "";
	var act = "";
	var idrelay = "";
	
	var numrelay = xmlRelayList.length;

	
	for(i=0; i<numrelay; i++)
	{
		val = xmlRelayList[i].getAttribute("val");
		act = xmlRelayList[i].getAttribute("act");
		
		if(val == '***')
		{
			// set status column to 'offline'
			document.getElementById("reltab_"+i+"_1").innerHTML = "<font color='gray'>" + document.getElementById("rel_offline").value + "</font>";  
			document.getElementById("reltab_"+i+"_2").innerHTML = "";
		}
		else
		{
			if(val == act)
			{
				idrelay = xmlRelayList[i].getAttribute("id");
				
				// set status column to 'active'
				document.getElementById("reltab_"+i+"_1").innerHTML = "<font color='red'>" + document.getElementById("rel_active").value + "</font>";

				// insert checkbox if relay is active
				if(document.getElementById("chr_"+idrelay).checked == true)
					// keep checked checkboxes, at every table refresh
					document.getElementById("reltab_"+i+"_2").innerHTML = "<input type='checkbox' name='chr_"+idrelay+"' checked/>";
				else
					document.getElementById("reltab_"+i+"_2").innerHTML = "<input type='checkbox' name='chr_"+idrelay+"' />";
			}
			else
			{
				// set status column to 'offline'
				// remove checkbox
				document.getElementById("reltab_"+i+"_1").innerHTML = "<font color='green'>" + document.getElementById("rel_idle").value + "</font>";    			
				document.getElementById("reltab_"+i+"_2").innerHTML = "";
			}
		}
	}
}

/**
* Per ridimensionare la tabella all'interno della pagina
*/
function resizeTableTab1()
{
	var hdev = MTcalcObjectHeight("trRelList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-60);
	}
}