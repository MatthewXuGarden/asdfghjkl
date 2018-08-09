var IO_filter = "wav";
var IO_MaxTry = 10;
var IO_MaxRetry = 15;

var RELAY_LIMIT = 50;
var RELAY_OFFSET = 0;
var REFRESHTIME = 200;

function IO_FaxLoad()
{
	IO_showMsg();
	enableAction(3);
	IoEnableTechFS();
}

var listProviders;
function IO_SmsLoad()
{
	listProviders = new Array();
	var iosmsprovider = document.getElementById("iosmsprovider");
	for(var i = 0; i < iosmsprovider.options.length; i++) {
		listProviders.push(iosmsprovider.options[i]);
		if( iosmsprovider.options[i].text == "Direct SMS Wavecom" )
			iosmsprovider.options[i].text = "Direct SMS";
	}
	IO_SmsProviders();
	
	IO_showMsg();
	enableAction(3);
	IoEnableTechFS();
}

function IO_SmsProviders()
{
	var iosmsprovider = document.getElementById("iosmsprovider");
	iosmsprovider.length = 0;

	var iomodemtypeG = document.getElementById("iomodemtypeG");	
	for(var i = 0; i < listProviders.length; i++) {
		if( iomodemtypeG.checked ) {
			if( listProviders[i].text.indexOf("Direct SMS") == 0 )
				iosmsprovider.options[iosmsprovider.length] = listProviders[i];
		}
		else if( listProviders[i].text.indexOf("Direct SMS") != 0 ) {
			iosmsprovider.options[iosmsprovider.length] = listProviders[i];
		}
	}
}

function IO_MailLoad()
{
	IO_showMsg();
	enableAction(3);
	IoEnableTechE();
}

function IoEnableTechFS()
{
	var oSelDev = document.getElementById("iomodeml");
	var oSelAddr = document.getElementById("numAddr").value;

	if((oSelDev != null) && (oSelAddr > 0))
	{
		if(oSelDev.selectedIndex != 0)
		{
			enableAction(2);
			enableAction(1);
		}
		else
		{
			disableAction(2);
			disableAction(1);
		}
	}
}

function IoEnableTechE()
{
	var oSmtp = document.getElementById("iomailsmtp");
	var oSelAddr = document.getElementById("numAddr").value;
	
	if((oSmtp != null) && (oSelAddr > 0))
	{
		if(oSmtp.value != "")
		{
			enableAction(2);
			enableAction(1);
		}
		else
		{
			disableAction(2);
			disableAction(1);
		}
	}
}


function IoTechEEnc(bTls)
{
	document.getElementById("iomailport").value = bTls ? 587 : 25;
}


function checkParamConf()
{
	var oTry 	= document.getElementById("iotrynum");
	var oRetry 	= document.getElementById("ioretryafter");
		
	if((oTry != null) && (Number(oTry.value) > IO_MaxTry) || (oTry.value==""))
		return false;
		
	if((oRetry != null) && (Number(oRetry.value) > IO_MaxRetry) || (oRetry.value==""))
		return false;
		
	return true;
}

function IO_saveFaxCfg()
{   
	if(checkParamConf())
	{
	    if(oDyn != null)
			oDyn.buildPost("BookTablePost");
		var ofrm = document.getElementById("frmcfgfax");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
	else 
		alert(document.getElementById("checkconf").value);
}

function IO_saveSmsCfg()
{
	setLabelProvider();
	if(checkParamConf())
	{
		var iosmsprovider = document.getElementById("iosmsprovider");
		if( iosmsprovider.options[iosmsprovider.selectedIndex].text == "Direct SMS - Extended charset" )
			alert(document.getElementById("sms70alert").value);
		if(oDyn != null)
			oDyn.buildPost("BookTablePost");
		var ofrm = document.getElementById("frmcfgsms");
		if(ofrm != null) {
			MTstartServerComm();
			ofrm.submit();
		}
	}
	else
		alert(document.getElementById("checkconf").value);
}

function IO_changeSMS(obj)
{
	var iosmscall = document.getElementById("iosmscall");
	if(obj.value == "G") {
		iosmscall.disabled = true;
	} else {
		iosmscall.disabled = false;
    }
	IO_SmsProviders();
}

function IO_saveMailCfg()
{
	
	var mailInput = document.getElementById("iomailsender");
	if(mailInput != null)
	{
		if(checkOnlyMail(mailInput) == false)
		{
			return;
		}
	}
	var t = document.getElementById("iomailtypeD");
	var doit = true;
	if(t.checked)
	{
		var username = document.getElementById("iomailsenderuser").value;
		if(username == null || username =="" ){
			alert( document.getElementById("requestUsername").value  );
			return ;
		}
		var password = document.getElementById("iomailsenderpass").value;
		if(password == null || password == ""){
			alert( document.getElementById("requestPassword").value  );
			return ;
		}
		if(!checkParamConf())
		{
			doit = false;
			alert(document.getElementById("controlconfig").value);
		}
	}

	if(doit) {
		if(oDyn != null)
			oDyn.buildPost("BookTablePost");
		var ofrm = document.getElementById("frmcfgmail");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function IO_saveRasCfg()
{
	var checkRis = true;
	
	checkRis = checkParamConf();
		
	if(checkRis)
	{   
	    if(oDyn != null)
	    {
			oDyn.buildPost("BookTablePost");
		}
		var ofrm = document.getElementById("frmcfgras");
		if(ofrm != null)
		{
			alert(document.getElementById("msgrasreboot").value);
			MTstartServerComm();
			ofrm.submit();
		}
	}
	else
	{
	  alert(document.getElementById("checkconf").value);
	}
}

function IO_saveEventCfg()
{
	if(IO_applyFilter())
	{
		var ofrm = document.getElementById("frmcfgevent");
		document.getElementById("cmd").value = "save";
		if(ofrm != null) {
			MTstartServerComm();
			ofrm.submit();
		}
	}
	else
		alert(document.getElementById("msgtouser").value);
}

function IO_delEventCfg()
{
	if (document.getElementById("iocfgevpath").value != "")
	{
		if (confirm(document.getElementById("confirmdel").value))
		{
			var ofrm = document.getElementById("frmcfgevent");
			document.getElementById("cmd").value = "remove";
			
			// make sure the file path is empty
			var e = document.getElementById("iocfgsound");
			if( e.value.length > 0 )
				if( e.type == "file" )
					document.getElementById("diviocfgsound").innerHTML =
						"<input id='iocfgsound' name='iocfgsound' class='"
						+ e.className + "' size='" + e.size + "' type='text' value=''>";
				else
					e.value = "";
			
			if(ofrm != null) {
				MTstartServerComm();
				ofrm.submit();
			}
		}
	}
	else alert(document.getElementById("nosoundfile").value);
}

function IO_saveRelayCfg()
{
	var ofrm = document.getElementById("frmcfgrelay");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function IO_clearRelayResetField(idx)
{
	var oy = document.getElementById("RR_"+idx);
	var ot = document.getElementById("RT_"+idx);
	if(ot != null && oy != null && oy.value != "T")
	{
		ot.value = "";
	}
	
	if((oy != null) && (oy.value == "T") && (ot != null))
	{
		ot.disabled = false;
		ot.focus();
	}
	else
	{
		ot.disabled = true;
	}
}

function IO_applyFilter()
{
	var sPath = document.getElementById("iocfgsound").value;
	var bRet = false;
	if((sPath != "") && (sPath.length > 3) && 
	  ((sPath.substring(sPath.length-3,sPath.length)).toLowerCase() == IO_filter))
		bRet = true;
	
	if(bRet) {
		document.getElementById("iocfgevpath").value = sPath;
    }
	return bRet;
}

function IO_addContatToBook(ismail)
{
	var label = "";
	var addre = "";
	label = document.getElementById("ioflabel");	
	addre = document.getElementById("iofaddress");
	
	//2010-01-06, add mail check
	if(ismail != null && ismail==true)
	{
		if(checkOnlyMail(addre) == false)
		{
			return;
		}
	}
	//end
	if(label.value != "" && addre.value != "" && oDyn != null)
		oDyn.add(new Array(label.value,addre.value));
	
	label.value = "";
	addre.value = "";
	addre.focus();
}

function IO_delContatToBook(actiontype)
{
	var msg = document.getElementById("iomsguser").value;
	if(confirm(msg)) {
		if(oDyn != null)
			oDyn.remove();
	}
	
	switch (actiontype)
	{
		case "E":IO_saveMailCfg();break;
		case "S":IO_saveSmsCfg();break;
		case "F":IO_saveFaxCfg();break;
		case "D":IO_saveRasCfg();break;
	}
}

function setLabelProvider()
{
	var slProv = document.getElementById("iosmsprovider");
	if(slProv != null) {
		var strProvider = slProv.options[slProv.selectedIndex].innerHTML;
		if( strProvider == "Direct SMS")
			strProvider += " Wavecom";
		document.getElementById("iolblprovider").value = strProvider;
	}
}

function IO_changeMailSend(obj)
{
	var odiv = document.getElementById("iomailprovider");
	var iotrynum = document.getElementById("iotrynum");
	var ioretryafter = document.getElementById("ioretryafter");	
	if(obj.value == "L") {
		odiv.disabled = true;
		iotrynum.disabled = false;
		ioretryafter.disabled = false;
	} else {
		odiv.disabled = false;
		iotrynum.disabled = false;
		ioretryafter.disabled = false;
    }
}

function IO_showMsg()
{
	var smsg = document.getElementById("setiomsg").value;
	if(smsg != "")
		alert(smsg);
}

function IO_onload()
{
	relay_AJAXinit();
}

function relay_AJAXinit()
{
	var param = "cmd=init&limit="+RELAY_LIMIT+"&offset="+RELAY_OFFSET;
	new AjaxRequest("servlet/ajrefresh", "POST", param,
			Callback_relay_init, false);
}

function Callback_relay_init(xml)
{
	var container = document.getElementById("relaycontainer");
	var relays = xml.getElementsByTagName("relay");
	var manual = document.getElementById("manual").value;
	var automatic = document.getElementById("automatic").value;
	var timed = document.getElementById("timed").value;
	var disabled = document.getElementById("disabled").value;
	var OnScreenKey = document.getElementById("OnScreenKey").value;
	for(var i=0;i<relays.length;i++)
	{
		if(RELAY_OFFSET == 0 && i == 0)
		{
			var relay = document.getElementById("relay").value;
			var attivo = document.getElementById("attivo").value;
			var reset = document.getElementById("reset").value;
			var time = document.getElementById("time").value;
			var seconds = document.getElementById("seconds").value;
			var show = document.getElementById("show").value;
			var r_head = container.insertRow(container.rows.length);
			var ch1 = r_head.insertCell();
			ch1.className = "th";
			ch1.style.width = "35%";
			ch1.style.textAlign = "center";
			ch1.innerHTML = "<b>"+relay+"</b>";
			var ch2 = r_head.insertCell();
			ch2.className = "th";
			ch2.style.width = "18%";
			ch2.style.textAlign = "center";
			ch2.innerHTML = "<b>"+attivo+"</b>";
			var ch3 = r_head.insertCell();
			ch3.className = "th";
			ch3.style.width = "18%";
			ch3.style.textAlign = "center";
			ch3.innerHTML = "<b>"+reset+"</b>";
			var ch4 = r_head.insertCell();
			ch4.className = "th";
			ch4.style.width = "18%";
			ch4.style.textAlign = "center";
			ch4.innerHTML = "<b>"+time+"("+seconds+")</b>";
			var ch5 = r_head.insertCell();
			ch5.className = "th";
			ch5.style.textAlign = "center";
			ch5.innerHTML = "<b>"+show+"</b>";
		}
		var idrelay = relays[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
		var r = container.insertRow(container.rows.length);
		r.className = (i%2==0?"Row1":"Row2");
		var c1 = r.insertCell();
		c1.className = "standardTxt";
		c1.innerHTML = relays[i].getElementsByTagName("des")[0].childNodes[0].nodeValue;

		var c2 = r.insertCell();
		c2.className = "td";
		c2.innerHTML = "<select "+disabled+" name='RA_"+idrelay+"' id='RA_"+idrelay+"' class='standardTxt'>"+
			"<option value='0' "+(relays[i].getElementsByTagName("a_state")[0].childNodes[0].nodeValue=="0"?"selected":"")+"> 0 </option>"+
			"<option value='1' "+(relays[i].getElementsByTagName("a_state")[0].childNodes[0].nodeValue=="1"?"selected":"")+"> 1 </option>"+
			"</select>";
		c2.style.textAlign = "center";
		var c3 = r.insertCell();
		c3.className = "td";
		c3.innerHTML = "<select "+disabled+" name='RR_"+idrelay+"' id='RR_"+idrelay+"' class='standardTxt' onchange='IO_clearRelayResetField("+idrelay+");'>"+
						 "<option value='M' "+(relays[i].getElementsByTagName("reset_tp")[0].childNodes[0].nodeValue=="M"?"selected":"")+">"+manual+"</option>"+
						 "<option value='A' "+(relays[i].getElementsByTagName("reset_tp")[0].childNodes[0].nodeValue=="A"?"selected":"")+">"+automatic+"</option>"+
						 "<option value='T' "+(relays[i].getElementsByTagName("reset_tp")[0].childNodes[0].nodeValue=="T"?"selected":"")+">"+timed+"</option>"+
						 "</select>";
		c3.style.textAlign = "center";
		var c4 = r.insertCell();
		c4.className = "td";
		var t_disabled = disabled;
		if(disabled == "" && relays[i].getElementsByTagName("reset_tp")[0].childNodes[0].nodeValue!="T")
		{
			t_disabled = "disabled";
		}
		c4.innerHTML = "<input "+t_disabled+" type='text' class='"+(OnScreenKey=="true"?"keyboardInput":"standardTxt")+"' size='4' maxlength='4' id='RT_"+idrelay+"' name='RT_"+idrelay+"'"+ 
			 		   " value='"+ (relays[i].getElementsByTagName("reset_tm")[0].childNodes[0].nodeValue=="-1"?"":relays[i].getElementsByTagName("reset_tm")[0].childNodes[0].nodeValue)+"'"+
			 		   " onblur='onlyNumberOnBlur(this);' onkeydown='checkOnlyNumber(this,event);'/>";
		c4.style.textAlign = "center";
		var c5 = r.insertCell();
		c5.className = "td";
		c5.innerHTML = "<input "+disabled+(relays[i].getElementsByTagName("show")[0].childNodes[0].nodeValue=="1"?" checked ":"")+" type='checkbox' id='RS_"+idrelay+"' name='RS_"+idrelay+"'/>";
		c5.style.textAlign = "center";
	}
	if(relays.length==RELAY_LIMIT)
	{
		RELAY_OFFSET += RELAY_LIMIT;
		setTimeout("relay_AJAXinit()", REFRESHTIME);
	}
	else
	{
		if(OnScreenKey == "true")
		{
			buildKeyboardInputs();
		}
		RELAY_OFFSET = 0;
		enableAction(1);
	}
}
function IO_techFaxCfg()
{
	if(confirm(document.getElementById("msgautomatic").value))
	{
		document.getElementById("executetechact").value = "T";
		IO_saveFaxCfg();
	}
}

function IO_ruleFaxCfg()
{
	if(confirm(document.getElementById("msgautomatic2").value))
	{
		document.getElementById("executetechact").value = "R";
		IO_saveFaxCfg();
	}
}

function IO_techSmsCfg()
{
	if(confirm(document.getElementById("msgautomatic").value))
	{
		document.getElementById("executetechact").value = "T";
		IO_saveSmsCfg();
	}
}

function IO_ruleSmsCfg()
{
	if(confirm(document.getElementById("msgautomatic2").value))
	{
		document.getElementById("executetechact").value = "R";
		IO_saveSmsCfg();
	}
}

function IO_techMailCfg()
{
	if(confirm(document.getElementById("msgautomatic").value))
	{
		document.getElementById("executetechact").value = "T";
		IO_saveMailCfg();
	}
}

function IO_ruleMailCfg()
{
	if(confirm(document.getElementById("msgautomatic2").value))
	{
		document.getElementById("executetechact").value = "R";
		IO_saveMailCfg();
	}
}

function IO_savePrinterCfg()
{
	var ofrm = document.getElementById("frmcfgprinter");
	if( ofrm != null ) {
		MTstartServerComm();
		ofrm.submit();
	}
}


function onWindowLoaded()
{
	// render correctly in case of a strict doctype
	var divContainer = document.getElementById("container");
	if( divContainer ) {
		divContainer.className = "";
		divContainer.setAttribute("style", "padding-left:20px;");
		if( screen.width <= 1024 && screen.height <= 768 )
			divContainer.style.height = "425px";
		else
			divContainer.style.height = "700px";
	}
}
