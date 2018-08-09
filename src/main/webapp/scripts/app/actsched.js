var lineSelected = null;
var clickState = 0;

function onLoadRule()
{
	if (!document.getElementById("description_txt").disabled)
		document.getElementById("description_txt").focus();
	
	
	enableAction(1);
	if (document.getElementById("cmd").value=="modify_rule")
		enableAction(3);
	
	if(document.getElementById("chkdbl").value == "T")
		alert(document.getElementById("msgdouble").value);
	
	if ((document.getElementById("rule_removed")) && (document.getElementById("rule_removed").value == "NO"))
		alert(document.getElementById("not_removed").value);
}

function setFocus_actsched()
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
		if (lineSelected!=null)
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
	if (document.getElementById("description_txt").value=="")
	{
		alert(document.getElementById("nodesc").value);
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
	var description = document.getElementById("description_txt").value;
	if( description == "" ) {
		alert(document.getElementById("nodescription").value);
		return;
	}
	// check for duplicates
	var listActions = Lsw0.mData;
	for(var i = 0; i < listActions.length; i++) {
		if( description == listActions[i][4] ) {
			alert(document.getElementById("duplicatecodemsg").value);
			return;
		}
	}
	
	document.getElementById("cmd").value="new_action";
	var ofrm = document.getElementById("frm_rules_actions");
	if(ofrm != null) {
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
			alert(document.getElementById("noremoveactionfromide").value);
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
	document.getElementById("action_to_remove").value=lineSelected;
	document.getElementById("cmd").value="set_description";
	var ofrm = document.getElementById("frm_rules_actions");
	if(ofrm != null) {
		MTstartServerComm();
		ofrm.submit();
	}
}


function modifyAction(actionType,idAction){
	if(new Number(idAction)<0)
		alert(document.getElementById("noremactionfromide").value);
	else
		top.frames['manager'].loadTrx("nop&folder=setaction"+(actionType=="true"?"2":"")+"&bo=BSetAction&type=click&actioncode="+idAction+"&desc=ncode08&sched="+actionType);
}//modifyAction

/**
* Per ridimensionare la tabella Regole
*/
function resizeTableRule()
{
	var hdev = MTcalcObjectHeight("trRuleList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-10);
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