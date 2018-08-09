var GUI_GLB_IMG = null;

function reloadGuardian()
{
//	document.getElementById("cmd").value="reload";
//	var params = document.getElementById("params");
//	params.value = getList2Value(document.getElementById("variable2"));
//	
//	var ofrm = document.getElementById("frm_guardian");
//	var dev = document.getElementById("combo");
//	var e = dev.options[dev.selectedIndex].value;
//	document.getElementById("guard_iddev").value=e;
//	if(ofrm != null)
//			MTstartServerComm();
//	ofrm.submit();
	document.getElementById("variable1").innerHTML = "";
	if(document.getElementById("combo").value != "0")
	{
		var param = "cmd=loaddevice&iddev="+document.getElementById("combo").value;
		new AjaxRequest("servlet/ajrefresh", "POST", param,
				Callback_reloadGuardian, true);
	}
}
function Callback_reloadGuardian(xml)
{
	var variable1 = document.getElementById("variable1");
	var vars = xml.getElementsByTagName("var");
	for(var i=0;i<vars.length;i++)
	{
		var value = vars[i].getElementsByTagName("v")[0].childNodes[0].nodeValue;
		var str = vars[i].getElementsByTagName("s")[0].childNodes[0].nodeValue;
		variable1.options.add(new Option(str,value));
	}
	for(i=0;i<variable1.options.length;i++)
	{
		variable1.options[i].className = i%2==0?"Row1":"Row2";
	}
}
function subtab5_init(){
	var list1=document.getElementById("list1");
	var list2=document.getElementById("list2");
	if((list1.options.length>0) && (list2.options.length>0)){
		for(var j=0;j<list2.options.length;j++){
			for(var i=0;i<list1.options.length;i++){
				if(list1.options[i].value==list2.options[j].value){
					list1.remove(i);
					break;
				}
			}
		}
	}

	for(var i=0; i < list1.options.length; i++)
		list1.options[i].className = "Row" + (i % 2 ? "2" : "1"); 
	for(var i=0; i < list2.options.length; i++)
		list2.options[i].className = "Row" + (i % 2 ? "2" : "1"); 
		
	var variable1=document.getElementById("variable1");
	var variable2=document.getElementById("variable2");
	if((variable1.options.length>0) && (variable2.options.length>0)){
		for(var j=0;j<variable2.options.length;j++){
			for(var i=0;i<variable1.options.length;i++){
				if(variable1.options[i].value==variable2.options[j].value){
					variable1.remove(i);
					break;
				}
			}
		}
	}

	for(var i=0; i < variable2.options.length; i++)
		variable2.options[i].className = "Row" + (i % 2 ? "2" : "1"); 
	
	enableAction(1);
}
function sendVarGuardian()
{
	var stopGuardian = document.getElementById("szac");
	var stopDays = document.getElementById("szdd");
	if(stopGuardian.checked && stringtrimmer(stopDays.value)=="")
	{
		alert(document.getElementById("disableDays").value);
		stopDays.focus();
		return;
	}
	var ofrm = document.getElementById("frm_guardian");
	var obj2 = document.getElementById("variable2");
	// No lista se remoto
	if(obj2 != null)
	{
		var values = getList2Value(obj2);
		document.getElementById("values").value= values;
	}
	//notification list
	var oDiv = document.getElementById("guiPostDiv");
	var objselout = document.getElementById("list2");
	var optTmp = null;
	var i =0;
	var toWrite = "";
	if (objselout != null) {
		oDiv.innerHTML = "<input type='hidden' value='" + objselout.length+ "' name='GuiNumSeg'>";
		for (i = 0; i < objselout.length; i++) {
			optTmp = objselout.options[i];
			if (optTmp != null)
				toWrite = "<input type='hidden' value='" + optTmp.value+ "' name='GC_" + i + "'>"
			oDiv.innerHTML = oDiv.innerHTML + toWrite;
		}
	}
	document.getElementById("cmd").value="sendguardianvars";
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function GuiOnDivScroll(idselect,size)
{
	var lstCollegeNames = document.getElementById(idselect.id);

    if (lstCollegeNames.options.length > size)
        lstCollegeNames.size=lstCollegeNames.options.length;
    else
        lstCollegeNames.size=size;
}

function GuiOnSelectFocus(obj,size)
{
    if (document.getElementById("divCollegeNames")!=null && document.getElementById("divCollegeNames").scrollLeft != 0)
    {
        document.getElementById("divCollegeNames").scrollLeft = 0;
    }

    var lstCollegeNames = obj;

    if( lstCollegeNames.options.length >size)
    {
        lstCollegeNames.focus();
        lstCollegeNames.size=size;
    }
}

//function GuiImportAddress()
//{
//	var idxSel = -1;
//	var objselin  = document.getElementById("indyguiin");
//	var objselout = document.getElementById("indyguiout");
//
//	if(objselin != null && objselout != null)
//	{
//		var lungh = objselout.length;
//		
//		idxSel = objselin.selectedIndex;
//		if(idxSel != -1)
//		{
//			var optSel = objselin.options[idxSel];
//			var optNew = document.createElement("OPTION");
//			var b = objselout.options[lungh];
//
//			optNew.text=optSel.text;
//			optNew.value=optSel.value;
//			
//			//controllo che non sia gia presente
//			var iscontained = false;
//			for (i=0;i<lungh;i++)
//		    {
//			    if (objselout.options[i].value==optSel.value)
//			    {
//			    	alert(document.getElementById("doubleElement").value);
//					iscontained = true;
//			    	break;
//			    }
//			}
//			
//			if (!iscontained)
//			{
//				objselout.add(optNew);
//			}
//		}
//	}
//}
//
//function GuiRemoveAddress()
//{
//	var idxSel = -1;
//	var objselout = document.getElementById("indyguiout");
//
//	if(objselout != null)
//	{
//		idxSel = objselout.selectedIndex;
//		if(idxSel != -1)
//			objselout.remove(idxSel);
//	}
//}

function GuiSaveAddress()
{
	var oDiv = document.getElementById("guiPostDiv");
	var objselout = document.getElementById("list2");
	var optTmp = null;
	var i =0;
	var toWrite = "";
	
	if(objselout != null)
	{
		oDiv.innerHTML = "<input type='hidden' value='"+objselout.length+"' name='GuiNumSeg'>";
		for(i=0; i<objselout.length; i++)
		{
			optTmp = objselout.options[i];
			if(optTmp != null)
				toWrite = "<input type='hidden' value='"+optTmp.value+"' name='GC_"+i+"'>"
				
			oDiv.innerHTML = oDiv.innerHTML+toWrite;
		}
	}
	
	var ofrm = document.getElementById("frm_guardian_conf");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
	
	GuiRestartService();
}

function GuiRestartService()
{
	CommSend("servlet/ajrefresh","GET","cmd=guiserv",3,true);
}

function Callback_3() {
	// Nothing
}
/*
function GuiGestMessage()
{
	var idSetActGui = document.getElementById("actionincon").value;
	if(idSetActGui != null && idSetActGui != "null")
		top.frames["header"].TopCheckGuiMsg(idSetActGui);
}
*/
