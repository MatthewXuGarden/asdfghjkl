var last_sel = -1;
var row_class = -1;

function setAlrFreqOnLoad()
{
	var freq = document.getElementById("freqval").value;
	document.getElementById("frequency").value = freq;
}

function dev_alr_save()
{
	hide_set(-2);
	if (document.getElementById("frequency").value==0)
	{
		alert(document.getElementById("nozerofreq").value);
	}
	else
	{
		document.getElementById("cmd").value="dev_alr_save";
		var ofrm = document.getElementById("frm_dev_alr_save");
		if (ofrm!=null)
			MTstartServerComm();
		ofrm.submit();
	}
}

function set_prior(idvar)
{
	alert("sdfs");
	if (document.getElementById("alr"+idvar).checked)
	{
		document.getElementById("prior"+idvar).disabled=false;
	}
	else
	{
		document.getElementById("prior"+idvar).disabled=true;
	}
}

function select_row(row)
{
	var n_row = row;
	var table = document.getElementById("TableData1");
	if (row_class>=0)
	{
		table.rows[row_class].className = row_class % 2 == 0 ? "Row1" : "Row2";
	}
	row_class = n_row;
	if (row_class>=0)
	{
		table.rows[row_class].className="selectedRow";
	}
}

function mod_line(line)
{
	if( line == last_sel )
		return;
	var alr_id = document.getElementById("alr_id"+line).value;
	var tab_line = line;
	//al click disabilito una eventuale seconda selezione
	hide_set(last_sel);
	var table = document.getElementById("TableData1");
			
	//enab
	var enab = document.getElementById("alr_"+alr_id).value;
	table.rows[tab_line].cells[0].innerHTML="<input type='checkbox' class='standardTxt' id='set_enab' "+(enab=="true"?"checked":"")+" />";
	
	//prior
	var prior = document.getElementById("prior"+alr_id).value;
	table.rows[tab_line].cells[2].innerHTML="<select id='set_prior' class='standardTxt'>"+getComboPrior(prior)+"</select>";
	
	last_sel = line;
}


function hide_set(line)
{
	var tab_line = last_sel;
	if (line==last_sel) return;
	if (last_sel!=-1&&document.getElementById("set_enab")!=null)
	{
		var table = document.getElementById("TableData1");
		var alr_id = document.getElementById("alr_id"+last_sel).value;
		
		//enbled
		var enab = document.getElementById("set_enab").checked;
		if (enab==true)
		{
			document.getElementById("alr_"+alr_id).value="true";
			table.rows[tab_line].cells[0].innerHTML="X";
		}
		else
		{
			document.getElementById("alr_"+alr_id).value="false";
			table.rows[tab_line].cells[0].innerHTML="";
		}
		//prior
		var prior = document.getElementById("set_prior").value;
		document.getElementById("prior"+alr_id).value=prior;
		if (enab==true)  //se non abilito allarme allora non scrivo la priorit?
		{
			table.rows[tab_line].cells[2].innerHTML=document.getElementById("set_prior").options[document.getElementById("set_prior").selectedIndex].innerHTML;
		}
		else
		{
			table.rows[tab_line].cells[2].innerHTML="";
		}
	}
}

function getComboPrior(prior)
{
	var veryhigh = document.getElementById('veryhigh').value;
	var high = document.getElementById('high').value;
	var mid = document.getElementById('mid').value;
	var low = document.getElementById('low').value;
	
	var opt = "<option value='1' "+(prior==1?"selected":"")+">"+veryhigh+"</option>";
	opt+=    "<option value='2' "+(prior==2?"selected":"")+">"+high+"</option>";
	opt+=    "<option value='3' "+(prior==3?"selected":"")+">"+mid+"</option>";
	opt+=    "<option value='4' "+(prior>=4?"selected":"")+">"+low+"</option>";
	return opt;
}
