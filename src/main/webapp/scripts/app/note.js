var idSelectedNote = null;
var clickState = null;
var state=null;
var note = null;
var contentElement = null;


/*
  VARIABILI GLOBALI
  
  idSelectedNote : id della nota selezionata al momento
  clickState=0 : non sto selezionando alcuna nota,quindi abiltito la textarea
  clickState=1 : c'� una nota selezionata
  state="a" : default, focus su textarea, abilitato solo tasto +.Ritorn o in a deselezionando una riga
  state="b" : seleziono una riga, abilito solo il tasto -
  state="c" : doppio click su riga, abilito solo il tasto + e "save" 
  state="d" : seleziono una riga, ma non posso nemmeno cancellare (tutto disabilitato)
*/


// da chiamare al caricamento della pagina
function setdefault()    
{
	note = document.getElementById("noteArea");
	if (note.disabled==false)
	{
		note.focus();
		state="a";
		setButton(state);
	}
	
	if (document.getElementById("b_wronguser").value=="yes")
	{
		alert(document.getElementById("s_wronguser").value);
	}
	
	if (document.getElementById("onlyreadnote").value=="yes")
	{
		note.disabled=true;
	}
	
	// resize note tab
	resizeTableNote();
}

//salva la modifica di una nota
function saveModifyNote() 
{
	note= document.getElementById("noteArea");
	if (note.value.length!=0)
	{
		if (note.value.length > 1023)
		{
			alert(document.getElementById("longNote").value);
			note.focus();
		}
		else 
		{
			var ofrm= document.getElementById("frm");
			var notetxt = note.value;
			notetxt = notetxt.replace(/'/g,"%!");
			note.value = notetxt;
			ofrm.method = "post";
			ofrm.action = "servlet/master;" + getSessId()+"?trx=../arch/note/note.jsp&cmd=mod&idnota="+idSelectedNote; 
			if(ofrm != null)
				MTstartServerComm();
			ofrm.submit();
		}
	}
	else
	{
		alert(document.getElementById("blankNote").value);
		note.focus();
	}
	
}	

//aggiunge una nota scritta nella textarea
function addNote()
{
	var note= document.getElementById("noteArea");
	if (note.value.length!=0)
	{
		//Biolo
		var notetxt = note.value;
		notetxt = notetxt.replace(/'/g,"%!");
		note.value = notetxt;
		//end Biolo
		
		if (note.value.length > 1023)
		{
			alert(document.getElementById("longNote").value);
			note.focus();
		}
	    else 
	    {
		postForm('save');
		note.disabled=true;
		note.value ="";
		}
	}
	else
	{
		alert(document.getElementById("blankNote").value);
		note.focus();
	}
}


//rimuove la nota selezionata
function removeNote()
{
	if (confirm(document.getElementById("confirmDelete").value)) 
	{
		var hiddenInput= document.getElementById("remove");
		var nota = hiddenInput.value;
		var ofrm= document.getElementById("removeForm");
		ofrm.method = "post";
		ofrm.action = "servlet/master;" + getSessId()+"?trx=../arch/note/note.jsp&cmd=rem&idnota="+nota; 
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
	
}

//entro in stato di modifica nota
function modifyNote(idnota)
{
	if (document.getElementById("onlyreadnote").value!="yes")
	{
		note= document.getElementById("noteArea");
		contentElement = document.getElementById(idnota).getAttribute("name");
		
		//Biolo
		contentElement = contentElement.replace(/%!/g,"'");
		//end 
		
		note.value=contentElement;
		note.disabled=false;
		note.focus();
		state="c";
		setButton(state);
	}
}

//in  base alla nota selezionata setto lo stato in cui sono,e se ho una nota selezionata o meno
function selectedNote(idnote)
{
	if (idSelectedNote==idnote)
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
	idSelectedNote= idnote;
	var hiddenInput = document.getElementById("remove");
	hiddenInput.value = idnote;
	if (document.getElementById('isprotected').value!=1&&document.getElementById("onlyreadnote").value!="yes")
	{
		enableText();
	}
	showNote(idnote);
}

//in  base alla nota selezionata setto lo stato in cui sono,e se ho una nota selezionata o meno
function selectedNoteNoDelete(idnote)
{
	if (idSelectedNote==idnote)
	{
		if ((clickState==null)|(clickState==0))
		{
			clickState=1;
			state="d";
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
		state="d";
		setButton(state);
	}
	idSelectedNote= idnote;
	var hiddenInput = document.getElementById("remove");
	hiddenInput.value = idnote;
	if (document.getElementById('isprotected').value!=1&&document.getElementById("onlyreadnote").value!="yes")
	{
		enableText();
	}
	showNote(idnote);
}


//abilito la textarea
function enableText()
{
	if (clickState==0)
	{
		note= document.getElementById("noteArea");
		note.disabled=false;
		note.value="";
		note.focus();
	}
}


//selezionando una nota la visualizzo nella textarea
function showNote(idnota)
{
	if (clickState==1)
	{
	
	note= document.getElementById("noteArea");
	contentElement = document.getElementById(idnota).getAttribute("name"); 
	//Biolo
	contentElement = contentElement.replace(/%!/g,"'");
	//end
	note.value=contentElement;
	note.disabled=true;
	}
}

function postForm(str)
    {
		var ofrm = document.getElementById("frm");
		ofrm.method = "post";
		ofrm.action = "servlet/master;" + getSessId()+"?trx=../arch/note/note.jsp&cmd="+str; 
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
    	
   	}

// in base agli stati setto i pulsanti abilitati o meno
function setButton(state)
{
	if (state=="a")
	{
		enableAction(1);
		disableAction(2);
		disableAction(3);
		disableAction(4);
	}
	
	if (state=="b")
	{
		disableAction(1);
		enableAction(2);
		disableAction(3);
		enableAction(4);
	}
	
	if (state=="c")
	{
		enableAction(1);
		disableAction(2);
		enableAction(3);
		disableAction(4);
	}
		
	if (state=="d")
	{
		disableAction(1);
		disableAction(2);
		disableAction(3);
		disableAction(4);
	}
}


function submitAck(string)
{
	if (confirm(string))
	{
		var ofrm = document.getElementById("ack");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function submitCancel(string)
{
	if (confirm(string))
	{
		var ofrm = document.getElementById("cancel");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}	
}

function submitReset(string)
{
	if (confirm(string))
	{
		var ofrm = document.getElementById("reset");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function note_filterInput(obj,evnt)
{
	var ctrlk = evnt.shiftKey;
	var cancel = false;
	
	switch(evnt.keyCode)
	{
		case 13:
			cancel = true;
			break;
		//Biolo
		/*case 219:					
			cancel = !ctrlk;
			break;*/
		//end Biolo
		case 50:
			cancel = ctrlk;
			break;
	}
	
	if(cancel)
	{
		evnt.cancelBubble = true;
		evnt.returnValue = false;
		if(!document.all)
			evnt.preventDefault();
	}
}

function resizeTableNote()
{
	var h1 = MTcalcObjectHeight("trNote");
	if(h1 != 0)
		MTresizeHtmlTable(0,h1-20);
} 