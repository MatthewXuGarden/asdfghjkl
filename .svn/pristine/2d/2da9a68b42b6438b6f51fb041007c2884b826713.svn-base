var ONColor = 0x00FF00;
var OFFColor = 0xEAEAEA;
var DisabledColor = 0x0000C0;
var ALARMColor = 0xC00000;
var retVal = "";  
var ssto;

// Asynchronous Javascript Xml Refresh
function startDtlRefresh()
{
	CommSend("servlet/ajrefresh","GET","cmd=devrfh",2);
	setTimeout("startDtlRefresh()",10000);
}

function Callback_2()
{
	var status = "0";
	var divBttCont = document.getElementById("bttContainer");
	var xmlDevice = xmlResponse.getElementsByTagName("device");
	var listVariables = null;
	var idVar = "";
	var vaVar = "";
	var tyVar = "";
	var item = null;

	//alert(xmlCommReq.responseText);
	
	if(xmlDevice.length > 0)
	{
		// update device status
		status = xmlDevice[0].getAttribute("status");
		setImg(document.getElementById("devsts"), "devsts", status);
		
		// Alessandro 20100325 : commands block must be always visible
		/*try {
			if(status == 0)
				divBttCont.style.visibility = "hidden";
			else
				divBttCont.style.visibility = "visible";
		}
		catch(e){}
		*/
		
		// Alessandro: update status (led+description) of the STAT variables
		listStatVariables = xmlDevice[0].getElementsByTagName("svar");
		if(listStatVariables != null)
		{
			for(j=0; j<listStatVariables.length; j++)
			{
				idVar = listStatVariables[j].getAttribute("id");
				tyVar = listStatVariables[j].getAttribute("type");
				vaVar = listStatVariables[j].getAttribute("value");
				item = listStatVariables[j].childNodes[0];
				if(item != null)
					descVar = item.nodeValue;
				else
					descVar = "***";
				
				if(idVar != null && idVar != "")
				{
					// update runtime variable based on specific id format
					// - the same variable could appear on the page with the
					// following id formats: var_devId_varId, varId, imgvarId
					// - for a single variable both formats could appear at the same time
					// update numeric variable
					setStatVar(document.getElementById(idVar), tyVar, vaVar, descVar);
					var idVarFormat = idVar.split("_");
					if( idVarFormat.length == 3 ) {
						var idNumber = idVarFormat[2];
						setStatVar(document.getElementById(idNumber), tyVar, vaVar, descVar);
						// update image variable
						setImg(document.getElementById("img" + idNumber), idNumber, vaVar);
					}
				}
			}
		}		
		
		listVariables = xmlDevice[0].getElementsByTagName("var");
		if(listVariables != null)
		{
			for(j=0; j<listVariables.length; j++)
			{
				idVar = listVariables[j].getAttribute("id");
				tyVar = listVariables[j].getAttribute("type");
				item = listVariables[j].childNodes[0];
				if(item != null)
					vaVar = item.nodeValue;
				else
					vaVar = "***";
				
				if(idVar != null && idVar != "")
				{
					// update runtime variable based on specific id format
					// - the same variable could appear on the page with the
					// following id formats: var_devId_varId, varId, imgvarId
					// - for a single variable both formats could appear at the same time
					// update numeric variable
					setVar(document.getElementById(idVar), tyVar, vaVar);
					var idVarFormat = idVar.split("_");
					if( idVarFormat.length == 3 ) {
						var idNumber = idVarFormat[2];
						setVar(document.getElementById(idNumber), tyVar, vaVar);
						// update image variable
						setImg(document.getElementById("img" + idNumber), idNumber, vaVar);
					}
				}
			}
		}
		
		listVariables = xmlDevice[0].getElementsByTagName("bar");
		if(listVariables != null)
		{
			for(j=0; j<listVariables.length; j++)
			{
				idVar = listVariables[j].getAttribute("id");
				vaVar = listVariables[j].getAttribute("value");
				tyVar = listVariables[j].getAttribute("led");
				item = listVariables[j].childNodes[0];
				
				// Set hidden to post
				document.getElementById("dtlst_"+idVar).value = vaVar;
				// Set led img
				try {
					if (tyVar!=null && tyVar!="")
					{
						document.getElementById("rleddtlst_"+idVar).src = "images/led/RectL"+tyVar+".png";
					}
				}
				catch(e){
				}
				// Set button img
				document.getElementById("bttdtlst_"+idVar).src = item.nodeValue;
			}
		}
	}
}
// End

function setVar(element, tyVar, vaVar)
{
	if( !element ) return;

	if(tyVar == "D" && isNumber(vaVar))
	{
		if(vaVar == "1")
			element.innerHTML = "<img src='images/led/L1.gif'/>";
		else
			element.innerHTML = "<img src='images/led/L0.gif'/>";
	}
	else
		// ALessandro: customization for devices who needs to show a description string instead of the led		
		element.innerHTML = "<b>"+vaVar+"</b>";
}


// Alessandro : set a Stat Variable with led + description in the black box
function setStatVar(element, tyVar, vaVar, descVar)
{
	if( !element ) return;
	// Alessandro: stat variables are always digital 
	if(vaVar == "1")
		element.innerHTML = "<img src='images/led/L1.gif' class='statusLed' />";
	else
		element.innerHTML = "<img src='images/led/L0.gif' class='statusLed' />";
	element.innerHTML += descVar;
}


function setImg(element, id, vaVar)
{
	if( !element ) return;
	
	var elementVal = document.getElementById("imgval" + id);
	if( elementVal ) {
		var values = elementVal.value.split(";");
		if( values[vaVar] )
			element.innerHTML = values[vaVar];
		else
			element.innerHTML = vaVar;
	}
}

function dtlmain_onload()
{
	if (document.getElementById("js_control") == null)
	{
		canCommit = true;
		enableAction(1);
		enableAction(2);
		
		dtlApplyDeviceImg();
		startDtlRefresh();
		
		if(document.getElementById("statusoffline") != null && 
		   document.getElementById("statusoffline").value == "false")
		{
			enableAction(3);
		}
	}
	else
	{
		disableAction(1);
		disableAction(2);
	}
	
	try {
		if(document.getElementById("openwt").value == "Y")
			dtlRenderTabParam(null,"open");
	}
	catch(e){}
	
	/*
	//permission parameters
	var permission = document.getElementById('permission').value;
	if (permission!=2)
	{
		disableAction(3);
	}
	*/
	
}

//2010-9-15, comment by Kevin, because the is no device flash LED in dtlview page
//function startSetLed()
//{
//	setTimeout("SetLed(document.getElementById('LedStato'))",1000);
//}
//
//function SetLed(led)
//{	
//	if(led != null)
//	{
//		if (led.name <= 1)
//		{
//			led.SetVariable('obj.Value', led.name);
//			led.SetVariable('obj.ONColor', ONColor);
//			led.SetVariable('obj.OFFColor', OFFColor);	
//		}
//		else if (led.name <= 2)
//		{
//			led.SetVariable('obj.Value', led.name/2);
//			led.SetVariable('obj.ONColor', ALARMColor);
//			led.SetVariable('obj.OFFColor', OFFColor);	
//		}
//		else if (led.name <= 3)
//		{
//			led.SetVariable('obj.Value', led.name/3);
//			led.SetVariable('obj.ONColor', DisabledColor);
//			led.SetVariable('obj.OFFColor', OFFColor);	
//		}
//		
//		if(led.name == 0)
//		{
//			//disableAction(2);
//			if(document.getElementById("statusoffline") != null)
//				document.getElementById("statusoffline").value = "true";
//		}
//		else
//		{
//			//enableAction(2);
//			if(document.getElementById("statusoffline") != null)
//				document.getElementById("statusoffline").value = "false";
//		}
//	}
//}

function controlMinMax(idvar)
{
	var min_permitted = document.getElementById('min_'+idvar).value;
	var max_permitted = document.getElementById('max_'+idvar).value;
	var set = document.getElementById('dtlst_'+idvar).value;
	
	if (set != "")
	{
		if (min_permitted != null)
		{
			if (Number(set) < Number(min_permitted))
			{
				alert(document.getElementById("s_minval").value + min_permitted);
				document.getElementById('dtlst_'+idvar).value = "";
				document.getElementById('dtlst_'+idvar).focus();
				return false;
			}
		}
		if (max_permitted != null)
		{
			if (Number(set) > Number(max_permitted))
			{
				alert(document.getElementById("s_maxval").value + max_permitted);
				document.getElementById('dtlst_'+idvar).value = "";
				document.getElementById('dtlst_'+idvar).focus();
				return false;
			}
		}
		return true;
	}
	return true;
}

function dtlSetVars()
{
	//2010-01-08, by kevin
	//2015-04-02, modified to fix values like this 2...3 into 2.3 and clear all NaN values
	
	if(numberCheck() == false)
	{
		return;
	}
	//end
	canCommit = false;
	if(document.getElementById("statusoffline") != null && 
	   document.getElementById("statusoffline").value == "false")
	{
		addSetFromCustomSection();
		
		MTstartServerComm();
		document.getElementById("frmdtl").submit();
	}
}
function numberCheck()
{
	var virtkey = document.getElementById("virtkeyboard").value;
	var inputs = document.frmdtl.getElementsByTagName("INPUT");

	for(var i=0;i<inputs.length;i++)
	{
		// try to fix wrong numbers escaped from filters
		if( inputs[i].type == "text" && inputs[i].value != "" ) {		
			var value = inputs[i].value;
			value = value.replace(",", ".");	// just in case convert , into .
			value = value.replace(/\.+/, ".");	// convert ... into .
			if( isNaN(parseFloat(value)) )
				inputs[i].value = "";
			else
				inputs[i].value = parseFloat(value);
		}
		if(virtkey == "on" && inputs[i].type == "text" && inputs[i].id.match("^dtlst_") && inputs[i].value != "")
		{
			var value = inputs[i].value;
			var id = inputs[i].id.split("_")[1];
			var maxO = document.getElementById("max_"+id);
			var minO = document.getElementById("min_"+id);
			//by Kevin. to fix the bug when checking digital variables. there is no max and min in hidden
			var max;
			var min;
			if(maxO == null || maxO == undefined) 
			{
				max = null;
			}
			else
			{	
				max = maxO.value;
			}
			if(minO == null || minO == undefined)
			{
				min = null;
			}
			else
			{
				min = minO.value;
			}
			if(maxminvaluecheck(value,min,max,id) == false)
			{
				return false;
			}
		}
	}
	return true;
}
function maxminvaluecheck(set,min_permitted,max_permitted,idvar)
{
	if (set != "")
	{
		if (min_permitted != null && min_permitted != "")
		{
			if (Number(set) < Number(min_permitted))
			{
				alert(document.getElementById("s_minval").value + min_permitted);
				document.getElementById('dtlst_'+idvar).value = "";
				//add this because maybe the input will be hidden
				window.onerror = killErrors;
				try
				{
					document.getElementById('dtlst_'+idvar).focus();
				}
				catch(err)
				{
				}
				finally
				{
					window.onerror = null;
				}
				return false;
			}
		}
		if (max_permitted != null && max_permitted != "")
		{
			if (Number(set) > Number(max_permitted))
			{
				alert(document.getElementById("s_maxval").value + max_permitted);
				document.getElementById('dtlst_'+idvar).value = "";
				//add this because maybe the input will be hidden
				window.onerror = killErrors;
				try
				{
					document.getElementById('dtlst_'+idvar).focus();
				}
				catch(err)
				{
				}
				finally
				{
					window.onerror = null;
				}
				return false;
			}
		}
		return true;
	}
	return true;
}
function mainViewSetVars()
{
	MTstartServerComm();
	document.getElementById("frmdtlview").submit();
}

function dtlBttSetVars(nform)
{
	// var:can_set = permission to execute set on field
	if(document.getElementById("statusoffline") != null && 
	   document.getElementById("statusoffline").value == "false" &&
	   document.getElementById("can_set").value=='true')
	{
		var oFrm = document.getElementById("frmdtlbtt"+nform);
		if(oFrm != null)
		{
			oFrm.action = oFrm.action + getSessId();
			MTstartServerComm();
			oFrm.submit();
		}
	}
}

function dtlRenderTabParam(obj,open)
{
	var oDiv = document.getElementById("writeTabContainer");
	
	if(open != null)
	{
		if(oDiv != null)
		{
			obj = document.getElementById("imgTabRw");
			oDiv.style.visibility = "visible";
			oDiv.style.display = "block";
			obj.src = "images/lsw/close.gif";
			obj.title = document.getElementById("close").value;
		}
		return;
	}
	
	if(oDiv != null)
	{
		if(oDiv.style.visibility == "hidden") // oDiv.style.display == "none"
		{
			oDiv.style.visibility = "visible";
			oDiv.style.display = "block";
			obj.src = "images/lsw/close.gif";
			obj.title = document.getElementById("close").value;
		}
		else
		{
			oDiv.style.visibility = "hidden";
			oDiv.style.display = "none";
			obj.src = "images/lsw/open.gif";
			obj.title = document.getElementById("open").value;
		}
	}
}

function dtlStoreDeviceImg(strImg)
{
	imgDevice = strImg;
}

function dtlApplyDeviceImg()
{
	var obj = document.getElementById("tdImgDev");
	if(obj != null)
	{
		var x = obj.offsetWidth;
		var obj = document.getElementById("dtlimgdev");
		if(obj != null && obj != 'undefined')
		{
			obj.src = imgDevice;
			//obj.style.width = x+"px";
		}
	}
}

function openmorevars()
{
	if( MioAskModUser() == true )
		top.frames['manager'].loadTrx('nop&folder=rodtlview&bo=BRoDtlView&type=click&desc='+document.getElementById('topdesc').value);
}

function querysitestatus()
{
	clearTimeout(ssto); 
	CommSend('servlet/ajrefresh','GET','cmd=sitestatus','qss',false)
}

function Callback_qss()
{
	/*
	if(xmlResponse)
	{
		var sitestatus = xmlResponse.getElementsByTagName("sitestatus");
		if(xmlResponse.text=='alarm')//alert(xmlResponse.text);
		{
			document.getElementById('alarms').style.visibility = 'visible';
			//document.getElementById('alarms').style.display = 'block';
		}
		else if(xmlResponse.text=='ok')
		{
			document.getElementById('alarms').style.visibility = 'hidden';
			//document.getElementById('alarms').style.display = 'none';
		}
	}
	ssto = setTimeout("querysitestatus()",10000);
	*/
}

function goToEndOfPage(node)
{
	//	node.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.scrollTop = 1000000000; //
	node.parentNode.parentNode.parentNode.scroll = 100000000;
	//window.scrollBy(0,screen.availHeight);
}

function cmbAdapt(idv,row)
{
	//personalizzo larghezza per ogni select:
	var cmboptions = document.getElementById('dtlst_'+idv);
	var maxlength = 0;
	var optlength = 0;
	
	//valori colonna precedente:
	var tblRW = document.getElementById('tblRW');
	var value = tblRW.rows[row].cells[0].innerHTML;
	var tdLength = value.length;
	
	//dato che options[0].text='', il ctrl parte da 1
	for (cmbo = 1; cmbo < cmboptions.length; cmbo++)
	{
		optlength = cmboptions.options[cmbo].text.length;
		
		if (optlength > maxlength)
		{
			maxlength = optlength;
		}
	}
	
	// 16 = max chars visibili nelle options (tbl. var R/W) su embedded
	var newWidth = Math.ceil((maxlength/16)*100);
	
	//adatto larghezza cmbbox secondo valore colonna precedente:
	// 16 = max chars xogni riga td precedente
	// se 4 righe:
	if (tdLength > 60)
		newWidth = newWidth * 0.4;
	// se 3 righe:
	else if (tdLength > 40)
		newWidth = newWidth * 0.6;
	// se 2 righe:
	else if (tdLength > 25)
		newWidth = newWidth * 0.80;
	
	//ridimensiono solo cmbbox pi�?larghe dello spazio disponibile
	if (newWidth > 100)
	{	
		document.getElementById('cmbcontainer_'+idv).style.width = ""+newWidth+"%";
	}
}

function cmbNormalize(idv)
{
	document.getElementById('cmbcontainer_'+idv).style.width = "100%";
}

function ctrl_cmb(idv)
{
	var combos = document.getElementsByTagName('SELECT');
	var j = 0;
	var idvar = -1;
	var id = "";
	
	for (j = 0; j < combos.length; j++)
	{
		id = combos[j].getAttribute('id');
		if ((id != "dtlst_"+idv) && (id.substring(0,6)=="dtlst_"))
		{
			idvar = id.split("_")[1];
			document.getElementById('cmbcontainer_'+idvar).style.width = "100%";
		}
	}
}


function addSetFromCustomSection()
{
	var objFrmSecA = null;
	var objStandardFrm = null;
	try {
		objFrmSecA = document.getElementById("frmSecA");
		objStandardFrm = document.getElementById("frmdtl");
	}
	catch(e){
		objFrmSecA = null;
		objStandardFrm = null;
	}
	
	if(objFrmSecA != null && objStandardFrm != null)
	{
		var inputs = objFrmSecA.getElementsByTagName("input");
		if(inputs != null)
		{
			var newHidden = null;
			for(var i=0;i<inputs.length;i++) 
			{
				try 
				{
					newHidden = null;
					if(inputs[i].type == "text")
					{
						newHidden = document.createElement("input");
						newHidden.setAttribute("type", "hidden");
						newHidden.setAttribute("name", inputs[i].name);
						newHidden.setAttribute("value", inputs[i].value);
						objStandardFrm.appendChild(newHidden);
					}
				}
				catch(e) {
				}
			}
		}
	}
}
