/* ********************************************************************
 *
 * HTML/Js Virtual Keyboard Interface script:
 *
 */
var g_keyboard = null;


function buildKeyboardInputs() {
	// DYN: g_keyboard keep the keyboard class instance
	// otherwise the previous instance it is lost and the visible object cannot be removed
	if( g_keyboard )
		if( typeof g_keyboard.VKI_close == 'function' )
			g_keyboard.VKI_close();
	g_keyboard = this;	

  var self = this;
  this.VKI_version = "";
  this.VKI_target = this.VKI_visible = "";
  this.VKI_shift = this.VKI_capslock = this.VKI_alternate = this.VKI_dead = false;
  this.VKI_deadkeysOn = false;
  
  //this.VKI_kt = "Numbers";  // Default keyboard layout
  if (document.getElementById("vkeytype"))
  {
  	this.VKI_kt = document.getElementById("vkeytype").value;
  }
  else
  {
  	this.VKI_kt = "Numbers";
  }
  
  this.VKI_range = false;
  this.VKI_keyCenter = 3;


  /* ***** Create keyboards **************************************** */
  this.VKI_layout = new Object();
  this.VKI_layoutDDK = new Object();


  /* ***** Keyboards layouts *************************************** */
  this.VKI_layout.Numbers = [ // only Numbers pad
    [["0"], ["1"], ["2"], ["3"], ["4"], ["5"], ["Bksp", "Bksp"]],
    [["6"], ["7"], ["8"], ["9"], ["."], ["-"], ["Enter", "Enter"]]
  ];
  
  this.VKI_layout.PVPro = [ // PVPro Standard Keyboard
    [["1", "1"], ["2", "2"], ["3", "3"], ["4", "4"], ["5", "5"], ["6", "6"], ["7", "7"], ["8", "8"], ["9", "9"], ["0", "0"], ["Bksp", "Bksp"]],
    [["", ""], ["q", "Q"], ["w", "W"], ["e", "E"], ["r", "R"], ["t", "T"], ["y", "Y"], ["u", "U"], ["i", "I"], ["o", "O"], ["p", "P"], ["-", "_"], [".", "."], ["", ""]],
    [["Caps", "Caps"], ["a", "A"], ["s", "S"], ["d", "D"], ["f", "F"], ["g", "G"], ["h", "H"], ["j", "J"], ["k", "K"], ["l", "L"], ["Enter", "Enter"]],
    [["Shift", "Shift"], ["z", "Z"], ["x", "X"], ["c", "C"], ["v", "V"], ["b", "B"], ["n", "N"], ["m", "M"], ["@", "@"], ["Shift", "Shift"]],
    [["", ""], [" ", " "], ["", ""]]
  ];

  /* ***** Define Dead Keys **************************************** */
  this.VKI_deadkey = new Object();


  /* ***** Find tagged input & textarea elements ******************* */
  var inputElems = [
    document.getElementsByTagName('input'),
    document.getElementsByTagName('textarea'),
  ];
  for (var y = 0, inputCount = 0; y < inputElems.length; y++) {
    if (inputElems[y]) {
      for (var x = 0; x < inputElems[y].length; x++) {
        if ((inputElems[y][x].nodeName == "TEXTAREA" || inputElems[y][x].type == "text" || inputElems[y][x].type == "password") && inputElems[y][x].className.indexOf("keyboardInput") > -1) {
          var keyid = "";
          if (!inputElems[y][x].id) {
            do { keyid = 'keyboardInputInitiator' + inputCount++; } while (document.getElementById(keyid));
          } else keyid = inputElems[y][x].id;

          var keybut = document.createElement('img');
      //        keybut.src = "images/keyboard.png";
              keybut.alt = "Virtual Keyboard";
              keybut.className = "keyboardInputInitiator";
              keybut.title = "Display Virtual Keyboard";
              
              //keybut.onclick = (function(a) { return function() { self.VKI_show(a) }; })(keyid);
              keybut.onclick = (function(a) { return function() {
            	  //2010-12-29, Kevin Ge, ListView Paging will not change Mio_userMod
            	  self.VKI_show(a);
            	  if(self.VKI_target == null || self.VKI_target.id == "" || !(self.VKI_target.id.indexOf("ListViewPaging_")==0))
            	  {
            		  Mio_userMod = true; 
            	  }
            	  }; })(keyid);


          inputElems[y][x].onmousedown = (function(a) { return function() {
        	  self.VKI_show(a);
        	  //2011-1-11 ,kevin, comment the below line, there is no need to set to true when mousedown
        	  //Mio_userMod = true; 
	          }; })(keyid);
          inputElems[y][x].className = "keyboardInputInitiator";
          
          inputElems[y][x].id = keyid;
          //inputElems[y][x].parentNode.insertBefore(keybut, inputElems[y][x].nextSibling);
          inputElems[y][x].onclick = inputElems[y][x].onkeyup = inputElems[y][x].onselect = function() {
            if (self.VKI_target.createTextRange) self.VKI_range = document.selection.createRange();
          }
        }
      }
    }
  }


  /* ***** Build the keyboard interface **************************** */
  this.VKI_keyboard = document.createElement('table');
  this.VKI_keyboard.id = "keyboardInputMaster";
  this.VKI_keyboard.cellSpacing = this.VKI_keyboard.cellPadding = this.VKI_keyboard.border = "0";

  var layouts = 0;
  for (ktype in this.VKI_layout)
   if (typeof this.VKI_layout[ktype] == "object") layouts++;

  var thead = document.createElement('thead');
  var tr = document.createElement('tr');
  var th = document.createElement('th');
       if (layouts > 1) {
          var kblist = document.createElement('select');
            for (ktype in this.VKI_layout) {
              if (typeof this.VKI_layout[ktype] == "object") {
                var opt = document.createElement('option');
                    opt.value = ktype;
                    opt.appendChild(document.createTextNode(((ktype=="PVPro")?"Extended":ktype)));
                  kblist.appendChild(opt);
              }
            }
              kblist.value = this.VKI_kt;
              kblist.onchange = function() {
                self.VKI_kt = this.value;
                self.VKI_buildKeys();
                self.VKI_position();
              }
          th.appendChild(kblist);
        }

          var label = document.createElement('label');
            var checkbox = document.createElement('input');
                checkbox.type = "checkbox";
                checkbox.checked = this.VKI_deadkeysOn;
                checkbox.title = "Toggle dead key input";
                checkbox.onclick = function() {
                  self.VKI_deadkeysOn = this.checked;
                  this.nextSibling.nodeValue = " Dead keys: " + ((this.checked) ? "On" : "Off");
                  self.VKI_modify("");
                  return true;
                }
              label.appendChild(checkbox);
              label.appendChild(document.createTextNode(" Dead keys: " + ((checkbox.checked) ? "On" : "Off")));
          th.appendChild(label);
        tr.appendChild(th);

      var td = document.createElement('td');
        var clearer = document.createElement('button');
            clearer.id = "keyboardInputClear";
            clearer.appendChild(document.createTextNode("Clear"));
            clearer.title = "Clear this input";
            clearer.onmousedown = function() { this.className = "pressed"; }
            clearer.onmouseup = function() { this.className = ""; }
            clearer.onclick = function() {
              self.VKI_target.value = "";
              self.VKI_target.focus();
              return false;
            }
          clearer.style.width = '70px';
          clearer.style.height = '40px';
          td.appendChild(clearer);

        var closer = document.createElement('button');
            closer.id = "keyboardInputClose";
            closer.appendChild(document.createTextNode('X'));
            closer.title = "Close this window";
            closer.onmousedown = function() { this.className = "pressed"; }
            closer.onmouseup = function() { this.className = ""; }
            closer.onclick = function(e) { self.VKI_close(); }
            closer.style.width = '30px';
            closer.style.height = '40px';
          td.appendChild(closer);

        tr.appendChild(td);
      thead.appendChild(tr);
  this.VKI_keyboard.appendChild(thead);

  var tbody = document.createElement('tbody');
    var tr = document.createElement('tr');
      var td = document.createElement('td');
          td.colSpan = "2";
        var div = document.createElement('div');
            div.id = "keyboardInputLayout";
          td.appendChild(div);
        var div = document.createElement('div');
          var ver = document.createElement('var');
            div.appendChild(ver);
          td.appendChild(div);
        tr.appendChild(td);
      tbody.appendChild(tr);
  this.VKI_keyboard.appendChild(tbody);      



  /* ***** Functions ************************************************ */
  /* ******************************************************************
   * Build or rebuild the keyboard keys
   *
   */
  this.VKI_buildKeys = function() {
    this.VKI_shift = this.VKI_capslock = this.VKI_alternate = this.VKI_dead = false;
    this.VKI_deadkeysOn = (this.VKI_layoutDDK[this.VKI_kt]) ? false : this.VKI_keyboard.getElementsByTagName('label')[0].getElementsByTagName('input')[0].checked;

    var container = this.VKI_keyboard.tBodies[0].getElementsByTagName('div')[0];
    while (container.firstChild) container.removeChild(container.firstChild);

    for (var x = 0, hasDeadKey = false; x < this.VKI_layout[this.VKI_kt].length; x++) {
      var table = document.createElement('table');
          table.cellSpacing = table.cellPadding = table.border = "0";
      if (this.VKI_layout[this.VKI_kt][x].length <= this.VKI_keyCenter) table.className = "keyboardInputCenter";
        var tbody = document.createElement('tbody');
          var tr = document.createElement('tr');
          for (var y = 0; y < this.VKI_layout[this.VKI_kt][x].length; y++) {
            if (!this.VKI_layoutDDK[this.VKI_kt] && !hasDeadKey)
              for (var z = 0; z < this.VKI_layout[this.VKI_kt][x][y].length; z++)
                if (this.VKI_deadkey[this.VKI_layout[this.VKI_kt][x][y][z]]) hasDeadKey = true;

            var td = document.createElement('td');
                td.appendChild(document.createTextNode(this.VKI_layout[this.VKI_kt][x][y][0]));

              var alive = false;
              if (this.VKI_deadkeysOn) for (key in this.VKI_deadkey) if (key === this.VKI_layout[this.VKI_kt][x][y][0]) alive = true;
                td.className = (alive) ? "alive" : "";
              if (this.VKI_layout[this.VKI_kt][x].length > this.VKI_keyCenter && y == this.VKI_layout[this.VKI_kt][x].length - 1)
                td.className += " last";

              if (this.VKI_layout[this.VKI_kt][x][y][0] == " ")
                td.style.paddingLeft = td.style.paddingRight = "50px";
                td.onmouseover = function(e) { if (this.className != "dead" && this.firstChild.nodeValue != "\xa0") this.className += " hover"; }
                td.onmouseout = function(e) { if (this.className != "dead") this.className = this.className.replace(/ ?(hover|pressed)/g, ""); }
                td.onmousedown = function(e) { if (this.className != "dead" && this.firstChild.nodeValue != "\xa0") this.className += " pressed"; }
                td.onmouseup = function(e) { if (this.className != "dead") this.className = this.className.replace(/ ?pressed/g, ""); }
                td.ondblclick = function() { return false; }

              switch (this.VKI_layout[this.VKI_kt][x][y][1]) {
                case "Caps":
                case "Shift":
                case "Alt":
                case "AltGr":
                  td.onclick = (function(type) { return function() { self.VKI_modify(type); return false; }})(this.VKI_layout[this.VKI_kt][x][y][1]);
                  break;
                case "Tab":
                  td.onclick = function() { self.VKI_insert("\t"); return false; }
                  break;
                case "Bksp":
                  td.onclick = function() {
                    self.VKI_target.focus();
                    if (self.VKI_target.setSelectionRange) {
                      var srt = self.VKI_target.selectionStart;
                      var len = self.VKI_target.selectionEnd;
                      if (srt < len) srt++;
                      self.VKI_target.value = self.VKI_target.value.substr(0, srt - 1) + self.VKI_target.value.substr(len);
                      self.VKI_target.setSelectionRange(srt - 1, srt - 1);
                    } else if (self.VKI_target.createTextRange) {
                      try { self.VKI_range.select(); } catch(e) {}
                      self.VKI_range = document.selection.createRange();
                      if (!self.VKI_range.text.length) self.VKI_range.moveStart('character', -1);
                      self.VKI_range.text = "";
                    } else self.VKI_target.value = self.VKI_target.value.substr(0, self.VKI_target.value.length - 1);
                    if (self.VKI_shift) self.VKI_modify("Shift");
                    if (self.VKI_alternate) self.VKI_modify("AltGr");
                    //2011-1-18, Kevin Ge
                    actb_keydown();
                    return true;
                  }
                  break;
                case "Enter":
                  td.onclick = function() {
                    //if (self.VKI_target.nodeName == "TEXTAREA") { self.VKI_insert("\n"); } else self.VKI_close();
                	  //2010-12-12, Kevin Ge, special codes for paging.
                	  //after user click "enter" in paging input field, there is a special work to do
                	  if(self.VKI_target != null)
                	  {
	                	  var input_id = self.VKI_target.id;
	                	  if(input_id != null && input_id.indexOf("ListViewPaging_")==0)
	                	  {
	                		  if(input_id.split("_").length>1)
	                		  {
		                		  var idTable = input_id.split("_")[1];
		                		  if(typeof(pageSetInput_enter) == "function")
		                		  {
		                			  pageSetInput_enter(self.VKI_target,idTable);
		                		  }
	                		  }
	                	  }
                	  }
                	  //--
                    self.VKI_close();
                    return true;
                  }
                  break;
                default:
                  td.onclick = function() {
                    if (self.VKI_deadkeysOn && self.VKI_dead) {
                      if (self.VKI_dead != this.firstChild.nodeValue) {
                        for (key in self.VKI_deadkey) {
                          if (key == self.VKI_dead) {
                            if (this.firstChild.nodeValue != " ") {
                              for (var z = 0, rezzed = false; z < self.VKI_deadkey[key].length; z++) {
                                if (self.VKI_deadkey[key][z][0] == this.firstChild.nodeValue) {
                                  self.VKI_insert(self.VKI_deadkey[key][z][1]);
                                  rezzed = true;
                                  break;
                                }
                              }
                            } else {
                              self.VKI_insert(self.VKI_dead);
                              rezzed = true;
                            }
                            break;
                          }
                        }
                      } else rezzed = true;
                    }
                    self.VKI_dead = false;

                    if (!rezzed && this.firstChild.nodeValue != "\xa0") {
                      if (self.VKI_deadkeysOn) {
                        for (key in self.VKI_deadkey) {
                          if (key == this.firstChild.nodeValue) {
                            self.VKI_dead = key;
                            this.className = "dead";
                            if (self.VKI_shift) self.VKI_modify("Shift");
                            if (self.VKI_alternate) self.VKI_modify("AltGr");
                            break;
                          }
                        }
                        if (!self.VKI_dead) self.VKI_insert(this.firstChild.nodeValue);
                      } else self.VKI_insert(this.firstChild.nodeValue);
                    }

                    self.VKI_modify("");
                    return false;
                  }

                  for (var z = this.VKI_layout[this.VKI_kt][x][y].length; z < 4; z++)
                    this.VKI_layout[this.VKI_kt][x][y][z] = "\xa0";
              }
              tr.appendChild(td);
            tbody.appendChild(tr);
          table.appendChild(tbody);
      }
      container.appendChild(table);
    }
    this.VKI_keyboard.getElementsByTagName('label')[0].style.display = (hasDeadKey) ? "inline" : "none";
  }

  this.VKI_buildKeys();
  if (window.sidebar || window.opera) {
    this.VKI_keyboard.onmousedown = function() { return false; }
    this.VKI_keyboard.onclick = function() { return true; }
  } else this.VKI_keyboard.onselectstart = function() { return false; }


  /* ******************************************************************
   * Controls modifier keys
   *
   */
  this.VKI_modify = function(type) {
    switch (type) {
      case "Alt":
      case "AltGr": this.VKI_alternate = !this.VKI_alternate; break;
      case "Caps": this.VKI_capslock = !this.VKI_capslock; break;
      case "Shift": this.VKI_shift = !this.VKI_shift; break;
    }
    var vchar = 0;
    if (!this.VKI_shift != !this.VKI_capslock) vchar += 1;

    var tables = this.VKI_keyboard.getElementsByTagName('table');
    for (var x = 0; x < tables.length; x++) {
      var tds = tables[x].getElementsByTagName('td');
      for (var y = 0; y < tds.length; y++) {
        var dead = alive = target = false;

        switch (this.VKI_layout[this.VKI_kt][x][y][1]) {
          case "Alt":
          case "AltGr":
            if (this.VKI_alternate) dead = true;
            break;
          case "Shift":
            if (this.VKI_shift) dead = true;
            break;
          case "Caps":
            if (this.VKI_capslock) dead = true;
            break;
          case "Tab": case "Enter": case "Bksp": break;
          default:
            if (type) tds[y].firstChild.nodeValue = this.VKI_layout[this.VKI_kt][x][y][vchar + ((this.VKI_alternate && this.VKI_layout[this.VKI_kt][x][y].length == 4) ? 2 : 0)];
            if (this.VKI_deadkeysOn) {
              var char = tds[y].firstChild.nodeValue;
              if (this.VKI_dead) {
                if (char == this.VKI_dead) dead = true;
                for (var z = 0; z < this.VKI_deadkey[this.VKI_dead].length; z++)
                  if (char == this.VKI_deadkey[this.VKI_dead][z][0]) { target = true; break; }
              }
              for (key in this.VKI_deadkey) if (key === char) { alive = true; break; }
            }
        }

        tds[y].className = (dead) ? "dead" : ((target) ? "target" : ((alive) ? "alive" : ""));
        if (y == tds.length - 1 && tds.length > this.VKI_keyCenter) tds[y].className += " last";
      }
    }
    this.VKI_target.focus();
  }


  /* ******************************************************************
   * Insert text at the cursor
   *
   */
  this.VKI_insert = function(text) {
    this.VKI_target.focus();
    if (this.VKI_target.setSelectionRange) {
      var srt = this.VKI_target.selectionStart;
      var len = this.VKI_target.selectionEnd;
      this.VKI_target.value = this.VKI_target.value.substr(0, srt) + text + this.VKI_target.value.substr(len);
      if (text == "\n" && window.opera) srt++;
      this.VKI_target.setSelectionRange(srt + text.length, srt + text.length);
    } else if (this.VKI_target.createTextRange) {
      try { this.VKI_range.select(); } catch(e) {}
      this.VKI_range = document.selection.createRange();
      this.VKI_range.text = text;
      this.VKI_range.collapse(true);
      this.VKI_range.select();
    } else this.VKI_target.value += text;
    if (this.VKI_shift) this.VKI_modify("Shift");
    if (this.VKI_alternate) this.VKI_modify("AltGr");
    //2011-1-18,Kevin Ge
    actb_keydown();
    this.VKI_target.focus();
  }


  /* ******************************************************************
   * Show the keyboard interface
   *
   */
  this.VKI_show = function(id) {
    if ( this.VKI_target = document.getElementById(id) ) {
    	// DYN: prevent virtual keyboard to popup on readonly fields
    	if( this.VKI_target.getAttribute("readonly") )
    		return;
      if (this.VKI_visible != id) {
        this.VKI_range = "";
        try { this.VKI_keyboard.parentNode.removeChild(this.VKI_keyboard); } catch (e) {}

        var elem = this.VKI_target;
        this.VKI_target.keyboardPosition = "absolute";
        //this.VKI_target.keyboardPosition = "fixed";
        do {
          if (VKI_getStyle(elem, "position") == "fixed") {
            this.VKI_target.keyboardPosition = "fixed";
            break;
          }
        } while (elem = elem.offsetParent);

        this.VKI_keyboard.style.top = this.VKI_keyboard.style.right = this.VKI_keyboard.style.bottom = this.VKI_keyboard.style.left = "auto";
        this.VKI_keyboard.style.position = this.VKI_target.keyboardPosition;
        document.body.appendChild(this.VKI_keyboard);

        this.VKI_visible = this.VKI_target.id;
        this.VKI_position();
        this.VKI_target.focus();
      } else this.VKI_close();
    }
  }


  /* ******************************************************************
   * Position the keyboard
   *
   */
  this.VKI_position = function() {
    if (self.VKI_visible != "") {
      var inputElemPos = VKI_findPos(self.VKI_target);
      
      //var inputElemPos = [0,0];
      //self.VKI_keyboard.style.top = inputElemPos[1] - ((self.VKI_target.keyboardPosition == "fixed") ? document.body.scrollTop : 0) + self.VKI_target.offsetHeight + 3 + "px";
      var temp=inputElemPos[0];
      self.VKI_keyboard.style.left=((VKI_innerDimensions()[0]>2*temp)?Math.min((temp+self.VKI_target.offsetWidth),VKI_innerDimensions()[0]-self.VKI_keyboard.offsetWidth):Math.max((temp-self.VKI_keyboard.offsetWidth),0)) + "px";
      temp=Math.min(inputElemPos[1],getEvent().clientY);
      self.VKI_keyboard.style.top =((VKI_innerDimensions()[1]>2*temp)?Math.min((temp+self.VKI_target.offsetHeight),VKI_innerDimensions()[1]-self.VKI_keyboard.offsetHeight):Math.max((temp-self.VKI_keyboard.offsetHeight),0)) + "px";
      //alert("inputElemPos[1]:\t"+inputElemPos[1]+"\n\nself.VKI_target.offsetHeight:\t"+self.VKI_target.offsetHeight+"\n\nself.VKI_keyboard.style.top:\t"+self.VKI_keyboard.style.top);
    }
  }

  if (window.addEventListener) {
    window.addEventListener('resize', this.VKI_position, false); 
  } else if (window.attachEvent)
    window.attachEvent('onresize', this.VKI_position);


  /* ******************************************************************
   * Close the keyboard interface
   *
   */
  this.VKI_close = function() {
    try { this.VKI_keyboard.parentNode.removeChild(this.VKI_keyboard); } catch (e) {}
    this.VKI_visible = "";
    if( typeof this.VKI_target.focus == 'function' )
    	this.VKI_target.focus();
    this.VKI_target = "";
  }
  
  //2011-1-18,Kevin Ge, for Site configure, device combobox, on top of the combobox
  //there is a text input, the following function's purpose: after click VK, call the refresh for that part
  function actb_keydown()
  {
	  if(typeof(actbDeviceSearch) != "undefined" && actbDeviceSearch != null)
	  {
		  if(typeof (this.VKI_target) != "undefined" && typeof(this.VKI_target.id) != "undefined"
			  && this.VKI_target.id == "device_search")
		  {
			  actbDeviceSearch.refreshForVirtualKeyboard();
		  }
	  }
  }
}


/* ***** Attach this script to the onload event ******************** */
if (window.addEventListener) {
  window.addEventListener('load', buildKeyboardInputs, false); 
} else if (window.attachEvent)
  window.attachEvent('onload', buildKeyboardInputs);


/* ********************************************************************
 * Handy element positioning function
 *
 */
function VKI_findPos(obj) {
  var curleft = curtop = 0;
  var curtop2 = 0;
  do {
    curleft += obj.offsetLeft;
    //curtop += obj.offsetTop; //valevole se non entro un div container
    curtop2 += (obj.offsetTop - obj.scrollTop + obj.clientTop);
  } while (obj = obj.offsetParent);

  var odvicont = document.getElementById("container");
  if(odvicont != null)
  {
    curtop = curtop - odvicont.scrollTop;
  }
  
  return [curleft, curtop2];
}


/* ********************************************************************
 * Return the dimensions of the viewport
 *
 */
function VKI_innerDimensions() {
  if (self.innerHeight) {
    return [self.innerWidth, self.innerHeight];
  } else if (document.documentElement && document.documentElement.clientHeight) {
    return [document.documentElement.clientWidth, document.documentElement.clientHeight];
  } else if (document.body)
    return [document.body.clientWidth, document.body.clientHeight];
  return [0, 0];
}


/* ********************************************************************
 * Return an element's set style
 *
 */
function VKI_getStyle(obj, styleProp) {
  if (obj.currentStyle) {
    var y = obj.currentStyle[styleProp];
  } else if (window.getComputedStyle)
    var y = window.getComputedStyle(obj, null)[styleProp];
  return y;
}
function getEvent(){
   if(document.all) return window.event; 
   func=getEvent.caller; 
   while(func!=null){ 
     var arg0=func.arguments[0]; 
     if(arg0){ 
       if((arg0.constructor==Event || arg0.constructor ==MouseEvent) || (typeof(arg0)=="object" && arg0.preventDefault && arg0.stopPropagation)){ 
         return arg0; 
       } 
     } 
     func=func.caller; 
   } 
   return null; 
}