function MioLimitUser(e)
{
	var block = false;
	var onField = false;
	var elem = MioGetObjectSrc(e);
	
	if(elem != null) {
		if(elem.tagName != null && elem.tagName.toLowerCase() == "input" && 
		   (elem.type && elem.type == "text" || elem.type && elem.type == "password"))
			onField = true;
		else if(elem.tagName != null && elem.tagName.toLowerCase() == "textarea")
			onField = true;
	}
	
	if(e.keyCode >= 112 && e.keyCode <= 123)
		block = true;		
	
	if(!block)
	{
		switch(e.keyCode)
		{
			case 8:
				if(!onField)
					block = true;
				break;
		}
	}
	
	if(block)
		return MioBlockEvent(e);
	else
		return true;
}

function MioOnlyNumber(e,ana)
{
	var block = false;
	block = MioBlockSpecialChar(e);
	
	// La maschera accetta anche il carattere "-" -> 109,189
	if((e.type == "keydown") && (!e.ctrlKey) && !block)
	{
		if (((e.keyCode > 95) && (e.keyCode < 106)) || 
			((e.keyCode > 47) && (e.keyCode < 58))||
			(e.keyCode==109)||(e.keyCode==189))
		{
			if(e.shiftKey)
				block = false;
			else
				return true;			
		}
		else
		{
			switch (e.keyCode)
			{
				case 8:
				case 9:
				case 46:
				case 39:
				case 37:
					break;
				default:
					block = true;
			}
		}
	}
	
	if(ana != null && ana != undefined && ana && block)
	{
		switch(e.keyCode)
		{
			case 190:
			case 110:   //punto del tastierino numerico
			case 188:
				if(!e.shiftKey)
					block = false;
				break;
			default:
				block = true;	
		}
	}
	
	if(block)
		return MioBlockEvent(e);
	else
		return true;
}

function MioOnlyChar(e)
{
	var block = false;
	
	block = MioBlockSpecialChar(e);
	
	if((e.type == "keydown") && (!e.ctrlKey) && !block)
	{
		if (((e.keyCode < 95) || (e.keyCode > 106)) && ((e.keyCode < 47) || (e.keyCode > 58)))
		{
			return true;
		}
		else
		{
			switch (e.keyCode)
			{
				case 8:
				case 9:
				case 46:
				case 39:
				case 37:
					break;
				default:
					block = true;
			}
		}
	}
	
	if(block)
		return MioBlockEvent(e);
	else
		return true;
}

function MioOnlyLettersNumbers(e)
{
	var block = false;
	switch (e.keyCode)
	{
		case 8:
		case 9:
		case 46:
		case 39:
		case 37:
			break;
		default:
			block = false;
	}
	if((e.type == "keydown") && (!e.ctrlKey) && !block)
	{
		
		// Number
		if (((e.keyCode > 47) && (e.keyCode < 58)) || ((e.keyCode > 95) && (e.keyCode < 106)))
		{
			if(!e.shiftKey)
				return false;
		}
		
		if ((e.keyCode==189)&&e.shiftKey)
		{
			return false;
		}
		
				
		if ((e.keyCode==8) ||(e.keyCode==9) ||(e.keyCode==46) ||(e.keyCode==37) ||(e.keyCode==39) || ((e.keyCode < 91) && (e.keyCode > 64)))
		{
			return false;
		}
		else
		{
			block = true;
		}
	}
		
	if(block)
		return MioBlockEvent(e);
	else
		return true;
}

function MioSiteName(e)
{
	var block = false;
	switch (e.keyCode)
	{
		case 8:
		case 9:
		case 46:
		case 39:
		case 37:
		case 32:
			break;
		default:
			block = false;
	}
	if((e.type == "keydown") && (!e.ctrlKey) && !block)
	{
		
		// Number
		if (((e.keyCode > 47) && (e.keyCode < 58)) || ((e.keyCode > 95) && (e.keyCode < 106)) || e.keyCode == 190)
		{
			if(!e.shiftKey)
				return false;
		}
		
		if ((e.keyCode==189)&&e.shiftKey)
		{
			return false;
		}		
		if ((e.keyCode==8) ||(e.keyCode==9)||(e.keyCode==32)||(e.keyCode==46) ||(e.keyCode==37) ||(e.keyCode==39) || ((e.keyCode < 91) && (e.keyCode > 64)))
		{
			return false;
		}
		else
		{
			block = true;
		}
	}
		
	if(block)
		return MioBlockEvent(e);
	else
		return true;
}

function MioOnlyDigitVal(e,bval)
{
	var block = bval;
	switch(e.keyCode)
	{
		case 48:
		case 49:
		case 97:
		case 96:
			if(e.shiftKey)
				block = false;
			break;
		case 8:
		case 9:
		case 46:
		case 39:
		case 37:
			block = false;
			break;
		default:
			block = true;
	}
	
	if(block)
	{
		MioBlockEvent(e);
		if (e.keyCode != 13)
			alert('min = 0  |  max = 1');
		return false;
	}
	else
		return true;
}

function MioOnlyAnalNumber(e)
{
	return MioOnlyNumber(e,true);
}

function MioBlockTagChar(e)
{
	var block = true;
	/*
	 * 111	BackSlash - Abilitato
	 * 50	Tasto SHIFT - Abilitato 	
	 * 55	Tasto SHIFT - Abilitato
	*/
	switch(e.keyCode)
	{
		case 220: break;
		case 226: break;
		case 50: 
			if(e.shiftKey)	//doppi apici
				break;
		default:
			block = false;
	}
	if (block)
	{
		return MioBlockEvent(e);
	}
	else
		return true;
}

function MioBlockSpecialChar(e)
{
	var block = true;
	
	/*
	 * 111	BackSlash - Abilitato
	 * 50	Tasto SHIFT - Abilitato 	
	 * 55	Tasto SHIFT - Abilitato
	*/
	switch(e.keyCode)
	{
		case 219:
		case 220: break;
		case 50: 
			if(e.shiftKey)	//doppi apici
				break;
		//case 111:
		//	break;
		//case 55:
		//	if(e.shiftKey)	
		//		break;
		default:
			block = false;
	}
	return block;
}
function MioBlockBadChar(e)
{
	if(MioBlockSpecialChar(e))
		return MioBlockEvent(e);
	else
		return true;
}
function MioNoAtOnBlur(obj) // no @
{
	var value = obj.value;
	var idx = value.indexOf("@");
	if(idx == -1)
	{
		return true;
	}
	else
	{
		MioNotifyToUser(value.charAt(idx));
		obj.focus();
		return false;
	}
}
function MioGetObjectSrc(e)
{
	var elem = null;
	elem = (e.target)?e.target:e.srcElement;
	return elem;	
}

function MioBlockEvent(e)
{
	e.cancelBubble = true;
	e.returnValue = false;
	if(!document.all)
		e.preventDefault();
	
	return false;
}

function MioCheckModUser(e)
{
	var elem = MioGetObjectSrc(e);
	if(elem != null) 
	{
		if(elem.tagName != null)
		{
			if(elem.tagName.toLowerCase() == "input")
			{
				if(elem.type && ((elem.type == "text") || (elem.type == "radio") || 
				                 (elem.type == "checkbox") || (elem.type == "password")))
				{
					//2010-12-29, Kevin Ge, ListView Paging will not change Mio_userMod
					var input_id = elem.id;
          	  		if(input_id == null || input_id == "" || !(input_id.indexOf("ListViewPaging_")==0))
          	  			Mio_userMod = true;
				}
			}
			else if(elem.tagName.toLowerCase() == "textarea")
				Mio_userMod = true;
			else if(elem.tagName.toLowerCase() == "select")
			{
				if(elem.id!="comborack")
					Mio_userMod = true;
			}
			if((elem.type == "text") || (elem.tagName.toLowerCase() == "textarea"))
			{
				var haveRep = false;
				if(elem.value.indexOf("\"") != -1)
					haveRep = true;
				
				if(haveRep)
					elem.value = elem.value.replace(/\"/g,"'");
			}
		}
	}
	return true;
}

function unlockModUser()
{
	Mio_userMod = false;
}

function setModUser()
{
	Mio_userMod = true;
}

function getModUser()
{
	return Mio_userMod;
}

function MioCheckModUserMouse(e)
{
	var elem = MioGetObjectSrc(e);
	if(elem != null) 
	{
		if(elem.tagName != null)
		{
			
			if(elem.tagName.toLowerCase() == "input")
			{
				if(elem.type && ((elem.type == "text") || (elem.type == "radio") || 
				                 (elem.type == "checkbox") || (elem.type == "password") || (elem.type == "file")))
				{
					//2010-12-29, Kevin Ge, ListView Paging will not change Mio_userMod
					var input_id = elem.id;
          	  		if(input_id == null || input_id == "" || !(input_id.indexOf("ListViewPaging_")==0))
          	  			Mio_userMod = true;
				}
			}
			else if(elem.tagName.toLowerCase() == "textarea")
				Mio_userMod = true;
			else if(elem.tagName.toLowerCase() == "select")
			{
				if(elem.id!="comborack")
					Mio_userMod = true;
			}
		}
	}
	return true;
}

function MioAskModUser()
{
	var msg = document.getElementById("msg03").value;
	if(msg == "")
		msg = "Do you want close this tab?"
	if(Mio_userMod)
		return confirm(msg);
	return true;
}

// REGULAR EXPRESSION

function MioOnlyDigitOnBlur(obj)
{
	var val = obj.value;
	if(val == "")
		return;
		
	if(val != null && val.length == 1)
	{
		var exp = /[0-1]/;
		var ris = -1;
		ris = val.search(exp);
		if(ris != -1)
			return true;
	}

	MioNotifyToUser(val);
//	MioNotifyToUser(val.charAt(ris));
	obj.value = "";
	obj.focus();
	return false;
}

function MioOnlyValueOnBlur(obj)
{
	var val = obj.value;
	var rism = -1;
	if(val == "")
		return;
		
	var exp = /[^0-9]/;
	var ris = -1;
	ris = val.search(exp);
	if(ris != -1)
	{
		rism = ris;
		exp = /[.]/
		ris = val.search(exp);
		if(ris == -1)
		{
			exp = /[-]/
			ris = val.search(exp);
			if(ris == -1)
			{
				MioNotifyToUser(val.charAt(rism));
				obj.value = "";
				obj.focus();
				return false;
			}
		}
	}
	return true;
}

function MioOnlyNumberOnBlur(obj)
{
	var val = obj.value;
	if(val == "")
		return;
		
	var exp = /[^0-9]/;
	var ris = -1;
	ris = val.search(exp);
	if(ris != -1)
	{
		MioNotifyToUser(val.charAt(ris));
		obj.value = "";
		obj.focus();
		return false;
	}
	return true;
}

function MioOnlyCharOnBlur(obj)
{
	var val = obj.value;
	var bfound = false;
	var i=0;
	var ch = "";
	
	if(val == "")
		return;
		
	var exp = '';// eliminato il seguente filtro: [^a-z^A-Z^0-9^(^)^=^_^-]/g;
	var ris = -1;
	ris = val.match(exp);
	
	if(ris != null)
	{
		for(i; i<ris.length; i++)
		{
			if(ris[i].replace(/^\s+/g,'').length != 0)
			{
				ch = ris[i];
				bfound = true;
				break;
			}
		}
	}
	
	if(bfound)
	{
		MioNotifyToUser(ch);
		obj.value = "";
		obj.focus();
		return false;
	}
	return true;
}


function MioOnlyCharNumOnBlur(obj)
{
	var strFiltered = obj.value.replace(/[^a-zA-Z0-9]*/g,'');
	if( strFiltered != obj.value ) {
		obj.value = strFiltered;
	}
}


function MioNotifyToUser(chr) {
	//fix a bug at firefox: can not use alert in onblur, otherwise elements in page will be selected, report by Riccardo
	if(window != null && typeof(window.getSelection) != 'undefined' && window.getSelection())
		window.getSelection().removeAllRanges();
	alert(document.getElementById("s_notallowedchar").value + " [ "+chr+" ]");
}

//------------------------------------------------------------------------------
function isDate(yyyy,mm,dd)
{
	var daysInMonth = DaysArray(12);
	var strMonth=""+mm;
	var strDay=""+dd;
	var strYear=""+yyyy;
	var strYr=strYear;
	month=parseInt(strMonth);
	day=parseInt(strDay);
	year=parseInt(strYr);
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month])
	{
		return false;
	}
	if (strYear.length != 4 || year==0 || year<1900 || year>2100)
	{
		return false;
	}
	return true;
}

function daysInFebruary (year)
{
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}

function DaysArray(n) 
{
	for (var i = 1; i <= n; i++) 
	{
		this[i] = 31;
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30;}
		if (i==2) {this[i] = 29;}
   } 
   return this;
}


/*==========================================================================#
# * Function for adding a Filter to an Input Field                          #
# * @param  : [filterType  ] Type of filter 0=>Alpha, 1=>Num, 2=>AlphaNum   #
# * @param  : [evt         ] The Event Object                               #
# * @param  : [allowDecimal] To allow Decimal Point set this to true        #
# * @param  : [allowCustom ] Custom Characters that are to be allowed       #
#==========================================================================*/
function filterInput(filterType, evt, allowDecimal, allowCustom){
    var keyCode, Char, inputField, filter = '';
    var alpha = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    var num   = '0123456789';
    // Get the Key Code of the Key pressed if possible else - allow
    if(window.event){
        keyCode = window.event.keyCode;
        evt = window.event;
    }else if (evt)keyCode = evt.which;
    else return true;
    // Setup the allowed Character Set
    if(filterType == 0) filter = alpha;
    else if(filterType == 1) filter = num;
    else if(filterType == 2) filter = alpha + num;
    if(allowCustom)filter += allowCustom;
    if(filter == '')return true;
    // Get the Element that triggered the Event
    inputField = evt.srcElement ? evt.srcElement : evt.target || evt.currentTarget;
    // If the Key Pressed is a CTRL key like Esc, Enter etc - allow
    if((keyCode==null) || (keyCode==0) || (keyCode==8) || (keyCode==9) || (keyCode==13) || (keyCode==27) )return true;
    // Get the Pressed Character
    Char = String.fromCharCode(keyCode);
    // If the Character is a number - allow
    if((filter.indexOf(Char) > -1)) return true;
    // Else if Decimal Point is allowed and the Character is '.' - allow
    else if(filterType == 1 && allowDecimal && (Char == '.') && inputField.value.indexOf('.') == -1)return true;
    else return false;
}
