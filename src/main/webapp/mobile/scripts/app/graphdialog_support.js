function doInit(destArgs)
{
	document.getElementById("GDtimeperiod").selectedIndex = Number(destArgs[1]);
	document.getElementById("GDyear").selectedIndex = Number(destArgs[2])-2000; // years index start from 0 (0--> 2000)
	document.getElementById("GDmonth").selectedIndex = Number(destArgs[3])-1; // months start from 0 (0--> January)
	document.getElementById("GDday").selectedIndex = Number(destArgs[4])-1; // days start from 0 (0 --> first day of the month)
	document.getElementById("GDhour").selectedIndex = Number(destArgs[5]); // no need to adjust
	document.getElementById("GDminut").selectedIndex = Number(destArgs[6]); // no need to adjust
	
	if(document.getElementById("GDtimeperiod").value == -1)
		GDchangeTimePeriod();
}

function GDchangeTimePeriod()
{
	activePrintGraph = false;
	
	if(document.getElementById("GDtimeperiod").value == -1)
	{
		//disableAction(1);
		
		document.getElementById("GDyear").disabled = true;
		document.getElementById("GDmonth").disabled = true;
		document.getElementById("GDday").disabled = true;
		document.getElementById("GDhour").disabled = true;
		document.getElementById("GDminut").disabled = true;
		
		actualMode = ZOOM_IN;
	}//if
	else
	{
		//enableAction(1);	
		
		document.getElementById("GDyear").disabled = false;
		document.getElementById("GDmonth").disabled = false;
		document.getElementById("GDday").disabled = false;
		document.getElementById("GDhour").disabled = false;
		document.getElementById("GDminut").disabled = false;

		actualMode = PLOT;
	}//else
}//changeTimePeriod

function goBack2Graph()
{
	var newArgs = new Array();
	newArgs[0] = null;
	newArgs[1] = document.getElementById("GDtimeperiod").selectedIndex;
	newArgs[2] = document.getElementById("GDyear").value;
	newArgs[3] = document.getElementById("GDmonth").value;
	newArgs[4] = document.getElementById("GDday").value;
	newArgs[5] = document.getElementById("GDhour").value;
	newArgs[6] = document.getElementById("GDminut").value;
	onCloseGraphDialog(newArgs);
}
