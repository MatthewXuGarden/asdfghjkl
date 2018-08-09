
var ERROR_CODE=0;
var OK_CODE=1;

var dateMsg=new String();


//carica il daily come predefinito e inizializza la pagina
function initialize(){
	dateMsg=document.getElementById("dateMsg").innerHTML;
}//initialize


function controlDate(){
	if(document.getElementById("hour_from").value>document.getElementById("hour_to").value){
		alert(dateMsg);
		return ERROR_CODE;
	}//if
	if(document.getElementById("hour_from").value==document.getElementById("hour_to").value){
		if(document.getElementById("minut_from").value>document.getElementById("minut_to").value){
			alert(dateMsg);
			return ERROR_CODE;
		}//if
	}//if
	return OK_CODE;
}//controlDescription

function setRange(x,type){
    var xid = x.id;
    var array_s = xid.split("_");
    var range = document.getElementById("range").value*1;    
	if(range == 0)
		range = 60;
	var ore = "";
	var minuti = "";
	if(type == "ore"){
		ore = document.getElementById(xid).value*1;
		minuti = document.getElementById("minutfrom_"+array_s[1]).value*1;
	}
	if(type == "minuti"){
		ore = document.getElementById("hourfrom_"+array_s[1]).value*1;
		minuti = document.getElementById(xid).value*1;
	}
	if(ore != "-1" && minuti != "-1"){
		if(minuti+range >= 60){	//Quando passo all'ora successiva
			ore +=1;
			minuti = minuti+range - 60;
			if(ore >= 24) //Modifica per oltre le 24
				ore = 0;
		}
		else
			minuti = minuti + range;
		if(minuti < 10)
			minuti = "0" + minuti;
		if(ore < 10)
			ore = "0" + ore;
		document.getElementById("hour_"+array_s[1]).value = ore;
		document.getElementById("minutes_"+array_s[1]).value = minuti;
	}
	else{
		document.getElementById("hour_"+array_s[1]).value = "";
		document.getElementById("minutes_"+array_s[1]).value = "";
	}
}

function changeRange(){
	var ore = 0;
	var minuti = 0;
	var newOre = "";
	var newMin = "";
	var range = document.getElementById("range").value*1;  
	for(i=1; i <= 4; i++){
		ore = document.getElementById("hourfrom_"+i).value*1;
		minuti = document.getElementById("minutfrom_"+i).value*1;
		if(ore != "-1" && minuti != "-1"){
		if(minuti+range >= 60){	//Quando passo all'ora successiva
			ore +=1;
			minuti = minuti+range - 60;
			if(ore >= 24) //Modifica per oltre le 24
				ore = 0;
		}
		else
			minuti = minuti + range;
		if(minuti < 10)
			minuti = "0" + minuti;
		if(ore < 10)
			ore = "0" + ore;
		document.getElementById("hour_"+i).value = ore;
		document.getElementById("minutes_"+i).value = minuti;
	 }
	  else{
		document.getElementById("hour_"+i).value = "";
		document.getElementById("minutes_"+i).value = "";
	 } 
	}
}

function setCampiObbligatori(){
  var nomeFascia = document.getElementById("desc").value;
  var primoslot = document.getElementById("hour_1").value;
  //var maxnrighe = document.getElementById("maxRigheTime").value*1;
  //var trovato = 0;
  //for(z = 1; z <= maxnrighe; z++){
  //	if(document.getElementById("hour_"+z).value != "")
  //		trovato = 1;
  //}
  //if(trovato == 1 && nomeFascia != "")
  //	return true;
  if(primoslot != "" && nomeFascia != "")
  	return true;	
  return false;	
}

//Blocco l'inserimento di fasce con lo stesso nome
function check_duplicati(){
	var nomeFascia = document.getElementById("desc").value;
	var fine = aValue.length;
	for(y=0; y < aValue.length; y++){
	   arrayName = aValue[y];
		if(arrayName instanceof Array){
	 		if(arrayName[arrayName.length-1] == nomeFascia)
			return false;
		}
	}
	return true;
}

function addFasce()
{
	//var nomeFascia = document.getElementById("desc").value;
	if(setCampiObbligatori())
	{
	 if(fascia_not_intersecate()){
	   if(check_duplicati()){
		document.getElementById("cmd").value="save";
		var ofrm = document.getElementById("frm_fascia_info");
		if(ofrm != null)
		 {
			MTstartServerComm();
			ofrm.submit();
		 }
	   }
	  else{
		alert(document.getElementById('duplicatevalue').value);
	  }
	 }
	}
	else
	{
	    alert(document.getElementById('emptyvalue').value);
	}
}

function fascia_not_intersecate(){
	hour_1 = document.getElementById('hour_1').value;
	minutes_1 = document.getElementById('minutes_1').value;
	hour_2 = document.getElementById('hour_2').value;
	minutes_2 = document.getElementById('minutes_2').value;
	hour_3 = document.getElementById('hour_3').value;
	minutes_3 = document.getElementById('minutes_3').value;
	hour_4 = document.getElementById('hour_4').value;
	minutes_4 = document.getElementById('minutes_4').value;
	
	var msgErr="";
	var emptyValue=0;
	var missingSlot = document.getElementById('missingSlot').value;
	var slotName = document.getElementById('slotName').value;
	var slotNonCons = document.getElementById('slotNonCons').value;
	
	//Controllo che le fasce inserite siano progressive es.1-2-3, 1-3 non va bene
	if(hour_4 != ""){
		emptyValue=1;
		if(hour_2 == "")
			msgErr += " 2,";
		if(hour_3 == "")
			msgErr += " 3";
		
	}
	if((hour_3 != "") && (emptyValue == 0)){
		if(hour_2 == "")
			msgErr += " 2 ";
	}
	
	if(msgErr != ""){
		alert(slotName+" "+msgErr+" "+missingSlot);
		return false;
	}
	
	//Controllo che gli orari per le varie fascie siano progressivi
	emptyValue=0;
	if(hour_4 != ""){
		emptyValue=1;
		for(i=1; i <= 3; i++){
			before_hour   = document.getElementById('hour_'+i).value*1; 
			before_minute = document.getElementById('minutes_'+i).value*1;
			before_hour_next   = document.getElementById('hourfrom_'+(i+1)).value*1; 
			before_minute_next = document.getElementById('minutfrom_'+(i+1)).value*1;
			if(before_hour == 24)
				before_hour = 0;
			if(before_hour_next == 24)
				before_hour_next = 0;
			if(before_hour_next < before_hour){
				alert(slotName+" "+i+","+(i+1)+" "+slotNonCons);
				return false;
			}
			if(before_hour_next == before_hour){
				if(before_minute_next <= before_minute){
					alert(slotName+" "+i+","+(i+1)+" "+slotNonCons);
					return false;
				}
			}
		}
	}
	
	if((hour_3 != "") && (emptyValue == 0)){
		emptyValue=1;
		for(i=1; i <= 2; i++){
			before_hour   = document.getElementById('hour_'+i).value*1; 
			before_minute = document.getElementById('minutes_'+i).value*1;
			before_hour_next   = document.getElementById('hourfrom_'+(i+1)).value*1; 
			before_minute_next = document.getElementById('minutfrom_'+(i+1)).value*1;
			if(before_hour == 24)
				before_hour = 0;
			if(before_hour_next == 24)
				before_hour_next = 0;
			if(before_hour_next < before_hour){
				alert(slotName+" "+i+","+(i+1)+" "+slotNonCons);
				return false;
			}
			if(before_hour_next == before_hour){
				if(before_minute_next <= before_minute){
					alert(slotName+" "+i+","+(i+1)+" "+slotNonCons);
					return false;
				}
			}
		}
	}
	
	if((hour_2 != "") && (emptyValue == 0)){
		for(i=1; i <= 1; i++){
			//first_before_hour   = document.getElementById('hourfrom_'+(i)).value*1; 
			//first_before_minute = document.getElementById('minutfrom_'+(i)).value*1;
			before_hour   = document.getElementById('hour_'+i).value*1; 
			before_minute = document.getElementById('minutes_'+i).value*1;
			before_hour_next   = document.getElementById('hourfrom_'+(i+1)).value*1; 
			before_minute_next = document.getElementById('minutfrom_'+(i+1)).value*1;
			if(before_hour == 24)
				before_hour = 0;
			if(before_hour_next == 24)
				before_hour_next = 0;
			//alert("Prima: "+before_hour+":"+before_minute+" Dopo: "+before_hour_next+":"+before_minute_next)
			if(before_hour_next < before_hour){
				alert(slotName+" "+i+","+(i+1)+" "+slotNonCons);
				return false;
			}
			if(before_hour_next == before_hour){
				if(before_minute_next <= before_minute){
					alert(slotName+" "+i+","+(i+1)+" "+slotNonCons);
					return false;
				}
			}
		}
	}
	
	return true;
}

var lineSelected = null;
var clickState = 0;

/* VECCHIA FUNZIONE FUNZIONANTE PER IL CLICK DEL MOUSE
function selectedLine(idLine)
{
	if (idLine==null) return true;
	if (lineSelected==idLine) 
	{
		if ((clickState==null)||(clickState==0))
		{
			//Sono in modifica Vecchio modo Funzionante
			//clickState=1;
			//enableAction(3);
			//disableAction(1);
			//disableAction(2);
			//alert("Modifica: " + clickState);
			
			//Nuovo Modo
			clickState=1;
			disableAction(1);
			enableAction(2);
			enableAction(3);
			setselectedLine(idLine);
			document.getElementById("desc").focus();
			
			//clear_old_values();
			//Fine Modo
			
		} 
		else
		{
		//Ritorno in modalità aggiungi riga
		//clear_old_values()
		//clickState=0;
		//enableAction(1);
		//disableAction(2);
		//disableAction(3);
		document.getElementById("desc").focus();
		//alert("Aggiungi: "+ clickState);
		
		//Nuovo Modo
		clickState=0;
		enableAction(1);
		disableAction(2);
		disableAction(3);
		clear_old_values();
		
		
		}
	}
	else
	{
		//Sono in remove
		//clickState=1;
		//enableAction(2);
		//disableAction(3);
		//disableAction(1);
		//document.getElementById("desc").focus();
		//alert("Remove: "+ clickState);
		clickState=1;
		disableAction(1);
		enableAction(2);
		enableAction(3);
		setselectedLine(idLine);
	}
	lineSelected= idLine;
	document.getElementById("removeFascia").value = idLine;
}
*/

function initialize()
{
	enableAction(1);
	disableAction(2);
	disableAction(3);
	
	clear_old_values();
}

function selectedLine(idLine)
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
	document.getElementById("removeFascia").value = idLine;
	clear_old_values();
}

var RIMUOVI_ID = 0;

function removeFasce()
{
	if (confirm(document.getElementById("confirmfasciadel").value))
	{
		isRemovable(document.getElementById("removeFascia").value);
		window.setTimeout('delete_record()',1000);
	}
}

//Perchè devo aspettare che la chiamata ad Ajax sia finita prima di sapere
//se posso eliminare il record 
function delete_record(){
	if(RIMUOVI_ID == 1){
		  if(document.getElementById("removeFascia").value == lineSelected)
		  {
			document.getElementById("cmd").value="remove";
			var ofrm = document.getElementById("frm_fascia_info");
			if(ofrm != null)
				MTstartServerComm();
			ofrm.submit();
		  }
		}
		else
		 alert(document.getElementById('isremovable').value); 
}


function saveFasce(){
	document.getElementById("cmd").value="set";
  if(setCampiObbligatori()){	
	if(fascia_not_intersecate()){
		if(document.getElementById("removeFascia").value != -1)
		{
		  if(confirm(document.getElementById("confirmfasciaupdate").value)){
	    	var ofrm = document.getElementById("frm_fascia_info");
	    	if(ofrm != null)
		    	MTstartServerComm();
		 	ofrm.submit();
		 	}
		}
  	}
  }
  else
  {
	alert(document.getElementById('emptyvalue').value);
  }
}

function isRemovable(idLine){
	//alert("IsRemovable: "+idLine)
	CommSend("servlet/ajrefresh","POST","cmd=isRemovable&idrmtimetable="+idLine+"","IsRemovable",true);
}

function Callback_IsRemovable()
{
	var ok = new Array(1); 
	var attributes = xmlResponse.getElementsByTagName('attrib');
	ok[0] = attributes[0].childNodes[0].nodeValue;	//ok se posso eliminare il record	
	if(ok[0] == "OK"){
		RIMUOVI_ID = 1;
	}
	else	
		RIMUOVI_ID = 0;
}

function setselectedLine(idLine){
	clear_old_values();
	enableAction(3);
	disableAction(1);
	disableAction(2);
	CommSend("servlet/ajrefresh","POST","cmd=set&idrmtimetable="+idLine+"","PopulateFields", true);
}

function clear_old_values(){
	document.getElementById('range').selectedIndex = 0;
	document.getElementById('desc').value = "";
  for(z = 1; z <= 4; z++){	
	document.getElementById('hourfrom_'+z).selectedIndex = 0;
	document.getElementById('minutfrom_'+z).selectedIndex = 0;
	document.getElementById('hour_'+z).value = "";
	document.getElementById('minutes_'+z).value = "";
	}
}

function Callback_PopulateFields()
{
	var time = new Array(24); 
	var attributes = xmlResponse.getElementsByTagName('attrib');
	time[0] = attributes[0].childNodes[0].nodeValue;	//nome fascia
	time[1] = attributes[1].childNodes[0].nodeValue; 	//hour_from
	time[2] = attributes[2].childNodes[0].nodeValue;	//minutes_from
	time[3] = attributes[3].childNodes[0].nodeValue;	//hour_to
	time[4] = attributes[4].childNodes[0].nodeValue;	//minute_to
	time[5] = attributes[5].childNodes[0].nodeValue;	//delta

	var range = document.getElementById('range');
	document.getElementById('desc').value = time[0]; 
	for(i=0; i < range.options.length; i++){
		if(range.options[i].value == time[5]){
			range.selectedIndex = i;
		}
	}
	
	document.getElementById('hourfrom_1').selectedIndex = time[1]*1+1;
	document.getElementById('minutfrom_1').selectedIndex = time[2]*1+1;
	if(time[3]*1 < 10)
		time[3] = "0"+time[3];
	document.getElementById('hour_1').value = time[3];
	if(time[4]*1 < 10)
		time[4] = "0"+time[4];
	document.getElementById('minutes_1').value = time[4];

	if(attributes[7].childNodes[0] != null){
		time[7] = attributes[7].childNodes[0].nodeValue;
		time[8] = attributes[8].childNodes[0].nodeValue;
		time[9] = attributes[9].childNodes[0].nodeValue;
		time[10] = attributes[10].childNodes[0].nodeValue;
	
	    document.getElementById('hourfrom_2').selectedIndex = time[7]*1+1;
	    document.getElementById('minutfrom_2').selectedIndex = time[8]*1+1;
	    if(time[9]*1 < 10)
			time[9] = "0"+time[9];
	    document.getElementById('hour_2').value = time[9];
	    if(time[10]*1 < 10)
			time[10] = "0"+time[10];
	    document.getElementById('minutes_2').value = time[10];
	}
	
	if(attributes[13].childNodes[0] != null){
		time[13] = attributes[13].childNodes[0].nodeValue;
		time[14] = attributes[14].childNodes[0].nodeValue;
		time[15] = attributes[15].childNodes[0].nodeValue;
		time[16] = attributes[16].childNodes[0].nodeValue;
	
	    document.getElementById('hourfrom_3').selectedIndex = time[13]*1+1;
	    document.getElementById('minutfrom_3').selectedIndex = time[14]*1+1;
	    if(time[15]*1 < 10)
			time[15] = "0"+time[15];
	    document.getElementById('hour_3').value = time[15];
	    if(time[16]*1 < 10)
			time[16] = "0"+time[16];
	    document.getElementById('minutes_3').value = time[16];
	}
	
	if(attributes[19].childNodes[0] != null){
		time[19] = attributes[19].childNodes[0].nodeValue;
		time[20] = attributes[20].childNodes[0].nodeValue;
		time[21] = attributes[21].childNodes[0].nodeValue;
		time[22] = attributes[22].childNodes[0].nodeValue;
	
	    document.getElementById('hourfrom_4').selectedIndex = time[19]*1+1;
	    document.getElementById('minutfrom_4').selectedIndex = time[20]*1+1;
	    if(time[21]*1 < 10)
			time[21] = "0"+time[21];
	    document.getElementById('hour_4').value = time[21];
	    if(time[22]*1 < 10)
			time[22] = "0"+time[22];
	    document.getElementById('minutes_4').value = time[22];
	}
}

/**
* Resize tabella
*/
function resizeTableTab()
{
	var hdev = MTcalcObjectHeight("trList");
	if(hdev != 0)
		MTresizeHtmlTable(0,hdev);
} 

