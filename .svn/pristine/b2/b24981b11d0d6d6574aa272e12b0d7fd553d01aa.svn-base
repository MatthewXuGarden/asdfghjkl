// credit: http://www.codeproject.com/KB/scripting/jsactb.aspx

LEFT_CORRECTION = -19;
WIDTH_CORRECTION = 19;


/* Event Functions */

// Add an event to the obj given
// event_name refers to the event trigger, without the "on", like click or mouseover
// func_name refers to the function callback when event is triggered
function addEvent(obj,event_name,func_name){
	if (obj.attachEvent){
		obj.attachEvent("on"+event_name, func_name);
	}else if(obj.addEventListener){
		obj.addEventListener(event_name,func_name,true);
	}else{
		obj["on"+event_name] = func_name;
	}
}

// Removes an event from the object
function removeEvent(obj,event_name,func_name){
	if (obj.detachEvent){
		obj.detachEvent("on"+event_name,func_name);
	}else if(obj.removeEventListener){
		obj.removeEventListener(event_name,func_name,true);
	}else{
		obj["on"+event_name] = null;
	}
}

// Stop an event from bubbling up the event DOM
function stopEvent(evt){
	evt || window.event;
	if (evt.stopPropagation){
		evt.stopPropagation();
		evt.preventDefault();
	}else if(typeof evt.cancelBubble != "undefined"){
		evt.cancelBubble = true;
		evt.returnValue = false;
	}
	return false;
}

// Get the obj that starts the event
function getElement(evt){
	if (window.event){
		return window.event.srcElement;
	}else{
		return evt.currentTarget;
	}
}
// Get the obj that triggers off the event
function getTargetElement(evt){
	if (window.event){
		return window.event.srcElement;
	}else{
		return evt.target;
	}
}
// For IE only, stops the obj from being selected
function stopSelect(obj){
	if (typeof obj.onselectstart != 'undefined'){
		addEvent(obj,"selectstart",function(){ return false;});
	}
}

/*    Caret Functions     */

// Get the end position of the caret in the object. Note that the obj needs to be in focus first
function getCaretEnd(obj){
	if(typeof obj.selectionEnd != "undefined"){
		return obj.selectionEnd;
	}else if(document.selection&&document.selection.createRange){
		var M=document.selection.createRange();
		try{
			var Lp = M.duplicate();
			Lp.moveToElementText(obj);
		}catch(e){
			var Lp=obj.createTextRange();
		}
		Lp.setEndPoint("EndToEnd",M);
		var rb=Lp.text.length;
		if(rb>obj.value.length){
			return -1;
		}
		return rb;
	}
}
// Get the start position of the caret in the object
function getCaretStart(obj){
	if(typeof obj.selectionStart != "undefined"){
		return obj.selectionStart;
	}else if(document.selection&&document.selection.createRange){
		var M=document.selection.createRange();
		try{
			var Lp = M.duplicate();
			Lp.moveToElementText(obj);
		}catch(e){
			var Lp=obj.createTextRange();
		}
		Lp.setEndPoint("EndToStart",M);
		var rb=Lp.text.length;
		if(rb>obj.value.length){
			return -1;
		}
		return rb;
	}
}
// sets the caret position to l in the object
function setCaret(obj,l){
	obj.focus();
	if (obj.setSelectionRange){
		obj.setSelectionRange(l,l);
	}else if(obj.createTextRange){
		m = obj.createTextRange();		
		m.moveStart('character',l);
		m.collapse();
		m.select();
	}
}
// sets the caret selection from s to e in the object
function setSelection(obj,s,e){
	obj.focus();
	if (obj.setSelectionRange){
		obj.setSelectionRange(s,e);
	}else if(obj.createTextRange){
		m = obj.createTextRange();		
		m.moveStart('character',s);
		m.moveEnd('character',e);
		m.select();
	}
}

/*    Escape function   */
String.prototype.addslashes = function(){
	return this.replace(/(["\\\.\|\[\]\^\*\+\?\$\(\)])/g, '\\$1');
}
String.prototype.trim = function () {
    return this.replace(/^\s*(\S*(\s+\S+)*)\s*$/, "$1");
};
/* --- Escape --- */

/* Offset position from top of the screen */
function curTop(obj){
	var toreturn = 0;
	while(obj){
		toreturn += obj.offsetTop;
		obj = obj.offsetParent;
	}
	return toreturn;
}
function curLeft(obj){
	var toreturn = LEFT_CORRECTION;
	while(obj){
		toreturn += obj.offsetLeft;
		obj = obj.offsetParent;
	}
	return toreturn;
}
function curWidth(obj){
	var toreturn = WIDTH_CORRECTION;
	toreturn += parseInt(obj.style.width, 10);
	return toreturn;
}
/* ------ End of Offset function ------- */

/* Types Function */

// is a given input a number?
function isNumber(a) {
    return typeof a == 'number' && isFinite(a);
}

/* Object Functions */

function replaceHTML(obj,text){
	while(el = obj.childNodes[0]){
		obj.removeChild(el);
	};
	obj.appendChild(document.createTextNode(text));
}


//get element position by Kevin
//differ to curTop upper: for curTop, there is a bug, the position is always there even if you click the scroll bar
function getElementPos(elementId) 
{
	var ua = navigator.userAgent.toLowerCase();
	var isOpera = (ua.indexOf('opera') != -1);
	var isIE = (ua.indexOf('msie') != -1 && !isOpera); // not opera spoof
	var el = document.getElementById(elementId);
	if(el.parentNode === null || el.style.display == 'none') 
	{
		return false;
	}      
	var parent = null;
	var pos = [];     
	var box;     
	if(el.getBoundingClientRect)    //IE
	{         
		box = el.getBoundingClientRect();
		var scrollTop = Math.max(document.documentElement.scrollTop, document.body.scrollTop);
		var scrollLeft = Math.max(document.documentElement.scrollLeft, document.body.scrollLeft);
		return {x:box.left + scrollLeft, y:box.top + scrollTop};
	}
	else if(document.getBoxObjectFor)    // gecko    	
	{
		box = document.getBoxObjectFor(el); 
		var borderLeft = (el.style.borderLeftWidth)?parseInt(el.style.borderLeftWidth):0; 
		var borderTop = (el.style.borderTopWidth)?parseInt(el.style.borderTopWidth):0; 
		pos = [box.x - borderLeft, box.y - borderTop];
	}
	else    // safari & opera    
	{
		pos = [el.offsetLeft, el.offsetTop];  
		parent = el.offsetParent;     
		if (parent != el) 
		{ 
			while (parent) 
			{  
				pos[0] += parent.offsetLeft; 
				pos[1] += parent.offsetTop; 
				parent = parent.offsetParent;
			}  
		}   
		if (ua.indexOf('opera') != -1 || ( ua.indexOf('safari') != -1 && el.style.position == 'absolute' )) 
		{ 
			pos[0] -= document.body.offsetLeft;
			pos[1] -= document.body.offsetTop;         
		}    
	}              
	if (el.parentNode)
	{ 
		parent = el.parentNode;
	} else {
		parent = null;
	}
	while (parent && parent.tagName != 'BODY' && parent.tagName != 'HTML') { // account for any scrolled ancestors
		pos[0] -= parent.scrollLeft;
		pos[1] -= parent.scrollTop;
		if (parent.parentNode) {
			parent = parent.parentNode;
		} else {
			parent = null;
		}
	}
	return {x:pos[0], y:pos[1]};
}