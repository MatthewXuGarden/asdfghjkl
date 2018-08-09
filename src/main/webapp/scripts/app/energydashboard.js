var ACTION_START	= 1;
var ACTION_STOP		= 2;
var ACTION_REFRESH	= 3;
var ACTION_RESET	= 4;


function onLoadDashboard()
{
	// make zebra rows
	var table = document.getElementById("energyreporttable");
	if( table ) { 
		for(var i = 1; i < table.rows.length; i++) {
			var className = i % 2 ? "Row1" : "Row2";
			table.rows[i].className = className;
		}
	}

	enableAction(ACTION_REFRESH);
	startstopinit();
}


function startstopinit() 
{
	if(!checkEnergyRegistered() || !checkEnergyConfigured())
	{	
		disableAction(ACTION_START);
		disableAction(ACTION_STOP);
		return;
	}
	
	if (document.getElementById("logrun") &&
		document.getElementById("logrun").value == "true") 
	{
		disableAction(ACTION_START);
		enableAction(ACTION_STOP);
	}
	else
	{
		enableAction(ACTION_START);
		disableAction(ACTION_STOP);
	}
}


function startenergyplugin() {
	var ans = confirm(document.getElementById("confirmstart").value);
	if(ans==false){
		return;
	}
	document.getElementById("cmd").value = "start";
	MTstartServerComm();
	document.getElementById("energycommand").submit();
}


function stopenergyplugin() {
	var ans = confirm(document.getElementById("confirmstop").value);
	if(ans==false){
		return;
	}
	document.getElementById("cmd").value = "stop";
	MTstartServerComm();
	document.getElementById("energycommand").submit();
}


function energyrefresh() {
	top.frames['manager'].loadTrx('nop&folder=energy&bo=BEnergy&type=menu&desc=energy');
}

function energyReset() {
	var groupNo = document.getElementById("groupNo").value;
	var variableIds = "";
	var siteMeter=document.getElementById("resetGroup_1");
	if(siteMeter != null && siteMeter.checked == true & siteMeter.value.indexOf(",")!=-1)
	{
		variableIds+=siteMeter.value+";";
	}	
	for(var i=0;i<groupNo;i++){
		var str=document.all["resetGroup"+i];
		for (var a=0;a<str.length;a++){
			if(str[a].checked == true & str[a].value.indexOf(",")!=-1)
			  {
				variableIds+=str[a].value+";";
			  }	
		}
	}
	var confirminfo = document.getElementById("confirmreset").value;
	if(confirm(confirminfo)){
		CommSend("servlet/ajrefresh","POST","cmd=resetenergy&variableIds="+variableIds,"getResult", true);
	}
}
function Callback_getResult()
{
	rsp = xmlResponse.getElementsByTagName("MSG")[0].childNodes[0].nodeValue;
	if(rsp=="OK")
	{
		alert(document.getElementById("confirmresetfeedback").value);
		energyrefresh();
	}
	
}

function selectCheckbox(range,group,groupSumer){
	var status = false;
	var checkboxAll = document.getElementById("resetParAll");
	if(range=="all"){
		var groupNo = document.getElementById("groupNo").value;
		if(checkboxAll != null && checkboxAll.checked == true){
			for(var i=0;i<groupNo;i++){
				checkAll("resetGroup"+i);
			}
			status = true;
		}else{
			for(var a=0;a<groupNo;a++){
				uncheckAll("resetGroup"+a);
			}
		}	
	}
	else if(range=="group"){
		if(document.all[group][0].checked == true){
			checkAll(group);
			status = true;
		}else{
			uncheckAll(group);
			if(checkboxAll != null)
				checkboxAll.checked = false;
		}	
	}
	else if(range=="device"){
		document.all[group][0].checked = false;
		if(checkboxAll != null)
			checkboxAll.checked = false;
	}
	if(!status){
		status = checkAllStatus();
	}
	if(status){
		enableAction(ACTION_RESET);
	}else{
		disableAction(ACTION_RESET);
	}
}

function checkAllStatus(){
	var status = false;
	var checkboxAll = document.getElementById("resetParAll");
	var groupNo = document.getElementById("groupNo").value;
	if(!status){
	if (checkboxAll != null && checkboxAll.checked == true){
		status = true;	
	}
	}
	if(!status)
	{
		var siteMeter = document.getElementById("resetGroup_1");
		if(siteMeter != null && siteMeter.checked == true)
			status = true;
	}
	if(!status){
		for(var i=0;i<groupNo;i++){
			if(!status){
				for(var a=0;a<document.all["resetGroup"+i].length;a++){
					if(document.all["resetGroup"+i][a].checked == true){
						status = true;
						break;
					}
				}
			}
		}
	}
	return status;	
}
function checkAll(str) 
{ 
	var resetPar = document.all[str]; 
	if(resetPar.length){ 
		for(var i=0;i<resetPar.length;i++) 
		{ 
			resetPar[i].checked = true; 
		} 
	}else{ 
		resetPar.checked = true; 
} 
} 
function uncheckAll(str) 
{ 
	var resetPar = document.all[str]; 
	if(resetPar.length){ 
		for(var i=0;i<resetPar.length;i++) 
		{ 
			resetPar[i].checked = false; 
		} 
	}else{ 
		resetPar.checked = false; 
	} 
}



