// TAB 4
function loadVar(obj)
{
	var changeDev = document.getElementById("changeDev").value;
	
	if ((changeDev == "false") || (confirm(document.getElementById("wantChangeDev").value)))
	{
		var iddevmdl = obj.value;
		
		if (iddevmdl == -1)
		{
			clearTables();
			document.getElementById("cusenab").disabled = false;
		}
		else
		{
			clearTables();
			document.getElementById("cusenab").disabled = true;
			CommSend("servlet/ajrefresh","POST","cmd=loadvars&iddevmdl="+iddevmdl+"","loadVars",true);
		}
		
		document.getElementById("changeDev").value = "false";
		disableAction(1);
	}
}

function onload_tab4()
{
	cmbobj = document.getElementById("dev_combo");
	
	if ((cmbobj) && (cmbobj.value != -1))
		loadVar(cmbobj);
}

function modDevice()
{
	document.getElementById("changeDev").value = "true";
}

var xml_vars = "";
var xml_mdl  = "";
var xml_mdl2 = "";
var xml_mdl3 = "";

var hide_orig = "";
var selez_orig = "";
var modif_orig = "";

var img_addorig_on = "<img src='images/actions/addsmall_on_black.png' onclick='"; //mod_origline("; //1);'/>";
var img_adddest_on = "<img src='images/actions/addsmall_on_black.png' onclick='"; //mod_destline("; //1);'/>";
var img_addext_on = "<img src='images/actions/addsmall_on_black.png' onclick='"; //mod_extline("; //1);'/>";
var img_add_off = "<img src='images/actions/addsmall_off.png'/>";

var img_remorig_on = "<img src='images/actions/removesmall_on_black.png' onclick='rem_origline("; //1)'/>";
var img_remdest_on = "<img src='images/actions/removesmall_on_black.png' onclick='rem_destline("; //1)'/>";
var img_remext_on = "<img src='images/actions/removesmall_on_black.png' onclick='rem_extline("; //1)'/>";
var img_rem_off = "<img src='images/actions/removesmall_off.png'/>";

function Callback_loadVars(xmlResponse)
{
	//variabili del device x combo
	xml_vars = xmlResponse.getElementsByTagName("var");
	
	//variabili del device gi� salvate in tabella master_mdl
	xml_mdl  = xmlResponse.getElementsByTagName("mdl");
	
	//variabili del device gi� salvate in tabella slave_mdl
	xml_mdl2 = xmlResponse.getElementsByTagName("mdl2");
	
	//variabili del device gi� salvate in tabella extra_mdl
	xml_mdl3 = xmlResponse.getElementsByTagName("mdl3");
	
	//variabile digitale di supporto (lato salve)
	xml_digvar = xmlResponse.getElementsByTagName("digvar");
	
	//creazione tabella variabili di origine
	fillOriginTable(xml_vars,xml_mdl);
	
	//creazione tabella variabili destinazione
	fillDestinationTable(xml_vars,xml_mdl2);
	
	//creazione tabella variabili extra
	fillExtraTable(xml_vars,xml_mdl3);
	
	//creazione tabella variabili heart-bit
	fillSafetyTable(xml_vars,xml_digvar);
}

function fillOriginTable(xml_vars,xml_mdl)
{
	var table = document.getElementById("t_master");
	var ind = "";
	
	var orig = "origin"; //x combo vars di Origin
	var cmbOrigVars = getComboVar(xml_vars,orig,"");
	
	var alrm = "alarms"; //x combo vars di Alarms
	var cmbAlrmVars = getComboVar(xml_vars,alrm,"");
	
	var alarmcode = "";
	var varcode = "";
	var minval = "";
	var maxval = "";
	var defval = "";
	var vdesc = "";
	var adesc = "";
	
	for(var index = 1; index < table.rows.length; index++)
	{
		for(var j = 0; j < xml_mdl.length; j++)
		{
			var variable = xml_mdl[j]; 
			ind = variable.getAttribute("id"); //index
			table.rows[index].className = "Row1";
			if (ind == index)
			{
				for(var k = 0; k < variable.childNodes.length; k++) {
		    		var name = variable.childNodes[k].nodeName; 
		    		if( name != "#text" ) {
		    			var value = variable.childNodes[k].childNodes.length ? variable.childNodes[k].childNodes[0].nodeValue : "";
		    			eval(name + " = '" + value.replace(/\'/g,"&acute;") + "'");
		    		}
		    	}
				
				if (minval == "null")
					minval = "";
				if (maxval == "null")
					maxval = "";
				if (defval == "null")
					defval = "";
				
				table.rows[index].cells[1].innerHTML = "<input type='hidden' id='varmdlcode_"+index+"' name='varmdlcode_"+index+"' value='"+varcode+"'/>"+vdesc; //vdesc
				table.rows[index].cells[1].className = "standardTxt";
				table.rows[index].cells[2].innerHTML = "<input type='hidden' id='alrmcode_"+index+"' name='alrmcode_"+index+"' value='"+alarmcode+"'/>"+adesc; //adesc
				table.rows[index].cells[2].className = "standardTxt";
				table.rows[index].cells[3].innerHTML = "<input type='hidden' id='origmin_"+index+"' name='origmin_"+index+"' value='"+minval+"'/>"+minval; //min
				table.rows[index].cells[3].className = "standardTxt";
				table.rows[index].cells[4].innerHTML = "<input type='hidden' id='origmax_"+index+"' name='origmax_"+index+"' value='"+maxval+"'/>"+maxval; //max
				table.rows[index].cells[4].className = "standardTxt";
				table.rows[index].cells[5].innerHTML = "<input type='hidden' id='origdef_"+index+"' name='origdef_"+index+"' value='"+defval+"'/>"+defval; //def
				table.rows[index].cells[5].className = "standardTxt";
				table.rows[index].cells[6].innerHTML = img_addorig_on + addOrig(index) + "'/>"; //img_add_off;
				table.rows[index].cells[6].className = "standardTxt";
				table.rows[index].cells[7].innerHTML = img_remorig_on + index + "); setModMaster();'/>";
				table.rows[index].cells[7].className = "standardTxt";
			}
		}
	}
	
	for(var index = 1; index < table.rows.length; index++)
	{		
		if (table.rows[index].cells[1].innerHTML == "")
		{
			table.rows[index].cells[1].innerHTML = "<input type='hidden' id='varmdlcode_"+index+"' name='varmdlcode_"+index+"' value=''/>"; //varcode
			table.rows[index].cells[1].className = "standardTxt";
			table.rows[index].cells[2].innerHTML = "<input type='hidden' id='alrmcode_"+index+"' name='alrmcode_"+index+"' value=''/>"; //alarm
			table.rows[index].cells[2].className = "standardTxt";
			table.rows[index].cells[3].innerHTML = "<input type='hidden' id='origmin_"+index+"' name='origmin_"+index+"' value=''/>"; //min
			table.rows[index].cells[3].className = "standardTxt";
			table.rows[index].cells[4].innerHTML = "<input type='hidden' id='origmax_"+index+"' name='origmax_"+index+"' value=''/>"; //max
			table.rows[index].cells[4].className = "standardTxt";
			table.rows[index].cells[5].innerHTML = "<input type='hidden' id='origdef_"+index+"' name='origdef_"+index+"' value=''/>"; //def
			table.rows[index].cells[5].className = "standardTxt";
			table.rows[index].cells[6].innerHTML = img_addorig_on + addOrig(index) + "'/>";
			table.rows[index].cells[6].className = "standardTxt";
			table.rows[index].cells[7].innerHTML = img_rem_off;
			table.rows[index].cells[7].className = "standardTxt";
		}
	}
}

function fillDestinationTable(xml_vars,xml_mdl2)
{
	var table = document.getElementById("t_slave");
	var ind = "";
	var varcode = "";
	var vdesc = "";
	var dest = "destination"; //x combo vars di Destination
	var cmbDestVars = getComboVar(xml_vars,dest,"");
	
	for(var index = 1; index < table.rows.length; index++)
	{
		for(var j = 0; j < xml_mdl2.length; j++)
		{
			var variable = xml_mdl2[j]; 
			ind = variable.getAttribute("id"); //index
			table.rows[index].className = "Row1";
			if (ind == index)
			{
				for(var k = 0; k < variable.childNodes.length; k++) {
		    		var name = variable.childNodes[k].nodeName; 
		    		if( name != "#text" ) {
		    			var value = variable.childNodes[k].childNodes.length ? variable.childNodes[k].childNodes[0].nodeValue : "";
		    			eval(name + " = '" + value.replace(/\'/g,"&acute;") + "'");
		    		}
				}
				table.rows[index].cells[1].innerHTML = "<input type='hidden' id='varmdl_"+index+"' name='varmdl_"+index+"' value='"+varcode+"'/>"+vdesc; //vdesc
				table.rows[index].cells[1].className = "standardTxt";
				table.rows[index].cells[2].innerHTML = img_adddest_on + addDest(index) + "'/>"; //img_add_off;
				table.rows[index].cells[2].className = "standardTxt";
				table.rows[index].cells[3].innerHTML = img_remdest_on + index + "); setModSlave()'/>";
				table.rows[index].cells[3].className = "standardTxt";
			}
		}
	}
	
	for(var index = 1; index < table.rows.length; index++)
	{
		if (table.rows[index].cells[1].innerHTML == "")
		{
			table.rows[index].cells[1].innerHTML = "<input type='hidden' id='varmdl_"+index+"' name='varmdl_"+index+"' value=''/>"; //vdesc
			table.rows[index].cells[1].className = "standardTxt";
			table.rows[index].cells[2].innerHTML = img_adddest_on + addDest(index) + "'/>";
			table.rows[index].cells[2].className = "standardTxt";
			table.rows[index].cells[3].innerHTML = img_rem_off;
			table.rows[index].cells[3].className = "standardTxt";
		}
	}
}

function fillExtraTable(xml_vars,xml_mdl3)
{
	var table = document.getElementById("t_extra");
	var ind = "";
	var varcode = "";
	var vdesc = "";
	var dest = "extra"; //x combo vars di Extra
	var cmbExtraVars = getComboVar(xml_vars,dest,"");
	
	for(var index = 1; index < table.rows.length; index++)
	{
		for(var j = 0; j < xml_mdl3.length; j++)
		{
			var variable = xml_mdl3[j]; 
			ind = variable.getAttribute("id"); //index
			table.rows[index].className = "Row1";
			if (ind == index)
			{
				for(var k = 0; k < variable.childNodes.length; k++) {
		    		var name = variable.childNodes[k].nodeName; 
		    		if( name != "#text" ) {
		    			var value = variable.childNodes[k].childNodes.length ? variable.childNodes[k].childNodes[0].nodeValue : "";
		    			eval(name + " = '" + value.replace(/\'/g,"&acute;") + "'");
		    		}
				}
				
				table.rows[index].cells[1].innerHTML = "<input type='hidden' id='varmdlex_"+index+"' name='varmdlex_"+index+"' value='"+varcode+"'/>"+vdesc; //vdesc
				table.rows[index].cells[1].className = "standardTxt";
				table.rows[index].cells[2].innerHTML = img_addext_on + addExtra(index) + "'/>"; //img_add_off;
				table.rows[index].cells[2].className = "standardTxt";
				table.rows[index].cells[3].innerHTML = img_remext_on + index + ");'/>";
				table.rows[index].cells[3].className = "standardTxt";
			}
		}
	}
	
	for(var index = 1; index < table.rows.length; index++)
	{
		if (table.rows[index].cells[1].innerHTML == "")
		{
			table.rows[index].cells[1].innerHTML = "<input type='hidden' id='varmdlex_"+index+"' name='varmdlex_"+index+"' value=''/>"; //vdesc
			table.rows[index].cells[1].className = "standardTxt";
			table.rows[index].cells[2].innerHTML = img_addext_on + addExtra(index) + "'/>";
			table.rows[index].cells[2].className = "standardTxt";
			table.rows[index].cells[3].innerHTML = img_rem_off;
			table.rows[index].cells[3].className = "standardTxt";
		}
	}
}

function ctrl_SafeVar()
{
	var digv = document.getElementById("digvar").value;
	var varobj = document.getElementById("timevar");
	var valobj = document.getElementById("varval");
	
	if (digv == "-1")
	{
		varobj.selectedIndex = 0;
		varobj.disabled = true;
		
		valobj.value = document.getElementById("acbasetime").value;
		valobj.disabled = true;
	}
	else
	{
		varobj.disabled = false;
		valobj.disabled = false;
	}
}

function fillSafetyTable(xml_vars,xml_digvar,xml_heartbit)
{
	var table = document.getElementById("t_safety");
	var varvalue = document.getElementById("acbasetime").value;
	var varcode = "";
	var digvar = "";
	//20091201-simon add
	//it will disable the calendar input box when the virtual Keyboard is open
	var vkeyboard="";
	if(document.getElementById("vkeytype")!=null){
		vkeyboard='class="keyboardInput"';
	}
	if (xml_digvar.length > 0)
	{
		variable = xml_digvar[0]; 
		digvar = variable.getAttribute("id");
		for(var k = 0; k < variable.childNodes.length; k++) {
    		var name = variable.childNodes[k].nodeName; 
    		if( name != "#text" ) {
    			var value = variable.childNodes[k].childNodes.length ? variable.childNodes[k].childNodes[0].nodeValue : "";
    			eval(name + " = '" + value.replace(/\'/g,"&acute;") + "'");
    		}
		}
	}
	
	var dest = "heartbit"; //x combo vars digitali HeartBit
	var cmbHeartBit = getComboVar(xml_vars,dest,digvar);

	dest = "safety"; //x combo vars intere o analogiche HeartBit
	var cmbSafetyVar = getComboVar(xml_vars,dest,varcode);
	
	table.rows[1].cells[1].innerHTML = cmbHeartBit;
	document.getElementById("digvar").onchange = function(){ctrl_SafeVar();modDevice();enableAction(1);};
	table.rows[1].cells[2].innerHTML = cmbSafetyVar;
	table.rows[1].cells[3].innerHTML = "<input type='text' id='varval' name='varval' "+vkeyboard+" value='"+varvalue+"' style='width:75%;' onchange='modDevice();' onkeydown='checkOnlyAnalog(this,event);enableAction(1);' onblur='checkOnlyAnalogOnBlur(this);'/>";
	ctrl_SafeVar();
	if(document.getElementById("vkeytype")!=null){
		buildKeyboardInputs();
	}
}

function clearTables()
{
	// tabella Origin:
	var table1 = document.getElementById("t_master");
	for (index = 1; index < table1.rows.length; index++)
	{
		table1.rows[index].cells[1].innerHTML = "";
		table1.rows[index].cells[2].innerHTML = "";
		table1.rows[index].cells[3].innerHTML = "";
		table1.rows[index].cells[4].innerHTML = "";
		table1.rows[index].cells[5].innerHTML = "";
		table1.rows[index].cells[6].innerHTML = "";
		table1.rows[index].cells[7].innerHTML = "";
	}
	
	// tabella Destination:
	var table2 = document.getElementById("t_slave");
	for (index = 1; index < table2.rows.length; index++)
	{
		table2.rows[index].cells[1].innerHTML = "";
		table2.rows[index].cells[2].innerHTML = "";
		table2.rows[index].cells[3].innerHTML = "";
	}
	
	// tabella Extra:
	var table3 = document.getElementById("t_extra");
	for (index = 1; index < table3.rows.length; index++)
	{
		table3.rows[index].cells[1].innerHTML = "";
		table3.rows[index].cells[2].innerHTML = "";
		table3.rows[index].cells[3].innerHTML = "";
	}
	
	// tabella Safety:
	var table4 = document.getElementById("t_safety");
	for (index = 1; index < table4.rows.length; index++)
	{
		table4.rows[index].cells[1].innerHTML = "";
		table4.rows[index].cells[2].innerHTML = "";
		table4.rows[index].cells[3].innerHTML = "";
	}
}

function trim(stringa)
{
	return stringa.replace(/^\s+|\s+$/g,"");
}

function getComboVar(xml_vars,destin,varmdlcode)
{
	var cmbid = "";

	var onchangecombo = "enableAction(1);modDevice();";
	
	if (destin=="origin")
	{
		cmbid = "varmdlcode";
		onchangecombo += "setModMaster();";
	}
	else if (destin=="destination")
	{
		cmbid = "varmdl";
		onchangecombo += "setModSlave();";
	}
	else if (destin == "extra")
	{
		cmbid = "varmdlex";
	}
	else if (destin == "alarms")
	{
		cmbid = "alrmcode";
	}
	else if (destin == "safety")
	{
		cmbid = "timevar";
	}
	else if (destin == "heartbit")
	{
		cmbid = "digvar";
	}
	
	var combo = "<select id='"+cmbid+"' name='"+cmbid+"' onchange='"+onchangecombo+"'";
	
	if ((destin != "destination") && (destin != "extra")) combo = combo + " style='width:97%;'";
	
	combo = combo + ">\n <option value='-1'> -------------------- </option>\n";
	
	if (xml_vars.length > 0)
	{
		var idvarmdl = "";
		var descr = "";
		var type = "";
		var rw = "";
		var varcode = "";
		
		for(var i = 0; i < xml_vars.length; i++)
		{
			var variable = xml_vars[i];
			idvarmdl = variable.getAttribute("id");
			for(var k = 0; k < variable.childNodes.length; k++) {
	    		var name = variable.childNodes[k].nodeName; 
	    		if( name != "#text" ) {
	    			var value = variable.childNodes[k].childNodes.length ? variable.childNodes[k].childNodes[0].nodeValue : "";
	    			eval(name + " = '" + value.replace(/\'/g,"&acute;") + "'");
	    		}
			}

			if ((destin=="extra" && trim(type)!="4") || (destin=="origin" && trim(type)!="4") || (destin=="destination" && trim(type)!="4" && trim(rw)!="1") || (destin=="heartbit" && trim(type)=="1") || (destin=="safety" && (trim(type)=="2" || trim(type)=="3")) || (destin=="alarms" && trim(type)=="4"))
			{
				if (varcode != "-1")
				{
					combo = combo + "<option value='"+varcode+"' ";
					if (varmdlcode == varcode) combo = combo + "selected ";
					combo = combo + ">"+descr+"</option>\n";
				}
			}
		}
		combo = combo + "</select>\n";	
	}
	return combo;
}

function setModMaster()
{
	document.getElementById("modMaster").value = "true";
}

function setModSlave()
{
	document.getElementById("modSlave").value = "true";
}


var last_orig_row = -1;
var last_orig_mod = -1;

var last_dest_row = -1;
var last_dest_mod = -1;

var last_ext_row = -1;
var last_ext_mod = -1;

function select_destrow(destriga)
{
	var table = document.getElementById("t_slave");
	if (last_dest_row > 0)
	{
		table.rows[last_dest_row].className = "Row1";
	}
	last_dest_row = destriga;
	if (last_dest_row > 0)
	{
		table.rows[last_dest_row].className = "selectedRow";
	}
}

function hide_destset(destline)
{
	if (destline == last_dest_mod) return;
	if (Number(last_dest_mod) != -1 && document.getElementById("varmdl") != null)
	{
		var table = document.getElementById("t_slave");

		var varmdlcode = "";
		var varmdldesc = "";
		
		// valore di option selezionata
		varmdlcode = document.getElementById("varmdl").value;
		if (varmdlcode == "-1")
		{
			varmdlcode = "";
			table.rows[last_dest_mod].cells[2].innerHTML = img_adddest_on + addDest(last_dest_mod) + "'/>";
			table.rows[last_dest_mod].cells[3].innerHTML = img_rem_off;
		}
		else
		{
			// valore del testo corrispondente a option selezionata
			varmdldesc = document.getElementById("varmdl").options[document.getElementById("varmdl").selectedIndex].text;
			table.rows[last_dest_mod].cells[2].innerHTML = img_adddest_on + addDest(last_dest_mod) + "'/>"; //img_add_off;
			table.rows[last_dest_mod].cells[3].innerHTML = img_remdest_on + last_dest_mod + ");'/>";
		}
		
		table.rows[last_dest_mod].cells[1].innerHTML = "<input type='hidden' id='varmdl_"+last_dest_mod+"' name='varmdl_"+last_dest_mod+"' value='"+varmdlcode+"'/>"+varmdldesc;
		
		last_dest_mod = -1;
		enableAction(1);
		//setModSlave();
	}
}

function mod_destline(desttab_line)
{
	var iddev = document.getElementById("dev_combo").value;
	if (iddev != "-1")
	{
		if (Number(desttab_line) != Number(last_dest_mod))
		{
			//al click disabilito una eventuale seconda selezione
			//hide_destset(last_dest_mod);
			var table = document.getElementById("t_slave");
			
			//varmdlcode
			var varmdlcode = document.getElementById("varmdl_"+desttab_line).value;
			table.rows[desttab_line].cells[1].innerHTML = getComboVar(xml_vars,"destination",varmdlcode);
		
			last_dest_mod = desttab_line;
		}
	}
}

function select_origrow(origriga)
{
	var table = document.getElementById("t_master");
	if (last_orig_row > 0)
	{
		table.rows[last_orig_row].className="Row1";
	}
	last_orig_row = origriga;
	if (last_orig_row > 0)
	{
		table.rows[last_orig_row].className="selectedRow";
	}
}

function hide_origset(origline)
{
	if (origline == last_orig_mod) return;
	if (Number(last_orig_mod) != -1 && document.getElementById("varmdlcode") != null)
	{
		var table = document.getElementById("t_master");
		
		notNumber = false;

		var varmdlcode = "";
		var varmdldesc = "";
		var alrmcode = "";
		var alrmdesc = "";
		var valore = "";
		
		var minvalue = "";
		var maxvalue = "";
		var defvalue = "";
		
		// valori di option selezionati
		varmdlcode = document.getElementById("varmdlcode").value;
		alrmcode = document.getElementById("alrmcode").value;
		
		//valori necessari almeno varcode + alrmcode
		//if ((varmdlcode == "-1") || (alrmcode == "-1"))
		if (varmdlcode == "-1")
		{
			varmdlcode = "";
			//alrmcode = "";
			
			table.rows[last_orig_mod].cells[6].innerHTML = img_addorig_on + addOrig(last_orig_mod) + "'/>";
			table.rows[last_orig_mod].cells[7].innerHTML = img_rem_off;
			
			if (alrmcode != "-1")
			{
				alert(document.getElementById("badvalues").value);
				return;
			}
		}
		else
		{
			// valore del testo corrispondente a option selezionata
			varmdldesc = document.getElementById("varmdlcode").options[document.getElementById("varmdlcode").selectedIndex].text;
			
			alrmdesc = document.getElementById("alrmcode").options[document.getElementById("alrmcode").selectedIndex].text;
			
			valore = document.getElementById("origmin_"+last_orig_mod).value;
			if (valore != "")
			{
				valore = valore.replace(",",".");
				//minvalue = Number(valore);
				minvalue = valore;
				if (isNaN(minvalue))
				{
					minvalue = 0;
					notNumber = true;
				}
			}

			valore = document.getElementById("origmax_"+last_orig_mod).value;
			if (valore != "")
			{
				valore = valore.replace(",",".");
				//maxvalue = Number(valore);
				maxvalue = valore;
				if (isNaN(maxvalue))
				{
					maxvalue = 0;
					notNumber = true;
				}
			}
			
			valore = document.getElementById("origdef_"+last_orig_mod).value;
			if (valore != "")
			{
				valore = valore.replace(",",".");
				//defvalue = Number(valore);
				defvalue = valore;
				if (isNaN(defvalue))
				{
					defvalue = 0;
					notNumber = true;
				}
			}
			
			table.rows[last_orig_mod].cells[6].innerHTML = img_addorig_on + addOrig(last_orig_mod) + "'/>"; //img_add_off;
			table.rows[last_orig_mod].cells[7].innerHTML = img_remorig_on + last_orig_mod + ");'/>";

			if ((defvalue == "") || (minvalue == "") || (maxvalue == "") || (Number(defvalue) < Number(minvalue)) || (Number(defvalue) > Number(maxvalue)) || (notNumber))
			{
				alert(document.getElementById("badvalues").value);
				
				minvalue = 0;
				maxvalue = 0;
				defvalue = 0;
				
				//blocco l'utente in fase di editing finch� non vengono digitati tutti e 3 i valori:
				return;
			}
		}

		table.rows[last_orig_mod].cells[1].innerHTML = "<input type='hidden' id='varmdlcode_"+last_orig_mod+"' name='varmdlcode_"+last_orig_mod+"' value='"+varmdlcode+"'/>"+varmdldesc;
		table.rows[last_orig_mod].cells[2].innerHTML = "<input type='hidden' id='alrmcode_"+last_orig_mod+"' name='alrmcode_"+last_orig_mod+"' value='"+alrmcode+"'/>"+alrmdesc;
		
		table.rows[last_orig_mod].cells[3].innerHTML = "<input type='hidden' id='origmin_"+last_orig_mod+"' name='origmin_"+last_orig_mod+"' value='"+minvalue+"'/>";
		if (!isNaN(minvalue) && minvalue!="")
			table.rows[last_orig_mod].cells[3].innerHTML = table.rows[last_orig_mod].cells[3].innerHTML + (Number(minvalue)).toFixed(1);
		
		table.rows[last_orig_mod].cells[4].innerHTML = "<input type='hidden' id='origmax_"+last_orig_mod+"' name='origmax_"+last_orig_mod+"' value='"+maxvalue+"'/>";
		if (!isNaN(maxvalue) && maxvalue!="")
			table.rows[last_orig_mod].cells[4].innerHTML = table.rows[last_orig_mod].cells[4].innerHTML + (Number(maxvalue)).toFixed(1);
		
		table.rows[last_orig_mod].cells[5].innerHTML = "<input type='hidden' id='origdef_"+last_orig_mod+"' name='origdef_"+last_orig_mod+"' value='"+defvalue+"'/>";
		if (!isNaN(defvalue) && defvalue!="")
			table.rows[last_orig_mod].cells[5].innerHTML = table.rows[last_orig_mod].cells[5].innerHTML + (Number(defvalue)).toFixed(1);
		
		last_orig_mod = -1;
		enableAction(1);
		//setModMaster();
	}
}

function mod_origline(origtab_line)
{
	var iddev = document.getElementById("dev_combo").value;
	if (iddev != "-1")
	{
		if (Number(origtab_line) != Number(last_orig_mod))
		{
			//al click disabilito una eventuale seconda selezione
			hide_origset(last_orig_mod);

			var table = document.getElementById("t_master");
				
			//varmdlcode
			var varmdlcode = document.getElementById("varmdlcode_"+origtab_line).value;
			var alrmcode = document.getElementById("alrmcode_"+origtab_line).value;
			var minvalue = document.getElementById("origmin_"+origtab_line).value;
			var maxvalue = document.getElementById("origmax_"+origtab_line).value;
			var defvalue = document.getElementById("origdef_"+origtab_line).value;
				
			//20091201-simon add
			//it will disable the calendar input box when the virtual Keyboard is open
			var vkeyboard="";
			if(document.getElementById("vkeytype")!=null){
				vkeyboard='class="keyboardInput"';
			}
			table.rows[origtab_line].cells[1].innerHTML = getComboVar(xml_vars,"origin",varmdlcode);
			table.rows[origtab_line].cells[2].innerHTML = getComboVar(xml_vars,"alarms",alrmcode);
			table.rows[origtab_line].cells[3].innerHTML = "<input type='text' style='width:100%' "+vkeyboard+" id='origmin_"+origtab_line+"' name='origmin_"+origtab_line+"' value='"+minvalue+"' onpaste='checkOnlyAnalog(this,event);return false;' onkeydown='checkOnlyAnalog(this,event);' />";
			table.rows[origtab_line].cells[4].innerHTML = "<input type='text' style='width:100%' "+vkeyboard+" id='origmax_"+origtab_line+"' name='origmax_"+origtab_line+"' value='"+maxvalue+"' onpaste='checkOnlyAnalog(this,event);return false;' onkeydown='checkOnlyAnalog(this,event);' />";
			table.rows[origtab_line].cells[5].innerHTML = "<input type='text' style='width:100%' "+vkeyboard+" id='origdef_"+origtab_line+"' name='origdef_"+origtab_line+"' value='"+defvalue+"' onpaste='checkOnlyAnalog(this,event);return false;' onkeydown='checkOnlyAnalog(this,event);' />";
				
			last_orig_mod = origtab_line;
			
			if(document.getElementById("vkeytype")!=null){
				buildKeyboardInputs();
			}
			
		}
	}
}

function rem_origline(origline)
{
	hide_origset(-1);
	
	if (confirm(document.getElementById("delrow").value))
	{
		var table = document.getElementById("t_master");
		
		table.rows[origline].cells[1].innerHTML = "<input type='hidden' id='varmdlcode_"+origline+"' name='varmdlcode_"+origline+"' value=''/>"; //varcode
		table.rows[origline].cells[1].className = "standardTxt";
		table.rows[origline].cells[2].innerHTML = "<input type='hidden' id='alrmcode_"+origline+"' name='alrmcode_"+origline+"' value=''/>"; //alarm
		table.rows[origline].cells[2].className = "standardTxt";
		table.rows[origline].cells[3].innerHTML = "<input type='hidden' id='origmin_"+origline+"' name='origmin_"+origline+"' value=''/>"; //min
		table.rows[origline].cells[3].className = "standardTxt";
		table.rows[origline].cells[4].innerHTML = "<input type='hidden' id='origmax_"+origline+"' name='origmax_"+origline+"' value=''/>"; //max
		table.rows[origline].cells[4].className = "standardTxt";
		table.rows[origline].cells[5].innerHTML = "<input type='hidden' id='origdef_"+origline+"' name='origdef_"+origline+"' value=''/>"; //def
		table.rows[origline].cells[5].className = "standardTxt";
		table.rows[origline].cells[6].innerHTML = img_addorig_on + addOrig(origline) + "'/>";
		table.rows[origline].cells[6].className = "standardTxt";
		table.rows[origline].cells[7].innerHTML = img_rem_off;
		table.rows[origline].cells[7].className = "standardTxt";
		
		last_orig_mod = -1;
		enableAction(1);
		//setModMaster();
	}
}

function rem_destline(destline)
{
	hide_destset(-1);
	
	if (confirm(document.getElementById("delrow").value))
	{
		var table = document.getElementById("t_slave");
		
		table.rows[destline].cells[1].innerHTML = "<input type='hidden' id='varmdl_"+destline+"' name='varmdl_"+destline+"' value=''/>"; //vdesc
		table.rows[destline].cells[1].className = "standardTxt";
		table.rows[destline].cells[2].innerHTML = img_adddest_on + addDest(destline) + "'/>";
		table.rows[destline].cells[2].className = "standardTxt";
		table.rows[destline].cells[3].innerHTML = img_rem_off;
		table.rows[destline].cells[3].className = "standardTxt";
		
		last_dest_mod = -1;
		enableAction(1);
		//setModSlave();
	}
}

function rem_extline(extline)
{
	hide_extset(-1);
	
	if (confirm(document.getElementById("delrow").value))
	{
		var table = document.getElementById("t_extra");
		
		table.rows[extline].cells[1].innerHTML = "<input type='hidden' id='varmdlex_"+extline+"' name='varmdlex_"+extline+"' value=''/>"; //vdesc
		table.rows[extline].cells[1].className = "standardTxt";
		table.rows[extline].cells[2].innerHTML = img_addext_on + addExtra(extline) + "'/>";
		table.rows[extline].cells[2].className = "standardTxt";
		table.rows[extline].cells[3].innerHTML = img_rem_off;
		table.rows[extline].cells[3].className = "standardTxt";
		
		last_ext_mod = -1;
		enableAction(1);
	}
}

function mod_extline(exttab_line)
{
	var iddev = document.getElementById("dev_combo").value;
	if (iddev != "-1")
	{
		if (Number(exttab_line) != Number(last_ext_mod))
		{
			//al click disabilito una eventuale seconda selezione
			//hide_destset(last_dest_mod);
			var table = document.getElementById("t_extra");
			
			//varmdlcode
			var varmdlcode = document.getElementById("varmdlex_"+exttab_line).value;
			table.rows[exttab_line].cells[1].innerHTML = getComboVar(xml_vars,"extra",varmdlcode);
		
			last_ext_mod = exttab_line;
		}
	}
}

function select_extrow(extriga)
{
	var table = document.getElementById("t_extra");
	if (last_ext_row > 0)
	{
		table.rows[last_ext_row].className="Row1";
	}
	last_ext_row = extriga;
	if (last_ext_row > 0)
	{
		table.rows[last_ext_row].className="selectedRow";
	}
}

function hide_extset(extline)
{
	if (extline == last_ext_mod) return;
	if (Number(last_ext_mod) != -1 && document.getElementById("varmdlex") != null)
	{
		var table = document.getElementById("t_extra");

		var varmdlcode = "";
		var varmdldesc = "";
		
		// valore di option selezionata
		varmdlcode = document.getElementById("varmdlex").value;
		if (varmdlcode == "-1")
		{
			varmdlcode = "";
			table.rows[last_ext_mod].cells[2].innerHTML = img_addext_on + addExtra(last_ext_mod) + "'/>";
			table.rows[last_ext_mod].cells[3].innerHTML = img_rem_off;
		}
		else
		{
			// valore del testo corrispondente a option selezionata
			varmdldesc = document.getElementById("varmdlex").options[document.getElementById("varmdlex").selectedIndex].text;
			table.rows[last_ext_mod].cells[2].innerHTML = img_addext_on + addExtra(last_ext_mod) + "'/>"; //img_add_off;
			table.rows[last_ext_mod].cells[3].innerHTML = img_remext_on + last_ext_mod + ");'/>";
		}
		
		table.rows[last_ext_mod].cells[1].innerHTML = "<input type='hidden' id='varmdlex_"+last_ext_mod+"' name='varmdlex_"+last_ext_mod+"' value='"+varmdlcode+"'/>"+varmdldesc;
		
		last_ext_mod = -1;
		enableAction(1);
	}
}

function save_tab4()
{
	//controlli su combobox e valori editabili non ancora salvati in pagina prima del SUBMIT !!!
	var iddev = document.getElementById("dev_combo").value;
	
	var ok_save4 = false;
	
	if (iddev != "-1")
	{
		//controllo valori x Heart bit:
		var mintime = document.getElementById("acbasetime").value;
		var digvar = document.getElementById("digvar").value;
		var timevar = document.getElementById("timevar").value;
		var varvalue = 0;
		if (document.getElementById("varval"))	varvalue = document.getElementById("varval").value;
	
		ok_save4 = ((digvar == -1) || ((digvar != -1) && (timevar != -1) && (varvalue >= mintime)));
		
		if (! ok_save4)
		{
			if (timevar == -1)
			{
				alert(document.getElementById("heartbit_no_ok").value);
			}
			else
			if (varvalue < mintime)
			{
				alert(document.getElementById("mintime_no_ok").value+"\n\n  "+mintime+" min");
			}
		}
		else
		if (confirm(document.getElementById("ok_save_tab4").value))
		{
			//chiudo eventuali controlli editabili aperti
			hide_origset(-1);
			hide_destset(-1);
			hide_extset(-1);
			
			//post della form
			document.getElementById("cmd").value = "save_models";
			MTstartServerComm();
			document.getElementById("ac_frm").submit();
		}
	}
	else
	{
		if (document.getElementById("dev_combo"))
		{
			document.getElementById("cmd").value = "save_cus_status";
			MTstartServerComm();
			document.getElementById("ac_frm").submit();
		}
	}
}

function addOrig(indice)
{
	return "hide_origset("+indice+");select_origrow("+indice+");mod_origline("+indice+");";
}

function addDest(indice)
{
	return "hide_destset("+indice+");select_destrow("+indice+");mod_destline("+indice+");";
}

function addExtra(indice)
{
	return "hide_extset("+indice+");select_extrow("+indice+");mod_extline("+indice+");";
}

function xSave()
{
	var avviso = document.getElementById("salva").value;
	alert(avviso);
}
