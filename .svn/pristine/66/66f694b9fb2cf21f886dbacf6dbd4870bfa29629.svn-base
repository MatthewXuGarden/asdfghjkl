var groupSelected = null;
var clickState = null;
var MAX_GROUP = 50;

function saveGroup()
{
	//check max group number
	var groupNumber = document.getElementById("groupNumber").value;
	if (groupNumber>MAX_GROUP)
	{
		alert(document.getElementById("s_grouplimit").value);
		return;
	}
	
	//controllo se manca descrizione di default
	var defLang = document.getElementById("defLang").value;
	if (document.getElementById(defLang).value=="")
	{
		alert(document.getElementById("missingdefault").value);
		return;
	}
	//impedisco di salvare se non selezionato device
	if (document.getElementById("dispositivi2").length==0)
	{
		alert(document.getElementById("nodev").value);
		return;
	}
	
	var obj = document.getElementById("dispositivi2");
	var devices = getList2Value(obj);
	document.getElementById("devices").value = devices;
	var ofrm = document.getElementById("frmnewgroup");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function setGroup()
{
	//controllo se manca descrizione di default
	var defLang = document.getElementById("defLang").value;
	var ofrm = document.getElementById("frmsetgroup");
	if (document.getElementById(defLang).value=="")
	{
		alert(document.getElementById("missingdefault").value);
		return;
	}
	//impedisco di salvare se non selezionato device
	if (document.getElementById("dispositivi2").length==0)
	{
		if (confirm(document.getElementById("lastdev").value))
		{
			document.getElementById("archgoback").value = "true";
			document.getElementById("cmd").value ="removegroup";
			ofrm.target="body";
			if(ofrm != null)
				MTstartServerComm();
			ofrm.submit();
			return;
		}
		else
		{
			return;
		}
	}
	
	//assegno a devices1 gli id dei devices della prima lista
	var obj1 = document.getElementById("dispositivi1");
	var devices = getList1Value(obj1);
	document.getElementById("devices1").value = devices;
	
	//assegno a devices2 gli id dei devices della seconda lista	
	var obj2 = document.getElementById("dispositivi2");
	var devices = getList2Value(obj2);
	document.getElementById("devices2").value = devices;
	document.getElementById("cmd").value ="setgroup";
	//post del form contenente le informazioni necessarie
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function selectedGroup(idGroup)
{
	if (groupSelected==idGroup)
	{
		if ((clickState==null)|(clickState==0))
		{
			clickState=1;
			enableAllAction();
		}
		else
		{
		clickState=0;
		disableAllAction();
		}
	}
	else
	{
		clickState=1;
		enableAllAction();
	}
	groupSelected= idGroup;
	var hiddenInput = document.getElementById("removeGroup");
	hiddenInput.value = idGroup;
}

function modifyGroup()
{
	top.frames['manager'].loadTrx('nop&folder=setgroup&bo=BSetGroup&type=click&group=' + groupSelected + '&desc=ncode05');
}

function removeGroup()
{
	if (confirm(document.getElementById("reallydeletegroup").value)) 
	{
		var hiddenInput= document.getElementById("removeGroup");
		var group = hiddenInput.value;
		var ofrm= document.getElementById("frmremovegroup");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}



function setFocus()
{
	var lang = document.getElementById("defLang").value;
	if (document.getElementById(lang).disabled==false)
	{
		document.getElementById(lang).focus();
	}
}

