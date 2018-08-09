var idSelectedVar = null;
var clickState = null;
var state=null;
var VarGroup = null;
var contentElement = null;
var radioSel = null;

/*
  VARIABILI GLOBALI
  
  idSelectedVar : id della nota selezionata al momento
  clickState=0 : non sto selezionando alcuna nota,quindi abiltito la textarea
  clickState=1 : c'� una nota selezionata
  state="a" : default, focus su textarea, abilitato solo tasto +.Ritorn o in a deselezionando una riga
  state="b" : seleziono una riga, abilito solo il tasto -
  state="c" : doppio click su riga, abilito solo il tasto + e "save" 
*/

function checkIdvarLenthByDB(limitInDB){
	var values = "";
	var list2 = document.getElementById("variabili2");
	for (i=0;i<list2.length;i++)
	{
		values += "pk" + list2.options[i].value+";";
	}
	
	var list1 = document.getElementById("variabili1");
	for (i=0;i<list1.length;i++){
		if(list1.options[i].selected){
			values += "pk"+list1.options[i].value+";";
		}
	}
	
	values = values.substring(0,(values.length-1));
	
	if(values.length>limitInDB){
		alert(document.getElementById("exceedLength").value);
	}else{
		multipleto2(document.getElementById("variabili1"),null);
	}
}

// da chiamare al caricamento della pagina
function setdefault()    //ok
{
	tmp = document.getElementById("defLang");
	VarGroup = document.getElementById(tmp.value);
	if (VarGroup.disabled==false)
	{
		VarGroup.focus();
	}
	cmd = document.getElementById("cmd").value;
	if ((cmd=="set")||(cmd=="set2"))
	{
		state="c";
		setButton(state);
	}
	else
	{
		state="a";
		setButton(state);
	}
	
	var selectRadios = document.getElementsByName("variableType");
	for (i=0;i<selectRadios.length;i++)
	{
		if (selectRadios[i].checked==true)
		{
			radioSel = selectRadios[i].id;
		} 
	}
}

//salva la modifica di una variabile di gruppo
function saveModifyVarGroup() //ok
{
	//controllo se manca descrizione di default
	var defLang = document.getElementById("defLang").value;
	if (document.getElementById(defLang).value=="")
	{
		alert(document.getElementById("missingdefault").value);
		return;
	}
	
	//impedisco di salvare se non selezionata almeno una var
	if (document.getElementById("variabili2").length==0)
	{
		alert(document.getElementById("novar").value);
		return;
	}
	
	/*if (document.getElementById("measureunit").selectedIndex==0)
	{
		alert(document.getElementById("uminsert").value);
		return;
	}*/
	
	var obj1 = document.getElementById("variabili2");  //lista di variabili da postare
	var vars = getList2Value(obj1);
	document.getElementById("varsList").value = vars;
	var ofrm= document.getElementById("frmsetgroup");
	document.getElementById("cmd").value="mod";
	ofrm.method = "post";
	ofrm.action = "servlet/master;" + getSessId(); 
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}	

//aggiunge una variabile di gruppo
function addVarGroup()			//ok
{
	//controllo se manca descrizione di default
	var defLang = document.getElementById("defLang").value;
	if (document.getElementById(defLang).value=="")
	{
		alert(document.getElementById("missingdefault").value);
		return;
	}
	
	//impedisco di salvare se non selezionata almeno una var
	if (document.getElementById("variabili2").length==0)
	{
		alert(document.getElementById("novar").value);
		return;
	}
	
	/*if (document.getElementById("measureunit").selectedIndex==0)
	{
		alert(document.getElementById("uminsert").value);
		return;
	}*/
	
	var obj1 = document.getElementById("variabili2");  //lista di variabili da postare
	var vars = getList2Value(obj1);
	document.getElementById("varsList").value = vars;
		
	var ofrm= document.getElementById("frmsetgroup");
	document.getElementById("cmd").value="add";
	ofrm.method = "post";
	ofrm.action = "servlet/master;" + getSessId(); 
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}


//rimuove la variabile di gruppo selezionata
function removeVarGroup()			//ok
{
	if (confirm(document.getElementById("reallydeletevar").value)) 
	{
		var ofrm= document.getElementById("frmsetgroup");
		document.getElementById("cmd").value="rem";
		document.getElementById("idvar").value=idSelectedVar;
		ofrm.method = "post";
		ofrm.action = "servlet/master;" + getSessId();
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

//entro in stato di modifica variabile di gruppo
function modifyVarGroup()			//ok
{
	
	
	var ofrm= document.getElementById("frmsetgroup");
		document.getElementById("cmd").value="set";
		document.getElementById("idvar").value=idSelectedVar;
		ofrm.method = "post";
		ofrm.action = "servlet/master;" + getSessId(); 
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	
	/*
	state="c";
	setButton(state);*/
}

//in  base alla variabile selezionata setto lo stato in cui sono
function selectedVarGroup(idVarGroup)
{
	if (idSelectedVar==idVarGroup)
	{
		if ((clickState==null)|(clickState==0))
		{
			clickState=1;
			state="b";
			setButton(state);
		}
		else
		{
		clickState=0;
		state="a";
		setButton(state);
		}
	}
	else
	{
		clickState=1;
		state="b";
		setButton(state);
	}
	idSelectedVar= idVarGroup;
	var hiddenInput = document.getElementById("idvar");
	hiddenInput.value = idVarGroup;	
}


// in base agli stati setto i pulsanti abilitati o meno
function setButton(state)
{
	var languages = document.getElementById("languages").value;
	var langs = languages.split(",");
	
	
	if (state=="a")
	{
		for (i=0;i<langs.length;i++)
		{
			document.getElementById(langs[i]).disabled=false;
		}
		document.getElementById("variableType1").disabled=false;
		document.getElementById("variableType2").disabled=false;
		document.getElementById("variableType3").disabled=false;
		document.getElementById("device").disabled=false;
		document.getElementById("variabili1").disabled=false;
		document.getElementById("variabili2").disabled=false;
		document.getElementById("measureunit").disabled=false;
		
		enableAction(1);
		disableAction(2);
		disableAction(3);
		VarGroup = document.getElementById(tmp.value);
		VarGroup.focus();
	}
	
	if (state=="b")
	{
		for (i=0;i<langs.length;i++)
		{
			document.getElementById(langs[i]).value="";
			document.getElementById(langs[i]).disabled=true;
		}
		document.getElementById("variableType1").checked=true;
		document.getElementById("variableType1").disabled=true;
		document.getElementById("variableType2").disabled=true;
		document.getElementById("variableType3").disabled=true;
		document.getElementById("device").value="";
		document.getElementById("device").disabled=true;
		document.getElementById("variabili1").length=0;
		document.getElementById("variabili1").disabled=true;
		document.getElementById("variabili2").length=0;
		document.getElementById("variabili2").disabled=true;
		document.getElementById("measureunit").selectedIndex=0;
		document.getElementById("measureunit").disabled=true;
		
		disableAction(1);
		enableAction(2);
		disableAction(3);
	}
	
	if (state=="c")
	{
		for (i=0;i<langs.length;i++)
		{
			document.getElementById(langs[i]).disabled=false;
		}
		document.getElementById("variableType1").disabled=false;
		document.getElementById("variableType2").disabled=false;
		document.getElementById("variableType3").disabled=false;
		document.getElementById("device").disabled=false;
		document.getElementById("variabili1").disabled=false;
		document.getElementById("variabili2").disabled=false;
		document.getElementById("measureunit").disabled=false;
		
		enableAction(1);
		disableAction(2);
		enableAction(3);
		tmp = document.getElementById("defLang");
		VarGroup = document.getElementById(tmp.value);
		VarGroup.focus();
	}
		
}

function varGroups_reload()
{
	var obj1 = document.getElementById("variabili2");  //lista di variabili da postare
	var vars = getList2Value(obj1);
	document.getElementById("varsList").value = vars;
	
	var ofrm= document.getElementById("frmsetgroup");
	var cmd = document.getElementById("cmd");
	if ((cmd.value!="set")&&(cmd.value!="set2")) cmd.value="reload";
	else cmd.value="set2";
	ofrm.method = "post";
	ofrm.action = "servlet/master;" + getSessId(); 
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function varGroups_reloadChangeVariable(obj)
{
	
	if (obj.id==radioSel) return;
	if (document.getElementById("variabili2").length!=0)
	{
		if (confirm(document.getElementById("confirmChangeVariable").value))
		{
			var ofrm= document.getElementById("frmsetgroup");
			var cmd = document.getElementById("cmd");
			if ((cmd.value!="set")&&(cmd.value!="set2")) cmd.value="reload";
			else cmd.value="set2";
			ofrm.method = "post";
			ofrm.action = "servlet/master;" + getSessId(); 
			if(ofrm != null)
				MTstartServerComm();
			ofrm.submit();
		}
		else
		{
			document.getElementById(radioSel).checked=true;
		}
	}
	else
	{
		var ofrm= document.getElementById("frmsetgroup");
		var cmd = document.getElementById("cmd");
		if ((cmd.value!="set")&&(cmd.value!="set2")) cmd.value="reload";
		else cmd.value="set2";
		ofrm.method = "post";
		ofrm.action = "servlet/master;" + getSessId(); 
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}