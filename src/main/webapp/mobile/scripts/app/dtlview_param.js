var idfortooltip=-1;
var tooltiptimeoutid;
var html_conf;
var tempflag=false;
//ONLOAD FUNCTIONS
function onload_buttons()
{
	if (document.getElementById("js_control")==null)
	{	
		if (document.getElementById("cmd").value=="export"){
			var filename=document.getElementById("export_cmd").value;
			var export_type=document.getElementById("export_type").value;
			if((filename!="null") && (filename!="")){
				var msg = document.getElementById("save_confirm").value;
				if("LOCAL"==export_type){
					if(filename == "ERROR"){
						msg = document.getElementById("save_error").value;
						alert(msg);
					}else{
						var inner = document.getElementById("inner").value;
						if(inner != "")
						{
							alert(document.getElementById("exportok").value);
						}
						else
						{
							alert(msg + filename);
						}
					}
				}else{
					var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
					window.open(sUrl);
				}
			}
//			alert(document.getElementById("exported").value);
		}else if ((fdLocal == false)&&(document.getElementById("cmd").value=="load_from_file")){
			var v=document.getElementById("params_values").value;
			if(v!=null || v!="null"){
				tempflag=true;
				load_from_file(v,false);
			}else{
				alert(document.getElementById("notexported").value);
			}
		}else if (document.getElementById("cmd").value=="copy_all")
			alert(document.getElementById("s_copyall").value);
		else if (document.getElementById("cmd").value=="export_failed")
			alert(document.getElementById("notexported").value);
		
		canCommit = true;
		
		//se si toglie il codice sottostante, ogni funzione ha un controllo che blocca le funzioni dei tasti 2,3,4
		var offline = document.getElementById("offline");
		var hasw_v = document.getElementById("hasw_v").value;//add by Kevin
		//if (offline!=null && offline.value!="true" && permission==2)
		if (offline!=null && offline.value!="true" && hasw_v == "true")
		{
			enableAction(2);
			enableAction(3);
			enableAction(4);
			enableAction(5);
			enableAction(6);
		}
		
		//Sempre abilitato
		enableAction(1);
	}
	else 
	{
		disableAction(1);
	}
}

function onload_group()
{
	if ((tempflag==false) && (document.getElementById("js_control")==null))
	{
		//set altezza corpo tabella in base al numero di gruppi di parametri
		set_table_height();
		
		var current_grp = document.getElementById('current_grp').value;
		var grps = document.getElementById("idsgroups").value.split(";");
		var sel = false;
		if (current_grp!="null"&&current_grp!="")
		{
			for (i=0;i<grps.length;i++)
			{
				if (grps[i]==current_grp)
				{
					load_params(current_grp);
					sel = true;		
				}
			}
		}
		else
		{ 
			if(grps.length > 1 ){
				load_params(grps[1]);
			}else{
				load_params(grps[0]);
			}
		} 
		if (!sel)
		{ 
			if(grps.length > 1 ){
				load_params(grps[1]);
			}else{
				load_params(grps[0]);
			}
		}
	}
}

//CHANGE PARAMETERS GROUPS
function load_params(id_vargroup)
{
		CommSend("servlet/ajrefresh","POST","cmd=load_params&group_id="+id_vargroup+"&iddev="+document.getElementById('iddev').value,"load_param",true);
}

function checkModUser()
{	
	if(getModUser())
	{
		return (confirm(document.getElementById("changegrpmsg").value));
	}
	return true;
}


function Callback_load_param(xml)
{ 
	var xmlResponse=xml;
	//dichiarazioni
	document.getElementById("div_head").style.display="block";
	document.getElementById("div_head2").style.display="none";
	var html = "<table id='body_table' class='table' cellpadding=0 cellspacing='1' width='100%'>";
	var	mm = "";
	var	grp_id = String(xmlResponse.getElementsByTagName("grp")[0].getAttribute("id"));
	var	vars = xmlResponse.getElementsByTagName("var");
	var	variable = null; 
 	var	id = null;
	var cur_val = null;
	var type = null;
	var decimals = null;
	var um = null;
	var code= null;
	var description= null;
	var min = null;
	var max = null;
	var loaded = null;
	var disab = null;
	var input = null;

	//creazione tabella parametri
	var myAr = new Array(vars.length);
	for (var i = 0; i < vars.length; i++)
	{
		variable = vars[i];
	  	id = String(variable.getAttribute("id"));
	    cur_val = variable.childNodes[0].childNodes.length ? variable.childNodes[0].childNodes[0].nodeValue : "";

	    // MODIFICA PER SET VARIABILI TRAMITE COMBO
	    var combo = variable.getElementsByTagName("option");
	    // se per la variabile esiste una combo di scelta valori, allora creo in modo diverso il campo
	    if(combo.length > 0)
	    {
	    	var c = null;
	    	var c_value = "";
	    	var c_desc = "";
	    	for(var k = 0; k < variable.childNodes.length; k++) {
	    		var name = variable.childNodes[k].nodeName; 
	    		if( name != "#text" ) {
	    			var value = variable.childNodes[k].childNodes.length ? variable.childNodes[k].childNodes[0].nodeValue : "";
	    			eval(name + " = '" + value + "'");
	    		}
	    	}
	    	for (var j = 0; j < combo.length; j++)
	    	{
	    		c = combo[j];
	    		c_value = c.childNodes[0].childNodes.length ? c.childNodes[0].childNodes[0].nodeValue : "";
	    		c_desc = c.childNodes[1].childNodes.length ? c.childNodes[1].childNodes[0].nodeValue : "";
	    		if ( parseFloat(c_value) == parseFloat(cur_val) )
	    		{
	    			cur_val = c_desc;
	    			break;
	    		}
	    	}
			input = createCombo(id,combo,loaded,disab,i);
	    }
	    // FINE MODIFICA SET CON COMBO
	    else
	    {
	    	for(var k = 0; k < variable.childNodes.length; k++) {
	    		var name = variable.childNodes[k].nodeName; 
	    		if( name != "#text" ) {
	    			var value = variable.childNodes[k].childNodes.length ? variable.childNodes[k].childNodes[0].nodeValue : "";
	    			eval(name + " = '" + value + "'");
	    		}
	    	}
			input = createInputType(id,type,loaded,disab,decimals);
	    }
	 	
	    var zebraRow = "";
	    (i%2==0)? zebraRow = "Row1" : "Row2"; 
		myAr[i] = "<tr style='height:30px;' class='"+zebraRow +"' id='rowcur_"+id+"' onmouseover='setlongdesc("+id+","+i+");' onclick='setlongdesc("+id+","+i+");' onmouseout='//document.getElementById(\"longdescdiv\").innerHTML=\"\";'>";
		myAr[i] += "<td id='cur_"+id+"' class='standardTxt' width='12%' align='center'>" + cur_val + "</td>";
		myAr[i] += "<td width='18%' align='center' style='cellpadding:0px'>" + input + "</td>";
		myAr[i] += "<td class='standardTxt' width='9%' align='center'>" + um + "</td>";
		myAr[i] += "<td class='standardTxt' align='center' width='12%'>" + code + "</td>";
		myAr[i] += "<td class='standardTxt' width='*' id='desccur_"+id+"'>" + description + "</td>";
		myAr[i] += "<td style='display: none;' id='longdescr_"+id+"' >" + longdescr + "</td></tr>";
		
		mm = mm + createMinMaxHiddenControl(id,min,max);
	}
	html += myAr.join("") + "</table>";
	var html_combo = "<table width='100%' class='tableParam' cellspacing='1'>"+createComboPage(xmlResponse)+"</table>";
	document.getElementById('div_params').innerHTML=html;
	document.getElementById('div_conf').innerHTML = html_combo;	
	document.getElementById('div_mm').innerHTML = mm;	
	document.getElementById('current_grp').value=grp_id;

	if( grp_id == -1 )
		linka();
}


function createComboPage(xmlResponse)
{
	var disabled = document.getElementById("permission").value != "0" ? "disabled" : "";

	var html_conf="";
	if(xmlResponse.getElementsByTagName("combos").length==0)
		return html_conf;
	var combos = xmlResponse.getElementsByTagName("combos")[0].getAttribute("id");
	var types = combos.split(";");
	for(var j=0;j<types.length;j++)
	{
		var type = types[j];
		if(xmlResponse.getElementsByTagName("c"+type)!=null)
			var tags=xmlResponse.getElementsByTagName("c"+type);
		else
			var tags="";	
	
		if(tags.length>0)
		{
	
			var vedi = xmlResponse.getElementsByTagName("view")[0].getAttribute("id");
			
	
			if(tags[0].childNodes[1].nodeValue!="")
				html_conf=html_conf+"<tr style='height:30px;' class="+ (j%2==0?"'Row1'":"Row2") +" onmouseover='setDes()'; onmouseout='document.getElementById(\"longdescdiv\").innerHTML=\"\";'><td width='25%' align='center' class='standardTxt'>"+tags[0].childNodes[1].nodeValue+"</td>";
			else
				html_conf=html_conf+"<tr style='height:30px;' class="+ (j%2==0?"'Row1'":"Row2") +" onmouseover='setDes()'; onmouseout='document.getElementById(\"longdescdiv\").innerHTML=\"\";'><td width='25%' align='center' class='standardTxt'>***</td>";
				
			html_conf=html_conf+"<td width='162px' align='center' class='standardTxt'><select "+vedi+" name='"+type+"_combo' style='width:160px' " + disabled + "><option value=''></option>";
	
			for(var i=0; i<tags.length; i++)
				html_conf=html_conf+"<option value='"+type+"-"+tags[i].getAttribute("id")+"' class='standardTxt'>"+tags[i].childNodes[0].nodeValue+"</option>";
		
			html_conf=html_conf+"</select></td><td class='standardTxt' width='*'>"+tags[0].childNodes[2].nodeValue+"</td>";
			
			html_conf=html_conf+"<td style='display: none;' id='confDes'>"+tags[0].childNodes[3].nodeValue+"</td></tr>";
	
		}
	}
	return html_conf;		
}

function setDes()
{
	document.getElementById("longdescdiv").innerHTML=document.getElementById("confDes").innerHTML;
}

function getOnblur(virtkey,idvar)
{
	if(virtkey == "on")
	{
		return " onblur='checkOnlyAnalogOnBlur(this);' ";
	}
	else
	{
		return " onblur='controlMinMax("+idvar+");checkOnlyAnalogOnBlur(this);' ";
	}
}
function createInputType(idvar,type,loaded,disab,decimals)
{
	var html = "";
	var disabled = "";
	if (disab == "true")
	{
		disabled = "disabled";
	}
	
	var virtualkeyboard = "";
	var virtkey = document.getElementById("virtkeyboard").value;
	
	//se input non disabilitato controllo stato tastiera virtuale:
	if (disabled == "")
	{
		if (virtkey == "on")
		{
			virtualkeyboard = "class=\"keyboardInput\"";
		}
	}
	
	if (type == 1) //digitale
    {
    	html = "<input type='text' "+virtualkeyboard+" "+disabled+" value='"+loaded+"' size='4' class='standardInpTxt' style='width:95%' name='dtlst_" + idvar + "' id='dtlst_" + idvar + "' onkeydown='checkOnlyDigit(this,event);' onblur='checkOnlyDigitOnBlur(this);'/>";
    }
    else if (decimals > 0) //analogica  o intera con dei decimals settati
    {
    	html = "<input type='text' "+virtualkeyboard+" "+disabled+" value='"+loaded+"' size='4' class='standardInpTxt' style='width:95%' "+getOnblur(virtkey,idvar)+" name='dtlst_" + idvar + "' id='dtlst_" + idvar +"' onkeydown='checkOnlyAnalog(this,event);'/>";
    }
    else //intera o analogica trattata come intera
    {
    	html = "<input type='text' "+virtualkeyboard+" "+disabled+" value='"+loaded+"' size='4' class='standardInpTxt' style='width:95%' "+getOnblur(virtkey,idvar)+" name='dtlst_" + idvar + "' id='dtlst_" + idvar +"' onkeydown='checkOnlyNumber(this,event);'/>";
    }
	
    return html;
}

// ex - CUSTOM CREA
function createCombo(idvar,combo,loaded,disab,row)
{
	var html = "";
	var disabled = "";
	
	if (disab=='true')
	{
		disabled = "disabled";
	}
	
	html+="<div id='cmbcontainer_" + idvar + "' style='width: 250px; position: relative;'>";
	//x IE-7:
	html+="<select name='dtlst_" + idvar + "' id='dtlst_" + idvar + "' "+disabled+" style='width: 100%;' onmousedown='cmbAdapt("+idvar+","+row+");' onchange='cmbNormalize("+idvar+");' onblur='cmbNormalize("+idvar+");'>";
	//x IE-6+7 (non approvato):
	//html+="<select name='dtlst_" + idvar + "' id='dtlst_" + idvar + "' "+disabled+" class='standardTxt' style='width: 100%;' onmouseover='ctrl_cmb("+idvar+");cmbAdapt("+idvar+","+row+");this.focus();' onchange='cmbNormalize("+idvar+");' onblur='cmbNormalize("+idvar+");'>";
	html+="<option value=''></option>";
	
	for (j=0;j<combo.length;j++)
	{
		c = combo[j];
		c_value = c.childNodes[0].nodeValue;
		c_desc = c.childNodes[1].nodeValue;
		html+="<option value='"+c_value+"' "+(loaded==c_value?'selected':'')+">"+c_desc+"</option>";
    }
    
	html+="</select>";
	html+="</div>";
    
    return html;
}
// FINE CUSTOM CREA

function cmbAdapt(idv,row)
{
	//personalizza larghezza per ogni select:
	var cmboptions = document.getElementById('dtlst_'+idv);
	var maxlength = 0;
	var optlength = 0;

	//valori colonna precedente:
	var tblRW = document.getElementById('body_table');
	var value = tblRW.rows[row].cells[0].innerHTML;
	
	var tdLength = value.length;

	//dato che options[0].text='', ctrl da 1
	for (cmbo = 1; cmbo < cmboptions.length; cmbo++)
	{
		optlength = cmboptions.options[cmbo].text.length;
		
		if (optlength > maxlength)
		{
			maxlength = optlength;
		}
	}
	
	// 21 = max chars visibili nelle options (pag Parametri) su embedded
	var newWidth = (Math.ceil(maxlength/21))*100;
	
	//adatto larghezza cmbbox secondo valore colonna precedente:
	// 21 = max chars xogni riga td precedente
	// se 4 righe:
	if (tdLength > 60)
		newWidth = newWidth * 0.4;
	// se 3 righe:
	else if (tdLength > 45)
		newWidth = newWidth * 0.45;
	// se 2 righe:
	else if (tdLength > 36)
		newWidth = newWidth * 0.55;
	else if (tdLength > 30)
		newWidth = newWidth * 0.65;
	else if (tdLength > 16)
		newWidth = newWidth * 0.75;
	// se 1 riga:
	else newWidth = newWidth * 0.87;
	
	//ridimensiono solo cmbbox pi� larghe dello spazio disponibile
	if (newWidth > 100)
	{	
		document.getElementById('cmbcontainer_'+idv).style.width = ""+newWidth+"%";
	}
}

function cmbNormalize(idv)
{
	document.getElementById('cmbcontainer_'+idv).style.width = "100%";
}

function ctrl_cmb(idv)
{
	var combos = document.getElementsByTagName('SELECT');
	var j = 0;
	var idvar = -1;
	var id = "";
	
	for (j = 0; j < combos.length; j++)
	{
		id = combos[j].getAttribute('id');
		if ((id != "dtlst_"+idv) && (id.substring(0,6)=="dtlst_"))
		{
			idvar = id.split("_")[1];
			document.getElementById('cmbcontainer_'+idvar).style.width = "100%";
		}
	}
}

function createMinMaxHiddenControl(idvar,min,max)
{
	var html = "<input type='hidden' id='min_"+idvar+"' value='"+min+"'/>";
	html = html + "<input type='hidden' id='max_"+idvar+"' value='"+max+"'/>\n";
	return html;	
}

function controlMinMax(idvar)
{
	var min_permitted = document.getElementById('min_'+idvar).value;
	var max_permitted = document.getElementById('max_'+idvar).value;
	var set = document.getElementById('dtlst_'+idvar).value;
	if (set!="")
	{
		if (min_permitted!=null)
		{
			if (Number(set)<Number(min_permitted))
			{
				alert(document.getElementById("s_minval").value +min_permitted);
				document.getElementById('dtlst_'+idvar).value="";
				document.getElementById('dtlst_'+idvar).focus();
				return false;
			}
		}
		if (max_permitted!=null)
		{
			if (Number(set)>Number(max_permitted))
			{
				alert(document.getElementById("s_maxval").value +max_permitted);
				document.getElementById('dtlst_'+idvar).value="";
				document.getElementById('dtlst_'+idvar).focus();
				return false;
			}
		}
		return true;
	}
}

function set_table_height()
{
	// altezza tabella 
	var scr_h = document.getElementById('scr_h').value;
	var offH = document.getElementById("div_params").style.offsetHeight;
	var rows_header = document.getElementById('row_header').value;
	// Alessandro: set the new height by considering:
	// 1) a static estimated offset that should be represent the top and the bottom menus
	// 2) the total estimated height used by all the category buttons rows (rows_header)
	offH = scr_h - 360 - (rows_header*31);
	document.getElementById("div_params").style.height = offH+'px';
}



// ###########################################   FUNZIONALIT? BOTTONI    #################################################
// BOTTONE 1: go_to_device() funzione architetturale in menuaction.js

// BOTTONE 2: SET VARIABILI SUL CAMPO
function dtlviewSetVars()
{
	//2010-01-08, add by Kevin
	//if virtual keyboard is on. will check the max min value
	//2015-04-02
	//modified to fix values like this 2...3 into 2.3 and clear all NaN values
	if(numberCheck() == false)
	{
		return;
	}
	//end
	if(document.getElementById("offline") != null && 
	   document.getElementById("offline").value == "false")
	{
		document.getElementById("cmd").value="set";
		MTstartServerComm();
		document.getElementById("frmdtlset").submit();
	}
}
//by Kevin
function numberCheck()
{
	var virtkey = document.getElementById("virtkeyboard").value;
	var inputs = document.frmdtlset.getElementsByTagName("INPUT");
	for(var i=0;i<inputs.length;i++)
	{
		// try to fix wrong numbers escaped from filters
		if( inputs[i].type == "text" && inputs[i].value != "" ) {		
			var value = inputs[i].value;
			value = value.replace(",", ".");	// just in case convert , into .
			value = value.replace(/\.+/, ".");	// convert ... into .
			if( isNaN(parseFloat(value)) )
				inputs[i].value = "";
			else
				inputs[i].value = parseFloat(value);
		}
		if(virtkey == "on" && inputs[i].type == "text" && inputs[i].id.match("^dtlst_") && inputs[i].value != "")
		{
			var value = inputs[i].value;
			var id = inputs[i].id.split("_")[1];
			var max = document.getElementById("max_"+id).value;
			var min = document.getElementById("min_"+id).value;
			if(maxminvaluecheck(value,min,max,id) == false)
			{
				return false;
			}
		}
	}
	return true;
}
function maxminvaluecheck(set,min_permitted,max_permitted,idvar)
{
	if (set != "")
	{
		if (min_permitted != null && min_permitted != "")
		{
			if (Number(set) < Number(min_permitted))
			{
				alert(document.getElementById("s_minval").value + min_permitted);
				document.getElementById('dtlst_'+idvar).value = "";
				//add this because maybe the input will be hidden
				window.onerror = killErrors;
				try
				{
					document.getElementById('dtlst_'+idvar).focus();
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
		if (max_permitted != null && max_permitted != "")
		{
			if (Number(set) > Number(max_permitted))
			{
				alert(document.getElementById("s_maxval").value + max_permitted);
				document.getElementById('dtlst_'+idvar).value = "";
				//add this because maybe the input will be hidden
				window.onerror = killErrors;
				try
				{
					document.getElementById('dtlst_'+idvar).focus();
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
		return true;
	}
	return true;
}
//end
// BOTTONE 3: COPIA DA VALORE CORRENTE ALLE TEXTBOX e/o COMBOBOX
function copy_values()
{
	var jsession = document.getElementById("jsession").value;
	var offline = document.getElementById("offline").value;
	if (offline == "true")
	{
		alert(document.getElementById("offline1").value);
	}
	else
	{
		var idvar = null;
		var cur = null;
		var row = null;
		var elem = null;
		var subelem = null;
		var e = 1;
		
		var table = document.getElementById("body_table");
		
		for (i = 0; i < table.rows.length; i++)
		{
			row = table.rows[i];
			idvar = row.cells[0].id.split("_")[1];
			cur = row.cells[0].innerHTML;
			elem = document.getElementById('dtlst_'+idvar);
			
			//procedo solo se il param ha un valore valido:
			if (cur != "***")
			{
				//caso input-box:
				if (elem.nodeName == "INPUT")
				{
					// fix valori con virgola per le migliaia:
					elem.value = cur.replace(',','');
					//controllo range validit� valore prima di copiarlo:
					//if (!controlMinMax(idvar))	elem.value = "";
				}
				else
				//caso combo-box:
				if (elem.nodeName == "SELECT")
				{
					// x indice e=0 --> valore='', quindi parto dalla pos. e=1
					for (e = 1; e < elem.length; e++)
					{
						if (elem.options[e].text == cur)
						{
							elem.options[e].selected = true;
							break;
						}
					}
				}
			}
		}
	}
}

// BOTTONE 4: EXPORT VALORI SU FILE
function exp_device_setting()
{
	//2010-01-08, add by Kevin
	//if virtual keyboard is on. will check the max min value
	if(maxmincheck() == false){
		return;
	}
	var jsession = document.getElementById("jsession").value;
	var offline = document.getElementById("offline").value;
	if (offline=="true"){
		alert(document.getElementById("offline1").value);
	}else{
//		fdSaveFile('frmexport_fullpath','',savefile);
		document.getElementById("uploadwin").style.display="block";
		setPopupWinCenter();
		document.getElementById("win_type").innerHTML="&nbsp;&nbsp;"+document.getElementById("saveon").value;
		document.getElementById("localasDIV").innerHTML=document.getElementById("savepath").value;
		if(document.getElementById("bLocal").value == "true")
		{
			document.getElementById("otherasDIV").innerHTML = document.getElementById("otheras").value;
		}
		else
		{
			document.getElementById("otherasDIV").innerHTML = document.getElementById("download").value;
		}
		document.getElementById("local_span_load").style.display="none";
		document.getElementById("local_span_save").style.display="inline";
		document.getElementById("other_span_load").style.display="none";
		document.getElementById("other_span_save").style.display="inline";
		if(document.getElementById("vkeytype")!=null){
			buildKeyboardInputs();
		}
		//default use local path
		document.getElementById("win_radio_l").click();
	}
}
function savefile(local, path, filename){
	document.getElementById("exp_file").value = filename;
	document.getElementById("cmd").value="export";
	document.getElementById("file_path").value=path;
	var ofrm = document.getElementById("frmdtlset");
	if(ofrm != null)
		MTstartServerComm();
	ofrm.submit();
}
function Callback_exportFile(local){
	MTstopServerComm();
	if (xmlResponse!=null && xmlResponse.text!="")
	{
		var filename = xmlResponse.text;
		var msg = document.getElementById("save_confirm").value;
		
		if(local == true)		{
			alert(msg + filename);
		}else{
			var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
			window.open(sUrl);
		}
	}
}
// BOTTONE 5: IMPORT SET DISPOSITIVO
function imp_device_setting(){
//	if(fdLocal==true){
//		fdLoadFile('','',uploadfile);
//	}else{
//		var win=document.getElementById("uploadwin");
//		win.style.display="block";
//	}
	document.getElementById("uploadwin").style.display="block";
	setPopupWinCenter();
	document.getElementById("win_type").innerHTML="&nbsp;&nbsp;"+document.getElementById("loadfrom").value;
	document.getElementById("localasDIV").innerHTML=document.getElementById("localas").value;
	document.getElementById("otherasDIV").innerHTML = document.getElementById("otheras").value;
	document.getElementById("local_span_load").style.display="inline";
	document.getElementById("local_span_save").style.display="none";
	document.getElementById("other_span_load").style.display="inline";
	document.getElementById("other_span_save").style.display="none";
	//default use local path
	document.getElementById("win_radio_l").click();
	new AjaxRequest("servlet/ajrefresh", "POST","cmd=getDeviceTemplateList", Callback_getDeviceTemplateList, true);
}
function Callback_getDeviceTemplateList(xml){
	var sel=xml.getElementsByTagName("sel")[0];
	var combo=document.getElementById("fileCombo");
	var papa=document.getElementById("local_span_load");
	if(combo!=null){
		papa.removeChild(combo);
	}
	if(sel.childNodes[0].nodeValue!="null"){
		papa.innerHTML=sel.childNodes[0].nodeValue;
	}
//	alert(sel.childNodes[0].nodeValue);
}

function load_from_file(filename,isLocal){
	new AjaxRequest("servlet/ajrefresh", "POST","cmd=load_from_file&filename="+filename+"&islocal="+isLocal+"", Callback_load_param, true);
}

//function openDialog(page_url, features ){
//	var sUrl = top.frames["manager"].getDocumentBase() + page_url;
//    retVal = window.showModalDialog( sUrl,"" ,features);
//}
//
//
//function copy_all_setting()
//{
//	//2010-01-08, add by Kevin
//	//if virtual keyboard is on. will check the max min value
//	if(maxmincheck() == false)
//	{
//		return;
//	}
//	//end
//	var jsession = document.getElementById("jsession").value;
//	var offline = document.getElementById("offline").value;
//	if (offline=="true")
//	{
//		alert(document.getElementById("offline1").value);
//	}
//	else
//	{
//		features="dialogWidth=570px;dialogHeight=450px;dialogTop:100;dialogLeft:150;scroll=no;border=thick;help=off;status=off;resizable=off;";
//		var iddev = document.getElementById("iddev").value;
//		openDialog("/app/dtlview/DialogPageCopyAll.jsp;jsessionid="+jsession+"?iddev="+iddev, features);
//		
//		var ids_toset = document.getElementById("ids_toset");
//		ids_toset.value = retVal;
//		
//		if ((retVal!="")&&(retVal!=null))
//		{
//			document.getElementById("cmd").value="copy_all";
//			var ofrm = document.getElementById("frmdtlset");
//			retVal = ""; //pulisco
//			if (ofrm!=null)
//				//MTstartServerComm();
//			ofrm.submit();
//		}
//		else
//		{
//			alert(document.getElementById("s_notcopyall").value);
//		}
//	}
//}

function setlongdesc(id,row)
{
	//document.getElementById("longdescdiv").innerHTML = document.getElementById("body_table").rows[row].cells[5].value;
	//document.getElementById("longdescdiv").innerHTML = document.getElementById("longdescr_"+id).innerHTML;
}

function linka()
{
	document.getElementById('div_params').innerHTML = document.getElementById('div_conf').innerHTML;
	document.getElementById("div_head").style.display = "none";
	document.getElementById("div_head2").style.display = "block";
}

function chooseFileLocation(L_type){
	var flag=false;
	if(L_type=="1"){
		flag=true;
	}
	document.getElementById("local_span_load").disabled=(!flag);
	document.getElementById("local_span_save").disabled=(!flag);
	document.getElementById("other_span_load").disabled=flag;
	document.getElementById("other_span_save").disabled=flag;
}

function submit_template_file(){
	var filename="";
	if(document.getElementById("local_span_save").style.display=="inline"){
		//save
		if(document.getElementById("win_radio_l").checked){
			//save.local
			filename=document.getElementById("fileN").value;
			if(filename==""){
				alert(document.getElementById("enterfilename").value);
				return false;
			}
			savefile(true, "", filename);
		}else{
			//save.other
			if(fdLocal==true){
				//save.other.localhost
				if(document.getElementById("save_file").value==""){
					alert(document.getElementById("choosefile").value);
					return false;
				}
				path=document.getElementById("save_file").value;
				var temp=path.split("/");
				filename=temp[temp.length-1];
				savefile(false,path, filename);
			}else{
				//save.other.remote
				savefile(false,"", "");
			}
		}
	}else{
		//load
		if(document.getElementById("win_radio_l").checked){
			//load.local
			if(document.getElementById("fileCombo")!=null){
				var selected = document.getElementById("fileCombo").options[document.getElementById("fileCombo").selectedIndex];
				filename = selected.innerHTML + "$$" + selected.value;
				//alert(filename);
				load_from_file(filename,true);
			}
		}else{
			//load.other
			if(document.getElementById("upload_file").value==""){
				alert(document.getElementById("choosefile").value);
				return false;
			}
			if(fdLocal==true){
				//load.other.localhost
				load_from_file(document.getElementById("upload_file").value,false);	
			}else{
				//load.other.remote
				document.getElementById("uploadfrm1").submit();
			}
		}
	}
	document.getElementById('uploadwin').style.display='none';
}
function setPopupWinCenter(){
	var win=document.getElementById("uploadwin");
	var left=(document.body.clientWidth-win.clientWidth)/2;
	var top=(document.body.clientHeight-win.clientHeight)/2;
	win.style.left=left;
	win.style.top=top;
}