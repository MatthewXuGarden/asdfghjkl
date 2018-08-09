var dovesonoarrivato=0;

function parameters_general_save(){
	
	var errmsg=document.getElementById("userkeybemptyerrormsg");
	
	var userk = document.getElementById("usertastiera");
	
	if (userk.value==""){
		alert(errmsg.value);
	}
	else
	{	
		var ofrm = document.getElementById("param_gen");
		if(ofrm != null)
				MTstartServerComm();
		ofrm.submit();
	}
	return false;
}

//function parameters_notify_save(){
//	
//	var ofrm = document.getElementById("param_notify");
//	if(ofrm != null)
//			MTstartServerComm();
//	ofrm.submit();
//	
//	return false;
//	
//	alert("Saved!");
//}


function parameters_variables_save(){
	
	var ofrm = document.getElementById("param_gen");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
	
	return false;
}

function parameters_notify_save()
{
	var oDiv = document.getElementById("guiPostDiv");
	var objselout = document.getElementById("list2");
	var optTmp = null;
	var i =0;
	var toWrite = "";
	var test="";
	
	if(objselout != null)
	{
		for(i=0; i<objselout.length; i++)
		{
			optTmp = objselout.options[i];
			if(optTmp != null)
			{
				test = test+optTmp.value+";";
			}
				
		}
		
		toWrite = "<input type='hidden' value='"+test+"' name='elenco_notifiche'>";
		oDiv.innerHTML = oDiv.innerHTML+toWrite;
		
	}

	objselout = document.getElementById("list_auth2");
	optTmp = null;
	i =0;
	toWrite = "";
	test="";
	
	if(objselout != null)
	{
		for(i=0; i<objselout.length; i++)
		{
			optTmp = objselout.options[i];
			if(optTmp != null)
			{
				test = test+optTmp.value+";";
			}
				
		}
		
		toWrite = "<input type='hidden' value='"+test+"' name='elenco_profili_auth'>";
		oDiv.innerHTML = oDiv.innerHTML+toWrite;
		
	}
	
	var ofrm = document.getElementById("param_notify");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function parameters_rollback(idevent){
	var event = document.getElementById("idevent");
	event.value=idevent;
	var ofrm = document.getElementById("param_rollback_form");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function parameters_get_photo(){
	var ofrm = document.getElementById("param_photo_form");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function parameters_start(){
	var ofrm = document.getElementById("param_start_stop");
	document.getElementById("param_action").value="start";
	if( ofrm != null ) {
		MTstartServerComm();
		ofrm.submit();
	}
	return false;
}
function parameters_stop(){
	var ofrm = document.getElementById("param_start_stop");
	document.getElementById("param_action").value="stop";
	if( ofrm != null ) {
		MTstartServerComm();
		ofrm.submit();
	}
	return false;
}
function parameters_restart(){
	var ofrm = document.getElementById("param_start_stop");
	document.getElementById("param_action").value="restart";
	if( ofrm != null ) {
		MTstartServerComm();
		ofrm.submit();
	}
	return false;
}

function reloadPhoto(){
	CommSend("servlet/ajrefresh",
			"POST",
			"action=-1&from="+dovesonoarrivato,
			"reloadPhoto", 
			false);
	return false;
}

function Callback_reloadPhoto(){
	var e =xmlResponse.getElementsByTagName("photo")[0];
	
	for (  int = 0; int < e.childNodes.length; int++) {
		if (e.childNodes[int].nodeName =="end"){
			dovesonoarrivato = e.childNodes[int].childNodes[0].nodeValue;
		} else if (e.childNodes[int].nodeName )
		{
			var n = e.childNodes[int].nodeName;
			var v = e.childNodes[int].childNodes[0].nodeValue;
	
			document.getElementById(n).innerHTML = v;
		}


		}
}


function GuiOnSelectFocus(obj,size)
{
	// do nothing, just catch onScroll event (bug 7237)
}
