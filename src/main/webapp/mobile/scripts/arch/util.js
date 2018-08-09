
// Alessandro: check if the argument is a valid number
function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}

function stringtrimmer(stringa){
	stringa =  stringa.replace(/(^\s*)|(\s*$)/g, "");
    return stringa;
}


function isIP(address) {
	var components1 = address.split(".");
	if(components1.length!=4){
		return false;
	} else {
		for(var i1 = 0; i1<4; i1++){
			if(components1[i1]!=parseInt(components1[i1])){
				return false;
			}
			if(components1[i1]<0 || components1[i1]>255){
				return false;
			}
		}
		if(components1[0]<1 || components1[0]>254) return false;
		if(components1[3]<1 || components1[3]>254) return false;
	}
	return true;
}


function showLayer(szDivId, bShow)
{
	var obj = document.layers ? document.layers[szDivId]
		: document.getElementById ? document.getElementById(szDivId).style
			: document.all[szDivId].style;
	if( obj )
		obj.visibility = document.layers ? (bShow ? "show" : "hide")
			: (bShow ? "visible" : "hidden");
	return obj;
}


function addEvent(obj, type, fn)
{
	if(obj.attachEvent) {
		obj['e'+type+fn] = fn;
		obj[type+fn] = function() {
			obj['e'+type+fn](window.event);
		};
		obj.attachEvent('on' + type, obj[type+fn]);
	}
	else {
		obj.addEventListener(type, fn, false);
	}
}


function removeEvent(obj, type, fn)
{
	if ( obj.detachEvent ) {
		obj.detachEvent( 'on'+type, obj[type+fn] );
		obj[type+fn] = null;
	}
	else {
		obj.removeEventListener( type, fn, false );
	}
}


var nBalloonInterval = null;


function showBalloon(caption, msg1, msg2, timeout)
{
	if( top.frames["body"] && top.frames["body"].frames["bodytab"] )
		top.frames["body"].frames["bodytab"].__showBalloon(caption, msg1, msg2, timeout);
}


function hideBalloon()
{
	if( nBalloonInterval ) {
		clearInterval(nBalloonInterval);
		nBalloonInterval = null;
	}
	showLayer("divBalloon", false);
}


function __showBalloon(caption, msg1, msg2, timeout)
{
	var body = top.frames["body"].frames["bodytab"];

	var table = document.getElementById("tableBalloon");
	if( table ) {
		table.rows[0].cells[0].innerHTML = caption ? caption : "&nbsp;";
		table.rows[1].cells[0].innerHTML = msg1 ? msg1 : "&nbsp;";
		table.rows[1].cells[0].innerHTML += msg2 ? msg2 : "&nbsp;";
	}
	
	var div = document.getElementById("divBalloon");
	if( div ) {
		// CSS1Compat is the standard-compliant mode for IE
		var yPos = (document.compatMode == "CSS1Compat"
			? document.documentElement.clientHeight - 120 // height defined in class
			: body.document.body.clientHeight - div.clientHeight)
			- 5;
		div.style.left = 50 + "px";
		div.style.top = yPos + "px";
		div.style.width = 320 + "px";
		div.style.height = 120 + "px";
	}
	
	if( nBalloonInterval ) {
		clearInterval(nBalloonInterval);
		nBalloonInterval = null;
	}
	showLayer("divBalloon", true);
	if( timeout )
		nBalloonInterval = setInterval("hideBalloon()", timeout * 1000);
}


function restartEngine()
{
	hideBalloon();
	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "GET", "&cmd=RESTART_ENGINE", Callback_restartEngine, true);
	return true;
}


function Callback_restartEngine(xml)
{
	MTstopServerComm();
}

Array.prototype.indexOf = function(obj){
    for(var i=0; i<this.length; i++){
        if(this[i]==obj){
            return i;
        }
    }
    return -1;
}

Date.prototype.format = function(format)
{ 
	var o = 
	{ 
		"M+" : this.getMonth()+1, //month 
		"d+" : this.getDate(), //day 
		"h+" : this.getHours(), //hour 
		"m+" : this.getMinutes(), //minute 
		"s+" : this.getSeconds(), //second 
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
		"S" : this.getMilliseconds() //millisecond 
	};

	if(/(y+)/.test(format)) 
	{ 
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 

	for(var k in o) 
	{ 
		if(new RegExp("("+ k +")").test(format)) 
		{ 
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		} 
	} 
	return format; 
};


function strEndsWith(str, sufix)
{
	if( str.length >= sufix.length )
		return str.substr(str.length - sufix.length, sufix.length).toLowerCase() == sufix.toLowerCase();
	return false;
}


// prev/next buttons show/hide
function updatePrevNext(bPrev, bNext)
{
	if( bPrev )
		top.frames['menu'].showPrev();
	else
		top.frames['menu'].hidePrev();
	if( bNext )
		top.frames['menu'].showNext();
	else
		top.frames['menu'].hideNext();
}


var viewportWidth;
var viewportHeight;
function computeViewport()
{
	// the more standards compliant browsers (mozilla/netscape/opera/IE7) use window.innerWidth and window.innerHeight
	if (typeof window.innerWidth != 'undefined')
	{
	     viewportWidth = window.innerWidth;
	     viewportHeight = window.innerHeight;
	}
	//IE6 in standards compliant mode (i.e. with a valid doctype as the first line in the document)
	else if (typeof document.documentElement != 'undefined'
	    && typeof document.documentElement.clientWidth !=
	    'undefined' && document.documentElement.clientWidth != 0)
	{
	      viewportWidth = document.documentElement.clientWidth;
	      viewportHeight = document.documentElement.clientHeight;
	}
	// older versions of IE
	else
	{
	      viewportWidth = document.getElementsByTagName('body')[0].clientWidth;
	      viewportHeight = document.getElementsByTagName('body')[0].clientHeight;
	}
}

var verticalPadding = 25;
var horizontalPadding = 20;
function adjustContainer(vpadTunning, hpadTunning)
{
	computeViewport();
	var tableTop = document.getElementById("tableTop");
	var padding = tableTop ? tableTop.offsetHeight : verticalPadding;
	if( vpadTunning )
		padding = vpadTunning;
	var div = document.getElementById("divContainerTrx");
	if( div ) {
		div.style.height = (viewportHeight - padding) + "px";
		//div.style.width = (viewportWidth - horizontalPadding) + "px";
	}
}
