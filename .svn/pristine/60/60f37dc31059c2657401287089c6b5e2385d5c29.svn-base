var lineSelected = null;
var clickState = 0;

function onLoadRule()
{
	if (document.getElementById("description_txt").disabled==false)
		document.getElementById("description_txt").focus();
	
	enableAction(1);
	
	if (document.getElementById("cmd").value=="modify_rule")
	{
		enableAction(3);
		disableAction(1);
	}
	
	if(document.getElementById("chkdbl").value == "T")
		alert(document.getElementById("msgdouble").value);
	
	if ((document.getElementById("rule_removed")) && (document.getElementById("rule_removed").value == "NO"))
		alert(document.getElementById("not_removed").value);
}

function setFocus_alrsched()
{
	if (!document.getElementById("description_txt").disabled)
	document.getElementById("description_txt").focus();
	enableAction(1);
	if (document.getElementById("isremoved").value=="NO")
	{
		alert(document.getElementById("actionnotremoved").value);
	}
	
	// Check duplicate
	var msg = document.getElementById("duplicatecode").value;	
	if(msg != null && msg == "T")
	{
		alert(document.getElementById("duplicatecodemsg").value);
	}
}


function selectedLine(row)
{
	idLine = Lsw0.mData[row][2];
	if (idLine==null) return true;
	if (lineSelected==idLine) 
	{
		if ((clickState==null)||(clickState==0))
		{
			clickState=1;
			disableAction(1);
			enableAction(2);
			enableAction(3);
			enableAction(4);
			document.getElementById("description_txt").focus();
			document.getElementById("description_txt").value=Lsw0.mData[row][4];
			
		}
		else
		{
		clickState=0;
		enableAction(1);
		disableAction(2);
		disableAction(3);
		disableAction(4);
		document.getElementById("description_txt").value="";
		document.getElementById("description_txt").focus();
		}
	}
	else
	{
		clickState=1;
		disableAction(1);
		enableAction(2);
		enableAction(3);
		enableAction(4);
		document.getElementById("description_txt").focus();
		document.getElementById("description_txt").value=Lsw0.mData[row][4];
		
	}
	lineSelected= idLine;
}

function selectedLineRule(idLine)
{
	if (idLine==null) return true;
	if (lineSelected==idLine) 
	{
		if ((clickState==null)||(clickState==0))
		{
			clickState=1;
			disableAction(1);
			enableAction(2);
			disableAction(3);
			enableAction(4);
			disab_setRule();
		}
		else
		{
		clickState=0;
		enableAction(1);
		disableAction(2);
		disableAction(3);
		disableAction(4);
		enab_setRule();
		}
	}
	else
	{
		clickState=1;
		disableAction(1);
		enableAction(2);
		disableAction(3);
		enableAction(4);
		disab_setRule();
	}
	lineSelected= idLine;
}

function disab_setRule()
{
	document.getElementById("description_txt").value="";
	document.getElementById("description_txt").disabled=true;
	document.getElementById("enabled").checked=false;
	document.getElementById("enabled").disabled=true;
	document.getElementById("idcondition").selectedIndex=0;
	document.getElementById("idcondition").disabled=true;
	document.getElementById("idtimeband").selectedIndex=0;
	document.getElementById("idtimeband").disabled=true;
	document.getElementById("idaction").selectedIndex=0;
	document.getElementById("idaction").disabled=true;
	document.getElementById("delay").value="";
	document.getElementById("delay").disabled=true;
}

function enab_setRule()
{
	document.getElementById("description_txt").disabled=false;
	document.getElementById("description_txt").focus();
	document.getElementById("enabled").disabled=false;
	document.getElementById("enabled").checked=true;
	document.getElementById("idcondition").disabled=false;
	document.getElementById("idtimeband").disabled=false;
	document.getElementById("idaction").disabled=false;
	document.getElementById("delay").disabled=false;
}



//AZIONI per TAB RULE,CONDITIONS,TIMEBANDS,ACTION

//RULE
function new_rule()
{
	if (document.getElementById("description_txt").value=="")
	{
		alert(document.getElementById("nodesc").value);
		return true;
	}
	
	if (document.getElementById("idcondition").value==0)
	{
		alert(document.getElementById("nocond").value);
		return true;
	}
	
	if (document.getElementById("idtimeband").value==0)
	{
		alert(document.getElementById("notimeband").value);
		return true;
	}
		
	if (document.getElementById("idaction").value==0)
	{
		alert(document.getElementById("noaction").value);
		return true;
	}
		
	if (document.getElementById("delay").value=="")
	{
		document.getElementById("delay").value=0;
	}
		
	document.getElementById("cmd").value="new_rule";
	var ofrm = document.getElementById("frm_rules");
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}

function remove_rule()
{
	if (confirm(document.getElementById("confirmruledel").value))
	{
		if (lineSelected<0)
		{
			alert(document.getElementById("noremrulefromide").value);
		}
		else if (lineSelected!=null)
		{
			document.getElementById("idrule").value=lineSelected;
			document.getElementById("cmd").value="remove_rule";
			var ofrm = document.getElementById("frm_rules");
			if(ofrm != null)
				MTstartServerComm();
			ofrm.submit();
		}
	}
}

function set_rule()
{
	var idrule = document.getElementById("idrule").value;
	if (idrule>0)
	{
		if (document.getElementById("description_txt").value=="")
		{
			alert(document.getElementById("nodesc").value);
			return true;
		}
		
		if (document.getElementById("idcondition").value==0)
		{
			alert(document.getElementById("nocond").value);
			return true;
		}
		
		if (document.getElementById("idtimeband").value==0)
		{
			alert(document.getElementById("notimeband").value);
			return true;
		}
			
		if (document.getElementById("idaction").value==0)
		{
			alert(document.getElementById("noaction").value);
			return true;
		}
				
		if (document.getElementById("delay").value=="")
		{
			document.getElementById("delay").value=0;
		}
	}
	document.getElementById("cmd").value="set_rule";
	var ofrm = document.getElementById("frm_rules");
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}

function modifyRule(idrule)
{
	document.getElementById("cmd").value="modify_rule";
	document.getElementById("idrule").value=idrule;
	var ofrm = document.getElementById("frm_rules");
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}




//ACTION
function new_action()
{
	if (document.getElementById("description_txt").value=="")
	{
		alert(document.getElementById("nodescription").value);
	}
	else
	{
		document.getElementById("cmd").value="new_action";
		var ofrm = document.getElementById("frm_rules_actions");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function remove_action()
{
	if (confirm(document.getElementById("confirmactiondel").value))
	{
		if (lineSelected<0)  //azioni da rule editor
		{
			alert(document.getElementById("noremactionfromide").value);
			enableAction(1);
			disableAction(2);
			disableAction(3);
		}
		else if (lineSelected!=null)
		{
			document.getElementById("action_to_remove").value=lineSelected;
			document.getElementById("cmd").value="remove_action";
			var ofrm = document.getElementById("frm_rules_actions");
			if(ofrm != null)
				MTstartServerComm();
			ofrm.submit();
		}
	}
}

function set_description()
{
	if (lineSelected<0)  //azioni da rule editor
		{
			alert(document.getElementById("noremactionfromide").value);
			enableAction(1);
			disableAction(2);
			disableAction(3);
			return;
		}
	document.getElementById("action_to_remove").value=lineSelected;
	document.getElementById("cmd").value="set_description";
	var ofrm = document.getElementById("frm_rules_actions");
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}

function modifyAction(actionType,idAction){
	if(new Number(idAction)<0){
			alert(document.getElementById("noremactionfromide").value);
			enableAction(1);
			disableAction(2);
			disableAction(3);
		}
	else
		top.frames['manager'].loadTrx("nop&folder=setaction"+(actionType=="true"?"2":"")+"&bo=BSetAction&type=click&actioncode="+idAction+"&desc=ncode08&sched="+actionType);
}//modifyAction

function resizeTableSchedulerDashboard()
{
	var hdev = MTcalcObjectHeight("trSchedulerDashboardList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-15);
	}
}

function writeTimeBandsTable(arg)
{
	if(arg == null || arg == "")
	{
		return;
	}
	var tbRow = new Array();
	tbRow = arg.split(";;;;");
	for(var i=0;i<tbRow.length-1;i++)
	{
		var tb = new Array();
		tb = tbRow[i].split(";;");
		var type = tb[1];
		var timeband = tb[2];
		var div = document.getElementById("divTimeband"+i);
		if(div != null)
		{
			//document.getElementById("divTimeband"+i).innerHTML = "adfasd";
			if(type == 1)
			{
				div.innerHTML = getTableDaily(timeband);
			}
			if(type == 2)
			{
				div.innerHTML = getTableWeek(timeband);
			}
			if(type == 3)
			{
				div.innerHTML =getTableMonth(timeband);
			}
		}
	}
}
function getTableMonth(arg)
{
	var timesMonth = arg.split(",");
	var tableMonth = "<br><table cellspacing=0 cellpadding=0 border=0>";
	for(var i=0;i<timesMonth.length;i++)
	{
		var day = timesMonth[i].split("|")[0];
		var timesArg = getTimeRangeArray(timesMonth[i].split("|")[1]);
		tableMonth += "<tr valign='middle'><td valign='middle'>";
		tableMonth += getMonth(day);
		tableMonth += "</td><td width='20px'/><td valign='middle'>";
		tableMonth += getTime(timesArg);
		tableMonth += "</td><tr>";
	}
	tableMonth += "</table>";
	return tableMonth;
}
function getMonth(day)
{
	var tableMonth = "<table cellspacing=1 cellpadding=0>";
	for(var i=0;i<5;i++)
	{
		tableMonth += "<tr>";
		for(var j=0;j<7;j++)
		{
			if(i==4 && j>2)
			{
				break;
			}
			if(isMonthSelected(i*7+j+1,day) == true)
			{
				tableMonth += "<td bgcolor='#F0F0F0' height='16px' width='16px' style='border: 1px solid #FF0000;font-size:10px' align='center'>"+(i*7+j+1)+"</td>";
			}
			else
			{
				tableMonth += "<td bgcolor='#F0F0F0' height='16px' width='16px' style='font-size:10px' align='center'>"+(i*7+j+1)+"</td>";
			}
		}
		tableMonth += "</tr>";
	}
	tableMonth += "</table>";
	return tableMonth;
}
function isMonthSelected(td,day)
{
	var dayN = Number(day);
	if(td == dayN)
	{
		return true;
	}
	else
	{
		return false;
	}
}
function getTableWeek(arg)
{
	var timesWeek = arg.split(",");
	var tableWeek = "<br><table cellspacing=0 cellpadding=0 border=0>";
	for(var i=0;i<timesWeek.length;i++)
	{
		var weeks = timesWeek[i].split("|")[0].split("+");
		var timesArg = getTimeRangeArray(timesWeek[i].split("|")[1]);
		tableWeek += "<tr><td valign='middle'>";
		tableWeek += getWeek(weeks);
		tableWeek += "</td><td width='20px'/><td valign='middle'>";
		tableWeek += getTime(timesArg);
		tableWeek += "</td><tr>";
	}
	tableWeek += "</table>";
	return tableWeek;
}
function getTimeRangeArray(arg)
{
	var times = arg.split("-");
	var timesArg = new Array(2);
	var time0 = 0;
	var time1 = 0;
	if(Number(times[0].split(":")[1]) == 10 || Number(times[0].split(":")[1]) == 0)
	{
		time0 = Number(times[0].split(":")[0])*60;
	}
	else if(Number(times[0].split(":")[1]) == 50)
	{
		time0 = (Number(times[0].split(":")[0])+1)*60;
	}
	else if(Number(times[0].split(":")[1]) == 20 || Number(times[0].split(":")[1]) == 40 ||Number(times[0].split(":")[1]) == 30)
	{
		time0 = Number(times[0].split(":")[0])*60 + 30;
	}
	if(Number(times[1].split(":")[1]) == 10 || Number(times[1].split(":")[1]) == 0)
	{
		time1 = Number(times[1].split(":")[0])*60;
	}
	else if(Number(times[1].split(":")[1]) == 50)
	{
		time1 = (Number(times[1].split(":")[0])+1)*60;
	}
	else if(Number(times[1].split(":")[1]) == 20 || Number(times[1].split(":")[1]) == 40 ||Number(times[1].split(":")[1]) == 30)
	{
		time1 = Number(times[1].split(":")[0])*60 + 30;
	}
	if(time0 == time1)
	{
		if(time0 == 1440)
		{
			time0 -= 30;
		}
		else
		{
			time1 += 30; 
		}
	}
	timesArg[0] = time0;
	timesArg[1] = time1;
	return timesArg;
}
function isWeekSelected(td,weeks)
{
	if(td<6)
	{
		for(var i=0;i<weeks.length;i++)
		{
			if(td+2 == weeks[i])
			{
				return true;
			}
		}
	}
	else if(td==6)
	{
		for(i=0;i<weeks.length;i++)
		{
			if(1 == weeks[i])
			{
				return true;
			}
		}
	}
	return false;
}
function getWeek(weeks)
{
	var tableWeek;
	tableWeek = "<table cellspacing=0 cellpadding=0 border=0><tr>";
	for(j=0;j<7;j++)
	{
		if(isWeekSelected(j,weeks))
		{
			if(j<6)
			{
				tableWeek += "<td bgcolor='black' height='25px' width='25px' style='border-top: 1px solid #BBBBBB;border-bottom: 1px solid #BBBBBB;border-left: 1px solid #BBBBBB;'>&nbsp;</td>";
			}
			else
			{
				tableWeek += "<td bgcolor='black' height='25px' width='25px' style='border: 1px solid #BBBBBB;'>&nbsp;</td>";
			}
		}
		else
		{
			if(j<6)
			{
				tableWeek += "<td height='25px' width='25px' style='border-top: 1px solid #BBBBBB;border-bottom: 1px solid #BBBBBB;border-left: 1px solid #BBBBBB;'>&nbsp;</td>";
			}
			else
			{
				tableWeek += "<td height='25px' width='25px' style='border: 1px solid #BBBBBB;'>&nbsp;</td>";
			}
		}
	}
	tableWeek += "</tr><tr>";
	for(j=0;j<7;j++)
	{
		tableWeek += "<td style='font-size:9px' align='center'>"+weekDesc.split(";")[j]+"</td>";
	}
	tableWeek += "</tr></table>";
	return tableWeek;
}
function getTableDaily(arg)
{
	var timeDaily = arg.split(";");
	var timesArg = new Array(timeDaily.length*2);
	var j = 0;
	for(var i=0;i<timeDaily.length;i++)
	{	
		var timeArray = getTimeRangeArray(timeDaily[i]);
		timesArg[j++] = timeArray[0];
		timesArg[j++] = timeArray[1];
	}
	var tableDaily = "<br>"+getTime(timesArg);
	return tableDaily;
}

function getTime(timesArg)
{
	var tableDaily = "<table cellspacing=0 cellpadding=0 border=0><tr>";
	for(i=0;i<25;i++)
	{
		if(i<24)
		{
			tableDaily += "<td height='30px' style='border-top: 1px solid #BBBBBB;border-bottom: 1px solid #BBBBBB;border-left: 1px solid #BBBBBB;'>";
			tableDaily += "<table cellspacing=0 cellpadding=0 width='100%'><tr>";
			for(j=0;j<6;j++)
			{
				if(isTimeSelected(((i*6)+j)*10,timesArg) == true)
				{
					tableDaily += "<td bgcolor='black' width='1.5px' height='36px'></td>";
				}
				else
				{
					tableDaily += "<td width='1.5px'></td>";
				}
			}
			tableDaily +="</tr></table></td>";
		}
		else
		{
			tableDaily += "<td style='border-left: 1px solid #BBBBBB;'>&nbsp;</td>";
		}
	}
	tableDaily += "</tr><tr>";
	for(i=0;i<9;i++)
	{
		tableDaily += "<td style='font-size:8px' colspan=3>"+i*3+"</td>";
	}
	tableDaily += "</tr></table>";
	return tableDaily;
}
function isTimeSelected(td,arg)
{
	for(var i=0; i<arg.length;i=i+2)
	{
		if(td>=arg[i] && td<=arg[i+1]-1)
		{
			return true;
		}
	}
	
	return false;
}
/**
* Per ridimensionare la tabella Regole
*/
function resizeTableRule()
{
	var hdev = MTcalcObjectHeight("trRuleList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-15);
	}
}

/**
* Per ridimensionare la tabella Azioni
*/
function resizeTableAction()
{
	var hdev = MTcalcObjectHeight("trActionList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev);
	}
}