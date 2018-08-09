var ERROR_CODE=0;
var OK_CODE=1;
var _graph_type='';

rowSelected=-1;
rowSelectedHistorical=-1;
changed=false;

function initialize(destArgs){
	
	var destArgs = window.dialogArguments;
	_graph_type = destArgs[0];
    
	if(_graph_type=='haccp'){ // haccp
		setConfigurationPageGraph(document.getElementById("onLoadCosmeticGraphInfoHaccp").innerHTML);
	}
	else { // historical
		setConfigurationPageGraph(document.getElementById("onLoadCosmeticGraphInfoHistorical").innerHTML);
	}//else
  
    //checkGraphType();
    
  try {
  	alignColor();
  }catch(e){}
   
  changed=false;
}//initialize

function initializeGraphConfig(destArgs){
	_graph_type = destArgs[0];
    
	if(_graph_type=='haccp'){ // haccp
		setConfigurationPageGraph(document.getElementById("onLoadCosmeticGraphInfoHaccp").innerHTML);
	}
	else { // historical
		setConfigurationPageGraph(document.getElementById("onLoadCosmeticGraphInfoHistorical").innerHTML);
	}//else
  
    //checkGraphType();
    
  try {
  	alignColor();
  }catch(e){}
   
  changed=false;
	
}


function addColor(obj)
{
	changed=true;
	var idCombo = obj.id;
	var rowSelected = idCombo.substring(8,idCombo.length);
	var bodyTable=document.getElementById("variableList").getElementsByTagName("tbody")[0];
	var bodyInvisibleTable=document.getElementById("invisibleVariableList").getElementsByTagName("tbody")[0];
	var colorSelect=obj.style.backgroundColor.substring(1,7);
	//bodyTable.rows[rowSelected].cells[3].innerHTML="<div style=\"width:40;heigth:10px;background-color: " +colorSelect+"\">&nbsp;</div>";
	bodyInvisibleTable.rows[rowSelected].cells[4].innerHTML=colorSelect;
	document.body.focus();
}//addColor


function alignColor()
{
	var objSel = null;
	var numSel = document.getElementById("colorVarNum").value;
	numSel = Number(numSel);
	if(numSel != 0)
	{
		for(i=0; i<numSel; i++)
		{
			objSel = document.getElementById("colorVar"+i);
			if(objSel != null)
				addColor(objSel);	
		}
	}
}

// style color could be in the format #rrggbb (hex values) or rgb(r,g,b) (decimal values)
// we expect rrggbb inside graph.js
function getColor(str) {
	if( !str.length ) {
		return "";
	}
	else if( str.substr(0, 1) == "#" ) {
		return str.substring(1,7); 
	}
	else {
		str = str.replace(/rgb\(|\)/g, "").split(",");
		str[0] = parseInt(str[0], 10).toString(16).toLowerCase();
		str[1] = parseInt(str[1], 10).toString(16).toLowerCase();
		str[2] = parseInt(str[2], 10).toString(16).toLowerCase();
		str[0] = (str[0].length == 1) ? '0' + str[0] : str[0];
		str[1] = (str[1].length == 1) ? '0' + str[1] : str[1];
		str[2] = (str[2].length == 1) ? '0' + str[2] : str[2];
		return str.join("");
	}
}


function buildReturnParams(){

	//Alessandro: adesso non � pi� presente la select ma un color picker diverso
	var colorViewFinderBg = getColor(document.getElementById("colorViewFinderBg").style.backgroundColor);
	var colorViewFinderFg = getColor(document.getElementById("colorViewFinderFg").style.backgroundColor);
	var colorGrid = getColor(document.getElementById("colorGrid").style.backgroundColor);
	var colorGraphBg = getColor(document.getElementById("colorGraphBg").style.backgroundColor);
	var colorAxis = getColor(document.getElementById("colorAxis").style.backgroundColor);
		
//	document.getElementById("cosmeticGraphInformation").value=
	var returnValue=
	document.getElementById("viewFinderCheckedBox").checked+";"+  
	document.getElementById("xgridCheckedBox").checked+";"+   
	document.getElementById("ygridCheckedBox").checked+";"+ 
	colorViewFinderBg+";"+
	colorViewFinderFg+";"+
	colorGrid+";"+
	colorGraphBg+";"+
	colorAxis;
	
	closeWindow();
	openGraphLayout(returnValue);
	

//	return returnValue;
}//buildReturnParams

function changeFocus()
{
	document.body.focus();
}


function setConfigurationPageGraph(info){
	var datas=info.split(";");
	document.getElementById("viewFinderCheckedBox").checked=datas[0]=="true"?true:false;
	document.getElementById("xgridCheckedBox").checked=datas[1]=="true"?true:false;
	document.getElementById("ygridCheckedBox").checked=datas[2]=="true"?true:false;
	
	document.getElementById("colorViewFinderBg").style.backgroundColor = datas[3];
	document.getElementById("colorViewFinderFg").style.backgroundColor = datas[4];
	document.getElementById("colorGrid").style.backgroundColor = datas[5];
	document.getElementById("colorGraphBg").style.backgroundColor = datas[6];
	document.getElementById("colorAxis").style.backgroundColor = datas[7];
	
	checkBoxType("viewFinderCheckedBox");
	checkBoxType("xgridCheckedBox");
	

}//setConfigurationPageGraph


function checkBoxType(typeBox){
	changed=true;
	for(;;){
		if(typeBox=="viewFinderCheckedBox"){
			document.getElementById("colorViewFinderBg").disabled=!document.getElementById(typeBox).checked;
			document.getElementById("colorViewFinderFg").disabled=!document.getElementById(typeBox).checked;
			break;
		}//	if
		if((typeBox=="xgridCheckedBox")||(typeBox=="ygridCheckedBox")){
			document.getElementById("colorGrid").disabled=!(document.getElementById("xgridCheckedBox").checked||document.getElementById("ygridCheckedBox").checked)
			break;
		}//	if
	break;
	}//forswitch
}//checkBoxType

