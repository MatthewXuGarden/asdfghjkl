var lineselected_alr = null;
var clickState_alr = null;

var lineselected_var = null;
var clickState_var = null;

var press_cancel = false;

function onVarPageLoad()
{
	onChangeSwitch();
	if( document.getElementById("restart_required").value == 1 )
		alert(document.getElementById("alert_restart").value);
}
//ricaricare modelli di variabili e allarmi configurati
function onChangeSwitch()
{
	var switch_id = document.getElementById("switch_id").value;
	if (switch_id==-1)
	{
		disableAction(1);
		disableAction(2);
		//pulire tutto in pagina
		clear_table('switch_varmdl_table');
		clear_table('switch_alrmdl_table');
		document.getElementById("devmdl_id").value=-1;
		onChangeDevMdl();
		document.getElementById("devmdl_id").disabled=true;
	}
	else
	{
		enableAction(1);
		enableAction(2);
		lineselected_alr = null;
		clickState_alr = null;
		lineselected_var = null;
		clickState_var = null;
		document.getElementById("devmdl_id").disabled=false;
		CommSend("servlet/ajrefresh","POST","cmd=load_switch&switch_id="+switch_id+"","onChangeSwitch");
	}
	
}
function createMinMaxHiddenControl(idvar,min,max)
{
	var html = "";
	if(min != null && min != "")
		html = "<input type='hidden' id='min_"+idvar+"' value='"+min+"'/>";
	if(max != null && max != "")
		html += "<input type='hidden' id='max_"+idvar+"' value='"+max+"'/>\n";
	return html;	
}
function getOnblur(prefix,virtkey,idvar)
{
	if(virtkey == "on")
	{
		return " onblur='checkOnlyAnalogOnBlur(this);' ";
	}
	else
	{
		return " onblur=\"controlMinMax('"+prefix+"',"+idvar+");checkOnlyAnalogOnBlur(this);\" ";
	}
}
function controlMinMax(prefix,idvar)
{
	var min_permitted = document.getElementById('min_'+idvar);
	if(min_permitted != null)
		min_permitted = min_permitted.value;
	var max_permitted = document.getElementById('max_'+idvar);
	if(max_permitted != null)
		max_permitted = max_permitted.value;
	var set = document.getElementById(prefix+idvar).value;
	if (set!="")
	{
		if (min_permitted!=null && min_permitted != "")
		{
			if (Number(set)<Number(min_permitted))
			{
				alert(document.getElementById("s_minval").value +min_permitted);
				document.getElementById(prefix+idvar).focus();
				return false;
			}
		}
		if (max_permitted!=null && max_permitted != "")
		{
			if (Number(set)>Number(max_permitted))
			{
				alert(document.getElementById("s_maxval").value +max_permitted);
				document.getElementById(prefix+idvar).focus();
				return false;
			}
		}
		return true;
	}
	else
	{
		return true;
	}
}
function createInputType(prefix,idvarmdl,type,loaded,decimals)
{

	var html = "";
	
	var virtualkeyboard = "";
	var virtkey = document.getElementById("virtkeyboard").value;
	
	if (virtkey == "on")
	{
		virtualkeyboard = "class=\"keyboardInput\"";
	}
	
	if (type == 1) //digitale
    {
    	html = "<input type='text' "+virtualkeyboard+" value='"+loaded+"' style='width:35px;' maxlenght='4' id='"+prefix+idvarmdl +"' onkeydown='checkOnlyDigit(this,event);' onblur='checkOnlyDigitOnBlur(this);'/>";
    }
    else if (decimals > 0) //analogica  o intera con dei decimals settati
    {
    	html = "<input type='text' "+virtualkeyboard+" value='"+loaded+"' style='width:35px;' maxlenght='4' "+getOnblur(prefix,virtkey,idvarmdl)+"' id='"+prefix+idvarmdl +"' onkeydown='checkOnlyAnalog(this,event);'/>";
    }
    else //intera o analogica trattata come intera
    {
    	html = "<input type='text' "+virtualkeyboard+" value='"+loaded+"' style='width:35px;' maxlenght='4' "+getOnblur(prefix,virtkey,idvarmdl)+"' id='"+prefix+idvarmdl +"' onkeydown='checkOnlyNumber(this,event);'/>";
    }
	
    return html;
}
function Callback_onChangeSwitch()
{
	// collect variables
	var xml_variables = xmlResponse.getElementsByTagName("variable");
	// collect alarms
	var xml_alarms = xmlResponse.getElementsByTagName("alarm");

	clear_table('switch_varmdl_table');
	var var_table = document.getElementById("switch_varmdl_table");
	for(var i=0;i<xml_variables.length;i++)
	{
		var variable = xml_variables[i];
		var mdl = String(variable.getAttribute("id"));
		var tmp = variable.childNodes[0].nodeValue.split(",");
		var dev_id = tmp[0];
		var eev = tmp[1];
		var tev = tmp[2];
		var descr = variable.childNodes[1].nodeValue;
		var type = variable.childNodes[2].nodeValue;
		var min = variable.childNodes[3].nodeValue;
		var max = variable.childNodes[4].nodeValue;
		var decimal = variable.childNodes[5].nodeValue;
		
		var_table.insertRow(i);
		var row_added = var_table.rows[i];
		row_added.className="Row1";
		row_added.name="varmdl_"+dev_id+","+mdl;
		row_added.id="varmdl_"+dev_id+","+mdl;
			
		row_added.onclick= function(){selectedLineVar(this,'switch_varmdl_table');return false;};
		row_added.ondblclick = function(){varDblClick(this);};
		row_added.insertCell(0);
		row_added.insertCell(1);
		row_added.insertCell(2);
		row_added.className="Row1";	
		row_added.cells[0].width=500;
		row_added.cells[1].width=35;
		row_added.cells[2].width=35;
		row_added.cells[0].innerHTML = descr;
		//row_added.cells[1].innerHTML = "<input type='text' id='e_"+mdl+"' class='standardTxt' style='width:35px;' maxlenght='4' value='"+eev+"'>";
		row_added.cells[1].innerHTML = createInputType("e_",mdl,type,eev,decimal);
		//row_added.cells[2].innerHTML = "<input type='text' id='t_"+mdl+"' class='standardTxt' style='width:35px;' maxlenght='4' value='"+tev+"'>";
		row_added.cells[2].innerHTML = createInputType("t_",mdl,type,tev,decimal)+
				createMinMaxHiddenControl(mdl,min,max);
		row_added.cells[0].className= "standardTxt";	
		row_added.cells[1].className= "standardTxt";	
		row_added.cells[2].className= "standardTxt";	
	}

	clear_table('switch_alrmdl_table');
	var alr_table = document.getElementById("switch_alrmdl_table");
	for(var i=0;i<xml_alarms.length;i++)
	{
		var alarm = xml_alarms[i];
		var mdl = String(alarm.getAttribute("id"));
		var tmp = alarm.childNodes[0].nodeValue.split(",");
		var dev_id = tmp[0];
		var descr = alarm.childNodes[1].nodeValue;
		alr_table.insertRow(i);
		var row_added = alr_table.rows[i];
		row_added.className="Row1";
		row_added.name="varmdl_"+dev_id+","+mdl;
		row_added.id="varmdl_"+dev_id+","+mdl;
			
		row_added.onclick= function(){selectedLineAlr(this,'switch_alrmdl_table');return false;};
		row_added.ondblclick = function(){alrDblClick(this);};
		row_added.insertCell(0);
		
		row_added.className="Row1";	
		row_added.cells[0].width=500;
		
		row_added.cells[0].innerHTML = descr;
		row_added.cells[0].className= "standardTxt";	
	}
	
}

//ricaricare modelli di variabili e allarmi per il dispositivo selezionato
function onChangeDevMdl()
{
	var iddev = document.getElementById("devmdl_id").value;
	document.getElementById("switch_varmdl").length=0;
	document.getElementById("switch_alrmdl").length=0;	
	if (iddev==-1)
	{
		//pulire variabili di sinistra
	}
	else
		CommSend("servlet/ajrefresh","POST","cmd=load_varmdl&iddev="+iddev+"","onChangeDevMdl",true);
}

function Callback_onChangeDevMdl()
{
	var xml_variables = xmlResponse.getElementsByTagName("variable");
	var xml_alarms = xmlResponse.getElementsByTagName("alarm");
	
	var table = document.getElementById("switch_varmdl_table");
	var size_table = table.rows.length;
		
	var combovar = document.getElementById("switch_varmdl");
	var cont = 0;
	if (xml_variables.length>0)
	{
		for (i=0;i<xml_variables.length;i++)
		{
			var idvar = String(xml_variables[i].getAttribute("id"));
			var desc = xml_variables[i].childNodes[0].nodeValue;
			var type = xml_variables[i].childNodes[1].nodeValue;
			var min = xml_variables[i].childNodes[2].nodeValue;
			var max = xml_variables[i].childNodes[3].nodeValue;
			var decimal = xml_variables[i].childNodes[4].nodeValue;
			var iscontained = false;
			for (j=0;j<size_table;j++)
			{
				tmp_rowid = table.rows[j].id;
				tmp_id = tmp_rowid.split(",")[1];
				if (idvar == tmp_id)
				{
					iscontained = true;
					break;
				}		 
			}
			if(iscontained == false)
			{
				combovar.options[cont++] = new Option(desc,idvar+","+type+","+min+","+max+","+decimal);
			}
		}
	}	
	table = document.getElementById("switch_alrmdl_table");
	size_table = table.rows.length;
	var comboalr = document.getElementById("switch_alrmdl");
	cont = 0;
	if (xml_alarms.length>0)
	{
		for (i=0;i<xml_alarms.length;i++)
		{
			var idalr = String(xml_alarms[i].getAttribute("id"));
			var desc = xml_alarms[i].childNodes[0].nodeValue;
			iscontained = false;
			for (j=0;j<size_table;j++)
			{
				tmp_rowid = table.rows[j].id;
				tmp_id = tmp_rowid.split(",")[1];
				if (idalr == tmp_id)
				{
					iscontained = true;
					break;
				}		 
			}
			if(iscontained == false)
			{
				comboalr.options[cont++] = new Option(desc,idalr);
			}
		}
	}	
}
function moveVarLeft2Right(leftId,rightId,deviceid)
{
	//controllo selezione switch
	if (document.getElementById("switch_id").value==-1)
	{
		alert(document.getElementById("selectswitchalert").value);
		return false;
	}
	var selected_index= document.getElementById(leftId).selectedIndex;
	if (selected_index==-1)
	{
		alert(document.getElementById("nullselected").value);
		return true;
	}
	//has many device
	var list1 = document.getElementById(leftId);
	var table = document.getElementById(rightId);
	var size_table = table.rows.length;
	
	//get device description
	var device = document.getElementById(deviceid);
	var dev_id = device.options[device.selectedIndex].value;
	var deviceDes = device.options[device.selectedIndex].text;
	
	
	//check double
	var addArray = new Array(); 
	var leftIndexArray = new Array();
	var j = 0;
	while (list1.selectedIndex != -1)
	{
		var add = list1.options[list1.selectedIndex];
		var select_id = add.value.split(",")[0];
		var tmp_id = -1;
		var tmp_rowid = "";
		var iscontained = false;
		for (i=0;i<size_table;i++)
		{
			tmp_rowid = table.rows[i].id;
			tmp_id = tmp_rowid.split(",")[1];
			if (select_id == tmp_id)
			{
				iscontained = true;
				break;
			}		 
		}
		if(iscontained == false)
		{
			var oOption = document.createElement("OPTION");
			oOption.text=deviceDes+" - "+add.text;
			oOption.value=add.value;;
			addArray[j] = oOption;
			leftIndexArray[j++] = list1.selectedIndex;
		}
		list1.options[list1.selectedIndex].selected = false;
	}
    if (addArray.length>0)
    {
    	for(var p=0;p<addArray.length;p++)
    	{
    		var current_length = table.rows.length;
    		table.insertRow(current_length);
    		var row_added = table.rows[current_length];
    		row_added.className="Row1";
    		row_added.name="varmdl_"+dev_id+","+addArray[p].value.split(",")[0];
    		row_added.id="varmdl_"+dev_id+","+addArray[p].value.split(",")[0];
    			
    		row_added.onclick= function(){selectedLineVar(this,'switch_varmdl_table');return false;};
    		row_added.ondblclick = function(){varDblClick(this)};
    		row_added.insertCell(0);
    		row_added.insertCell(1);
    		row_added.insertCell(2);
    		row_added.className="Row1";	
    		row_added.cells[0].width=500;
    		row_added.cells[1].width=35;
    		row_added.cells[2].width=35;
    		row_added.cells[0].innerHTML = addArray[p].text;
    		//row_added.cells[1].innerHTML = "<input type='text' id='e_"+mdl+"' class='standardTxt' style='width:35px;' maxlenght='4' value='"+eev+"'>";
    		row_added.cells[1].innerHTML = createInputType("e_",addArray[p].value.split(",")[0],addArray[p].value.split(",")[1],"",addArray[p].value.split(",")[4]);
    		//row_added.cells[2].innerHTML = "<input type='text' id='t_"+mdl+"' class='standardTxt' style='width:35px;' maxlenght='4' value='"+tev+"'>";
    		row_added.cells[2].innerHTML = createInputType("t_",addArray[p].value.split(",")[0],addArray[p].value.split(",")[1],"",addArray[p].value.split(",")[4])+
    				createMinMaxHiddenControl(addArray[p].value.split(",")[0],addArray[p].value.split(",")[2],addArray[p].value.split(",")[3]);
    		row_added.cells[0].className= "standardTxt";	
    		row_added.cells[1].className= "standardTxt";	
    		row_added.cells[2].className= "standardTxt";	
    	}
    	for(p = leftIndexArray.length-1;p>=0;p--)
    	{
    		list1.remove(leftIndexArray[p]);
    	}
	}
}

function moveAlrLeft2Right(leftId,rightId,deviceid)
{
	//controllo selezione switch
	if (document.getElementById("switch_id").value==-1)
	{
		alert(document.getElementById("selectswitchalert").value);
		return false;
	}
	var selected_index= document.getElementById(leftId).selectedIndex;
	if (selected_index==-1)
	{
		alert(document.getElementById("nullselected").value);
		return true;
	}
	
	//has many device
	var list1 = document.getElementById(leftId);
	var table = document.getElementById(rightId); 
	var size_table = table.rows.length;
	
	//get device description
	var device = document.getElementById(deviceid);
	var dev_id = device.options[device.selectedIndex].value;
	var deviceDes = device.options[device.selectedIndex].text;
	
	
	//check double
	var addArray = new Array(); 
	var leftIndexArray = new Array();
	var j = 0;
	while (list1.selectedIndex != -1)
	{
		var add = list1.options[list1.selectedIndex];
		var select_id = add.value;
		var tmp_id = -1;
		var tmp_rowid = "";
		var iscontained = false;
		for (i=0;i<size_table;i++)
		{
			tmp_rowid = table.rows[i].id;
			tmp_id = tmp_rowid.split(",")[1];
			if (select_id == tmp_id)
			{
				iscontained = true;
				break;
			}		 
		}
		if(iscontained == false)
		{
			var oOption = document.createElement("OPTION");
			oOption.text=deviceDes+" - "+add.text;
			oOption.value=add.value;;
			addArray[j] = oOption;
			leftIndexArray[j++] = list1.selectedIndex;
		}
		list1.options[list1.selectedIndex].selected = false;
	}
    if (addArray.length>0)
    {
    	for(var p=0;p<addArray.length;p++)
    	{
    		var current_length = table.rows.length;
    		table.insertRow(current_length);
    		var row_added = table.rows[current_length];
    		row_added.className="Row1";
    		row_added.name="varmdl_"+dev_id+","+addArray[p].value;
    		row_added.id="varmdl_"+dev_id+","+addArray[p].value;
    			
    		row_added.onclick= function(){selectedLineAlr(this,'switch_alrmdl_table');return false;};
    		row_added.ondblclick = function(){alrDblClick(this);};
    		row_added.insertCell(0);
    		row_added.className="Row1";	
    		row_added.cells[0].width=500;
    		row_added.cells[0].innerHTML = addArray[p].text;
    		row_added.cells[0].className= "standardTxt";		
    	}
    	for(p = leftIndexArray.length-1;p>=0;p--)
    	{
    		list1.remove(leftIndexArray[p]);
    	}
	}
}
function remVar(tableid)
{
	var table = document.getElementById(tableid);
	
	if ((lineselected_var!=null)&&(lineselected_var!=-1))
	{
		table.deleteRow(lineselected_var);
		lineselected_var=null;
		press_cancel = true;
	}
	else
	{
		alert(document.getElementById("nullselected").value);
	}
}
function varDblClick(row)
{
	var index = row.rowIndex;
	var obj=document.getElementById("switch_varmdl_table"); 
	obj.deleteRow(index);
	lineselected_var=null;
}

function remAlr(tableid)
{
	var table = document.getElementById(tableid);
	
	if ((lineselected_alr!=null)&&(lineselected_alr!=-1))
	{
		table.deleteRow(lineselected_alr);
		lineselected_alr=null;
		press_cancel = true;
	}
	else
	{
		alert(document.getElementById("nullselected").value);
	}
}
function alrDblClick(row)
{
	var index = row.rowIndex;
	var obj=document.getElementById("switch_alrmdl_table"); 
	obj.deleteRow(index);
	lineselected_alr=null;
}
function selectedLineVar(line,table)
{	
	var idline = line.rowIndex;
	
	if (lineselected_var==idline)
	{
		if ((clickState_var==null)||(clickState_var==0))
		{
			clickState_var=1;	
			document.getElementById(table).rows[idline].className="selectedRow";
		}
		else
		{
		clickState_var=0;
		document.getElementById(table).rows[idline].className="Row1";
		}
	}
	else
	{
		clickState_var=1;
		document.getElementById(table).rows[idline].className="selectedRow";
		if (lineselected_var!=null)
		{
			document.getElementById(table).rows[lineselected_var].className="Row1";
		}
		
	}
	lineselected_var= idline;
}

function selectedLineAlr(line,table)
{	
	var idline = line.rowIndex;
	
	if (lineselected_alr==idline)
	{
		if ((clickState_alr==null)||(clickState_alr==0))
		{
			clickState_alr=1;	
			document.getElementById(table).rows[idline].className="selectedRow";
		}
		else
		{
		clickState_alr=0;
		document.getElementById(table).rows[idline].className="Row1";
		}
	}
	else
	{
		clickState_alr=1;
		document.getElementById(table).rows[idline].className="selectedRow";
		if (lineselected_alr!=null)
		{
			document.getElementById(table).rows[lineselected_alr].className="Row1";
		}
		
	}
	lineselected_alr= idline;
}
function minMaxCheck(tableid)
{
	var table = document.getElementById(tableid);
	var tmp_id = "";
	var tmp_var = "";
	var isok = true;
	for (i=0;i<table.rows.length;i++)
	{
		tmp_id = table.rows[i].id.substring(7,table.rows[i].id.length);
		tmp_var = tmp_id.split(",")[1];
		isok = controlMinMax("e_",tmp_var);
		if(!isok)
			break;
		isok = controlMinMax("t_",tmp_var);
		if(!isok)
			break;
	}
	return isok;
}
function prePostVar(tableid)
{
	var isok = true;
	var table = document.getElementById(tableid);
	var tmp_id = "";
	var tmp_var = "";
	var eev = -1;
	var tev = -1;
	var var_to_post = "";
	
	for (i=0;i<table.rows.length;i++)
	{
		tmp_id = table.rows[i].id.substring(7,table.rows[i].id.length);
		tmp_var = tmp_id.split(",")[1];
		eev = document.getElementById("e_"+tmp_var).value;
		if (eev=="")
		{
			if (isok==true) document.getElementById("e_"+tmp_var).focus();
			table.rows[i].cells[1].className='statoAllarme1_b';
			isok=false;
		}
		else
			table.rows[i].cells[1].className='Row1';
		
		tev = document.getElementById("t_"+tmp_var).value;
		if (tev=="")
		{
			if (isok==true) document.getElementById("t_"+tmp_var).focus();
			table.rows[i].cells[2].className='statoAllarme1_b';
			isok=false;
		}
		else
			table.rows[i].cells[2].className='Row1';
		var_to_post = var_to_post + tmp_id + "," + eev + "," + tev + ";"
	}
	if (var_to_post!="")
		var_to_post = var_to_post.substring(0,var_to_post.length-1) 
	if (isok)
	{
		return var_to_post;
	}
	else
	{
		alert(document.getElementById("missingvalues").value);
		return "missing_value";
	}
}

function prePostAlr(tableid)
{
	var table = document.getElementById(tableid);
	var tmp_id = "";
	var alr_to_post = "";
	
	for (i=0;i<table.rows.length;i++)
	{
		tmp_id = table.rows[i].id.substring(7,table.rows[i].id.length);
		alr_to_post = alr_to_post + tmp_id +";"
	}
	if (alr_to_post!="")
		alr_to_post = alr_to_post.substring(0,alr_to_post.length-1) 
	
	return alr_to_post;
}

function switch_mdl_save()
{
	var save = true;
	//controllo selezione switch
	if (document.getElementById("switch_id").value==-1)
	{
		alert(document.getElementById("selectswitchalert").value);
		return false;
	}
	
	if(!minMaxCheck("switch_varmdl_table"))
		return false;
	var ofrm = document.getElementById("frm_switch_mdl");
	var var_to_post = prePostVar("switch_varmdl_table");
	var alr_to_post = prePostAlr("switch_alrmdl_table");
		
	if (var_to_post=="missing_value")
	{
		return false;
	}
	
	if (press_cancel == true)
	{
		if (!confirm(document.getElementById("confirmsave").value))
		{
			save =false;
		}
	}
	
	if (save)
	{
		document.getElementById("var_to_post").value = var_to_post;
		document.getElementById("alr_to_post").value = alr_to_post;
		if(ofrm != null)
				MTstartServerComm();
		ofrm.submit();
	}
}

function clear_table(table_id)
{
	var table = document.getElementById(table_id);
	if (table.rows.length>0)
	{
		for (i=table.rows.length-1;i>-1;i--)
		{
			table.deleteRow(i);
		}
	}
}

function set_default_Switch_config()
{
	var switch_id = document.getElementById("switch_id").value;
	if (switch_id!=-1)
	{
		if (confirm(document.getElementById("commitdefault").value))
		{
			CommSend("servlet/ajrefresh","POST","cmd=set_default&switch_id="+switch_id+"","set_default",true);
		}
	}
}

function Callback_set_default()
{
	var obj_switch = xmlResponse.getElementsByTagName("switch");
	var id_sw = obj_switch[0].getAttribute("id");
	document.getElementById("switch_id").value = id_sw; 
	//richiamare l'onchange x aggiornare i dati in pagina
	//esco dalla callback prima di chiamare l'altra x non entrare in ricorsione
	alert(document.getElementById("alert_restart").value);
	setTimeout(onChangeSwitch(),10);
}