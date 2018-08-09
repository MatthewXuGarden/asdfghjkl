function checkNoTagChar(obj,evnt)
{
	MioBlockTagChar(evnt);
}

function checkBadChar(obj,evnt)
{
	MioBlockBadChar(evnt);
}

//accetta lettere,numeri e "_"
function checkLettNum(obj,evnt)
{
	MioOnlyLettersNumbers(evnt);
}

function checkOnlyNumber(obj,evnt)
{
	MioOnlyNumber(evnt);
}

function checkOnlyChar(obj,evnt)
{
	MioOnlyChar(evnt);
}

function checkOnlyDigit(obj,evnt)
{
	var bVal = false;
	if(obj != null && obj.value.length >= 1)
		bVal = true;
		
	MioOnlyDigitVal(evnt,bVal);
}

function checkOnlyDigitOnBlur(obj)
{
	MioOnlyDigitOnBlur(obj);
}

function noBadCharOnBlur(obj,evnt)
{
	MioOnlyCharOnBlur(obj);
}

function onlyNumberOnBlur(obj)
{
	MioOnlyNumberOnBlur(obj);
}

function checkOnlyAnalog(obj,evnt)
{
	MioOnlyAnalNumber(evnt);
}

function checkOnlyAnalogOnBlur(obj)
{
	MioOnlyValueOnBlur(obj);
}

function checkOnlyMail(obj)
{
	var sVal = obj.value;
	if(sVal != "")
	{
		var block = false;
		var filter  = /^\w+([-\.]\w+)*@\w+([\.-]\w+)*\.\w{2,4}$/;
		if (!filter.test(sVal)){
			block = true;
		}	
//		var idx = sVal.indexOf("@");
//		var block = true;
//		if(idx != -1)
//		{
//			idx = sVal.indexOf(".",idx);
//			if(idx != -1)
//				block = false;
//		}
		
		if(block)
		{	
			alert(document.getElementById("writecorrectemail").value);
			//2010-1-6, add by Kevin
			//add onerror here because when focus, some control is disabled or invisible
			//will cause javascript error, add onerror to hidden the error
			//in finally, I reset the onerror method. so it is OK, later, when there are errors, we can find them again
			window.onerror = killErrors;
			try
			{
				obj.focus();
			}
			catch(err)
			{
			}
			finally
			{
				window.onerror = null;
			}
			return false;
		}
		
		
		
	}
	//2010-1-6, add by Kevin
	return true;
}
function killErrors() 
{ 
	return true; 
}
function strictCheckAtleast1char1number1speicial(str)
{
	var exp1 = /[a-zA-Z]/;
	var exp2 = /[0-9]/;
	var exp3 = /[.,_!?$%&]/;
	if(exp1.test(str) && exp2.test(str) && exp3.test(str))
	{
		return true;
	}
	return false;
}


