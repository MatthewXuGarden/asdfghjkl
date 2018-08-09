// Variables
var ONColor = 0x00FF00;
var OFFColor = 0xEAEAEA;
var DisabledColor = 0x0000C0;
var ALARMColor = 0xC00000;

function WyOnLoad()
{
	enableAction(1);
	WyStartSetLed();
	//WyResizeImage();
	WSetDataMap();
	WyInitGauge();
	WySetValue();
}

function WSetDataMap()
{
	WprvSetLedStatusPro(document.getElementById("pstatus").value);
	WprvSetStatusProLbl(document.getElementById("lstatus").value);
	WprvSetColorStatus(document.getElementById("pcolor").value);
	WprvSetColorLbl(document.getElementById("lcolor").value);
}

function WprvSetLedStatusPro(value) {
	document.getElementById("wstatusproc").src = "images/led/L"+value+".gif";
	WActiveButton(value);
}

function WprvSetStatusProLbl(value) {
	document.getElementById("wstatuslbl").innerHTML = value;
}

function WprvSetColorStatus(value) {
	document.getElementById("colorState").style.backgroundColor = value;
}

function WprvSetColorLbl(value) {
	document.getElementById("colorState").innerHTML = value;
}

function WyStartSetLed()
{
	setTimeout("WySetLed(document.getElementById('LedStato'))",1000);
}

function WActiveButton(value)
{
	var isreg = document.getElementById("wisreg").value;
	
	if(isreg != null && isreg == "TRUE")
	{
		if(value == "0")
		{
			document.getElementById("WyCoolBtt").disabled = false;
			document.getElementById("WyHeatBtt").disabled = false;
			document.getElementById("WyStopBtt").disabled = true;
		}
		else
		{
			document.getElementById("WyCoolBtt").disabled = true;
			document.getElementById("WyHeatBtt").disabled = true;
			document.getElementById("WyStopBtt").disabled = false;
		}
	}
	else
	{
		document.getElementById("WyCoolBtt").disabled = true;
		document.getElementById("WyHeatBtt").disabled = true;
		document.getElementById("WyStopBtt").disabled = true;
	}
}

function WySetLed(led)
{	
	if(led != null)
	{
		if (led.name <= 1)
		{
			led.SetVariable('obj.Value', led.name);
			led.SetVariable('obj.ONColor', ONColor);
			led.SetVariable('obj.OFFColor', OFFColor);	
			enableAction(2);
		}
		else if (led.name <= 2)
		{
			led.SetVariable('obj.Value', led.name/2);
			led.SetVariable('obj.ONColor', ALARMColor);
			led.SetVariable('obj.OFFColor', OFFColor);
			enableAction(2);
		}
		else if (led.name <= 3)
		{
			led.SetVariable('obj.Value', led.name/3);
			led.SetVariable('obj.ONColor', DisabledColor);
			led.SetVariable('obj.OFFColor', OFFColor);
		}
		
		if(led.name == 0)
			disableAction(2);		
	}
}

function WyResizeImage()
{
	var obj = document.getElementById("wyimgdev");
	if(obj != null && obj != 'undefined')
	{
		var x = Number(obj.width);
		var y = Number(obj.height);
		var prop = y/x;
		x = x-50;
		y = parseInt(prop*x);
		if(obj.style != null)
		{
			if(x > 0 && y > 0) 
			{
				obj.style.width = x+"px";
				obj.style.height = y+"px";
			}
		}
	}
}

function WyStartCool() {
	WyPrvStart(0);	
}

function WyStartHeat() {
	WyPrvStart(1);
}

function WyPrvStart(sState)
{
	var sP = document.getElementById("dtlst_0").value;
	var sH = document.getElementById("dtlst_1").name;
	var sL = document.getElementById("dtlst_2").name;
	
	if(sH != null)
		sH = sH.substring("dtlst_".length);
	
	if(sL != null)
		sL = sL.substring("dtlst_".length);
		
	var sT = document.getElementById("wtemprate").value;
	if(sP != null && sP != "" && sT != null && sT != "")
	{
		document.getElementById("whcmd").value = ""+sState;
		document.getElementById("whidah").value = sH;
		document.getElementById("whidal").value = sL;
		
		if(document.getElementById("frmwset"))
		{
			MTstartServerComm();
			document.getElementById("frmwset").submit();
		}
	}
}

function WyStopCool()
{
	document.getElementById("whcmd").value = "2";
	if(document.getElementById("frmwset"))
	{
		MTstartServerComm();
		document.getElementById("frmwset").submit();
	}
}

function WySendData()
{
	MTstartServerComm();
	document.getElementById("frmwset").submit();
}

/*********************************************************************************
 * Gestione Gauge:
 * - MinScale
 * - MaxScale
 * - Unit
 * - HiAlr
 * - LoAlr
 * - Value
 */

function WyInitGauge()
{
	var og = WyPrvGetGauge()
	var setAlr = true;
	if(og != null)
	{
		var sp = Number(document.getElementById("wst").value);
		var ah = Number(document.getElementById("wal").value);
		var al = Number(document.getElementById("wah").value);
		if(al == 0 && ah == 0)
			setAlr = false;
		
		al = (sp-al);
		ah = (sp+ah);
		og.SetVariable('obj.MinScale',(al-10));
		og.SetVariable('obj.MaxScale',(ah+10));
		og.SetVariable('obj.Unit',"");
		
		if(setAlr)
		{
			og.SetVariable('obj.HiAlr',ah);
			og.SetVariable('obj.LoAlr',al);
		}
	}
}

function WySetValue()
{
	var og = WyPrvGetGauge()
	if(og != null)
		og.SetVariable('obj.Value',Number(document.getElementById("wtp").value));
}

function WyPrvGetGauge()
{
	return document.getElementById("GaugeTemp");
}

function WyBttSetVars(obj)
{
	try
	{
		var idVar = obj.id.substring("bttdtlst_".length);
		var hobj = document.getElementById("h"+obj.id);
		var odiv = document.getElementById("containerwhide");
		if(hobj != null)
		{
			odiv.innerHTML = "<input type='hidden' value='"+hobj.value+"' name='dtlst_"+idVar+"'>";		
			var oFrm = document.getElementById("frmwsetbtt");
			if(oFrm != null)
			{
				MTstartServerComm();
				oFrm.submit();
			}
		}
	}
	catch(e){}
}