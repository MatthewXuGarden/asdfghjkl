var rowSelected=-1;  //riga selezionata in tabella
var daySelect=-1;    //giorno selezionato nella selezione mensile/annuale/special
var selectRowMasterTable=-1; //riga selezionata nella tabella centrale della pagina 
var selectArgMasterTable=null;
var month31Table=monthTable(31); //tabella con 31 giorni
var	month30Table=monthTable(30); //tabella con 30 giorni
var	month29Table=monthTable(29); //tabella con 29 giorni
var	month28Table=monthTable(28); //tabella con 28 giorni

//informazioni sulla Data Attuale
var data = Date();
var dataAttuale=data.toString().split(" ");
var giornoAttuale=dataAttuale[0];  
var meseAttuale=dataAttuale[1];
var giornoNumericoAttuale=dataAttuale[2];
var oraAttuale=dataAttuale[3];
var annoAttuale=dataAttuale[4].split(":").length==3?dataAttuale[3]:dataAttuale[4];

/*
alert("giornoAttuale"+giornoAttuale);
alert("meseAttuale"+meseAttuale);
alert("giornoNumericoAttuale"+giornoNumericoAttuale);
alert("oraAttuale"+oraAttuale);
alert("annoAttuale"+annoAttuale);
*/

var minutsSelectBody= new String();

var annoSelezionato=annoAttuale;

var ERROR_CODE=0;
var OK_CODE=1;

var DAILY=1;
var WEEKLY=2;
var MONTHLY=3;
var YEARLY=5;
var SPECIAL=4;



//Messaggi

var dateMsg=new String();
var descMsg=new String();
var daysMsg=new String();


var tableTextMsg=new String();
var daySelectMsg=new String();
var noRemoveMsg=new String();
var ideMsg=new String();
var rowDuplicateMsg=new String();
var noAddMsg = new String();
var noModMsg = new String();



//carica il daily come predefinito e inizializza la pagina
function initialize(){
	
	document.getElementById("pseudo_frame").innerHTML = document.getElementById("1").innerHTML;
	dateMsg=document.getElementById("dateMsg").innerHTML;
	descMsg=document.getElementById("descMsg").innerHTML;
	daysMsg=document.getElementById("daysMsg").innerHTML;
	rowDuplicateMsg=document.getElementById("rowDuplicateMsg").innerHTML;
	enableAction(1);
	
	noRemoveMsg=document.getElementById("noRemoveMsg").innerHTML;
	var removeDefaultForbid=document.getElementById("removeDefaultForbid").innerHTML;
	ideMsg=document.getElementById("ideMsg").innerHTML;
	noAddMsg=document.getElementById("noAddMsg").innerHTML;
	noModMsg=document.getElementById("noModMsg").innerHTML;
	tableTextMsg=document.getElementById("tableTextMsg").innerHTML;
	daySelectMsg=document.getElementById("daySelectMsg").innerHTML;

	if(document.getElementById("noRemove").innerHTML=="NO")
		alert(noRemoveMsg);
	if(document.getElementById("noRemove").innerHTML=="NoDefault")
		alert(removeDefaultForbid);
	if(document.getElementById("noRemove").innerHTML=="-1")
		alert(ideMsg);
	if(document.getElementById("noRemove").innerHTML=="noAdd")
		alert(noAddMsg);
	if(document.getElementById("noRemove").innerHTML=="noMod")
		alert(noModMsg);
}//initialize


//carica il div corretto a seconda della selezione del periodo 
function changePseudoFrame(){
	var type=document.getElementById("type").value;
	var calendar=document.getElementById("caldar");
	if (type != '') {
		document.getElementById("cal_div").appendChild(calendar);//move the calendar to safe area
		document.getElementById("pseudo_frame").innerHTML = document.getElementById(type).innerHTML;
		if(type=="3" ){
			document.getElementById("cal_month").appendChild(calendar);
		}else if(type=="4" ){
			document.getElementById("cal_spec").appendChild(calendar);
		}else if(type=="5"){
			document.getElementById("cal_year").appendChild(calendar);
		}
	}
	else {
		document.getElementById("pseudo_frame").innerHTML = document.getElementById("1").innerHTML;
	}
//	document.getElementById("monthTable").innerHTML=month31Table;
	resetTimeBands('changePseudoFrame');
}//changePseudoFrame

function dayClick(day){
	
	if(daySelect!=-1)
		document.getElementById("day"+daySelect).style.color ='black';
	document.getElementById("day"+day).style.color ='red';
	daySelect=day;
}//dayClick

function arrowAdd(type){
	var dmy,mese;
	if(type>=3){
		//get calendar's date that user choosed.
		dmy=getChosedDate(document.getElementById("tester").value);
		daySelect=dmy[0];
		mese=dmy[1];
		document.getElementById("monthNumber").innerHTML=dmy[1];
	}
	if(controlOverlappedDate(type)==ERROR_CODE)
		return;
	if(controlDate()==ERROR_CODE)
		return;
	var table="<table  class='table' width=\"100%\">";
	var bodyTable=document.getElementById("divTable"+type).getElementsByTagName("tbody")[0];
	
	var ore = document.getElementById("hour_from").value + ":" +	document.getElementById("minut_from").value + "-"+	document.getElementById("hour_to").value+ ":"+	document.getElementById("minut_to").value;
//	var mese= document.getElementById("monthNumber").innerHTML;
	var values= new Array();
	if(type==1){//daily
 		var bodyAdd= "<tr style=\"cursor:pointer\" class=\"Row1\" onclick=\"select('divTable1',this);\"><td  class='tdmini' width='20%' >&nbsp;</td><td  class=\"tdmini\" width=\"80%\" >" +ore +"</td></tr></div>";
		values[0]=ore;
 	}else if(type==2){//weekly
		if(controlDaysSelect()==ERROR_CODE)
			return;
 		var valuesDay=new Array();
 		values[0]=ore;

		valuesDay[0]=((document.getElementById("monday").checked==true)?"<img src=\"images/ok.gif\"/>":"&nbsp;");
 		valuesDay[1]=((document.getElementById("tuesday").checked==true)?"<img src=\"images/ok.gif\"/>":"&nbsp;");
		valuesDay[2]=((document.getElementById("wednesday").checked==true)?"<img src=\"images/ok.gif\"/>":"&nbsp;");
		valuesDay[3]=((document.getElementById("thursday").checked==true)?"<img src=\"images/ok.gif\"/>":"&nbsp;");
		valuesDay[4]=((document.getElementById("friday").checked==true)?"<img src=\"images/ok.gif\"/>":"&nbsp;");
		valuesDay[5]=((document.getElementById("saturday").checked==true)?"<img src=\"images/ok.gif\"/>":"&nbsp;");
		valuesDay[6]=((document.getElementById("sunday").checked==true)?"<img src=\"images/ok.gif\"/>":"&nbsp;");
		
		values[1]=((document.getElementById("monday").checked==true)?null:"&nbsp;");
		values[2]=((document.getElementById("tuesday").checked==true)?null:"&nbsp;");
		values[3]=((document.getElementById("wednesday").checked==true)?null:"&nbsp;");
		values[4]=((document.getElementById("thursday").checked==true)?null:"&nbsp;");
		values[5]=((document.getElementById("friday").checked==true)?null:"&nbsp;");
		values[6]=((document.getElementById("saturday").checked==true)?null:"&nbsp;");
		values[7]=((document.getElementById("sunday").checked==true)?null:"&nbsp;");
		
 		var bodyAdd= "<tr style=\"cursor:pointer\" class=\"Row1\" onclick=\"select('divTable2',this);\"><td class='tdmini'  width='10%' >&nbsp;</td><td  class='tdmini' width='34%' >" +  ore + "</td><td class='tdmini' width='8%' >"+valuesDay[0]+"</td><td class='tdmini'  width='8%'>"+valuesDay[1]+"</td><td class='tdmini' width='8%' >"+valuesDay[2]+"</td><td class='tdmini' width='8%' >"+valuesDay[3]+"</td><td class='tdmini' width='8%'  >"+valuesDay[4]+"</td><td class='tdmini' width='8%'  >"+valuesDay[5]+"</td><td class='tdmini' width='8%' >"+valuesDay[6]+"</td></tr></div>";
		
	}else if(type==3){//monthly
		if(controlDayNumberSelect()==ERROR_CODE)
			return;
		values[0]=	(daySelect<10?"0"+daySelect:""+daySelect)+ " - "+ore;
		var bodyAdd= "<tr style=\"cursor:pointer\" class=\"Row1\" onclick=\"select('divTable3',this);\"><td class='tdmini' width='20%' >&nbsp;</td><td  class='tdmini' width='80%' >"+values[0]+"</td></tr></div>";
	}else if(type==4){//special
		if(controlDayNumberSelect()==ERROR_CODE)
			return;
		values[0]=	(daySelect<10?"0"+daySelect:""+daySelect)+"/"+(mese<10?"0"+mese:""+mese)+ "/"+annoSelezionato+" - "+ore;
		var bodyAdd= "<tr style=\"cursor:pointer\" class=\"Row1\" onclick=\"select('divTable4',this);\"><td class='tdmini' width='20%' >&nbsp;</td><td  class='tdmini' width='80%' >"+values[0]+"</td></tr></div>";
	}else if(type==5){//yearly
		if(controlDayNumberSelect()==ERROR_CODE)
			return;
		values[0]=	(daySelect<10?"0"+daySelect:""+daySelect)+"/"+(mese<10?"0"+mese:""+mese)+ " - "+ore;
		var bodyAdd= "<tr style=\"cursor:pointer\" class=\"Row1\" onclick=\"select('divTable5',this);\"><td class='tdmini' width='20%' >&nbsp;</td><td  class='tdmini' width='80%' >"+values[0]+"</td></tr></div>";
	}
	var stringa=bodyTable.innerHTML;

	if(controlRedundantRow(bodyTable,values)==ERROR_CODE)
		return;
	
	
	table+=bodyTable.innerHTML+bodyAdd+"</table>";
	document.getElementById("divTable"+type).innerHTML=table;
	
	//riorganizza il valore del campo #	
	bodyTable=document.getElementById("divTable"+type).getElementsByTagName("tbody")[0];
	reorderTableIndex(bodyTable);
}//arrowAdd

function getChosedDate(da){
	var s=da.split("/");
	if(s.length<3)
		return (new Array(3));
	else
		return s;
}
function select(id,obj){
	var bodyTable=document.getElementById(id).getElementsByTagName("tbody")[0];
	if(obj.className=="Row1"){
		for (var i = 0; i < bodyTable.rows.length; i++){
			if(bodyTable.rows[i]==obj){
				if(rowSelected!=-1)
					bodyTable.rows[rowSelected].className="Row1";
				rowSelected=i;
			break;
			}//if	
		}//for
		obj.className="selectedRow";
	}else{
		obj.className="Row1";
		rowSelected=-1;
 	}//else 
}//select


function deleteRow(type){
	if(rowSelected!=-1){
		var bodyTable=document.getElementById("divTable"+type).getElementsByTagName("tbody")[0];
		bodyTable.deleteRow(rowSelected);	
		rowSelected=-1;
		reorderTableIndex(bodyTable);
	}//if
}//deleteRow
	
	
function reorderTableIndex(obj){
	for (var i = 0; i < obj.rows.length; i++){
		obj.rows[i].cells[0].firstChild.nodeValue=i+1;
	}//for	
}//reorderTableIndex

function loadTimeBands(arg){
	if(arg == null || arg == "null")
	{
		return;
	}
	var informationRow = new Array();
	informationRow=arg.split(";;");
	if(new Number(informationRow[3])<0){
		alert(ideMsg);
		return;
	}//if
	var type=informationRow[1];
	
	document.getElementById("type").value=type;
	document.getElementById("type").onchange();
	
	//Mantenere l'ordine rispetto al change altrimenti perdo i valori del idtimebadns	
	document.getElementById("desc").value=informationRow[0];
	document.getElementById("idTimeBands").value=informationRow[3]; 
	
	
		enableAction(3);//save
		disableAction(1);
		disableAction(2);	
	
	var table="<table  class='table' width=\"100%\"><tbody>";
	var rownumber = 1;
 	if(type==1){//daily
	 	var tableRows = new Array();
		tableRows=informationRow[2].split(";");
		for(var i=0;i<tableRows.length;i++)
 			table+="<tr style=\"cursor:pointer\" class=\"Row1\" onclick=\"select('divTable1',this);\"><td width=\"20%\" class='tdmini'>"+(i+1)+"</td><td width=\"80%\" class='tdmini'>" +tableRows[i]+ "</td></tr>";	
	}else if(type==2){//weekly
		var firstLevel=new Array();
		firstLevel=informationRow[2].split(",");
		for(var i=0;i<firstLevel.length;i++){
			var secondLevel=new Array();
			secondLevel=firstLevel[i].split("|");
			var days=secondLevel[0].split("+");
			var lun="&nbsp";var mar="&nbsp";var mer="&nbsp";var gio="&nbsp";var ven="&nbsp";var sab="&nbsp";var dom="&nbsp";
				for(var z=0;z<days.length;z++){
					switch(Number(days[z])){ 
						case 1:
							dom="<img src=\"images/ok.gif\"/>";
						break;
						case 2:
							lun="<img src=\"images/ok.gif\"/>";
						break;
						case 3:
							mar="<img src=\"images/ok.gif\"/>";
						break;
						case 4:
							mer="<img src=\"images/ok.gif\"/>";
						break;
						case 5:
							gio="<img src=\"images/ok.gif\"/>";
						break;
						case 6:
							ven="<img src=\"images/ok.gif\"/>";
						break;
						case 7:
							sab="<img src=\"images/ok.gif\"/>";
						break;
					}//switch 
				}//for in z
				var thirdLevel=secondLevel[1].split(";");
				for(var w=0;w<thirdLevel.length;w++){
					table+= "<tr style=\"cursor:pointer\" class=\"Row1\" onclick=\"select('divTable2',this);\"><td class='tdmini'   width='10%' >"+(rownumber++)+"</td><td  class='tdmini' width='34%' >" +	thirdLevel[w] + "</td><td class='tdmini' width='8%'   >"+lun+"</td><td class='tdmini' width='8%' >"+mar+"</td><td class='tdmini'  width='8%' >"+mer+"</td><td class='tdmini' width='8%' >"+gio+"</td><td class='tdmini'  >"+ven+"</td><td class='tdmini'  width='8%' >"+sab+"</td><td class='tdmini' width='8%' >"+dom+"</td></tr>";
				}//for in w	
		}//for in i
	}else if(type==3){//monthly
		var firstLevel=new Array();
		firstLevel=informationRow[2].split(",");
		for(var i=0;i<firstLevel.length;i++){
			var secondLevel=new Array();
			secondLevel=firstLevel[i].split("|");
			var days=secondLevel[0].split("+");
			var thirdLevel=secondLevel[1].split(";");
			for(var z=0;z<days.length;z++){
				for(var w=0;w<thirdLevel.length;w++){
					table += "<tr style=\"cursor:pointer\" class=\"Row1\" onclick=\"select('divTable3',this);\"><td class='tdmini' width='20%' >"+(rownumber++)+"</td><td  class='tdmini' width='80%' >"+days[z]+" - "+thirdLevel[w]+"</td></tr>";
				}//for in w	
			}//for in z
		}//for in i
	
	}else if(type==4){//special
		firstLevel=informationRow[2].split(",");
		for(var i=0;i<firstLevel.length;i++){
			var secondLevel=new Array();
			secondLevel=firstLevel[i].split("|");
			table+= "<tr style=\"cursor:pointer\" class=\"Row1\" onclick=\"select('divTable4',this);\"><td class='tdmini' width='20%' >"+(rownumber++)+"</td><td  class='tdmini' width='80%' >"+secondLevel[0]+ " - "+secondLevel[1]+"</td></tr>";
		}//for		
		
		}else if(type==5){//yearly
		var firstLevel=new Array();
		firstLevel=informationRow[2].split(",");
		for(var i=0;i<firstLevel.length;i++){
			var secondLevel=new Array();
			secondLevel=firstLevel[i].split("|");
			table+= "<tr style=\"cursor:pointer\" class=\"Row1\" onclick=\"select('divTable5',this);\"><td class='tdmini' width='20%' >"+(rownumber++)+"</td><td  class='tdmini' width='80%' >"+secondLevel[0]+ " - "+secondLevel[1]+"</td></tr>";
		}//for		
	}
	table+="</tbody></table>";
	if (type == null) {
		type=1;
	}
	var d= document.getElementById("divTable"+type);
 	document.getElementById("divTable"+type).innerHTML=table;

 	if (document.getElementById("desc").value == "null") {
 		document.getElementById("desc").value = '';
 	}
	//riorganizza il valore del campo #	
	//var bodyTable=document.getElementById("divTable"+type).getElementsByTagName("tbody")[0];
	//reorderTableIndex(bodyTable);
}//loadTimeBands


function resetTimeBands(arg){
	var informationRow = new Array();
	informationRow=arg.split(";;");
	annoSelezionato=annoAttuale;
	
	//document.getElementById("desc").value="";
	rowSelected=-1;
	daySelect=-1;
	document.getElementById("divTable1").innerHTML="<table  class='table'><tbody></tbody></table>";
	document.getElementById("divTable2").innerHTML="<table  class='table'><tbody></tbody></table>";
	document.getElementById("divTable3").innerHTML="<table  class='table'><tbody></tbody></table>";
	document.getElementById("divTable4").innerHTML="<table  class='table'><tbody></tbody></table>";
	document.getElementById("divTable5").innerHTML="<table  class='table'><tbody></tbody></table>";

	if(arg!="changePseudoFrame"){//cmbbox del tipo ripetizione evento
		document.getElementById("idTimeBands").value=informationRow[3];
		if(selectRowMasterTable==informationRow[3]){
			enableAction(1);//add
			disableAction(2);
			disableAction(3);
			disableAction(4);
			selectRowMasterTable=-1;
			selectArgMasterTable=null;
		}//if
		else{
			enableAction(2);//delete
			disableAction(1);
			enableAction(3);//save
			enableAction(4);
			selectRowMasterTable=informationRow[3];
			selectArgMasterTable=arg;
		}//else	
	}//if
	else{
		if(selectRowMasterTable==-1){
			enableAction(1);//add
			disableAction(2);
			disableAction(3);
			disableAction(4);
		}//if
		else{
			enableAction(2);//delete
			disableAction(1);
			enableAction(3);//save
			enableAction(4);
		}//else
	}//else
}//resetTimeBands


function monthTable(days){
	var monthTbl= new String();
	monthTbl+="<table class=\"standardTxt\">";
	monthTbl+="\n<tr><tbody>";		
	
	for(var i=0;i<days+1;i++){

		if(i%7==0)
			monthTbl+="\n</tr>\n<tr>";		
		
		if(i==days){
			monthTbl+="<td style=\"float:left;width:22px;\" >&nbsp;</td></tr>";	
			break;
		}//if
		monthTbl+="\n<td style=\"float:left;cursor:pointer;width:22px;background-color:#EFF1FE\" id=\"day";
		monthTbl+=(i+1);
		monthTbl+="\" onclick=\" dayClick(";
		monthTbl+=((i+1)<10)?("0"+(i+1)):(""+(i+1));
		monthTbl+=") \">";
		monthTbl+=((i+1)<10)?("0"+(i+1)):(""+(i+1));
		monthTbl+="</td>";		
	}//for
		
	monthTbl+="</tbody></table>";
	
	return monthTbl;
}//monthTable

function changeMonth(num){
	daySelect=-1;
	var n=Number(document.getElementById("monthNumber").innerHTML);
	
	if(num=="1"){n++;if(n>12)n=1;}//if
	else{n--;if(n<1)n=12;}//else
	
	document.getElementById("monthNumber").innerHTML=n;	
	document.getElementById("monthString").innerHTML=document.getElementById("month"+n).innerHTML;	
	
	if((n==1)||(n==3)||(n==5)||(n==7)||(n==8)||(n==10)||(n==12)){
		document.getElementById("monthTable").innerHTML=month31Table;
	}//if
	else
	if((n==4)||(n==6)||(n==9)||(n==11)){
		document.getElementById("monthTable").innerHTML=month30Table;
	}//if
	else{
	if((annoSelezionato%4==0)&&((annoSelezionato%100!=0)||(annoSelezionato%400==0)))	
		document.getElementById("monthTable").innerHTML=month29Table;
	else
		document.getElementById("monthTable").innerHTML=month28Table;
	}//else
	
}//changeMonth


function changeYear(num){
	daySelect=-1;
	var n=Number(document.getElementById("yearNumber").innerHTML);
	
	if(num=="1"){n++;if(n>2099)n=2006;}//if
	else{n--;if(n<2006)n=2099;}//else
	annoSelezionato=n;
	
	document.getElementById("yearNumber").innerHTML=annoSelezionato;
	
	//Carico Gennaio al cambio di annata
	document.getElementById("monthNumber").innerHTML=1;	
	document.getElementById("monthString").innerHTML=document.getElementById("month1").innerHTML;	
	document.getElementById("monthTable").innerHTML=month31Table;	
	
}//changeMonth


function timeBandsAction(actionType){
	var valueTxt="";
	for(;;){
		if(actionType=="add"){
			if(controlDescription()==ERROR_CODE)
				return;
			document.getElementById("action").value="add";
			 valueTxt=prepareTimeBandsValue();
			if(controlTableText(valueTxt)==ERROR_CODE)
				return;
			document.getElementById("timeBandValue").value=valueTxt;
			
		break;
		}//if
		if(actionType=="del")
		{
			if (confirm(document.getElementById("confirmtimebdel").value))
			{	
				document.getElementById("action").value="del";
				break;
			}
			else return false;
		}//if
		if(actionType=="mod"){
			document.getElementById("action").value="mod";
				valueTxt=prepareTimeBandsValue();
			if(controlTableText(valueTxt)==ERROR_CODE)
				return;
			document.getElementById("timeBandValue").value=valueTxt;
		break;
		}//if
		break;
	}//for
	var ofrm = document.getElementById("frm_timebands");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
	
}//timeBandsAction

function controlDescription(){
	var description = document.getElementById("desc").value; 
	if( description == "" ) {
		alert(descMsg);
		return ERROR_CODE;
	}
	// check for duplicates
	if( Lsw1 && Lsw1.mData ) {
		var listTimebands = Lsw1.mData;
		for(var i = 0; i < listTimebands.length; i++) {
			if( description == listTimebands[i][4] ) {
				alert(document.getElementById("duplicatecodemsg").value);
				return ERROR_CODE;
			}
		}
	}
	return OK_CODE;
}//controlDescription


function prepareTimeBandsValue(){
	var type=document.getElementById("type").value
	var tableEl =document.getElementById("divTable"+type).getElementsByTagName("tbody")[0];
	var i;
	var value="";
	for(;;){
		if(type==1){
			for (i = 0; i < tableEl.rows.length; i++){
				value+=tableEl.rows[i].cells[1].firstChild.nodeValue;
				if(i!=tableEl.rows.length-1)//sarebbe da fare fuori ma...
					value+=";";
			}//for
			break;
		}//if
		if(type==2){
			for (i = 0; i < tableEl.rows.length; i++){
	  			valueTmp="";
	  			if(tableEl.rows[i].cells[2].firstChild.nodeValue==null)
	  				if(valueTmp!="")valueTmp+="+2";
	  				else valueTmp+="2";
	  			if(tableEl.rows[i].cells[3].firstChild.nodeValue==null)
	  				if(valueTmp!="") valueTmp+="+3";
	  				else valueTmp="3";
	  			if(tableEl.rows[i].cells[4].firstChild.nodeValue==null)
	  				if(valueTmp!="") valueTmp+="+4";
	  				else valueTmp+="4";
	  			if(tableEl.rows[i].cells[5].firstChild.nodeValue==null)
	  				if(valueTmp!="") valueTmp+="+5";
	  				else valueTmp+="5";
	  			if(tableEl.rows[i].cells[6].firstChild.nodeValue==null)
	  				if(valueTmp!="") valueTmp+="+6";
	  				else valueTmp+="6";
	  			if(tableEl.rows[i].cells[7].firstChild.nodeValue==null)
	  				if(valueTmp!="") valueTmp+="+7";
	  				else valueTmp+="7";
	  			if(tableEl.rows[i].cells[8].firstChild.nodeValue==null)
	  				if(valueTmp!="") valueTmp+="+1";
	  				else valueTmp+="1";
	  			value+=valueTmp;	
	  			value+="|";
				value+=tableEl.rows[i].cells[1].firstChild.nodeValue;
				if(i!=tableEl.rows.length-1)
					value+=",";
			}//for
			break;
		}//if
		if((type==3)||(type==5)||(type==4)){
			for (i = 0; i < tableEl.rows.length; i++){
				var data=tableEl.rows[i].cells[1].firstChild.nodeValue.split("-");
			    value+=data[0]+"|"+data[1]+"-"+data[2];
			if(i!=tableEl.rows.length-1)
				value+=",";
			}//for
			break;
		}//if	
	break;
	}//forswitch
	
	return value;
}//prepareTimeBandsValue


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

function controlDaysSelect(){
	if((!document.getElementById("monday").checked)&&
	(!document.getElementById("tuesday").checked)&&
	(!document.getElementById("wednesday").checked)&&
	(!document.getElementById("thursday").checked)&&
	(!document.getElementById("friday").checked)&&
	(!document.getElementById("saturday").checked)&&
	(!document.getElementById("sunday").checked)){
		alert(daysMsg);
		return ERROR_CODE;
	}//if
	return OK_CODE;
}//controlDaysSelect


function controlTableText(tableText){
	if(tableText==""){
		alert(tableTextMsg);
		return ERROR_CODE;
	}//if
	return OK_CODE;
}//controlTableText

function controlDayNumberSelect(){
	if(daySelect==-1){
		alert(daySelectMsg);
		return ERROR_CODE;
	}//if
	return OK_CODE;
}//controlTableText



function controlRedundantRow(table,values){
	var flag=0;
	for(var i = 0; i < table.rows.length; i++){
		flag=0;
 		for (var j = 1; j < table.rows[i].cells.length; j++){
 			if((trimString(table.rows[i].cells[j].firstChild.nodeValue)==trimString(values[j-1]))||(trimString(table.rows[i].cells[j].innerHTML)==trimString(values[j-1])))
 				flag++;
 		}//for
		if(flag==j-1){
			alert(rowDuplicateMsg);
			return ERROR_CODE;
		}//if
	}//for
	return OK_CODE;
}//controlRedundantRow


function trimString(str){
	var tmpStrs= new Array();
	var tmpStr= new String();
	if(str==null)
		return null;
	tmpStrs=str.split(' ');
	for(var i=0;i<tmpStrs.length;i++){
		if(tmpStrs[i].length>0){
			tmpStr+=tmpStrs[i];
		}//if
	}//for
	return tmpStr;
}//trimString


function control2425(){
	if(document.getElementById("hour_to").value=="24"){
		document.getElementById("minut_to").value="00";
		document.getElementById("minut_to").disabled=true;
	}//if
	else{
		document.getElementById("minut_to").disabled=false;
	}//else
}//control2425

//A------------------B--------//
//--------A'---------------B'-//
//  Fasce Orarie sovrapposte //





function controlOverlappedDate(type){
	
	var bodyTable=document.getElementById("divTable"+type).getElementsByTagName("tbody")[0];
	for(var i=0;i<bodyTable.rows.length;i++){
		var checkData=false;	
		var tmpTime= new Array();
		if(type==DAILY){
			tmpTime=bodyTable.rows[i].cells[1].firstChild.nodeValue.split("-");
			
			checkData=true;
		}//if DAILY
		if(type==WEEKLY){
			tmpTime=bodyTable.rows[i].cells[1].firstChild.nodeValue.split("-");
			do{	
				if((bodyTable.rows[i].cells[2].firstChild.nodeValue==null)&&(document.getElementById("monday").checked)){
					checkData=true;
					break;
				}//if
				if((bodyTable.rows[i].cells[3].firstChild.nodeValue==null)&&(document.getElementById("tuesday").checked)){
					checkData=true;
					break;
				}//if
				if((bodyTable.rows[i].cells[4].firstChild.nodeValue==null)&&(document.getElementById("wednesday").checked)){
					checkData=true;
					break;
				}//if
		  		if((bodyTable.rows[i].cells[5].firstChild.nodeValue==null)&&(document.getElementById("thursday").checked)){
					checkData=true;
					break;
				}//if
				if((bodyTable.rows[i].cells[6].firstChild.nodeValue==null)&&(document.getElementById("friday").checked)){
					checkData=true;
					break;
				}//if
				if((bodyTable.rows[i].cells[7].firstChild.nodeValue==null)&&(document.getElementById("saturday").checked)){
					checkData=true;
					break;
				}//if
				if((bodyTable.rows[i].cells[8].firstChild.nodeValue==null)&&(document.getElementById("sunday").checked)){
					checkData=true;
					break;
				}//if
			}while(false); 
		}//if WEEKLY
		if(type==MONTHLY){	
			var tmptmpTime=bodyTable.rows[i].cells[1].firstChild.nodeValue.split("-");
			tmpTime[0]=tmptmpTime[1];
			tmpTime[1]=tmptmpTime[2];

			if(daySelect==tmptmpTime[0])
				checkData=true;
		}//if MONTHLY
		if(type==SPECIAL){
			var tmptmpTime=bodyTable.rows[i].cells[1].firstChild.nodeValue.split("-");
			tmpTime[0]=tmptmpTime[1];
			tmpTime[1]=tmptmpTime[2];

			var tmpSplitData=tmptmpTime[0].split("/");
			if((Number(daySelect)==Number(tmpSplitData[0]))&&(Number(document.getElementById("monthNumber").innerHTML)==Number(tmpSplitData[1]))&&(Number(annoSelezionato)==Number(tmpSplitData[2])))
				checkData=true;

		}//if SPECIAL
		if(type==YEARLY){
			var tmptmpTime=bodyTable.rows[i].cells[1].firstChild.nodeValue.split("-");
			tmpTime[0]=tmptmpTime[1];
			tmpTime[1]=tmptmpTime[2];

			var tmpSplitData=tmptmpTime[0].split("/");
			if((Number(daySelect)==Number(tmpSplitData[0]))&&(Number(document.getElementById("monthNumber").innerHTML)==Number(tmpSplitData[1])))
				checkData=true;

			
		}//if YEARLY
		
		if(checkData){
			var tmpTime1=tmpTime[0].split(":");
			var tmpTime2=tmpTime[1].split(":");
			var lowerBoundDataInMinuts=Number(tmpTime1[0])*60+Number(tmpTime1[1]);
			var upperBoundDataInMinuts=Number(tmpTime2[0])*60+Number(tmpTime2[1]);
			var a=Number(document.getElementById("hour_from").value)*60+Number(document.getElementById("minut_from").value);
			var b=Number(document.getElementById("hour_to").value)*60+Number(document.getElementById("minut_to").value);
			if(((a<=upperBoundDataInMinuts)&&(a>=lowerBoundDataInMinuts))||((b<=upperBoundDataInMinuts)&&(b>=lowerBoundDataInMinuts))||((a<=lowerBoundDataInMinuts)&&(b>=upperBoundDataInMinuts))){
				alert(document.getElementById("timeBandsOverLappedMsg").innerHTML);
				return ERROR_CODE;
			}//if
		checkData=false
		}//if checkData
	}//for
	
	return OK_CODE;
}//controlOverlappedDate 

/**
* Per ridimensionare la tabella Condizioni Evento
*/
function resizeTableEvnCond()
{
	var hdev = MTcalcObjectHeight("trEvnCondList");
	if(hdev != 0){
		MTresizeHtmlTable(1,hdev);
	}
}
