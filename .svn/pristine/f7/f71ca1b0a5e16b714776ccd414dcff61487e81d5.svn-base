//window.onload=enableTooltips; //tooltip - REMOVED
var m_selected = null; //general
var curObj = null;

//general function
function change(currLoc)
{
	if(m_selected != null)
		m_selected.className = "Tab_Area";
	else
	{
		if(currLoc.id != "tab_0")
			document.getElementById("tab_0").className = "Tab_Area";
	}
		
	if(currLoc != null)
	{
		m_selected = currLoc;
		m_selected.className = "Tab_AreaSelected";
	}	
	
}
			
function evid(currLoc, name)
{
	if(currLoc != null && currLoc.className != "Tab_AreaSelected")
		currLoc.className=name;
	
	if((currLoc != null) && (currLoc.id != "undefined"))
	{
		if((m_selected == null) && (currLoc.id == "tab_0") && (name != "Tab_AreaOver"))
		{
			m_selected = currLoc;
			m_selected.className = "Tab_AreaSelected";		
		}
			
		if((m_selected != null) && (name != "Tab_AreaOver"))
			m_selected.className = "Tab_AreaSelected";
	}
		
}
	
function loadTrxTab(trxName, currLoc)
{
	if (currLoc.className != "Tab_AreaSelected")
		top.frames['manager'].Redirect("servlet/master","?trx="+trxName,top.frames['body'].frames['bodytab']);
}

// tooltip - DEPRECATED
function enableTooltips()
{
	var links=document.getElementsByTagName("a");
	for(i=0;i<links.length;i++){
    	t=links[i].getAttribute("title");
    	if(t!=""){
	        links[i].removeAttribute("title");
	        links[i].style.position="relative";
	        tooltip=document.createElement("div");
	        tooltip.className="tooltip";
	        tooltip.style.display="none";
	        tooltip.appendChild(document.createTextNode(t));
	        links[i].appendChild(tooltip);
	        links[i].onmouseover=showTooltip;
	        links[i].onmouseout=hideTooltip;
        }
    }
}

function showTooltip(event){
    this.style.zIndex="25";
    this.getElementsByTagName("div")[0].style.display="block";
    }

function hideTooltip(event){
    this.style.zIndex="24";
    this.getElementsByTagName("div")[0].style.display="none";
    }
    
//image rollover

function swapRestore() 
{ 
  var i,x,a=document.sr; 
  for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) 
  	x.src=x.oSrc;
}


function findObj(n, d) 
{ 
  var p,i,x;
  
  if(!d) d=document;
  
  if((p=n.indexOf("?"))>0&&parent.frames.length)
  {
  	d=parent.frames[n.substring(p+1)].document;
   	n=n.substring(0,p);
  }
  if(!(x=d[n])&&d.all)
  		x=d.all[n];
  for (i=0;!x&&i<d.forms.length;i++) 
  		x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) 
  		x=findObj(n,d.layers[i].document);
  if(!x && document.getElementById) 
  		x=document.getElementById(n);
  		
  return x;
}

function swapImage() { 
  var i,j=0,x,a=swapImage.arguments;
  document.sr=new Array;
  for(i=0;i<(a.length-2);i+=3)
  		if ((x=findObj(a[i]))!=null)
  		{
  			document.sr[j++]=x;
  			if(!x.oSrc) x.oSrc=x.src;
  			x.src=a[i+2];
  		}
}

// New TabMenu functions

function MT_onMouseOver(obj)
{
	if((curObj != null) && (curObj.id == obj.id))
		return;
	
	obj.style.cursor="pointer";
	obj.style.backgroundImage="url(images/tab/tab_onmouseover.png)";
	obj.style.color="white";
	return;
}

function MT_onMouseOut(obj)
{
	if((curObj != null) && (curObj.id == obj.id))
		return;
	
	obj.style.cursor="default";
	obj.style.backgroundImage="url(images/tab/taboff.png)";
	obj.style.color="#AAAAAA";
	return;
}

function MT_MouseClick(where,param,obj)
{
	if(top.frames['body'] != null)
	{
		if(top.frames['body'].frames['bodytab'] != null)
		{
			try {
				if(!top.frames['body'].frames['bodytab'].MioAskModUser())
					return false;
			}
			catch(e){}
		}
	}
	
	if(curObj != null)
	{
		if(curObj.id != obj.id)
		{
			var swap = curObj;
			curObj = obj;
			MT_onMouseOut(swap);
		}
		else
			return;
	}
	curObj = obj;
	curObj.style.cursor="default";
	curObj.style.backgroundImage="url(images/tab/tabon.png)";
	curObj.style.color="black";
	
	if(where != null && param != "")
	{
		top.frames['manager'].Redirect("servlet/master"+where,"?trx="+param,top.frames['body'].frames['bodytab']);
	}
}

function MT_MouseClickDummy(idtab)
{
	var idx = 0;
	try {
		idx = Number(idtab);
	}
	catch(e){}

	
	if(idx > 0)	
		idx--;
	
	var tabid = "tab_"+idx;
	var objTabVoice = document.getElementById(tabid);
	if(objTabVoice != null)
		MT_MouseClick("","",objTabVoice);
}

function MT_MouseClickByIdx(idx)
{
	var tabId = "tab_"+idx;
	var objTabVoice = document.getElementById(tabId);	
	if(objTabVoice != null)
		objTabVoice.onclick();
}

function MT_BodyClickTab(idx)
{
	if(idx != "-1")
	{
		top.frames["body"].frames["TabMenu"].MT_MouseClickByIdx(idx);
	}
		
}
// END 

// Funzioni per swap immagini su bottoni

function myMouseOver(obj,over)
{
	var cNode = obj.childNodes;
	
	for(var i=0; i<cNode.length; i++)
	{
		if(cNode[i].tagName == "IMG")
			cNode[i].src=over;
	}
}

function myMouseOut(obj,out)
{
	var cNode = obj.childNodes;
	
	for(var i=0; i<cNode.length; i++)
	{
		if(cNode[i].tagName == "IMG")
			cNode[i].src=out;
	}
}

function myMouseClick(obj,click)
{
	var cNode = obj.childNodes;
	
	for(var i=0; i<cNode.length; i++)
	{
		if(cNode[i].tagName == "IMG")
			cNode[i].src=click;
	}
}

// Server Comm
function MTstartServerComm() {
	top.frames["manager"].setServerCom();
	MTprvMsgUser(true);
}
function MTstopServerComm() {
	top.frames["manager"].resetServerCom();
	MTprvMsgUser(false);
}
function MTisServerComm() {
	return top.frames["manager"].isServerCom();
}

function MTprvMsgUser(bol)
{
	var dmsg = document.getElementById("messageDiv");
	var imsg = document.getElementById("messageDivI");
	var windHeigth,windWidth;	
	if (document.all) 
		{
		windWidth = document.body.clientWidth;
		windHeigth = document.body.clientHeight;
		}
	else
		{
		windWidth = window.innerWidth;
		windHeigth = window.innerHeight;
		}	
	if(dmsg != null)
	{
		
		dmsg.style.width=(bol)?"210px":"10px";
		dmsg.style.height=(bol)?"80px":"10px";
		dmsg.style.top =(bol)?((windHeigth/2)-(80/2))+"px":"0px";
		dmsg.style.left =(bol)?((windWidth/2)-(210/2))+"px":"0px"; 
		dmsg.style.visibility= (bol)?"visible":"hidden";
		dmsg.style.display = (bol)?"block":"none";
		
		
		if(imsg != null) {
			
			imsg.style.width=(bol)?"210px":"10px";
			imsg.style.height=(bol)?"80px":"10px";
			imsg.style.top = (bol)?((windHeigth/2)-(80/2))+"px":"0px";
			imsg.style.left = (bol)?((windWidth/2)-(210/2))+"px":"0px"; 	
			imsg.style.visibility= (bol)?"visible":"hidden";
			imsg.style.display = (bol)?"block":"none";
		}
	}
	
	try {
		top.frames['manager'].MgrLoadTrxGraph(false);
	}
	catch(e){}
}

function MTresizeDivContainer(hasPadding)
{
	//var IE = navigator.appName.indexOf("Microsoft") != -1;
	var size = getViewport();
	var width = size[0];
	var height = size[1];
	var container = document.getElementById("container");
	if( container ) {
		if(hasPadding)
		{
			if( document.compatMode == "CSS1Compat" ) {
				container.className = "";
				container.style.overflowY = "auto";
				container.style.paddingLeft = "20px";
				container.style.width = (width - 24) + "px";
				container.style.height = (height - 50) + "px"; // altezza stimata bottom menu
			}
			else {
				//container.style.width = (IE ? width : width - 24) + "px"; // the padding-left of 20 px caused on oversizing of the page in FF
				container.style.width = (width - 24) + "px"; //by Kevin, IE10 is like FF
				container.style.height = (height - 50) + "px"; // altezza stimata bottom menu
			}
		}
		else
		{
			container.style.width = width + "px";
			container.style.height = (height - 50) + "px";
		}
	}
/*
	var h = MTcalcObjectHeight("trContainer");
	if(h != 0)
		document.getElementById("container").style.height = h - 50; // altezza stimata bottom menu
	//alert("Altezza attuale div container: "+document.getElementById("container").style.height);
	var w = MTcalcObjectWidth("trContainer");
	if(w != 0)
		document.getElementById("container").style.width = IE ? w : w - 24; // the padding-left of 20 px caused on oversizing of the page in FF
	//alert("Larghezza attuale div container: "+document.getElementById("container").style.width);
*/	
}

/*
 * Calcolo altezza pixel tr contenente oggetto	
 */
function MTcalcObjectHeight(sId)
{
	var h = 0;
	try
	{
		var obj = document.getElementById(sId);
		h = obj.offsetHeight;
	}
	catch(e) {
	}
	return h;
}

/*
 * Calcolo larghezza pixel tr contenente oggetto	
 */
function MTcalcObjectWidth(sId)
{
	var w = 0;
	try
	{
		var obj = document.getElementById(sId);
		w = obj.offsetWidth;
	}
	catch(e) {
	}
	return w;
}

/*
 * Ridimensionamento HTMLTable sul client
 */
function MTresizeHtmlTable(id,h)
{
	try
	{
		var hdev = h;
		document.getElementById("LWTitle"+id).style.height = hdev;
		document.getElementById("LWlist"+id).style.height = hdev;
		document.getElementById("LWborder"+id).style.height = hdev;
		document.getElementById("LWContainer"+id).style.height = hdev-2;
		document.getElementById("LWCtDataName"+id).style.height = hdev-26;
	}
	catch(e){}
}

//Controllo se il broadcast � attivo, e poi eseguo la funzione passata come parametro,coi relativi parametri
function stop_broad()
{
	var args = arguments;
	var funct = args[0];
	var params = "";
	
	if (args.length>1)
	{
		for (i=1;i<args.length;i++)
		{
			params = params +"'"+ args[i] + "',";
		}
	}
	params = params.substring(0,params.length-1);
	CommSend("servlet/ajrefresh","GET","cmd=broad&funct="+funct+"&params="+params,"stop_broad");
}

function Callback_stop_broad()
{
	var response = xmlResponse.getElementsByTagName("broad")[0]; 
	var broad = response.childNodes[0].nodeValue;
	var funct = response.childNodes[1].nodeValue;
	var params = response.childNodes[2].nodeValue;
	var funct_to_call ="";
	
	funct_to_call = funct + "(" + params + ")";
		
	if (broad=="on")
	{
		if (confirm(top.frames['header'].document.getElementById("broad_confirm").value))
		{
			eval(funct_to_call);
		}
	}
	else
	{
		eval(funct_to_call);
	}
}


function getViewport()
{
	var viewportwidth;
	var viewportheight;
	
	// the more standards compliant browsers (mozilla/netscape/opera/IE7) use window.innerWidth and window.innerHeight
	if (typeof window.innerWidth != 'undefined')
	{
	     viewportwidth = window.innerWidth,
	     viewportheight = window.innerHeight
	}
	
	//IE6 in standards compliant mode (i.e. with a valid doctype as the first line in the document)
	else if (typeof document.documentElement != 'undefined'
	    && typeof document.documentElement.clientWidth !=
	    'undefined' && document.documentElement.clientWidth != 0)
	{
	      viewportwidth = document.documentElement.clientWidth,
	      viewportheight = document.documentElement.clientHeight
	}
	
	// older versions of IE
	else
	{
	      viewportwidth = document.getElementsByTagName('body')[0].clientWidth,
	      viewportheight = document.getElementsByTagName('body')[0].clientHeight
	}
	
	return [viewportwidth, viewportheight];
}
