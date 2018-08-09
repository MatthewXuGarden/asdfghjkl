//Blocco l'inserimento della coppia sito-fascia se già presente in tabella
function check_duplicati(){
	var idSite = document.getElementById("site");
	var Sitename = idSite.options[idSite.selectedIndex].innerHTML;
	var idFascia = document.getElementById("fascie");
	var Fascianame = idFascia.options[idFascia.selectedIndex].innerHTML;
	var fine = aValue.length;
	for(y=0; y < aValue.length; y++){
	   arrayName = aValue[y];
		if(arrayName instanceof Array){
	 		//if(arrayName[4] == Sitename && arrayName[5] == Fascianame)
	 		if(arrayName[4] == Sitename)
	 		{
				return false;
		  }
		}
	}
	return true;
}


//Blocco modifica della coppia sito-fascia se già presente in tabella
function set_check_duplicati(){
	var id = document.getElementById('id').value;
	var idSite = document.getElementById("site");
	var Sitename = idSite.options[idSite.selectedIndex].innerHTML;
	var idFascia = document.getElementById("fascie");
	var Fascianame = idFascia.options[idFascia.selectedIndex].innerHTML;
	var fine = aValue.length;
	for(y=0; y < aValue.length; y++){
	   arrayName = aValue[y];
		if(arrayName instanceof Array){
			if(id == arrayName[1]) //Controllo che non ci siano altre coppie (nomeSito-nomeFascia) ad di fuori della riga selezionata
				continue;
	 		//if(arrayName[4] == Sitename && arrayName[5] == Fascianame)
	 		if(arrayName[4] == Sitename)
	 		{
			return false;
		  }
		}
	}
	return true;
}



//Per inserimento su DB
function save_timesite()
{
	var idSite = document.getElementById("site").value;
	var idFascia = document.getElementById("fascie").value;
	
	if(idFascia != "-1" && idSite != "-1")
	{
	  if(check_duplicati()){
		 document.getElementById("cmd").value="save_sitetime";
		 var ofrm = document.getElementById("frm_fascia_info");
		 if(ofrm != null)
		 {
			MTstartServerComm();
			ofrm.submit();
		 }
	   }
	  else{
	  	alert(document.getElementById('duplicatefasciasite').value);
	  }
	}
	else
	{
		try {
			alert(document.getElementById('notselected').value);
		}catch(e){}
	}
}

//Per modifica record nel DB
function modify_timesite()
{
 	if(setCampiObbligatori()){
 	 if(set_check_duplicati()){
 	   document.getElementById("cmd").value="setSite";
	   var ofrm = document.getElementById("frm_fascia_info");
	   if(ofrm != null)
		   MTstartServerComm();
		ofrm.submit();
 	}
 	 else{
 	 	alert(document.getElementById('duplicatefasciasite').value);
 	 }
 	}
 	else
 	  alert(document.getElementById('notselected').value);
}


function setCampiObbligatori()
{
  var idsite = document.getElementById("site").value;
  var idfascie = document.getElementById("fascie").value;

  if(idsite != "-1" && idfascie != "-1")
	return true;
  return false;
}

//Per rimuovere record dal DB
function remove_timesite()
{
	if (confirm(document.getElementById("confirmfasciasitedel").value))
	{
		document.getElementById("cmd").value="remove_timesite";
		if(document.getElementById("id").value == lineSelected)
		{
			var ofrm = document.getElementById("frm_fascia_info");
			if(ofrm != null)
				MTstartServerComm();
			ofrm.submit();
		}	
	}
}

var lineSelected = null;
var clickState = 0;


function initialize()
{
	enableAction(1);
	disableAction(2);
	disableAction(3);
	
	clear_old_values();
}


function selectedSiteLine(idLine)
{
	if(lineSelected==idLine)
	{
		initialize();
		lineSelected=-1;
	}
	else
	{
		enableAction(2);
		disableAction(1);
		disableAction(3);
		lineSelected=idLine;
	}		
	document.getElementById("id").value = idLine;
	clear_old_values();
}


function selectedLine(idLine){
	clear_old_values();
	CommSend("servlet/ajrefresh","POST","cmd=setsite&id="+idLine+"","PopulateSiteFields", true);

	enableAction(3);
	disableAction(1);
	disableAction(2);
}

function Callback_PopulateSiteFields()
{
	var sitetime = new Array(3);
	var attributes = xmlResponse.getElementsByTagName('attrib');
	sitetime[0] = attributes[0].childNodes[0].nodeValue;	//id sito
	sitetime[1] = attributes[1].childNodes[0].nodeValue; 	//id fascia
	sitetime[2] = attributes[2].childNodes[0].nodeValue;	//abilitata o meno

	var checkabilita = document.getElementById('abilita');
	var fascie = document.getElementById('fascie');
	var site = document.getElementById('site');

	if(sitetime[2] == "true" || sitetime[2] == "TRUE")
		checkabilita.checked = true;
	else
		checkabilita.checked = false;

	for(z=0; z < fascie.options.length; z++){
		if(fascie.options[z].value == sitetime[1]){
			fascie.selectedIndex = z;
			break;
		}
	}
	
	for(y=0; y < site.options.length; y++){
		if(site.options[y].value == sitetime[0]){
			site.selectedIndex = y;
			break;
		}
	}
	
}


function clear_old_values(){
	document.getElementById('site').selectedIndex = 0;
	document.getElementById('fascie').selectedIndex = 0;
	document.getElementById('abilita').checked = false;
}


/**
* Resize tabella tab configurazione heartbeat
*/
function resizeTableTabConfiguration()
{
	var hdev = MTcalcObjectHeight("trConfList");
	if(hdev != 0)
		MTresizeHtmlTable(0,hdev-25);
} 


