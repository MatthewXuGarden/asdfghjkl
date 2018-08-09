function ESgetDateFrom()
{
	return document.getElementById("tester").value;
}

function ESgetDateTo()
{
	return document.getElementById("tester2").value;
}

function ESgetOptionDate()
{
	var dataselect = null;
	var period = document.ESdata.period
	for(var i=0; i<period.length; i++)
	{
		if (period[i].checked)
		{
			return period[i].id;
		}	
	}
}

function ESsetStateCal()
{
	var isSelected = document.getElementById("fromto").checked==true;
	if (isSelected==true)
	{
		enableCal();
		var date_from = document.getElementById("reload_from").value;
		var date_to = document.getElementById("reload_to").value;
		setDate(date_from,date_to);
	}
	else disableCal();
}

function ESEvSearch()
{
	if (verifyDate())
	{
		var user = document.getElementById("userevent").value;
		var category =  document.getElementById("EScategory").value;
		var desc = document.getElementById("ESdescriptionPage").value;
		
		MTstartServerComm();
		top.frames['manager'].loadTrx('nop&folder=searchev&bo=BSearchEv&type=click&desc='
									+desc+'&remote=no&dataselect='+ESgetOptionDate()+'&userevent='+user+'&datefrom='
									+ESgetDateFrom()+'&dateto='+ESgetDateTo()+'&EScategory='+category);
	}
}