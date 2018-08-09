var pageName="";
var flashInfoCtrl= new Array();
var lock=false;
 
function initializeComponent()
{
  if (document.getElementById("dev_access").value=="no")
  {
	  alert("Permission denied");
  }
  if(window.navigator.userAgent.indexOf("Chrome") !== -1)
	  setTimeout(setInitVar, 100);
  else
	  setInitVar();
  setInterval(callServletData, 40000);
  callServletData();
}//initializeComponent
	
function setInitVar(){
	myDocumentElement=document;//top.frames['display'].document;
	  var i;
	  for(i=0;;i++)
	  {
	    var element=myDocumentElement.getElementById("flashset"+i);
	    if(element==null)
		  break;
	    flashInfoCtrl[i]=element;
	    var informationSet = new Array();
	    var tmp=flashInfoCtrl[i].innerHTML;
	    informationSet= tmp.split(";");
	    
	       
	    for(var j=0;j<informationSet.length;j++)
	    {
		    var obj=  myDocumentElement.getElementById("flashobj"+i);
		    //alert(informationSet[j]);
		    //alert(informationSet[j+1]);
		    if(obj!=null)
		    	obj.SetVariable("obj."+informationSet[j],informationSet[++j]);
	    }//for
	  }//for
}

function callServletData(){
     if(lock!=true){
    lock=true;
    var ofrm = document.getElementById("frm");
	if(ofrm != null) {
		MTstartServerComm();   //BIOLO: MTstopServerComm in MapData.java; introduced to have 1 post for variable set
		ofrm.submit();
	}
	//refresh of SDK object on maps
	PVPK_RefreshOnDemand();
  }//if
}//callServletData


//set of a variable value
function sendValue(id,value) {

    //alert(id+" "+value);
    //value = roundTo(value,1);
    //alert("rounded: " + value);
    
    if(document.getElementById("input"+id)!=null){
    	var datas=document.getElementById("input"+id).value.split(";");
    	document.getElementById("input"+id).value=datas[0]+";"+ value;
    }else{
    	if(document.getElementsByName("input"+id).length>0){
    		var datas=document.getElementsByName("input"+id)[0].value.split(";");
    		document.getElementsByName("input"+id)[0].value=datas[0]+";"+ value;
    	}
    }
    callServletData();
}

// round of a number
function roundTo(value, decimalpositions)
{
    var i = value * Math.pow(10,decimalpositions);
    i = Math.round(i);
    return i / Math.pow(10,decimalpositions);
} 

