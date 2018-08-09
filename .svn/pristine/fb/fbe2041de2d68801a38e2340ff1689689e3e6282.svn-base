var energycurrentrow = null;
//var energycurrentrowspecial = null;
var selectedgroup = -1;

function manageChildTd(num,strClass)
{
	try 
	{
		document.getElementById("gtd1"+num).className=strClass;
		document.getElementById("gtd2"+num).className=strClass;
	}
	catch(e){}
}

function energyover(obj) {
	if (obj.id == "g" + selectedgroup) {
		return;
	}
	obj.className="egtabmouseover";
	manageChildTd(obj.id.substring(1),"egtabmouseover");
}
function energyout(obj) {
	if (obj.id == "g" + selectedgroup) {
		return;
	}
	obj.className="egtabnotselected";
	manageChildTd(obj.id.substring(1),"egtabnotselected");
}
function energyswitchgroup(obj) {
	var num = obj.id.substring(1);
	if (num == selectedgroup) {
		return;
	}
	
	if(selectedgroup>0){ //caso iniziale nullo
		var oldgrp = document.getElementById("group" + selectedgroup);
		var oldlbl = document.getElementById("g" + selectedgroup);
	
		oldgrp.style.visibility = "hidden";
		oldgrp.style.display = "none";
	
		oldlbl.className="egtabnotselected";
		manageChildTd(selectedgroup,"egtabnotselected");
	}
	
	var newgrp = document.getElementById("group" + num);
	var newlbl = document.getElementById("g" + num);

	newgrp.style.visibility = "visible";
	newgrp.style.display = "block";

	newlbl.className="egtabselected";
	manageChildTd(num,"egtabselected");
	
	selectedgroup = num;
}

function varpos_rowsel(arow, atable)
{
	if(energycurrentrow==arow){
		energycurrentrow.className = "Row1";
		energycurrentrow=null;
		return;
	}
	if(energycurrentrow)
		energycurrentrow.className = "Row1";
	if(arow.className!="selectedRow")
	{
		energycurrentrow = arow;
		arow.className="selectedRow";
	}
	else
	{
		energycurrentrow=null;
		arow.className="Row1";
	}
}
/*
function varpos_rowsel_special(arow, atable)
{
	if(energycurrentrow==arow){
		energycurrentrow.className = "Row1";
		energycurrentrow=null;
		return;
	}
	if(energycurrentrow)
		energycurrentrow.className = "Row1";
	if(arow.className!="selectedRow")
	{
		energycurrentrow = arow;
		arow.className="selectedRow";
	}
	else
	{
		energycurrentrow=null;
		arow.className="Row1";
	}
}
*/

function checkEnergyRegistered()
{
	// Controllo Energy Registered
	var isEnReg = "false";
	try {
		isEnReg = document.getElementById("plgennotreg").value;
	}
	catch(e){
		isEnReg = "false";
	}
	if(isEnReg == "false")
		return false;
	else
		return true;
}

function checkEnergyConfigured()
{
	// Controllo Energy Registered
	var isEnCnf = "false";
	try {
		isEnCnf = document.getElementById("plgnotcnf").value;
	}
	catch(e){
		isEnCnf = "false";
	}
	if(isEnCnf == "false")
		return false;
	else
		return true;
}

function changeYear(var1){
	var week=document.getElementById('week');
	var curWeek = week.value;	
	CommSend("servlet/ajrefresh", "POST","cmd=changeYear&year="+var1+"&curWeek="+curWeek, "newYearInit", false);
//	week.options.length=0;
//	var strWeek = "";
//	for (var iWeek=1;iWeek <= 52; iWeek++){
//		strWeek = getXDate(var1,iWeek);
//		week.options.add(new Option(strWeek,iWeek));
//		if (currWeek == iWeek){
//			week.options[iWeek-1].selected = true; 
//		}
//	}
}

function Callback_newYearInit(xml) {
	rsp = xml.getElementsByTagName("CalendarOption")[0].childNodes[0].nodeValue
	$("#week").html(rsp);
	//document.getElementById("week").innerHTML = rsp;
	
}

function getXDate(year,weeks){
	var date = new Date(year,"0","1");
	var time = date.getTime();
	time+=(weeks-1)*7*24*3600000;
	date.setTime(time);
	return getNextDate(date,1) + " - " + getNextDate(date,7);
} 
function getNextDate(nowDate,weekDay){
	var day = nowDate.getDay();
	var time = nowDate.getTime();
	var sub = weekDay-day;
	time+=sub*24*3600000;
	nowDate.setTime(time);
	nowDate.getDate();
	nowDate.getMonth();
	var thisDay = nowDate.getDate();
	var thisMonth = nowDate.getMonth();
	var month_slot = document.getElementById("month_slot").value;
	var month = month_slot.split(",");
	thisMonthDesc = month[thisMonth];
	var thisDate = thisDay + " " + thisMonthDesc;
	return thisDate;
}
