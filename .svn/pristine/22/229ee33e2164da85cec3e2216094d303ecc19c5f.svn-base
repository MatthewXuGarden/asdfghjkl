var cstmFolder;
var cstmResource;
var cstmDevice; 

function PVPK_goToDetail(idDevice)
{
	top.frames["manager"].loadTrx("nop&folder=dtlview&bo=BDtlView&type=click&iddev="+idDevice+"&desc=ncode01");			
}

function PVPK_goToMoreVars(sDesc)
{
	top.frames['manager'].loadTrx('nop&folder=rodtlview&bo=BRoDtlView&type=click&desc='+sDesc);
}

function PVPK_ActiveRefresh(itime)
{
	var data = preparePacket();
	CommSend2("servlet/sdk","POST",data,"cstmcb");
	if(itime===undefined)
	{
		itime = 10;
	}
	if(itime<5)
	{
		itime=5;
	}
	setTimeout("PVPK_ActiveRefresh("+itime+")",(itime*1000));
}

function PVPK_RefreshOnDemand()
{
	var data = preparePacket();
	CommSend2("servlet/sdk","POST",data,"cstmcb");
}

function Callback_cstmcb()
{
	var vars = xmlResponse.getElementsByTagName("var");
	var spans = document.getElementsByTagName("span");
	
	for(var i=0;i<vars.length;i++) 
	{
		try {
			if(vars[i].childNodes[0]!=null)
			{
				document.getElementById(vars[i].attributes[0].value).innerHTML = vars[i].childNodes[0].nodeValue;
			}
			else
			{
				document.getElementById(vars[i].attributes[0].value).childNodes[0].nodeValue = "***";
			}
		}
		catch(e) {};
	}
	
	if(document.getElementById("alarms")===undefined || document.getElementById("alarms")==null)
		return;

	if (window.ActiveXObject)
	{
		// IE xml/xslt transform
		try
		{
			xslRef = new ActiveXObject("Microsoft.XMLDOM");
			xslRef.async = false;
			xslRef.load("custom/dtlview/"+cstmDevice+"/stylesheet.xsl");
			var xmlRef = xmlResponse;
			document.getElementById("alarms").innerHTML  = xmlRef.transformNode(xslRef);
			document.getElementById("alarms").innerHTML;
		}
		catch(e)
		{
			//alert("Errore:\n"+e.message);
		}
	}
	else if( document.implementation && document.implementation.createDocument ) {
		// Firefox xml/xslt transform
		var xsltDoc = document.implementation.createDocument("", "", null);
		xsltDoc.async = false;
		
//		xsltDoc.load("custom/dtlview/"+cstmDevice+"/stylesheet.xsl");
		var url = "custom/dtlview/"+cstmDevice+"/stylesheet.xsl"
		try{
			xsltDoc.load(url);
		}catch (ex){
//			alert(ex.message) ;
			var xhr = new XMLHttpRequest(); 
			xhr.open("GET", url, false); 
			xhr.send(null); 
			xsltDoc = xhr.responseXML.documentElement; 
			
		}
		var xsltProc = new XSLTProcessor();
		xsltProc.importStylesheet(xsltDoc);
		var xmlDoc = xsltProc.transformToDocument(xmlResponse);
		var serializer = new XMLSerializer();
		document.getElementById("alarms").innerHTML = serializer.serializeToString(xmlDoc);		
	}	
	else
	{
		alert("Your browser can't handle this script (arkustom.js)");
	}
}

function preparePacket()
{
	var spans = document.getElementsByTagName("span");
	var ret = new Array();
	for(var i=0; i<spans.length; i++)
	{
		ret.push(spans[i].id + ",");
	}
	return ret.join("");	
}

function PVPK_addButtons()
{
	var divbtt = document.getElementById("sdkActionButton");
	if(divbtt===undefined || divbtt==null) return;
	
	//if(cstmFolder!="dtlview" || cstmResource!="SubTab1.jsp") return;
	if((cstmFolder!="dtlview" && cstmFolder!="mstrmaps") || (cstmResource!="SubTab1.jsp" && cstmResource!="SubTab2.jsp")) return;
	
	var refresha = document.getElementById("refresha").value;
	var htmlbtt = "";
	htmlbtt += ""+
	"<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\">"+
		"<tbody>"+
		"<tr valign=\"middle\" align=\"left\">"+
			"<td valign=\"bottom\" align=\"center\" id=\"subtab3name\" title=\""+refresha+"\" style=\"cursor: pointer;\">"+
				"<div class=\"actBttCustom\" onclick=\"customRefresh();\" id=\"ActBttDivEn1\">"+
					"<img class=\"actBttImg\" border=\"0\" align=\"middle\" src=\"images/actions/refresh_on.png\" id=\"ActBttImgEn1\"/>"+
					"<div id=\"ActBttDivTxtEn1\" class=\"actBttDivTxt\">"+refresha+"</div>"+
				"</div>"+
			"</td>"+
			"<td style=\"width: 2px;\">"+
				"<img border=\"0\" src=\"images/tab/tabbar1.png\"/>"+
			"</td>";
	var Imposta = document.getElementById("imposta").value;
	var write_permission = document.getElementById("write_permission");
	if(write_permission != undefined)
		write_permission = write_permission.value;
	else
		write_permission = true;
	if (document.getElementById("formSettableVars") && write_permission == "true")
	{
		htmlbtt += "<td valign=\"bottom\" align=\"center\" id=\"subtab3name\" title=\""+Imposta+"\" style=\"cursor: pointer;\">"+
				"<div class=\"actBttCustom\" onclick=\"customPost();\" id=\"ActBttDivEn1\">"+
					"<img class=\"actBttImg\" border=\"0\" align=\"middle\" src=\"images/actions/params_on.png\" id=\"ActBttImgEn1\"/>"+
					"<div id=\"ActBttDivTxtEn1\" class=\"actBttDivTxt\">"+Imposta+"</div>"+
				"</div>"+
			"</td>"+
			"<td style=\"width: 2px;\">"+
				"<img border=\"0\" src=\"images/tab/tabbar1.png\"/>"+
			"</td>";
	}
	
	htmlbtt += ""+"</tr>"+
		"</tbody>"+
	"</table>";
	divbtt.innerHTML=htmlbtt;
}

function customRefresh()
{
	var target_device = "";
	
	if (document.getElementById("combo_dev")!=null)
		target_device = document.getElementById("combo_dev").value;
	
	top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=redirect&iddev='+target_device+'&desc=ncode01');
}

function customPost()
{
	var oForm = document.getElementById("formSettableVars");
	oForm.method = "POST";
	oForm.action = "servlet/master;"+top.frames['manager'].getSessionId()+"?cmdk=sdks";
	MTstartServerComm();
	oForm.submit();
}

function PVPK_setData(obj)
{
	var oForm = obj.form;
	oForm.method = "POST";
	oForm.action = "servlet/master;"+top.frames['manager'].getSessionId()+"?cmdk=sdks";
	MTstartServerComm();
	oForm.submit();
}

function PVPK_SetCustom(sFolder,sResource,sDevice)
{
	cstmFolder = sFolder;
	cstmResource = sResource;
	cstmDevice = sDevice;
}

function PVPK_SetButtonValue(nform)
{
	try {
		var ret = customCheckBeforSend(nform);
		if(!ret)
			return;
	}
	catch(e){}
	
	var oFrm = document.getElementById("frmdtlbtt"+nform);
	
	if(oFrm != null)
	{
		oFrm.action = oFrm.action + getSessId()+"?cmdk=sdks";
		MTstartServerComm();
		oFrm.submit();
	}
}

/* Funzione per settare l'ora di uno strumento con l'ora attuale
 * passando come parametri ora,minuti e relativi code pi? code variabile di conferma
 */

function syncroTime(hh_id,mm_id)
{
	var now = new Date();
	var current_hh = now.getHours();
	var current_mm = now.getMinutes();
	var div = document.getElementById("syncro");
	var form = "<form name='f_syncro' id='f_syncro' action='servlet/master;' method='post'>";
	form+= "<input type='hidden' name='dtlst_"+hh_id+"' value='"+current_hh+"'/>";
	form+= "<input type='hidden' name='dtlst_"+mm_id+"' value='"+current_mm+"'/>";
	form+= "<input type='hidden' name='syncronow' value='ok'/>";
	form+= "</form>";
	div.innerHTML=form;
  
	var oFrm = document.getElementById("f_syncro");
	if(oFrm != null)
	{
		oFrm.action = oFrm.action + getSessId();
		MTstartServerComm();
		oFrm.submit();
	}
}

function setClock(idack)
{
	var set = document.getElementById("dosyncro").value;
	if (set=="ok")
	{
		var div = document.getElementById("syncro");
	  	var form = "<form name='f_syncro' id='f_syncro' action='servlet/master;' method='post'>";
		form+= "<input type='hidden' name='dtlst_"+idack+"' value='1'/></form>";
		div.innerHTML=form;
		var oFrm = document.getElementById("f_syncro");
		if(oFrm != null)
		{
			oFrm.action = oFrm.action + getSessId();
			MTstartServerComm();
			oFrm.submit();
		}
	}
}

/**
 * SINCE PVP 2.0
 * Check custom SDK MIN MAX variable value
 */
function sdk_checkMinMaxValue(obj,iMin,iMax)
{
	var iVal = undefined;
	try {
		if(obj.value == "")
			return false;
		iVal = Number(obj.value);
	}
	catch(e){
		obj.value = "";
		return false;
	}
	
	if(iVal != undefined)
	{
		if(iVal < iMin)
		{
			var msg = "";
			try {
				msg = document.getElementById("s_minval").value;
			}
			catch(e){}
			alert(msg + iMin);
			obj.value = "";
			obj.focus();
			return false;
		}
		if(iVal > iMax)
		{
			var msg = "";
			try {
				msg = document.getElementById("s_maxval").value;
			}
			catch(e){}
			alert(msg + iMax);
			obj.value = "";
			obj.focus();
			return false;
		}
	}		
}


// call this fn to synchronize device clock with supervisor clock
// idDevice - Device Id
// codeHour - variable code for hour
// codeMinute - variable code for minute
// codeConfirmation - optional
function timeSync(idDevice, codeHour, codeMinute, codeConfirmation)
{
	var params = "cmd=timesync"
		+ "&iddev=" + idDevice
		+ "&codehour=" + codeHour
		+ "&codeminute=" + codeMinute;
	if( codeConfirmation )
		params += ("&codeconfirmation=" + codeConfirmation);
	CommSend("servlet/ajrefresh", "POST", encodeURI(params), "timeSync", true);	
}


function Callback_timeSync(response)
{
	var result = response.getElementsByTagName("timesync")[0].childNodes[0].nodeValue;
	if( result != "OK" )
		alert(result);
}
