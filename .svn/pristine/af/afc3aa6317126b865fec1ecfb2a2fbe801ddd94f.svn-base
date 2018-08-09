var now = new Date();
var current_day = now.getDate();
var current_month = now.getMonth();
var current_year = now.getFullYear();



function changeMonthYear(idobj)
{
	//selected x il mese
	var month_to_set = document.getElementById("month_to_set").value;
	
	if (month_to_set=="")
	{
		document.getElementById("mm"+idobj).selectedIndex=current_month+1;
	}
	else
	{
		document.getElementById("mm"+idobj).selectedIndex=(Number(month_to_set));	
	}
			
	//selected x l'anno
	var year_to_set = document.getElementById("year_to_set").value;
	if (year_to_set=="") year_to_set = current_year;
	
	for (i=0;i<document.getElementById("aaaa"+idobj).length;i++)
	{
		if (document.getElementById("aaaa"+idobj).options[i].value==year_to_set)
		{
			document.getElementById("aaaa"+idobj).selectedIndex=i;		
		}
	}
	
	var n = document.getElementById("mm"+idobj).value;
	var selectedYear = document.getElementById("aaaa"+idobj).value;
	
	if((n==1)||(n==3)||(n==5)||(n==7)||(n==8)||(n==10)||(n==12))
	{
		VerifyDayCombo(31,idobj);
	}
	else if((n==4)||(n==6)||(n==9)||(n==11))
	{
		VerifyDayCombo(30,idobj);
	}
	else 
	{
		if((selectedYear%4==0)&&((selectedYear%100!=0)||(selectedYear%400==0)))
		{	
			VerifyDayCombo(29,idobj);
		}
		else
		{
			VerifyDayCombo(28,idobj);
		}
	}
	
	
}

function VerifyDayCombo(num_of_days,idobj)
{
	
	var combolength = document.getElementById("gg"+idobj).length;
	var day_to_set = document.getElementById("day_to_set").value;
	
	if (num_of_days<combolength)
	{
		for (i=num_of_days;i<32;i++)
		{
			document.getElementById("gg"+idobj).remove(num_of_days+1);
		}
	}
	else if (num_of_days>combolength-1)
	{
		for (i=combolength;i<num_of_days+1;i++)
		{
			document.getElementById("gg"+idobj).options.add(new Option(i,i));
		}
	}
	if (day_to_set=="") day_to_set = current_day;
	document.getElementById("gg"+idobj).selectedIndex=day_to_set;
	
}

function disable_date(idobj)
{
	document.getElementById("gg"+idobj).selectedIndex=0;
	document.getElementById("gg"+idobj).disabled=true;
	document.getElementById("mm"+idobj).selectedIndex=0;
	document.getElementById("mm"+idobj).disabled=true;
	document.getElementById("aaaa"+idobj).selectedIndex=0;
	document.getElementById("aaaa"+idobj).disabled=true;
	document.getElementById("day_to_set").value="";
	document.getElementById("month_to_set").value="";
	document.getElementById("year_to_set").value="";
}

function enable_date(idobj)
{
	var cmd = document.getElementById("cmd").value;
	var day = "";
	var month = "";
	var year = "";
	if (cmd=="reload")
	{
		day = document.getElementById("day_to_set").value;
		month = document.getElementById("month_to_set").value;
		year = document.getElementById("year_to_set").value;
	}
	else
	{
		day = current_day;
		month = current_month;
		year = current_year;
	}
	document.getElementById("gg"+idobj).disabled=false;
	document.getElementById("gg"+idobj).selecteIndex= day;
	document.getElementById("mm"+idobj).disabled=false;
	document.getElementById("mm"+idobj).selecteIndex= month;
	document.getElementById("aaaa"+idobj).disabled=false;
	document.getElementById("aaaa"+idobj).selecteIndex= year; 
}

function save_day(idobj)
{
	document.getElementById("day_to_set").value=document.getElementById("gg"+idobj).value;
}
function save_month(idobj)
{
	document.getElementById("month_to_set").value=document.getElementById("mm"+idobj).value;
}
function save_year(idobj)
{
	document.getElementById("year_to_set").value=document.getElementById("aaaa"+idobj).value;
}


//FUNZIONI PER SECONDA DATA     (FROM - TO)
function changeMonthYear_b(idobj)
{
	//selected x il mese
	var month_to_set = document.getElementById("month_to_set_b").value;
	
	if (month_to_set=="")
	{
		document.getElementById("mm"+idobj).selectedIndex=current_month+1;
	}
	else
	{
		document.getElementById("mm"+idobj).selectedIndex=(Number(month_to_set));	
	}
			
	//selected x l'anno
	var year_to_set = document.getElementById("year_to_set_b").value;
	if (year_to_set=="") year_to_set = current_year;
	
	for (i=0;i<document.getElementById("aaaa"+idobj).length;i++)
	{
		if (document.getElementById("aaaa"+idobj).options[i].value==year_to_set)
		{
			document.getElementById("aaaa"+idobj).selectedIndex=i;		
		}
	}
	
	var n = document.getElementById("mm"+idobj).value;
	var selectedYear = document.getElementById("aaaa"+idobj).value;
	
	if((n==1)||(n==3)||(n==5)||(n==7)||(n==8)||(n==10)||(n==12))
	{
		VerifyDayCombo_b(31,idobj);
	}
	else if((n==4)||(n==6)||(n==9)||(n==11))
	{
		VerifyDayCombo_b(30,idobj);
	}
	else 
	{
		if((selectedYear%4==0)&&((selectedYear%100!=0)||(selectedYear%400==0)))
		{	
			VerifyDayCombo_b(29,idobj);
		}
		else
		{
			VerifyDayCombo_b(28,idobj);
		}
	}
	
	
}

function VerifyDayCombo_b(num_of_days,idobj)
{
	
	var combolength = document.getElementById("gg"+idobj).length;
	var day_to_set = document.getElementById("day_to_set_b").value;
	
	if (num_of_days<combolength)
	{
		for (i=num_of_days;i<32;i++)
		{
			document.getElementById("gg"+idobj).remove(num_of_days+1);
		}
	}
	else if (num_of_days>combolength-1)
	{
		for (i=combolength;i<num_of_days+1;i++)
		{
			document.getElementById("gg"+idobj).options.add(new Option(i,i));
		}
	}
	if (day_to_set=="") day_to_set = current_day;
	document.getElementById("gg"+idobj).selectedIndex=day_to_set;
	
}

function save_day_b(idobj)
{
	document.getElementById("day_to_set_b").value=document.getElementById("gg"+idobj).value;
}
function save_month_b(idobj)
{
	document.getElementById("month_to_set_b").value=document.getElementById("mm"+idobj).value;
}
function save_year_b(idobj)
{
	document.getElementById("year_to_set_b").value=document.getElementById("aaaa"+idobj).value;
}

function enable_date_b(idobj)
{
	var cmd = document.getElementById("cmd").value;
	var day = "";
	var month = "";
	var year = "";
	if (cmd=="reload")
	{
		day = document.getElementById("day_to_set_b").value;
		month = document.getElementById("month_to_set_b").value;
		year = document.getElementById("year_to_set_b").value;
	}
	else
	{
		day = current_day;
		month = current_month;
		year = current_year;
	}
	document.getElementById("gg"+idobj).disabled=false;
	document.getElementById("gg"+idobj).selecteIndex= day;
	document.getElementById("mm"+idobj).disabled=false;
	document.getElementById("mm"+idobj).selecteIndex= month;
	document.getElementById("aaaa"+idobj).disabled=false;
	document.getElementById("aaaa"+idobj).selecteIndex= year; 
}
